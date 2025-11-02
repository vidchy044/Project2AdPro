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
import se233.project2.view.GameStage2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Boss2 extends BossBase {
    private int health = 250;
    private Rectangle healthBar;
    private boolean alive = true;
    private GameStage2 gameStage;
    private Image characterImg;
    private AnimatedSprite imageView;
    private int x, y, characterWidth, characterHeight;

    private List<ImageView> skillBullets;
    private Image skillImg;
    private AnimatedSprite skillSprite;
    private Image bossDieImg;
    private AnimatedSprite bossDieSprite;
    private Timeline shootTimeline;


    public Boss2(GameStage2 gameStage, int id, int x, int y, String imgName,
                 int count, int column, int row, int width, int height) {
        this.gameStage = gameStage;
        this.x = x;
        this.y = y;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.characterWidth = width;
        this.characterHeight = height;

        this.characterImg = new Image(Launcher.class.getResourceAsStream(imgName));
        this.imageView = new AnimatedSprite(characterImg, count, column, row, 0, 0, width, height);
        this.imageView.setFitWidth(width * 1.2);
        this.imageView.setFitHeight(height * 1.2);

        this.bossDieImg = new Image(Launcher.class.getResourceAsStream("assets/BossDie1.png"));
        this.bossDieSprite = new AnimatedSprite(bossDieImg, 1, 1, 1, 0, 0, 283, 411);
        this.bossDieSprite.setFitWidth(width * 1.2);
        this.bossDieSprite.setFitHeight(height * 1.2);

        this.healthBar = new Rectangle(250, 20, Color.RED);
        this.healthBar.setTranslateY(-20);
        this.getChildren().addAll(healthBar, imageView);

        skillBullets = new ArrayList<>();

        skillImg = new Image(Launcher.class.getResourceAsStream("assets/skillboss2.png"));
        skillSprite = new AnimatedSprite(skillImg, 4, 4, 1, 0, 0, 25, 32);
        Timeline skillSpriteTimeline = new Timeline(
                new KeyFrame(Duration.millis(100), e -> skillSprite.tick())
        );
        skillSpriteTimeline.setCycleCount(12); // เล่นซ้ำตลอด
        skillSpriteTimeline.play(); // ✅ เริ่มเล่น timeline
        // ยิง skill ทุก 2 วินาที
        shootTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> useSkill()));
        shootTimeline.setCycleCount(Timeline.INDEFINITE);
        shootTimeline.play();
    }

    /** ใช้ skill ปล่อยกระสุนจากรูป skillboss2.png */
    public void useSkill() {
        if (!alive) return;

        double bulletX = getTranslateX() + characterWidth * 0.15;
        double bulletY = getTranslateY() + characterHeight * 0.85;

        double[][] dirs = {
                {-6, 0}, {-5, -2}, {-5, 2}, {-4, -4}, {-4, 4}
        };

        for (double[] d : dirs) {
            ImageView skill = new ImageView(skillImg);
            skill.setFitWidth(40);
            skill.setFitHeight(40);
            skill.setTranslateX(bulletX);
            skill.setTranslateY(bulletY);
            skillBullets.add(skill);

            Platform.runLater(() -> gameStage.getChildren().add(skill));

            new Thread(() -> {
                while (alive && skill.isVisible()) {
                    Platform.runLater(() -> {
                        skill.setTranslateX(skill.getTranslateX() + d[0]);
                        skill.setTranslateY(skill.getTranslateY() + d[1]);

                        // ถ้าออกนอกจอ
                        if (skill.getTranslateX() < 0 || skill.getTranslateX() > GameStage2.WIDTH
                                || skill.getTranslateY() < 0 || skill.getTranslateY() > GameStage2.HEIGHT) {
                            skill.setVisible(false);
                            gameStage.getChildren().remove(skill);
                        }
                    });
                    try { Thread.sleep(16); } catch (InterruptedException ignored) {}
                }
            }).start();
        }
    }

    public void repaint() {
        Iterator<ImageView> it = skillBullets.iterator();
        while (it.hasNext()) {
            ImageView b = it.next();
            if (!b.isVisible()) it.remove();
        }
    }

    public void takeDamage(int dmg) {
        if (!alive) return;
        health -= dmg;
        healthBar.setWidth(health);
        if (health <= 0) die();
    }

    /** เมื่อบอสตาย */
    private void die() {
        alive = false;
        Platform.runLater(() -> {
            System.out.println("Boss2 defeated!");
            if (shootTimeline != null) shootTimeline.stop();

            // ลบกระสุนทั้งหมด
            for (ImageView s : skillBullets) {
                s.setVisible(false);
                gameStage.getChildren().remove(s);
            }
            skillBullets.clear();

            // เปลี่ยน sprite เป็น boss ตาย
            this.getChildren().clear();
            this.getChildren().add(bossDieSprite);
            bossDieSprite.tick();

            this.setMouseTransparent(true);
            this.setDisable(true);

            // 🟩 หลังบอสตาย 2 วินาที → โหลดฉาก stage2.png กลับมาใหม่ + เปิดทางออก
            new Thread(() -> {
                try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

                Platform.runLater(() -> {
                    gameStage.resetStageAfterBossDeath(); // ✅ เรียกฟังก์ชันใน GameStage2
                });
            }).start();
        });
    }

    public boolean isAlive() { return alive; }
    public int getHealth() { return health; }
    public double getX() { return getTranslateX(); }
    public double getY() { return getTranslateY(); }
    public int getCharacterWidth() { return characterWidth; }
    public int getCharacterHeight() { return characterHeight; }


}
