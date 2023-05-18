// Source File Name:   ActorPosMove.java
// By western: add bombs' _Hardpoint hook referred , offset position value on 09th/Oct./2020

package com.maddox.il2.engine;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.rts.Time;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.engine:
//            ActorPos, Actor, HookRender, ActorException, 
//            Loc, Orient, Engine, Hook, 
//            InterpolateAdapter, RendersMain, MsgBase, DrawEnv, 
//            CollideEnv, DreamEnv

public class ActorPosMove extends ActorPos
{

    private Loc nextReference()
    {
        curReference = (curReference + 1) % SIZE_REFERENCES;
        return bufReference[curReference];
    }

    public void setUpdateEnable(boolean flag)
    {
        if(flag)
        {
            flg &= 0xffffffef;
            if((flg & ABS_CHANGED) != 0)
                Engine.cur.posChanged.add(actor);
        } else
        {
            flg |= 0x10;
        }
    }

    public boolean isUpdateEnable()
    {
        return (flg & UPDATE_DISABLED) == 0;
    }

    public void inValidate(boolean flag)
    {
        if(flag)
        {
            if((flg & ABS_CHANGED) == 0)
            {
                if((flg & UPDATE_DISABLED) == 0)
                    Engine.cur.posChanged.add(actor);
                flg |= ABS_CHANGED;
            }
            flg &= -2;
            if(baseAttached != null)
            {
                int k = baseAttached.size();
                for(int i = 0; i < k; i++)
                {
                    Actor actor1 = (Actor)baseAttached.get(i);
                    if(actor1 == actor1.pos.actor())
                        actor1.pos.inValidate(true);
                }

            }
        } else
        if(baseAttached != null)
        {
            int l = baseAttached.size();
            for(int j = 0; j < l; j++)
            {
                Actor actor2 = (Actor)baseAttached.get(j);
                if(actor2 == actor2.pos.actor() && actor2.pos.baseHook() != null)
                    actor2.pos.inValidate(true);
            }

        }
    }

    private void inValidateValid(boolean flag)
    {
        if((flg & ABS_CHANGED) == 0)
        {
            if((flg & UPDATE_DISABLED) == 0)
                Engine.cur.posChanged.add(actor);
            flg |= ABS_CHANGED;
        }
        if(flag && baseAttached != null)
        {
            int j = baseAttached.size();
            for(int i = 0; i < j; i++)
            {
                Actor actor1 = (Actor)baseAttached.get(i);
                if(actor1 == actor1.pos.actor())
                    actor1.pos.inValidate(true);
            }

        }
    }

    protected void validate()
    {
        if(base == null)
            Labs.set(L);
        else
        if(baseHook != null)
        {
            Labs.set(L);
            baseHook.computePos(base, base.pos.getAbs(), Labs);
            // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
            if(attachHook != null || offsetPos != null)
            {
                if(saveAttachL == null)
                {
                    saveAttachL = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    if(attachHook != null)
                        attachHook.computePos(actor, saveAttachL, saveAttachL);
                    if(offsetPos != null)
                        saveAttachL.add(offsetPos);
                }
                Point3d tmpAtcP = new Point3d(saveAttachL.getPoint());
                tmpAbsO = Labs.getOrient();
                tmpAbsO.transform(tmpAtcP);
                Loc loctempatc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                loctempatc.set(tmpAtcP);
                Labs.sub(loctempatc);
            }
            // ---
        } else
        {
            Labs.add(L, base.pos.getAbs());
        }
        if(!InterpolateAdapter.isProcess())
            flg |= ABS_VALID;
    }

    protected void validatePrev()
    {
        int i = Time.tickCounter();
        if(i == curTick)
        {
            return;
        } else
        {
            curTick = i;
            prevLabs.set(curLabs);
            return;
        }
    }

