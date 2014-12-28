package cn.gotom.web.action;

import java.util.ArrayList;
import java.util.List;

import cn.gotom.pojos.User;
import cn.gotom.sso.TicketImpl;
import cn.gotom.sso.util.GsonUtils;

public class Test
{

	public static void main(String[] agrs)
	{
		// List<TicketImpl> userList = new ArrayList<TicketImpl>();
		// TicketImpl user = new TicketImpl("");
		// userList.add(user);
		// user = new TicketImpl("");
		// userList.add(user);
		// String json = GsonUtils.toJson(userList, GsonUtils.dateFormat);
		// System.out.println(json);
		// User[] users = GsonUtils.toArray(User[].class, json);
		// System.out.println(users.length);
		new Test().init();
	}

	void init()
	{
		final Business bus = new Business();// 声明一个对象
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				// sub thread
				for (int i = 0; i < 10000; i++)
				{
					bus.work(1000);
				}
			}
		}).start();
		// main thread sub每执行一次main执行三次
		for (int i = 0; i < 10000; i++)
		{
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bus.set();
		}
	}

	// 内部类
	class Business
	{
		public synchronized void work(long ms)
		{
			try
			{
				bShoud = true;
				System.out.println("work wait");
				this.wait(ms);
				System.out.println("work notify " + bShoud);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		public synchronized void set()
		{
			bShoud = false;
			System.out.println("work set");
			this.notify();
			System.out.println("set notify");
		}

		boolean bShoud = true; // 线程控制变量

		public synchronized void sub(int i)
		{ // 线程互斥
			while (!bShoud)
			{ // 不能用if 容易产生伪唤醒
				try
				{
					this.wait(); // 当前线程进入等待状态
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			for (int j = 0; j < 1; j++)
			{
				System.out.println("sub thread sequece of " + i + " loop of " + j);
			}
			bShoud = false;
			this.notify(); // 唤醒当前对象正在等待的线程
		}

		public synchronized void main(int i)
		{ // 线程互斥
			while (bShoud)
			{
				try
				{
					this.notify();
					this.wait(); // 当前线程进入等待状态
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			for (int j = 0; j < 3; j++)
			{
				System.out.println("main thread sequece of " + i + " loop of " + j);
			}
			bShoud = true;
			this.notify(); // 唤醒当前对象正在等待的线程
		}
	}
}
