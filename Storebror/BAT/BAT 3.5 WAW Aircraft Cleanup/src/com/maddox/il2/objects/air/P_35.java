package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.BaseGameVersion;

public class P_35 extends P_35xyz {

    public P_35() {
    }

    public void missionStarting() {
        super.missionStarting();
        if (this.FM.isStationedOnGround()) {
            this.FM.AS.setCockpitDoor(((Interpolate) (this.FM)).actor, 1);
            this.FM.CT.cockpitDoorControl = 1.0F;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("P35A")) {
            this.hierMesh().chunkVisible("WingGunL_D0", true);
            this.hierMesh().chunkVisible("WingGunR_D0", true);
            if (BaseGameVersion.is412orLater()) {
                this.FM.EI.engines[0].load(this.FM, "PW_R-1830_Series:P47Pack_FM", "R-1830-45", 0);
            } else {
                this.FM.EI.engines[0].load(this.FM, "FlightModels/PW_R-1830_Series:P47Pack_FM.emd", "R-1830-45", 0);
            }
        } else {
            this.hierMesh().chunkVisible("WingGunL_D0", false);
            this.hierMesh().chunkVisible("WingGunR_D0", false);
        }
    }

    static {
        Class class1 = P_35.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P_35");
        Property.set(class1, "meshName", "3DO/Plane/P-35/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1951F);
        Property.set(class1, "FlightModel", "FlightModels/P-35.fmd:P47Pack_FM");
        Property.set(class1, "FlightModelA", "FlightModels/P-35A.fmd:P47Pack_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_35.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
