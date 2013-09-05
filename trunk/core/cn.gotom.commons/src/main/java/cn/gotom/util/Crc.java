package cn.gotom.util;

import cn.gotom.annotation.Description;

public class Crc
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

	@Description("CRC16")
	public static short create16(byte[] bytes)
	{
		return create16(bytes, 0, bytes.length);
	}

	public static short create16(byte[] bytes, int startIndex, int length)
	{
		int crc = 0xffff;
		for (int i = startIndex; i < length; i++)
		{
			for (int b = 128; b != 0; b >>= 1)
			{
				if ((crc & 0x8000) != 0)
				{
					crc <<= 1;
					crc ^= 0x1021;
				}
				else
				{
					crc <<= 1;
				}
				if ((bytes[i] & b) != 0)
				{
					crc ^= 0x1021;
				}
			}
		}
		return (short) crc;
	}

}
