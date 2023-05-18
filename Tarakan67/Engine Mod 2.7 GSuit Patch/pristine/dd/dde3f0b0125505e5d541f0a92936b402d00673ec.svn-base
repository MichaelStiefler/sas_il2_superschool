// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   CockpitBF_110G_Gunner.java

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookGunner;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapExt;

// Referenced classes of package com.maddox.il2.objects.air:
//            CockpitGunner, AircraftLH, Aircraft

public class CockpitBF_110G_Gunner extends CockpitGunner
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
            HierMesh hiermesh = aircraft().hierMesh();
            hiermesh.chunkVisible("Interior_D0", false);
            hiermesh.chunkVisible("Blister1_D0", false);
            hiermesh.chunkVisible("Blister2_D0", false);
            hiermesh.chunkVisible("Blister3_D0", false);
            hiermesh.chunkVisible("Turret1B_D0", false);
            hiermesh.chunkVisible("Head1_D0", false);
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        hiermesh.chunkVisible("Interior_D0", true);
        hiermesh.chunkVisible("Blister1_D0", true);
        hiermesh.chunkVisible("Blister2_D0", true);
        hiermesh.chunkVisible("Blister3_D0", true);
        hiermesh.chunkVisible("Turret1B_D0", true);
        hiermesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        super.doFocusLeave();
        ((AircraftLH)aircraft()).bWantBeaconKeys = bBeaconKeysEnabled;
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        if(!isToggleAim())
            return;
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        mesh.chunkSetAngles("TurretA", 0.0F, -f, 0.0F);
        mesh.chunkSetAngles("TurretB", 0.0F, f1, 0.0F);
        mesh.chunkSetAngles("TurretC", 0.0F, -FMMath.clamp(f, -cvt(f1, -19F, 12F, 5F, 35F), cvt(f1, -19F, 12F, 5F, 35F)), 0.0F);
        mesh.chunkSetAngles("TurretD", 0.0F, f1, 0.0F);
        float f2;
        byte byte0;
        float f3;
        if(f1 < 0.0F)
        {
            f2 = cvt(f1, -19F, 0.0F, 20F, 30F);
            byte0 = 0;
            f3 = (f1 + 19F) / 19F;
        } else
        if(f1 < 12F)
        {
            f2 = cvt(f1, 0.0F, 12F, 30F, 35F);
            byte0 = 1;
            f3 = (f1 - 0.0F) / 12F;
        } else
        {
            f2 = cvt(f1, 12F, 30F, 35F, 40F);
            byte0 = 2;
            f3 = (f1 - 12F) / 18F;
        }
        float f4 = f / f2;
        f4++;
        float f5 = floatindex(f4, scalePatronsR[byte0]);
        float f6 = floatindex(f4, scalePatronsR[byte0 + 1]);
        float f7 = FMMath.interpolate(f5, f6, f3);
        resetYPRmodifier();
        xyz[1] = f7;
        mesh.chunkSetLocate("PatronsR", xyz, ypr);
        f5 = floatindex(f4, scalePatronsL[byte0]);
        f6 = floatindex(f4, scalePatronsL[byte0 + 1]);
        f7 = FMMath.interpolate(f5, f6, f3);
        resetYPRmodifier();
        xyz[1] = f7;
        mesh.chunkSetLocate("PatronsL", xyz, ypr);
        f5 = floatindex(f4, scalePatronsR1[byte0]);
        f6 = floatindex(f4, scalePatronsR1[byte0 + 1]);
        f7 = FMMath.interpolate(f5, f6, f3);
        mesh.chunkSetAngles("PatronsR1", 0.0F, -f7, 0.0F);
        f5 = floatindex(f4, scalePatronsR2[byte0]);
        f6 = floatindex(f4, scalePatronsR2[byte0 + 1]);
        f7 = FMMath.interpolate(f5, f6, f3);
        mesh.chunkSetAngles("PatronsR2", 0.0F, -f7, 0.0F);
        f5 = floatindex(f4, scalePatronsL1[byte0]);
        f6 = floatindex(f4, scalePatronsL1[byte0 + 1]);
        f7 = FMMath.interpolate(f5, f6, f3);
        mesh.chunkSetAngles("PatronsL1", 0.0F, -f7, 0.0F);
        f5 = floatindex(f4, scalePatronsL2[byte0]);
        f6 = floatindex(f4, scalePatronsL2[byte0 + 1]);
        f7 = FMMath.interpolate(f5, f6, f3);
        mesh.chunkSetAngles("PatronsL2", 0.0F, -f7, 0.0F);
        f5 = floatindex(f4, scaleHylse1[byte0]);
        f6 = floatindex(f4, scaleHylse1[byte0 + 1]);
        f7 = FMMath.interpolate(f5, f6, f3);
        mesh.chunkSetAngles("Hylse1", 0.0F, -f7, 0.0F);
        f5 = floatindex(f4, scaleHylse2[byte0]);
        f6 = floatindex(f4, scaleHylse2[byte0 + 1]);
        f7 = FMMath.interpolate(f5, f6, f3);
        mesh.chunkSetAngles("Hylse2", 0.0F, -f7, 0.0F);
    }

    public void clipAnglesGun(Orient orient)
    {
        if(!isRealMode())
            return;
        if(!aiTurret().bIsOperable)
        {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        if(f1 < -19F)
            f1 = -19F;
        if(f1 > 30F)
            f1 = 30F;
        float f2;
        if(f1 < 0.0F)
            f2 = cvt(f1, -19F, 0.0F, 20F, 30F);
        else
        if(f1 < 12F)
            f2 = cvt(f1, 0.0F, 12F, 30F, 35F);
        else
            f2 = cvt(f1, 12F, 30F, 35F, 40F);
        if(f < 0.0F)
        {
            if(f < -f2)
                f = -f2;
        } else
        if(f > f2)
            f = f2;
        orient.setYPR(-f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[10] = bGunFire;
        Orient orient = hookGunner().getGunMove();
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if(bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN05");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN05");
            if(hook2 == null)
                hook2 = new HookNamed(aircraft(), "_MGUN06");
            doHitMasterAircraft(aircraft(), hook2, "_MGUN06");
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode() || !isToggleAim())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[10] = bGunFire;
    }

    public CockpitBF_110G_Gunner()
    {
        super("3DO/Cockpit/Bf-110G-Gun/hier.him", "bf109");
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
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Pilot1"));
        mesh.materialReplace("Pilot1", mat);
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

    public Hook getHookCameraGun()
    {
        if(bAiming)
            return super.getHookCameraGun();
        else
            return ObserverHook;
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
        mesh.chunkVisible("Head1_D0", aircraft().hierMesh().isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("Head1_D1", aircraft().hierMesh().isChunkVisible("Pilot1_D1"));
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
    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    private LightPointActor light1;
    private boolean bAiming;
    private Hook ObserverHook;
    private boolean bBeaconKeysEnabled;
    private static final float scalePatronsR[][] = {
        {
            0.0F, 0.0F, 0.0F
        }, {
            0.02F, 0.0F, 0.018F
        }, {
            0.061F, 0.044F, 0.061F
        }, {
            0.083F, 0.069F, 0.083F
        }
    };
    private static final float scalePatronsL[][] = {
        {
            0.0F, 0.0F, 0.0F
        }, {
            0.02F, 0.0F, 0.18F
        }, {
            0.061F, 0.044F, 0.061F
        }, {
            0.083F, 0.069F, 0.083F
        }
    };
    private static final float scalePatronsR1[][] = {
        {
            5.5F, 2.0F, -2.5F
        }, {
            13.5F, 0.0F, -1.5F
        }, {
            12F, 0.0F, -1F
        }, {
            15F, 4F, 2.0F
        }
    };
    private static final float scalePatronsR2[][] = {
        {
            4F, 0.0F, -3F
        }, {
            4.5F, 0.0F, -3.5F
        }, {
            9F, 0.5F, -3.5F
        }, {
            10F, 0.0F, -4.5F
        }
    };
    private static final float scalePatronsL1[][] = {
        {
            -4.5F, 2.0F, 4F
        }, {
            -4.5F, 0.0F, 9F
        }, {
            -3F, 0.0F, 10.5F
        }, {
            -3F, 4F, 15F
        }
    };
    private static final float scalePatronsL2[][] = {
        {
            0.0F, 0.0F, 3F
        }, {
            -2F, 0.0F, 9F
        }, {
            -1F, 0.0F, 2.5F
        }, {
            -4F, 0.0F, 8F
        }
    };
    private static final float scaleHylse1[][] = {
        {
            6F, 7F, 6F
        }, {
            0.0F, 0.0F, 0.0F
        }, {
            -8F, 0.0F, -8F
        }, {
            -17F, 0.0F, -17F
        }
    };
    private static final float scaleHylse2[][] = {
        {
            -8F, 0.0F, 8F
        }, {
            -7F, 0.0F, 7F
        }, {
            -8F, 0.0F, 8F
        }, {
            -1F, 0.0F, 1.0F
        }
    };
    private Hook hook1;
    private Hook hook2;

    static 
    {
        Property.set(CLASS.THIS(), "normZN", 0.8F);
    }








}
