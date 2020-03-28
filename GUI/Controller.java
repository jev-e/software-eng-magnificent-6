package GUI;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;


public class Controller implements Initializable {

    private Label setupTitle;
    private static int playerSize = 0;
    // Change!! dont know how to implement it better at this time
    private Spinner spinP1;
    private Spinner spinP2;
    private Spinner spinP3;
    private Spinner spinP4;
    private Spinner spinP5;
    private Spinner spinP6;

    private TextField nameP1;
    private TextField nameP2;
    private TextField nameP3;
    private TextField nameP4;
    private TextField nameP5;
    private TextField nameP6;

    @FXML private Button addPlayer = new Button();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Label used in setup scene for the title
        setupTitle = new Label();
        setupTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        setupTitle.setText("Property Tycoon Setup\n");

        spinP1 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameP1 = new TextField();
        nameP1.setStyle("-fx-font-size: 14px");
        nameP1.setPromptText("Enter players' 1 name");

        spinP2 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameP2 = new TextField();
        nameP2.setStyle("-fx-font-size: 14px");
        nameP2.setPromptText("Enter players' 2 name");

        spinP3 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP3.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameP3 = new TextField();
        nameP3.setStyle("-fx-font-size: 14px");
        nameP3.setPromptText("Enter players' 3 name");

        spinP4 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameP4 = new TextField();
        nameP4.setStyle("-fx-font-size: 14px");
        nameP4.setPromptText("Enter players' 4 name");

        spinP5 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP5.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameP5 = new TextField();
        nameP5.setStyle("-fx-font-size: 14px");
        nameP5.setPromptText("Enter players' 5 name");

