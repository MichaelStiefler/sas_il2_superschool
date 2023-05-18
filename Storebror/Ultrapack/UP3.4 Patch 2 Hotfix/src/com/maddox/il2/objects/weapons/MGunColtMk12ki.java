package com.maddox.il2.objects.weapons;
import java.security.SecureRandom;

import com.maddox.JGP.Color3f;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;

public class MGunColtMk12ki extends MGunHispanoMkIs
{
    private void initRandom()
    {
        if(theRangeRandom != null)
        {
            return;
        } else
        {
            long lTime = System.currentTimeMillis();
            SecureRandom secRandom = new SecureRandom();
            secRandom.setSeed(lTime);
            long lSeed1 = secRandom.nextInt();
            long lSeed2 = secRandom.nextInt();
            long lSeed = (lSeed1 << 32) + lSeed2;
            theRangeRandom = new RangeRandom(lSeed);
            return;
        }
    }

    private int nextRandomInt(int iMin, int iMax)
    {
        initRandom();
        return theRangeRandom.nextInt(iMin, iMax);
    }

    public void init()
    {
        super.init();
        int iRandBullet = nextRandomInt(0, 0x3fffffff) % this.prop.bullet.length + 1;
        for(int i = 0; i < iRandBullet; i++)
            nextIndexBulletType();

    }

    public GunProperties createProperties()
    {
        GunProperties gunproperties = super.createProperties();
        gunproperties.bCannon = false;
        gunproperties.bUseHookAsRel = true;
        gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = null;
        gunproperties.sound = "weapon.MGunColtMk12s";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 10F;
        gunproperties.emitR = 3F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 1000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.246F;
        gunproperties.shotFreqDeviation = 0.08F;
        gunproperties.shotFreq = 16.667F;
        gunproperties.traceFreq = 5;
        gunproperties.bullets = 250;
        gunproperties.bulletsCluster = 3;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.114F;
        gunproperties.bullet[0].kalibr = 0.00032F;
        gunproperties.bullet[0].speed = 1010F;
        gunproperties.bullet[0].power = 0.0104F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.34F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 1.5F;
        gunproperties.bullet[1].massa = 0.109F;
        gunproperties.bullet[1].kalibr = 0.00024F;
        gunproperties.bullet[1].speed = 1010F;
        gunproperties.bullet[1].power = 0.0052F;
        gunproperties.bullet[1].powerType = 0;
        gunproperties.bullet[1].powerRadius = 0.0F;
        gunproperties.bullet[1].traceMesh = null;
        gunproperties.bullet[1].traceTrail = null;
        gunproperties.bullet[1].traceColor = 0;
        gunproperties.bullet[1].timeLife = 1.5F;
        return gunproperties;
    }

    private static RangeRandom theRangeRandom;
}
