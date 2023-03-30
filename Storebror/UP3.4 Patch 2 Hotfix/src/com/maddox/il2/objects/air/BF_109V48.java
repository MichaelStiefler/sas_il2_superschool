package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class BF_109V48 extends BF_109Fx {

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, 20F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
    }

    // New Gear Animation Code,
    // historically accurate in terms of moving main gear before tail wheel
    // In order to do so, new Parameter "bDown" has been introduced to distinguish between
    // gear up and gear down
    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos, boolean bDown) {
        if (bDown) { // Center gear, lower
            hiermesh.chunkSetAngles("GearC3_D0", smoothCvt(tailWheelPos, 0.725F, 0.925F, 0.0F, 70F), 0.0F, 0.0F);
        } else { // Center gear, raise
            hiermesh.chunkSetAngles("GearC3_D0", smoothCvt(tailWheelPos, 0.01F, 0.20F, 0.0F, 70F), 0.0F, 0.0F);
        }

        hiermesh.chunkSetAngles("GearL2_D0", smoothCvt(leftGearPos, 0.01F, 0.60F, 0.0F, -33.5F), 0.0F, 0.0F); // Left Gear
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, smoothCvt(leftGearPos, 0.01F, 0.60F, 0.0F, -77.5F), 0.0F);

        hiermesh.chunkSetAngles("GearR2_D0", smoothCvt(rightGearPos, 0.30F, 0.90F, 0.0F, 33.5F), 0.0F, 0.0F); // Right Gear
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, smoothCvt(rightGearPos, 0.30F, 0.90F, 0.0F, 77.5F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(hiermesh, leftGearPos, rightGearPos, tailWheelPos, true);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(this.hierMesh(), leftGearPos, rightGearPos, tailWheelPos, this.FM.CT.GearControl > 0.5F);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float gearPos, boolean bDown) {
        moveGear(hiermesh, gearPos, gearPos, gearPos, bDown); // re-route old style function calls to new code
    }

    public static void moveGear(HierMesh hiermesh, float gearPos) {
        moveGear(hiermesh, gearPos, gearPos, gearPos, true); // re-route old style function calls to new code
    }

    protected void moveGear(float gearPos) {
        moveGear(this.hierMesh(), gearPos, this.FM.CT.GearControl > 0.5F);
    }

    private static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + (outMax - outMin) * (-0.5F * (float) Math.cos((inputValue - inMin) / (inMax - inMin) * Math.PI) + 0.5F);
    }
    // ************************************************************************************************

    static {
        Class class1 = BF_109V48.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109V48");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109V48/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109V48.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109V.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_ExternalDev01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb01",
                "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
