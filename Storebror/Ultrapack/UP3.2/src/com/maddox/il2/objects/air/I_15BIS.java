package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class I_15BIS extends I_15xyz {

	public I_15BIS() {
	}

	public void missionStarting() {
		super.missionStarting();
		this.customization();
	}

	private void customization() {
		if (!Mission.isSingle()) { return; }
		int i = this.hierMesh().chunkFindCheck("cf_D0");
		int j = this.hierMesh().materialFindInChunk("Gloss1D0o", i);
		Mat mat = this.hierMesh().material(j);
		String s = mat.Name();
		if (s.startsWith("PaintSchemes/Cache")) {
			try {
				s = s.substring(19);
				s = s.substring(0, s.indexOf("/"));
				String s1 = Main.cur().netFileServerSkin.primaryPath();
				File file = new File(HomePath.toFileSystemName(s1 + "/I-15bis/Customization.ini", 0));
				BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
				boolean flag = false;
				boolean flag1 = false;
				do {
					String s2;
					if ((s2 = bufferedreader.readLine()) == null) { break; }
					if (s2.equals("[WheelSpats]")) {
						flag = true;
						flag1 = false;
					} else if (s2.equals("[WheelSpatsGreen]")) {
						flag = true;
						flag1 = true;
					} else if (s2.equals(s) && flag) {
						this.hierMesh().chunkVisible("GearL4_D0", true);
						this.hierMesh().chunkVisible("GearR4_D0", true);
						if (flag1 && Config.isUSE_RENDER()) { this.hierMesh().materialReplace("Spat", "SpatGreen"); }
					}
				} while (true);
				bufferedreader.close();
			} catch (Exception exception) {
				System.out.println(exception);
			}
		} else if (World.Rnd().nextFloat(0.0F, 1.0F) > 0.9F) {
			this.hierMesh().chunkVisible("GearL4_D0", true);
			this.hierMesh().chunkVisible("GearR4_D0", true);
		}
	}

	static {
		Class class1 = I_15BIS.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "I-15bis");
		Property.set(class1, "meshName", "3DO/Plane/I-15bis/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFCSPar08());
		Property.set(class1, "originCountry", PaintScheme.countryRussia);
		Property.set(class1, "yearService", 1937F);
		Property.set(class1, "yearExpired", 1942F);
		Property.set(class1, "FlightModel", "FlightModels/I-15bis.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitI_15Bis.class });
		Property.set(class1, "LOSElevation", 0.84305F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08",
				"_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
	}
}
