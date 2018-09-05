/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life;

import javafx.scene.layout.GridPane;
import java.util.ArrayList;

/**
 *
 * @author Carlson
 */
public class Board extends GridPane{
    private int cellsWide;
    private int cellsHigh;
    private Cell[][] cells;
    
    public Board(int w, int h) {
        cellsWide = w;
        cellsHigh = h;
        cells = new Cell[cellsWide][cellsHigh];
        
        for (int i = 0; i < cellsWide; i++)
            for (int j = 0; j < cellsHigh; j++)
                super.add(cells[i][j] = new Cell(), i, j); // Class Hierarchy
    }
    
    public Cell[][] getCells() {
        return cells;
    }
    
    public void clear() {
        for (Cell[] row : cells)
            for (Cell c : row)
                if (c.isAlive())
                    c.makeDead();
    }
    
    private ArrayList<Position<Integer>> getNeighbors(Position<Integer> pos) {
        ArrayList<Position<Integer>> neighbors = new ArrayList<>();
        for (int i = pos.getX()-1; i <= pos.getX()+1; i++) {
            for (int j = pos.getY()-1; j <= pos.getY()+1; j++) {
                if ((i >= 0) && (i < cellsWide) && 
                    (j >= 0) && (j < cellsHigh) &&
                    (i != pos.getX() || j != pos.getY())) {
                    neighbors.add(new Position(i, j));
                }
            }
        }
        return neighbors;
    }
    
    public int getLiveNeighbs(Position<Integer> pos) {
        ArrayList<Position<Integer>> neighbors = getNeighbors(pos);
        int liveCount = 0;
        for (Position<Integer> n : neighbors) {
            if (cells[n.getX()][n.getY()].isAlive())
                liveCount++;
        }
        
        return liveCount;
    }
}
