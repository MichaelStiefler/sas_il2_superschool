// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode
// Source File Name:   HookView.java

package com.maddox.il2.engine.hotkey;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TestRunway;
import com.maddox.il2.objects.ships.TransparentTestRunway;
import com.maddox.rts.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.game.HookKeys;

public class HookView extends HookRender
{
    static class ClipFilter
        implements ActorFilter
    {
        public boolean isUse(Actor actor, double d)
        {
            return actor instanceof BigshipGeneric;
        }

        ClipFilter()
        {
        }
    }

    public boolean getFollow()
    {
        return bFollow;
    }

//    Stock 4.12.2
//    public void setFollow(boolean flag)
//    {
//        bFollow = flag;
//    }

//  GoPro
    public void setFollow(boolean flag)
    {
        if(flag && Config.isUSE_RENDER()) {
            overrideActorChange = true;
            overrideActor = Main3D.cur3D().viewActor();
        } else
        {
            overrideActorChange = false;
        }

        if(bFollow && flag && sameActorSelected)
        {
            hookViewIndex++;
            if(Time.real() > hookViewIndexTime + 2000L)
                hookViewIndex--;
            hookViewIndexTime = Time.real();
            setHookView();
        }

        sameActorSelected = false;
        bFollow = flag;
    }

    protected boolean isUseCommon()
    {
        return useFlags != 0;
    }

    protected void useCommon(int i, boolean flag)
    {
        if(bUse)
        {
            useFlags |= i;
        } else
        {
            useFlags &= ~i;
// GoPro
            sameActorSelected = false;
            lastBaseActor = null;
            overrideActorChange = false;
            overrideActor = null;
        }
    }

    protected static float minLen()
    {
        if(_visibleR > 0.0F)
            return _visibleR + 1.5F;
        else
            return _minLen;
    }

    public static float defaultLen()
    {
        if(_visibleR > 0.0F)
            return _visibleR * 3F;
        else
            return _defaultLen;
    }

    protected static float maxLen()
    {
        return _maxLen;
    }

    public float len()
    {
        return len;
    }

    public static boolean isCinemaMode()
    {
//      Disable NG-CAM effects in intros, training and tracks (non-manual)
        return !HotKeyEnv.isEnabled("SnapView");
    }

    public static boolean isCamEnabled()
    {
        return (!isCinemaMode() && ((HookKeys.current.isPanView() && isPanKillSwitch == 0) || (!HookKeys.current.isPanView() && isPanKillSwitch == 1)));
    }

    public void resetGame()
    {
        lastBaseActor = null;
        _visibleR = -1F;
        _Azimut = Azimut = 0.0F;
        _Tangage = Tangage = 0.0F;
        timeViewSet = -2000L;
        isPanView = HookKeys.current.isPanView();
        isPanKillSwitch = (int)Config.cur.ini.get("Mods", "ngCAMkillswitch", 0, 0, 1);
        iniSoftFactor = (int)Config.cur.ini.get("Mods", "ngCAMspeedext", 2, 0, 3);
        isLsoView = false;
// GoPro
        hookViewIndex = 0;
        overrideActorChange = false;
    }

    public void saveRecordedStates(PrintWriter printwriter)
        throws Exception
    {
        printwriter.println(len);
        printwriter.println(Azimut);
        printwriter.println(_Azimut);
        printwriter.println(Tangage);
        printwriter.println(_Tangage);
        printwriter.println(o.azimut());
        printwriter.println(o.tangage());
        printwriter.println(koofAzimut);
        printwriter.println(koofTangage);
        printwriter.println(koofLen);
        printwriter.println(_minLen);
        printwriter.println(_defaultLen);
        printwriter.println(_maxLen);
        printwriter.println(koofSpeed);
    }

    public void loadRecordedStates(BufferedReader bufferedreader)
        throws Exception
    {
        len = Float.parseFloat(bufferedreader.readLine());
        Azimut = Float.parseFloat(bufferedreader.readLine());
        _Azimut = Float.parseFloat(bufferedreader.readLine());
        Tangage = Float.parseFloat(bufferedreader.readLine());
        _Tangage = Float.parseFloat(bufferedreader.readLine());
        o.set(Float.parseFloat(bufferedreader.readLine()), Float.parseFloat(bufferedreader.readLine()), 0.0F);
        koofAzimut = Float.parseFloat(bufferedreader.readLine());
        koofTangage = Float.parseFloat(bufferedreader.readLine());
        koofLen = Float.parseFloat(bufferedreader.readLine());
        _minLen = Float.parseFloat(bufferedreader.readLine());
        _defaultLen = Float.parseFloat(bufferedreader.readLine());
        _maxLen = Float.parseFloat(bufferedreader.readLine());
        koofSpeed = Float.parseFloat(bufferedreader.readLine());
    }

    public void reset()
    {
// GoPro
        overrideActorChange = false;
        timeViewSet = -2000L;
        if(AircraftHotKeys.bFirstHotCmd)
        {
            return;
        } else
        {
            set(lastBaseActor, defaultLen(), 0.0F, 0.0F);
            return;
        }
    }

    public void set(Actor actor, float f, float f1)
    {
// GoPro
        if(bFollow && !(actor instanceof Aircraft))
            setFollow(false);

        if(overrideActorChange && Config.isUSE_RENDER())
        {
            actor = overrideActor;
            if(Main3D.cur3D().viewActor() != actor)
                Main3D.cur3D().setView(actor);
        }
        if(lastBaseActor == actor && f == 10F && f1 == -10F)
            sameActorSelected = true;
// GoPro - ???
//        set(actor, defaultLen() > 5.0F ? defaultLen() : 5.0F, f, f1);
        set(actor, defaultLen(), f, f1);
    }