        spinP6 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP6.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameP6 = new TextField();
        nameP6.setStyle("-fx-font-size: 14px");
        nameP6.setPromptText("Enter players' 6 name");
    }

    /**
     * Switch scene from setup when "Generate Game" button is clicked
     * @param event
     * @throws IOException
     */
    public void gameBoardSceneChange(ActionEvent event) throws IOException {
        Parent gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoardScene.fxml"));
        Scene gameScene  = new Scene(gameBoardParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameScene);
        window.show();
    }

    /**
     *  Add a new spinner and a textfield to setup scene if button is clicked
     * @param event
     * @throws IOException
     */
    public void addNewPlayer(ActionEvent event) throws IOException {
        // Grab the code from setupScene (Add player and Add AI)
        Parent setupParent = FXMLLoader.load(getClass().getResource("setupScene.fxml"));
        // Used to add title (label), playerSetup (spinner and textfield), setupParent (Add player and Add AI)
        VBox setup = new VBox(10);
        setup.setAlignment(Pos.TOP_CENTER);

        // Used to add token choice (spinner) and players name (text field) for each players
        HBox playerSetup1 = new HBox(10);
        playerSetup1.setAlignment(Pos.BOTTOM_CENTER);
        HBox playerSetup2 = new HBox(10);
        playerSetup2.setAlignment(Pos.BOTTOM_CENTER);
        HBox playerSetup3 = new HBox(10);
        playerSetup3.setAlignment(Pos.BOTTOM_CENTER);
        HBox playerSetup4 = new HBox(10);
        playerSetup4.setAlignment(Pos.BOTTOM_CENTER);
        HBox playerSetup5 = new HBox(10);
        playerSetup5.setAlignment(Pos.BOTTOM_CENTER);
        HBox playerSetup6 = new HBox(10);
        playerSetup6.setAlignment(Pos.BOTTOM_CENTER);

        VBox alignAddPlayer = new VBox(10);
        alignAddPlayer.setAlignment(Pos.TOP_CENTER);

        // Making a list of tokens that the user could choose from
        ObservableList<String> tokenName = FXCollections.observableArrayList(
                "Boot", "Smartphone", "Goblet","Hatstand","Cat","Spoon"
        );

        switch(playerSize) {
            case 0:
                SpinnerValueFactory<String> tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);
                playerSetup1.getChildren().addAll(spinP1, nameP1);
                alignAddPlayer.getChildren().add(playerSetup1);
                break;
            case 1:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);

                SpinnerValueFactory<String> tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinP2.setValueFactory(tokenChoice2);
                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                alignAddPlayer.getChildren().addAll(playerSetup1,playerSetup2);
                break;
            case 2:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinP2.setValueFactory(tokenChoice2);

                SpinnerValueFactory<String> tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinP3.setValueFactory(tokenChoice3);

                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                playerSetup3.getChildren().addAll(spinP3, nameP3);

                alignAddPlayer.getChildren().addAll(playerSetup1,playerSetup2,playerSetup3);
                break;
            case 3:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinP2.setValueFactory(tokenChoice2);

                tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinP3.setValueFactory(tokenChoice3);

                SpinnerValueFactory<String> tokenChoice4 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice4.setValue("Boot");
                spinP4.setValueFactory(tokenChoice3);

                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                playerSetup3.getChildren().addAll(spinP3, nameP3);
                playerSetup4.getChildren().addAll(spinP4, nameP4);
                alignAddPlayer.getChildren().addAll(playerSetup1,playerSetup2,playerSetup3,playerSetup4);
                break;
            case 4:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinP2.setValueFactory(tokenChoice2);

                tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinP3.setValueFactory(tokenChoice3);

                tokenChoice4 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice4.setValue("Boot");
                spinP4.setValueFactory(tokenChoice4);

                SpinnerValueFactory<String> tokenChoice5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice5.setValue("Boot");
                spinP5.setValueFactory(tokenChoice5);

                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                playerSetup3.getChildren().addAll(spinP3, nameP3);
                playerSetup4.getChildren().addAll(spinP4, nameP4);
                playerSetup5.getChildren().addAll(spinP5, nameP5);
                alignAddPlayer.getChildren().addAll(
                        playerSetup1,playerSetup2,playerSetup3,playerSetup4,playerSetup5
                );
                break;
            case 5:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinP2.setValueFactory(tokenChoice2);

                tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinP3.setValueFactory(tokenChoice3);

                tokenChoice4 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice4.setValue("Boot");
                spinP4.setValueFactory(tokenChoice4);

                tokenChoice5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice5.setValue("Boot");
                spinP5.setValueFactory(tokenChoice5);

                SpinnerValueFactory<String> tokenChoice6 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice6.setValue("Boot");
                spinP6.setValueFactory(tokenChoice6);

                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                playerSetup3.getChildren().addAll(spinP3, nameP3);
                playerSetup4.getChildren().addAll(spinP4, nameP4);
                playerSetup5.getChildren().addAll(spinP5, nameP5);
                playerSetup6.getChildren().addAll(spinP6, nameP6);
                alignAddPlayer.getChildren().addAll(
                        playerSetup1,playerSetup2,playerSetup3,playerSetup4,playerSetup5,playerSetup6
                );
                break;
        }

        Button generateGame = new Button("Generate Game");
        generateGame.setOnAction(e -> {
            // When clicked, switch to game board scene
            try {
                gameBoardSceneChange(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // If player size == 6, disable add player and add AI button (0-5)
        if (playerSize < 5) {
            setup.getChildren().addAll(
                    setupTitle, alignAddPlayer, setupParent
            );
        }else{
            setup.getChildren().addAll(
                    setupTitle, alignAddPlayer, generateGame
            );
        }

        Scene setupScene  = new Scene(setup,370,420);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        playerSize++;
        window.setScene(setupScene);
        window.show();
    }

    /**
     * Switch scene from main menu to setup (where the game is setup) when Start button is clicked
     * @param event
     */
    public void setupSceneChange(ActionEvent event) throws IOException {
        Parent setupParent = FXMLLoader.load(getClass().getResource("setupScene.fxml"));
        VBox setup = new VBox(10);
        setup.setAlignment(Pos.TOP_CENTER);

        setup.getChildren().addAll(setupTitle,setupParent);
        Scene setupScene  = new Scene(setup);

        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(setupScene);
        window.show();
    }

    /**
     * Switch scene from main menu to rules
     * @param event
     */
    public void ruleSceneChange(ActionEvent event) throws IOException {
        Parent ruleParent = FXMLLoader.load(getClass().getResource("ruleScene.fxml"));
        Scene ruleScene  = new Scene(ruleParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(ruleScene);
        window.show();
    }

    /**
     * Switch scene from rules to main menu
     * @param event
     */
    public void mainSceneChange(ActionEvent event) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene mainScene  = new Scene(mainParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    /**
     * When "exit" button is clicked, exit application
     * @param event
     */
    public void quit(ActionEvent event){
        Platform.exit();
    }
}
