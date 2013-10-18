package cn.gotom.service;

import cn.gotom.service.impl.IAntUrlPathMatcher;
import cn.gotom.util.UrlMatcher;

import com.google.inject.ImplementedBy;

@ImplementedBy(IAntUrlPathMatcher.class)
public interface IUrlMatcher extends UrlMatcher
{

}
