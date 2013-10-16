package javax.comm;

public abstract interface CommDriver
{
  public abstract void initialize();

  public abstract CommPort getCommPort(String paramString, int paramInt);
}