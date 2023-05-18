package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.air.Aircraft;

/**
 * @author SAS~Skylla
 * This class was created by SAS~Skylla in the course of the AC-47 rework.
 * @see AC_47, MGunMiniGun3000, MGunMiniGun6000
**/

public abstract class MGunAdjustableMiniGun extends MGunAircraftGeneric{

	protected float rpm = 0.0F;
	protected float maxRPM = 0.0F;
	protected float minRPM = 0.0F;
	protected float stepRPM = 0.0F;
	
	private static final float RECOIL_FACTOR = 0.083F;
    private Vector3d vtemp = new Vector3d();
    private Vector3d vwldtemp = new Vector3d();
    private float ftemp;
		
  //Alterable RPM: -------------------------------------------------------------------------------------
	
	public float incRPM() {
		if(rpm + stepRPM <= maxRPM) {
			rpm += stepRPM;
		} else {
			rpm = maxRPM;
		}
		return rpm;
	}
	
	public float decRPM() {
		if(rpm - stepRPM >= minRPM) {
			rpm -= stepRPM;
		} else {
			rpm = minRPM;
		}
		return rpm;
	}
	
	public float resRPM() {
		this.rpm = maxRPM;
		return rpm;
	}
	
	public void shots(int i, float f) {
		this._shotStep = (float)prop.bulletsCluster / (rpm/60.0F);
		super.shots(i,f);
	}
	
  //Reduced Recoil: -----------------------------------------------------------------------------------
	
