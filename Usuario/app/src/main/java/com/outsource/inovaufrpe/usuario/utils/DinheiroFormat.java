package com.outsource.inovaufrpe.usuario.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Heitor on 05/12/2017.
 */

public class DinheiroFormat {

    /*public String format(String dinheiro){
        DecimalFormat df = new DecimalFormat("####0.00");
        return "R$ "+ df.format(Float.parseFloat(dinheiro)).replace(".",",");
    }

    public String dataFormat(String data){
        String nData = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            nData = dateFormat.format(dateFormat2.parse(data));

        }catch (ParseException e){
            e.printStackTrace();
        }
        return nData;

    }*/
}
