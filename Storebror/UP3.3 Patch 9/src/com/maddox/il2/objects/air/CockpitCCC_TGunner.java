package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitCCC_TGunner extends CockpitGunner
{
    private class Variables
    {

        float throttle1;
        float mix1;
        float pictElev;
        float pictAiler;
        float pictRudd;
        float compasTangage;
        float compasKren;
        float azimuth;
        float altimeter;
        float turn;
        float Asbr2;
        float dimPos;
        float Airstartr;

        private Variables()
        {
        }

    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                setNew.pictElev = 0.8F * setOld.pictElev + 0.2F * fm.CT.ElevatorControl;
                setNew.pictAiler = 0.8F * setOld.pictAiler + 2.0F * fm.CT.AileronControl;
                setNew.pictRudd = 0.8F * setOld.pictRudd + 0.2F * fm.CT.getRudder();
                setNew.throttle1 = 0.9F * setOld.throttle1 + 0.1F * fm.EI.engines[0].getControlThrottle();
                setNew.mix1 = 0.8F * setOld.mix1 + 0.2F * fm.EI.engines[0].getControlMix();
                setNew.altimeter = fm.getAltitude();
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.turn = (12F * setOld.turn + w.z) / 13F;
                if(Math.abs(fm.Or.getKren()) < 20F && Math.abs(fm.Or.getTangage()) < 20F)
                    setNew.azimuth = (35F * setOld.azimuth + -fm.Or.getYaw()) / 36F;
                if(setOld.azimuth > 270F && setNew.azimuth < 90F)
                    setOld.azimuth -= 360F;
                if(setOld.azimuth < 90F && setNew.azimuth > 270F)
                    setOld.azimuth += 360F;
                setNew.compasTangage = 0.95F * setOld.compasTangage + 0.05F * cvt(fm.Or.getTangage(), -20F, 20F, -20F, 20F);
                setNew.compasKren = 0.95F * setOld.compasKren + 0.05F * cvt(fm.Or.getKren(), -20F, 20F, -20F, 20F);
                if(cockpitDimControl)
                {
                    if(setNew.dimPos < 1.0F)
                        setNew.dimPos = setOld.dimPos + 0.05F;
                } else
                if(setNew.dimPos > 0.0F)
                    setNew.dimPos = setOld.dimPos - 0.05F;
                if(fm.EI.engines[0].getStage() == 1 || fm.EI.engines[0].getStage() == 2)
                {
                    if(setNew.Airstartr < 1.0F)
                        setNew.Airstartr = setOld.Airstartr + 0.1F;
                    if(setNew.Airstartr > 1.0F)
                        setNew.Airstartr = 1.0F;
                } else
                {
                    if(setNew.Airstartr > 0.0F)
                        setNew.Airstartr = setOld.Airstartr - 0.1F;
                    if(setNew.Airstartr < 0.0F)
                        setNew.Airstartr = 0.0F;
                }
                IntervalometrNum = 0;
                if(fm.CT.Weapons[3] != null)
                    if(fm.CT.Weapons[3].length == 2)
                    {
                        if(fm.CT.Weapons[3][1].haveBullets())
                            IntervalometrNum = 20;
                    } else
                    if(fm.CT.Weapons[3].length == 4 && fm.CT.Weapons[3][3].haveBullets())
                        IntervalometrNum = 20;
                byte byte0 = 0;
                if(fm.CT.Weapons[3] != null)
                {
                    if(fm.CT.Weapons[3].length > 22)
                    {
                        if(fm.CT.Weapons[3][23].haveBullets())
                            byte0 = 24;
                        if(fm.CT.Weapons[3][22].haveBullets())
                            byte0 = 23;
                    }
                    if(fm.CT.Weapons[3].length > 20)
                    {
                        if(fm.CT.Weapons[3][21].haveBullets())
                            byte0 = 22;
                        if(fm.CT.Weapons[3][20].haveBullets())
                            byte0 = 21;
                    }
                    if(fm.CT.Weapons[3].length > 4)
                    {
                        if(fm.CT.Weapons[3][19].haveBullets())
                            byte0 = 20;
                        if(fm.CT.Weapons[3][18].haveBullets())
                            byte0 = 19;
                        if(fm.CT.Weapons[3][17].haveBullets())
                            byte0 = 18;
                        if(fm.CT.Weapons[3][16].haveBullets())
                            byte0 = 17;
                        if(fm.CT.Weapons[3][15].haveBullets())
                            byte0 = 16;
                        if(fm.CT.Weapons[3][14].haveBullets())
                            byte0 = 15;
                        if(fm.CT.Weapons[3][13].haveBullets())
                            byte0 = 14;
                        if(fm.CT.Weapons[3][12].haveBullets())
                            byte0 = 13;
                        if(fm.CT.Weapons[3][11].haveBullets())
                            byte0 = 12;
                        if(fm.CT.Weapons[3][10].haveBullets())
                            byte0 = 11;
                        if(fm.CT.Weapons[3][9].haveBullets())
                            byte0 = 10;
                        if(fm.CT.Weapons[3][8].haveBullets())
                            byte0 = 9;
                        if(fm.CT.Weapons[3][7].haveBullets())
                            byte0 = 8;
                        if(fm.CT.Weapons[3][6].haveBullets())
                            byte0 = 7;
                        if(fm.CT.Weapons[3][5].haveBullets())
                            byte0 = 6;
                        if(fm.CT.Weapons[3][4].haveBullets())
                            byte0 = 5;
                    }
                    if(fm.CT.Weapons[3].length > 2)
                    {
                        if(fm.CT.Weapons[3][3].haveBullets())
                            byte0 = 4;
                        if(fm.CT.Weapons[3][2].haveBullets())
                            byte0 = 3;
                    }
                    if(fm.CT.Weapons[3].length > 0)
                    {
                        if(fm.CT.Weapons[3][1].haveBullets())
                            byte0 = 2;
                        if(fm.CT.Weapons[3][0].haveBullets())
                            byte0 = 1;
                    }
                }
                mesh.chunkSetAngles("CCC_zESBR01", 0.0F, -floatindex(cvt(byte0 + IntervalometrNum, 0.0F, 26F, 0.0F, 26F), CockpitCCC_TGunner.ESBR_Scale), 0.0F);
                if(aircraft().FM.CT.getBombDropMode() == Controls.defaultFire && fm.CT.saveWeaponControl[3])
                    setNew.Asbr2 = setOld.Asbr2 = 1.0F;
                else
                if(setNew.Asbr2 > 0.0F)
                    setNew.Asbr2 = setOld.Asbr2 - 0.1F;
                else
                    setNew.Asbr2 = setOld.Asbr2 = 0.0F;
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    public void toggleDim()
    {
        cockpitDimControl = !cockpitDimControl;
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            mesh.materialReplace("equip_AN4b", "equip_AN4b_light");
            mesh.materialReplace("equip_AN4c", "equip_AN4c_light");
            mesh.materialReplace("equip_AN4_sh", "equip_AN4_sh_light");
            mesh.chunkSetAngles("zReostat", -70F, 0.0F, 0.0F);
        } else
        {
            mesh.materialReplace("equip_AN4b", "equip_AN4b");
            mesh.materialReplace("equip_AN4c", "equip_AN4c");
            mesh.materialReplace("equip_AN4_sh", "equip_AN4_sh");
            mesh.chunkSetAngles("zReostat", 0.0F, 0.0F, 0.0F);
        }
        setNightMats(false);
    }

//    private void retoggleLight()
//    {
//        if(cockpitLightControl)
//            setNightMats(false);
//        else
//            setNightMats(false);
//    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D1o"));
        mesh.materialReplace("Gloss1D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss1D2o"));
        mesh.materialReplace("Gloss1D2o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D0o"));
        mesh.materialReplace("Gloss2D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D1o"));
        mesh.materialReplace("Gloss2D1o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Gloss2D2o"));
        mesh.materialReplace("Gloss2D2o", mat);
    }

    protected void reflectPlaneToModel()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("CF_D0_00", hiermesh.isChunkVisible("CF_D0"));
        mesh.chunkVisible("CF_D1_00", hiermesh.isChunkVisible("CF_D1"));
        mesh.chunkVisible("CF_D2_00", hiermesh.isChunkVisible("CF_D2"));
        mesh.chunkVisible("Tail1_D0", hiermesh.isChunkVisible("Tail1_D0"));
        mesh.chunkVisible("Tail1_D1", hiermesh.isChunkVisible("Tail1_D1"));
        mesh.chunkVisible("Tail1_D2", hiermesh.isChunkVisible("Tail1_D2"));
        mesh.chunkVisible("WingRMid_D0", hiermesh.isChunkVisible("WingRMid_D0"));
        mesh.chunkVisible("WingRMid_D1", hiermesh.isChunkVisible("WingRMid_D1"));
        mesh.chunkVisible("WingRMid_D2", hiermesh.isChunkVisible("WingRMid_D2"));
        mesh.chunkVisible("WingLMid_D0", hiermesh.isChunkVisible("WingLMid_D0"));
        mesh.chunkVisible("WingLMid_D1", hiermesh.isChunkVisible("WingLMid_D1"));
        mesh.chunkVisible("WingLMid_D2", hiermesh.isChunkVisible("WingLMid_D2"));
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            reflectPlaneToModel();
            bNeedSetUp = false;
        }
        if(R_5xyz.bChangedPit)
        {
            reflectPlaneToModel();
            R_5xyz.bChangedPit = false;
        }
        resetYPRmodifier();
        float f1 = setNew.pictRudd * 20F;
        mesh.chunkSetAngles("Pedals", f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Pedal_tros_L1", -f1, 0.0F, 0.0F);
        mesh.chunkSetAngles("Pedal_tros_R1", -f1, 0.0F, 0.0F);
        float f2 = setNew.pictRudd;
        if(f2 > 0.0F)
        {
            mesh.chunkSetAngles("Pedal_tros_L2", -f2 * 22.2F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Pedal_tros_R2", -f2 * 19.1F, 0.0F, 0.0F);
        } else
        {
            mesh.chunkSetAngles("Pedal_tros_L2", -f2 * 19.1F, 0.0F, 0.0F);
            mesh.chunkSetAngles("Pedal_tros_R2", -f2 * 22.2F, 0.0F, 0.0F);
        }
        mesh.chunkSetAngles("zRollerL", 200F * f2, 0.0F, 0.0F);
        mesh.chunkSetAngles("zRollerR", 200F * f2, 0.0F, 0.0F);
        if(tros2Mat != null)
        {
            tros2 = 0.25F * f2;
            tros2Mat.setLayer(0);
            tros2Mat.set((byte)11, tros2);
        }
        if(Gun5 != null)
        {
            if(bGunFire && Gun5.haveBullets() && PatronsMat != null)
            {
                Patrons -= 0.35F * f;
                PatronsMat.setLayer(0);
                PatronsMat.set((byte)11, Patrons);
            }
            if(World.cur().diffCur.Limited_Ammo && Gun5.countBullets() < 50)
                mesh.chunkVisible("CCC_TUR8Patrons", false);
        } else
        {
            mesh.chunkVisible("CCC_TUR8Patrons", false);
        }
        float f3 = 0.0F;
        if(setNew.pictElev > 0.0F)
            f3 = 21F * setNew.pictElev;
        else
            f3 = 13F * setNew.pictElev;
        mesh.chunkSetAngles("StickElev", 0.0F, 0.0F, f3);
        mesh.chunkSetAngles("Stick", 0.0F, setNew.pictAiler, 0.0F);
        mesh.chunkSetAngles("StickKardan", 0.0F, 0.0F, -f3 * (float)Math.cos(setNew.pictAiler * 0.01745329F));
        mesh.chunkSetAngles("StickConnctr", -f3 * (float)Math.sin(setNew.pictAiler * 0.01745329F), 0.0F, 0.0F);
        mesh.chunkSetAngles("StickEl_trosL1", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("StickEl_trosL2", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("StickEl_trosR1", 0.0F, 0.0F, -f3);
        mesh.chunkSetAngles("StickEl_trosR2", 0.0F, 0.0F, -f3);
        float f4 = -50F * setNew.throttle1;
        mesh.chunkSetAngles("zThrotle", 0.0F, 0.0F, f4);
        mesh.chunkSetAngles("Cable_throtle01", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Cable_throtle02", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("zThrotleNav", 0.0F, 0.0F, -f4);
        mesh.chunkSetAngles("Cable_throtle03", 0.0F, 0.0F, f4);
        float f5 = -37.5F * setNew.mix1;
        mesh.chunkSetAngles("zMixture", 0.0F, 0.0F, f5);
        mesh.chunkSetAngles("Cable_mixture01", 0.0F, 0.0F, -f5);
        mesh.chunkSetAngles("Cable_mixture02", 0.0F, 0.0F, -f5);
        mesh.chunkSetAngles("zMixtureNav", 0.0F, 0.0F, -f5);
        mesh.chunkSetAngles("Cable_mixture03", 0.0F, 0.0F, f5);
        float f6 = cvt(fm.EI.engines[0].getRPM(), 900F, 1200F, 0.0F, -50F);
        mesh.chunkSetAngles("zIgnition", 0.0F, 0.0F, f6);
        mesh.chunkSetAngles("Cable_Ignition", 0.0F, 0.0F, -f6);
        mesh.chunkSetAngles("Z_AN4a", 0.0F, -setNew.compasKren, -setNew.compasTangage);
        mesh.chunkSetAngles("Z_AN4b", interp(setNew.azimuth, setOld.azimuth, f), 0.0F, 0.0F);
        mesh.chunkSetAngles("zArr_Speed2", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 60F, 400F, 0.0F, 17F), IAS_Scale), 0.0F);
        float f7 = 0.0F;
        mesh.chunkSetAngles("zArr_PioneerBall2", 0.0F, cvt(f7, -4F, 4F, -11F, 11F), 0.0F);
        f7 = cvt(setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
        mesh.chunkSetAngles("zArr_Pioneer2", 0.0F, f7, 0.0F);
        mesh.chunkSetAngles("zArr_ClockS2", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockM2", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("zArr_ClockH2", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("zArr_Alt2", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 12000F, 0.0F, 720F), 0.0F);
        float f8 = 0.0F;
        if(fm.AS.bLandingLightOn)
            f8 = -2.5F;
        if(fm.AS.bNavLightsOn)
            f8--;
        mesh.chunkSetAngles("zArr_Volt", 0.0F, f8, 0.0F);
        mesh.chunkSetAngles("zArr_AirPress250", 0.0F, 0.0F, 153F);
        mesh.chunkSetAngles("zAirstartr", 0.0F, 0.0F, -90F * setNew.Airstartr);
        if(fm.EI.engines[0].getStage() == 1 || fm.EI.engines[0].getStage() == 2)
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, -245F * setNew.Airstartr + 5F * World.Rnd().nextFloat());
        else
            mesh.chunkSetAngles("zArr_AirPress25", 0.0F, 0.0F, 0.0F);
        HierMesh hiermesh = aircraft().hierMesh();
        if(!hiermesh.isChunkVisible("Pilot1_D0") && !hiermesh.isChunkVisible("Pilot1_D1"))
        {
            mesh.chunkVisible("GS", true);
            mesh.chunkVisible("GS_Cap", true);
            mesh.chunkVisible("GS_Tinter", true);
            mesh.chunkVisible("GS_Spring", true);
            mesh.chunkSetAngles("GS_Cap", 0.0F, 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F));
            mesh.chunkSetAngles("GS_Tinter", 0.0F, 0.0F, cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 0.69F, 0.0F, -90F));
            mesh.chunkSetAngles("GS_Spring", cvt(interp(setNew.dimPos, setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        } else
        {
            mesh.chunkVisible("GS", false);
            mesh.chunkVisible("GS_Cap", false);
            mesh.chunkVisible("GS_Tinter", false);
            mesh.chunkVisible("GS_Spring", false);
        }
        mesh.chunkVisible("Pilot1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("Head1_D0", hiermesh.isChunkVisible("Pilot1_D0"));
        mesh.chunkVisible("HMask1_D0", hiermesh.isChunkVisible("HMask1_D0"));
        mesh.chunkVisible("Pilot1_D1", hiermesh.isChunkVisible("Pilot1_D1"));
        mesh.chunkSetAngles("CCC_zESBR02", 0.0F, cvt(aircraft().FM.CT.getBombDropMode(), 1.0F, 4F, 0.0F, -180F), 0.0F);
        mesh.chunkSetAngles("CCC_zESBR03", 0.0F, Atmosphere.temperature((float)fm.Loc.z) >= 258F ? 0.0F : 150F, 0.0F);
        mesh.chunkVisible("ZFlareRed", fm.CT.saveWeaponControl[3] && fm.CT.Weapons[3] != null);
        mesh.chunkSetAngles("CCC_zASBR2", -30F * setNew.Asbr2, 0.0F, 0.0F);
        if(Gun1 != null && Gun1.haveBullets())
        {
            if(fm.CT.saveWeaponControl[0])
                mesh.chunkVisible("ZFlareYellow01", World.Rnd().nextFloat() > 0.5F);
            else
                mesh.chunkVisible("ZFlareYellow01", false);
        } else
        {
            mesh.chunkVisible("ZFlareYellow01", true);
        }
        if(Gun2 != null && Gun2.haveBullets())
        {
            if(fm.CT.saveWeaponControl[1])
                mesh.chunkVisible("ZFlareYellow02", World.Rnd().nextFloat() > 0.5F);
            else
                mesh.chunkVisible("ZFlareYellow02", false);
        } else
        {
            mesh.chunkVisible("ZFlareYellow02", true);
        }
        if(Gun3 != null && Gun3.haveBullets())
        {
            if(fm.CT.saveWeaponControl[1])
                mesh.chunkVisible("ZFlareYellow03", World.Rnd().nextFloat() > 0.5F);
            else
                mesh.chunkVisible("ZFlareYellow03", false);
        } else
        {
            mesh.chunkVisible("ZFlareYellow03", true);
        }
        if(Gun4 != null && Gun4.haveBullets())
        {
            if(fm.CT.saveWeaponControl[0])
                mesh.chunkVisible("ZFlareYellow04", World.Rnd().nextFloat() > 0.5F);
            else
                mesh.chunkVisible("ZFlareYellow04", false);
        } else
        {
            mesh.chunkVisible("ZFlareYellow04", true);
        }
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("Interior_D0", false);
//            aircraft().FM.turret[0].bMultiFunction = true;
            ((R_5xyz)aircraft()).bMultiFunction = true;
            ((R_5xyz)aircraft()).bPlayerTurret = true;
            doHidePilot();
            bNeedSetUp = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretUp");
            resetYPRmodifier();
            mesh.chunkSetLocate("CameraRodA", Cockpit.xyz, Cockpit.ypr);
            resetYPRmodifier();
            mesh.chunkSetLocate("CameraRodB", Cockpit.xyz, Cockpit.ypr);
            
            aircraft().hierMesh().chunkVisible("Turret1A_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            aircraft().hierMesh().chunkVisible("Turret1D_D0", false);

            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        aircraft().hierMesh().chunkVisible("Interior_D0", true);
//        aircraft().FM.turret[0].bMultiFunction = false;
        ((R_5xyz)aircraft()).bPlayerTurret = false;
        
        HierMesh hiermesh = aircraft().hierMesh();
        R_5xyz r_5xyz = (R_5xyz)aircraft();
        hiermesh.chunkVisible("Turret1A_D0", true);
        if(r_5xyz.getGunnerAnimation() < 1.0F)
        {
            hiermesh.chunkVisible("Turret1B_D0", false);
            hiermesh.chunkVisible("Turret1C_D0", false);
            hiermesh.chunkVisible("Turret1D_D0", true);
        } else
        {
            hiermesh.chunkVisible("Turret1B_D0", true);
            hiermesh.chunkVisible("Turret1C_D0", true);
            hiermesh.chunkVisible("Turret1D_D0", false);
        }
        super.doFocusLeave();
        doShowPilot();
    }

    public void moveGun(Orient orient)
    {
        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if((double)r_5xyz.getGunnerAnimation() < 1.0D)
        {
            mesh.chunkSetAngles("zTurret1A", 0.0F, 0.0F, 0.0F);
            float f = cvt(r_5xyz.getGunnerAnimation(), 0.75F, 1.0F, -70F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, f);
        } else
        {
            super.moveGun(orient);
            mesh.chunkSetAngles("zTurret1A", -orient.getYaw(), 0.0F, 0.0F);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -orient.getTangage());
            float f1 = orient.getTangage();
            if(f1 > 35F)
                mesh.chunkSetAngles("CameraRodA", 0.0F, 0.0F, -35F);
            else
            if(f1 < -10F)
                mesh.chunkSetAngles("CameraRodA", 0.0F, 0.0F, 10F);
            else
                mesh.chunkSetAngles("CameraRodA", 0.0F, 0.0F, -f1);
        }
    }

    public void clipAnglesGun(Orient orient)
    {
        if(!isRealMode())
            return;
        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if(!aiTurret().bIsOperable || (double)r_5xyz.getGunnerAnimation() < 1.0D)
        {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if(f < -95F)
            f = -95F;
        if(f > 95F)
            f = 95F;
        if(f1 > 70F)
            f1 = 70F;
        if(f1 < -3F && f > -12F && f < 12F)
            f1 = -3F;
        else
        if(f > -37F && f <= -12F && f1 < cvt(f, -37F, -12F, -25F, -3F))
            f1 = cvt(f, -37F, -12F, -25F, -3F);
        else
        if(f >= 12F && f < 37F && f1 < cvt(f, 12F, 37F, -3.5F, -25F))
            f1 = cvt(f, 12F, 37F, -3.5F, -25F);
        else
        if(f1 < -25F)
            f1 = -25F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick()
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if(bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUNCCC05");
            doHitMasterAircraft(aircraft(), hook1, "_MGUNCCC05");
        }
        R_5xyz r_5xyz = (R_5xyz)aircraft();
        if((double)r_5xyz.getGunnerAnimation() > 0.75D)
        {
            if((double)r_5xyz.getGunnerAnimation() < 1.0D)
            {
                resetYPRmodifier();
                mesh.chunkSetLocate("CameraRodA", Cockpit.xyz, Cockpit.ypr);
                resetYPRmodifier();
                mesh.chunkSetLocate("CameraRodB", Cockpit.xyz, Cockpit.ypr);
                float f = cvt(r_5xyz.getGunnerAnimation(), 0.75F, 1.0F, -70F, 0.0F);
                mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, f);
            }
        } else
        if((double)r_5xyz.getGunnerAnimation() > 0.0D)
        {
            resetYPRmodifier();
            mesh.chunkSetLocate("CameraRodA", Cockpit.xyz, Cockpit.ypr);
            Cockpit.xyz[0] = 0.0F;
            Cockpit.xyz[1] = 0.0F;
            Cockpit.xyz[2] = cvt(r_5xyz.getGunnerAnimation(), 0.0F, 0.75F, -0.06421845F, 0.0F);
            Cockpit.ypr[0] = cvt(r_5xyz.getGunnerAnimation(), 0.0F, 0.75F, -180F, 0.0F);
            Cockpit.ypr[1] = 0.0F;
            Cockpit.ypr[2] = 0.0F;
            mesh.chunkSetLocate("CameraRodB", Cockpit.xyz, Cockpit.ypr);
            mesh.chunkSetAngles("zTurret1B", 0.0F, 0.0F, -70F);
        }
    }

    public void doGunFire(boolean flag)
    {
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        else
            bGunFire = flag;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 2) != 0)
            mesh.chunkVisible("xHullDm1", true);
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("xHullDm4", true);
            mesh.chunkVisible("zArr_ClockH2", false);
            mesh.chunkVisible("zArr_ClockM2", false);
            mesh.chunkVisible("zArr_ClockS2", false);
            mesh.chunkVisible("zArr_ClockDop2", false);
            mesh.materialReplace("Prib_ClockACHO", "Prib_ClockACHO_dmg");
        }
        if((fm.AS.astateCockpitState & 8) != 0)
        {
            mesh.chunkVisible("xHullDm5", true);
            mesh.chunkVisible("zArr_Alt2", false);
            mesh.materialReplace("Prib_Alt6km", "Prib_Alt6km_dmg");
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("xGlassDm3", true);
            mesh.chunkVisible("xHullDm6", true);
            mesh.chunkVisible("zArr_Pioneer2", false);
            mesh.chunkVisible("zArr_PioneerBall2", false);
            mesh.materialReplace("Prib_Peoneer2", "Prib_Peoneer2_dmg");
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("xGlassDm2", true);
            mesh.chunkVisible("xHullDm7", true);
            mesh.materialReplace("Prib_Kpa3Alt", "Prib_Kpa3Alt_dmg");
            mesh.chunkVisible("zArr_Speed2", false);
            mesh.materialReplace("Prib_Prib40", "Prib_Prib40_dmg");
        }
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("xGlassDm1", true);
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm2", true);
            mesh.chunkVisible("zArr_Volt", false);
            mesh.materialReplace("Prib_Volt", "Prib_Volt_dmg");
        }
        if((fm.AS.astateCockpitState & 2) != 0 && (fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("xHullDm3", true);
            mesh.chunkVisible("zArr_AirPress25", false);
            mesh.materialReplace("Prib_Oxy25", "Prib_Oxy25_dmg");
            mesh.chunkVisible("zArr_AirPress250", false);
            mesh.materialReplace("Prib_Oxy250", "Prib_Oxy250_dmg");
        }
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("xOilSplats_D1", true);
    }

    public boolean useMultiFunction()
    {
        return true;
    }

    public void doMultiFunction(boolean flag)
    {
    }

    public CockpitCCC_TGunner()
    {
        super("3DO/Cockpit/R-5-TGun/hier-CCC.him", "u2");
        bNeedSetUp = true;
        setOld = new Variables();
        setNew = new Variables();
        Gun1 = null;
        Gun2 = null;
        Gun3 = null;
        Gun4 = null;
        Gun5 = null;
        Aircraft aircraft = aircraft();
        try
        {
            if(!(aircraft.getGunByHookName("_MGUNCCC01") instanceof GunEmpty))
                Gun1 = aircraft.getGunByHookName("_MGUNCCC01");
        }
        catch(Exception exception) { }
        try
        {
            if(!(aircraft.getGunByHookName("_MGUNCCC02") instanceof GunEmpty))
                Gun2 = aircraft.getGunByHookName("_MGUNCCC02");
        }
        catch(Exception exception1) { }
        try
        {
            if(!(aircraft.getGunByHookName("_MGUNCCC03") instanceof GunEmpty))
                Gun3 = aircraft.getGunByHookName("_MGUNCCC03");
        }
        catch(Exception exception2) { }
        try
        {
            if(!(aircraft.getGunByHookName("_MGUNCCC04") instanceof GunEmpty))
                Gun4 = aircraft.getGunByHookName("_MGUNCCC04");
        }
        catch(Exception exception3) { }
        try
        {
            if(!(aircraft.getGunByHookName("_MGUNCCC05") instanceof GunEmpty))
                Gun5 = aircraft.getGunByHookName("_MGUNCCC05");
        }
        catch(Exception exception4) { }
        w = new Vector3f();
        tros2 = 0.5F;
        tros2Mat = null;
        int i = -1;
        i = mesh.materialFind("tros2");
        if(i != -1)
        {
            tros2Mat = mesh.material(i);
            tros2Mat.setLayer(0);
        }
        Patrons = 0.5F;
        PatronsMat = null;
        i = -1;
        i = mesh.materialFind("patron");
        if(i != -1)
        {
            PatronsMat = mesh.material(i);
            PatronsMat.setLayer(0);
        }
        hook1 = null;
        cockpitNightMats = new String[0];
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
        hidePilot = true;
    }

    protected float waypointAzimuth()
    {
        return this.waypointAzimuthInvertMinus(10F);
    }

    private boolean bNeedSetUp;
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;
    public Vector3f w;
    private float Patrons;
    private Mat PatronsMat;
    private float tros2;
    private Mat tros2Mat;
    private Gun Gun1;
    private Gun Gun2;
    private Gun Gun3;
    private Gun Gun4;
    private Gun Gun5;
    private int IntervalometrNum;
    private Hook hook1;
    private static final float IAS_Scale[] = {
        0.0F, 13.7F, 29.5F, 49.5F, 72.2F, 98.8F, 127.09F, 161.5F, 192.5F, 229F, 
        262.6F, 299F, 335.29F, 370.29F, 405.29F, 439.59F, 471.29F, 504.69F
    };
    private static final float ESBR_Scale[] = {
        0.0F, 17.9F, 37.9F, 52.9F, 66.5F, 79.2F, 91.9F, 105.8F, 117.9F, 127.9F, 
        139.2F, 151.7F, 162.8F, 174F, 186.1F, 197.4F, 209.6F, 223.3F, 234.5F, 245.4F, 
        255.8F, 266.3F, 276.3F, 286.7F, 298F, 309.4F, 322.3F
    };

    static 
    {
        Property.set(CockpitCCC_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitCCC_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitCCC_TGunner.class, "astatePilotIndx", 1);
        Property.set(CockpitCCC_TGunner.class, "normZNs", new float[] {
            0.3F, 0.31F, 0.3F, 0.31F
        });
    }
}
