package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Ba65AA extends Ba_65xyz {

    public Ba65AA() {
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("FlapL_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("FlapR_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 45F * f, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        Math.max(-f * 800F, -70F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 49F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 49F * f, 0.0F);
    }

    protected void moveGear(float f) {
        Ba65AA.moveGear(this.hierMesh(), f);
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.5F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    static {
        Class class1 = Ba65AA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ba65");
        Property.set(class1, "meshName", "3DO/Plane/Ba65/hierAA.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/Ba65/hierAA.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/Ba65AA.fmd:Ba65AA_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBa65A.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 10, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04" });
    }
}
