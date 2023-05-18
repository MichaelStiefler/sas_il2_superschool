package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class Golie extends Scheme1 {

    public Golie() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
    }

    protected void moveGear(float f) {
        Golie.moveGear(this.hierMesh(), f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireRudL_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireRudR_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireRudL2_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireRudR2_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLrod_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireAL_D0", 0.0F, -13F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRrod_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireAR_D0", 0.0F, -13F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorC_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireVL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireVLn_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireVR_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireVRn_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireVC_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireVCn_D0", 0.0F, -30F * f, 0.0F);
    }

    static {
        Class class1 = Golie.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Golie");
        Property.set(class1, "meshName", "3DO/Plane/Golie/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1963F);
        Property.set(class1, "yearExpired", 1992F);
        Property.set(class1, "FlightModel", "FlightModels/DOF.fmd:DOF_generic_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGolie.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
