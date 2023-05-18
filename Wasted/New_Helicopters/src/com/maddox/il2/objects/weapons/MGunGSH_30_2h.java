package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.Mi24P;
import com.maddox.sas1946.il2.util.TrueRandom;



public class MGunGSH_30_2h
extends MGunVYas
{
	private float fShotFreqFactor;
    private Vector3d vtemp = new Vector3d();
    private Vector3d vwldtemp = new Vector3d();
    private float ftemp;

	public MGunGSH_30_2h() {}
	


	public void shots(int i, float f)
	{		
		if (getOwner() instanceof Mi24P) {
			if (((Mi24P)getOwner()).shotFreqCannon == 0){
				this.fShotFreqFactor = 1.0F;
			} else if (((Mi24P)getOwner()).shotFreqCannon == 1){
				this.fShotFreqFactor = 0.1F;
			}
			this._shotStep = (this.prop.bulletsCluster / (this.prop.shotFreq * this.fShotFreqFactor));
		}
		super.shots(i, f);
	}
	
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
		this.sound.setControl(100, this.fShotFreqFactor);
		super.doStartBullet(d);
        if (modifyAM) {
        	((RealFlightModel)((Aircraft)actor).FM).producedAM.set(this.vtemp);
        	((Aircraft)actor).FM.Vwld.set(this.vwldtemp);
        	((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel = this.ftemp;
        	int rndB = TrueRandom.nextInt(0, 2);
        	double d1 = this.prop.bullet[rndB].massa * this.prop.bullet[rndB].speed;
        	vtemp.x = (TrueRandom.nextDouble(-100.0D, 0.0D) * d1);
        	vtemp.y = (TrueRandom.nextDouble(400.0D, 100.0D) * d1);
        	vtemp.z = (TrueRandom.nextDouble(-300.0D, -100.0D) * d1);
        	vtemp.scale(0.3D);
        	((RealFlightModel)((Aircraft)actor).FM).gunMomentum(vtemp, false);
        }
	}
	
	public GunProperties createProperties()
	{
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
		localGunProperties.emitI = 10.F;
		localGunProperties.emitR = 3.0F;
		localGunProperties.emitTime = 0.03F;
		localGunProperties.aimMinDist = 10.0F;
		localGunProperties.aimMaxDist = 2000.0F;
		localGunProperties.weaponType = 3;
		localGunProperties.maxDeltaAngle = 0.28F;
		localGunProperties.shotFreqDeviation = 0.03F;
		localGunProperties.shotFreq = 41.0F;
		localGunProperties.traceFreq = 3;
		localGunProperties.bullets = 120;
		localGunProperties.bulletsCluster = 1;
		localGunProperties.bullet = new BulletProperties[] { new BulletProperties(), new BulletProperties(), new BulletProperties() };

		localGunProperties.bullet[0].massa = 0.39F;
		localGunProperties.bullet[0].kalibr = 4.6799995E-4F;
		localGunProperties.bullet[0].speed = 940.0F;
		localGunProperties.bullet[0].power = 0.02F;
		localGunProperties.bullet[0].powerType = 0;
		localGunProperties.bullet[0].powerRadius = 1.0F;
		localGunProperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
		localGunProperties.bullet[0].traceTrail = null;
		localGunProperties.bullet[0].traceColor = -654299393;
		localGunProperties.bullet[0].timeLife = 5.1432F;

		localGunProperties.bullet[1].massa = 0.3876F;
		localGunProperties.bullet[1].kalibr = 4.6799995E-4F;
		localGunProperties.bullet[1].speed = 936.0F;
		localGunProperties.bullet[1].power = 0.025F;
		localGunProperties.bullet[1].powerType = 0;
		localGunProperties.bullet[1].powerRadius = 1.2F;
		localGunProperties.bullet[1].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
		localGunProperties.bullet[1].traceTrail = null;
		localGunProperties.bullet[1].traceColor = -654299393;
		localGunProperties.bullet[1].timeLife = 4.1433F;

		localGunProperties.bullet[2].massa = 0.39872F;
		localGunProperties.bullet[2].kalibr = 4.6799995E-4F;
		localGunProperties.bullet[2].speed = 921.0F;
		localGunProperties.bullet[2].power = 0.03F;
		localGunProperties.bullet[2].powerType = 0;
		localGunProperties.bullet[2].powerRadius = 0.5F;
		localGunProperties.bullet[2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
		localGunProperties.bullet[2].traceTrail = null;
		localGunProperties.bullet[2].traceColor = 0;
		localGunProperties.bullet[2].timeLife = 2.47F;
		return localGunProperties;
	}
}

