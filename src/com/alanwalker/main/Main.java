package com.alanwalker.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = Settings.TITLE;
		cfg.useGL30 = false;
		cfg.width = Settings.V_WIDTH;
		cfg.height = Settings.V_HEIGHT;
		cfg.resizable = false;
		cfg.vSyncEnabled = true;
		new LwjglApplication(new AlanWalker(), cfg);
	}

}