	public void doStartBullet(double d) {
		Actor actor = getOwner();
		boolean modifyAM = false;
        if(actor == World.getPlayerAircraft()) {
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
        	((RealFlightModel)((Aircraft)actor).FM).producedAM.set(factorizedVector(((RealFlightModel)((Aircraft)actor).FM).producedAM, this.vtemp, RECOIL_FACTOR));
        	((Aircraft)actor).FM.Vwld.set(factorizedVector(((Aircraft)actor).FM.Vwld, this.vwldtemp, RECOIL_FACTOR));
        	((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel = ((RealFlightModel)((Aircraft)actor).FM).producedShakeLevel * RECOIL_FACTOR + this.ftemp * (1F -RECOIL_FACTOR);
        }
	}
	
	private Vector3d factorizedVector(Vector3d v1, Vector3d v2, float factor) {
	  v1.x = v1.x * factor + v2.x * (1F - factor);
	  v1.y = v1.y * factor + v2.y * (1F - factor);
	  v1.z = v1.z * factor + v2.z * (1F - factor);
	  return v1;
	}
	
  //---------------------------------------------------------------------------------------------------
	
    public GunProperties createProperties() {
        final GunProperties properties = super.createProperties();
        properties.bCannon = false;
        properties.bUseHookAsRel = true;
        properties.bEnablePause = true;
        properties.fire = null;
        properties.shells = null;
        properties.sound = "weapon.MiniGun";
        properties.shotFreq = 50.0f;
        properties.bullets = 8000;
        properties.maxDeltaAngle = 0.255F; //default = 0.229F;
        properties.fireMesh = "3DO/Effects/GunFire/12mm/mono.sim";
        properties.sprite = "3DO/Effects/GunFire/12mm/GunFlare.eff";
        properties.smoke = "effects/smokes/MachineGun.eff";
        properties.emitColor = new Color3f(1.0f, 1.0f, 0.0f);
        properties.emitI = 2.5f;
        properties.emitR = 1.5f;
        properties.emitTime = 0.03f;
        properties.aimMinDist = 10.0f;
        properties.aimMaxDist = 1000.0f;
        properties.weaponType = -1;
        properties.traceFreq = 2;
        properties.bulletsCluster = 2;
        int bl = 2;
        bl = bl * 5 + 4;
        properties.bullet = new BulletProperties[bl];
        for(int j = 0; j < bl; j++) {
        	properties.bullet[j] =  new BulletProperties();
        }
        for(int i = 0; i < bl - 4; i += 5) {
        	properties.bullet[i+0].massa = 0.046f;
        	properties.bullet[i+0].kalibr = 1.181215E-4f;
        	properties.bullet[i+0].speed = 870.0f;
        	properties.bullet[i+0].power = 0.0f;
        	properties.bullet[i+0].powerType = 0;
        	properties.bullet[i+0].powerRadius = 0.0f;
        	properties.bullet[i+0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        	properties.bullet[i+0].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        	properties.bullet[i+0].traceColor = -117440257;
        	properties.bullet[i+0].timeLife = 6.5f;
        	properties.bullet[i+1].massa = 0.041f;
        	properties.bullet[i+1].kalibr = 1.185215E-4f;
        	properties.bullet[i+1].speed = 900.0f;
        	properties.bullet[i+1].power = 0.0022f;
        	properties.bullet[i+1].powerType = 0;
        	properties.bullet[i+1].powerRadius = 0.0f;
        	properties.bullet[i+1].traceMesh = null;
        	properties.bullet[i+1].traceTrail = null;
        	properties.bullet[i+1].traceColor = 0;
        	properties.bullet[i+1].timeLife = 6.3f;
        	properties.bullet[i+2].massa = 0.046f;
        	properties.bullet[i+2].kalibr = 1.181215E-4f;
        	properties.bullet[i+2].speed = 870.0f;
        	properties.bullet[i+2].power = 0.0f;
        	properties.bullet[i+2].powerType = 0;
        	properties.bullet[i+2].powerRadius = 0.0f;
        	properties.bullet[i+2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        	properties.bullet[i+2].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        	properties.bullet[i+2].traceColor = -117440257;
        	properties.bullet[i+2].timeLife = 6.5f;
        	properties.bullet[i+3].massa = 0.041f;
        	properties.bullet[i+3].kalibr = 1.185215E-4f;
        	properties.bullet[i+3].speed = 900.0f;
        	properties.bullet[i+3].power = 0.0022f;
        	properties.bullet[i+3].powerType = 0;
        	properties.bullet[i+3].powerRadius = 0.0f;
        	properties.bullet[i+3].traceMesh = null;
        	properties.bullet[i+3].traceTrail = null;
        	properties.bullet[i+3].traceColor = 0;
        	properties.bullet[i+3].timeLife = 6.3f;
        	properties.bullet[i+4].massa = 0.042f;
        	properties.bullet[i+4].kalibr = 1.182215E-4f;
        	properties.bullet[i+4].speed = 820.0f;
        	properties.bullet[i+4].power = 0.001f;
        	properties.bullet[i+4].powerType = 0;
        	properties.bullet[i+4].powerRadius = 0.0f;
        	properties.bullet[i+4].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        	properties.bullet[i+4].traceTrail = "3DO/Effects/TEXTURES/fumeefine.eff";
        	properties.bullet[i+4].traceColor = -117440257;
        	properties.bullet[i+4].timeLife = 6.5f;
        }
        properties.bullet[bl-4].massa = 0.043f;
        properties.bullet[bl-4].kalibr = 1.209675E-4f;
        properties.bullet[bl-4].speed = 890.0f;
        properties.bullet[bl-4].power = 0.002f;
        properties.bullet[bl-4].powerType = 0;
        properties.bullet[bl-4].powerRadius = 0.0f;
        properties.bullet[bl-4].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
        properties.bullet[bl-4].traceTrail = "Effects/Smokes/SmokeBlack_BuletteTrail.eff";
        properties.bullet[bl-4].traceColor = -654311169;
        properties.bullet[bl-4].timeLife = 6.5f;
        properties.bullet[bl-3].massa = 0.043f;
        properties.bullet[bl-3].kalibr = 1.105062E-4f;
        properties.bullet[bl-3].speed = 890.0f;
        properties.bullet[bl-3].power = 0.002f;
        properties.bullet[bl-3].powerType = 0;
        properties.bullet[bl-3].powerRadius = 0.0f;
        properties.bullet[bl-3].traceMesh = null;
        properties.bullet[bl-3].traceTrail = null;
        properties.bullet[bl-3].traceColor = 0;
        properties.bullet[bl-3].timeLife = 6.52f;
        properties.bullet[bl-2].massa = 0.043f;
        properties.bullet[bl-2].kalibr = 1.105062E-4f;
        properties.bullet[bl-2].speed = 890.0f;
        properties.bullet[bl-2].power = 9.768E-4f;
        properties.bullet[bl-2].powerType = 0;
        properties.bullet[bl-2].powerRadius = 0.15f;
        properties.bullet[bl-2].traceMesh = null;
        properties.bullet[bl-2].traceTrail = null;
        properties.bullet[bl-2].traceColor = 0;
        properties.bullet[bl-1].timeLife = 6.5f;
        properties.bullet[bl-1].massa = 0.043f;
        properties.bullet[bl-1].kalibr = 1.105062E-4f;
        properties.bullet[bl-1].speed = 890.0f;
        properties.bullet[bl-1].power = 0.002f;
        properties.bullet[bl-1].powerType = 0;
        properties.bullet[bl-1].powerRadius = 0.0f;
        properties.bullet[bl-1].traceMesh = null;
        properties.bullet[bl-1].traceTrail = null;
        properties.bullet[bl-1].traceColor = 0;
        properties.bullet[bl-1].timeLife = 6.25f;
        return properties;
    }
    
    /*
    public void initRealisticGunnery(final boolean b) {
    	for (int length = this.prop.bullet.length, i = 0; i < length; ++i) {
    		if (b) {
            	this.bulletAG[i] = -9.81f;
                this.bulletKV[i] = -(1000.0f * this.prop.bullet[i].kalibr / this.prop.bullet[i].massa);
            } else {
                this.bulletAG[i] = 0.0f;
                this.bulletKV[i] = 0.0f;
            }
        }
	}
    */
}
