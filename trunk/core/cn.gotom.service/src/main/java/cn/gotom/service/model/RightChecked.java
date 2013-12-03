package cn.gotom.service.model;


public class RightChecked extends RightTree
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean checked;

	public boolean isChecked()
	{
		return checked;
	}

	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}

}
