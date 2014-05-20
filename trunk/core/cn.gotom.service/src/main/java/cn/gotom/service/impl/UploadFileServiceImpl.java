package cn.gotom.service.impl;

import cn.gotom.dao.jpa.GenericDaoJpa;
import cn.gotom.pojos.UploadFile;
import cn.gotom.service.UploadFileService;

import com.google.inject.Singleton;

@Singleton
public class UploadFileServiceImpl extends GenericDaoJpa<UploadFile, String> implements UploadFileService
{
	public UploadFileServiceImpl()
	{
		super(UploadFile.class);
	}
}
