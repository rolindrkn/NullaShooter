package com.mygdx.nullapointershooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.math.Intersector.intersectRectangles;

public class ShotManager {
    public static final int SHOT_Y_OFFSET = 40;
    public static final int SHOT_SPEED = 10;
    private static final float MININUM_TIME_BETWEEN_SHOTS = .5f;
    private static final float ENEMY_SHOT_Y_OFFSET = Gdx.graphics.getHeight() - 100;
    private final Texture shotTexture;
    private Texture enemyShotTexture;
    private List<AnimatedSprite> shots = new ArrayList<AnimatedSprite>();
    List<AnimatedSprite> enemyShots = new ArrayList<AnimatedSprite>();
    private float timeSinceLastShot = 0;


    public ShotManager(Texture shotTexture, Texture enemyShotTexture) {
        this.shotTexture = shotTexture;
        this.enemyShotTexture = enemyShotTexture;
    }

    public void firePlayerShot(int shipCenterXLocation) {
       if(canFireShot()) {
           Sprite newShot = new Sprite(shotTexture);
           AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
           newShotAnimated.setPosition(shipCenterXLocation, SHOT_Y_OFFSET);
           newShotAnimated.setVelocity(new Vector2(0, SHOT_SPEED));

           shots.add(newShotAnimated);
           timeSinceLastShot = 0f;
       }
    }

    private boolean canFireShot() {
        return timeSinceLastShot > MININUM_TIME_BETWEEN_SHOTS;
    }

    public void update() {
        Iterator<AnimatedSprite> i = shots.iterator();
        while(i.hasNext()) {
            AnimatedSprite shot = i.next();
            shot.move();
            if(shot.getY() > Gdx.graphics.getHeight()) {
                i.remove();
            }
        }

        Iterator<AnimatedSprite> j = enemyShots.iterator();
        while(j.hasNext()) {
            AnimatedSprite shot = j.next();
            shot.move();
            if(shot.getY() < 0) {
                j.remove();
            }
        }
        timeSinceLastShot += Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        for(AnimatedSprite shot: shots) {
            shot.draw(batch);
        }
        for(AnimatedSprite enemyShot : enemyShots) {
            enemyShot.draw(batch);
        }
    }

    public void fireEnemyShot(int enemyCenterXLocation) {
        Sprite newShot = new Sprite(enemyShotTexture);
        AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
        newShotAnimated.setPosition(enemyCenterXLocation, ENEMY_SHOT_Y_OFFSET);
        newShotAnimated.setVelocity(new Vector2(0, -SHOT_SPEED));
        enemyShots.add(newShotAnimated);
    }

    public boolean playerShotTouches(Rectangle boundingBox) {
        return shotTouches(shots, boundingBox);
    }

    private boolean intersectRectangles(Rectangle boundingBox, Rectangle boundingBox1) {
        if(boundingBox.overlaps(boundingBox1))
            return true;
        return false;
    }

    public boolean enemyShotsTouches(Rectangle boundingBox) {
        return shotTouches(enemyShots, boundingBox);
    }

    private boolean shotTouches(List<AnimatedSprite> shots, Rectangle boundingBox) {
        Iterator<AnimatedSprite> i = shots.iterator();
        while(i.hasNext()) {
            AnimatedSprite shot = i.next();
            if(intersectRectangles(shot.getBoundingBox(), boundingBox)) {
                i.remove();
                return true;
            }
        }
        return false;
    }
}
