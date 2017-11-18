package com.alanwalker.entities;

import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Monster {

	private String state;
	private Texture monster;
	private int monsterHp;
	private int monsterAttack;
	private int monsterExp;

	private int level;
	private LoadSave loadPlayer;
	
	public Monster(String state) {
		
		// Load data player
		loadPlayer = new LoadSave();
		level = Integer.parseInt(loadPlayer.getLevel());
		
		this.state = state;
		if(state == "JungleState") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/Slime.png"));
			monsterHp = (int) (10 + Math.pow(level, 2));
			monsterAttack = 0;
			monsterExp = 2;
		} else if(state == "JungleBoss") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/BossLion.png"));
			monsterHp = (int) (50 + Math.pow(level, level));
			monsterAttack = 0;
			monsterExp = 10;
		} else if(state == "JungleToCaveState") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/Slime.png"));
			monsterHp = (int) (50 + Math.pow(level, 2));
			monsterAttack = 0;
			monsterExp = 5;
		} else if(state == "CaveState") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/Corrupted-Rock-Spirit.png"));
			monsterHp = (int) (60 + Math.pow(level, 2));
			monsterAttack = 0;
			monsterExp = 10;
		} else if(state == "CaveBoss") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/Golem.png"));
			monsterHp = (int) (60 + Math.pow(level, level));
			monsterAttack = 0;
			monsterExp = 10;
		}
	}
	
	public void update() {
		if (state == "JungleState") {
			monsterAttack = (int) (Math.random() * 10 + Math.pow(level, 2));
		} else if(state == "JungleBoss") {
			monsterAttack = (int) (Math.random() * 20 + Math.pow(level, 2));
		} else if(state == "JungleToCaveState") {
			monsterAttack = (int) (Math.random() * 30 + Math.pow(level, 2));
		} else if(state == "CaveState") {
			monsterAttack = (int) (Math.random() * 40 + Math.pow(level, 2)) ;
		} else if(state == "CaveBoss") {
			monsterAttack = (int) (Math.random() * 50 + Math.pow(level, 2)) ;
		}
	}

	public int getHpMonster() {
		return monsterHp;
	}

	public int getMonsterAttack() {
		return monsterAttack;
	}

	public void setHpMonster(int hpMonster) {
		this.monsterHp = hpMonster;
	}

	public Texture getMonster() {
		return monster;
	}

	public int getMonsterExp() {
		return monsterExp;
	}
	
}
