package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Property;

public class BombNebelgerat_S200_V extends Bomb {
    public void start() {
        super.start();
        Aircraft aircraft = (Aircraft) this.getOwner();
        for (int i = 0; i <= aircraft.FM.CT.Weapons.length; i++)
            if (aircraft.FM.CT.Weapons[3] != null && aircraft.getBulletEmitterByHookName("_ExternalBomb0" + (i + 1)) instanceof BombGunNebelgerat_S200_V && Config.isUSE_RENDER()) {
                this.drawing(false);
                com.maddox.il2.engine.Hook hook = this.getOwner().findHook("_ExternalBomb0" + (i + 1));
                Eff3DActor.New(this.getOwner(), hook, new Loc(-0.32D, 0.0D, -0.42D, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FumigeneA_New.eff", 180F);
                PylonNebelgerat_S200_V pylonNebelgerat_S200_V = new PylonNebelgerat_S200_V();
                pylonNebelgerat_S200_V.pos.setBase(aircraft, hook, false);
                pylonNebelgerat_S200_V.pos.resetAsBase();
                pylonNebelgerat_S200_V.drawing(true);
            }

        this.destroy();
    }

    static {
        Class class1 = BombNebelgerat_S200_V.class;
        Property.set(class1, "mesh", "3do/arms/Nebelgerat_S200_V/mono.sim");
        Property.set(class1, "radius", 0.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 362F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
