/*Modified Squares class for the SAS Engine Mod*/

/*
 * Changelog:
 * 2016.Apr.11: reading Pylons' mass property
 * 2016.Aug.22: bFuelTanksDropped
 * 2017.Nov.18: No-Jettison fueltank
 * 2020.Apr.25: reflecting external fuel tank's fuel consumed weight at parasiteMass
 */

package com.maddox.il2.fm;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.SectFile;

public class Mass {

	public Mass() {
		bFuelTanksLoaded = false;
		nitro = 0.0F;
		maxNitro = 1.0F;
		pylonCoeff = 0.0F;
		pylonCoeffB = 0.0F;
		pylonCoeffR = 0.0F;
		fuelCoeff = 0.0F;
	}

	public void load(SectFile sectfile, FlightModelMain flightmodelmain) {
		String s = "Mass";
		String s1 = "Critical Mass in " + sectfile.toString();
		FM = flightmodelmain;
		float f = sectfile.get(s, "Empty", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		massEmpty = f;
		f = sectfile.get(s, "Oil", -1F);
		if (f == -1F)
			throw new RuntimeException(s1);
		massEmpty += f;
		f = sectfile.get("Aircraft", "Crew", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		massEmpty += f * 90F;
		referenceWeight = massEmpty;
		f = sectfile.get(s, "TakeOff", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		maxWeight = f;
		f = sectfile.get(s, "Fuel", 0.0F);
		if (f == 0.0F) {
			throw new RuntimeException(s1);
		} else {
			maxFuel = f;
			mass = massEmpty;
			fuel = maxFuel;
			f = sectfile.get(s, "Nitro", 0.0F);
			maxNitro = nitro = f;
			referenceWeight += fuel + maxNitro;
			return;
		}
	}

	public void onFuelTanksChanged() {
		bFuelTanksLoaded = true;

		// Once release the array
		fuelTanks = null;

		fuelTanks = FM.CT.getFuelTanks();
		if (fuelTanks.length == 0 || fuelTanks[0] == null)
			bFuelTanksDropped = true;
	}

	public boolean requestFuel(float f) {
		mass = massEmpty + fuel + nitro + parasiteMass;
		if (!bFuelTanksLoaded) {
			bFuelTanksDropped = false;
			FM.CT.bDroptanksDropped = false;
			fuelTanks = FM.CT.getFuelTanks();
			bFuelTanksLoaded = true;
			if (fuelTanks.length == 0) {
				bFuelTanksDropped = true;
				FM.CT.bDroptanksDropped = true;
			}
		}
		if (fuelTanks.length != 0) {
			fuelCoeff = 1.0F;
			float f1 = 0.0F;
			for (int i = 0; i < fuelTanks.length; i++)
				f1 += fuelTanks[i].getFuel(f / (float)fuelTanks.length);

			if (f1 > 0.0F)
				return true;
		} else {
			fuelCoeff = 0.0F;
		}
		fuel -= f;
		if (fuel < 0.0F) {
			fuel = 0.0F;
			return false;
		} else {
			return true;
		}
	}

	public boolean requestNitro(float f) {
		mass = massEmpty + fuel + nitro + parasiteMass;
		nitro -= f;
		if (nitro < 0.0F) {
			nitro = 0.0F;
			return false;
		} else {
			return true;
		}
	}

	public float getFullMass() {
		return mass;
	}

	public void computeParasiteMass(BulletEmitter abulletemitter[][]) {
		parasiteMass = 0.0F;
		parasiteJx = 0.0F;
		for (int i = 0; i < abulletemitter.length; i++) {
			if (abulletemitter[i] == null || abulletemitter[i].length <= 0)
				continue;
			for (int j = 0; j < abulletemitter[i].length; j++) {
				if (abulletemitter[i][j] instanceof GunGeneric) {
					int k = abulletemitter[i][j].countBullets();
					float f = abulletemitter[i][j].bulletMassa() * (float)(k < 0 ? 50 : k) * 3F;
					float f3 = (float)((Actor)abulletemitter[i][j]).pos.getRelPoint().z;
					float f6 = (float)((Actor)abulletemitter[i][j]).pos.getRelPoint().y;
					parasiteJx += (f3 * f3 + f6 * f6) * f;
					parasiteMass += f;
				}
				if ((abulletemitter[i][j] instanceof BombGun) || (abulletemitter[i][j] instanceof RocketGun) || (abulletemitter[i][j] instanceof RocketBombGun)) {
					int l = abulletemitter[i][j].countBullets();
					float f1 = abulletemitter[i][j].bulletMassa() * (float)(l < 0 ? 1 : l);
		// +++ Engine MOD reflecting external fuel tank's fuel consumed weight at parasiteMass
					if ((abulletemitter[i][j] instanceof FuelTankGun) && f1 > 0) {
						FuelTank tmpFT = ((FuelTankGun) abulletemitter[i][j]).getFuelTank();
						if (tmpFT != null) f1 -= tmpFT.checkFreeTankSpace();
					}
		// --- Engine MOD reflecting external fuel tank's fuel consumed weight at parasiteMass
					float f4 = 0.0F;
					float f7 = 2.0F;
					if (l > 0)
						if (((abulletemitter[i][j] instanceof BombGun) || (abulletemitter[i][j] instanceof RocketBombGun)) && abulletemitter[i][j].getHookName().startsWith("_External"))
							pylonCoeffB = 1.5F;
						else
							pylonCoeffR = 1.0F;
					pylonCoeff = pylonCoeffB + pylonCoeffR;
					parasiteJx += (f4 * f4 + f7 * f7) * f1;
					parasiteMass += f1;
				}
				if (!(abulletemitter[i][j] instanceof Pylon))
					continue;
				float f2;
				if ((abulletemitter[i][j] instanceof PylonRO_82_1) || (abulletemitter[i][j] instanceof PylonRO_82_3) || (abulletemitter[i][j] instanceof PylonPE8_FAB100) || (abulletemitter[i][j] instanceof PylonPE8_FAB250) || (abulletemitter[i][j] instanceof PylonMG15120Internal))
					f2 = 0.0F;
		// +++ Engine MOD counting Pylon's specific massa
				else
				if (((Pylon) abulletemitter[i][j]).getMassa() > 0F)
					f2 = ((Pylon) abulletemitter[i][j]).getMassa();
		// --- Engine MOD counting Pylon's specific massa
				else
				if (abulletemitter[i][j] instanceof PylonB25RAIL)
					f2 = 5F;
				else
				if (abulletemitter[i][j] instanceof PylonBEAUPLN2)
					f2 = 108F;
				else
				if (abulletemitter[i][j] instanceof PylonBEAUPLN3)
					f2 = 108F;
				else
				if (abulletemitter[i][j] instanceof PylonF4FPLN1)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonF6FPLN2)
					f2 = 5F;
				else
				if (abulletemitter[i][j] instanceof PylonP38RAIL3FL)
					f2 = 36.3F;
				else
				if (abulletemitter[i][j] instanceof PylonP38RAIL3FR)
					f2 = 36.3F;
				else
				if (abulletemitter[i][j] instanceof PylonP38RAIL3WL)
					f2 = 36.3F;
				else
				if (abulletemitter[i][j] instanceof PylonP38RAIL3WR)
					f2 = 36.3F;
				else
				if (abulletemitter[i][j] instanceof PylonP38RAIL5)
					f2 = 50F;
				else
				if (abulletemitter[i][j] instanceof PylonRO_4andHalfInch_3)
					f2 = 36.3F;
				else
				if (abulletemitter[i][j] instanceof PylonRO_WfrGr21)
					f2 = 37F;
				else
				if (abulletemitter[i][j] instanceof PylonRO_WfrGr21Dual)
					f2 = 70F;
				else
				if (abulletemitter[i][j] instanceof PylonSpitL)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonSpitR)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonSpitROCK)
					f2 = 5F;
				else
				if (abulletemitter[i][j] instanceof PylonTEMPESTPLN3)
					f2 = 108F;
				else
				if (abulletemitter[i][j] instanceof PylonTEMPESTPLN4)
					f2 = 108F;
				else
				if (abulletemitter[i][j] instanceof PylonA5MPLN1)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonA6MPLN2)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonETC71)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonF4FPLN2)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonS328)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonHs129BombRackL)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonHs129BombRackR)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonR5BombRackC)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonR5BombRackL)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonR5BombRackL)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonR5GPodL)
					f2 = 35F;
				else
				if (abulletemitter[i][j] instanceof PylonR5GPodR)
					f2 = 35F;
				else
				if (abulletemitter[i][j] instanceof PylonTBD_TRack)
					f2 = 60F;
				else
				if (abulletemitter[i][j] instanceof PylonTBD_RRack)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonTBD_LRack)
					f2 = 10F;
				else
				if (abulletemitter[i][j] instanceof PylonTBD_FRack)
					f2 = 50F;
				else
				if (abulletemitter[i][j] instanceof PylonTBD_CRack)
					f2 = 50F;
				else
				if (abulletemitter[i][j] instanceof PylonHurriDummyRack)
					f2 = 20F;
				else
				if (abulletemitter[i][j] instanceof PylonHS123BombRack)
					f2 = 40F;
				else
				if (abulletemitter[i][j] instanceof PylonETC50)
					f2 = 40F;
				else
				if (abulletemitter[i][j] instanceof PylonETC50Bf109)
					f2 = 40F;
				else
				if (abulletemitter[i][j] instanceof PylonETC501FW190)
					f2 = 60F;
				else
				if (abulletemitter[i][j] instanceof PylonP51PLN2)
					f2 = 9F;
				else
				if (abulletemitter[i][j] instanceof PylonP63CPLN2)
					f2 = 7F;
				else
				if (abulletemitter[i][j] instanceof PylonSpitC)
					f2 = 23F;
				else
				if (abulletemitter[i][j] instanceof PylonA6MPLN1)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonB25PLN2)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonBEAUPLN1)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonETC250)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonETC900)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonF6FPLN1)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonKI43PLN1)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonKI84PLN2)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonKMB)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonMe262_R4M_Left)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonMe262_R4M_Right)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonN1K1PLN1)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonP39PLN1)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonTEMPESTPLN1)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonTEMPESTPLN2)
					f2 = 30F;
				else
				if (abulletemitter[i][j] instanceof PylonHs129BombRackC250)
					f2 = 50F;
				else
				if (abulletemitter[i][j] instanceof PylonHs129BombRackC4x50)
					f2 = 50F;
				else
				if (abulletemitter[i][j] instanceof PylonB5NPLN1)
					f2 = 70F;
				else
				if (abulletemitter[i][j] instanceof PylonB5NPLN2)
					f2 = 70F;
				else
				if (abulletemitter[i][j] instanceof PylonB5NPLN3)
					f2 = 70F;
				else
				if (abulletemitter[i][j] instanceof PylonB6NPLN1)
					f2 = 70F;
				else
				if (abulletemitter[i][j] instanceof PylonF4UPLN2)
					f2 = 70F;
				else
				if (abulletemitter[i][j] instanceof PylonF4UPLN3)
					f2 = 70F;
				else
				if (abulletemitter[i][j] instanceof PylonB5NPLN0)
					f2 = 150F;
				else
				if (abulletemitter[i][j] instanceof PylonBEAUPLN4)
					f2 = 150F;
				else
				if (abulletemitter[i][j] instanceof PylonDer16TB3Fake)
					f2 = 150F;
				else
				if (abulletemitter[i][j] instanceof PylonVAP250)
					f2 = 150F;
				else
				if (abulletemitter[i][j] instanceof PylonBF110R3)
					f2 = 150F;
				else
				if (abulletemitter[i][j] instanceof PylonBF110R4)
					f2 = 350F;
				else
				if (abulletemitter[i][j] instanceof PylonMG15120x2)
					f2 = 100F;
				else
				if (abulletemitter[i][j] instanceof PylonHS129BK37)
					f2 = 350F;
				else
				if (abulletemitter[i][j] instanceof PylonHS129MG17S)
					f2 = 80F;
				else
				if (abulletemitter[i][j] instanceof PylonHS129MK101)
					f2 = 250F;
				else
				if (abulletemitter[i][j] instanceof PylonHS129MK103)
					f2 = 196F;
				else
				if (abulletemitter[i][j] instanceof PylonHS129BK75) {
					if (FM.isPlayers() && ((RealFlightModel)FM).isRealMode())
						f2 = 905F;
					else
						f2 = 300F;
				} else
				if (abulletemitter[i][j] instanceof PylonMG15120)
					f2 = 61F;
				else
				if (abulletemitter[i][j] instanceof PylonMiG_3_BK)
					f2 = 35F;
				else
				if (abulletemitter[i][j] instanceof PylonMk103)
					f2 = 140F;
				else
				if (abulletemitter[i][j] instanceof PylonMk108)
					f2 = 90F;
				else
				if (abulletemitter[i][j] instanceof PylonP38GUNPOD)
					f2 = 90F;
				else
				if (abulletemitter[i][j] instanceof PylonP63CGUNPOD)
					f2 = 45F;
				else
					f2 = 150F;
				float f5 = (float)((Actor)abulletemitter[i][j]).pos.getRelPoint().z;
				float f8 = (float)((Actor)abulletemitter[i][j]).pos.getRelPoint().y;
				parasiteJx += (f5 * f5 + f8 * f8) * f2;
				parasiteMass += f2;
			}

		}

		pylonCoeffB = 0.0F;
		pylonCoeffR = 0.0F;
	}

	public void computeFullJ(Vector3d vector3d, Vector3d vector3d1) {
		vector3d.scale(massEmpty, vector3d1);
		vector3d.x += parasiteJx;
	}

	private FlightModelMain FM;
	public float massEmpty;
	public float mass;
	public float maxWeight;
	private float parasiteMass;
	private float parasiteJx;
	public float fuel;
	public float maxFuel;
	private FuelTank fuelTanks[];
	private boolean bFuelTanksLoaded;
	public boolean bFuelTanksDropped;
	public float nitro;
	public float maxNitro;
	public float referenceWeight;
	public float pylonCoeff;
	private float pylonCoeffB;
	private float pylonCoeffR;
	public float fuelCoeff;
}
