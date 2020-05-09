package me.stevenhans.hangman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * HangmanGame adalah permainan game Hangman itu sendiri.
 * Kelas ini mengatur suatu game Hangman yang sedang dimainkan.
 *
 * Singkatnya, Hangman adalah permainan tebak kata dimana pemain
 * akan diberikan suatu kata yang seluruh hurufnya tersembunyi.
 * Pemaini akan menebak huruf huruf dari kata ini dengan menebak huruf demi huruf.
 * Tebakan yang benar akan menampilkan huruf tersebut pada kata yang ditebak.
 * Namun, tebakan yang salah akan menyebabkan gambar orang yang digantung semakin jelas.
 * Apabila pemain salah menebak lima kali, gambar akan selesai dan pemain akan digantung oleh hangman.
 * Pemain menang apabila berhasil menebak seluruh huruf yang ada pada kata.
 */
public class HangmanGame {
    /**
     * Daftar kata pada permainan. Salah satu kata akan diambil dari daftar ini untuk ditebak.
     */
    private ArrayList<String> wordlist;

    /**
     * Jumlah maksimum tebakan yang salah. Nilai 0 berarti pemain salah menebak sebanyak 5 kali dan kalah.
     */
    private int life = 5;

    /**
     * guessed adalah himpunan karakter yang telah ditebak oleh pemain.
     */
    private Set<Character> guessed;

    /**
     * currentWord adalah kata yang sedang ditebak oleh pemain.
     */
    private String currentWord;

    /**
     * currentFill adalah status tebakan pemain.
     *
     * Misalkan currentWord = "jazz"
     *
     * Pada awalnya, currentFill akan menjadi "_ _ _ _".
     *
     * Apabila pemain menebak huruf "a", maka currentFill akan menjadi "_ a _ _"
     *
     * dan seterusnya hingga permainan selesai.
     */
    private StringBuilder currentFill;

    /**
     * Score menyimpan tebakan yang berhasil ditebak pemain.
     */
    private int score = 0;

    /**
     * Jumlah huruf yang berhasil ditebak berturut-turut
     */
    private int strike = 0;

    public HangmanGame(ArrayList<String> wordlist) {
        this.wordlist = wordlist;
        guessed = new TreeSet<>();

        restartGame();
    }

    /**
     * Memulai permainan dari awal.
     */
    public void restartGame() {
        life = 5;

        if (guessed.size() > 0 && !isWin()) {
            score = 0;
            strike = 0;
        }

        guessed.clear();
        int rand = ThreadLocalRandom.current().nextInt(0, this.wordlist.size());
        currentWord = this.wordlist.get(rand);
        currentFill = new StringBuilder(String.join("", Collections.nCopies(currentWord.length(), "_")));
    }

    /**
     * Mengambil kata yang sedang ditebak.
     * @return kata yang sedang ditebak.
     */
    public String getCurrentWord() {
        return currentWord;
    }

    /**
     * Menampilkan status kata yang telah ditebak.
     * @return status tebakan kata (contoh: _ a _ _, untuk kata tebakan "jazz")
     */
    public String getCurrentFill() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < currentFill.length(); ++i) {
            res.append(currentFill.charAt(i));
            if (i != currentFill.length()-1) {
                res.append(" ");
            }
        }
        return res.toString();
    }

    /**
     * Apabila pemain menebak dengan benar, maka akan ditampilkan huruf ini pada status tebakan kata.
     * @param c huruf yang ditebak.
     */
    private void fillGuess(char c) {
        for (int i = 0; i < currentWord.length(); ++i) {
            if (currentWord.charAt(i) == c) {
                currentFill.setCharAt(i, c);
            }
        }
    }

    /**
     * Mendapatkan kesempatan yang ada saat waktu itu.
     * @return jumlah kesempatan.
     */
    public int getLife() {
        return life;
    }

    /**
     * Mengurangi jumlah kesempatan tebakan salah pemain.
     */
    private void decreaseLife() {
        life--;
    }

    /**
     * Mengembalikan jumlah huruf yang telah ditebak pemain.
     * @return
     */
    public int getNumberOfGuesses() {
        return guessed.size();
    }

    /**
     * Menebak huruf pada kata. Terdapat tiga hasil tebakan yang disesuaikan dengan GuessResult:
     * ALREADY_GUESSED, CORRECT, dan INCORRECT.
     *
     * Apabila ALREADY_GUESSED maka tidak ada konsekuensi pada permainan.
     * Apabila CORRECT, maka akan muncul huruf yang ditebak pada status tebakan kata.
     * Apabila INCORRECT, maka pemain akan dikurangi kesempatan tebakan salahnya dan ditampilkan gambar hangman
     * selanjutnya.
     *
     * Setiap tebakan akan dimasukkan pada himpunan tebakan guessed.
     * @param c huruf yang ditebak pemain.
     * @return hasil tebakan dalam GuessResult.
     */
    public GuessResult guess(char c) {
        Character guess = c;

        if (guessed.contains(guess)) {
            return GuessResult.ALREADY_GUESSED;
        } else {
            guessed.add(guess);
            if (currentWord.contains(String.valueOf(c))) {
                fillGuess(c);
                score += 5*(++strike);
                if (isWin()) score += 50;
                return GuessResult.CORRECT;
            } else {
                strike = 0;
                decreaseLife();
                return GuessResult.INCORRECT;
            }
        }
    }

    /**
     * Mengembalikan jumlah strike (huruf yang berhasil ditebak benar berturut-turut).
     * @return jumlah strike sekarang.
     */
    public int getStrike() { return strike; }

    /**
     * Menentukan apabila permainan telah selesai atau belum.
     * @return true, apabila sudah selesai, false, apabila belum selesai.
     */
    public boolean isFinished() {
        return (life == 0 || isWin());
    }

    /**
     * Menentukan apabila pemain menang dalam permainan sekarang.
     * @return true, apabila pemain menang dalam permainan sekarang, fase, apabila sebaliknya.
     */
    public boolean isWin() {
        return currentFill.toString().equalsIgnoreCase(currentWord);
    }

    /**
     * Mengembalikan score pemain saat itu juga.
     * @return score game.
     */
    public int getScore() {
        return score;
    }

    /**
     * Melakukan reset pada score.
     */
    public void resetScore() {
        score = 0;
    }
}
