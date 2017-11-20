package com.alanwalker.state;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

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

public class CaveState extends AbstractState {

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

	private Rectangle actor, npcQuest;

	// Monster System
	private Rectangle monsterSpawn1;
	private Rectangle monsterSpawn2;
	private Rectangle monsterSpawn3;
	private double positionMonster1X;
	private double positionMonster1Y;
	private double positionMonster2X;
	private double positionMonster2Y;
	private double positionMonster3X;
	private double positionMonster3Y;

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
	private Skin skin, skinQuest;
	private Stage stage;
	private Label playerHPLabel, playerLevelLabel, playerExpLabel;
	private Label.LabelStyle playerHPStyle, playerLevelStyle, playerExpStyle;
	private Label countQuestLabel;
	private Label.LabelStyle countQuestStyle;

	// Quest System
	private Texture dialogueBox;
	private Label npcQuestLabel;
	private Label.LabelStyle npcQuestStyle;
	private TextButton yesButton, noButton;
	private boolean npcCheck = false;

	public CaveState(AlanWalker aw, float positionX, float positionY) {
		super(aw);
		sb = new SpriteBatch();

		// Respawn Monster in Map
		Random rand = new Random();
		positionMonster1X = rand.nextInt(11) + 7;
		positionMonster1Y = rand.nextInt(3) + 9;
		positionMonster2X = rand.nextInt(11) + 7;
		positionMonster2Y = rand.nextInt(3) + 9;
		positionMonster3X = rand.nextInt(4) + 7;
		positionMonster3Y = rand.nextInt(2) + 11;

		// Load data player
		loadPlayer = new LoadSave();
		level = loadPlayer.getLevel();
		exp = loadPlayer.getExp();
		attack = loadPlayer.getAttack();
		playerHP = loadPlayer.getPlayerHP();
		positionPlayerX = positionX;
		positionPlayerY = positionY;

		// Load Sound
		sound = (Sound) Gdx.audio.newSound(Gdx.files.internal("resource/sounds/CaveSound.mp3"));
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
		countQuestStyle = new Label.LabelStyle();
		countQuestStyle.font = skin.getFont("default");
		npcQuestStyle = new Label.LabelStyle();
		npcQuestStyle.font = skinQuest.getFont("default");

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
		countQuestLabel = new Label("Quest 2 : " + loadPlayer.getProp().getProperty("Quest2CountMonster") + "/5",
				countQuestStyle);
		countQuestLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
		countQuestLabel.setColor(Color.WHITE);
		countQuestLabel.setFontScale(1f, 1f);

		// HUD Quest
		yesButton = new TextButton("", yesButtonStyle); // Use the initialized skin
		yesButton.setPosition(Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 16);
		yesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!(loadPlayer.getProp().getProperty("Quest1").equals("end"))) {
					aw.setScreen(new CaveState(aw, player.getX(), player.getY()));
				} else {
					if (loadPlayer.getProp().getProperty("Quest2").equals("start")) {
						aw.setScreen(new CaveState(aw, player.getX(), player.getY()));
					} else if (loadPlayer.getProp().getProperty("Quest2").equals("end")) {
						aw.setScreen(new CaveState(aw, player.getX(), player.getY()));
					} else {
						try {
							loadPlayer.getProp().setProperty("Quest2", "start");
							loadPlayer.getProp().setProperty("Quest2CountMonster", "0");
							loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
						} catch (IOException e) {
							e.printStackTrace();
						}
						aw.setScreen(new CaveState(aw, player.getX(), player.getY()));
					}
				}

