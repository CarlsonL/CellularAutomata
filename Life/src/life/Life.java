/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


/**
 *
 * @author Carlson
 * Music help from https://stackoverflow.com/questions/6045384/playing-mp3-and-wav-in-java
 * Music file from https://freemidi.org/download2-12160-africa-toto
 */
public class Life extends Application {
    private final double GAME_SPEED_SECS = 0.016667;
    
    private boolean isPaused = true;
    private static final int CELLS_WIDE = 50;
    private static final int CELLS_HIGH = 50;
    
    private String surviveRule = "23";
    private String birthRule = "3";
    
    private Stage mainStage;
    private Scene mainScene;
    
    private Board board;
    
    private Cell[][] cellsMap;
    
    private final TextField S_RULEBOX = new TextField(surviveRule);
    private final TextField B_RULEBOX = new TextField(birthRule);
    //private final Slider cellsWideSlider = new Slider(10, 800, 200);
    //private final Slider cellsHighSlider = new Slider(10, 800, 200);
    
    /*private double getGridWidth() {
        return grid.getWidth();
    }*/
    
    /*private double getGridHeight() {
        return grid.getHeight();
    }*/
    
    /*private double getScalingFactor() {
        return (getGridWidth() + getGridHeight()) / (cellsWide + cellsHigh);
    }*/
    
    private String getSRuleEntry() {
        return S_RULEBOX.getText();
    }
    
    private String getBRuleEntry() {
        return B_RULEBOX.getText();
    }
    
    /*private Position<Integer> getCellAreaBySliders() {
        return new Position<>(
            (int)cellsWideSlider.getValue(),
            (int)cellsHighSlider.getValue());
    }*/
    
    private void setNewRules() {
        surviveRule = getSRuleEntry();
        birthRule = getBRuleEntry();
    }
    
    /*private void setCellAreaBySliders() {
        Position<Integer> cellArea = getCellAreaBySliders();
        
        cellsWide = cellArea.getX();
        cellsHigh = cellArea.getY();
        updateBoard();
    }
    
    private void updateBoard() {
        for (Cell[] row : board) {
            for (Cell c : row) {
                c = null;
                grid.getChildren().remove(c);
            }
        }
        
        board = new Cell[cellsWide][cellsHigh];
        
        for (int i = 0; i < cellsWide; i++)
            for (int j = 0; j < cellsHigh; j++)
                grid.add(board[i][j] = new Cell(), j, i);
    }*/
    
    public boolean cellSurvives(Position<Integer> pos) {
        int liveNeighbs = board.getLiveNeighbs(pos);
        return (surviveRule.indexOf((char)(liveNeighbs)+'0') >= 0);
    }
    
    public boolean cellBornNextGen(Position<Integer> pos) {
        int liveNeighbs = board.getLiveNeighbs(pos);
        return (birthRule.indexOf((char)(liveNeighbs)+'0') >= 0);
    }
    
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setResizable(false);
        mainStage.centerOnScreen();
        
        //Pane to hold cells
        board = new Board(CELLS_WIDE, CELLS_HIGH);
        board.setAlignment(Pos.CENTER);
        cellsMap = board.getCells();
        
        BorderPane root = new BorderPane();
        
        mainScene = new Scene(root, mainStage.getWidth(), mainStage.getHeight());
        
        mainStage.setTitle("Life");
        mainStage.setScene(mainScene);
        
        HBox toolBar = new HBox(5);
        setUpTopBar(toolBar);
        toolBar.setAlignment(Pos.CENTER);
        
        HBox settingsBar = new HBox(5);
        setUpBottomBar(settingsBar);
        settingsBar.setAlignment(Pos.CENTER);
        //setUpSettingsSliders();
        
        root.setTop(toolBar);
        root.setCenter(board); // Polymorphism*
        root.setBottom(settingsBar);
        
