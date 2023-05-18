package com.maddox.il2.engine.hotkey;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookRender;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.CommonTools;

public class HookGunner extends HookRender
{
    public static interface Move
    {

        public abstract void moveGun(Orient orient);

        public abstract void clipAnglesGun(Orient orient);

        public abstract Hook getHookCameraGun();

        public abstract void doGunFire(boolean flag);
    }


    public static void resetGame()
    {
        for(int i = 0; i < all.size(); i++)
        {
            HookGunner hookgunner = (HookGunner)all.get(i);
            hookgunner.mover = null;
            hookgunner.target = null;
            hookgunner.target2 = null;
            hookgunner.bUse = false;
            hookgunner.oGunMove.set(0.0F, 0.0F, 0.0F);
        }

        all.clear();
        current = null;
    }

    public Orient getGunMove()
    {
        return oGunMove;
    }

    public void resetMove(float f, float f1)
    {
        oGunMove.set(f, f1, 0.0F);
        if(mover != null)
        {
            mover.clipAnglesGun(oGunMove);
            mover.moveGun(oGunMove);
        }
    }

    public void setMover(Move move)
    {
        mover = move;
    }

    private void _reset()
    {
        if(!AircraftHotKeys.bFirstHotCmd)
        {
            _Azimut = Azimut = 0.0F;
            _Tangage = Tangage = 0.0F;
        }
        Px = Py = Pz = 0.0F;
        L.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        tstamp = -1L;
        roolTime = -1L;
    }

    public void reset()
    {
        _reset();
    }

    private void headRoll(Aircraft aircraft)
    {
        long l = roolTime - tstamp;
        if(l >= 0L && l < 50L)
            return;
        roolTime = tstamp;
        float f = (float)(-(aircraft.FM.getAccel().y + (double)aircraft.FM.getRollAcceleration()) * 0.05D);
        float f1 = (float)(-aircraft.FM.getAccel().x * 0.1D);
        float f2 = (float)(-aircraft.FM.getAccel().z * 0.02D);
        if(!(f1 >= 1.0F) || !(f1 <= -1F)) // mind you, f1 could be NaN!
//        if(f1 >= 1.0F || f1 <= -1F)
            if(f1 < -1F)
                f1 = -1F;
            else
            if(f1 > 1.0F)
                f1 = 1.0F;
            else
                f1 = 0.0F;
        if(!(f >= 1.0F) || !(f <= -1F)) // mind you, f could be NaN!
//        if(f >= 1.0F || f <= -1F)
            if(f < -1F)
                f = -1F;
            else
            if(f > 1.0F)
                f = 1.0F;
            else
                f = 0.0F;
        if(!(f2 >= 1.0F) || !(f2 <= -1F)) // mind you, f2 could be NaN!
//        if(f2 >= 1.0F || f2 <= -1F)
            if(f2 < -1F)
                f2 = -1F;
            else
            if(f2 > 1.0F)
                f2 = 1.0F;
            else
                f2 = 0.0F;
        L.set(Px += (f1 * 0.015F - Px) * 0.4F, Py += (f * 0.015F - Py) * 0.4F, Pz += (f2 * 0.015F - Pz) * 0.4F, 0.0F, 0.0F, 0.0F);
    }

    private float bvalue(float f, float f1, long l)
    {
        float f2 = (HookView.koofSpeed * (float)l) / 30F;
        if(f == f1)
            return f;
        if(f > f1)
            if(f < f1 + f2)
                return f;
            else
                return f1 + f2;
        if(f > f1 - f2)
            return f;
        else
            return f1 - f2;
    }

    public boolean computeRenderPos(Actor actor, Loc loc, Loc loc1)
    {
        long l = Time.currentReal();
        if(l != prevTime)
        {
            long l1 = l - prevTime;
            prevTime = l;
            if (!CommonTools.equals(_Azimut, Azimut) || !CommonTools.equals(_Tangage, Tangage))
            //if(_Azimut != Azimut || _Tangage != Tangage)
            {
                Azimut = bvalue(_Azimut, Azimut, l1);
                Tangage = bvalue(_Tangage, Tangage, l1);
            }
        }
        computePos(actor, loc, loc1);
        return true;
    }

