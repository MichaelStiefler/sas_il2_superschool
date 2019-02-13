package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.RocketGunX4H;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class J7W2_A extends J7Wx implements TypeX4Carrier {

    public J7W2_A() {
        this.bChangedPit = true;
        this.arrestor2 = 0.0F;
        this.arrestor = 0.0F;
        this.bHasBoosters = true;
        this.boosterFireOutTime = -1L;
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.missilesList = new ArrayList();
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (((super.FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode()) || !flag || !(super.FM instanceof Pilot)) {
            return;
        }
        if (this.missilesList.isEmpty()) {
            return;
        }
        Pilot pilot = (Pilot) super.FM;
        if ((Time.current() > (this.tX4Prev + 10000L)) && ((pilot.get_maneuver() == 27) || (pilot.get_maneuver() == 62) || (pilot.get_maneuver() == 63)) && (((Maneuver) (pilot)).target != null)) {
            Point3d point3d = new Point3d(((FlightModelMain) (((Maneuver) (pilot)).target)).Loc);
            point3d.sub(((FlightModelMain) (super.FM)).Loc);
            ((FlightModelMain) (super.FM)).Or.transformInv(point3d);
            if (pilot.get_maneuver() != 63 ? (((Tuple3d) (point3d)).x > (((FlightModelMain) (super.FM)).CT.Weapons[0][1].countBullets() <= 20 ? 350D : 800D)) && (((Tuple3d) (point3d)).x < (1500D + (250D * ((FlightModelMain) (super.FM)).Skill))) : ((((Tuple3d) (point3d)).x > 2000D) && (((Tuple3d) (point3d)).x < 3500D)) || ((((Tuple3d) (point3d)).x > 100D) && (((Tuple3d) (point3d)).x < 3000D) && (World.Rnd().nextFloat() < 0.33F))) {
                double d = Math.pow(((Tuple3d) (point3d)).x / 1500D, 2D) * 1500D;
                if ((((Tuple3d) (point3d)).y < d) && (((Tuple3d) (point3d)).y > -d) && (((Tuple3d) (point3d)).z < d) && (((Tuple3d) (point3d)).z > -d) && ((pilot.get_maneuver() != 63) || (((Tuple3d) (point3d)).x < 2500D) || (((((Tuple3d) (point3d)).y * 2D) < ((Tuple3d) (point3d)).x) && ((((Tuple3d) (point3d)).y * 2D) > -((Tuple3d) (point3d)).x) && ((((Tuple3d) (point3d)).z * 2D) < ((Tuple3d) (point3d)).x) && ((((Tuple3d) (point3d)).z * 2D) > -((Tuple3d) (point3d)).x)))) {
                    Orientation orientation = new Orientation();
                    Orientation orientation1 = new Orientation();
                    super.FM.getOrient(orientation);
                    ((Maneuver) (pilot)).target.getOrient(orientation1);
                    float f1 = Math.abs(orientation.getAzimut() - orientation1.getAzimut()) % 360F;
                    f1 = f1 <= 180F ? f1 : 360F - f1;
                    f1 = f1 <= 90F ? f1 : 180F - f1;
                    float f2 = Math.abs(orientation.getTangage() - orientation1.getTangage()) % 360F;
                    f2 = f2 <= 180F ? f2 : 360F - f2;
                    f2 = f2 <= 90F ? f2 : 180F - f2;
                    double d1 = (((Tuple3d) (point3d)).x * (5 - ((FlightModelMain) (super.FM)).Skill)) / (((Maneuver) (pilot)).target.getSpeed() + 1.0F);
                    if ((f1 < d1) && (f2 < d1)) {
                        this.launchMsl();
                        this.tX4Prev = Time.current();
                        Voice.speakAttackByRockets(this);
                    }
                }
            }
        }
    }

    public void createMissilesList() {
        for (int i = 0; i < ((FlightModelMain) (super.FM)).CT.Weapons.length; i++) {
            if (((FlightModelMain) (super.FM)).CT.Weapons[i] != null) {
                for (int j = 0; j < ((FlightModelMain) (super.FM)).CT.Weapons[i].length; j++) {
                    if (((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof RocketGunX4H) {
                        this.missilesList.add(((FlightModelMain) (super.FM)).CT.Weapons[i][j]);
                    }
                }

            }
        }

    }

    public void launchMsl() {
        if (this.missilesList.isEmpty()) {
            return;
        } else {
            ((RocketGunX4H) this.missilesList.remove(0)).shots(1);
            return;
        }
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doCutBoosters() {
        for (int i = 0; i < 1; i++) {
            if (this.booster[i] != null) {
                this.booster[i].start();
                this.booster[i] = null;
            }
        }

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.missilesList.clear();
        this.createMissilesList();
        if (this.thisWeaponsName.startsWith("light")) {
            this.FM.M.massEmpty -= 70F;
        }
        if (this.thisWeaponsName.startsWith("heavy")) {
            this.FM.M.massEmpty += 120F;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33: // '!'
            case 34: // '"'
            case 35: // '#'
            case 36: // '$'
            case 37: // '%'
            case 38: // '&'
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (super.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (super.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    public void moveArrestorHook(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -1.545F * f;
        Aircraft.ypr[1] = -this.arrestor;
        this.hierMesh().chunkSetLocate("Hook1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void update(float f) {
        super.update(f);
        if (Config.isUSE_RENDER() && this.FM.AS.isMaster()) {
            if ((this.FM.EI.engines[0].getPowerOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.95F) {
                    this.FM.AS.setSootState(this, 0, 3);
                } else {
                    this.FM.AS.setSootState(this, 0, 2);
                }
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
        if (this.FM.AS.isMaster() && (this.FM.AS.astateBailoutStep == 2)) {
            super.FM.EI.engines[0].setEngineDies(this);
        }
        float f1 = ((FlightModelMain) (super.FM)).CT.getArrestor();
        float f2 = 81F * f1 * f1 * f1 * f1 * f1 * f1 * f1;
        if (f1 > 0.01F) {
            if (((FlightModelMain) (super.FM)).Gears.arrestorVAngle != 0.0F) {
                this.arrestor = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.arrestorVAngle, -f2, f2, -f2, f2);
                this.moveArrestorHook(f1);
                if (((FlightModelMain) (super.FM)).Gears.arrestorVAngle >= -81F) {
                    ;
                }
            } else {
                float f3 = 58F * ((FlightModelMain) (super.FM)).Gears.arrestorVSink;
                if ((f3 > 0.0F) && (super.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, ((FlightModelMain) (super.FM)).Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                this.arrestor += f3;
                if (this.arrestor > f2) {
                    this.arrestor = f2;
                }
                if (this.arrestor < -f2) {
                    this.arrestor = -f2;
                }
                this.moveArrestorHook(f1);
            }
        }
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
                    this.FM.producedAF.x += 10000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 10000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
        }
    }

    public void doSetSootState(int i, int j) {
        for (int k = 0; k < 2; k++) {
            if (this.FM.AS.astateSootEffects[i][k] != null) {
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            }
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        switch (j) {
            case 1: // '\001'
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.8F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_02"), null, 1.8F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                break;

            case 3: // '\003'
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.8F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                // fall through

            case 2: // '\002'
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.4F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                break;

            case 5: // '\005'
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 3F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
                // fall through

            case 4: // '\004'
                this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                break;
        }
    }

    private Bomb      booster[] = { null };
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;
    private float     arrestor;
    public boolean    bChangedPit;
    protected float   arrestor2;
    public boolean    bToFire;
    private long      tX4Prev;
    private float     deltaAzimuth;
    private float     deltaTangage;
    private ArrayList missilesList;

    static {
        Class class1 = J7W2_A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "J7W");
        Property.set(class1, "meshName", "3DO/Plane/J7W2-A/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/J7W2_A.fmd:J7W_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJ7W.class });
        Property.set(class1, "LOSElevation", 1.0151F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Cannon01", "_Cannon02", "_Cannon03", "_Cannon04", "_Externalbomb01", "_Externalbomb02", "_Externalbomb03", "_Externalbomb04", "_Externaldev01", "_Externaldev02", "_Externaldev03", "_Externaldev04", "_Externaldev05", "_Externaldev06", "_X401", "_X402", "_X403", "_X403", "_Externaldev07", "_Externaldev08" });
    }
}
