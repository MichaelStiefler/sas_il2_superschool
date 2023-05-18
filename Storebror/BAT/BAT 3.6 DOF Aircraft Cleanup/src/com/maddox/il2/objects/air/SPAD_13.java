package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class SPAD_13 extends SPAD_X implements TypeFighter, TypeTNBFighter {

    public SPAD_13() {
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, 15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, 15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, 15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, 15F * f, 0.0F);
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("St1_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St2_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St3_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St4_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St5_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St6_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St7_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St8_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("St9_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * -90F, 0.0F);
        super.update(f);
    }

    static {
        Class class1 = SPAD_13.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SPAD");
        Property.set(class1, "meshName", "3DO/Plane/SPAD-13(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/SPAD7.fmd:SPAD7_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSPAD_13.class });
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
