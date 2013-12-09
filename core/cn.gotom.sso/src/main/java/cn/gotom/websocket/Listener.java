package cn.gotom.websocket;

import java.util.EventListener;

public interface Listener<T, M> extends EventListener
{
	public abstract void onListener(T sender, M msg);
}
