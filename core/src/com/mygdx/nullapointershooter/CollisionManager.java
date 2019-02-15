package com.mygdx.nullapointershooter;

class CollisionManager {
    private AnimatedSprite spaceshipAnimated;
    private Enemy enemy;
    private ShotManager shotManager;

    public CollisionManager(AnimatedSprite spaceshipAnimated, Enemy enemy, ShotManager shotManager) {
        this.spaceshipAnimated = spaceshipAnimated;
        this.enemy = enemy;
        this.shotManager = shotManager;
    }

    public void handleCollisions() {
        handleEnemyShot();
        handlePlayerShot();
    }

    private void handlePlayerShot() {
        if(shotManager.enemyShotsTouches(spaceshipAnimated.getBoundingBox())) {
            spaceshipAnimated.setDead(true);
        }
    }

    private void handleEnemyShot() {
        if(shotManager.playerShotTouches(enemy.getBoundingBox())) {
            enemy.hit();
        }
    }
}
