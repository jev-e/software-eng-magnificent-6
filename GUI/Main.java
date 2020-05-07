package GUI;

import ClassStructure.*;
import javafx.application.Application;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

/**
 * GUI Main Executable, Handles all GUI Displaying
 *
 * @author Callum Crawford, Danny Che
 */
public class Main extends Application {

    // Variables
    private Label[] tiles;
    private FlowPane[] playerCards;
    private int playerNo = 6; // Temp variable to simulate number of players TODO Update to actual player number

    // Scenes
    private Scene gameScene; // Shows gameBP

    // BorderPanes
    private BorderPane gameBP; // Holds entire game screen

    // GridPanes
    private GridPane boardGP;    // Main Board, Left side of screen

    private GridPane buttonGP;   // Bottom GridPane for game buttons

    private GridPane topRowGP;   // Top 9 tiles container (Not the top two corners)
    private GridPane leftColGP;  // Left 9 tiles container (Not the Corners)
    private GridPane rightColGP; // Right 9 tiles container (Not the corners)
    private GridPane botRowGP;   // Bottom 9 tiles container (Not the corners)

    // VBoxes

    private VBox playersVB; // Player cards, Right side of Screen

    Stage window;
    Scene menuScene, ruleScene, playerSetupScene, gameSetupScene, gameBoardScene, tradingSetupScene, tradingScene;

    // Holds the players name
    public ArrayList<TextField> playerNameTextField = new ArrayList<>();
    public ArrayList<TextField> aiNameTextField = new ArrayList<>();
    // Holds the players tokens
    public ArrayList<Spinner> playerTokenSpin = new ArrayList<>();
    public ArrayList<Spinner> aiTokenSpin = new ArrayList<>();

    // Game board data
    private static HashMap<Integer, BoardTile> board = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();
    private static List<CardEffect> opportunityKnocksPack;
    private static List<CardEffect> potLuckPack;
    private Board gameSystem;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Property Tycoon");

        createMainMenuScene();
        window.setScene(menuScene);
        window.show();
        //displayGameScene();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Calls Key Functions to display the game, sets gameScene active
     */
    public void displayGameScene() {
        initGameVariables();  // Initalise Variables and Containers
        addKeyGameButtons();  // Adds Roll, Property Management, Quit and Trading buttons
        displayPlayerCards(); // Displays player cards to the gameBP
        displayGameBoard();   // Displays main game board


        // Show Game Screen
        window.setScene(gameScene);
        window.show();
    }

    public void addKeyGameButtons() {
        // Initialise Buttons
        Button rollBtn = new Button("Roll Dice");
        Button propManageBtn = new Button("Manage Properties");
        Button tradeBtn = new Button("Trade");
        Button quitBtn = new Button("Quit");

        // Set Button Actions
        rollBtn.setOnAction((ActionEvent event) -> {
            // Roll Dice
            System.out.println("Roll Button Clicked");
        });

        propManageBtn.setOnAction((ActionEvent event) -> {

        });

        tradeBtn.setOnAction((ActionEvent event) -> {

        });

        quitBtn.setOnAction((ActionEvent event) -> {

        });

        // Add buttons to GridPane
        buttonGP.add(rollBtn, 0,0);
        buttonGP.add(propManageBtn, 1, 0);
        buttonGP.add(tradeBtn, 2, 0);
        buttonGP.add(quitBtn, 3, 0);

        // Set Button's start state (Visible or Not Visible
        //propManageBtn.setVisible(false);
        //tradeBtn.setVisible(false);
    }

    /**
     * Displays Board to primaryStage
     */
    public void displayGameBoard() {

        int shortSide = 60, longSide = 100;
        for (int i = 0; i < 40; i++) {

            String labelValue = String.valueOf(i);
            Label label = new Label("Tile: " + labelValue);

            // Add Tiles to Gridpane
            switch (i) {

                // Go Tile
                case 0:
                    boardGP.add(label, 2, 2);
                    break;

                // Bottom Row
                case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9:
                    botRowGP.add(label, (9 - i), 0);
                    break;

                // Jail Tile
                case 10:
                    boardGP.add(label, 0,2);
                    break;

                // Left Column
                case 11: case 12: case 13: case 14: case 15: case 16: case 17: case 18: case 19:
                    leftColGP.add(label, 0, (20 - i));
                    break;

                // Free Parking Tile
                case 20:
                    boardGP.add(label, 0, 0);
                    break;

                // Top Row
                case 21: case 22: case 23: case 24: case 25: case 26: case 27: case 28: case 29:
                    topRowGP.add(label, i, 0);
                    break;

                // Go To Jail Tile
                case 30:
                    boardGP.add(label, 2,0);
                    break;

                // Right Column
                case 31: case 32: case 33: case 34: case 35: case 36: case 37: case 38: case 39:
                    rightColGP.add(label, 0, i);
                    break;

                default:

                    break;
            }

            tiles[i] = label;
        }
        tiles[0].setText("Go Tile");
        tiles[10].setText("Jail Tile");
    }

