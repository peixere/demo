package cn.gotom.comm.server;

import java.io.IOException;

import cn.gotom.annotation.Description;
import cn.gotom.comm.channel.Channel;

/**
 * 服务接口
 * 
 * @author <a href="mailto:pqixere@qq.com">裴绍国</a>
 * @version 2013-10-16
 */
@Description("服务接口")
public interface Server extends Channel
{
	boolean started();

	@Description("连接通道")
	void start() throws IOException;

	@Description("关闭")
	void stop();
}