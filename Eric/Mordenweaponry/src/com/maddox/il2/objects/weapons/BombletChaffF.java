package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.MsgShot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombletChaffF extends Bomb {

	protected boolean haveSound() {
		return index % 16 == 0;
	}

	protected void doExplosion(Actor actor, String s) {
		Class class1 = getClass();
		Point3d point3d = new Point3d();
		pos.getTime(Time.current(), point3d);
		float f = Property.floatValue(class1, "power", 0.0F);
		int i = Property.intValue(class1, "powerType", 0);
		float f1 = Property.floatValue(class1, "radius", 0.0F);
		MsgShot.send(actor, s, point3d, new Vector3f(0.0F, 0.0F, -600F), M, getOwner(), f, 1, 0.0D);
		if (index % 20 == 0)
			Explosions.generate(actor, point3d, f, i, f1, !Mission.isNet());
		destroy();
	}

	static {
		Class class1 = BombletChaffF.class;
		Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
		Property.set(class1, "radius", 0.1F);
		Property.set(class1, "power", 0.1F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "timeLife", 7F);
		Property.set(class1, "kalibr", 0.1F);
		Property.set(class1, "massa", 22.6F);
		Property.set(class1, "randomOrient", 1);
		Property.set(class1, "sound", "weapon.bomb_cassette");
	}
}