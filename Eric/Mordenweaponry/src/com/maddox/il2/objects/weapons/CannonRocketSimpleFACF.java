package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.Orient;

public class CannonRocketSimpleFACF extends CannonRocketSimple {

	protected void Specify(GunProperties gunproperties) {
		gunproperties.sound = "weapon.rocketgun_132";
		gunproperties.shotFreq = 10.33F;
		gunproperties.aimMinDist = 10F;
		gunproperties.aimMaxDist = 8000F;
		BulletProperties bulletproperties = gunproperties.bullet[0];
		bulletproperties.speed = 2000F;
	}

	public void launch(Point3d point3d, Orient orient, float f, Actor actor) {
		RocketSimpleFACF rocketSimpleFACF = new RocketSimpleFACF(point3d, orient, actor);
		rocketSimpleFACF.start(f);
	}
}