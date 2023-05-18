package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;





public class RocketGunFlareLO56_gn16
extends RocketGun
{
	public RocketGunFlareLO56_gn16() {}

	static
	{
		Class localClass = com.maddox.il2.objects.weapons.RocketGunFlareLO56_gn16.class;
		Property.set(localClass, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketFlareLO56_gn16.class);
		Property.set(localClass, "bullets", 1);
		Property.set(localClass, "shotFreq", 7.0F);
		Property.set(localClass, "sound", "weapon.Flare");
	}
}