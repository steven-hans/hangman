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

import java.util.ArrayList;
import java.util.Arrays;

public class GameplayScreen extends SwitchableScreen implements Screen {
    private ArrayList<Texture> stick;
    private ArrayList<String> wordlist;
    private SpriteBatch batch;
    private BitmapFont guessFont;
    private BitmapFont infoFont;
    private GuessResult lastGuessResult;
    private Music backgroundMusic;
    private Music loseMusic;

    private HangmanGame game;

    /**
     * Memuat font yang akan digunakan pada game.
     */
    private void setupFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PTSerif-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        guessFont = generator.generateFont(parameter);

        parameter.size = 20;
        infoFont = generator.generateFont(parameter);

        generator.dispose();

        guessFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        guessFont.getData().setScale(1.1f, 1.1f);

        infoFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        infoFont.getData().setScale(0.85f, 0.85f);
    }

    /**
     * Memuat gambar-gambar hangman yang akan ditampilkan pada game.
     * Gambar-gambar ini meliputi: 0.png, 1.png, ..., 5.png.
     */
    private void loadStickAsset() {
        stick = new ArrayList<>();
        for (int i = 0; i <= 5; ++i) {
            stick.add(new Texture(Gdx.files.internal("stick/" + i + ".png")));
        }
    }

    /**
     * Memuat daftar kata yang akan ditampilkan pada game.
     * Nama file dari daftar ini adalah wordlist.txt.
     */
    private void loadWordList() {
        FileHandle file = Gdx.files.internal("wordlist.txt");
        String text = file.readString();

        wordlist = new ArrayList<>(Arrays.asList(text.split("\n")));

    }

    /**
     * Memuat lagu yang akan diputar pada game.
     */
    private void setupMusic() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/aku_bersyukur.mp3"));
        backgroundMusic.setLooping(true);
        loseMusic = Gdx.audio.newMusic(Gdx.files.internal("music/circus_song.mp3"));
        loseMusic.setLooping(true);
    }

    /**
     * Memuat event saat player menekan tombol pada keyobard.
     */
    private void setupKeyTypedEvent() {
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
    }

    /**
     * Saat screen gameplay dikonstruksi, maka game akan memuat wordlist, memuat gambar stick yang akan digambar,
     * memuat font, memuat musik yang ada, dan mengatur event saat player menekan tombol pada keyboard.
     * @param hangman hangman.
     */
    public GameplayScreen(Hangman hangman) {
        super(hangman);
        batch = new SpriteBatch();
        loadWordList();
        game = new HangmanGame(wordlist);

        loadStickAsset();
        setupFont();
        setupKeyTypedEvent();
        setupMusic();
    }

    /**
     * Memainkan musik awal saat layar ini pertama kali muncul.
     */
    @Override
    public void show() {
        backgroundMusic.play();
    }

    /**
     * Menggambar stick figure yang akan digantung oleh hangman.
     */
    private void drawStick() {
        batch.draw(stick.get(game.getLife()), Gdx.graphics.getWidth() / 2 - 256, Gdx.graphics.getHeight() / 2 - 100, 300, 300);
    }

    /**
     * Menampilkan score pada game.
     */
    private void drawScore() {
        String scoreText = "Score: " + game.getScore();

        infoFont.draw(batch, scoreText, Gdx.graphics.getWidth()-90, Gdx.graphics.getHeight()-20);
    }

    /**
     * Menampilkan teks informasi di samping stick figure.
     */
    private void drawSideText() {
        String sideText;
        if (game.isFinished()) {
            if (game.isWin()) {
                sideText = "You win!";
            } else {
                sideText = "You're dead!";
            }

            sideText += "\nPress (r) to restart\nPress (q) to go to menu";
        } else {
            if (game.getNumberOfGuesses() == 0) {
                sideText = "Guess now!\nYour life is on the line, after all.";
            } else {
                if (lastGuessResult == GuessResult.ALREADY_GUESSED) {
                    sideText = "You already guessed that!\nTry another one!";
                } else if (lastGuessResult == GuessResult.INCORRECT) {
                    sideText = "Incorrect!\nYou're one step closer to death.";
                } else {
                    sideText = "Correct!\nKeep Guessing!";
                }
            }
        }

        infoFont.draw(batch, sideText, Gdx.graphics.getWidth() - 256, Gdx.graphics.getHeight() / 2 + 64);
    }

    /**
     * Mengatur musik yang ada dalam game.
     */
    private void handleMusic() {
        if (game.isFinished()) {
            if (game.isWin()) {

            } else {
                if (backgroundMusic.isPlaying()) {
                    backgroundMusic.stop();
                    loseMusic.play();
                }
            }
        }
    }

    /**
     * Menunjukkan status kata yang ditebak.
     */
    private void drawCurrentFill() {
        if (game.isFinished()) {
            guessFont.draw(batch, game.getCurrentWord(), Gdx.graphics.getWidth() / 2 - 256, 96);
        } else {
            guessFont.draw(batch, game.getCurrentFill(), Gdx.graphics.getWidth() / 2 - 256, 96);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        drawStick();
        drawSideText();
        handleMusic();
        drawScore();
        drawCurrentFill();
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