    protected void validateRender()
    {
        if(renderTick == RendersMain.frame())
            return;
        if(base != null && baseHook != null && (baseHook instanceof HookRender) && (flg & REL_CHANGED) == 0)
        {
            if((flg & ABS_VALID) == 0)
                validate();
            renderLabs.set(L);
            if(!((HookRender)baseHook).computeRenderPos(base, base.pos.getRender(), renderLabs))
                renderLabs.interpolate(curLabs, Labs, Time.tickOffset());
        } else
        if(Time.isPaused())
        {
            if((flg & ABS_VALID) == 0)
                validate();
            renderLabs.set(Labs);
        } else
        if((flg & ABS_CHANGED) != 0)
        {
            if(base != null && baseHook == null && (flg & REL_CHANGED) == 0)
            {
                renderLabs.add(L, base.pos.getRender());
            } else
            {
                if((flg & ABS_VALID) == 0)
                    validate();
                renderLabs.interpolate(curLabs, Labs, Time.tickOffset());
            }
        } else
        {
            renderLabs.set(curLabs);
        }
        if((flg & ABS_VALID) != 0)
            renderTick = RendersMain.frame();
    }

    public Actor actor()
    {
        return actor;
    }

    public Actor homeBase()
    {
        Actor actor1 = base;
        if(actor1 == null)
            return null;
        do
        {
            Actor actor2 = actor1.pos.base();
            if(actor2 != null)
                actor1 = actor2;
            else
                return actor1;
        } while(true);
    }

    public Actor base()
    {
        return base;
    }

    public Object baseHook()
    {
        return baseHook;
    }

    public Object[] getBaseAttached()
    {
        if(baseAttached != null)
            return baseAttached.toArray();
        else
            return null;
    }

    public Object[] getBaseAttached(Object aobj[])
    {
        if(baseAttached != null)
            return baseAttached.toArray(aobj);
        else
            return null;
    }

    protected List getListBaseAttached()
    {
        return baseAttached;
    }

    // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
    public void setBase(Actor actor1, Hook hook, boolean flag, Hook hookatc)
    {
        setBase(actor1, hook, flag, false, false, hookatc);
    }

    public void setBase(Actor actor1, Hook hook, boolean flag, Hook hookatc, Point3d posoffset)
    {
        setBase(actor1, hook, flag, false, false, hookatc, posoffset);
    }

    protected void setBase(Actor actor1, Hook hook, boolean flag, boolean flag1, boolean flag2, Hook hookatc)
    {
        setBase(actor1, hook, flag, false, false, hookatc, null);
    }

    protected void setBase(Actor actor1, Hook hook, boolean flag, boolean flag1, boolean flag2, Hook hookatc, Point3d posoffset)
    {
        if(actor == null)
            return;
        if(hookatc == null && posoffset == null)
        {
            setBase(actor1, hook, flag, flag1, flag2);
            return;
        }
        if(actor1 != base || hook != baseHook || hookatc != attachHook || posoffset != offsetPos)
        {
            if(actor1 != null && !Actor.isValid(actor1))
                throw new ActorException("new base is destroyed");
            Actor actor2 = base;
            Hook hook1 = baseHook;
            Hook hook2 = attachHook;
            if(flag && (flg & ABS_VALID) == 0)
                validate();
            if(actor2 != actor1 && Actor.isValid(actor2))
            {
                actor2.pos.removeChildren(actor);
                MsgBase.detach(actor2, actor);
            }
            base = actor1;
            baseHook = hook;
            attachHook = hookatc;
            offsetPos = posoffset;
            if(flag)
            {
                Labs.get(tmpBaseP, tmpBaseO);
                setAbs(tmpBaseP, tmpBaseO);
            } else
            {
                inValidate(true);
            }
            if(actor2 != actor1 && actor1 != null)
            {
                actor1.pos.addChildren(actor);
                MsgBase.attach(actor1, actor);
            }
            if(flag1)
                MsgBase.change(actor, actor1, hook, actor2, hook1);
            int i = actor.flags;
            if((i & 3) == 3)
            {
                if(actor2 == null && actor1 != null)
                    Engine.cur.drawEnv.remove(actor);
                else
                if(actor2 != null && actor1 == null && !flag2)
                    Engine.cur.drawEnv.add(actor);
                setUpdateFlag();
            }
        }
    }
    // ---

