package cn.gotom.commons;

import java.util.EventListener;

public interface Listener<T> extends EventListener
{
	public abstract void onListener(Object sender, T e);
}
