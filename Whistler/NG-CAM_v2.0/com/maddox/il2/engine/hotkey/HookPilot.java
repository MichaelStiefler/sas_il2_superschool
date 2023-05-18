// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 16/03/2021 09:35:51
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   HookPilot.java

package com.maddox.il2.engine.hotkey;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.CockpitPilot;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.rts.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

// Referenced classes of package com.maddox.il2.engine.hotkey:
//            HookView

public class HookPilot extends HookRender
{
    private class RubberBand
    {

        public float getValue(byte byte0)
        {
            switch(byte0)
            {
            case 1: // '\001'
                return rubberBandSide;

            case 2: // '\002'
                return rubberBandUpDown;

            case 3: // '\003'
                return rubberBandBackForward;
            }
            return 1.0F;
        }

        public void setValue(byte byte0, float f)
        {
            switch(byte0)
            {
            case 1: // '\001'
                rubberBandSide = f;
                return;

            case 2: // '\002'
                rubberBandUpDown = f;
                return;

            case 3: // '\003'
                rubberBandBackForward = f;
                return;
            }
        }

        public static final byte SIDE = 1;
        public static final byte UP_DOWN = 2;
        public static final byte BACK_FORWARD = 3;
        float rubberBandSide;
        float rubberBandUpDown;
        float rubberBandBackForward;

        private RubberBand()
        {
            rubberBandSide = 1.0F;
            rubberBandUpDown = 1.0F;
            rubberBandBackForward = 1.0F;
        }

    }


    public void resetGame()
    {
        enemy = null;
        bUp = false;
    }

    public Point3d pCamera()
    {
        if(bTubeSight && isAimReached())
            return pAim;
        if(bUp)
            return pUp;
        else
            return pCenter;
    }

    public float getAzimut()
    {
        return o.getAzimut();
    }

    public float getTangage()
    {
        return o.getTangage();
    }

    public boolean isAimReached()
    {
        if(!bAim)
            return false;
        if(bTubeSight)
        {
            if((double)Math.abs(_Azimut - Azimut) > 0.5D)
                return false;
            if((double)Math.abs(_Tangage - Tangage) > 0.5D)
                return false;
            if((double)Math.abs(_Roll - Roll) > 0.5D)
                return false;
        }
        if(_leanForward != leanForward || _leanSide != leanSide || _raise != raise)
            return false;
        if(bSimpleUse)
        {
            pCenter.set(pAim);
            o.set(Azimut, Tangage, Roll);
            le.set(pCenter, o);
        }
        return true;
    }

    public void setSimpleUse(boolean flag)
    {
        bSimpleUse = flag;
        if(!bSimpleUse)
        {
            pCenter.set(pAimTube);
            _leanForward = pAimLeanF;
            _leanSide = pAimLeanS;
            _raise = pAimRaise;
            headMoveToDef();
            rprevTime = Time.currentReal();
            keyboardPrevTime = Time.currentReal();
        }
    }

    public void setSimpleAimOrient(float f, float f1, float f2)
    {
        if(f >= 180F)
            f -= 360F;
        if(f < -180F)
            f += 360F;
        if(f1 >= 180F)
            f1 -= 360F;
        if(f1 < -180F)
            f1 += 360F;
        _Azimut = f;
        _Tangage = f1;
        Roll = _Roll = f2;
    }

    public void setInstantOrient(float f, float f1, float f2)
    {
        if(f >= 180F)
            f -= 360F;
        if(f < -180F)
            f += 360F;
        if(f1 >= 180F)
            f1 -= 360F;
        if(f1 < -180F)
            f1 += 360F;
        _Azimut = Azimut = f;
        _Tangage = Tangage = f1;
        _Roll = Roll = f2;
        o.set(f, f1, f2);
        le.set(pCamera(), o);
    }

    public void setInstantAim(boolean flag)
    {
        if(flag)
            pCenter.set(pAim);
        else
            pCenter.set(pCenterOrig);
    }

    public void setCenter(Point3d point3d)
    {
        pCenter.set(point3d);
        pCenterOrig.set(point3d);
    }

    public void setAim(Point3d point3d)
    {
        pAim.set(pCenter);
        if(point3d != null)
            pAim.set(point3d);
        setUp(point3d);
        findAimCoordinates(pAim);
    }

    public void setUp(Point3d point3d)
    {
        pUp.set(pCenter);
        if(point3d != null)
            pUp.set(point3d);
    }

    public void setSteps(float f, float f1)
    {
        stepAzimut = f;
        stepTangage = f1;
    }

    public void setMinMax(float f, float f1, float f2)
    {
        maxAzimut = f;
        minTangage = f1;
        maxTangage = f2;
    }

    public void setForward(boolean flag)
    {
        bForward = flag;
    }

    public void endPadlock()
    {
        bPadlockEnd = true;
    }

    private void _reset(boolean flag)
    {

// Pilot Soft View

        iniSoftFactor = (int)Config.cur.ini.get("Mods", "ngCAMspeedint", 2, 0, 3);
        defSoftFactor = (iniSoftFactor == 1 ? 0.025F : (iniSoftFactor == 2 ? 0.050F : 0.10F));
        curSoftFactor = 1.0F;

        if(!AircraftHotKeys.bFirstHotCmd)
        {
            _Azimut = Azimut = 0.0F;
            _Tangage = Tangage = 0.0F;
            o.set(0.0F, 0.0F, 0.0F);
            le.set(pCamera(), o);
        }
        Px = Py = Pz = 0.0F;
        azimPadlock = 0.0F;
        tangPadlock = 0.0F;
        timeKoof = 1.0F;
        prevTime = -1L;
        roolTime = -1L;
        enemy = null;
        bPadlock = false;
        tPadlockEnd = -1L;
        tPadlockEndLen = 0L;
        bPadlockEnd = false;
        bForward = false;
        if(!Main3D.cur3D().isDemoPlaying())
            new MsgAction(64, 0.0D) {

                public void doAction()
                {
                    HotKeyCmd.exec("misc", "target_");
                }

            }
;
        timeViewSet = -2000L;
        headShift.set(0.0D, 0.0D, 0.0D);
        counterForce.set(0.0D, 0.0D, 0.0D);
        oldHeadTime = -1L;
        oldWx = 0.0D;
        oldWy = 0.0D;
        if(!flag)
        {
            bTubeSight = false;
            set6DoFLimits();
            findAimCoordinates(pAim);
            bRaiseUp = false;
            bRaiseDown = false;
            bLeanF = false;
            bLeanB = false;
            bLeanSideL = false;
            bLeanSideR = false;
        }
    }