    public void computePos(Actor actor, Loc loc, Loc loc1)
    {
        if(bUse && mover != null)
        {
            
            if(Config.cur.inertiaGunnerEnabled/* && !Mission.isNet()*/)
                oGunMove.interpolate(oGunMove, toGunMove, Config.cur.inertiaGunnerValue);
            else
                oGunMove.interpolate(oGunMove, toGunMove, 1.0F);
            
            oGunMove.wrap360();
            mover.moveGun(oGunMove);
            
            if(World.cur().diffCur.Head_Shake)
            {
                Aircraft aircraft = World.getPlayerAircraft();
                if(Actor.isValid(aircraft))
                {
                    long l = Time.current();
                    if(l != tstamp && !aircraft.FM.Gears.onGround())
                    {
                        tstamp = l;
                        headRoll(aircraft);
                    }
                }
            }
            loc1.add(L, loc1);
            o.set(Azimut, Tangage, 0.0F);
            
//            loc1.getOrient().add(o);
            if(Time.isPaused() && !wPaused)
                lo.set(o);
            if(Config.cur.inertiaGunnerEnabled)
                lo.interpolate(lo, o, Config.cur.inertiaGunnerValue);
            else
                lo.interpolate(lo, o, 1.0F);
            lo.wrap360();
            loc1.getOrient().add(lo);
            wPaused = Time.isPaused();
            
            mover.getHookCameraGun().computePos(actor, loc, loc1);
        } else
        {
            loc1.set(loc);
        }
    }

    public void setTarget(Actor actor)
    {
        target = actor;
    }

    public void setTarget2(Actor actor)
    {
        target2 = actor;
    }

    public boolean use(boolean flag)
    {
        boolean flag1 = bUse;
        bUse = flag;
        if(Actor.isValid(target))
            target.pos.inValidate(true);
        if(Actor.isValid(target2))
            target2.pos.inValidate(true);
        if(bUse)
            current = this;
        else
            current = null;
        return flag1;
    }

    public void gunFire(boolean flag)
    {
        if(mover == null)
        {
            return;
        } else
        {
            mover.doGunFire(flag);
            return;
        }
    }

    public void mouseMove(int i, int j, int k)
    {
        if(mover == null)
            return;
//        oGunMove.set(oGunMove.azimut() - (float)i * HookView.koofAzimut, oGunMove.tangage() + (float)j * HookView.koofTangage, 0.0F);
//        oGunMove.wrap();
//        mover.clipAnglesGun(oGunMove);
//        mover.moveGun(oGunMove);
        toGunMove.set(toGunMove.azimut() - (float)i * HookView.koofAzimut, toGunMove.tangage() + (float)j * HookView.koofTangage, 0.0F);
        toGunMove.wrap();
        mover.clipAnglesGun(toGunMove);

        
        if(Actor.isValid(target))
            target.pos.inValidate(true);
        if(Actor.isValid(target2))
            target2.pos.inValidate(true);
    }

    public static void doSnapSet(float f, float f1)
    {
        for(int i = 0; i < all.size(); i++)
        {
            HookGunner hookgunner = (HookGunner)all.get(i);
            hookgunner.snapSet(f, f1);
        }

    }

    public void snapSet(float f, float f1)
    {
        if(!bUse)
            return;
        _Azimut = 45F * f;
        _Tangage = 44F * f1;
        if(Actor.isValid(target))
            target.pos.inValidate(true);
        if(Actor.isValid(target2))
            target2.pos.inValidate(true);
    }

    public static void doPanSet(int i, int j)
    {
        for(int k = 0; k < all.size(); k++)
        {
            HookGunner hookgunner = (HookGunner)all.get(k);
            hookgunner.panSet(i, j);
        }

    }

