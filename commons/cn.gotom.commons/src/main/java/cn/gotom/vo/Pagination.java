package cn.gotom.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 分页类(从第一页开始)
 * 
 * @author <a href="mailto:pqixere@qq.com">裴绍国</a>
 * @version 2013-11-17
 */
public class Pagination<T> extends JsonResponse
{

	/**
	 * 页号
	 */
	private int pageNum = 1;

	/**
	 * 每页行数
	 */
	private int rowSize = 25;

	/**
	 * 总行数
	 */
	private int rowCount;

	/**
	 * 当前页数据
	 */
	private List<T> list;

	/**
	 * 总页数
	 */
	private int size;

	/**
	 * 是否有下一页
	 */
	private boolean next;

	/**
	 * 是否有上一页
	 */
	private boolean prev;

	/**
	 * 分页类
	 */
	public Pagination()
	{
		list = new ArrayList<T>();
	}

	public Pagination(int rowCount, List<T> list, int rowSize, int pageNum)
	{
		this.rowCount = rowCount;
		this.rowSize = rowSize;
		this.list = list;
		this.pageNum = pageNum;
	}

	public int size()
	{
		int totalSize = rowCount / rowSize;
		return rowCount % rowSize > 0 ? totalSize + 1 : totalSize;
	}

	public boolean getNext()
	{
		next = pageNum < size();
		return next;
	}

	public int getSize()
	{
		size = size();
		return size;
	}

	public boolean getPrev()
	{
		prev = pageNum > 1;
		return prev;
	}

	public int getPageNum()
	{
		return pageNum;
	}

	public int getRowSize()
	{
		return rowSize;
	}

	public int getRowCount()
	{
		return rowCount;
	}

	public List<T> getList()
	{
		return list;
	}

	public void setPageNum(int pageNum)
	{
		this.pageNum = pageNum;
	}

	public void setRowSize(int rowSize)
	{
		this.rowSize = rowSize;
	}

	public void setRowCount(int rowCount)
	{
		this.rowCount = rowCount;
	}

	public void setList(List<T> list)
	{
		this.list = list;
	}

}
