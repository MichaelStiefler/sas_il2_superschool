package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.vehicles.stationary.StationarySAM.S_75M;
import com.maddox.rts.NetChannel;

public class GunMissileV755 extends CannonAntiAirGeneric
{
	public NetChannel netchannel;
	Point3d point3d = new Point3d();
	Orient orient = new Orient();
	private static Point3d tmpP = new Point3d();
	private static Orient tmpO = new Orient();
	private Actor victim;



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
		victim = ((S_75M) getOwner()).getMissileTarget();
		MissileV755 missile = new MissileV755(getOwner(), netchannel, 1, tmpP, tmpO, 100, victim);
		missile.start(0, 0);
	}
}
