package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombTorpFFF extends BombParaTorp {

	public BombTorpFFF() {
		this.chute1 = null;
		this.chute2 = null;
		this.bOnChute1 = false;
		this.bOnChute2 = false;
	}

	public void start() {
		super.start();
		this.ttcurTM = 1.5F;
		if (!(this instanceof BombTorpFFF1)) {
			this.ttcurTM += 1.2F;
			this.openHeight = 210F;
		} else {
			this.openHeight = 180F;
		}
	}

	public void destroy() {
		if (this.chute1 != null) { this.chute1.destroy(); }
		if (this.chute2 != null) { this.chute2.destroy(); }
		super.destroy();
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if (actor instanceof ActorLand && (this.chute1 != null || this.chute2 != null)) { this.bOnChute1 = false; }
		this.ttcurTM = 100000F;
		if (this.chute1 != null) { this.chute1.landing(); }
		if (this.chute2 != null) { this.chute2.landing(); }
		super.msgCollision(actor, s, s1);
	}

	public void interpolateTick() {
		this.curTm += Time.tickLenFs();
		super.interpolateTick();
		if (this.bOnChute2) {
			this.getSpeed(v3d);
			v3d.scale(0.99D);
			if (v3d.z < -40D) { v3d.z += 8F * Time.tickConstLenFs(); }
			this.setSpeed(v3d);
			this.pos.getAbs(P, Or);
		} else if (this.bOnChute1) {
			this.getSpeed(v3d);
			v3d.scale(0.99D);
			if (v3d.z < -90D) { v3d.z += 1.1F * Time.tickConstLenFs(); }
			this.setSpeed(v3d);
			this.pos.getAbs(P, Or);
			if (P.z < this.openHeight) {
				this.bOnChute2 = true;
				this.bOnChute1 = false;
				// TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//              chute2 = new Chute(this);
				this.chute2 = new BombChute(this, 2.5F);
				this.chute2.collide(false);
				// TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//              chute2.mesh().setScale(2.5F);
				this.chute2.pos.setRel(new Point3d(3D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
				if (this.chute1 != null) { this.chute1.destroy(); }
			}
		} else if (this.curTm > this.ttcurTM) {
			this.bOnChute1 = true;
			// TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//          chute1 = new Chute(this);
			this.chute1 = new BombChute(this, 0.4F);
			this.chute1.collide(false);
			// TODO: Bugfix by SAS~Storebror: Avoid Scaling Meshes with Shadows!
//          chute1.mesh().setScale(0.4F);
			this.chute1.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
		}
	}

	private Chute           chute1;
	private Chute           chute2;
	public boolean          bOnChute1;
	public boolean          bOnChute2;
	private static Vector3d v3d = new Vector3d();
	private float           ttcurTM;
	private float           openHeight;

	static {
		final Class class1 = BombTorpFFF.class;
		Property.set(class1, "mesh", "3DO/Arms/MotobombaFFF/mono.sim");
		Property.set(class1, "radius", 70F);
		Property.set(class1, "power", 120F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.5F);
		Property.set(class1, "massa", 360F);
		Property.set(class1, "sound", "weapon.torpedo");
		Property.set(class1, "velocity", 6.1F);
		Property.set(class1, "traveltime", 2100F);
		Property.set(class1, "startingspeed", 0.0F);
		Property.set(class1, "impactAngleMin", 0.0F);
		Property.set(class1, "impactAngleMax", 90.5F);
		Property.set(class1, "impactSpeed", 115F);
		Property.set(class1, "armingTime", 3.5F);
	}
}
