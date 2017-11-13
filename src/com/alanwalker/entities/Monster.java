package com.alanwalker.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Monster {

	private String state;
	private Texture monster;
	private int monsterHp;
	private int monsterAttack;
	private int monsterExp;

	public Monster(String state) {
		this.state = state;
		if(state == "JungleState") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/Slime.png"));
			monsterHp = 10;
			monsterAttack = 0;
			monsterExp = 2;
		} else if(state == "JungleBoss") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/BossLion.png"));
			monsterHp = 50;
			monsterAttack = 0;
			monsterExp = 10;
		} else if(state == "JungleToCaveState") {
			monster = new Texture(Gdx.files.internal("resource/monsterandboss/Slime.png"));
			monsterHp = 50;
			monsterAttack = 0;
			monsterExp = 5;
		}
	}
	
	public void update() {
		if (state == "JungleState") {
			monsterAttack = (int) (Math.random() * 10);
		} else if(state == "JungleBoss") {
			monsterAttack = (int) (Math.random() * 20);
		} else if(state == "JungleToCaveState") {
			monsterAttack = (int) (Math.random() * 30);
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
