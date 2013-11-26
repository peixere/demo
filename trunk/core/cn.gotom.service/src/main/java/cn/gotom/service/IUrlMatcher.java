package cn.gotom.service;

import cn.gotom.matcher.UrlMatcher;
import cn.gotom.service.impl.IAntUrlPathMatcher;

import com.google.inject.ImplementedBy;

@ImplementedBy(IAntUrlPathMatcher.class)
public interface IUrlMatcher extends UrlMatcher
{

}
