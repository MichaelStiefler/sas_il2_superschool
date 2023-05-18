package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.Random;

public class Bomb500lbsi extends Bomb
{

	public Bomb500lbsi()
	{
		boom = false;
	}

	public void start()
	{
		super.start();
		drawing(true);
		t1 = Time.current() + 0x1c3a90L + World.Rnd().nextLong(0L, 850L);
		t2 = Time.current() + 0x1c3a90L + World.Rnd().nextLong(0L, 3800L);
	}

	public void msgCollision(Actor actor, String s, String s1)
	{
		if(actor instanceof ActorLand)
		{
			if(Time.current() <= (t2 + t1) / 2L)
			{
				Point3d point3d = new Point3d();
				pos.getTime(Time.current(), point3d);
				Class class1 = getClass();
				float f = Property.floatValue(class1, "power", 0.0F);
				int i = Property.intValue(class1, "powerType", 0);
				float f1 = Property.floatValue(class1, "radius", 1.0F);
				MsgExplosion.send(actor, s1, point3d, getOwner(), M, f, i, f1);
				Vector3d vector3d = new Vector3d();
				getSpeed(vector3d);
				vector3d.x *= 0.5D;
				vector3d.y *= 0.5D;
				vector3d.z = 0.5D;
				setSpeed(vector3d);
				if(!boom)
				{
					World.MaxVisualDistance = 25000F;
					Explosions.generate(actor, point3d, 50F, 0, 125F, !Mission.isNet());
					MsgExplosion.send(actor, s, point3d, getOwner(), 0.0F, 50F, 0, 125F);
					Engine.land();
					int j = Landscape.getPixelMapT(Engine.land().WORLD2PIXX(point3d.x), Engine.land().WORLD2PIXY(point3d.y));
					if(j >= 16 && j < 20)
					{
						Random random = new Random();
						int k = random.nextInt(240);
						float f2 = (float)k + 60F;
						Eff3DActor.New(this, null, new Loc(), 0.3F, "Effects/Smokes/CityFire.eff", f2);
					}
					boom = true;
				}
			}
		} else
		{
			super.msgCollision(actor, s, s1);
		}
	}

	private long t1;
	private long t2;
	boolean boom;

	static 
	{
		Class class1 = com.maddox.il2.objects.weapons.Bomb500lbsi.class;
		Property.set(class1, "mesh", "3DO/Arms/500LbsBomb/mono.sim");
		Property.set(class1, "power", 80F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.32F);
		Property.set(class1, "massa", 250F);
		Property.set(class1, "sound", "weapon.bomb_mid");
		Property.set(class1, "fuze", ((Object) (new Object[] {
		            com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_M112.class, com.maddox.il2.objects.weapons.Fuze_M115.class, com.maddox.il2.objects.weapons.Fuze_M135A1.class
		})));

	}
}