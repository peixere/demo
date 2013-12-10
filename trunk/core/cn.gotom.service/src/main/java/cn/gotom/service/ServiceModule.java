package cn.gotom.service;

import cn.gotom.matcher.UrlMatcher;
import cn.gotom.matcher.UrlMatcherAnt;
import cn.gotom.util.PasswordEncoder;
import cn.gotom.util.PasswordEncoderMessageDigest;

import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		bind(UrlMatcher.class).to(UrlMatcherAnt.class).asEagerSingleton();
		bind(PasswordEncoder.class).to(PasswordEncoderMessageDigest.class).asEagerSingleton();
	}

}
