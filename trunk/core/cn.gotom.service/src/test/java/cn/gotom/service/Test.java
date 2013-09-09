package cn.gotom.service;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		byte b = 65;
		byte fcb = 0;
		fcb = (byte) (fcb << 6);
		if ((b & 64) == 64) {
			b  = (byte)(b & 0xFF);
		}
		b = (byte) (b | fcb);
	}

}
