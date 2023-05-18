package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class ME_210CA1ZSTR extends ME_210 implements TypeFighter, TypeBNZFighter, TypeStormovik, TypeStormovikArmored {

    public ME_210CA1ZSTR() {
    }

    protected void moveBayDoor(float f) {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = ME_210CA1ZSTR.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me-210");
        Property.set(class1, "meshName", "3DO/Plane/Me-210Ca-1Zerstorer/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-210Ca-1.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_210CA1ZSTR.class });
        Property.set(class1, "LOSElevation", 0.66895F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 10, 10, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_MGUN03", "_MGUN04", "_CANNON03" });
    }
}
