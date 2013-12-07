package cn.gotom.sso.util;

public interface PasswordEncoder
{

	public abstract String encode(String password);

	public abstract void setCharacterEncoding(String characterEncoding);

	public abstract void setEncodingAlgorithm(String encodingAlgorithm);

}