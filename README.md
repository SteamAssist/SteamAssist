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
    -This is not needed now since it is automated
    
    - node app.js
    - This will start a server on port 3000. You can access it by going to:
http://localhost:3000/?id=STEAM_USER_ID
Replace STEAM_USER_ID with the Steam user ID you want to scrape data for.

    
## Documents:
 You can find them in [https://github.com/SteamAssist/docs](https://github.com/SteamAssist/SteamAssist/tree/main/docs)
## FIRST SPRINT:

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
 

## SECOND SPRINT:

### Tasks:
- **Sprint 1 fix**: Fix some problems based on the feedback.  
- **Requirements Analysis**: Update documents so they are up to date with the code (UI Mockup, use cases...) 
- **Develop Use Cases**:  
  - "AddAccounts" use case
  - "HowLongToBeatLoggedIn" use case
- **Backlog Customization**:  
  - Track hours invested.  
  - Note assignment progress.


 
## THIRD SPRINT:

### Tasks:
- **Sprint 2 fix**: Fix some problems based on the feedback.
- **Develop Use Cases**:
      - "GetGameRecommendations" use case

- **Upgrade GUI and previous Use Cases**. 


## TIPICAL ERRORS

- If when downloading the project this error appears: 'java.lang.module.FindException: Module eus.ehu.template not found' just click on Maven Sync/Reload All Maven Projects to solve the error

- If after the first test the port 3000 doesnt get free execute the following code in the cmd:
    - netstat -ano | findstr :3000 (here you will get the process ID of the process in port 3000)
    - taskkill /F /PID <your process ID>
-This could also happen with the port 3001