    public void set(Actor actor, float f, float f1, float f2)
    {
// GoPro
        if(bFollow && !(actor instanceof Aircraft))
            setFollow(false);

        if(overrideActorChange && Config.isUSE_RENDER())
        {
            actor = overrideActor;
            if(Main3D.cur3D().viewActor() != actor)
                Main3D.cur3D().setView(actor);
        }

        lastBaseActor = actor;
        _visibleR = -1F;
        if(Actor.isValid(actor) && (actor instanceof ActorMesh))
            _visibleR = ((ActorMesh)actor).mesh().visibilityR();
        o.set(f1, f2, 0.0F);

// GoPro
        oCam.set(0.0F, 0.0F, 0.0F);
        pCam.set(0.0D, 0.0D, 0.0D);
        oView.set(0.0F, 0.0F, 0.0F);
        pView.set(0.0D, 0.0D, 0.0D);

        _Azimut = Azimut = 0.0F;
        _Tangage = Tangage = 0.0F;
        prevTime = Time.real();
        len = f;
        if(camera != null)
        {
            Actor actor1 = camera.pos.base();
            if(actor1 != null)
            {
//                if(bFollow)
//                {
//                    o.set(0.0F, 0.0F, 0.0F);
//                    oIn.set(0.0F, -8F, 0.0F);
//                }
//                else
                if(isCamEnabled() && iniSoftFactor > 0)
                {
                    bFirstPan = true;
                    o.set(f1, f2, 0.0F);
                    oIn.set(f1 + 5F, f2 - 5F, 0.0F);

                } else
                {
                    actor1.pos.getAbs(o);
                    o.increment(f1, f2, 0.0F);
                    o.set(o.azimut(), o.tangage(), 0.0F);
                }

                o6DOF.set(0.0F, 0.0F, 0.0F);
// GoPro
//                oCam.set(0.0F, 0.0F, 0.0F);
//                pCam.set(0.0D, 0.0D, 0.0D);
            }
            if(bUse)
                camera.pos.inValidate(true);
        }
        o.wrap();
        o6DOF.wrap();
    }

    private float bvalue(float f, float f1, long l)
    {
        float f2 = (koofSpeed * (float)l) / 30F;
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
        if(!bFollow && isPanView != HookKeys.current.isPanView())
        {
            isPanView = HookKeys.current.isPanView();
//            timeViewSet = -2000L;
            set(lastBaseActor, defaultLen(), 10F, -10F);
        }

        long l = Time.currentReal();
        if(l != prevTime)
        {
            long l1 = l - prevTime;
            prevTime = l;
            if(_Azimut != Azimut || _Tangage != Tangage)
            {
                Azimut = bvalue(_Azimut, Azimut, l1);
                Tangage = bvalue(_Tangage, Tangage, l1);
                loc.get(o);
                float f = o.azimut() + Azimut;
                o.set(f, Tangage, 0.0F);
                o.wrap360();
            }
            if(_Azimut == Azimut && (Azimut > 180F || Azimut < -180F))
            {
                Azimut %= 360F;
                if(Azimut > 180F)
                    Azimut -= 360F;
                else
                if(Azimut < -180F)
                    Azimut += 360F;
                _Azimut = Azimut;
            }
            if(_Tangage == Tangage && (Tangage > 180F || Tangage < -180F))
            {
                Tangage %= 360F;
                if(Tangage > 180F)
                    Tangage -= 360F;
                else
                if(Tangage < -180F)
                    Tangage += 360F;
                _Tangage = Tangage;
            }
        }
        computePos(actor, loc, loc1);
        return true;
    }

//    Stock 4.12
//    public void computePos(Actor actor, Loc loc, Loc loc1)
//    {
//        loc.get(pAbs, oAbs);
//        if(bUse)
//        {
//            if(lastBaseActor != actor)
//            {
//                lastBaseActor = actor;
//                _visibleR = -1F;
//                if(Actor.isValid(actor) && (actor instanceof ActorMesh))
//                    _visibleR = ((ActorMesh)actor).mesh().visibilityR();
//            }
//            p.set(-len, 0.0D, 0.0D);
//            if(bFollow)
//                o.set(oAbs.getAzimut(), o.getTangage(), o.getKren());
//            o.transform(p);
//            pAbs.add(p);
//            if(bClipOnLand)
//            {
//                double d = Engine.land().HQ_Air(pAbs.x, pAbs.y) + 2D;
//                if(pAbs.z < d)
//                    pAbs.z = d;
//                pClipZ1.set(pAbs);
//                pClipZ2.set(pAbs);
//                pClipZ1.z -= 2D;
//                pClipZ2.z += 42D;
//                Actor actor1 = Engine.collideEnv().getLine(pClipZ2, pClipZ1, false, clipFilter, pClipRes);
//                if(Actor.isValid(actor1) && pAbs.z < pClipRes.z + 2D)
//                    pAbs.z = pClipRes.z + 2D;
//            }
//            loc1.set(pAbs, o);
//        } else
//        {
//            loc1.set(pAbs, oAbs);
//        }
//    }

