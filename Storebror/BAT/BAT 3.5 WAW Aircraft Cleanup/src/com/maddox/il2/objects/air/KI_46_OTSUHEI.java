package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class KI_46_OTSUHEI extends KI_46 implements TypeFighter, TypeJazzPlayer {

    public KI_46_OTSUHEI() {
        this.bChangedPit = true;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    public boolean hasCourseWeaponBullets() {
        return ((this.FM.CT.Weapons[0] != null) && (this.FM.CT.Weapons[0][0] != null) && (this.FM.CT.Weapons[0][1] != null) && (this.FM.CT.Weapons[0][0].countBullets() != 0)) || (this.FM.CT.Weapons[0][1].countBullets() != 0);
    }

    public boolean hasSlantedWeaponBullets() {
        return (this.FM.AS.astatePilotStates[1] < 100) && (this.FM.CT.Weapons[1] != null) && (this.FM.CT.Weapons[1][0] != null) && (this.FM.CT.Weapons[1][0].countBullets() != 0);
    }

    public Vector3d getAttackVector() {
        return KI_46_OTSUHEI.ATTACK_VECTOR;
    }

    private static final Vector3d ATTACK_VECTOR = new Vector3d(150D, 0.0D, -400D);
    public boolean                bChangedPit;

    static {
        Class class1 = KI_46_OTSUHEI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ki-46");
        Property.set(class1, "meshName", "3DO/Plane/Ki-46(Otsu-Hei)(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-46-IIIKai.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_46_OTSUHEI.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03" });
    }
}
