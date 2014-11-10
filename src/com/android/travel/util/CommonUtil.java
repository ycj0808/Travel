package com.android.travel.util;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

public class CommonUtil {

	
	/**
	 * 获得随机的GUID
	 * 
	 * @return 返回GUID
	 */
	public static String getGUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 产生n位伪随机数(n最大值为128)
	 * 
	 * @param strLength
	 * @return
	 */
	public String getFixLenthString(int n) {
		String res = "NaN";
		if (n < 129) {
			n++;
			Random rm = new Random();
			BigDecimal pross = new BigDecimal((1 + rm.nextDouble())
					* Math.pow(10, n));
			String fixLenthString = String.valueOf(pross);
			res = fixLenthString.substring(2, n + 1);
		}
		return res;
	}
}
