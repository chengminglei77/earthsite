package edu.xpu.cs.lovexian.common.utils;

import java.text.DecimalFormat;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 * 文件大小转换工具类
 *
 */
public class FileSizeUtil {

    /**
     * 根据文件大小转换为B、KB、MB、GB单位字符串显示
     * @param filesize 文件的大小（long型）
     * @return 返回 转换后带有单位的字符串
     */
    public static String GetLength(long filesize){
        String strFileSize = null;
        if(filesize < 1024){
            strFileSize = filesize+"B";
            return strFileSize;
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        if ((filesize >= 1024) && (filesize < 1024*1024)){
            strFileSize = df.format(((double)filesize)/1024)+"KB";
        }else if((filesize >= 1024*1024)&&(filesize < 1024*1024*1024)){
            strFileSize = df.format(((double)filesize)/(1024*1024))+"MB";
        }else{
            strFileSize = df.format(((double)filesize)/(1024*1024*1024))+"GB";
        }
        return strFileSize;
    }

    /**
     * @param filesize 文件的大小（long型）
     * @return 返回 转换后不带单位的字符串
     */
    public static String GetMBLength(long filesize){
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(((double)filesize)/(1024*1024));
    }
}