				if (loadPlayer.getProp().getProperty("Quest2").equals("start")
						&& loadPlayer.getProp().getProperty("Quest2CountMonster").equals("5")) {
					aw.setScreen(new BattleState(aw, "CaveBoss", player.getX(), player.getY()));
				}
				sound.stop();
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
				aw.setScreen(new CaveState(aw, player.getX(), player.getY()));
			}
		});

		// Label
		npcQuestLabel = new Label(
				"กำจัด Corrupted Rock Spirit (อยู๋ด้านบนขวาของแผนที่)\nจำนวน 5 ตัวเพื่อสู้กับบอสของแมพ เจ้าจะทำหรือไม่ ?",
				npcQuestStyle);
		npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 12, Gdx.graphics.getHeight() / 4 - 14, 10, 10);
		npcQuestLabel.setColor(Color.WHITE);
		npcQuestLabel.setFontScale(1f, 1f);

		stage.addActor(yesButton);
		stage.addActor(noButton);
		stage.addActor(npcQuestLabel);
		stage.addActor(playerHPLabel);
		stage.addActor(playerLevelLabel);
		stage.addActor(playerExpLabel);
		stage.addActor(countQuestLabel);

		yesButton.setVisible(false);
		noButton.setVisible(false);
		npcQuestLabel.setVisible(false);

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
		if (loadPlayer.getProp().getProperty("Quest2").equals("start") || loadPlayer.getProp().getProperty("Quest2").equals("end")) {
			map = new TmxMapLoader().load("resource/maps/cave/cave.tmx");
		} else {
			map = new TmxMapLoader().load("resource/maps/cave/cave-quest.tmx");
		}

		// Render Map Village
		mapRender = new OrthogonalTiledMapRenderer(map);

		// Load Camera
		camera = new OrthographicCamera();

		// Load Player
		player = new Actor(positionPlayerX, positionPlayerY, animationAlan, "CaveState");

		// Load Player Control
		playerControll = new PlayerControl(player);

		// Input Movement
		Gdx.input.setInputProcessor(playerControll);
	}

	@Override
	public void dispose() {
		map.dispose();
		mapRender.dispose();
		sound.dispose();
		sb.dispose();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	public void update(float delta) {
		countQuestLabel.setText("Quest 2 : " + loadPlayer.getProp().getProperty("Quest2CountMonster") + "/5");
		playerLevelLabel.setText("Level : " + level);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		if (loadPlayer.getProp().getProperty("Quest2").equals("start")) {
			countQuestLabel.setVisible(true);
		} else {
			countQuestLabel.setVisible(false);
		}

		npcQuest = new Rectangle(1.5f, 7.5f, 1, 1);
		toBossMap = new Rectangle(7.5f, 14.5f, 1, 0.5f);
		toJungleToCave = new Rectangle(0.5f, 13.5f, 0.5f, 0.5f);
		monsterSpawn1 = new Rectangle(7, 9, 11, 5);
		monsterSpawn2 = new Rectangle(7, 9, 11, 5);
		monsterSpawn3 = new Rectangle(7, 12, 4.5f, 1);

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
		// if (camera.position.y > Settings.V_HEIGHT) {
		// camera.position.y = Settings.V_HEIGHT;
		// } else if (camera.position.y < Settings.V_HEIGHT / 4) {
		// camera.position.y = Settings.V_HEIGHT / 4;
		// }

		camera.update();

		// Press "Space" to talk NPC in nearby
		if (actor.overlaps(npcQuest)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				npcCheck = true;
				Gdx.input.setInputProcessor(stage);
				yesButton.setVisible(true);
				noButton.setVisible(true);
				npcQuestLabel.setVisible(true);
			}
		}

		// Check Quest
		if (loadPlayer.getProp().getProperty("Quest1").equals("end")) {
			if (loadPlayer.getProp().getProperty("Quest2").equals("start")) {
				if (loadPlayer.getProp().getProperty("Quest2CountMonster").equals("5")) {
					npcQuestLabel.setText("คุณต้องการส่งเควสและสู้กับบอสแมพใช่หรือไม่");
					npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, 10, 10);
					npcQuestLabel.setColor(Color.WHITE);
					npcQuestLabel.setFontScale(1f, 1f);
				} else {
					npcQuestLabel.setText("คุณยังกำจัด Spider ไม่ครบตามจำนวนที่กำหนด");
					npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, 10, 10);
					npcQuestLabel.setColor(Color.WHITE);
					npcQuestLabel.setFontScale(1f, 1f);
					yesButton.setPosition(Gdx.graphics.getWidth() / 4 + 50, Gdx.graphics.getHeight() / 16);
					noButton.setVisible(false);
				}
			} else if (loadPlayer.getProp().getProperty("Quest2").equals("end")) {
				npcQuestLabel.setText("คุณผ่านเควสนี้เรียบร้อยแล้ว...กรุณาทำเควสด่านถัดไป");
				npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, 10, 10);
				npcQuestLabel.setColor(Color.WHITE);
				npcQuestLabel.setFontScale(1f, 1f);
				yesButton.setPosition(Gdx.graphics.getWidth() / 4 + 50, Gdx.graphics.getHeight() / 16);
				noButton.setVisible(false);
			}
		} else {
			npcQuestLabel.setText("คุณยังไม่ผ่านเควส 1 กรุณาทำเควส 1 ให้เสร็จเรียบร้อย");
			npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, 10, 10);
			npcQuestLabel.setColor(Color.WHITE);
			npcQuestLabel.setFontScale(1f, 1f);
			yesButton.setPosition(Gdx.graphics.getWidth() / 4 + 50, Gdx.graphics.getHeight() / 16);
			noButton.setVisible(false);
		}

		// Move Map
		if (actor.overlaps(toBossMap)) { // -- to Boss Map -- //
			try {
				loadPlayer.getProp().setProperty("mapName", "BossMapState");
				loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sound.stop();
			screen = new BossMapState(aw, 5, 1.5f);
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
			sound.stop();
			screen = new JungleToCaveState(aw, 1, 9);
			aw.setScreen(screen);
		}

		// Detection Monster in map
		if (actor.overlaps(monsterSpawn1) || actor.overlaps(monsterSpawn2) || actor.overlaps(monsterSpawn3)) {
			if ((int) positionMonster1X == player.getX() && (int) positionMonster1Y == player.getY()) {
				screen = new BattleState(aw, "CaveState", player.getX(), player.getY());
				aw.setScreen(screen);
			}
		} else if (actor.overlaps(monsterSpawn1) || actor.overlaps(monsterSpawn2) || actor.overlaps(monsterSpawn3)) {
			if ((int) positionMonster2X == player.getX() && (int) positionMonster2Y == player.getY()) {
				sound.stop();
				screen = new BattleState(aw, "CaveState", player.getX(), player.getY());
				aw.setScreen(screen);
			}
		} else if (actor.overlaps(monsterSpawn1) || actor.overlaps(monsterSpawn2) || actor.overlaps(monsterSpawn3)) {
			if ((int) positionMonster3X == player.getX() && (int) positionMonster3Y == player.getY()) {
				sound.stop();
				screen = new BattleState(aw, "CaveState", player.getX(), player.getY());
				aw.setScreen(screen);
			}
		}

		mapRender.setView(camera);
		mapRender.render();
		sb.begin();

		// Show Chat Box
		if (npcCheck) {
			sb.draw(dialogueBox, Gdx.graphics.getWidth() / 55, 0);
			player.initMove(DIRECTION.STAND);
		}

		sb.draw(player.getSprite(), player.getWorldX() * Settings.SCALED_TILE_SIZE,
				player.getWorldY() * Settings.SCALED_TILE_SIZE, Settings.SCALED_TILE_SIZE,
				Settings.SCALED_TILE_SIZE * 1.5f);
		if ((player.getX() >= 0 && player.getX() <= 9) && (player.getY() >= 11 && player.getY() <= 14)) {
			sb.draw(alanHud, 340, 380, 300, 100);
			playerHPLabel.setBounds(Gdx.graphics.getWidth() / 2 + 125, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
			playerLevelLabel.setBounds(Gdx.graphics.getWidth() / 2 + 125, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
			playerExpLabel.setBounds(Gdx.graphics.getWidth() / 2 + 195, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
			countQuestLabel.setBounds(Gdx.graphics.getWidth() / 2 + 195, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
		} else {
			sb.draw(alanHud, 0, 380, 300, 100);
			playerHPLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
			playerLevelLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
			playerExpLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
			countQuestLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
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
