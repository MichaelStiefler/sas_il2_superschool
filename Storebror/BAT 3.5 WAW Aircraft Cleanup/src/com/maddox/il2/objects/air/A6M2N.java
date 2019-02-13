package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class A6M2N extends A6M implements TypeSeaPlane {

    public A6M2N() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
    }

    protected void moveGear(float f, float f1, float f2) {
    }

    protected void moveGear(float f) {
    }

    public void moveWheelSink() {
    }

    public void moveSteering(float f) {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        super.hitBone(s, shot, point3d);
        if (s.startsWith("xgearc")) {
            this.hitChunk("GearC2", shot);
        }
        if (s.startsWith("xgearl")) {
            this.hitChunk("GearL2", shot);
        }
        if (s.startsWith("xgearr")) {
            this.hitChunk("GearR2", shot);
        }
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }
            }

        }

    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, -30F * f, 0.0F);
    }

    private static Point3d tmpp = new Point3d();

    static {
        Class class1 = A6M2N.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A6M");
        Property.set(class1, "meshName", "3DO/Plane/A6M2-N(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/A6M2-N(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/A6M2N.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA6M2N.class} );
        Property.set(class1, "LOSElevation", 1.01885F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3, 9, 9, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb04" });
        weaponsRegister(class1, "default", new String[] { "MGunMG15sipzl 1000", "MGunMG15sipzl 1000", "MGunMGFFk 60", "MGunMGFFk 60", null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x60", new String[] { "MGunMG15sipzl 1000", "MGunMG15sipzl 1000", "MGunMGFFk 60", "MGunMGFFk 60", null, null, null, "PylonA6MPLN2", "PylonA6MPLN2", "BombGun50kg", "BombGun50kg" });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null });
    }
}