    private void findAimCoordinates(Point3d point3d)
    {
        Point3d point3d1 = new Point3d();
        point3d1.set(point3d);
        point3d1.add(-faceL, 0.0D, 0.0D);
        pAimRaise = (float)point3d1.distance(pSpine) - spineL;
        pAimLeanS = (float)Math.asin((point3d1.y - pSpine.y) / (double)(pAimRaise + spineL));
        pAimLeanF = (float)Math.asin((point3d1.x - pSpine.x) / (double)(pAimRaise + spineL));
        if(bAim && !AircraftHotKeys.bFirstHotCmd)
        {
            leanForward = _leanForward = pAimLeanF;
            leanSide = _leanSide = pAimLeanS;
            raise = _raise = pAimRaise;
        } else
        {
            leanForward = _leanForward = leanForwardDefault;
            leanSide = _leanSide = 0.0F;
            raise = _raise = 0.0F;
        }
    }

    public boolean isTubeSight()
    {
        return bTubeSight;
    }

    public void setTubeSight(boolean flag)
    {
        bTubeSight = flag;
    }

    public void setTubeSight(Point3d point3d)
    {
        bTubeSight = true;
        pAimTube.set(point3d);
        pAimTube.add(pCenter);
        findAimCoordinates(pAimTube);
    }

    private void set6DoFLimits()
    {
        String s = Main3D.cur3D().cockpitCur.getClass().getName();
        s = s.substring(27);
        float af[] = ((CockpitPilot)Main3D.cur3D().cockpitCur).get6DoFLimits();
        spineL = af[0];
        faceL = af[1];
        spineOffsetX = af[2];
        leanSideMax = af[3];
        leanForwardMax = af[4];
        leanForwardMin = af[5];
        raiseMax = af[6];
        raiseMin = af[7];
        planeString = s + "_HC";
        trackIR_AngleScale = 10F;
        trackIRFaceL = 0.055F;
        leanForwardDefault = (float)Math.asin(spineOffsetX / spineL);
        if(!AircraftHotKeys.bFirstHotCmd)
        {
            _leanForward = leanForward = leanForwardDefault;
            _leanSide = leanSide = 0.0F;
            _raise = raise = 0.0F;
        }
        pNeck.set(pCenterOrig);
        pNeck.add(-faceL, 0.0D, 0.0D);
        float f = -(float)Math.sqrt(spineL * spineL - spineOffsetX * spineOffsetX);
        pSpine.set(pNeck);
        pSpine.add(-spineOffsetX, 0.0D, f);
        leanForwardRange = leanForwardMax - leanForwardDefault;
        if(leanForwardRange < leanForwardDefault - leanForwardMin)
            leanForwardRange = leanForwardDefault - leanForwardMin;
        leanForwardRange = leanForwardRange * 1.1F;
        leanSideRange = leanSideMax * 1.1F;
        raiseRange = raiseMax;
        if(raiseRange < -raiseMin)
            raiseRange = -raiseMin;
        raiseRange = raiseRange * 1.1F;
    }

    public void saveRecordedStates(PrintWriter printwriter)
        throws Exception
    {
        printwriter.println(Azimut);
        printwriter.println(_Azimut);
        printwriter.println(Tangage);
        printwriter.println(_Tangage);
        printwriter.println(o.azimut());
        printwriter.println(o.tangage());
        printwriter.println(Roll);
        printwriter.println(_Roll);
        printwriter.println(o.kren());
        printwriter.println(leanForward);
        printwriter.println(_leanForward);
        printwriter.println(leanSide);
        printwriter.println(_leanSide);
        printwriter.println(raise);
        printwriter.println(_raise);
        printwriter.println(pCenter.x);
        printwriter.println(pCenter.y);
        printwriter.println(pCenter.z);
    }

    public void loadRecordedStates(BufferedReader bufferedreader)
        throws Exception
    {
        Azimut = Float.parseFloat(bufferedreader.readLine());
        _Azimut = Float.parseFloat(bufferedreader.readLine());
        Tangage = Float.parseFloat(bufferedreader.readLine());
        _Tangage = Float.parseFloat(bufferedreader.readLine());
        o.set(Float.parseFloat(bufferedreader.readLine()), Float.parseFloat(bufferedreader.readLine()), 0.0F);
        if(NetMissionTrack.playingVersion() > 103)
        {
            Roll = Float.parseFloat(bufferedreader.readLine());
            _Roll = Float.parseFloat(bufferedreader.readLine());
            o.set(o.getAzimut(), o.getTangage(), Float.parseFloat(bufferedreader.readLine()));
            leanForward = Float.parseFloat(bufferedreader.readLine());
            _leanForward = Float.parseFloat(bufferedreader.readLine());
            leanSide = Float.parseFloat(bufferedreader.readLine());
            _leanSide = Float.parseFloat(bufferedreader.readLine());
            raise = Float.parseFloat(bufferedreader.readLine());
            _raise = Float.parseFloat(bufferedreader.readLine());
            pCenter.x = Float.parseFloat(bufferedreader.readLine());
            pCenter.y = Float.parseFloat(bufferedreader.readLine());
            pCenter.z = Float.parseFloat(bufferedreader.readLine());
        }
        le.set(pCamera(), o);
    }

