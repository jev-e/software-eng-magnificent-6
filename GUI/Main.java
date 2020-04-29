package GUI;

import ClassStructure.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;


public class Main extends Application {

    Stage window;
    Scene menuScene, ruleScene, playerSetupScene, gameSetupScene, gameBoardScene;

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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.setTitle("Property Tycoon");
        createMainMenuScene();
        window.setScene(menuScene);
        window.show();
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

        // Drop down box for user to select how many players and AIs (default value = 1 player, 1 AI)
        ComboBox playerNumber = new ComboBox();
        playerNumber.getItems().addAll("0", "1", "2", "3", "4", "5", "6");
        playerNumber.setValue("1");
        ComboBox AINumber = new ComboBox();
        AINumber.getItems().addAll("0", "1", "2", "3", "4", "5", "6");
        // Setting the default value for the combo box to have 1 player and 1 AI
        AINumber.setValue("1");

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

        nextButton .setPrefSize(110,30);
        nextButton .setStyle("-fx-font-size:14;");

        rule.setPrefSize(110,30);
        rule.setStyle("-fx-font-size:14;");

        quit.setPrefSize(110,30);
        quit.setStyle("-fx-font-size:14;");

        // Button Functionality
        nextButton.setOnAction(e -> {
            // Fetch value from the combo box and converting into a integer
            int tempPlayerNum = Integer.valueOf(playerNumber.getValue().toString());
            int tempAINum = Integer.valueOf(AINumber.getValue().toString());
            // Error message
            Alert exceedPlayerMessage = new Alert(Alert.AlertType.WARNING);
            // Maximum player constraint
            if(tempPlayerNum + tempAINum <= 6 && tempPlayerNum + tempAINum >= 2){
                createGameSetupScene(tempPlayerNum, tempAINum);
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
        playerPane.getChildren().addAll(title, numberOfPlayerLabel, playerNumber, numberOfAILabel, AINumber, optionPane);
        playerSetupScene = new Scene(playerPane, 500, 500);
    }

    /***
     * Create a set of spinners and textfield per player/AI
     * Spinners (tokens that the player could choose from)
     * Textfield (placeholder to show AIs name or allow the player to set their own name)
     * @param playerSize Number of player size
     * @param aiSize Number of AIs
     */
    public void createGameSetupScene(int playerSize, int aiSize){
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

        Button generateGame = new Button("Generate Game");
        Button backToPlayerSetup = new Button("Back");
        Button quit = new Button("Quit");

        // CSS
        title.setStyle(
                // Top, Right, Bottom and Left for padding in Css
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        generateGame.setPrefSize(130,30);
        generateGame.setStyle("-fx-font-size:14;");

        backToPlayerSetup.setPrefSize(130,30);
        backToPlayerSetup.setStyle("-fx-font-size:14;");

        quit.setPrefSize(130,30);
        quit.setStyle("-fx-font-size:14;");

        gameSetupPane.getChildren().add(title);
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
//            window.setScene(playerSetupScene);
//            window.show();
        });

        backToPlayerSetup.setOnAction(e -> {
            window.setScene(playerSetupScene);
            window.show();
        });

        quit.setOnAction(e -> Platform.exit());

        optionPane.getChildren().addAll(generateGame, backToPlayerSetup, quit);
        gameSetupPane.getChildren().add(optionPane);
        gameSetupScene = new Scene(gameSetupPane, 500, 500);
    }
}