package cn.gotom.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.gotom.pojos.UploadFile;
import cn.gotom.service.UploadFileService;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

public class DownloadServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String CONTENT_TYPE = "text/html; charset=utf-8";
	@Inject
	private UploadFileService uploadFileService;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType(CONTENT_TYPE);
		String id = request.getParameter("id");
		String alt = request.getParameter("alt");
		UploadFile uf = uploadFileService.get(id);
		if (uf != null && uf.getFileStream() != null)
		{
			// response.setContentType("application/x-msdownload");
			response.setContentType(uf.getContentType());
			response.setContentLength(uf.getFileStream().length);
			String filename = uf.getFileName();
			if (StringUtils.isNotEmpty(alt) && StringUtils.isNotEmpty(filename))
			{
				response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gbk"), "iso-8859-1"));
			}
			OutputStream os = response.getOutputStream();
			os.write(uf.getFileStream());
			os.flush();
		}
		else
		{
			response.getWriter().print("找不到文件：" + id);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	public void destroy()
	{
	}
}
