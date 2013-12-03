package cn.gotom.service.model;

import javax.persistence.Transient;

import cn.gotom.pojos.Right;

public class RightTree extends Right
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Transient
	private boolean expanded;

	@Transient
	private java.util.List<RightTree> children;

	public boolean isExpanded()
	{
		return expanded;
	}

	public void setExpanded(boolean expanded)
	{
		this.expanded = expanded;
	}

	public java.util.List<RightTree> getChildren()
	{
		return children;
	}

	public void setChildren(java.util.List<RightTree> children)
	{
		this.children = children;
	}

}
