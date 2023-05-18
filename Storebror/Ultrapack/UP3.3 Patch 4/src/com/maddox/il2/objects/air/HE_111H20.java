package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.objects.weapons.BombPC1600;
import com.maddox.il2.objects.weapons.BombSC1000;
import com.maddox.il2.objects.weapons.BombSC1800;
import com.maddox.il2.objects.weapons.BombSC2000;
import com.maddox.il2.objects.weapons.BombSC2500;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class HE_111H20 extends HE_111xyz implements TypeRocketBoost {

    public HE_111H20() {
        this.booster = new BombStarthilfe109500[2];
        this.boosterEffects = new Eff3DActor[2];
        this.boostState = AircraftState._AS_BOOST_NOBOOST;
        this.boosterFireOutTime = -1L;
    }

    public void setBoostState(int state) {
        if (this.boostState == state) return; // Nothing to do here, we are in the requested state already.
        if (((state ^ this.boostState) & AircraftState._AS_BOOST_EXISTS) != 0) { // The existence of boosters has changed
            if ((state & AircraftState._AS_BOOST_EXISTS) != 0) { // Boosters exist now
                this.doAttachBoosters();
            } else { // Boosters don't exist anymore
                this.doCutBoosters();
            }
        }
        if (((state ^ this.boostState) & AircraftState._AS_BOOST_ACTIVE) != 0) { // The boosters activity state changed
            if ((state & AircraftState._AS_BOOST_ACTIVE) != 0) { // Boosters are active now
                this.doFireBoosters();
            } else { // Boosters aren't active
                this.doShutoffBoosters();
            }
        }
        this.boostState = state;
    }

    public int getBoostState() {
        return this.boostState;
    }

    public void doAttachBoosters() {
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
    
    public void doCutBoosters() {
      for (int i = 0; i < 2; i++) {
          if (this.booster[i] != null) {
              this.booster[i].start();
              this.booster[i] = null;
          }
      }
      this.stopBoosterSound();
    }

    public void doFireBoosters() {
        for (int i=0; i<2; i++) {
            this.boosterEffects[i] = Eff3DActor.New(this, this.findHook("_Booster" + (i+1)), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
        }
        this.startBoosterSound();
    }

    public void doShutoffBoosters() {
        for (int i=0; i<2; i++) {
            Eff3DActor.finish(this.boosterEffects[i]); // No null checks etc. required here, it's done internally already.
        }
        this.stopBoosterSound();
    }

    public void startBoosterSound() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].startSound();
            }
        }
    }

    public void stopBoosterSound() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].stopSound();
            }
        }
    }
    
    private void boostUpdate() {
        if (!(this.FM instanceof Pilot)) return;
        if ((this.boostState & AircraftState._AS_BOOST_EXISTS) == 0) return;
            // TODO: Changed Booster cutoff reasons from absolute altitude to altitude above ground
            if (this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y) > 300F && this.boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
                this.FM.AS.setBoostState(this, AircraftState._AS_BOOST_NOBOOST);
                this.FM.AS.setGliderBoostOff();
            }
            if (this.boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[1].getStage() == 6 && this.FM.getSpeedKMH() > 20F) {
                this.boosterFireOutTime = Time.current() + 30000L;
                this.FM.AS.setBoostState(this, this.boostState | AircraftState._AS_BOOST_ACTIVE);
                this.FM.AS.setGliderBoostOn();
            }
            if (this.boosterFireOutTime > 0L) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 20000D;
                } else { // Stop sound
                    this.FM.AS.setBoostState(this, this.boostState & ~AircraftState._AS_BOOST_ACTIVE);
                }
                if (Time.current() > this.boosterFireOutTime + 10000L) { // cut boosters 10 seconds after burnout regardless altitude if not done so before.
                    this.FM.AS.setBoostState(this, AircraftState._AS_BOOST_NOBOOST);
                    this.FM.AS.setGliderBoostOff();
                }
            }
    }
    
    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        if (!this.isNetMaster()) return; // FIXME: Maybe FM.AS.isMaster() works better? Idea is to deal with "setOnGround" in single player missions only, or in Dogfight missions on server side only.
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) break;
                if (aobj[i] instanceof BombSC1000 || aobj[i] instanceof BombSC2000 || aobj[i] instanceof BombPC1600 || aobj[i] instanceof BombSC1800 || aobj[i] instanceof BombSC2500) {
                    this.FM.AS.setBoostState(this, this.boostState | AircraftState._AS_BOOST_EXISTS);
                    break;
                }
                i++;
            } while (true);
        }
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }
    
    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                this.FM.AS.setBoostState(this, AircraftState._AS_BOOST_NOBOOST);
                this.FM.AS.setGliderBoostOff();
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        this.boostUpdate();
    }

    private BombStarthilfe109500      booster[];
    private Eff3DActor                    boosterEffects[];
    private int           boostState;
    protected long        boosterFireOutTime;

    static {
        Class class1 = HE_111H20.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111H-20/hier_H20.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111H-20.fmd");
        Property.set(class1, "cockpitClass",
                new Class[] { CockpitHE_111H20.class, CockpitHE_111H20_Bombardier.class, CockpitHE_111H20_NGunner.class, CockpitHE_111H20_TGunner.class, CockpitHE_111H20_BGunner.class, CockpitHE_111H20_LGunner.class, CockpitHE_111H20_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
                3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03",
                        "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb09",
                        "_ExternalBomb10", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb14", "_ExternalDev01", "_ExternalDev02",
                        "_ExternalBomb15", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb20",
                        "_ExternalBomb21", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb26",
                        "_ExternalBomb27", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb32",
                        "_ExternalBomb33", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb34" });
    }
}
