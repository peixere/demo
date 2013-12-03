package cn.gotom.web.action;

import java.io.IOException;
import java.io.StringReader;

import javax.servlet.ServletOutputStream;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;

/**
 * highcharts 导出
 * 
 * @author <a href="mailto:peixere@qq.com">裴绍国</a>
 * @version 2013-12-03
 */
@Namespace("/p")
@Action(value = "/highcharts", results = { @Result(name = "success", type = "stream") })
public class HighchartsExportAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private String type;
	private String svg;
	private String filename;

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setSvg(String svg)
	{
		this.svg = svg;
	}

	public String getSvg()
	{
		return svg;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getType()
	{
		return type;
	}

	public void execute()
	{
//		SVGUniverse svgUniverse = new SVGUniverse();
//	    SVGDiagram diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(f.toURL()));
//	    BufferedImage bi = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB);
//	    Graphics2D ig2 = bi.createGraphics();
//	    diagram.render(ig2);
//	    ImageIO.write(bi, "PNG", new File("./yourImageName.png"));
	    
		String type = this.getType();
		String svg = this.getSvg();
		svg = svg.replaceAll("<SPAN style=", "<text style=");
		svg = svg.replaceAll("</SPAN>", "</text>");
		svg = svg.replaceAll("Highcharts.com", "gotom.cn");
		svg = svg.replaceAll(":rect", "rect");
		if (type.equals("image/svg+xml"))
		{
			exportSvg(svg);
			return;
		}
		else
		{
			String ext = "";
			Transcoder t = null;

			if (type.equals("image/png"))
			{
				ext = "png";
				t = new PNGTranscoder();

			}
			else if (type.equals("image/jpeg"))
			{
				ext = "jpg";
				t = new JPEGTranscoder();

			}
			else if (type.equals("application/pdf"))
			{
				ext = "pdf";
				t = new PDFTranscoder();
			}
			ServletOutputStream outputStream = null;
			String encoing = ServletActionContext.getRequest().getCharacterEncoding();
			try
			{
				outputStream = ServletActionContext.getResponse().getOutputStream();
				System.out.println("type:" + type);
				if (null != type && null != svg)
				{
					ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(this.getFilename(), "UTF-8") + "." + ext);
					ServletActionContext.getResponse().setContentType(type + ";charset=" + encoing);
					if (null != t)
					{
						TranscoderInput input = new TranscoderInput(new StringReader(svg));
						TranscoderOutput output = new TranscoderOutput(outputStream);
						try
						{
							t.transcode(input, output);
						}
						catch (TranscoderException e)
						{
							outputStream.print("Problem transcoding stream. See the web logs for more details.");
							e.printStackTrace();
						}

					}
					else
					{
						outputStream.print("Invalid type: " + type);
					}
				}
				else
				{
					ServletActionContext.getResponse().setContentType("text/html;charset=" + encoing);
					outputStream.println("Usage:\n\tParameter[svg]: The DOM Element to be converted.\n\tParameter [type]: The destinationMIME type for the elment to be transcoded.");
				}
				outputStream.flush();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				log.error(e.getMessage());
			}
			finally
			{
				if (outputStream != null)
				{
					try
					{
						outputStream.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						log.error(e.getMessage());
					}
				}
			}
		}
		// return com.opensymphony.xwork2.Action.SUCCESS;
	}

	private void exportSvg(String svg)
	{
		try
		{
			String encoing = ServletActionContext.getRequest().getCharacterEncoding();
			ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(this.getFilename(), "UTF-8") + ".svg");
			ServletActionContext.getResponse().setContentType(type + ";charset=" + encoing);
			ServletActionContext.getResponse().getWriter().println(svg);
		}
		catch (IOException e)
		{
			log.error("导出SVG异常：" + e.getMessage());
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ServletActionContext.getResponse().getWriter().flush();
				ServletActionContext.getResponse().getWriter().close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
