package com.ahutjw.api;

import com.ahutjw.utils.D;

/**
 * 账户验证
 * 
 */
public class ApiValid {

	/**
	 * 验证用户
	 * 
	 * @param type
	 *            teacher 或者 student
	 * @param username
	 * @param pwd
	 * @return
	 */
	public static String validUser(String type, String username, String pwd) {
		String s = "";
		String u = "";
		if (type.equals("教师")) {
			s = "teacher";
			u = "zgh";
		} else if (type.equals("本科生")) {
			s = "student";
			u = "xh";
		}
		// 地址拼接
		String url = s + "Service.asmx/verifyUser";
		// 返回结果
		String result = "";
		try {
			String ret = ApiBaseHttp.doPost(url, new String[] { u, "pwd" },
					new String[] { username, pwd });
			System.out.println(">>>ret>>>" + ret);
			// 根据返回结果特征，处理数据
			result = ret;
		} catch (Exception e) {
			Api.analyseError(e);
			D.out(">>>validUser.error>>>" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
