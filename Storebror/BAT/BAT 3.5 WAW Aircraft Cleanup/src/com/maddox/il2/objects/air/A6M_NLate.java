package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class A6M_NLate extends JC_A6M implements TypeSailPlane {

    public A6M_NLate() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
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
                    A6M_NLate.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    A6M_NLate.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(A6M_NLate.tmpp);
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
        Class class1 = A6M_NLate.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A6M");
        Property.set(class1, "meshName", "3DO/Plane/A6M-NLate(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ja", "3DO/Plane/A6M-NLate(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1944.5F);
        Property.set(class1, "FlightModel", "FlightModels/A6M2N.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA6M2N.class });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 3, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalDev01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
