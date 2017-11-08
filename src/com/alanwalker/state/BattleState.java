package com.alanwalker.state;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Time;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.alanwalker.entities.Actor;
import com.alanwalker.entities.Monster;
import com.alanwalker.main.AlanWalker;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattleState extends AbstractState {

	private AbstractState screen;
	private int oldX, oldY;
	private Monster monster;
	private Actor player;
	private SpriteBatch sb;
	private Skin skin;
	private Stage stage;
	private Label monsterHpLabel, playerHpLabel, playerTurnLabel;
	private Label.LabelStyle monsterHPStyle, playerHPStyle, playerTurnStyle;
	private TextButton attackButton;
	private Texture dialogueBox;
	private boolean youTurn = true, monTurn;
	private int flagTurnShowLabel;
	private float delayTime = 0, delayTimeAttack = 0;
	
	private Properties prop = new Properties();
	private InputStream input = null;
	private OutputStream output = null;
	
	// Player Status
	private int playerHp;
	private int playerLevel;
	private int playerExp;
	private int playerAttack;
	
	// Monster Status
	private int monsterHp;
	private int monsterAttack;
	private int monsterExp;

	public BattleState(AlanWalker aw, String monster) {
		super(aw);
		this.monster = new Monster(monster);
		this.player = new Actor();
		sb = new SpriteBatch();
	
		try {

			input = new FileInputStream("saves/save.properties");

			// load a properties file
			prop.load(input);
			playerLevel = Integer.parseInt(prop.getProperty("level"));
			playerExp = Integer.parseInt(prop.getProperty("exp"));
			playerAttack = Integer.parseInt(prop.getProperty("attack"));

			// get the property value and print it out
			System.out.println(prop.getProperty("hp"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Load Dialoguebox UI
		dialogueBox = new Texture(Gdx.files.internal("resource/ui/dialoguebox/dialoguebox.png"));

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

		// Create a label style
		monsterHPStyle = new Label.LabelStyle();
		monsterHPStyle.font = skin.getFont("default");
		playerHPStyle = new Label.LabelStyle();
		playerHPStyle.font = skin.getFont("default");
		playerTurnStyle = new Label.LabelStyle();
		playerTurnStyle.font = skin.getFont("default");

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		attackButton = new TextButton("", attackButtonStyle); // Use the initialized skin
		attackButton.setPosition(Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 7);
		attackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				delayTimeAttack = 0;
				monsterHp -= playerAttack;
				if (monsterHp <= 0) {
					playerExp += monsterExp;
					try {
						output = new FileOutputStream("saves/save.properties");
						prop.setProperty("exp", Integer.toString(playerExp));
						prop.store(output, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					aw.setScreen(new VillageState(aw));
				}
				youTurn = false;
				monTurn = true;
			}
		});

		// HP Status
		monsterHpLabel = new Label("", monsterHPStyle);
		monsterHpLabel.setBounds(Gdx.graphics.getWidth() / 2 + 200, Gdx.graphics.getHeight() / 2 + 200, 10, 10);
		monsterHpLabel.setColor(Color.WHITE);
		monsterHpLabel.setFontScale(1f, 1f);
		playerHpLabel = new Label("", playerHPStyle);
		playerHpLabel.setBounds(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 3, 10, 10);
		playerHpLabel.setColor(Color.WHITE);
		playerHpLabel.setFontScale(1f, 1f);

		// Show Player Turn
		playerTurnLabel = new Label("", playerTurnStyle);
		playerTurnLabel.setBounds(Gdx.graphics.getWidth() / 2.5f, Gdx.graphics.getHeight() / 2, 100, 100);
		playerTurnLabel.setColor(Color.WHITE);
		playerTurnLabel.setFontScale(2f, 2f);
		playerTurnLabel.setText("Your Turn !!");
		attackButton.setTouchable(Touchable.disabled);
		attackButton.setDisabled(true);
		
		stage.addActor(attackButton);
		stage.addActor(monsterHpLabel);
		stage.addActor(playerHpLabel);
		stage.addActor(playerTurnLabel);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		sb.dispose();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	public void update(float delta) {
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
		
		// Delay Time Label and Delay Time to Attack
		delayTimeAttack += delta;

		if (delayTime > 3) {
			playerTurnLabel.setText("");
			attackButton.setTouchable(Touchable.enabled);
			attackButton.setDisabled(false);
			delayTime = 0;
		} else {
			delayTime += delta;
		}

		// Check Player Turn
		if (youTurn == false) {
			attackButton.setTouchable(Touchable.disabled);
			attackButton.setDisabled(true);
		}
		
		// Check Monster Turn and Show Label
		if (monTurn == true) {
			if(delayTimeAttack > 3) {
				delayTime = 0;
				if (monsterAttack == 0) {
					attackButton.setTouchable(Touchable.enabled);
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
					playerAttack = (int) (Math.random()*10) * Integer.parseInt(prop.getProperty("level"));
					playerHp -= monsterAttack;
					try {
						output = new FileOutputStream("saves/save.properties");
						prop.setProperty("hp", Integer.toString(playerHp));
						prop.setProperty("attack", Integer.toString(playerAttack));
						prop.store(output, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					attackButton.setTouchable(Touchable.enabled);
					youTurn = true;
					monTurn = false;
					playerTurnLabel.setText("Your Turn !!");
				}
			}
		}
		
		// Update All Text on Screen
		update(delta);

		// Draw All Object on Screen
		sb.begin();
		sb.draw(monster.getMonster(), Gdx.graphics.getWidth() / 2 + 100, Gdx.graphics.getHeight() / 2 + 100);
		sb.draw(dialogueBox, (Gdx.graphics.getWidth() / 60), 0, Gdx.graphics.getWidth() - 20,
				Gdx.graphics.getHeight() - 320);
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
		monsterHp = monster.getHpMonster();
		monsterAttack = monster.getMonsterAttack();
		monsterExp = monster.getMonsterExp();
		playerHp = Integer.parseInt(prop.getProperty("hp"));
	}

}