    protected void setBase(Actor actor1, Hook hook, boolean flag, boolean flag1, boolean flag2)
    {
        if(actor == null)
            return;
        if(actor1 != base || hook != baseHook)
        {
            if(actor1 != null && !Actor.isValid(actor1))
                throw new ActorException("new base is destroyed");
            Actor actor2 = base;
            Hook hook1 = baseHook;
            if(flag && (flg & ABS_VALID) == 0)
                validate();
            if(actor2 != actor1 && Actor.isValid(actor2))
            {
                actor2.pos.removeChildren(actor);
                MsgBase.detach(actor2, actor);
            }
            base = actor1;
            baseHook = hook;
            if(flag)
            {
                Labs.get(tmpBaseP, tmpBaseO);
                setAbs(tmpBaseP, tmpBaseO);
            } else
            {
                inValidate(true);
            }
            if(actor2 != actor1 && actor1 != null)
            {
                actor1.pos.addChildren(actor);
                MsgBase.attach(actor1, actor);
            }
            if(flag1)
                MsgBase.change(actor, actor1, hook, actor2, hook1);
            int i = actor.flags;
            if((i & 3) == 3)
            {
                if(actor2 == null && actor1 != null)
                    Engine.cur.drawEnv.remove(actor);
                else
                if(actor2 != null && actor1 == null && !flag2)
                    Engine.cur.drawEnv.add(actor);
                setUpdateFlag();
            }
        }
    }

    protected void setBase(Actor actor1, Hook hook, boolean flag, boolean flag1)
    {
        setBase(actor1, hook, flag, flag1, false);
    }

    public void setBase(Actor actor1, Hook hook, boolean flag)
    {
        setBase(actor1, hook, flag, false);
    }

    public void changeBase(Actor actor1, Hook hook, boolean flag)
    {
        setBase(actor1, hook, flag, true);
    }

    public void changeHookToRel()
    {
        if(!Actor.isValid(base))
            throw new ActorException("base is empty or destroyed");
        if(baseHook == null)
            throw new ActorException("hook is empty");
        if((flg & ABS_VALID) == 0)
            validate();
        baseHook.computePos(base, base.pos.getAbs(), L);
        L.sub(base.pos.getAbs());

        // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
        if(baseHook != null)
        {
            if(attachHook != null || offsetPos != null)
            {
                if(saveAttachL == null)
                {
                    saveAttachL = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    if(attachHook != null)
                        attachHook.computePos(actor, saveAttachL, saveAttachL);
                    if(offsetPos != null)
                        saveAttachL.add(offsetPos);
                }
                Point3d tmpAtcP = new Point3d(saveAttachL.getPoint());
                tmpAbsO = Labs.getOrient();
                tmpAbsO.transform(tmpAtcP);
                Loc loctempatc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                loctempatc.set(tmpAtcP);
                L.sub(loctempatc);
                attachHook = null;
            }
        }
        // ---

        baseHook = null;
    }

    public void getRel(Loc loc)
    {
        loc.set(L);
    }

    public void getRel(Point3d point3d, Orient orient)
    {
        L.get(point3d, orient);
    }

    public void getRel(Point3d point3d)
    {
        L.get(point3d);
    }

    public void getRel(Orient orient)
    {
        L.get(orient);
    }

    public Loc getRel()
    {
        return L;
    }

    public Point3d getRelPoint()
    {
        return L.getPoint();
    }

    public Orient getRelOrient()
    {
        return L.getOrient();
    }

    public void setRel(Loc loc)
    {
        L.set(loc);
        inValidate(true);
        flg |= REL_CHANGED;
    }

    public void setRel(Point3d point3d, Orient orient)
    {
        L.set(point3d, orient);
        inValidate(true);
        flg |= REL_CHANGED;
    }

    public void setRel(Point3d point3d)
    {
        L.set(point3d);
        inValidate(true);
        flg |= REL_CHANGED;
    }

    public void setRel(Orient orient)
    {
        L.set(orient);
        inValidate(true);
        flg |= REL_CHANGED;
    }

    public void getAbs(Loc loc)
    {
        if((flg & ABS_VALID) == 0)
            validate();
        loc.set(Labs);
    }

    public void getAbs(Point3d point3d, Orient orient)
    {
        if((flg & ABS_VALID) == 0)
            validate();
        Labs.get(point3d, orient);
    }

    public void getAbs(Point3d point3d)
    {
        if((flg & ABS_VALID) == 0)
            validate();
        Labs.get(point3d);
    }

