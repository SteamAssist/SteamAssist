# COPILOT FANBOYS

## Team Members:
- **Cherqui Madarieta, Yunes**  
- **Elortondo Garro, Oier**  
- **Ituarte Arroitajauregi, Gorka**  
- **Zunzunegui Mintegui, Alejandro**  
- **Gonzalez Blazquez, Ivan**  

---

## Requirements:
- **1-You'll need to install Node.js first if you don't have it:**
    - Download and install from nodejs.org
- **2-Install the required packages:**
    - npm install express cors puppeteer
- **3-Run the script**
    - node app.js
    - This will start a server on port 3000. You can access it by going to:
http://localhost:3000/?id=STEAM_USER_ID
Replace STEAM_USER_ID with the Steam user ID you want to scrape data for.

## How to use different services:
- **AddAccounts:
  - Once you are logged in you can add differents SteamIds to you session by clicking "addAccount" button and filling the textfields.
  - The account name textfield is ment to store the identifier you will give to your SteamId.
  - The SteamId textfield is ment to store a valid ID (A real ID that also has to have its games in public view).

- ** VinculateAccount:
  - As one user can have many SteamIds, once you are logged and have different SteamIds associated, you can select one SteamId to be your current one to be vinculated to you session, this will fasten up HowLongToBeat.

- **HowLongToBeat:
  - If you are not logged in, you can access by clicking "howLongToBeat" button. The stage will change and you will be able to see the stats of the completition time of the games you have by introducing your SteamId (76561198851916825 or 76561198848950507 for example).

  - If you are logged in you should select your current SteamId by using VinculateAccount, then when clicking "HowLongToBeat" it will automatically proceed with the currently vinculated SteamId.
    
## FIRST SPLIT:

### Tasks:
- **Wording**: Organize and structure the ideas of the project.  
- **Create GitHub Repository**: Set up and share the repository with all team members.  
- **Requirements Analysis**: Develop documentation and store it in the `Req.Analysis` folder, including:  
  - Use Case  
  - Event Flow  
  - Domain Model  
  - UI Mockup  
- **Develop Use Cases**:  
  - Sign-in & Sign-up functionality  
  - "HowLongToBeat" use case  
- **Backlog Customization**:  
  - Track hours invested.  
  - Note assignment progress.  
- **Create Discord Server**:  
  - Facilitate team communication (presentation, coding, Git management, etc.).
 

## SECOND SPLIT:

### Tasks:
- **Sprint 1 fix**: Fix some problems based on the feedback.  
- **Requirements Analysis**: Update documents so they are up to date with the code (UI Mockup, use cases...) 
- **Develop Use Cases**:  
  - "AddAccounts" use case
  - "HowLongToBeatLoggedIn" use case
- **Backlog Customization**:  
  - Track hours invested.  
  - Note assignment progress.
If when downloading the project this error appears: 'java.lang.module.FindException: Module eus.ehu.template not found' just click on Maven Sync/Reload All Maven Projects to solve the error
