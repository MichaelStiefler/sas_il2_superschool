package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Fury_S extends ParasolX {

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        this.resetYPRmodifier();
        Aircraft.xyz[2] = f1 * -0.45F;
        this.hierMesh().chunkSetLocate("Water_D0", Aircraft.xyz, Aircraft.ypr);
        super.update(f);
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Goertz_D0", false);
            } else {
                this.hierMesh().chunkVisible("Goertz_D0", true);
            }
        }
    }

    static {
        Class class1 = Fury_S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FuryS");
        Property.set(class1, "meshName", "3DO/Plane/Fury/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/Fury/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/FuryS.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFury_S.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
