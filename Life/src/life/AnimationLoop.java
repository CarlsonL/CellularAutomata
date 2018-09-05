/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package life;

import java.util.function.Consumer;
import javafx.animation.AnimationTimer;

/**
 *
 * @author Carlson
 * Most code from https://codereview.stackexchange.com/questions/107844/full-color-clone-of-conways-game-of-life-with-a-decent-gui
 */
public class AnimationLoop extends AnimationTimer { //AnimationTimer is abstract
    public final static long NANOSPERSECOND = 1000000000;
    public final static long IDEALFRAMERATENS = (long)(1/60.0 * NANOSPERSECOND);
    
    private final long updateGraphicsEvery;
    private final Consumer<Long> doEveryUpdate;
    private long lastTime = IDEALFRAMERATENS;
    
    public AnimationLoop(long updateEveryNS, Consumer<Long> f) {
        this.updateGraphicsEvery = updateEveryNS;
        doEveryUpdate = f;
    }

    @Override
    public void handle(long now) {
        long nanosElapsed = now - lastTime;
        
        if (nanosElapsed >= updateGraphicsEvery) {
            lastTime = now;
            doEveryUpdate.accept(nanosElapsed);
        }
    }
}
