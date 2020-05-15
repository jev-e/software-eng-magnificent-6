package GUI;

import src.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.lang.Object;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GUI Main Executable, Handles all GUI Displaying
 *
 * @author Callum Crawford, Danny Che
 */
public class Main extends Application {

    // Variables
    private int playerNo;               // Number of players in the game
    private Label potTotal;             // Keeps track of the tax / Free Parking pot total

    private FlowPane jailTokens;      // Tokens of InJail Players
    private FlowPane[] playerCards;     // Player cards on right hand side of game screen
    private FlowPane[] tokenDisplay;    // FlowPanes showing tokens on tiles
    private FlowPane[] propDevelopment; // Shows houses and hotel on the property

    private Player[] players;           // Keep permanent order of players for player cards
    private Label[] playerMoney;        // Labels to show how much money the player has
    private Label[] playerNames;        // Labels of player names to show in assets
    private ImageView[] playerIVs;      // Player token ImageViews to show in assets

    // Images - Tiles
    private Image goTilePNG;
    private Image goJailPNG;
    private Image freeParkingPNG;
    private Image visitingPNG;
    private Image waterPNG; // Edison Water Utility
    private Image powerPNG; // Tesla Power Utility
    private Image oppKnocksPNG;
    private Image potLuckPNG;
    private Image stationPNG;
    private Image globalPotPNG;

    // Images - Assets
    private Image hotelPNG;
    private Image housePNG;
    private Image jailFreePNG;

    // Images - Tokens
    private Image bootTokenPNG;
    private Image catTokenPNG;
    private Image gobletTokenPNG;
    private Image hatstandTokenPNG;
    private Image phoneTokenPNG;
    private Image spoonTokenPNG;

    // ImageViews - Tiles
    private ImageView goTile, goJail, freeParking, visitingJail; // Corner Tiles
    private ImageView edisonWater, teslaPower;                   // Utilities
    private ImageView oppKnocks7, oppKnocks22, oppKnocks36;      // Opportunity Knocks
    private ImageView potLuck2, potLuck17, potLuck33;            // Pot Lucks
    private ImageView bStation, hStation, fStation, lStation;    // Stations
    private ImageView globalPot;

    // ImageViews - Assets
    private ImageView hotel, house, jailFreeCard;

    // ImageViews - Tokens
    private ImageView bootToken, catToken, gobletToken, hatstandToken, phoneToken, spoonToken;

    // Scenes
    private Scene gameScene; // Shows gameBP

    // BorderPanes
    private BorderPane gameBP; // Holds entire game screen

    // FlowPanes
    private FlowPane buttonFP;   // Bottom FlowPane for game buttons

    // GridPanes
    private GridPane boardGP;    // Main Board, Left side of screen

    private GridPane topRowGP;   // Top 9 tiles container (Not the top two corners)
    private GridPane leftColGP;  // Left 9 tiles container (Not the Corners)
    private GridPane rightColGP; // Right 9 tiles container (Not the corners)
    private GridPane botRowGP;   // Bottom 9 tiles container (Not the corners)
    private GridPane midGridGP;  // Middle of the board

    // VBoxes

    private VBox playersVB; // Player cards, Right side of Screen

    // Danny's Variables

    Stage window;
    Scene menuScene, ruleScene, playerSetupScene, gameSetupScene, gameBoardScene, tradingSetupScene, tradingScene, auctionScene, jailSetupScene;
    Scene assetSellingManagementScene, assetSellingHouseScene, assetMortgageScene, assetSellingScene, assetImproveScene, improveScene, unMortgageScene;
    Scene winnerSetup, summaryScene;

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
    private Player playerTwo;
    private Pair<String, Integer> highestBidder = new Pair<String, Integer>("", 0);
    private Pair<String, Integer> secondHighestBidder = new Pair<String, Integer>("", 0);
    // The player that activated the auction scene (player could not afford the asset or didnt want to buy it)
    Player auctioneer;
    // Holds the player object with the highest bid in auction
    Player highestPlayerBidder;
    AtomicInteger bidderLeft;
    AtomicInteger fundRequired;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Property Tycoon");

        createMainMenuScene();
        window.setScene(menuScene);
        window.show();
        //displayGameScene();

        Player basic = new Player("Calvin", Token.SPOON, gameSystem, false);
//        createBoard("full");
//        int[] buildingRents = {10,10,20,30};
//        Player basic = new Player("Calvin", Token.SPOON, gameSystem, false);
//        order.add(basic);
//        Property test = (Property) gameSystem.tiles.get(1);
//        System.out.println(test.getTitle());
//        basic.addAsset(test);
//        test.setHousesNo(4);
//        test.setHotelNo(0);
//        test.setDeveloped(true);
//        assetSellingManagementSetupScene(basic, 500);
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Calls Key Functions to display the game, sets gameScene active
     */
    public void displayGameScene() {
        initGameVariables();  // Initialise Variables and Containers
        initGameImages();     // Load Game Images
        initGameImageViews(); // Loads Game Images into their respective ImageViews
        formatGameImageViews(); // Formats Game ImageViews
        formatGameScene();    // Formats Game Scene
        retrieveBoardData();  // Provides initial data from board to scene
        displayPlayerCards(); // Displays player cards to the gameBP
        displayGameBoard();   // Displays main game board
        initalTokenDisplay(); // Draw Tokens at GO Tile
        displayPot();

        // Show Game Screen
        window.setScene(gameScene);
        window.setX(0);
        window.setY(0);
        window.show();
    }

    /**
     * Retrieves necessary data from the board
     */
    public void retrieveBoardData() {
        for (int i = 0; i < 40; i++) {
            board.get(i).initGuiElements();
            board.get(i).setTileName();
            board.get(i).setTilePrice();
        }
    }

    /**
     * Draws tokens to the GO Tile at the start of the game
     */
    public void initalTokenDisplay() {
        for (int i = 0; i < playerNo; i++) {
            Token token = players[i].getToken();

            tokenDisplay[0].getChildren().add(getTokenIV(token));
        }
    }

    /**
     * Returns the Image Object that represents the token
     *
     * @param token The token to be represented
     * @return The Image Object of the token
     */
    private Image getTokenImage(Token token) {
        switch (token) {
            case SMARTPHONE:
                return phoneTokenPNG;

            case BOOT:
                return bootTokenPNG;

            case SPOON:
                return spoonTokenPNG;

            case CAT:
                return catTokenPNG;

            case GOBLET:
                return gobletTokenPNG;

            case HATSTAND:
                return hatstandTokenPNG;

        }
        return null;
    }

    /**
     * Returns the imageview that represents that token
     *
     * @param token The token to be represented
     * @return The imageview of the token
     */
    private ImageView getTokenIV(Token token) {
        switch (token) {
            case SMARTPHONE:
                return phoneToken;

            case BOOT:
                return bootToken;

            case SPOON:
                return spoonToken;

            case CAT:
                return catToken;

            case GOBLET:
                return gobletToken;

            case HATSTAND:
                return hatstandToken;

        }
        return null;
    }

    /**
     * Removes Player's token from the board
     *
     * @param currentPlayer The player to remove
     */
    public void removeToken(Player currentPlayer) {
        int pos = currentPlayer.getCurrentPos();
        Token token = currentPlayer.getToken();

        tokenDisplay[pos].getChildren().remove(getTokenIV(token));
    }

    /**
     * Updates visual tokens on the board to position of players
     *
     * @param currentPlayer The player who's positon needs to be updated
     */
    public void displayTokens(Player currentPlayer) {

        int pos = currentPlayer.getCurrentPos();
        Token token = currentPlayer.getToken();

        boolean inJail = currentPlayer.isInJail();

        // Prevents Duplicate Children
        if (pos != 10) {
            if (!tokenDisplay[pos].getChildren().contains(getTokenIV(token))) {
                tokenDisplay[pos].getChildren().add(getTokenIV(token));
            }
        } else {
            if (!inJail) {
                if (!tokenDisplay[pos].getChildren().contains(getTokenIV(token))) {
                    tokenDisplay[pos].getChildren().add(getTokenIV(token));
                }
            } else {
                if (!jailTokens.getChildren().contains(getTokenIV(token))) {
                    jailTokens.getChildren().add(getTokenIV(token));
                }
            }
        }
    }

    /**
     * Updates every player's money label
     */
    private void updateAllMoneyLabels() {
        for (int i = 0; i < playerNo; i++) {
            playerMoney[i].setText("£" + String.valueOf(players[i].getMoney()));
        }
    }

    /**
     * Updates given player's money label on board
     *
     * @param currentPlayer player's money label to be updated
     */
    public void showPlayerMoney(Player currentPlayer) {
        int money = currentPlayer.getMoney();

        for (int i = 0; i < playerNo; i++) {
            if (currentPlayer == players[i]) {
                playerMoney[i].setText("£" + String.valueOf(money));
            }
        }
    }

    /**
     * Refresh's the given player's asset card
     *
     * @param currentPlayer The player's assets to be redrawn
     */
    public void refreshPlayerCards(Player currentPlayer) {
        int i = 0;

        for (int j = 0; j < playerNo; j++) {
            if (currentPlayer == players[j]) {
                i = j;
            }
        }

        playerCards[i].getChildren().clear();
        playerCards[i].getChildren().addAll(playerIVs[i], playerNames[i], playerMoney[i]);

        for (Object item : currentPlayer.getAssets()) {
            Label label = new Label();

            if (item instanceof Property) {
                String name = ((Property) item).getNameLabel().getText();
                label.setText(name);
                label.setAccessibleText(name);
            } else if (item instanceof Station) {
                String name = ((Station) item).getNameLabel().getText();
                label.setText(name);
                label.setAccessibleText(name);
            } else if (item instanceof Utility) {
                String name = ((Utility) item).getNameLabel().getText();
                label.setText(name);
                label.setAccessibleText(name);
            }
            playerCards[i].getChildren().add(label);
        }
    }

    /**
     * Refresh the houses and hotel displays
     *
     * @param tile
     */
    public void refreshDevelopment(Property tile) {
        int tileNo = tile.getiD();
        propDevelopment[tileNo].getChildren().clear();
        propDevelopment[tileNo].getChildren().addAll(tile.getDevelopment());
    }

    /**
     * Displays pot image and label for pot total
     */
    public void displayPot() {
        updatePot();

        midGridGP.add(globalPot, 0, 0);
        midGridGP.add(potTotal, 1, 0);
    }

    /**
     * Updates the Tax Pot label
     */
    public void updatePot() {
        potTotal.setText("Tax Pot: £" + String.valueOf(gameSystem.getTaxPot()));
    }