        AnimationLoop animationLoop = new AnimationLoop(
                (long)(GAME_SPEED_SECS * 10000000000L), elaspedNS -> {
                    if (!isPaused) {
                        // Find cells that appear in next generation
                        ArrayList<Cell> nextGen = new ArrayList<>();
                        for (int i = 0; i < CELLS_WIDE; i++) {
                            for (int j = 0; j < CELLS_HIGH; j++) {
                                if (cellsMap[i][j].isAlive()) {
                                    if (cellSurvives(new Position(i, j))) {
                                        nextGen.add(cellsMap[i][j]);
                                    }
                                } else if (cellBornNextGen(new Position(i, j)))
                                    nextGen.add(cellsMap[i][j]);
                            }
                        }
                        
                        //Clear board and make nextGen cells alive
                        board.clear();
                        nextGen.forEach((c) -> {
                            c.makeAlive();
                        });
                    }
                    else {
                        // Do Pause Thread Stuff
                    }
                });
        
        Thread musicThread = new Thread(new Runnable() {
            final String TOTO = "src/resources/Africa.mp3";
            Media hit = new Media(new File(TOTO).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            
            @Override
            public void run() {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.play();
            }
        });
        
        animationLoop.start();
        musicThread.start();
        
        mainStage.show();
    }
    
    private void setUpTopBar(HBox toolBar) {
        Button pauseButton = new Button("Play");
        pauseButton.setOnMouseClicked((Event e) -> {
            isPaused = !isPaused;
            
            if (isPaused)
                pauseButton.setText("Play");
            else
                pauseButton.setText("Pause");
        });
        toolBar.getChildren().add(pauseButton);
        
        Button clearButton = new Button("Clear");
        clearButton.setOnMouseClicked(clearHandler);
        toolBar.getChildren().add(clearButton);
    }
    
    private void setUpBottomBar(HBox settingsBar) {
        settingsBar.getChildren().add(new Label("Cell Survival Rule: "));
        settingsBar.getChildren().add(S_RULEBOX);
        /*settingsBar.getChildren().add(new Label(
            "23 means that cells with 2 or 3 neighbors will survive"));*/
        
        settingsBar.getChildren().add(new Label("Cell Birth Rule: "));
        settingsBar.getChildren().add(B_RULEBOX);
        /*settingsBar.getChildren().add(new Label(
            "3 means that cells will be born in empty squares with 3 neighbors"));*/
        
        /*settingsBar.getChildren().add(new Label("Cells Wide: "));
        settingsBar.getChildren().add(cellsWideSlider);
        
        settingsBar.getChildren().add(new Label("Cells High: "));
        settingsBar.getChildren().add(cellsHighSlider);*/
        
        Button applyButton = new Button("Apply");
        settingsBar.getChildren().add(applyButton);
        applyButton.setOnMouseClicked(e -> {
            try { // Exception Handling
                if ((!S_RULEBOX.getText().matches("[0-8]+") &&
                        !S_RULEBOX.getText().isEmpty()) ||
                        (!B_RULEBOX.getText().matches("[0-8]+") &&
                        !B_RULEBOX.getText().isEmpty()))
                    throw new IllegalArgumentException("Invalid Rule");
                board.clear();
                setNewRules();
                //setCellAreaBySliders();
            }
            catch (IllegalArgumentException ex) {
                S_RULEBOX.setText(ex.getMessage());
                B_RULEBOX.setText(ex.getMessage());
            }
        });
    }
    
    /*private void setUpSettingsSliders() {
        cellsWideSlider.setShowTickMarks(true);
        cellsWideSlider.setMajorTickUnit(50);
        cellsWideSlider.setMinorTickCount(1);
        cellsWideSlider.setSnapToTicks(true);
        cellsWideSlider.setBlockIncrement(10);
        
        cellsHighSlider.setShowTickMarks(true);
        cellsHighSlider.setMajorTickUnit(50);
        cellsHighSlider.setMinorTickCount(1);
        cellsHighSlider.setSnapToTicks(true);
        cellsHighSlider.setBlockIncrement(10);
    }*/
    
    EventHandler<Event> pauseHandler = event -> {
        isPaused = !isPaused;
        
    };
    
    EventHandler<Event> clearHandler = event -> {
        board.clear();
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
