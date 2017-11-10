package com.alanwalker.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class LoadSave {
	
	// Status Player
	private String level = "1";
	private String attack = Integer.toString((int) (Math.random() * 10));
	private String playerHP = "100";
	private String startX = "10.5";
	private String startY = "0"; 
	private String exp = "0";
	
	private Properties prop = new Properties();
	private OutputStream outputSave = null;
	private InputStream inputSave = null;
	
	public LoadSave() {
		try {
			inputSave = new FileInputStream("saves/save.properties");
			prop.load(inputSave);
			level = prop.getProperty("level");
			exp = prop.getProperty("exp");
			attack = prop.getProperty("attack");
			playerHP = prop.getProperty("hp");
			startX = prop.getProperty("startX");
			startY = prop.getProperty("startY");
		} catch (FileNotFoundException e) {
			try {
				outputSave = new FileOutputStream("saves/save.properties");
				// set the properties value
				prop.setProperty("hp", playerHP);
				prop.setProperty("level", level);
				prop.setProperty("exp", "0");
				prop.setProperty("attack", attack);
				prop.setProperty("startX", startX);
				prop.setProperty("startY", startY);
				// save properties to project root folder
				prop.store(outputSave, null);
			} catch (IOException io) {
				io.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getAttack() {
		attack = Integer.toString((int) (Math.random() * 10));
		return attack;
	}

	public void setAttack(String attack) {
		this.attack = attack;
	}

	public String getPlayerHP() {
		return playerHP;
	}

	public void setPlayerHP(String playerHP) {
		this.playerHP = playerHP;
	}

	public String getStartX() {
		return startX;
	}

	public void setStartX(String startX) {
		this.startX = startX;
	}

	public String getStartY() {
		return startY;
	}

	public void setStartY(String startY) {
		this.startY = startY;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public OutputStream getOutputSave() {
		return outputSave;
	}

	public InputStream getInputSave() {
		return inputSave;
	}

}
