package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.FObj;
import com.maddox.il2.engine.Mat;
import com.maddox.rts.Property;

public class HE_177A3 extends HE_177_TD {

	private void teamDildos414SkinWorkaround() {
		String s = getPropertyMesh(this.getClass(), loadingCountry);
		String s2 = s.substring(0, s.lastIndexOf('/') + 1);
//		String s3 = getSkinPrefix(loadingCountry, this.getRegiment());
		String s3 = "";
		String s5 = World.cur().camouflage == 1 ? "winter" : "summer";
		String s4 = s3 + s5;
		String s1 = s2 + s4;
		String as[] = { s1 + "/skin1o.tga", s1 + "/skin1p.tga", s1 + "/skin1q.tga" };
		int ai[] = new int[4];
		String _skinMat[] = { "Gloss1aD0o", "Gloss1aD0p", "Gloss1aD0q", "Gloss1aD1o", "Gloss1aD1p", "Gloss1aD1q", "Gloss1aD2o", "Gloss1aD2p", "Gloss1aD2q", "Matt1aD0o", "MattaD0p", "Matt1aD0q", "Matt1aD1o", "Matt1aD1p", "Matt1aD1q", "Matt1aD2o",
				"Matt1aD2p", "Matt1aD2q" };
		String _curSkin[] = { "skin1o.tga", "skin1p.tga", "skin1q.tga" };

		for (int i = 0; i < _skinMat.length; i++) {
			int j = this.hierMesh().materialFind(_skinMat[i]);
			if (j >= 0) {
				Mat mat = this.hierMesh().material(j);
				boolean flag = false;
				for (int k = 0; k < 4; k++) {
					ai[k] = -1;
					if (mat.isValidLayer(k)) {
						mat.setLayer(k);
						String s6 = mat.get('\0');
						for (int l = 0; l < 3; l++) {
							if (!s6.regionMatches(true, s6.length() - 10, _curSkin[l], 0, 10)) continue;
							ai[k] = l;
							flag = true;
							break;
						}

					}
				}

				if (flag) {
					String s7 = s1 + "/" + _skinMat[i] + ".mat";
					Mat mat1;
					if (FObj.Exist(s7)) mat1 = (Mat) FObj.Get(s7);
					else {
						mat1 = (Mat) mat.Clone();
						mat1.Rename(s7);
						for (int i1 = 0; i1 < 4; i1++)
							if (ai[i1] >= 0) {
								mat1.setLayer(i1);
								mat1.set('\0', as[ai[i1]]);
							}

					}
					this.hierMesh().materialReplace(_skinMat[i], mat1);
				}
			}
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.teamDildos414SkinWorkaround();
		float f = 0.0F;
		float f1 = this.FM.M.fuel / this.FM.M.maxFuel;
		if (this.thisWeaponsName.startsWith("RustB")) {
			f = 7952.296F;
			this.iRust = 1;
		} else if (this.thisWeaponsName.startsWith("RustC")) {
			f = 9382.67F;
			this.iRust = 2;
		} else {
			f = 6521.92F;
			this.iRust = 0;
		}
		com.maddox.il2.ai.BulletEmitter abulletemitter[][] = this.FM.CT.Weapons;
		if (abulletemitter[3] == null) return;
		this.FM.M.fuel = this.FM.M.maxFuel = f;
		this.FM.M.computeParasiteMass(abulletemitter);
		this.FM.M.requestNitro(0.0F);
		float f2 = this.FM.M.getFullMass();
		if (f2 > 31000F) {
			float f4 = f2 - 31000F;
			if (f > f4) f -= f4;
		}
		this.FM.M.fuel = f1 * f;
		this.FM.M.maxFuel = f;
		this.FM.M.computeParasiteMass(abulletemitter);
		this.FM.M.requestNitro(0.0F);
	}

	public int getBombTrainMaxAmount() {
		if (this.thisWeaponsName.startsWith("RustA_12x")) return 12;
		if (this.thisWeaponsName.startsWith("RustB_8x")) return 8;
		if (this.thisWeaponsName.startsWith("RustA_48x") || this.thisWeaponsName.startsWith("RustA_6x") || this.thisWeaponsName.startsWith("RustA_2xSC1800+4xSC250")) return 6;
		return !this.thisWeaponsName.startsWith("RustA_4x") && !this.thisWeaponsName.startsWith("RustA_2xSC1800") && !this.thisWeaponsName.startsWith("RustB_32x") && !this.thisWeaponsName.startsWith("RustB_4x")
				&& !this.thisWeaponsName.startsWith("RustC_4x") ? 2 : 4;
	}

	protected void moveBayDoor(float f) {
		if (this.iRust < 1) {
			this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -74F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -74F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -90F * f, 0.0F);
		}
		if (this.iRust < 2) {
			this.hierMesh().chunkSetAngles("Bay5_D0", 0.0F, -74F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -90F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay7_D0", 0.0F, -74F * f, 0.0F);
			this.hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -90F * f, 0.0F);
		}
		this.hierMesh().chunkSetAngles("Bay9_D0", 0.0F, -74F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay10_D0", 0.0F, -90F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay11_D0", 0.0F, -74F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Bay12_D0", 0.0F, -90F * f, 0.0F);
	}

	public boolean bToFire;

	static {
		Class class1 = HE_177A3.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He-177");
		Property.set(class1, "meshName", "3DO/Plane/He-177A-3/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1942F);
		Property.set(class1, "yearExpired", 1945F);
		Property.set(class1, "FlightModel", "FlightModels/He-177A-3.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHE_177A3.class, CockpitHE_177A3_Bombardier.class, CockpitHE_177A3_NGunner.class, CockpitHE_177A3_TGunner.class, CockpitHE_177A3_FGunner.class, CockpitHE_177A3_BGunner.class,
				CockpitHE_177A3_TGunner2.class, CockpitHE_177A3_HGunner.class });
		Property.set(class1, "LOSElevation", 1.0976F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 14, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
				3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		Aircraft.weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_BombSpawn00", "_BombSpawn68", "_BombSpawn69", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06",
						"_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20",
						"_BombSpawn21a", "_BombSpawn22a", "_BombSpawn23a", "_BombSpawn24a", "_BombSpawn25a", "_BombSpawn26a", "_BombSpawn27a", "_BombSpawn28", "_BombSpawn29a", "_BombSpawn30a", "_BombSpawn31a", "_BombSpawn32a", "_BombSpawn33a",
						"_BombSpawn34a", "_BombSpawn35a", "_BombSpawn68", "_BombSpawn69", "_BombSpawn07", "_BombSpawn36", "_BombSpawn37a", "_BombSpawn38a", "_BombSpawn39a", "_BombSpawn40a", "_BombSpawn41a", "_BombSpawn42a", "_BombSpawn43a",
						"_BombSpawn44", "_BombSpawn45a", "_BombSpawn46a", "_BombSpawn47a", "_BombSpawn48a", "_BombSpawn49a", "_BombSpawn50a", "_BombSpawn51a", "_BombSpawn52", "_BombSpawn53a", "_BombSpawn54a", "_BombSpawn55a", "_BombSpawn56a",
						"_BombSpawn57a", "_BombSpawn58a", "_BombSpawn59a", "_BombSpawn60", "_BombSpawn61a", "_BombSpawn62a", "_BombSpawn63a", "_BombSpawn64a", "_BombSpawn65a", "_BombSpawn66a", "_BombSpawn67a" });
	}
}
