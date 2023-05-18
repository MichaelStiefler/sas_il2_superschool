package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Property;

public class Mi24V extends Mi24X {

    private void OperatorTurret() {
        Pilot pilot = (Pilot) this.FM;
        if ((pilot != null) && !Mission.isNet()) {
            if (this.isAI) {
                Actor actor = War.GetNearestEnemy(this, 1, 3000F);
                if ((pilot != null) && Actor.isAlive(actor) && !(actor instanceof BridgeSegment)) {
                    Point3d point3d = new Point3d();
                    actor.pos.getAbs(point3d);
                    if (this.pos.getAbsPoint().distance(point3d) < 1700D) {
                        point3d.sub(this.FM.Loc);
                        this.FM.Or.transformInv(point3d);
                        if (point3d.y < 0.0D) {
                            this.FM.turret[1].target = actor;
                            this.FM.turret[1].tMode = 2;
                        }
                    }
                } else if ((actor != null) && (this.FM.turret[1].target != null) && !(this.FM.turret[1].target instanceof Aircraft) && !Actor.isAlive(this.FM.turret[1].target)) {
                    this.FM.turret[1].target = null;
                }
            }
        } else {
            Actor actor = this.victim;
            if ((pilot != null) && (this.victim != null) && Actor.isAlive(actor)) {
                Point3d point3d = new Point3d();
                actor.pos.getAbs(point3d);
                if (this.pos.getAbsPoint().distance(point3d) < 1700D) {
                    point3d.sub(this.FM.Loc);
                    this.FM.Or.transformInv(point3d);
                    if (point3d.y < 0.0D) {
                        this.FM.turret[1].target = actor;
                        this.FM.turret[1].tMode = 2;
                    }
                }
            } else if ((actor != null) && (this.FM.turret[1].target != null) && !(this.FM.turret[1].target instanceof Aircraft) && !Actor.isAlive(this.FM.turret[1].target)) {
                this.FM.turret[1].target = null;
            }
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -80F) {
                    f = -80F;
                    flag = false;
                }
                if (f > 20F) {
                    f = 20F;
                    flag = false;
                }
                if (f1 < -60F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -80F) {
                    f = -80F;
                    flag = false;
                }
                if (f > 20F) {
                    f = 20F;
                    flag = false;
                }
                if (f1 < -60F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void update(float f) {
        this.OperatorTurret();
        super.update(f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.turret[0].bIsAIControlled = false;
    }

    static {
        Class class1 = Mi24V.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-24V");
        Property.set(class1, "meshName", "3DO/Plane/Mi-24V/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1950F);
        Property.set(class1, "yearExpired", 1960.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HELIFMD");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMi24.class, CockpitMi24_FLIR.class, CockpitMi24_GUNNER.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 11, 10, 9, 9, 9, 9, 2, 2, 2, 2, 7, 7, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_BombSpawn01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Flare01", "_Flare02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev11", "_ExternalDev12" });
    }
}
