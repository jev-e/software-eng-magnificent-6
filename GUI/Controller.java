package GUI;

import ClassStructure.*;
import javafx.fxml.Initializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

public class Controller implements Initializable {

    @FXML
    Label setupTitle;

    private static int totalPlayerSize = 0;
    private static int playerSize = 0;
    private static int aiSize = 0;
    private static int randomSeed = 0;

    // Player setup (choosing tokens and players name)
    private Spinner spinP1;
    private Spinner spinP2;
    private Spinner spinP3;
    private Spinner spinP4;
    private Spinner spinP5;
    private Spinner spinP6;

    private TextField txtP1;
    private TextField txtP2;
    private TextField txtP3;
    private TextField txtP4;
    private TextField txtP5;
    private TextField txtP6;

    // AI setup (name randomly chosen for each Ai)
    private Spinner spinAI1;
    private Spinner spinAI2;
    private Spinner spinAI3;
    private Spinner spinAI4;
    private Spinner spinAI5;
    private Spinner spinAI6;

    private TextField txtAI1;
    private TextField txtAI2;
    private TextField txtAI3;
    private TextField txtAI4;
    private TextField txtAI5;
    private TextField txtAI6;

    // Players name and token that they have chosen
    private String nameP1;
    private String nameP2;
    private String nameP3;
    private String nameP4;
    private String nameP5;
    private String nameP6;

    private String tokenP1;
    private String tokenP2;
    private String tokenP3;
    private String tokenP4;
    private String tokenP5;
    private String tokenP6;

    // AIs name and token that they have been assigned
    private String nameAI1;
    private String nameAI2;
    private String nameAI3;
    private String nameAI4;
    private String nameAI5;
    private String nameAI6;

    private String tokenAI1;
    private String tokenAI2;
    private String tokenAI3;
    private String tokenAI4;
    private String tokenAI5;
    private String tokenAI6;

    // Vbox containing the approriate spinner and textfield for each player and AI
    private static VBox playerAmount = new VBox(10);
    private static VBox aiAmount = new VBox(10);

    private ComboBox gameMode;
    private String mode;
    // Button in setup scene (When clicked, switch from setup scene to board scene)
    private Button generateGame = new Button("Generate Game");

    // Callum's Additions

    private Group[][] tiles;
    private Image bootTokenPNG;
    private Image catTokenPNG;
    private Image gobletTokenPNG;
    private Image hatstandTokenPNG;
    private Image smartphoneTokenPNG;
    private Image spoonTokenPNG;

    private static HashMap<Integer, BoardTile> board = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();
    private static List<CardEffect> oppourtunityKnocksPack;
    private static List<CardEffect> potLuckPack;


    @FXML
    private AnchorPane gameAP = new AnchorPane();    // The Anchor Pane Holding Everything
    @FXML
    private BorderPane gameBP = new BorderPane();    // The Border Pane Holding Everything
    @FXML
    private GridPane gameGP = new GridPane();        // The 3x3 Grid Pane

    @FXML
    public GridPane topRowGP = new GridPane();      // The 9 x 1 Grid Pane Representing the Top Row
    @FXML
    private GridPane botRowGP = new GridPane();      // The 9 x 1 Grid Pane Representing the Bottom Row
    @FXML
    private GridPane leftColumnGP = new GridPane();  // The 1 x 9 Grid Pane Representing the Left Column
    @FXML
    private GridPane rightColumnGP = new GridPane(); // The 1 x 9 Grid Pane Representing the Right Column

    @FXML
    private ImageView jailTile = new ImageView();    // The imageview for the JailTile
    @FXML
    private ImageView goTile = new ImageView();
    @FXML
    private ImageView goToJailTile = new ImageView();
    @FXML
    private ImageView freeParkingTile = new ImageView();


    //Canvases .... The long way

    // Bottom Row of Canvases
    @FXML
    private Canvas tile1Colour = new Canvas();
    @FXML
    private Canvas tile3Colour = new Canvas();
    @FXML
    private Canvas tile6Colour = new Canvas();
    @FXML
    private Canvas tile8Colour = new Canvas();
    @FXML
    private Canvas tile9Colour = new Canvas();

