package cn.gotom.vo;

import java.util.ArrayList;
import java.util.List;

import cn.gotom.annotation.Description;

/**
 * 
 * 分页类(从第一页开始)
 * 
 * @author <a href="mailto:pqixere@qq.com">裴绍国</a>
 * @version 2013-11-17
 */
@Description("分页类(从第一页开始)")
public class Pagination<T> extends JsonResponse
{

	@Description("页号,从1开始")
	private int pageNum = 1;
	@Description("每页行数")
	private int pageSize = 25;
	@Description("总行数")
	private int total;
	@Description("当前页数据")
	private List<T> list;
	@Description("总页数")
	private int size;
	@Description("是否有下一页")
	private boolean next;
	@Description("是否有上一页")
	private boolean prev;
	private int start;
	private int limit;

	@Description("分页")
	public Pagination()
	{
		list = new ArrayList<T>();
	}

	/**
	 * 分页实例化
	 * 
	 * @param total
	 *            总行数
	 * @param list
	 *            当前页数据
	 * @param pageSize
	 *            每页行数
	 * @param pageNum
	 *            当前页码
	 */
	public Pagination(int total, List<T> list, int pageSize, int pageNum)
	{
		this.total = total;
		this.pageSize = pageSize;
		this.list = list;
		this.pageNum = pageNum;
		size();
		getPrev();
		getNext();
		this.start = (pageNum - 1) * pageSize;
		this.start = pageNum * pageSize;
	}

	/**
	 * 分页实例化
	 * 
	 * @param list
	 *            当前页数据
	 * @param total
	 *            总行数
	 * @param start
	 *            开始行 从0开始
	 * @param limit
	 *            结束行
	 */
	public Pagination(List<T> list, int total, int start, int limit)
	{
		this.total = total;
		this.pageSize = (limit - start) + 1;
		this.list = list;
		this.pageNum = (limit < total) ? limit / pageSize : (total / limit);
		this.pageNum = this.pageNum + 1;
		size();
		getPrev();
		getNext();
		this.start = (pageNum - 1) * pageSize;
		this.start = pageNum * pageSize;
	}

	private int size()
	{
		int totalSize = total / pageSize;
		return total % pageSize > 0 ? totalSize + 1 : totalSize;
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

	public int getStart()
	{
		return start;
	}

	public int getLimit()
	{
		return limit;
	}

	public int getPageNum()
	{
		return pageNum;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public int getTotal()
	{
		return total;
	}

	public List<T> getList()
	{
		return list;
	}

}
