package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.HUD;
import com.maddox.rts.*;

public class CockpitB29_NoseGunner extends CockpitGunner
{
    private class Variables
    {

        float altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables()
        {
            azimuth = new AnglesFork();
            waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef
    {

        public boolean tick()
        {
            if(fm != null)
            {
                if(bNeedSetUp)
                {
                    reflectPlaneMats();
                    bNeedSetUp = false;
                }
                setTmp = setOld;
                setOld = setNew;
                setNew = setTmp;
                w.set(fm.getW());
                fm.Or.transform(w);
                setNew.altimeter = fm.getAltitude();
                float f = waypointAzimuth();
                setNew.azimuth.setDeg(setOld.azimuth.getDeg(1.0F), fm.Or.azimut());
                if(useRealisticNavigationInstruments())
                {
                    setNew.waypointAzimuth.setDeg(f - 90F);
                    setOld.waypointAzimuth.setDeg(f - 90F);
                } else
                {
                    setNew.waypointAzimuth.setDeg(setOld.waypointAzimuth.getDeg(0.1F), f);
                }
            }
            return true;
        }

        Interpolater()
        {
        }
    }


    protected float waypointAzimuth()
    {
        return super.waypointAzimuthInvertMinus(10F);
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        mesh.chunkSetAngles("Body", 180F, 0.0F, 180F);
        if(bUpper)
            mesh.chunkSetAngles("Turret1A", 180F + orient.getYaw(), 180F, 180F);
        if(bLower)
            mesh.chunkSetAngles("Turret1A", orient.getYaw(), 0.0F, 0.0F);
        mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage() - 2.0F, 180F);
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
        } else
        {
            if(!aiTurret().bIsOperable)
                orient.setYPR(180F, 0.0F, 0.0F);
            if(bNeedSetUp)
            {
                prevTime = Time.current() - 1L;
                bNeedSetUp = false;
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f3 = f - prevA0;
            float f4 = 0.001F * (float)(Time.current() - prevTime);
            float f5 = Math.abs(f3 / f4);
            if(f5 > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f4;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f4;
            prevTime = Time.current();
            if(bUpper)
            {
                if(f1 < -4F)
                {
                    if(prevElev - f1 >= 4F)
                    {
                        selectLowerT();
                        f1 = 0.0F;
                    } else
                    {
                        f1 = -4F;
                    }
                } else
                if(f1 > 85F)
                    f1 = 85F;
                if(f > 110F)
                    f = 110F;
                else
                if(f < -110F)
                    f = -110F;
            } else
            if(bLower)
            {
                if(f1 > 0.0F)
                {
                    if(f1 - prevElev >= 4F)
                    {
                        selectUpperT();
                        f1 = -4F;
                    } else
                    {
                        f1 = 0.0F;
                    }
                } else
                if(f1 < -55F)
                    f1 = -55F;
                if(f > 70F)
                    f = 70F;
                else
                if(f < -70F)
                    f = -70F;
            }
            prevElev = f1;
            prevA0 = f;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
        }
    }

    protected void selectUpperT()
    {
        HUD.log("Upper turret Selected!");
        bUpper = true;
        bLower = false;
        fm.turret[0].bIsAIControlled = fm.turret[1].bIsAIControlled;
    }

    protected void selectLowerT()
    {
        HUD.log("Lower turret Selected!");
        bUpper = false;
        bLower = true;
        fm.turret[1].bIsAIControlled = fm.turret[0].bIsAIControlled;
    }

    public Turret aiTurret()
    {
        return fm.turret[bUpper ? 0 : 1];
    }

    public int weaponControlNum()
    {
        return bUpper ? 10 : 11;
    }

    protected void interpTick()
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
            if(bGunFire)
            {
                if(hook1 == null)
                    hook1 = new HookNamed(aircraft(), "_MGUN01");
                doHitMasterAircraft(aircraft(), hook1, "_MGUN01");
                if(hook2 == null)
                    hook2 = new HookNamed(aircraft(), "_MGUN02");
                doHitMasterAircraft(aircraft(), hook2, "_MGUN02");
                if(hook3 == null)
                    hook3 = new HookNamed(aircraft(), "_MGUN03");
                doHitMasterAircraft(aircraft(), hook3, "_MGUN03");
                if(hook4 == null)
                    hook4 = new HookNamed(aircraft(), "_MGUN04");
                doHitMasterAircraft(aircraft(), hook4, "_MGUN04");
            }
        }
    }

    public void doGunFire(boolean flag)
    {
        if(isRealMode())
        {
            if(emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
                bGunFire = false;
            else
                bGunFire = flag;
            fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
        }
    }

    public CockpitB29_NoseGunner()
    {
        super("3DO/Cockpit/B-29/NoseGunnerB29.him", "he111");
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
        hook1 = null;
        hook2 = null;
        hook3 = null;
        hook4 = null;
        setOld = new Variables();
        setNew = new Variables();
        w = new Vector3f();
        cockpitNightMats = (new String[] {
            "Gauges1", "textrbm4"
        });
        setNightMats(false);
        interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void toggleLight()
    {
        cockpitLightControl = !cockpitLightControl;
        if(cockpitLightControl)
            setNightMats(true);
        else
            setNightMats(false);
    }

    public void reflectWorldToInstruments(float f)
    {
        if(bNeedSetUp)
        {
            reflectPlaneMats();
            bNeedSetUp = false;
        }
        mesh.chunkSetAngles("Zspeedbomb", 0.0F, floatindex(cvt(Pitot.Indicator((float)fm.Loc.z, fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        mesh.chunkSetAngles("Zalt2bomb", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        mesh.chunkSetAngles("Zalt01bomb", 0.0F, cvt(interp(setNew.altimeter, setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        mesh.chunkSetAngles("Zclock1bomb", 0.0F, cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        mesh.chunkSetAngles("Zclock2bomb", 0.0F, cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        mesh.chunkSetAngles("Zcompassbomb", 0.0F, setNew.azimuth.getDeg(f), 0.0F);
        mesh.chunkSetAngles("Zcompass02bomb", 0.0F, setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            mesh.chunkVisible("XGlassDamage8", true);
        if((fm.AS.astateCockpitState & 8) != 0)
            mesh.chunkVisible("XGlassDamage7", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("XGlassDamage2", true);
            mesh.chunkVisible("XGlassDamage3", true);
            mesh.chunkVisible("XGlassDamage4", true);
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
            mesh.chunkVisible("XGlassDamage5", true);
        if((fm.AS.astateCockpitState & 1) != 0)
            mesh.chunkVisible("XGlassDamage6", true);
        if((fm.AS.astateCockpitState & 0x40) != 0);
        if((fm.AS.astateCockpitState & 0x80) != 0)
            mesh.chunkVisible("XGlassDamage1", true);
    }

    protected void reflectPlaneMats()
    {
        HierMesh hiermesh = aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        mesh.materialReplace("Gloss1D0o", mat);
    }

    protected boolean doFocusEnter()
    {
        return super.doFocusEnter();
    }

    protected void doFocusLeave()
    {
        if(!isFocused())
            return;
        else
            return;
    }

    private float prevElev;
    private static boolean bUpper = true;
    private static boolean bLower = false;
    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    private Hook hook1;
    private Hook hook2;
    private Hook hook3;
    private Hook hook4;
    public Vector3f w;
    private static final float speedometerScale[] = {
        0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 
        296.5F, 308.5F, 324F, 338.5F
    };
    private Variables setOld;
    private Variables setNew;
    private Variables setTmp;

    static 
    {
        Property.set(CLASS.THIS(), "astatePilotIndx", 2);
    }

}