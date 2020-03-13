package com.jm.mgr;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


class AdminMgr 
{
  private ArrayList<Task> list=new ArrayList();
  private void init()
   {
     Timer timer=new Timer();
     timer.schedule(new TimerTask()
        {
                    public void run() 
                    {
                       processOnTime();
                    }
                },
     1000);
   }


  public  void add(OnTime onTime,int n) 
  {
    Task task=new Task(onTime,n);
    list.add(task);
  }

  private void processOnTime() 
  {
      for (Task task:list) 
         if (task.isTime()) task.onTime.run();
    
  }

  private class Task 
  {
      public  OnTime onTime;
      private int delay;
      private int count;
      public Task(OnTime onTime,int delay) 
      {
         this.onTime=onTime;
         this.delay=delay;
         count=delay; 
      }
      private boolean isTime() 
      {
          count--;
          boolean yes=count==0;
          if (count==0) count=delay;
          return yes;
      }
  }
 
  public  static void log(String message) {
    System.out.println(message);
}
  private static AdminMgr mgr;
  private static Object lock=new Object();
  public  synchronized static AdminMgr instance() 
  {
    if (mgr!=null) return mgr;
    synchronized(lock) 
    {
        mgr=new AdminMgr();
        mgr.init();
        return mgr;
    }
  }
}
