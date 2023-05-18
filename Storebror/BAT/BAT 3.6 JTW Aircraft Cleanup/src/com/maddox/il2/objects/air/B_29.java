package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B_29 extends B_29X implements TypeBomber, TypeX4Carrier, TypeGuidedBombCarrier {

    public B_29() {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.killPilot(this, 4);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                break;

            case 3:
                this.FM.turret[1].setHealth(f);
                break;

            case 4:
                this.FM.turret[2].setHealth(f);
                break;

            case 5:
                this.FM.turret[3].setHealth(f);
                this.FM.turret[4].setHealth(f);
                break;
        }
    }

    static {
        Class class1 = B_29.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-29");
        Property.set(class1, "meshName", "3DO/Plane/B-29(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/B-29(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-29.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB29.class, CockpitB29_Bombardier.class, CockpitB29_TGunner.class, CockpitB29_T2Gunner.class, CockpitB29_FGunner.class, CockpitB29_RGunner.class, CockpitB29_AGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04" });
    }
}
