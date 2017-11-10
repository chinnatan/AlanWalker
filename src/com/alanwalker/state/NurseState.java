package com.alanwalker.state;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class NurseState extends AbstractState {

	private AbstractState screen;
	private float oldX, oldY;
	private Monster monster;
	private Actor player;
	private SpriteBatch sb;
	private Skin skin;
	private Stage stage;
	private Label nurseLabel;
	private Label.LabelStyle nurseStyle, playerHPStyle, playerTurnStyle;
	private TextButton yesButton, noButton;
	private Texture dialogueBox, nurse;
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

	public NurseState(AlanWalker aw, float oldX, float oldY) {
		super(aw);
		this.oldX = oldX;
		this.oldY = oldY;
		sb = new SpriteBatch();

		// Load Dialoguebox UI
		dialogueBox = new Texture(Gdx.files.internal("resource/ui/dialoguebox/dialoguebox.png"));

		// Load Nurse
		nurse = new Texture(Gdx.files.internal("resource/character/joy-texture.gif"));

		// Load Button TextureAtlas
		TextureAtlas startButtonAtlas = new TextureAtlas(Gdx.files.internal("resource/ui/button/button.atlas"));

		// Create a font
		BitmapFont font = new BitmapFont(Gdx.files.internal("resource/fonts/Kanit-Regular-18.fnt"));

		// Load Button UI
		skin = new Skin(startButtonAtlas);
		skin.add("default", font);

		// Create a button style
		TextButton.TextButtonStyle yesButtonStyle = new TextButton.TextButtonStyle();
		yesButtonStyle.up = skin.newDrawable("yes-active");
		yesButtonStyle.over = skin.newDrawable("yes-over");
		yesButtonStyle.down = skin.newDrawable("yes-clicked");
		yesButtonStyle.font = skin.getFont("default");
		
		TextButton.TextButtonStyle noButtonStyle = new TextButton.TextButtonStyle();
		noButtonStyle.up = skin.newDrawable("no-active");
		noButtonStyle.over = skin.newDrawable("no-over");
		noButtonStyle.down = skin.newDrawable("no-clicked");
		noButtonStyle.font = skin.getFont("default");
		
		// Create a label style
		nurseStyle = new Label.LabelStyle();
		nurseStyle.font = skin.getFont("default");
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		yesButton = new TextButton("", yesButtonStyle); // Use the initialized skin
		yesButton.setPosition(Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 16);
		yesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				playerHp = 100;
				try {
					output = new FileOutputStream("saves/save.properties");
					prop.setProperty("hp", Integer.toString(playerHp));
					prop.setProperty("startX", Integer.toString((int) oldX));
					prop.setProperty("startY", Integer.toString((int) oldY));
					prop.store(output, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				aw.setScreen(new VillageState(aw));
			}
		});
		
		noButton = new TextButton("", noButtonStyle); // Use the initialized skin
		noButton.setPosition(Gdx.graphics.getWidth() / 2 + 70, Gdx.graphics.getHeight() / 16);
		noButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				aw.setScreen(new VillageState(aw));
			}
		});
		
		// Label
		nurseLabel = new Label("คุณต้องการเพิ่ม hp หรือไม่", nurseStyle);
		nurseLabel.setBounds(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 5, 10, 10);
		nurseLabel.setColor(Color.WHITE);
		nurseLabel.setFontScale(1f, 1f);

		stage.addActor(yesButton);
		stage.addActor(noButton);
		stage.addActor(nurseLabel);
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

	}

	@Override
	public void render(float delta) {

		// Clear Screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Draw All Object on Screen
		sb.begin();
		sb.draw(nurse, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 4, 240, 320);
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
		try {
			input = new FileInputStream("saves/save.properties");
			// load a properties file
			prop.load(input);
			playerHp = Integer.parseInt(prop.getProperty("hp"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
