package GUI;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
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

    @FXML Label setupTitle;

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

    private TextField nameP1;
    private TextField nameP2;
    private TextField nameP3;
    private TextField nameP4;
    private TextField nameP5;
    private TextField nameP6;

    // AI setup (name randomly chosen for each Ai)
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


    // Vbox containing the approriate spinner and textfield for each player and AI
    private static VBox playerAmount = new VBox(10);
    private static VBox aiAmount = new VBox(10);

    private ComboBox gameMode;
    private Button generateGame;

    // This is called every time a new scene is built
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Label used in setup scene for the title
        setupTitle = new Label();
        setupTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        setupTitle.setText("Property Tycoon Setup\n");

        // Button in setup scene (When clicked, switch from setup scene to board scene)
        generateGame = new Button("Generate Game");
        // CSS for the generate game button
        generateGame.setPrefSize(170,30);
        // Change to the generated game scene when clicked
        generateGame.setOnAction(e -> {
            // When clicked, switch to game board scene
            try {
                // Fetch and see if player chose the normal game version or abridged
                //String mode = gameMode.getValue().toString();
                //System.out.println(nameP1.getText());
                setupToBoardScene(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    
    /***
     * Change to the auctioning popup screen if 'auction' has been clicked from buyProperty()
     * @throws IOException
     */
    public void auctionSceneChange() throws IOException{
        Parent gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoardScene.fxml"));
        Scene gameScene  = new Scene(gameBoardParent);
        System.out.println(nameP1.getText());

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

    /**
     * Switch scene from setup when "Generate Game" button is clicked
     * @param event When "generate game"" button is clicked, switch scene from setup to the game board scene
     * @throws IOException
     */
    public void setupToBoardScene(ActionEvent event) throws IOException {
        Parent gameBoardParent = FXMLLoader.load(getClass().getResource("gameBoardScene.fxml"));
        Scene gameScene  = new Scene(gameBoardParent);

        // Grab stage information and change scene to setup
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(gameScene);
        //window.setFullScreen(true);
        window.show();
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
                nameAI1.setEditable(false);

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
                // Prevent players from changing the AI's name
                nameAI1.setEditable(false);
                nameAI2.setEditable(false);

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
                // Prevent players from changing the AI's name
                nameAI1.setEditable(false);
                nameAI2.setEditable(false);
                nameAI3.setEditable(false);

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
                // Prevent players from changing the AI's name
                nameAI1.setEditable(false);
                nameAI2.setEditable(false);
                nameAI3.setEditable(false);
                nameAI4.setEditable(false);

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
                // Prevent players from changing the AI's name
                nameAI1.setEditable(false);
                nameAI2.setEditable(false);
                nameAI3.setEditable(false);
                nameAI4.setEditable(false);
                nameAI5.setEditable(false);

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
                // Prevent players from changing the AI's name
                nameAI1.setEditable(false);
                nameAI2.setEditable(false);
                nameAI3.setEditable(false);
                nameAI4.setEditable(false);
                nameAI5.setEditable(false);
                nameAI6.setEditable(false);

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

        spinAI1 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI1 = new TextField();
        nameAI1.setStyle("-fx-font-size: 14px");
        nameAI1.setText(tempName);
        // Remove the AI's name that been assigned so no duplicate name at one time
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI2 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI2 = new TextField();
        nameAI2.setStyle("-fx-font-size: 14px");
        nameAI2.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI3 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI3.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI3 = new TextField();
        nameAI3.setStyle("-fx-font-size: 14px");
        nameAI3.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI4 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI4.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI4 = new TextField();
        nameAI4.setStyle("-fx-font-size: 14px");
        nameAI4.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI5 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI5.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI5 = new TextField();
        nameAI5.setStyle("-fx-font-size: 14px");
        nameAI5.setText(tempName);
        nameAI.remove(randomIndex);

        randomIndex = rand.nextInt(nameAI.size());
        tempName = nameAI.get(randomIndex);
        spinAI6 = new Spinner <String>();
        // Making the arrows on the spinner HORIZONTAL (rather than vertical)
        spinAI6.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        nameAI6 = new TextField();
        nameAI6.setStyle("-fx-font-size: 14px");
        nameAI6.setText(tempName);
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
}
