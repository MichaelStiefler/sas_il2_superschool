package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class He45 extends Scheme1 implements TypeScout, TypeBomber, TypeStormovik {

	public He45() {
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
	}

	protected void moveAileron(float f) {
		this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneLrod1_D0", 0.0F, 30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneLrod2_D0", 0.0F, 30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneRrod1_D0", 0.0F, 30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneRrod2_D0", 0.0F, 30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, -30F * f, 0.0F);
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 1:
				this.FM.turret[0].bIsOperable = false;
				break;
		}
	}

	protected void nextDMGLevel(String s, int i, Actor actor) {
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	protected void nextCUTLevel(String s, int i, Actor actor) {
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers()) { bChangedPit = true; }
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				break;
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
		}
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int i;
			if (s.endsWith("a")) {
				byte0 = 1;
				i = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				i = s.charAt(6) - 49;
			} else {
				i = s.charAt(5) - 49;
			}
			this.hitFlesh(i, shot, byte0);
		}
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		if (af[0] < -142F) {
			af[0] = -142F;
			flag = false;
		} else if (af[0] > 142F) {
			af[0] = 142F;
			flag = false;
		}
		if (af[1] > 45F) {
			af[1] = 45F;
			flag = false;
		}
		if (!flag) { return false; }
		float f = Math.abs(af[0]);
		if (f < 2.5F && af[1] < 20.8F) {
			af[1] = 20.8F;
			return false;
		}
		if (f < 21F && af[1] < 16.1F) {
			af[1] = 16.1F;
			return false;
		}
		if (f < 41F && af[1] < -8.5F) {
			af[1] = -8.5F;
			return false;
		}
		if (f < 103F && af[1] < -45F) {
			af[1] = -45F;
			return false;
		}
		if (f < 180F && af[1] < -7.8F) {
			af[1] = -7.8F;
			return false;
		} else {
			return true;
		}
	}

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
	}

	public void typeBomberAdjDistancePlus() {
	}

	public void typeBomberAdjDistanceMinus() {
	}

	public void typeBomberAdjSideslipReset() {
	}

	public void typeBomberAdjSideslipPlus() {
	}

	public void typeBomberAdjSideslipMinus() {
	}

	public void typeBomberAdjAltitudeReset() {
	}

	public void typeBomberAdjAltitudePlus() {
	}

	public void typeBomberAdjAltitudeMinus() {
	}

	public void typeBomberAdjSpeedReset() {
	}

	public void typeBomberAdjSpeedPlus() {
	}

	public void typeBomberAdjSpeedMinus() {
	}

	public void typeBomberUpdate(float f) {
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
	}

	public static boolean bChangedPit = false;

	static {
		Class class1 = He45.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He45");
		Property.set(class1, "meshName", "3DO/Plane/He45/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
		Property.set(class1, "meshName_it", "3DO/Plane/He45/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1943F);
		Property.set(class1, "FlightModel", "FlightModels/He45.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHe45.class, CockpitHe45_TGunner.class });
		Property.set(class1, "LOSElevation", 0.742F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06" });
	}
}
