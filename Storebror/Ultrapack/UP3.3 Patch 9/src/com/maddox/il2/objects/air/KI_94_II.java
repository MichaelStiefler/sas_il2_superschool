package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class KI_94_II extends KI_94 implements TypeFighter {

    protected void moveFan(float f) {
        super.moveFan(f);
        this.hierMesh().chunkSetAngles(Props[0][0], 0.0F, this.propPos[0], 0.0F);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.Loc.z > 9000.0D) {
            if (!this.FM.EI.engines[0].getControlAfterburner()) {
                this.FM.EI.engines[0].setAfterburnerType(2);
            }
        } else if (!this.FM.EI.engines[0].getControlAfterburner()) {
            this.FM.EI.engines[0].setAfterburnerType(9);
        }
    }

    static {
        Class class1 = KI_94_II.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-94-II");
        Property.set(class1, "meshName", "3DO/Plane/Ki-94-II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1955F);
        Property.set(class1, "FlightModel", "FlightModels/Ki94II.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_94_II.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
