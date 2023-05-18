package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.AircraftState;
import com.maddox.rts.Property;

public class E7K1 extends E7K {
    
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.astateSootEffects = new Eff3DActor[AircraftState.MAX_ENGINES][3];
    }
    
    public void doSetSootState(int i, int j) {
        int effIndex;
        for (effIndex = 0; effIndex < 3; effIndex++) {
            if (this.FM.AS.astateSootEffects[i][effIndex] != null) Eff3DActor.finish(this.FM.AS.astateSootEffects[i][effIndex]);
            this.FM.AS.astateSootEffects[i][effIndex] = null;
        }
        switch (j) {
            case 0:
                break;
            case 1:
                for (effIndex = 0; effIndex < 3; effIndex++) {
                    this.FM.AS.astateSootEffects[i][effIndex] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_0" + (effIndex + 1)), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1.0F);
                }
                break;
            case 3:
                for (effIndex = 0; effIndex < 3; effIndex++) {
                   this.FM.AS.astateSootEffects[i][effIndex] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_0" + (effIndex + 1)), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0F);
                }
            case 2:
                for (effIndex = 0; effIndex < 3; effIndex++) {
                   this.FM.AS.astateSootEffects[i][effIndex] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_0" + (effIndex + 1)), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1.0F);
                }
                break;
            case 5:
                for (effIndex = 0; effIndex < 3; effIndex++) {
                    this.FM.AS.astateSootEffects[i][effIndex] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_0" + (effIndex + 1)), null, 3.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1.0F);
                }
            case 4:
                for (effIndex = 0; effIndex < 3; effIndex++) {
                    this.FM.AS.astateSootEffects[i][effIndex] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_0" + (effIndex + 1)), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1.0F);
                }
                break;
        }
    }
    
    static {
        Class class1 = E7K1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Alf");
        Property.set(class1, "meshName", "3DO/Plane/E7K(Multi1)/hierE7K1.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/E7K1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitE7K.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06" });
    }
}
