package GUI;

import ClassStructure.*;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.*;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javax.swing.*;
import java.io.IOException;

public class Controller implements Initializable {

    @FXML Label setupTitle;

    private static int totalPlayerSize = 0;
    private static int playerSize = 0;
    private static int aiSize = 0;
    private static int randomSeed = 0;

    //Tom's controls for the game board scene
    @FXML private Label diceRoll = new Label();
    @FXML private ChoiceBox playerSelector = new ChoiceBox();
    @FXML private Button mortgageBtn = new Button();
    @FXML private Button tradeBtn = new Button();
    //Tom's Controls for start of turn
    @FXML private Label curPlayerLbl = new Label();

    //Tom Controls for trade Screen
    @FXML private BorderPane tradeScreen = new BorderPane();
    @FXML private ListView player1Props = new ListView();
    @FXML private ListView player2Props = new ListView();
    @FXML private ChoiceBox playerBX = new ChoiceBox();
    @FXML private Label player1 = new Label();
    @FXML private Label player2 = new Label();
    private ArrayList player1Trades = new ArrayList<>();

    // Player setup (choosing tokens and players name)
    private Spinner spinP1 = new Spinner <String>();
    private Spinner spinP2 = new Spinner <String>();
    private Spinner spinP3 = new Spinner <String>();
    private Spinner spinP4 = new Spinner <String>();
    private Spinner spinP5 = new Spinner <String>();
    private Spinner spinP6 = new Spinner <String>();

    private TextField txtP1 = new TextField();
    private TextField txtP2 = new TextField();
    private TextField txtP3 = new TextField();
    private TextField txtP4 = new TextField();
    private TextField txtP5 = new TextField();
    private TextField txtP6 = new TextField();

    // AI setup (name randomly chosen for each Ai)
    private Spinner spinAI1  = new Spinner <String>();
    private Spinner spinAI2  = new Spinner <String>();
    private Spinner spinAI3  = new Spinner <String>();
    private Spinner spinAI4  = new Spinner <String>();
    private Spinner spinAI5  = new Spinner <String>();
    private Spinner spinAI6  = new Spinner <String>();

    private TextField txtAI1 = new TextField();
    private TextField txtAI2 = new TextField();
    private TextField txtAI3 = new TextField();
    private TextField txtAI4 = new TextField();
    private TextField txtAI5 = new TextField();
    private TextField txtAI6 = new TextField();

    // Vbox containing the approriate spinner and textfield for each player and AI
    private static VBox playerAmount = new VBox(10);
    private static VBox aiAmount = new VBox(10);

    private ComboBox gameMode;
    private String mode;
    // Button in setup scene (When clicked, switch from setup scene to board scene)
    private Button generateGame = new Button("Generate Game");

    //Game board data
    private static HashMap<Integer, BoardTile> board = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();
    private static List<CardEffect> opportunityKnocksPack;
    private static List<CardEffect> potLuckPack;
    private Board gameSystem;
    private int playerNumber;

    // This is called every time a new scene is built
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        // Label used in setup scene for the title
        setupTitle = new Label();
        setupTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        setupTitle.setText("Property Tycoon Setup\n");

        // CSS for the generate game button
        generateGame.setPrefSize(170,30);
        // Setting the default value for game mode to be normal
        //Setting the selectionModel for the Trade Screen in game board scene
        player1Props.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        player2Props.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void bid(ActionEvent event){
    }

