// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CockpitE13A1.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.*;
import com.maddox.sound.Acoustics;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.util.HashMapExt;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, E13A, Aircraft, E13A1, 
//            Cockpit

public class CockpitE13A1 extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(ac != null && ac.bChangedPit)
            {
                reflectPlaneToModel();
                ac.bChangedPit = false;
            }
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.flaps = 0.9F * setOld.flaps + 0.1F * fm.CT.FlapsControl;
                setNew.hook = 0.9F * setOld.hook + 0.1F * fm.CT.arrestorControl;
                setNew.gear = 0.7F * setOld.gear + 0.3F * fm.CT.GearControl;
                setNew.throttle = 0.8F * setOld.throttle + 0.2F * fm.CT.PowerControl;
                setNew.prop = 0.8F * setOld.prop + 0.2F * fm.EI.engines[0].getControlProp();
                setNew.mix = 0.8F * setOld.mix + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.altimeter = fm.getAltitude();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                setNew.vspeed = (399F * setOld.vspeed + fm.getVertSpeed()) / 400F;
                if(gearLightsDamaged && Math.random() > 0.5D)
                    flickerLight = !flickerLight;
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                setNew.manifold = 0.8F * setOld.manifold + 0.2F * (fm.EI.engines[0].getManifoldPressure() - 1.0F) * 76F;
                float f = waypointAzimuth();
                if(useRealisticNavigationInstruments())
                    setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(1.0F), getBeaconDirection());
                else
                    setNew.waypointDeviation.setDeg(setOld.waypointDeviation.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
                if(amountOfBombs == 1)
                {
                    if(!bombs[0].haveBullets())
                        bombLever2 = 1.0F;
                } else
                if(amountOfBombs == 3)
                {
                    if(!bombs[1].haveBullets())
                        bombLever1 = 1.0F;
                    if(!bombs[2].haveBullets())
                        bombLever2 = 1.0F;
                    if(!bombs[3].haveBullets())
                        bombLever3 = 1.0F;
                } else
                if(amountOfBombs == 6)
                {
                    if(!bombs[4].haveBullets())
                        bombLever1 = 0.5F;
                    if(!bombs[5].haveBullets())
                        bombLever1 = 1.0F;
                    if(!bombs[6].haveBullets())
                        bombLever2 = 0.5F;
                    if(!bombs[7].haveBullets())
                        bombLever2 = 1.0F;
                    if(!bombs[8].haveBullets())
                        bombLever3 = 0.5F;
                    if(!bombs[9].haveBullets())
                        bombLever3 = 1.0F;
                }
                if(amountOfBombs != 0)
                {
                    if(bombLever1cur < bombLever1)
                        bombLever1cur += 0.1F;
                    if(bombLever2cur < bombLever2)
                        bombLever2cur += 0.1F;
                    if(bombLever3cur < bombLever3)
                        bombLever3cur += 0.1F;
                    if(bombLever1cur == 1.0F && bombLever2cur == 1.0F && bombLever3cur == 1.0F)
                        amountOfBombs = 0;
                }
                torpSightAngle = ((E13A1)aircraft()).fSightCurTorpslip;
                HookPilot hookpilot = HookPilot.current;
                if(bTorp && isFocused() && hookpilot.isAimReached())
                {
                    hookpilot.setInstantOrient(torpSightAngle, 0.0F, 0.0F);
                    setAimCoordinates();
                }
            }
            return true;
        }

        Interpolater()
        {
        }
    }

    private class Variables
    {

        float flaps;
        float turn;
        float hook;
        float gear;
        float throttle;
        float prop;
        float mix;
        float divebrake;
        float altimeter;
        float vspeed;
        float manifold;
        AnglesFork azimuth;
        AnglesFork waypointDeviation;

        private Variables()
        {
            manifold = 0.0F;
            azimuth = new AnglesFork();
            waypointDeviation = new AnglesFork();
        }

    }


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(5F);
    }

    protected void setCameraOffset()
    {
        cameraCenter.add(0.079999998211860657D, 0.0D, 0.0D);
    }

    public CockpitE13A1()
    {
        super("3DO/Cockpit/B5N2/hier.him", "bf109");
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        pictAiler = 0.0F;
        pictElev = 0.0F;
        bombLever1cur = 0.0F;
        bombLever2cur = 0.0F;
        bombLever3cur = 0.0F;
        bombLever1 = 0.0F;
        bombLever2 = 0.0F;
        bombLever3 = 0.0F;
        bNeedSetUp = true;
        amountOfBombs = 0;
        gearLightsDamaged = false;
        flickeringLight = "";
        flickerLight = false;
        torpSightAngle = 0.0F;
        enteringAim = false;
        bTorp = false;
        ac = null;
        bEntered = false;
        cockpitNightMats = (new String[] {
            "bombergauges", "dgauges1", "dgauges2", "dgauges3", "dgauges4", "dgauges5", "dgauges6", "gauges1", "gauges2", "gauges3", 
            "gauges4", "gauges5", "gauges6", "turnbankneedles", "gyro", "Voltammeter", "DVoltammeter"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        if(acoustics != null)
            acoustics.globFX = new ReverbFXRoom(0.45F);
        ac = (E13A)aircraft();
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(126F, 232F, 245F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK01", light1);
        hooknamed = new HookNamed(mesh, "LAMPHOOK02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        light2.light.setColor(126F, 232F, 245F);
        light2.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK02", light2);
        hooknamed = new HookNamed(mesh, "LAMPHOOK03");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        light3.light.setColor(126F, 232F, 245F);
        light3.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK03", light3);
        try
        {
            bombs[0] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb02");
            amountOfBombs++;
        }
        catch(Exception exception) { }
        try
        {
            bombs[1] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
            amountOfBombs++;
        }
        catch(Exception exception1) { }
        try
        {
            bombs[2] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
            amountOfBombs++;
        }
        catch(Exception exception2) { }
        try
        {
            bombs[3] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
            amountOfBombs++;
        }
        catch(Exception exception3) { }
        try
        {
            bombs[4] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb06");
            amountOfBombs++;
        }
        catch(Exception exception4) { }
        try
        {
            bombs[5] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb07");
            amountOfBombs++;
        }
        catch(Exception exception5) { }
        try
        {
            bombs[6] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb08");
            amountOfBombs++;
        }
        catch(Exception exception6) { }
        try
        {
            bombs[7] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb09");
            amountOfBombs++;
        }
        catch(Exception exception7) { }
        try
        {
            bombs[8] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb10");
            amountOfBombs++;
        }
        catch(Exception exception8) { }
        try
        {
            bombs[9] = (BombGun)((Aircraft)fm.actor).getBulletEmitterByHookName("_ExternalBomb11");
            amountOfBombs++;
        }
        catch(Exception exception9) { }
        if(amountOfBombs == 1)
        {
            bombLever1cur = 1.0F;
            bombLever3cur = 1.0F;
        }
        if(aircraft().thisWeaponsName.startsWith("1x91"))
            bTorp = true;
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        resetYPRmodifier();
        xyz[1] = cvt(fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.655F);
        mesh.chunkSetLocate("Z_Canopy", xyz, ypr);
        if(!gearLightsDamaged)
        {
            mesh.chunkVisible("XGreenL", fm.CT.getGearL() == 1.0F && fm.Gears.lgear);
            mesh.chunkVisible("XGreenR", fm.CT.getGearR() == 1.0F && fm.Gears.lgear);
            mesh.chunkVisible("XRedL", fm.CT.getGearL() == 0.0F);
            mesh.chunkVisible("XRedR", fm.CT.getGearR() == 0.0F);
            mesh.chunkVisible("XGreenC", fm.CT.getArrestor() == 1.0F);
        } else
        {
            mesh.chunkVisible(flickeringLight, flickerLight);
        }
        mesh.chunkSetAngles("Z_Stick4", 0.0F, -(pictElev = 0.85F * pictElev + 0.15F * fm.CT.ElevatorControl) * 10F, 0.0F);
        float f1 = (pictAiler = 0.85F * pictAiler + 0.15F * fm.CT.AileronControl) * 15F;
        mesh.chunkSetAngles("Z_Stick1", 0.0F, f1, 0.0F);
        mesh.chunkSetAngles("Z_Stick3", 0.0F, -f1, 0.0F);
        mesh.chunkSetAngles("Z_Stick2", 0.0F, -f1, 0.0F);
        mesh.chunkSetAngles("Z_Rudder", 0.0F, -15F * fm.CT.getRudder(), 0.0F);
        mesh.chunkSetAngles("Z_ElevatorTrim", 0.0F, 70F * fm.CT.getTrimElevatorControl(), 0.0F);
        mesh.chunkSetAngles("Z_RudderTrim", 0.0F, 40F * fm.CT.getTrimRudderControl(), 0.0F);
        mesh.chunkSetAngles("Z_AileronTrim", 0.0F, 40F * fm.CT.getTrimAileronControl(), 0.0F);
        mesh.chunkSetAngles("Z_ArrestingHook", 0.0F, -80F * setNew.hook, 0.0F);
        mesh.chunkSetAngles("Z_LandingGear", 0.0F, 70F * setNew.gear, 0.0F);
        mesh.chunkSetAngles("Z_Flaps", 0.0F, 70F * setNew.flaps, 0.0F);
        mesh.chunkSetAngles("Z_CowlFlaps", 0.0F, -75F + fm.EI.engines[0].getControlRadiator() * 75F, 0.0F);
        mesh.chunkSetAngles("Z_AutoThrottle", 0.0F, cvt(interp(setNew.throttle, setOld.throttle, f), 0.0F, 1.2F, 0.0F, 110F), 0.0F);
        mesh.chunkSetAngles("Z_AutoProp", 0.0F, cvt(interp(setNew.prop, setOld.prop, f), 0.0F, 1.0F, 0.0F, 110F), 0.0F);
        mesh.chunkSetAngles("Z_Mixture", 0.0F, cvt(interp(setNew.mix, setOld.mix, f), 0.0F, 1.2F, 0.0F, 85F), 0.0F);
        mesh.chunkSetAngles("Z_Need_airspeed", 0.0F, floatindex(cvt(0.539957F * Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 30F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Z_Need_RPM", 0.0F, cvt(fm.EI.engines[0].getRPM(), 500F, 3500F, 0.0F, 540F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Variometer", 0.0F, cvt(setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Turn1", 0.0F, cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F), 0.0F);
        float f2 = getBall(4D);
        mesh.chunkSetAngles("Z_Need_ball", 0.0F, cvt(f2, -4F, 4F, -7.5F, 7.5F), 0.0F);
        mesh.chunkSetAngles("Z_Need_compass", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_Need_gyro", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Z_horizon1a", 0.0F, -fm.Or.getKren(), 0.0F);
        mesh.chunkSetAngles("Z_horizon1b", 0.0F, cvt(-fm.Or.getTangage(), -45F, 45F, -22F, 22F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Cylheadtemp", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 69F), 0.0F);
        mesh.chunkSetAngles("Z_Need_oiltemp", 0.0F, floatindex(cvt(fm.EI.engines[0].tOilIn, 30F, 110F, 0.0F, 8F), oilTempScale), 0.0F);
        mesh.chunkSetAngles("Z_Need_Rfuel", 0.0F, cvt(fm.M.fuel / fm.M.maxFuel, 0.0F, 1.0F, 0.0F, 236F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Lfuel", 0.0F, cvt(fm.M.fuel / fm.M.maxFuel, 0.0F, 1.0F, 0.0F, 236F), 0.0F);
        resetYPRmodifier();
        Cockpit.xyz[1] = -cvt(fm.Or.getTangage(), -15F, 15F, 0.043652F, -0.043652F);
        mesh.chunkSetLocate("Z_Inclinometer", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        Cockpit.xyz[1] = -cvt(fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -0.1175F);
        mesh.chunkSetLocate("Z_Flapindicator", Cockpit.xyz, Cockpit.ypr);
        mesh.chunkSetAngles("Z_Need_navigation", 0.0F, cvt(setNew.waypointDeviation.getDeg(f * 0.2F), -25F, 25F, -45F, 45F), 0.0F);
        mesh.chunkSetAngles("Z_BombRelease1", 0.0F, 50F * bombLever1cur, 0.0F);
        mesh.chunkSetAngles("Z_BombRelease2", 0.0F, 50F * bombLever2cur, 0.0F);
        mesh.chunkSetAngles("Z_BombRelease3", 0.0F, 50F * bombLever3cur, 0.0F);
        mesh.chunkSetAngles("Z_Need_manifold", 0.0F, cvt(setNew.manifold, -50F, 30F, -202.5F, 112.5F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Alt1a", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Alt1b", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 30000F, 0.0F, 2160F), 0.0F);
        float f3 = fm.turret[0].tu[0];
        float f4 = fm.turret[0].tu[1];
        mesh.chunkSetAngles("Z_Gun1", 0.0F, f3, 0.0F);
        mesh.chunkSetAngles("Z_T92Mg", 0.0F, f4, 0.0F);
        mesh.chunkSetAngles("Z_MagnetoSwitch", 0.0F, cvt(fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, -46F, 46F), 0.0F);
        mesh.chunkSetAngles("Z_Need_Cylheadtemp", 0.0F, floatindex(cvt(fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 3F), waterTempScale), 0.0F);
        mesh.chunkSetAngles("Z_Need_exhausttemp", 0.0F, cvt(fm.EI.engines[0].tWaterOut, 50F, 350F, 0.0F, 68F), 0.0F);
        mesh.chunkSetAngles("Z_Need_oilpressb", 0.0F, 100F, 0.0F);
        mesh.chunkSetAngles("Z_Need_oilpressc", 0.0F, 100F, 0.0F);
        mesh.chunkSetAngles("Z_Need_pressfuel", 0.0F, cvt(fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 2.0F, 0.0F, 180F), 0.0F);
        mesh.chunkSetAngles("Z_Need_pressoil", 0.0F, cvt(1.0F + 0.05F * fm.EI.engines[0].tOilIn, 0.0F, 8.5F, 0.0F, -182F), 0.0F);
        if(bTorp)
        {
            mesh.chunkSetAngles("Z_TorpedoSight1", 0.0F, ((E13A1)aircraft()).fSightCurTorpslip, 0.0F);
            mesh.chunkSetAngles("Z_TorpedoSight2", 0.0F, ((E13A1)aircraft()).fSightCurTorpslip, 0.0F);
            mesh.chunkSetAngles("Z_TorpedoSight3", 0.0F, -cvt(((E13A1)aircraft()).fSightCurTorpslip, -40F, 40F, -48F, 48F), 0.0F);
            mesh.chunkSetAngles("Z_TorpedoSight4", 0.0F, cvt(((E13A1)aircraft()).fSightCurTorpslip, -40F, 40F, -48F, 48F), 0.0F);
        }
        mesh.chunkSetAngles("Z_Need_vacuum", 0.0F, 180F, 0.0F);
        if(bTorp && enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            if(hookpilot.isAimReached())
            {
                enteringAim = false;
                enter();
            } else
            if(!hookpilot.isAim())
                enteringAim = false;
        }
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("holesMiddle", true);
            mesh.chunkVisible("DGauge4", true);
            mesh.chunkVisible("Gauge4", false);
            mesh.chunkVisible("Z_Need_airspeed", false);
            mesh.chunkVisible("Z_Need_pressoil", false);
            mesh.chunkVisible("Z_Need_pressfuel", false);
            mesh.chunkVisible("Z_Need_oiltemp", false);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("DGauge1", true);
            mesh.chunkVisible("Gauge1", false);
            mesh.chunkVisible("Z_Need_Alt1b", false);
            mesh.chunkVisible("Z_Need_Alt1a", false);
            mesh.chunkVisible("Z_Need_RPM", false);
            mesh.chunkVisible("Z_Need_manifold", false);
            mesh.chunkVisible("Z_Need_Cylheadtemp", false);
            mesh.chunkVisible("Z_Need_carburator", false);
            mesh.chunkVisible("DGauge6", true);
            mesh.chunkVisible("Gauge6", false);
            mesh.chunkVisible("Z_Need_vacuum", false);
            mesh.chunkVisible("Z_Inclinometer", false);
            gearLightsDamaged = true;
            if(fm.CT.getGear() == 1.0F)
                flickeringLight = "XGreenL";
            else
            if(fm.getAltitude() > 2000F)
                flickeringLight = "XRedL";
            else
            if(fm.getAltitude() > 500F)
                flickeringLight = "XGreenC";
            else
                flickeringLight = "XGreenR";
            mesh.chunkVisible("XGreenL", false);
            mesh.chunkVisible("XGreenR", false);
            mesh.chunkVisible("XRedL", false);
            mesh.chunkVisible("XRedR", false);
            mesh.chunkVisible("XGreenC", false);
            mesh.chunkVisible("holesPit", true);
        }
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("holesGunner", true);
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("holesCanopyNav", true);
            mesh.chunkVisible("DGauge3", true);
            mesh.chunkVisible("Gauge3", false);
            mesh.chunkVisible("Z_Need_Lfuel", false);
        }
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("ZOil", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            mesh.chunkVisible("holesCanopyNav", true);
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("holesCanopy", true);
            mesh.chunkVisible("DGauge5", true);
            mesh.chunkVisible("Gauge5", false);
            mesh.chunkVisible("Z_Need_gyro", false);
            mesh.chunkVisible("Z_Need_exhausttemp", false);
            mesh.chunkVisible("Z_Need_oilpressc", false);
            mesh.chunkVisible("holesPit", true);
        }
        if((fm.AS.astateCockpitState & 2) != 0)
        {
            mesh.chunkVisible("holesFront", true);
            mesh.chunkVisible("DGauge2", true);
            mesh.chunkVisible("Gauge2", false);
            mesh.chunkVisible("Z_Need_Turn1", false);
            mesh.chunkVisible("Z_Need_ball", false);
            mesh.chunkVisible("Z_Need_Variometer", false);
            mesh.chunkVisible("Z_Need_navigation", false);
        }
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", false);
            aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", false);
            aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", false);
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            aircraft().hierMesh().chunkVisible("HMask2_D0", false);
            aircraft().hierMesh().chunkVisible("HMask3_D0", false);
            ((E13A1)aircraft()).bTorpSight = false;
            if(bTorp)
            {
                ((E13A1)aircraft()).bTorpSight = true;
                HookPilot hookpilot = HookPilot.current;
                hookpilot.doAim(false);
                setAimCoordinates();
            }
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
        {
            return;
        } else
        {
            aircraft().hierMesh().chunkVisible("BlisterInterior1_D0", true);
            aircraft().hierMesh().chunkVisible("BlisterInterior2_D0", true);
            aircraft().hierMesh().chunkVisible("BlisterInterior3_D0", true);
            aircraft().hierMesh().chunkVisible("Interior_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1A_D0", true);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot2_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D0", true);
            aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
            ((E13A1)aircraft()).bTorpSight = false;
            leave();
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter()
    {
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        if(bTorp)
        {
            hookpilot.setSimpleAimOrient(torpSightAngle, 0.0F, 0.0F);
            setAimCoordinates();
        }
        hookpilot.doAim(true);
        enteringAim = true;
    }

    private void enter()
    {
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleUse(true);
        doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(enteringAim)
        {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setInstantOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if(!bEntered)
        {
            return;
        } else
        {
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
            return;
        }
    }

    protected void reflectPlaneToModel()
    {
        //aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
        //aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
        aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
        aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
        aircraft().hierMesh().chunkVisible("HMask2_D0", false);
        aircraft().hierMesh().chunkVisible("HMask3_D0", false);
        mesh.chunkVisible("pilotNavigator_d0", aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        mesh.chunkVisible("pilotNavigator_d1", !aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        mesh.chunkVisible("pilotgunner_d0", aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
        mesh.chunkVisible("pilotgunner_d1", !aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        mesh.materialReplace("Pilot2", mat);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light1.light.setEmit(0.003F, 0.3F);
            light2.light.setEmit(0.003F, 0.3F);
            light3.light.setEmit(0.003F, 0.3F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            light2.light.setEmit(0.0F, 0.0F);
            light3.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }

    public void doToggleAim(boolean flag)
    {
        if(!isFocused())
            return;
        if(isToggleAim() == flag)
            return;
        if(flag)
            prepareToEnter();
        else
            leave();
    }

    public void destroy()
    {
        leave();
        super.destroy();
    }

    private void setAimCoordinates()
    {
        HookPilot hookpilot = HookPilot.current;
        Point3d point3d = new Point3d();
        double d = 0.20499999999999999D;
        double d1 = -Math.cos(((double)torpSightAngle * 3.1415926535897931D) / 180D) * d;
        double d2 = Math.sin(((double)torpSightAngle * 3.1415926535897931D) / 180D) * d;
        point3d.set(0.40000000596046448D + d + d1, 0.0D + d2, 0.0D);
        point3d.add(cameraCenter);
        hookpilot.setAim(point3d);
    }

    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float pictAiler;
    private float pictElev;
    private float bombLever1cur;
    private float bombLever2cur;
    private float bombLever3cur;
    private float bombLever1;
    private float bombLever2;
    private float bombLever3;
    private boolean bNeedSetUp;
    private LightPointActor light1;
    private LightPointActor light2;
    private LightPointActor light3;
    private BombGun bombs[] = {
        null, null, null, null, null, null, null, null, null, null
    };
    private int amountOfBombs;
    private boolean gearLightsDamaged;
    private String flickeringLight;
    private boolean flickerLight;
    private static final float speedometerScale[] = {
        0.0F, 7F, 14F, 21F, 28F, 43.5F, 62F, 81F, 104.5F, 130F, 
        157F, 184.5F, 214F, 244.5F, 275.5F, 305F, 333F, 363F, 388F, 420F, 
        445F, 472F, 497F, 522F, 549F, 573F, 595F, 616F, 635F, 656F, 
        675F
    };
    private static final float oilTempScale[] = {
        0.0F, 19F, 43F, 68F, 98F, 134F, 175F, 221F, 264F
    };
    private static final float waterTempScale[] = {
        0.0F, 20F, 38F, 69F
    };
    private float torpSightAngle;
    private boolean enteringAim;
    private boolean bTorp;
    private E13A ac;
    private boolean bEntered;

    static 
    {
        Property.set(CLASS.THIS(), "normZNs", new float[] {
            1.17F, 1.0F, 1.0F, 1.0F
        });
    }

}
