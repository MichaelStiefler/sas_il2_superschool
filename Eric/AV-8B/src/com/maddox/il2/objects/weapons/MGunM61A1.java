package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.rts.Time;

public class MGunM61A1 extends MGunAircraftGeneric {
	
	public MGunM61A1() {
	    this.tLastShotFreqChange = 0L;
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
            this.sound.setPitch(this.fShotFreqFactor);
    	}
        this.tLastShotFreqChange = Time.current();
    	super.shots(i, f);
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
//        gunproperties.customSound = "weapon.Gau4";
        gunproperties.emitColor = new Color3f(0.6F, 0.4F, 0.2F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 2000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.7F;
        gunproperties.shotFreq = 100F;
        gunproperties.traceFreq = 4;
        gunproperties.bullets = 250;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.102F;
        gunproperties.bullet[0].kalibr = 0.00032F;
        gunproperties.bullet[0].speed = 1030F;
        gunproperties.bullet[0].power = 0.0072F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.2F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 3F;
        return gunproperties;
    }
    
    private long tLastShotFreqChange;
    private float fShotFreqFactor;
}
