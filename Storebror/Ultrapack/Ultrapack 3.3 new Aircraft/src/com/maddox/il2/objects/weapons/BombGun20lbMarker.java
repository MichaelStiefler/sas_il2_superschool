package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun20lbMarker extends BombGun {

	public BombGun20lbMarker() {
	}

	public void setBombDelay(float f) {
		this.bombDelay = 0.0F;
		if (this.bomb != null) this.bomb.delayExplosion = this.bombDelay;
	}

	static {
		Class class1 = BombGun20lbMarker.class;
		Property.set(class1, "bulletClass", (Object) Bomb20lbMarker.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 2.0F);
		Property.set(class1, "external", 1);
		Property.set(class1, "sound", "weapon.bombgun_phball");
	}
}
