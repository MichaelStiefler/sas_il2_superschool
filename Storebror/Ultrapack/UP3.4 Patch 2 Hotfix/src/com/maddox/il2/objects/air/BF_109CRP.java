package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class BF_109CRP extends BF_109Fx {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        hierMesh.chunkVisible("MGL", !thisWeaponsName.equals("none"));
        hierMesh.chunkVisible("MGR", !thisWeaponsName.equals("none"));
    }
    
    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -32F * f, 0.0F);
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

    protected void moveFan(float f) {
        this.hierMesh().chunkFind(Aircraft.Props[1][0]);
        int i = 0;
        for (int j = 0; j < 2; j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
                if (i >= 1)
                    i = 1;
                if (i != this.oldProp[j] && this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]])) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) {
                this.propPos[j] = (this.propPos[j] + 57.3F * this.FM.EI.engines[0].getw() * f) % 360F;
            } else {
                float f1 = 57.3F * this.FM.EI.engines[0].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if (f1 <= 0.5F)
                    f1 *= 2.0F;
                else
                    f1 = f1 * 2.0F - 2.0F;
                f1 *= 1200F;
                this.propPos[j] = (this.propPos[j] + f1 * f) % 360F;
            }
            this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, this.propPos[j] * (j == 0 ? 1.0F : -1.0F), 0.0F);
        }

    }

    public void hitProp(int i, int j, Actor actor) {
        if (i > this.FM.EI.getNum() - 1 || this.oldProp[i] == 2)
            return;
        if (this.isChunkAnyDamageVisible("Prop1") || this.isChunkAnyDamageVisible("PropRot1")) {
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    static {
        Class class1 = BF_109CRP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109CRP");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109CRP/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109CRP.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109CRP.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01",
                "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
