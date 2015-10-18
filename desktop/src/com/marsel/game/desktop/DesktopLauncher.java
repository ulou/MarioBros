package com.marsel.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.marsel.game.MyGdxGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = MyGdxGame.V_WIDTH * 3;
		config.height = MyGdxGame.V_HEIGHT * 3;

		new LwjglApplication(new MyGdxGame(), config);
}
		}
