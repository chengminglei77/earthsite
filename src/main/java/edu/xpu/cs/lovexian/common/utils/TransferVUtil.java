package edu.xpu.cs.lovexian.common.utils;

/**                                                                                ____________________
      _                _                                                           < 神兽护体，永无bug! >
    | |__  _   _  ___| |__   ___ _ __   __ _ _ __   ___ _ __   __ _                --------------------
   | '_ \| | | |/ __| '_ \ / _ \ '_ \ / _` | '_ \ / _ \ '_ \ / _` |                       \   ^__^
  | | | | |_| | (__| | | |  __/ | | | (_| | |_) |  __/ | | | (_| |                        \  (oo)\_______
 |_| |_|\__,_|\___|_| |_|\___|_| |_|\__, | .__/ \___|_| |_|\__, |                           (__)\       )\/\
                                   |___/|_|                |___/                                ||----w |
                                                                                                ||     ||
 * @author huchengpeng
 */
public class TransferVUtil {

	protected TransferVUtil(){

	}

	private static final double[] Vms={12600,12500,12460,12320,12270,12200,12110,12000,11250,11000,10400};

	private static final int[] Steps={4,8,14,10,14,18,22,30,50,120,220};

	private static final int[] Vmp={100,75,70,60,55,50,45,40,15,10,5};

	private static int currentP;

	public static int encrypt(float currentV) {
		if(currentV<=12600&&currentV>=9300) {
			for (int i = 0; i < 11; i++) {
				if (currentV >= Vms[i] && currentV < Vms[i - 1]) {
					currentP = (int) (Vmp[i - 1] - ((Vms[i - 1] - currentV) / Steps[i - 1]));
				}
			}
			return currentP;
		}
		return -1;
	}
	public static void main(String[] args) {
		System.out.println(encrypt(12212));
	}
}
