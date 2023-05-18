package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class IL_2I extends IL_2 implements TypeFighter {

    public IL_2I() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.53F);
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
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.75F;
    }

    static {
        Class class1 = IL_2I.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "IL2");
        Property.set(class1, "meshName", "3do/plane/Il-2I(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_ru", "3do/plane/Il-2I/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Il-2I.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitIL_2_1940.class });
        Property.set(class1, "LOSElevation", 0.81F);
        Property.set(class1, "Handicap", 1.1F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Cannon01", "_Cannon02" });
    }
}
