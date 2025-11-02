package se233.project2.view;

public class Stage1 extends GameStageBase {

    public Stage1() {
        super("assets/stage1_bg.png");
    }

    @Override
    protected void setupStage() {
        addCharacter(new GameCharacter(0, 30, 30, "assets/Character1.png",
                4, 3, 2, 111, 97, KeyCode.A, KeyCode.D, KeyCode.W));

        addPlatform(new PlatformG(100, 250, 200, 20));
        addScoreUI(new Score(30, GROUND + 30));

        addBoss(new Boss1(500, 200));
    }

    @Override
    public void restartStage() {
        System.out.println("Restart Stage 1");
    }

    @Override
    public void goToNextStage() {
        System.out.println("Go to Stage 2");
    }
}
