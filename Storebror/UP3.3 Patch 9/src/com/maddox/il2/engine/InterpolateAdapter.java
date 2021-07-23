package com.maddox.il2.engine;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.War;
import com.maddox.rts.Message;
import com.maddox.rts.MsgTimeOut;
import com.maddox.rts.MsgTimeOutListener;
import com.maddox.rts.Time;

public final class InterpolateAdapter
    implements MsgTimeOutListener
{

    public static void getSphere(AbstractCollection abstractcollection, Point3d point3d, double d)
    {
        adapter()._getSphere(abstractcollection, point3d, d);
    }

    public static void getFiltered(AbstractCollection abstractcollection, Point3d point3d, double d, ActorFilter actorfilter)
    {
        adapter()._getFiltered(abstractcollection, point3d, d, actorfilter);
    }

    public static void getNearestEnemies(Point3d point3d, double d, int i, Accumulator accumulator)
    {
        adapter()._getNearestEnemies(point3d, d, i, accumulator);
    }

    public static void getNearestEnemiesCyl(Point3d point3d, double d, double d1, double d2, int i, 
            Accumulator accumulator)
    {
        adapter()._getNearestEnemiesCyl(point3d, d, d1, d2, i, accumulator);
    }

    public static InterpolateAdapter adapter()
    {
        return Engine.cur.interpolateAdapter;
    }

    public static int step()
    {
        return Engine.cur.interpolateAdapter.stepStamp;
    }

    public static boolean isActive()
    {
        return Engine.cur.interpolateAdapter.bActive;
    }

    public static void active(boolean flag)
    {
        Engine.cur.interpolateAdapter.bActive = flag;
    }

    public static boolean isProcess()
    {
        return Engine.cur.interpolateAdapter.bProcess;
    }

    public static boolean containsListener(Actor actor)
    {
        return Engine.cur.interpolateAdapter.listeners.contains(actor);
    }

    public static void forceInterpolate(Actor actor)
    {
        Engine.cur.interpolateAdapter._forceInterpolate(actor);
    }

    private void _forceInterpolate(Actor actor)
    {
        if(!bProcess)
            return;
        if(currentListener == actor || stackListeners.contains(actor))
        {
            System.err.println("ERROR: Cycle reference interpolate position");
            int i = 0;
            if(currentListener != actor)
                i = stackListeners.indexOf(actor) + 1;
            int j = stackListeners.size();
            System.err.println("  " + actor);
            for(; i < j; i++)
                System.err.println("  " + stackListeners.get(i));

            return;
        }
        if(curListListeners.contains(actor))
        {
            stackListeners.add(actor);
            actor.interpolateTick();
            stackListeners.remove(stackListeners.size() - 1);
        }
    }

    private void updatePos()
    {
        ArrayList arraylist = Engine.cur.posChanged;
        int i = arraylist.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)arraylist.get(j);
            if(Actor.isValid(actor))
                actor.pos.updateCurrent();
        }

        arraylist.clear();
        Engine.cur.dreamEnv.doChanges();
        