    /**
     * Displays All Tiles to the boardGP
     */
    public void displayGameBoard() {

        double shortSide = 90, longSide = 120;

        for (int i = 0; i < 40; i++) {
            Label label = board.get(i).getNameLabel();

            Label priceLabel = board.get(i).getTilePrice();
            Canvas canvas = board.get(i).getColourDisplay();

            GridPane container = new GridPane();
            Insets insets = new Insets(10);
            container.setMinSize(longSide, shortSide);

            StackPane stack = new StackPane();                   // Holds Background image and tokens FlowPane

            FlowPane tokens = new FlowPane(5, 5);      // Holds tokens in each tile
            tokens.setPrefWidth(longSide);

            jailTokens = new FlowPane(5, 5);          // Holds tokens in jail
            jailTokens.setPrefWidth(longSide);
            jailTokens.setMinHeight(50);

            FlowPane developImgs = new FlowPane(5, 5); // Holds houses and hotels
            developImgs.setPrefWidth(longSide);

            // Add Tiles to GridPane
            switch (i) {

                // Go Tile
                case 0:
                    stack.getChildren().addAll(goTile, tokens);
                    container.add(stack, 0, 1);
                    boardGP.add(container, 2, 2);
                    break;

                // Bottom Row
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    container.add(label, 0, 1);
                    container.setHalignment(label, HPos.CENTER);
                    container.add(priceLabel, 0, 2);
                    container.setHalignment(priceLabel, HPos.CENTER);

                    // Properties
                    if (!(i == 2 || i == 4 || i == 5 || i == 7)) {
                        label.setTextFill(Color.WHITE);
                        board.get(i).setColour();
                        stack.getChildren().addAll(canvas, label);
                        container.add(stack, 0, 0);
                        container.add(tokens, 0, 4);
                        container.add(developImgs, 0, 3);
                    }

                    // Pot Luck
                    if (i == 2) {
                        stack.getChildren().addAll(potLuck2, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Income Tax
                    if (i == 4) {
                        Label Tax = new Label("£200");
                        container.add(Tax, 0, 2);
                        container.setHalignment(Tax, HPos.CENTER);
                        container.add(tokens, 0, 3);
                    }

                    // Brighton Station
                    if (i == 5) {
                        stack.getChildren().addAll(bStation, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Opportunity Knocks
                    if (i == 7) {
                        stack.getChildren().addAll(oppKnocks7, tokens);
                        container.add(stack, 0, 2);
                    }

                    botRowGP.add(container, (9 - i), 0);
                    break;

                // Jail Tile
                case 10:
                    stack.getChildren().addAll(visitingJail, tokens, jailTokens);
                    stack.setAlignment(jailTokens, Pos.TOP_LEFT);
                    stack.setAlignment(tokens, Pos.BOTTOM_LEFT);
                    boardGP.add(stack, 0, 2);
                    break;

                // Left Column
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                    container.add(label, 0, 1);
                    container.setHalignment(label, HPos.CENTER);
                    container.add(priceLabel, 0, 2);
                    container.setHalignment(priceLabel, HPos.CENTER);

                    // Properties
                    if (!(i == 12 || i == 15 || i == 17)) {
                        label.setTextFill(Color.WHITE);
                        board.get(i).setColour();
                        stack.getChildren().addAll(canvas, label);
                        container.add(stack, 0, 0);
                        container.add(tokens, 0, 4);
                        container.add(developImgs, 0, 3);
                    }

                    // Tesla Power
                    if (i == 12) {
                        stack.getChildren().addAll(teslaPower, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Hove Station
                    if (i == 15) {
                        stack.getChildren().addAll(hStation, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Pot Luck
                    if (i == 17) {
                        stack.getChildren().addAll(potLuck17, tokens);
                        container.add(stack, 0, 2);
                    }

                    leftColGP.add(container, 0, (20 - i));
                    break;

                // Free Parking Tile
                case 20:
                    stack.getChildren().addAll(freeParking, tokens);
                    boardGP.add(stack, 0, 0);
                    break;

                // Top Row
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                    container.add(label, 0, 1);
                    container.setHalignment(label, HPos.CENTER);
                    container.add(priceLabel, 0, 2);
                    container.setHalignment(priceLabel, HPos.CENTER);

                    if (i == 21 || i == 23 || i == 24) {
                        label.setTextFill(Color.WHITE);
                    }

                    // Properties
                    if (!(i == 22 || i == 25 || i == 28)) {
                        board.get(i).setColour();
                        stack.getChildren().addAll(canvas, label);
                        container.add(stack, 0, 0);
                        container.add(tokens, 0, 4);
                        container.add(developImgs, 0, 3);
                    }

                    // Opportunity Knocks
                    if (i == 22) {
                        stack.getChildren().addAll(oppKnocks22, tokens);
                        container.add(stack,0,2);
                    }

                    // Falmer Station
                    if (i == 25) {
                        stack.getChildren().addAll(fStation, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Edison Water
                    if (i == 28) {
                        stack.getChildren().addAll(edisonWater, tokens);
                        container.add(stack, 0, 2);
                    }

                    topRowGP.add(container, (i - 21), 0);
                    break;

                // Go To Jail Tile
                case 30:
                    stack.getChildren().addAll(goJail, tokens);
                    boardGP.add(stack, 2, 0);
                    break;

                // Right Column
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                    container.add(label, 0, 1);
                    container.setHalignment(label, HPos.CENTER);
                    container.add(priceLabel, 0, 2);
                    container.setHalignment(priceLabel, HPos.CENTER);

                    // Properties
                    if (!(i == 33 || i == 35 || i == 36 || i == 38)) {
                        label.setTextFill(Color.WHITE);
                        board.get(i).setColour();
                        stack.getChildren().addAll(canvas, label);
                        container.add(stack, 0, 0);
                        container.add(tokens, 0, 4);
                        container.add(developImgs, 0, 3);
                    }

                    // Pot Luck
                    if (i == 33) {
                        stack.getChildren().addAll(potLuck33, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Lewis Station
                    if (i == 35) {
                        stack.getChildren().addAll(lStation, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Opportunity Knocks
                    if (i == 36) {
                        stack.getChildren().addAll(oppKnocks36, tokens);
                        container.add(stack, 0, 2);
                    }

                    // Super Tax
                    if (i == 38) {
                        Label sTax = new Label("£100");
                        container.add(sTax, 0, 2);
                        container.setHalignment(sTax, HPos.CENTER);
                        container.add(tokens, 0, 3);
                    }

                    rightColGP.add(container, 0, (i - 31));
                    break;

                default:

                    break;
            }
            tokenDisplay[i] = tokens;
            propDevelopment[i] = developImgs;
        }
    }

    /**
     * Display Player Cards to primaryStage
     * Called Before game scene showed
     */
    public void displayPlayerCards() {

        for (int i = 0; i < playerNo; i++) {
            FlowPane playerPane = new FlowPane(5, 5);
            formatGivenCard(playerPane);

            ImageView token = new ImageView(getTokenImage(players[i].getToken()));
            token.setPreserveRatio(true);
            token.setFitWidth(20);
            token.setFitHeight(20);

            Label playerName;

            updateAllMoneyLabels();

            if (!players[i].isAiAgent()) {
                playerName = new Label("Player: " + players[i].getName());
            } else {
                playerName = new Label("AI: " + players[i].getName());
            }

            playerPane.getChildren().addAll(token, playerName, playerMoney[i]);

            playerNames[i] = playerName;
            playerIVs[i] = token;
            playerCards[i] = playerPane;

            playersVB.getChildren().add(playerPane);
        }
    }

    /**
     * Formats a given player card, called by displayPlayerCards()
     *
     * @param card The player card to be formatted
     */
    private void formatGivenCard(FlowPane card) {
    }

    /**
     * Initialises all Panes and Variables for Game Scene
     * Formats gameGP and buttonFP
     */
    public void initGameVariables() {

        // Initialise main Game containers
        gameBP = new BorderPane();
        boardGP = new GridPane();
        playersVB = new VBox(25);

        // Initialise Scene
        gameScene = new Scene(gameBP);

        // Initialise formatting
        Insets insets = new Insets(10);
        Label label = new Label();

        // Add Panes to gameBP
        gameBP.setLeft(boardGP);
        gameBP.setRight(playersVB);
        gameBP.setPadding(insets);

        // Initialise inner containers
        topRowGP = new GridPane();
        leftColGP = new GridPane();
        rightColGP = new GridPane();
        botRowGP = new GridPane();
        midGridGP = new GridPane();

        // Format GameBP
        gameBP.setCenter(label);
        gameBP.setMargin(label, insets);

        // Format midGridGP
        midGridGP.setPadding(insets);

        // Add inner containers to boardGP
        boardGP.add(topRowGP, 1, 0);
        boardGP.add(leftColGP, 0, 1);
        boardGP.add(rightColGP, 2, 1);
        boardGP.add(botRowGP, 1, 2);
        boardGP.add(midGridGP, 1, 1);

        // Initialise Variables
        potTotal = new Label("£ 0");
        tokenDisplay = new FlowPane[40];
        propDevelopment = new FlowPane[40];

        playerNo = gameSystem.turnOrder.size();

        players = new Player[playerNo];
        playerCards = new FlowPane[playerNo];
        playerMoney = new Label[playerNo];
        playerNames = new Label[playerNo];
        playerIVs = new ImageView[playerNo];

        for (int i = 0; i < playerNo; i++) {
            Label moneyLabel = new Label();
            players[i] = gameSystem.turnOrder.get(i);
            playerMoney[i] = moneyLabel;
        }
    }

    /**
     * Load Game Images from Lib into Image objects
     */
    public void initGameImages() {
        try {
            // Tiles
            goTilePNG = new Image(new FileInputStream("Lib/TilesDesign/goWithColour64bit.png"));
            goJailPNG = new Image(new FileInputStream("Lib/TilesDesign/goJail64bit.png"));
            freeParkingPNG = new Image(new FileInputStream("Lib/TilesDesign/freeParking64bit.png"));
            visitingPNG = new Image(new FileInputStream("Lib/TilesDesign/visiting64bit.png"));
            waterPNG = new Image(new FileInputStream("Lib/TilesDesign/edisonWater64bit.png"));
            powerPNG = new Image(new FileInputStream("Lib/TilesDesign/teslaPower64bit.png"));
            oppKnocksPNG = new Image(new FileInputStream("Lib/TilesDesign/opportunityKnock64bit.png"));
            potLuckPNG = new Image(new FileInputStream("Lib/TilesDesign/potLuck64bit.png"));
            stationPNG = new Image(new FileInputStream("Lib/TilesDesign/trainStation64bit.png"));
            globalPotPNG = new Image(new FileInputStream("Lib/TilesDesign/globalPot64bit.png"));

            // Assets
            hotelPNG = new Image(new FileInputStream("Lib/Assets/hotel64bit.png"));
            housePNG = new Image(new FileInputStream("Lib/Assets/house64bit.png"));
            jailFreePNG = new Image(new FileInputStream("Lib/Assets/jailFree64bit.png"));

            // Tokens
            bootTokenPNG = new Image(new FileInputStream("Lib/Tokens/BootToken.png"));
            catTokenPNG = new Image(new FileInputStream("Lib/Tokens/CatToken.png"));
            gobletTokenPNG = new Image(new FileInputStream("Lib/Tokens/GobletToken.png"));
            hatstandTokenPNG = new Image(new FileInputStream("Lib/Tokens/HatstandToken.png"));
            phoneTokenPNG = new Image(new FileInputStream("Lib/Tokens/SmartphoneToken.png"));
            spoonTokenPNG = new Image(new FileInputStream("Lib/Tokens/SpoonToken.png"));

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    /**
     * Loads Game Images into ImageViews
     */
    public void initGameImageViews() {
        // Tiles
        goTile = new ImageView(goTilePNG);
        goJail = new ImageView(goJailPNG);
        freeParking = new ImageView(freeParkingPNG);
        visitingJail = new ImageView(visitingPNG);
        edisonWater = new ImageView(waterPNG);
        teslaPower = new ImageView(powerPNG);
        globalPot = new ImageView(globalPotPNG);

        // Tiles - Stations
        bStation = new ImageView(stationPNG);
        hStation = new ImageView(stationPNG);
        fStation = new ImageView(stationPNG);
        lStation = new ImageView(stationPNG);

        // Tiles - Opportunity Knocks
        oppKnocks7 = new ImageView(oppKnocksPNG);
        oppKnocks22 = new ImageView(oppKnocksPNG);
        oppKnocks36 = new ImageView(oppKnocksPNG);

        // Tiles - Pot Lucks
        potLuck2 = new ImageView(potLuckPNG);
        potLuck17 = new ImageView(potLuckPNG);
        potLuck33 = new ImageView(potLuckPNG);

        // Assets
        hotel = new ImageView(hotelPNG);
        house = new ImageView(housePNG);
        jailFreeCard = new ImageView(jailFreePNG);

        // Tokens
        bootToken = new ImageView(bootTokenPNG);
        catToken = new ImageView(catTokenPNG);
        gobletToken = new ImageView(gobletTokenPNG);
        hatstandToken = new ImageView(hatstandTokenPNG);
        phoneToken = new ImageView(phoneTokenPNG);
        spoonToken = new ImageView(spoonTokenPNG);
    }

    /**
     * Formats the ImageViews used on the Game Scene
     */
    public void formatGameImageViews() {
        int tokenSize = 25;

        phoneToken.setPreserveRatio(true);
        phoneToken.setFitWidth(tokenSize);
        phoneToken.setFitHeight(tokenSize);

        spoonToken.setPreserveRatio(true);
        spoonToken.setFitWidth(tokenSize);
        spoonToken.setFitHeight(tokenSize);

        bootToken.setPreserveRatio(true);
        bootToken.setFitWidth(tokenSize);
        bootToken.setFitHeight(tokenSize);

        gobletToken.setPreserveRatio(true);
        gobletToken.setFitWidth(tokenSize);
        gobletToken.setFitHeight(tokenSize);

        hatstandToken.setPreserveRatio(true);
        hatstandToken.setFitWidth(tokenSize);
        hatstandToken.setFitHeight(tokenSize);

        catToken.setPreserveRatio(true);
        catToken.setFitWidth(tokenSize);
        catToken.setFitHeight(tokenSize);

        // Assets
        house.setPreserveRatio(true);
        house.setFitWidth(20);
        house.setFitHeight(20);

        hotel.setPreserveRatio(true);
        hotel.setFitWidth(20);
        hotel.setFitHeight(20);

    }

    /**
     * Provides most formatting for the Game Scene
     */
    public void formatGameScene() {
        Insets insets = new Insets(10);

        boardGP.setGridLinesVisible(true);
        topRowGP.setGridLinesVisible(true);
        leftColGP.setGridLinesVisible(true);
        rightColGP.setGridLinesVisible(true);
        botRowGP.setGridLinesVisible(true);

    }

    /***
     * A main menu scene which contains a label, start button, rule button and a quit button
     * Label (Title of the game), start button (switch to next scene), rule button (switch to rule scene) and quit
     */
    private void createMainMenuScene() {
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

        start.setPrefSize(110, 30);
        start.setStyle(
                "-fx-font-size:14;" + "-fx-font-weight: bold;"
        );

        rule.setPrefSize(110, 30);
        rule.setStyle(
                "-fx-font-size:14;" + "-fx-font-weight: bold;"
        );

        quit.setPrefSize(110, 30);
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
        menuScene = new Scene(menuPane, 500, 500);
    }

    /***
     * A rule scene which contains a 2 labels, 3 buttons
     * Label (Rule title), next label (text of all of the rules), start button (switch to next scene),
     * Main menu button (go back to the last scene) and quit
     */
    public void createRuleScene() {
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

        start.setPrefSize(110, 30);
        start.setStyle("-fx-font-size:14;");

        mainMenu.setPrefSize(110, 30);
        mainMenu.setStyle("-fx-font-size:14;");

        quit.setPrefSize(110, 30);
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
        ruleScene = new Scene(rulePane, 1100, 600);
    }

    /***
     * A setup scene which contains a 3 labels, 2 combo box, 3 buttons
     * Label (Setup title), next label (number of players), last label (number of AIs)
     * ComboBox drop down box which contains how many players or AIs u can add to the game
     */
    public void createPlayerSetupScene() {
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

        nextButton.setPrefSize(110, 30);
        nextButton.setStyle("-fx-font-size:14;");

        rule.setPrefSize(110, 30);
        rule.setStyle("-fx-font-size:14;");

        quit.setPrefSize(110, 30);
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
            if (tempPlayerNum + tempAINum <= 6 && tempPlayerNum + tempAINum >= 2) {
                createGameSetupScene(tempPlayerNum, tempAINum, mode);
                window.setScene(gameSetupScene);
                window.show();
            } else if (tempPlayerNum + tempAINum < 2) {
                // Player amount did not exceeded 2 (player and AI combined)
                exceedPlayerMessage.setTitle("Property Tycoon Setup");
                exceedPlayerMessage.setHeaderText("Player amount was not exceeded");
                exceedPlayerMessage.setContentText("Please ensure that there is a minimum of 2 players in total (Players and AIs combined)");
                exceedPlayerMessage.showAndWait();
            } else {
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
    public void createGameSetupScene(int playerSize, int aiSize, String gameMode) {
        VBox gameSetupPane = new VBox(10);
        gameSetupPane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        HBox timerPane = new HBox(10);
        timerPane.setAlignment(Pos.CENTER);

        TextField timerNum = new TextField("0");

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
        Label timerName = new Label("Enter in game duration (minutes)");

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
        generateGame.setPrefSize(130, 30);
        generateGame.setStyle("-fx-font-size:14;");

        backToPlayerSetup.setPrefSize(130, 30);
        backToPlayerSetup.setStyle("-fx-font-size:14;");

        quit.setPrefSize(130, 30);
        quit.setStyle("-fx-font-size:14;");

        gameSetupPane.getChildren().addAll(title, mode);
        // Create the right amount of spinners and textfield for each player
        for (int i = 0; i < playerSize; i++) {
            // HBox container which contains a spinner and textfield per player
            HBox spinnerTextfieldPane = new HBox(10);
            spinnerTextfieldPane.setAlignment(Pos.CENTER);

            TextField playerName = new TextField();
            playerName.setStyle("-fx-font-size: 14px");
            playerName.setPromptText("Enter players' " + (i + 1) + " name");
            playerNameTextField.add(playerName);

            Spinner token = new Spinner<>();
            token.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
            // Creating a list of tokens that the player could choose from
            ObservableList<String> tokenName = FXCollections.observableArrayList(
                    "Boot", "Smartphone", "Goblet", "Hatstand", "Cat", "Spoon"
            );
            // Setting the default value for all spinners for all the players to be boot
            SpinnerValueFactory<String> tokenChoice = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
            tokenChoice.setWrapAround(true);
            tokenChoice.setValue("Boot");
            token.setValueFactory(tokenChoice);
            playerTokenSpin.add(token);

            spinnerTextfieldPane.getChildren().addAll(token, playerName);
            gameSetupPane.getChildren().addAll(spinnerTextfieldPane);
        }

        // Create the right amount of spinners and textfield for each AIs
        for (int j = 0; j < aiSize; j++) {
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
                    "Boot", "Smartphone", "Goblet", "Hatstand", "Cat", "Spoon"
            );
            // Setting the default value for all spinners for all the AIs to be boot
            SpinnerValueFactory<String> tokenChoice = new SpinnerValueFactory.ListSpinnerValueFactory<>(tokenName);
            tokenChoice.setWrapAround(true);
            tokenChoice.setValue("Boot");
            token.setValueFactory(tokenChoice);
            aiTokenSpin.add(token);

            spinnerTextfieldPane.getChildren().addAll(token, aiName);
            gameSetupPane.getChildren().addAll(spinnerTextfieldPane);
        }

        // Button functionality
        generateGame.setOnAction(e -> {
            try {
                int numConstraint = Integer.parseInt(timerNum.getText());
                // nameTest = true if textfield is not null else some or all text fields is null or " "
                boolean nameTest = nameConstraintCheck();
                // tokenTest = true if constraint is met else false if constraints is not met
                boolean tokenTest = tokenConstraintCheck();
                // Change scene if tokenTest = true, else Alert pops up
                constraintCheck(nameTest, tokenTest, gameMode.toLowerCase(), numConstraint);
            } catch (Exception except) {
                Alert constraintNotMet = new Alert(Alert.AlertType.ERROR);
                constraintNotMet.setTitle("Invalid Number");
                constraintNotMet.setHeaderText("Not a number");
                constraintNotMet.show();
                constraintNotMet.close();
            }

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

        timerPane.getChildren().addAll(timerName, timerNum);
        optionPane.getChildren().addAll(generateGame, backToPlayerSetup, quit);
        if (gameMode.equals("Abridged")) {
            gameSetupPane.getChildren().addAll(timerPane, optionPane);
        } else {
            gameSetupPane.getChildren().addAll(optionPane);
        }
        gameSetupScene = new Scene(gameSetupPane, 500, 500);
    }

    /***
     * Check to see that all textfield is not null or containing just " "
     * @return A boolean value (true = no null textfield, false = textfield is either null or containing only " ")
     */
    public boolean nameConstraintCheck() {
        // True = all textfield is not null, else some/all textfield is " " or empty
        boolean nameCheck = true; // Default to true (if game = all AI)
        int i = 0;

        // Fetch the name value from each textfield
        while (i < playerNameTextField.size()) {
            String playerName = playerNameTextField.get(i).getText();

            // Check if the textfield is empty, containing only " ", or null
            if (playerName == null || playerNameTextField.get(i).getText().trim().isEmpty()) {
                // Name constraint failed
                nameCheck = false;
                break;
            } else {
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
    public boolean tokenConstraintCheck() {
        // true = pass constraint check, else failed constraint check
        boolean tokenCheck;
        // arrayList which holds all the tokens that the player and AI currently has chosen
        ArrayList<String> allToken = new ArrayList<>();

        // Fetch a spinner from the player list and assigned to playerToken
        for (int i = 0; i < playerTokenSpin.size(); i++) {
            // Fetching the tokens that was assigned to each spinner (player)
            String playerToken = playerTokenSpin.get(i).getValue().toString();
            allToken.add(playerToken);
        }

        // Fetch a spinner from the AI list and assigned to aiToken
        for (int j = 0; j < aiTokenSpin.size(); j++) {
            // Fetching the tokens that was assigned to each spinner (AI)
            String aiToken = aiTokenSpin.get(j).getValue().toString();
            allToken.add(aiToken);
        }

        // Add all the tokens to a into tokenConstraint (tokenConstraint is a datatype of set meaning no duplicates)
        Set<String> tokenConstraint = new HashSet<String>(allToken);

        // No duplicate token found if the size of both allToken and tokenConstraint is the same
        if (allToken.size() == tokenConstraint.size()) {
            tokenCheck = true;
        } else {
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
    public void constraintCheck(boolean nameTest, boolean tokenTest, String gameMode, int timeLimit) {
        Alert tokenError = new Alert(Alert.AlertType.ERROR);
        if (nameTest == false) {
            tokenError.setTitle("Name Assignment Error");
            tokenError.setHeaderText("Name Constraint");
            tokenError.setContentText("Please enter in a name for all of the players");
            tokenError.showAndWait();
        } else if (tokenTest == false) {
            tokenError.setTitle("Token Assignment Error");
            tokenError.setHeaderText("Token Constraint");
            tokenError.setContentText("Please ensure all players and AIs has a unique token");
            tokenError.showAndWait();
        } else {
            // Initialize board and the pack of cards
            if (gameMode.equals("full")) {
                createBoard(gameMode);
            } else {
                createBoard(gameMode, timeLimit);
            }
            // Assign player to the turn order
            assignPlayerToTurnOrder();

            displayGameScene();
            window.setScene(gameScene);
            gameLoop();
            window.show();
        }
    }

    /***
     * Create a new player with their token and name then assigning it to the turn order if playerSize >= 1
     * Also creating a new player with their token and name then assigning it to the turn order if aiSize >= 1
     */
    public void assignPlayerToTurnOrder() {
        // Add player to the turn order if playerSize >= 1

        for (int i = 0; i < playerNameTextField.size(); i++) {
            // Creating a new player and assigning it to turn order
            Player player = new Player(playerNameTextField.get(i).getText(), Token.valueOf(playerTokenSpin.get(i).getValue().toString().toUpperCase()), gameSystem, false);
            order.add(player);
        }

        // Add AI to the turn order if aiSize >= 1
        for (int j = 0; j < aiNameTextField.size(); j++) {
            // Creating a new player and assigning it to turn order
            Player ai = new Player(aiNameTextField.get(j).getText(), Token.valueOf(aiTokenSpin.get(j).getValue().toString().toUpperCase()), gameSystem, true);
            order.add(ai);
        }
    }

    public void diceRollMessage(Player currentPlayer, int diceCount) {
        Alert diceMessage = new Alert(Alert.AlertType.NONE);
        ButtonType roll = new ButtonType("Roll Dice");

        diceMessage.setTitle("Property Tycoon Roll Dice");
        diceMessage.setContentText("Click Roll Dice down below to roll your dices " + currentPlayer.getName());
        diceMessage.getButtonTypes().add(roll);

        Optional<ButtonType> option = diceMessage.showAndWait();
        // After clicking the "Roll Dice" button from the popup
        if (option.get() == roll) {
            diceRoll(currentPlayer, diceCount);
        }
        diceMessage.show();
        diceMessage.close();
    }

    /***
     * Dice rolling function, Allow the player to roll the dice and then move appropriately
     * @param currentPlayer Current player in the turn order to roll dice
     * @param diceCount Counter to keep track of how many times it has rolled the same number
     */
    public void diceRoll(Player currentPlayer, int diceCount) {
        // Roll dice using the back-end function
        gameSystem.roll(currentPlayer, diceCount);
        // Fetch the number for die one
        int dice1 = currentPlayer.getLastRoll1();
        // Fetch the number for die two
        int dice2 = currentPlayer.getLastRoll2();

        Alert diceMessage = new Alert(Alert.AlertType.INFORMATION);
        diceMessage.setTitle("Property Tycoon Dice Generated");
        diceMessage.setHeaderText(currentPlayer.getName() + " rolled " + (dice1 + dice2));
        diceMessage.setContentText("Die one rolled " + dice1 + "\nDie two rolled " + dice2);

        diceMessage.showAndWait();
        diceMessage.close();
    }

    /***
     * Create the trading Scene where it shows your assets and the selected player assets that is trade-able
     * @param currentPlayer Current player
     * @param tradePlayer The selected player that they wish to trade with
     */
    public void tradingScene(Player currentPlayer, String tradePlayer) {
        Stage tradePopUpStage = new Stage();
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
        // Holds four button (select another player, trade, help and cancel the trade)
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Trading");
        Label playerOneName = new Label(currentPlayer.getName() + " Assets");
        Label playerTwoName = new Label(tradePlayer + " Assets");

        // Store a link list of what playerTwo will give to current player in a datatype String
        LinkedList<Object> giveString = new LinkedList<>();
        // Store a link list of what playerTwo will receive from current player in a datatype String
        LinkedList<Object> receiveString = new LinkedList<>();

        ListView playerOneAsset = new ListView();
        // Add current player assets to it their listView
        playerOneAsset = addAssetToViewList(currentPlayer, playerOneAsset);
        // Allow the user to select multiple properties to trade with
        playerOneAsset.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListView playerTwoAsset = new ListView();
        playerTwoAsset.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Find the player object with the given player name
        while (i < order.size()) {
            if (tradePlayer == order.get(i).getName()) {
                playerTwo = order.get(i);
                playerTwoAsset = addAssetToViewList(playerTwo, playerTwoAsset);
                break;
            }
            i++;
        }

        Button trade = new Button("Trade");
        Button help = new Button("Help");
        Button back = new Button("Select Another Player");
        Button cancelTrade = new Button("Cancel Trade");

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        playerOneName.setStyle(
                "-fx-font-size: 12;" + "-fx-font-weight: bold;"
        );
        playerTwoName.setStyle(
                "-fx-font-size: 12;" + "-fx-font-weight: bold;"
        );

        // Button Functionality
        back.setOnAction(e -> {
            tradingSetupScene(currentPlayer);
            // Close the popup (showing your assets and the other persons asset)
            tradePopUpStage.close();
        });
        help.setOnAction(e -> {
            Alert helpMessage = new Alert(Alert.AlertType.INFORMATION);
            helpMessage.setTitle("Property Tycoon Trading");
            helpMessage.setHeaderText("Property Tycoon Help Message");
            helpMessage.setContentText("To trade multiple assets, hold 'Ctrl' and click on the assets you wish to trade");
            helpMessage.showAndWait();
        });
        ListView finalPlayerOneAsset = playerOneAsset;
        ListView finalPlayerTwoAsset = playerTwoAsset;

        trade.setOnAction(e -> {
            // Close trading setup
            tradePopUpStage.close();
            Alert tradeMessage = new Alert(Alert.AlertType.NONE);
            tradeMessage.setTitle("Property Tycoon Trading Offer");
            Alert decisionMessage = new Alert(Alert.AlertType.INFORMATION);
            decisionMessage.setTitle("Property Tycoon Trading Decision");
            ButtonType acceptTrade = new ButtonType("Accept Trade");
            ButtonType declineTrade = new ButtonType("Decline Trade");
            String msgOffering = "";
            String msgFor = "";

            // Store a link list of objects on what playerTwo will give to current player
            LinkedList<Object> giveObject = new LinkedList<>();
            // Store a link list of objects on what playerTwo will receive from current player
            LinkedList<Object> receiveObject = new LinkedList<>();

            // Fetch the information that has been selected for the current player and display it into alert header
            ObservableList listOfPlayerOneAsset = finalPlayerOneAsset.getSelectionModel().getSelectedItems();
            for (Object item : listOfPlayerOneAsset) {
                msgOffering += String.format("%s%n", item);
                // Add the properties playerTwo will receive from current player
                receiveString.add(item);
            }
            // Converting the String asset into a object asset that playerTwo will receive from current player
            receiveObject = (LinkedList<Object>) getAsset(currentPlayer, receiveString);

            tradeMessage.setHeaderText(currentPlayer.getName() + " offering\n" + msgOffering);
            // Fetch the information of all the assets that they wish for from the other player and display it into context
            ObservableList listOfPlayerTwoAsset = finalPlayerTwoAsset.getSelectionModel().getSelectedItems();
            for (Object item : listOfPlayerTwoAsset) {
                msgFor += String.format("%s%n", item);
                // Add the properties playerTwo will give to current player
                giveString.add(item);
            }
            // Converting the String asset into a object asset that playerTwo will give to current player
            giveObject = (LinkedList<Object>) getAsset(playerTwo, giveString);

            tradeMessage.setContentText("For these assets " + playerTwo.getName() + "\n" + msgFor);

            // If playerTwo is not a AI
            if (playerTwo.isAiAgent() == false) {
                tradeMessage.getButtonTypes().addAll(acceptTrade, declineTrade);
                Optional<ButtonType> option = tradeMessage.showAndWait();
                if (option.get() == acceptTrade) {
                    decisionMessage.setHeaderText(playerTwo.getName() + " accepted your offer");
                    tradingChangeOwner(currentPlayer, playerTwo, giveObject, receiveObject);
                    // Check for complete set
                    currentPlayer.completeSetProperties();
                    playerTwo.completeSetProperties();
                    decisionMessage.showAndWait();
                    decisionMessage.close();
                } else if (option.get() == declineTrade) {
                    decisionMessage.setHeaderText(playerTwo.getName() + " declined your offer");
                    decisionMessage.showAndWait();
                    decisionMessage.close();
                    window.setScene(gameBoardScene);
                }
            } else { // playerTwo is a AI
                tradeMessage = new Alert(Alert.AlertType.INFORMATION);
                tradeMessage.setTitle("Property Tycoon AI Trading Decision");
                // AI decide if they want to trade or not
                boolean tradeDecision = playerTwo.decide(giveObject, receiveObject);
                // tradeDecision (true = accept, false = decline trade)
                if (tradeDecision == true) {
                    tradeMessage.setHeaderText(playerTwo.getName() + " accepted your offer");
                    tradingChangeOwner(currentPlayer, playerTwo, giveObject, receiveObject);
                    tradeMessage.show();
                    tradeMessage.close();
                } else {
                    tradeMessage.setHeaderText(playerTwo.getName() + " declined your offer");
                    tradeMessage.showAndWait();
                    tradeMessage.close();
                }
            }
        });
        cancelTrade.setOnAction(e -> {
            // Doesnt want to trade, go back to board scene
            window.setScene(gameScene);
            window.show();
            // Close the trading popup scene
            tradePopUpStage.close();
        });
        playerOnePane.getChildren().addAll(playerOneName, playerOneAsset);
        playerTwoPane.getChildren().addAll(playerTwoName, playerTwoAsset);
        allPlayerPane.getChildren().addAll(playerOnePane, playerTwoPane);
        optionPane.getChildren().addAll(back, help, trade, cancelTrade);
        tradingPane.getChildren().addAll(title, allPlayerPane, optionPane);
        tradingScene = new Scene(tradingPane);
        // Creating the popup effect
        tradePopUpStage.setScene(tradingScene);
        tradePopUpStage.initModality(Modality.APPLICATION_MODAL);
        tradePopUpStage.showAndWait();
        tradePopUpStage.close();
    }

    /***
     * A function which will change the ownership of the asset if they agreed on the trade
     * @param currentPlayer Current Player
     * @param tradePlayer The selected player that the current player wishes to trade with
     * @param give A link-list which contains all the asset that tradePlayer will need to give to current player
     * @param receive A link-list which contains all the asset that current player will need to give to tradePlayer
     */
    public void tradingChangeOwner(Player currentPlayer, Player tradePlayer, LinkedList<Object> give, LinkedList<Object> receive) {
        // Go through the 'give' link list on what tradePlayer (player two) will give to current player and change ownership of asset
        for (Object asset : give) {
            tradePlayer.removeAsset(asset);
            currentPlayer.addAsset(asset);
        }


        // Go through the 'receive' link list on what current player will give to the selected player and change ownership of asset
        for (Object asset : receive) {
            currentPlayer.removeAsset(asset);
            tradePlayer.addAsset(asset);
        }
        currentPlayer.completeSetProperties();
        tradePlayer.completeSetProperties();
    }

    /***
     * Add all of the trade-able assets to the given list view with the given player
     * @param player The current player you wish to find their assets
     * @param asset A empty list view which will show all of the trade-able assets
     * @return A filled list view of all of the trade-able assets in the datatype String
     */
    public ListView addAssetToViewList(Player player, ListView asset) {
        LinkedList<Object> tradeableAsset = player.tradeableAssets(player);
        for (int i = 0; i < tradeableAsset.size(); i++) {
            // Fetch a property and casting it to a board tile
            BoardTile propertyTile = (BoardTile) tradeableAsset.get(i);
            // Fetch the property name
            asset.getItems().add(propertyTile.getTitle());
        }
        return asset;
    }

    /***
     * A function used to fetch the asset object with the given name in the linked-list
     * @param assetName A list of all the names of the asset in the datatype of String
     * @return A list of objects of the given assetName
     */
    public Object getAsset(Player player, LinkedList<Object> assetName) {
        LinkedList<Object> assetObject = new LinkedList<>();
        for (Object asset : assetName) {
            for (int i = 0; i < player.getAssets().size(); i++) {
                BoardTile assetTile = (BoardTile) player.getAssets().get(i);
                if (asset == assetTile.getTitle()) {
                    assetObject.add(player.getAssets().get(i));
                }
            }
        }
        return assetObject;
    }

    /***
     * Create a popup scene which allow the current player to select who they want to trade with
     * @param currentPlayer Is a parameter which tell us who is the current player is
     */
    public void tradingSetupScene(Player currentPlayer) {
        Stage tradeSetupPopUpStage = new Stage();

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

        // Adding the players name that current player could trade with (cannot be self)
        for (int i = 0; i < order.size(); i++) {
            // Make sure you cant trade with yourself and players are not in jail (cannot trade with players that is in jail) and also players has shit trade
            if (currentPlayer.getName() != order.get(i).getName() && !order.get(i).isInJail() && order.get(i).getAssets().size() != 0) {
                listOfPlayer.getItems().add(order.get(i).getName());
                // Default value set to the first player that is not itself
                listOfPlayer.setValue(order.get(i).getName());
            }
        }

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        tradeMessage.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;"
        );
        nextSetup.setPrefSize(130, 30);
        nextSetup.setStyle("-fx-font-size:14;");
        leaveTrade.setPrefSize(130, 30);
        leaveTrade.setStyle("-fx-font-size:14;");

        nextSetup.setOnAction(e -> {
            // Fetch the name of the player that they want to trade with
            String tradingPlayer = listOfPlayer.getValue().toString();
            // Change scene with current player asset and selected player asset to trade with
            tradingScene(currentPlayer, tradingPlayer);
            // Close the popup (to select which person you want to trade with)
            tradeSetupPopUpStage.close();
        });
        leaveTrade.setOnAction(e -> {
            // Close the trading popup scene
            tradeSetupPopUpStage.close();
            // Doesnt want to trade, go back to board scene
            window.setScene(gameScene);
            window.show();
        });

        selectPlayerPane.getChildren().addAll(tradeMessage, listOfPlayer);
        optionPane.getChildren().addAll(nextSetup, leaveTrade);
        tradingSetupPane.getChildren().addAll(title, selectPlayerPane, optionPane);
        tradingSetupScene = new Scene(tradingSetupPane, 400, 400);
        // Creating the popup effect
        tradeSetupPopUpStage.setScene(tradingSetupScene);
        tradeSetupPopUpStage.initModality(Modality.APPLICATION_MODAL);
        tradeSetupPopUpStage.showAndWait();
        tradeSetupPopUpStage.close();
    }

    /***
     * Create a list of all of the players in turn order minus current player (potential bidders)
     * @param currentPlayer The current Player that cannot afford the asset or wishes to auction it
     * @param asset The asset (station, property, utility) that is up for auction
     */
    public void auctionPlayerSetup(Player currentPlayer, BoardTile asset) {
        // How many bidders left to bid
        bidderLeft = new AtomicInteger(order.size() - 1);
        for (Player bidder : gameSystem.turnOrder) {
            if (bidder == currentPlayer) {
                // Variable is used when auction is restarted (know who initiated the auction)
                auctioneer = currentPlayer;
            } else if (bidder != currentPlayer && !bidder.isInJail()) {
                // Check bidder is not the auctioneer and is not in jail (player in jailed cannot participate in auctions)
                auctionSetupScene(bidder, asset);
            }
        }
    }

    /***
     * Create a popup scene where the auctioning would happen if the current player does not want/ afford a unowned property
     * @param bidder The player from the turn order (not current player) that wishes to bid
     * @param asset A boardTile that could either be station, utility or property that can be auctioned
     */
    public void auctionSetupScene(Player bidder, BoardTile asset) {
        Stage auctionPopUpStage = new Stage();
        VBox auctionSetupPane = new VBox(10);
        auctionSetupPane.setAlignment(Pos.CENTER);
        auctionSetupPane.setPadding(new Insets(0, 20, 10, 20));
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Auctioning");
        Label propName = new Label();
        Label bidTitle = new Label("Enter in your bid down below " + bidder.getName());
        Label highestBid = new Label();

        // AI auction
        if (bidder.isAiAgent()) {
            int aiDecision = bidder.auctionDecide(asset, highestBidder.getValue());
            auctionLogic(bidder, aiDecision, asset, bidderLeft);
        }

        // If its not the first person to bid
        if (highestBidder.getValue() != 0) {
            String highestBidPlayerName = highestBidder.getKey();
            int currentBid = highestBidder.getValue();
            highestBid.setText("Highest bid currently £" + currentBid + " by " + highestBidPlayerName);
        } else {
            highestBid.setText("No bid currently for this item");
        }

        ImageView propImg = new ImageView();

        TextField bidTxt = new TextField();
        bidTxt.setMaxWidth(63);

        Button bid = new Button("Bid");
        Button withdraw = new Button("Withdraw");
        Button help = new Button("Help");

        if (asset instanceof Property) {
            propImg.setImage(new Image("/Lib/TilesDesign/property64bit.png"));
            // Fetch the name of the property
            propName.setText(asset.getTitle());
        } else if (asset instanceof Utility) {
            String utilName = "Edison Water";
            // Check which utility it is (electricity or water)
            if (asset.getTitle() == utilName) {
                propImg.setImage(new Image("/Lib/TilesDesign/edisonWater64bit.png"));
            } else {
                propImg.setImage(new Image("/Lib/TilesDesign/teslaPower64bit.png"));
            }
            // Fetch the utility name (either electricity or water)
            propName.setText(asset.getTitle());
        } else if (asset instanceof Station) {
            propImg.setImage(new Image("/Lib/TilesDesign/trainStation64bit.png"));
            // Fetch the train station name
            propName.setText(asset.getTitle());
        }

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        propName.setStyle("-fx-font-size: 15;");
        bidTitle.setStyle("-fx-font-size: 14;");

        // Button functionality
        bid.setOnAction(e -> {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle("Property Tycoon Auction");
            try {
                // Check to see if the bid is a number and doesnt contain any text
                int numConstraint = Integer.parseInt(bidTxt.getText());
                if (bidder.getMoney() >= numConstraint) {
                    auctionLogic(bidder, numConstraint, asset, bidderLeft);
                    auctionPopUpStage.close();
                } else {
                    errorMessage.setHeaderText("Property Tycoon Bid Error");
                    errorMessage.setContentText("Bid exceeded balance " + bidder.getName());
                    errorMessage.showAndWait();
                    errorMessage.close();
                }
            } catch (Exception except) {
                except.printStackTrace();
                // Remove invalid bid from text field
                bidTxt.clear();
                errorMessage.setHeaderText("Property Tycoon Bid Error");
                errorMessage.setContentText("Please enter in a valid bid (numbers only!)");
                errorMessage.showAndWait();
                errorMessage.close();
            }
        });
        withdraw.setOnAction(e -> {
            bidderLeft.getAndDecrement();
            auctionPopUpStage.close();
        });
        help.setOnAction(e -> {
            Alert helpMessage = new Alert(Alert.AlertType.INFORMATION);
            helpMessage.setTitle("Property Tycoon Auction");
            helpMessage.setHeaderText("Property Tycoon Help Message");
            helpMessage.setContentText("Remember you can only bid once for a property, so bid wisely!");
            helpMessage.showAndWait();
        });

        optionPane.getChildren().addAll(bid, withdraw, help);
        auctionSetupPane.getChildren().addAll(title, propImg, propName, highestBid, bidTitle, bidTxt, optionPane);
        auctionScene = new Scene(auctionSetupPane, 400, 400);
        // Creating the popup effect
        if (!(bidder.isAiAgent())) {
            auctionPopUpStage.setScene(auctionScene);
            auctionPopUpStage.initModality(Modality.APPLICATION_MODAL);
            auctionPopUpStage.showAndWait();
            auctionPopUpStage.close();
        }
        auctionPopUpStage.close();
    }

    /***
     * Perform the auction logic (setting the highest and second highest bidder and bid, trigger won auction)
     * @param bidder The current player that wishes to bid for the asset
     * @param bid The current player entered in bid for the property (amount they wish to pay for)
     * @param asset The asset that is up for auction
     * @param bidderLeft How many players are left to auction
     */
    public void auctionLogic(Player bidder, int bid, BoardTile asset, AtomicInteger bidderLeft) {
        // If current bid is higher than the highest bidder, replace with the newest bid
        if (bid > highestBidder.getValue()) {
            // Replace highestBidder with the new highest bidder (name and their bid)
            highestBidder = new Pair<>(bidder.getName(), bid);
            // Basically bidderLeft--
            bidderLeft.getAndDecrement();
        } else if (bid > secondHighestBidder.getValue()) {
            // Stores the second highest bid for the current auction (use for constraint check)
            secondHighestBidder = new Pair<>(bidder.getName(), bid);
            // Basically bidderLeft--
            bidderLeft.getAndDecrement();
        } else {
            // Basically bidderLeft--
            bidderLeft.getAndDecrement();
        }

        if (bidderLeft.intValue() == 0) {
            // Fetch the highest bidder player object with the given string
            for (int i = 0; i < order.size(); i++) {
                if (highestBidder.getKey() == order.get(i).getName()) {
                    highestPlayerBidder = order.get(i);
                }
            }
        }

        // If highest bid is > the second highest bid then deduct that amount from bidder and set owner to bidder
        if (bidderLeft.intValue() == 0 && highestBidder.getValue() > secondHighestBidder.getValue()) {
            if (!(highestPlayerBidder.isAiAgent())) {
                Alert auctionWinnerMessage = new Alert(Alert.AlertType.INFORMATION);
                auctionWinnerMessage.setTitle("Property Tycoon Auction");
                auctionWinnerMessage.setHeaderText("Congratulation " + highestPlayerBidder.getName());
                auctionWinnerMessage.setContentText("You have won the auction and acquired " + asset.getTitle() + " for £" + highestBidder.getValue());
                auctionWinnerMessage.showAndWait();
                auctionWinnerMessage.close();
            }
            highestPlayerBidder.deductAmount(highestBidder.getValue());
            highestPlayerBidder.addAsset(asset);
            highestPlayerBidder.completeSetProperties();
        } else if (bidderLeft.intValue() == 0 && highestBidder.getValue() == secondHighestBidder.getValue()) {
            // Restart auction if highest bid == second highest bid
            if (highestBidder.getValue() != 0) {
                auctionPlayerSetup(auctioneer, asset);
            }
        }
    }

    /***
     * Create the asset selling management scene (selling building, mortgage and sell properties)
     * @param currentPlayer The current player
     * @param fundNeededToRise The fund that current player needed to raise
     */
    public void assetSellingManagementSetupScene(Player currentPlayer, int fundNeededToRise) {
        Stage assetSellingManagePopUp = new Stage();
        VBox assetManagementSetupPane = new VBox(10);
        assetManagementSetupPane.setPadding(new Insets(0, 20, 10, 20));
        assetManagementSetupPane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Asset Management");
        Label fundNeed = new Label("£" + fundNeededToRise + " fund is required " + currentPlayer.getName());

        Button sellBuilding = new Button("Sell Houses and Hotels");
        Button mortgageProp = new Button("Mortgage Properties");
        Button sellAsset = new Button("Sell Properties");
        Button autoSell = new Button("Auto-Sell");

        fundRequired = new AtomicInteger(fundNeededToRise);

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        fundNeed.setStyle("-fx-font-size: 14;");

        // Button functionality
        sellBuilding.setOnAction(e -> {
            sellHouseHotel(currentPlayer);
            fundNeed.setText("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());
            if (fundRequired.intValue() <= 0) {
                assetSellingManagePopUp.close();
            }
        });
        mortgageProp.setOnAction(e -> {
            mortgageProperties(currentPlayer);
            fundNeed.setText("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());
            if (fundRequired.intValue() <= 0) {
                assetSellingManagePopUp.close();
            }
        });
        sellAsset.setOnAction(e -> {
            sellProperties(currentPlayer);
            fundNeed.setText("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());
            if (fundRequired.intValue() <= 0) {
                assetSellingManagePopUp.close();
            }
        });
        autoSell.setOnAction(e -> {
            currentPlayer.agentSellAssets(fundNeededToRise);
            Alert moneyRaised = new Alert(Alert.AlertType.INFORMATION);
            moneyRaised.setTitle("Property Tycoon fund raising");
            moneyRaised.setHeaderText(currentPlayer.getName() + " you have raised enough fund");
            moneyRaised.showAndWait();
            moneyRaised.close();
            assetSellingManagePopUp.close();
        });

        optionPane.getChildren().addAll(sellBuilding, mortgageProp, sellAsset, autoSell);
        assetManagementSetupPane.getChildren().addAll(title, fundNeed, optionPane);
        assetSellingManagementScene = new Scene(assetManagementSetupPane, 500, 500);
        // Pop Up
        assetSellingManagePopUp.setScene(assetSellingManagementScene);
        assetSellingManagePopUp.initModality(Modality.APPLICATION_MODAL);
        assetSellingManagePopUp.showAndWait();
        assetSellingManagePopUp.close();
    }

    /***
     * Allow player to sell their developed properties to raise fund
     * @param currentPlayer The current player
     */
    public void sellHouseHotel(Player currentPlayer) {
        Stage sellHouseHotelPopUpStage = new Stage();
        VBox sellHouseHotelPane = new VBox(10);
        sellHouseHotelPane.setAlignment(Pos.CENTER);
        sellHouseHotelPane.setPadding(new Insets(0, 20, 10, 20));
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        // Fetch all of the current player asset
        LinkedList<Object> playerAsset = currentPlayer.getAssets();

        Label title = new Label("Property Tycoon Property Management (Selling Houses and Hotels)");
        Label playerAssetMessage = new Label(currentPlayer.getName() + " this is all of your properties where you can sell houses and hotels");
        Label remainingFundLeft = new Label("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());

        Button sellBuilding = new Button("Selling Buildings");
        Button back = new Button("Return to Asset Selling Management ");

        // Creating a table view with the following columns
        TableView assetInformation = new TableView();
        TableColumn column1 = new TableColumn<>("Property Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn column2 = new TableColumn<>("House number");
        column2.setCellValueFactory(new PropertyValueFactory<>("housesNo"));

        TableColumn column3 = new TableColumn<>("Hotel Number");
        column3.setCellValueFactory(new PropertyValueFactory<>("hotelNo"));

        assetInformation.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        assetInformation.getColumns().addAll(column1, column2, column3);

        // Going through players asset and add it to table-view if its a property and is developed
        for (Object asset : playerAsset) {
            if (asset instanceof Property) {
                // Checking if the property contains houses or hotel
                if (((Property) asset).isDeveloped()) {
                    assetInformation.getItems().add(((Property) asset));
                }
            }
        }
        // Creating a table view with a vertical scroll bar
        ScrollPane scrollForAssetInformation = new ScrollPane(assetInformation);
        scrollForAssetInformation.setFitToWidth(true);
        scrollForAssetInformation.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        playerAssetMessage.setStyle("-fx-font-size: 14;");

        // Button Functionality
        sellBuilding.setOnAction(e -> {
            // Fetch the information that has been selected for the current player and display it into alert header
            ObservableList listOfPlayerAsset = assetInformation.getSelectionModel().getSelectedItems();
            Property assetObject = (Property) listOfPlayerAsset.get(0);
            // saleAmount = the money they received from selling a building (return 0 if failed)
            int saleAmount = assetObject.sellHouseOrHotel();
            if (saleAmount == 0) {
                Alert sellError = new Alert(Alert.AlertType.ERROR);
                sellError.setTitle("Property Tycoon Selling Management");
                sellError.setHeaderText("Selling building violates regulations");
                sellError.showAndWait();
                sellError.close();
            } else {
                // Give player money from selling houses/hotels
                currentPlayer.deductAmount(-saleAmount);
                int fundLeft = fundRequired.intValue() - saleAmount;
                fundRequired = new AtomicInteger(fundLeft);
                assetInformation.refresh();
                remainingFundLeft.setText("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());

                // If current player has raised enough money fund
                if (fundRequired.intValue() <= 0) {
                    Alert fundMessage = new Alert(Alert.AlertType.INFORMATION);
                    fundMessage.setTitle("Property Tycoon Selling Management");
                    fundMessage.setHeaderText("You have raised enough fund");
                    fundMessage.showAndWait();
                    sellHouseHotelPopUpStage.close();
                    fundMessage.close();
                }
            }

        });
        back.setOnAction(e -> {
            sellHouseHotelPopUpStage.close();
        });

        optionPane.getChildren().addAll(sellBuilding, back);
        sellHouseHotelPane.getChildren().addAll(title, remainingFundLeft, playerAssetMessage, scrollForAssetInformation, optionPane);
        assetSellingHouseScene = new Scene(sellHouseHotelPane);
        // Creating the popup effect
        sellHouseHotelPopUpStage.setScene(assetSellingHouseScene);
        sellHouseHotelPopUpStage.initModality(Modality.APPLICATION_MODAL);
        sellHouseHotelPopUpStage.showAndWait();
        sellHouseHotelPopUpStage.close();
    }

    /***
     * Allow player to mortgage their properties to raise fund
     * @param currentPlayer The current player
     */
    public void mortgageProperties(Player currentPlayer) {
        Stage mortgagePopUpStage = new Stage();
        VBox mortgagePropertiesPane = new VBox(10);
        mortgagePropertiesPane.setAlignment(Pos.CENTER);
        mortgagePropertiesPane.setPadding(new Insets(0, 20, 10, 20));
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Property Management (Mortgaging Properties)");
        Label playerAssetMessage = new Label(currentPlayer.getName() + " this is all of your properties where you can mortgage");
        Label remainingFundLeft = new Label("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());

        ListView assetList = new ListView();

        Button mortgage = new Button("Mortgage Properties");
        Button back = new Button("Return to Asset Selling Management ");

        // Fetch all of the current player asset
        LinkedList<Object> playerAsset = currentPlayer.getAssets();

        // Going through players assets and add it to list-view (asset = String) if its a property
        for (Object asset : playerAsset) {
            if (asset instanceof Property) {
                // Checking if the property is able to mortgaged and not is not developed
                if (!((Property) asset).isMortgaged() && !((Property) asset).isDeveloped()) {
                    assetList.getItems().add(((Property) asset).getTitle());
                }
            }
        }

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        playerAssetMessage.setStyle("-fx-font-size: 14;");

        // Button Functionality
        mortgage.setOnAction(e -> {
            // Fetch the information that has been selected for the current player
            ObservableList listOfPlayerAsset = assetList.getSelectionModel().getSelectedItems();

            // Finding the Property object with the selected string from list-view
            for (Object item : playerAsset) {
                if (item instanceof Property) {
                    if (((Property) item).getTitle().equals(listOfPlayerAsset.get(0))) {
                        int fundRaised = ((Property) item).mortgageProperty();
                        currentPlayer.deductAmount(-fundRaised);
                        // Give player money from mortgaging property
                        int fundLeft = fundRequired.intValue() - fundRaised;
                        fundRequired = new AtomicInteger(fundLeft);
                    }
                }
            }
            // Refresh Label
            remainingFundLeft.setText("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());
            // If you raised enough fund, you are kicked from from this scene
            if (fundRequired.intValue() <= 0) {
                Alert fundMessage = new Alert(Alert.AlertType.INFORMATION);
                fundMessage.setTitle("Property Tycoon Selling Management");
                fundMessage.setHeaderText("You have raised enough fund");
                fundMessage.showAndWait();
                mortgagePopUpStage.close();
                fundMessage.close();
            }
        });
        back.setOnAction(e -> {
            mortgagePopUpStage.close();
        });

        optionPane.getChildren().addAll(mortgage, back);
        mortgagePropertiesPane.getChildren().addAll(title, remainingFundLeft, playerAssetMessage, assetList, optionPane);
        assetMortgageScene = new Scene(mortgagePropertiesPane);
        // Creating the popup effect
        mortgagePopUpStage.setScene(assetMortgageScene);
        mortgagePopUpStage.initModality(Modality.APPLICATION_MODAL);
        mortgagePopUpStage.showAndWait();
        mortgagePopUpStage.close();
    }

    /***
     * Allow player to sell their properties to raise fund
     * @param currentPlayer The current player
     */
    public void sellProperties(Player currentPlayer) {
        Stage sellPropPopUpStage = new Stage();
        VBox sellPropertiesPane = new VBox(10);
        sellPropertiesPane.setAlignment(Pos.CENTER);
        sellPropertiesPane.setPadding(new Insets(0, 20, 10, 20));
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Property Management (Selling Properties)");
        Label playerAssetMessage = new Label(currentPlayer.getName() + " this is all of your properties where you can sell");
        Label remainingFundLeft = new Label("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());

        ListView assetList = new ListView();

        Button sellProp = new Button("Sell Property");
        Button back = new Button("Return to Asset Selling Management ");

        // Fetch all of the current player asset
        LinkedList<Object> playerAsset = currentPlayer.getAssets();

        // Going through players assets and add it to list-view (asset = String) if its a property, utility and station
        for (Object asset : playerAsset) {
            // Making sure asset is a property or station or utility
            if (!(asset instanceof GetOutOfJail)) {
                if (asset instanceof Property && !((Property) asset).isDeveloped()) {
                    assetList.getItems().add(((Property) asset).getTitle());
                } else if (asset instanceof Utility) {
                    assetList.getItems().add(((Utility) asset).getTitle());
                } else if (asset instanceof Station) {
                    assetList.getItems().add(((Station) asset).getTitle());
                }
            }
        }

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        playerAssetMessage.setStyle("-fx-font-size: 14;");

        // Button Functionality
        sellProp.setOnAction(e -> {
            // Fetch the information that has been selected for the current player
            ObservableList listOfPlayerAsset = assetList.getSelectionModel().getSelectedItems();

            // Sell accordingly to what is selected
            for (Object item : playerAsset) {
                if (item instanceof Property) {
                    if (((Property) item).getTitle().equals(listOfPlayerAsset.get(0))) {
                        int fundRaised = ((Property) item).sellProperty();
                        currentPlayer.deductAmount(-fundRaised);
                        // Give player money from mortgaging property
                        int fundLeft = fundRequired.intValue() - fundRaised;
                        fundRequired = new AtomicInteger(fundLeft);
                    }
                } else if (item instanceof Utility) {
                    if (((Utility) item).getTitle().equals(listOfPlayerAsset.get(0))) {
                        int fundRaised = ((Utility) item).sellUtility();
                        currentPlayer.deductAmount(-fundRaised);
                        // Give player money from mortgaging property
                        int fundLeft = fundRequired.intValue() - fundRaised;
                        fundRequired = new AtomicInteger(fundLeft);
                    }
                } else if (item instanceof Station) {
                    if (((Station) item).getTitle().equals(listOfPlayerAsset.get(0))) {
                        int fundRaised = ((Station) item).sellStation();
                        currentPlayer.deductAmount(-fundRaised);
                        // Give player money from mortgaging property
                        int fundLeft = fundRequired.intValue() - fundRaised;
                        fundRequired = new AtomicInteger(fundLeft);
                    }
                }
            }
            // Refresh label
            remainingFundLeft.setText("£" + fundRequired.intValue() + " fund is required " + currentPlayer.getName());
            // If you raised enough fund, you are kicked from from this scene
            if (fundRequired.intValue() <= 0) {
                Alert fundMessage = new Alert(Alert.AlertType.INFORMATION);
                fundMessage.setTitle("Property Tycoon Selling Management");
                fundMessage.setHeaderText("You have raised enough fund");
                fundMessage.showAndWait();
                sellPropPopUpStage.close();
                fundMessage.close();
            }
        });
        back.setOnAction(e -> {
            sellPropPopUpStage.close();
        });

        optionPane.getChildren().addAll(sellProp, back);
        sellPropertiesPane.getChildren().addAll(title, remainingFundLeft, playerAssetMessage, assetList, optionPane);
        assetSellingScene = new Scene(sellPropertiesPane);
        // Creating the popup effect
        sellPropPopUpStage.setScene(assetSellingScene);
        sellPropPopUpStage.initModality(Modality.APPLICATION_MODAL);
        sellPropPopUpStage.showAndWait();
        sellPropPopUpStage.close();
    }


    /***
     * Collection of options a player can do at end of turn
     * @param currentPlayer The current player
     */
    public void endOfTurnOptionSetupScene(Player currentPlayer) {
        Stage assetManagePopUp = new Stage();
        VBox assetManagementSetupPane = new VBox(10);
        assetManagementSetupPane.setPadding(new Insets(0, 20, 10, 20));
        assetManagementSetupPane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon End of Turn Options");

        Button improveBuilding = new Button("Add buildings");
        Button unMortgage = new Button("Un-Mortgage Properties");
        Button trade = new Button("Trade");
        Button leaveGame = new Button("Leave Game");
        Button endTurn = new Button("End Turn");

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );

        // Button functionality
        improveBuilding.setOnAction(e -> {
            improveBuilding(currentPlayer);
        });
        unMortgage.setOnAction(e -> {
            unMortgageProperty(currentPlayer);
        });
        trade.setOnAction(e -> {
            LinkedList<Player> playerList = new LinkedList<>();
            LinkedList<Player> canTrade = new LinkedList<>();
            for (int i = 0; i < order.size(); i++) {
                if (currentPlayer.getName() != order.get(i).getName()) {
                    playerList.add(order.get(i));
                    if (order.get(i).getAssets().size() != 0) {
                        canTrade.add(order.get(i));
                    }
                }
            }

            if (canTrade.size() != 0) {
                tradingSetupScene(currentPlayer);
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Property Tycoon Trading");
                error.setHeaderText("No one to trade with");
                error.showAndWait();
                error.close();
            }
        });
        leaveGame.setOnAction(e -> {
            if (!currentPlayer.leaveGame()) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Property Tycoon Leaving Decision");
                error.setHeaderText("Voting un-successful");
                error.showAndWait();
                error.close();
            } else {
                summaryTurn(currentPlayer);
                assetManagePopUp.close();
            }
        });
        endTurn.setOnAction(e -> {
            summaryTurn(currentPlayer);
            assetManagePopUp.close();
        });

        optionPane.getChildren().addAll(improveBuilding, unMortgage, trade, leaveGame, endTurn);
        assetManagementSetupPane.getChildren().addAll(title, optionPane);
        assetImproveScene = new Scene(assetManagementSetupPane);

        // Pop Up
        assetManagePopUp.setScene(assetImproveScene);
        assetManagePopUp.initModality(Modality.APPLICATION_MODAL);
        assetManagePopUp.showAndWait();
        assetManagePopUp.close();
    }

    /***
     * Create a scene which allow the player to improve their properties
     * @param currentPlayer
     */
    public void improveBuilding(Player currentPlayer) {
        Stage improveHouseHotelPopUpStage = new Stage();
        VBox improveHouseHotelPane = new VBox(10);
        improveHouseHotelPane.setAlignment(Pos.CENTER);
        improveHouseHotelPane.setPadding(new Insets(0, 20, 10, 20));
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        // Fetch all of the current player asset
        LinkedList<Object> playerAsset = currentPlayer.getAssets();

        Label title = new Label("Property Tycoon Property Management (Buying buildings)");
        Label playerAssetMessage = new Label(currentPlayer.getName() + " this is all of your completed set of properties");


        Button improveBuilding = new Button("Add Building to property");
        Button back = new Button("Back");

        // Creating a table view with the following columns
        TableView assetInformation = new TableView();
        TableColumn column1 = new TableColumn<>("Property Name");
        column1.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn column2 = new TableColumn<>("House number");
        column2.setCellValueFactory(new PropertyValueFactory<>("housesNo"));

        TableColumn column3 = new TableColumn<>("Hotel Number");
        column3.setCellValueFactory(new PropertyValueFactory<>("hotelNo"));

        assetInformation.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        assetInformation.getColumns().addAll(column1, column2, column3);

        // Going through players asset and add it to table-view if its a property and is developed
        for (Object asset : playerAsset) {
            if (asset instanceof Property) {
                // Add asset that is in a complete set to table-view
                if (((Property) asset).isCompletedSet()) {
                    assetInformation.getItems().add(asset);
                }
            }
        }
        // Creating a table view with a vertical scroll bar
        ScrollPane scrollForAssetInformation = new ScrollPane(assetInformation);
        scrollForAssetInformation.setFitToWidth(true);
        scrollForAssetInformation.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        playerAssetMessage.setStyle("-fx-font-size: 14;");

        // Button Functionality
        improveBuilding.setOnAction(e -> {
            // Fetch the information that has been selected for the current player and display it into alert header
            ObservableList listOfPlayerAsset = assetInformation.getSelectionModel().getSelectedItems();
            Property assetObject = (Property) listOfPlayerAsset.get(0);

            Alert improveError = new Alert(Alert.AlertType.ERROR);
            int buildingCost = assetObject.getGroup().getBuildingCost();
            if (currentPlayer.getMoney() >= buildingCost) {
                if (assetObject.canBuyHouse()) {
                    assetObject.purchaseHouse();
                    assetInformation.refresh();
                } else if (assetObject.canBuyHotel()) {
                    assetObject.purchaseHotel();
                    assetInformation.refresh();
                } else {
                    improveError.setTitle("Property Tycoon Improvement Management");
                    improveError.setHeaderText("Building improvement violates regulations");
                    improveError.showAndWait();
                    improveError.close();
                }
            } else {
                improveError.setTitle("Property Tycoon Improvement Management");
                improveError.setHeaderText(currentPlayer.getName() + " insufficient fund");
                improveError.showAndWait();
                improveError.close();
            }

        });
        back.setOnAction(e -> {
            improveHouseHotelPopUpStage.close();
        });

        optionPane.getChildren().addAll(improveBuilding, back);
        improveHouseHotelPane.getChildren().addAll(title, playerAssetMessage, scrollForAssetInformation, optionPane);
        improveScene = new Scene(improveHouseHotelPane);
        // Creating the popup effect
        improveHouseHotelPopUpStage.setScene(improveScene);
        improveHouseHotelPopUpStage.initModality(Modality.APPLICATION_MODAL);
        improveHouseHotelPopUpStage.showAndWait();
        improveHouseHotelPopUpStage.close();
    }

    /***
     * Create a scene which allow the player to un-mortgage their properties
     * @param currentPlayer
     */
    public void unMortgageProperty(Player currentPlayer) {
        Stage unMortgagePopUpStage = new Stage();
        VBox unMortgagePropertiesPane = new VBox(10);
        unMortgagePropertiesPane.setAlignment(Pos.CENTER);
        unMortgagePropertiesPane.setPadding(new Insets(0, 20, 10, 20));
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Property Management (Un-Mortgaging Properties)");

        ListView assetList = new ListView();

        Button unMortgage = new Button("Un-Mortgage Properties");
        Button back = new Button("Back");

        // Fetch all of the current player asset
        LinkedList<Object> playerAsset = currentPlayer.getAssets();

        // Going through players assets and add it to list-view (asset = String) if its a property
        for (Object asset : playerAsset) {
            if (asset instanceof Property) {
                // Checking if the property is mortgaged
                if (((Property) asset).isMortgaged()) {
                    assetList.getItems().add(((Property) asset).getTitle());
                }
            }
        }

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );

        // Button Functionality
        unMortgage.setOnAction(e -> {
            // Fetch the information that has been selected for the current player
            ObservableList listOfPlayerAsset = assetList.getSelectionModel().getSelectedItems();

            // Finding the Property object with the selected string from list-view
            for (Object item : playerAsset) {
                if (item instanceof Property) {
                    if (((Property) item).getTitle().equals(listOfPlayerAsset.get(0))) {
                        if (currentPlayer.getMoney() >= (((Property) item).getCost() / 2)) {
                            ((Property) item).unmortgageProperty();
                            assetList.getItems().remove(((Property) item).getTitle());
                            assetList.refresh();
                        } else {
                            Alert unMortError = new Alert(Alert.AlertType.ERROR);
                            unMortError.setTitle("Property Tycoon Management");
                            unMortError.setHeaderText(currentPlayer.getName() + " insufficient fund");
                            unMortError.showAndWait();
                            unMortError.close();
                        }
                    }
                }
            }
        });
        back.setOnAction(e -> {
            unMortgagePopUpStage.close();
        });

        optionPane.getChildren().addAll(unMortgage, back);
        unMortgagePropertiesPane.getChildren().addAll(title, assetList, optionPane);
        unMortgageScene = new Scene(unMortgagePropertiesPane);
        // Creating the popup effect
        unMortgagePopUpStage.setScene(unMortgageScene);
        unMortgagePopUpStage.initModality(Modality.APPLICATION_MODAL);
        unMortgagePopUpStage.showAndWait();
        unMortgagePopUpStage.close();
    }

    /***
     * Create a popup scene where jail decision happens (serve time, bail or use get out of jail card)
     * @param currentPlayer The current player
     * @param jailCard Tell us if the current own a get out of jail card or not
     */
    public void sentToJailSetupScene(Player currentPlayer, GetOutOfJail jailCard) {
        Stage jailPopUpStage = new Stage();
        VBox sentToJailSetupPane = new VBox(10);
        sentToJailSetupPane.setPadding(new Insets(0, 20, 10, 20));
        sentToJailSetupPane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(10);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Jail Decision " + currentPlayer.getName());
        ImageView jailImg = new ImageView("/Lib/TilesDesign/goJail64bit.png");

        Button serveTime = new Button("Serve Time");
        Button bail = new Button("Bail");
        Button getOutCard = new Button("Use Get Out of Jail Card");
        Button help = new Button("Help");

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );

        // Button Functionality
        serveTime.setOnAction(e -> {
            // Close the popup effect
            jailPopUpStage.close();
        });
        bail.setOnAction(e -> {
            currentPlayer.deductAmount(50);
            currentPlayer.leaveJail();
            jailPopUpStage.close();
        });
        getOutCard.setOnAction(e -> {
            // Player activated the get out of jail card
            jailCard.playCard();
            jailPopUpStage.close();
        });
        help.setOnAction(e -> {
            Alert jailHelpMessage = new Alert(Alert.AlertType.INFORMATION);
            jailHelpMessage.setTitle("Property Tycoon Jail Reminder");
            jailHelpMessage.setHeaderText("Remember once you make a decision you cannot reverse it");
            jailHelpMessage.setContentText("So choose wisely " + currentPlayer.getName());
            jailHelpMessage.show();
        });

        // If current player has a get out of jail card, show the options if not dont show the options
        if (jailCard == null && currentPlayer.getMoney() >= 50) {
            optionPane.getChildren().addAll(serveTime, bail, help);
        } else if (jailCard != null && currentPlayer.getMoney() >= 50) {
            optionPane.getChildren().addAll(serveTime, bail, getOutCard, help);
        } else if (jailCard != null && currentPlayer.getMoney() < 50) {
            optionPane.getChildren().addAll(serveTime, getOutCard, help);
        } else if (jailCard == null && currentPlayer.getMoney() < 50) {
            optionPane.getChildren().addAll(serveTime, help);
        }

        sentToJailSetupPane.getChildren().addAll(title, jailImg, optionPane);
        jailSetupScene = new Scene(sentToJailSetupPane);
        // Creating the popup effect
        jailPopUpStage.setScene(jailSetupScene);
        jailPopUpStage.initModality(Modality.APPLICATION_MODAL);
        jailPopUpStage.showAndWait();
        jailPopUpStage.close();
    }

    /***
     * Create a alert message which congratulate the winner
     * @param winner Last remaining player in order
     */
    public void winnerSetupScene(Player winner) {
        Stage winnerMessagePopUp = new Stage();
        VBox winnerMessagePane = new VBox(10);
        winnerMessagePane.setPadding(new Insets(0, 20, 10, 20));
        winnerMessagePane.setAlignment(Pos.CENTER);
        HBox optionPane = new HBox(5);
        optionPane.setAlignment(Pos.CENTER);

        Label title = new Label("Property Tycoon Winner!");
        Label winMessage = new Label(winner.getName() + " has won the game with a net worth of " + winner.netWorth());

        Button stats = new Button("Show Stats");
        Button endGame = new Button("End Game");

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );
        winMessage.setStyle("-fx-font-size: 14;");

        // Button Functionality
        stats.setOnAction(e -> {
            endGameSummary();
        });

        endGame.setOnAction(e -> {
            window.setScene(menuScene);
            window.show();
            winnerMessagePopUp.close();
        });

        optionPane.getChildren().addAll(stats, endGame);
        winnerMessagePane.getChildren().addAll(title, winMessage, optionPane);
        winnerSetup = new Scene(winnerMessagePane);
        // Creating the popup effect
        winnerMessagePopUp.setScene(winnerSetup);
        winnerMessagePopUp.initModality(Modality.APPLICATION_MODAL);
        winnerMessagePopUp.showAndWait();
        winnerMessagePopUp.close();
    }

    /***
     * Function to display all of the actions that they have taken (actionLog)
     * @param currentPlayer The current player
     */
    public void summaryTurn(Player currentPlayer) {
        Stage summaryPopUp = new Stage();
        VBox summaryTurnPane = new VBox(10);
        summaryTurnPane.setAlignment(Pos.CENTER);
        summaryTurnPane.setPadding(new Insets(0, 20, 10, 20));

        Label title = new Label(currentPlayer.getName() + " turn summary");

        Text summaryTxt = new Text();
        summaryTxt.setText(currentPlayer.getActionLog());
        summaryTxt.setDisable(true);

        Button nextTurn = new Button("Next Turn");

        // CSS
        title.setStyle(
                "-fx-label-padding: 20 0 10 0;" + "-fx-font-size: 14;" + "-fx-font-weight: bold;"
        );

        // Button Functionality
        nextTurn.setOnAction(e -> {
            summaryPopUp.close();
        });

        summaryTurnPane.getChildren().addAll(title, summaryTxt, nextTurn);
        summaryScene = new Scene(summaryTurnPane);
        // Creating the popup effect
        summaryPopUp.setScene(summaryScene);
        summaryPopUp.initModality(Modality.APPLICATION_MODAL);
        summaryPopUp.showAndWait();
        summaryPopUp.close();
    }

    /***
     * Initializing the game board and it's deck's of cards
     * @param gameMode A string which initialize the game to be either full or abridged
     */
    public void createBoard(String gameMode) {
        Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
        Deque<CardEffect> opp;

        try {
            board = Json.fromJsonToTileSet("BoardTileData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add cards to decks
        try {
            opportunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
            potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Shuffles order
        Collections.shuffle(opportunityKnocksPack);
        Collections.shuffle(potLuckPack);

        //Load shuffled pack into decks
        pot = new ArrayDeque<>(potLuckPack);
        opp = new ArrayDeque<>(opportunityKnocksPack);
        gameSystem = new Board(order, board, pot, opp, gameMode, this);

    }

    /***
     * Initializing the game board and it's deck's of cards
     * @param gameMode A string which initialize the game to be either full or abridged
     * @param timeLimit the time limit in minuets
     */
    public void createBoard(String gameMode, int timeLimit) {
        Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
        Deque<CardEffect> opp;

        try {
            board = Json.fromJsonToTileSet("BoardTileData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add cards to decks
        try {
            opportunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
            potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Shuffles order
        Collections.shuffle(opportunityKnocksPack);
        Collections.shuffle(potLuckPack);

        //Load shuffled pack into decks
        pot = new ArrayDeque<>(potLuckPack);
        opp = new ArrayDeque<>(opportunityKnocksPack);
        gameSystem = new Board(order, board, pot, opp, gameMode, timeLimit, this);

    }

    /**
     * Creates the required popup to allow a player to buy an asset or to send it to auction instead
     */
    public boolean assetBuyingScene(String title, int cost, Player currentPlayer) {
        boolean decision = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Property Tycoon");
        alert.setHeaderText(currentPlayer.getActionLog());
        alert.setContentText("Do you want to buy this for £" + cost);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            decision = true;
        } else {
            decision = false;
        }
        return (decision);
    }

    /**
     * Creates an alert box that ask the player whether they want to pay the tax or draw an opportunity knocks card
     *
     * @return Player's decision on whether to pay tax or draw opportunity knock card
     */
    public boolean taxOrDrawScreen() {
        boolean decision = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Property Tycoon");
        alert.setHeaderText("You've landed on pay tax or Opportunity knocks ");
        alert.setContentText("Do you want to pay the tax or cancel and draw an Oppotunity Knocks card instead?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            decision = true;
        } else {
            decision = false;
        }
        return (decision);
    }

    /**
     * Get confirmation from current player on whether if the player who wishes to leave can or not?
     *
     * @param currentPlayer The current player being asked.
     * @param leavingPlayer The player who is wishing to leave the game.
     * @return The decision from current player on if leaving player can leave.
     */
    public boolean getLeavePermission(Player currentPlayer, Player leavingPlayer) {
        boolean decision = true;
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Property Tycoon");
        alert.setHeaderText(currentPlayer.getName() + ", " + " do you agree?");
        alert.setContentText("Are you ok with " + leavingPlayer.getName() + " leaving the game early?");
        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No");

        alert.getButtonTypes().addAll(yes, no);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yes) {
            decision = true;
        } else if (result.get() == no) {
            decision = false;
        }
        return (decision);
    }

    /**
     * gets confirmation from the user whether they want to leave teh game or not
     *
     * @param currentPlayer the player asking to leave the game.
     * @return the decision to leave game or not
     */
    public boolean leaveConfirmationAlert(Player currentPlayer) {
        boolean decision;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Property Tycoon");
        alert.setHeaderText(currentPlayer.getName() + " leave confirmation");
        alert.setContentText("Are you sure you want to leave the game? Note that you can only leave with the " +
                "permission with all of the rest of the players.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            decision = true;
        } else {
            decision = false;
        }
        return (decision);
    }

    /***
     * Creates end game summary screen
     */
    private void endGameSummary() {
        Stage graphStage = new Stage();
        graphStage.setTitle("Game Summary");

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Turns");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Net-Worth");

        //define setup
        VBox root = new VBox();
        Scene scene = new Scene(root, 800, 600);
        //align
        root.setAlignment(Pos.CENTER);
        //create line chart
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        //graph styling
        xAxis.setTickUnit(1);
        lineChart.setTitle("Net-Worth Over Turns");
        lineChart.setCreateSymbols(false);

        //defining a series
        for (String key : gameSystem.dataStore.keySet()) {
            //for each player
            XYChart.Series series = new XYChart.Series();
            series.setName(key);
            for (int ii = 0; ii < gameSystem.dataStore.get(key).size(); ii++) {
                //for each turn:net worth pair
                series.getData().add(new XYChart.Data<>(gameSystem.dataStore.get(key).get(ii).getKey(),
                        gameSystem.dataStore.get(key).get(ii).getValue()));
            }
            lineChart.getData().add(series);
        }
        root.getChildren().add(lineChart);

        //other stats
        Text text = new Text("Turns taken: " + gameSystem.turns);
        root.getChildren().add(text);
        Text timing = new Text("Length of game: " + gameSystem.timeElapsed + "ms");
        root.getChildren().add(timing);

        graphStage.setScene(scene);
        graphStage.sizeToScene();
        graphStage.show();
    }

    /***
     * How the game is run
     */
    public void gameLoop() {
        gameSystem.startGameTimer();
        gameSystem.checkSimulated();
        int retirePoint = 300;
        //displayAsString();
        Collections.shuffle(gameSystem.turnOrder);
        // graph
        for (Player p : gameSystem.turnOrder) {
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
                    if (!gameSystem.isAIGame) {
                        summaryTurn(currentPlayer);
                    }
                    continue;//move to next turn
                }
                int count = 0;
                do {
                    count++;
                    // Dice rolling the same number
                    gameSystem.repeat = false;
                    // Get dice roll input from player (not AI)
                    if (!currentPlayer.isAiAgent()) {
                        // Player click roll dice from alert
                        diceRollMessage(currentPlayer, count);
                    } else {
                        gameSystem.roll(currentPlayer, count);
                    }

                    gameSystem.tiles.get(currentPlayer.getCurrentPos()).activeEffect(currentPlayer);
                    //currentPlayer.passGo();
                    if (gameSystem.turnOrder.contains(currentPlayer) && !currentPlayer.isInJail()) {
                        if (!currentPlayer.isAiAgent()) {
                            endOfTurnOptionSetupScene(currentPlayer);
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
                    // roll again
                } while (gameSystem.repeat);
                if (currentPlayer.isAiAgent() && !gameSystem.isAIGame) {
                    summaryTurn(currentPlayer);
                }
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