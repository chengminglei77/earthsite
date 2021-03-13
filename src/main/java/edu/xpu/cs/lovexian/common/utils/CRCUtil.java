package edu.xpu.cs.lovexian.common.utils;

/**
 * ____________________
 * _                _                                                           < 神兽护体，永无bug! >
 * | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
 * | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
 * | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 * |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
 * |___/|_|                |___/                                ||----w |
 * ||     ||
 *
 * @author huchengpeng
 */
public class CRCUtil {


    /**
     * CRC16_CCITT：多项式x16+x12+x5+1（0x1021），初始值0x0000，低位在前，高位在后，结果与0x0000异或
     * 0x8408是0x1021按位颠倒后的结果。
     *
     * @param buffer
     * @return
     */
    public static int CRC16_CCITT(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0x8408;
        for (byte b : buffer) {
            wCRCin ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((wCRCin & 0x0001) != 0) {
                    wCRCin >>= 1;
                    wCRCin ^= wCPoly;
                } else {
                    wCRCin >>= 1;
                }
            }
        }
        //wCRCin=(wCRCin<<8)|(wCRCin>>8);
        //wCRCin &= 0xffff;

        return wCRCin ^= 0x0000;

    }

    public static class arrayReverse {



        /*数组中元素位置进行交换*/

        static void reverse(int a[], int n)

        {

            int i, k, t;

            for (i = 0; i < n / 2; i++) {

                t = a[i];

                a[i] = a[n - i - 1];

                a[n - i - 1] = t;

            }



            System.out.println("\n反转数组是:");

            for (k = 0; k < n; k++) {

                System.out.println(a[k]);

            }

        }

    }
/*
    public static int CRC16_CCITT_FALSE(byte[] buffer) {
        int wCRCin = 0x0000;
        int wCPoly = 0x8408;
        for (byte b : buffer) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;
    }*/

    public static void main(String[] args) {
        byte[] buf = {0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a};//new StringBuilder("AA").reverse().toString().getBytes();
        System.out.println(Integer.toHexString(CRC16_CCITT(buf)).toUpperCase());

    }
}
