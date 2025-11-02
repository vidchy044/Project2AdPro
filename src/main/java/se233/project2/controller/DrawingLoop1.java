
package se233.project2.controller;

import se233.project2.model.GameCharacter1;
import se233.project2.view.GameStageBase;

import java.util.List;

public class DrawingLoop implements Runnable {
    //private GameStage1 gameStage;
    private int frameRate = 60;
    private float interval = 1000.0f / frameRate;
    private boolean running = true;

//    public DrawingLoop1(GameStage1 gameStage) {
//        this.gameStage = gameStage;
//    }
    private GameStageBase gameStage;
        public DrawingLoop(GameStageBase gameStage) {
            this.gameStage = gameStage;
    }


    private void updatePhysics(List<GameCharacter1> players) {
        for (GameCharacter1 c : players) {
            c.checkReachGameWall();
            c.checkReachHighest();
            //c.checkReachFloor();
        }
    }

    private void paint(List<GameCharacter1> players) {
        for (GameCharacter1 c : players) {
            c.repaint();
        }
    }

    @Override
    public void run() {
        while (running) {
            long start = System.currentTimeMillis();

            updatePhysics(gameStage.getGameCharacterList());
            paint(gameStage.getGameCharacterList());

            long elapsed = System.currentTimeMillis() - start;
            if (elapsed < interval) {
                try {
                    Thread.sleep((long) (interval - elapsed));
                } catch (InterruptedException ignored) {}
            }
        }
    }
    public void stop() {
        running = false;
    }

}
