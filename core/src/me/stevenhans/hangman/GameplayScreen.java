package me.stevenhans.hangman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class GameplayScreen implements Screen {
    private ArrayList<Texture> stick;
    private ArrayList<String> wordlist;
    private Hangman parent;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont guessFont;
    private BitmapFont infoFont;
    private GuessResult lastGuessResult;
    private Music backgroundMusic;
    private Music loseMusic;

    private HangmanGame game;

    public void resetInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

    public GameplayScreen(Hangman hangman) {
        parent = hangman;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();

        stick = new ArrayList<>();
        for (int i = 0; i <= 5; ++i) {
            stick.add(new Texture(Gdx.files.internal("stick/" + i + ".png")));
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PTSerif-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        guessFont = generator.generateFont(parameter);

        parameter.size = 18;
        infoFont = generator.generateFont(parameter);

        generator.dispose();

        FileHandle file = Gdx.files.internal("wordlist.txt");
        String text = file.readString();

        wordlist = new ArrayList<>(Arrays.asList(text.split("\n")));

        guessFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        guessFont.getData().setScale(1.1f, 1.1f);

        infoFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        infoFont.getData().setScale(0.85f, 0.85f);

        game = new HangmanGame(wordlist);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (Character.isLetter(character)) {
                    if (!game.isFinished()) {
                        lastGuessResult = game.guess(Character.toLowerCase(character));
                    } else {
                        if (character == 'r') {
                            game.restartGame();
                            loseMusic.stop();
                            backgroundMusic.play();
                        } else if (character == 'q') {
                            game.restartGame();
                            loseMusic.stop();
                            backgroundMusic.stop();
                            parent.changeScreen(ScreenSection.MENU);
                        }
                    }
                }
                return super.keyTyped(event, character);
            }
        });

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/aku_bersyukur.mp3"));
        backgroundMusic.setLooping(true);
        loseMusic = Gdx.audio.newMusic(Gdx.files.internal("music/circus_song.mp3"));
        loseMusic.setLooping(true);
    }

    @Override
    public void show() {
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(stick.get(game.getLife()), Gdx.graphics.getWidth()/2-256, Gdx.graphics.getHeight()/2-100, 300, 300);

        String sideText;
        if (game.isFinished()) {
            if (game.isWin()) {
                sideText = "You win!";
            } else {
                if (backgroundMusic.isPlaying()) {
                    backgroundMusic.stop();
                    loseMusic.play();
                }
                sideText = "You lose!\nThe crowd are celebrating now!";
            }

            sideText += "\nPress (r) to restart\nPress (q) to go to menu";
            guessFont.draw(batch, game.getCurrentWord(), Gdx.graphics.getWidth()/2-256, 96);
        } else {
            if (game.getNumberOfGuesses() == 0) {
                sideText = "Guess now!\nYour life is on the line, after all.";
            } else {
                if (lastGuessResult == GuessResult.ALREADY_GUESSED) {
                    sideText = "You already guessed that!\nTry another one!";
                } else if (lastGuessResult == GuessResult.INCORRECT) {
                    sideText = "Incorrect!\nPeople are getting wilder!";
                } else {
                    sideText = "Correct!\nKeep Guessing!";
                }
            }
            guessFont.draw(batch, game.getCurrentFill(), Gdx.graphics.getWidth()/2-256, 96);
        }

        infoFont.draw(batch, sideText, Gdx.graphics.getWidth()-256, Gdx.graphics.getHeight()/2+64);

        batch.end();
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
        stage.dispose();
    }
}
