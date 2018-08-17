package com.example.emirhan.iingilizce_ogren.abstraction.game;


import android.util.Log;

import com.example.emirhan.iingilizce_ogren.abstraction.core.Chain;
import com.example.emirhan.iingilizce_ogren.abstraction.core.Ref;
import com.example.emirhan.iingilizce_ogren.abstraction.data.FirebaseService;
import com.example.emirhan.iingilizce_ogren.abstraction.models.ScoreModel;
import com.example.emirhan.iingilizce_ogren.abstraction.models.WordModel;

/**
 * Created by emirhan on 12/14/2017.
 *
 * Oyun motoru su sekilde kullanilir:
 *
 * Her oyun turu icin;
 *
 * 1) NextGame() ile oyun baslatilir, donen degerden
 *    veritabani islemlerinin basarili olup olmadigina gore devam edilir
 *
 * 2) GetCurrentWord() ile oyun icin cekilmis olan kelime String olarak alinir
 *
 * 3) Kullanici tahminini yazdiktan sonra
 *
 *    CheckAnswerUpdateScore(kullanici girisi, cevap ciktisi, yeni skor)
 *
 *    ile cevap dogru mu yanlis mi belirlenir. Yeni skor parametre referansinda guncellenir.
 *    Dogru cevap string olarak parametre referansinda guncellenir
 *
 *
 * 4) 1. adimdan oyuna devam edilir.
 */

public class GameEngine {

    private WordModel currentWord;
    private GameMode  currentGameMode;

    private FirebaseService firebaseService;

    public GameEngine(GameMode gameMode) {

        this.currentGameMode = gameMode;
        this.firebaseService = new FirebaseService();
    }

    public char mapTurChar(char c) {

        switch(c) {
            case 'ç': return 'c';
            case 'Ç': return 'C';

            case 'ş': return 's';
            case 'Ş': return 'S';

            case 'ğ': return 'g';
            case 'Ğ': return 'G';

            case 'ı': return 'i';
            case 'I': return 'I';

            case 'i': return 'i';
            case 'İ': return 'I';

            case 'ö': return 'o';
            case 'Ö': return 'O';

            case 'ü': return 'u';
            case 'Ü': return 'U';
            default:
                return c;
        }
    }

    public String mapTurWord(String wordTur) {

        char[] letters = wordTur.toCharArray();
        char[] buffer = new char[wordTur.length()];

        for (int i = 0; i < wordTur.length(); i++) {
            buffer[i] = mapTurChar(letters[i]);
        }

        return String.valueOf(buffer);
    }

    public String GetCurrentWord() {

        switch (currentGameMode) {

        case Eng_Tur:
            return currentWord.getWording();

        case Tur_Eng:
            return currentWord.getWordtur();

        default:
            // nasil gelebilirki buraya?
            throw new RuntimeException("GameMode not implemented on switch case");

        }
    }

    public Chain CheckAnswerUpdateScore(String input, Ref<Boolean> answerCorrect, Ref<String> correctAnswer, Ref<ScoreModel> newScore) {

        String answer;

        switch (currentGameMode) {

        case Eng_Tur:
            answer = currentWord.getWordtur();
            break;

        case Tur_Eng:
            answer = currentWord.getWording();
            break;

        default:
            throw new RuntimeException("GameMode not implemented on switch case");
        }

        Chain future;


        boolean result = mapTurWord(answer).equalsIgnoreCase(mapTurWord(input));

        answerCorrect.set(result);

        if (result) {

            future = firebaseService.increaseWordScore(currentWord.id, newScore);

        } else {

            future = firebaseService.decreaseWordScore(currentWord.id, newScore);
        }

        correctAnswer.set(answer);

        return future;
    }

    public Chain NextGame() {

        final Ref<WordModel> out = new Ref<>(currentWord);

        Chain ret = firebaseService.fetchRandomWord(out);

        return ret.thenApply(new Runnable() {
            @Override
            public void run() {

                currentWord = out.get();
            }
        });
    }

}
