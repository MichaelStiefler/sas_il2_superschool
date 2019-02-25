package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class MiG_15SB extends Mig_15F implements TypeStormovik {

    public MiG_15SB() {
        this.fxSirena = this.newSound("aircraft.Sirena2", false);
        this.smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        this.sirenaSoundPlaying = false;
        this.smplSirena.setInfinite(true);
    }

    private boolean sirenaWarning() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        Aircraft aircraft1 = War.getNearestEnemy(this, 4000F);
        if ((aircraft1 instanceof Aircraft) && (aircraft1.getArmy() != World.getPlayerArmy()) && (aircraft1 instanceof TypeFighterAceMaker) && (aircraft1 instanceof TypeRadarGunsight) && (aircraft1 != World.getPlayerAircraft()) && (aircraft1.getSpeed(vector3d) > 20D)) {
            this.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + aircraft1.pos.getAbsPoint().x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + aircraft1.pos.getAbsPoint().z;
            new String();
            new String();
            double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
            new String();
            double d7 = d3 - d;
            double d8 = d4 - d1;
            float f = 57.32484F * (float) Math.atan2(d8, -d7);
            int k = (int) (Math.floor((int) f) - 90D);
            if (k < 0) {
                k += 360;
            }
            int l = k - i;
            double d9 = d - d3;
            double d10 = d1 - d4;
            double d11 = Math.sqrt(d6 * d6);
            int i1 = (int) (Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)) / 10D) * 10D);
            float f1 = 57.32484F * (float) Math.atan2(i1, d11);
            int j1 = (int) (Math.floor((int) f1) - 90D);
            if (j1 < 0) {
                j1 += 360;
            }
            int k1 = j1 - j;
            int l1 = (int) (Math.ceil((i1 * 3.2808399000000001D) / 100D) * 100D);
            if (l1 >= 5280) {
                l1 = (int) Math.floor(l1 / 5280);
            }
            this.bRadarWarning = (i1 <= 3000D) && (i1 >= 50D) && (k1 >= 195) && (k1 <= 345) && (Math.sqrt(l * l) >= 120D);
            this.playSirenaWarning(this.bRadarWarning);
        } else {
            this.bRadarWarning = false;
            this.playSirenaWarning(this.bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean flag) {
        if (flag && !this.sirenaSoundPlaying) {
            this.fxSirena.play(this.smplSirena);
            this.sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else if (!flag && this.sirenaSoundPlaying) {
            this.fxSirena.cancel();
            this.sirenaSoundPlaying = false;
        }
    }

    public void update(float f) {
        super.update(f);
        if (((World.getPlayerRegiment().country() == "ru") || (World.getPlayerRegiment().country() == "kx") || (World.getPlayerRegiment().country() == "kp")) && (Mission.curYear() >= 1952)) {
            this.sirenaWarning();
        }
    }

    private SoundFX fxSirena;
    private Sample  smplSirena;
    private boolean sirenaSoundPlaying;
    private boolean bRadarWarning;

    static {
        Class class1 = MiG_15SB.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-15");
        Property.set(class1, "meshName_ru", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar1956());
        Property.set(class1, "meshName_sk", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_ro", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName_hu", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
        Property.set(class1, "meshName", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1949.9F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-15F.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMig_15F.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb05", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29",
                "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock41", "_ExternalRock42", "_ExternalRock42", "_ExternalRock43", "_ExternalRock43", "_ExternalRock44", "_ExternalRock44" });
    }
}
