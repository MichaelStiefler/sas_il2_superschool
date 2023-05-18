// Source File Name: RocketNull.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-03
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class RocketNull extends Rocket {

	static {
		Class class1 = RocketNull.class;
		Property.set(class1, "mesh", "3DO/Arms/Null/mono.sim");
		Property.set(class1, "sprite", (Object) null);
		Property.set(class1, "flame", (Object) null);
		Property.set(class1, "smoke", (Object) null);
		Property.set(class1, "emitColor", new Color3f(0.0F, 0.0F, 0.0F));
		Property.set(class1, "emitLen", 0.0F);
		Property.set(class1, "emitMax", 0.0F);
		Property.set(class1, "sound", (Object) null);
		Property.set(class1, "radius", 0.1F);
		Property.set(class1, "timeLife", 999.999F);
		Property.set(class1, "timeFire", 0.0F);
		Property.set(class1, "force", 0.0F);
		Property.set(class1, "power", 0.01485F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.001F);
		Property.set(class1, "massa", 0.01485F);
		Property.set(class1, "massaEnd", 0.01485F);
	}

	public RocketNull() {
	}

	protected void doExplosion(Actor actor, String s) {
	}

	protected void doExplosionAir() {
	}

	public void start(float f, int i) {
		super.start(f, i);
		this.drawing(false);
	}
}
