package com.example.emirhan.iingilizce_ogren.abstraction.game;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by emirhan on 12/20/2017.
 */
public class GameEngineTest {

    @Test
    public void mapTurChar() {

        GameEngine engine = new GameEngine(GameMode.Eng_Tur);

        char[] turkish = new char[]{'Ç', 'Ş', 'Ğ', 'I', 'İ', 'Ö', 'Ü'};
        char[] turkish_lower = new char[]{'ç', 'ş', 'ğ', 'ı', 'i', 'ö', 'ü'};

        char[] mapped  = new char[]{'C', 'S', 'G', 'I', 'I', 'O', 'U'};
        char[] mapped_lower  = new char[]{'c', 's', 'g', 'i', 'i', 'o', 'u'};

        for(int i = 0; i < turkish.length; i++) {
            turkish[i] = engine.mapTurChar(turkish[i]);
            turkish_lower[i] = engine.mapTurChar(turkish_lower[i]);
        }

        assertEquals(String.valueOf(mapped), String.valueOf(turkish));
        assertEquals(String.valueOf(mapped_lower), String.valueOf(turkish_lower));
    }
    @Test
    public void mapTurWord() {

        GameEngine engine = new GameEngine(GameMode.Eng_Tur);

        assertEquals(
                "Cilek",
                engine.mapTurWord("Çilek")
        );

        assertEquals(
                "Kopek",
                engine.mapTurWord("Köpek")
        );

        assertEquals(
                "Fistikcisahap",
                engine.mapTurWord("Fıstıkçışahap")
        );

        assertEquals(
                "Pijamali hasta yagiz sofore cabucak guvendi",
                engine.mapTurWord("Pijamalı hasta yağız şoföre çabucak güvendi")
        );
    }

}