// Source File Name:   ActorPos.java
// By western: add bombs' _Hardpoint hook referred , offset position value on 09th/Oct./2020

package com.maddox.il2.engine;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.Destroy;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.engine:
//            Actor, Hook, ActorException, Engine, 
//            DrawEnv, CollideEnv, DreamEnv, Loc, 
//            Orient

public abstract class ActorPos
    implements Destroy
{

    public ActorPos()
    {
    }

    public void inValidate()
    {
        inValidate(true);
    }

    public void inValidate(boolean flag)
    {
    }

    public void setUpdateEnable(boolean flag)
    {
    }

    public boolean isUpdateEnable()
    {
        return true;
    }

    public Actor actor()
    {
        return null;
    }

    public void actorChanged()
    {
        Actor actor1 = actor();
        if(!Actor.isValid(actor1))
            return;
        Object aobj[] = getBaseAttached();
        if(aobj == null)
            return;
        for(int i = 0; i < aobj.length; i++)
        {
            Actor actor2 = (Actor)aobj[i];
            if(actor2 == null)
                break;
            if(Actor.isValid(actor2) && actor2.pos.actor() == actor2 && actor2.pos.baseHook() != null && (actor2.pos.baseHook() instanceof Hook))
                ((Hook)actor2.pos.baseHook()).baseChanged(actor1);
        }

        inValidate(false);
    }

    public Actor homeBase()
    {
        return null;
    }

    public Actor base()
    {
        return null;
    }

    public Object baseHook()
    {
        return null;
    }

    public Object[] getBaseAttached()
    {
        return null;
    }

    public Object[] getBaseAttached(Object aobj[])
    {
        return null;
    }

    protected List getListBaseAttached()
    {
        return null;
    }

    public void setBase(Actor actor1, Hook hook, boolean flag)
    {
        throw new ActorException("method 'setBase' not implemented");
    }

    // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
    public void setBase(Actor actor1, Hook hook, boolean flag, Hook hookatc)
    {
        throw new ActorException("method 'setBase' not implemented");
    }

    public void setBase(Actor actor1, Hook hook, boolean flag, Hook hookatc, Point3d posoffset)
    {
        throw new ActorException("method 'setBase' not implemented");
    }
    // ---

    public void changeBase(Actor actor1, Hook hook, boolean flag)
    {
        throw new ActorException("method 'changeBase' not implemented");
    }

    public void changeHookToRel()
    {
        throw new ActorException("method 'changeHookToRel' not implemented");
    }

    public void getRel(Loc loc)
    {
        throw new ActorException("method 'getRel' not implemented");
    }

    public void getRel(Point3d point3d, Orient orient)
    {
        throw new ActorException("method 'getRel' not implemented");
    }

    public void getRel(Point3d point3d)
    {
        throw new ActorException("method 'getRel' not implemented");
    }

    public void getRel(Orient orient)
    {
        throw new ActorException("method 'getRel' not implemented");
    }

    public Loc getRel()
    {
        throw new ActorException("method 'getRel' not implemented");
    }

    public Point3d getRelPoint()
    {
        throw new ActorException("method 'getRelPoint' not implemented");
    }

    public Orient getRelOrient()
    {
        throw new ActorException("method 'getRelOrient' not implemented");
    }

    public void setRel(Loc loc)
    {
        throw new ActorException("method 'setRel' not implemented");
    }

    public void setRel(Point3d point3d, Orient orient)
    {
        throw new ActorException("method 'setRel' not implemented");
    }

    public void setRel(Point3d point3d)
    {
        throw new ActorException("method 'setRel' not implemented");
    }

    public void setRel(Orient orient)
    {
        throw new ActorException("method 'setRel' not implemented");
    }

    public void getAbs(Loc loc)
    {
        throw new ActorException("method 'getAbs' not implemented");
    }

    public void getAbs(Point3d point3d, Orient orient)
    {
        throw new ActorException("method 'getAbs' not implemented");
    }

    public void getAbs(Point3d point3d)
    {
        throw new ActorException("method 'getAbs' not implemented");
    }

    public void getAbs(Orient orient)
    {
        throw new ActorException("method 'getAbs' not implemented");
    }

    public Loc getAbs()
    {
        throw new ActorException("method 'getAbs' not implemented");
    }

    public Point3d getAbsPoint()
    {
        throw new ActorException("method 'getAbsPoint' not implemented");
    }

    public Orient getAbsOrient()
    {
        throw new ActorException("method 'getAbsOrient' not implemented");
    }

    public void setAbs(Loc loc)
    {
        throw new ActorException("method 'setAbs' not implemented");
    }

    public void setAbs(Point3d point3d, Orient orient)
    {
        throw new ActorException("method 'setAbs' not implemented");
    }

    public void setAbs(Point3d point3d)
    {
        throw new ActorException("method 'setAbs' not implemented");
    }

    public void setAbs(Orient orient)
    {
        throw new ActorException("method 'setAbs' not implemented");
    }

    public void reset()
    {
        throw new ActorException("method 'reset' not implemented");
    }

    public void resetAsBase()
    {
        throw new ActorException("methodAs 'reset' not implemented");
    }

    public boolean isChanged()
    {
        throw new ActorException("method 'isChanged' not implemented");
    }

    public void getCurrent(Loc loc)
    {
        throw new ActorException("method 'getCurrent' not implemented");
    }

    public void getCurrent(Point3d point3d)
    {
        throw new ActorException("method 'getCurrent' not implemented");
    }

    public Loc getCurrent()
    {
        throw new ActorException("method 'getCurrent' not implemented");
    }

    public Point3d getCurrentPoint()
    {
        throw new ActorException("method 'getCurrentPoint' not implemented");
    }

    public Orient getCurrentOrient()
    {
        throw new ActorException("method 'getCurrentOrient' not implemented");
    }

    public void getPrev(Loc loc)
    {
        throw new ActorException("method 'getPrev' not implemented");
    }

    public Loc getPrev()
    {
        throw new ActorException("method 'getPrev' not implemented");
    }

    protected void drawingChange(boolean flag)
    {
    }

    protected void collideChange(boolean flag)
    {
    }

    protected void dreamFireChange(boolean flag)
    {
    }

    public Loc getRender()
    {
        throw new ActorException("method 'getRender' not implemented");
    }

    public void getRender(Loc loc)
    {
        throw new ActorException("method 'getRender' not implemented");
    }

    public void getRender(Point3d point3d, Orient orient)
    {
        throw new ActorException("method 'getRender' not implemented");
    }

    public void getRender(Point3d point3d)
    {
        throw new ActorException("method 'getRender' not implemented");
    }

    public void getTime(long l, Loc loc)
    {
        throw new ActorException("method 'getTime' not implemented");
    }

    public void getTime(long l, Point3d point3d)
    {
        throw new ActorException("method 'getTime' not implemented");
    }

    protected void updateCurrent()
    {
        throw new ActorException("method 'updateCurrent' not implemented");
    }

    public double speed(Vector3d vector3d)
    {
        if(vector3d != null)
            vector3d.set(0.0D, 0.0D, 0.0D);
        return 0.0D;
    }

    protected void addChildren(Actor actor1)
    {
        throw new ActorException("method 'addChildren' not implemented");
    }

    protected void removeChildren(Actor actor1)
    {
        throw new ActorException("method 'removeChildren' not implemented");
    }

    public boolean isDestroyed()
    {
        return actor() == null;
    }

    public void destroy()
    {
    }

    protected void clearEnvs(Actor actor1)
    {
        int i = actor1.flags;
        if((i & 1) != 0 && ((i & 2) == 0 || base() == null))
            Engine.cur.drawEnv.remove(actor1);
        if((i & 0x30) == 16)
            Engine.cur.collideEnv.remove(actor1);
        if((i & 0x200) == 512)
            Engine.cur.dreamEnv.removeListener(actor1);
        if((i & 0x100) == 256)
            Engine.dreamEnv().removeFire(actor1);
        int j = Engine.cur.posChanged.indexOf(actor1);
        if(j >= 0)
            Engine.cur.posChanged.remove(j);
    }

    protected void initEnvs(Actor actor1)
    {
        actor1.pos = this;
        int i = actor1.flags;
        if((i & 1) != 0 && ((i & 2) == 0 || base() == null))
            Engine.cur.drawEnv.add(actor1);
        if((i & 0x30) == 16)
            Engine.cur.collideEnv.add(actor1);
        if((i & 0x200) == 512)
            Engine.cur.dreamEnv.addListener(actor1);
        if((i & 0x100) == 256)
            Engine.dreamEnv().addFire(actor1);
    }
}
