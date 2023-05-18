package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class BF_110C4 extends BF_110 {

	public BF_110C4() {
	}

	public void update(float f) {
		this.afterburnerhud();
		super.update(f);
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void afterburnerhud() {
		if (this.FM.isPlayers() && this.FM.EI.engines[0].getControlAfterburner()) { HUD.logRightBottom("Start- und Notleistung ENABLED!"); }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = BF_110C4.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Bf-110");
		Property.set(class1, "meshName", "3DO/Plane/Bf-110C-4/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "yearService", 1940F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Bf-110C-4.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitBF_110C4.class, CockpitBF_110Early_Gunner.class });
		Property.set(class1, "LOSElevation", 0.66895F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 10 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_MGUN05" });
	}
}
