package com.alanwalker.state;

import com.alanwalker.main.AlanWalker;
import com.alanwalker.main.Settings;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
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

public class MenuState extends AbstractState{
	
	private AbstractState screen;
	private SpriteBatch sb;
	private Texture bg;
	private Skin skin;
	private Stage stage;
	private LoadSave load;
	
	public MenuState(AlanWalker aw) {
		super(aw);

		sb = new SpriteBatch();
		
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
//		TextButton.TextButtonStyle continueButtonStyle = new TextButton.TextButtonStyle();
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
		
		bg = new Texture(Gdx.files.internal("resource/backgrounds/menubg-2.gif"));

		stage = new Stage();
        Gdx.input.setInputProcessor(stage);// Make the stage consume events

        TextButton newGameButton = new TextButton("", newGameButtonStyle); // Use the initialized skin
        newGameButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/6 , Gdx.graphics.getHeight()/2);
        newGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				screen = new StoryState(aw);
				screen = new CaveState(aw, 8, 10);
				aw.setScreen(screen);
			}
		});
        
        TextButton tutorialButton = new TextButton("", tutorialButtonStyle); // Use the initialized skin
        tutorialButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/6 , Gdx.graphics.getHeight()/2.6f);
        
        TextButton exitButton = new TextButton("", exitButtonStyle); // Use the initialized skin
        exitButton.setPosition(Gdx.graphics.getWidth()/2 - Gdx.graphics.getWidth()/6 , Gdx.graphics.getHeight()/3.7f);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.exit(0);
			}
		});
		
		
		// Add Button in Stage
        stage.addActor(newGameButton);
        stage.addActor(tutorialButton);
        stage.addActor(exitButton);
	}

	@Override
	public void dispose() {
		
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
		// New Game or Load File Save
		load = new LoadSave();
	}

}
