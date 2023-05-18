package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitSB103_TGunner extends CockpitGunner
{

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Z_speed01", 0.0F, -floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeed2KMH()), 0.0F, 600F, 0.0F, 15F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Z_ND_HOUR02", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Z_ND_MINUTE02", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_ND_SECOND02", 0.0F, cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Z_ND_ALT_METRS02", 0.0F, cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        mesh.chunkSetAngles("Z_ND_ALT_KM02", 0.0F, cvt(fm.getAltitude(), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        if(bGunFire)
        {
            if(patronMat != null)
            {
                patron += 0.5F * f;
                patronMat.setLayer(0);
                patronMat.set((byte)11, patron);
            }
        } else
        if(patronMat != null)
        {
            patron = 1.2F;
            patronMat.setLayer(0);
            patronMat.set((byte)11, patron);
        }
        if(emitter != null && World.cur().diffCur.Limited_Ammo)
        {
            if(emitter.countBullets() < 22)
                mesh.chunkVisible("belt1p", false);
            if(emitter.countBullets() < 20)
                mesh.chunkVisible("belt2p", false);
            if(emitter.countBullets() < 18)
                mesh.chunkVisible("belt3p", false);
            if(emitter.countBullets() < 16)
                mesh.chunkVisible("belt4p", false);
            if(emitter.countBullets() < 14)
                mesh.chunkVisible("belt5p", false);
            if(emitter.countBullets() < 12)
                mesh.chunkVisible("belt6p", false);
            if(emitter.countBullets() < 10)
                mesh.chunkVisible("belt7p", false);
            if(emitter.countBullets() < 8)
                mesh.chunkVisible("belt8p", false);
            if(emitter.countBullets() < 6)
                mesh.chunkVisible("belt9p", false);
            if(emitter.countBullets() < 4)
                mesh.chunkVisible("belt10p", false);
            if(emitter.countBullets() < 2)
                mesh.chunkVisible("belt11p", false);
        }
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = -orient.getTangage();
        mesh.chunkSetAngles("Z_Tur_part_Main", f, 0.0F, 0.0F);
        mesh.chunkSetAngles("ZGun", f1, 0.0F, 0.0F);
        float f2 = ((SB)aircraft()).turretUp * 2.25F;
        if(f2 < 89F && Math.abs(f) < 1.0F)
            f1 = cvt(f2, 0.0F, 90F, 0.0F, -17F);
        if(f1 < -60F)
            f1 = -60F;
        else
        if(f1 > 30F)
            f1 = 30F;
        mesh.chunkSetAngles("CameraB", f1, 0.0F, 0.0F);
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
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
            return;
        }
        float f2 = ((SB)aircraft()).turretUp * 2.25F;
        if(f2 < 89F)
        {
            float f3 = cvt(f2, 67F, 90F, 0.0F, 180F);
            if(f > f3)
                f = f3;
            if(f < -f3)
                f = -f3;
        }
        if(f < -120F && prevA0 > 120F)
            f += 360F;
        else
        if(f > 120F && prevA0 < -120F)
            prevA0 += 360F;
        float f4 = f - prevA0;
        float f5 = 0.001F * (float)(Time.current() - prevTime);
        float f6 = Math.abs(f4 / f5);
        if(f6 > 120F)
            if(f > prevA0)
                f = prevA0 + 120F * f5;
            else
            if(f < prevA0)
                f = prevA0 - 120F * f5;
        prevTime = Time.current();
        float f7 = Math.abs(f);
        float f8 = 0.0F;
        float f9 = 0.0F;
        float f10 = 0.0F;
        float f11 = 0.0F;
        if(f7 < 50F)
        {
            if(f7 < 1.0F)
            {
                if(f2 > 89F)
                {
                    f9 = 1.0F;
                    f10 = 17F;
                    f11 = 17F;
                } else
                {
                    f = 0.0F;
                    f1 = cvt(f2, 0.0F, 90F, -10F, 17F);
                    f9 = 1.0F;
                    f10 = -10F;
                    f11 = -10F;
                }
            } else
            if(f7 < 5F)
            {
                f8 = 0.0F;
                f9 = 5F;
                f10 = 17F;
                f11 = 16.3F;
            } else
            if(f7 < 10F)
            {
                f8 = 5F;
                f9 = 10F;
                f10 = 16.3F;
                f11 = 14.5F;
            } else
            if(f7 < 15F)
            {
                f8 = 10F;
                f9 = 15F;
                f10 = 14.5F;
                f11 = 11.5F;
            } else
            if(f7 < 20F)
            {
                f8 = 15F;
                f9 = 20F;
                f10 = 11.5F;
                f11 = 5.8F;
            } else
            if(f7 < 25F)
            {
                f8 = 20F;
                f9 = 25F;
                f10 = 5.8F;
                f11 = -2.2F;
            } else
            if(f7 < 30F)
            {
                f8 = 25F;
                f9 = 30F;
                f10 = -2.2F;
                f11 = -10F;
            } else
            if(f7 < 50F)
            {
                f8 = 30F;
                f9 = 50F;
                f10 = -10F;
                f11 = -45F;
            }
            float f12 = cvt(f7, f8, f9, f10, f11);
            if(f1 < f12)
                f1 = f12;
        } else
        if(f7 > 105F)
        {
            if(f7 > 165F)
            {
                f8 = 165F;
                f9 = 180F;
                f10 = 25F;
                f11 = 30F;
            } else
            if(f7 > 140F)
            {
                f8 = 140F;
                f9 = 165F;
                f10 = -20F;
                f11 = 25F;
            } else
            if(f7 > 105F)
            {
                f8 = 105F;
                f9 = 140F;
                f10 = -45F;
                f11 = -20F;
            }
            float f13 = cvt(f7, f8, f9, f10, f11);
            if(f1 < f13)
                f1 = f13;
        }
        if(f1 < -45F)
            f1 = -45F;
        if(f1 > 85F)
            f1 = 85F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        prevA0 = f;
    }

    protected void interpTick()
    {
        float f = ((SB)aircraft()).turretUp * 2.25F;
        if(!aiTurret().bIsNetMirror && !aiTurret().bIsAIControlled && f < 89F && f > 0.0F)
            hookGunner().mouseMove(0, 0, 0);
        resetYPRmodifier();
        mesh.chunkSetAngles("Z_Tur_part_2", -f, 0.0F, 0.0F);
        mesh.chunkSetAngles("ZGunUpPivot", f, 0.0F, 0.0F);
        float f1 = f * 0.2F;
        float f2 = f * 0.05F;
        mesh.chunkSetAngles("Belt5", -f1, -f2, 0.0F);
        mesh.chunkSetAngles("Belt6", -f1, -f2, 0.0F);
        mesh.chunkSetAngles("Belt7", -f1, -f2, 0.0F);
        mesh.chunkSetAngles("Belt8", f1, -f2, 0.0F);
        mesh.chunkSetAngles("Belt9", f1, -f2, 0.0F);
        mesh.chunkSetAngles("Belt10", f1, -f2, 0.0F);
        mesh.chunkSetAngles("Z_shell_tube", 0.0F, 0.0F, f * 0.76F);
        if(!isRealMode())
            return;
        if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
            bGunFire = false;
        fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        if(bGunFire)
        {
            if(hook1 == null)
                hook1 = new HookNamed(aircraft(), "_MGUN03");
            doHitMasterAircraft(aircraft(), hook1, "_MGUN03");
        }
        if (aircraft().FM.turret[1].bIsAIControlled != onAuto) {
            onAuto = aircraft().FM.turret[1].bIsAIControlled;
            ((SB)aircraft()).bMultiFunction = !onAuto;
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

    public CockpitSB103_TGunner()
    {
        super("3DO/Cockpit/SB_103-TGunner/hier.him", "he111_gunner");
        bNeedSetUp = true;
        patron = 1.2F;
        patronMat = null;
        onAuto = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        cockpitNightMats = (new String[] {
            "arrow", "Dprib_six", "Prib_six"
        });
        setNightMats(false);
        int i = -1;
        i = mesh.materialFind("patron");
        if(i != -1)
        {
            patronMat = mesh.material(i);
            patronMat.setLayer(0);
        }
        if(emitter == null)
        {
            for(int j = 1; j <= 11; j++)
                mesh.chunkVisible("belt" + j + "p", false);

        }
        HookNamed hooknamed = new HookNamed(mesh, "LAMPHOOK06");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        light6 = new LightPointActor(new LightPoint(), loc.getPoint());
        light6.light.setColor(0.9607843F, 0.8666667F, 0.7411765F);
        light6.light.setEmit(0.0F, 0.0F);
        pos.base().draw.lightMap().put("LAMPHOOK06", light6);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
        {
            light6.light.setEmit(0.2F, 0.35F);
            mesh.materialReplace("Prib_six", "Prib_six_light");
            mesh.materialReplace("DPrib_six", "DPrib_six_light");
            mesh.materialReplace("arrow", "arrow_light");
            mesh.materialReplace("supp2", "supp2_light");
            mesh.materialReplace("equip_radio_panel", "equip_radio_panel_light");
        } else
        {
            light6.light.setEmit(0.0F, 0.0F);
            mesh.materialReplace("Prib_six", "Prib_six");
            mesh.materialReplace("DPrib_six", "DPrib_six");
            mesh.materialReplace("arrow", "arrow");
            mesh.materialReplace("supp2", "supp2");
            mesh.materialReplace("equip_radio_panel", "equip_radio_panel");
        }
        setNightMats(false);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            aircraft().hierMesh().chunkVisible("turret2b_D0", false);
            aircraft().FM.turret[2].bIsAIControlled = true;
            aircraft().FM.turret[1].bIsAIControlled = onAuto;
            ((SB)aircraft()).bMultiFunction = !onAuto;
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
            aircraft().hierMesh().chunkVisible("turret2b_D0", true);
            onAuto = aircraft().FM.turret[1].bIsAIControlled;
            ((SB)aircraft()).bMultiFunction = !onAuto;
            super.doFocusLeave();
            return;
        }
    }

    public boolean useMultiFunction()
    {
        return true;
    }

    public void doMultiFunction(boolean flag)
    {
        if(flag)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretUp");
        else
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TurretDown");
    }

    private LightPointActor light6;
    private boolean bNeedSetUp;
    private float patron;
    private Mat patronMat;
    private boolean onAuto;
    private static final float speedometerScale[] = {
        0.0F, -10F, -19.5F, -32F, -46F, -66.5F, -89F, -114F, -141F, -170.5F, 
        -200.5F, -232.5F, -264F, -295.5F, -328F, -360F
    };
    private long prevTime;
    private float prevA0;
    private Hook hook1;

    static 
    {
        Property.set(CockpitSB103_TGunner.class, "aiTuretNum", 1);
        Property.set(CockpitSB103_TGunner.class, "weaponControlNum", 11);
        Property.set(CockpitSB103_TGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitSB103_TGunner.class, "normZN", 0.8F);
        Property.set(CockpitSB103_TGunner.class, "gsZN", 0.8F);
    }
}
