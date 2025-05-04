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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import static eus.ehu.template.ui.MainUI.stage;

public class MainController {

    @FXML
    private Hyperlink ContactUsLink;
    @FXML
    private Hyperlink AboutUsLink;
    @FXML
    private Hyperlink PrivacyPolicyLink;
    @FXML
    private Label FollowUs;
    @FXML
    private Button Next;
    @FXML
    private Button Previous;

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
    private ComboBox<String> languageComboBox;

    @FXML
    private ImageView instagramImage;
    @FXML
    private ImageView youtubeimg;
    @FXML
    private ImageView tiktokimg;

    @FXML
    private ImageView close_btn;


    // Variables for the Steam ID
    private boolean SteamIDSet = false;
    private String SteamID;
    // Variables SteamAPIkey
    private static String SteamAPIkey;

    //bundle for i18n
    private ResourceBundle bundle;

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
    private int PrivacyText=0;

    @FXML
    private Label IDLabel;


    @FXML
    public void initialize() {
        //Language setup
        bundle = MainUI.getBundle();

        FXMLLoader loginCon = new FXMLLoader(getClass().getResource("login.fxml"));
        FXMLLoader idCon = new FXMLLoader(getClass().getResource("steam_id.fxml"));
        FXMLLoader hltbCon = new FXMLLoader(getClass().getResource("howlongtobeat.fxml"));
        FXMLLoader registerCon = new FXMLLoader(getClass().getResource("register.fxml"));
        FXMLLoader steamAddCon = new FXMLLoader(getClass().getResource("steam_addAccount.fxml"));
        FXMLLoader recommendCon = new FXMLLoader(getClass().getResource("/eus/ehu/template/ui/recommended.fxml"));
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

        updateUITexts();
        ChangeSteamID();

        //
        languageComboBox.getItems().addAll("English", "Euskera", "Español");
        languageComboBox.setValue("English");

        //Set SteamAPIkey default
        SteamAPIkey="FE22AD6FF3F54621F5BE4652A491CEFF";
        updateUI();
        this.setSteamIDSet(false);
    }

    @FXML
    public void recommend_games(ActionEvent event) {
            RecommendStage.show();
            stage.hide();
            howLongToBeatStage.hide();
            idStage.hide();
            recommendedController.setMainController(this);

    }

    public void updateLoginStatus(String message, String color) {
        introtext.setVisible(false);
        li_confirm.setText(message);
        li_confirm.setStyle("-fx-text-fill: " + color + ";");
    }

    private void updateUITexts() {
        bundle = MainUI.getBundle();

        // Actualizar textos principalez
        hltb_bttn.setText(bundle.getString("howlongtobeat"));
        rg_bttn.setText(bundle.getString("recommendations"));
        login_bttn.setText(isLoggedIn ? bundle.getString("logout") : bundle.getString("loginSignup"));
        add_acounts_btn.setText(bundle.getString("addAccounts"));
        introtext.setText(bundle.getString("introText"));

        //Actualizar titulos de los controladores
        loginStage.setTitle(bundle.getString("loginTitle"));
        RegisterStage.setTitle(bundle.getString("registerTitle"));
        howLongToBeatStage.setTitle(bundle.getString("howlongtobeatTitle"));
        idStage.setTitle(bundle.getString("steamIDTitle"));
        RecommendStage.setTitle(bundle.getString("recommendGameTitle"));
        AddAccountStage.setTitle(bundle.getString("addSteamAccountTitle"));

        // Actualizar otros texto
        Next.setText(bundle.getString("next"));
        Previous.setText(bundle.getString("previous"));
        ChangeSteamID();
        PrivacyPolicyLink.setText(bundle.getString("privacyPolicyTitle"));
        AboutUsLink.setText(bundle.getString("aboutUsTitle"));
        ContactUsLink.setText(bundle.getString("contactUsTitle"));
        FollowUs.setText(bundle.getString("followUs"));

        // Actualizar textos largos
        switch (PrivacyText) {
            case 0:
                privacyPolicyTextArea.setText(bundle.getString("privacyPolicy"));
                break;
            case 1:
                privacyPolicyTextArea.setText(bundle.getString("aboutUs"));
                break;
            case 2:
                privacyPolicyTextArea.setText(bundle.getString("contactUs"));
                break;
        }

    }

