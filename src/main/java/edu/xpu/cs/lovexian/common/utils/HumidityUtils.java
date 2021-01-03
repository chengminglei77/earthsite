package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;

/**                                                                                ____________________
 _                _                                                           < 神兽护体，永无bug! >
 | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
 | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
 | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
 |___/|_|                |___/                                ||----w |
 ||     ||

/**
 * @author zhanganjie
 *
 */
public class HumidityUtils {
    private static final double [] humidity = new double[3];
    private static final String [] deep = new String[3];
    public static double[] humidityDecode(String message){
        for (int i=0;i<deep.length;i++){
            deep[i] = new BigInteger(message.substring(34+12*i, 40+12*i), 16).toString(10);
        }
       for (int i=0;i<3;i++){
               humidity[i] = 100-(Math.pow(Double.valueOf(deep[i]),0.25)/2)*10;
       }
       return humidity;
    }
}
