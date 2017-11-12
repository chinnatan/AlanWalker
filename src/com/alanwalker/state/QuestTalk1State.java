package com.alanwalker.state;

import java.io.FileOutputStream;
import java.io.IOException;
import com.alanwalker.main.AlanWalker;
import com.alanwalker.util.LoadSave;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class QuestTalk1State extends AbstractState {

	private AbstractState screen;
	private SpriteBatch sb;
	private Skin skin;
	private Stage stage;
	private Label npcQuestLabel;
	private Label.LabelStyle npcQuestStyle;
	private TextButton yesButton, noButton;
	private Texture dialogueBox, npc;

	// Player Status
	private int playerHp;
	private int playerLevel;
	private int playerExp;
	private int playerAttack;
	private float positionPlayerX, positionPlayerY;
	private LoadSave loadPlayer;

	public QuestTalk1State(AlanWalker aw, float oldX, float oldY) {
		super(aw);
		sb = new SpriteBatch();
		
		// Load data player
		loadPlayer = new LoadSave();
		playerLevel = Integer.parseInt(loadPlayer.getLevel());
		playerHp = Integer.parseInt(loadPlayer.getPlayerHP());
		playerExp = Integer.parseInt(loadPlayer.getExp());
		playerAttack = Integer.parseInt(loadPlayer.getAttack());
		positionPlayerX = oldX;
		positionPlayerY = oldY;

		// Load Dialoguebox UI
		dialogueBox = new Texture(Gdx.files.internal("resource/ui/dialoguebox/dialoguebox.png"));

		// Load Nurse
		npc = new Texture(Gdx.files.internal("resource/character/npc-quest-1.png"));

		// Load Button TextureAtlas
		TextureAtlas startButtonAtlas = new TextureAtlas(Gdx.files.internal("resource/ui/button/button.atlas"));

		// Create a font
		BitmapFont font = new BitmapFont(Gdx.files.internal("resource/fonts/Kanit-Regular-18.fnt"));

		// Load Button UI
		skin = new Skin(startButtonAtlas);
		skin.add("default", font);

		// Create a button style
		TextButton.TextButtonStyle yesButtonStyle = new TextButton.TextButtonStyle();
		yesButtonStyle.up = skin.newDrawable("yes-active");
		yesButtonStyle.over = skin.newDrawable("yes-over");
		yesButtonStyle.down = skin.newDrawable("yes-clicked");
		yesButtonStyle.font = skin.getFont("default");
		
		TextButton.TextButtonStyle noButtonStyle = new TextButton.TextButtonStyle();
		noButtonStyle.up = skin.newDrawable("no-active");
		noButtonStyle.over = skin.newDrawable("no-over");
		noButtonStyle.down = skin.newDrawable("no-clicked");
		noButtonStyle.font = skin.getFont("default");
		
		// Create a label style
		npcQuestStyle = new Label.LabelStyle();
		npcQuestStyle.font = skin.getFont("default");
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);// Make the stage consume events

		yesButton = new TextButton("", yesButtonStyle); // Use the initialized skin
		yesButton.setPosition(Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 16);
		yesButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if(loadPlayer.getProp().getProperty("Quest1").equals("start")) {
					aw.setScreen(new JungleState(aw, positionPlayerX, positionPlayerY));
				} else if(loadPlayer.getProp().getProperty("Quest1").equals("end")) {
					aw.setScreen(new JungleState(aw, positionPlayerX, positionPlayerY));
				} else {
					try {
						loadPlayer.getProp().setProperty("Quest1", "start");
						loadPlayer.getProp().setProperty("Quest1CountMonster", "0");
						loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					aw.setScreen(new JungleState(aw, positionPlayerX, positionPlayerY));
				}
				
				if(loadPlayer.getProp().getProperty("Quest1").equals("start") && loadPlayer.getProp().getProperty("Quest1CountMonster").equals("10")) {
//					try {
//						loadPlayer.getProp().setProperty("Quest1", "end");
//						loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
					aw.setScreen(new BattleState(aw, "JungleBoss", positionPlayerX, positionPlayerY));
				}
			}
		});
		
		noButton = new TextButton("", noButtonStyle); // Use the initialized skin
		noButton.setPosition(Gdx.graphics.getWidth() / 2 + 70, Gdx.graphics.getHeight() / 16);
		noButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					loadPlayer.getProp().setProperty("startX", String.valueOf(positionPlayerX));
					loadPlayer.getProp().setProperty("startY", String.valueOf(positionPlayerY));
					loadPlayer.getProp().store(new FileOutputStream("saves/save.properties"), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				aw.setScreen(new JungleState(aw, positionPlayerX, positionPlayerY));
			}
		});
		
		// Label
		npcQuestLabel = new Label("กำจัด Slime จำนวน 10 ตัวเพื่อสู้กับบอสของแมพ เจ้าจะทำหรือไม่ ?", npcQuestStyle);
		npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 5, 10, 10);
		npcQuestLabel.setColor(Color.WHITE);
		npcQuestLabel.setFontScale(1f, 1f);

		stage.addActor(yesButton);
		stage.addActor(noButton);
		stage.addActor(npcQuestLabel);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		sb.dispose();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	public void update(float delta) {

	}

	@Override
	public void render(float delta) {

		// Clear Screen
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Check Quest
		if(loadPlayer.getProp().getProperty("Quest1").equals("start")) {
			if(loadPlayer.getProp().getProperty("Quest1CountMonster").equals("10")) {
				npcQuestLabel.setText("คุณต้องการส่งเควสและสู้กับบอสแมพใช่หรือไม่");
				npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, 10, 10);
				npcQuestLabel.setColor(Color.WHITE);
				npcQuestLabel.setFontScale(1f, 1f);
			} else{
				npcQuestLabel.setText("คุณยังกำจัด Slime ไม่ครบตามจำนวนที่กำหนด");
				npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, 10, 10);
				npcQuestLabel.setColor(Color.WHITE);
				npcQuestLabel.setFontScale(1f, 1f);
			}
		} else {
			npcQuestLabel.setText("คุณผ่านเควสนี้เรียบร้อยแล้ว...กรุณาทำเควสด่านถัดไป");
			npcQuestLabel.setBounds(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, 10, 10);
			npcQuestLabel.setColor(Color.WHITE);
			npcQuestLabel.setFontScale(1f, 1f);
			yesButton.setPosition(Gdx.graphics.getWidth() / 4 + 50, Gdx.graphics.getHeight() / 16);
			noButton.setVisible(false);
		}

		// Draw All Object on Screen
		sb.begin();
		sb.draw(npc, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 4, 240, 320);
		sb.draw(dialogueBox, (Gdx.graphics.getWidth() / 60), 0, Gdx.graphics.getWidth() - 20,
				Gdx.graphics.getHeight() - 320);
		sb.end();
		stage.act();
		stage.draw();
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

	}

}
