package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class D_3800 extends MS400X {

    public D_3800() {
    }

    public void update(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.2F, 0.0F);
        this.hierMesh().chunkSetLocate("OilRad_D0", Aircraft.xyz, Aircraft.ypr);
        super.update(f);
    }

    static {
        Class class1 = D_3800.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D3800");
        Property.set(class1, "meshName", "3DO/Plane/D-3800(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1951.8F);
        Property.set(class1, "FlightModel", "FlightModels/MS410.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMorane40.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_MGUN03", "_MGUN04" });
    }
}
