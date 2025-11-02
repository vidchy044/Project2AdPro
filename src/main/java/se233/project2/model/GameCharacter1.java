
package se233.project2.model;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.project2.Launcher;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import se233.project2.view.GameStageBase;



public class GameCharacter extends Pane {
    private se233.project2.view.GameStageBase gameStage;
    private Image characterImg;
    private AnimatedSprite imageView;
    private int x;
    private int y;
    private int startX;
    private int startY;
    private int characterWidth;
    private int characterHeight;
    private int score = 0;
    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode upKey;
    private KeyCode spaceKey;
    private KeyCode downKey;
    boolean isMoveLeft = false;
    public boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;

    private boolean isShooting = false;
    private Image shootingSpriteSheet;
    private AnimatedSprite shootingSprite;
    private Image crawlingSpriteSheet;
    private AnimatedSprite crawlingSprite;
    private Image crawlingShootingSheet;
    private AnimatedSprite crawlingShootingSprite;

    private List<Bullet> bullets;
    private int lives = 3;
    private boolean isDead = false;

    // ===== ระบบแรงโน้มถ่วงและพื้นต่างระดับ =====
    private double gravity = 0.5;       // ความแรงของแรงโน้มถ่วง
    private double jumpStrength = 15; // แรงกระโดด (เดิม 22)
    private double maxFallSpeed = 5; // ความเร็วตกสูงสุด
    private double velocityY = 0;       // ความเร็วแนวดิ่ง
    private boolean onPlatform = false; // สถานะว่ายืนอยู่บน platform
    private boolean onGround;
    private double moveSpeed = 7;

    public GameCharacter(GameStageBase gameStage, int id, int x, int y, String imgName,
                          int count, int column, int row, int width, int height,
                          KeyCode leftKey, KeyCode rightKey, KeyCode upKey,
                          KeyCode spaceKey, KeyCode downKey) {
        this.gameStage = gameStage;
        this.startX = x;
        this.startY = y;
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
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.spaceKey = spaceKey;
        this.downKey = downKey;

        this.getChildren().addAll(this.imageView);
        setScaleX(id % 2 * 2 - 1);
        this.bullets = new ArrayList<>();

        shootingSpriteSheet = new Image(Launcher.class.getResourceAsStream("assets/Shooting12.png"));
        this.shootingSprite = new AnimatedSprite(shootingSpriteSheet, 2, 2, 1, 0, 0, 46, 56);
        this.shootingSprite.setFitWidth((int) (width * 1.2));
        this.shootingSprite.setFitHeight((int) (height * 1.2));

        crawlingSpriteSheet = new Image(Launcher.class.getResourceAsStream("assets/Crawling12.png"));
        this.crawlingSprite = new AnimatedSprite(crawlingSpriteSheet, 1, 1, 1, 0, 0, 32, 14);
        this.crawlingSprite.setFitWidth((int) (characterWidth * 2.2));
        this.crawlingSprite.setFitHeight((int) (characterHeight * 0.5));

        crawlingShootingSheet = new Image(Launcher.class.getResourceAsStream("assets/Crawling12.png"));
        crawlingShootingSprite = new AnimatedSprite(crawlingShootingSheet, 1, 1, 1, 0, 0, 32, 14);
        crawlingShootingSprite.setFitWidth((int) (characterWidth * 2.2));
        crawlingShootingSprite.setFitHeight((int) (characterHeight * 0.5));
    }

    public void updateMovementFromKeys() {
        isMoveLeft = gameStage.getKeys().isPressed(leftKey);
        isMoveRight = gameStage.getKeys().isPressed(rightKey);

        if (gameStage.getKeys().isPressed(upKey)) jump();
        if (gameStage.getKeys().isPressed(spaceKey)) shoot();
        if (gameStage.getKeys().isPressed(downKey)) prone();
        else stopProne();
    }

    // ========= ท่าคลาน =========
    public void prone() {
        if (imageView == crawlingSprite || imageView == crawlingShootingSprite) return;
        Platform.runLater(() -> {
            double heightDiff = (characterHeight * 1.2) - (characterHeight * 0.5);
            y -= heightDiff;
            setTranslateY(y);
            this.getChildren().clear();
            imageView = crawlingSprite;
            this.getChildren().add(crawlingSprite);
            crawlingSprite.tick();
        });
    }


