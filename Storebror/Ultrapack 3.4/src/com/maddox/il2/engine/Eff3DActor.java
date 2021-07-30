package com.maddox.il2.engine;

import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.Spawn;
import com.maddox.rts.State;
import com.maddox.rts.States;
import com.maddox.rts.Time;

public class Eff3DActor extends Actor
    implements MsgBaseListener
{
    public static class SPAWN
        implements ActorSpawn
    {

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            if(!Config.isUSE_RENDER())
            {
                return null;
            } else
            {
                Loc loc = actorspawnarg.getAbsLoc();
                Eff3D.spawnSetCommonFields(actorspawnarg, loc);
                Eff3D eff3d = Eff3D.New();
                Eff3DActor eff3dactor = eff3d.NewActor(loc);
                actorspawnarg.set(eff3dactor);
                return eff3dactor;
            }
        }

        public SPAWN()
        {
        }
    }

    public class Finish extends State
    {

        public void begin(int i)
        {
            float f = ((Eff3D)draw).timeFinish();
            boolean flag = ((Eff3D)draw).isTimeReal();
            if(f <= 0.0F)
            {
                destroy();
                return;
            } else
            {
                ((Eff3D)draw).finish();
                MsgDestroy.Post(flag ? 64 : 0, (flag ? Time.currentReal() : Time.current()) + (long)(f * 1000F), superObj());
                return;
            }
        }

        public Finish(Object obj)
        {
            super(obj);
        }
    }

    public class Ready extends State
    {

        public void begin(int i)
        {
            float f = ((Eff3D)draw).timeLife();
            boolean flag = ((Eff3D)draw).isTimeReal();
            if(f >= 0.0F)
                new MsgAction(flag ? 64 : 0, (flag ? Time.currentReal() : Time.current()) + (long)(f * 1000F)) {

                    public void doAction(Object obj)
                    {
                        if(states != null && states.getState() == 0)
                            states.setState(1);
                    }

                }
;
        }


        public Ready(Object obj)
        {
            super(obj);
        }
    }


    public boolean isUseIntensityAsSwitchDraw()
    {
        return bUseIntensityAsSwitchDraw;
    }

    public void setUseIntensityAsSwitchDraw(boolean flag)
    {
        bUseIntensityAsSwitchDraw = flag;
    }

    public void msgBaseAttach(Actor actor)
    {
    }

    public void msgBaseDetach(Actor actor)
    {
    }

    public void msgBaseChange(Actor actor, Hook hook, Actor actor1, Hook hook1)
    {
        if(actor == null && actor1 != null && actor1.isDestroyed())
            _finish();
    }

    public void _finish()
    {
        ((Eff3D)draw).setIntesity(0.0F);
        states.setState(1);
    }

    public static void finish(Eff3DActor eff3dactor)
    {
        if(Actor.isValid(eff3dactor))
            eff3dactor._finish();
    }

    public void _setIntesity(float f)
    {
        if(states.getState() == 0)
        {
            ((Eff3D)draw).setIntesity(f);
            if(bUseIntensityAsSwitchDraw)
            {
                if(f != 0.0F)
                {
                    drawing(true);
                    syncObj = null;
                    return;
                }
                float f1 = ((Eff3D)draw).timeFinish();
                if(f1 <= 0.0F)
                {
                    drawing(false);
                } else
                {
                    boolean flag = ((Eff3D)draw).isTimeReal();
                    syncObj = new Object();
                    new MsgAction(flag ? 64 : 0, (flag ? Time.currentReal() : Time.current()) + (long)(f1 * 1000F), syncObj) {

                        public void doAction(Object obj)
                        {
                            if(obj != syncObj || states.getState() != 0)
                            {
                                return;
                            } else
                            {
                                drawing(false);
                                return;
                            }
                        }

                    }
;
                }
            }
        }
    }

    public static void setIntesity(Eff3DActor eff3dactor, float f)
    {
        if(Actor.isValid(eff3dactor))
            eff3dactor._setIntesity(f);
    }

    public float _getIntesity()
    {
        if(states.getState() == 0)
            return ((Eff3D)draw).getIntesity();
        else
            return 0.0F;
    }

    public static float getIntesity(Eff3DActor eff3dactor)
    {
        if(Actor.isValid(eff3dactor))
            return eff3dactor._getIntesity();
        else
            return 0.0F;
    }

    public static Eff3DActor New(Actor actor, Hook hook, Loc loc, float f, String s, float f1)
    {
        return New(actor, hook, loc, f, s, f1, false);
    }

    public static Eff3DActor New(Actor actor, Hook hook, Loc loc, float f, String s, float f1, boolean flag)
    {
        if(!Config.isUSE_RENDER())
            return null;
        // TODO: Added by SAS~Storebror - Enable identification of "INTERNAL ERROR: Str2FloatClamp()" log errors.
        lastEffectFile = s;
        // ---
        apos.setBase(actor, hook, false);
        if(loc != null)
            apos.setRel(loc);
        apos.resetAsBase();
        apos.getRender(lres);
        apos.setBase(null, null, false);
        if(loc != null)
            apos.setRel(lempty);
        Eff3DActor eff3dactor = NewPosMove(lres, f, s, f1);
        eff3dactor.pos.setBase(actor, hook, false);
        if(loc != null)
            eff3dactor.pos.setRel(loc);
        else
            eff3dactor.pos.setRel(lempty);
        eff3dactor.pos.resetAsBase();
        if(flag)
        {
            eff3dactor.pos.changeHookToRel();
            eff3dactor.pos.setUpdateEnable(false);
        }
        return eff3dactor;
    }

    public static Eff3DActor NewPosMove(Loc loc, float f, String s, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return null;
        } else
        {
            // TODO: Added by SAS~Storebror - Enable identification of "INTERNAL ERROR: Str2FloatClamp()" log errors.
            lastEffectFile = s;
            // ---
            Eff3D.initSetLocator(loc);
            Eff3D.initSetSize(f);
            Eff3D.initSetParamFileName(s);
            Eff3D.initSetProcessTime(f1);
            Eff3D eff3d = Eff3D.New();
            _isStaticPos = false;
            Eff3DActor eff3dactor = eff3d.NewActor(loc);
            _isStaticPos = true;
            return eff3dactor;
        }
    }

    public static Eff3DActor New(Loc loc, float f, String s, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return null;
        } else
        {
            // TODO: Added by SAS~Storebror - Enable identification of "INTERNAL ERROR: Str2FloatClamp()" log errors.
            lastEffectFile = s;
            // ---
            Eff3D.initSetLocator(loc);
            Eff3D.initSetSize(f);
            Eff3D.initSetParamFileName(s);
            Eff3D.initSetProcessTime(f1);
            Eff3D eff3d = Eff3D.New();
            return eff3d.NewActor(loc);
        }
    }

    public static Eff3DActor New(ActorPos actorpos, float f, String s, float f1)
    {
        if(!Config.isUSE_RENDER())
        {
            return null;
        } else
        {
            // TODO: Added by SAS~Storebror - Enable identification of "INTERNAL ERROR: Str2FloatClamp()" log errors.
            lastEffectFile = s;
            // ---
            Eff3D.initSetLocator(actorpos.getAbs());
            Eff3D.initSetSize(f);
            Eff3D.initSetParamFileName(s);
            Eff3D.initSetProcessTime(f1);
            Eff3D eff3d = Eff3D.New();
            return eff3d.NewActor(actorpos);
        }
    }

    protected Eff3DActor(Eff3D eff3d, Loc loc)
    {
        bUseIntensityAsSwitchDraw = false;
        syncObj = null;
        draw = eff3d;
        if(_isStaticPos)
            pos = new ActorPosStaticEff3D(this, loc);
        else
            pos = new ActorPosMove(this, loc);
        states = new States(new Object[] {
            new Ready(this), new Finish(this)
        });
        states.setState(0);
        drawing(true);
        Engine.cur.allEff3DActors.put(this, null);
    }

    protected Eff3DActor(Eff3D eff3d, ActorPos actorpos)
    {
        bUseIntensityAsSwitchDraw = false;
        syncObj = null;
        draw = eff3d;
        pos = actorpos;
        states = new States(new Object[] {
            new Ready(this), new Finish(this)
        });
        states.setState(0);
        flags |= 3;
        actorpos.base().pos.addChildren(this);
        Engine.cur.allEff3DActors.put(this, null);
    }

    protected Eff3DActor()
    {
        bUseIntensityAsSwitchDraw = false;
        syncObj = null;
    }

    protected void createActorHashCode()
    {
        makeActorRealHashCode();
    }

    public void destroy()
    {
        if(isDestroyed())
            return;
        Engine.cur.allEff3DActors.remove(this);
        super.destroy();
        draw = null;
        if(pos instanceof ActorPosStaticEff3D)
            pos = null;
        syncObj = null;
    }
    
    // TODO: Added by SAS~Storebror - Enable identification of "INTERNAL ERROR: Str2FloatClamp()" log errors.
    private static String lastEffectFile = "";

    public static String getLastEffectFile() {
        return lastEffectFile;
    }
    // ---

    public static final int STATE_READY = 0;
    public static final int STATE_FINISH = 1;
    protected boolean bUseIntensityAsSwitchDraw;
    protected Object syncObj;
    private static Loc lres = new Loc();
    private static Loc lempty = new Loc();
    private static ActorPosMoveInit apos = new ActorPosMoveInit();
    protected static boolean _isStaticPos = true;

    static 
    {
        Spawn.add(Eff3DActor.class, new SPAWN());
    }
}
