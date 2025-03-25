const puppeteer = require('puppeteer');
const express = require('express');
const cors = require('cors');

// Create Express app
const app = express();

// Enable CORS for all routes
app.use(cors());

// Parse JSON bodies
app.use(express.json());

// Main scraping function
async function scrapeHowLongToBeat(steamUserName) {
    // Determine if we're running in a server environment
    const isServerEnvironment = process.env.NODE_ENV === 'production';

    const browser = await puppeteer.launch({
        headless: isServerEnvironment ? 'new' : false, // Use 'new' headless mode in production, visible browser in development
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-features=site-per-process',
            '--disable-dev-shm-usage',
            '--disable-accelerated-2d-canvas',
            '--no-first-run',
            '--no-zygote',
            '--window-size=1920,1080',
            '--hide-scrollbars',
            '--mute-audio',
            '--disable-web-security',
            '--disable-extensions'
        ]
    });

    const page = await browser.newPage();
    const timeout = 60000;
    page.setDefaultTimeout(timeout);

    // Set a common user agent
    await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36');

    // Set viewport to standard desktop
    await page.setViewport({
        width: 1920,
        height: 1080,
        deviceScaleFactor: 1,
    });

    try {
        // Navigate to the page
        await page.goto(`https://howlongtobeat.com/steam?userName=${steamUserName}`, {
            waitUntil: 'networkidle2'
        });

        // Wait for the table to be fully loaded
        await page.waitForSelector('table');

        // Extra wait for dynamic content
        await new Promise(resolve => setTimeout(resolve, 5000));

        // Extract the table headers
        const headers = await page.evaluate(() => {
            const headerElements = Array.from(document.querySelectorAll('table th, table thead tr td'));
            return headerElements.map(el => el.textContent.trim());
        });

        // Generate headers if none found
        const finalHeaders = headers.length ? headers :
            await page.evaluate(() => {
                const firstRow = document.querySelector('table tbody tr:first-child');
                if (!firstRow) return [];
                const cellCount = firstRow.querySelectorAll('td').length;
                return Array.from({ length: cellCount }, (_, i) => `Column${i+1}`);
            });

        // Extract the table rows
        const rows = await page.evaluate(() => {
            const allRows = Array.from(document.querySelectorAll('table tbody tr'));
            return allRows.map(row => {
                const cells = Array.from(row.querySelectorAll('td'));
                return cells.map(cell => {
                    let content = cell.textContent.trim();

                    // Check for images
                    const img = cell.querySelector('img');
                    if (img && content === '') {
                        const altText = img.getAttribute('alt');
                        if (altText) content = `[Image: ${altText}]`;
                    }

                    // Check for links
                    const link = cell.querySelector('a');
                    if (link && content === '') {
                        content = link.textContent.trim();
                    }

                    return content;
                });
            });
        });

        // Combine headers and rows into a structured dataset
        const tableData = rows.map(row => {
            const rowData = {};
            const numCols = Math.min(finalHeaders.length, row.length);

            for (let i = 0; i < numCols; i++) {
                rowData[finalHeaders[i]] = row[i];
            }

            return rowData;
        });

        return tableData;
    } catch (error) {
        throw error;
    } finally {
        await browser.close();
    }
}

// Define routes

// Root endpoint with health check
app.get('/', async (req, res) => {
    const steamId = req.query.id;

    // Validate the steam ID parameter
    if (!steamId) {
        return res.status(400).json({
            error: 'Missing Steam ID parameter. Use /?id=YOURID'
        });
    }

    try {
        // Run the scraper
        const data = await scrapeHowLongToBeat(steamId);

        // Send the response
        res.json({
            success: true,
            steamId,
            count: data.length,
            data
        });
    } catch (error) {
        // Send error response
        res.status(500).json({
            success: false,
            error: error.message
        });
    }
});

// Add a specific endpoint for scraping
app.get('/scrape', async (req, res) => {
    const steamId = req.query.id;

    if (!steamId) {
        return res.status(400).json({
            error: 'Missing Steam ID parameter. Use /scrape?id=YOURID'
        });
    }

    try {
        const data = await scrapeHowLongToBeat(steamId);
        res.json({
            success: true,
            steamId,
            count: data.length,
            data
        });
    } catch (error) {
        res.status(500).json({
            success: false,
            error: error.message
        });
    }
});

// Error handler
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(500).json({
        success: false,
        error: 'Internal Server Error'
    });
});

// Set the port
const PORT = process.env.PORT || 3000;

// Start the server
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});