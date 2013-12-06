package cn.gotom.sso.util;

public interface PasswordEncoder
{

	public abstract String encode(String password);

}