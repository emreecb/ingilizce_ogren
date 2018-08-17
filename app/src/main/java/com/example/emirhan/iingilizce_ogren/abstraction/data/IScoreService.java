package com.example.emirhan.iingilizce_ogren.abstraction.data;

import com.example.emirhan.iingilizce_ogren.abstraction.core.Ref;
/**
 * Created by emirhan on 12/16/2017.
 *
 *
 * Kelime programinda skor icin ihtiyacimiz olan 2 temel sorgu ve islem var
 *
 * Birincisi increaseWordScore(kelime id) ile kullanicinin iligli kelime ile olan pauni arttirilir
 * Ikincisi  decreaseWordScore(kelime id) ile kullanicinin ilgili kelime ile olan puani azaltilir
 *
 */

public interface IScoreService<TResult, TScoreModel> {

    TResult increaseWordScore(int wordId, Ref<TScoreModel> out);
    TResult decreaseWordScore(int wordId, Ref<TScoreModel> out);
}
