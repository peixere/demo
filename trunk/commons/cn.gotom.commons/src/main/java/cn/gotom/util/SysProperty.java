package cn.gotom.util;


public abstract class SysProperty
{

	public final static SystemType os = SystemType.current();
	public final static String os_arch = System.getProperty("os.arch");
	public final static String arch_data_model = System.getProperty("sun.arch.data.model");
	public final static String java_home = System.getProperty("java.home");
	public final static String class_path = System.getProperty("java.class.path");
	public final static String user_home = System.getProperty("user.home");
	public final static String file_separator = System.getProperty("file.separator");

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println(os);
		System.out.println(os_arch);
		System.out.println(arch_data_model);
		System.out.println(java_home);
		System.out.println(class_path);
		System.out.println(user_home);
		System.out.println(file_separator);
		// Map<?, ?> map = System.getProperties();
		// for (Object key : map.keySet())
		// {
		// System.out.println(key + "=" + map.get(key));
		// }
	}
}
