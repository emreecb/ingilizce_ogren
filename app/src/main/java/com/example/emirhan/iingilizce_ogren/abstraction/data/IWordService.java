package com.example.emirhan.iingilizce_ogren.abstraction.data;

import com.example.emirhan.iingilizce_ogren.abstraction.core.Ref;

/**
 * Created by emirhan on 12/10/2017.
 *
 * Veritabani islemlerini soyutlamak ve tanimlamak icin olusturulmus olan arayuzdur
 *
 *
 * fetchRandomWord(cikti kelime referansi) ile kullaniciya gosterilecek olan kelime
 *           veritabanindan rastgele getirilir ve soru olarak sorulur.
 */

public interface IWordService<TResult, TWordModel> {

    TResult fetchRandomWord(Ref<TWordModel> out);
}