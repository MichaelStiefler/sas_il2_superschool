// Source File Name: FuelTank
// Author:		   western0221
// Last Modified by: western0221 2016-Aug.-21
// add functions of checkfuel, refuel, checkFreeTankSpace.

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.*;


public class FuelTank extends Bomb {

	public FuelTank() {
		Class class1 = getClass();
		fill(Property.floatValue(class1, "massa", 0.0F));
	}

	protected void doExplosion(Actor actor, String s) {
		MsgDestroy.Post(Time.current(), this);
		pos.getTime(Time.current(), p);
		if (World.land().isWater(p.x, p.y))
			Explosions.WreckageDrop_Water(p);
	}

	protected void fill(float f) {
		setName("_fueltank_");
		M = f;
		Fuel = f * 0.9F;
		maxFuel = Fuel;
	}

	public float getFuel(float f) {
		if (f > Fuel) f = Fuel;
		Fuel -= f;
		M -= f;
		return f;
	}

	public float checkFuel() {
		return Fuel;
	}

	public float checkFreeTankSpace() {
		return maxFuel - Fuel;
	}

	public boolean doRefuel(float f) {
		if (f + Fuel > maxFuel) return false;
		Fuel += f;
		M += f;
		return true;
	}

	private float Fuel;
	private float maxFuel;
	private static Point3d p = new Point3d();

}
