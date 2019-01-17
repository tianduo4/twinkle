package com.twinkle.common.authentication.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * description: 密码加密辅助类
 *
 * @author ：King
 * @date ：2019/1/12 23:28
 */
public class EncryptPasswordUtil {

	private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
	private static String algorithmName = "md5";
	private static int hashIterations = 2;
	
	/**
	 * description: 密码加密算法
	 * created by King on 2019/1/17 22:59.
	 * @Param: pwd
	 * @return java.lang.String[]
	 */
	public static String[] encryptPassword(String pwd) {
		String salt = randomNumberGenerator.nextBytes().toHex();// 32位字符
		String newPwd = new SimpleHash(algorithmName, pwd, ByteSource.Util.bytes(salt), hashIterations).toHex();
		String[] values = new String[2];
		values[0] = newPwd;
		values[1] = salt;
		return values;
	}

	/**
	 * description: 密码加密算法
	 * created by King on 2019/1/17 22:59.
	 * @Param: pwd
	 * @Param: salt
	 * @return java.lang.String
	 */
	public static String encryptPassword(String pwd, String salt) {
		String newPwd = new SimpleHash(algorithmName, pwd, ByteSource.Util.bytes(salt), hashIterations).toHex();
		return newPwd;
	}

}
