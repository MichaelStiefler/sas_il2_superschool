package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.il2.objects.weapons.MGunBredaSAFAT127k;
import com.maddox.il2.objects.weapons.MGunMG15120MGsii;
import com.maddox.il2.objects.weapons.MGunShVAKsii;
import com.maddox.il2.objects.weapons.MGunType992sii;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Dragonfly39m extends Dragonfly39nm {

    public Dragonfly39m() {
        this.bHasBoosters = false;
        this.boosterFireOutTime = -1L;
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
        Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doCutBoosters() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].start();
                this.booster[i] = null;
            }
        }

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getGunByHookName("_CANNON01") instanceof MGunShVAKsii) {
            this.FM.M.massEmpty -= 64F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunMG15120MGsii) {
            this.FM.M.massEmpty -= 84F;
        }
        if (this.getGunByHookName("_MGUN03") instanceof MGunBredaSAFAT127k) {
            this.FM.M.massEmpty += 48F;
        }
        if (this.getGunByHookName("_CANNON01") instanceof MGunType992sii) {
            this.FM.M.massEmpty -= 56F;
        }
        if (this.thisWeaponsName.endsWith("X")) {
            this.hierMesh().chunkVisible("GearL3_D0", false);
            this.hierMesh().chunkVisible("GearR3_D0", false);
        }
        if (this.thisWeaponsName.endsWith("RATO")) {
            this.bHasBoosters = true;
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
            }
            if (this.bHasBoosters && (this.boosterFireOutTime > 0L)) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 20000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 10000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
        }
    }

    private Bomb      booster[] = { null, null };
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;

    static {
        Class class1 = Dragonfly39m.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly39m");
        Property.set(class1, "meshName", "3DO/Plane/Dragonfly39n/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly39n.fmd:Dragonfly39naval_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly39n.class });
        Property.set(class1, "LOSElevation", 0.8941F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev11", "_ExternalDev12", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev51", "_ExternalDev52", "_ExternalDev53", "_ExternalDev54", "_ExternalDev55", "_ExternalDev56", "_ExternalDev57", "_ExternalDev58", "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58", "_ExternalDev61", "_ExternalDev62", "_ExternalRock61", "_ExternalRock62",
                "_ExternalRock63", "_ExternalRock64", "_ExternalRock65", "_ExternalRock66", "_ExternalRock67", "_ExternalRock68", "_ExternalDev71", "_ExternalDev72", "_ExternalRock71", "_ExternalRock72" });
    }
}
