package cn.gotom.pojos;

import java.io.Serializable;

public interface IdSerializable extends Serializable
{
	String getId();

	long getVersionNow();

	void setVersionNow(long versionNow);
}