    @FXML
    public void changeLanguage(ActionEvent event) {
        String selectedLanguage = languageComboBox.getValue();
        String locale;

        switch (selectedLanguage) {
            case "English":
                locale = "en";
                break;
            case "Euskera":
            default:
                locale = "eu";
                break;
            case "Español":
                locale = "es";
                break;
        }

        // Actualizar el ResourceBundle en MainUI
        MainUI.ChangeLanguage(locale);

        // Actualizar textos en la interfaz
        updateUITexts();

        // Actualizar textos en los controladores
        loginController.updateUITexts();
        registerController.updateUITexts();
        steamIDController.updateUITexts();
        howLongToBeatController.updateUITexts();
        recommendedController.updateUITexts();
        steamAddAccountController.updateUITexts();
    }

    @FXML
    void handleLoginButton(ActionEvent event) {
        if (isLoggedIn) {
            // Handle logout
            setLoggedIn(false);
            li_confirm.setText("");
            introtext.setVisible(true);
            loggedUser=null;
            SteamID=null;
            ChangeSteamID();
        } else {
            showLogin(event);
        }
    }

    private void showLogin(ActionEvent ignoredEvent) {
        loginStage.show();
        stage.hide();
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
        bundle = MainUI.getBundle();
        // Cambiar texto usando ResourceBundle
        login_bttn.setText(isLoggedIn ? bundle.getString("logout") : bundle.getString("loginSignup"));
        if (isLoggedIn) {
            login_bttn.setStyle("-fx-background-color: #DC2626; -fx-text-fill: #FFFFFF;");
            buttonBar.getButtons().removeAll(add_acounts_btn, login_bttn);
            buttonBar.getButtons().addAll(add_acounts_btn, login_bttn);
        } else {
            login_bttn.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: #FFFFFF;");
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
            stage.hide();
            loginStage.hide();
        } else {
            idStage.show();
            howLongToBeatStage.hide();
            stage.hide();
            loginStage.hide();
            li_confirm.setText(bundle.getString("loginRequired"));
        }
    }



    @FXML
    void addAccounts(ActionEvent event) {
        System.out.print(AccountsNames.toString());
        System.out.print(AccountsIDs.toString());
        AddAccountStage.show();
        // Actualizar título a través del ResourceBundle
        AddAccountStage.setTitle(bundle.getString("addSteamAccountTitle"));
        steamAddAccountController.setMainController(this);
        stage.hide();
    }

    public void setLoggedUser(User user) {
        loggedUser = user;
    }

    public User getLoggedUser() {
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

    //When we log in, we get the SteamID and AccountName from the database
    public void LogInSteamIDs(){
        BlFacade bl = BlFacadeImplementation.getInstance();
        User user = getLoggedUser();
        if (user == null) {
            IDLabel.setText(bundle.getString("noSteamIDsFound"));
            return;
        }
        AccountsNames = bl.getAccountname(user.getUserName());
        ArrayList<String> SteamIDs = bl.getSteamID(user.getUserName()); // Get the IDs from the database
        if (AccountsNames != null && !AccountsNames.isEmpty()) {
            steamIDIndex = 0;
            SteamID= SteamIDs.get(steamIDIndex);
            SteamIDSet=true;
            ChangeSteamID();
        } else {
            IDLabel.setText(bundle.getString("noSteamIDsFound"));
        }
    }

    public void ChangeSteamID() {
        BlFacade bl = BlFacadeImplementation.getInstance();
        User user = getLoggedUser();
        if (user == null) {
            IDLabel.setText(bundle.getString("noSteamIDsFound"));
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
            IDLabel.setText(bundle.getString("noSteamIDsFound"));
        }
    }
    public void setSteamIDSet(boolean steamIDSet) {
        SteamIDSet = steamIDSet;
    }

    public boolean isSteamIDSet() {return SteamIDSet;}

    public void setSteamAPIkey(String apikeyText) {
        SteamAPIkey = apikeyText;
    }

    public String getSteamAPIKey() {
        return SteamAPIkey;
    }
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
        privacyPolicyTextArea.setText(bundle.getString("privacyPolicy"));
        PrivacyText=0;
    }

    @FXML
    public void showaboutus(ActionEvent ignoredEvent) {
        privacyPolicyTextArea.setVisible(true);
        close_btn.setVisible(true);
        privacyPolicyTextArea.setText(bundle.getString("aboutUs"));
        PrivacyText=1;
    }

    @FXML
    public void showcontactus(ActionEvent ignoredEvent) {
        privacyPolicyTextArea.setVisible(true);
        close_btn.setVisible(true);
        privacyPolicyTextArea.setText(bundle.getString("contactUs"));
        PrivacyText=2;
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