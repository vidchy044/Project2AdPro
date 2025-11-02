package se233.project2.controller;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import se233.project2.model.*;
import se233.project2.view.GameStage1;
import se233.project2.view.GameStageBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameLoop1 implements Runnable {
    //private GameStage1 gameStage;
    private GameStageBase gameStage;
    private int frameRate = 10;
    private float interval = 1000.0f / frameRate;
    private boolean running = true;



    public GameLoop1(GameStageBase gameStage) {
    this.gameStage = gameStage;
}


private void update(List<GameCharacter1> gameCharacterList) {
        for (GameCharacter1 gameCharacter : gameCharacterList) {
            boolean leftPressed = gameStage.getKeys().isPressed(gameCharacter.getLeftKey());
            boolean rightPressed = gameStage.getKeys().isPressed(gameCharacter.getRightKey());
            boolean upPressed = gameStage.getKeys().isPressed(gameCharacter.getUpKey());
            boolean spacePressed = gameStage.getKeys().isPressed(gameCharacter.getSpaceKey());
            boolean downPressed = gameStage.getKeys().isPressed(gameCharacter.getDownKey());

            if (downPressed) {
                gameCharacter.prone(); // คลาน
            } else {
                gameCharacter.stopProne(); // ปล่อยปุ่มกลับมายืน
            }

            if (leftPressed && rightPressed) {
                gameCharacter.stop();
            } else if (leftPressed) {
                gameCharacter.getImageView().tick();
                gameCharacter.moveLeft();
            } else if (rightPressed) {
                gameCharacter.getImageView().tick();
                gameCharacter.moveRight();
            } else if (!downPressed){
                gameCharacter.stop();
            }

            if (spacePressed) {
                gameCharacter.getImageView().tick();
                gameCharacter.shoot();
            }

            if (upPressed) {
                gameCharacter.jump();
            }

            // อัปเดตตำแหน่งและ bullet
            gameCharacter.repaint();
            gameCharacter.checkPlatformCollision(gameStage.getPlatforms());
            // ตลคไปฉากต่อไป
//            if (gameCharacter.getX() + gameCharacter.getCharacterWidth() >= GameStage1.WIDTH - 5) {
//                Platform.runLater(() -> {
//                    System.out.println("Next stage!");
//                    gameStage.goToNextStage();
//                });
//            }
            // ตลคไปฉากต่อไป (เฉพาะเมื่อบอสตายแล้วใน Stage 1)
            if (gameCharacter.getX() + gameCharacter.getCharacterWidth() >= GameStage1.WIDTH - 5) {
                boolean canExit = true;
                if (gameStage instanceof se233.project2.view.GameStage1) {
                    se233.project2.view.GameStage1 gs1 = (se233.project2.view.GameStage1) gameStage;
                    canExit = gs1.isBossDefeated(); // ต้องให้บอสตายก่อน จึงจะไปต่อได้
                }
                if (canExit) {
                    Platform.runLater(() -> {
                        System.out.println("Next stage!");
                        gameStage.goToNextStage();
                    });
                }
            }


        }
    }

//    private void updateBoss1Bullets(List<Boss1> bosses) {
    private void updateBoss1Bullets(List<? extends Boss1> bosses) {
        for (Boss1 boss : bosses) {
            if (boss.isAlive()) {
                // เรียก repaint() ของ Boss1 เพื่ออัปเดตกระสุน
                boss.repaint();  // อัปเดตการเคลื่อนที่ของกระสุน
            }
        }
    }


    private void checkBulletHitBoss(List<GameCharacter1> players, List<? extends Boss1> bosses) {
        for (Boss1 boss : bosses) {
            if (!boss.isAlive()) continue;
            for (GameCharacter1 player : players) {
                Iterator<Bullet> iterator = player.getBullets().iterator();
                while (iterator.hasNext()) {
                    Bullet bullet = iterator.next();
                    if (bullet.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                        boss.takeDamage(10);
                        iterator.remove();
                        Platform.runLater(() -> gameStage.getBulletsLayer().getChildren().remove(bullet));

                        // 🧱 ถ้า Boss HP หมด → ไป Stage ต่อไป
//                        if (boss.getHealth() <= 0) {
//                            Platform.runLater(() -> gameStage.goToNextStage());
//                        }
                        if (boss.getHealth() <= 0) {
                            // ไม่ข้ามฉากทันที ปล่อยให้ flow ไปตามเงื่อนไข 'เดินชนขอบเมื่อบอสตายแล้ว'
                            boolean playerAtExit = false;
                            for (GameCharacter1 p : players) {
                                if (p.getX() + p.getCharacterWidth() >= GameStage1.WIDTH - 5) {
                                    playerAtExit = true;
                                    break;
                                }
                            }
                            if (playerAtExit) {
                                Platform.runLater(() -> gameStage.goToNextStage());
                            }
                        }


                        break;
                    }
                }
            }
        }
    }


    private void checkBossBulletHitPlayer(List<? extends Boss1> bosses, List<GameCharacter1> players) {
        for (Boss1 boss : bosses) {
            if (!boss.isAlive()) continue;

            for (GameCharacter1 player : players) {
                Iterator<Boss1Bullet> iterator = boss.bossBullets.iterator();
                while (iterator.hasNext()) {
                    Boss1Bullet bullet = iterator.next();
                    if (bullet.getBoundsInParent().intersects(player.getBoundsInParent())) {
                        // ❌ ลดหัวใจ 1 ดวง เมื่อโดนยิง
                        Platform.runLater(() -> {
                            player.die(); // จะเรียก updateHearts(lives) และ gameOver() ให้เอง (มีอยู่ใน GameCharacter1)
                        });

                        // ✅ ลบกระสุนออก
                        iterator.remove();
                        Platform.runLater(() -> {
                            if (player.getParent() != null) {
                                ((Pane) player.getParent()).getChildren().remove(bullet);
                            }
                        });

                        break; // ออกจาก loop เมื่อโดนกระสุน 1 นัด
                    }
                }
            }
        }
    }



    //เพิ่มฟังก์ชันนี้เพื่อตรวจว่า player เดินชน boss หรือไม่
//    private void checkPlayerBossCollision(List<GameCharacter1> players, List<Boss1> bosses) {
    private void checkPlayerBossCollision(List<GameCharacter1> players, List<? extends Boss1> bosses) {
        for (GameCharacter1 player : players) {
            for (Boss1 boss : bosses) {
                // ตรวจสอบขอบเขตซ้อนกัน
                boolean overlapX = player.getX() < boss.getX() + boss.getCharacterWidth() &&
                        player.getX() + player.getCharacterWidth() > boss.getX();
                boolean overlapY = player.getY() < boss.getY() + boss.getCharacterHeight() &&
                        player.getY() + player.getCharacterHeight() > boss.getY();

//                if (overlapX && overlapY) {
//                    // เรียก collided() เพื่อให้หยุดการเคลื่อนไหว
//                    player.collided(boss);
//                }
                if (boss.isAlive() && overlapX && overlapY) {
                    player.collided(boss);
                }

            }
        }
    }
//
//    @Override
//    public void run() {
//        while (running) {
//            long start = System.currentTimeMillis();
//
//            update(gameStage.getGameCharacterList());
////            updateBoss1Bullets(gameStage.getBossList()); // อัปเดตการเคลื่อนที่ของกระสุนของ Boss1
////            checkBulletHitBoss(gameStage.getGameCharacterList(), gameStage.getBossList());
////            checkPlayerBossCollision(gameStage.getGameCharacterList(), gameStage.getBossList()); // ✅ เรียกที่นี่
//            updateBoss1Bullets((List<? extends Boss1>) gameStage.getBossList());
//            checkBulletHitBoss(gameStage.getGameCharacterList(), (List<? extends Boss1>) gameStage.getBossList());
//            checkPlayerBossCollision(gameStage.getGameCharacterList(), (List<? extends Boss1>) gameStage.getBossList());
//            checkBossBulletHitPlayer(gameStage.getBossList(), gameStage.getGameCharacterList());
//
//            long elapsed = System.currentTimeMillis() - start;
//            if (elapsed < interval) {
//                try {
//                    Thread.sleep((long) (interval - elapsed));
//                } catch (InterruptedException ignored) {}
//            }
//        }
//    }

private void checkBulletHitMinion(List<GameCharacter1> players, List<Minion1> minions) {
    for (Minion1 minion : minions) {
        if (!minion.isAlive()) continue;
        for (GameCharacter1 player : players) {
            Iterator<Bullet> iterator = player.getBullets().iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                if (bullet.getBoundsInParent().intersects(minion.getBoundsInParent())) {
                    minion.takeDamage(10); // ลด HP มินเนี่ยน
                    iterator.remove();


                    // ลบ bullet ออกจากฉาก
                    Platform.runLater(() -> gameStage.getBulletsLayer().getChildren().remove(bullet));
                    break;
                }
            }
        }
    }
}
    // ✅ อัปเดตให้ตรวจสอบ minion ก่อนยิง boss ได้
    private void checkBulletHitBossAndMin1(List<GameCharacter1> players,
                                           List<? extends Boss1> bosses,
                                           List<Minion1> minions) {
        // ถ้ายังมี minion อยู่ → ห้ามยิงโดน boss
        boolean allMinionsDefeated = minions.stream().noneMatch(Minion1::isAlive);
        if (!allMinionsDefeated) return;


        for (Boss1 boss : bosses) {
            if (!boss.isAlive()) continue;
            for (GameCharacter1 player : players) {
                Iterator<Bullet> iterator = player.getBullets().iterator();
                while (iterator.hasNext()) {
                    Bullet bullet = iterator.next();
                    if (bullet.getBoundsInParent().intersects(boss.getBoundsInParent())) {
                        boss.takeDamage(10);
                        iterator.remove();
                        Platform.runLater(() -> gameStage.getBulletsLayer().getChildren().remove(bullet));


                        if (boss.getHealth() <= 0) {
                            boolean playerAtExit = false;
                            for (GameCharacter1 p : players) {
                                if (p.getX() + p.getCharacterWidth() >= GameStage1.WIDTH - 5) {
                                    playerAtExit = true;
                                    break;
                                }
                            }
                            if (playerAtExit) {
                                Platform.runLater(() -> gameStage.goToNextStage());
                            }
                        }
                        break;
                    }
                }
            }
        }
    }


    @Override
    public void run() {
        while (running) {
            long start = System.currentTimeMillis();


            update(gameStage.getGameCharacterList());
            updateBoss1Bullets((List<? extends Boss1>) gameStage.getBossList());
            // 🔹 ตรวจว่ากระสุนชน minion ก่อน
            checkBulletHitMinion(gameStage.getGameCharacterList(), gameStage.getMinionList());
            // 🔹 ยิง boss ได้เฉพาะตอน minion ตายหมด
            checkBulletHitBossAndMin1(gameStage.getGameCharacterList(),
                    (List<? extends Boss1>) gameStage.getBossList(),
                    gameStage.getMinionList());

            checkPlayerBossCollision(gameStage.getGameCharacterList(),
                    (List<? extends Boss1>) gameStage.getBossList());
            checkBossBulletHitPlayer(gameStage.getBossList(), gameStage.getGameCharacterList());


            long elapsed = System.currentTimeMillis() - start;
            if (elapsed < interval) {
                try {
                    Thread.sleep((long) (interval - elapsed));
                } catch (InterruptedException ignored) {}
            }
        }
    }

    public void stop() {
        running = false;
    }

}
