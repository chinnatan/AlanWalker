package com.alanwalker.state;

import com.alanwalker.main.AlanWalker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StoryState extends AbstractState implements InputProcessor {
	
	private AbstractState screen;
	private BitmapFont font;
	private SpriteBatch sb;
	private int next = 0;
	private String[] storys = { "กดปุ่ม 'Enter' ดำเนินการต่อ", "ณ หมู่บ้านแห่งหนึ่งที่สงบสุข และห่างไกล ชาวบ้านทุกคนต่าง",
			"อยู่กันอย่างมีความสุขแต่แล้ววันหนึ่งก็มีเหล่าพ่อมดชั่วร้ายผ่าน",
			"หมู่บ้านนี้มาและเข้ามาทำลายหมู่บ้าน โดยสาบให้หมู่บ้านนี้",
			"แห้งแล้ง และมืดมน หลังจากนั้นผู้คนในหมู่บ้านก็ไม่มีความสุข",
			"ไม่เคยมีพระอาทิตขึ้นอีกเลย น้ำก็แห้งขอด การจะช่วยหมู่บ้านนี้",
			"ได้คือต้องไปตามหาเทพธิดาเพื่อให้พวกเขาช่วยแก้มนต์ดำให้ " };
	
	public StoryState(AlanWalker aw) {
		super(aw);

		font = new BitmapFont(Gdx.files.internal("resource/fonts/Kanit-Regular-18.fnt"));
		
		sb = new SpriteBatch();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		font.setColor(Color.WHITE);
		font.draw(sb, storys[next], Gdx.graphics.getWidth() / 6, Gdx.graphics.getHeight() / 2);
		sb.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.ENTER) {
			if(next != 6) {
				next++;
			} else {
				screen = new VillageState(aw, 12, 2);
				aw.setScreen(screen);
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
	public boolean keyUp(int keycode) {
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
