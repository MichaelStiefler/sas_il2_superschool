package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Ca_311 extends CA_Twins2 implements TypeBomber, TypeStormovik, TypeTransport {

	public Ca_311() {
		this.bChangedExts = false;
		bChangedPit = true;
		this.fSightSetForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 850F;
		this.fSightCurSpeed = 250F;
		this.fSightCurReadyness = 0.0F;
	}

	public boolean typeBomberToggleAutomation() {
		return false;
	}

	public void typeBomberAdjDistanceReset() {
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		this.fSightCurForwardAngle++;
		if (this.fSightCurForwardAngle > 75F) this.fSightCurForwardAngle = 75F;
	}

	public void typeBomberAdjDistanceMinus() {
		this.fSightCurForwardAngle--;
		if (this.fSightCurForwardAngle < -0F) this.fSightCurForwardAngle = -0F;
	}

	public void typeBomberAdjSideslipReset() {
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		this.fSightCurSideslip += 0.1D;
		if (this.fSightCurSideslip > 3F) this.fSightCurSideslip = 3F;
	}

	public void typeBomberAdjSideslipMinus() {
		this.fSightCurSideslip -= 0.1F;
		if (this.fSightCurSideslip < -3F) this.fSightCurSideslip = -3F;
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 850F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 10F;
		if (this.fSightCurAltitude > 6000F) this.fSightCurAltitude = 6000F;
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 10F;
		if (this.fSightCurAltitude < 850F) this.fSightCurAltitude = 850F;
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 250F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 10F;
		if (this.fSightCurSpeed > 300F) this.fSightCurSpeed = 300F;
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 10F;
		if (this.fSightCurSpeed < 250F) this.fSightCurSpeed = 250F;
	}

	public void typeBomberUpdate(float f) {
		double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
		d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
		this.fSightSetForwardAngle = (float) Math.atan(d / this.fSightCurAltitude);
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeFloat(this.fSightCurSpeed);
		netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
		netmsgguaranted.writeFloat(this.fSightCurSideslip);
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readFloat();
		this.fSightCurSideslip = netmsginput.readFloat();
	}

	public boolean        bChangedExts;
	public static boolean bChangedPit = false;
	public float          fSightSetForwardAngle;
	public float          fSightCurForwardAngle;
	public float          fSightCurSideslip;
	public float          fSightCurAltitude;
	public float          fSightCurSpeed;
	public float          fSightCurReadyness;

	static {
		Class class1 = Ca_311.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Ca311");
		Property.set(class1, "meshName", "3DO/Plane/Ca-311(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
		Property.set(class1, "yearService", 1937F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/Ca311.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitCa_311.class, CockpitCa311_Bombardier.class });
		Property.set(class1, "LOSElevation", 0.73425F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 11, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN07", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10",
				"_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14" });
	}
}
