// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/19/2013 4:37:14 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunGAU12U.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.rts.Time;

import java.security.SecureRandom;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunVYas

public class MGunGAU12U extends MGunVYas
{

    public MGunGAU12U()
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
//    		HUD.training("SF: " + this.fShotFreqFactor);
            this.sound.setPitch(this.fShotFreqFactor);
    	}
        this.tLastShotFreqChange = Time.current();
    	super.shots(i, f);
    }
    
    private long tLastShotFreqChange;
    private double dAccel;
    private float fShotFreqFactor;

    private void initRandom()
    {
        if(theRangeRandom != null)
        {
            return;
        } else
        {
            long l = System.currentTimeMillis();
            SecureRandom securerandom = new SecureRandom();
            securerandom.setSeed(l);
            long l1 = securerandom.nextInt();
            long l2 = securerandom.nextInt();
            long l3 = (l1 << 32) + l2;
            theRangeRandom = new RangeRandom(l3);
            return;
        }
    }

    private int nextRandomInt(int i, int j)
    {
        initRandom();
        return theRangeRandom.nextInt(i, j);
    }

    public void init()
    {
        super.init();
        int i = nextRandomInt(0, 0x3fffffff) % super.prop.bullet.length + 1;
        for(int j = 0; j < i; j++)
            nextIndexBulletType();

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
        gunproperties.sound = "weapon.Gau12";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 0F;
        gunproperties.emitR = 0F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 2000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.55F;
        //gunproperties.shotFreqDeviation = 0.03F;
        gunproperties.shotFreq = 80.0F;
        gunproperties.traceFreq = 4;
        gunproperties.bullets = 300;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.055F;
        gunproperties.bullet[0].kalibr = 0.0001945F;
        gunproperties.bullet[0].speed = 1050F;
        gunproperties.bullet[0].power = 0.9065F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.8F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = null;
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 3F;
        
        return gunproperties;
    }

    private static RangeRandom theRangeRandom;
}