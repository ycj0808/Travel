package com.android.travel.test;

import android.test.AndroidTestCase;

import com.android.travel.db.UserService;
import com.android.travel.util.LogUtil;
import com.ycj.android.common.utils.CryptUtils;
/**
 * 数据库操作单元测试类
 * @author yangchj
 *
 */
public class DBTest extends AndroidTestCase {

	public void testLogin(){
		UserService userService=new UserService(this.getContext());
		boolean flag=userService.login("admin",CryptUtils.md5("123456"));
		LogUtil.i("登陆结果:"+flag);
	}
}
