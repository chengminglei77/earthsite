package edu.xpu.cs.lovexian.common.utils;

import java.math.BigInteger;

public class HexUtils {

    public static void main(String[] args) {
		/*System.out.println(hex16To10("0101"));
		System.out.println(hex16To10Str("0101"));
		System.out.println(hex10To16(257));
		System.out.println(hex10To16(257,2));
		*/
		System.out.println(String.format("%0"+1+"X",new BigInteger("10",10)));
	}
    
	/** 
	 * 16进制的数据变成字节数组
	 * 十六进制值为一个字节数组。网络传输之前用。
	 * 假设我有一个字符串“00A0BF”，我想把它解析为字节。[{0x00，0xA0，0xBf}
     * Convert hex string to byte[] 
     * @param hexString the hex string 
     * @return byte[] 
     */  
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            System.out.print(d[i]+" ");
        }

        return d;
    }
    
    /***
     * 16进制转换为10进制数字
     * @param strHex
     * @return
     */
    public static int hex16To10(String strHex){
    	BigInteger intNum = new BigInteger(strHex, 16);
    	return intNum.intValue();
    }
    
    /***
     * 16进制转换为10进制字符串
     * @param strHex
     * @return
     */
    public static String hex16To10Str(String strHex){
    	String hexStr= new BigInteger(strHex, 16).toString(10);
    	return hexStr;
    }
    
    /***
     * 10进制转化为16进制
     * @param valueTen
     * @return
     */
    public static String hex10To16(int valueTen){
    	return String.format("%X", valueTen);
    }
    
    /***
     * 10进制转化为16进制,规定返回的字符长度,不足长度前面补0。
     * @param valueTen
     * @param returnLength 返回的字符串总长度
     * @return
     */
    public static String hex10To16(int valueTen,int returnLength){
    	return String.format("%0"+returnLength+"X", valueTen);
    }
    
    /***
     * 10进制转化为16进制,规定返回的字符长度,不足长度前面补0。
     * @param valueTen
     * @param returnLength 返回的字符串总长度
     * @return
     */
    public static String hex10To16(String valueTen,int returnLength){
    	return String.format("%0"+returnLength+"X", new BigInteger(valueTen,10));
    }

	/**
     * 将16进制字符串转换为byte[]
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr);
        }
        return bytes;
    }
    
    /** 
     * Convert char to byte 
     * @param c char 
     * @return byte 
     */  
     private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    }

}
