package eus.ehu.template.ui;

import eus.ehu.template.businessLogic.BlFacade;
import eus.ehu.template.businessLogic.BlFacadeImplementation;
import eus.ehu.template.domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {
    @FXML
    private  Button hltb_bttn;
    @FXML
    private  Button rg_bttn;
    @FXML
    private Button add_acounts_btn;

    @FXML
    private Button login_bttn;
    @FXML
    private ButtonBar buttonBar;


    @FXML
    private Label li_confirm;
    @FXML
    private Label introtext;


    @FXML
    private Label instagram;
    @FXML
    private Label youtube;
    @FXML
    private Label tiktok;


    @FXML
    private ImageView instagramImage;
    @FXML
    private ImageView youtubeimg;
    @FXML
    private ImageView tiktokimg;

    @FXML
    private ImageView close_btn;



    private boolean SteamIDSet = false;
    private String SteamID;
    private boolean isLoggedIn = false;
    private ArrayList<String> AccountsNames = new ArrayList<>();
    private ArrayList<String> AccountsIDs = new ArrayList<>();
    public static int steamIDIndex = 0;
    public static int steamIDSize = 0;

    public static Stage RegisterStage;
    public RegisterController registerController;
    public static Stage loginStage;
    public LogInController loginController;
    public static Stage howLongToBeatStage;
    public static HLTBController howLongToBeatController;
    public static Stage idStage;
    public SteamIDController steamIDController;
    public static Stage AddAccountStage;
    public SteamAddAccountController steamAddAccountController;
    public static Stage RecommendStage;
    public RecommendedController recommendedController;

    private static User loggedUser=null;
    private Parent RegisterUI;
    private Parent loginUI;
    private Parent idUI;
    private Parent hltbUI;
    private Parent steamAddUI;
    private Parent recommendUI;

    @FXML
    private Label IDLabel;


    @FXML
    public void initialize() {
        //i18n setup
        Locale locale = Locale.forLanguageTag("eu");
        ResourceBundle bundle = ResourceBundle.getBundle("eus.ehu.i18n.messages", locale);

        FXMLLoader loginCon = new FXMLLoader(getClass().getResource("login.fxml"), bundle);
        FXMLLoader idCon = new FXMLLoader(getClass().getResource("steam_id.fxml"), bundle);
        FXMLLoader hltbCon = new FXMLLoader(getClass().getResource("howlongtobeat.fxml"), bundle);
        FXMLLoader registerCon = new FXMLLoader(getClass().getResource("register.fxml"), bundle);
        FXMLLoader steamAddCon = new FXMLLoader(getClass().getResource("steam_addAccount.fxml"), bundle);
        FXMLLoader recommendCon = new FXMLLoader(getClass().getResource("/eus/ehu/template/ui/recommended.fxml"), bundle);
        try {
            loginUI = loginCon.load();
            idUI = idCon.load();
            hltbUI = hltbCon.load();
            RegisterUI = registerCon.load();
            steamAddUI = steamAddCon.load();
            recommendUI = recommendCon.load();

        } catch (IOException e) {
           System.out.println("There was an error at initialize");
        }

        loginController = loginCon.getController();
        steamIDController = idCon.getController();
        howLongToBeatController = hltbCon.getController();
        registerController = registerCon.getController();
        steamAddAccountController = steamAddCon.getController();
        recommendedController = recommendCon.getController();

        loginStage = new Stage();
        howLongToBeatStage = new Stage();
        idStage = new Stage();
        RegisterStage = new Stage();
        AddAccountStage = new Stage();
        RecommendStage= new Stage();

        loginStage.setScene(new Scene(loginUI));
        howLongToBeatStage.setScene(new Scene(hltbUI));
        idStage.setScene(new Scene(idUI));
        RecommendStage.setScene(new Scene(recommendUI));
        RegisterStage.setScene(new Scene(RegisterUI));
        AddAccountStage.setScene(new Scene(steamAddUI));


        loginStage.hide();
        howLongToBeatStage.hide();
        idStage.hide();
        RegisterStage.hide();
        AddAccountStage.hide();
        RecommendStage.hide();

        steamIDController.setMainController(this);
        steamAddAccountController.setMainController(this);
        howLongToBeatController.setMainController(this);
        recommendedController.setMainController(this);

        loginStage.setTitle(bundle.getString("titlelogin"));
        RegisterStage.setTitle(bundle.getString("titlesignup"));
        howLongToBeatStage.setTitle(bundle.getString("titlehltb"));
        idStage.setTitle(bundle.getString("titlesteamid"));
        RecommendStage.setTitle(bundle.getString("titlerecommend"));
        ChangeSteamID();

        updateUI();
        this.setSteamIDSet(false);
    }

    @FXML
    public void recommend_games(ActionEvent event) {

            RecommendStage.show();
            MainUI.stage.hide();
            howLongToBeatStage.hide();
            idStage.hide();
            recommendedController.setMainController(this);

    }


    public void updateLoginStatus(String message, String color) {
        introtext.setVisible(false);
        li_confirm.setText(message);
        li_confirm.setStyle("-fx-text-fill: " + color + ";");
    }

    @FXML
    void handleLoginButton(ActionEvent event) {
        if (isLoggedIn) {
            // Handle logout
            setLoggedIn(false);
            li_confirm.setText("");
            introtext.setVisible(true);
        } else {
            showLogin(event);
        }
    }

    private void showLogin(ActionEvent ignoredEvent) {
        loginStage.show();
        MainUI.stage.hide();
        howLongToBeatStage.hide();
        idStage.hide();
        loginController.setMainController(this);
        registerController.setMainController(this);
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        updateUI();
    }

    private void updateUI() {
        add_acounts_btn.setVisible(isLoggedIn);
        login_bttn.setText(isLoggedIn ? "Log out" : "Log in/Sign up");
        if (isLoggedIn) {
            login_bttn.setStyle("-fx-background-color: #DC2626; -fx-text-fill: #FFFFFF;");
            buttonBar.getButtons().removeAll(add_acounts_btn, login_bttn);
            buttonBar.getButtons().addAll(add_acounts_btn, login_bttn);

        } else {
            login_bttn.setStyle("-fx-background-color: #2e7d32   ; -fx-text-fill: #FFFFFF;");
            buttonBar.getButtons().removeAll(add_acounts_btn, login_bttn);
            buttonBar.getButtons().addAll(login_bttn, add_acounts_btn);
        }
    }

    public String getSteamID() {
        return SteamID;
    }

    public void setSteamID(String steamID) {
        this.SteamID = steamID;
    }

    @FXML
    public void howlongtobeat(ActionEvent ignoredEvent) {
        //Reset ID if you are not logged in
        if(!isLoggedIn) {
            SteamIDController.setIdentifier(null);
        }
        // Check if the user is logged in before showing the HLTB stage, the id is in the SteamIDController
        if(SteamID != null) {
            howLongToBeatController.cargaLaVaina();
            howLongToBeatStage.show();
            howLongToBeatController.setMainController(this);
            //howLongToBeatController.meteJogos();
            idStage.hide();
            MainUI.stage.hide();
            loginStage.hide();
        } else {
            idStage.show();
            howLongToBeatStage.hide();
            MainUI.stage.hide();
            loginStage.hide();
            li_confirm.setText("Please log in to access this feature.");
        }
    }



    @FXML
    void addAccounts(ActionEvent event) {
        System.out.print(AccountsNames.toString());
        System.out.print(AccountsIDs.toString());
        AddAccountStage.show();
        AddAccountStage.setTitle("Add Steam Account");
        steamAddAccountController.setMainController(this);
        MainUI.stage.hide();
    }

    public static void setLoggedUser(User user) {
        loggedUser = user;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    @FXML
    void Next(ActionEvent event) {
        if(steamIDIndex + 1 < AccountsNames.size()) {

            steamIDIndex++;
            ChangeSteamID();
        }
    }

    @FXML
    void Previous(ActionEvent event) {
        if(steamIDIndex > 0) {
            steamIDIndex--;
            ChangeSteamID();
        }
    }

    public void ChangeSteamID() {
        BlFacade bl = BlFacadeImplementation.getInstance();
        User user = getLoggedUser();
        if (user == null) {
            IDLabel.setText("No Steam IDs found.");
            return;
        }
        AccountsNames = bl.getAccountname(user.getUserName());
        ArrayList<String> SteamIDs = bl.getSteamID(user.getUserName()); // Get the IDs from the database
        if (AccountsNames != null && !AccountsNames.isEmpty()) {
            if (steamIDIndex < AccountsNames.size() && steamIDIndex >= 0) {
                String steamID = SteamIDs.get(steamIDIndex);
                String accountName = AccountsNames.get(steamIDIndex);
                IDLabel.setText(accountName);
                SteamID = steamID;
            } else {
                while(steamIDIndex >= AccountsNames.size() || steamIDIndex < 0) {
                    if(steamIDIndex >= AccountsNames.size()) {
                        steamIDIndex--;
                    } else {
                        steamIDIndex++;
                    }
                }
                ChangeSteamID();
            }
        } else {
            IDLabel.setText("No Steam IDs found.");
        }
    }
    public void setSteamIDSet(boolean steamIDSet) {
        SteamIDSet = steamIDSet;
    }
    public boolean isSteamIDSet() {return SteamIDSet;}

    //All visual below here

    @FXML
    public void instagramclick(MouseEvent ignoredEvent) {
        String url = instagram.getText();
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void tiktokclick(MouseEvent ignoredEvent) {
        String url = tiktok.getText();
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void youtubeclick(MouseEvent ignoredEvent) {
        String url = youtube.getText();
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleInstagramMouseEnter(MouseEvent ignoredEvent) {
        instagramImage.setScaleX(1.1);
        instagramImage.setScaleY(1.1);
        instagramImage.setCursor(Cursor.HAND);

    }

    @FXML
    public void handleInstagramMouseExit(MouseEvent ignoredEvent) {
        instagramImage.setScaleX(1.0);
        instagramImage.setScaleY(1.0);
        instagramImage.setCursor(Cursor.DEFAULT);

    }
    @FXML
    public void handleyoutubeMouseEnter(MouseEvent ignoredEvent) {
        youtubeimg.setScaleX(1.1);
        youtubeimg.setScaleY(1.1);
        youtubeimg.setCursor(Cursor.HAND);

    }

    @FXML
    public void handleyoutubeMouseExit(MouseEvent ignoredEvent) {
        youtubeimg.setScaleX(1.0);
        youtubeimg.setScaleY(1.0);
        youtubeimg.setCursor(Cursor.DEFAULT);
    }
    @FXML
    public void handletiktokMouseEnter(MouseEvent ignoredEvent) {
        tiktokimg.setScaleX(1.1);
        tiktokimg.setScaleY(1.1);
        tiktokimg.setCursor(Cursor.HAND);

    }
    @FXML
    public void handletiktokMouseExit(MouseEvent ignoredEvent) {
        tiktokimg.setScaleX(1.0);
        tiktokimg.setScaleY(1.0);
        tiktokimg.setCursor(Cursor.DEFAULT);
    }

    @FXML
    private TextArea privacyPolicyTextArea;

    @FXML
    public void showPrivacyPolicy(ActionEvent ignoredEvent) {
        privacyPolicyTextArea.setVisible(true);
        close_btn.setVisible(true);
        privacyPolicyTextArea.setText(
                """
                        Privacy Policy
                        
                        This Privacy Policy explains how SteamAssist collects, uses, and protects information related to your Steam account.
                         By using our service, you agree to the terms of this policy.
                        
                        Information We Collect
                        When you connect your Steam account to our service, we may collect the following data:
                        
                        Your Steam ID (publicly available)
                        
                        Your public profile data (e.g., avatar, display name)
                        
                        A list of games in your library
                        
                        Playtime data for each game
                        
                        We only access publicly available data through the Steam Web API, and we never collect your Steam password or any private information.
                        
                        How We Use Your Data
                        We use your data strictly for the following purposes:
                        
                        To generate personalized game recommendations based on your playtime
                        
                        To display your most played games in your profile on our platform (if applicable)
                        
                        We do not sell or share your data with third parties.
                        
                        Data Storage and Retention
                        Your Steam data may be stored on our secure servers to provide a seamless experience.
                        
                        We retain your data only as long as necessary for the purposes outlined above or until you request deletion.
                        
                        Data Sharing
                        We do not share, sell, or rent your data to third parties. +"\\n" +Your data is used solely to enhance your experience within our service.
                        
                        Security
                        We implement industry-standard security measures to protect your data from unauthorized access or misuse. +"\\n" +However, no method of transmission over the internet is 100% secure.
                        
                        Changes to This Policy
                        We may update this Privacy Policy from time to time. Changes will be posted on this page with the updated date. +"\\n" +Continued use of our service after changes are posted constitutes your agreement to the new terms.
                        """);
    }
    @FXML
    public void showaboutus(ActionEvent ignoredEvent) {
        privacyPolicyTextArea.setVisible(true);
        close_btn.setVisible(true);
        privacyPolicyTextArea.setText("""
                We are a team of the next members:\s
                    Cherqui Madarieta, Yunes
                    Elortondo Garro, Oier
                    Ituarte Arroitajauregi, Gorka
                    Zunzunegui Mintegui, Alejandro
                    Gonzalez Blazquez, Ivan
                """);
    }
    @FXML
    public void showcontactus(ActionEvent ignoredEvent) {
        privacyPolicyTextArea.setVisible(true);
        close_btn.setVisible(true);
        privacyPolicyTextArea.setText("""
                Contact Us
                If you have any questions about the app or concerns about the Privacy Policy or your data, you can contact us at:
                \s
                \uD83D\uDCE7 [steamassist55@gmail.com]""");
    }

    @FXML
    public void close(javafx.scene.input.MouseEvent ignoredEvent){
        privacyPolicyTextArea.setVisible(false);
        close_btn.setVisible(false);
    }
    public void closeenter(MouseEvent ignoredMouseEvent) {
        close_btn.setCursor(Cursor.HAND);
    }

    public void closeexit(MouseEvent ignoredMouseEvent) {
        close_btn.setCursor(Cursor.DEFAULT);
    }


    public void htlbMouseEnter(MouseEvent ignoredMouseEvent) {
        hltb_bttn.setScaleX(1.1);
        hltb_bttn.setScaleY(1.1);
        hltb_bttn.setStyle("-fx-background-color: #60A5FA; -fx-text-fill: #FFFFFF;");
        hltb_bttn.setCursor(Cursor.HAND);
    }

    public void htlbMouseExited(MouseEvent ignoredMouseEvent) {
        hltb_bttn.setScaleX(1.0);
        hltb_bttn.setScaleY(1.0);
        hltb_bttn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: #FFFFFF;");
        hltb_bttn.setCursor(Cursor.DEFAULT);
    }



    public void recommendMouseEnter(MouseEvent ignoredMouseEvent) {
        rg_bttn.setScaleX(1.1);
        rg_bttn.setScaleY(1.1);
        rg_bttn.setStyle("-fx-background-color: #60A5FA; -fx-text-fill: #FFFFFF;");
        rg_bttn.setCursor(Cursor.HAND);
    }

    public void recommendMouseExited(MouseEvent ignoredMouseEvent) {
        rg_bttn.setScaleX(1.0);
        rg_bttn.setScaleY(1.0);
        rg_bttn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: #FFFFFF;");
        rg_bttn.setCursor(Cursor.DEFAULT);
    }

    public void loginMouseEnter(MouseEvent ignoredMouseEvent) {
        login_bttn.setScaleX(1.1);
        login_bttn.setScaleY(1.1);
        if (isLoggedIn) {
            login_bttn.setStyle("-fx-background-color: #B91C1C; -fx-text-fill: #FFFFFF;");
        } else {
            login_bttn.setStyle("-fx-background-color: #1b5e20   ; -fx-text-fill: #FFFFFF;");
        }
        login_bttn.setCursor(Cursor.HAND);
    }

    public void loginMouseExited(MouseEvent ignoredMouseEvent) {
        login_bttn.setScaleX(1.0);
        login_bttn.setScaleY(1.0);
        if (isLoggedIn){
            login_bttn.setStyle("-fx-background-color: #DC2626; -fx-text-fill: #FFFFFF;");
        }
        else{
            login_bttn.setStyle("-fx-background-color: #2e7d32  ; -fx-text-fill: #FFFFFF;");
        }
        login_bttn.setCursor(Cursor.DEFAULT);
    }





    public void addacountsMouseEnter(MouseEvent ignoredMouseEvent) {
        add_acounts_btn.setScaleX(1.1);
        add_acounts_btn.setScaleY(1.1);
        add_acounts_btn.setStyle("-fx-background-color: #60A5FA; -fx-text-fill: #FFFFFF;");
        add_acounts_btn.setCursor(Cursor.HAND);
    }

    public void addacountsMouseExited(MouseEvent ignoredMouseEvent) {
        add_acounts_btn.setScaleX(1.0);
        add_acounts_btn.setScaleY(1.0);
        add_acounts_btn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: #FFFFFF;");
        add_acounts_btn.setCursor(Cursor.DEFAULT);
    }

}