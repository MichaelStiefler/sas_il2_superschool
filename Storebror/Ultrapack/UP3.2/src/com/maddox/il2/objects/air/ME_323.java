package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class ME_323 extends Scheme7 implements TypeTransport, TypeBomber {

	public ME_323() {
	}

	protected void moveFlap(float f) {
		float f1 = -50F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 1:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				break;

			case 2:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 3:
				if (f < -45F) {
					f = -45F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 4:
				if (f1 < -3F) {
					f1 = -3F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 5:
				if (f1 < -3F) {
					f1 = -3F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 6:
				if (f < -30F) {
					f = -30F;
					flag = false;
				}
				if (f > 30F) {
					f = 30F;
					flag = false;
				}
				if (f1 < 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xengine")) {
			int i = s.charAt(7) - 49;
			if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.hitEngine(shot.initiator, i, 1); }
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int j;
			if (s.endsWith("a")) {
				byte0 = 1;
				j = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				j = s.charAt(6) - 49;
			} else {
				j = s.charAt(5) - 49;
			}
			this.hitFlesh(j, shot, byte0);
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			case 33:
				return super.cutFM(34, j, actor);

			case 36:
				return super.cutFM(37, j, actor);

			case 3:
				return false;

			case 4:
				return false;

			case 5:
				return false;

			case 6:
				return false;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		this.FM.Gears.lgear = true;
		this.FM.Gears.rgear = true;
		super.update(f);
	}

	public void doKillPilot(int i) {
		switch (i) {
			case 2:
				this.FM.turret[6].bIsOperable = false;
				break;

			case 3:
				this.FM.turret[4].bIsOperable = false;
				break;

			case 4:
				this.FM.turret[5].bIsOperable = false;
				break;

			case 5:
				this.FM.turret[0].bIsOperable = false;
				break;

			case 6:
				this.FM.turret[1].bIsOperable = false;
				break;

			case 7:
				this.FM.turret[2].bIsOperable = false;
				break;

			case 8:
				this.FM.turret[3].bIsOperable = false;
				break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			default:
				break;

			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				if (this.hierMesh().isChunkVisible("Blister1_D0")) { this.hierMesh().chunkVisible("Gore1_D0", true); }
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				if (this.hierMesh().isChunkVisible("Blister1_D0")) { this.hierMesh().chunkVisible("Gore2_D0", true); }
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				break;

			case 3:
				this.hierMesh().chunkVisible("Pilot4_D0", false);
				this.hierMesh().chunkVisible("Pilot4_D1", true);
				break;

			case 4:
				this.hierMesh().chunkVisible("Pilot5_D0", false);
				this.hierMesh().chunkVisible("Pilot5_D1", true);
				break;
		}
	}

	public void msgShot(Shot shot) {
		this.setShot(shot);
		if (!"CF_D3".equals(shot.chunkName)) { super.msgShot(shot); }
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

	static {
		Class class1 = ME_323.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Me-323");
		Property.set(class1, "meshName", "3Do/Plane/Me-323/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Me-323.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitME_323.class, CockpitME_323_FRGunner.class, CockpitME_323_FLGunner.class, CockpitME_323_TGunner.class, CockpitME_323_TLGunner.class, CockpitME_323_TRGunner.class,
				CockpitME_323_RGunner.class, CockpitME_323_LGunner.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15, 16, 3 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_ExternalBomb01" });
	}
}
