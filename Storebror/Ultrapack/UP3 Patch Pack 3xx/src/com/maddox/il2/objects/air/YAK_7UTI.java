package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class YAK_7UTI extends YAK implements TypeFighter, TypeTNBFighter, TypeScout {

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		Aircraft.xyz[0] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.58F);
		this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		if (Config.isUSE_RENDER()) {
			if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
				Main3D.cur3D().cockpits[0].onDoorMoved(f);
			}
			this.setDoorSnd(f);
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.CT.bHasCockpitDoorControl = true;
		this.FM.CT.dvCockpitDoor = 0.75F;
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		final float f1 = Math.max(-f * 1500F, -60F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
	}

	public void update(float f) {
		this.hierMesh().chunkSetAngles("Water_luk", 0.0F, 12F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		this.hierMesh().chunkSetAngles("Oil_luk", 0.0F, 24F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
		super.update(f);
	}

	static {
		final Class class1 = YAK_7UTI.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Yak");
		Property.set(class1, "meshName", "3DO/Plane/Yak-7UTI/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_7UTIper.class, CockpitYAK_7Vzad.class });
		Property.set(class1, "FlightModel", "FlightModels/Yak-7uti.fmd");
		Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
	}
}
