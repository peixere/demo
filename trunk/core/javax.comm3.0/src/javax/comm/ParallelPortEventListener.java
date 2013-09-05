package javax.comm;

import java.util.EventListener;

public abstract interface ParallelPortEventListener extends EventListener
{
  public abstract void parallelEvent(ParallelPortEvent paramParallelPortEvent);
}