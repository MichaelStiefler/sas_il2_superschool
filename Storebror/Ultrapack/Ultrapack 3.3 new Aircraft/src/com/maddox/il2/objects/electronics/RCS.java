package com.maddox.il2.objects.electronics;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.air.Aircraft;

public class RCS {

	public RCS() {
		this.R_2_lambda = 10F;
		this.SMALL_LOBES_SIZE = 10F;
		this.SIDE_LOBE_SIZE = 15F;
		this.FRONT_LOBE_SIZE = 5F;
		this.REAR_LOBE_SIZE = 1.0F;
		this.TOP_LOBE_SIZE = 25F;
		this.RCS_DIVISION_FACTOR = 310F;
	}

	protected float func(float f, float f1) {
		double d = Math.sin(Math.toRadians(f));
		d *= d;
		double d1 = Math.sin(Math.toRadians(f1)) * Math.cos(Math.toRadians(f));
		d1 *= d1;
		return (float) Math.cos(Math.PI * this.R_2_lambda * (float) Math.sqrt(d + d1)) * f / 90F;
	}

	protected float mult(float f, float f1) {
		float f2;
		if (f > 78F && f < 102F) {
			f2 = -0.006953F * (90F - f) * (90F - f) + 1.0F;
			f2 = this.SMALL_LOBES_SIZE + f2 * this.SIDE_LOBE_SIZE;
		} else if (f < 12F) {
			f2 = -0.006953F * f * f + 1.0F;
			f2 = this.SMALL_LOBES_SIZE + f2 * this.FRONT_LOBE_SIZE;
		} else if (f > 168F) {
			f2 = -0.006953F * (180F - f) * (180F - f) + 1.0F;
			f2 = this.SMALL_LOBES_SIZE + f2 * this.REAR_LOBE_SIZE;
		} else f2 = this.SMALL_LOBES_SIZE;
		float f3 = 1.0F;
		if (f1 > 78F) {
			f3 = -0.006953F * (90F - f1) * (90F - f1) + 1.0F;
			f3 = this.SMALL_LOBES_SIZE + f3 * this.TOP_LOBE_SIZE;
		} else f3 = this.SMALL_LOBES_SIZE;
		return Math.max(f2, f3);
	}

	protected float baseRCS(Orient orient) {
		float f1 = orient.getAzimut();
		float f = Math.abs(orient.getTangage());
		return this.baseRCS(f1, f);
	}

	protected float baseRCS(float f, float f1) {
		for (; f < 0.0F; f += 360F)
			;
		for (; f > 360F; f -= 360F)
			;
		if (f > 180F) f = 360F - f;
		float f3 = Math.abs(f - 90F);
		float f2 = Math.abs(this.func(f3, f1) + this.func(90F - f3, f1));
		f2 *= this.mult(f, f1);
		return f2;
	}

	float getRCS(Actor actor, Orient orient) {
		float f = this.baseRCS(orient);
		f -= 7F;
		if (Actor.isValid(actor)) {
			float f1 = ((Aircraft) actor).FM.Wingspan * ((Aircraft) actor).FM.Length / this.RCS_DIVISION_FACTOR;
			f += -3.053F / f1 + f1 * 3.152F;
		}
		f += World.Rnd().nextFloat(-2.5F, 2.5F);
		return f;
	}

	protected float R_2_lambda;
	protected float SMALL_LOBES_SIZE;
	protected float SIDE_LOBE_SIZE;
	protected float FRONT_LOBE_SIZE;
	protected float REAR_LOBE_SIZE;
	protected float TOP_LOBE_SIZE;
	protected float RCS_DIVISION_FACTOR;
}
