package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B_17D extends B_17 implements TypeBomber {

	public B_17D() {
		this.calibDistance = 0.0F;
		this.bSightAutomation = false;
		this.bSightBombDump = false;
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 3000F;
		this.fSightCurSpeed = 200F;
		this.fSightCurReadyness = 0.0F;
	}

	protected void moveBayDoor(float f) {
		this.hierMesh().chunkSetAngles("Bay01_D0", 0.0F, -85F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay02_D0", 0.0F, -85F * f, 0.0F);
	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
			case 2:
				this.FM.turret[0].setHealth(f);
				this.FM.turret[1].setHealth(f);
				this.FM.turret[2].setHealth(f);
				break;

			case 6:
				this.FM.turret[3].setHealth(f);
				break;

			case 7:
				this.FM.turret[4].setHealth(f);
				break;

			case 8:
				this.FM.turret[5].setHealth(f);
				break;
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.AS.wantBeaconsNet(true);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -11F) {
					f = -11F;
					flag = false;
				}
				if (f > 11F) {
					f = 11F;
					flag = false;
				}
				if (f1 < -14F) {
					f1 = -14F;
					flag = false;
				}
				if (f1 > 14F) {
					f1 = 14F;
					flag = false;
				}
				break;

			case 1:
				if (f < -26F) {
					f = -26F;
					flag = false;
				}
				if (f > 0.0F) {
					f = 0.0F;
					flag = false;
				}
				if (f1 < -14F) {
					f1 = -14F;
					flag = false;
				}
				if (f1 > 14F) {
					f1 = 14F;
					flag = false;
				}
				break;

			case 2:
				if (f < -11F) {
					f = -11F;
					flag = false;
				}
				if (f > 11F) {
					f = 11F;
					flag = false;
				}
				if (f1 < -25F) {
					f1 = -25F;
					flag = false;
				}
				if (f1 > 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				break;

			case 3:
				if (f < -12F) {
					f = -12F;
					flag = false;
				}
				if (f > 12F) {
					f = 12F;
					flag = false;
				}
				if (f1 < -45F) {
					f1 = -45F;
					flag = false;
				}
				if (f1 > 2.0F) {
					f1 = 2.0F;
					flag = false;
				}
				break;

			case 4:
				if (f < -41F) {
					f = -41F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				break;

			case 5:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 53F) {
					f = 53F;
					flag = false;
				}
				if (f1 < -10F) {
					f1 = -10F;
					flag = false;
				}
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	private static final float toMeters(float f) {
		return 0.3048F * f;
	}

	private static final float toMetersPerSecond(float f) {
		return 0.4470401F * f;
	}

	public boolean typeBomberToggleAutomation() {
		this.bSightAutomation = !this.bSightAutomation;
		this.bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
		return this.bSightAutomation;
	}

	public void typeBomberAdjDistanceReset() {
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		this.fSightCurForwardAngle++;
		if (this.fSightCurForwardAngle > 85F) { this.fSightCurForwardAngle = 85F; }
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) { this.typeBomberToggleAutomation(); }
	}

	public void typeBomberAdjDistanceMinus() {
		this.fSightCurForwardAngle--;
		if (this.fSightCurForwardAngle < 0.0F) { this.fSightCurForwardAngle = 0.0F; }
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) { this.typeBomberToggleAutomation(); }
	}

	public void typeBomberAdjSideslipReset() {
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		this.fSightCurSideslip += 0.1F;
		if (this.fSightCurSideslip > 3F) { this.fSightCurSideslip = 3F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjSideslipMinus() {
		this.fSightCurSideslip -= 0.1F;
		if (this.fSightCurSideslip < -3F) { this.fSightCurSideslip = -3F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 3000F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 50F;
		if (this.fSightCurAltitude > 50000F) { this.fSightCurAltitude = 50000F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 50F;
		if (this.fSightCurAltitude < 1000F) { this.fSightCurAltitude = 1000F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 200F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 10F;
		if (this.fSightCurSpeed > 450F) { this.fSightCurSpeed = 450F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 10F;
		if (this.fSightCurSpeed < 100F) { this.fSightCurSpeed = 100F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
			this.fSightCurReadyness -= 0.0666666F * f;
			if (this.fSightCurReadyness < 0.0F) { this.fSightCurReadyness = 0.0F; }
		}
		if (this.fSightCurReadyness < 1.0F) {
			this.fSightCurReadyness += 0.0333333F * f;
		} else if (this.bSightAutomation) {
			this.fSightCurDistance -= toMetersPerSecond(this.fSightCurSpeed) * f;
			if (this.fSightCurDistance < 0.0F) {
				this.fSightCurDistance = 0.0F;
				this.typeBomberToggleAutomation();
			}
			this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / toMeters(this.fSightCurAltitude)));
			this.calibDistance = toMetersPerSecond(this.fSightCurSpeed) * floatindex(Aircraft.cvt(toMeters(this.fSightCurAltitude), 0.0F, 7000F, 0.0F, 7F), calibrationScale);
			if (this.fSightCurDistance < this.calibDistance + toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(toMeters(this.fSightCurAltitude) * (2F / 9.81F))) { this.bSightBombDump = true; }
			if (this.bSightBombDump) {
				if (this.FM.isTick(3, 0)) {
					if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
						this.FM.CT.WeaponControl[3] = true;
						HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
					}
				} else {
					this.FM.CT.WeaponControl[3] = false;
				}
			}
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(this.fSightCurDistance);
		netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		int i = netmsginput.readUnsignedByte();
		this.bSightAutomation = (i & 1) != 0;
		this.bSightBombDump = (i & 2) != 0;
		this.fSightCurDistance = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
		this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
		this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
	}

	public static boolean bChangedPit        = false;
	private float         calibDistance;
	private boolean       bSightAutomation;
	private boolean       bSightBombDump;
	private float         fSightCurDistance;
	public float          fSightCurForwardAngle;
	public float          fSightCurSideslip;
	public float          fSightCurAltitude;
	public float          fSightCurSpeed;
	public float          fSightCurReadyness;
	static final float    calibrationScale[] = { 0.0F, 0.2F, 0.4F, 0.66F, 0.86F, 1.05F, 1.2F, 1.6F };

	static {
		Class class1 = B_17D.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "B-17");
		Property.set(class1, "meshName", "3DO/Plane/B-17D(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
		Property.set(class1, "meshName_us", "3DO/Plane/B-17D(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1941F);
		Property.set(class1, "yearExpired", 2800.9F);
		Property.set(class1, "FlightModel", "FlightModels/B-17D.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitB17D.class, CockpitB17D_Bombardier.class });
		weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 15, 3, 3 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn02" });
	}
}