    // Left Column of Canvases
    @FXML
    private Canvas tile12Colour = new Canvas();
    @FXML
    private Canvas tile14Colour = new Canvas();
    @FXML
    private Canvas tile15Colour = new Canvas();
    @FXML
    private Canvas tile17Colour = new Canvas();
    @FXML
    private Canvas tile19Colour = new Canvas();
    @FXML
    private Canvas tile20Colour = new Canvas();

    // Top Row of Canvases
    @FXML
    private Canvas tile21Colour = new Canvas();
    @FXML
    private Canvas tile23Colour = new Canvas();
    @FXML
    private Canvas tile24Colour = new Canvas();
    @FXML
    private Canvas tile26Colour = new Canvas();
    @FXML
    private Canvas tile27Colour = new Canvas();
    @FXML
    private Canvas tile29Colour = new Canvas();

    // Right Column of Canvases
    @FXML
    private Canvas tile32Colour = new Canvas();
    @FXML
    private Canvas tile33Colour = new Canvas();
    @FXML
    private Canvas tile35Colour = new Canvas();
    @FXML
    private Canvas tile38Colour = new Canvas();
    @FXML
    private Canvas tile40Colour = new Canvas();

    private Canvas[] tileColours;

    // This is called every time a new scene is built
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Label used in setup scene for the title
        setupTitle = new Label();
        setupTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        setupTitle.setText("Property Tycoon Setup\n");

        //loadGameAssets(); // Loads images onto gameboard
        //displayTopRow(); // Display the top tiles

        /**
        tileColours = new Canvas[22];

        // Bottom Row of Tiles
        tileColours[0] = tile1Colour;
        tileColours[1] = tile3Colour;
        tileColours[2] = tile6Colour;
        tileColours[3] = tile8Colour;
        tileColours[4] = tile9Colour;

        // Left Column of Tiles
        tileColours[5] = tile12Colour;
        tileColours[6] = tile14Colour;
        tileColours[7] = tile15Colour;
        tileColours[8] = tile17Colour;
        tileColours[9] = tile19Colour;
        tileColours[10] = tile20Colour;

        // Top Row of Tiles
        tileColours[11] = tile21Colour;
        tileColours[12] = tile23Colour;
        tileColours[13] = tile24Colour;
        tileColours[14] = tile26Colour;
        tileColours[15] = tile27Colour;
        tileColours[16] = tile29Colour;

        // Right Column of Tiles
        tileColours[17] = tile32Colour;
        tileColours[18] = tile33Colour;
        tileColours[19] = tile35Colour;
        tileColours[20] = tile38Colour;
        tileColours[21] = tile40Colour;


        for (int i = 0; i < 5; i++) {
            Canvas tempCanvas = tileColours[i];
            GraphicsContext gc = tempCanvas.getGraphicsContext2D();
            gc.setFill(Color.RED);
            gc.fillRect(0, 0, tempCanvas.getWidth(), 15);
        }

        for (int i = 5; i < 11; i++) {
            Canvas tempCanvas = tileColours[i];
            GraphicsContext gc = tempCanvas.getGraphicsContext2D();
            gc.setFill(Color.ORANGE);
            gc.fillRect((tempCanvas.getWidth() - 15), 0, 15, tempCanvas.getHeight());
        }

        for (int i = 11; i < 17; i++) {
            Canvas tempCanvas = tileColours[i];
            GraphicsContext gc = tempCanvas.getGraphicsContext2D();
            gc.setFill(Color.BLUE);
            gc.fillRect(0, (tempCanvas.getHeight() - 15), tempCanvas.getWidth(), 15);
        }

        for (int i = 17; i < 22; i++) {
            Canvas tempCanvas = tileColours[i];
            GraphicsContext gc = tempCanvas.getGraphicsContext2D();
            gc.setFill(Color.GREEN);
            gc.fillRect(0, 0, 15, tempCanvas.getHeight());
        }
         **/