    /***
     * Change to the auctioning popup screen if 'auction' has been clicked from buyProperty()
     * @throws IOException
     */
    public void auctionSceneChange() throws IOException{
        Stage stage = new Stage();
        Parent root;

        root = FXMLLoader.load(getClass().getResource("auctionScene.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /***
     * If player land on a unowned property, ask player or AI if they would like to buy it
     * @throws IOException
     */
    public void buyProperty() throws IOException {
        // Fetch data to see if property is owned by a player
        boolean unownedProperty = true;
        // Fetch the property name
        String propertyName = "bob valley";
        int propertyValue = 500;
        ButtonType buyProperty = new ButtonType("Buy Property");
        ButtonType auctionProperty = new ButtonType("Auction");

        if(unownedProperty == true){
            Alert propertyMessage = new Alert(AlertType.NONE);
            propertyMessage.setHeaderText("Do you want to buy " + propertyName + "?");
            propertyMessage.setContentText(propertyName + " would cost £" + propertyValue);
            propertyMessage.getButtonTypes().addAll(buyProperty,auctionProperty);
            Optional<ButtonType> option = propertyMessage.showAndWait();

            // Implement with logic!!!!!!!!!!!!!!!!!!!!!!!!!!
            if(option.get() == buyProperty){
                // After clicking the "Buy Property" button from the popup
                System.out.println("you Payed $500");
            }else if(option.get() == auctionProperty){
                // After clicking the "Auction" button from the popup
                System.out.println("Auction");
                auctionSceneChange();
            }
        }else{
            // If property is owned then pay rent!!!!
            System.out.println("Pay rent £200");
        }
    }

    /***
     * If two dices rolled the same number, allow the user the roll again
     * @param event If roll button is clicked, a new window appears showing you all the options that you can choose from
     * @throws IOException
     */
    public void diceRoll(ActionEvent event) throws IOException {
        int diceOne = 2;
        int diceTwo = 3;

        // Allow the user to roll again if both the dices == same
        ButtonType rollAgain = new ButtonType("Reroll");
        if(diceOne == diceTwo){
            Alert diceMessage = new Alert(AlertType.NONE);
            diceMessage.setTitle("Dice Generated");
            diceMessage.setHeaderText("Your first dice rolled a " + diceOne + " and the second rolled a " + diceTwo);
            diceMessage.setContentText("Roll again");
            // IMPLEMENT LATER OPTION TO BUY
            //buyProperty();
            diceMessage.getButtonTypes().add(rollAgain);
            Optional<ButtonType> option = diceMessage.showAndWait();
            // After clicking the "Reroll" button from the popup
            if(option.get() == rollAgain){
                // function to roll again implement later
                Alert reroll = new Alert(AlertType.INFORMATION);
                reroll.setTitle("Dice Generated");
                reroll.setHeaderText("Reroll test");
                reroll.show();
            }
        }else{ // dice one and dice two != same
            Alert diceMessage = new Alert(AlertType.INFORMATION);
            diceMessage.setTitle("Dice Generated");
            diceMessage.setHeaderText("You rolled a " + diceOne + " and a " + diceTwo);
            diceMessage.showAndWait();
            buyProperty();
        }
    }

    /***
     * Used to set the players name and tokens that they have chose to the appropriate variable
     */
    public void setPlayerInfo(){
        switch(playerSize){
            case 1:
                // Assign player ones name and tokens that they have selected
                order.add(new Player(txtP1.getText(), Token.valueOf(((String) spinP1.getValue()).toUpperCase()), gameSystem, false));
                break;
            case 2:
                order.add(new Player(txtP1.getText(), Token.valueOf(((String) spinP1.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP2.getText(), Token.valueOf(((String) spinP2.getValue()).toUpperCase()), gameSystem, false));
                break;
            case 3:
                order.add(new Player(txtP1.getText(), Token.valueOf(((String) spinP1.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP2.getText(), Token.valueOf(((String) spinP2.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP3.getText(), Token.valueOf(((String) spinP3.getValue()).toUpperCase()), gameSystem, false));
                break;
            case 4:
                order.add(new Player(txtP1.getText(), Token.valueOf(((String) spinP1.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP2.getText(), Token.valueOf(((String) spinP2.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP3.getText(), Token.valueOf(((String) spinP3.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP4.getText(), Token.valueOf(((String) spinP4.getValue()).toUpperCase()), gameSystem, false));
                break;
            case 5:
                order.add(new Player(txtP1.getText(), Token.valueOf(((String) spinP1.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP2.getText(), Token.valueOf(((String) spinP2.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP3.getText(), Token.valueOf(((String) spinP3.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP4.getText(), Token.valueOf(((String) spinP4.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP5.getText(), Token.valueOf(((String) spinP5.getValue()).toUpperCase()), gameSystem, false));
                break;
            case 6:
                order.add(new Player(txtP1.getText(), Token.valueOf(((String) spinP1.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP2.getText(), Token.valueOf(((String) spinP2.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP3.getText(), Token.valueOf(((String) spinP3.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP4.getText(), Token.valueOf(((String) spinP4.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP5.getText(), Token.valueOf(((String) spinP5.getValue()).toUpperCase()), gameSystem, false));
                order.add(new Player(txtP6.getText(), Token.valueOf(((String) spinP6.getValue()).toUpperCase()), gameSystem, false));
                break;
            default:
                break;
        }
    }

    /***
     * Used to set the ais name and tokens that they have been assigned to the appropriate variable
     */
    public void setAiInfo(){
        switch(aiSize){
            case 1:
                // Assign player ones name and tokens that they have selected
                order.add(new Player(txtAI1.getText(), Token.valueOf(((String) spinAI1.getValue()).toUpperCase()), gameSystem, true));
                break;
            case 2:
                order.add(new Player(txtAI1.getText(), Token.valueOf(((String) spinAI1.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI2.getText(), Token.valueOf(((String) spinAI2.getValue()).toUpperCase()), gameSystem, true));
                break;
            case 3:
                order.add(new Player(txtAI1.getText(), Token.valueOf(((String) spinAI1.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI2.getText(), Token.valueOf(((String) spinAI2.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI3.getText(), Token.valueOf(((String) spinAI3.getValue()).toUpperCase()), gameSystem, true));
                break;
            case 4:
                order.add(new Player(txtAI1.getText(), Token.valueOf(((String) spinAI1.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI2.getText(), Token.valueOf(((String) spinAI2.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI3.getText(), Token.valueOf(((String) spinAI3.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI4.getText(), Token.valueOf(((String) spinAI4.getValue()).toUpperCase()), gameSystem, true));
                break;
            case 5:
                order.add(new Player(txtAI1.getText(), Token.valueOf(((String) spinAI1.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI2.getText(), Token.valueOf(((String) spinAI2.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI3.getText(), Token.valueOf(((String) spinAI3.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI4.getText(), Token.valueOf(((String) spinAI4.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI5.getText(), Token.valueOf(((String) spinAI5.getValue()).toUpperCase()), gameSystem, true));
                break;
            case 6:
                order.add(new Player(txtAI1.getText(), Token.valueOf(((String) spinAI1.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI2.getText(), Token.valueOf(((String) spinAI2.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI3.getText(), Token.valueOf(((String) spinAI3.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI4.getText(), Token.valueOf(((String) spinAI4.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI5.getText(), Token.valueOf(((String) spinAI5.getValue()).toUpperCase()), gameSystem, true));
                order.add(new Player(txtAI6.getText(), Token.valueOf(((String) spinAI6.getValue()).toUpperCase()), gameSystem, true));
                break;
            default:
                break;
        }
    }

    /**
     * Switch scene from setup when "Generate Game" button is clicked
     * @param event When "generate game"" button is clicked, switch scene from setup to the game board scene
     * @throws IOException
     */
    public void setupToBoardScene(ActionEvent event) throws IOException {
        playerNumber = order.size();
        Parent gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoardScene.fxml"));
        Scene gameScene  = new Scene(gameBoardParent);
        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameScene);
        window.setFullScreen(true);
        window.show();
    }

    public void createBoard(){
        //Initiliazing the game board and it's deck's of cards
        Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
        Deque<CardEffect> opp;
        //add cards to decks

        try{
            board = Json.fromJsonToTileSet("BoardTileData.json");
        }catch (IOException e){
            e.printStackTrace();
        }

        try{
            opportunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
            potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        }catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(opportunityKnocksPack);//shuffles order
        Collections.shuffle(potLuckPack);

        pot = new ArrayDeque<>(potLuckPack);//Load shuffled pack into decks
        opp = new ArrayDeque<>(opportunityKnocksPack);

        gameSystem = new Board(order, board, pot, opp);
    }
    /**
     *  Recreate the same scene but adding a addition spinner and textfield (add spinner and textfield for the player)
     * @param event Recreate scene with the appropriate amount of spinners and textfield
     * @throws IOException
     */
    public void addNewPlayer(ActionEvent event) throws IOException {
        // Grab the code from setupScene (Add player and Add AI button)
        Parent setupParent = FXMLLoader.load(getClass().getResource("setupScene.fxml"));
        // Used to add title (label), playerSetup (spinner and textfield), setupParent (Add player and Add AI)
        VBox setup = new VBox(10);
        setup.setAlignment(Pos.TOP_CENTER);

        // Change to the generated game scene when clicked
        generateGame.setOnAction(e -> {
            // When clicked, switch to game board scene
            try {
                // Fetch and see if player chose the normal game version or abridged
                mode = gameMode.getValue().toString();
                createBoard();
                setPlayerInfo();
                setAiInfo();
                setupToBoardScene(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Increase the total player size by one
        totalPlayerSize++;
        // playerSize is used a index
        playerSize++;

        // Fetches the right amount of spinners and textfield (retrieving the Hbox from the function)
        playerAmount = addPlayerSetup();
        playerAmount.setAlignment(Pos.TOP_CENTER);
        // Create and initialize gamemode (either normal or abridge)
        aiAmount = addAISetup();
        initializeGameMode();

        if (totalPlayerSize == playerSize && playerSize < 2) {
            // Check if only players has been added
            setup.getChildren().addAll(
                    setupTitle, playerAmount,setupParent
            );
        }else if(totalPlayerSize == playerSize && playerSize != 6){
            // Check if only players has been added and if player size > 2 allow the game to start
            setup.getChildren().addAll(
                    setupTitle, playerAmount, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize == playerSize && totalPlayerSize == 6) {
            // Check if only players has been added and if player size == 6, then disable add players and AI button
            setup.getChildren().addAll(
                    setupTitle, playerAmount, gameMode, generateGame
            );
        }else if(totalPlayerSize != playerSize && totalPlayerSize < 6){
            // Check if both AIs and players has been added and allow the options to add in more players and AIs
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize != playerSize && totalPlayerSize == 6){
            // Check if AIs and players has been added and if totalPlayerSize == 6, then disable add players and AI button
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame
            );
        }
        //String bob = gameMode.getValue().toString(); //!!!! assigned to a variable later on (value converted to string atm)
        Scene setupScene  = new Scene(setup,370,420);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(setupScene);
        window.show();


    }

    /**
     *  Recreate the same scene but adding a addition spinner and textfield (add spinner and textfield for the AI)
     * @param event Recreate scene with the appropriate amount of spinners and textfield
     * @throws IOException
     */
    public void addNewAI(ActionEvent event) throws IOException {
        // Grab the code from setupScene (Add player and Add AI button)
        Parent setupParent = FXMLLoader.load(getClass().getResource("setupScene.fxml"));
        // Used to add title (label), playerSetup (spinner and textfield), setupParent (Add player and Add AI)
        VBox setup = new VBox(10);
        setup.setAlignment(Pos.TOP_CENTER);

        // Change to the generated game scene when clicked
        generateGame.setOnAction(e -> {
            // When clicked, switch to game board scene
            try {
                // Fetch and see if player chose the normal game version or abridged
                mode = gameMode.getValue().toString();
                createBoard();
                setPlayerInfo();
                setAiInfo();
                setupToBoardScene(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // Increase the total player size by one
        totalPlayerSize++;
        // aiSize is used a index
        aiSize++;

        // Fetches the right amount of spinners and textfield (retrieving the Hbox from the function)
        playerAmount = addPlayerSetup();
        aiAmount = addAISetup();
        aiAmount.setAlignment(Pos.TOP_CENTER);
        // Create and initialize gamemode (either normal or abridge)
        initializeGameMode();

        if (totalPlayerSize == aiSize && aiSize < 2) {
            setup.getChildren().addAll(
                    setupTitle, aiAmount, setupParent
            );
        }else if(totalPlayerSize == aiSize && aiSize != 6){
            setup.getChildren().addAll(
                    setupTitle, aiAmount, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize == aiSize && totalPlayerSize == 6) {
            setup.getChildren().addAll(
                    setupTitle, aiAmount, gameMode, generateGame
            );
        }else if(totalPlayerSize != aiSize && totalPlayerSize < 6){
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame, setupParent
            );
        }else if(totalPlayerSize != playerSize && totalPlayerSize == 6){
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame
            );
        }
        Scene setupScene  = new Scene(setup,370,420);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(setupScene);
        window.show();
    }

    /***
     * @return VBox which return the appropriate amount of spinners and textfield for all the players
     */
    public VBox addPlayerSetup() {
        // Initialize players spinners and textfield
        initializePlayer();

        // Used to add token choice (spinner) and players name (text field)
        // Uses a different Hbox depending on the number of players
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

        // Making a list of tokens that the user could choose from
        ObservableList<String> tokenName = FXCollections.observableArrayList(
                "Boot", "Smartphone", "Goblet","Hatstand","Cat","Spoon"
        );

        switch(playerSize) {
            case 1:
                SpinnerValueFactory<String> tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);
                playerSetup1.getChildren().addAll(spinP1, txtP1);
                alignPlayer.getChildren().add(playerSetup1);
                break;
            case 2:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinP1.setValueFactory(tokenChoice1);

                SpinnerValueFactory<String> tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinP2.setValueFactory(tokenChoice2);

                playerSetup1.getChildren().addAll(spinP1, txtP1);
                playerSetup2.getChildren().addAll(spinP2, txtP2);
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

                playerSetup1.getChildren().addAll(spinP1, txtP1);
                playerSetup2.getChildren().addAll(spinP2, txtP2);
                playerSetup3.getChildren().addAll(spinP3, txtP3);
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

                playerSetup1.getChildren().addAll(spinP1, txtP1);
                playerSetup2.getChildren().addAll(spinP2, txtP2);
                playerSetup3.getChildren().addAll(spinP3, txtP3);
                playerSetup4.getChildren().addAll(spinP4, txtP4);
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

                playerSetup1.getChildren().addAll(spinP1, txtP1);
                playerSetup2.getChildren().addAll(spinP2, txtP2);
                playerSetup3.getChildren().addAll(spinP3, txtP3);
                playerSetup4.getChildren().addAll(spinP4, txtP4);
                playerSetup5.getChildren().addAll(spinP5, txtP5);
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

                playerSetup1.getChildren().addAll(spinP1, txtP1);
                playerSetup2.getChildren().addAll(spinP2, txtP2);
                playerSetup3.getChildren().addAll(spinP3, txtP3);
                playerSetup4.getChildren().addAll(spinP4, txtP4);
                playerSetup5.getChildren().addAll(spinP5, txtP5);
                playerSetup6.getChildren().addAll(spinP6, txtP6);
                alignPlayer.getChildren().addAll(
                        playerSetup1,playerSetup2,playerSetup3,playerSetup4,playerSetup5,playerSetup6
                );
                break;
        }
        return alignPlayer;
    }

    /***
     *
     * @return VBox which return the appropriate amount of spinners and textfield for all the AIs
     */
    public VBox addAISetup() {
        // Generate a random seed for the AIs name
        if(aiSize == 1){
            Random rand = new Random();
            randomSeed = rand.nextInt();
        }
        // Initialize AIs spinners and textfield
        initializeAI(randomSeed);

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

        // Making a list of tokens that the user could choose from
        ObservableList<String> tokenName = FXCollections.observableArrayList(
                "Boot", "Smartphone", "Goblet","Hatstand","Cat","Spoon"
        );

        switch(aiSize) {
            case 1:
                SpinnerValueFactory<String> tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);
                // Prevent players from changing the AI's name
                txtAI1.setEditable(false);

                aiSetup1.getChildren().addAll(spinAI1, txtAI1);
                alignAI.getChildren().add(aiSetup1);
                break;
            case 2:
                tokenChoice1 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice1.setValue("Boot");
                spinAI1.setValueFactory(tokenChoice1);

                SpinnerValueFactory<String> tokenChoice2 = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
                tokenChoice2.setValue("Boot");
                spinAI2.setValueFactory(tokenChoice2);
                // Prevent players from changing the AI's name
                txtAI1.setEditable(false);
                txtAI2.setEditable(false);

                aiSetup1.getChildren().addAll(spinAI1, txtAI1);
                aiSetup2.getChildren().addAll(spinAI2, txtAI2);
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
                // Prevent players from changing the AI's name
                txtAI1.setEditable(false);
                txtAI2.setEditable(false);
                txtAI3.setEditable(false);

                aiSetup1.getChildren().addAll(spinAI1, txtAI1);
                aiSetup2.getChildren().addAll(spinAI2, txtAI2);
                aiSetup3.getChildren().addAll(spinAI3, txtAI3);
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
                // Prevent players from changing the AI's name
                txtAI1.setEditable(false);
                txtAI2.setEditable(false);
                txtAI3.setEditable(false);
                txtAI4.setEditable(false);

                aiSetup1.getChildren().addAll(spinAI1, txtAI1);
                aiSetup2.getChildren().addAll(spinAI2, txtAI2);
                aiSetup3.getChildren().addAll(spinAI3, txtAI3);
                aiSetup4.getChildren().addAll(spinAI4, txtAI4);
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
                // Prevent players from changing the AI's name
                txtAI1.setEditable(false);
                txtAI2.setEditable(false);
                txtAI3.setEditable(false);
                txtAI4.setEditable(false);
                txtAI5.setEditable(false);

                aiSetup1.getChildren().addAll(spinAI1, txtAI1);
                aiSetup2.getChildren().addAll(spinAI2, txtAI2);
                aiSetup3.getChildren().addAll(spinAI3, txtAI3);
                aiSetup4.getChildren().addAll(spinAI4, txtAI4);
                aiSetup5.getChildren().addAll(spinAI5, txtAI5);
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
                // Prevent players from changing the AI's name
                txtAI1.setEditable(false);
                txtAI2.setEditable(false);
                txtAI3.setEditable(false);
                txtAI4.setEditable(false);
                txtAI5.setEditable(false);
                txtAI6.setEditable(false);

                aiSetup1.getChildren().addAll(spinAI1, txtAI1);
                aiSetup2.getChildren().addAll(spinAI2, txtAI2);
                aiSetup3.getChildren().addAll(spinAI3, txtAI3);
                aiSetup4.getChildren().addAll(spinAI4, txtAI4);
                aiSetup5.getChildren().addAll(spinAI5, txtAI5);
                aiSetup6.getChildren().addAll(spinAI6, txtAI6);
                alignAI.getChildren().addAll(
                        aiSetup1,aiSetup2,aiSetup3,aiSetup4,aiSetup5,aiSetup6
                );
                break;
        }
        return alignAI;
    }

    /***
     * Initialize combo box with the elements normal and abridged but default value to normal game mode
     */
    public void initializeGameMode(){
        gameMode = new ComboBox();
        // Setting up the choice box so players can select the game mode either normal or abridged
        gameMode.getItems().addAll("Normal", "Abridged");
        gameMode.setPrefWidth(170);
        gameMode.setPrefHeight(30);
        // Setting the default value for game mode to be normal
        gameMode.setValue("Normal");
    }

    /***
     * Initialize players spinner and textfield with a default value
     */
    public void initializePlayer(){
        // Initializing players' spinner and textfield
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP1.setStyle("-fx-font-size: 14px");
        txtP1.setPromptText("Enter players' 1 name");

        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP2.setStyle("-fx-font-size: 14px");
        txtP2.setPromptText("Enter players' 2 name");

        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP3.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP3.setStyle("-fx-font-size: 14px");
        txtP3.setPromptText("Enter players' 3 name");

        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP4.setStyle("-fx-font-size: 14px");
        txtP4.setPromptText("Enter players' 4 name");

        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP5.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP5.setStyle("-fx-font-size: 14px");
        txtP5.setPromptText("Enter players' 5 name");

        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP6.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP6.setStyle("-fx-font-size: 14px");
        txtP6.setPromptText("Enter players' 6 name");
    }

    /***
     * Initialize AIs spinner and textfield with a default value
     */
    public void initializeAI(int randomSeed){
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
        rand.setSeed(randomSeed);
        int randomIndex = rand.nextInt(nameAI.size());
        String tempName = nameAI.get(randomIndex);

        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI1.setStyle("-fx-font-size: 14px");
        txtAI1.setText(tempName);
        // Remove the AI's name that been assigned so no duplicate name at one time
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI2.setStyle("-fx-font-size: 14px");
        txtAI2.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI3.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI3.setStyle("-fx-font-size: 14px");
        txtAI3.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI4.setStyle("-fx-font-size: 14px");
        txtAI4.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI5.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI5.setStyle("-fx-font-size: 14px");
        txtAI5.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI6.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI6.setStyle("-fx-font-size: 14px");
        txtAI6.setText(tempName);
        nameAI.remove(randomIndex);
    }

    /**
     * @param event When "start" button is clicked, switch scene from main menu to the setup scene
     */
    public void mainToSetupScene(ActionEvent event) throws IOException {
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
     * @param event When "main menu" button is clicked, switch scene from rules to main menu
     */
    public void ruleToMainScene(ActionEvent event) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene mainScene  = new Scene(mainParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    /**
     * @param event When "rules" button is clicked, switch scene from main menu to rules
     */
    public void mainToRuleScene(ActionEvent event) throws IOException {
        Parent ruleParent = FXMLLoader.load(getClass().getResource("ruleScene.fxml"));
        Scene ruleScene  = new Scene(ruleParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(ruleScene);
        window.show();
    }

    /**
     * @param event When "exit" button is clicked, exit application
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
        String txtMsg = "";
        ObservableList listOfProps = player1Props.getSelectionModel().getSelectedItems();
        for(Object item: listOfProps){
            txtMsg += String.format("%s%n", (String) item);
        }
        testMsg.setHeaderText("You've selected: " + txtMsg);
        testMsg.show();
    }

    public void player2TradeConfirm(ActionEvent event){
        Alert testMsg = new Alert(Alert.AlertType.INFORMATION);
        testMsg.setTitle(player2.getText());
        String txtMsg = "";
        ObservableList listOfProps = player2Props.getSelectionModel().getSelectedItems();
        for(Object item: listOfProps){
            txtMsg += String.format("%s%n", (String) item);
        }
        testMsg.setHeaderText("You've selected: " + txtMsg);
        testMsg.show();
    }

    public void rollDice(ActionEvent event) throws IOException{
        Random rand = new Random();
        diceRoll.setText("21");
        System.out.println(order.getFirst().getName());
        curPlayerLbl.setText(order.getFirst().getName());

    }
}
