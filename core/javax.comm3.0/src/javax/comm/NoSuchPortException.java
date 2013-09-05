package javax.comm;

@SuppressWarnings("serial")
public class NoSuchPortException extends Exception
{
	NoSuchPortException(String str)
	{
		super(str);
	}

	NoSuchPortException()
	{
	}
}