        // CSS for the generate game button
        generateGame.setPrefSize(170, 30);
    }

    /***
     * Change to the auctioning popup screen if 'auction' has been clicked from buyProperty()
     * @throws IOException
     */
    public void auctionSceneChange() throws IOException {
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

        if (unownedProperty == true) {
            Alert propertyMessage = new Alert(AlertType.NONE);
            propertyMessage.setHeaderText("Do you want to buy " + propertyName + "?");
            propertyMessage.setContentText(propertyName + " would cost £" + propertyValue);
            propertyMessage.getButtonTypes().addAll(buyProperty, auctionProperty);
            Optional<ButtonType> option = propertyMessage.showAndWait();

            // Implement with logic!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (option.get() == buyProperty) {
                // After clicking the "Buy Property" button from the popup
                System.out.println("you Payed $500");
                System.out.println(nameP1);
                System.out.println(nameP2);
            } else if (option.get() == auctionProperty) {
                // After clicking the "Auction" button from the popup
                System.out.println("Auction");
                auctionSceneChange();
            }
        } else {
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
        if (diceOne == diceTwo) {
            Alert diceMessage = new Alert(AlertType.NONE);
            diceMessage.setTitle("Dice Generated");
            diceMessage.setHeaderText("Your first dice rolled a " + diceOne + " and the second rolled a " + diceTwo);
            diceMessage.setContentText("Roll again");
            // IMPLEMENT LATER OPTION TO BUY
            //buyProperty();
            diceMessage.getButtonTypes().add(rollAgain);
            Optional<ButtonType> option = diceMessage.showAndWait();
            // After clicking the "Reroll" button from the popup
            if (option.get() == rollAgain) {
                // function to roll again implement later
                Alert reroll = new Alert(AlertType.INFORMATION);
                reroll.setTitle("Dice Generated");
                reroll.setHeaderText("Reroll test");
                reroll.show();
            }
        } else { // dice one and dice two != same
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
    public void setPlayerInfo() {
        switch (playerSize) {
            case 1:
                // Assign player ones name and tokens that they have selected
                nameP1 = txtP1.getText();
                tokenP1 = (String) spinP1.getValue();
                break;
            case 2:
                nameP1 = txtP1.getText();
                tokenP1 = (String) spinP1.getValue();
                nameP2 = txtP2.getText();
                tokenP2 = (String) spinP2.getValue();
                break;
            case 3:
                nameP1 = txtP1.getText();
                tokenP1 = (String) spinP1.getValue();
                nameP2 = txtP2.getText();
                tokenP2 = (String) spinP2.getValue();
                nameP3 = txtP3.getText();
                tokenP3 = (String) spinP3.getValue();
                break;
            case 4:
                nameP1 = txtP1.getText();
                tokenP1 = (String) spinP1.getValue();
                nameP2 = txtP2.getText();
                tokenP2 = (String) spinP2.getValue();
                nameP3 = txtP3.getText();
                tokenP3 = (String) spinP3.getValue();
                nameP4 = txtP4.getText();
                tokenP4 = (String) spinP4.getValue();
                break;
            case 5:
                nameP1 = txtP1.getText();
                tokenP1 = (String) spinP1.getValue();
                nameP2 = txtP2.getText();
                tokenP2 = (String) spinP2.getValue();
                nameP3 = txtP3.getText();
                tokenP3 = (String) spinP3.getValue();
                nameP4 = txtP4.getText();
                tokenP4 = (String) spinP4.getValue();
                nameP5 = txtP5.getText();
                tokenP5 = (String) spinP5.getValue();
                break;
            case 6:
                nameP1 = txtP1.getText();
                tokenP1 = (String) spinP1.getValue();
                nameP2 = txtP2.getText();
                tokenP2 = (String) spinP2.getValue();
                nameP3 = txtP3.getText();
                tokenP3 = (String) spinP3.getValue();
                nameP4 = txtP4.getText();
                tokenP4 = (String) spinP4.getValue();
                nameP5 = txtP5.getText();
                tokenP5 = (String) spinP5.getValue();
                nameP6 = txtP6.getText();
                tokenP6 = (String) spinP6.getValue();
                break;
            default:
                break;
        }
    }

    /***
     * Used to set the ais name and tokens that they have been assigned to the appropriate variable
     */
    public void setAiInfo() {
        switch (aiSize) {
            case 1:
                // Assign player ones name and tokens that they have selected
                nameAI1 = txtAI1.getText();
                tokenAI1 = (String) spinAI1.getValue();
                break;
            case 2:
                nameAI1 = txtAI1.getText();
                tokenAI1 = (String) spinAI1.getValue();
                nameAI2 = txtAI2.getText();
                tokenAI2 = (String) spinAI2.getValue();
                break;
            case 3:
                nameAI1 = txtAI1.getText();
                tokenAI1 = (String) spinAI1.getValue();
                nameAI2 = txtAI2.getText();
                tokenAI2 = (String) spinAI2.getValue();
                nameAI3 = txtAI3.getText();
                tokenAI3 = (String) spinAI3.getValue();
                break;
            case 4:
                nameAI1 = txtAI1.getText();
                tokenAI1 = (String) spinAI1.getValue();
                nameAI2 = txtAI2.getText();
                tokenAI2 = (String) spinAI2.getValue();
                nameAI3 = txtAI3.getText();
                tokenAI3 = (String) spinAI3.getValue();
                nameAI4 = txtAI4.getText();
                tokenAI4 = (String) spinAI4.getValue();
                break;
            case 5:
                nameAI1 = txtAI1.getText();
                tokenAI1 = (String) spinAI1.getValue();
                nameAI2 = txtAI2.getText();
                tokenAI2 = (String) spinAI2.getValue();
                nameAI3 = txtAI3.getText();
                tokenAI3 = (String) spinAI3.getValue();
                nameAI4 = txtAI4.getText();
                tokenAI4 = (String) spinAI4.getValue();
                nameAI5 = txtAI5.getText();
                tokenAI5 = (String) spinAI5.getValue();
                break;
            case 6:
                nameAI1 = txtAI1.getText();
                tokenAI1 = (String) spinAI1.getValue();
                nameAI2 = txtAI2.getText();
                tokenAI2 = (String) spinAI2.getValue();
                nameAI3 = txtAI3.getText();
                tokenAI3 = (String) spinAI3.getValue();
                nameAI4 = txtAI4.getText();
                tokenAI4 = (String) spinAI4.getValue();
                nameAI5 = txtAI5.getText();
                tokenAI5 = (String) spinAI5.getValue();
                nameAI6 = txtAI6.getText();
                tokenAI6 = (String) spinAI6.getValue();
                break;
            default:
                break;
        }
    }

    /**
     * Switch scene from setup when "Generate Game" button is clicked
     *
     * @param event When "generate game"" button is clicked, switch scene from setup to the game board scene
     * @throws IOException
     */
    public void setupToBoardScene(ActionEvent event) throws IOException {
        Parent gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoardScene.fxml"));
        Scene gameScene = new Scene(gameBoardParent);

        System.out.println("Before Scene changed to board");

        // Grab stage information and change scene to setup
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gameScene);
        //window.setFullScreen(true);
        window.show();

        System.out.println("Board Scene Shown");

    }

    /**
     * Recreate the same scene but adding a addition spinner and textfield (add spinner and textfield for the player)
     *
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
                System.out.println("Add Player");
                // Fetch and see if player chose the normal game version or abridged
                mode = gameMode.getValue().toString();
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
        initializeGameMode();

        if (totalPlayerSize == playerSize && playerSize < 2) {
            // Check if only players has been added
            setup.getChildren().addAll(
                    setupTitle, playerAmount, setupParent
            );
        } else if (totalPlayerSize == playerSize && playerSize != 6) {
            // Check if only players has been added and if player size > 2 allow the game to start
            setup.getChildren().addAll(
                    setupTitle, playerAmount, gameMode, generateGame, setupParent
            );
        } else if (totalPlayerSize == playerSize && totalPlayerSize == 6) {
            // Check if only players has been added and if player size == 6, then disable add players and AI button
            setup.getChildren().addAll(
                    setupTitle, playerAmount, gameMode, generateGame
            );
        } else if (totalPlayerSize != playerSize && totalPlayerSize < 6) {
            // Check if both AIs and players has been added and allow the options to add in more players and AIs
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame, setupParent
            );
        } else if (totalPlayerSize != playerSize && totalPlayerSize == 6) {
            // Check if AIs and players has been added and if totalPlayerSize == 6, then disable add players and AI button
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame
            );
        }
        //String bob = gameMode.getValue().toString(); //!!!! assigned to a variable later on (value converted to string atm)
        Scene setupScene = new Scene(setup, 370, 420);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(setupScene);
        window.show();
    }

    /**
     * Recreate the same scene but adding a addition spinner and textfield (add spinner and textfield for the AI)
     *
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
        aiAmount = addAISetup();
        aiAmount.setAlignment(Pos.TOP_CENTER);
        // Create and initialize gamemode (either normal or abridge)
        initializeGameMode();

        if (totalPlayerSize == aiSize && aiSize < 2) {
            setup.getChildren().addAll(
                    setupTitle, aiAmount, setupParent
            );
        } else if (totalPlayerSize == aiSize && aiSize != 6) {
            setup.getChildren().addAll(
                    setupTitle, aiAmount, gameMode, generateGame, setupParent
            );
        } else if (totalPlayerSize == aiSize && totalPlayerSize == 6) {
            setup.getChildren().addAll(
                    setupTitle, aiAmount, gameMode, generateGame
            );
        } else if (totalPlayerSize != aiSize && totalPlayerSize < 6) {
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame, setupParent
            );
        } else if (totalPlayerSize != playerSize && totalPlayerSize == 6) {
            setup.getChildren().addAll(
                    setupTitle, playerAmount, aiAmount, gameMode, generateGame
            );
        }
        Scene setupScene = new Scene(setup, 370, 420);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
                "Boot", "Smartphone", "Goblet", "Hatstand", "Cat", "Spoon"
        );

        switch (playerSize) {
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
                alignPlayer.getChildren().addAll(playerSetup1, playerSetup2);
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
                alignPlayer.getChildren().addAll(playerSetup1, playerSetup2, playerSetup3);
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
                alignPlayer.getChildren().addAll(playerSetup1, playerSetup2, playerSetup3, playerSetup4);
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
                        playerSetup1, playerSetup2, playerSetup3, playerSetup4, playerSetup5
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
                        playerSetup1, playerSetup2, playerSetup3, playerSetup4, playerSetup5, playerSetup6
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
        if (aiSize == 1) {
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
                "Boot", "Smartphone", "Goblet", "Hatstand", "Cat", "Spoon"
        );

        switch (aiSize) {
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
                alignAI.getChildren().addAll(aiSetup1, aiSetup2);
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
                alignAI.getChildren().addAll(aiSetup1, aiSetup2, aiSetup3);
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
                alignAI.getChildren().addAll(aiSetup1, aiSetup2, aiSetup3, aiSetup4);
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
                        aiSetup1, aiSetup2, aiSetup3, aiSetup4, aiSetup5
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
                        aiSetup1, aiSetup2, aiSetup3, aiSetup4, aiSetup5, aiSetup6
                );
                break;
        }
        return alignAI;
    }

    /***
     * Initialize combo box with the elements normal and abridged but default value to normal game mode
     */
    public void initializeGameMode() {

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
    public void initializePlayer() {
        // Initializing players' spinner and textfield
        spinP1 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP1 = new TextField();
        txtP1.setStyle("-fx-font-size: 14px");
        txtP1.setPromptText("Enter players' 1 name");

        spinP2 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP2 = new TextField();
        txtP2.setStyle("-fx-font-size: 14px");
        txtP2.setPromptText("Enter players' 2 name");

        spinP3 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP3.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP3 = new TextField();
        txtP3.setStyle("-fx-font-size: 14px");
        txtP3.setPromptText("Enter players' 3 name");

        spinP4 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP4 = new TextField();
        txtP4.setStyle("-fx-font-size: 14px");
        txtP4.setPromptText("Enter players' 4 name");

        spinP5 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP5.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP5 = new TextField();
        txtP5.setStyle("-fx-font-size: 14px");
        txtP5.setPromptText("Enter players' 5 name");

        spinP6 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinP6.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtP6 = new TextField();
        txtP6.setStyle("-fx-font-size: 14px");
        txtP6.setPromptText("Enter players' 6 name");
    }

    /***
     * Initialize AIs spinner and textfield with a default value
     */
    public void initializeAI(int randomSeed) {
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

        spinAI1 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI1 = new TextField();
        txtAI1.setStyle("-fx-font-size: 14px");
        txtAI1.setText(tempName);
        // Remove the AI's name that been assigned so no duplicate name at one time
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI2 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI2 = new TextField();
        txtAI2.setStyle("-fx-font-size: 14px");
        txtAI2.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI3 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI3.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI3 = new TextField();
        txtAI3.setStyle("-fx-font-size: 14px");
        txtAI3.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI4 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI4 = new TextField();
        txtAI4.setStyle("-fx-font-size: 14px");
        txtAI4.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI5 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI5.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI5 = new TextField();
        txtAI5.setStyle("-fx-font-size: 14px");
        txtAI5.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI6 = new Spinner<String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI6.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        txtAI6 = new TextField();
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

        setup.getChildren().addAll(setupTitle, setupParent);
        Scene setupScene = new Scene(setup);

        // Grab stage information and change scene to setup
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(setupScene);
        window.show();
    }

    /**
     * @param event When "main menu" button is clicked, switch scene from rules to main menu
     */
    public void ruleToMainScene(ActionEvent event) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene mainScene = new Scene(mainParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    /**
     * @param event When "rules" button is clicked, switch scene from main menu to rules
     */
    public void mainToRuleScene(ActionEvent event) throws IOException {
        Parent ruleParent = FXMLLoader.load(getClass().getResource("ruleScene.fxml"));
        Scene ruleScene = new Scene(ruleParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ruleScene);
        window.show();
    }

    /**
     * @param event When "exit" button is clicked, exit application
     */
    public void quit(ActionEvent event) {
        Platform.exit();
    }


    /*******************************************************************************************************************
     *
     * Callum's Functions
     *
     ******************************************************************************************************************/

    public void displayTopRow() {
        for (int i = 0; i < 9; i++) {
            if ((i != 1) && (i != 4) && (i != 7)) {
                addGroupToRowGP(topRowGP, i);
            }
        }
    }


    private void addGroupToRowGP(GridPane rowGP, int i) {

        System.out.println("add Group to Row GP Called. " + i);

        Group group = new Group();
        GridPane contents = new GridPane();
        GridPane tokens = new GridPane();
        Canvas canvas = new Canvas();
        Label propName = new Label("Property Name");
        Label propVal = new Label("Property Value");

        // Canvas Setup
        canvas.setHeight(15);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 100, 100);

        // Token Grid Setup

        ImageView tokenTL = new ImageView(bootTokenPNG);
        ImageView tokenTM = new ImageView(catTokenPNG);
        ImageView tokenTR = new ImageView(gobletTokenPNG);
        ImageView tokenBL = new ImageView(hatstandTokenPNG);
        ImageView tokenBM = new ImageView(smartphoneTokenPNG);
        ImageView tokenBR = new ImageView(spoonTokenPNG);

        // Set size of tokens
        formatTestTokens(tokenTL, tokenTM, tokenTR, tokenBL, tokenBM, tokenBR, 20);

        tokens.setConstraints(tokenTL, 0, 0);
        tokens.setConstraints(tokenTM, 0, 1);
        tokens.setConstraints(tokenTR, 1, 0);
        tokens.setConstraints(tokenBL, 1, 1);
        tokens.setConstraints(tokenBM, 2, 0);
        tokens.setConstraints(tokenBR, 2, 1);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(33);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(33);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(33);

        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(30);
        RowConstraints row2 = new RowConstraints();
        row2.setMinHeight(30);

        tokens.getColumnConstraints().addAll(col1, col2, col3);
        tokens.getRowConstraints().addAll(row1, row2);

        tokens.getChildren().addAll(tokenTL, tokenTM, tokenTR, tokenBL, tokenBM, tokenBR);

        // Set Location of desired contents
        contents.setConstraints(canvas, 0, 0);
        contents.setConstraints(propName, 0, 1);
        contents.setConstraints(tokens, 0, 2);
        contents.setConstraints(propVal, 0, 3);

        // Add contents to the gridPane
        contents.getChildren().addAll(canvas, propName, tokens, propVal);

        // Add the grid to the group
        group.getChildren().add((contents));

        // Add the group to the row / column of gameboard
        rowGP.setConstraints(group, i, 0);
        rowGP.getChildren().add(group);

    }

    /**
     * Loads images for the board, called on initialise
     * - Inefficient, runs too many times
     */
    public void loadGameAssets() {

        System.out.println("Load Game Assets Called.");

        try {
            // Load Tile Images
            Image freeParkingPNG = new Image(new FileInputStream("Lib/TilesDesign/freeParking64bit.png"));
            Image jailPNG = new Image(new FileInputStream("Lib/TilesDesign/visiting64bit.png"));
            Image goToJailPNG = new Image(new FileInputStream("Lib/TilesDesign/goJail64bit.png"));
            Image goPNG = new Image(new FileInputStream("Lib/TilesDesign/goWithColour64bit.png"));

            // Load Token Images
            bootTokenPNG = new Image(new FileInputStream("Lib/Tokens/BootToken.png"));
            catTokenPNG = new Image(new FileInputStream("Lib/Tokens/CatToken.png"));
            gobletTokenPNG = new Image(new FileInputStream("Lib/Tokens/GobletToken.png"));
            hatstandTokenPNG = new Image(new FileInputStream("Lib/Tokens/HatstandToken.png"));
            smartphoneTokenPNG = new Image(new FileInputStream("Lib/Tokens/SmartphoneToken.png"));
            spoonTokenPNG = new Image(new FileInputStream("Lib/Tokens/SpoonToken.png"));

            // Assign corner images to the 4 corner tiles
            freeParkingTile.setImage(freeParkingPNG);
            jailTile.setImage((jailPNG));
            goToJailTile.setImage(goToJailPNG);
            goTile.setImage(goPNG);
        } catch (FileNotFoundException e) {

        }
    }

    private void formatTestTokens(ImageView tl, ImageView tm, ImageView tr, ImageView bl, ImageView bm, ImageView br, int size) {
        tl.setPreserveRatio(true);
        tl.setFitHeight(size);
        tl.setFitHeight(size);
        //tl.setVisible(false);

        tm.setPreserveRatio(true);
        tm.setFitHeight(size);
        tm.setFitHeight(size);
        //tm.setVisible(false);

        tr.setPreserveRatio(true);
        tr.setFitHeight(size);
        tr.setFitHeight(size);
        //tr.setVisible(false);

        bl.setPreserveRatio(true);
        bl.setFitHeight(size);
        bl.setFitHeight(size);
        //bl.setVisible(false);

        bm.setPreserveRatio(true);
        bm.setFitHeight(size);
        bm.setFitHeight(size);
        //bm.setVisible(false);

        br.setPreserveRatio(true);
        br.setFitHeight(size);
        br.setFitHeight(size);
        //br.setVisible(false);
    }

    public void StartGame() {
        //player creation
        Player ayman = new Player("Ayman", Token.HATSTAND);
        Player danny = new Player("Danny", Token.CAT);
        Player jacob = new Player("Jacob", Token.BOOT);
        Player calvin = new Player("Calvin", Token.SMARTPHONE);
        Player callum = new Player("Callum", Token.SPOON);
        Player tom = new Player("Tom", Token.GOBLET);
        //load players into turn order
        order.add(ayman);
        order.add(danny);
        order.add(jacob);
        order.add(calvin);
        order.add(callum);
        order.add(tom);

        Deque<CardEffect> pot = new ArrayDeque<CardEffect>();
        Deque<CardEffect> opp = new ArrayDeque<CardEffect>();
        //add cards to decks

        try {
            board = Json.fromJsonToTileSet("BoardTileData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            oppourtunityKnocksPack = Json.fromJsonToList("OpportunityKnocksCardData.json");
            potLuckPack = Json.fromJsonToList("PotLuckCardData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(oppourtunityKnocksPack);//shuffles order
        Collections.shuffle(potLuckPack);

        pot = new ArrayDeque<>(potLuckPack);//Load shuffled pack into decks
        opp = new ArrayDeque<>(oppourtunityKnocksPack);

        Board b = new Board(order, board, pot, opp);
        b.demo();//run demo method shown in sprint 1 meeting
    }

    private void readDataForBoard() {
        try {
            board = Json.fromJsonToTileSet("BoardTileData.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
