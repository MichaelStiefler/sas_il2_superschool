package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class YAK_1PFLight extends YAK implements TypeTNBFighter {

    public YAK_1PFLight() {
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("Wind_luk", 0.0F, 32F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Water_luk", 0.0F, 32F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        super.update(f);
    }

    public void update_windluk(float f) {
        this.update(f);
    }

    static {
        Class class1 = YAK_1PFLight.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-1PF_Light.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_1FAIRING.class });
        Property.set(class1, "LOSElevation", 0.6609F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01" });
    }
}
