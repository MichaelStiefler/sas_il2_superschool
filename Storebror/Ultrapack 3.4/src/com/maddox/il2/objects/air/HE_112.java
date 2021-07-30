package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class HE_112 extends HE_1xx implements TypeBNZFighter {

    public HE_112() {
        this.cockpitDoor_ = 0.0F;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    // New Gear Animation Code,
    // historically accurate in terms of moving main gear before tail wheel
    // In order to do so, new Parameter "bDown" has been introduced to distinguish between
    // gear up and gear down
    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos, boolean bDown, float[] rnd) {
        hiermesh.chunkSetAngles("GearC2_D0", smoothCvt(tailWheelPos, rnd[2] + (bDown ? 0.55F : 0.01F), rnd[2] + (bDown ? 0.84F : 0.3F), 0.0F, 90.0F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", smoothCvt(leftGearPos, 0.1F + rnd[0], 0.75F + rnd[0], 0.0F, -13.5F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, smoothCvt(leftGearPos, 0.1F + rnd[0], 0.75F + rnd[0], 0.0F, -90.0F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, smoothCvt(leftGearPos, 0.1F + rnd[0], 0.26F + rnd[0], 0.0F, -90.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", smoothCvt(rightGearPos, 0.2F + rnd[1], 0.849F + rnd[1], 0.0F, 13.5F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, smoothCvt(rightGearPos, 0.2F + rnd[1], 0.849F + rnd[1], 0.0F, 90.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, smoothCvt(rightGearPos, 0.2F + rnd[1], 0.36F + rnd[1], 0.0F, 90.0F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(hiermesh, leftGearPos, rightGearPos, tailWheelPos, true, rndgearnull);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float tailWheelPos) {
        moveGear(this.hierMesh(), leftGearPos, rightGearPos, tailWheelPos, this.FM.CT.GearControl > 0.5F, this.rndgear);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float gearPos, boolean bDown) {
        moveGear(hiermesh, gearPos, gearPos, gearPos, bDown, rndgearnull); // re-route old style function calls to new code
    }

    public static void moveGear(HierMesh hiermesh, float gearPos) {
        moveGear(hiermesh, gearPos, gearPos, gearPos, true, rndgearnull); // re-route old style function calls to new code
    }

    protected void moveGear(float gearPos) {
        moveGear(this.hierMesh(), gearPos, gearPos, gearPos, this.FM.CT.GearControl > 0.5F, this.rndgear);
    }
    // ************************************************************************************************

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 90F, -f, 0.0F);
            return;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        xyz[1] = smoothCvt(f, 0.01F, 0.99F, 0.0F, 0.6F);
        this.hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public float cockpitDoor_;

    static void initStatic(Class class1) {
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He112");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1944.3F);
        Property.set(class1, "yearExpired", 1955F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitHE_112.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06" });
    }
}
