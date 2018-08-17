package com.example.emirhan.iingilizce_ogren.abstraction.models;

import com.example.emirhan.iingilizce_ogren.abstraction.core.ICloneable;

/**
 * Created by emirhan on 12/16/2017.
 *
 * Kullanicilarin kelimeler uzerinde olan dogru yanlis istatistigini temsil eden veri modeli
 */

public class ScoreModel implements ICloneable<ScoreModel> {

    public int wordId;
    public String userId;

    public int correctCount;
    public int incorrectCount;

    public ScoreModel() {

    }

    public ScoreModel(int wordId, String userId) {

        this(wordId, userId, 0, 0);
    }

    public ScoreModel(int wordId, String userId, int correctCount, int incorrectCount) {

        this.wordId = wordId;
        this.userId = userId;

        this.correctCount   = correctCount;
        this.incorrectCount = incorrectCount;
    }

    public void clone(ScoreModel cloneFrom) {

        this.userId = cloneFrom.userId;
        this.wordId = cloneFrom.wordId;

        this.correctCount   = cloneFrom.correctCount;
        this.incorrectCount = cloneFrom.incorrectCount;
    }
}
