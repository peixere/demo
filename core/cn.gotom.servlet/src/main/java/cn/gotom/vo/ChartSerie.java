package cn.gotom.vo;

import java.util.List;

public class ChartSerie
{
	private String name;
	private List<ChartSeriePoint> data;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<ChartSeriePoint> getData()
	{
		return data;
	}

	public void setData(List<ChartSeriePoint> data)
	{
		this.data = data;
	}

}
