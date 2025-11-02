

package se233.project2.model;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import se233.project2.Launcher;


public class Minion1 extends Pane {
    private int health = 30;
    private boolean alive = true;
    private GameStage1 gameStage;
    private Image characterImg;
    private AnimatedSprite imageView;
    private int characterWidth;
    private int characterHeight;


    // 🔹 Physics
    private double x;
    private double y;
    private double velocityY = 0;
    private final double gravity = 0.5;
    private final double maxFallSpeed = 10;
    private boolean onGround = false;


    // 🔹 Animation
    private Timeline idleTimeline;
    private Timeline physicsTimeline;


    public Minion1(GameStage1 gameStage, int id, int x, int y, String imgName,
                   int count, int column, int row, int width, int height) {
        this.gameStage = gameStage;
        this.x = x;
        this.y = y;
        this.characterWidth = width;
        this.characterHeight = height;


        // โหลดภาพของมินเนี่ยน
        this.characterImg = new Image(Launcher.class.getResourceAsStream(imgName));
        this.imageView = new AnimatedSprite(characterImg, count, column, row, 0, 0, width, height);
        this.imageView.setFitWidth(width * 1.5);
        this.imageView.setFitHeight(height * 1.5);


        // เริ่มต้นตำแหน่ง
        setTranslateX(x);
        setTranslateY(y);


        getChildren().add(imageView);


        // Animation เดินนิ่ง ๆ
        idleTimeline = new Timeline(
                new KeyFrame(Duration.millis(250), e -> imageView.tick())
        );
        idleTimeline.setCycleCount(Timeline.INDEFINITE);
        idleTimeline.play();


        // Physics (ตกลงพื้น / platform)
        physicsTimeline = new Timeline(
                new KeyFrame(Duration.millis(16), e -> applyGravity())
        );
        physicsTimeline.setCycleCount(Timeline.INDEFINITE);
        physicsTimeline.play();
    }


    // ✅ ฟังก์ชันแรงโน้มถ่วง + ตรวจชน Platform
    private void applyGravity() {
        if (!alive) return;


        onGround = false; // assume ว่าเริ่มไม่แตะพื้น


        // ตรวจ platform ทุกตัว
        for (PlatformG platform : gameStage.getPlatforms()) {
            double top = platform.getTopY();
            double left = platform.getLeftX();
            double right = platform.getRightX();


            // ถ้าอยู่เหนือ platform และกำลังตกลงมา
            if (y + characterHeight <= top && y + characterHeight + velocityY >= top) {
                if (x + characterWidth > left && x < right) {
                    y = top - characterHeight; // วางบน platform
                    velocityY = 0;
                    onGround = true;
                    break; // เจอ platform แล้วหยุด loop
                }
            }
        }


        // ถ้าไม่ได้แตะ platform -> ตกลงมา
        if (!onGround) {
            velocityY += gravity;
            if (velocityY > maxFallSpeed) velocityY = maxFallSpeed;
            y += velocityY;


            // ตรวจพื้นหลักของ stage
            if (y + characterHeight >= GameStage1.GROUND) {
                y = GameStage1.GROUND - characterHeight;
                velocityY = 0;
                onGround = true;
            }
        }


        // อัปเดตตำแหน่งจริงบนจอ
        Platform.runLater(() -> setTranslateY(y));
    }


    // ✅ ฟังก์ชันโดนโจมตี
    public void takeDamage(int damage) {
        if (!alive) return;
        health -= damage;
        if (health <= 0) die();
    }


    private void die() {
        alive = false;
        idleTimeline.stop();
        physicsTimeline.stop();


        Platform.runLater(() -> {
            System.out.println("Minion defeated!");


            // เอฟเฟกต์ระเบิดตอนตาย
            Image bomImg = new Image(Launcher.class.getResourceAsStream("assets/bom1.png"));
            AnimatedSprite explosion = new AnimatedSprite(bomImg, 4, 4, 1, 0, 0, 32, 32);
            explosion.setFitWidth(80);
            explosion.setFitHeight(80);
            explosion.setTranslateX(getTranslateX() + characterWidth / 2 - 40);
            explosion.setTranslateY(getTranslateY() + characterHeight / 2 - 40);
            gameStage.getChildren().add(explosion);


            Timeline explosionTimeline = new Timeline(
                    new KeyFrame(Duration.millis(100), e -> explosion.tick())
            );
            explosionTimeline.setCycleCount(8);
            explosionTimeline.setOnFinished(e -> {
                gameStage.getChildren().remove(explosion);
                gameStage.getChildren().remove(this);
            });
            explosionTimeline.play();
        });
    }


    public boolean isAlive() { return alive; }
    public double getX() { return getTranslateX(); }
    public double getY() { return getTranslateY(); }
}

