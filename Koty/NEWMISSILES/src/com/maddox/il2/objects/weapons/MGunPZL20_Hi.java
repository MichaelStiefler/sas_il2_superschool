// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 07.04.2019 2:10:49
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MGunPZL20.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import java.security.SecureRandom;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            MGunVYas

public class MGunPZL20_Hi extends MGunVYas
{

    public MGunPZL20_Hi()
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
        gunproperties.fireMesh = null;
        gunproperties.fire = "3DO/Effects/GunFire/30mm/GunFire.eff";
        gunproperties.sprite = "3DO/Effects/GunFire/30mm/GunFlare.eff";
        gunproperties.smoke = "effects/smokes/MachineGun.eff";
        gunproperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
        gunproperties.sound = "weapon.MGun231";
        gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        gunproperties.emitI = 2.5F;
        gunproperties.emitR = 1.5F;
        gunproperties.emitTime = 0.03F;
        gunproperties.aimMinDist = 10F;
        gunproperties.aimMaxDist = 2000F;
        gunproperties.weaponType = 3;
        gunproperties.maxDeltaAngle = 0.28F;
        gunproperties.shotFreqDeviation = 0.03F;
        gunproperties.shotFreq = 43F;
        gunproperties.traceFreq = 1;
        gunproperties.bullets = 120;
        gunproperties.bulletsCluster = 1;
        gunproperties.bullet = (new BulletProperties[] {
            new BulletProperties(), new BulletProperties(), new BulletProperties()
        });
        gunproperties.bullet[0].massa = 0.1F;
        gunproperties.bullet[0].kalibr = 3.2000002E-4F;
        gunproperties.bullet[0].speed = 1020F;
        gunproperties.bullet[0].power = 0F;
        gunproperties.bullet[0].powerType = 0;
        gunproperties.bullet[0].powerRadius = 0F;
        gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
        gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
        gunproperties.bullet[0].traceColor = 0xd200ffff;
        gunproperties.bullet[0].timeLife = 9F;
        
        gunproperties.bullet[1].massa = 0.1F;
        gunproperties.bullet[1].kalibr = 3.2000002E-4F;
        gunproperties.bullet[1].speed = 1020F;
        gunproperties.bullet[1].power = 0.02F;
        gunproperties.bullet[1].powerType = 1;
        gunproperties.bullet[1].powerRadius = 0.5F;
        gunproperties.bullet[1].timeLife = 11F;
        
        gunproperties.bullet[2].massa = 0.1F;
        gunproperties.bullet[2].kalibr = 3.2000002E-4F;//0.0005245F;
        gunproperties.bullet[2].speed = 1020F;
        gunproperties.bullet[2].power = 0.02F;
        gunproperties.bullet[2].powerType = 1;
        gunproperties.bullet[2].powerRadius = 0.5F;
        gunproperties.bullet[2].timeLife = 11F;
        return gunproperties;
    }

    private static RangeRandom theRangeRandom;
}