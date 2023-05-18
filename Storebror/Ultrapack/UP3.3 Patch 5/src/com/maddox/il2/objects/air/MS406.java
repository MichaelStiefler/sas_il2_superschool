package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MS406 extends MS400X {

    public void update(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.2F, 0.0F);
        this.hierMesh().chunkSetLocate("OilRad_D0", Aircraft.xyz, Aircraft.ypr);
        super.update(f);
    }

    static {
        Class class1 = MS406.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Morane");
        Property.set(class1, "meshName", "3DO/Plane/MS406(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_fi", "3DO/Plane/MS406(fi)/hier.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFCSPar02());
        Property.set(class1, "meshName_fr", "3DO/Plane/MS406(fr)/hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/MS406.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMorane.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01" });
    }
}