    public void reset()
    {
        stamp = -1L;
        _reset(false);
    }

    private void setTimeKoof()
    {
        long l = Time.current();
        if(prevTime == -1L)
            timeKoof = 1.0F;
        else
            timeKoof = (float)(l - prevTime) / 30F;
        prevTime = l;
    }

    private void headRoll(Aircraft aircraft)
    {
        if(!(aircraft.FM instanceof RealFlightModel))
            return;
        long l = roolTime - stamp;
        if(l >= 0L && l < 50L)
            return;
        roolTime = stamp;
        shakeLVL = ((RealFlightModel)(RealFlightModel)aircraft.FM).shakeLevel;
        float f;
        float f1;
        float f2;
        if(World.cur().diffCur.Head_Shake)
        {
            long l1 = Time.current();
            if(oldHeadTime == -1L)
            {
                oldHeadTime = Time.current();
                oldWx = aircraft.FM.getW().x;
                oldWy = aircraft.FM.getW().y;
            }
            long l2 = l1 - oldHeadTime;
            oldHeadTime = l1;
            if(l2 > 200L)
                l2 = 200L;
            double d1 = 0.0030000000000000001D * (double)l2;
            double d2 = aircraft.FM.getW().x - oldWx;
            double d3 = aircraft.FM.getW().y - oldWy;
            oldWx = aircraft.FM.getW().x;
            if(d1 < 0.001D)
                d2 = 0.0D;
            else
                d2 /= d1;
            oldWy = aircraft.FM.getW().y;
            if(d1 < 0.001D)
                d3 = 0.0D;
            else
                d3 /= d1;
            if(aircraft.FM.Gears.onGround())
            {
                tmpA.set(0.0D, 0.0D, 0.0D);
                headShift.scale(1.0D - d1);
                tmpA.scale(d1);
                headShift.add(tmpA);
                f1 = (float)headShift.y;
                f = (float)(headShift.x + (double)(0.03F * shakeLVL * (0.5F - rnd.nextFloat())));
                f2 = (float)(headShift.z + (double)(1.2F * shakeLVL * (0.5F - rnd.nextFloat())));
            } else
            {
                tmpB.set(0.0D, 0.0D, 0.0D);
                tmpA.set(aircraft.FM.getAccel());
                aircraft.FM.Or.transformInv(tmpA);
                tmpA.scale(-0.59999999999999998D);
                if(tmpA.z > 0.0D)
                    tmpA.z *= 0.80000000000000004D;
                tmpB.add(tmpA);
                counterForce.scale(1.0D - 0.20000000000000001D * d1);
                tmpA.scale(0.20000000000000001D * d1);
                counterForce.add(tmpA);
                tmpB.sub(counterForce);
                counterForce.scale(1.0D - 0.050000000000000003D * d1);
                if(counterForce.z > 0.0D)
                    counterForce.z *= 1.0D - 0.080000000000000002D * d1;
                tmpB.scale(0.080000000000000002D);
                tmpA.set(-0.69999999999999996D * d3, d2, 0.0D);
                tmpA.add(tmpB);
                headShift.scale(1.0D - d1);
                tmpA.scale(d1);
                headShift.add(tmpA);
                f1 = (float)headShift.y;
                f = (float)(headShift.x + (double)(0.3F * shakeLVL * (0.5F - rnd.nextFloat())));
                f2 = (float)(headShift.z + (double)(0.4F * shakeLVL * (0.5F - rnd.nextFloat())));
            }
        } else
        {
            f1 = 0.0F;
            f = 0.0F;
            f2 = 0.0F;
        }
        if(World.cur().diffCur.Wind_N_Turbulence)
        {
            float f3 = SpritesFog.dynamicFogAlpha;
            double d = aircraft.pos.getAbsPoint().z;
            if(f3 > 0.01F && d > 300D && d < 2500D)
            {
                float f7 = aircraft.FM.getSpeed();
                if(f7 > 138.8889F)
                    f7 = 138.8889F;
                f7 -= 55.55556F;
                if(f7 < 0.0F)
                    f7 = 0.0F;
                f7 /= 83.33334F;
                f1 += f7 * 0.05F * f3 * (0.5F - rnd.nextFloat());
                f2 += f7 * 0.3F * f3 * (0.5F - rnd.nextFloat());
            }
        }
        if(f >= 1.0F || f <= -1F)
            if(f < -1F)
                f = -1F;
            else
            if(f > 1.0F)
                f = 1.0F;
            else
                f = 0.0F;
        if(f1 >= 1.0F || f1 <= -1F)
            if(f1 < -1F)
                f1 = -1F;
            else
            if(f1 > 1.0F)
                f1 = 1.0F;
            else
                f1 = 0.0F;
        if(f2 >= 1.0F || f2 <= -1F)
            if(f2 < -1F)
                f2 = -1F;
            else
            if(f2 > 1.0F)
                f2 = 1.0F;
            else
                f2 = 0.0F;
        P.set(Px += (f * (bAim ? 0.01F : 0.02F) - Px) * 0.4F, Py += (f1 * (bAim ? 0.01F : 0.02F) - Py) * 0.4F, Pz += (f2 * (bAim ? 0.01F : 0.02F) - Pz) * 0.4F);
        oTmp.set((float)(6D * P.y), (float)(6D * P.z), (float)(60D * P.y));
        float f4 = Py * -425F;
        if(!bAim)
            f4 *= 0.5F;
        float f5 = f4 * (float)Math.cos(Math.toRadians(Azimut));
        float f6 = f4 * (float)Math.sin(Math.toRadians(-Azimut));
        oTmp.increment(0.31F * rnd.nextFloat(-shakeLVL, shakeLVL), f6 + 0.31F * rnd.nextFloat(-shakeLVL, shakeLVL), f5 + 0.54F * rnd.nextFloat(-shakeLVL, shakeLVL));
    }

    public boolean isPadlock()
    {
        return bPadlock;
    }

