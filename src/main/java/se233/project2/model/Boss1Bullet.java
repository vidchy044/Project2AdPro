package se233.project2.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import se233.project2.view.GameStage1;

public class Boss1Bullet extends Rectangle {
    private double vX;
    private double vY;
    private double speedMultiplier = 2.0;
    private long startTime;

    public Boss1Bullet(double x, double y, double vX, double vY) {
        super(10, 10, Color.RED); // ขนาดและสีของกระสุน
        this.vX = vX* speedMultiplier;
        this.vY = vY* speedMultiplier;
        this.startTime = System.currentTimeMillis();

        setTranslateX(x);
        setTranslateY(y);
    }

    public void move() {
        setTranslateX(getTranslateX() + vX);
        setTranslateY(getTranslateY() + vY);

        // ลบกระสุนเมื่อออกนอกจอ
        if (getTranslateX() < 0 || getTranslateX() > GameStage1.WIDTH
                || getTranslateY() < 0 || getTranslateY() > GameStage1.HEIGHT) {
            setVisible(false);
            setDisable(true);
        }
    }
        public boolean isCollidedWithPlayer(GameCharacter1 player) {
        return getBoundsInParent().intersects(player.getBoundsInParent());
    }

    public boolean isOutOfScreen() {
        return getTranslateX() < 0 || getTranslateX() > GameStage1.WIDTH;
    }
}
