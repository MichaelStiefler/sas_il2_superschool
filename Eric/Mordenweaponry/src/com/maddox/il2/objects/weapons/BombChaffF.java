package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombChaffF extends Bomb {

	protected boolean haveSound() {
		return false;
	}

	public void start() {
		super.start();
		t1 = Time.current() + 10L;
	}

	public void interpolateTick() {
		super.interpolateTick();
		if (t1 < Time.current())
			doFireContaineds();
	}

	public void msgCollision(Actor actor, String s, String s1) {
		if (t1 > Time.current())
			doFireContaineds();
		super.msgCollision(actor, s, s1);
	}

	private void doFireContaineds() {
		Actor actor = null;
		if (Actor.isValid(getOwner()))
			actor = getOwner();
		Point3d point3d = new Point3d(pos.getAbsPoint());
		Orient orient = new Orient();
		Vector3d vector3d = new Vector3d();
		for (int i = 0; i < 50; i++) {
			orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
			getSpeed(vector3d);
			vector3d.add(World.Rnd().nextDouble(-7D, 7D), World.Rnd().nextDouble(-7D, 7D), World.Rnd().nextDouble(-7D, 7D));
			BombletBLU108 BombletBLU108 = new BombletBLU108();
			((Bomb) (BombletBLU108)).pos.setUpdateEnable(true);
			((Bomb) (BombletBLU108)).pos.setAbs(point3d, orient);
			((Bomb) (BombletBLU108)).pos.reset();
			BombletBLU108.start();
			BombletBLU108.setOwner(actor, false, false, false);
			BombletBLU108.setSpeed(vector3d);
		}

		postDestroy();
	}

	private long t1;

	static {
		Class class1 = BombChaffF.class;
		Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
		Property.set(class1, "radius", 0.001F);
		Property.set(class1, "power", 0.001F);
		Property.set(class1, "dragCoefficient", 0.0F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.00001F);
		Property.set(class1, "massa", 0.00001F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}