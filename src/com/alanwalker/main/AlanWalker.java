package com.alanwalker.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.alanwalker.state.AbstractState;
import com.alanwalker.state.MenuState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AlanWalker extends Game{

	private AbstractState screen;
	private AssetManager assetManager;
	private float oldX, oldY;
	
	// Status Player
	private String level = "1";
	private String attack = Integer.toString((int) (Math.random() * 10));
	private String playerHP = "100";
	
	private Properties prop = new Properties();
	private OutputStream output = null;
	private InputStream inputSave = null;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.load("resource/character/alan/alan.atlas", TextureAtlas.class);
		assetManager.finishLoading();
		
		// New Game or Load File Save
		try {
			inputSave = new FileInputStream("saves/save.properties");
			prop.load(inputSave);
			oldX = Integer.parseInt(prop.getProperty("startX"));
			oldY = Integer.parseInt(prop.getProperty("startY"));
		} catch (FileNotFoundException e) {
			try {
				output = new FileOutputStream("saves/save.properties");
				// set the properties value
				prop.setProperty("hp", playerHP);
				prop.setProperty("level", level);
				prop.setProperty("attack", attack);
				prop.setProperty("startX", "10");
				prop.setProperty("startY", "0");
				// save properties to project root folder
				prop.store(output, null);
			} catch (IOException io) {
				io.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		screen = new MenuState(this);
		this.setScreen(screen);
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public float getOldX() {
		return oldX;
	}

	public void setOldX(float oldX) {
		this.oldX = oldX;
	}

	public float getOldY() {
		return oldY;
	}

	public void setOldY(float oldY) {
		this.oldY = oldY;
	}
	
}
