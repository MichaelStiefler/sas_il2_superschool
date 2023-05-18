package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetChannel;
import com.maddox.sas1946.il2.util.Reflection;

public class GunMissile9M32M extends CannonAntiAirGeneric
{
	public NetChannel netchannel;
	Point3d point3d = new Point3d();
	Orient orient = new Orient();
	private static Point3d tmpP = new Point3d();
	private static Orient tmpO = new Orient();
	private Actor victim = null;



	protected float Specify(GunProperties paramGunProperties)
	{
		BulletProperties localBulletProperties = paramGunProperties.bullet[0];
		localBulletProperties.speed = 150.0F;
		return 56.0F;
	}

	public void doStartBullet(double paramDouble)
	{
		this.pos.getAbs(tmpP);
		this.pos.getAbs(tmpO);
		netchannel = null;
		Object[] fireDeviceArr = (Object[])Reflection.getValue(getOwner(), "arms");
		if (fireDeviceArr.length > 0) {
		  Aim aim = (Aim)Reflection.getValue(fireDeviceArr[0], "aim");
		  victim = aim.getEnemy();
		}
		Missile9M32M missile = new Missile9M32M(getOwner(), netchannel, 1, tmpP, tmpO, 100, victim);
	}
}
