package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.rts.Property;

public class AT_32F extends AT_32xyz implements TypeSailPlane {
    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    AT_32F.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    AT_32F.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(AT_32F.tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }
            }

        }

    }

    private static Point3d tmpp = new Point3d();

    static {
        Class class1 = AT_32F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CondorF");
        Property.set(class1, "meshName", "3DO/Plane/AT-32F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/AT32F.fmd");
        Property.set(class1, "LOSElevation", 1.1058F);
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_AT32.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "null" });
    }
}
