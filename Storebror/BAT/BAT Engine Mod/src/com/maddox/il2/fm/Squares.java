/*Modified Squares class for the SAS Engine Mod*/

/*
 * Changelog:
 * 2014.08.11: doSetRocketHook method changed to make this class independent from Guided Missile Mod
 * 2016.04.11: reading Pylons' dragCx property
 * 2017.11.18: recover broken 4.12.2m kalibr calculation
 * 2017.11.18: low drag bombs or rockets consider
 * 2017.12.17: decrease drag of missiles
 */

package com.maddox.il2.fm;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.GuidedMissileInterop;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.MissileGun;
import com.maddox.il2.objects.weapons.Pylon;
import com.maddox.il2.objects.weapons.PylonF6FPLN2;
import com.maddox.il2.objects.weapons.PylonHS129BK37;
import com.maddox.il2.objects.weapons.PylonHS129BK75;
import com.maddox.il2.objects.weapons.PylonMG15120Internal;
import com.maddox.il2.objects.weapons.PylonP38RAIL3FL;
import com.maddox.il2.objects.weapons.PylonP38RAIL3FR;
import com.maddox.il2.objects.weapons.PylonP38RAIL3WL;
import com.maddox.il2.objects.weapons.PylonP38RAIL3WR;
import com.maddox.il2.objects.weapons.PylonP38RAIL5;
import com.maddox.il2.objects.weapons.PylonP38RAILS;
import com.maddox.il2.objects.weapons.PylonPE8_FAB100;
import com.maddox.il2.objects.weapons.PylonPE8_FAB250;
import com.maddox.il2.objects.weapons.PylonRO_82_1;
import com.maddox.il2.objects.weapons.PylonRO_82_3;
import com.maddox.il2.objects.weapons.PylonRO_WfrGr21;
import com.maddox.il2.objects.weapons.PylonRO_WfrGr21Dual;
import com.maddox.il2.objects.weapons.PylonVAP250;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.RocketGunNull;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;

// Referenced classes of package com.maddox.il2.fm:
//			Controls

public class Squares {

	public Squares() {
		dragParasiteCx = 0.0F;
		dragAirbrakeCx = 0.0F;
		dragChuteCx = 1.5F;
		dragFuselageCx = 0.0F;
		dragProducedCx = 0.0F;
		toughness = new float[44];
		eAbsorber = new float[44];
	}

