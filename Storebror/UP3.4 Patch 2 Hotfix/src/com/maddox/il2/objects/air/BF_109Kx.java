package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class BF_109Kx extends BF_109Gy implements TypeBNZFighter {

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flettner1_D0", 0.0F, -45F * f, 0.0F);
    }

    public void update(float f) {
        super.update(f);
        this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 20F * this.kangle, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        BF_109Gy.moveGear(hiermesh, f);
        float f2 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f2, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 90F * f, 0.0F, 0.0F);
    }

    protected void moveGear(float f) {
        super.moveGear(f);
        float f2 = Math.max(-f * 1500F, -90F);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, -f2, 0.0F);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, f2, 0.0F);
        this.hierMesh().chunkSetAngles("GearC2_D0", 90F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() >= 0.98F) this.hierMesh().chunkSetAngles("GearC2_D0", 90.0F, -f, 0.0F);
    }

    static {
        Class class1 = BF_109Kx.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3do/plane/Bf-109K-4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1944.3F);
        Property.set(class1, "yearExpired", 1955F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109K-4.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109K4.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01",
                "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
