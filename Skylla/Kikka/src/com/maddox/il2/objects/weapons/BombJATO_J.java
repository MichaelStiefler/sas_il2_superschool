package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
//import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
//import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombJATO_J extends Bomb {
	
	/**
	 * @author SAS~Skylla
	 *	for the moment, this is just a copy of the BombJATO class used for F-84.
	 * 	new 3D might be added in the future if I find someone willing to make it for me ..
	 * @see Kikka.java
	 */
	
	private Chute chute = null;
	private boolean bOnChute = false;
	private static Orient or = new Orient();
	private static Orient or_ = new Orient(0.0F, 0.0F, 0.0F);
	private static Vector3d v3d = new Vector3d();
	private float ttcurTM;
  
	protected boolean haveSound() {
		return false;
    }
  
	public void start() {
		super.start();
		this.ttcurTM = World.Rnd().nextFloat(0.5F, 3.5F);
	}
  
	public void interpolateTick(){
		super.interpolateTick();
		if (this.bOnChute){
			this.pos.getAbs(or);
			or.interpolate(or_, 0.4F);
			this.pos.setAbs(or);
			getSpeed(v3d);
			v3d.scale(0.997D);
			if (v3d.z < -5.0D) {
				v3d.z += 1.1F * Time.tickConstLenFs();
			}
			setSpeed(v3d);
		} else if (this.curTm > this.ttcurTM) {
			this.bOnChute = true;
			this.chute = new Chute(this);
			this.chute.collide(false);
			//TODO new 3D
			setMesh("3DO/Arms/JatoJChuted/mono.sim");
		}
	}
  
	public void msgCollision(Actor paramActor, String paramString1, String paramString2){
		if ((paramActor instanceof ActorLand)){
			if (this.chute != null) {
				this.chute.landing();
			}
			postDestroy();
		} else {
			super.msgCollision(paramActor, paramString1, paramString2);
		}
	}
  
	static {
		Class localClass = CLASS.THIS();
		//TODO new 3D
		Property.set(localClass, "mesh", "3DO/Arms/JatoJ/mono.sim");
		Property.set(localClass, "radius", 0.1F);
		Property.set(localClass, "power", 0.0F);
		Property.set(localClass, "powerType", 0);
		Property.set(localClass, "kalibr", 0.7F);
		Property.set(localClass, "massa", 0.9F);
		Property.set(localClass, "sound", "weapon.bomb_phball");
	}
}
