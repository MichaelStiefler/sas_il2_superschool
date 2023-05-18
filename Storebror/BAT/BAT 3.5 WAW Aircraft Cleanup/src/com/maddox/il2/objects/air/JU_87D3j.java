package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.PylonWB151;
import com.maddox.il2.objects.weapons.PylonWB20;
import com.maddox.il2.objects.weapons.PylonWB81A;
import com.maddox.il2.objects.weapons.PylonWB81B;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class JU_87D3j extends JU_87D3 implements TypeStormovik {

    public JU_87D3j() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.58F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
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
        this.FM.CT.dvCockpitDoor = 0.65F;
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if ((aobj[i] instanceof PylonWB81B) || (aobj[i] instanceof PylonWB81A) || (aobj[i] instanceof PylonWB20) || (aobj[i] instanceof PylonWB151)) {
                    this.FM.M.massEmpty += 190F;
                    break;
                }
                i++;
            } while (true);
        }
    }

    static {
        Class class1 = JU_87D3j.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87D-3j.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87D-3/hier_D1.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87D3.class, CockpitJU_87D3_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_MGUN17", "_MGUN18", "_MGUN19", "_MGUN20", "_MGUN21", "_MGUN22", "_MGUN23", "_MGUN24", "_MGUN25", "_MGUN26", "_MGUN27", "_MGUN28", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON10", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04" });
    }
}
