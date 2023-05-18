package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class VickersFB19MkII extends VickersFB19 implements TypeFighter {

    public VickersFB19MkII() {
    }

    public void update(float f) {
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER() && (this.FM.CT.PowerControl > 0.95F) && (this.FM.EI.engines[0].getRPM() > 400F)) {
            this.FM.AS.setSootState(this, 0, 1);
        } else {
            this.FM.AS.setSootState(this, 0, 0);
        }
        super.update(f);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            VickersFB19MkII.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            VickersFB19MkII.bChangedPit = true;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;
        }
    }

    static Class mthclass$(String s) {
        Class class1;
        try {
            class1 = Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = VickersFB19MkII.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "VickersFB19MkII");
        Property.set(class1, "meshName", "3DO/Plane/VickersFB.19.MkII/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1933F);
        Property.set(class1, "yearExpired", 1967F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitVickersFB19MkII.class });
        Property.set(class1, "FlightModel", "FlightModels/VickersFB19MkII.fmd:VickersFB19_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
