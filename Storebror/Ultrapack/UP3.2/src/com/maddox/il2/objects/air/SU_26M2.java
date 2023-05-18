package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class SU_26M2 extends IAR_8X implements TypeFighter, TypeTNBFighter {

	public SU_26M2() {
		this.kangle = 0.0F;
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public void moveCockpitDoor(float f) {
		this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 110F * f);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(f); }
			this.setDoorSnd(f);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		if (this.FM.CT.getGear() >= 0.98F) { this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F); }
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Stvorka1_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka2_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka3_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka4_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka5_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka6_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka7_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka8_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka9_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka10_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka11_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka12_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka13_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka14_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka15_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("Stvorka16_D0", 0.0F, -73F * this.kangle, 0.0F);
		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		super.update(f);
	}

	private float         kangle;
	public static boolean bChangedPit = false;

	static {
		Class class1 = SU_26M2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Su-26");
		Property.set(class1, "meshName", "3DO/Plane/SU_26/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(class1, "yearService", 1989F);
		Property.set(class1, "yearExpired", 2050);
		Property.set(class1, "cockpitClass", new Class[] { CockpitSU26SAS.class });
		Property.set(class1, "FlightModel", "FlightModels/Su-26.fmd");
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
	}
}
