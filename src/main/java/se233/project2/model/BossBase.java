//package se233.project2.model;
//
//import javafx.scene.layout.Pane;
//
//public abstract class BossBase extends Pane {
//    public abstract void takeDamage(int damage);
//    public abstract boolean isAlive();
//}

package se233.project2.model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import se233.project2.Launcher;

import java.util.ArrayList;
import java.util.List;

public abstract class BossBase extends Pane {
    protected int health;
    protected boolean alive = true;
    protected boolean dead = false;
    private int health = 200;
    private Rectangle healthBar;
    private boolean alive = true;
    private GameStage1 gameStage;
    private Image characterImg;
    private AnimatedSprite imageView;
    private int x;
    private int y;
    private int characterWidth;
    private int characterHeight;
    public List<Boss1Bullet> bossBullets;
    private Image bossDieImg;
    private AnimatedSprite bossDieSprite;
    private ImageView deadBoss;



    private Timeline shootTimeline;

    public BossBase(GameStage1 gameStage, int x, int y, String imgName, int count, int column, int row, int width, int height) {
        this.gameStage = gameStage;
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterWidth = width;
        this.characterHeight = height;
        this.characterImg = new Image(Launcher.class.getResourceAsStream(imgName));
        this.imageView = new AnimatedSprite(characterImg, count, column, row, 0, 0, width, height);
        this.imageView.setFitWidth((int) (width * 1.2));
        this.imageView.setFitHeight((int) (height * 1.2));

        this.bossDieImg = new Image(Launcher.class.getResourceAsStream("assets/BossDie1.png"));
        this.bossDieSprite = new AnimatedSprite(bossDieImg, 1, 1, 1, 0, 0, 283, 411);
        this.bossDieSprite.setFitWidth((int) (width * 1.2));
        this.bossDieSprite.setFitHeight((int) (height * 1.2));

        this.healthBar = new Rectangle(200, 20, Color.RED);  // กำหนดขนาดของหลอดเลือดและสี
        this.healthBar.setTranslateY(-20);  // แสดงเหนือหัวบอส
        this.getChildren().add(healthBar);  // เพิ่ม health bar ลงในบอส

        this.getChildren().addAll(this.imageView);
        setScaleX(+1);

        this.bossBullets = new ArrayList<>();

        // สร้าง Timeline เพื่อยิงกระสุนทุกๆ 2 วินาที
        shootTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> shoot())
        );
        shootTimeline.setCycleCount(Timeline.INDEFINITE);  // ให้มันทำงานซ้ำไปเรื่อยๆ
        shootTimeline.play();  // เริ่มต้นการยิงกระสุน
    }

    public void shoot() {
        double bulletX = getTranslateX() + characterWidth * 0.15;
        double bulletY = getTranslateY() + characterHeight * 0.85;

        //สร้างกระสุน 3 ทิศ: ซ้าย, บน, ล่าง
        Boss1Bullet leftBullet = new Boss1Bullet(bulletX, bulletY, -5, 0);   // ซ้าย
        Boss1Bullet upBullet = new Boss1Bullet(bulletX, bulletY, -4, -3);    // บน-ซ้าย
        Boss1Bullet downBullet = new Boss1Bullet(bulletX, bulletY, -4, 3);   // ล่าง-ซ้าย

        bossBullets.add(leftBullet);
        bossBullets.add(upBullet);
        bossBullets.add(downBullet);

        Platform.runLater(() -> {
            if (gameStage != null) {
                gameStage.getChildren().addAll(leftBullet, upBullet, downBullet);
            }
        });

        System.out.println("Boss fired 3 bullets!");
    }
    public abstract void takeDamage(int damage);

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() { return dead; }

    public int getHealth() {
        return health;
    }
}
