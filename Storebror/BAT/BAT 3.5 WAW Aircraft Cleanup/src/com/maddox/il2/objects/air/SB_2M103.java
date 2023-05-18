package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class SB_2M103 extends SBxyz {

    public SB_2M103() {
    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        if (this.FM.isPlayers()) {
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.dvCockpitDoor = 0.5F;
            this.FM.CT.cockpitDoorControl = 1.0F;
        }
    }

    public void update(float f) {
        super.update(f);
        if (!this.FM.isPlayers() && this.FM.Gears.onGround()) {
            if (this.FM.EI.engines[1].getRPM() < 100F) {
                this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
                this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
    }

    static {
        Class class1 = SB_2M103.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SB-2M");
        Property.set(class1, "meshName", "3DO/Plane/SB-2M-103(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "meshName_ru", "3DO/Plane/SB-2M-103(Russian)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/SB-2M-103.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSB103.class, CockpitSB103_Bombardier.class, CockpitSB103_NGunner.class, CockpitSB103_BGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06" });
    }
}
