package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.engine.*;
import java.security.SecureRandom;

public class MGunM39Eki extends MGunHispanoMkIs
{

    public MGunM39Eki()
    {
    }

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
        gunproperties.fire = null;
        gunproperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = null;
        gunproperties.sound = "weapon.M39";
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
        gunproperties.traceFreq = 3;
        gunproperties.bullets = 250;
        gunproperties.bulletsCluster = 3;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.101F;
        gunproperties.bullet[0].kalibr = 0.00032F;
        gunproperties.bullet[0].speed = 1030F;
        gunproperties.bullet[0].power = 0.0104F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0.34F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 1.5F;
        gunproperties.bullet[1].massa = 0.101F;
        gunproperties.bullet[1].kalibr = 0.00024F;
        gunproperties.bullet[1].speed = 1030F;
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