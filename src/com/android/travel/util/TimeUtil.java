package com.android.travel.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	private static final String DEFAULT_DATE_FORMAT = "yyyy年MM月dd日 HH:mm:ss";
	/**
	 * 获取日期时间
	 * @param time
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	public static String getFormatTime(long time){
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return sdf.format(new Date(time));
	}
}
