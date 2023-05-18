package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class HE_100D extends HE_1xx implements TypeFighter, TypeBNZFighter, TypeStormovik {

    public HE_100D() {
        this.cockpitDoor_ = 0.0F;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos, boolean bDown, float rnd[]) {
        HE_1xx.myResetYPRmodifier();
        hiermesh.chunkSetAngles("GearC2_D0", HE_1xx.smoothCvt(tailWheelPos, rnd[2] + (bDown ? 0.55F : 0.01F), rnd[2] + (bDown ? 0.84F : 0.3F), 0.0F, 90F), 0.0F, 0.0F);
        Aircraft.xyz[2] = HE_1xx.smoothCvt(leftGearPos, 0.3F + rnd[0], 0.4F + rnd[0], 0.04F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", HE_1xx.smoothCvt(leftGearPos, 0.1F + rnd[0], 0.75F + rnd[0], 0.0F, 22.5F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, HE_1xx.smoothCvt(leftGearPos, 0.1F + rnd[0], 0.75F + rnd[0], 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, HE_1xx.smoothCvt(leftGearPos, 0.1F + rnd[0], 0.26F + rnd[0], 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL88_D0", 0.0F, HE_1xx.smoothCvt(leftGearPos, 0.01F + rnd[0], 0.65F + rnd[0], 0.0F, 90F), 0.0F);
        hiermesh.chunkSetLocate("GearL99_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[2] = HE_1xx.smoothCvt(rightGearPos, 0.4F + rnd[1], 0.5F + rnd[1], 0.04F, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", HE_1xx.smoothCvt(rightGearPos, 0.2F + rnd[1], 0.849F + rnd[1], 0.0F, -22.5F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, HE_1xx.smoothCvt(rightGearPos, 0.2F + rnd[1], 0.849F + rnd[1], 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, HE_1xx.smoothCvt(rightGearPos, 0.2F + rnd[1], 0.36F + rnd[1], 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR88_D0", 0.0F, HE_1xx.smoothCvt(rightGearPos, 0.1F + rnd[1], 0.75F + rnd[1], 0.0F, -90F), 0.0F);
        hiermesh.chunkSetLocate("GearR99_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float tailWheelPos) {
        HE_100D.moveGear(hiermesh, leftGearPos, rightGearPos, tailWheelPos, true, HE_1xx.rndgearnull);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float tailWheelPos) {
        HE_100D.moveGear(this.hierMesh(), leftGearPos, rightGearPos, tailWheelPos, this.FM.CT.GearControl > 0.5F, this.rndgear);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos, boolean bDown) {
        HE_100D.moveGear(hiermesh, gearPos, gearPos, gearPos, bDown, HE_1xx.rndgearnull);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos) {
        HE_100D.moveGear(hiermesh, gearPos, gearPos, gearPos, true, HE_1xx.rndgearnull);
    }

    protected void moveGear(float gearPos) {
        HE_100D.moveGear(this.hierMesh(), gearPos, gearPos, gearPos, this.FM.CT.GearControl > 0.5F, this.rndgear);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 90F, -f, 0.0F);
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = HE_1xx.smoothCvt(f, 0.01F, 0.99F, 0.0F, 0.7F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.toLowerCase().startsWith("late")) {
            this.hierMesh().chunkVisible("GunL_MG151_D0", true);
            this.hierMesh().chunkVisible("GunR_MG151_D0", true);
        }
    }

    public float cockpitDoor_;

    static {
        Class class1 = HE_100D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He100");
        Property.set(class1, "meshName", "3do/plane/He-100D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1944.11F);
        Property.set(class1, "yearExpired", 1955F);
        Property.set(class1, "FlightModel", "FlightModels/He-100D-1.fmd:HE1XX_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHE_100.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        int ai[] = new int[5];
        ai[2] = 1;
        Aircraft.weaponTriggersRegister(class1, ai);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03" });
    }
}