    public void computePos(Actor actor, Loc loc, Loc loc1)
    {
//        if(Time.current() > 0L)
        if(Mission.isPlaying())
        {

            if(bFollow && (actor instanceof Aircraft))
                computeFollowPos(actor, loc, loc1);
            else if(isCamEnabled() && iniSoftFactor > 0)
                computeInertiaPos(actor, loc, loc1);
            else if((actor instanceof BigshipGeneric) && !isCamEnabled() && !isCinemaMode())
                computeCarrierPos(actor, loc, loc1);
            else
                computeDirectPos(actor, loc, loc1);
        } else
        {
            computeDirectPos(actor, loc, loc1);
        }
    }

//  GoPro
    public void computeFollowPos(Actor actor, Loc loc, Loc loc1)
    {
        isLsoView = false;
        if(hookViewIndex == 0)
        {
            loc.get(pAbs, oAbs);
            if(bUse)
            {
                if(lastBaseActor != actor)
                {
                    lastBaseActor = actor;
                    _visibleR = -1F;
                    if(Actor.isValid(actor) && (actor instanceof ActorMesh))
                        _visibleR = ((ActorMesh)actor).mesh().visibilityR();
                }
                p.set(-len, 0.0D, 0.0D);
                o.set(oAbs.getAzimut(), o.getTangage(), o.getKren());
                o.transform(p);
                oView.set(o);
                oView.increment(oCam);
                pAbs.add(p);
                if(bClipOnLand)
                {
                    double d = Engine.land().HQ_Air(pAbs.x, pAbs.y) + 2D;
                    if(pAbs.z < d)
                        pAbs.z = d;
                    pClipZ1.set(pAbs);
                    pClipZ2.set(pAbs);
                    pClipZ1.z -= 2D;
                    pClipZ2.z += 42D;
                    Actor actor1 = Engine.collideEnv().getLine(pClipZ2, pClipZ1, false, clipFilter, pClipRes);
                    if(Actor.isValid(actor1) && pAbs.z < pClipRes.z + 2D)
                        pAbs.z = pClipRes.z + 2D;
                }
                pView.set(pAbs);
                pView.add(pCam);
                loc1.set(pView, oView);
            } else
            {
                loc1.set(pAbs, oAbs);
            }
        } else
        {
            loc.get(pAbs, oAbs);
            p.set(pExt);
            oAbs.transform(p);
            pAbs.add(p);
            if(bClipOnLand)
            {
                double d = Engine.land().HQ_Air(pAbs.x, pAbs.y) + 2D;
                if(pAbs.z < d)
                    pAbs.z = d;
                pClipZ1.set(pAbs);
                pClipZ2.set(pAbs);
                pClipZ1.z -= 2D;
                pClipZ2.z += 42D;
                Actor actor1 = Engine.collideEnv().getLine(pClipZ2, pClipZ1, false, clipFilter, pClipRes);
                if(Actor.isValid(actor1) && pAbs.z < pClipRes.z + 2D)
                    pAbs.z = pClipRes.z + 2D;
            }
            oAbs.increment(oExt);
            oAbs.increment(oCam);
            oView.set(oExt);
            oView.increment(oCam);
            loc1.set(pAbs, oAbs);
        }
    }


//  VisualMod
    public void computeInertiaPos(Actor actor, Loc loc, Loc loc1)
    {
        isLsoView = false;
        long l = Time.currentReal();
        float fi = (float)(l - pTime) / 10F;
        pTime = l;
        loc.get(pAbs, oAbs);
        if(bUse)
        {
            if(lastBaseActor != actor)
            {
                lastBaseActor = actor;
                _visibleR = -1F;
                if(Actor.isValid(actor) && (actor instanceof ActorMesh))
                    _visibleR = ((ActorMesh)actor).mesh().visibilityR();
            }
            p.set(-len, 0.0D, 0.0D);

//            if(actor != null && Actor.isValid(actor) && (actor instanceof ActorMesh))
//            {
//                if((actor instanceof BigshipGeneric) && !(actor instanceof TestRunway) && Mission.isPlaying())
//                {
//                    p.set(0.0D, 0.0D, 0.0D);
//                    if(hookCarrier == null)
//                        hookCarrier = new HookNamed((BigshipGeneric)actor, "_Prop");
//                    if(hookCarrier == null)
//                    {
//                        Loc locC = new Loc();
//                        hookCarrier.computePos((BigshipGeneric)actor, new Loc(), locC);
//                        locC.add(loc);
//                        com.maddox.il2.ai.air.CellAirField cellairfield = ((BigshipGeneric)actor).getCellTO();
//                        double xView = 10D;
//                        double yView = (double)cellairfield.getWidth() * 0.40000000000000002D + 3D;
//                        double zView = ((Tuple3d) (pAbs)).z;
//                        Loc locD = new Loc(new Point3d(-xView, -yView, zView), o);
//                        locD.add(locC);
//                        locD.get(pAbs, oAbs);
//                    }
//                }
//            }

// inEI = PAL3DExternalSoftView

            if(inEI * fi <= 1.0F)
                oIn.interpolate(oIn, o, inEI * fi);
            else
                oIn.interpolate(oIn, o, 1F);
            oIn.wrap360();
            oIn.transform(p);

            if(Time.isPaused() || iniSoftFactor == 3 || (!(actor instanceof Aircraft) && actor != myLatestReleasedOrdinance()))
                fi *= 10F;
            else if(iniSoftFactor == 2)
                fi *= 2F;

            if(actor != null)
            {
                if(Actor.isValid(actor) && (actor instanceof ActorMesh))
                {
                    if(!actor.isAlive())
                        oAbs.set(oAbs.getAzimut(), 0.0F, 0.0F);
                    else
                    if(actor instanceof Aircraft)
                    {
                        Aircraft aircraft = (Aircraft)actor;
                        if(aircraft.FM.Gears.getLandingState() > 0.0F)
                            oAbs.set(oAbs.getAzimut(), 0.0F, 0.0F);
                        else if(aircraft.FM.getAOA() > aircraft.FM.AOA_Crit)
                            fi *= 5F;
                    }
                } else
                {
                    oAbs.set(oAbs.getAzimut(), 0.0F, 0.0F);
                }
            }

// inA = PAL3DFollowInertiaAngle

            if(!bFirstPan && (inA * fi <= 1.0F))
                oAbsIn.interpolate(oAbsIn, oAbs, inA * fi);
            else
                oAbsIn.interpolate(oAbsIn, oAbs, 1F);
            oAbsIn.wrap360();
            orv.set(oAbsIn);
            orv.increment(oIn);
            orv.increment(o6DOF);
            oAbs.wrap360();
//            if(oAdj || oAdjM)
//            {
//                oAbs.transform(p);
//                oAbsIn.set(oAbs);
//                lorv.set(oAbsIn);
//                lp.set(p);
//                oAdj = oAdjM = false;
//            } else
//            {
                oAbsIn.transform(p);

// Reset roll when following land/sea actors

                if(!(actor instanceof Aircraft) && actor != myLatestReleasedOrdinance())
                    orv.setRoll(0.0F);

// inA = PAL3DFollowInertiaAngle

                if(!bFirstPan && (inA * fi <= 1.0F))
                    orv.interpolate(lorv, orv, inA * fi);
                else
                    orv.interpolate(lorv, orv, 1F);
                orv.wrap360();
                lorv.set(orv);
                oAbs.set(orv);

// bFirstPan = remove the initial change of "len" of the camera
// inF = PAL3DFollowInertia

                if(!bFirstPan && (inF * (double)fi <= 1.0D))
                    lp.interpolate(lp, p, inF * (double)fi);
                else
                    lp.interpolate(lp, p, 1F);
                p.set(lp);
//            }
            pAbs.add(p);
            if(bClipOnLand)
            {
                double d = Engine.land().HQ_Air(((Tuple3d) (pAbs)).x, ((Tuple3d) (pAbs)).y) + 2D;
                if(((Tuple3d) (pAbs)).z < d)
                    pAbs.z = d;
                pClipZ1.set(pAbs);
                pClipZ2.set(pAbs);
                pClipZ1.z -= 2D;
                pClipZ2.z += 42D;
                Actor actor1 = Engine.collideEnv().getLine(pClipZ2, pClipZ1, false, clipFilter, pClipRes);
                if(Actor.isValid(actor1) && ((Tuple3d) (pAbs)).z < ((Tuple3d) (pClipRes)).z + 2D)
                    pAbs.z = ((Tuple3d) (pClipRes)).z + 2D;
            }
            loc1.set(pAbs, oAbs);

        } else
        {
            loc1.set(pAbs, oAbs);
        }

        bFirstPan = false;
    }

