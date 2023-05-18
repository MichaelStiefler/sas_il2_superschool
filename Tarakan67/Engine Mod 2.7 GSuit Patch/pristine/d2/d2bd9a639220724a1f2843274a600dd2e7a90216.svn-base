// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 17.01.2015 13:09:14
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CockpitBF_110G4NJ_RadarOp.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple2f;
import com.maddox.JGP.Vector2f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.electronics.RadarLiSN2;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitPilot, Cockpit, BF_110G4NJ

public class CockpitBF_110G4NJ_RadarOp extends CockpitPilot
{
    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
        	setTmp = setOld;
            setOld = setNew;
            setNew = setTmp;
            if(fm == null)
                return true;
            setNew.altimeter = fm.getAltitude();          
            if(bNeedSetUp)
            {
                reflectPlaneMats();
                bNeedSetUp = false;
            }
            float f = waypointAzimuthInvertMinus(20F);
            if(useRealisticNavigationInstruments())
            {
                setNew.waypointAzimuth.setDeg(f - 90F);
                setOld.waypointAzimuth.setDeg(f - 90F);
                setNew.radioCompassAzimuth.setDeg(setOld.radioCompassAzimuth.getDeg(0.02F), radioCompassAzimuthInvertMinus() - setOld.azimuth.getDeg(1.0F) - 90F);
            } else
            {
                setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f - setOld.azimuth.getDeg(1.0F));
            }
            setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
            setNew.beaconDirection = (10F * setOld.beaconDirection + getBeaconDirection()) / 11F;
            setNew.beaconRange = (10F * setOld.beaconRange + getBeaconRange()) / 11F;         
            return true;
        }

        Interpolater()
        {
        }
    }
    private class Variables
    {

        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float beaconDirection;
        float beaconRange;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
            radioCompassAzimuth = new AnglesFork();
        }

    }

    protected boolean doFocusEnter()
    {
    	 bBeaconKeysEnabled = ((AircraftLH)aircraft()).bWantBeaconKeys;
         ((AircraftLH)aircraft()).bWantBeaconKeys = true;
    	if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            aircraft().hierMesh().chunkVisible("CF_D0", false);
            aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            aircraft().hierMesh().chunkVisible("Blister2_D0", false);
            aircraft().hierMesh().chunkVisible("Blister3_D0", false);
            aircraft().hierMesh().chunkVisible("Blister4_D0", false);
            aircraft().hierMesh().chunkVisible("Head1_D0", false);
            aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Interior_D0", true);
        aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        aircraft().hierMesh().chunkVisible("CF_D0", true);
        aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
        aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        aircraft().hierMesh().chunkVisible("Blister2_D0", true);
        aircraft().hierMesh().chunkVisible("Blister3_D0", true);
        aircraft().hierMesh().chunkVisible("Blister4_D0", true);
        aircraft().hierMesh().chunkVisible("Head1_D0", true);
        aircraft().hierMesh().chunkVisible("Pilot3_D0", true);
        super.doFocusLeave();
        ((AircraftLH)aircraft()).bWantBeaconKeys = bBeaconKeysEnabled;
    }

    protected void interpTick()
    {
    }

    public void doGunFire(boolean flag)
    {
        System.out.println("FIRE");
    }

    public CockpitBF_110G4NJ_RadarOp()
    {
        super("3DO/Cockpit/Bf-110G-Radar/hier.him", "he111");
        bNeedSetUp = true;
        bAiming = true;
        ObserverHook = null;
        hook1 = null;
        hook2 = null;
        setOld = new Variables();
        setNew = new Variables();
        cockpitNightMats = (new String[] {
            "cadran1", "radio", "bague2"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        light1.light.setColor(126F, 232F, 245F);
        light1.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK1", light1);
        ObserverHook = new HookNamed(mesh, "CAMERAAIM");
        printCompassHeading = true;
        bBeaconKeysEnabled = ((AircraftLH)aircraft()).bWantBeaconKeys;
        ((AircraftLH)aircraft()).bWantBeaconKeys = true;
        bEntered = false;
        rangeMaxMovement = 0.073296F;
        rangeMinMovement = 0.011637F;
        contactMaxMovement = 0.012F;
        spikeSlope = 0.2F;
        contactRotationAngle = 5F;
        maxNumberOfContacts = 41;
        ac = null;
        updateRadar = 0;
        ac = (BF_110G4NJ)aircraft();
        contacts = new Vector2f[4][maxNumberOfContacts];
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < maxNumberOfContacts; j++)
                contacts[i][j] = new Vector2f();

        }

        radar = new RadarLiSN2();
        radar.init(ac, contactMaxMovement, rangeMaxMovement, maxNumberOfContacts, spikeSlope);
    }

    private void enter()
    {
        System.out.println("ENTER");
        saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if(hookpilot.isPadlock())
            hookpilot.stopPadlock();
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        bEntered = true;
    }

    private void leave()
    {
        if(bEntered)
        {
            System.out.println("leave");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            bEntered = false;
        }
    }

    public void destroy()
    {
        leave();
        super.destroy();
    }

   

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        super.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        super.mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
        super.mesh.materialReplace("Pilot1", mat);
    }
    
    public boolean isToggleAim()
    {
        return bAiming;
    }

    public void doToggleAim(boolean flag)
    {
        bAiming = !bAiming;
        super.doToggleAim(flag);
    }

    

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light1.light.setEmit(0.004F, 1.0F);
            setNightMats(true);
        } else
        {
            light1.light.setEmit(0.0F, 0.0F);
            setNightMats(false);
        }
    }
    public void reflectWorldToInstruments(float f)
    {
        if(fm == null)
            return;
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
           
        super.mesh.chunkVisible("Head1_D0", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
            super.mesh.chunkVisible("Head1_D1", aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
            mesh.chunkSetAngles("zAlt2", cvt(interp(setNew.altimeter, setNew.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
            mesh.chunkSetAngles("zAlt1", cvt(interp(setNew.altimeter, setNew.altimeter, f), 0.0F, 14000F, 0.0F, 313F), 0.0F, 0.0F);
            if(useRealisticNavigationInstruments())
            {
                mesh.chunkSetAngles("Z_CompassRim", 0.0F, -setNew.waypointAzimuth.getDeg(f), 0.0F);
                mesh.chunkSetAngles("Z_CompassPlane", 0.0F, setNew.azimuth.getDeg(f) - setNew.waypointAzimuth.getDeg(f), 0.0F);
                mesh.chunkSetAngles("Z_CompassNeedle", 0.0F, setNew.radioCompassAzimuth.getDeg(f * 0.02F) + 90F, 0.0F);
            } else
            {
                mesh.chunkSetAngles("Z_CompassRim", 0.0F, -setNew.azimuth.getDeg(f), 0.0F);
                mesh.chunkSetAngles("Z_CompassPlane", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
                mesh.chunkSetAngles("Z_CompassNeedle", 0.0F, 0.0F, 0.0F);
            }
            if(aircraft().FM.AS.listenLorenzBlindLanding)
            {
                mesh.chunkSetAngles("Z_AFN12", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, -14F, 14F), 0.0F);
                mesh.chunkSetAngles("Z_AFN11", 0.0F, cvt(setNew.beaconRange, 0.0F, 1.0F, 26.5F, -26.5F), 0.0F);
                mesh.chunkSetAngles("Z_AFN22", 0.0F, 0.0F, 0.0F);
                mesh.chunkSetAngles("Z_AFN21", 0.0F, 20F, 0.0F);
                mesh.chunkVisible("AFN1_RED", isOnBlindLandingMarker());
            } else
            {
                mesh.chunkSetAngles("Z_AFN22", 0.0F, cvt(setNew.beaconDirection, -45F, 45F, -20F, 20F), 0.0F);
                mesh.chunkSetAngles("Z_AFN21", 0.0F, cvt(setNew.beaconRange, 0.0F, 1.0F, 20F, -20F), 0.0F);
                mesh.chunkSetAngles("Z_AFN12", 0.0F, 0.0F, 0.0F);
                mesh.chunkSetAngles("Z_AFN11", 0.0F, -26.5F, 0.0F);
                mesh.chunkVisible("AFN1_RED", false);
            }
            updateRadar();
        }
    

    private void updateRadar()
    {
        updateRadar++;
        if(updateRadar < 0)
            return;
        updateRadar = 0;
        radar.rareAction();
        int ai[] = radar.getContacts(contacts);
        float f = (float)Math.random();
        float f1 = (float)(Math.random() * 0.00014999999999999999D);
        Cockpit.ypr[0] = 0.0F;
        Cockpit.ypr[1] = 0.0F;
        Cockpit.ypr[2] = 0.0F;
        Cockpit.xyz[0] = 0.0F;
        Cockpit.xyz[1] = 0.0F;
        Cockpit.xyz[2] = 0.0F;
        int i = (int)Math.round(Math.random() * 5D);
        for(int j = 0; j < 6; j++)
        {
            String s = "BackgroundNoise" + j;
            String s2 = "EGroundNoise" + j;
            String s4 = "AGroundNoise" + j;
            String s6 = "ETransferPulse" + j;
            String s8 = "ATransferPulse" + j;
            if(j == i)
            {
                super.mesh.chunkVisible(s, true);
                super.mesh.chunkVisible(s6, true);
                super.mesh.chunkVisible(s8, true);
                Cockpit.xyz[1] = -rangeMinMovement + f * 0.0005F;
                super.mesh.chunkSetLocate(s6, Cockpit.xyz, Cockpit.ypr);
                super.mesh.chunkSetLocate(s8, Cockpit.xyz, Cockpit.ypr);
            } else
            {
                super.mesh.chunkVisible(s, false);
                super.mesh.chunkVisible(s2, false);
                super.mesh.chunkVisible(s4, false);
                super.mesh.chunkVisible(s6, false);
                super.mesh.chunkVisible(s8, false);
            }
        }

        for(int k = 0; k < maxNumberOfContacts; k++)
        {
            String s1 = "EContact_" + k + "_U";
            String s3 = "EContact_" + k + "_D";
            String s5 = "AContact_" + k + "_L";
            String s7 = "AContact_" + k + "_R";
            super.mesh.chunkVisible(s1, true);
            super.mesh.chunkVisible(s3, true);
            super.mesh.chunkVisible(s5, true);
            super.mesh.chunkVisible(s7, true);
            Cockpit.xyz[1] = -((Tuple2f) (contacts[1][k])).x;
            Cockpit.xyz[0] = ((Tuple2f) (contacts[1][k])).y - contactMaxMovement;
            Cockpit.ypr[1] = cvt(Cockpit.xyz[0], -contactMaxMovement, 0.0F, 0.0F, contactRotationAngle);
            super.mesh.chunkSetLocate(s3, Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[1] = -((Tuple2f) (contacts[0][k])).x;
            Cockpit.xyz[0] = ((Tuple2f) (contacts[0][k])).y - contactMaxMovement;
            Cockpit.ypr[1] = -cvt(Cockpit.xyz[0], -contactMaxMovement, 0.0F, 0.0F, contactRotationAngle);
            super.mesh.chunkSetLocate(s1, Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[1] = -((Tuple2f) (contacts[2][k])).x;
            Cockpit.xyz[0] = ((Tuple2f) (contacts[2][k])).y - contactMaxMovement;
            Cockpit.ypr[1] = -cvt(Cockpit.xyz[0], -contactMaxMovement, 0.0F, 0.0F, contactRotationAngle);
            super.mesh.chunkSetLocate(s5, Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[1] = -((Tuple2f) (contacts[3][k])).x;
            Cockpit.xyz[0] = ((Tuple2f) (contacts[3][k])).y - contactMaxMovement;
            Cockpit.ypr[1] = cvt(Cockpit.xyz[0], -contactMaxMovement, 0.0F, 0.0F, contactRotationAngle);
            super.mesh.chunkSetLocate(s7, Cockpit.xyz, Cockpit.ypr);
        }

    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private boolean bAiming;
    private Hook ObserverHook;
    private boolean bBeaconKeysEnabled;
    private boolean bEntered;
    private float saveFov;
    private float rangeMaxMovement;
    private float rangeMinMovement;
    private float contactMaxMovement;
    float spikeSlope;
    float contactRotationAngle;
    int maxNumberOfContacts;
    private BF_110G4NJ ac;
    private int updateRadar;
    private Vector2f contacts[][];
    private RadarLiSN2 radar;
    private Hook hook1;
    private Hook hook2;
    
    static 
    {
        Property.set(com.maddox.il2.objects.air.CockpitBF_110G4NJ_RadarOp.class, "aiTuretNum", 6);
        Property.set(com.maddox.il2.objects.air.CockpitBF_110G4NJ_RadarOp.class, "weaponControlNum", 20);
    }


}
