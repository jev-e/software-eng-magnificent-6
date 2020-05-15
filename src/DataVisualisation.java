package src;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class DataVisualisation extends Application {

    private static HashMap<Integer, BoardTile> board = new HashMap<Integer, BoardTile>();
    private static LinkedList<Player> order = new LinkedList<Player>();
    private static List<CardEffect> opportunityKnocksPack;
    private static List<CardEffect> potLuckPack;
    private Board b;
    private int playerNumber;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        endGameSummary(primaryStage);
    }

    /**
     * Creates end game summary screen
     */
    private void endGameSummary( Stage primaryStage ) {
        primaryStage.setTitle("Game Summary");

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
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        //graph styling
        xAxis.setTickUnit(1);
        lineChart.setTitle("Net-Worth Over Turns");
        lineChart.setCreateSymbols(false);

        //defining a series
        for(String key : b.dataStore.keySet()){
            //for each player
            XYChart.Series series = new XYChart.Series();
            series.setName( key );
            for( int ii = 0; ii < b.dataStore.get(key).size(); ii++ ){
                //for each turn:net worth pair
                series.getData().add(new XYChart.Data<>( b.dataStore.get(key).get(ii).getKey(), b.dataStore.get(key).get(ii).getValue()));
            }
            lineChart.getData().add(series);
        }
        root.getChildren().add(lineChart);

        //other stats
        Text text = new Text("Turns taken: " + b.turns);
        root.getChildren().add(text);
        Text timing = new Text("Length of game: " + b.timeElapsed + "ms");
        root.getChildren().add(timing);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();

        primaryStage.show();
    }

    /**
     * Plays game for demo purpose, needs to happen elsewhere in real thing
     */
    @Override
    public void init() {
            //launch and play the game
            //will need to be moved from init but this GUI only cares about end game state
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

            b = new Board(order, board, pot, opp, "full");
            //player creation
            Player ayman = new Player("Ayman", Token.HATSTAND, b, true);
            Player danny = new Player("Danny", Token.CAT, b, true);
            Player jacob = new Player("Jacob", Token.BOOT, b, true);
            Player calvin = new Player("Calvin", Token.SMARTPHONE, b, true);
            Player callum = new Player("Callum", Token.SPOON, b, true);
            Player tom = new Player("Tom", Token.GOBLET, b, true);
            //load players into turn order
            order.add(ayman);
            order.add(danny);
            order.add(jacob);
            order.add(calvin);
            order.add(callum);
            order.add(tom);
            playerNumber = order.size();

            try {
                //b.testLoop();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An error occurred in game execution");
            }
    }



}
