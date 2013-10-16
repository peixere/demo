package cn.gotom.commons;

public abstract class Sleep
{	
	private static long lastTimeMillis;
	public static void sleep(long timeMillis){
		lastTimeMillis = System.currentTimeMillis();
		while (System.currentTimeMillis() - lastTimeMillis < timeMillis)
		{
			;
		}
	}	
}
