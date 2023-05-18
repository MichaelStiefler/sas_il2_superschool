
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.TrueRandom;


public class RocketFlareLO56_gn16 extends RocketFlare_gn16
{
	private long t1;
	private Eff3DActor eff1;

	public RocketFlareLO56_gn16()
	{
	}

	public void start(float f, int i)
	{
		float f1 = 30F;
		super.start(f1, i);
		getOwner().getSpeed(speed);
		setSpeed(speed);
		if (com.maddox.il2.engine.Config.isUSE_RENDER())
		{
            Loc loc = new Loc();
            loc.set(0D, 0D, 0D, 180F, 0F, 0F);
			this.eff1 = Eff3DActor.New(this, null, loc, 0.8F, "3DO/Effects/Tracers/GuidedRocket/White.eff", f1);
		}
		this.t1 = Time.current() + 5000L;
		Engine.countermeasures().add(this);
	}

	public void interpolateTick()
	{
		super.interpolateTick();
		if (t1 + TrueRandom.nextLong(2000) < Time.current()) {
			destroy();
		}
	}

	public void destroy() {
		if (com.maddox.il2.engine.Config.isUSE_RENDER())
		{
			Eff3DActor.finish(this.eff1);
		}
		super.destroy();
	}

	protected void doExplosion(Actor actor, String s)
	{
		super.doExplosion(actor, s);
	}

	protected void doExplosionAir()
	{
	}

	static 
	{
		Class class1 = com.maddox.il2.objects.weapons.RocketFlareLO56_gn16.class;
		Property.set(class1, "mesh", "3do/arms/Piropatron/mono.sim");
		Property.set(class1, "sprite", (Object)null);
		Property.set(class1, "flame", (Object)null);
		Property.set(class1, "smoke", (Object)null);
		Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
		Property.set(class1, "emitLen", 5F);
		Property.set(class1, "emitMax", 20F);
		Property.set(class1, "sound", (Object)null);
		Property.set(class1, "timeLife", 2F);
		Property.set(class1, "timeFire", 0.001F);
		Property.set(class1, "force", 500F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "power", 1E-005F);
		Property.set(class1, "radius", 0.0F);
		Property.set(class1, "kalibr", 0.2F);
		Property.set(class1, "massa", 3F);
		Property.set(class1, "massaEnd", 3F);
		Property.set(class1, "friendlyName", "Flare");
		Property.set(class1, "iconFar_shortClassName", "Flare");
	}
}