    public void getAbs(Orient orient)
    {
        if((flg & ABS_VALID) == 0)
            validate();
        Labs.get(orient);
    }

    public Loc getAbs()
    {
        if((flg & ABS_VALID) == 0)
            validate();
        return Labs;
    }

    public Point3d getAbsPoint()
    {
        if((flg & ABS_VALID) == 0)
            validate();
        return Labs.getPoint();
    }

    public Orient getAbsOrient()
    {
        if((flg & ABS_VALID) == 0)
            validate();
        return Labs.getOrient();
    }

    private void setAbsBased(Loc loc, boolean flag)
    {
        if(baseHook != null)
        {
            Labs.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            baseHook.computePos(base, base.pos.getAbs(), Labs);
            // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
            if(attachHook != null || offsetPos != null)
            {
                if(saveAttachL == null)
                {
                    saveAttachL = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    if(attachHook != null)
                        attachHook.computePos(actor, saveAttachL, saveAttachL);
                    if(offsetPos != null)
                        saveAttachL.add(offsetPos);
                }
                Point3d tmpAtcP = new Point3d(saveAttachL.getPoint());
                tmpAbsO = Labs.getOrient();
                tmpAbsO.transform(tmpAtcP);
                Loc loctempatc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                loctempatc.set(tmpAtcP);
                Labs.sub(loctempatc);
            }
            // ---
        } else
        {
            base.pos.getAbs(Labs);
        }
        L.sub(loc, Labs);
        flg |= REL_CHANGED;
        Labs.set(loc);
        inValidateValid(flag);
    }

    public void setAbs(Loc loc)
    {
        if(base == null)
        {
            setRel(loc);
        } else
        {
            boolean flag = (flg & ABS_VALID) != 0;
            if(!flag)
                validate();
            setAbsBased(loc, flag);
        }
    }

    public void setAbs(Point3d point3d, Orient orient)
    {
        if(base == null)
        {
            setRel(point3d, orient);
        } else
        {
            boolean flag = (flg & ABS_VALID) != 0;
            if(!flag)
                validate();
            tmpAbsL.set(point3d, orient);
            setAbsBased(tmpAbsL, flag);
        }
    }

    public void setAbs(Point3d point3d)
    {
        if(base == null)
        {
            setRel(point3d);
        } else
        {
            boolean flag = (flg & ABS_VALID) != 0;
            if(!flag)
                validate();
            Labs.get(tmpAbsO);
            tmpAbsL.set(point3d, tmpAbsO);
            setAbsBased(tmpAbsL, flag);
        }
    }

    public void setAbs(Orient orient)
    {
        if(base == null)
        {
            setRel(orient);
        } else
        {
            boolean flag = (flg & ABS_VALID) != 0;
            if(!flag)
                validate();
            Labs.get(tmpAbsP);
            tmpAbsL.set(tmpAbsP, orient);
            setAbsBased(tmpAbsL, flag);
        }
    }

    public boolean isChanged()
    {
        return (flg & ABS_CHANGED) != 0;
    }

    public void getCurrent(Loc loc)
    {
        loc.set(curLabs);
    }

    public void getCurrent(Point3d point3d)
    {
        curLabs.get(point3d);
    }

    public Loc getCurrent()
    {
        return curLabs;
    }

    public Point3d getCurrentPoint()
    {
        return curLabs.getPoint();
    }

    public Orient getCurrentOrient()
    {
        return curLabs.getOrient();
    }

    public void getPrev(Loc loc)
    {
        validatePrev();
        loc.set(prevLabs);
    }

    public Loc getPrev()
    {
        validatePrev();
        return prevLabs;
    }

    public Loc getRender()
    {
        validateRender();
        return renderLabs;
    }

    public void getRender(Loc loc)
    {
        validateRender();
        loc.set(renderLabs);
    }

    public void getRender(Point3d point3d, Orient orient)
    {
        validateRender();
        renderLabs.get(point3d, orient);
    }

    public void getRender(Point3d point3d)
    {
        validateRender();
        renderLabs.get(point3d);
    }

