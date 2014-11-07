package cn.gotom.sso.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.gotom.sso.SSOException;

public class SSOServiceImpl implements SSOService
{
	private static final JDBCManager manager = JDBCManager.single;

	@Override
	public boolean login(String username, String password, String sql) throws SSOException
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try
		{
			int num = sql.split("\\?").length;
			if (!sql.endsWith("\\?"))
			{
				num = num - 1;
			}
			Connection conn = manager.connection();
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < num;)
			{
				stmt.setString(++i, username);
			}
			rs = stmt.executeQuery();
			if (rs.next())
			{
				if (password.equals(rs.getString(1)))
				{
					return true;
				}
			}
		}
		catch (Exception ex)
		{
			throw new SSOException("服务器程序异常：" + ex.getMessage(), ex);
		}
		finally
		{
			try
			{
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			manager.close();
		}
		return false;
	}

}
