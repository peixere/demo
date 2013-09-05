package cn.gotom.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.log4j.Logger;

import cn.gotom.annotation.Description;

public abstract class Converter
{
	protected static Logger log = Logger.getLogger(Converter.class.getName());

	public static void main(String[] args)
	{
		test();
	}

	public static void test()
	{
		try
		{
			byte[] bf = toBytes("ff fe 01 02");
			log.info("toHexString：" + toHexString(bf));
			log.info("Integer.parseInt：" + Integer.parseInt(toHexString(new byte[] { 0, 0, 01, 02 }).replace(" ", ""), 16));
			log.info("Integer.parseInt：" + Integer.parseInt(toHexString(new byte[] { 0, 0, (byte) 0xFF, (byte) 0xFF }).replace(" ", ""), 16));
			log.info("Converter.ToInt32：" + ToInt32(new byte[] { 01, 02, 0, 0 }, 0));
			int ia = (int) (Math.random() * (Math.random() * 100000));
			int ib = Converter.ToInt32(Converter.GetBytes(ia), 0);
			log.info("Converter.ToInt32：" + Converter.toHexString(Converter.GetBytes(ia)) + " & " + ia + "==" + ib + " " + (ia == ib));
			short sa = (short) (Math.random() * (Math.random() * 10000));
			short sb = Converter.ToInt16(Converter.GetBytes(sa), 0);
			log.info("Converter.ToInt16：" + Converter.toHexString(Converter.GetBytes(sa)) + " & " + sa + "==" + sb + " " + (sa == sb));
			long la = (long) (Math.random() * (Math.random() * 10000000));
			long lb = Converter.ToInt64(Converter.GetBytes(la), 0);
			log.info("Converter.ToInt64：" + Converter.toHexString(Converter.GetBytes(la)) + " & " + la + "==" + lb + " " + (la == lb));
			String tmp = "中文ABC";
			byte[] bytes = GetBytes(tmp);
			log.info("Converter：" + tmp + "=" + Converter.toHexString(bytes));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}

	public static String Encoding = "GBK";

	// public final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public final static String dateFormat = "yyyy-MM-dd";

	public final static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

	public static byte[] readFile(String fileName)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(fileName);
			int bytesRead = 0;
			int offSet = 0;
			byte[] buffer = new byte[100];
			while ((bytesRead = fis.read(buffer, offSet, buffer.length)) > 0)
			{
				baos.write(buffer, 0, bytesRead);
			}
			buffer = baos.toByteArray();
			fis.close();
			baos.close();
			return buffer;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return new byte[0];
	}

	@Description("16进制字符串转为byte数组 fe f0 ff")
	public static byte[] toBytes(String hexString)
	{
		String[] tmps = hexString.split(" ");
		ByteList list = new ByteList();
		for (String tmp : tmps)
		{
			if (tmp.trim().length() == 2)
			{
				list.add(Integer.valueOf(tmp, 16).byteValue());
			}
		}
		return list.ToArray();
	}

	@Description("byte数组转为16进制字符串")
	public static String toHexString(byte[] bytes)
	{
		return toHexString(bytes, 0, bytes.length);
	}

