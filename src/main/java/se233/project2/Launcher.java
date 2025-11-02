//package se233.project2;
//
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import se233.project2.controller.DrawingLoop1;
//import se233.project2.controller.GameLoop1;
//import se233.project2.view.GameStage1;
//import se233.project2.view.GameStage2;
//
//public class Launcher extends Application {
//    private static Stage primaryStage;
//
//    // 🔹 เก็บ loop ปัจจุบันไว้
//    private static GameLoop1 currentGameLoop;
//    private static DrawingLoop1 currentDrawingLoop;
//
//    @Override
//    public void start(Stage stage) {
//        primaryStage = stage;
//        goToStage1();
//    }
//
//    // ✅ เริ่ม Stage 1
//    public static void goToStage1() {
//        GameStage1 stage1 = new GameStage1();
//        Scene scene = new Scene(stage1, GameStage1.WIDTH, GameStage1.HEIGHT);
//
//        scene.setOnKeyPressed(e -> stage1.getKeys().add(e.getCode()));
//        scene.setOnKeyReleased(e -> stage1.getKeys().remove(e.getCode()));
//
//        currentGameLoop = new GameLoop1(stage1);
//        currentDrawingLoop = new DrawingLoop1(stage1);
//
//        new Thread(currentGameLoop).start();
//        new Thread(currentDrawingLoop).start();
//
//        primaryStage.setTitle("2D Platformer - Stage 1");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        stage1.requestFocus();
//    }
//
//    // ✅ ไป Stage 2 (หยุด loop เก่า, โหลดใหม่)
//    public static void goToNextStage() {
//        Platform.runLater(() -> {
//            // 🔴 หยุด loop เก่าทั้งคู่
//            if (currentGameLoop != null) currentGameLoop.stop();
//            if (currentDrawingLoop != null) currentDrawingLoop.stop();
//
//            // 🟩 โหลด Stage 2
//            GameStage2 nextStage = new GameStage2();
//            Scene nextScene = new Scene(nextStage, GameStage2.WIDTH, GameStage2.HEIGHT);
//
//            nextScene.setOnKeyPressed(e -> nextStage.getKeys().add(e.getCode()));
//            nextScene.setOnKeyReleased(e -> nextStage.getKeys().remove(e.getCode()));
//
//            // 🟢 เริ่ม loop ใหม่
//            currentGameLoop = new GameLoop1(nextStage);
//            currentDrawingLoop = new DrawingLoop1(nextStage);
//            new Thread(currentGameLoop).start();
//            new Thread(currentDrawingLoop).start();
//
//            primaryStage.setTitle("2D Platformer - Stage 2");
//            primaryStage.setScene(nextScene);
//            nextStage.requestFocus();
//        });
//    }
//
//    public static void switchToMenu() {
//        // ใส่เมนูของนายเองได้ตรงนี้ เช่นกลับไปหน้าเลือก Stage
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
package se233.project2;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se233.project2.controller.DrawingLoop1;
import se233.project2.controller.GameLoop1;
import se233.project2.model.AnimatedSprite;
import se233.project2.view.GameStage1;
import se233.project2.view.GameStage2;
import javafx.scene.text.Font;


public class Launcher extends Application {
    private static Stage primaryStage;




    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        goToStage1();


        Font gameFont = Font.loadFont(
                Launcher.class.getResource("/se233/project2/assets/PressStart2P_Regular.ttf").toExternalForm(),
                20
        );


        Button playButton = new Button("Stage 1");
        playButton.setFont(gameFont);
        playButton.setStyle("-fx-pref-width: 200px; -fx-background-color: transparent; -fx-text-fill: white;");




        Button playButton2 = new Button("Stage 2");
        playButton2.setFont(gameFont);
        playButton2.setStyle("-fx-pref-width: 200px; -fx-background-color: transparent; -fx-text-fill: white;");




        Button playButton3 = new Button("Stage 3");
        playButton3.setFont(gameFont);
        playButton3.setStyle("-fx-pref-width: 200px; -fx-background-color: transparent; -fx-text-fill: white;");




        Button quitButton = new Button("Quit");
        quitButton.setFont(gameFont);
        quitButton.setStyle("-fx-pref-width: 200px; -fx-background-color: transparent; -fx-text-fill: white;");




