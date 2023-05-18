package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombAB23 extends Bomb {

	public BombAB23() {
		this.bombletsTotal = this.bombletsLeft = 23;
		this.fireTime = 300;
		this.fireContainedStarted = false;
		this.minFirePerTick = (int) ((float) this.bombletsLeft * (float) Time.tickLen() / this.fireTime);
	}

	public void start() {
		super.start();
		this.t1 = Time.current() + Math.max(this.armingTime, 750L) + World.Rnd().nextLong(-100L, 100L);
		this.t2 = this.t1 + this.fireTime;
	}

	public void interpolateTick() {
		super.interpolateTick();
		if (this.t1 < Time.current()) { this.doFireContaineds(); }
	}

	public void msgCollision(Actor actor, String s, String s1) {
		super.msgCollision(actor, s, s1);
		if (this.t1 > Time.current() && this.isFuseArmed()) { this.doFireContaineds(); }
	}

	private void doFireContaineds() {
		if (!this.fireContainedStarted) {
			Explosions.AirFlak(this.pos.getAbsPoint(), 1);
			this.fireContainedStarted = true;
		}
		if (this.t2 < Time.current() || this.bombletsLeft < 1) {
			this.doFireContaineds(this.bombletsLeft);
			Explosions.AirFlak(this.pos.getAbsPoint(), 1);
			this.postDestroy();
			return;
		}
		int numToFire = this.bombletsLeft - (int) ((float) this.bombletsTotal * (float) (this.t2 - Time.current()) / this.fireTime);
		if (numToFire < this.minFirePerTick) { numToFire = this.minFirePerTick; }
		this.doFireContaineds(numToFire);
	}

	private void doFireContaineds(int numToFire) {
		if (numToFire > this.bombletsLeft) { numToFire = this.bombletsLeft; }
		if (numToFire == 0) { return; }
		Actor actor = Actor.isValid(this.getOwner()) ? this.getOwner() : null;
		Point3d point3d = new Point3d();
		Orient orient = new Orient();
		Vector3d vector3d = new Vector3d();
		Vector3d vector3d1 = new Vector3d();
		Loc loc = new Loc();
		this.pos.getCurrent(loc);
		this.getSpeed(vector3d1);
		while (numToFire-- > 0) {
			loc.get(point3d, orient);
			orient.increment(World.Rnd().nextFloat(-180F, 180F), World.Rnd().nextFloat(-180F, 0F), World.Rnd().nextFloat(180F, -180F));
			vector3d.set(1.0D, 0.0D, 0.0D);
			orient.transform(vector3d);
			vector3d.scale(World.Rnd().nextDouble(3D, 15D));
			vector3d.add(vector3d1);
			BombSD2A bombsd2a = new BombSD2A();
			bombsd2a.pos.setUpdateEnable(true);
			bombsd2a.pos.setAbs(point3d, orient);
			bombsd2a.pos.reset();
			bombsd2a.delayExplosion = this.delayExplosion;
			bombsd2a.start();
			bombsd2a.setOwner(actor, false, false, false);
			bombsd2a.setSpeed(vector3d);
			if (this.bombletsLeft-- % 2 == 0) { Eff3DActor.New(bombsd2a, null, null, 3F, "effects/Smokes/SmokeBlack_BuletteTrail.eff", 30F); }
		}
	}

	private long    t1;
	private long    t2;
	private int     bombletsLeft;
	private int     bombletsTotal;
	private int     minFirePerTick;
	private long    fireTime;
	private boolean fireContainedStarted;

	static {
		Class class1 = BombAB23.class;
		Property.set(class1, "mesh", "3DO/Arms/AB-23/mono.sim");
		Property.set(class1, "radius", 1.0F);
		Property.set(class1, "power", 0.15F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.18F);
		Property.set(class1, "massa", 46F);
		Property.set(class1, "sound", "weapon.bomb_std");
	}
}
