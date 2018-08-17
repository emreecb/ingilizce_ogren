package com.example.emirhan.iingilizce_ogren.abstraction.core;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by emirhan on 12/20/2017.
 */
public class ChainTest {
    @Test
    public void runAsync() throws Exception {

    }

    @Test
    public void thenApply() throws Exception {

        final List<Integer> buffer = new ArrayList<Integer>();

        buffer.add(0);

        Chain.runAsync(new Runnable() {
            @Override
            public void run() {
                buffer.add(1);
            }
        }).thenApply(new Runnable() {
            @Override
            public void run() {
                buffer.add(2);
            }
        }).thenApply(new Runnable() {

            @Override
            public void run() {
                buffer.add(3);

            }
        });

        Thread.sleep(2000);

        assertArrayEquals(
                new Integer[] {0, 1, 2, 3},
                buffer.toArray(new Integer[buffer.size()])
        );
    }

}