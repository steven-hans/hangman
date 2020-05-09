package me.stevenhans.hangman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class SwitchableScreen {
    protected Stage stage;
    protected Hangman parent;

    protected SwitchableScreen(Hangman hangman) {
        parent = hangman;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    public void resetInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

}
