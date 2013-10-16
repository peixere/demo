package javax.comm;

import java.util.EventObject;

public class ParallelPortEvent extends EventObject
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Deprecated
	public int eventType;
	private boolean oldValue;
	private boolean newValue;
	public static final int PAR_EV_ERROR = 1;
	public static final int PAR_EV_BUFFER = 2;

	public ParallelPortEvent(ParallelPort srcport, int eventtype, boolean oldvalue, boolean newvalue)
	{
		super(srcport);

		this.eventType = eventtype;
		this.oldValue = oldvalue;
		this.newValue = newvalue;
	}

	public int getEventType()
	{
		return this.eventType;
	}

	public boolean getNewValue()
	{
		return this.newValue;
	}

	public boolean getOldValue()
	{
		return this.oldValue;
	}
}