package javax.comm;

public class PortInUseException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String currentOwner;

	PortInUseException(String owner)
	{
		super("Port currently owned by " + owner);

		this.currentOwner = owner;
	}
}
