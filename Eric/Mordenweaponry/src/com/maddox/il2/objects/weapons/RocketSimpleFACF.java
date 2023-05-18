package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class RocketSimpleFACF extends RocketSimple {

	public RocketSimpleFACF(Point3d point3d, Orient orient, Actor actor) {
		super(point3d, orient, actor);
	}

	static {
		Class class1 = RocketSimpleFACF.class;
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 25F);
		Property.set(class1, "emitMax", 1.0F);
		Property.set(class1, "sound", "weapon.rocket_82");
		Property.set(class1, "radius", 20F);
		Property.set(class1, "timeLife", 999.999F);
		Property.set(class1, "timeFire", 1F);
		Property.set(class1, "force", 50000000000000000F);
		Property.set(class1, "power", 9.6F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.16985F);
		Property.set(class1, "massa", 11F);
		Property.set(class1, "massaEnd", 6F);
	}
}