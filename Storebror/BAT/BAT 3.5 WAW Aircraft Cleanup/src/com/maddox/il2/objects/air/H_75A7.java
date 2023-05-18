package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class H_75A7 extends P_36 {

    public H_75A7() {
        this.kangle = 0.0F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        for (int i = 1; i < 12; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        super.update(f);
    }

    private float kangle;

    static {
        Class class1 = H_75A7.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "H-75");
        Property.set(class1, "meshName", "3DO/Plane/Hawk75A-7(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-36A-4.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHawk_75A3.class });
        Property.set(class1, "LOSElevation", 1.06965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06" });
    }
}
