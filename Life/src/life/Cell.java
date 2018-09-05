/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Carlson
 */
public class Cell extends Pane {
    private boolean alive;
    private int x;
    private int y;
    
    public Cell() {
        this(10.0);
    }
    
    public Cell(double x) {
        alive = false;
        setStyle("-fx-border-color: lightgrey; " +
                "-fx-background-color: white;");
        this.setPrefSize(x, x);
        this.setOnMouseClicked(e -> changeState());
    }

    public void changeState() {
        if (alive)
            makeDead();
        else
            makeAlive();
    }
    
    public void makeDead() {
        setStyle("-fx-border-color: lightgrey; " +
                "-fx-background-color: white");
        alive = false;
    }
    
    public void makeAlive() {
        setStyle("-fx-background-color: black; " +
                "-fx-border-color: lightgrey;");
        alive = true;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getX() {
        return x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getY() {
        return y;
    }
}
