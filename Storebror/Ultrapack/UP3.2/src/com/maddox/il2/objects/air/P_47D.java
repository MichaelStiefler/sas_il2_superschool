package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class P_47D extends P_47 implements TypeFighterAceMaker {
	public int   k14Mode;
	public int   k14WingspanType;
	public float k14Distance;

	public P_47D() {
		this.k14Mode = 0;
		this.k14WingspanType = 0;
		this.k14Distance = 200.0f;
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
		} else if (this.k14Distance < 180.0f) { this.k14Distance = 180.0f; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int) this.k14Distance + "m");
	}

	/*
	 * old code: public void typeFighterAceMakerAdjDistancePlus() { this.k14Distance += 10.0f; if (this.k14Distance > 800.0f) { this.k14Distance = 800.0f; } HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc"); }
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
		final Class class1 = P_47D.class;
		new SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "P-47");
		Property.set(class1, "meshName", "3DO/Plane/P-47D(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "meshName_us", "3DO/Plane/P-47D(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1944.0f);
		Property.set(class1, "yearExpired", 1947.5f);
		Property.set(class1, "FlightModel", "FlightModels/P-47D-27_late.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D25.class });
		Property.set(class1, "LOSElevation", 1.1104f);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalDev01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev02",
				"_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
	}
}