    public void getTime(long l, Loc loc)
    {
        if(InterpolateAdapter.isProcess())
        {
            validatePrev();
            loc.interpolate(prevLabs, curLabs, ((float)(l - Time.tick()) + Time.tickLenFms()) / Time.tickLenFms());
        } else
        if(l == Time.tick())
            loc.set(curLabs);
        else
        if(l < Time.tick())
        {
            validatePrev();
            loc.interpolate(prevLabs, curLabs, ((float)(l - Time.tick()) + Time.tickLenFms()) / Time.tickLenFms());
        } else
        if(l == Time.tickNext())
        {
            if((flg & ABS_VALID) == 0)
                validate();
            loc.set(Labs);
        } else
        {
            if((flg & ABS_VALID) == 0)
                validate();
            loc.interpolate(curLabs, Labs, (float)(l - Time.tick()) / Time.tickLenFms());
        }
    }

    public void getTime(long l, Point3d point3d)
    {
        if(InterpolateAdapter.isProcess())
        {
            validatePrev();
            point3d.interpolate(prevLabs.getPoint(), curLabs.getPoint(), ((float)(l - Time.tick()) + Time.tickLenFms()) / Time.tickLenFms());
        } else
        if(l == Time.tick())
            curLabs.get(point3d);
        else
        if(l < Time.tick())
        {
            validatePrev();
            point3d.interpolate(prevLabs.getPoint(), curLabs.getPoint(), ((float)(l - Time.tick()) + Time.tickLenFms()) / Time.tickLenFms());
        } else
        if(l == Time.tickNext())
        {
            if((flg & ABS_VALID) == 0)
                validate();
            Labs.get(point3d);
        } else
        {
            if((flg & ABS_VALID) == 0)
                validate();
            point3d.interpolate(curLabs.getPoint(), Labs.getPoint(), (float)(l - Time.tick()) / Time.tickLenFms());
        }
    }

    public void resetAsBase()
    {
        if(!Actor.isValid(base))
        {
            reset();
            return;
        }
        if((flg & ABS_VALID) == 0)
            validate();
        // western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
        Loc loctempatc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        if(baseHook != null)
        {
            prevLabs.set(L);
            baseHook.computePos(base, base.pos.getCurrent(), prevLabs);
            // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
            if((attachHook != null || offsetPos != null) && saveAttachL != null)
            {
                Point3d tmpAtcP = new Point3d(saveAttachL.getPoint());
                tmpAbsO = prevLabs.getOrient();
                tmpAbsO.transform(tmpAtcP);
                loctempatc.set(tmpAtcP);
                prevLabs.sub(loctempatc);
            }
            // ---
        } else
        {
            prevLabs.add(L, base.pos.getCurrent());
        }
        if((flg & UPDATE_ENV) != 0)
        {
            int i = actor.flags;
            if((i & 1) != 0 && ((i & 2) == 0 || base == null))
                Engine.cur.drawEnv.changedPos(actor, curLabs.getPoint(), prevLabs.getPoint());
            if((i & 0x30) == 16)
                Engine.cur.collideEnv.changedPos(actor, curLabs.getPoint(), prevLabs.getPoint());
            if((i & 0x200) == 512)
                Engine.cur.dreamEnv.changedListenerPos(actor, curLabs.getPoint(), prevLabs.getPoint());
            if((i & 0x100) == 256)
                Engine.cur.dreamEnv.changedFirePos(actor, curLabs.getPoint(), prevLabs.getPoint());
        }
        curLabs.set(prevLabs);
        if(baseHook != null)
        {
            prevLabs.set(L);
            baseHook.computePos(base, base.pos.getPrev(), prevLabs);
            // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
            if((attachHook != null || offsetPos != null) && saveAttachL != null)
            {
                Point3d tmpAtcP = new Point3d(saveAttachL.getPoint());
                tmpAbsO = prevLabs.getOrient();
                tmpAbsO.transform(tmpAtcP);
                loctempatc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                loctempatc.set(tmpAtcP);
                prevLabs.sub(loctempatc);
            }
            // ---
        } else
        {
            prevLabs.add(L, base.pos.getPrev());
        }
        curTick = Time.tickCounter();
        renderTick = 0;
    }

