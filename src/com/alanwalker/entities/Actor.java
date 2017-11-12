package com.alanwalker.entities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import com.alanwalker.util.AnimationSet;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

public class Actor {

	private float x, y;
	private DIRECTION facing;
	private float worldX, worldY;
	private float srcX, srcY;
	private float destX, destY;
	private float animaTimer;
	private float ANIMA_TIME = 0.3f;
	private float walkTimer;
	private boolean moveRequestThisFrame;
	private ACTOR_STATE state;
	private AnimationSet animationPlayer;
	private String mapName;

	// Status Player
	private String level;
	private String exp;
	private String attack;
	private String playerHP;
	private int expMax = 10;
	private int levelMax;
	private LoadSave loadPlayer;
	
	public Actor() { }

	public Actor(float x, float y, AnimationSet animations, String mapName) {
		this.x = x;
		this.y = y;
		this.worldX = x;
		this.worldY = y;
		this.animationPlayer = animations;
		this.state = ACTOR_STATE.STANDING;
		this.facing = DIRECTION.SOUTH;
		this.mapName = mapName;

		// Load data player
		loadPlayer = new LoadSave();
		level = loadPlayer.getLevel();
		exp = loadPlayer.getExp();
		attack = loadPlayer.getAttack();
		playerHP = loadPlayer.getPlayerHP();
		levelMax = Integer.parseInt(loadPlayer.getLevel());
		
		// Up Level Player
		if (Integer.parseInt(loadPlayer.getExp()) >= expMax * Integer.parseInt(loadPlayer.getLevel())) {
			levelMax++;
			try {
				loadPlayer.getProp().setProperty("exp", "0");
				loadPlayer.getProp().setProperty("level", String.valueOf(levelMax));
				loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public enum ACTOR_STATE {
		WALKING, STANDING,;
	}

	public void update(float delta) {
		if (state == ACTOR_STATE.WALKING) {
			animaTimer += delta;
			walkTimer += delta;
			worldX = Interpolation.linear.apply(srcX, destX, animaTimer / ANIMA_TIME);
			worldY = Interpolation.linear.apply(srcY, destY, animaTimer / ANIMA_TIME);
			if (animaTimer > ANIMA_TIME) {
				float leftOverTime = animaTimer - ANIMA_TIME;
				walkTimer -= leftOverTime;
				finishMove();
				if (moveRequestThisFrame) {
					move(facing);
				} else {
					walkTimer = 0f;
				}
			}
		}

		moveRequestThisFrame = false;
	}

	public boolean move(DIRECTION dir) {
		// Show X, Y
		System.out.println("X : " + getX());
		System.out.println("Y : " + getY());

		if (state == ACTOR_STATE.WALKING) {
			if (facing == dir) {
				moveRequestThisFrame = true;
			}
			return false;
		}

		// Check Map
		if (mapName == "VillageState") {
			// Collision Lava
			if (x + dir.getDx() < 10 && y + dir.getDy() < 1) {
				return false;
			} else if ((x + dir.getDx() > 5.5f && x + dir.getDx() < 7.5f) && y + dir.getDy() == 1) {
				return false;
			} else if ((x + dir.getDx() > 5.5f && x + dir.getDx() < 7.5f)
					&& (y + dir.getDy() > 2 && y + dir.getDy() < 5.5f)) {
				return false;
			} else if ((x + dir.getDx() > 6 && x + dir.getDx() < 8) && (y + dir.getDy() > 5 && y + dir.getDy() < 6)) {
				return false;
			} else if ((x + dir.getDx() > 6 && x + dir.getDx() < 8)
					&& (y + dir.getDy() > 6.5f && y + dir.getDy() < 12.5f)) {
				return false;
			} else if ((x + dir.getDx() > 7 && x + dir.getDx() < 10)
					&& (y + dir.getDy() > 7.5f && y + dir.getDy() < 12.5f)) {
				return false;
			} else if ((x + dir.getDx() > 5.5f && x + dir.getDx() < 8)
					&& (y + dir.getDy() > 4 && y + dir.getDy() < 5.5f)) {
				return false;
			} else if ((x + dir.getDx() > 0 && x + dir.getDx() < 2) && (y + dir.getDy() > 0 && y + dir.getDy() < 13)) {
				return false;
			} else if ((x + dir.getDx() > 8.5f && x + dir.getDx() < 10.5f)
					&& (y + dir.getDy() > 4 && y + dir.getDy() < 5.5f)) {
				return false;
			} else if ((x + dir.getDx() > 9 && x + dir.getDx() < 10.5f)
					&& (y + dir.getDy() > 5 && y + dir.getDy() < 11.5f)) {
				return false;
			} else if ((x + dir.getDx() > 10 && x + dir.getDx() < 11) && (y + dir.getDy() > 6 && y + dir.getDy() < 7)) {
				return false;
			} else if ((x + dir.getDx() > 10 && x + dir.getDx() < 13.5f)
					&& (y + dir.getDy() > 9.5f && y + dir.getDy() < 11.5f)) {
				return false;
			} else if ((x + dir.getDx() > 12 && x + dir.getDx() < 13.5f)
					&& (y + dir.getDy() > 6 && y + dir.getDy() < 10)) {
				return false;
			} else if ((x + dir.getDx() > 11.5f && x + dir.getDx() < 13)
					&& (y + dir.getDy() > 6 && y + dir.getDy() < 7)) {
				return false;
			} else if ((x + dir.getDx() > 13 && x + dir.getDx() < 15.5f)
					&& (y + dir.getDy() > 6 && y + dir.getDy() < 7)) {
				return false;
			} else if ((x + dir.getDx() > 15.5f && x + dir.getDx() < 18)
					&& (y + dir.getDy() > 6 && y + dir.getDy() < 7)) {
				return false;
			} else if ((x + dir.getDx() > 17.5f) && (y + dir.getDy() > 6.5f && y + dir.getDy() < 13)) {
				return false;
			} else if (y + dir.getDy() > 12.5f) {
				return false;
			} else if ((x + dir.getDx() > 17.5f) && (y + dir.getDy() > 0.5f && y + dir.getDy() < 7)) {
				return false;
			} else if ((x + dir.getDx() > 11) && (y + dir.getDy() < 1)) {
				return false;
			}

			// Collision House
			if ((x + dir.getDx() > 14 && x + dir.getDx() < 16.5f) && (y + dir.getDy() > 1 && y + dir.getDy() < 4.5f)) {
				return false;
			} else if ((x + dir.getDx() > 14.5f && x + dir.getDx() < 16.5f) && (y + dir.getDy() > 8)) {
				return false;
			} else if ((x + dir.getDx() > 1 && x + dir.getDx() < 6) && (y + dir.getDy() > 7)) {
				return false;
			}
		} else if (mapName == "JungleState") {
			// Collision Tree
			if ((x + dir.getDx() > 0 && x + dir.getDx() < 6) && (y + dir.getDy() > 11.5f && y + dir.getDy() < 13.5f)) {
				return false;
			} else if ((x + dir.getDx() >= 0 && x + dir.getDx() < 1)
					&& (y + dir.getDy() > 9.5f && y + dir.getDy() < 13.5f)) {
				return false;
			} else if ((x + dir.getDx() > 1.5f && x + dir.getDx() < 6)
					&& (y + dir.getDy() > 7.5f && y + dir.getDy() < 10.5f)) {
				return false;
			} else if ((x + dir.getDx() >= 0 && x + dir.getDx() < 2.5f)
					&& (y + dir.getDy() > 5.5f && y + dir.getDy() < 8.5f)) {
				return false;
			} else if ((x + dir.getDx() >= 0 && x + dir.getDx() < 0.5f)
					&& (y + dir.getDy() >= 0 && y + dir.getDy() < 6)) {
				return false;
			} else if ((x + dir.getDx() >= 0.5f && x + dir.getDx() <= 11)
					&& (y + dir.getDy() >= 0 && y + dir.getDy() < 2)) {
				return false;
			} else if ((x + dir.getDx() > 11 && x + dir.getDx() < 14.5f)
					&& (y + dir.getDy() >= 0 && y + dir.getDy() <= 4)) {
				return false;
			} else if ((x + dir.getDx() > 4 && x + dir.getDx() <= 7)
					&& (y + dir.getDy() > 5 && y + dir.getDy() < 7.5f)) {
				return false;
			} else if ((x + dir.getDx() > 14 && x + dir.getDx() <= 16)
					&& (y + dir.getDy() > 11 && y + dir.getDy() < 13)) {
				return false;
			}

			// Collision Water
			if ((x + dir.getDx() > 6.5f && x + dir.getDx() <= 7.5f)
					&& (y + dir.getDy() >= 8 && y + dir.getDy() <= 12.5f)) {
				return false;
			} else if ((x + dir.getDx() >= 8 && x + dir.getDx() <= 19.5f)
					&& (y + dir.getDy() > 12.5f && y + dir.getDy() <= 13.5f)) {
				return false;
			} else if ((x + dir.getDx() > 6.5f && x + dir.getDx() <= 7.5f)
					&& (y + dir.getDy() > 3.5f && y + dir.getDy() < 7.5f)) {
				return false;
			} else if ((x + dir.getDx() > 6.5f && x + dir.getDx() <= 7.5f)
					&& (y + dir.getDy() >= 0 && y + dir.getDy() < 3.5f)) {
				return false;
			} else if ((x + dir.getDx() >= 8 && x + dir.getDx() <= 19.5f)
					&& (y + dir.getDy() > 4 && y + dir.getDy() < 5.5f)) {
				return false;
			}

			// Collsion House
			if ((x + dir.getDx() > 8.5f && x + dir.getDx() < 12) && (y + dir.getDy() >= 10 && y + dir.getDy() < 12)) {
				return false;
			} else if ((x + dir.getDx() > 16 && x + dir.getDx() <= 19.5f)
					&& (y + dir.getDy() >= 10 && y + dir.getDy() <= 11)) {
				return false;
			} else if ((x + dir.getDx() > 15 && x + dir.getDx() < 19)
					&& (y + dir.getDy() >= 6.5f && y + dir.getDy() <= 8)) {
				return false;
			}

			// Collision Wall
			if ((x + dir.getDx() >= 8.5f && x + dir.getDx() <= 19.5f)
					&& (y + dir.getDy() >= 5.5f && y + dir.getDy() < 6)) {
				return false;
			} else if ((x + dir.getDx() >= 5.5f && x + dir.getDx() < 8.5f)
					&& (y + dir.getDy() >= 5.5f && y + dir.getDy() < 7)) {
				return false;
			} else if ((x + dir.getDx() >= 7 && x + dir.getDx() < 8.5f)
					&& (y + dir.getDy() > 7.5f && y + dir.getDy() < 12.5f)) {
				return false;
			} else if ((x + dir.getDx() >= 8.5f && x + dir.getDx() <= 19.5f)
					&& (y + dir.getDy() > 11.5f && y + dir.getDy() < 12.5f)) {
				return false;
			}
		}

		if (x + dir.getDx() >= Gdx.graphics.getWidth() / 32 || x + dir.getDx() < 0
				|| y + dir.getDy() >= Gdx.graphics.getHeight() / 33 || y + dir.getDy() < 0) {
			return false;
		}

		initMove(dir);
		x += dir.getDx();
		y += dir.getDy();

		return true;
	}

	public void initMove(DIRECTION dir) {
		this.facing = dir;
		this.srcX = x;
		this.srcY = y;
		this.destX = x + dir.getDx();
		this.destY = y + dir.getDy();
		this.worldX = x;
		this.worldY = y;
		animaTimer = 0f;
		state = ACTOR_STATE.WALKING;
	}

	public void finishMove() {
		state = ACTOR_STATE.STANDING;
		this.worldX = destX;
		this.worldY = destY;
		this.srcX = 0;
		this.srcY = 0;
		this.destX = 0;
		this.destY = 0;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWorldX() {
		return worldX;
	}

	public float getWorldY() {
		return worldY;
	}

	public TextureRegion getSprite() {
		if (state == ACTOR_STATE.WALKING) {
			return animationPlayer.getWalking(facing).getKeyFrame(walkTimer);
		} else if (state == ACTOR_STATE.STANDING) {
			return animationPlayer.getStanding(facing);
		}

		return animationPlayer.getStanding(DIRECTION.SOUTH);
	}

	public int getPlayerHP() {
		return Integer.parseInt(playerHP);
	}

	public void setPlayerHP(String playerHP) {
		this.playerHP = playerHP;
	}

}
