package edu.xpu.cs.lovexian.common.utils;

/**
 * 循环冗余检验：CRC-16-CCITT查表法
 */
public final class CRC16 {
    
    public static void main(String[] args) {
        //short result=CRC16.GetCrc16(new byte[]{1,2,3});
//    	short result=CRC16.GetCrc16(new byte[]{0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a});
//        System.out.println(Integer.toHexString(result));
//        System.out.println(String.format("0x%04x", result));
    	int result2=CRC16.CRC16_CCITT(new byte[]{0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a});
    	//System.out.println(result2);
    	System.out.println(String.format("0x%04x", result2));
    	//System.out.println(HexUtils.hex10To16(result2, 4));
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