    public void computeCarrierPos(Actor actor, Loc loc, Loc loc1)
    {
        if(!(actor instanceof TestRunway) && !(actor instanceof TransparentTestRunway) && Actor.isAlive(actor) && (((BigshipGeneric)actor).getAirport() != null))
        {
            loc.get(pAbs, oAbs);
            if(bUse)
            {
                if(lastBaseActor != actor)
                {
                    lastBaseActor = actor;
                    _visibleR = -1F;
                    if(Actor.isValid(actor) && (actor instanceof ActorMesh))
                        _visibleR = ((ActorMesh)actor).mesh().visibilityR();
                }
                p.set(0.0D, 0.0D, 0.0D);
                o.transform(p);
                pAbs.add(p);
                if(bClipOnLand)
                {
                    double d = Engine.land().HQ_Air(pAbs.x, pAbs.y) + 2D;
                    if(pAbs.z < d)
                        pAbs.z = d;
                    pClipZ1.set(pAbs);
                    pClipZ2.set(pAbs);
                    pClipZ1.z -= 2D;
                    pClipZ2.z += 42D;
                    Actor actor1 = Engine.collideEnv().getLine(pClipZ2, pClipZ1, false, clipFilter, pClipRes);
                    if(Actor.isValid(actor1) && pAbs.z < pClipRes.z + 2D)
                        pAbs.z = pClipRes.z + 2D;
                }
                try
                {
                    Loc loc2 = new Loc();
                    HookNamed hooknamed = new HookNamed((BigshipGeneric)actor, "_Prop");
                    hooknamed.computePos((BigshipGeneric)actor, new Loc(), loc2);
                    loc2.add(loc);
                    com.maddox.il2.ai.air.CellAirField cellairfield = ((BigshipGeneric)actor).getCellTO();
//                    double d1 = 10D;
                    double d1 = 12D;
                    double d2 = (double)cellairfield.getWidth() * 0.4D + 3D;
//                    double d3 = pAbs.z;
                    double d3 = pAbs.z + 1D;
                    Loc loc3 = new Loc(new Point3d(-d1, -d2, d3), o);
                    loc3.add(loc2);
                    loc1.set(loc3.getPoint(), o);
                    isLsoView = true;
                }
                catch(Exception exception)
                {
                    computeDirectPos(actor, loc, loc1);
                }

            } else
            {
                loc1.set(pAbs, oAbs);
            }
        } else
        {
            computeDirectPos(actor, loc, loc1);
        }
    }

    public void computeDirectPos(Actor actor, Loc loc, Loc loc1)
    {
        isLsoView = false;
        loc.get(pAbs, oAbs);
        if(bUse)
        {
            if(lastBaseActor != actor)
            {
                lastBaseActor = actor;
                _visibleR = -1F;
                if(Actor.isValid(actor) && (actor instanceof ActorMesh))
                    _visibleR = ((ActorMesh)actor).mesh().visibilityR();
            }
            p.set(-len, 0.0D, 0.0D);
//            if(bFollow)
//                o.set(oAbs.getAzimut(), o.getTangage(), o.getKren());
            o.transform(p);
// GoPro
            oView.set(o);
            oView.increment(oCam);
// No roll
            oView.setRoll(0.0F);

            pAbs.add(p);
            if(bClipOnLand)
            {
                double d = Engine.land().HQ_Air(((Tuple3d) (pAbs)).x, ((Tuple3d) (pAbs)).y) + 2D;
                if(((Tuple3d) (pAbs)).z < d)
                    pAbs.z = d;
                pClipZ1.set(pAbs);
                pClipZ2.set(pAbs);
                pClipZ1.z -= 2D;
                pClipZ2.z += 42D;
                Actor actor1 = Engine.collideEnv().getLine(pClipZ2, pClipZ1, false, clipFilter, pClipRes);
                if(Actor.isValid(actor1) && ((Tuple3d) (pAbs)).z < ((Tuple3d) (pClipRes)).z + 2D)
                    pAbs.z = ((Tuple3d) (pClipRes)).z + 2D;
            }
// GoPro
              pView.set(pAbs);
              pView.add(pCam);
              loc1.set(pView, oView);

//            loc1.set(pAbs, o);
        } else
        {
            loc1.set(pAbs, oAbs);
        }
    }

