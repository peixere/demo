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

	@Column(name = "description", length = 255)
	private String description;

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

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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