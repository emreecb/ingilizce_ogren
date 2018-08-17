package com.example.emirhan.iingilizce_ogren.abstraction.core;

/**
 * Created by emirhan on 12/10/2017.
 *
 * Referans verilen objenin alanlarinin guncellenebilme garantisi icin gerekli arayuz
 */

public interface ICloneable<T> {

    void clone(T cloneFrom);
}
