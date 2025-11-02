package se233.project2.model;

import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Keys {
    private  HashMap<KeyCode,Boolean> keys;
    public Keys() {
        keys = new HashMap<>();
    }
    public void add(KeyCode key) {
        keys.put(key, true);
    }
    public void remove(KeyCode key) {
        keys.put(key, false);
    }
    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key,false);
    }

    public void setKeyPressed(KeyCode keyCode) {
        this.keys.put(keyCode,true);
    }
    public void setKeyReleased(KeyCode keyCode) {
        this.keys.put(keyCode,false);
    }
}