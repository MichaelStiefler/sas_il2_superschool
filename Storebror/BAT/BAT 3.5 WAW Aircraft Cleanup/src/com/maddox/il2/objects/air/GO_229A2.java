package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.FMMath;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.RocketGunX4;
import com.maddox.il2.objects.weapons.RocketGunX4homing;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class GO_229A2 extends GO_229 implements TypeFighter, TypeBNZFighter, TypeX4Carrier, TypeStormovik {

    public GO_229A2() {
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.missilesList = new ArrayList();
        this.homing = false;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !flag || !(this.FM instanceof Pilot)) {
            return;
        }
        if (this.missilesList.isEmpty()) {
            return;
        }
        Pilot pilot = (Pilot) this.FM;
        if ((Time.current() > (this.tX4Prev + 10000L)) && ((pilot.get_maneuver() == 27) || (pilot.get_maneuver() == 62) || (pilot.get_maneuver() == 63)) && (((Maneuver) (pilot)).target != null)) {
            Point3d point3d = new Point3d(((FlightModelMain) (((Maneuver) (pilot)).target)).Loc);
            point3d.sub(this.FM.Loc);
            this.FM.Or.transformInv(point3d);
            if (this.homing) {
                if (pilot.get_maneuver() != 63 ? (point3d.x > (this.FM.CT.Weapons[0][1].countBullets() <= 20 ? 350D : 800D)) && (point3d.x < (3500D + (500D * this.FM.Skill))) : ((point3d.x > 7000D) && (point3d.x < 10000D)) || ((point3d.x > 350D) && (point3d.x < 9000D) && (World.Rnd().nextFloat() < 0.33F))) {
                    double d = Math.pow(point3d.x / 5000D, 2D) * 5000D;
                    if ((point3d.y < d) && (point3d.y > -d) && (point3d.z < d) && (point3d.z > -d) && ((pilot.get_maneuver() != 63) || (point3d.x < 7000D) || (((point3d.y * 2D) < point3d.x) && ((point3d.y * 2D) > -point3d.x) && ((point3d.z * 2D) < point3d.x) && ((point3d.z * 2D) > -point3d.x)))) {
                        Orientation orientation2 = new Orientation();
                        Orientation orientation3 = new Orientation();
                        this.FM.getOrient(orientation2);
                        ((Maneuver) (pilot)).target.getOrient(orientation3);
                        float f3 = Math.abs(orientation2.getAzimut() - orientation3.getAzimut()) % 360F;
                        f3 = f3 <= 180F ? f3 : 360F - f3;
                        f3 = f3 <= 90F ? f3 : 180F - f3;
                        float f4 = Math.abs(orientation2.getTangage() - orientation3.getTangage()) % 360F;
                        f4 = f4 <= 180F ? f4 : 360F - f4;
                        f4 = f4 <= 90F ? f4 : 180F - f4;
                        double d2 = (point3d.x * (5 - this.FM.Skill)) / (((Maneuver) (pilot)).target.getSpeed() + 1.0F);
                        if ((f3 < d2) && (f4 < d2)) {
                            this.launchMsl();
                            this.tX4Prev = Time.current();
                            Voice.speakAttackByRockets(this);
                        }
                    }
                }
            } else if ((pilot.get_maneuver() != 63 ? (point3d.x > (this.FM.CT.Weapons[0][1].countBullets() <= 20 ? 350D : 800D)) && (point3d.x < (2000D + (1000D * this.FM.Skill))) : ((point3d.x > 4000D) && (point3d.x < 5500D)) || ((point3d.x > 100D) && (point3d.x < 5000D) && (World.Rnd().nextFloat() < 0.33F))) && (((point3d.y * 1.5D) < point3d.x) && ((point3d.y * 1.5D) > -point3d.x) && ((point3d.z * 2D) < point3d.x) && ((point3d.z * 2D) > -point3d.x))) {
                Orientation orientation = new Orientation();
                Orientation orientation1 = new Orientation();
                this.FM.getOrient(orientation);
                ((Maneuver) (pilot)).target.getOrient(orientation1);
                float f1 = Math.abs(orientation.getAzimut() - orientation1.getAzimut()) % 360F;
                f1 = f1 <= 180F ? f1 : 360F - f1;
                f1 = f1 <= 90F ? f1 : 180F - f1;
                float f2 = Math.abs(orientation.getTangage() - orientation1.getTangage()) % 360F;
                f2 = f2 <= 180F ? f2 : 360F - f2;
                f2 = f2 <= 90F ? f2 : 180F - f2;
                double d1 = (point3d.x * (4.5D - this.FM.Skill)) / (((Maneuver) (pilot)).target.getSpeed() + 1.0F);
                if ((f1 < d1) && (f2 < d1)) {
                    double d3 = FMMath.RAD2DEG(Math.atan(Math.sqrt(Math.pow(Math.tan(FMMath.DEG2RAD(f1)), 2D) + Math.pow(Math.tan(FMMath.DEG2RAD(f2)), 2D))));
                    com.maddox.JGP.Vector3d vector3d = ((Maneuver) (pilot)).target.getAccel();
                    double d4 = Math.sqrt(Math.pow(((Tuple3d) (vector3d)).x, 2D) + Math.pow(((Tuple3d) (vector3d)).y, 2D) + Math.pow(((Tuple3d) (vector3d)).z, 2D));
                    double d5 = (90D - d3) / (9 + this.FM.Skill);
                    if (d4 < d5) {
                        this.launchMsl();
                        this.tX4Prev = Time.current();
                        Voice.speakAttackByRockets(this);
                    }
                }
            }
        }
    }

    public void createMissilesList() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j] instanceof RocketGunX4) {
                        if (this.FM.CT.Weapons[i][j] instanceof RocketGunX4homing) {
                            this.homing = true;
                        }
                        this.missilesList.add(this.FM.CT.Weapons[i][j]);
                    }
                }

            }
        }

    }

    public void launchMsl() {
        if (this.missilesList.isEmpty()) {
            return;
        } else {
            ((RocketGunX4) this.missilesList.remove(0)).shots(1);
            return;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.missilesList.clear();
        this.createMissilesList();
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

    public boolean    bToFire;
    private long      tX4Prev;
    private float     deltaAzimuth;
    private float     deltaTangage;
    private ArrayList missilesList;
    private boolean   homing;

    static {
        Class class1 = GO_229A2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Go-229");
        Property.set(class1, "meshName", "3DO/Plane/Go-229A2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946.5F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/Ho-229A2.fmd:HORTEN-229");
        Property.set(class1, "cockpitClass", new Class[] { CockpitGO_229.class });
        Property.set(class1, "LOSElevation", 0.51305F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04" });
    }
}
