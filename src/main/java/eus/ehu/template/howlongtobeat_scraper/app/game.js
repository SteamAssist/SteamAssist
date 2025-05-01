const puppeteer = require('puppeteer');
const express = require('express');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

function cleanText(text) {
    return text ? text.replace(/\s+/g, ' ').replace(/[\u00A0]/g, ' ').trim() : '';
}

async function scrapeGameDetails(gameId) {
    const isServerEnvironment = process.env.NODE_ENV === 'production';

    const browser = await puppeteer.launch({
        headless: isServerEnvironment ? 'new' : false,
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-features=site-per-process',
            '--disable-dev-shm-usage',
            '--disable-accelerated-2d-canvas',
            '--no-first-run',
            '--no-zygote',
            '--window-size=1,1',
            '--hide-scrollbars',
            '--mute-audio',
            '--disable-web-security',
            '--disable-extensions'
        ],
        protocolTimeout: 120000
    });

    const page = await browser.newPage();
    const timeout = 90000;
    page.setDefaultTimeout(timeout);

    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36');
    await page.setViewport({ width: 1, height: 1, deviceScaleFactor: 1 });

    try {
        await page.goto(`https://howlongtobeat.com/game/${gameId}`, {
            waitUntil: 'networkidle2',
            timeout: timeout
        });

        // Wait for critical elements
        await Promise.race([
            page.waitForSelector('.game_times, h1, .profile_header', { timeout: 15000 }),
            new Promise(resolve => setTimeout(resolve, 5000))
        ]);

        // Extract all data
        const gameData = await page.evaluate((cleanTextFn) => {
            // Make cleanText available in the browser context
            const cleanText = (text) => {
                return text ? text.replace(/\s+/g, ' ').replace(/[\u00A0]/g, ' ').trim() : '';
            };

            // Find element with text content
            const findElementWithText = (text, tagName = 'div') => {
                const elements = Array.from(document.querySelectorAll(tagName));
                return elements.find(el => el.textContent.includes(text));
            };

            const title = cleanText(
                document.querySelector('.GameHeader_profile_header__q_PID.shadow_text')?.textContent ||
                document.querySelector('h1')?.textContent
            );

            // Extract image
            const imageElement = document.querySelector('.game_image img') || document.querySelector('img[alt*="Box Art"]');
            const imageUrl = imageElement ? imageElement.src : '';
            const imageAlt = imageElement ? imageElement.alt : '';

            // Extract times
            const times = {};
            const timeBoxes = Array.from(document.querySelectorAll('.game_times li, .time_100'));
            timeBoxes.forEach(box => {
                const typeElement = box.querySelector('h4') || box.querySelector('.time_label');
                const durationElement = box.querySelector('h5') || box.querySelector('.time');
                const type = cleanText(typeElement ? typeElement.textContent : '');
                const duration = cleanText(durationElement ? durationElement.textContent : '');
                if (type && duration) {
                    times[type.replace(':', '')] = duration;
                }
            });

            // Fallback time extraction
            if (Object.keys(times).length === 0) {
                const timeElements = [
                    { label: 'Main Story', regex: /Main Story[^<]*?(\d+½? Hours)/i },
                    { label: 'Main + Sides', regex: /Main \+ Sides[^<]*?(\d+½? Hours)/i },
                    { label: 'Completionist', regex: /Completionist[^<]*?(\d+½? Hours)/i },
                    { label: 'All Styles', regex: /All Styles[^<]*?(\d+½? Hours)/i }
                ];

                const pageText = document.body.textContent;
                timeElements.forEach(({ label, regex }) => {
                    const match = pageText.match(regex);
                    if (match) {
                        times[label] = cleanText(match[1]);
                    }
                });
            }

            // Inside the page.evaluate() function:

// Extract description by class name
            let description = '';
            const descriptionElement = document.querySelector('.GameSummary_profile_info__HZFQu.GameSummary_large__TIGhL');

            if (descriptionElement) {
                description = cleanText(descriptionElement.textContent)
                    // Remove any trailing "How long is..." text if it appears in the same element
                    .replace(/How long is.*$/i, '')
                    // Remove any "Join Now" or similar CTAs
                    .replace(/Join Now.*$/i, '')
                    // Clean up any remaining extra spaces
                    .trim();
            }

// Fallback if the specific class isn't found
            if (!description) {
                // Use the previous robust method as fallback
                const fallbackElement = Array.from(document.querySelectorAll('p, div')).find(el => {
                    const text = el.textContent;
                    return text.length > 100 &&
                        !text.includes('How long is') &&
                        !text.includes('Join Now');
                });
                if (fallbackElement) {
                    description = cleanText(fallbackElement.textContent);
                }
            }

            // 5. Extract Price by Class Name
            const priceElement = document.querySelector('.StoreButton_price__agxuh');
            const price = cleanText(priceElement?.textContent) || 'Not available';

            // Handle discounted price if present
            const originalPriceElement = document.querySelector('.StoreButton_discounted_price__ABCyi');
            const originalPrice = cleanText(originalPriceElement?.textContent);

            const priceData = {
                current: price,
                original: originalPrice || null,
                onSale: !!originalPrice
            };

            return {
                title: title || 'Unknown Title',
                image: {
                    url: imageUrl,
                    alt: imageAlt
                },
                times,
                description,
                price: price || 'Not available',
            };
        }, cleanText.toString()); // Pass cleanText function to browser context

        return gameData;
    } catch (error) {
        throw new Error(`Scraping failed: ${error.message}`);
    } finally {
        await browser.close();
    }
}

// Define routes
app.get('/game', async (req, res) => {
    const gameId = req.query.id;

    if (!gameId) {
        return res.status(400).json({
            error: 'Missing Game ID parameter. Use /game?id=YOURID'
        });
    }

    try {
        const data = await scrapeGameDetails(gameId);
        res.json({
            success: true,
            gameId,
            data
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            error: error.message,
            suggestion: 'Try again later or verify the game ID exists on howlongtobeat.com'
        });
    }
});

// Error handler
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({
        success: false,
        error: 'Internal Server Error',
        details: process.env.NODE_ENV === 'development' ? err.message : undefined
    });
});

const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});