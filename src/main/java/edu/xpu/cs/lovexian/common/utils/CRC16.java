package edu.xpu.cs.lovexian.common.utils;

/**
 * 循环冗余检验：CRC-16-CCITT查表法
 */
public final class CRC16 {
    
    public static void main(String[] args) {

        String data = "AA551EA600020002";

        String crc = HexUtils.hex10To16(CRC16.CRC16_CCITT(HexUtils.hexStringToBytes(data)),4) ;
        System.out.println(crc);

    	/*int result2=CRC16.CRC16_CCITT(new byte[]{'A',5,5,1,'E','A',6,0,0,0,2,0,0,0,2});
    	System.out.println(String.format("0x%04x", result2));*/
    }

    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = "0x"+Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }
    public static int CRC16_CCITT(byte[] bytes) {
        int crc = 0x0000; // initial value
        int polynomial = 0x8408;// poly value reversed 0x1021; 0x8408
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            crc ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((crc & 0x00000001) != 0) {
                    crc >>= 1;
                    crc ^= polynomial;

                } else {
                    crc >>= 1;

                }
            }
        }
        System.out.println(crc);
        return crc;
    }
}