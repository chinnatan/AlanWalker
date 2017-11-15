package com.alanwalker.state;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.alanwalker.entities.Actor;
import com.alanwalker.main.AlanWalker;
import com.alanwalker.main.Settings;
import com.alanwalker.util.AnimationSet;
import com.alanwalker.util.LoadSave;
import com.alanwalker.util.PlayerControll;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class CaveState extends AbstractState{
	
	private AbstractState screen;
	private Actor player;
	private PlayerControll playerControll;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRender;
	private OrthographicCamera camera;
	private SpriteBatch sb;
	private AnimationSet animationAlan;
	private Texture alanHud;

	protected Rectangle monsterSpawn1, monsterSpawn2, actor, npcQuest;
	private double positionMonster1X;
	private double positionMonster1Y;
	private double positionMonster2X;
	private double positionMonster2Y;
	
	// Move Map
	private Rectangle toBossMap;
	private Rectangle toJungleToCave;

	// Status Player
	private String level;
	private String exp;
	private String attack;
	private String playerHP;
	private float positionPlayerX, positionPlayerY;
	private LoadSave loadPlayer;
	
	// HUD
	private Skin skin;
	private Stage stage;
	private Label playerHPLabel, playerLevelLabel, playerExpLabel;
	private Label.LabelStyle playerHPStyle, playerLevelStyle, playerExpStyle;
	private Label countQuestLabel;
	private Label.LabelStyle countQuestStyle;
	
	public CaveState(AlanWalker aw, float positionX, float positionY) {
		super(aw);
		sb = new SpriteBatch();

		// Load data player
		loadPlayer = new LoadSave();
		level = loadPlayer.getLevel();
		exp = loadPlayer.getExp();
		attack = loadPlayer.getAttack();
		playerHP = loadPlayer.getPlayerHP();
		positionPlayerX = positionX;
		positionPlayerY = positionY;
		
		// Create a font
		BitmapFont font = new BitmapFont();

		// Load Button UI
		skin = new Skin();
		skin.add("default", font);
		
		// Create a label style
		playerHPStyle = new Label.LabelStyle();
		playerHPStyle.font = skin.getFont("default");
		playerLevelStyle = new Label.LabelStyle();
		playerLevelStyle.font = skin.getFont("default");
		playerExpStyle = new Label.LabelStyle();
		playerExpStyle.font = skin.getFont("default");
		countQuestStyle = new Label.LabelStyle();
		countQuestStyle.font = skin.getFont("default");

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events
		
		// Hud Status
		playerHPLabel = new Label("HP : " + playerHP, playerHPStyle);
		playerHPLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		playerHPLabel.setColor(Color.WHITE);
		playerHPLabel.setFontScale(1f, 1f);
		playerLevelLabel = new Label("Level : " + level, playerLevelStyle);
		playerLevelLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
		playerLevelLabel.setColor(Color.WHITE);
		playerLevelLabel.setFontScale(1f, 1f);
		playerExpLabel = new Label("Exp : " + exp, playerExpStyle);
		playerExpLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		playerExpLabel.setColor(Color.WHITE);
		playerExpLabel.setFontScale(1f, 1f);
		countQuestLabel = new Label("Quest 2 : " + loadPlayer.getProp().getProperty("Quest2CountMonster") + "/10", countQuestStyle);
		countQuestLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
		countQuestLabel.setColor(Color.WHITE);
		countQuestLabel.setFontScale(1f, 1f);
		
//		stage.addActor(playerHPLabel);
//		stage.addActor(playerLevelLabel);
//		stage.addActor(playerExpLabel);
//		stage.addActor(countQuestLabel);

		positionMonster1X = Math.random() * 8 + 8;
		positionMonster1Y = Math.random() * 4 + 9;
		positionMonster2X = Math.random() * 8 + 8;
		positionMonster2Y = Math.random() * 2 + 6;
		
		// Load Alan Hud
		alanHud = new Texture(Gdx.files.internal("resource/hud/alan-hud.png"));

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
		map = new TmxMapLoader().load("resource/maps/cave/cave.tmx");

		// Render Map Village
		mapRender = new OrthogonalTiledMapRenderer(map);

		// Load Camera
		camera = new OrthographicCamera();

		// Load Player
		player = new Actor(positionPlayerX, positionPlayerY, animationAlan, "CaveState");

		// Load Player Control
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	public void update(float delta) {
		countQuestLabel.setText("Quest 2 : " + loadPlayer.getProp().getProperty("Quest2CountMonster") + "/10");
		playerLevelLabel.setText("Level : " + level);
	}
	
	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		update(delta);
		
		if(loadPlayer.getProp().getProperty("Quest2").equals("start")) {
			countQuestLabel.setVisible(true);
		} else {
			countQuestLabel.setVisible(false);
		}
		
		npcQuest = new Rectangle(7.5f, 8, 1, 1);
		toBossMap = new Rectangle(7.5f, 14.5f, 1, 0.5f);
		toJungleToCave = new Rectangle(0.5f, 13.5f, 0.5f, 0.5f);
		monsterSpawn1 = new Rectangle(8, 9.5f, 8, 4);
		monsterSpawn2 = new Rectangle(8, 6.5f, 8, 2);

		actor = new Rectangle(player.getX(), player.getY(), 1, 1);
		playerControll.update(delta);
		player.update(delta);
		camera.position.set(player.getWorldX() * Settings.SCALED_TILE_SIZE + Gdx.graphics.getWidth() / 2,
				player.getWorldY() * Settings.SCALED_TILE_SIZE + Gdx.graphics.getHeight() / 2, 0);
		 if(camera.position.x > Settings.V_WIDTH) {
		 camera.position.x = Settings.V_WIDTH;
		 } else if(camera.position.x < Settings.V_WIDTH / 2) {
		 camera.position.x = Settings.V_WIDTH / 2;
		 }
//		if (camera.position.y > Settings.V_HEIGHT) {
//			camera.position.y = Settings.V_HEIGHT;
//		} else if (camera.position.y < Settings.V_HEIGHT / 4) {
//			camera.position.y = Settings.V_HEIGHT / 4;
//		}

		camera.update();
		
		// Press "C" to talk NPC in nearby
		if (actor.overlaps(npcQuest)) {
			if (Gdx.input.isKeyPressed(Input.Keys.C)) {
				screen = new QuestTalk2State(aw, player.getX(), player.getY());
				aw.setScreen(screen);
			}
		}
		
		// Move Map
		if (actor.overlaps(toBossMap)) { // -- to Boss Map -- //
			try {
				loadPlayer.getProp().setProperty("mapName", "JungleState");
				loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			screen = new JungleState(aw, 0.5f, 9);
			aw.setScreen(screen);
		} else if (actor.overlaps(toJungleToCave)) { // -- to Jungle To Cave Map -- //
			try {
				loadPlayer.getProp().setProperty("mapName", "JungleToCaveState");
				loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			screen = new JungleToCaveState(aw, 1, 9);
			aw.setScreen(screen);
		}
		
//		System.out.println("X 1 : " + positionMonster1X);
//		System.out.println("Y 1 : " + positionMonster1Y);
//		System.out.println("X 2 : " + positionMonster2X);
//		System.out.println("Y 2 : " + positionMonster2Y);
		
		// Detection Monster in map
//		if (actor.overlaps(monsterSpawn1)) {
//			if((int) positionMonster1X == player.getX() && (int) positionMonster1Y == player.getY()) {
//				screen = new BattleState(aw, "JungleToCaveState", player.getX(), player.getY());
//				aw.setScreen(screen);
//			}
//		} else if (actor.overlaps(monsterSpawn2)) {
//			if((int) positionMonster2X == player.getX() && (int) positionMonster2Y == player.getY()) {
//				screen = new BattleState(aw, "JungleToCaveState", player.getX(), player.getY());
//				aw.setScreen(screen);
//			}
//		}
		
		mapRender.setView(camera);
		mapRender.render();
		sb.begin();
		sb.draw(player.getSprite(), player.getWorldX() * Settings.SCALED_TILE_SIZE,
				player.getWorldY() * Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE,
				Settings.SCALED_TILE_SIZE * 1.5f);
//		sb.draw(alanHud, 0, 380, 300, 100);
		sb.end();
		stage.act();
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

}
