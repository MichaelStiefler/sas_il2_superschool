package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class J6K1 extends J6Kxyz {

    public J6K1() {
    }

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 9; i++) {
                this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, -20F * f1, 0.0F);
            }

        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -0.61F);
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
        if (this.FM.CT.Weapons[3] != null) {
            this.hierMesh().chunkVisible("RackL_D0", true);
            this.hierMesh().chunkVisible("RackR_D0", true);
        }
    }

    static {
        Class class1 = J6K1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "J6K1");
        Property.set(class1, "meshName", "3DO/Plane/J6K1(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/J6K1.fmd:J6K1_FM");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit_J6K1.class });
        Property.set(class1, "LOSElevation", 1.0728F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 0, 0, 0, 0, 9, 9, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
