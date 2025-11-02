
package se233.project2.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import se233.project2.Launcher;
import se233.project2.model.*;


import java.util.ArrayList;
import java.util.List;

public class GameStage1 extends GameStageBase {
    public static final int WIDTH = 919;
    public static final int HEIGHT = 642;
    public final static int GROUND = 558;
    private Pane bulletsLayer = new Pane(); // Layer สำหรับกระสุน
    private List<PlatformG> platforms = new ArrayList<>();
    private boolean bossDefeated = false;
    private List<Minion1> minionList;

    private Image gameStageImg;
    private List<GameCharacter1> gameCharacterList;
    private List<Boss1> bossList;
    private List<ImageView> hearts = new ArrayList<>();
    private Keys keys;
    private Thread gameLoopThread;



    public GameStage1() {
        gameCharacterList = new ArrayList<>();
        bossList = new ArrayList<>();
        keys = new Keys();
        minionList = new ArrayList<>();

        gameStageImg = new Image(Launcher.class.getResourceAsStream("assets/stage1.png"));
        ImageView backgroundImg = new ImageView(gameStageImg);
        backgroundImg.setFitHeight(HEIGHT);
        backgroundImg.setFitWidth(WIDTH);

        GameCharacter1 player = new GameCharacter1(this, 0, 30, 30, "assets/runChar1.png", 6, 6, 1, 30, 56,
                KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.SPACE, KeyCode.DOWN);
        gameCharacterList.add(player);

        Boss1 boss = new Boss1(this, 0, WIDTH - 320, GROUND - 470, "assets/Boss1.png", 1, 1, 1, 283, 470);
        bossList.add(boss);

        Minion1 minion1 = new Minion1(this, 0, 200, 30,"assets/Minion1.png",2,2,1,29,60);
        Minion1 minion2 = new Minion1(this, 0, 250, 30,"assets/Minion1.png",2,2,1,29,60);
        Minion1 minion3 = new Minion1(this, 0, 300, 30,"assets/Minion1.png",2,2,1,29,60);
        minionList.addAll(List.of(minion1, minion2, minion3));
        //getChildren().addAll(minionList);

        // 🔹 พื้นหลัก
        PlatformG ground = new PlatformG(WIDTH, 40, 0, GROUND, Color.TRANSPARENT);

        PlatformG g1 = new PlatformG(275, 20, 94, GROUND - 110, Color.TRANSPARENT);
        PlatformG g2 = new PlatformG(89, 20, 370, GROUND - 155, Color.TRANSPARENT);
        PlatformG g3 = new PlatformG(89, 20, 460, GROUND - 65, Color.TRANSPARENT);
        PlatformG g4 = new PlatformG(366, 20, 0, GROUND - 247, Color.TRANSPARENT);

        platforms.addAll(List.of(ground, g1, g2,g3,g4
        ));

        getChildren().add(backgroundImg);
        getChildren().addAll(gameCharacterList);
        getChildren().addAll(bossList);
        getChildren().addAll(minionList);
        getChildren().add(bulletsLayer);
        getChildren().addAll(ground, g1, g2, g3, g4);

        for (int i = 0; i < 3; i++) {
            ImageView heart = new ImageView(new Image(Launcher.class.getResourceAsStream("assets/heart.png")));
            heart.setFitWidth(40);
            heart.setFitHeight(40);
            heart.setTranslateX(20 + (i * 45)); // ระยะห่างแต่ละหัวใจ
            heart.setTranslateY(20);
            hearts.add(heart);
            getChildren().add(heart);
        }

        this.setFocusTraversable(true);
        this.requestFocus();
    }


    public void updateHearts(int lives) {
        for (int i = 0; i < hearts.size(); i++) {
            hearts.get(i).setVisible(i < lives); // แสดงเฉพาะจำนวนชีวิตที่เหลือ
        }
    }

    public void restartStage() {
        Platform.runLater(() -> {
            // ✅ สร้าง stage ใหม่
            GameStage1 newStage = new GameStage1();
            Scene newScene = new Scene(newStage, WIDTH, HEIGHT);

            // ตั้งค่า key input
            newScene.setOnKeyPressed(event -> newStage.getKeys().add(event.getCode()));
            newScene.setOnKeyReleased(event -> newStage.getKeys().remove(event.getCode()));

            // ✅ เริ่ม game loop ใหม่
            se233.project2.controller.GameLoop1 newGameLoop = new se233.project2.controller.GameLoop1(newStage);
            se233.project2.controller.DrawingLoop1 newDrawingLoop = new se233.project2.controller.DrawingLoop1(newStage);
            new Thread(newGameLoop).start();
            new Thread(newDrawingLoop).start();

            // ✅ เปลี่ยน scene กลับเข้า stage ใหม่
            se233.project2.Launcher.goToNextStage();
            ;

            // 🪂 บังคับให้ตัวละครอยู่บนพื้นทันที
            if (!newStage.getGameCharacterList().isEmpty()) {
                GameCharacter1 player = newStage.getGameCharacterList().get(0);
                player.applyGravity();      // ให้แรงโน้มถ่วงตกลงพื้น
                player.checkReachFloor();   // ตรวจพื้น
            }

            // ✅ focus เพื่อให้ปุ่ม keyboard ใช้งานได้เลย
            newStage.requestFocus();
        });
    }


    public Pane getBulletsLayer() {
        return bulletsLayer;
    }

    public List<GameCharacter1> getGameCharacterList() {
        return gameCharacterList;
    }

    public List<Boss1> getBossList() {
        return bossList;
    }


    public Keys getKeys() {
        return keys;
    }

    public void goToNextStage() {
        Launcher.goToNextStage();
        ;
    }

    public List<PlatformG> getPlatforms() {return platforms;}

    public boolean isBossDefeated() {
        return bossDefeated;
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
    }

    public List<Minion1> getMinionList() {
        return minionList;
    }
}
