package me.stevenhans.hangman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hangman extends Game {
	private LoadingScreen loadingScreen;
	private MenuScreen menuScreen;
	private GameplayScreen gameplayScreen;
	private ResultScreen resultScreen;

	public void changeScreen(ScreenSection screenSection) {
		switch (screenSection) {
			case LOADING:
				if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
				this.setScreen(loadingScreen);
				break;
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				menuScreen.resetInputProcessor();
				this.setScreen(menuScreen);
				break;
			case GAMEPLAY:
				if (gameplayScreen == null) gameplayScreen = new GameplayScreen(this);
				gameplayScreen.resetInputProcessor();
				this.setScreen(gameplayScreen);
				break;
			case RESULT:
				if (resultScreen == null) resultScreen = new ResultScreen(this);
				this.setScreen(resultScreen);
				break;
		}
	}

	@Override
	public void create() {
		loadingScreen = new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	@Override
	public void render() {
		super.render();
	}
}
