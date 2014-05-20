package cn.gotom.service;

import cn.gotom.pojos.UploadFile;
import cn.gotom.service.impl.UploadFileServiceImpl;

import com.google.inject.ImplementedBy;

@ImplementedBy(UploadFileServiceImpl.class)
public interface UploadFileService extends GenericService<UploadFile, String>
{

}