    public void reset()
    {
        updateCurrent();
        prevLabs.set(curLabs);
        int i = Engine.cur.posChanged.indexOf(actor);
        if(i >= 0)
            Engine.cur.posChanged.remove(i);
        renderTick = 0;
    }

    protected void updateCurrent()
    {
        Loc loc = prevLabs;
        prevLabs = curLabs;
        curLabs = loc;
        if((flg & ABS_CHANGED) != 0)
        {
            getAbs(curLabs);
            if((flg & UPDATE_ENV) != 0)
            {
                int i = actor.flags;
                if((i & 1) != 0 && ((i & 2) == 0 || base == null))
                    Engine.cur.drawEnv.changedPos(actor, prevLabs.getPoint(), curLabs.getPoint());
                if((i & 0x30) == 16)
                    Engine.cur.collideEnv.changedPos(actor, prevLabs.getPoint(), curLabs.getPoint());
                if((i & 0x200) == 512)
                    Engine.cur.dreamEnv.changedListenerPos(actor, prevLabs.getPoint(), curLabs.getPoint());
                if((i & 0x100) == 256)
                    Engine.cur.dreamEnv.changedFirePos(actor, prevLabs.getPoint(), curLabs.getPoint());
            }
            flg &= -7;
        } else
        {
            curLabs.set(prevLabs);
        }
        curTick = Time.tickCounter();
    }

    private void setUpdateFlag()
    {
        boolean flag = false;
        int i = actor.flags;
        if((i & 1) != 0 && ((i & 2) == 0 || base == null))
            flag = true;
        if((i & 0x30) == 16)
            flag = true;
        if((i & 0x200) == 512)
            flag = true;
        if((i & 0x100) == 256)
            flag = true;
        if(flag)
            flg |= UPDATE_ENV;
        else
            flg &= -9;
    }

    public double speed(Vector3d vector3d)
    {
        if(InterpolateAdapter.isProcess())
        {
            validatePrev();
            Point3d point3d = prevLabs.getPoint();
            Point3d point3d2 = curLabs.getPoint();
            double d = point3d2.x - point3d.x;
            double d2 = point3d2.y - point3d.y;
            double d4 = point3d2.z - point3d.z;
            double d6 = 1.0F / Time.tickLenFs();
            if(vector3d != null)
                vector3d.set(d * d6, d2 * d6, d4 * d6);
            return Math.sqrt(d * d + d2 * d2 + d4 * d4) * d6;
        }
        if((flg & ABS_VALID) == 0)
            validate();
        Point3d point3d1 = Labs.getPoint();
        Point3d point3d3 = curLabs.getPoint();
        double d1 = point3d1.x - point3d3.x;
        double d3 = point3d1.y - point3d3.y;
        double d5 = point3d1.z - point3d3.z;
        double d7 = 1.0F / Time.tickLenFs();
        if(vector3d != null)
            vector3d.set(d1 * d7, d3 * d7, d5 * d7);
        return Math.sqrt(d1 * d1 + d3 * d3 + d5 * d5) * d7;
    }

    protected void drawingChange(boolean flag)
    {
        if(flag)
        {
            if(actor.isVisibilityAsBase() && base != null)
                Engine.cur.drawEnv.remove(actor);
            else
                Engine.cur.drawEnv.add(actor);
        } else
        {
            Engine.cur.drawEnv.remove(actor);
        }
        setUpdateFlag();
    }

    protected void collideChange(boolean flag)
    {
        if(flag)
            Engine.cur.collideEnv.add(actor);
        else
            Engine.cur.collideEnv.remove(actor);
        setUpdateFlag();
    }

    protected void dreamFireChange(boolean flag)
    {
        if(flag)
            Engine.dreamEnv().addFire(actor);
        else
            Engine.dreamEnv().removeFire(actor);
        setUpdateFlag();
    }

    protected void addChildren(Actor actor1)
    {
        if(baseAttached == null)
            baseAttached = new ArrayList();
        baseAttached.add(actor1);
    }

    protected void removeChildren(Actor actor1)
    {
        baseAttached.remove(baseAttached.indexOf(actor1));
    }

