package me.stevenhans.hangman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * SwitchableScreen adalah layar pada game yang dapat diganti. Untuk mengganti layar ke layar lain,
 * kita harus melakukan setScreen pada game. Ini sudah diatur pada Hangman dalam fungsi changeScreen.
 * Namun, kita masih harus mengganti input processor ke layar yang akan diganti supaya layar yang akan
 * diganti menerima input dari pengguna. Ini akan dilakukan oleh setiap layar yang dapat diganti, sehingga
 * kelas abstrak ini dibuat untuk mengakomodasinya.
 */
public abstract class SwitchableScreen {
    protected Stage stage;
    protected Hangman parent;

    /**
     * Saat suatu SwitchableScreen dibuat, referensi dari kelas game (parent) akan disimpan
     * supaya dapat dilakukan penggantian layar (mengakses fungsi changeScreen).
     * @param hangman
     */
    protected SwitchableScreen(Hangman hangman) {
        parent = hangman;
        stage = new Stage(new ScreenViewport());
        resetInputProcessor();
    }

    /**
     * Mengganti input processor ke layar ini.
     * HARUS DILAKUKAN SETIAP KALI MENGGANTI LAYAR.
     */
    public void resetInputProcessor() {
        Gdx.input.setInputProcessor(stage);
    }

}
