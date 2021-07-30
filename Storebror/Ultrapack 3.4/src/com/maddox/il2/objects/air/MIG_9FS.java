package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class MIG_9FS extends MIG_9 {

    public MIG_9FS() {
        this.nCN37 = -1;
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster() && this.FM.CT.Weapons[1] != null && this.FM.CT.Weapons[1][0] != null) {
            if (this.FM.CT.Weapons[1][0].countBullets() < this.nCN37) {
                if (World.Rnd().nextFloat() < cvt(this.FM.getAltitude(), 3000F, 7000F, 0.0F, 0.1F)) this.FM.EI.engines[0].setEngineStops(this);
                if (World.Rnd().nextFloat() < cvt(this.FM.getAltitude(), 3000F, 7000F, 0.0F, 0.1F)) this.FM.EI.engines[1].setEngineStops(this);
            }
            this.nCN37 = this.FM.CT.Weapons[1][0].countBullets();
        }
    }

    private int nCN37;

    static {
        Class class1 = MIG_9FS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-9");
        Property.set(class1, "meshName", "3DO/Plane/MiG-9FS(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_ru", "3DO/Plane/MiG-9FS(ru)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-9.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_9.class });
        Property.set(class1, "LOSElevation", 0.75635F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03" });
    }
}
