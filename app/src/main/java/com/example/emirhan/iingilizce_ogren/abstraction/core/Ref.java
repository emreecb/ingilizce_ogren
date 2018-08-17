package com.example.emirhan.iingilizce_ogren.abstraction.core;

/**
 * Created by emirhan on 12/10/2017.
 */

public class Ref<T> {

    T obj;
    public Ref(T obj) {
        this.obj = obj;
    }

    public T get() {
        return obj;
    }

    public void set(T obj) {
        this.obj = obj;
    }

}
