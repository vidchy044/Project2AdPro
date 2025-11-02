package se233.project2.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import se233.project2.Launcher;

public class Bullet extends Pane {
    private ImageView imageView;
    private int xVelocity = 10; // Bullet speed

    public Bullet(int startX, int startY, boolean facingLeft) {
        System.out.println("Creating bullet at (" + startX + ", " + startY + ")");
        Image bulletImage = new Image(Launcher.class.getResourceAsStream("assets/Bullet.png"));
        this.imageView = new ImageView(bulletImage);
        this.imageView.setFitWidth(10); // Adjust as necessary
        this.imageView.setFitHeight(10); // Adjust as necessary

        this.setTranslateX(startX);
        this.setTranslateY(startY);

        // Flip the bullet if the character is facing left
        if (facingLeft) {
            this.setScaleX(-1);
            this.xVelocity = -Math.abs(xVelocity); // Move left
        }

        this.getChildren().add(imageView);
    }

    public void move() {
        setTranslateX(getTranslateX() + xVelocity);
    }
}
