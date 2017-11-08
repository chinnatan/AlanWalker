package com.alanwalker.main;

import com.alanwalker.state.AbstractState;
import com.alanwalker.state.MenuState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AlanWalker extends Game{

	// TEST
	private AbstractState screen;
	private AssetManager assetManager;
	private float oldX, oldY;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		assetManager.load("resource/character/alan/alan.atlas", TextureAtlas.class);
		assetManager.finishLoading();

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
