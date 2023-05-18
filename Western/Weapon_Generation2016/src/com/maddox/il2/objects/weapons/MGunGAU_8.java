// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 15/05/2017 22:26:05
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunGAU_8.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.ai.World;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.fm.RealFlightModel;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunAircraftGeneric

public class MGunGAU_8 extends MGunAircraftGeneric
{

    public MGunGAU_8()
    {
        this.tLastShotFreqChange = 0L;
        this.dAccel = 0D;
        this.fShotFreqFactor = 0.2F; 
    }

    public void shots(int i, float f)
    {
        if (i == 0 && this.fShotFreqFactor > 0.2F) {
            this.fShotFreqFactor -= (float)(Time.current() - this.tLastShotFreqChange) / 1250F;
            if (this.fShotFreqFactor < 0.2F) fShotFreqFactor = 0.2F;
        } else {
            if (this.fShotFreqFactor < 1.0F) {
                this.fShotFreqFactor += (float)(Time.current() - this.tLastShotFreqChange) / 375F;
                if (this.fShotFreqFactor > 1.0F) fShotFreqFactor = 1.0F;
            }
            this._shotStep = (float)prop.bulletsCluster / (prop.shotFreq * this.fShotFreqFactor);
//            HUD.training("SF: " + this.fShotFreqFactor);
            this.sound.setPitch(this.fShotFreqFactor);
        }
        this.tLastShotFreqChange = Time.current();
        super.shots(i, f);
    }
    
    // No recoil code 
    public void doStartBullet(double d) {
        Actor actor = getOwner();
        boolean modifyAM = false;
        if(actor == World.getPlayerAircraft())
        {
            if(World.cur().diffCur.Realistic_Gunnery && (((Aircraft)actor).FM instanceof RealFlightModel))
            {
                modifyAM = true;
            }
        }
        if (modifyAM) {
            this.vtemp.set(((RealFlightModel)((Aircraft)actor).FM).producedAM);
            this.vwldtemp.set(((Aircraft)actor).FM.Vwld);
            this.ftemp = ((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel;
        }
        super.doStartBullet(d);
        if (modifyAM) {
            ((RealFlightModel)((Aircraft)actor).FM).producedAM.set(this.vtemp);
            ((Aircraft)actor).FM.Vwld.set(this.vwldtemp);
            ((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel = this.ftemp;
        }
    }
    
    
    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
        gunproperties.fire = "3DO/Effects/GunFire/37mm/GunFire.eff";
        gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/CannonTank.eff";
        gunproperties.shells = null;
        gunproperties.sound = "weapon.Gau4";
        gunproperties.emitColor = new Color3f(0.6F, 0.4F, 0.2F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 3660F;
        gunproperties.weaponType = -1;
        gunproperties.maxDeltaAngle = 0.01F;
        gunproperties.shotFreq = 65F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 250;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties()
        });
        gunproperties.bullet[0].massa = 1.1F;
        gunproperties.bullet[0].kalibr = 0.00075F;
        gunproperties.bullet[0].speed = 1100F;
        gunproperties.bullet[0].power = 6F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.1F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 20F;
        return gunproperties;
    }

    private long tLastShotFreqChange;
    private double dAccel;
    private float fShotFreqFactor;
    private Vector3d vtemp = new Vector3d();
    private Vector3d vwldtemp = new Vector3d();
    private float ftemp;

}