//        //TODO: TEST +++
//        for (i=0; i<Engine.targets().size(); i++) {
//            if (Engine.targets().get(i) instanceof ActorPosMove) {
//                ActorPosMove actorPosMove = (ActorPosMove)Engine.targets().get(i);
//                if (actorPosMove.curTick <= Time.tickCounter() && Time.tickCounter() - actorPosMove.curTick > 1000) {
//                    System.out.println("Actor " + actorPosMove.hashCode() + " (" + actorPosMove.getClass().getName() + ") last Update >1s ago !!!");
//                }
//            }
//        }
    }

    public void msgTimeOut(Object obj)
    {
        boolean flag = Message.current().isRealTime();
        if(flag)
            realTicker.post();
        else
            ticker.post();
        curListListeners = flag ? realListeners : listeners;
        Engine.processPostDestroyActors();
        GObj.DeleteCppObjects();
        if(bActive)
        {
            bProcess = true;
            if(!flag)
                War.cur().interpolateTick();
            if(!flag)
            {
                updatePos();
            } else
            {
                if(Time.isRealOnly())
                    updatePos();
                Engine.cur.profile.endFrame();
            }
            for(iCur = 0; iCur < curListListeners.size(); iCur++)
            {
                currentListener = (Actor)curListListeners.get(iCur);
                currentListener.interpolateTick();
            }

            currentListener = null;
            bProcess = false;
            if(!flag)
            {
                Engine.cur.collideEnv.doCollision(Engine.cur.posChanged);
                Engine.cur.collideEnv.doBulletMoveAndCollision();
            }
            Engine.processPostDestroyActors();
            stepStamp++;
        }
    }

    public void addListener(Actor actor)
    {
        ArrayList arraylist = actor.isRealTime() ? realListeners : listeners;
        if(!arraylist.contains(actor))
        {
            if(!Actor.isValid(actor))
                return;
            arraylist.add(actor);
        }
    }

    public void removeListener(Actor actor)
    {
        ArrayList arraylist = actor.isRealTime() ? realListeners : listeners;
        int i = arraylist.indexOf(actor);
        if(i >= 0)
        {
            arraylist.remove(i);
            if(bProcess && i <= iCur)
                iCur--;
        }
    }

    public static void forceListener(Actor actor)
    {
        Engine.cur.interpolateAdapter._forceListener(actor);
    }

    private void _forceListener(Actor actor)
    {
        ArrayList arraylist = actor.isRealTime() ? realListeners : listeners;
        int i = arraylist.indexOf(actor);
        if(i >= 0)
        {
            arraylist.remove(i);
            arraylist.add(0, actor);
        }
    }

    public List listeners()
    {
        return listeners;
    }

    public List realListeners()
    {
        return realListeners;
    }

    private void clearDestroyedListeners(List list)
    {
        for(int j = 0; j < list.size(); j++)
        {
            Actor actor = (Actor)list.get(j);
            if(!Actor.isValid(actor))
                list.remove(j);
        }

    }

    protected void resetGameClear()
    {
        ArrayList arraylist = new ArrayList(realListeners);
        Engine.destroyListGameActors(arraylist);
        arraylist.addAll(listeners);
        Engine.destroyListGameActors(arraylist);
        clearDestroyedListeners(listeners);
        clearDestroyedListeners(realListeners);
    }

    protected void resetGameCreate()
    {
        ticker.post();
    }

    protected InterpolateAdapter()
    {
        stackListeners = new ArrayList();
        iCur = 0;
        stepStamp = 0;
        ticker = new MsgTimeOut(null);
        ticker.setTickPos(-1000);
        ticker.setNotCleanAfterSend();
        ticker.setFlags(8);
        ticker.setListener(this);
        ticker.post();
        realTicker = new MsgTimeOut(null);
        realTicker.setTickPos(-1000);
        realTicker.setNotCleanAfterSend();
        realTicker.setFlags(72);
        realTicker.setListener(this);
        realTicker.post();
        listeners = new ArrayList();
        realListeners = new ArrayList();
        bProcess = false;
        bActive = true;
    }

    private void _getSphere(AbstractCollection abstractcollection, Point3d point3d, double d)
    {
        double d1 = d * d;
        int i = listeners.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)listeners.get(j);
            if(actor.pos == null)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
            if(d2 <= d1)
                abstractcollection.add(actor);
        }

    }

    private void _getFiltered(AbstractCollection abstractcollection, Point3d point3d, double d, ActorFilter actorfilter)
    {
        double d1 = d * d;
        int i = listeners.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)listeners.get(j);
            if(actor.pos == null)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
            if(d2 <= d1 && actorfilter.isUse(actor, d2) && abstractcollection != null)
                abstractcollection.add(actor);
        }

    }

    private void _getNearestEnemies(Point3d point3d, double d, int i, Accumulator accumulator)
    {
        double d1 = d * d;
        int j = listeners.size();
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)listeners.get(k);
            if(actor.pos == null)
                continue;
            int l = actor.getArmy();
            if(l == 0 || l == i)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d2 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y) + (point3d.z - point3d1.z) * (point3d.z - point3d1.z);
            if(d2 <= d1)
                accumulator.add(actor, d2);
        }

    }

    private void _getNearestEnemiesCyl(Point3d point3d, double d, double d1, double d2, 
            int i, Accumulator accumulator)
    {
        double d3 = d * d;
        int j = listeners.size();
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)listeners.get(k);
            if(actor.pos == null)
                continue;
            int l = actor.getArmy();
            if(l == 0 || l == i)
                continue;
            Point3d point3d1 = actor.pos.getAbsPoint();
            double d4 = (point3d.x - point3d1.x) * (point3d.x - point3d1.x) + (point3d.y - point3d1.y) * (point3d.y - point3d1.y);
            if(d4 > d3)
                continue;
            double d5 = point3d1.z - point3d.z;
            if(d5 <= d2 && d5 >= d1)
                accumulator.add(actor, d4 + d5 * d5);
        }

    }

    public static final int TICK_POS = -1000;
    private Actor currentListener;
    private ArrayList stackListeners;
    private ArrayList curListListeners;
    private int iCur;
    private MsgTimeOut ticker;
    private MsgTimeOut realTicker;
    private ArrayList listeners;
    private ArrayList realListeners;
    private boolean bProcess;
    private boolean bActive;
    private int stepStamp;
}