    // แก้ stopProne() ให้ y ถูกต้อง
    public void stopProne() {
        if (imageView != crawlingSprite && imageView != crawlingShootingSprite) return;
        Platform.runLater(() -> {
            double heightDiff = (characterHeight * 1.2) - (characterHeight * 0.5);
            y += heightDiff;  // เปลี่ยนจาก -= เป็น +=
            setTranslateY(y);

            AnimatedSprite runningSprite = new AnimatedSprite(characterImg, 6, 6, 1, 0, 0, characterWidth, characterHeight);
            runningSprite.setFitWidth((int) (characterWidth * 1.2));
            runningSprite.setFitHeight((int) (characterHeight * 1.2));
            imageView = runningSprite;
            this.getChildren().clear();
            this.getChildren().add(imageView);
            imageView.tick();
        });
    }
    // ========= ยิง =========
    public void shoot() {
        if (isShooting) return;
        isShooting = true;

        Platform.runLater(() -> {
            this.getChildren().clear();
            if (imageView == crawlingSprite) {
                imageView = crawlingShootingSprite;
            } else {
                imageView = shootingSprite;
            }
            this.getChildren().add(imageView);
            imageView.tick();
        });

        Bullet bullet = new Bullet(x + characterWidth / 2, y + characterHeight / 2, isMoveLeft);
        bullets.add(bullet);
        Platform.runLater(() -> gameStage.getBulletsLayer().getChildren().add(bullet));

        new Thread(() -> {
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
            Platform.runLater(this::stopShooting);
        }).start();
    }

    public void stopShooting() {
        if (!isShooting) return;
        isShooting = false;
        Platform.runLater(() -> {
            this.getChildren().clear();

            // ✅ ถ้ายิงจากท่าคลาน ให้กลับมาเป็นท่าคลาน
            if (imageView == crawlingShootingSprite) {
                imageView = crawlingSprite;
            }
            // ✅ ถ้ายิงจากท่ายืน ให้กลับมาเป็นท่ายืน
            else {
                AnimatedSprite runningSprite = new AnimatedSprite(
                        characterImg, 6, 6, 1, 0, 0, characterWidth, characterHeight);
                runningSprite.setFitWidth((int) (characterWidth * 1.2));
                runningSprite.setFitHeight((int) (characterHeight * 1.2));
                imageView = runningSprite;
            }

            this.getChildren().add(imageView);
            imageView.tick();
        });
    }

    public void moveBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();
            if (bullet.getTranslateX() < 0 || bullet.getTranslateX() > GameStage1.WIDTH) {
                iterator.remove();
                Platform.runLater(() -> gameStage.getBulletsLayer().getChildren().remove(bullet));
            }
        }
    }

    // ========= การเคลื่อนที่แนวนอน =========
    public void moveLeft() { setScaleX(1); isMoveLeft = true; isMoveRight = false; }
    public void moveRight() { setScaleX(-1); isMoveLeft = false; isMoveRight = true; }
    public void stop() { isMoveLeft = false; isMoveRight = false; }

    public void setMoveSpeed(double speed) {
        this.moveSpeed = speed;
    }
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void setJumpStrength(double jumpStrength) {
        this.jumpStrength = jumpStrength;
    }

    public void setMaxFallSpeed(double maxFallSpeed) {
        this.maxFallSpeed = maxFallSpeed;
    }

    public void moveX() {
        // การเคลื่อนที่ซ้าย/ขวา
        if (isMoveLeft) {
            x -= moveSpeed;
            setScaleX(1);
        } else if (isMoveRight) {
            x += moveSpeed;
            setScaleX(-1);
        }
        setTranslateX(x);
        // ✅ ตรวจว่าจบด่านหรือยัง (Stage 1 → Stage 2)
        if (gameStage instanceof GameStage1) {
            GameStage1 gs1 = (GameStage1) gameStage;
            if (!gs1.getBossList().isEmpty()
                    && !gs1.getBossList().get(0).isAlive()
                    && getTranslateX() > GameStage1.WIDTH) {
                Platform.runLater(gs1::goToNextStage);
            }
        }
        // ✅ ตรวจชนกับบอส
        for (BossBase boss : gameStage.getBossList()) {
            if (boss instanceof Boss1) {
                collided((Boss1) boss);
            } else if (boss instanceof Boss2) {
                collided((Boss2) boss);
            }
        }
    }

    public void checkPlatformCollision(List<PlatformG> platforms) {
        onGround = false;

        for (PlatformG platform : platforms) {
            double platformTop = platform.getTopY();
            double platformLeft = platform.getLeftX();
            double platformRight = platform.getRightX();

            // ตรวจกระโดด/ตกลงพื้น platform
            if (y + characterHeight >= platformTop && y + characterHeight <= platformTop + 10) {
                if (x + characterWidth > platformLeft && x < platformRight) {
                    y = (int)platformTop - characterHeight;
                    velocityY = 0;
                    onGround = true;
                }
            }

            // ตรวจชนซ้าย/ขวา platform (option)
            if (y + characterHeight > platformTop && y < platform.getBottomY()) {
                if (x < platformRight && x + characterWidth > platformRight) { // ชนขวา
                    x = (int)platformRight;
                    stop();
                }
                if (x + characterWidth > platformLeft && x < platformLeft) { // ชนซ้าย
                    x = (int)platformLeft - characterWidth;
                    stop();
                }
            }
        }
    }

