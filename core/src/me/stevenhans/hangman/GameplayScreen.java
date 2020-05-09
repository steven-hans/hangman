package me.stevenhans.hangman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class GameplayScreen extends SwitchableScreen implements Screen {
    protected static ArrayList<String> wordlist;
    protected static ArrayList<Texture> stick;
    protected static Music backgroundMusic;
    protected static Music loseMusic;
    protected static Sound type_correct;
    protected static Sound type_incorrect;
    protected static BitmapFont guessFont;
    protected static BitmapFont infoFont;
    private TextButton hintButton;
    private SpriteBatch batch;
    private GuessResult lastGuessResult;
    private HangmanGame game;

    /**
     * Saat screen gameplay dikonstruksi, maka game akan memuat wordlist, memuat gambar stick yang akan digambar,
     * memuat font, memuat musik yang ada, dan mengatur event saat player menekan tombol pada keyboard.
     *
     * @param hangman hangman.
     */
    public GameplayScreen(Hangman hangman) {
        super(hangman);
        batch = new SpriteBatch();
        game = new HangmanGame(wordlist);

        setupKeyTypedEvent();
    }

    /**
     * Memuat font yang akan digunakan pada game.
     */
    protected static void setupFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("PTSerif-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 45;
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
     * Memuat suara feedback saat pemain menekan alfabet di keyboard.
     */
    protected static void loadSounds() {
        type_correct = Gdx.audio.newSound(Gdx.files.internal("sound/type_correct.mp3"));
        type_incorrect = Gdx.audio.newSound(Gdx.files.internal("sound/type_incorrect.mp3"));
    }

    /**
     * Memuat daftar kata yang akan ditampilkan pada game.
     * Nama file dari daftar ini adalah wordlist.txt.
     */
    protected static void loadWordList() {
        FileHandle file = Gdx.files.internal("wordlist.txt");
        String text = file.readString();

        wordlist = new ArrayList<>(Arrays.asList(text.split("\n")));
    }

    /**
     * Memuat gambar-gambar hangman yang akan ditampilkan pada game.
     * Gambar-gambar ini meliputi: 0.png, 1.png, ..., 5.png.
     */
    protected static void loadStickAsset() {
        stick = new ArrayList<>();
        for (int i = 0; i <= 5; ++i) {
            stick.add(new Texture(Gdx.files.internal("stick/" + i + ".png")));
        }
    }

    /**
     * Memuat lagu yang akan diputar pada game.
     */
    protected static void setupMusic() {
        int trackNo = ThreadLocalRandom.current().nextInt(1, 11);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/"+ trackNo + ".mp3"));
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
            public boolean keyDown(InputEvent event, int keycode) {
                char character = Character.toLowerCase(Input.Keys.toString(keycode).charAt(0));

                if (keycode == Input.Keys.ESCAPE) {
                    game.restartGame();
                    loseMusic.stop();
                    backgroundMusic.stop();
                    parent.changeScreen(ScreenSection.MENU);
                } else if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) {
                    if (canHint()) game.doHint();
                } else if (Input.Keys.A <= keycode && keycode <= Input.Keys.Z) {
                    if (!game.isFinished()) {
                        lastGuessResult = game.guess(Character.toLowerCase(character));
                        if (lastGuessResult == GuessResult.CORRECT) {
                            type_correct.play();
                        } else {
                            type_incorrect.play();
                        }
                    } else {
                        if (character == 'r') {
                            game.nextGame();
                            loseMusic.stop();
                            backgroundMusic.play();
                        } else if (character == 'q') {
                            game.restartGame();
                            loseMusic.stop();
                            backgroundMusic.stop();
                            parent.changeScreen(ScreenSection.MENU);
                        } else {
                            type_incorrect.play();
                        }
                    }
                }

                return super.keyDown(event, keycode);
            }
        });
    }

    private void setupHintButton() {
        Skin skin = new Skin((Gdx.files.internal("skin/skin.json")));
        hintButton = new TextButton("Hint (shift)", skin);
        hintButton.setWidth(128);
        hintButton.setHeight(64);

        hintButton.setPosition(Gdx.graphics.getWidth() - 256, Gdx.graphics.getHeight() - 310);
        stage.addActor(hintButton);
        hintButton.setVisible(false);

        hintButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.doHint();
            }
        });
    }

    /**
     * Memainkan musik awal saat layar ini pertama kali muncul.
     */
    @Override
    public void show() {
        backgroundMusic.play();

        setupHintButton();
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

        if (game.getStrike() > 0) {
            scoreText += "\nStrike: " + game.getStrike();
        }
        infoFont.draw(batch, scoreText, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 20);
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
            guessFont.draw(batch, game.getCurrentWord(), Gdx.graphics.getWidth() / 2 - 256, 100);
        } else {
            guessFont.draw(batch, game.getCurrentFill(), Gdx.graphics.getWidth() / 2 - 256, 100);
        }
    }

    /**
     * Menentukan apakah hint dapat dipakai sekarang atau tidak.
     * Hint dapat dipakai apabila score > 20.
     *
     * @return true, apabila ya, false, apabila sebaliknya.
     */
    private boolean canHint() {
        return (!game.isFinished() && game.getScore() > 20);
    }

    /**
     * Menampilkan button hint apabila score lebih dari 20 selama game masih berlangsung.
     */
    private void handleHintButton() {
        if (canHint()) {
            hintButton.setVisible(true);
        } else {
            hintButton.setVisible(false);
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

        handleHintButton();

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
        stage.dispose();
    }
}