    public void setCamera(Actor actor)
    {
        camera = actor;
    }

    public void use(boolean flag)
    {
        bUse = flag;
        if(camera != null)
            camera.pos.inValidate(true);
        useCommon(1, bUse);
    }

    public boolean isUse()
    {
        return bUse;
    }

    public static void useMouse(boolean flag)
    {
        bUseMouse = flag;
    }

    public static boolean isUseMouse()
    {
        return bUseMouse;
    }

    public boolean clipOnLand(boolean flag)
    {
        boolean flag1 = bClipOnLand;
        bClipOnLand = flag;
        return flag1;
    }

    protected void clipLen(Actor actor)
    {
        if(len < minLen())
            if(Actor.isValid(actor) && (actor instanceof ActorViewPoint))
            {
                if(len < -maxLen())
                    len = -maxLen();
            } else
            {
                len = minLen();
            }
        if(len > maxLen())
            len = maxLen();
    }


    protected void mouseMove(int i, int j, int k)
    {
        if(bUseMouse && isUseCommon())
        {
            if(!bFollow)
            {
                if(bChangeLen && !isLsoView)
                {
                    len += (float)j * koofLen * 0.5F;
                    clipLen(lastBaseActor);
                } else
                {
                    if(Time.real() < timeViewSet + 1000L)
                        return;

                    if(bUse)
                    {
                        if(Mission.isPlaying() && !isCinemaMode() && Mouse.adapter().isPressed(1) && !(Main3D.cur3D().viewActor() instanceof ActorViewPoint) && !isLsoView)
                        {
                            if(isCamEnabled() && iniSoftFactor > 0)
                            {
                                o6DOF.set(o6DOF.getAzimut() + (float)i * koofAzimut * 0.5F, o6DOF.getTangage() + (float)j * koofTangage * 0.5F, o6DOF.getKren());
                                oAbs.wrap360();
                            } else
                            {
                                oCam.set(oCam.getAzimut() + (float)i * koofAzimut * 0.25F, oCam.getTangage() + (float)j * koofTangage * 0.25F, oCam.getKren());
                                oCam.wrap360();
                            }
                        } else
                        {
                            o.set(o.azimut() + (float)i * koofAzimut, o.tangage() + (float)j * koofTangage, 0.0F);
                            o.wrap360();
                        }
                    }
                }

            } else
            {
                if(!isCinemaMode() && Mouse.adapter().isPressed(1))
                {
                    oCam.set(oCam.getAzimut() + (float)i * koofAzimut * 0.25F, oCam.getTangage() + (float)j * koofTangage * 0.25F, oCam.getKren());
                    oCam.wrap360();
                } else
                {
                    if(hookViewIndex > 0)
                    {
                        pCam.set(0.0D, 0.0D, 0.0D);
                        if(bChangeLen)
                        {
                            pCam.set(-(float)j * koofLen * 0.01F, 0.0D, 0.0D);
                        } else
                        {
                            pCam.set(0.0D, -(float)i * koofAzimut * 0.01F, (float)j * koofTangage * 0.01F);
                        }
                        oView.transform(pCam);
                        pExt.add(pCam);

                    } else
                    {
                        if(bChangeLen)
                        {
                            len += (float)j * koofLen * 0.1F;
                            clipLen(lastBaseActor);
                        } else
                        {
                            if(Time.real() < timeViewSet + 1000L)
                                return;
//                            if(bFollow)
                                i = 0;
                            if(bUse)
                            {
                                o.set(o.azimut() + (float)i * koofAzimut, o.tangage() + (float)j * koofTangage, o.kren());
                                o.wrap360();
                            }
                        }
                    }
                }
            }

            if(Actor.isValid(camera))
                camera.pos.inValidate(true);
            Azimut = _Azimut;
            Tangage = _Tangage;
        }
    }

    public void viewSet(float f, float f1)
    {
        if(!bUseMouse && !isUseCommon())
            return;
        if(isUseCommon())
            return;
        timeViewSet = Time.real();
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
        if(bFollow)
        {
            o.set(o.azimut(), f1, 0.0F);
            o.wrap360();
        } else
        {
            Azimut = _Azimut = f;
            Tangage = _Tangage = f1;
            o.set(Azimut, Tangage, 0.0F);
        }
        if(Actor.isValid(camera))
            camera.pos.inValidate(true);
    }

    public void snapSet(float f, float f1)
    {
        if(!bUse || !bUseMouse || !isUseCommon())
            return;
        _Azimut = 45F * f;
        _Tangage = 44F * f1;
        if(camera != null)
        {
            Actor actor = camera.pos.base();
            if(actor != null)
            {
//                if((actor instanceof BigshipGeneric) && !(actor instanceof TestRunway) && Mission.isPlaying())
//                    _Azimut += 180F;

                Azimut = (o.azimut() - actor.pos.getAbsOrient().azimut()) % 360F;
                if(Azimut > _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut -= 360F);
                else
                if(Azimut < _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut += 360F);
                Tangage = o.tangage() % 360F;
                if(Tangage > _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage -= 360F);
                else
                if(Tangage < _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage += 360F);
                camera.pos.inValidate(true);
            }

//            if(!Config.cur.bMouseI)
//                oAdj = true;
        }
    }

