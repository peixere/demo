package cn.gotom.ireport;

import java.awt.image.BufferedImage;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.apache.commons.beanutils.PropertyUtils;

public class IReportExporter
{

	private JasperPrint jasperPrint;

	public IReportExporter(InputStream jrxml, Map<String, Object> parameters) throws Exception
	{
		this(jrxml, parameters, null);
	}

	public IReportExporter(InputStream jrxml, Map<String, Object> parameters, Object dataSource) throws Exception
	{
		JRDataSource ds = null;
		// if (dataSource instanceof JRDataSource)
		if (dataSource == null)
		{
			ds = new JREmptyDataSource();
		}
		else
		{
			ds = (JRDataSource) dataSource;
		}
		JasperReport jr = JasperCompileManager.compileReport(jrxml);
		// JasperReport jr = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/report/AYearReport.jrxml"));
		jasperPrint = JasperFillManager.fillReport(jr, parameters, ds);
	}

	public IReportExporter(String filename, Map<String, Object> parameters) throws Exception
	{
		JRDataSource ds = new JREmptyDataSource();
		JasperReport jr = null;
		if (filename.endsWith(".jrxml"))
		{
			jr = JasperCompileManager.compileReport(filename);
		}
		else
		{
			jr = (JasperReport) JRLoader.loadObjectFromFile(filename);
		}
		jasperPrint = JasperFillManager.fillReport(jr, parameters, ds);
	}

	public void toHtml(OutputStream os) throws Exception
	{
		JRHtmlExporter jrHtmlExp = new JRHtmlExporter();
		jrHtmlExp.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		jrHtmlExp.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		jrHtmlExp.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		jrHtmlExp.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		jrHtmlExp.exportReport();
		os.flush();
	}

	public String toHtmlString() throws Exception
	{
		JRHtmlExporter jrExp = new JRHtmlExporter();
		jrExp.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		jrExp.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		StringBuffer html = new StringBuffer();
		jrExp.setParameter(JRExporterParameter.OUTPUT_STRING_BUFFER, html);
		jrExp.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		jrExp.exportReport();
		return html.toString();
	}

	public void expHtml(String filename) throws Exception
	{
		File file = new File(filename);
		FileOutputStream fos = new FileOutputStream(file);
		PrintWriter pw = new PrintWriter(fos);
		String html = toHtmlString();
		html = html.replaceAll("<td style=\"width: 45px; height: 1px;\"></td>", "<td style=\"width: 45px; height: 0px;\"></td>");
		html = html.replaceAll("width: 540px; border-collapse: collapse; empty-cells: show", "border-collapse: collapse; empty-cells: show");
		pw.write(html);
		pw.flush();
		pw.close();
		// JRHtmlExporter jrExp = new JRHtmlExporter();
		// jrExp.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		// jrExp.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		// jrExp.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filename);
		// jrExp.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
		// jrExp.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		// jrExp.exportReport();
	}

