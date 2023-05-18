package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class AR_234B2NJ extends AR_234F implements TypeX4Carrier, TypeRadarLiSN2Carrier {

    public AR_234B2NJ() {
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.radarGain = 50;
        this.radarMode = 0;
        this.booster = new Bomb[2];
        this.bHasBoosters = true;
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
        for (int i = 0; i < 2; i++) {
            try {
                this.booster[i] = new BombStarthilfe109500();
                ((Actor) (this.booster[i])).pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
                ((Actor) (this.booster[i])).pos.resetAsBase();
                this.booster[i].drawing(true);
            } catch (Exception exception) {
                this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
            }
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

    public void typeX4CAdjSidePlus() {
        this.radarMode++;
        if (this.radarMode > 1) {
            this.radarMode = 0;
        }
    }

    public void typeX4CAdjSideMinus() {
        this.radarMode--;
        if (this.radarMode < 0) {
            this.radarMode = 1;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        this.radarGain += 10;
        if (this.radarGain > 100) {
            this.radarGain = 100;
        }
    }

    public void typeX4CAdjAttitudeMinus() {
        this.radarGain -= 10;
        if (this.radarGain < 0) {
            this.radarGain = 0;
        }
    }

    public void typeX4CResetControls() {
        this.radarGain = 50;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void setCurPilot(int theCurPilot) {
        System.out.println("### Attempt to set Pilot Index on single crew plane!!! ###");
    }

    public int getCurPilot() {
        return 1;
    }

    public int getRadarGain() {
        return this.radarGain;
    }

    public int getRadarMode() {
        return this.radarMode;
    }

    private float     deltaAzimuth;
    private float     deltaTangage;
    private int       radarGain;
    private int       radarMode;
    private Bomb      booster[];
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;

    static {
        Class class1 = AR_234B2NJ.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ar 234");
        Property.set(class1, "meshName", "3DO/Plane/Ar-234B-2NJ/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.8F);
        Property.set(class1, "FlightModel", "FlightModels/Ar-234B-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAR_234B2NJ.class });
        Property.set(class1, "LOSElevation", 1.14075F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalDev02" });
    }
}