    public Actor getEnemy()
    {
        return enemy;
    }

    public void stopPadlock()
    {
        if(!bPadlock)
        {
            return;
        } else
        {
            stamp = -1L;
            _reset(true);
            return;
        }
    }

    public boolean startPadlock(Actor actor)
    {
        if(!bUse || bSimpleUse)
            return false;
        if(!Actor.isValid(actor))
        {
            bPadlock = false;
            return false;
        }
        Aircraft aircraft = World.getPlayerAircraft();
        if(!Actor.isValid(aircraft))
        {
            bPadlock = false;
            return false;
        }
        enemy = actor;
        Azimut = _Azimut;
        Tangage = _Tangage;
        bPadlock = true;
        bPadlockEnd = false;
        bVisibleEnemy = true;
        aircraft.pos.getAbs(pAbs, oAbs);
        Camera3D camera3d = (Camera3D)target2;
        camera3d.pos.getAbs(o);
        o.sub(oAbs);
        azimPadlock = o.getAzimut();
        tangPadlock = o.getTangage();
        azimPadlock = (azimPadlock + 3600F) % 360F;
        if(azimPadlock > 180F)
            azimPadlock -= 360F;
        stamp = -1L;
        if(!Main3D.cur3D().isDemoPlaying())
            new MsgAction(64, 0.0D) {

                public void doAction()
                {
                    HotKeyCmd.exec("misc", "target_");
                }

            }
;
        return true;
    }

    public boolean isAim()
    {
        return bAim;
    }

    public void doAim(boolean flag)
    {
        if(bAim == flag)
            return;
        if(flag)
        {
            _leanForward = pAimLeanF;
            _leanSide = pAimLeanS;
            _raise = pAimRaise;
        } else
        {
            _leanForward = leanForwardDefault;
            _leanSide = 0.0F;
            _raise = 0.0F;
        }
        bAim = flag;
    }

    public boolean isUp()
    {
        return bUp;
    }

