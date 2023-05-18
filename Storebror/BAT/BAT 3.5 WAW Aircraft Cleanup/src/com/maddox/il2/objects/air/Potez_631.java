package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class Potez_631 extends Potez_630 implements TypeFighter {

    public Potez_631() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.75F;
    }

    public boolean hasCourseWeaponBullets() {
        return (this.FM.CT.Weapons[0] != null) && (this.FM.CT.Weapons[0][0] != null) && (this.FM.CT.Weapons[0][0].countBullets() != 0);
    }

    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurReadyness;

    static {
        Class class1 = Potez_631.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P631");
        Property.set(class1, "meshName", "3do/plane/Potez-631(Multi1)/HEI_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_fr", "3do/plane/Potez-631(fr)/HEI_hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "meshName_vi", "3do/plane/Potez-631(vi)/HEI_hier.him");
        Property.set(class1, "PaintScheme_vi", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Potez_630.fmd:Potez63x_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitPotez_630.class, CockpitPotez_630_Gunner.class });
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 9, 9, 9, 9, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON05", "_CANNON05", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_MGUN01" });
    }
}