public void applyGravity() {
    onGround = false;

    for (PlatformG platform : gameStage.getPlatforms()) {
        double top = platform.getTopY();
        double left = platform.getLeftX();
        double right = platform.getRightX();

        if (y + characterHeight <= top && y + characterHeight + velocityY >= top) {
            if (x + characterWidth > left && x < right) {
                y = (int)top - characterHeight;
                velocityY = 0;
                onGround = true;
            }
        }
    }

    if (!onGround) {
        velocityY += gravity;
        if (velocityY > maxFallSpeed) velocityY = maxFallSpeed;
        y += velocityY;

        if (y + characterHeight >= GameStage2.GROUND) {
            y = GameStage2.GROUND - characterHeight;
            velocityY = 0;
            onGround = true;
        }
    }

    setTranslateY(y);
}

    public void moveY() {
        if (isJumping) {
            y += velocityY;
            velocityY += gravity / 3;
            if (velocityY >= 0) {
                isJumping = false;
                isFalling = true;
            }
        } else {
            applyGravity();
        }
        setTranslateY(y);
    }

    public void jump() {
        if (onGround) {
            velocityY = -jumpStrength; // แรงกระโดด
            onGround = false;
        }
    }


    public void repaint() {
        moveX();
        moveY();
        moveBullets();
        applyGravity(); // 🪂
    }

    // ========= ชนกับบอส =========
    public void collided(Boss1 c) {
        if (c == null || !c.isAlive()) return;
        if (this.getBoundsInParent().intersects(c.getBoundsInParent())) {
            if (this.isMoveRight && this.x < c.getX()) this.x = (int) (c.getX() - this.characterWidth);
            else if (this.isMoveLeft && this.x > c.getX()) this.x = (int) (c.getX() + c.getCharacterWidth());
            this.stop(); setTranslateX(this.x);
        }
    }

    public void collided(Boss2 c) {
        if (c == null || !c.isAlive()) return;
        if (this.getBoundsInParent().intersects(c.getBoundsInParent())) {
            if (this.isMoveRight && this.x < c.getX()) this.x = (int) (c.getX() - this.characterWidth);
            else if (this.isMoveLeft && this.x > c.getX()) this.x = (int) (c.getX() + c.getCharacterWidth());
            this.stop(); setTranslateX(this.x);
        }
    }

    public void respawn() {
        this.x = this.startX;
        this.y = this.startY;
        setTranslateX(x);
        setTranslateY(y);

        this.isFalling = true;
        this.canJump = false;
        this.isJumping = false;

        // 🪂 บังคับให้ตกลงพื้นหลังรีสปาวน์ (แก้ลอย)
        Platform.runLater(() -> {
            applyGravity();     // ใช้แรงโน้มถ่วง 1 ครั้ง
            checkReachFloor();  // ตรวจพื้น (จากระบบเดิม)
        });
    }


    public void die() {
        if (lives > 1) {
            lives--;
            gameStage.updateHearts(lives);
            respawn();
        } else {
            lives = 0;
            gameStage.updateHearts(lives);
            gameOver();
        }
    }

    public void gameOver() {
        Platform.runLater(() -> {
            VBox popupLayout = new VBox(20);
            popupLayout.setAlignment(Pos.CENTER);
            popupLayout.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 30px;");
            Text loseText = new Text("YOU LOSE");
            loseText.setFont(Font.font("Verdana", 40));
            loseText.setFill(Color.RED);
            Button tryAgainBtn = new Button("TRY AGAIN");
            Button backBtn = new Button("BACK TO MENU");
            popupLayout.getChildren().addAll(loseText, tryAgainBtn, backBtn);
            Scene popupScene = new Scene(popupLayout, GameStage1.WIDTH, GameStage1.HEIGHT);
            Stage popupStage = new Stage();
            popupStage.setTitle("Game Over");
            popupStage.setScene(popupScene);
            popupStage.show();

            tryAgainBtn.setOnAction(e -> {
                popupStage.close();
                gameStage.restartStage();
            });
            backBtn.setOnAction(e -> {
                popupStage.close();
                Launcher.switchToMenu();
            });
        });
    }

    // ========= getter =========
    public KeyCode getLeftKey() { return leftKey; }
    public KeyCode getRightKey() { return rightKey; }
    public KeyCode getUpKey() { return upKey; }
    public KeyCode getSpaceKey() { return spaceKey; }
    public KeyCode getDownKey() { return downKey; }
    public AnimatedSprite getImageView() { return imageView; }
    public int getCharacterWidth() { return characterWidth; }
    public int getCharacterHeight() { return characterHeight; }
    public int getX() { return x; }
    public int getY() { return y; }
    public List<Bullet> getBullets() { return bullets; }

    // ========== Compatibility for old DrawingLoop ==========
    public void checkReachGameWall() {
        // ป้องกันไม่ให้ออกนอกจอ
        if (x < 0) x = 0;
        else if (x + characterWidth > GameStage1.WIDTH)
            x = GameStage1.WIDTH - characterWidth;
        setTranslateX(x);
    }

    public void checkReachHighest() {
        // ถ้ากระโดดสูงสุดแล้วหยุดขึ้น
        if (isJumping && velocityY >= 0) {
            isJumping = false;
            isFalling = true;
        }
    }

    public void checkReachFloor() {
        // ให้ระบบแรงโน้มถ่วง handle พื้น (เรียกใช้ applyGravity)
        applyGravity();
    }

}
