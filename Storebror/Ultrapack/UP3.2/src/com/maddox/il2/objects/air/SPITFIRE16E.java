package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SPITFIRE16E extends SPITFIRE9 implements TypeFighterAceMaker {
	public int     k14Mode;
	public int     k14WingspanType;
	public float   k14Distance;
	public boolean bHasBlister;
	private float  fMaxKMHSpeedForOpenCanopy;

	public SPITFIRE16E() {
		this.k14Mode = 0;
		this.k14WingspanType = 0;
		this.k14Distance = 200.0f;
		this.bHasBlister = true;
		this.fMaxKMHSpeedForOpenCanopy = 250.0f;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (!(this.getGunByHookName("_CANNON03") instanceof GunEmpty)) { this.hierMesh().chunkVisible("Cannon_L", true); }
		if (!(this.getGunByHookName("_CANNON04") instanceof GunEmpty)) { this.hierMesh().chunkVisible("Cannon_R", true); }
	}

	public void moveCockpitDoor(final float n) {
		this.resetYPRmodifier();
		Aircraft.xyz[1] = Aircraft.cvt(n, 0.01f, 0.99f, 0.0f, 0.55f);
		this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
		final float n2 = (float) Math.sin(Aircraft.cvt(n, 0.01f, 0.99f, 0.0f, 3.141593f));
		this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0f, 0.0f, 9.0f * n2);
		this.hierMesh().chunkSetAngles("Head1_D0", 12.0f * n2, 0.0f, 0.0f);
		if (Config.isUSE_RENDER()) {
			if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) { Main3D.cur3D().cockpits[0].onDoorMoved(n); }
			this.setDoorSnd(n);
		}
	}

	public void update(final float n) {
		super.update(n);
		if (super.FM.CT.getCockpitDoor() > 0.2 && this.bHasBlister && super.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy && this.hierMesh().chunkFindCheck("Blister1_D0") != -1) {
			try {
				if (this == World.getPlayerAircraft()) { ((CockpitSpit16E) Main3D.cur3D().cockpitCur).removeCanopy(); }
			} catch (Exception ex) {}
			this.hierMesh().hideSubTrees("Blister1_D0");
			final Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
			wreckage.collide(false);
			final Vector3d speed = new Vector3d();
			speed.set(super.FM.Vwld);
			wreckage.setSpeed(speed);
			this.bHasBlister = false;
			super.FM.CT.bHasCockpitDoorControl = false;
			super.FM.setGCenter(-0.3f);
		}
	}

	public boolean typeFighterAceMakerToggleAutomation() {
		++this.k14Mode;
		if (this.k14Mode > 2) { this.k14Mode = 0; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
		return true;
	}

	public void typeFighterAceMakerAdjDistanceReset() {
	}

	// -------------------------------------------------------------------------------------------------------
	// TODO: skylla: gyro-gunsight distance HUD log (for details please see
	// P_51D25NA.class):

	public void typeFighterAceMakerAdjDistancePlus() {
		this.adjustK14AceMakerDistance(+10.0f);
	}

	public void typeFighterAceMakerAdjDistanceMinus() {
		this.adjustK14AceMakerDistance(-10.0f);
	}

	private void adjustK14AceMakerDistance(float f) {
		this.k14Distance += f;
		if (this.k14Distance > 730.0f) {
			this.k14Distance = 730.0f;
		} else if (this.k14Distance < 160.0f) { this.k14Distance = 160.0f; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int) this.k14Distance + "m");
	}
	/*
	 * public void typeFighterAceMakerAdjDistancePlus() { this.k14Distance += 10.0f; if (this.k14Distance > 800.0f) { this.k14Distance = 800.0f; } HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc"); }
	 *
	 * public void typeFighterAceMakerAdjDistanceMinus() { this.k14Distance -= 10.0f; if (this.k14Distance < 200.0f) { this.k14Distance = 200.0f; } HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec"); }
	 */
	// -------------------------------------------------------------------------------------------------------

	public void typeFighterAceMakerAdjSideslipReset() {
	}

	public void typeFighterAceMakerAdjSideslipPlus() {
		--this.k14WingspanType;
		if (this.k14WingspanType < 0) { this.k14WingspanType = 0; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
	}

	public void typeFighterAceMakerAdjSideslipMinus() {
		++this.k14WingspanType;
		if (this.k14WingspanType > 9) { this.k14WingspanType = 9; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
	}

	public void typeFighterAceMakerReplicateToNet(final NetMsgGuaranted netMsgGuaranted) throws IOException {
		netMsgGuaranted.writeByte(this.k14Mode);
		netMsgGuaranted.writeByte(this.k14WingspanType);
		netMsgGuaranted.writeFloat(this.k14Distance);
	}

	public void typeFighterAceMakerReplicateFromNet(final NetMsgInput netMsgInput) throws IOException {
		this.k14Mode = netMsgInput.readByte();
		this.k14WingspanType = netMsgInput.readByte();
		this.k14Distance = netMsgInput.readFloat();
	}

	static {
		final Class class1 = SPITFIRE16E.class;
		new SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Spit");
		Property.set(class1, "meshName", "3DO/Plane/SpitfireMkXVIe(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "meshName_gb", "3DO/Plane/SpitfireMkXVIe(Multi1)/hier.him");
		Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar04());
		Property.set(class1, "yearService", 1944.0f);
		Property.set(class1, "yearExpired", 1946.5f);
		Property.set(class1, "FlightModel", "FlightModels/SpitfireXVI.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitSpit16E.class });
		Property.set(class1, "LOSElevation", 0.5926f);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 1, 9, 9, 9, 3, 3, 9, 3, 2, 2 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev08", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev01",
				"_ExternalBomb01", "_ExternalRock01", "_ExternalRock02" });
	}
}
