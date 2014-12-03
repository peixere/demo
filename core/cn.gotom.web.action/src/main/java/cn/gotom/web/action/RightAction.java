package cn.gotom.web.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.CustomRight;
import cn.gotom.pojos.Right;
import cn.gotom.pojos.UploadFile;
import cn.gotom.service.CustomService;
import cn.gotom.service.RightService;
import cn.gotom.service.UploadFileService;
import cn.gotom.service.model.RightTree;
import cn.gotom.sso.util.GsonUtils;
import cn.gotom.util.FileUtils;
import cn.gotom.util.StringUtils;
import cn.gotom.vo.JsonResponse;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Namespace(value = "/p")
@Action(value = "/right", results = { @Result(name = "success", type = "json") })
public class RightAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private boolean success;

	@Inject
	private RightService rightService;
	@Inject
	private CustomService customService;

	public void execute() throws IOException
	{
		Right right = rightService.get(this.getId());
		if (right == null)
		{
			right = new Right();
		}
		toJSON(right);
	}

	public void fresh() throws IOException
	{
		Right right = new Right();
		toJSON(right);
	}

	public void tree() throws IOException
	{
		List<RightTree> menuList = rightService.loadTree();
		if (menuList.size() == 0)
		{
			Right o = new Right();
			o.setText("系统管理");
			rightService.save(o);
			menuList = rightService.loadTree();
		}
		toJSON(menuList);
	}

	public void list() throws IOException
	{
		List<Right> list = rightService.findAll();
		toJSON(list);
	}

	public void down() throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("application/x-msdownload");
		response.setContentType("application/json");
		response.setHeader("Content-Disposition", "attachment;filename=right.json");
		List<Right> list = rightService.findAll();
		String json = GsonUtils.toJson(list);
		response.getWriter().println(json);
	}

	private File upload;
	private String uploadFileName, uploadContentType;
	@Inject
	private UploadFileService uploadFileService;

	public void up()
	{
		JsonResponse json = new JsonResponse();
		try
		{
			UploadFile uf = new UploadFile();
			uf.setFileStream(FileUtils.read(upload));
			uf.setFkTable(Right.class.getName());
			uf.setFileName(uploadFileName);
			uf.setContentType(uploadContentType);
			uf.setFileCharset(FileCharset.getCharset(upload));
			uf.setUser(getCurrentUser());
			String jsonStr = new String(uf.getFileStream(), uf.getFileCharset());
			List<Right> rights = GsonUtils.toList(Right[].class, jsonStr);
			rightService.saveAll(rights);
			for (Right right : rights)
			{
				CustomRight cr = customService.getCustomRight(right.getId(), this.getCurrentCustomId());
				if (cr == null)
				{
					cr = new CustomRight();
					cr.setCustom(new Custom());
					cr.getCustom().setId(this.getCurrentCustomId());
					cr.setRight(right);
					customService.persist(cr);
				}
			}
			json.setData("上传成功");
			uploadFileService.save(uf);
		}
		catch (Throwable ex)
		{
			json.setData("上传失败：" + ex.getClass().getSimpleName() + " " + ex.getMessage());
			log.debug("", ex);
		}
		finally
		{
			upload.deleteOnExit();
		}
		this.toJSON(json);
	}

	public String remove()
	{
		String ids = getId();
		if (StringUtils.isNotEmpty(ids))
		{
			String[] idarray = ids.split(",");
			this.setSuccess(true);
			for (int i = idarray.length - 1; i >= 0; i--)
			{
				String id = idarray[i].trim();
				if (rightService.findByParentId(id).size() == 0)
				{
					// customService.removeCustomRight(id);
					rightService.removeById(id);
				}
				else
				{
					this.setSuccess(false);
				}
			}
		}
		return "success";
	}

	public String save()
	{
		try
		{
			Right right = new Right();
			Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
			BeanUtils.copyProperties(right, params);
			this.setSuccess(true);
			rightService.save(right);
			CustomRight cr = customService.getCustomRight(right.getId(), this.getCurrentCustomId());
			if (cr == null)
			{
				cr = new CustomRight();
				cr.setCustom(new Custom());
				cr.getCustom().setId(this.getCurrentCustomId());
				cr.setRight(right);
				customService.persist(cr);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return "success";
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public String getUploadFileName()
	{
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType()
	{
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType)
	{
		this.uploadContentType = uploadContentType;
	}
}
