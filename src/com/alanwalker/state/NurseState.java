package com.alanwalker.state;

import java.io.FileOutputStream;
import java.io.IOException;
import com.alanwalker.main.AlanWalker;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NurseState extends AbstractState {

	private AbstractState screen;
	private SpriteBatch sb;
	private Skin skin;
	private Stage stage;
	private Label nurseLabel;
	private Label.LabelStyle nurseStyle;
	private TextButton yesButton, noButton;
	private Texture dialogueBox, nurse;

	// Player Status
	private int playerHp;
	private int playerLevel;
	private int playerExp;
	private int playerAttack;
	private float positionPlayerX, positionPlayerY;
	private LoadSave loadPlayer;

	public NurseState(AlanWalker aw, float oldX, float oldY) {
		super(aw);
		sb = new SpriteBatch();
		
		// Load data player
		loadPlayer = new LoadSave();
		playerLevel = Integer.parseInt(loadPlayer.getLevel());
		playerHp = Integer.parseInt(loadPlayer.getPlayerHP());
		playerExp = Integer.parseInt(loadPlayer.getExp());
		playerAttack = Integer.parseInt(loadPlayer.getAttack());
		positionPlayerX = oldX;
		positionPlayerY = oldY;

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
					loadPlayer.getProp().setProperty("hp", Integer.toString(playerHp));
					loadPlayer.getProp().setProperty("startX", String.valueOf(positionPlayerX));
					loadPlayer.getProp().setProperty("startY", String.valueOf(positionPlayerY));
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
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
				try {
					loadPlayer.getProp().setProperty("startX", String.valueOf(positionPlayerX));
					loadPlayer.getProp().setProperty("startY", String.valueOf(positionPlayerY));
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	}

}
