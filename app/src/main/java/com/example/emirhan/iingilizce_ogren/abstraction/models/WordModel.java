package com.example.emirhan.iingilizce_ogren.abstraction.models;

import com.example.emirhan.iingilizce_ogren.abstraction.core.ICloneable;

/**
 * Created by emirhan on 12/10/2017.
 */

public class WordModel implements ICloneable<WordModel> {

    public int id;
    private String wordIng;
    private String wordTur;

    public WordModel() {

    }
    public WordModel(int id, String wordEng, String wordTur) {

        this.id = id;
        this.wordIng = wordEng;
        this.wordTur = wordTur;
    }

    public String getWordtur() {
        return this.wordTur;
    }
    public String getWording() {
        return this.wordIng;
    }
    public void setWordtur(String set) {
        this.wordTur = set;
    }
    public void setWording(String set) {
        this.wordIng = set;
    }

    public void clone(WordModel copyFrom) {

        // super.clone(copyFrom); allah belasini versin java gibi dilin

        this.id             = copyFrom.id;
        this.wordIng        = copyFrom.wordIng;
        this.wordTur        = copyFrom.wordTur;
    }
}
