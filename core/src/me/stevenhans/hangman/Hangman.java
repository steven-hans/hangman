package me.stevenhans.hangman;

import com.badlogic.gdx.Game;

public class Hangman extends Game {
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;
    private GameplayScreen gameplayScreen;
    private ResultScreen resultScreen;

    /**
     * Mengganti layar dengan layar lain.
     * @param screenSection layar yang ada pada game.
     */
    public void changeScreen(ScreenSection screenSection) {
        switch (screenSection) {
            case LOADING:
                if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
                loadingScreen.resetInputProcessor();
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
        changeScreen(ScreenSection.MENU);
    }

    @Override
    public void render() {
        super.render();
    }
}
