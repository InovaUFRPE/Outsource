package com.outsource.inovaufrpe.usuario.utils;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//TODO: se tiver em desuso, excluir esta classe....

public class CardFormat {

    public static String dinheiroFormat(String dinheiro){
        DecimalFormat df = new DecimalFormat("####0.00");
        return "R$ "+ df.format(Float.parseFloat(dinheiro)).replace(".",",");
    }

    public static String dinheiroRefactor(String dinheiro){
        DecimalFormat df = new DecimalFormat("####0.00");
        return "R$ "+ df.format(Float.parseFloat(dinheiro)).replace(",",".");
    }

    public static String dataFormat(String data, String pattern){
        String nData = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            nData = dateFormat.format(dateFormat2.parse(data));

        }catch (ParseException e){
            e.printStackTrace();
        }
        return nData;

    }

    public static String tempoFormat(long tempo) {

        return DateUtils.getRelativeTimeSpanString(tempo).toString();

    }
}
