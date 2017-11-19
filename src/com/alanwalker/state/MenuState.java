package com.alanwalker.state;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.alanwalker.main.AlanWalker;
import com.alanwalker.main.Settings;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuState extends AbstractState {

	private AbstractState screen;
	private SpriteBatch sb;
	private Texture bg;
	private Skin skin;
	private Stage stage;
	private float positionPlayerX, positionPlayerY;
	private Sound sound;
	private LoadSave loadPlayer;

	// Button
	private TextButton continueButton;
	private TextButton newGameButton;

	public MenuState(AlanWalker aw) {
		super(aw);

		sb = new SpriteBatch();

		// Load Sound
		sound = (Sound) Gdx.audio.newSound(Gdx.files.internal("resource/sounds/Menu-Lost-Child-(instrument-IU).mp3"));
		long id;
		id = sound.play();
		sound.setPan(id, 1f, 1f); // sets the pan of the sound to the left side at full volume
		sound.setLooping(id, true);

		// New Game or Load File Save
		loadPlayer = new LoadSave();
		positionPlayerX = Float.valueOf(loadPlayer.getStartX());
		positionPlayerY = Float.valueOf(loadPlayer.getStartY());

		// Load Button TextureAtlas
		TextureAtlas startButtonAtlas = new TextureAtlas(Gdx.files.internal("resource/ui/button/button.atlas"));

		// Create a font
		BitmapFont font = new BitmapFont();

		// Load Button UI
		skin = new Skin(startButtonAtlas);
		skin.add("default", font);

		// Create a button style
		TextButton.TextButtonStyle newGameButtonStyle = new TextButton.TextButtonStyle();
		TextButton.TextButtonStyle tutorialButtonStyle = new TextButton.TextButtonStyle();
		TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle();
		TextButton.TextButtonStyle continueButtonStyle = new TextButton.TextButtonStyle();
		newGameButtonStyle.up = skin.newDrawable("new-game-active");
		newGameButtonStyle.over = skin.newDrawable("new-game-over");
		newGameButtonStyle.down = skin.newDrawable("new-game-clicked");
		newGameButtonStyle.font = skin.getFont("default");
		tutorialButtonStyle.up = skin.newDrawable("tutorial-active");
		tutorialButtonStyle.over = skin.newDrawable("tutorial-over");
		tutorialButtonStyle.down = skin.newDrawable("tutorial-clicked");
		tutorialButtonStyle.font = skin.getFont("default");
		exitButtonStyle.up = skin.newDrawable("exit-active");
		exitButtonStyle.over = skin.newDrawable("exit-over");
		exitButtonStyle.down = skin.newDrawable("exit-clicked");
		exitButtonStyle.font = skin.getFont("default");
		continueButtonStyle.up = skin.newDrawable("continue-active");
		continueButtonStyle.over = skin.newDrawable("continue-over");
		continueButtonStyle.down = skin.newDrawable("continue-clicked");
		continueButtonStyle.font = skin.getFont("default");

		bg = new Texture(Gdx.files.internal("resource/backgrounds/menubg-2.gif"));

		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		newGameButton = new TextButton("", newGameButtonStyle); // Use the initialized skin
		newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 6,
				Gdx.graphics.getHeight() / 2);
		newGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					loadPlayer.getProp().setProperty("newgame", "new");
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sound.stop();
				screen = new StoryState(aw);
				aw.setScreen(screen);
			}
		});

		continueButton = new TextButton("", continueButtonStyle); // Use the initialized skin
		continueButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 6,
				Gdx.graphics.getHeight() / 2);
		continueButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (loadPlayer.getProp().getProperty("mapName").equals("VillageState")) {
					aw.setScreen(new VillageState(aw, positionPlayerX, positionPlayerY));
				} else if (loadPlayer.getProp().getProperty("mapName").equals("JungleState")) {
					aw.setScreen(new JungleState(aw, positionPlayerX, positionPlayerY));
				} else if (loadPlayer.getProp().getProperty("mapName").equals("JungleToCaveState")) {
					aw.setScreen(new JungleToCaveState(aw, positionPlayerX, positionPlayerY));
				} else if (loadPlayer.getProp().getProperty("mapName").equals("CaveState")) {
					aw.setScreen(new CaveState(aw, positionPlayerX, positionPlayerY));
				} else if (loadPlayer.getProp().getProperty("mapName").equals("BossMapState")) {
					aw.setScreen(new BossMapState(aw, positionPlayerX, positionPlayerY));
				}
				sound.stop();
			}
		});

		TextButton tutorialButton = new TextButton("", tutorialButtonStyle); // Use the initialized skin
		tutorialButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 6,
				Gdx.graphics.getHeight() / 2.6f);

		TextButton exitButton = new TextButton("", exitButtonStyle); // Use the initialized skin
		exitButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 6,
				Gdx.graphics.getHeight() / 3.7f);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.exit(0);
			}
		});

		// Add Button in Stage
		stage.addActor(newGameButton);
		stage.addActor(continueButton);
		stage.addActor(tutorialButton);
		stage.addActor(exitButton);
	}

	@Override
	public void dispose() {
		stage.dispose();
		sb.dispose();
		sound.dispose();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (loadPlayer.getProp().getProperty("newgame").equals("new")) {
			newGameButton.setVisible(false);
			continueButton.setVisible(true);
		} else {
			newGameButton.setVisible(true);
			continueButton.setVisible(false);
		}

		sb.begin();
		sb.draw(bg, 0, 0, Settings.V_WIDTH, Settings.V_HEIGHT);
		sb.end();

		// Show Button Stage
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

	}

	@Override
	public void show() {

	}

}