    public void destroy()
    {
        if(actor == null)
            return;
        clearEnvs(actor);
        if(baseAttached != null)
            while(baseAttached.size() > 0) 
            {
                Actor actor1 = (Actor)baseAttached.get(0);
                if(actor1 == actor1.pos.actor())
                    actor1.pos.changeBase(null, null, true);
                else
                    baseAttached.remove(0);
            }
        setBase(null, null, true, true, true);
        actor = null;
    }

    protected ActorPosMove()
    {
        actor = null;
        base = null;
        baseHook = null;
        baseAttached = null;
        L = new Loc();
        Labs = new Loc();
        flg = 0;
        curTick = 0;
        curLabs = new Loc();
        prevLabs = new Loc();
        renderLabs = new Loc();
        renderTick = -1;
        // western: Engine mod 2.9.x
        attachHook = null;
        offsetPos = null;
        saveAttachL = null;
    }

    public ActorPosMove(Actor actor1)
    {
        actor = null;
        base = null;
        baseHook = null;
        baseAttached = null;
        L = new Loc();
        Labs = new Loc();
        flg = 0;
        curTick = 0;
        curLabs = new Loc();
        prevLabs = new Loc();
        renderLabs = new Loc();
        renderTick = -1;
        actor = actor1;
        initEnvs(actor);
        setUpdateFlag();
        // western: Engine mod 2.9.x
        attachHook = null;
        saveAttachL = null;
    }

    public ActorPosMove(Actor actor1, Point3d point3d, Orient orient)
    {
        actor = null;
        base = null;
        baseHook = null;
        baseAttached = null;
        L = new Loc();
        Labs = new Loc();
        flg = 0;
        curTick = 0;
        curLabs = new Loc();
        prevLabs = new Loc();
        renderLabs = new Loc();
        renderTick = -1;
        actor = actor1;
        L.set(point3d, orient);
        curLabs.set(L);
        prevLabs.set(L);
        initEnvs(actor);
        setUpdateFlag();
        // western: Engine mod 2.9.x
        attachHook = null;
        saveAttachL = null;
    }

    public ActorPosMove(Actor actor1, Loc loc)
    {
        actor = null;
        base = null;
        baseHook = null;
        baseAttached = null;
        L = new Loc();
        Labs = new Loc();
        flg = 0;
        curTick = 0;
        curLabs = new Loc();
        prevLabs = new Loc();
        renderLabs = new Loc();
        renderTick = -1;
        actor = actor1;
        L.set(loc);
        curLabs.set(L);
        prevLabs.set(L);
        initEnvs(actor);
        setUpdateFlag();
        // western: Engine mod 2.9.x
        attachHook = null;
        saveAttachL = null;
    }

    public ActorPosMove(ActorPos actorpos)
    {
        this(actorpos.actor(), actorpos.getAbs());
        Object aobj[] = actorpos.getBaseAttached();
        if(aobj != null && aobj.length > 0)
        {
            baseAttached = new ArrayList();
            for(int i = 0; i < aobj.length; i++)
                baseAttached.add(aobj[i]);

        }
        setUpdateFlag();
    }

    private static final boolean DEBUG_REFERENCE = false;
    private static final int SIZE_REFERENCES = 256;
    private static int curReference = 0;
    private static Loc bufReference[] = new Loc[SIZE_REFERENCES];
    private Actor actor;
    protected Actor base;
    protected Hook baseHook;
    private List baseAttached;
    protected Loc L;
    protected Loc Labs;
    public static final int ABS_VALID = 1;
    public static final int ABS_CHANGED = 2;
    public static final int REL_CHANGED = 4;
    public static final int UPDATE_ENV = 8;
    public static final int UPDATE_DISABLED = 16;
    protected int flg;
    protected int curTick;
    protected Loc curLabs;
    protected Loc prevLabs;
    protected Loc renderLabs;
    protected int renderTick;
    protected static Point3d tmpBaseP = new Point3d();
    protected static Orient tmpBaseO = new Orient();
    private static Loc tmpAbsL = new Loc();
    private static Point3d tmpAbsP = new Point3d();
    private static Orient tmpAbsO = new Orient();

    // +++ western: Engine mod 2.9.x Bomb hook position in _Hardpoint refering
    protected Hook attachHook;
    protected Point3d offsetPos;
    protected Loc saveAttachL;
    // ---
}
