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

import javafx.scene.layout.Pane;
import se233.project2.model.*;

import java.util.ArrayList;
import java.util.List;

public abstract class GameStageBase extends Pane {
    public abstract Pane getBulletsLayer();

    public abstract void updateHearts(int lives);

    public abstract void restartStage();

    public abstract List<GameCharacter1> getGameCharacterList();

    public abstract List<PlatformG> getPlatforms();

    public abstract void goToNextStage();

    public abstract Keys getKeys();

    public List<Minion1> getMinionList() {
        return getMinionList();
    }

    public List<? extends Boss1> getBossList() {
    }
}

