package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class HE_177A5 extends HE_177_MOD implements TypeBomber, TypeTransport, TypeX4Carrier, TypeGuidedBombCarrier, TypeHasToKG {

	public HE_177A5() {
		deltaAzimuth = 0.0F;
		deltaTangage = 0.0F;
		isGuidingBomb = false;
        hasToKG = false;
        spreadAngle = 0;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		if (thisWeaponsName.startsWith("A51") || thisWeaponsName.startsWith("A53") || thisWeaponsName.startsWith("A54")) {
			if (thisWeaponsName.indexOf("293") != -1) {
				hierMesh().chunkVisible("PylonHs293C", true);
			} else if (thisWeaponsName.indexOf("FritzX") != -1) {
				hierMesh().chunkVisible("PylonFritzXC", true);
			} else if (thisWeaponsName.indexOf("LT50") != -1) {
				hierMesh().chunkVisible("PylonLT50L", true);
				hierMesh().chunkVisible("PylonLT50R", true);
				hierMesh().chunkVisible("PylonLT50C1", true);
				hierMesh().chunkVisible("PylonLT50C2", true);
				hasToKG = true;
			} else if (thisWeaponsName.indexOf("SC2500") != -1) {
				hierMesh().chunkVisible("PylonSC2500C", true);
			} else {
				hierMesh().chunkVisible("PylonC", true);
			}
		}
		if (thisWeaponsName.startsWith("A52") || thisWeaponsName.startsWith("A53")) {
			if (thisWeaponsName.indexOf("293") != -1) {
				hierMesh().chunkVisible("PylonHs293L", true);
				hierMesh().chunkVisible("PylonHs293R", true);
			} else if (thisWeaponsName.indexOf("FritzX") != -1 || thisWeaponsName.indexOf("SC") != -1) {
				hierMesh().chunkVisible("PylonFritzXL", true);
				hierMesh().chunkVisible("PylonFritzXR", true);
			} else if (thisWeaponsName.indexOf("LT50") != -1) {
				hierMesh().chunkVisible("PylonLT50C1", true);
				hierMesh().chunkVisible("PylonLT50C2", true);
				hasToKG = true;
			} else {
				hierMesh().chunkVisible("PylonL", true);
				hierMesh().chunkVisible("PylonR", true);
			}
		}
	}

	public void update(float f) {
		super.update(f);
		this.checkAftVentralGun();
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (Main3D.cur3D().cockpitCurIndx() != 3 && Main3D.cur3D().cockpitCurIndx() != 4) { // ventral gunner bay is not manned by player
			if (Time.current() > this.ventralGunnerTargetCheckTime) { // frequently (every 5-20 seconds) check if ventral gunner should change his position
				this.ventralGunnerTargetCheckTime = Time.current() + World.Rnd().nextLong(5000L, 20000L);
				if (FM.turret.length != 0)
					if (FM.turret[2].target == null) // If aft ventral gun has no target in sight, let gunner face forward
						this.setVentralGunnerDirection(0);
					else
						this.setVentralGunnerDirection(1);
			}
		}
	}
	
    public boolean isSalvo()
    {
		return thisWeaponsName.indexOf("spread") == -1;
    }

	public void typeBomberAdjAltitudePlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudePlus();
			return;
		}
		if (hasToKG) {
			fShipSpeed++;
			if (fShipSpeed > 35F)
				fShipSpeed = 35F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) fShipSpeed) });
			return;
		}
		super.typeBomberAdjAltitudePlus();
	}

	public void typeBomberAdjAltitudeMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudeMinus();
			return;
		}
		if (hasToKG) {
			fShipSpeed--;
			if (fShipSpeed < 0.0F)
				fShipSpeed = 0.0F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) fShipSpeed) });
			return;
		}
		super.typeBomberAdjAltitudeMinus();
	}

	public void typeBomberAdjSideslipPlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjSidePlus();
			return;
		}
		if (hasToKG) {
			fAOB++;
			if (fAOB > 180F)
				fAOB = 180F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) fAOB) });
			return;
		}
		fSightCurSideslip += 0.05F;
		if (fSightCurSideslip > 3F)
			fSightCurSideslip = 3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjSideslipMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjSideMinus();
			return;
		}
		if (hasToKG) {
			fAOB--;
			if (fAOB < -180F)
				fAOB = -180F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) fAOB) });
			return;
		}
		super.typeBomberAdjSideslipMinus();
	}

	public void typeBomberAdjDistancePlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudePlus();
			return;
		}
		if (hasToKG) {
			fShipSpeed++;
			if (fShipSpeed > 35F)
				fShipSpeed = 35F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) fShipSpeed) });
			return;
		}
		super.typeBomberAdjDistancePlus();
	}

	public void typeBomberAdjDistanceMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudeMinus();
			return;
		}
		if (hasToKG) {
			fShipSpeed--;
			if (fShipSpeed < 0.0F)
				fShipSpeed = 0.0F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) fShipSpeed) });
			return;
		}
		super.typeBomberAdjDistanceMinus();
	}

	public void typeBomberAdjSpeedPlus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudePlus();
			return;
		}
		if (hasToKG) {
			fShipSpeed++;
			if (fShipSpeed > 35F)
				fShipSpeed = 35F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) fShipSpeed) });
			return;
		}
		super.typeBomberAdjSpeedPlus();
	}

	public void typeBomberAdjSpeedMinus() {
		if (this.isGuidingBomb) {
			this.typeX4CAdjAttitudeMinus();
			return;
		}
		if (hasToKG) {
			fShipSpeed--;
			if (fShipSpeed < 0.0F)
				fShipSpeed = 0.0F;
			ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
			HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) fShipSpeed) });
			return;
		}
		super.typeBomberAdjSpeedMinus();
	}
    
    public boolean typeGuidedBombCisMasterAlive() {
		return isMasterAlive;
	}

	public void typeGuidedBombCsetMasterAlive(boolean flag) {
		isMasterAlive = flag;
	}

	public boolean typeGuidedBombCgetIsGuiding() {
		return isGuidingBomb;
	}

	public void typeGuidedBombCsetIsGuiding(boolean flag) {
		isGuidingBomb = flag;
	}

	public void typeX4CAdjSidePlus() {
		deltaAzimuth = 0.002F;
	}

	public void typeX4CAdjSideMinus() {
		deltaAzimuth = -0.002F;
	}

	public void typeX4CAdjAttitudePlus() {
		deltaTangage = 0.002F;
	}

	public void typeX4CAdjAttitudeMinus() {
		deltaTangage = -0.002F;
	}

	public void typeX4CResetControls() {
		deltaAzimuth = deltaTangage = 0.0F;
	}

	public float typeX4CgetdeltaAzimuth() {
		return deltaAzimuth;
	}

	public float typeX4CgetdeltaTangage() {
		return deltaTangage;
	}

	private float deltaAzimuth;
	private float deltaTangage;
	private boolean isGuidingBomb;
	private boolean isMasterAlive;

    protected float fAOB;
    protected float fShipSpeed;
    public boolean hasToKG;
    protected int spreadAngle;

	static {
		Class class1 = HE_177A5.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He-177");
		Property.set(class1, "meshName", "3do/plane/He-177A-3/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1939.5F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/He-177A-3.fmd:He177_FM");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHE_177A5.class, CockpitHE_177A5_Bombardier.class, CockpitHE_177A5_NGunner.class, CockpitHE_177A5_FGunner.class, CockpitHE_177A5_BGunner.class, CockpitHE_177A5_PGunner.class,
				CockpitHE_177A5_TGunner.class, CockpitHE_177A5_RGunner.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 15, 3, 3, 3, // 0-9
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 10-19
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 20-29
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 30-39
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 40-49
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 50-59
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 60-69
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 70-79
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 80-89
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 90-99
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 100-109
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 110-119
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 120-129 
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 130-139 
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 140-149 
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 150-159 
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, // 160-169 
				3, 3, 9, 9, 9, 9, 9, 9 }); // 170-177
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", // 0-9
				"_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", // 10-19
				"_BombSpawn07", "_BombSpawn08", "_BombSpawn08", "_BombSpawn09", "_BombSpawn09", "_BombSpawn10", "_BombSpawn10", "_BombSpawn11", "_BombSpawn11", "_BombSpawn12", // 20-29
				"_BombSpawn12", "_BombSpawn13", "_BombSpawn13", "_BombSpawn14", "_BombSpawn14", "_BombSpawn15", "_BombSpawn15", "_BombSpawn16", "_BombSpawn16", "_BombSpawn17", // 30-39
				"_BombSpawn17", "_BombSpawn18", "_BombSpawn18", "_BombSpawn19", "_BombSpawn19", "_BombSpawn20", "_BombSpawn20", "_BombSpawn21", "_BombSpawn21", "_BombSpawn22", // 40-49
				"_BombSpawn22", "_BombSpawn23", "_BombSpawn23", "_BombSpawn24", "_BombSpawn24", "_BombSpawn25", "_BombSpawn25", "_BombSpawn26", "_BombSpawn26", "_BombSpawn27", // 50-59
				"_BombSpawn27", "_BombSpawn28", "_BombSpawn28", "_BombSpawn29", "_BombSpawn29", "_BombSpawn30", "_BombSpawn30", "_BombSpawn31", "_BombSpawn31", "_BombSpawn32", // 60-69
				"_BombSpawn32", "_BombSpawn33", "_BombSpawn33", "_BombSpawn34", "_BombSpawn34", "_BombSpawn35", "_BombSpawn35", "_BombSpawn36", "_BombSpawn36", "_BombSpawn37", // 70-79
				"_BombSpawn37", "_BombSpawn38", "_BombSpawn38", "_BombSpawn39", "_BombSpawn39", "_BombSpawn40", "_BombSpawn40", "_BombSpawn41", "_BombSpawn41", "_BombSpawn42", // 80-89
				"_BombSpawn42", "_BombSpawn43", "_BombSpawn43", "_BombSpawn44", "_BombSpawn44", "_BombSpawn45", "_BombSpawn45", "_BombSpawn46", "_BombSpawn46", "_BombSpawn47", // 90-99
				"_BombSpawn47", "_BombSpawn48", "_BombSpawn48", "_BombSpawn49", "_BombSpawn49", "_BombSpawn50", "_BombSpawn50", "_BombSpawn51", "_BombSpawn51", "_BombSpawn52", // 100-109
				"_BombSpawn52", "_BombSpawn53", "_BombSpawn53", "_BombSpawn54", "_BombSpawn54", "_BombSpawn55", "_BombSpawn55", "_BombSpawn56", "_BombSpawn56", "_BombSpawn57", // 110-119
				"_BombSpawn57", "_BombSpawn58", "_BombSpawn58", "_BombSpawn59", "_BombSpawn59", "_BombSpawn60", "_BombSpawn60", "_BombSpawn61", "_BombSpawn61", "_BombSpawn62", // 120-129 
				"_BombSpawn62", "_BombSpawn63", "_BombSpawn63", "_BombSpawn64", "_BombSpawn64", "_BombSpawn65", "_BombSpawn65", "_BombSpawn66", "_BombSpawn66", "_BombSpawn67", // 130-139
				"_BombSpawn67", "_BombSpawn68", "_BombSpawn68", "_BombSpawn69", "_BombSpawn69", "_BombSpawn70", "_BombSpawn70", "_BombSpawn71", "_BombSpawn71", "_BombSpawn72", // 140-149
				"_BombSpawn72", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb11", "_ExternalBomb11", "_ExternalBomb12", // 150-159
				"_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb13", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb23", // 160-169
				"_ExternalBomb24", "_ExternalBomb31", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06" }); // 170-177
	}
}
