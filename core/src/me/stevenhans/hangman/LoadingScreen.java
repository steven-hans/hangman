package me.stevenhans.hangman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Arrays;

import static me.stevenhans.hangman.GameplayScreen.*;

public class LoadingScreen extends SwitchableScreen implements Screen {
    public LoadingScreen(Hangman hangman) {
        super(hangman);
    }

    private void loadGameResources() {
        if (GameplayScreen.wordlist == null) {
            loadStickAsset();
            setupFont();
            loadSounds();
            loadWordList();
        }

        setupMusic();
    }

    private void showLoadingScreen() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Skin skin = new Skin((Gdx.files.internal("skin/skin.json")));

        Label loadingLabel = new Label("loading...", skin);

        table.add(loadingLabel).fillX().uniformX();
        table.row();
    }

    @Override
    public void show() {
        stage.addAction(Actions.sequence(Actions.delay(0.5f), new Action() {
            @Override
            public boolean act(float delta) {
                showLoadingScreen();
                return true;
            }
        }, Actions.delay(0.05f), new Action() {
            @Override
            public boolean act(float delta) {
                loadGameResources();
                return true;
            }
        }, Actions.delay(0.05f), new Action() {
            @Override
            public boolean act(float delta) {
                parent.changeScreen(ScreenSection.GAMEPLAY);
                return true;
            }
        }));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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
