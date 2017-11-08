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
		if(state == "VillageState") {
			monster = new Texture(Gdx.files.internal("resource/character/alan.png"));
			monsterHp = 10;
			monsterAttack = 0;
			monsterExp = 2;
		}
	}
	
	public void update() {
		if (state == "VillageState") {
			monsterAttack = (int) (Math.random() * 10);
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
