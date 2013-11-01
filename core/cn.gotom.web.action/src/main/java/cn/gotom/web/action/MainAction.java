package cn.gotom.web.action;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import cn.gotom.pojos.Right;
import cn.gotom.service.AuthService;
import cn.gotom.service.RightService;
import cn.gotom.servlet.JsonAction;
import cn.gotom.util.StringUtils;

import com.google.inject.Inject;

@ParentPackage("json-default")
@Action(value = "/main", results = { @Result(name = "success", type = "json") })
public class MainAction extends JsonAction
{
	protected final Logger log = Logger.getLogger(getClass());

	@Inject
	private AuthService authService;

	@Inject
	private RightService rightService;

	private List<Right> rightList;

	private String id;

	private String username;

	private String action;

	private String casServerLogoutUrl;

	private String title;

	public void execute() throws IOException
	{
		if (action == null)
		{
			main();
		}
		else
		{
			menu();
		}
	}

	private void main() throws IOException
	{
		username = ServletActionContext.getRequest().getRemoteUser();
		this.setTitle("统合管理平台");
		casServerLogoutUrl = ServletActionContext.getServletContext().getInitParameter("casServerLogoutUrl");
		rightList = authService.findRightList(username, id);
		toJSON(this);
	}

	private void menu() throws IOException
	{
		List<Right> menuList = null;
		if (StringUtils.isNullOrEmpty(id))
		{
			menuList = rightService.findByParentId(id);
		}
		else
		{
			menuList = rightService.loadTreeByParentId(id);
		}
		toJSON(menuList);
		//resource();
	}

	private boolean hasRight = true;

	public void resource() throws IOException
	{
		List<Right> menuList = null;
		String sql = "select  t.id, t.text, t.component, t.description, t.type, t.iconCls, t.sort,t.leaf from resource t";
		if (StringUtils.isNullOrEmpty(id))
		{
			sql += " where t.parent_id is null";
		}
		else
		{
			sql += " where t.parent_id = '" + id + "'";
		}

		if (rightService.findAll().size() == 0)
		{
			hasRight = false;
			menuList = rightService.query(Right.class, sql);
			for (Right r : menuList)
			{
				r.setExpanded(true);
				loadChildrencallback(r);
			}
		}
		else
		{
			if (StringUtils.isNullOrEmpty(id))
			{
				menuList = rightService.findByParentId(id);
			}
			else
			{
				menuList = rightService.loadTreeByParentId(id);
			}
		}
		this.toJSON(menuList);
	}

	private void loadChildrencallback(Right right)
	{
		String sql = "select  t.id, t.text, t.component, t.description, t.type, t.iconCls, t.sort,t.leaf from resource t";
		sql += " where t.parent_id = '" + right.getId() + "'";
		List<Right> menuList = rightService.query(Right.class, sql);
		if (!hasRight)
		{
			right.setId(UUID.randomUUID().toString());
			rightService.save(right);
		}
		right.setChildren(menuList);
		for (Right r : menuList)
		{
			r.setParentId(right.getId());
			loadChildrencallback(r);
		}
	}

	public List<Right> getRightList()
	{
		return rightList;
	}

	public void setRightList(List<Right> rightList)
	{
		this.rightList = rightList;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCasServerLogoutUrl()
	{
		return casServerLogoutUrl;
	}

	public void setCasServerLogoutUrl(String casServerLogoutUrl)
	{
		this.casServerLogoutUrl = casServerLogoutUrl;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
