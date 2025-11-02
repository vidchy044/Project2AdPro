//package se233.project2.model;
//
//import javafx.scene.layout.Pane;
//
//public abstract class BossBase extends Pane {
//    public abstract void takeDamage(int damage);
//    public abstract boolean isAlive();
//}

package se233.project2.model;

import javafx.scene.layout.Pane;

public abstract class BossBase extends Pane {
    protected int health;
    protected boolean alive = true;
    protected boolean dead = false;

    public abstract void takeDamage(int damage);

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() { return dead; }

    public int getHealth() {
        return health;
    }
}