    /**
     * Display Player Cards to primaryStage
     */
    public void displayPlayerCards() {


        for(int i = 0; i < playerNo; i++) {
            FlowPane playerPane = new FlowPane();
            Label playerName = new Label("Player: " + String.valueOf(i + 1));

            playerPane.getChildren().add(playerName);

            playerCards[i] = playerPane;

            playersVB.getChildren().add(playerPane);
        }
    }

    /**
     * Initialises all Panes and Variables for Game Scene
     * Formats gameGP and buttonGP
     */
    public void initGameVariables() {

        // Initialise main Game containers
        gameBP = new BorderPane();
        boardGP = new GridPane();
        buttonGP = new GridPane();
        playersVB = new VBox();

        // Initialise Scene
        gameScene = new Scene(gameBP);

        // Add Panes to gameBP
        gameBP.setLeft(boardGP);
        gameBP.setRight(playersVB);
        gameBP.setBottom(buttonGP);

        // Initialise inner containers
        topRowGP = new GridPane();
        leftColGP = new GridPane();
        rightColGP = new GridPane();
        botRowGP = new GridPane();

        // Initialise formatting
        Insets insets = new Insets(10);
        Label label = new Label();

        // Format GameBP
        gameBP.setCenter(label);
        gameBP.setMargin(label, insets);
        gameBP.setMargin(buttonGP, insets);

        // Format buttonGP
        buttonGP.setHgap(10);

        // Add inner containers to boardGP
        boardGP.add(topRowGP, 1, 0);
        boardGP.add(leftColGP, 0, 1);
        boardGP.add(rightColGP, 2,1);
        boardGP.add(botRowGP,1,2);

        // Initialise Variables
        tiles = new Label[40];
        playerCards = new FlowPane[playerNo];
    }