        VBox menuButtons = new VBox(20, playButton, playButton2, playButton3 , quitButton);
        menuButtons.setAlignment(Pos.CENTER_LEFT);
        menuButtons.setTranslateX(170);
        menuButtons.setTranslateY(90);


        // === พื้นหลัง ===
        Image backgroundImage = new Image(Launcher.class.getResourceAsStream("assets/Menu.png"));
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(800);
        backgroundView.setFitHeight(600);
        backgroundView.setPreserveRatio(false);


        Image titleImage = new Image(Launcher.class.getResourceAsStream("assets/MenuTitle1.png"));
        AnimatedSprite animatedTitle = new AnimatedSprite(titleImage, 4, 1, 4, 0, 0, 240, 100);


        animatedTitle.setTranslateX(0);
        animatedTitle.setTranslateY(-120);


        // === รวมทุกอย่างไว้ใน StackPane ===
        StackPane root = new StackPane(backgroundView, animatedTitle, menuButtons);
        Scene menuScene = new Scene(root, 800, 600);


        stage.setTitle("2D Platformer - Main Menu");
        stage.setScene(menuScene);
        stage.show();


        // === เริ่ม loop เพื่ออัปเดตอนิเมชันของ title ===
        AnimationTimer titleAnimation = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 150_000_000) { // ทุก 0.15 วินาที
                    animatedTitle.tick();
                    lastUpdate = now;
                }
            }
        };
        titleAnimation.start();


        // === ปุ่ม ===
        playButton.setOnAction(e -> {
            titleAnimation.stop();
            goToStage1();
        });


        playButton2.setOnAction(e -> {
            titleAnimation.stop();
            goToNextStage();
        });


//        playButton3.setOnAction(e -> {
//            titleAnimation.stop();
//            goToStage3();
//        });


        quitButton.setOnAction(e -> {
            titleAnimation.stop();
            Platform.exit();
        });
    }


    // 🔹 เก็บ loop ปัจจุบันไว้
    private static GameLoop1 currentGameLoop;
    private static DrawingLoop1 currentDrawingLoop;




    // ✅ เริ่ม Stage 1
    public static void goToStage1() {
        GameStage1 stage1 = new GameStage1();
        Scene scene = new Scene(stage1, GameStage1.WIDTH, GameStage1.HEIGHT);


        scene.setOnKeyPressed(e -> stage1.getKeys().add(e.getCode()));
        scene.setOnKeyReleased(e -> stage1.getKeys().remove(e.getCode()));


        currentGameLoop = new GameLoop1(stage1);
        currentDrawingLoop = new DrawingLoop1(stage1);


        new Thread(currentGameLoop).start();
        new Thread(currentDrawingLoop).start();


        primaryStage.setTitle("2D Platformer - Stage 1");
        primaryStage.setScene(scene);
        primaryStage.show();
        stage1.requestFocus();
    }




    // ✅ ไป Stage 2 (หยุด loop เก่า, โหลดใหม่)
    public static void goToNextStage() {
        Platform.runLater(() -> {
            // 🔴 หยุด loop เก่าทั้งคู่
            if (currentGameLoop != null) currentGameLoop.stop();
            if (currentDrawingLoop != null) currentDrawingLoop.stop();


            // 🟩 โหลด Stage 2
            GameStage2 nextStage = new GameStage2();
            Scene nextScene = new Scene(nextStage, GameStage2.WIDTH, GameStage2.HEIGHT);


            nextScene.setOnKeyPressed(e -> nextStage.getKeys().add(e.getCode()));
            nextScene.setOnKeyReleased(e -> nextStage.getKeys().remove(e.getCode()));


            // 🟢 เริ่ม loop ใหม่
            currentGameLoop = new GameLoop1(nextStage);
            currentDrawingLoop = new DrawingLoop1(nextStage);
            new Thread(currentGameLoop).start();
            new Thread(currentDrawingLoop).start();


            primaryStage.setTitle("2D Platformer - Stage 2");
            primaryStage.setScene(nextScene);
            nextStage.requestFocus();
        });
    }


    public static void switchToMenu() {
        // ใส่เมนูของนายเองได้ตรงนี้ เช่นกลับไปหน้าเลือก Stage
    }


    public static void main(String[] args) {
        launch(args);
    }
}