    public void panSet(int i, int j)
    {
        if(!bUse || !bUseMouse || !isUseCommon())
            return;
        if(i == 0 && j == 0)
        {
            _Azimut = 0.0F;
            _Tangage = 0.0F;
        }
        _Azimut = (float)i * stepAzimut + _Azimut;
        _Tangage = (float)j * stepTangage + _Tangage;
        if(camera != null)
        {
            Actor actor = camera.pos.base();
            if(actor != null)
            {
//                if((actor instanceof BigshipGeneric) && !(actor instanceof TestRunway) && Mission.isPlaying() && bFirstPan)
//                {
//                    _Azimut += 180F;
//                    bFirstPan = false;
//                }
//                Azimut = ((o.azimut() - actor.pos.getAbsOrient().azimut()) % 360F + 540F) % 360F - 180F;
//                if(bFirstPan)
//                {
//                    _Azimut += Azimut + (i < 0 ? 30F : -30F);
//                    bFirstPan = false;
//                }
//                if(Azimut > _Azimut)
//                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut -= 360F);
//                else
//                if(Azimut < _Azimut)
//                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut += 360F);
//                Tangage = o.tangage() % 360F;
//                if(Tangage > _Tangage)
//                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage -= 360F);
//                else
//                if(Tangage < _Tangage)
//                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage += 360F);
//                camera.pos.inValidate(true);

                Azimut = (o.azimut() - actor.pos.getAbsOrient().azimut()) % 360F;
                if(Azimut > _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut -= 360F);
                else
                if(Azimut < _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut += 360F);
                Tangage = o.tangage() % 360F;
                if(Tangage > _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage -= 360F);
                else
                if(Tangage < _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage += 360F);
                camera.pos.inValidate(true);
            }

//            if(!Config.cur.bMouseI)
//                oAdj = true;
        }
    }

