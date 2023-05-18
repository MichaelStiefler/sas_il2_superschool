package com.maddox.rts;

import com.maddox.il2.game.Main;

public class BackgroundTask
{

    public BackgroundTask()
    {
        bRun = false;
        bComplete = false;
        percentComplete = 0.0F;
        bRequestCanceling = false;
    }

    public boolean isRun()
    {
        return bRun;
    }

    public boolean isComplete()
    {
        return bComplete;
    }

    public float percentComplete()
    {
        return percentComplete;
    }

    public String messageComplete()
    {
        return messageComplete;
    }

    public boolean isRequestCanceling()
    {
        return bRequestCanceling;
    }

    public String messageCancel()
    {
        return messageCancel;
    }

    public static void addListener(Object obj)
    {
        RTSConf.cur.backgroundLoop.listeners.addListener(obj);
    }

    public static void removeListener(Object obj)
    {
        RTSConf.cur.backgroundLoop.listeners.removeListener(obj);
    }

    protected void beforeStartRun()
    {
        bRequestCanceling = false;
        bComplete = false;
        percentComplete = 0.0F;
        messageComplete = null;
        bRun = true;
    }

    protected void beforeRun()
    {
    }

    protected void afterRun()
    {
        bComplete = !bRequestCanceling;
        bRun = false;
    }

    protected void run()
        throws Exception
    {
    }

    public static boolean step(float f, String s)
    {
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        if(backgroundtask == null)
        {
            return true;
        } else
        {
            boolean flag = step();
            updatePercent(f, s);
            return flag;
        }
    }

    protected boolean _step(float f, String s)
    {
        percentComplete = f;
        messageComplete = s;
        MsgBackgroundTask.post(RTSConf.cur.backgroundLoop.listeners.get(), 1, this);
        RTSConf.cur.backgroundLoop.step();
        return !bRequestCanceling;
    }

    public static boolean step()
    {
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        if(backgroundtask == null)
        {
            return true;
        } else
        {
            RTSConf.cur.backgroundLoop.step();
            return !backgroundtask.bRequestCanceling;
        }
    }

    public static void step_void()
    {
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        if(backgroundtask != null)
            RTSConf.cur.backgroundLoop.step();
    }

    public static void step_void_exception()
        throws BackgroundTaskCancelException
    {
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        if(backgroundtask != null)
        {
            RTSConf.cur.backgroundLoop.step();
            if(backgroundtask.bRequestCanceling)
                throw new BackgroundTaskCancelException(executed().messageCancel());
        }
    }

    public static void updatePercent(float f, String s)
    {
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        if(backgroundtask == null)
        {
            return;
        } else
        {
            backgroundtask.percentComplete = f;
            backgroundtask.messageComplete = s;
            MsgBackgroundTask.post(RTSConf.cur.backgroundLoop.listeners.get(), 1, backgroundtask);
            return;
        }
    }

    public static float getPercent()
    {
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        if(backgroundtask == null)
            return -1F;
        else
            return backgroundtask.percentComplete;
    }

    public static void execute(BackgroundTask backgroundtask)
    {
        if(isExecuted())
        {
            throw new RTSException("background task alredy executed");
        } else
        {
            RTSConf.cur.backgroundLoop.curTask = backgroundtask;
            backgroundtask.beforeStartRun();
            return;
        }
    }

    public static void cancel(String s)
    {
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        if(backgroundtask == null)
        {
            return;
        } else
        {
            backgroundtask.bRequestCanceling = true;
            backgroundtask.messageCancel = s;
            return;
        }
    }

    public static boolean isExecuted()
    {
        return RTSConf.cur.backgroundLoop.curTask != null;
    }

    public static BackgroundTask executed()
    {
        return RTSConf.cur.backgroundLoop.curTask;
    }

    public static void doRun()
    {
        if(!isExecuted())
            throw new RTSException("background task not found");
        BackgroundTask backgroundtask = RTSConf.cur.backgroundLoop.curTask;
        backgroundtask.beforeRun();
        MsgBackgroundTask.post(RTSConf.cur.backgroundLoop.listeners.get(), 0, backgroundtask);
        RTSConf.cur.backgroundLoop.step();
        try
        {
            backgroundtask.run();
        }
        catch(BackgroundTaskCancelException backgroundtaskcancelexception) { }
        catch(Exception exception)
        {
            if (Main.cur().mission != null && !Main.cur().mission.isDestroyed()) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                throw new RuntimeException("BackgroundTask crashed:");
            }
        }
        RTSConf.cur.backgroundLoop.curTask = null;
        backgroundtask.afterRun();
        MsgBackgroundTask.post(RTSConf.cur.backgroundLoop.listeners.get(), 2, backgroundtask);
        RTSConf.cur.backgroundLoop.step();
    }

    protected boolean bRun;
    protected boolean bComplete;
    protected float percentComplete;
    protected String messageComplete;
    protected boolean bRequestCanceling;
    protected String messageCancel;
}
