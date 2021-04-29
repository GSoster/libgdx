package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;


import java.util.Iterator;

public class Drop extends ApplicationAdapter {
	SpriteBatch batch;

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private OrthographicCamera camera;
	private Rectangle bucket;

	private Array<Rectangle> raindrops;
	private long lastDropTime;//last time spawned a raindrop
	private Vector3 touchPos = new Vector3();

	private static final int SCREEN_WIDTH = 800;
	private static final int SCREEN_HEIGHT = 480;
	private static final short raindropImageSize = 64;

	@Override
	public void create () {
		batch = new SpriteBatch();

		dropImage = new Texture(Gdx.files.internal("drop.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		batch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = SCREEN_WIDTH / 2 - 64 / 2; //centered in the middle
		bucket.y = 20; //20pixels above the bottom edge
		bucket.width = raindropImageSize;
		bucket.height = raindropImageSize;

		raindrops = new Array<>();
		spawnRaindrop();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0.2f, 1);

		//mouse movement
		if(Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - raindropImageSize / 2;
		}

		//keyboard movement
		//Gdx.graphics.getDeltaTime() returns the time passed between the last and the current frame in seconds.
		//this way we ALWAYS move a max of 200pixels per second
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();
		//make bucket stay within screen limits
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > SCREEN_WIDTH - raindropImageSize) bucket.x = SCREEN_WIDTH - raindropImageSize;

		//spawn a new raindrop if necessary
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

		//make raindrops move
		for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + raindropImageSize < 0) iter.remove();

			//remove raindrops that are captured by the bucket
			if(raindrop.overlaps(bucket)) {
				dropSound.play();
				iter.remove();
			}
		}

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop : raindrops) {
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, SCREEN_WIDTH - raindropImageSize);
		raindrop.y = SCREEN_HEIGHT;
		raindrop.width = raindropImageSize;
		raindrop.height = raindropImageSize;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();

	}

}