    private void initHotKeys(String s)
    {
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdMouseMove(true, "Move") {

            public void move(int i, int j, int k)
            {
                mouseMove(i, j, k);
            }

            public void created()
            {
                setRecordId(27);
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "Reset") {

            public void begin()
            {
                if(isUseCommon())
                    reset();
            }

            public void created()
            {
                setRecordId(28);
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "Len") {

            public void begin()
            {
                if(isUseCommon())
                    HookView.bChangeLen = true;
            }

            public void end()
            {
                if(isUseCommon())
                    HookView.bChangeLen = false;
            }

            public void created()
            {
                setRecordId(29);
            }

        }
);
        String sAV = "aircraftView";
        HotKeyCmdEnv.setCurrentEnv(sAV);
        HotKeyEnv.fromIni(sAV, Config.cur.ini, "HotKey " + sAV);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OrdinanceView", "52") {

            public void created()
            {
                setRecordId(246);
            }

            public void begin()
            {
                if(World.cur().diffCur.No_Outside_Views)
                    return;
                Actor actor = myLatestReleasedOrdinance();
//                if(Actor.isValid(actor) && (actor != Main3D.cur3D().viewActor()))
                if(Actor.isValid(actor))
                {
                    boolean flag = !Main3D.cur3D().isViewOutside();
                    Main3D.cur3D().setViewFlow10(actor, false);
                }
            }
        }
);
    }

    private Actor myLatestReleasedOrdinance()
    {
        if(Selector.isEnableTrackArgs())
            return Selector.setCurRecordArg0(Selector.getTrackArg0());
        World.cur();
        Aircraft aircraft = World.getPlayerAircraft();
        if(aircraft != null)
        {
            for(int i = Engine.ordinances().size() - 1; i >= 0; i--)
            {
                Actor actor = (Actor)Engine.ordinances().get(i);
                if(actor != null && actor.getOwner() == aircraft)
                {
                    String actorName = actor.getClass().getName().substring(actor.getClass().getName().lastIndexOf('.') + 1);
                    if(!actorName.equals("RocketNull") && !actorName.equals("BombNull"))
                        return Selector.setCurRecordArg0(actor);
                }
            }
        }
        return Selector.setCurRecordArg0(null);
    }

    public static void loadConfig()
    {
        koofAzimut = Config.cur.ini.get(sectConf, "AzimutSpeed", koofAzimut, 0.01F, 1.0F);
        koofTangage = Config.cur.ini.get(sectConf, "TangageSpeed", koofTangage, 0.01F, 1.0F);
        koofLen = Config.cur.ini.get(sectConf, "LenSpeed", koofLen, 0.1F, 10F);
        _minLen = Config.cur.ini.get(sectConf, "MinLen", _minLen, 1.0F, 20F);
        _defaultLen = Config.cur.ini.get(sectConf, "DefaultLen", _defaultLen, 1.0F, 100F);
        _maxLen = Config.cur.ini.get(sectConf, "MaxLen", _maxLen, 1.0F, 50000F);
        if(_defaultLen < _minLen)
            _defaultLen = _minLen;
        if(_maxLen < _defaultLen)
            _maxLen = _defaultLen;
        koofSpeed = Config.cur.ini.get(sectConf, "Speed", koofSpeed, 0.1F, 100F);
        koofLeanF = Config.cur.ini.get(sectConf, "LeanF", koofLeanF, 0.01F, 1.0F) * 0.5F;
        koofLeanS = Config.cur.ini.get(sectConf, "LeanS", koofLeanS, 0.01F, 1.0F) * 0.5F;
        koofRaise = Config.cur.ini.get(sectConf, "Raise", koofRaise, 0.01F, 1.0F) * 0.5F;
        rubberBandStretch = Config.cur.ini.get(sectConf, "RubberBand", rubberBandStretch, 0.0F, 1.0F) * 0.08F;
    }

    public static void saveConfig()
    {
        Config.cur.ini.setValue(sectConf, "LeanF", Float.toString(koofLeanF * 2.0F));
        Config.cur.ini.setValue(sectConf, "LeanS", Float.toString(koofLeanS * 2.0F));
        Config.cur.ini.setValue(sectConf, "Raise", Float.toString(koofRaise * 2.0F));
        Config.cur.ini.setValue(sectConf, "RubberBand", Float.toString(rubberBandStretch * 12.5F));
        Config.cur.ini.saveFile();
    }

    public HookView(String s)
    {
        camera = null;
        bUse = false;
        bClipOnLand = true;
        o = new Orientation();
        p = new Point3d();
        pAbs = new Point3d();
        oAbs = new Orient();
        bFollow = false;
        timeViewSet = -2000L;
        HotKeyEnv.fromIni(s, Config.cur.ini, s);
        sectConf = s + " Config";
        loadConfig();
        initHotKeys(s);
        current = this;

// GoPro
        hookViewIndex = 0;
        hookViewIndexTime = Time.real();
        pExt = new Point3d();
        oExt = new Orient();
        oCam = new Orientation();
        pCam = new Point3d();
        oView = new Orientation();
        pView = new Point3d();
        sameActorSelected = false;

// VisualMod
        bFirstPan = true;
        oAbsIn = new Orient();
        oIn = new Orient();
        o6DOF = new Orient();
        orv = new Orient();
        lorv = new Orient();
        lp = new Point3d();
//        oAdj = false;
//        oAdjM = false;

// NG-CAM: HookView
        isPanKillSwitch = (int)Config.cur.ini.get("Mods", "ngCAMkillswitch", 0, 0, 1);
        iniSoftFactor = (int)Config.cur.ini.get("Mods", "ngCAMspeedext", 2, 0, 3);
    }

    protected HookView()
    {
        camera = null;
        bUse = false;
        bClipOnLand = true;
        o = new Orientation();
        p = new Point3d();
        pAbs = new Point3d();
        oAbs = new Orient();
        bFollow = false;
        timeViewSet = -2000L;

// GoPro
        hookViewIndex = 0;
        hookViewIndexTime = Time.real();
        pExt = new Point3d();
        oExt = new Orient();
        oCam = new Orientation();
        pCam = new Point3d();
        oView = new Orientation();
        pView = new Point3d();
        sameActorSelected = false;

// VisualMod
        bFirstPan = true;
        oAbsIn = new Orient();
        oIn = new Orient();
        o6DOF = new Orient();
        orv = new Orient();
        lorv = new Orient();
        lp = new Point3d();
//        oAdj = false;
//        oAdjM = false;

// NG-CAM: HookView
        isPanKillSwitch = (int)Config.cur.ini.get("Mods", "ngCAMkillswitch", 0, 0, 1);
        Config.cur.ini.set("Mods", "ngCAMkillswitch", isPanKillSwitch);
        iniSoftFactor = (int)Config.cur.ini.get("Mods", "ngCAMspeedext", 2, 0, 3);
        Config.cur.ini.set("Mods", "ngCAMspeedext", iniSoftFactor);

// NG-CAM: HookPilot
        Config.cur.ini.set("Mods", "ngCAMspeedint", (int)Config.cur.ini.get("Mods", "ngCAMspeedint", 2, 0, 3));

// NG-CAM: CmdFov
        Config.cur.ini.set("Mods", "ngCAMfovdefault", (int)Config.cur.ini.get("Mods", "ngCAMfovdefault", 90, 60, 120));
        Config.cur.ini.set("Mods", "ngCAMfovspeed", (int)Config.cur.ini.get("Mods", "ngCAMfovspeed", 2, 0, 3));
        Config.cur.ini.set("Mods", "ngCAMfovlog", (int)Config.cur.ini.get("Mods", "ngCAMfovlog", 0, 0, 1));
    }

    public static HookView cur()
    {
        return current;
    }

//  GoPro
    private boolean getHookByName(Aircraft aircraft, String s, Point3d point3d)
    {
        Orient orient = new Orient();
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        Hook hook = aircraft.findHook(s);
        if(hook == null)
        {
            return false;
        } else
        {
            hook.computePos(aircraft, loc, loc1);
            loc1.get(point3d, orient);
            return true;
        }
    }

