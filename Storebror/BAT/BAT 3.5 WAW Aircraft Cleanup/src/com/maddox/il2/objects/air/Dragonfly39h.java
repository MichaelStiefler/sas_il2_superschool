package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Dragonfly39h extends Dragonfly39hx {

    public Dragonfly39h() {
        this.bHasBoosters = false;
        this.bHasBoosters2 = false;
        this.boosterFireOutTime = -1L;
        this.boosterFireOutTime2 = -1L;
        this.bStartBoosters2 = false;
        this.bPrepBoosters2 = false;
    }

    public void destroy() {
        this.doCutBoosters();
        this.doCutBoosters2();
        super.destroy();
    }

    public void doFireBoosters() {
        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
        Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doFireBoosters2() {
        Eff3DActor.New(this, this.findHook("_Booster3"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
        Eff3DActor.New(this, this.findHook("_Booster4"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doCutBoosters() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].start();
                this.booster[i] = null;
            }
        }

    }

    public void doCutBoosters2() {
        for (int i = 2; i < 4; i++) {
            if (this.booster[i] != null) {
                this.booster[i].start();
                this.booster[i] = null;
            }
        }

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("ru")) {
            this.FM.M.massEmpty -= 64F;
        }
        if (this.thisWeaponsName.startsWith("ge1")) {
            this.FM.M.massEmpty -= 148F;
        }
        if (this.thisWeaponsName.startsWith("ge2")) {
            this.FM.M.massEmpty -= 84F;
        }
        if (this.thisWeaponsName.startsWith("it1")) {
            this.FM.M.massEmpty -= 100F;
        }
        if (this.thisWeaponsName.startsWith("it2")) {
            this.FM.M.massEmpty -= 136F;
        }
        if (this.thisWeaponsName.startsWith("ijn1")) {
            this.FM.M.massEmpty -= 92F;
        }
        if (this.thisWeaponsName.startsWith("ijn2")) {
            this.FM.M.massEmpty -= 56F;
        }
        if (this.thisWeaponsName.endsWith("RATO")) {
            this.bHasBoosters = true;
            this.bHasBoosters2 = true;
            for (int i = 0; i < 2; i++) {
                try {
                    this.booster[i] = new BombStarthilfe109500();
                    this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
                    this.booster[i].pos.resetAsBase();
                    this.booster[i].drawing(true);
                } catch (Exception exception) {
                    this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
                }
            }

            for (int j = 2; j < 4; j++) {
                try {
                    this.booster[j] = new BombStarthilfe109500();
                    this.booster[j].pos.setBase(this, this.findHook("_BoosterH" + (j + 1)), false);
                    this.booster[j].pos.resetAsBase();
                    this.booster[j].drawing(true);
                } catch (Exception exception1) {
                    this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
                }
            }

        }
    }

    public void update(float f) {
        super.update(f);
        if (!(this.FM instanceof Pilot)) {
            return;
        }
        if (this.bHasBoosters) {
            if ((this.FM.getAltitude() > 300F) && (this.boosterFireOutTime == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
            }
            if (this.bHasBoosters && (this.boosterFireOutTime == -1L) && this.FM.Gears.onGround() && (this.FM.EI.getPowerOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.getSpeedKMH() > 20F)) {
                this.boosterFireOutTime = Time.current() + 30000L;
                this.doFireBoosters();
                this.FM.AS.setGliderBoostOn();
                this.bPrepBoosters2 = true;
            }
            if (this.bHasBoosters && (this.boosterFireOutTime > 0L)) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 20000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 10000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                    this.bStartBoosters2 = true;
                }
            }
        }
        if (this.bHasBoosters2) {
            if (!this.bPrepBoosters2 && (this.FM.getAltitude() > 300F) && (this.boosterFireOutTime2 == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
                this.doCutBoosters2();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters2 = false;
            }
            if (this.bHasBoosters2 && this.bStartBoosters2 && (this.boosterFireOutTime2 == -1L)) {
                this.boosterFireOutTime2 = Time.current() + 30000L;
                this.doFireBoosters2();
                this.FM.AS.setGliderBoostOn();
            }
            if (this.bHasBoosters2 && (this.boosterFireOutTime2 > 0L)) {
                if (Time.current() < this.boosterFireOutTime2) {
                    this.FM.producedAF.x += 20000D;
                }
                if (Time.current() > (this.boosterFireOutTime2 + 10000L)) {
                    this.doCutBoosters2();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters2 = false;
                }
            }
        }
    }

    private Bomb      booster[] = { null, null, null, null };
    protected boolean bHasBoosters;
    protected boolean bHasBoosters2;
    protected boolean bStartBoosters2;
    protected boolean bPrepBoosters2;
    protected long    boosterFireOutTime;
    protected long    boosterFireOutTime2;

    static {
        Class class1 = Dragonfly39h.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly39h");
        Property.set(class1, "meshName", "3DO/Plane/Dragonfly39h/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly39h.fmd:Dragonfly39naval_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly39h.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev11", "_ExternalDev12", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev51", "_ExternalDev52", "_ExternalDev53", "_ExternalDev54", "_ExternalDev55", "_ExternalDev56", "_ExternalDev57", "_ExternalDev58", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58", "_ExternalDev61", "_ExternalDev62", "_ExternalRock61", "_ExternalRock62",
                "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", "_ExternalRock68" });
    }
}
