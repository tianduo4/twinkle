package com.twinkle.common.authentication.credentials;

import com.twinkle.common.authentication.util.EncryptPasswordUtil;
import com.twinkle.common.authentication.token.NotPasswordToken;
import lombok.Data;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.util.ByteSource;

@Data
public class SimpleCredentialsMatcherWapper extends SimpleCredentialsMatcher {

	private String hashAlgorithm;

	@Override
	protected Object getCredentials(AuthenticationInfo info) {
		Object credentials = info.getCredentials();// 数据库密码
		return credentials;
	}

	@SuppressWarnings("restriction")
	private String encrypt(AuthenticationToken token, AuthenticationInfo info) {
		if(token instanceof NotPasswordToken){
			return null;
		}

		ByteSource salt = null;
		if (info instanceof SaltedAuthenticationInfo) {
			salt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
		}

		String password = String.valueOf(((UsernamePasswordToken) token).getPassword());
		try {
			return EncryptPasswordUtil.encryptPassword(password, new String(salt.getBytes()));
		} catch (Exception e) {
			throw new UnknownAccountException("用户名或密码错误。");
		}
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		if(token instanceof NotPasswordToken){
			return Boolean.TRUE;
		}
		Object tokenHashedCredentials = encrypt(token, info);
		Object accountCredentials = getCredentials(info);
		return equals(tokenHashedCredentials, accountCredentials);
	}
}
