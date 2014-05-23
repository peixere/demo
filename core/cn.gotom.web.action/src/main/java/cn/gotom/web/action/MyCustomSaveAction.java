package cn.gotom.web.action;

import java.io.File;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;

import cn.gotom.pojos.Custom;
import cn.gotom.pojos.UploadFile;
import cn.gotom.service.CustomService;
import cn.gotom.service.UploadFileService;
import cn.gotom.util.FileUtils;
import cn.gotom.vo.JsonResponse;

import com.google.inject.Inject;

@Namespace(value = "/p")
@Action(value = "/myCustomSave")
@InterceptorRefs(value = { @InterceptorRef("fileUploadStack") })
public class MyCustomSaveAction extends AbsPortalAction
{
	protected final Logger log = Logger.getLogger(getClass());

	private Custom custom;
	private File topbg;
	private String topbgFileName;
	private String topbgContentType;
	private File logo;
	private String logoFileName;
	private String logoContentType;

	@Inject
	private CustomService customService;

	@Inject
	private UploadFileService uploadFileService;

	public void execute()
	{
		save();
	}

	public void save()
	{
		JsonResponse jr = new JsonResponse();
		try
		{
			custom = new Custom();
			Map<String, String[]> params = ServletActionContext.getRequest().getParameterMap();
			BeanUtils.copyProperties(custom, params);
			upload(custom);
			customService.save(custom);
			jr.setSuccess(true);
		}
		catch (Exception ex)
		{
			jr.setData(ex.getMessage());
			jr.setSuccess(false);
			log.error(ex.getMessage(), ex);
		}
		toJSON(jr);
	}

	private void upload(Custom custom)
	{
		// @SuppressWarnings("deprecation")
		// String realPath = this.getRequest().getRealPath("");
		// String upload = "/uploads/";
		try
		{
			if (logo != null && logo.length() > 0)
			{
				UploadFile uf = new UploadFile();
				uf.setFileStream(FileUtils.read(logo));
				uf.setFkId(custom.getId());
				uf.setFkTable(custom.getClass().getName());
				uf.setFileName(logoFileName);
				uf.setContentType(logoContentType);
				uf.setFileCharset(FileCharset.getCharset(logo));
				uf.setUser(getCurrentUser());
				uploadFileService.save(uf);
				custom.setLogoId(uf.getId());
				logo.deleteOnExit();
			}
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(), ex);
		}
		try
		{
			if (topbg != null && topbg.length() > 0)
			{
				UploadFile uf = new UploadFile();
				uf.setFileStream(FileUtils.read(topbg));
				uf.setFkId(custom.getId());
				uf.setFkTable(custom.getClass().getName());
				uf.setFileName(topbgFileName);
				uf.setContentType(this.topbgContentType);
				uf.setFileCharset(FileCharset.getCharset(topbg));
				uf.setUser(getCurrentUser());
				uploadFileService.save(uf);
				custom.setTopbgId(uf.getId());
				topbg.deleteOnExit();
			}
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage(), ex);
		}
	}

	public Custom getCustom()
	{
		return custom;
	}

	public void setCustom(Custom custom)
	{
		this.custom = custom;
	}

	public File getTopbg()
	{
		return topbg;
	}

	public void setTopbg(File topbg)
	{
		this.topbg = topbg;
	}

	public String getTopbgFileName()
	{
		return topbgFileName;
	}

	public void setTopbgFileName(String topbgFileName)
	{
		this.topbgFileName = topbgFileName;
	}

	public File getLogo()
	{
		return logo;
	}

	public void setLogo(File logo)
	{
		this.logo = logo;
	}

	public String getLogoFileName()
	{
		return logoFileName;
	}

	public void setLogoFileName(String logoFileName)
	{
		this.logoFileName = logoFileName;
	}

	public String getTopbgContentType()
	{
		return topbgContentType;
	}

	public void setTopbgContentType(String topbgContentType)
	{
		this.topbgContentType = topbgContentType;
	}

	public String getLogoContentType()
	{
		return logoContentType;
	}

	public void setLogoContentType(String logoContentType)
	{
		this.logoContentType = logoContentType;
	}

}
