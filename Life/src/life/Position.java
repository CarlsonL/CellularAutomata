/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life;

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 *
 * @author Carlson
 * @param <T>
 * Code other than signature and compareTO from:
 * https://codereview.stackexchange.com/questions/107844/full-color-clone-of-conways-game-of-life-with-a-decent-gui
 * BinaryOperator help from https://www.boraji.com/java-8-binaryoperator-interface-example
 */
 //Generics //Comparable Interface
public class Position<T extends Comparable<T>> implements Comparable<Position<T>> {
    private final T xPos;
    private final T yPos;
    
    public Position(T x, T y) {
        xPos = x;
        yPos = y;
    }
    
    public T getX() {
        return xPos;
    }
    
    public T getY() {
        return yPos;
    }
    
    public Position<T> map(UnaryOperator<T> f) {
        return new Position<>(f.apply(xPos), f.apply(yPos));
    }
    
    public Position<T> map(Position<T> otherPos, BinaryOperator<T> f) {
        return new Position<>(
                f.apply(xPos, otherPos.xPos), f.apply(yPos, otherPos.yPos));
    }

    @Override
    public int compareTo(Position<T> o) {
        Comparator<T> comparator = (a, b) -> (a.compareTo(b));
        BinaryOperator<T> opMin = BinaryOperator.minBy(comparator);
        BinaryOperator<T> opMax = BinaryOperator.maxBy(comparator);
        Position<T> minResult = this.map(o, opMin);
        Position<T> maxResult = this.map(o, opMax);
        if (minResult.getX().compareTo(this.getX()) == 0) {
            if (maxResult.getX().compareTo(this.getX()) == 0)
                return 0;
            else
                return -1;
        }
        else
            return 1;
    }
}
