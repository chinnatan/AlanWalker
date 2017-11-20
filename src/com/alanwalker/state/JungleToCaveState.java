package com.alanwalker.state;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.alanwalker.entities.Actor;
import com.alanwalker.entities.DIRECTION;
import com.alanwalker.main.AlanWalker;
import com.alanwalker.main.Settings;
import com.alanwalker.util.AnimationSet;
import com.alanwalker.util.LoadSave;
import com.alanwalker.util.PlayerControl;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class JungleToCaveState extends AbstractState {

	private AbstractState screen;
	private Actor player;
	private PlayerControl playerControll;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRender;
	private OrthographicCamera camera;
	private SpriteBatch sb;
	private AnimationSet animationAlan;
	private Texture alanHud;
	private Sound sound;

	protected Rectangle monsterSpawn1, monsterSpawn2, actor, npcNurse;
	private double positionMonster1X;
	private double positionMonster1Y;
	private double positionMonster2X;
	private double positionMonster2Y;

	// Move Map
	private Rectangle toJungle;
	private Rectangle toCave;

	// Status Player
	private String level;
	private String exp;
	private String attack;
	private String playerHP;
	private float positionPlayerX, positionPlayerY;
	private LoadSave loadPlayer;

	// HUD
	private Skin skin, skinQuest;
	private Stage stage, stageQuest;
	private Label playerHPLabel, playerLevelLabel, playerExpLabel;
	private Label.LabelStyle playerHPStyle, playerLevelStyle, playerExpStyle;
	private Label nurseLabel;
	private Label.LabelStyle nurseStyle;
	private TextButton yesButton, noButton;
	private Texture dialogueBox;
	private boolean npcCheck = false;

	public JungleToCaveState(AlanWalker aw, float positionX, float positionY) {
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

		// Load Sound
		sound = (Sound) Gdx.audio.newSound(Gdx.files.internal("resource/sounds/Village-Jungle.mp3"));
		long id;
		id = sound.play();
		sound.setPan(id, 1f, 1f); // sets the pan of the sound to the left side at full volume
		sound.setLooping(id, true);

		// Load Dialoguebox UI
		dialogueBox = new Texture(Gdx.files.internal("resource/ui/dialoguebox/dialoguebox.png"));

		// Load Button TextureAtlas
		TextureAtlas startButtonAtlas = new TextureAtlas(Gdx.files.internal("resource/ui/button/button.atlas"));

		// Create a font
		BitmapFont font = new BitmapFont();
		BitmapFont fontQuest = new BitmapFont(Gdx.files.internal("resource/fonts/Kanit-Regular-18.fnt"));

		// Load Button UI
		skin = new Skin();
		skin.add("default", font);
		skinQuest = new Skin(startButtonAtlas);
		skinQuest.add("default", fontQuest);

		// Create a label style
		playerHPStyle = new Label.LabelStyle();
		playerHPStyle.font = skin.getFont("default");
		playerLevelStyle = new Label.LabelStyle();
		playerLevelStyle.font = skin.getFont("default");
		playerExpStyle = new Label.LabelStyle();
		playerExpStyle.font = skin.getFont("default");
		nurseStyle = new Label.LabelStyle();
		nurseStyle.font = skinQuest.getFont("default");

		// Create a button style
		TextButton.TextButtonStyle yesButtonStyle = new TextButton.TextButtonStyle();
		yesButtonStyle.up = skinQuest.newDrawable("yes-active");
		yesButtonStyle.over = skinQuest.newDrawable("yes-over");
		yesButtonStyle.down = skinQuest.newDrawable("yes-clicked");
		yesButtonStyle.font = skinQuest.getFont("default");

		TextButton.TextButtonStyle noButtonStyle = new TextButton.TextButtonStyle();
		noButtonStyle.up = skinQuest.newDrawable("no-active");
		noButtonStyle.over = skinQuest.newDrawable("no-over");
		noButtonStyle.down = skinQuest.newDrawable("no-clicked");
		noButtonStyle.font = skinQuest.getFont("default");

		stage = new Stage();

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

		yesButton = new TextButton("", yesButtonStyle); // Use the initialized skin
		yesButton.setPosition(Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 16);
		yesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				playerHP = Integer.toString(100);
				try {
					loadPlayer.getProp().setProperty("hp", playerHP);
					loadPlayer.getProp().setProperty("startX", String.valueOf(positionPlayerX));
					loadPlayer.getProp().setProperty("startY", String.valueOf(positionPlayerY));
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sound.stop();
				aw.setScreen(new JungleToCaveState(aw, player.getX(), player.getY()));
			}
		});

		noButton = new TextButton("", noButtonStyle); // Use the initialized skin
		noButton.setPosition(Gdx.graphics.getWidth() / 2 + 70, Gdx.graphics.getHeight() / 16);
		noButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					loadPlayer.getProp().setProperty("startX", String.valueOf(positionPlayerX));
					loadPlayer.getProp().setProperty("startY", String.valueOf(positionPlayerY));
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sound.stop();
				aw.setScreen(new JungleToCaveState(aw, player.getX(), player.getY()));
			}
		});

		// HUD Nurse
		nurseLabel = new Label("คุณต้องการเพิ่ม hp หรือไม่", nurseStyle);
		nurseLabel.setBounds(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 5, 10, 10);
		nurseLabel.setColor(Color.WHITE);
		nurseLabel.setFontScale(1f, 1f);

		stage.addActor(playerHPLabel);
		stage.addActor(playerLevelLabel);
		stage.addActor(playerExpLabel);
		stage.addActor(yesButton);
		stage.addActor(noButton);
		stage.addActor(nurseLabel);

		yesButton.setVisible(false);
		noButton.setVisible(false);
		nurseLabel.setVisible(false);

		positionMonster1X = Math.random() * 8 + 8;
		positionMonster1Y = Math.random() * 4 + 9;
		positionMonster2X = Math.random() * 8 + 8;
		positionMonster2Y = Math.random() * 2 + 6;

		// Load Alan Hud
		alanHud = new Texture(Gdx.files.internal("resource/hud/alan-hud.png"));

		// Load Alan Character
		TextureAtlas alanAtlas = aw.getAssetManager().get("resource/character/alan/alan.atlas", TextureAtlas.class);
		animationAlan = new AnimationSet(
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_stand_north"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_north"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_south"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_east"), PlayMode.LOOP_PINGPONG),
				new Animation(0.3f / 2f, alanAtlas.findRegions("alan_walk_west"), PlayMode.LOOP_PINGPONG),
				alanAtlas.findRegion("alan_stand_north"), alanAtlas.findRegion("alan_stand_south"),
				alanAtlas.findRegion("alan_stand_east"), alanAtlas.findRegion("alan_stand_west"));

		// Load Map Village
		map = new TmxMapLoader().load("resource/maps/jungle/jungletocave.tmx");

		// Render Map Village
		mapRender = new OrthogonalTiledMapRenderer(map);

		// Load Camera
		camera = new OrthographicCamera();

		// Load Player
		player = new Actor(positionPlayerX, positionPlayerY, animationAlan, "JungleToCaveState");

		// Load Player Controll
		playerControll = new PlayerControl(player);

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
		playerLevelLabel.setText("Level : " + level);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		npcNurse = new Rectangle(7.5f, 8, 1, 1);
		toJungle = new Rectangle(19, 9, 0.5f, 0.5f);
		toCave = new Rectangle(1, 10, 0.5f, 05.f);
		monsterSpawn1 = new Rectangle(8, 9.5f, 8, 4);
		monsterSpawn2 = new Rectangle(8, 6.5f, 8, 2);

		actor = new Rectangle(player.getX(), player.getY(), 1, 1);
		playerControll.update(delta);
		player.update(delta);
		camera.position.set(player.getWorldX() * Settings.SCALED_TILE_SIZE + Gdx.graphics.getWidth() / 2,
				player.getWorldY() * Settings.SCALED_TILE_SIZE + Gdx.graphics.getHeight() / 2, 0);
		if (camera.position.x > Settings.V_WIDTH) {
			camera.position.x = Settings.V_WIDTH;
		} else if (camera.position.x < Settings.V_WIDTH / 2) {
			camera.position.x = Settings.V_WIDTH / 2;
		}

		camera.update();

		// Press "Space" to talk NPC in nearby
		if (actor.overlaps(npcNurse)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				npcCheck = true;
				Gdx.input.setInputProcessor(stage);
				yesButton.setVisible(true);
				noButton.setVisible(true);
				nurseLabel.setVisible(true);
			}
		}

		// Move Map
		if (actor.overlaps(toJungle)) { // -- to Village Map -- //
			try {
				loadPlayer.getProp().setProperty("mapName", "JungleState");
				loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sound.stop();
			screen = new JungleState(aw, 0.5f, 9);
			aw.setScreen(screen);
		} else if (actor.overlaps(toCave)) {
			try {
				loadPlayer.getProp().setProperty("mapName", "CaveState");
				loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sound.stop();
			screen = new CaveState(aw, 0.5f, 12.5f);
			aw.setScreen(screen);
		}

		// Detection Monster in map
		if (actor.overlaps(monsterSpawn1)) {
			if ((int) positionMonster1X == player.getX() && (int) positionMonster1Y == player.getY()) {
				sound.stop();
				screen = new BattleState(aw, "JungleToCaveState", player.getX(), player.getY());
				aw.setScreen(screen);
			}
		} else if (actor.overlaps(monsterSpawn2)) {
			if ((int) positionMonster2X == player.getX() && (int) positionMonster2Y == player.getY()) {
				sound.stop();
				screen = new BattleState(aw, "JungleToCaveState", player.getX(), player.getY());
				aw.setScreen(screen);
			}
		}

		mapRender.setView(camera);
		mapRender.render();
		sb.begin();
		sb.draw(player.getSprite(), player.getWorldX() * Settings.SCALED_TILE_SIZE,
				player.getWorldY() * Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE,
				Settings.SCALED_TILE_SIZE * 1.5f);

		// Show Chat Box
		if (npcCheck) {
			sb.draw(dialogueBox, Gdx.graphics.getWidth() / 55, 0);
			player.initMove(DIRECTION.STAND);
		}

		if ((player.getX() >= 0 && player.getX() <= 9) && (player.getY() >= 11 && player.getY() <= 14)) {
			sb.draw(alanHud, 340, 380, 300, 100);
			playerHPLabel.setBounds(Gdx.graphics.getWidth() / 2 + 125, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
			playerLevelLabel.setBounds(Gdx.graphics.getWidth() / 2 + 125, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
			playerExpLabel.setBounds(Gdx.graphics.getWidth() / 2 + 195, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		} else {
			sb.draw(alanHud, 0, 380, 300, 100);
			playerHPLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
			playerLevelLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
			playerExpLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		}
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