	@Description("byte数组转为16进制字符串")
	public static String toHexString(byte[] bytes, int startIndex, int length)
	{
		if (bytes == null || bytes.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		String tmp = "";
		for (int i = startIndex; i < length; i++)
		{
			tmp = Integer.toHexString(bytes[i]).toUpperCase(Locale.getDefault());
			if (tmp.length() > 2)
			{
				tmp = tmp.substring(tmp.length() - 2, tmp.length());
			}
			else if (tmp.length() < 2)
			{
				tmp = "0" + tmp;
			}
			sb.append(" " + tmp);
		}
		return sb.toString();
	}

	/**
	 * 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248 转全角的函数(SBC case)
	 * 
	 * @param input
	 *            任意字符串
	 * @return 全角字符串
	 */
	public static String ToSBC(String input)
	{
		/**
		 * 半角转全角：
		 */
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == 32)
			{
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	/**
	 * 转半角的函数(DBC case) 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
	 * 
	 * @param input
	 *            任意字符串
	 * @return 半角字符串
	 */
	public static String ToDBC(String input)
	{
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++)
		{
			if (c[i] == 12288)
			{
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static byte[] GetBytes(byte[] bytes)
	{
		byte[] bf = new byte[bytes.length];
		for (int i = 0; i < bf.length; i++)
		{
			bf[i] = bytes[i];
		}
		return bf;
	}

	public static byte[] GetBytes(short num)
	{
		return GetBytes(num, 2);
	}

	public static byte[] GetBytes(int num)
	{
		return GetBytes(num, 4);
	}

	public static byte[] GetBytes(long num)
	{
		return GetBytes(num, 8);
	}

	/**
	 * 低位在前高位在后
	 * 
	 * @param num
	 * @param length
	 * @return
	 */
	public static byte[] GetBytes(long num, int length)
	{
		byte[] bytes = new byte[length];
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = (byte) (num >> (i * 8));
		}
		return bytes;
	}

	/**
	 * 低位在前高位在后 默认方式
	 * 
	 * @param num
	 * @param length
	 * @return
	 */
	private static long ToInt64(byte[] bytes, int index, int length)
	{
		long num = 0;
		for (int i = 0; i < length; i++)
		{
			long l = (bytes[index + i]) & 0xFF;
			num |= (l << (i * 8));
		}
		return num;
	}

	/**
	 * 高位在前低位在后
	 * 
	 * @param bytes
	 * @param index
	 * @param length
	 * @return
	 */
	public static long toInt64(byte[] bytes, int index, int length)
	{
		long num = 0;
		for (int i = 0; i < length; i++)
		{
			long l = (bytes[index + i]) & 0xFF;
			num |= (l << ((length - 1 - i) * 8));
		}
		return num;
	}

	/**
	 * 高位在前低位在后
	 * 
	 * @param num
	 * @param length
	 * @return
	 */
	public static byte[] getBytes(long num, int length)
	{
		byte[] b = new byte[length];
		for (int i = 0; i < b.length; i++)
		{
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((num >>> offset) & 0xFF);
		}
		return b;
	}

	public static short ToInt16(byte[] bytes, int index)
	{
		return (short) ToInt64(bytes, index, 2);
	}

	public static int ToInt32(byte[] bytes, int index)
	{
		return (int) ToInt64(bytes, index, 4);
	}

	public static long ToInt64(byte[] bytes, int index)
	{
		return ToInt64(bytes, index, 8);
	}

	public static byte[] getBytes(String name)
	{
		try
		{
			return name.getBytes(Encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			log.error(e.getMessage(), e);
			return new byte[0];
		}
	}

	public static byte[] GetBytes(String name) throws UnsupportedEncodingException
	{
		return name.getBytes(Encoding);
	}

	public static byte[] GetBytes(String name, int len) throws UnsupportedEncodingException
	{
		byte[] buffer = name.getBytes(Encoding);
		if (buffer.length > 255)
		{
			ByteList bl = new ByteList();
			bl.addRange(buffer);
			bl.RemoveRange(255, buffer.length - 255);
			buffer = bl.ToArray();
		}
		return buffer;
	}

	public static String toString(byte[] bytes)
	{
		try
		{
			return new String(bytes, Encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			log.error(e.getMessage(), e);
			return "";
		}
	}

	public static byte[] asByteArray(UUID uuid)
	{
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		byte[] buffer = new byte[16];
		for (int i = 0; i < 8; i++)
		{
			buffer[i] = (byte) (msb >>> 8 * (7 - i));
		}
		for (int i = 8; i < 16; i++)
		{
			buffer[i] = (byte) (lsb >>> 8 * (7 - i));
		}
		return buffer;
	}

	public static UUID toUUID(byte[] buffer)
	{
		return toUUID(buffer, 0);
	}

	public static UUID toUUID(byte[] buffer, int startIndex)
	{
		long msb = 0;
		long lsb = 0;
		for (int i = startIndex; i < startIndex + 8; i++)
			msb = (msb << 8) | (buffer[i] & 0xff);
		for (int i = startIndex + 8; i < startIndex + 16; i++)
			lsb = (lsb << 8) | (buffer[i] & 0xff);
		UUID result = new UUID(msb, lsb);
		return result;
	}

	public static Date parse(String times)
	{
		try
		{
			return new SimpleDateFormat(dateTimeFormat, Locale.getDefault()).parse(times);
		}
		catch (ParseException e)
		{
			log.error(e.getMessage(), e);
			return new Date();
		}
	}

	public static Date parse(String times, String format)
	{
		try
		{
			return new SimpleDateFormat(format, Locale.getDefault()).parse(times);
		}
		catch (ParseException e)
		{
			log.error(e.getMessage(), e);
			return new Date();
		}
	}

	public static String format(Date date)
	{
		return new SimpleDateFormat(dateTimeFormat, Locale.getDefault()).format(date);
	}

	public static String format(Date date, String format)
	{
		return new SimpleDateFormat(format, Locale.getDefault()).format(date);
	}

	public static int parseInt(String text)
	{
		try
		{
			return Integer.parseInt(text);
		}
		catch (Exception ex)
		{
			return 0;
		}
	}
}
