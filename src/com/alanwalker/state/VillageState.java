package com.alanwalker.state;

import com.alanwalker.entities.Actor;
import com.alanwalker.main.AlanWalker;
import com.alanwalker.main.Settings;
import com.alanwalker.util.AnimationSet;
import com.alanwalker.util.LoadSave;
import com.alanwalker.util.PlayerControll;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class VillageState extends AbstractState {

	private AbstractState screen;
	private Actor player;
	private PlayerControll playerControll;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRender;
	private OrthographicCamera camera;
	private SpriteBatch sb;
	private AnimationSet animationAlan;
	private Skin skin;
	private Stage stage;
	private TextButton attackButton;

	protected Rectangle monsterSpawn, actor, nurse;
	private double positionMonsterX;
	private double positionMonsterY;

	// Status Player
	private String level;
	private String attack;
	private String playerHP;
	private float positionPlayerX, positionPlayerY;
	private LoadSave loadPlayer;

	public VillageState() {

	}

	public VillageState(AlanWalker aw) {
		super(aw);
		sb = new SpriteBatch();

		// Load data player
		loadPlayer = new LoadSave();
		level = loadPlayer.getLevel();
		attack = loadPlayer.getAttack();
		playerHP = loadPlayer.getPlayerHP();
		positionPlayerX = Float.parseFloat(loadPlayer.getStartX());
		positionPlayerY = Float.parseFloat(loadPlayer.getStartY());

		positionMonsterX = Math.random() * 10 + 1;
		positionMonsterY = Math.random() * 3 + 1;
		
		// Load Button TextureAtlas
		TextureAtlas startButtonAtlas = new TextureAtlas(Gdx.files.internal("resource/ui/button/button.atlas"));

		// Create a font
		BitmapFont font = new BitmapFont();

		// Load Button UI
		skin = new Skin(startButtonAtlas);
		skin.add("default", font);

		// Create a button style
		TextButton.TextButtonStyle attackButtonStyle = new TextButton.TextButtonStyle();
		attackButtonStyle.up = skin.newDrawable("attack-active");
		attackButtonStyle.over = skin.newDrawable("attack-over");
		attackButtonStyle.down = skin.newDrawable("attack-clicked");
		attackButtonStyle.disabled = skin.newDrawable("attack-disable");
		attackButtonStyle.font = skin.getFont("default");
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		attackButton = new TextButton("", attackButtonStyle); // Use the initialized skin
		attackButton.setPosition(Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 7);
		
		stage.addActor(attackButton);

		// Load Alan Character
		TextureAtlas alanAtlas = aw.getAssetManager().get("resource/character/alan/alan.atlas", TextureAtlas.class);
		animationAlan = new AnimationSet(
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_north"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_south"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_east"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_west"), PlayMode.LOOP_PINGPONG),
				alanAtlas.findRegion("alan_stand_north"), alanAtlas.findRegion("alan_stand_south"),
				alanAtlas.findRegion("alan_stand_east"), alanAtlas.findRegion("alan_stand_west"));

		// Load Map Village
		map = new TmxMapLoader().load("resource/maps/village/village.tmx");

		// Render Map Village
		mapRender = new OrthogonalTiledMapRenderer(map);

		// Load Camera
		camera = new OrthographicCamera();

		// Load Player
		player = new Actor(positionPlayerX, positionPlayerY, animationAlan, "VillageState");

		// Load Player Controll
		playerControll = new PlayerControll(player);

		// Input Movement
		Gdx.input.setInputProcessor(playerControll);
	}

	@Override
	public void dispose() {
		map.dispose();
		mapRender.dispose();
		sb.dispose();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		nurse = new Rectangle(12, 7, 1, 1);
//		monsterSpawn = new Rectangle((int) positionMonsterX, (int) positionMonsterY, 0, 0);

		actor = new Rectangle(player.getX(), player.getY(), 2, 2);
		playerControll.update(delta);
		player.update(delta);
		camera.position.set(player.getWorldX() * Settings.SCALED_TILE_SIZE + Gdx.graphics.getWidth() / 2,
				player.getWorldY() * Settings.SCALED_TILE_SIZE + Gdx.graphics.getHeight() / 2, 0);
		// if(camera.position.x > Settings.V_WIDTH) {
		// camera.position.x = Settings.V_WIDTH;
		// } else if(camera.position.x < Settings.V_WIDTH / 2) {
		// camera.position.x = Settings.V_WIDTH / 2;
		// }
		if (camera.position.y > Settings.V_HEIGHT) {
			camera.position.y = Settings.V_HEIGHT;
		} else if (camera.position.y < Settings.V_HEIGHT / 4) {
			camera.position.y = Settings.V_HEIGHT / 4;
		}

		camera.update();
		
		// Press "C" to talk Nurse in nearby
		if (actor.overlaps(nurse)) {
			if (Gdx.input.isKeyPressed(Input.Keys.C)) {
				screen = new NurseState(aw, player.getX(), player.getY());
				aw.setScreen(screen);
			}
		}
		
		// Detection Monster in map
//		if (actor.overlaps(monsterSpawn)) {
//			screen = new BattleState(aw, "VillageState", player.getX(), player.getY());
//			aw.setScreen(screen);
//		}
		
		mapRender.setView(camera);
		mapRender.render();
		sb.begin();
		sb.draw(player.getSprite(), player.getWorldX() * Settings.SCALED_TILE_SIZE,
				player.getWorldY() * Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE,
				Settings.SCALED_TILE_SIZE * 1.5f);
		sb.end();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {
		
	}

}
