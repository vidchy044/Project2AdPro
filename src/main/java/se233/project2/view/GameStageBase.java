////package se233.project2.view;
////
////import javafx.application.Platform;
////import javafx.scene.layout.Pane;
////import se233.project2.model.Boss1;
////import se233.project2.model.GameCharacter1;
////import se233.project2.model.PlatformG;
////
////import java.util.List;
////
////public abstract class GameStageBase extends Pane {
////    //public abstract List<Platform> getPlatforms();
////
////    public abstract Pane getBulletsLayer();
////
////    public abstract void updateHearts(int lives);
////
////    public abstract void restartStage();
////
////    //public abstract List<BossBase> getBossList();
////    public abstract List<? extends Boss1> getBossList();
////
////
////    public List<GameCharacter1> getGameCharacterList() {
////    }
////
////    public List<PlatformG> getPlatforms() {
////        return null;
////    }
////
////    public void goToNextStage() {
////        restartStage();
////    }
////}
//
//package se233.project2.view;
//
//import javafx.scene.layout.Pane;
//import se233.project2.model.*;
//
//import java.util.List;
//
//public abstract class GameStageBase extends Pane {
//    public abstract Pane getBulletsLayer();
//
//    public abstract void updateHearts(int lives);
//
//    public abstract void restartStage();
//
//    public abstract List<GameCharacter1> getGameCharacterList();
//
//    public abstract List<PlatformG> getPlatforms();
//
//    public abstract void goToNextStage();
//
//    public abstract List<? extends Boss1> getBossList();
//
//    // ✅ เพิ่มบรรทัดนี้
//    public abstract Keys getKeys();
//}
//
//
package se233.project2.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.project2.Launcher;
import se233.project2.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class GameStageBase extends Pane {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public static final int GROUND = 300;

    private Image background;
    private final List<GameCharacter> gameCharacterList = new ArrayList<>();
    private final List<PlatformG> platformList = new ArrayList<>();
    private final List<Boss1> bossList = new ArrayList<>();
    private final List<Minion1> minionList = new ArrayList<>();
    private final List<Score> scoreList = new ArrayList<>();
    private final Keys keys = new Keys();

    public GameStageBase(String backgroundPath) {
        // Load background
        background = new Image(Launcher.class.getResourceAsStream(backgroundPath));
        ImageView bg = new ImageView(background);
        bg.setFitHeight(HEIGHT);
        bg.setFitWidth(WIDTH);
        getChildren().add(bg);

        // Let subclass add its stage content
        setupStage();
    }

    /** Subclasses must add platforms, characters, boss, etc. */
    protected abstract void setupStage();

    /** Restart logic per stage */
    public abstract void restartStage();

    /** Move to next stage */
    public abstract void goToNextStage();

    // --- GETTERS ---
    public List<GameCharacter> getGameCharacterList() { return gameCharacterList; }
    public List<PlatformG> getPlatforms() { return platformList; }
    public List<Boss1> getBossList() { return bossList; }
    public List<Minion1> getMinionList() { return minionList; }
    public List<Score> getScoreList() { return scoreList; }
    public Keys getKeys() { return keys; }

    // --- ADD HELPERS ---
    protected void addCharacter(GameCharacter gc) {
        gameCharacterList.add(gc);
        getChildren().add(gc);
    }

    protected void addPlatform(PlatformG p) {
        platformList.add(p);
        getChildren().add(p);
    }

    protected void addBoss(Boss1 boss) {
        bossList.add(boss);
        getChildren().add(boss);
    }

    protected void addMinion(Minion1 m) {
        minionList.add(m);
        getChildren().add(m);
    }

    protected void addScoreUI(Score s) {
        scoreList.add(s);
        getChildren().add(s);
    }
}

//    public abstract Pane getBulletsLayer();
//
//    public abstract void updateHearts(int lives);
//
//    public abstract void restartStage();
//
//    public abstract List<GameCharacter1> getGameCharacterList();
//
//    public abstract List<PlatformG> getPlatforms();
//
//    public abstract void goToNextStage();
//
//    public abstract Keys getKeys();
//
//    public List<Minion1> getMinionList() {
//        return getMinionList();
//    }
//
//    public List<? extends Boss1> getBossList() {}
//}

