package com.alanwalker.state;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.alanwalker.entities.Actor;
import com.alanwalker.entities.Monster;
import com.alanwalker.main.AlanWalker;
import com.alanwalker.main.Settings;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleState extends AbstractState {

	private AbstractState screen;
	private Monster monster;
	private Actor player;
	private SpriteBatch sb;
	private Label monsterHpLabel, playerHpLabel, playerTurnLabel, playerWinLabel;
	private Label.LabelStyle monsterHPStyle, playerTurnStyle, playerWinStyle;
	private Texture alanHud, alanCharacter, bgStage, monsterHud, alanHudTop;
	private boolean youTurn = true, monTurn;
	private float delayTime = 0, delayTimeAttack = 0, delayYouWin = 0;
	private Sound sound;

	// Button in State
	private TextButton attackBtn;
	private TextButton runBtn;

	// Player Status
	private int playerHp;
	private int playerLevel;
	private int playerExp;
	private int playerAttack;
	private float positionPlayerX, positionPlayerY;
	private LoadSave loadPlayer;

	// Monster Status
	private int monsterHp;
	private int monsterAttack;
	private int monsterExp;
	private int countMonster;
	private String monsterName;

	// HUD
	private Skin skin;
	private Stage stage;
	private Label playerLevelLabel, playerExpLabel;
	private Label.LabelStyle playerHPStyle, playerLevelStyle, playerExpStyle;
	private boolean mapCheck = false;

	public BattleState(AlanWalker aw, String monster, float oldX, float oldY) {
		super(aw);
		this.monster = new Monster(monster);
		this.monsterName = monster;
		this.player = new Actor();
		sb = new SpriteBatch();

		// Load Sound
		sound = (Sound) Gdx.audio.newSound(Gdx.files.internal("resource/sounds/BattleState.mp3"));
		long id;
		id = sound.play();
		sound.setPan(id, 1f, 1f); // sets the pan of the sound to the left side at full volume
		sound.setLooping(id, true);

		// Load data player
		loadPlayer = new LoadSave();
		playerLevel = Integer.parseInt(loadPlayer.getLevel());
		playerHp = Integer.parseInt(loadPlayer.getPlayerHP());
		playerExp = Integer.parseInt(loadPlayer.getExp());
		playerAttack = Integer.parseInt(loadPlayer.getAttack());
		positionPlayerX = oldX;
		positionPlayerY = oldY;

		// Load Background Stage
		bgStage = new Texture(Gdx.files.internal("resource/backgrounds/battlestage.png"));

		// Load Alan Character
		alanCharacter = new Texture(Gdx.files.internal("resource/character/alan/alan-battle.png"));

		// Load Alan HUD
		alanHud = new Texture(Gdx.files.internal("resource/hud/hud-alan-battlestage.png"));

		// Load Monster HUD
		monsterHud = new Texture(Gdx.files.internal("resource/hud/hud-monster-battlestage.png"));

		// Load Button TextureAtlas
		TextureAtlas startButtonAtlas = new TextureAtlas(Gdx.files.internal("resource/ui/button/button.atlas"));

		// Create a font
		BitmapFont font = new BitmapFont();

		// Load Button UI
		skin = new Skin(startButtonAtlas);
		skin.add("default", font);

		// Create a button style
		TextButton.TextButtonStyle attackBtnStyle = new TextButton.TextButtonStyle();
		attackBtnStyle.up = skin.newDrawable("attack-active");
		attackBtnStyle.over = skin.newDrawable("attack-over");
		attackBtnStyle.down = skin.newDrawable("attack-clicked");
		attackBtnStyle.disabled = skin.newDrawable("attack-disable");
		attackBtnStyle.font = skin.getFont("default");
		TextButton.TextButtonStyle runBtnStyle = new TextButton.TextButtonStyle();
		runBtnStyle.up = skin.newDrawable("run-active");
		runBtnStyle.over = skin.newDrawable("run-over");
		runBtnStyle.down = skin.newDrawable("run-clicked");
		runBtnStyle.disabled = skin.newDrawable("run-disable");
		runBtnStyle.font = skin.getFont("default");

		// Create a label style
		monsterHPStyle = new Label.LabelStyle();
		monsterHPStyle.font = skin.getFont("default");
		playerTurnStyle = new Label.LabelStyle();
		playerTurnStyle.font = skin.getFont("default");
		playerHPStyle = new Label.LabelStyle();
		playerHPStyle.font = skin.getFont("default");
		playerLevelStyle = new Label.LabelStyle();
		playerLevelStyle.font = skin.getFont("default");
		playerExpStyle = new Label.LabelStyle();
		playerExpStyle.font = skin.getFont("default");
		playerWinStyle = new Label.LabelStyle();
		playerWinStyle.font = skin.getFont("default");

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		// Show Player Win
		playerWinLabel = new Label("", playerWinStyle);
		playerWinLabel.setBounds(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 2, 100, 100);
		playerWinLabel.setColor(Color.WHITE);
		playerWinLabel.setFontScale(2f, 2f);

		attackBtn = new TextButton("", attackBtnStyle); // Use the initialized skin
		attackBtn.setPosition(Gdx.graphics.getWidth() / 45, Gdx.graphics.getHeight() / 7);
		attackBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				delayTimeAttack = 0;
				monsterHp -= playerAttack;
				try {
					loadPlayer.getProp().setProperty("attack", String.valueOf(playerAttack));
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (monsterHp <= 0) {
					delayYouWin = 0;
					playerExp += monsterExp;

					try {
						loadPlayer.getProp().setProperty("exp", Integer.toString(playerExp));
						loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					// Kill BossMap Complete
					if (monster == "JungleBoss") {
						try {
							loadPlayer.getProp().setProperty("Quest1", "end");
							loadPlayer.getProp().setProperty("exp", Integer.toString(playerExp));
							loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (monster == "CaveBoss") {
						try {
							loadPlayer.getProp().setProperty("Quest2", "end");
							loadPlayer.getProp().setProperty("exp", Integer.toString(playerExp));
							loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (monster == "BossMapState") {
						try {
							loadPlayer.getProp().setProperty("newgame", "end");
							loadPlayer.getProp().setProperty("Boss", "end");
							loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// Kill BossMap Complete (END)

					// Save Amount Monster Kill in Quest
					if (!(loadPlayer.getProp().getProperty("Quest1CountMonster").equals("5"))
							&& loadPlayer.getProp().getProperty("Quest1").equals("start")) {
						countMonster++;
						try {
							loadPlayer.getProp().setProperty("exp", Integer.toString(playerExp));
							loadPlayer.getProp().setProperty("Quest1CountMonster", Integer.toString(countMonster));
							loadPlayer.getProp().setProperty("startX", String.valueOf(positionPlayerX));
							loadPlayer.getProp().setProperty("startY", String.valueOf(positionPlayerY));
							loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (!(loadPlayer.getProp().getProperty("Quest2CountMonster").equals("5"))
							&& loadPlayer.getProp().getProperty("Quest2").equals("start")) {
						countMonster++;
						try {
							loadPlayer.getProp().setProperty("exp", Integer.toString(playerExp));
							loadPlayer.getProp().setProperty("Quest2CountMonster", Integer.toString(countMonster));
							loadPlayer.getProp().setProperty("startX", String.valueOf(positionPlayerX));
							loadPlayer.getProp().setProperty("startY", String.valueOf(positionPlayerY));
							loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// Save Amount Monster Kill in Quest (END)

				}
				youTurn = false;
				monTurn = true;
			}
		});

		runBtn = new TextButton("", runBtnStyle); // Use the initialized skin
		runBtn.setPosition(Gdx.graphics.getWidth() / 45, Gdx.graphics.getHeight() / 35);
		runBtn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				sound.stop();
				if (loadPlayer.getProp().getProperty("mapName").equals("JungleState")) {
					aw.setScreen(new JungleState(aw, positionPlayerX, positionPlayerY));
				} else if (loadPlayer.getProp().getProperty("mapName").equals("JungleToCaveState")) {
					aw.setScreen(new JungleToCaveState(aw, positionPlayerX, positionPlayerY));
				} else if (loadPlayer.getProp().getProperty("mapName").equals("CaveState")) {
					aw.setScreen(new CaveState(aw, positionPlayerX, positionPlayerY));
				} else if (loadPlayer.getProp().getProperty("mapName").equals("BossMapState")) {
					aw.setScreen(new BossMapState(aw, positionPlayerX, positionPlayerY));
				}
			}
		});

		// Hud Status
		playerLevelLabel = new Label("Level : " + playerLevel, playerLevelStyle);
		playerLevelLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
		playerLevelLabel.setColor(Color.WHITE);
		playerLevelLabel.setFontScale(1f, 1f);
		playerExpLabel = new Label("Exp : " + playerExp, playerExpStyle);
		playerExpLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		playerExpLabel.setColor(Color.WHITE);
		playerExpLabel.setFontScale(1f, 1f);

		// Load Alan Hud
		alanHudTop = new Texture(Gdx.files.internal("resource/hud/alan-hud.png"));

		// HP Status
		playerHpLabel = new Label("", playerHPStyle);
		playerHpLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		playerHpLabel.setColor(Color.WHITE);
		playerHpLabel.setFontScale(1f, 1f);
		playerLevelLabel.setBounds(Gdx.graphics.getWidth() / 5 - 25, Gdx.graphics.getHeight() / 2 + 170, 10, 10);
		playerExpLabel.setBounds(Gdx.graphics.getWidth() / 3 - 40, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		monsterHpLabel = new Label("", monsterHPStyle);
		monsterHpLabel.setBounds(Gdx.graphics.getWidth() / 2 + 225, Gdx.graphics.getHeight() / 2 + 20, 10, 10);
		monsterHpLabel.setColor(Color.WHITE);
		monsterHpLabel.setFontScale(1f, 1f);

		// Show Player Turn
		playerTurnLabel = new Label("", playerTurnStyle);
		playerTurnLabel.setBounds(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 2, 100, 100);
		playerTurnLabel.setColor(Color.WHITE);
		playerTurnLabel.setFontScale(2f, 2f);
		playerTurnLabel.setText("Your Turn !!");

		attackBtn.setTouchable(Touchable.disabled);
		attackBtn.setDisabled(true);
		runBtn.setTouchable(Touchable.disabled);
		runBtn.setDisabled(true);

		stage.addActor(attackBtn);
		stage.addActor(runBtn);
		stage.addActor(monsterHpLabel);
		stage.addActor(playerHpLabel);
		stage.addActor(playerLevelLabel);
		stage.addActor(playerExpLabel);
		stage.addActor(playerTurnLabel);
		stage.addActor(playerWinLabel);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		sb.dispose();
	}

	@Override
	public void hide() {
		// dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	public void update(float delta) {
		// Update Attack
		playerAttack = Integer.parseInt(loadPlayer.getAttack());

		monsterHpLabel.setText("HP : " + monsterHp);
		playerHpLabel.setText("HP : " + playerHp);
		monster.update();
		monsterAttack = monster.getMonsterAttack();
	}

	@Override
	public void render(float delta) {

		// Clear Screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Update All Text on Screen
		update(delta);

		// Delay Time Label and Delay Time to Attack
		delayTimeAttack += delta;
		delayYouWin += delta;

		if ((delayYouWin >= 0 && delayYouWin < 6) && monsterHp <= 0) {
			playerWinLabel.setText("Your Win.");
			playerWinLabel.setVisible(true);
			mapCheck = true;
		} else {
			playerWinLabel.setVisible(false);
		}
		
		if(mapCheck && delayYouWin > 1) {
			sound.stop();
			// Come Back In Present Map
			if (loadPlayer.getProp().getProperty("mapName").equals("JungleState")) {
				aw.setScreen(new JungleState(aw, positionPlayerX, positionPlayerY));
			} else if (loadPlayer.getProp().getProperty("mapName").equals("JungleToCaveState")) {
				aw.setScreen(new JungleToCaveState(aw, positionPlayerX, positionPlayerY));
			} else if (loadPlayer.getProp().getProperty("mapName").equals("CaveState")) {
				aw.setScreen(new CaveState(aw, positionPlayerX, positionPlayerY));
			} else if (loadPlayer.getProp().getProperty("mapName").equals("BossMapState")) {
				aw.setScreen(new BossMapState(aw, positionPlayerX, positionPlayerY));
			}
			// Come Back In Present Map (END)
		}

		System.out.println("DELAY YOU WIN : " + delayYouWin);

		if (delayTime > 3) {
			playerTurnLabel.setText("");

			attackBtn.setTouchable(Touchable.enabled);
			attackBtn.setDisabled(false);
			runBtn.setTouchable(Touchable.enabled);
			runBtn.setDisabled(false);

			delayTime = 0;
		} else {
			delayTime += delta;
		}

		// Check Player Turn
		if (youTurn == false) {
			attackBtn.setTouchable(Touchable.disabled);
			attackBtn.setDisabled(true);
		}

		// Draw All Object on Screen
		sb.begin();

		// Check Monster Turn and Show Label
		if (monTurn == true) {
			if (delayTimeAttack > 3) {
				delayTime = 0;
				if (monsterAttack == 0) {
					attackBtn.setTouchable(Touchable.enabled);
					youTurn = true;
					monTurn = false;
					playerTurnLabel.setText("Miss !!");
					Timer.schedule(new Task() {
						@Override
						public void run() {
							playerTurnLabel.setText("Your Turn !!");
						}
					}, 2);
				} else {
					playerHp -= monsterAttack;
					try {
						loadPlayer.getProp().setProperty("hp", Integer.toString(playerHp));
						loadPlayer.getProp().setProperty("attack", Integer.toString(playerAttack));
						loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					attackBtn.setTouchable(Touchable.enabled);
					youTurn = true;
					monTurn = false;
					playerTurnLabel.setText("Your Turn !!");
				}

				if (playerHp <= 0) {
					sound.stop();
					try {
						loadPlayer.getProp().setProperty("hp", "50");
						loadPlayer.getProp().setProperty("mapName", "VillageState");
						loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					youTurn = false;
					monTurn = false;
					playerTurnLabel.setText("Your Die.");
					aw.setScreen(new VillageState(aw, 12, 2));
				}
			}
		}
		sb.draw(bgStage, 0, 0, Settings.V_WIDTH, Settings.V_HEIGHT);
		sb.draw(alanHudTop, 0, 380, 300, 100);
		sb.draw(alanCharacter, 40, 130, 75, 137);
		sb.draw(monster.getMonster(), Gdx.graphics.getWidth() / 2 + 180, 130, 150, 150);
		sb.draw(alanHud, -80, 0, 320, 143);
		sb.draw(monsterHud, Gdx.graphics.getWidth() / 2 + 210, 250, 79, 35);
		sb.end();
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// Load data monster
		monsterHp = monster.getHpMonster();
		monsterAttack = monster.getMonsterAttack();
		monsterExp = monster.getMonsterExp();
		if (monsterName == "JungleState") {
			countMonster = Integer.parseInt(loadPlayer.getProp().getProperty("Quest1CountMonster"));
		} else if (monsterName == "CaveState") {
			countMonster = Integer.parseInt(loadPlayer.getProp().getProperty("Quest2CountMonster"));
		}
	}

}
