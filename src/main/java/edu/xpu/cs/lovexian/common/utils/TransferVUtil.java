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
public class TransferVUtil {

    protected TransferVUtil() {

    }

    private static final double[] Vms = {12660, 12300, 12000, 11910, 11700, 11580, 11430, 11340, 11190, 11130, 11040,
            10980, 10950, 10890, 10830, 10800, 10650, 10500, 10380, 10200, 9990, 8100};

    private static final int[] Vmp = {100, 95, 90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25, 20, 15, 10, 5, 0};

    private static int currentP;

    public static int encrypt(float currentV) {
        if(currentV>=12600){
            return 100;
        } else if (currentV >= 8100) {
            for (int i = 0; i < 22; i++) {
                if (currentV >= Vms[i] && currentV < Vms[i - 1]) {
                    currentP = (int)Vmp[i - 1];
                }
            }
            return currentP;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(encrypt(11000));
    }
}
