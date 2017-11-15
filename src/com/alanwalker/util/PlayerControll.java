package com.alanwalker.util;

import com.alanwalker.entities.Actor;
import com.alanwalker.entities.DIRECTION;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

public class PlayerControll implements InputProcessor{

	private Actor player;
	private boolean up, down, left, right;
	
	public PlayerControll(Actor player) {
		this.player = player;
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.W) {
			up = true;
		}
		if(keycode == Keys.S) {
			down = true;
		}
		if(keycode == Keys.A) {
			left = true;
		}
		if(keycode == Keys.D) {
			right = true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Keys.W) {
			up = false;
		}
		if(keycode == Keys.S) {
			down = false;
		}
		if(keycode == Keys.A) {
			left = false;
		}
		if(keycode == Keys.D) {
			right = false;
		}
		
		return false;
	}
	
	public void update(float delta) {
		if(up) {
			player.move(DIRECTION.NORTH);
			return;
		}
		if(down) {
			player.move(DIRECTION.SOUTH);
			return;
		}
		if(left) {
			player.move(DIRECTION.WEST);
			return;
		}
		if(right) {
			player.move(DIRECTION.EAST);
			return;
		}
	}

	@Override
	public boolean keyTyped(char arg0) {
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
