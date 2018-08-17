package com.example.emirhan.iingilizce_ogren.abstraction.core;

/**
 * Created by emirhan on 12/20/2017.
 *
 */

public class Chain {

    public boolean finished = false;

    public static Chain runAsync(final Runnable function) {

        final Chain nextRing = new Chain();

        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (nextRing) {

                    function.run();
                    nextRing.finished = true;
                    nextRing.notify();
                }
            }
        }).start();

        return nextRing;
    }

    public Chain thenApply(final Runnable function) {

        final Chain nextRing = new Chain();
        final Chain prevRing = this;

        new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (prevRing) {
                    while (!prevRing.finished)
                        try {
                            prevRing.wait();
                        } catch (InterruptedException e) {}
                }

                synchronized (nextRing) {

                    function.run();
                    nextRing.finished = true;
                    nextRing.notify();
                }
            }
        }).start();

        return nextRing;
    }
}
