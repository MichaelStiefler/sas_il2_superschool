package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class Demoiselle extends Scheme1 {

    public Demoiselle() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
    }

    protected void moveGear(float f) {
        Demoiselle.moveGear(this.hierMesh(), f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireRudL_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("WireRudR_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -15F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -15F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    static {
        Class class1 = Demoiselle.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Demoiselle");
        Property.set(class1, "meshName", "3DO/Plane/Demoiselle(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Niu4.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDemoiselle.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
