package com.mygdx.nullapointershooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShooterGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	SpriteBatch batch;
	private Texture background;
	private AnimatedSprite spaceshipAnimated;
	private ShotManager shotManager;
	private Enemy enemy;
	private ShotManager enemyShot;
	private CollisionManager collisionManager;
	private boolean isGameOver = false;

	public ShooterGame() {
	}

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		batch = new SpriteBatch();
		//background = new Texture(Gdx.files.internal("background.png"));
		Texture spaceshipTexture = new Texture(Gdx.files.internal("animation.png"));
		Sprite spaceshipSprite = new Sprite(spaceshipTexture);
		spaceshipAnimated = new AnimatedSprite(spaceshipSprite);
		spaceshipAnimated.setPosition(Gdx.graphics.getWidth()/2, 0);

		Texture shotTexture = new Texture(Gdx.files.internal("blastersAnimated.png"));
		Texture enemyShotTexture = new Texture(Gdx.files.internal("enemyLaserAnimated.png"));
		shotManager = new ShotManager(shotTexture, enemyShotTexture);
				
		Texture enemyTexture = new Texture(Gdx.files.internal("enemyAnimated.png"));
		enemy = new Enemy(enemyTexture, shotManager);

		collisionManager = new CollisionManager(spaceshipAnimated, enemy, shotManager);
	}

	@Override
	public void render () { //update the game state
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		batch.draw(background, 0, 0);

		if(isGameOver) {
			BitmapFont font = new BitmapFont();

			font.draw(batch, "PLAYER HIT", 200, 200);
		}

		spaceshipAnimated.draw(batch);
		enemy.draw(batch);
		shotManager.draw(batch);
		batch.end();

		//touch
		handleInput();

		if(!isGameOver) {
			//update position
			spaceshipAnimated.move();
			enemy.update();
			shotManager.update();

			collisionManager.handleCollisions();
		}

		if(spaceshipAnimated.isDead()) {
			isGameOver = true;
		}
	}

	private void handleInput() {
		if(Gdx.input.isTouched()) {
			if(isGameOver) {
				isGameOver = false;
				spaceshipAnimated.setDead(false);
			}
			int xTouch = Gdx.input.getX();
			//int yTouch = Gdx.input.getY();
			if(xTouch > spaceshipAnimated.getX()) {
				spaceshipAnimated.moveRight();
			} else {
				spaceshipAnimated.moveLeft();
			}
			shotManager.firePlayerShot(spaceshipAnimated.getX());
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
//		background.dispose();
	}
}
