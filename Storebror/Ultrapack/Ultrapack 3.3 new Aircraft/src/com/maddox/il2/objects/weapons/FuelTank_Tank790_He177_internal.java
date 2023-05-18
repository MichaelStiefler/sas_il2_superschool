package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FuelTank_Tank790_He177_internal extends FuelTank {

	public FuelTank_Tank790_He177_internal() {
		this.randomRotation = new Orient();
	}

	public void start() {
		super.start();
		this.randomRotation.set(0.0F, 90.0F, 0.0F);
		this.pos.getAbsOrient().add(this.randomRotation);
		this.randomRotation.set(World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F), World.Rnd().nextFloat(-1.0F, 1.0F));
		this.t1 = Time.current();
	}

	public void interpolateTick() {
		if (Time.current() < this.t1 + 1000L) this.S *= 1.1F;
		if (Time.current() < this.t1 + 3000L) this.randomRotation.set(this.randomRotation.getYaw() * 1.02F, -this.randomRotation.getTangage() * 1.02F, this.randomRotation.getKren() * 1.02F);
		this.pos.getAbsOrient().add(this.randomRotation);
		super.interpolateTick();
	}

	Orient       randomRotation;
	private long t1;

	static {
		Class class1 = FuelTank_Tank790_He177_internal.class;
		Property.set(class1, "mesh", "3DO/Arms/He-177-Tank790_internal/mono.sim");
		Property.set(class1, "kalibr", 0.5F);
		Property.set(class1, "massa", 600.0F);
		Property.set(class1, "randomOrient", 1);
	}
}
