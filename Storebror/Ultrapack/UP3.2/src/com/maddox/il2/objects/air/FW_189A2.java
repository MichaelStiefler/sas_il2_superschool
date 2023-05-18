package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class FW_189A2 extends Scheme3 implements TypeScout, TypeBomber {

	public static void moveGear(HierMesh var0, float var1) {
		var0.chunkSetAngles("GearL2_D0", 0.0F, 115.0F * var1, 0.0F);
		var0.chunkSetAngles("GearR2_D0", 0.0F, 115.0F * var1, 0.0F);
		var0.chunkSetAngles("GearC3_D0", 0.0F, 100.0F * var1, 0.0F);
		var0.chunkSetAngles("GearC2_D0", 0.0F, 90.0F * var1, 0.0F);
		float var2 = Math.max(-var1 * 1500.0F, -110.0F);
		var0.chunkSetAngles("GearL3_D0", 0.0F, -var2, 0.0F);
		var0.chunkSetAngles("GearL4_D0", 0.0F, -var2, 0.0F);
		var0.chunkSetAngles("GearR3_D0", 0.0F, -var2, 0.0F);
		var0.chunkSetAngles("GearR4_D0", 0.0F, -var2, 0.0F);
	}

	protected void moveGear(float var1) {
		moveGear(this.hierMesh(), var1);
	}

	protected void moveFlap(float var1) {
		float var2 = -40.0F * var1;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, var2, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, var2, 0.0F);
		this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, var2, 0.0F);
	}

	public void msgShot(Shot var1) {
		this.setShot(var1);
		if (var1.chunkName.startsWith("WingLIn")) {
			if (World.Rnd().nextFloat() < 0.048F) { this.FM.AS.setJamBullets(0, 0); }

			if (Aircraft.v1.x < 0.25D && World.Rnd().nextFloat() < 0.25F && World.Rnd().nextFloat(0.01F, 0.121F) < var1.mass) { this.FM.AS.hitTank(var1.initiator, 0, (int) (1.0F + var1.mass * 26.08F)); }
		}

		if (var1.chunkName.startsWith("WingRIn")) {
			if (World.Rnd().nextFloat() < 0.048F) { this.FM.AS.setJamBullets(0, 1); }

			if (Aircraft.v1.x < 0.25D && World.Rnd().nextFloat() < 0.25F && World.Rnd().nextFloat(0.01F, 0.121F) < var1.mass) { this.FM.AS.hitTank(var1.initiator, 1, (int) (1.0F + var1.mass * 26.08F)); }
		}

		if (var1.chunkName.startsWith("Engine1")) {
			if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F) { this.FM.AS.hitEngine(var1.initiator, 0, (int) (1.0F + var1.mass * 20.7F)); }

			if (World.Rnd().nextFloat() < 0.01F) { this.FM.AS.hitEngine(var1.initiator, 0, 5); }
		}

		if (var1.chunkName.startsWith("Engine2")) {
			if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F) { this.FM.AS.hitEngine(var1.initiator, 1, (int) (1.0F + var1.mass * 20.7F)); }

			if (World.Rnd().nextFloat() < 0.01F) { this.FM.AS.hitEngine(var1.initiator, 1, 5); }
		}

		if (var1.chunkName.startsWith("Pilot1")) {
			if (Aircraft.v1.x > -0.5D || var1.power * -Aircraft.v1.x > 12800.0D) {
				this.killPilot(var1.initiator, 0);
				this.FM.setCapableOfBMP(false, var1.initiator);
				if (Aircraft.Pd.z > 0.5D && var1.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
			}

			var1.chunkName = "CF_D0";
		}

		if (var1.chunkName.startsWith("Pilot2")) {
			this.killPilot(var1.initiator, 1);
			if (Aircraft.Pd.z > 0.5D && var1.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }

			var1.chunkName = "CF_D0";
		}

		if (var1.chunkName.startsWith("Pilot3")) {
			this.killPilot(var1.initiator, 2);
			if (Aircraft.Pd.z > 0.5D && var1.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }

			var1.chunkName = "CF_D0";
		}

		if (var1.chunkName.startsWith("Tail1") && World.Rnd().nextFloat() < 0.05F) { this.FM.AS.hitTank(var1.initiator, 0, (int) (1.0F + var1.mass * 26.08F)); }

		if (var1.chunkName.startsWith("Tail2") && World.Rnd().nextFloat() < 0.05F) { this.FM.AS.hitTank(var1.initiator, 1, (int) (1.0F + var1.mass * 26.08F)); }

		if ((this.FM.AS.astateEngineStates[0] == 4 || this.FM.AS.astateEngineStates[1] == 4) && World.Rnd().nextInt(0, 99) < 33) { this.FM.setCapableOfBMP(false, var1.initiator); }

		super.msgShot(var1);
	}

	protected boolean cutFM(int var1, int var2, Actor var3) {
		switch (var1) {
			case 33:
				if (World.Rnd().nextFloat() < 0.66F) { this.FM.AS.hitTank(this, 0, 6); }

				return super.cutFM(34, var2, var3);
			case 36:
				if (World.Rnd().nextFloat() < 0.66F) { this.FM.AS.hitTank(this, 1, 6); }

				return super.cutFM(37, var2, var3);
			default:
				return super.cutFM(var1, var2, var3);
		}
	}

	public void rareAction(float var1, boolean var2) {
		super.rareAction(var1, var2);

		for (int var3 = 1; var3 < 4; ++var3) {
			if (this.FM.getAltitude() < 3000.0F) {
				this.hierMesh().chunkVisible("HMask" + var3 + "_D0", false);
			} else {
				this.hierMesh().chunkVisible("HMask" + var3 + "_D0", this.hierMesh().isChunkVisible("Pilot" + var3 + "_D0"));
			}
		}

	}

	public void doKillPilot(int var1) {
		if (var1 == 1) {
			this.FM.turret[1].bIsOperable = false;
			this.hierMesh().chunkVisible("Turret2A_D0", false);
			this.hierMesh().chunkVisible("Turret2B_D0", false);
			this.hierMesh().chunkVisible("Turret2B_D1", true);
		}

		if (var1 == 2) {
			this.FM.turret[0].bIsOperable = false;
			this.hierMesh().chunkVisible("Turret1A_D0", false);
			this.hierMesh().chunkVisible("Turret1B_D0", false);
			this.hierMesh().chunkVisible("Turret1B_D1", true);
		}

	}

	public void doMurderPilot(int var1) {
		switch (var1) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				break;
			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("HMask2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
		}

	}

	public boolean turretAngles(int var1, float[] var2) {
		boolean var3 = super.turretAngles(var1, var2);
		float var4 = -var2[0];
		float var5 = var2[1];
		switch (var1) {
			case 0:
				if (var5 < 40.0F) {
					var5 = 40.0F;
					var3 = false;
				}

				if (var5 > 96.0F) {
					var5 = 96.0F;
					var3 = false;
				}
				break;
			case 1:
				if (var4 < -75.0F) {
					var4 = -75.0F;
					var3 = false;
				}

				if (var4 > 85.0F) {
					var4 = 85.0F;
					var3 = false;
				}

				if (var5 < 4.0F) {
					var5 = 4.0F;
					var3 = false;
				}

				if (var5 > 80.0F) {
					var5 = 80.0F;
					var3 = false;
				}
		}

		var2[0] = -var4;
		var2[1] = var5;
		return var3;
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

	public void typeBomberUpdate(float var1) {
		if (this.FM.isPlayers()) {
			if (!Main3D.cur3D().isViewOutside()) {
				this.hierMesh().chunkVisible("CF_D0", false);
			} else {
				this.hierMesh().chunkVisible("CF_D0", true);
			}
		}

		if (this.FM.isPlayers()) {
			if (!Main3D.cur3D().isViewOutside()) { this.hierMesh().chunkVisible("CF_D1", false); }

			this.hierMesh().chunkVisible("CF_D2", false);
			this.hierMesh().chunkVisible("CF_D3", false);
		}

		if (this.FM.isPlayers()) {
			if (!Main3D.cur3D().isViewOutside()) {
				this.hierMesh().chunkVisible("Blister1_D0", false);
			} else {
				this.hierMesh().chunkVisible("Blister1_D0", true);
			}

			Point3d var2 = World.getPlayerAircraft().pos.getAbsPoint();
			if (var2.z - World.land().HQ(var2.x, var2.y) < 0.009999999776482582D) { this.hierMesh().chunkVisible("CF_D0", true); }

			if (this.FM.AS.bIsAboutToBailout) { this.hierMesh().chunkVisible("Blister1_D0", false); }
		}

	}

	public void typeBomberReplicateToNet(NetMsgGuaranted var1) throws IOException {
	}

	public void typeBomberReplicateFromNet(NetMsgInput var1) throws IOException {
	}

	static {
		Class clazz = FW_189A2.class;
		new NetAircraft.SPAWN(clazz);
		Property.set(clazz, "iconFar_shortClassName", "Fw-189");
		Property.set(clazz, "meshName", "3do/plane/Fw-189A-2/hier.him");
		Property.set(clazz, "PaintScheme", new PaintSchemeBMPar03());
		Property.set(clazz, "originCountry", PaintScheme.countryGermany);
		Property.set(clazz, "yearService", 1941.6F);
		Property.set(clazz, "yearExpired", 1948.0F);
		Property.set(clazz, "FlightModel", "FlightModels/Fw-189A-2.fmd");
		Property.set(clazz, "cockpitClass", new Class[] { CockpitFw189.class, CockpitFw189_RGunner.class, CockpitFW189_BGunner.class });
		Aircraft.weaponTriggersRegister(clazz, new int[] { 0, 0, 3, 3, 3, 3, 10, 10, 11, 11 });
		Aircraft.weaponHooksRegister(clazz, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06" });
	}
}
