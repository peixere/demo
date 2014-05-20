package cn.gotom.pojos;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * 用户上传数据
 * 
 * @author peixere@qq.com
 * 
 * @version 2014-05-21
 * 
 */
@Entity
@Table(name = "core_upload_file")
public class UploadFile extends SuperEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_charset")
	private String fileCharset;

	@Column(name = "file_stream", length = 10000000)
	private byte[] fileStream;

	@Column(name = "fk_id", nullable = false, columnDefinition = "char(36)", length = 36)
	private String fkId;

	@Column(name = "fk_table")
	private String fkTable;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getFileCharset()
	{
		return fileCharset;
	}

	public void setFileCharset(String fileCharset)
	{
		this.fileCharset = fileCharset;
	}

	public byte[] getFileStream()
	{
		return fileStream;
	}

	public void setFileStream(byte[] fileStream)
	{
		this.fileStream = fileStream;
	}

	public String getFkId()
	{
		return fkId;
	}

	public void setFkId(String fkId)
	{
		this.fkId = fkId;
	}

	public String getFkTable()
	{
		return fkTable;
	}

	public void setFkTable(String fkTable)
	{
		this.fkTable = fkTable;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

}