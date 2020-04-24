package GUI;

import ClassStructure.Board;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class Controller implements Initializable {

    private Label setupTitle;
    private static int totalPlayerSize = 0;
    private static int playerSize = 0;
    private static int aiSize = 0;

    private static VBox alignPlayer;
    private static VBox alignAI;
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

    private Spinner spinAI1;
    private Spinner spinAI2;
    private Spinner spinAI3;
    private Spinner spinAI4;
    private Spinner spinAI5;
    private Spinner spinAI6;

    private TextField nameAI1;
    private TextField nameAI2;
    private TextField nameAI3;
    private TextField nameAI4;
    private TextField nameAI5;
    private TextField nameAI6;

    private Button generateGame;
    private ComboBox gameMode;

    //Tom's controls for the game board scene
    @FXML private Label diceRoll = new Label();
    @FXML private ChoiceBox playerSelector = new ChoiceBox();
    @FXML private Button mortgageBtn = new Button();
    @FXML private Button tradeBtn = new Button();
    //Controls for trade Screen
    @FXML private BorderPane tradeScreen = new BorderPane();
    @FXML private ListView player1Props = new ListView();
    @FXML private ListView player2Props = new ListView();
    @FXML private ChoiceBox playerBX = new ChoiceBox();
    @FXML private Label player1 = new Label();
    @FXML private Label player2 = new Label();
    private ArrayList player1Trades = new ArrayList<>();


    //@FXML private Button addPlayer = new Button();

    // This is called when the GUI is loading
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Label used in setup scene for the title
        setupTitle = new Label();
        setupTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        setupTitle.setText("Property Tycoon Setup\n");

        // Initializing players' spinner and textfield
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

        // Initializing AIs' spinner and textfield
        // A array-list containing all the possible name for the AI (randomly generated)
        ArrayList<String> nameAI = new ArrayList<String>(); // Contains 6 names (possibly 6 AIs' playing)
        nameAI.add("Bob");
        nameAI.add("Fred");
        nameAI.add("Alex");
        nameAI.add("Jess");
        nameAI.add("Annie");
        nameAI.add("Adel");

        Random rand = new Random();
        int randomIndex = rand.nextInt(nameAI.size());
        String tempName = nameAI.get(randomIndex);

        spinAI1 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI1 = new TextField();
        nameAI1.setStyle("-fx-font-size: 14px");
        nameAI1.setPromptText(tempName);
        // Remove the AI's name that been assigned so no duplicate name at one time
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI2 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI2 = new TextField();
        nameAI2.setStyle("-fx-font-size: 14px");
        nameAI2.setPromptText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI3 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI3.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI3 = new TextField();
        nameAI3.setStyle("-fx-font-size: 14px");
        nameAI3.setPromptText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI4 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI4 = new TextField();
        nameAI4.setStyle("-fx-font-size: 14px");
        nameAI4.setPromptText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI5 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI5.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI5 = new TextField();
        nameAI5.setStyle("-fx-font-size: 14px");
        nameAI5.setPromptText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI6 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI6.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI6 = new TextField();
        nameAI6.setStyle("-fx-font-size: 14px");
        nameAI6.setPromptText(tempName);
        nameAI.remove(randomIndex);

        generateGame = new Button("Generate Game");
        // CSS for the generate game button
        generateGame.setPrefSize(170,30);
        // Change to the generated game scene when clicked
        generateGame.setOnAction(e -> {
            // When clicked, switch to game board scene
            try {
                // Fetch and see if player chose the normal game version or abridged
                String mode = gameMode.getValue().toString(); ;
                gameBoardSceneChange(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Setting up the choice box so players can select the game mode either normal or abridged
        gameMode = new ComboBox();
        gameMode.getItems().addAll("Normal", "Abridged");
        // Setting the default value for game mode to be normal
        gameMode.setValue("Normal");
        //Setting the selectionModel for the Trade Screen in game board scene
        player1Props.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        player2Props.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        window.setFullScreen(true);
        window.show();
    }

    /**
     *  Recreate the same scene but adding a addition spinner and textfield (add spinner and textfield for AI)
     * @param event
     * @throws IOException
     */
    public void addNewPlayer(ActionEvent event) throws IOException {
        // Grab the code from setupScene (Add player and Add AI button)
        Parent setupParent = FXMLLoader.load(getClass().getResource("setupScene.fxml"));
        // Used to add title (label), playerSetup (spinner and textfield), setupParent (Add player and Add AI)
        VBox setup = new VBox(10);
        setup.setAlignment(Pos.TOP_CENTER);

        totalPlayerSize++;
        // playerSize is used a index
        playerSize++;

        VBox alignAddPlayer = new VBox();
        alignAddPlayer = addPlayerSetup();

        if (totalPlayerSize == playerSize && playerSize < 2) {
            // Check if only players has been added
            setup.getChildren().addAll(
                    setupTitle, alignAddPlayer,setupParent
            );
        }else if(totalPlayerSize == playerSize && playerSize != 6){
            // Check if only players has been added and if player size > 2 allow the game to start
            setup.getChildren().addAll(
                    setupTitle, alignAddPlayer, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize == playerSize && totalPlayerSize == 6) {
            // Check if only players has been added and if player size == 6, then disable add players and AI button
            setup.getChildren().addAll(
                    setupTitle, alignAddPlayer, gameMode, generateGame
            );
        }else if(totalPlayerSize != playerSize && totalPlayerSize < 6){
            // Check if both AIs and players has been added and allow the options to add in more players and AIs
            setup.getChildren().addAll(
                    setupTitle, alignAddPlayer, alignAI, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize != playerSize && totalPlayerSize == 6){
            // Check if AIs and players has been added and if totalPlayerSize == 6, then disable add players and AI button
            setup.getChildren().addAll(
                    setupTitle, alignAddPlayer, alignAI, gameMode, generateGame
            );
        }
        alignPlayer = alignAddPlayer;

        //String bob = gameMode.getValue().toString(); //!!!! assigned to a variable later on (value converted to string atm)
        Scene setupScene  = new Scene(setup,370,420);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(setupScene);
        window.show();
    }

    /***
     *
     * @return VBox which setup the spinner and textfield for each players'
     */
    public VBox addPlayerSetup() {
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

        VBox alignPlayer = new VBox(10);
        alignPlayer.setAlignment(Pos.TOP_CENTER);

        // Making a list of tokens that the user could choose from
        ObservableList<String> tokenName = FXCollections.observableArrayList(
                "Boot", "Smartphone", "Goblet","Hatstand","Cat","Spoon"
        );

        switch(playerSize) {
            case 1:
                SpinnerValueFactory<String> tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);
                playerSetup1.getChildren().addAll(spinP1, nameP1);
                alignPlayer.getChildren().add(playerSetup1);
                break;
            case 2:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);
                SpinnerValueFactory<String> tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinP2.setValueFactory(tokenChoice2);

                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                alignPlayer.getChildren().addAll(playerSetup1,playerSetup2);
                break;
            case 3:
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
                alignPlayer.getChildren().addAll(playerSetup1,playerSetup2,playerSetup3);
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

                SpinnerValueFactory<String> tokenChoice4 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice4.setValue("Boot");
                spinP4.setValueFactory(tokenChoice4);

                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                playerSetup3.getChildren().addAll(spinP3, nameP3);
                playerSetup4.getChildren().addAll(spinP4, nameP4);
                alignPlayer.getChildren().addAll(playerSetup1,playerSetup2,playerSetup3,playerSetup4);
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

                SpinnerValueFactory<String> tokenChoice5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice5.setValue("Boot");
                spinP5.setValueFactory(tokenChoice5);

                playerSetup1.getChildren().addAll(spinP1, nameP1);
                playerSetup2.getChildren().addAll(spinP2, nameP2);
                playerSetup3.getChildren().addAll(spinP3, nameP3);
                playerSetup4.getChildren().addAll(spinP4, nameP4);
                playerSetup5.getChildren().addAll(spinP5, nameP5);
                alignPlayer.getChildren().addAll(
                        playerSetup1,playerSetup2,playerSetup3,playerSetup4,playerSetup5
                );
                break;
            case 6:
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
                alignPlayer.getChildren().addAll(
                        playerSetup1,playerSetup2,playerSetup3,playerSetup4,playerSetup5,playerSetup6
                );
                break;
        }
        return alignPlayer;
    }

    /***
     * Recreate the same scene but adding a addition spinner and textfield (add spinner and textfield for AI)
     * @param event
     * @throws IOException
     */
    public void addNewAI(ActionEvent event) throws IOException {
        // Grab the code from setupScene (Add player and Add AI button)
        Parent setupParent = FXMLLoader.load(getClass().getResource("setupScene.fxml"));
        // Used to add title (label), playerSetup (spinner and textfield), setupParent (Add player and Add AI)
        VBox setup = new VBox(10);
        setup.setAlignment(Pos.TOP_CENTER);

        totalPlayerSize++;
        // playerSize is used a index
        aiSize++;

        VBox alignAddAi = new VBox();
        alignAddAi = addAISetup();

        if (totalPlayerSize == aiSize && aiSize < 2) {
            setup.getChildren().addAll(
                    setupTitle, alignAddAi, setupParent
            );
        }else if(totalPlayerSize == aiSize && aiSize != 6){
            setup.getChildren().addAll(
                    setupTitle, alignAddAi, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize == aiSize && totalPlayerSize == 6) {
            setup.getChildren().addAll(
                    setupTitle, alignAddAi, gameMode, generateGame
            );
        }else if(totalPlayerSize != aiSize && totalPlayerSize < 6){
            setup.getChildren().addAll(
                    setupTitle, alignPlayer, alignAddAi, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize != playerSize && totalPlayerSize == 6){
            setup.getChildren().addAll(
                    setupTitle, alignPlayer, alignAddAi, gameMode, generateGame
            );
        }
        alignAI = alignAddAi;

        Scene setupScene  = new Scene(setup,370,420);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(setupScene);
        window.show();
    }

    /***
     *
     * @return VBox which setup the spinner and textfield for each AIs' sadasdshakjdashdkjashdsajkhasjk
     */
    public VBox addAISetup() {
        // Used to add token choice (spinner) and players name (text field) for each players
        HBox aiSetup1 = new HBox(10);
        aiSetup1.setAlignment(Pos.BOTTOM_CENTER);
        HBox aiSetup2 = new HBox(10);
        aiSetup2.setAlignment(Pos.BOTTOM_CENTER);
        HBox aiSetup3 = new HBox(10);
        aiSetup3.setAlignment(Pos.BOTTOM_CENTER);
        HBox aiSetup4 = new HBox(10);
        aiSetup4.setAlignment(Pos.BOTTOM_CENTER);
        HBox aiSetup5 = new HBox(10);
        aiSetup5.setAlignment(Pos.BOTTOM_CENTER);
        HBox aiSetup6 = new HBox(10);
        aiSetup6.setAlignment(Pos.BOTTOM_CENTER);

        VBox alignAI = new VBox(10);
        alignAI.setAlignment(Pos.TOP_CENTER);

        // Making a list of tokens that the user could choose from
        ObservableList<String> tokenName = FXCollections.observableArrayList(
                "Boot", "Smartphone", "Goblet","Hatstand","Cat","Spoon"
        );

        switch(aiSize) {
            case 1:
                SpinnerValueFactory<String> tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);

                aiSetup1.getChildren().addAll(spinAI1, nameAI1);
                alignAI.getChildren().add(aiSetup1);
                break;
            case 2:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);

                SpinnerValueFactory<String> tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinAI2.setValueFactory(tokenChoice2);

                aiSetup1.getChildren().addAll(spinAI1, nameAI1);
                aiSetup2.getChildren().addAll(spinAI2, nameAI2);
                alignAI.getChildren().addAll(aiSetup1,aiSetup2);
                break;
            case 3:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinAI2.setValueFactory(tokenChoice2);

                SpinnerValueFactory<String> tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinAI3.setValueFactory(tokenChoice3);

                aiSetup1.getChildren().addAll(spinAI1, nameAI1);
                aiSetup2.getChildren().addAll(spinAI2, nameAI2);
                aiSetup3.getChildren().addAll(spinAI3, nameAI3);
                alignAI.getChildren().addAll(aiSetup1,aiSetup2,aiSetup3);
                break;
            case 4:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinAI2.setValueFactory(tokenChoice2);

                tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinAI3.setValueFactory(tokenChoice3);

                SpinnerValueFactory<String> tokenChoice4 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice4.setValue("Boot");
                spinAI4.setValueFactory(tokenChoice4);

                aiSetup1.getChildren().addAll(spinAI1, nameAI1);
                aiSetup2.getChildren().addAll(spinAI2, nameAI2);
                aiSetup3.getChildren().addAll(spinAI3, nameAI3);
                aiSetup4.getChildren().addAll(spinAI4, nameAI4);
                alignAI.getChildren().addAll(aiSetup1,aiSetup2,aiSetup3,aiSetup4);
                break;
            case 5:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinAI2.setValueFactory(tokenChoice2);

                tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinAI3.setValueFactory(tokenChoice3);

                tokenChoice4 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice4.setValue("Boot");
                spinAI4.setValueFactory(tokenChoice4);

                SpinnerValueFactory<String> tokenChoice5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice5.setValue("Boot");
                spinAI5.setValueFactory(tokenChoice5);

                aiSetup1.getChildren().addAll(spinAI1, nameAI1);
                aiSetup2.getChildren().addAll(spinAI2, nameAI2);
                aiSetup3.getChildren().addAll(spinAI3, nameAI3);
                aiSetup4.getChildren().addAll(spinAI4, nameAI4);
                aiSetup5.getChildren().addAll(spinAI5, nameAI5);
                alignAI.getChildren().addAll(
                        aiSetup1,aiSetup2,aiSetup3,aiSetup4,aiSetup5
                );
                break;
            case 6:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);

                tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinAI2.setValueFactory(tokenChoice2);

                tokenChoice3 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice3.setValue("Boot");
                spinAI3.setValueFactory(tokenChoice3);

                tokenChoice4 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice4.setValue("Boot");
                spinAI4.setValueFactory(tokenChoice4);

                tokenChoice5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice5.setValue("Boot");
                spinAI5.setValueFactory(tokenChoice5);

                SpinnerValueFactory<String> tokenChoice6 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice6.setValue("Boot");
                spinAI6.setValueFactory(tokenChoice6);

                aiSetup1.getChildren().addAll(spinAI1, nameAI1);
                aiSetup2.getChildren().addAll(spinAI2, nameAI2);
                aiSetup3.getChildren().addAll(spinAI3, nameAI3);
                aiSetup4.getChildren().addAll(spinAI4, nameAI4);
                aiSetup5.getChildren().addAll(spinAI5, nameAI5);
                aiSetup6.getChildren().addAll(spinAI6, nameAI6);
                alignAI.getChildren().addAll(
                        aiSetup1,aiSetup2,aiSetup3,aiSetup4,aiSetup5,aiSetup6
                );
                break;
        }
        return alignAI;
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

    public void goToTrade(ActionEvent event){
        tradeScreen.setVisible(true);
        player1.setText("Tom Jones");
        player1Props.getItems().addAll("Gangster Paradise", "Tesla PowerCo","Brighton Station","Weeping Angel");
        player2Props.getItems().addAll("Falmer Station","Kirk Close","Picard Avenue","Edison Water","Crusher Creek");
    }

    public void player1TradeConfirm(ActionEvent event){
        Alert testMsg = new Alert(Alert.AlertType.INFORMATION);
        testMsg.setTitle(player1.getText());
        String txtMsg = "1";

        testMsg.setHeaderText("You've selected: " + txtMsg);
        testMsg.showAndWait();
    }

    public void rollDice(ActionEvent event) throws IOException{
        Random rand = new Random();
        diceRoll.setText("21");
    }
}
