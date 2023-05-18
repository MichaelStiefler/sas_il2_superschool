package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombPC1600;
import com.maddox.il2.objects.weapons.BombSC1000;
import com.maddox.il2.objects.weapons.BombSC1800;
import com.maddox.il2.objects.weapons.BombSC2000;
import com.maddox.il2.objects.weapons.BombSC2500;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class HE_111H20 extends HE_111xyz {

    public HE_111H20() {
        this.booster = new Bomb[2];
        this.bHasBoosters = true;
        this.boosterFireOutTime = -1L;
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if ((aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2500)) {
                    Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
                    Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
                    break;
                }
                i++;
            } while (true);
        }
    }

    public void doCutBoosters() {
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if ((aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2500)) {
                    for (int j = 0; j < 2; j++) {
                        if (this.booster[j] != null) {
                            this.booster[j].start();
                            this.booster[j] = null;
                        }
                    }

                    break;
                }
                i++;
            } while (true);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if ((aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2500)) {
                    for (int j = 0; j < 2; j++) {
                        try {
                            this.booster[j] = new BombStarthilfe109500();
                            ((Actor) (this.booster[j])).pos.setBase(this, this.findHook("_BoosterH" + (j + 1)), false);
                            ((Actor) (this.booster[j])).pos.resetAsBase();
                            this.booster[j].drawing(true);
                        } catch (Exception exception) {
                            this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
                        }
                    }

                    break;
                }
                i++;
            } while (true);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        if ((this.FM instanceof Pilot) && this.bHasBoosters) {
            if ((this.FM.getAltitude() > 300F) && (this.boosterFireOutTime == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
            }
            if (this.bHasBoosters && (this.boosterFireOutTime == -1L) && this.FM.Gears.onGround() && (this.FM.EI.getPowerOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[1].getStage() == 6) && (this.FM.getSpeedKMH() > 20F)) {
                Object aobj[] = this.pos.getBaseAttached();
                if (aobj != null) {
                    int i = 0;
                    do {
                        if (i >= aobj.length) {
                            break;
                        }
                        if ((aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2500)) {
                            this.boosterFireOutTime = Time.current() + 30000L;
                            this.doFireBoosters();
                            this.FM.AS.setGliderBoostOn();
                            break;
                        }
                        i++;
                    } while (true);
                } else {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
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

    protected boolean boostersEnable;
    private Bomb      booster[];
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;

    static {
        Class class1 = HE_111H20.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111H-20/hier_H20.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111H-6.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHE_111H20.class, CockpitHE_111H20_Bombardier.class, CockpitHE_111H20_NGunner.class, CockpitHE_111H20_TGunner.class, CockpitHE_111H20_BGunner.class, CockpitHE_111H20_LGunner.class, CockpitHE_111H20_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb14", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb15", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb21", "_ExternalBomb22",
                "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb32", "_ExternalBomb33", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb34" });
    }
}
