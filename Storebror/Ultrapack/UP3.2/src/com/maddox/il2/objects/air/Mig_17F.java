package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class Mig_17F extends Mig_17 implements TypeGSuit {

	public Mig_17F() {
	}

	public void getGFactors(TypeGSuit.GFactors theGFactors) {
		theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
//    private static final float NEG_G_TOLERANCE_FACTOR = 1.0F;
//    private static final float NEG_G_TIME_FACTOR = 1.0F;
//    private static final float NEG_G_RECOVERY_FACTOR = 1.0F;
//    private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
//    private static final float POS_G_TIME_FACTOR = 1.5F;
//    private static final float POS_G_RECOVERY_FACTOR = 1.0F;
	}

	public void update(float f1) {
		super.update(f1);
		this.typeFighterAceMakerRangeFinder();
		if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
			if (this.FM.EI.engines[0].getPowerOutput() > 0.5F && this.FM.EI.engines[0].getStage() == 6) {
				if (this.FM.EI.engines[0].getPowerOutput() > 0.5F) {
					if (this.FM.EI.engines[0].getPowerOutput() > 1.001F) {
						this.FM.AS.setSootState(this, 0, 5);
					} else {
						this.FM.AS.setSootState(this, 0, 3);
					}
				}
			} else {
				this.FM.AS.setSootState(this, 0, 0);
			}
		}
	}

	private static final float NEG_G_TOLERANCE_FACTOR = 1F;
	private static final float NEG_G_TIME_FACTOR      = 1F;
	private static final float NEG_G_RECOVERY_FACTOR  = 1F;
	private static final float POS_G_TOLERANCE_FACTOR = 1.8F;
	private static final float POS_G_TIME_FACTOR      = 1.5F;
	private static final float POS_G_RECOVERY_FACTOR  = 1F;

	static {
		Class class1 = Mig_17F.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "MiG-17");
		Property.set(class1, "meshName_ru", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar1956());
		Property.set(class1, "meshName_sk", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
		Property.set(class1, "meshName_ro", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
		Property.set(class1, "meshName_hu", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
		Property.set(class1, "meshName", "3DO/Plane/MiG-17F(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(class1, "yearService", 1952.11F);
		Property.set(class1, "yearExpired", 1960.3F);
		Property.set(class1, "FlightModel", "FlightModels/MiG-17F.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitMig_17.class });
		Property.set(class1, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 0, 9, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev03", "_ExternalDev04" });
	}
}