	public void expPDF(String filename) throws Exception
	{
		JRPdfExporter pdfExp = new JRPdfExporter();
		pdfExp.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		pdfExp.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		pdfExp.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, filename);
		pdfExp.exportReport();
	}

	public void expPDF(OutputStream os) throws Exception
	{
		// response.setContentType("application/x-msdownload");
		// // response.setContentType("application/pdf");
		// response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		// OutputStream os = response.getOutputStream();
		JRExporter exp = new JRPdfExporter();
		exp.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exp.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exp.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		exp.exportReport();
		os.flush();
	}

	public void toDocx(OutputStream os) throws Exception
	{
		// OutputStream os = response.getOutputStream();
		JRExporter exporter = new JRDocxExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		exporter.exportReport();
		// response.setContentType("application/docx");
		// response.setHeader("Content-disposition", "attachment; filename=" + filename);
		os.flush();
	}

	public BufferedImage toImage() throws Exception
	{
		BufferedImage image = new BufferedImage(jasperPrint.getPageWidth() + 1, jasperPrint.getPageHeight() + 1, BufferedImage.TYPE_INT_RGB);
		JRExporter exporter = new JRGraphics2DExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, image.getGraphics());
		// exporter.setParameter(JRExporterParameter.PAGE_INDEX, new Integer(pageIndex));
		exporter.exportReport();
		return image;
	}

	public void toXls(OutputStream os) throws Exception
	{
		// OutputStream os = response.getOutputStream();
		JRExporter exporter = new JRXlsExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE); // 删除空白行
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE); // 是否是每一页产生一个工作表
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE); // 是否为空白背景
		exporter.exportReport();
		// response.setContentType("application/xls");
		// response.setHeader("Content-disposition", "attachment; filename=" + filename);
		os.flush();
	}

	public void viewReport()
	{
		JasperViewer.viewReport(jasperPrint);
	}

	public static Map<String, Object> toMap(Object value, String dateformat)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> clazz = value.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields)
		{
			if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))
			{
				try
				{
					map.put(f.getName(), f.get(value));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		while ((clazz = clazz.getSuperclass()) != null)
		{
			fields = clazz.getDeclaredFields();
			for (Field f : fields)
			{
				if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()))
				{
					try
					{
						if (!map.containsKey(f.getName()))
							map.put(f.getName(), f.get(value));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}

	public static Map<String, Object> toParameters(Object bean, boolean printZero, String dateformat)
	{
		SimpleDateFormat df = null;
		String nullDate = "";
		if (dateformat != null && dateformat.trim().length() > 0)
		{
			df = new SimpleDateFormat(dateformat);
			nullDate = dateformat.replaceAll("Y", "　");
			nullDate = nullDate.replaceAll("y", "　");
			nullDate = nullDate.replaceAll("M", "　");
			nullDate = nullDate.replaceAll("m", "　");
			nullDate = nullDate.replaceAll("D", "　");
			nullDate = nullDate.replaceAll("d", "　");
			nullDate = nullDate.replaceAll("H", "　");
			nullDate = nullDate.replaceAll("h", "　");
			nullDate = nullDate.replaceAll("S", "　");
			nullDate = nullDate.replaceAll("s", "　");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean.getClass());
		for (PropertyDescriptor pd : pds)
		{
			try
			{
				Type returnType = pd.getReadMethod().getReturnType();
				Object value = pd.getReadMethod().invoke(bean, new Object[] {});
				if (value != null)
				{
					if (value instanceof Number)
					{
						if (Double.parseDouble(value.toString()) > 0)
						{
							map.put(pd.getName(), value.toString());
						}
						else
						{
							if (printZero)
							{
								map.put(pd.getName(), value.toString());
							}
							else
							{
								map.put(pd.getName(), " ");
							}
						}
					}
					if (value instanceof Date && df != null)
					{
						map.put(pd.getName(), df.format((Date) value));
					}
					else
					{
						map.put(pd.getName(), value);
					}
				}
				else
				{
					if (returnType.equals(Date.class))
					{

						map.put(pd.getName(), nullDate);
					}
					else
					{
						map.put(pd.getName(), " ");
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return map;
	}

	public static void main(String[] args)
	{
		try
		{
			Map<String, Object> parameters = new HashMap<String, Object>();
			// YearReportParameters p = new YearReportParameters();
			// InputStream is = ReportExporter.class.getResourceAsStream("/report/AYearReport.jrxml");
			String filename = ClassLoader.getSystemClassLoader().getResource("report/1YearReport.jrxml").getFile();
			IReportExporter exporter = new IReportExporter(filename, parameters);
			URL url = ClassLoader.getSystemClassLoader().getResource("");
			String html = exporter.toHtmlString();
			html = html.replaceAll("<td style=\"width: 45px; height: 1px;\"></td>", "<td style=\"width: 45px; height: 0px;\"></td>");
			exporter.expHtml(url.getPath() + "AYearReport.html");
			exporter.expPDF(url.getPath() + "AYearReport.pdf");
			exporter.viewReport();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
