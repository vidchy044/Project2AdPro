package se233.project2.view;

public class Stage2 extends GameStageBase {

    public Stage2() {
        super("assets/stage2_bg.png");
    }

    @Override
    protected void setupStage() {
        addCharacter(new GameCharacter(0, 50, 30, "assets/Character1.png",
                4, 3, 2, 111, 97, KeyCode.A, KeyCode.D, KeyCode.W));

        addPlatform(new PlatformG(150, 200, 300, 25));

        addBoss(new Boss2(550, 180));  // another boss type
        addMinion(new Minion1(300, 250));
    }

    @Override
    public void restartStage() { }

    @Override
    public void goToNextStage() { }
}

