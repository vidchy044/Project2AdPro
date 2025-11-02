package se233.project2.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlatformG extends Rectangle {
    private double x;
    private double y;

    public PlatformG(double width, double height, double x, double y, Color color) {
        super(width, height, color);
        this.x = x;
        this.y = y;
        setTranslateX(x);
        setTranslateY(y);
    }

    public double getXPos() {
        return x;
    }

    public double getYPos() {
        return y;
    }

    public double getTopY() {
        return y;
    }

    public double getBottomY() {
        return y + getHeight();
    }
    public double getLeftX() { return x; }
    public double getRightX() { return x + getWidth(); }
}
