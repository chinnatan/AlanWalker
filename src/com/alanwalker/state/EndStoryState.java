package com.alanwalker.state;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import com.alanwalker.main.AlanWalker;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EndStoryState extends AbstractState implements InputProcessor {

	// Status Player
	private String level = "1";
	private String attack = Integer.toString((int) (Math.random() * 10));
	private String playerHP = "100";
	private String startX = "10.5";
	private String startY = "1";
	private String exp = "0";
	private String mapName = "VillageState";
	
	private Texture endStory;
	private BitmapFont font;
	private SpriteBatch sb;
	private int next = 0;
	private String[] storys = { "กดปุ่ม 'Enter' ดำเนินการต่อ",
			"ขอบคุณที่ช่วยกำจัดพ่อมดและช่วยเหลือเทพธิดา",
			"....เมื่อเทพธิดากลับมาที่หมูบ้าน....",
			"...หมู่บ้านก็กลับมาสงบสุขอีกครั้ง..." };

	public EndStoryState(AlanWalker aw) {
		super(aw);

		font = new BitmapFont(Gdx.files.internal("resource/fonts/Kanit-Regular-18.fnt"));

		sb = new SpriteBatch();
		
		endStory = new Texture(Gdx.files.internal("resource/maps/endstory.png"));
	}

	@Override
	public void dispose() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		sb.draw(endStory, 0, 0);
		font.setColor(Color.WHITE);
		font.draw(sb, storys[next], Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
		sb.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {

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
					LoadSave end = new LoadSave();
					end.getProp().setProperty("newgame", "end");
					end.getProp().setProperty("hp", playerHP);
					end.getProp().setProperty("level", level);
					end.getProp().setProperty("exp", "0");
					end.getProp().setProperty("attack", attack);
					end.getProp().setProperty("startX", startX);
					end.getProp().setProperty("startY", startY);
					end.getProp().setProperty("mapName", mapName);
					end.getProp().setProperty("Quest1", "null");
					end.getProp().setProperty("Quest1CountMonster", "0");
					end.getProp().setProperty("Quest2", "null");
					end.getProp().setProperty("Quest2CountMonster", "0");
					end.getProp().setProperty("Boss", "null");
					// save properties to project root folder
					end.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				aw.setScreen(new MenuState(aw));
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
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
