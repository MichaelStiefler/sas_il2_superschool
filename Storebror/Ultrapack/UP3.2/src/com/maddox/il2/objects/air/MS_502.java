package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class MS_502 extends Scheme1 implements TypeScout, TypeTransport {

	public MS_502() {
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
	}

	protected void moveGear(float f) {
	}

	public void moveWheelSink() {
		this.resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 0.5F);
		this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 5F);
		this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, floatindex(f, gearL2), 0.0F);
		this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, floatindex(f, gearL4), 0.0F);
		this.hierMesh().chunkSetAngles("GearL5_D0", 0.0F, floatindex(f, gearL5), 0.0F);
		Aircraft.xyz[2] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 0.5F);
		this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
		f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 5F);
		this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -floatindex(f, gearL2), 0.0F);
		this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, -floatindex(f, gearL4), 0.0F);
		this.hierMesh().chunkSetAngles("GearR5_D0", 0.0F, -floatindex(f, gearL5), 0.0F);
	}

	protected void moveFlap(float f) {
		float f1 = -60F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public void msgShot(Shot shot) {
		this.setShot(shot);
		if (shot.chunkName.startsWith("WingLMid") && World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass) { this.FM.AS.hitTank(shot.initiator, 0, (int) (1.0F + shot.mass * 18.95F * 2.0F)); }
		if (shot.chunkName.startsWith("WingRMid") && World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass) { this.FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + shot.mass * 18.95F * 2.0F)); }
		if (shot.chunkName.startsWith("Engine")) {
			if (World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass) { this.FM.AS.hitEngine(shot.initiator, 0, 1); }
			if (Aircraft.v1.z > 0.0D && World.Rnd().nextFloat() < 0.12F) {
				this.FM.AS.setEngineDies(shot.initiator, 0);
				if (shot.mass > 0.1F) { this.FM.AS.hitEngine(shot.initiator, 0, 5); }
			}
			if (Aircraft.v1.x < 0.1D && World.Rnd().nextFloat() < 0.57F) { this.FM.AS.hitOil(shot.initiator, 0); }
		}
		if (shot.chunkName.startsWith("Pilot1")) {
			this.killPilot(shot.initiator, 0);
			this.FM.setCapableOfBMP(false, shot.initiator);
			if (Aircraft.Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) { HUD.logCenter("H E A D S H O T"); }
		} else if (this.FM.AS.astateEngineStates[0] == 4 && World.Rnd().nextInt(0, 99) < 33) { this.FM.setCapableOfBMP(false, shot.initiator); }
		super.msgShot(shot);
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 34:
				return super.cutFM(35, j, actor);

			case 37:
				return super.cutFM(38, j, actor);
		}
		return super.cutFM(i, j, actor);
	}

	public void doKillPilot(int i) {
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				if (!this.FM.AS.bIsAboutToBailout) { this.hierMesh().chunkVisible("Gore1_D0", true); }
				break;
		}
	}

	private static final float gearL2[] = { 0.0F, 1.0F, 2.0F, 2.9F, 3.2F, 3.35F };
	private static final float gearL4[] = { 0.0F, 7.5F, 15F, 22F, 29F, 35.5F };
	private static final float gearL5[] = { 0.0F, 1.5F, 4F, 7.5F, 10F, 11.5F };

	static {
		Class class1 = MS_502.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MS-502");
		Property.set(class1, "meshName", "3do/plane/MS-502/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1956F);
		Property.set(class1, "FlightModel", "FlightModels/Fi-156B-2.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMS_502.class });
		Property.set(class1, "LOSElevation", 0.9119F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
	}
}
