package cn.gotom.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表对应类
 * 
 * @author <a href="mailto:peixere@qq.com">裴绍国</a>
 * @version 2013-11-05
 * 
 */
public class Chart
{
	/**
	 * 主标题
	 */
	private String title;
	/**
	 * 副标题
	 */
	private String subtitle;

	private String xAxisTitle;

	private String yAxisTitle;

	private List<ChartSerie> series;

	public Chart()
	{
		series = new ArrayList<ChartSerie>();
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSubtitle()
	{
		return subtitle;
	}

	public void setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
	}

	public String getxAxisTitle()
	{
		return xAxisTitle;
	}

	public void setxAxisTitle(String xAxisTitle)
	{
		this.xAxisTitle = xAxisTitle;
	}

	public String getyAxisTitle()
	{
		return yAxisTitle;
	}

	public void setyAxisTitle(String yAxisTitle)
	{
		this.yAxisTitle = yAxisTitle;
	}

	public List<ChartSerie> getSeries()
	{
		return series;
	}

	public void setSeries(List<ChartSerie> series)
	{
		this.series = series;
	}

}
