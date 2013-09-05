package cn.gotom.beancoding;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class CodingModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Encoding.class), new EncodingInterceptor("GBK", "ISO8859-1"));
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Decoding.class), new DecodingInterceptor("ISO8859-1", "GBK"));
	}

}
