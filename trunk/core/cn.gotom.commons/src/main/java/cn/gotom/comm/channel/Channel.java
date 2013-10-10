package cn.gotom.comm.channel;

import java.io.IOException;

import cn.gotom.annotation.Description;
import cn.gotom.commons.Listener;

@Description("通道接口")
public interface Channel extends java.io.Serializable
{

	@Description("获取通道参数")
	Parameters getParameters();
	
	@Description("设置通道参数")
	void setParameters(Parameters parameters);

	@Description("设置通道参数")
	void setParameters(String... parameters);

	@Description("通道Id")
	String getId();

	boolean connected();

	@Description("连接通道")
	void connect() throws IOException;

	@Description("读数据")
	void addReceiveListener(Listener<byte[]> listener);

	@Description("读数据")
	void removeReceiveListener(Listener<byte[]> listener);

	@Description("写数据到通道")
	void write(byte[] bytes) throws IOException;

	@Description("关闭")
	void close();

	@Description("添加通道事件监")
	void addStateListener(Listener<State> stateListener);

	@Description("删除通道事件监")
	void removeStateListener(Listener<State> stateListener);

	State getState();

}