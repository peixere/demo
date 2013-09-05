package javax.comm;

import java.util.EventListener;

public abstract interface SerialPortEventListener extends EventListener
{
  public abstract void serialEvent(SerialPortEvent paramSerialPortEvent);
}