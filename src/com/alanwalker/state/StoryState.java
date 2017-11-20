package com.alanwalker.state;

import java.io.FileOutputStream;
import java.io.IOException;

import com.alanwalker.main.AlanWalker;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StoryState extends AbstractState implements InputProcessor {
	
	private SpriteBatch sb;
	private int next = 0;
	private Texture[] storys;
	private String fileName;
	private Sound sound;
	
	public StoryState(AlanWalker aw) {
		super(aw);
		sb = new SpriteBatch();
		storys = new Texture[4];
		for(int i=0;i<4;i++) {
			fileName = String.format("resource/backgrounds/story/%1d.png", i+1);
			storys[i] = new Texture(Gdx.files.internal(fileName));
		}
		
		// Load Sound
		sound = (Sound) Gdx.audio.newSound(Gdx.files.internal("resource/sounds/StoryScreen.mp3"));
		long id;
		id = sound.play();
		sound.setPan(id, 1f, 1f); // sets the pan of the sound to the left side at full volume
		sound.setLooping(id, true);
	}

	@Override
	public void dispose() {
		sb.dispose();
		sound.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		sb.draw(storys[next], 0, 0);
		sb.end();
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
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.ENTER) {
			if(next != 3) {
				next++;
			} else {
				try {
					LoadSave loadPlayer = new LoadSave();
					loadPlayer.getProp().setProperty("mapName", "VillageState");
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sound.stop();
				aw.setScreen(new VillageState(aw, 12, 2));
			}
		} else if(keycode == Keys.ESCAPE) {
			try {
				LoadSave loadPlayer = new LoadSave();
				loadPlayer.getProp().setProperty("mapName", "VillageState");
				loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sound.stop();
			aw.setScreen(new VillageState(aw, 12, 2));
		}
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
