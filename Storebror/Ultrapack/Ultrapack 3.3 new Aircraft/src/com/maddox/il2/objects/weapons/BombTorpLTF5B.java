package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpLTF5B extends Torpedo {

	static {
		Class class1 = BombTorpLTF5B.class;
		Property.set(class1, "mesh", "3DO/Arms/F5B/mono.sim");
		Property.set(class1, "radius", 12F);
		Property.set(class1, "power", 200F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.45F);
		Property.set(class1, "massa", 725F);
		Property.set(class1, "sound", "weapon.torpedo");
		Property.set(class1, "velocity", 20.6F);
		Property.set(class1, "traveltime", 111.6505F);
		Property.set(class1, "startingspeed", 0.0F);
		Property.set(class1, "impactAngleMin", 18.6F);
		Property.set(class1, "impactAngleMax", 44F);
		Property.set(class1, "impactSpeed", 77.5F);
		Property.set(class1, "armingTime", 2.0F);
		Property.set(class1, "dropAltitude", 40F);
		Property.set(class1, "dropSpeed", 260F);
	}
}
