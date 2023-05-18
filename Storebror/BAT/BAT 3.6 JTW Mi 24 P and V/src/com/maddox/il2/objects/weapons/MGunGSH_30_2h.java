package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Mi24P;
import com.maddox.sas1946.il2.util.TrueRandom;

public class MGunGSH_30_2h extends MGunVYas {

    public MGunGSH_30_2h() {
        this.vtemp = new Vector3d();
        this.vwldtemp = new Vector3d();
    }

    public void shots(int i, float f) {
        if (this.getOwner() instanceof Mi24P) {
            if (((Mi24P) this.getOwner()).shotFreqCannon == 0) {
                this.fShotFreqFactor = 1.0F;
            } else if (((Mi24P) this.getOwner()).shotFreqCannon == 1) {
                this.fShotFreqFactor = 0.1F;
            }
            this._shotStep = this.prop.bulletsCluster / (this.prop.shotFreq * this.fShotFreqFactor);
        }
        super.shots(i, f);
    }

    public void doStartBullet(double d) {
        if (!(this.getOwner() instanceof Aircraft)) {
            return;
        }
        Aircraft owner = (Aircraft) this.getOwner();
        boolean modifyAM = false;
        if ((owner == World.getPlayerAircraft()) && World.cur().diffCur.Realistic_Gunnery && (owner.FM instanceof RealFlightModel)) {
            modifyAM = true;
        }
        if (modifyAM) {
            this.vtemp.set(owner.FM.producedAM);
            this.vwldtemp.set(owner.FM.Vwld);
            if (owner.FM instanceof RealFlightModel) {
                this.ftemp = ((RealFlightModel) owner.FM).producedShakeLevel;
            }
        }
        this.sound.setControl(100, this.fShotFreqFactor);
        super.doStartBullet(d);
        if (modifyAM) {
            owner.FM.producedAM.set(this.vtemp);
            owner.FM.Vwld.set(this.vwldtemp);
            if (owner.FM instanceof RealFlightModel) {
                ((RealFlightModel) owner.FM).producedShakeLevel = this.ftemp;
            }
            int rndB = TrueRandom.nextInt(0, 2);
            double d1 = this.prop.bullet[rndB].massa * this.prop.bullet[rndB].speed;
            this.vtemp.x = TrueRandom.nextDouble(-100D, 0.0D) * d1;
            this.vtemp.y = TrueRandom.nextDouble(400D, 100D) * d1;
            this.vtemp.z = TrueRandom.nextDouble(-300D, -100D) * d1;
            this.vtemp.scale(0.3D);
            if (owner.FM instanceof RealFlightModel) {
                ((RealFlightModel) owner.FM).gunMomentum(this.vtemp, false);
            }
        }
    }

    public GunProperties createProperties() {
        GunProperties localGunProperties = super.createProperties();
        localGunProperties.bCannon = false;
        localGunProperties.bUseHookAsRel = true;
        localGunProperties.fireMesh = null;
        localGunProperties.fire = "3DO/Effects/GunFire/37mmSlow/GunFire.eff";
        localGunProperties.sprite = null;
        localGunProperties.smoke = "effects/smokes/37mmSlow.eff";
        localGunProperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
        localGunProperties.sound = "weapon.MGunGSh30_2";
        localGunProperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
        localGunProperties.emitI = 10F;
        localGunProperties.emitR = 3F;
        localGunProperties.emitTime = 0.03F;
        localGunProperties.aimMinDist = 10F;
        localGunProperties.aimMaxDist = 2000F;
        localGunProperties.weaponType = 3;
        localGunProperties.maxDeltaAngle = 0.28F;
        localGunProperties.shotFreqDeviation = 0.03F;
        localGunProperties.shotFreq = 41F;
        localGunProperties.traceFreq = 3;
        localGunProperties.bullets = 120;
        localGunProperties.bulletsCluster = 1;
        localGunProperties.bullet = (new BulletProperties[] { new BulletProperties(), new BulletProperties(), new BulletProperties() });
        localGunProperties.bullet[0].massa = 0.39F;
        localGunProperties.bullet[0].kalibr = 0.000468F;
        localGunProperties.bullet[0].speed = 940F;
        localGunProperties.bullet[0].power = 0.02F;
        localGunProperties.bullet[0].powerType = 0;
        localGunProperties.bullet[0].powerRadius = 1.0F;
        localGunProperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        localGunProperties.bullet[0].traceTrail = null;
        localGunProperties.bullet[0].traceColor = 0xd9002eff;
        localGunProperties.bullet[0].timeLife = 5.1432F;
        localGunProperties.bullet[1].massa = 0.3876F;
        localGunProperties.bullet[1].kalibr = 0.000468F;
        localGunProperties.bullet[1].speed = 936F;
        localGunProperties.bullet[1].power = 0.025F;
        localGunProperties.bullet[1].powerType = 0;
        localGunProperties.bullet[1].powerRadius = 1.2F;
        localGunProperties.bullet[1].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        localGunProperties.bullet[1].traceTrail = null;
        localGunProperties.bullet[1].traceColor = 0xd9002eff;
        localGunProperties.bullet[1].timeLife = 4.1433F;
        localGunProperties.bullet[2].massa = 0.39872F;
        localGunProperties.bullet[2].kalibr = 0.000468F;
        localGunProperties.bullet[2].speed = 921F;
        localGunProperties.bullet[2].power = 0.03F;
        localGunProperties.bullet[2].powerType = 0;
        localGunProperties.bullet[2].powerRadius = 0.5F;
        localGunProperties.bullet[2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        localGunProperties.bullet[2].traceTrail = null;
        localGunProperties.bullet[2].traceColor = 0;
        localGunProperties.bullet[2].timeLife = 2.47F;
        return localGunProperties;
    }

    private float    fShotFreqFactor;
    private Vector3d vtemp;
    private Vector3d vwldtemp;
    private float    ftemp;
}
