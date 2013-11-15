package cn.gotom.service.model;

import cn.gotom.pojos.Right;

public class RightChecked extends Right
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