    public void panSet(int i, int j)
    {
        if(!bUse)
            return;
        if(i == 0 && j == 0)
        {
            _Azimut = 0.0F;
            _Tangage = 0.0F;
        }
        _Azimut = (float)i * stepAzimut + _Azimut;
        if(_Azimut < -maxAzimut)
            _Azimut = -maxAzimut;
        if(_Azimut > maxAzimut)
            _Azimut = maxAzimut;
        _Tangage = (float)j * stepTangage + _Tangage;
        if(_Tangage < minTangage)
            _Tangage = minTangage;
        if(_Tangage > maxTangage)
            _Tangage = maxTangage;
        if(Actor.isValid(target))
            target.pos.inValidate(true);
        if(Actor.isValid(target2))
            target2.pos.inValidate(true);
    }

    public void viewSet(float f, float f1)
    {
        if(!bUse)
            return;
        f %= 360F;
        if(f > 180F)
            f -= 360F;
        else
        if(f < -180F)
            f += 360F;
        f1 %= 360F;
        if(f1 > 180F)
            f1 -= 360F;
        else
        if(f1 < -180F)
            f1 += 360F;
        if(f < -maxAzimut)
            f = -maxAzimut;
        else
        if(f > maxAzimut)
            f = maxAzimut;
        if(f1 > maxTangage)
            f1 = maxTangage;
        else
        if(f1 < minTangage)
            f1 = minTangage;
        _Azimut = Azimut = f;
        _Tangage = Tangage = f1;
        if(Actor.isValid(target))
            target.pos.inValidate(true);
        if(Actor.isValid(target2))
            target2.pos.inValidate(true);
    }

    public static void saveRecordedStates(PrintWriter printwriter)
        throws Exception
    {
        if(current == null)
        {
            printwriter.println(0);
            printwriter.println(0);
            printwriter.println(0);
            printwriter.println(0);
        } else
        {
            printwriter.println(current.Azimut);
            printwriter.println(current._Azimut);
            printwriter.println(current.Tangage);
            printwriter.println(current._Tangage);
        }
    }

    public static void loadRecordedStates(BufferedReader bufferedreader)
        throws Exception
    {
        save_Azimut = Float.parseFloat(bufferedreader.readLine());
        save__Azimut = Float.parseFloat(bufferedreader.readLine());
        save_Tangage = Float.parseFloat(bufferedreader.readLine());
        save__Tangage = Float.parseFloat(bufferedreader.readLine());
    }

    public HookGunner(Actor actor, Actor actor1)
    {
        stepAzimut = 45F;
        stepTangage = 30F;
        maxAzimut = 135F;
        maxTangage = 89F;
        minTangage = -60F;
        Azimut = 0.0F;
        Tangage = 0.0F;
        _Azimut = 0.0F;
        _Tangage = 0.0F;
        prevTime = 0L;
        tstamp = -1L;
        roolTime = -1L;
        bUse = false;
        oGunMove = new Orient();
        L = new Loc();
        o = new Orient();
        Azimut = save_Azimut;
        Tangage = save_Tangage;
        _Azimut = save__Azimut;
        _Tangage = save__Tangage;
        setTarget(actor);
        setTarget2(actor1);
        all.add(this);
    }

    public static HookGunner current()
    {
        return current;
    }

    private float stepAzimut;
    private float stepTangage;
    private float maxAzimut;
    private float maxTangage;
    private float minTangage;
    private float Azimut;
    private float Tangage;
    private float _Azimut;
    private float _Tangage;
    private long prevTime;
    private float Px;
    private float Py;
    private float Pz;
    private long tstamp;
    private long roolTime;
    private Move mover;
    private Actor target;
    private Actor target2;
    private boolean bUse;
    private Orient oGunMove;
    private Loc L;
    private Orient o;
    private static float save_Azimut = 0.0F;
    private static float save_Tangage = 0.0F;
    private static float save__Azimut = 0.0F;
    private static float save__Tangage = 0.0F;
    private static HookGunner current;
    private static ArrayList all = new ArrayList();

    // +++ Visual Mod +++
    private Orient lo = new Orient();
    private Orient toGunMove = new Orient();
    private static boolean wPaused = false;
    // --- Visual Mod ---
}