    /***
     * A main menu scene which contains a label, start button, rule button and a quit button
     * Label (Title of the game), start button (switch to next scene), rule button (switch to rule scene) and quit
     */
    private void createMainMenuScene(){
        // Create a VBox with a padding of 10 which contains the main menu scene
        VBox menuPane = new VBox(10);
        menuPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Game");
        Button start = new Button("Start");
        Button rule = new Button("Rules");
        Button quit = new Button("Quit");

        // CSS  for a Label and Buttons
        title.setStyle(
                // Top, Right, Bottom and Left for padding in Css
                "-fx-label-padding: 20 0 20 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );

        start.setPrefSize(110,30);
        start.setStyle(
                "-fx-font-size:14;" + "-fx-font-weight: bold;"
        );

        rule.setPrefSize(110,30);
        rule.setStyle(
                "-fx-font-size:14;" + "-fx-font-weight: bold;"
        );

        quit.setPrefSize(110,30);
        quit.setStyle(
                "-fx-font-size:14;" + "-fx-font-weight: bold;"
        );

        // Button functionality
        start.setOnAction(e -> {
            createPlayerSetupScene();
            window.setScene(playerSetupScene);
            window.show();
        });

        rule.setOnAction(e -> {
            createRuleScene();
            window.setScene(ruleScene);
            window.show();
        });

        quit.setOnAction(e -> Platform.exit());

        menuPane.getChildren().addAll(title, start, rule, quit);
        menuScene = new Scene(menuPane,500,500);
    }

    /***
     * A rule scene which contains a 2 labels, 3 buttons
     * Label (Rule title), next label (text of all of the rules), start button (switch to next scene),
     * Main menu button (go back to the last scene) and quit
     */
    public void createRuleScene(){
        // rulePane would hold a label, ruleTextPane and optionPane
        VBox rulePane = new VBox(10);
        rulePane.setAlignment(Pos.CENTER);
        // Contains 3 labels which is used to display the rules of the game
        HBox ruleTextPane = new HBox(5);
        ruleTextPane.setAlignment(Pos.CENTER);
        // optionPane will contain three buttons; main menu, start, quit
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Rules");
        Label ruleText1 = new Label();
        Label ruleText2 = new Label();
        Label ruleText3 = new Label();
        Button start = new Button("Start");
        Button mainMenu = new Button("Main Menu");
        Button quit = new Button("Quit");

        // CSS
        title.setStyle(
                // Top, Right, Bottom and Left for padding in Css
                "-fx-padding: 10 0 5 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );

        ruleText1.setMaxWidth(350);
        ruleText1.setStyle("-fx-border-color: #000000;" + "-fx-label-padding: 5 5 5 5;");
        ruleText1.setText("Rule 1: Each player is assigned a token, boot, smartphone,\ngoblet, hatstand, cat and spoon\n" +
                "Rule 2: All players start on Go at beginning of game\n" +
                "Rule 3: At the start of the game, players have £1500\n" +
                "Rule 4: If a player lands on free parking, they collect all the\nmoney in the free parking space\n" +
                "Rule 5: A player is only allowed to leave the game with\neveryone else’s consent\n" +
                "Rule 6: Players move around the board by rolling 2 dice and\nmoving the appropriate squares based off the total dice roll\n" +
                "Rule 7: Players can purchase property when they land on it.\nPlayers cannot purchase property until they have passed go\nat least once\n" +
                "Rule 8: If a player decides not to buy a property, it is put\nup for auction. Each player optionally makes a bid and the\nproperty is sold to the highest bidder. If no players make a bid,\nthe property remains unsold\n" +
                "Rule 9: If a player lands on the pot luck or opportunity knocks\nspace they must draw a card from the appropriate pile and\ncarry out the instructions on the card, the card is then replaced\nat the bottom of the pile\n" +
                "Rule 10: If a player lands on a property owned by another player,\nthey must pay that player the rent value listed on the\nproperty card\n" +
                "Rule 11: If a player owns all the properties in a colour group\nand the properties are not developed, then the rent value\ndoubles\n"
        );

        ruleText2.setMaxWidth(350);
        //ruleText2.setTextAlignment(TextAlignment.JUSTIFY);
        ruleText2.setStyle("-fx-border-color: #000000;" + "-fx-label-padding: 5 5 5 5;");
        ruleText2.setText("Rule 12: A property can be improved with houses and hotels,\nthis increases the rent amount on the card. A property can only\nbe improved at the end of a players turn and can only be\nbought when a player owns all the cards in a colour coded\ngroup. Houses and hotels can be bought for the value given\non the property card. A hotel is worth 5 times the purchase\ncost of a house.\n" +
                "Rule 13: Every property in the set must have the same number\nof improvements +1 or -1. A player may have 4 houses on one\nproperty and a hotel on the other. Each property can only have\none hotel.\n" +
                "Rule 14: A property can only be owned by one person\n" +
                "Rule 15: When a player passes the Go tile, they collect £200\n" +
                "Rule 16: If both dice rolls are the same number, then the player\ncan take another turn\n" +
                "Rule 17: On the third turn in a row, a double being rolled\nmeans that the player goes to jail. They do not pass Go or\ncollect £200\n" +
                "Rule 18: Players can sell hotels and houses off properties they\nown at the same cost of buying them\n" +
                "Rule 19: Players can mortgage properties for half of the\nproperties’ value as long as there are no houses or hotels built\non them\n" +
                "Rule 20: Mortgaged properties are still owned by the player\nbut do not collect rent when other players\nland on the property\n\n"
        );
        ruleText3.setMaxWidth(350);
        ruleText3.setStyle("-fx-border-color: #000000;" + "-fx-label-padding: 5 5 5 5;");
        ruleText3.setText("Rule 21: Players can un-mortgage properties they own for the\nsame amount it cost to mortgage them\n" +
                "Rule 22: If a player goes to jail they may pay £50 to be released.\nThe £50 is added to free parking fund. The player is moved to\n‘just visiting’ and takes a normal turn next go round. If a\nplayer doesn’t pay the £50 they give up their turn for the next\n2 rounds. They do not collect any rent whilst in jail. At the end\nof the 2 rounds they moved to just visiting and take a normal\nturn next go round. A player can also use a get out of jail free\ncard to move to just visiting. The card is placed at the bottom\nof the appropriate pile\n" +
                "Rule 23: If a player cannot pay rent or a fine, they must sell\nassets to be able to pay. A player can sell houses and hotels\nback to the bank for the original purchase price. Properties can\nbe sold back to the bank for the original value shown on the\ncard if there are no improvements remaining. If the player is still\nunable to pay the rent then they are bankrupt. The player can\nalso mortgage properties back to the bank for half the\npurchase cost of the property. No rent can be collected whilst\nthe property is mortgaged. If the mortgaged property is sold\nback to the bank it is bought for half the purchase price. A\nproperty cannot be mortgaged if it has properties on it\n" +
                "Rule 24: Once a player is bankrupt, they are no longer allowed\nto continue playing the game\n" +
                "Rule 25: When fines are paid, the proceeds are ‘stored’ in the\nfree parking space in the centre of the board\n\n"
        );

        start.setPrefSize(110,30);
        start.setStyle("-fx-font-size:14;");

        mainMenu.setPrefSize(110,30);
        mainMenu.setStyle("-fx-font-size:14;");

        quit.setPrefSize(110,30);
        quit.setStyle("-fx-font-size:14;");

        // Button functionality
        start.setOnAction(e -> {
            createPlayerSetupScene();
            window.setScene(playerSetupScene);
            window.show();
        });

        mainMenu.setOnAction(e -> {
            window.setScene(menuScene);
            window.show();
        });

        quit.setOnAction(e -> Platform.exit());

        ruleTextPane.getChildren().addAll(ruleText1, ruleText2, ruleText3);
        optionPane.getChildren().addAll(start, mainMenu, quit);
        rulePane.getChildren().addAll(title, ruleTextPane, optionPane);
        ruleScene = new Scene(rulePane,1100,600);
    }

    /***
     * A setup scene which contains a 3 labels, 2 combo box, 3 buttons
     * Label (Setup title), next label (number of players), last label (number of AIs)
     * ComboBox drop down box which contains how many players or AIs u can add to the game
     */
    public void createPlayerSetupScene(){
        VBox playerPane = new VBox(15);
        playerPane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Setup");
        Label numberOfPlayerLabel = new Label("Number of players");
        Label numberOfAILabel = new Label("Number of AIs ");
        Label gameModeLabel = new Label("Select the desired game mode");

        // Drop down box for user to select how many players and AIs (default value = 1 player, 1 AI)
        ComboBox playerNumber = new ComboBox();
        playerNumber.setPrefWidth(100);
        playerNumber.getItems().addAll("0", "1", "2", "3", "4", "5", "6");
        playerNumber.setValue("1");

        ComboBox AINumber = new ComboBox();
        AINumber.setPrefWidth(100);
        AINumber.getItems().addAll("0", "1", "2", "3", "4", "5", "6");
        // Setting the default value for the combo box to have 1 player and 1 AI
        AINumber.setValue("1");

        // Drop down box for game mode (full or abridged)
        ComboBox gameMode = new ComboBox();
        // Setting up the choice box so players can select the game mode either full or abridged
        gameMode.getItems().addAll("Full", "Abridged");
        gameMode.setPrefWidth(100);
        // Setting the default value for game mode to be full
        gameMode.setValue("Full");

        Button nextButton = new Button("Next");
        Button rule = new Button("Rules");
        Button quit = new Button("Quit");

        // CSS
        title.setStyle(
                // Top, Right, Bottom and Left for padding in Css
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );

        numberOfPlayerLabel.setStyle("-fx-font-size: 15;");
        numberOfAILabel.setStyle("-fx-font-size: 15;");
        gameModeLabel.setStyle("-fx-font-size: 15;");

        nextButton .setPrefSize(110,30);
        nextButton .setStyle("-fx-font-size:14;");

        rule.setPrefSize(110,30);
        rule.setStyle("-fx-font-size:14;");

        quit.setPrefSize(110,30);
        quit.setStyle("-fx-font-size:14;");

        // Button Functionality
        nextButton.setOnAction(e -> {
            // Fetch value from the player and ai combo box and converting into a integer
            int tempPlayerNum = Integer.valueOf(playerNumber.getValue().toString());
            int tempAINum = Integer.valueOf(AINumber.getValue().toString());
            // Fetch the game mode value from combo box
            String mode = gameMode.getValue().toString();
            // Error message for player constraint
            Alert exceedPlayerMessage = new Alert(Alert.AlertType.WARNING);
            // Maximum and minimum player constraint implemented
            if(tempPlayerNum + tempAINum <= 6 && tempPlayerNum + tempAINum >= 2){
                createGameSetupScene(tempPlayerNum, tempAINum, mode);
                window.setScene(gameSetupScene);
                window.show();
            }else if(tempPlayerNum + tempAINum < 2){
                // Player amount did not exceeded 2 (player and AI combined)
                exceedPlayerMessage.setTitle("Property Tycoon Setup");
                exceedPlayerMessage.setHeaderText("Player amount was not exceeded");
                exceedPlayerMessage.setContentText("Please ensure that there is a minimum of 2 players in total (Players and AIs combined)");
                exceedPlayerMessage.showAndWait();
            }else{
                // Player amount exceeded 6 (player and AI combined)
                exceedPlayerMessage.setTitle("Property Tycoon Setup");
                exceedPlayerMessage.setHeaderText("Player amount exceeded");
                exceedPlayerMessage.setContentText("Please ensure that there is a maximum of 6 players in total (Players and AIs combined)");
                exceedPlayerMessage.showAndWait();
            }
        });

        rule.setOnAction(e -> {
            createRuleScene();
            window.setScene(ruleScene);
            window.show();
        });

        quit.setOnAction(e -> Platform.exit());

        optionPane.getChildren().addAll(nextButton, rule, quit);
        playerPane.getChildren().addAll(title, numberOfPlayerLabel, playerNumber, numberOfAILabel, AINumber, gameModeLabel, gameMode, optionPane);
        playerSetupScene = new Scene(playerPane, 500, 500);
    }

    /***
     * Create a set of spinners and textfield per player/AI
     * Spinners (tokens that the player could choose from)
     * Textfield (placeholder to show AIs name or allow the player to set their own name)
     * @param playerSize Number of player size
     * @param aiSize Number of AIs
     * @param gameMode A string which represent the game mode that the user selected
     */
    public void createGameSetupScene(int playerSize, int aiSize, String gameMode){
        VBox gameSetupPane = new VBox(10);
        gameSetupPane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        // A array-list containing all the possible name for the AI (randomly assigned)
        ArrayList<String> nameAI = new ArrayList<String>(); // Contains 6 names (possibly 6 AIs' playing)
        nameAI.add("Bob");
        nameAI.add("Fred");
        nameAI.add("Alex");
        nameAI.add("Jess");
        nameAI.add("Annie");
        nameAI.add("Adel");

        Random rand = new Random();

        Label title = new Label("Property Tycoon Player Setup");
        Label mode = new Label("Game mode currently selected: " + gameMode);

        Button generateGame = new Button("Generate Game");
        Button backToPlayerSetup = new Button("Back");
        Button quit = new Button("Quit");

        // CSS
        title.setStyle(
                // Top, Right, Bottom and Left for padding in CSS
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        mode.setStyle(
                "-fx-label-padding: 10 0 5 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        generateGame.setPrefSize(130,30);
        generateGame.setStyle("-fx-font-size:14;");

        backToPlayerSetup.setPrefSize(130,30);
        backToPlayerSetup.setStyle("-fx-font-size:14;");

        quit.setPrefSize(130,30);
        quit.setStyle("-fx-font-size:14;");

        gameSetupPane.getChildren().addAll(title, mode);
        // Create the right amount of spinners and textfield for each player
        for(int i = 0; i < playerSize; i++){
            // HBox container which contains a spinner and textfield per player
            HBox spinnerTextfieldPane = new HBox(10);
            spinnerTextfieldPane.setAlignment(Pos.CENTER);

            TextField playerName = new TextField();
            playerName.setStyle("-fx-font-size: 14px");
            playerName.setPromptText("Enter players' " + (i+1) + " name");
            playerNameTextField.add(playerName);

            Spinner token = new Spinner<>();
            token.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            // Creating a list of tokens that the player could choose from
            ObservableList<String> tokenName = FXCollections.observableArrayList(
                    "Boot", "Smartphone", "Goblet","Hatstand","Cat","Spoon"
            );
            // Setting the default value for all spinners for all the players to be boot
            SpinnerValueFactory<String> tokenChoice = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
            tokenChoice.setValue("Boot");
            token.setValueFactory(tokenChoice);
            playerTokenSpin.add(token);

            spinnerTextfieldPane.getChildren().addAll(token, playerName);
            gameSetupPane.getChildren().addAll(spinnerTextfieldPane);
        }

        // Create the right amount of spinners and textfield for each AIs
        for(int j = 0; j < aiSize; j++){
            // HBox container which contains a spinner and textfield per player
            HBox spinnerTextfieldPane = new HBox(10);
            spinnerTextfieldPane.setAlignment(Pos.CENTER);

            // Assign a random name from the list to the AIs textfield
            int randomIndex = rand.nextInt(nameAI.size());
            String tempName = nameAI.get(randomIndex);

            TextField aiName = new TextField();
            aiName.setStyle("-fx-font-size: 14px");
            aiName.setText(tempName);
            aiName.setEditable(false);
            nameAI.remove(randomIndex);
            aiNameTextField.add(aiName);

            Spinner token = new Spinner<>();
            token.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            // Creating a list of tokens that the player could choose from
            ObservableList<String> tokenName = FXCollections.observableArrayList(
                    "Boot", "Smartphone", "Goblet","Hatstand","Cat","Spoon"
            );
            // Setting the default value for all spinners for all the AIs to be boot
            SpinnerValueFactory<String> tokenChoice = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
            tokenChoice.setValue("Boot");
            token.setValueFactory(tokenChoice);
            aiTokenSpin.add(token);

            spinnerTextfieldPane.getChildren().addAll(token, aiName);
            gameSetupPane.getChildren().addAll(spinnerTextfieldPane);
        }

        // Button functionality
        generateGame.setOnAction(e -> {
            // nameTest = true if textfield is not null else some or all text fields is null or " "
            boolean nameTest = nameConstraintCheck();
            // tokenTest = true if constraint is met else false if constraints is not met
            boolean tokenTest = tokenConstraintCheck();
            // Change scene if tokenTest = true, else Alert pops up
            constraintCheck(nameTest, tokenTest, gameMode.toLowerCase());
        });

        backToPlayerSetup.setOnAction(e -> {
            // Reset the values when back button is clicked
            playerNameTextField = new ArrayList<>();
            aiNameTextField = new ArrayList<>();
            playerTokenSpin = new ArrayList<>();
            aiTokenSpin = new ArrayList<>();
            window.setScene(playerSetupScene);
            window.show();
        });

        quit.setOnAction(e -> Platform.exit());

        optionPane.getChildren().addAll(generateGame, backToPlayerSetup, quit);
        gameSetupPane.getChildren().add(optionPane);
        gameSetupScene = new Scene(gameSetupPane, 500, 500);
    }
    /***
     * Check to see that all textfield is not null or containing just " "
     * @return A boolean value (true = no null textfield, false = textfield is either null or containing only " ")
     */
    public boolean nameConstraintCheck(){
        // True = all textfield is not null, else some/all textfield is " " or empty
        boolean nameCheck = true; // Default to true (if game = all AI)
        int i = 0;

        // Fetch the name value from each textfield
        while(i < playerNameTextField.size()){
            String playerName = playerNameTextField.get(i).getText();

            // Check if the textfield is empty, containing only " ", or null
            if(playerName == null || playerNameTextField.get(i).getText().trim().isEmpty()){
                // Name constraint failed
                nameCheck = false;
                break;
            }else{
                // Name constraint passed
                nameCheck = true;
            }
            i++;
        }
        return nameCheck;
    }

    /***
     * Check to see that no duplicate token has been chosen
     * @return A boolean value (true = no duplicate tokens found, false = duplicate tokens found amongst the players and AI)
     */
    public boolean tokenConstraintCheck(){
        // true = pass constraint check, else failed constraint check
        boolean tokenCheck;
        // arrayList which holds all the tokens that the player and AI currently has chosen
        ArrayList<String> allToken = new ArrayList<>();

        // Fetch a spinner from the player list and assigned to playerToken
        for(int i = 0; i < playerTokenSpin.size(); i++){
            // Fetching the tokens that was assigned to each spinner (player)
            String playerToken = playerTokenSpin.get(i).getValue().toString();
            allToken.add(playerToken);
        }

        // Fetch a spinner from the AI list and assigned to aiToken
        for(int j = 0; j < aiTokenSpin.size(); j++){
            // Fetching the tokens that was assigned to each spinner (AI)
            String aiToken = aiTokenSpin.get(j).getValue().toString();
            allToken.add(aiToken);
        }

        // Add all the tokens to a into tokenConstraint (tokenConstraint is a datatype of set meaning no duplicates)
        Set<String> tokenConstraint = new HashSet<String>(allToken);

        // No duplicate token found if the size of both allToken and tokenConstraint is the same
        if(allToken.size() == tokenConstraint.size()){
            tokenCheck = true;
        }else{
            // Duplicate token found (size of allToken and tokenConstraint is different)
            tokenCheck = false;
        }
        return tokenCheck;
    }

    /***
     * Check and see if it has passed all of the constraint test (false = error message, true = switch to board scene)
     * @param nameTest A boolean value specifying if all of the name assigned has met the condition to switch scene
     * @param tokenTest A boolean value specifying if the tokens assigned has met the condition to switch scene
     * @param gameMode A string which dictates which game mode they will be playing
     */
    public void constraintCheck(boolean nameTest, boolean tokenTest, String gameMode){
        Alert tokenError = new Alert(Alert.AlertType.ERROR);
        if(nameTest == false){
            tokenError.setTitle("Name Assignment Error");
            tokenError.setHeaderText("Name Constraint");
            tokenError.setContentText("Please enter in a name for all of the players");
            tokenError.showAndWait();
        }else if(tokenTest == false){
            tokenError.setTitle("Token Assignment Error");
            tokenError.setHeaderText("Token Constraint");
            tokenError.setContentText("Please ensure all players and AIs has a unique token");
            tokenError.showAndWait();
        }else{
            // Initialize board and the pack of cards
            createBoard(gameMode);
            // Assign player to the turn order
            assignPlayerToTurnOrder();

            // Testing  dannnny !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // TODO change after testing
            tradingSetupScene(order.getFirst());
            window.setScene(tradingSetupScene);
            window.show();

            // Change scene
//            displayGame();
//            window.setScene(gameScene);
//            gameLoop();
//            window.show();
        }
    }

    /***
     * Create a new player with their token and name then assigning it to the turn order if playerSize >= 1
     * Also creating a new player with their token and name then assigning it to the turn order if aiSize >= 1
     */
    public void assignPlayerToTurnOrder(){
        // Add player to the turn order if playerSize >= 1
        for(int i = 0; i < playerNameTextField.size(); i++){
            // Creating a new player and assigning it to turn order
            Player player = new Player(playerNameTextField.get(i).getText(), Token.valueOf(playerTokenSpin.get(i).getValue().toString().toUpperCase()), gameSystem,false);
            order.add(player);
        }

        // Add AI to the turn order if aiSize >= 1
        for(int j = 0; j < aiNameTextField.size(); j++){
            // Creating a new player and assigning it to turn order
            Player ai = new Player(aiNameTextField.get(j).getText(), Token.valueOf(aiTokenSpin.get(j).getValue().toString().toUpperCase()), gameSystem,true);
            order.add(ai);
        }
    }

    /***
     * Dice rolling function, Allow the player to roll the dice and then move appropriately
     * @param nextPlayer Current player in the turn order to roll dice
     * @param diceCount Counter to keep track of how many times it has rolled the same number
     */
    public void diceRoll(Player nextPlayer, int diceCount){
        int diceNum = gameSystem.roll(nextPlayer, diceCount);

        Alert diceMessage = new Alert(Alert.AlertType.INFORMATION);
        diceMessage.setTitle("Dice Generated");
        diceMessage.setHeaderText(nextPlayer.getName() + " rolled " + diceNum);
        // diceMessage.setContentText("Roll again");
        diceMessage.showAndWait();
    }

    /***
     * Create the trading Scene where it shows your assets and the selected player assets that is trade-able
     * @param currentPlayer Current player
     * @param tradePlayer The selected player that they wish to trade with
     */
    public void tradingScene(Player currentPlayer, String tradePlayer){
        int i = 0;
        VBox tradingPane = new VBox(10);
        tradingPane.setPadding(new Insets(0, 20, 10, 20));
        tradingPane.setAlignment(Pos.TOP_CENTER);
        // Hold playerOnePane and PlayerTwoPane (asset information for both players)
        HBox allPlayerPane = new HBox(15);
        allPlayerPane.setAlignment(Pos.CENTER);
        // Holds only the current players assets that is trade-able
        VBox playerOnePane = new VBox();
        playerOnePane.setAlignment(Pos.CENTER);
        // Holds only the selected player assets that is trade-able
        VBox playerTwoPane = new VBox();
        playerTwoPane.setAlignment(Pos.CENTER);
        // Holds three button (select another player, trade and cancel the trade)
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Trading");
        Label playerOneName = new Label(currentPlayer.getName() + " Assets");
        Label playerTwoName = new Label(tradePlayer + " Assets");

        // TODO remove after finish testing
        Property temp;
        Property temp2;
        temp = new Property(1, "Example Street", Group.GREEN, 10, 10, null, 10, null);
        temp2 = new Property(2, "Example Street1", Group.GREEN, 10, 10, null, 10, null);
        currentPlayer.addAsset(temp);
        currentPlayer.addAsset(temp2);

        ListView playerOneAsset = new ListView();
        // Add current player assets to it their listView
        playerOneAsset = addAssetToViewList(currentPlayer, playerOneAsset);
        // Allow the user to select multiple properties to trade with
        playerOneAsset.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListView playerTwoAsset = new ListView();
        playerTwoAsset.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Find the player object with the given player name
        while(i < order.size()){
            if(tradePlayer == order.get(i).getName()){
                Player playerTwo = order.get(i);
                // Add selected player assets to their listView
                playerTwoAsset = addAssetToViewList(playerTwo, playerTwoAsset);
                break;
            }
            i++;
        }

        Button trade = new Button("Trade");
        Button help = new Button("Help");
        Button back = new Button("Select Another Player");
        Button cancelTrade = new Button("Cancel Trade");

        // Button Functionality
        trade.setOnAction(e -> {
            Alert tradeMessage = new Alert(Alert.AlertType.NONE);

        });
        help.setOnAction(e -> {
            Alert helpMessage = new Alert(Alert.AlertType.INFORMATION);
            helpMessage.setTitle("Property Tycoon Trading");
            // TODO ask tom which key is hold
            helpMessage.setHeaderText("Property Tycoon Help Message");
            helpMessage.setContentText("To trade multiple assets, hold 'x' and click on the asset");
            helpMessage.showAndWait();
        });
        back.setOnAction(e -> {

        });
        cancelTrade.setOnAction(e -> {

        });

        playerOnePane.getChildren().addAll(playerOneName, playerOneAsset);
        playerTwoPane.getChildren().addAll(playerTwoName, playerTwoAsset);
        allPlayerPane.getChildren().addAll(playerOnePane, playerTwoPane);
        optionPane.getChildren().addAll(back, help, trade, cancelTrade);
        tradingPane.getChildren().addAll(title, allPlayerPane, optionPane);

        tradingScene = new Scene(tradingPane);
    }

    /***
     * Add all of the trade-able assets to the given list view with the given player
     * @param player The current player you wish to find their assets
     * @param asset A empty list view which will show all of the trade-able assets
     * @return A filled list view of all of the trade-able assets
     */
    public ListView addAssetToViewList(Player player, ListView asset){
        LinkedList<Object> tradeableAsset = player.tradeableAssets(player);
        for(int i = 0; i < tradeableAsset.size(); i++){
            // Fetch a property and casting it to a board tile
            BoardTile propertyTile = (BoardTile) tradeableAsset.get(i);
            // Fetch the property name
            asset.getItems().add(propertyTile.getTitle());
        }
        return asset;
    }

    /***
     * Create a popup scene which allow the current player to select who they want to trade with
     * @param currentPlayer Is a parameter which tell us who is the current player is
     */
    public void tradingSetupScene(Player currentPlayer){
        // TODO make it a popup scene, (waiting on board scene to be finished)
        VBox tradingSetupPane = new VBox(10);
        tradingSetupPane.setAlignment(Pos.CENTER);
        HBox selectPlayerPane = new HBox(5);
        selectPlayerPane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Trading Setup");
        Label tradeMessage = new Label("Select the players you want to trade with " + currentPlayer.getName());

        Button nextSetup = new Button("Next");
        Button leaveTrade = new Button("Leave Trade");

        ComboBox listOfPlayer = new ComboBox();
        // Copying all of the players - current player (choices for current player to trade with)
        LinkedList<Player> tempPlayerList = (LinkedList<Player>) order.clone();
        for(int i = 0; i < order.size(); i++){
            // Make sure you cant trade with yourself
            if(currentPlayer.getName() != tempPlayerList.get(i).getName()){
                listOfPlayer.getItems().add(tempPlayerList.get(i).getName());
                // Default value set to the first player that is not itself
                listOfPlayer.setValue(tempPlayerList.get(1).getName());
            }
        }

        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        tradeMessage.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;"
        );
        nextSetup.setPrefSize(130,30);
        nextSetup.setStyle("-fx-font-size:14;");
        leaveTrade.setPrefSize(130,30);
        leaveTrade.setStyle("-fx-font-size:14;");

        nextSetup.setOnAction(e -> {
            // Fetch the name of the player that they want to trade with
            String tradingPlayer = listOfPlayer.getValue().toString();
            tradingScene(currentPlayer, tradingPlayer);
            // Change scene with current player asset and selected player asset to trade with
            window.setScene(tradingScene);
            window.show();
        });
        leaveTrade.setOnAction(e -> {
            // Doesnt want to trade, go back to board scene
            window.setScene(gameScene);
            window.show();
        });

        selectPlayerPane.getChildren().addAll(tradeMessage, listOfPlayer);
        optionPane.getChildren().addAll(nextSetup, leaveTrade);
        tradingSetupPane.getChildren().addAll(title, selectPlayerPane, optionPane);
        tradingSetupScene = new Scene(tradingSetupPane,400,400);
    }

    /***
     * Initializing the game board and it's deck's of cards
     * @param gameMode A string which initialize the game to be either full or abridged
     */
    public void createBoard(String gameMode){
        Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
        Deque<CardEffect> opp;

        try{
            board = Json.fromJsonToTileSet("BoardTileData.json");
        }catch (IOException e){
            e.printStackTrace();
        }
        // Add cards to decks
        try{
            opportunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
            potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        }catch (IOException e) {
            e.printStackTrace();
        }

        // Shuffles order
        Collections.shuffle(opportunityKnocksPack);
        Collections.shuffle(potLuckPack);

        //Load shuffled pack into decks
        pot = new ArrayDeque<>(potLuckPack);
        opp = new ArrayDeque<>(opportunityKnocksPack);
        gameSystem = new Board(order, board, pot, opp, gameMode);
    }

    /***
     * How the game is run
     */
    public void gameLoop() {
        int retirePoint = 300;
        gameSystem.setStart(Instant.now());
        //displayAsString();
        Collections.shuffle(gameSystem.turnOrder);
        // graph
        for( Player p: gameSystem.turnOrder ){
            gameSystem.dataStore.put(p.getName(), new ArrayList<>());
        }
        gameSystem.turns = 0;

        while (gameSystem.turnOrder.size() > 1) {
            gameSystem.turns++;
            for (int i = 0; i < gameSystem.turnOrder.size(); i++) {
                Player currentPlayer = gameSystem.turnOrder.get(i);
                if (currentPlayer == null) {
                    continue;//skip players that have been removed from turn order
                } else if (currentPlayer.isInJail()) {
                    gameSystem.tiles.get(10).activeEffect(currentPlayer);//Activate the jail tile to serve time
                    gameSystem.storeData(currentPlayer, currentPlayer.netWorth());
                    continue;//move to next turn
                }
                int count = 0;
                do {
                    count++;
                    // Dice rolling the same number
                    gameSystem.repeat = false;
                    // Get dice roll input from player (not AI)
                    if (!currentPlayer.isAiAgent()) {
                        //diceRoll(currentPlayer, count);
                    }
                    currentPlayer.setLastRoll(gameSystem.roll(currentPlayer, count));//keep track of player roll
                    currentPlayer.passGo();
                    gameSystem.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);

                    if (gameSystem.turnOrder.contains(currentPlayer) && !currentPlayer.isInJail()) {
                        if (!currentPlayer.isAiAgent()) {
                            currentPlayer.leaveGame();
                            //TODO Player property management GUI here
                            //TODO Ask player if they want to trade/ GUI trade here
                        } else {
                            currentPlayer.initiateTrade();
                            if (gameSystem.turns > retirePoint && gameSystem.turnOrder.size() > 4) {
                                retirePoint += 100;
                                currentPlayer.agentRetire();
                            }
                        }
                        currentPlayer.developProperties();
                        if (!gameSystem.repeat) {
                            gameSystem.storeData(currentPlayer, currentPlayer.netWorth());
                        }
                    }

                } while (gameSystem.repeat);
            }
            if (gameSystem.timeUp) {

                break;
            }
        }
        try {
            gameSystem.gameOver();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}