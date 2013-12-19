package cn.gotom.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.util.Log;
import cn.gotom.annotation.Description;

public class ContextUtils
{

	/**
	 * 启动Activity
	 * 
	 * @param ctx
	 * @param claz
	 */
	@Description("启动Activity")
	public static void startActivity(Context ctx, Class<?> claz)
	{
		Intent intent = new Intent();
		intent.setClass(ctx.getApplicationContext(), claz);
		ctx.startActivity(intent);
	}

	@Description("Open URL")
	public static void openURL(Context ctx, Uri uri)
	{
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		// Uri url = Uri.parse("http://183.234.21.26:8080/hmew/");
		intent.setData(uri);
		ctx.startActivity(intent);
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param className
	 *            判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	@Description("用来判断服务是否运行")
	public static boolean isStartedService(Context context, Class<?> claz)
	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0))
		{
			return false;
		}
		for (RunningServiceInfo info : serviceList)
		{
			// Log.i(ServiceUtils.class.getName(), info.service.getClassName() + " started=" + info.started);
			if (claz.getName().equals(info.service.getClassName()) && info.started)
				return true;
		}
		return false;
	}

	/**
	 * 重启服务
	 * 
	 * @param context
	 * @param claz
	 */
	@Description("重启服务")
	public static void reStartService(Context context, Class<?> claz)
	{
		Intent service = new Intent(context, claz);
		if (isStartedService(context, claz))
		{
			Log.i(ContextUtils.class.getName(), "stopService " + claz.getName());
			context.stopService(service);
		}
		Log.i(ContextUtils.class.getName(), "startService " + claz.getName());
		context.startService(service);
	}

	@Description("停止服务")
	public static void stopService(Context context, Class<?> claz)
	{
		Intent service = new Intent(context, claz);
		if (isStartedService(context, claz))
		{
			Log.i(ContextUtils.class.getName(), "stopService " + claz.getName());
			context.stopService(service);
		}
	}

	@Description("绑定服务")
	public static void bindService(Context context, ServiceConnection conn, Class<?> claz)
	{
		try
		{
			unbindService(context, conn);
			context.getApplicationContext().bindService(new Intent(context, claz), conn, Context.BIND_AUTO_CREATE);
		}
		catch (Exception e)
		{
			Log.e(ContextUtils.class.getName(), "bindService " + e.getMessage());
		}
	}

	@Description("解绑服务")
	public static void unbindService(Context context, ServiceConnection conn)
	{
		try
		{
			context.getApplicationContext().unbindService(conn);
		}
		catch (Exception e)
		{
			Log.e(ContextUtils.class.getName(), "unbindService " + e.getMessage());
		}
	}
}
