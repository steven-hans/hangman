package me.stevenhans.hangman;

import com.badlogic.gdx.Screen;

public class LoadingScreen implements Screen {
    private Hangman parent;

    public LoadingScreen(Hangman hangman) {
        parent = hangman;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        parent.changeScreen(ScreenSection.MENU);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}