    public void doUp(boolean flag)
    {
        if(bUp == flag)
        {
            return;
        } else
        {
            bUp = flag;
            return;
        }
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

    public void leanForwardMove(float f)
    {
        if(!bUse || bSimpleUse || bAim)
        {
            return;
        } else
        {
            _leanForward = f * leanForwardRange + leanForwardDefault;
            _leanForward = smoothLimit(_leanForward, leanForwardMin, leanForwardMax, (byte)3, false);
            return;
        }
    }

    public void leanSideMove(float f)
    {
        if(!bUse || bSimpleUse || bAim)
        {
            return;
        } else
        {
            _leanSide = f * leanSideRange;
            _leanSide = smoothLimit(_leanSide, -leanSideMax, leanSideMax, (byte)1, false);
            return;
        }
    }

    public void raiseMove(float f)
    {
        if(!bUse || bSimpleUse || bAim)
        {
            return;
        } else
        {
            _raise = f * raiseRange;
            _raise = smoothLimit(_raise, raiseMin, raiseMax, (byte)2, false);
            return;
        }
    }

    public void moveHead(float f, float f1, float f2)
    {
        bUseTiR = true;
        f *= spineL;
        f1 *= spineL;
        f2 *= spineL;
        f = 0.85F * mx + f * 0.15F;
        f1 = 0.85F * my + f1 * 0.15F;
        f2 = 0.85F * mz + f2 * 0.15F;
        mx = f;
        my = f1;
        mz = f2;
        if(!bUse || bSimpleUse || bAim)
        {
            return;
        } else
        {
            float f3 = (checkAzimut(o.azimut()) * 0.01745329F) / trackIR_AngleScale;
            float f4 = (o.tangage() * 0.01745329F) / trackIR_AngleScale;
            float f5 = 0.0F;
            pNeck.set(pCenterOrig);
            pNeck.add(f, f1, f2);
            float f6 = trackIRFaceL * (float)(Math.cos(f4) * Math.cos(f3));
            float f7 = -trackIRFaceL * (float)(Math.cos(f4) * Math.sin(f3));
            float f8 = trackIRFaceL * (float)Math.sin(f4);
            pNeck.add(-f6, -f7, -f8);
            float f9 = (float)pNeck.distance(pSpine);
            _raise = f9 - spineL;
            f9 = spineL + _raise;
            _leanForward = (float)Math.asin((pNeck.x - pSpine.x) / (double)f9);
            _leanSide = (float)Math.asin((pNeck.y - pSpine.y) / (double)f9);
            _raise = smoothLimit(_raise, raiseMin, raiseMax, (byte)2, false);
            _leanForward = smoothLimit(_leanForward, leanForwardMin, leanForwardMax, (byte)3, true);
            _leanSide = smoothLimit(_leanSide, -leanSideMax, leanSideMax, (byte)1, false);
            return;
        }
    }

    private float smoothLimit(float f, float f1, float f2, byte byte0, boolean flag)
    {
        float f3 = 0.0F;
        float f6 = 0.05F;
        float f7 = rubberBand.getValue(byte0);
        f7 *= 0.98F - HookView.rubberBandStretch;
        if((double)f7 < 0.0001D)
            f7 = 0.0F;
        float f8 = f;
        if(f > f2)
        {
            float f4 = f - f2;
            f8 = f2 + f4 * f7;
            if(f8 > f2 + f6)
                f8 = f2 + f6;
        } else
        if(f < f1)
        {
            float f5 = f - f1;
            f8 = f1 + f5 * f7;
            if(f8 < f1 - f6)
                f8 = f1 - f6;
        } else
        {
            float f9 = f1 + f2;
            if((double)Math.abs(f - f9) < 0.01D)
                f7 = 1.0F;
        }
        rubberBand.setValue(byte0, f7);
        return f8;
    }

    public void headMoveToDef()
    {
        if(bSimpleUse)
        {
            return;
        } else
        {
            _leanForward = leanForwardDefault;
            _leanSide = 0.0F;
            _raise = 0.0F;
            _Tangage = 0.0F;
            _Azimut = 0.0F;
            Roll = _Roll = 0.0F;
            bAim = false;
            return;
        }
    }

    private void checkForKeyBInput()
    {
        if(!bUse || bSimpleUse)
            return;
        if(!bAim && !bUseTiR)
        {
            long l = Time.currentReal();
            float f = 0.0F;
            if(keyboardPrevTime != l)
            {
                f = (float)(l - keyboardPrevTime) / 400F;
                keyboardPrevTime = l;
            }
            if(bRaiseUp)
            {
                _raise = raise + HookView.koofRaise * f;
                _raise = smoothLimit(_raise, raiseMin, raiseMax, (byte)2, false);
            }
            if(bRaiseDown)
            {
                _raise = raise - HookView.koofRaise * f;
                _raise = smoothLimit(_raise, raiseMin, raiseMax, (byte)2, false);
            }
            if(bLeanF)
            {
                _leanForward = leanForward + HookView.koofLeanF * 2.0F * f;
                _leanForward = smoothLimit(_leanForward, leanForwardMin, leanForwardMax, (byte)3, true);
            }
            if(bLeanB)
            {
                _leanForward = leanForward - HookView.koofLeanF * 2.0F * f;
                _leanForward = smoothLimit(_leanForward, leanForwardMin, leanForwardMax, (byte)3, true);
            }
            if(bLeanSideL)
            {
                _leanSide = leanSide + HookView.koofLeanS * 2.0F * f;
                _leanSide = smoothLimit(_leanSide, -leanSideMax, leanSideMax, (byte)1, false);
            }
            if(bLeanSideR)
            {
                _leanSide = leanSide - HookView.koofLeanS * 2.0F * f;
                _leanSide = smoothLimit(_leanSide, -leanSideMax, leanSideMax, (byte)1, false);
            }
        }
        bUseTiR = false;
    }

    private void moveHead()
    {
        if(!bAim)
        {
            if(_leanForward < leanForwardMin || _leanForward > leanForwardMax)
                _leanForward = smoothLimit(_leanForward, leanForwardMin, leanForwardMax, (byte)3, false);
            if(_leanSide < -leanSideMax || _leanSide > leanSideMax)
                _leanSide = smoothLimit(_leanSide, -leanSideMax, leanSideMax, (byte)1, false);
            if(_raise < raiseMin || _raise > raiseMax)
                _raise = smoothLimit(_raise, raiseMin, raiseMax, (byte)2, false);
        }
        float f = spineL + raise;
        pNeck.set(pSpine);
        pNeck.add((double)f * Math.sin(leanForward), (double)f * Math.sin(leanSide), (double)f * Math.cos(leanForward) * Math.cos(leanSide));
        addNeck();
    }

    private void addNeck()
    {
        float f = (Azimut * 3.141593F) / 180F;
        float f1 = (Tangage * 3.141593F) / 180F;
        float f2 = faceL * (float)(Math.cos(f1) * Math.cos(f));
        float f3 = -faceL * (float)(Math.cos(f1) * Math.sin(f));
        float f4 = faceL * (float)Math.sin(f1);
        Point3d point3d = new Point3d(pNeck);
        point3d.add(f2, f3, f4);
        pCenter.set(point3d);
    }

    private void debugPrint()
    {
    }

    private float checkAzimut(float f)
    {
        if(f > 180F)
            f -= 360F;
        else
        if(f < -180F)
            f += 360F;
        return f;
    }

    public boolean computeRenderPos(Actor actor, Loc loc, Loc loc1)
    {
        checkForKeyBInput();
        if(bDebugPrint)
            debugPrint();
        if(bUse)
        {
            if(bPadlock)
            {
                Aircraft aircraft = World.getPlayerAircraft();
                if(!Actor.isValid(aircraft))
                {
                    reset();
                    loc1.add(le, loc);
                    return true;
                }
                long l2 = Time.currentReal();
                if(l2 != rprevTime && !bSimpleUse)
                {
                    long l4 = l2 - rprevTime;
                    rprevTime = l2;
                    if(_leanForward != leanForward || _leanSide != leanSide || _raise != raise)
                    {
                        leanForward = cValue(leanForward, _leanForward, HookView.koofLeanF, l4);
                        leanSide = cValue(leanSide, _leanSide, HookView.koofLeanS, l4);
                        raise = cValue(raise, _raise, HookView.koofRaise, l4);
                    }
                    moveHead();
                }
                l2 = Time.current();
                if(l2 != stamp && enemy.pos != null && aircraft.pos != null)
                {
                    stamp = l2;
                    setTimeKoof();
                    enemy.pos.getRender(pe);
                    pEnemyAbs.set(pe);
                    aircraft.pos.getRender(pAbs, oAbs);
                    Ve.sub(pe, pAbs);
                    o.setAT0(Ve);
                    if(World.cur().diffCur.Head_Shake || World.cur().diffCur.Wind_N_Turbulence)
                    {
                        headRoll(aircraft);
                        pe.add(pCamera(), P);
                        le.set(pe);
                    } else
                    {
                        le.set(pCamera());
                    }
                    o.sub(oAbs);
                    padlockSet(o);
                    op.set(o);
                    op.add(oAbs);
                    op.increment(oTmp);
                }
                loc1.add(le, loc);
                loc1.set(op);
                return true;
            }
            long l = Time.currentReal();
            if(l != rprevTime && !bSimpleUse)
            {
                long l3 = l - rprevTime;
                rprevTime = l;
                if(_Azimut != Azimut || _Tangage != Tangage)
                {
                    Azimut = bvalue(_Azimut, Azimut, l3);
                    Tangage = bvalue(_Tangage, Tangage, l3);
                    o.set(Azimut, Tangage, Roll);
                }
                if(_leanForward != leanForward || _leanSide != leanSide || _raise != raise)
                {
                    leanForward = cValue(leanForward, _leanForward, HookView.koofLeanF, l3);
                    leanSide = cValue(leanSide, _leanSide, HookView.koofLeanS, l3);
                    raise = cValue(raise, _raise, HookView.koofRaise, l3);
                }
                moveHead();
            }
            if((World.cur().diffCur.Head_Shake || World.cur().diffCur.Wind_N_Turbulence) && !bSimpleUse)
            {
                Aircraft aircraft1 = World.getPlayerAircraft();
                if(Actor.isValid(aircraft1))
                {
                    long l1 = Time.current();
                    if(l1 != stamp)
                    {
                        stamp = l1;
                        headRoll(aircraft1);
                    }
                }
                pe.add(pCamera(), P);

// Pilot Soft View

                if(HookView.isCamEnabled() && iniSoftFactor > 0 && (!isTubeSight() || (isTubeSight() && !isAim())))
                {
                    curSoftFactor = (curSoftFactor < 1F && Main3D.FOVX < 30F) ? Main3D.FOVX / 2500F : curSoftFactor;
                    lo.interpolate(lo, o, curSoftFactor);
                    oTmp2.set(lo);
                    oTmp2.increment(oTmp);
                    oTmp2.wrap360();
                } else
                {
                    oTmp2.set(o);
                    oTmp2.increment(oTmp);
                }
                le.set(pe, oTmp2);
            } else
            {

// Pilot Soft View

                if(HookView.isCamEnabled() && iniSoftFactor > 0 && (!isTubeSight() || (isTubeSight() && !isAim())))
                {
                    curSoftFactor = (curSoftFactor < 1F && Main3D.FOVX < 30F) ? Main3D.FOVX / 2500F : curSoftFactor;
                    lo.interpolate(lo, o, curSoftFactor);
                    oTmp2.set(lo);
                    oTmp2.increment(oTmp);
                    oTmp2.wrap360();
                    le.set(pCamera(), oTmp2);
                } else
                {
                    le.set(pCamera(), o);
                }
            }
            loc1.add(le, loc);
        } else
        {
            loc1.set(loc);
        }

// Pilot Soft View

        curSoftFactor = defSoftFactor;

        return true;
    }

    public void computePos(Actor actor, Loc loc, Loc loc1)
    {
        if(bUse)
        {
            if(Time.isPaused() && !bPadlock)
            {
                if(World.cur().diffCur.Head_Shake)
                {
                    pe.add(pCamera(), P);
                    le.set(pe, o);
                } else
                {
                    le.set(pCamera(), o);
                }
                loc1.add(le, loc);
                return;
            }
            loc1.add(le, loc);
            if(bPadlock)
                loc1.set(op);
        } else
        {
            loc1.set(loc);
        }
    }

    private float avalue(float f, float f1)
    {
        if(f >= 0.0F)
            if(f <= f1)
                return 0.0F;
            else
                return f - f1;
        if(f >= -f1)
            return 0.0F;
        else
            return f + f1;
    }

    private float cValue(float f, float f1, float f2, long l)
    {
        float f3 = (f2 * (float)l) / 150F;
        if(f1 > f)
            if(f1 < f + f3)
                return f1;
            else
                return f + f3;
        if(f1 > f - f3)
            return f1;
        else
            return f - f3;
    }

    private float bvalue(float f, float f1)
    {
        float f2 = ((HookView.koofSpeed * 4F) / 6F) * timeKoof;
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

    private void padlockSet(Orient orient)
    {
        float f = orient.getAzimut();
        float f1 = orient.getTangage();
        if(bPadlockEnd || bForward)
        {
            f = f1 = 0.0F;
            tPadlockEnd = -1L;
        } else
        {
            Camera3D camera3d = (Camera3D)target2;
            float f2 = camera3d.FOV() * 0.3F;
            float f3 = f2 / camera3d.aspect();
            f = (f + 3600F) % 360F;
            if(f > 180F)
                f -= 360F;
            f = avalue(f, f2);
            f1 = avalue(f1, f3);
            boolean flag = false;
            if(f < -maxAzimut)
            {
                f = -maxAzimut;
                flag = true;
            }
            if(f > maxAzimut)
            {
                f = maxAzimut;
                flag = true;
            }
            if(f1 < minTangage)
            {
                f1 = minTangage;
                flag = true;
            }
            if(flag || !bVisibleEnemy || !Actor.isAlive(enemy))
            {
                if(tPadlockEnd != -1L)
                    tPadlockEndLen += Time.current() - tPadlockEnd;
                tPadlockEnd = Time.current();
                if(tPadlockEndLen > 4000L)
                {
                    bPadlockEnd = true;
                    tPadlockEnd = -1L;
                    tPadlockEndLen = 0L;
                }
            } else
            {
                tPadlockEnd = -1L;
                tPadlockEndLen = 0L;
            }
        }
        f = bvalue(f, azimPadlock);
        f1 = bvalue(f1, tangPadlock);
        orient.set(f, f1, 0.0F);
        azimPadlock = f;
        tangPadlock = f1;
        Azimut = f;
        Tangage = f1;
        _Azimut = f;
        _Tangage = f1;
        if(bPadlockEnd && -1F < azimPadlock && azimPadlock < 1.0F && -1F < tangPadlock && tangPadlock < 1.0F)
        {
            stamp = -1L;
            _reset(true);
        }
    }

    public void checkPadlockState()
    {
        if(!bPadlock)
            return;
        if(!Actor.isAlive(enemy))
        {
            return;
        } else
        {
            VisibilityChecker.checkLandObstacle = true;
            VisibilityChecker.checkCabinObstacle = true;
            VisibilityChecker.checkPlaneObstacle = true;
            VisibilityChecker.checkObjObstacle = true;
            bVisibleEnemy = VisibilityChecker.computeVisibility(null, enemy) > 0.0F;
            return;
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
        return flag1;
    }

    public boolean useMouse(boolean flag)
    {
        boolean flag1 = bUseMouse;
        bUseMouse = flag;
        return flag1;
    }

    public void mouseMoveHead(int i, int j, int k)
    {
        if(!bUse || bSimpleUse || bAim)
        {
            return;
        } else
        {
            _leanSide += ((float)(-i) * HookView.koofLeanS) / 200F;
            _leanForward += ((float)j * HookView.koofLeanF) / 200F;
            _raise += ((float)k * HookView.koofRaise) / 100F;
            _raise = smoothLimit(_raise, raiseMin, raiseMax, (byte)2, false);
            _leanForward = smoothLimit(_leanForward, leanForwardMin, leanForwardMax, (byte)3, false);
            _leanSide = smoothLimit(_leanSide, -leanSideMax, leanSideMax, (byte)1, false);
            return;
        }
    }

    public void mouseMove(int i, int j, int k)
    {
        if(!bUse || bPadlock || bSimpleUse)
            return;
        if(bTubeSight && bAim)
            return;
        if(bUseMouse && Time.real() > timeViewSet + 1000L)
        {
            float f = (o.azimut() + (float)i * HookView.koofAzimut) % 360F;
            if(f > 180F)
                f -= 360F;
            else
            if(f < -180F)
                f += 360F;
            if(f < -maxAzimut)
            {
                if(i <= 0)
                    f = -maxAzimut;
                else
                    f = maxAzimut;
            } else
            if(f > maxAzimut)
                if(i >= 0)
                    f = maxAzimut;
                else
                    f = -maxAzimut;
            float f1 = (o.tangage() + (float)j * HookView.koofTangage) % 360F;
            if(f1 > 180F)
                f1 -= 360F;
            else
            if(f1 < -180F)
                f1 += 360F;
            if(f1 < minTangage)
            {
                if(j <= 0)
                    f1 = minTangage;
                else
                    f1 = maxTangage;
            } else
            if(f1 > maxTangage)
                if(j >= 0)
                    f1 = maxTangage;
                else
                    f1 = minTangage;
            f = checkAzimut(f);
            o.set(f, f1, 0.0F);
            if(Actor.isValid(target))
                target.pos.inValidate(true);
            if(Actor.isValid(target2))
                target2.pos.inValidate(true);
            Azimut = _Azimut;
            Tangage = _Tangage;
            _Azimut = f;
            _Tangage = f1;
        }
    }

    public void viewSet(float f, float f1, float f2)
    {
        if(!bUse || bPadlock || bSimpleUse)
            return;
        if(bTubeSight && bAim)
            return;
        if(bUseMouse)
        {
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
            f2 %= 360F;
            if(f2 > 180F)
                f2 -= 360F;
            else
            if(f2 < -180F)
                f2 += 360F;
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
            if(f2 > maxRoll)
                f2 = maxRoll;
            else
            if(f2 < minRoll)
                f2 = minRoll;
            f = checkAzimut(f);
            _Azimut = Azimut = f;
            _Tangage = Tangage = f1;
            _Roll = Roll = f2;
            o.set(f, f1, f2);
            if(Actor.isValid(target))
                target.pos.inValidate(true);
            if(Actor.isValid(target2))
                target2.pos.inValidate(true);
        }
    }

    public void snapSet(float f, float f1)
    {
        if(!bUse || bPadlock || bSimpleUse)
            return;
        _Azimut = 45F * f;
        _Tangage = 44F * f1;
        Azimut = o.azimut() % 360F;
        if(Azimut > 180F)
            Azimut -= 360F;
        else
        if(Azimut < -180F)
            Azimut += 360F;
        Tangage = o.tangage() % 360F;
        if(Tangage > 180F)
            Tangage -= 360F;
        else
        if(Tangage < -180F)
            Tangage += 360F;
        if(Actor.isValid(target))
            target.pos.inValidate(true);
        if(Actor.isValid(target2))
            target2.pos.inValidate(true);
    }

    public void panSet(int i, int j)
    {
        if(!bUse || bPadlock || bSimpleUse)
            return;
        if(i == 0 && j == 0)
        {
            _Azimut = 0.0F;
            _Tangage = 0.0F;
        }
        if(_Azimut == -maxAzimut)
        {
            int k = (int)(_Azimut / stepAzimut);
            if(-_Azimut % stepAzimut > 0.01F * stepAzimut)
                k--;
            _Azimut = (float)k * stepAzimut;
        } else
        if(_Azimut == maxAzimut)
        {
            int l = (int)(_Azimut / stepAzimut);
            if(_Azimut % stepAzimut > 0.01F * stepAzimut)
                l++;
            _Azimut = (float)l * stepAzimut;
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
        Azimut = o.azimut() % 360F;
        if(Azimut > 180F)
            Azimut -= 360F;
        else
        if(Azimut < -180F)
            Azimut += 360F;
        Tangage = o.tangage() % 360F;
        if(Tangage > 180F)
            Tangage -= 360F;
        else
        if(Tangage < -180F)
            Tangage += 360F;
        if(Actor.isValid(target))
            target.pos.inValidate(true);
        if(Actor.isValid(target2))
            target2.pos.inValidate(true);
    }

    private HookPilot()
    {
        stepAzimut = 45F;
        stepTangage = 30F;
        maxAzimut = 155F;
        maxTangage = 89F;
        minTangage = -60F;
        maxRoll = 45F;
        minRoll = -45F;
        Azimut = 0.0F;
        Tangage = 0.0F;
        Roll = 0.0F;
        _Azimut = 0.0F;
        _Tangage = 0.0F;
        _Roll = 0.0F;
        rprevTime = 0L;
        keyboardPrevTime = 0L;
        timeKoof = 1.0F;
        o = new Orient();

// Pilot Soft View

        lo = new Orient();
        iniSoftFactor = (int)Config.cur.ini.get("Mods", "ngCAMspeedint", 2, 0, 3);
        defSoftFactor = curSoftFactor = 1.0F;

        op = new Orient();
        le = new Loc();
        pe = new Point3d();
        pEnemyAbs = new Point3d();
        Ve = new Vector3d();
        pAbs = new Point3d();
        oAbs = new Orient();
        target = null;
        target2 = null;
        enemy = null;
        stamp = -1L;
        prevTime = -1L;
        roolTime = -1L;
        bUse = false;
        bPadlock = true;
        tPadlockEnd = -1L;
        tPadlockEndLen = 0L;
        bPadlockEnd = false;
        bForward = false;
        bAim = false;
        bUp = false;
        bTubeSight = false;
        bVisibleEnemy = true;
        bSimpleUse = false;
        pCenter = new Point3d();
        pAim = new Point3d();
        pUp = new Point3d();
        rubberBand = new RubberBand();
        bUseTiR = false;
        mx = 0.0F;
        my = 0.0F;
        mz = 0.0F;
        bUseMouse = true;
        timeViewSet = -2000L;
        spineL = 0.5F;
        faceL = 0.08F;
        spineOffsetX = -0.05F;
        leanSideMax = 0.15F;
        leanForwardMax = 0.2F;
        leanForwardMin = -0.2F;
        leanForwardRange = 1.0F;
        leanSideRange = 0.4F;
        raiseRange = 0.15F;
        raiseMax = 0.05F;
        raiseMin = -0.05F;
        trackIR_AngleScale = 10F;
        trackIRFaceL = 0.055F;
        blue = new Color4f(0.0F, 0.0F, 1.0F, 1.0F);
        red = new Color4f(1.0F, 0.0F, 0.0F, 1.0F);
        yellow = new Color4f(1.0F, 1.0F, 0.0F, 1.0F);
        black = new Color4f(0.0F, 0.0F, 0.0F, 1.0F);
        pCenterOrig = new Point3d();
        pSpine = new Point3d();
        pNeck = new Point3d();
        pAimTube = new Point3d();
        bRaiseUp = false;
        bRaiseDown = false;
        bLeanF = false;
        bLeanB = false;
        bLeanSideL = false;
        bLeanSideR = false;
        bHeadMouseMove = false;
        bDebugPrint = false;
    }

    public static HookPilot New()
    {
        if(current == null)
            current = new HookPilot();
        return current;
    }

    public static HookPilot cur()
    {
        return New();
    }

    public float getZNearOffsetX()
    {
        return (float)((pCenter.x - pCenterOrig.x) + (double)spineOffsetX);
    }

    public float getZNearOffsetY()
    {
        return (float)(pCenter.y - pCenterOrig.y);
    }

    private static final boolean __DEBUG__ = false;
    private float stepAzimut;
    private float stepTangage;
    private float maxAzimut;
    private float maxTangage;
    private float minTangage;
    private float maxRoll;
    private float minRoll;
    private float Azimut;
    private float Tangage;
    private float Roll;
    private float _Azimut;
    private float _Tangage;
    private float _Roll;
    private long rprevTime;
    private long keyboardPrevTime;
    private float Px;
    private float Py;
    private float Pz;
    private float azimPadlock;
    private float tangPadlock;
    private float timeKoof;
    private Orient o;

// Pilot Soft View

    private Orient lo;
    private int iniSoftFactor;
    private float defSoftFactor;
    private float curSoftFactor;

    private Orient op;
    private Loc le;
    private Point3d pe;
    private Point3d pEnemyAbs;
    private Vector3d Ve;
    private Point3d pAbs;
    private Orient oAbs;
    private Actor target;
    private Actor target2;
    private Actor enemy;
    private long stamp;
    private long prevTime;
    private long roolTime;
    private boolean bUse;
    private boolean bPadlock;
    private long tPadlockEnd;
    private long tPadlockEndLen;
    private boolean bPadlockEnd;
    private boolean bForward;
    private boolean bAim;
    private boolean bUp;
    private boolean bTubeSight;
    private boolean bVisibleEnemy;
    private boolean bSimpleUse;
    private Point3d pCenter;
    private Point3d pAim;
    private Point3d pUp;
    private RubberBand rubberBand;
    private boolean bUseTiR;
    private static RangeRandom rnd = new RangeRandom();
    private static Point3d P = new Point3d();
    private static Vector3d tmpA = new Vector3d();
    private static Vector3d tmpB = new Vector3d();
    private static Vector3d headShift = new Vector3d();
    private static Vector3d counterForce = new Vector3d();
    private static long oldHeadTime = 0L;
    private static double oldWx = 0.0D;
    private static double oldWy = 0.0D;
    private float mx;
    private float my;
    private float mz;
    private boolean bUseMouse;
    private long timeViewSet;
    public static HookPilot current;
    private static Orient oTmp = new Orient();
    private static Orient oTmp2 = new Orient();
    private static float shakeLVL;
    private Point3d pCenterOrig;
    private float leanForward;
    private float leanSide;
    private float raise;
    private float _leanForward;
    private float _leanSide;
    private float _raise;
    private float spineL;
    private float faceL;
    private float spineOffsetX;
    private float leanForwardDefault;
    private float leanSideMax;
    private float leanForwardMax;
    private float leanForwardMin;
    private float leanForwardRange;
    private float leanSideRange;
    private float raiseRange;
    private float raiseMax;
    private float raiseMin;
    private float trackIR_AngleScale;
    private float trackIRFaceL;
    private float pAimRaise;
    private float pAimLeanS;
    private float pAimLeanF;
    private Point3d pSpine;
    private Point3d pNeck;
    private Point3d pAimTube;
    public boolean bRaiseUp;
    public boolean bRaiseDown;
    public boolean bLeanF;
    public boolean bLeanB;
    public boolean bLeanSideL;
    public boolean bLeanSideR;
    public boolean bHeadMouseMove;
    private String planeString;
    private boolean bDebugPrint;
    private Color4f blue;
    private Color4f red;
    private Color4f yellow;
    private Color4f black;

}