	public void load(SectFile sectfile) {
		String s1 = "Zero Square processed from " + sectfile.toString();
		String s = "Squares";
		float f = sectfile.get(s, "Wing", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		squareWing = f;
		f = sectfile.get(s, "Aileron", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		squareAilerons = f;
		f = sectfile.get(s, "Flap", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		squareFlaps = f;
		f = sectfile.get(s, "Stabilizer", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		liftStab = f;
		f = sectfile.get(s, "Elevator", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		squareElevators = f;
		f = sectfile.get(s, "Keel", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		liftKeel = f;
		f = sectfile.get(s, "Rudder", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		squareRudders = f;
		f = sectfile.get(s, "Wing_In", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		liftWingLIn = liftWingRIn = f;
		f = sectfile.get(s, "Wing_Mid", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		liftWingLMid = liftWingRMid = f;
		f = sectfile.get(s, "Wing_Out", 0.0F);
		if (f == 0.0F)
			throw new RuntimeException(s1);
		liftWingLOut = liftWingROut = f;
		f = sectfile.get(s, "AirbrakeCxS", -1F);
		if (f == -1F)
			throw new RuntimeException(s1);
		dragAirbrakeCx = f;
		f = sectfile.get("Params", "SpinCxLoss", -1F);
		if (f == -1F)
			throw new RuntimeException(s1);
		spinCxloss = f;
		f = sectfile.get("Params", "SpinCyLoss", -1F);
		if (f == -1F)
			throw new RuntimeException(s1);
		spinCyloss = f;
		for (int i = 0; i < 8; i++)
			dragEngineCx[i] = 0.0F;

		s = "Toughness";
		for (int j = 0; j < 44; j++)
			toughness[j] = (float)sectfile.get(s, Aircraft.partNames()[j], 100) * 0.0001F;

		toughness[43] = 3.402823E+038F;
		float f1 = (2.0F * (liftWingLIn + liftWingLMid + liftWingLOut)) / (squareWing + 0.01F);
		if (f1 < 0.9F || f1 > 1.1F) {
			if (World.cur().isDebugFM())
				System.out.println("Error in flightmodel " + sectfile.toString() + ": (wing square) != (sum of squares*2)");
			if (f1 > 1.0F)
				squareWing = 2.0F * (liftWingLIn + liftWingLMid + liftWingLOut);
			else
				liftWingLIn = liftWingLMid = liftWingLOut = liftWingRIn = liftWingRMid = liftWingROut = 0.166667F * squareWing;
		}
	}

	public float getToughness(int i) {
		return toughness[i];
	}

	// TODO: Adds in new drag parameters for chutes etc
	public void computeParasiteDrag(Controls controls, BulletEmitter abulletemitter[][]) {
		dragParasiteCx = 0.0F;
		for (int i = 0; i < abulletemitter.length; i++) {
			if (abulletemitter[i] == null || abulletemitter[i].length <= 0)
				continue;
			for (int j = 0; j < abulletemitter[i].length; j++) {
				if ((abulletemitter[i][j] instanceof BombGunNull) || (abulletemitter[i][j] instanceof RocketGunNull))
					continue;
				if (((abulletemitter[i][j] instanceof BombGun) || (abulletemitter[i][j] instanceof RocketBombGun) || (abulletemitter[i][j] instanceof TorpedoGun))
					&& abulletemitter[i][j].haveBullets() && (abulletemitter[i][j].getHookName().startsWith("_External") || abulletemitter[i][j].getHookName().startsWith("_Static"))
					&& dragParasiteCx < 0.704F) {
					float f = 0.125F;
					if (abulletemitter[i][j] instanceof FuelTankGun)
						f = 0.05F;
					Class class2 = (Class)Property.value(abulletemitter[i][j].getClass(), "bulletClass", null);
					float kalibr = Property.floatValue(class2, "kalibr", 0.10F); // change default from 0.0F into 0.10F to avoit f4 made 0.0F
					float dragCf = Property.floatValue(class2, "dragCoefficient", 1.0F);
					if (dragCf < 0.01F) dragCf = 0.01F;
					if (dragCf > 100.0F) dragCf = 100.0F;
					float f4 = (float)(3.1415926535897931D * (double) kalibr * (double) kalibr * (double) f * (double) dragCf);
					dragParasiteCx += f4;
				}
				if ((abulletemitter[i][j] instanceof RocketGun) && abulletemitter[i][j].haveBullets() && abulletemitter[i][j].getHookName().startsWith("_External")) {
					Class class1 = (Class) Property.value(abulletemitter[i][j].getClass(), "bulletClass", null);
					float kalibr = Property.floatValue(class1, "kalibr", 0.0F);
					float dragCf = Property.floatValue(class1, "dragCoefficient", 1.0F);
					if (dragCf < 0.01F) dragCf = 0.01F;
					if (dragCf > 100.0F) dragCf = 100.0F;
				// TODO: ++ Changed Code to make Engine Mod independent of Guided Missiles Mod ++
					if (GuidedMissileInterop.getGuidedMissileModExists() && (abulletemitter[i][j] instanceof MissileGun)) {
						dragCf *= 0.10F;
					}
				// TODO: -- Changed Code to make Engine Mod independent of Guided Missiles Mod --
					float f3 = (float)(3.1415926535897931D * (double) kalibr * (double) kalibr * 0.11999999731779099D * (double) dragCf);
					dragParasiteCx += f3;
				}
				if (!(abulletemitter[i][j] instanceof Pylon) || (abulletemitter[i][j] instanceof PylonRO_82_1)
					|| (abulletemitter[i][j] instanceof PylonRO_82_3) || (abulletemitter[i][j] instanceof PylonPE8_FAB100)
					|| (abulletemitter[i][j] instanceof PylonPE8_FAB250)
					|| (abulletemitter[i][j] instanceof PylonP38RAIL3FL) || (abulletemitter[i][j] instanceof PylonP38RAIL3FR)
					|| (abulletemitter[i][j] instanceof PylonP38RAIL3WL) || (abulletemitter[i][j] instanceof PylonP38RAIL3WR)
					|| (abulletemitter[i][j] instanceof PylonP38RAIL5) || (abulletemitter[i][j] instanceof PylonP38RAILS)
					|| (abulletemitter[i][j] instanceof PylonF6FPLN2) || (abulletemitter[i][j] instanceof PylonMG15120Internal))
					continue;
			// +++ Engine MOD counting Pylon's specific dragCx
				if(((Pylon) abulletemitter[i][j]).getDragCx() > 0F) {
					if(((Pylon) abulletemitter[i][j]).isMinusDrag())
						dragParasiteCx -= ((Pylon) abulletemitter[i][j]).getDragCx();
					else
						dragParasiteCx += ((Pylon) abulletemitter[i][j]).getDragCx();

					continue;
				}
			// --- Engine MOD counting Pylon's specific dragCx
				dragParasiteCx += 0.035F;
				if ((abulletemitter[i][j] instanceof PylonHS129BK75) || (abulletemitter[i][j] instanceof PylonHS129BK37))
					dragParasiteCx += 0.45F;
				if (abulletemitter[i][j] instanceof PylonRO_WfrGr21)
					dragParasiteCx += 0.015F;
				if (abulletemitter[i][j] instanceof PylonRO_WfrGr21Dual)
					dragParasiteCx += 0.02F;
				if (abulletemitter[i][j] instanceof PylonVAP250)
					dragParasiteCx += 0.02F;
			}

		}

		dragParasiteCx += 0.015F * controls.getRefuel();
		dragParasiteCx += 0.02F * controls.getCockpitDoor();
		if (controls.bHasBayDoorControl)
			dragParasiteCx += 0.02F * controls.getBayDoor();
	}

	public float squareWing;
	public float squareAilerons;
	public float squareElevators;
	public float squareRudders;
	public float squareFlaps;
	public float liftWingLIn;
	public float liftWingLMid;
	public float liftWingLOut;
	public float liftWingRIn;
	public float liftWingRMid;
	public float liftWingROut;
	public float liftStab;
	public float liftKeel;
	public float dragEngineCx[] = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
	public float dragParasiteCx;
	public float dragAirbrakeCx;
	public float dragChuteCx;
	public float dragFuselageCx;
	public float dragProducedCx;
	float spinCxloss;
	float spinCyloss;
	public float toughness[];
	public float eAbsorber[];
	public final float dragSmallHole = 0.06F;
	public final float dragBigHole = 0.12F;
	public final float wingSmallHole = 0.4F;
	public final float wingBigHole = 0.8F;
}