//  GoPro
    public void setHookView()
    {
        Aircraft aircraft = null;
        if(Config.isUSE_RENDER() && (Main3D.cur3D().viewActor() instanceof Aircraft))
            aircraft = (Aircraft)Main3D.cur3D().viewActor();
        if(aircraft == null)
            return;
        set(aircraft, 10F, -10F);
        pExt.set(0.0D, 0.0D, 0.0D);
        oExt.set(0.0F, 0.0F, 0.0F);
        if(hookViewIndex > 9)
            hookViewIndex = 0;
        boolean flag = false;
        switch(hookViewIndex)
        {
        default:
            break;

        case 1:
            try
            {
                getHookByName(aircraft, "_NavLight4", pExt);
                pExt.add(-1.5D, -0.6D, 0.7D);
                oExt.set(0.0F, -10F, 0.0F);
            }
            catch(Exception exception1)
            {
                flag = true;
            }
            break;

        case 2:
            try
            {
                getHookByName(aircraft, "_ClipCGear", pExt);
                if(aircraft.FM.CT.getGear() > 0.99F)
                {
                    pExt.add(-2.5D, 0.0D, -0.2D);
                    oExt.set(0.0F, -5F, 0.0F);
                } else
                {
                    pExt.add(-2.5D, 0.0D, -1D);
                    oExt.set(0.0F, -5F, 0.0F);
                }
            }
            catch(Exception exception2)
            {
                flag = true;
            }
            break;

        case 3:
            try
            {
                getHookByName(aircraft, "_ClipLGear", pExt);
                if(aircraft.FM.CT.getGear() > 0.99F)
                {
                    pExt.add(-2D, 0.7D, 0.5D);
                    oExt.set(0.0F, 5F, 0.0F);
                } else
                {
                    pExt.add(-2D, 0.7D, -1D);
                    oExt.set(0.0F, 5F, 0.0F);
                }
            }
            catch(Exception exception3)
            {
                flag = true;
            }
            break;

        case 4:
            try
            {
                getHookByName(aircraft, "_ClipRGear", pExt);
                if(aircraft.FM.CT.getGear() > 0.99F)
                {
                    pExt.add(-2D, -0.7D, 0.5D);
                    oExt.set(0.0F, 5F, 0.0F);
                } else
                {
                    pExt.add(-2D, -0.7D, -1D);
                    oExt.set(0.0F, 5F, 0.0F);
                }
            }
            catch(Exception exception4)
            {
                flag = true;
            }
            break;

        case 5:
            pExt.set(15D, 0.0D, 0.0D);
            oExt.set(180F, 0.0F, 0.0F);
            break;

        case 6:
            pExt.set(2.8D, -1.6D, 1.0D);
            oExt.set(200F, 0.0F, 0.0F);
            break;

        case 7:
            try
            {
                getHookByName(aircraft, "_WingTipR", pExt);
                pExt.add(0.0D, -4D, 1.0D);
                oExt.set(-92F, -5F, 0.0F);
            }
            catch(Exception exception7)
            {
                flag = true;
            }
            break;

        case 8:
            try
            {
                getHookByName(aircraft, "_WingTipL", pExt);
                pExt.add(0.0D, 4D, 1.0D);
                oExt.set(92F, -5F, 0.0F);
            }
            catch(Exception exception8)
            {
                flag = true;
            }
            break;

        case 9:
            pExt.set(-2D, 0.0D, 15D);
            oExt.set(0.0F, -90F, 0.0F);
            break;
        }

        if(flag)
        {
            hookViewIndex++;
            setHookView();
        }
    }

    protected static Actor lastBaseActor = null;
    protected Actor camera;
    protected boolean bUse;
    protected boolean bClipOnLand;
    protected static final int EXT = 1;
    protected static boolean bUseMouse = true;
    protected static boolean bChangeLen = false;
    protected static int useFlags = 0;
    private static float _minLen = 2.0F;
    private static float _defaultLen;
    private static float _maxLen = 500F;
    protected static float _visibleR = -1F;
    protected static float len;
    protected static float stepAzimut = 45F;
    protected static float stepTangage = 30F;
    protected static float maxAzimut = 179F;
    protected static float maxTangage = 89F;
    protected static float minTangage = -89F;
    protected static float Azimut = 0.0F;
    protected static float Tangage = 0.0F;
    protected static float _Azimut = 0.0F;
    protected static float _Tangage = 0.0F;
    protected static long prevTime = 0L;
    protected static float koofAzimut = 0.1F;
    protected static float koofTangage = 0.1F;
    protected static float koofLen = 1.0F;
    protected static float koofSpeed = 6F;
    public static float koofLeanF = 0.4F;
    public static float koofLeanS = 0.4F;
    public static float koofRaise = 0.2F;
    public static float rubberBandStretch = 0.5F;
    protected Orient o;
    protected Point3d p;
    protected Point3d pAbs;
    protected Orient oAbs;
    private boolean bFollow;
    private static Point3d pClipZ1 = new Point3d();
    private static Point3d pClipZ2 = new Point3d();
    private static Point3d pClipRes = new Point3d();
    static ClipFilter clipFilter = new ClipFilter();
    private static final float Circ = 360F;
    private long timeViewSet;
    private static String sectConf;
    public static HookView current = null;

// GoPro
    private int hookViewIndex;
    private long hookViewIndexTime;
    private Point3d pExt;
    private Orient oExt;
    private Orient oCam;
    private Point3d pCam;
    private Orient oView;
    private Point3d pView;
    private boolean sameActorSelected = false;
    private static boolean overrideActorChange = false;
    private static Actor overrideActor = null;

//  VisualMod
    protected static long pTime = 0L;
    protected boolean bFirstPan;
//    protected boolean oAdj;
//    protected boolean oAdjM;
    protected Orient o6DOF;
    protected Orient oIn;
    protected Orient oAbsIn;
    protected Orient lorv;
    protected Orient orv;
    protected Point3d lp;
//  Config.cur.inFEnabled (PAL3DFollowInertiaEnabled)
//    public boolean inFEnabled = true; // from 0 to 1
//  Config.cur.inEI (PAL3DExternalSoftView)
    public float inEI = 0.075F; // from 0.001F to 1.0F
//  Config.cur.inA (PAL3DFollowInertiaAngle)
    public float inA = 0.01F; // from 0.0001F to 1.0F
//  Config.cur.inF (PAL3DFollowInertia)
    public double inF = 0.01F; // from 0.0001F to 1.0F

// NG-CAM
    private static String ngCAMversion = "NG-CAM v2.0";
    public static boolean isPanView;
    public static int isPanKillSwitch;
    public static int iniSoftFactor;
    public static boolean isLsoView;

    static
    {
        _defaultLen = 20F;
        len = _defaultLen;
    }
}
