
package se233.project2.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import se233.project2.Launcher;
import se233.project2.controller.GameLoop1;
import se233.project2.controller.DrawingLoop1;
import se233.project2.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameStage2 extends GameStageBase {
    public static final int WIDTH = 919;
    public static final int HEIGHT = 526;
    public final static int GROUND = 430;

    private Pane bulletsLayer = new Pane();
    private List<PlatformG> platforms = new ArrayList<>();
    private boolean bossDefeated = false;
    private List<Minion1> minionList;
    private Image gameStageImg;
    private List<GameCharacter1> gameCharacterList;
    private List<Boss2> bossList;
    private List<ImageView> hearts = new ArrayList<>();
    private Keys keys;

    public GameStage2() {
        gameCharacterList = new ArrayList<>();
        bossList = new ArrayList<>();
        keys = new Keys();
        minionList = new ArrayList<>();

        // 🔹 พื้นหลัง Stage 2
        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/stage2.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);

        // 🔹 ผู้เล่น
        GameCharacter1 player = new GameCharacter1(this, 0, 30, 30,
                "assets/runChar1.png", 6, 6, 1, 30, 56,
                KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.SPACE, KeyCode.DOWN);
        gameCharacterList.add(player);

        // 🔹 Boss2
        Boss2 boss = new Boss2(this, 0, WIDTH - 320, GROUND - 470,
                "assets/Boss2.png", 1, 1, 1, 204, 225);
        bossList.add(boss);

        // 🔹 Minion (เพิ่มถ้าต้องการในฉาก 2)
//        Minion1 minion1 = new Minion1(this, 0, 150, 30, "assets/Minion1.png", 2, 2, 1, 29, 60);
//        Minion1 minion2 = new Minion1(this, 0, 300, 30, "assets/Minion1.png", 2, 2, 1, 29, 60);
//        minionList.addAll(List.of(minion1, minion2));

        // 🔹 พื้นและแท่น
        PlatformG ground = new PlatformG(WIDTH, 40, 0, GROUND, Color.TRANSPARENT);
        PlatformG g1 = new PlatformG(250, 20, 60, GROUND - 70, Color.TRANSPARENT);
        PlatformG g2 = new PlatformG(167, 20, 0, GROUND - 180, Color.TRANSPARENT);
        PlatformG g3 = new PlatformG(85, 20, 510, GROUND - 150, Color.TRANSPARENT);
        platforms.addAll(List.of(ground, g1, g2, g3));

        // 🔹 จัดเรียงลำดับการแสดงผล
        getChildren().add(backgroundImg);
        getChildren().addAll(gameCharacterList);
        getChildren().addAll(bossList);
        //getChildren().addAll(minionList);
        getChildren().add(bulletsLayer);
        getChildren().addAll(ground, g1, g2, g3);

        // 🔹 หัวใจ (ระบบ HP)
        for (int i = 0; i < 3; i++) {
            ImageView heart = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/heart.png")));
            heart.setFitWidth(40);
            heart.setFitHeight(40);
            heart.setTranslateX(20 + (i * 45));
            heart.setTranslateY(20);
            hearts.add(heart);
            getChildren().add(heart);
        }

        // 🔹 Key control
        this.setFocusTraversable(true);
        this.requestFocus();
    }

    // 🔹 อัปเดตหัวใจ
    public void updateHearts(int lives) {
        for (int i = 0; i < hearts.size(); i++) {
            hearts.get(i).setVisible(i < lives);
        }
    }

    // 🔹 รีสตาร์ท stage
    public void restartStage() {
        Platform.runLater(() -> {
            GameStage2 newStage = new GameStage2();
            Scene newScene = new Scene(newStage, WIDTH, HEIGHT);

            // ตั้ง key input
            newScene.setOnKeyPressed(event -> newStage.getKeys().add(event.getCode()));
            newScene.setOnKeyReleased(event -> newStage.getKeys().remove(event.getCode()));

            // เริ่ม Game Loop ใหม่
            GameLoop1 newGameLoop = new GameLoop1(newStage);
            DrawingLoop1 newDrawingLoop = new DrawingLoop1(newStage);
            new Thread(newGameLoop).start();
            new Thread(newDrawingLoop).start();

            // เปลี่ยนไปฉากใหม่
            Launcher.goToNextStage();

            // ให้ player ลงพื้น
            if (!newStage.getGameCharacterList().isEmpty()) {
                GameCharacter1 player = newStage.getGameCharacterList().get(0);
                player.applyGravity();
                player.checkReachFloor();
            }

            newStage.requestFocus();
        });
    }

    // 🔹 Getter ต่าง ๆ
    public Pane getBulletsLayer() { return bulletsLayer; }
    public List<GameCharacter1> getGameCharacterList() { return gameCharacterList; }
    @Override public List<Boss2> getBossList() { return bossList; }
    public Keys getKeys() { return keys; }
    public List<PlatformG> getPlatforms() { return platforms; }
    public boolean isBossDefeated() { return bossDefeated; }
    public void setBossDefeated(boolean defeated) { this.bossDefeated = defeated; }
    public List<Minion1> getMinionList() { return minionList; }

    public void goToNextStage() { Launcher.goToNextStage(); }
}
