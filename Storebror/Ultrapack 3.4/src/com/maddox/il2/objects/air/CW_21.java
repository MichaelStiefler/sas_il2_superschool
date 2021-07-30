package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class CW_21 extends CW21xyz {

    public CW_21() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.8F, 0.0F, -82F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.3F, 0.99F, 0.0F, -82F), 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("LLight_D0", 0.0F, f1, 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.21F, 0.63F, 0.0F, 0.09F);
        Aircraft.ypr[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 75F);
        hiermesh.chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) return;
        else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void moveWheelSink() {
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = CW_21.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "CW-21");
        Property.set(class1, "meshName", "3DO/Plane/CW-21(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00du());
        Property.set(class1, "meshName_du", "3DO/Plane/CW-21(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_du", new PaintSchemeFMPar00du());
        Property.set(class1, "yearService", 1940.6F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/CW-21.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitCW_21.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
