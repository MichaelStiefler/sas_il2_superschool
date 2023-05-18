package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ME_262A2NJ extends ME_262B implements TypeX4Carrier {

	public ME_262A2NJ() {
		this.bToFire = false;
		this.tX4Prev = 0L;
		this.deltaAzimuth = 0.0F;
		this.deltaTangage = 0.0F;
	}

	public void moveCockpitDoor(float f) {
		this.resetYPRmodifier();
		this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(f); }
			this.setDoorSnd(f);
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode() || !flag || !(this.FM instanceof Pilot)) { return; }
		Pilot pilot = (Pilot) this.FM;
		if (pilot.get_maneuver() == 63 && ((Maneuver) pilot).target != null) {
			Point3d point3d = new Point3d(((FlightModelMain) ((Maneuver) pilot).target).Loc);
			point3d.sub(this.FM.Loc);
			this.FM.Or.transformInv(point3d);
			if ((point3d.x > 4000D && point3d.x < 5500D || point3d.x > 100D && point3d.x < 5000D && World.Rnd().nextFloat() < 0.33F) && Time.current() > this.tX4Prev + 10000L) {
				this.bToFire = true;
				this.tX4Prev = Time.current();
			}
		}
	}

	public void typeX4CAdjSidePlus() {
		this.deltaAzimuth = 1.0F;
	}

	public void typeX4CAdjSideMinus() {
		this.deltaAzimuth = -1F;
	}

	public void typeX4CAdjAttitudePlus() {
		this.deltaTangage = 1.0F;
	}

	public void typeX4CAdjAttitudeMinus() {
		this.deltaTangage = -1F;
	}

	public void typeX4CResetControls() {
		this.deltaAzimuth = this.deltaTangage = 0.0F;
	}

	public float typeX4CgetdeltaAzimuth() {
		return this.deltaAzimuth;
	}

	public float typeX4CgetdeltaTangage() {
		return this.deltaTangage;
	}

	public void doMurderPilot(int i) {
		switch (i) {
			default:
				break;

			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				if (!this.FM.AS.bIsAboutToBailout) { this.hierMesh().chunkVisible("Gore1_D0", true); }
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Head2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				if (!this.FM.AS.bIsAboutToBailout) { this.hierMesh().chunkVisible("Gore3_D0", true); }
				break;
		}
	}

	public void update(float f) {
		if (this.FM.isPlayers() && !Main3D.cur3D().isViewOutside()) {
			this.hierMesh().chunkVisible("Blister1_D0", false);
		} else {
			this.hierMesh().chunkVisible("Blister1_D0", true);
		}
		if (this.FM.AS.bIsAboutToBailout) { this.hierMesh().chunkVisible("Blister1_D0", false); }
		super.update(f);
	}

	public boolean bToFire;
	private long   tX4Prev;
	private float  deltaAzimuth;
	private float  deltaTangage;

	static {
		Class class1 = ME_262A2NJ.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Me 262");
		Property.set(class1, "meshName", "3DO/Plane/Me-262A-2NJ/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1944.1F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Me-262B-1a.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitME_262NJ.class, CockpitME_262NJR.class });
		Property.set(class1, "LOSElevation", 0.7498F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03",
						"_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15",
						"_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev01", "_ExternalDev02", "_ExternalRock25",
						"_ExternalRock25", "_ExternalRock26", "_ExternalRock26", "_ExternalDev07", "_ExternalDev08" });
	}
}
