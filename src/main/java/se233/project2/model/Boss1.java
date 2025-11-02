
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
import se233.project2.view.GameStage1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Boss1 extends BossBase {
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

    public Boss1(GameStage1 gameStage, int id, int x, int y, String imgName, int count, int column, int row, int width, int height) {
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

    public void moveBullets() {
        // เคลื่อนที่กระสุนของบอส
        Iterator<Boss1Bullet> iterator = bossBullets.iterator();
        while (iterator.hasNext()) {
            Boss1Bullet bullet = iterator.next();
            bullet.move();
            if (bullet.getTranslateX() < 0 || bullet.getTranslateX() > GameStage1.WIDTH) {
                iterator.remove();
                getChildren().remove(bullet);  // ลบกระสุนเมื่อมันออกจากจอ
            }
        }
    }

    public void repaint() {
        // อัปเดตการเคลื่อนที่ของกระสุน
        moveBullets();
    }

    public void takeDamage(int damage) {
        if (!alive) return;
        health -= damage;
        healthBar.setWidth(health);
        System.out.println("Boss took damage! Remaining HP: " + health);

        Platform.runLater(() -> this.setOpacity(0.5));

        new Thread(() -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> this.setOpacity(1.0));
        }).start();

        if (health <= 0) die();
    }


private void die() {
    alive = false;


    Platform.runLater(() -> {
        System.out.println("Boss defeated!");


        // 💥 เพิ่มเอฟเฟกต์ระเบิดก่อนเปลี่ยน sprite
        Image bomImg = new Image(Launcher.class.getResourceAsStream("assets/bom1.png"));
// สมมติ bom1.png มี 6 เฟรม, 3 columns, 2 rows, width 50, height 50
        AnimatedSprite explosion = new AnimatedSprite(bomImg, 4, 4, 1, 0, 0, 32, 32);
        explosion.setFitWidth(300);
        explosion.setFitHeight(300);
        explosion.setTranslateX(getTranslateX() + characterWidth / 2 - 75);
        explosion.setTranslateY(getTranslateY() + characterHeight / 2 - 75);
        gameStage.getChildren().add(explosion);
        Timeline explosionTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> explosion.tick())
        );
        explosionTimeline.setCycleCount(12); // จำนวนเฟรม
        explosionTimeline.setOnFinished(e -> gameStage.getChildren().remove(explosion));
        explosionTimeline.play();
        // ตั้งเวลาให้ระเบิดหายไปหลัง 1 วินาที แล้วเปลี่ยนเป็น sprite BossDie1.png
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                gameStage.getChildren().remove(explosion);
                // 🧱 หยุดยิงทันที
                if (shootTimeline != null) {
                    shootTimeline.stop();
                }


                // 🔴 ลบกระสุนทั้งหมด
                for (Boss1Bullet bullet : bossBullets) {
                    bullet.setVisible(false);
                    bullet.setDisable(true);
                    if (gameStage != null) gameStage.getChildren().remove(bullet);
                }
                bossBullets.clear();


                // 🔄 เพิ่ม sprite ศพบอสลงบน scene แยก Pane
                ImageView deadBoss = new ImageView(bossDieImg);
                deadBoss.setFitWidth((int)(characterWidth * 1.2));
                deadBoss.setFitHeight((int)(characterHeight * 1.2));
                deadBoss.setTranslateX(getTranslateX());
                deadBoss.setTranslateY(getTranslateY());
                gameStage.getChildren().add(1,deadBoss);


                // 🚪 ซ่อน Pane ของบอสตัวจริงเพื่อให้ตัวละครเดินทับได้
                this.setVisible(false);
                this.setDisable(true);
                this.setMouseTransparent(true);


                // ✅ แจ้ง Stage ว่าบอสตายแล้ว (เปิดทางดำ)
                gameStage.setBossDefeated(true);


                System.out.println("Boss1 exploded and dead sprite added, player can walk over!");
            });
        }).start();
    });
}



    public boolean isAlive() {
        return alive;
    }

    public int getHealth() {
        return health;
    }

    public double getX() {
        return getTranslateX();
    }

    public double getY() {
        return getTranslateY();
    }

    public int getCharacterWidth() {
        return characterWidth;
    }

    public int getCharacterHeight() {
        return characterHeight;
    }
}
