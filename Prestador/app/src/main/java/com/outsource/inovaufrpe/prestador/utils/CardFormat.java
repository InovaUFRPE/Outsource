package com.outsource.inovaufrpe.prestador.utils;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Heitor on 29/12/2017.
 */

public class CardFormat {

    public static String dinheiroFormat(String dinheiro){
        DecimalFormat df = new DecimalFormat("####0.00");
        if(dinheiro.isEmpty()){
            return "R$ "+ df.format(0.0).replace(".",",");
        }
        return "R$ "+ df.format(Float.parseFloat(dinheiro)).replace(".",",");
    }

    public static String dinheiroRefactor(String dinheiro){
        DecimalFormat df = new DecimalFormat("####0.00");
        String din = dinheiro.replace(",",".").trim();
        return df.format(Float.parseFloat(din));
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

    public static StringBuilder dataFormatMes(String data, String pattern){
        String nData;
        StringBuilder nData2 = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            nData = dateFormat.format(dateFormat2.parse(data));
            nData2 = new StringBuilder(nData);
            nData2.delete(3,5);
            nData2.append(Utils.meses(Integer.valueOf(nData.substring(3))));

        }catch (ParseException e){
            e.printStackTrace();
        }
        return nData2;
    }

    public static String tempoFormat(long tempo) {
        String r;
        if (DateUtils.getRelativeTimeSpanString(tempo).toString().substring(0,1).equals("0")) {
            r = "agora";
        } else {
            r = DateUtils.getRelativeTimeSpanString(tempo).toString();
        }
        return r;
    }
}
