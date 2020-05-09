package me.stevenhans.hangman;

import com.badlogic.gdx.Screen;

public class LoadingScreen extends SwitchableScreen implements Screen {
    public LoadingScreen(Hangman hangman) {
        super(hangman);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // TODO actually load files instead of skipping directly to menu screen.
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
