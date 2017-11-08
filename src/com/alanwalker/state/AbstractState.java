package com.alanwalker.state;

import com.alanwalker.main.AlanWalker;
import com.badlogic.gdx.Screen;

public abstract class AbstractState implements Screen {
	
	protected AlanWalker aw;
	
	public AbstractState() {
		
	}
	
	public AbstractState(AlanWalker aw) {
		this.aw = aw;
	}

	@Override
	public abstract void dispose();

	@Override
	public abstract void hide();

	@Override
	public abstract void pause();
	@Override
	public abstract void render(float delta);

	@Override
	public abstract void resize(int width, int height);

	@Override
	public abstract void resume();

	@Override
	public abstract void show();
}
