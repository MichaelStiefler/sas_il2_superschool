package com.maddox.il2.objects.weapons;

import com.maddox.JGP.AxisAngle4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.rts.Time;

public class Ballistics {

    public Ballistics() {
    }

    private static float KD(float f) {
        return 1.0F + (f * (-9.59387E-005F + (f * (3.53118E-009F + (f * -5.83556E-014F)))));
    }

    private static float KF(float f) {
        return (608.5F + ((-1.81327F + (0.00165114F * f)) * f)) * f;
    }

    public static void updateBomb(Bomb bomb, float mass, float square, float inertia, float distFromCMtoStab) {
        float fTickLenFs = Time.tickLenFs();
        float fBombSpeed = (float) bomb.getSpeed(Ballistics.v);
        bomb.pos.getAbs(Ballistics.pos, Ballistics.or);
        Ballistics.dirC.set(1.0D, 0.0D, 0.0D);
        Ballistics.or.transform(Ballistics.dirC);
        if (fBombSpeed < 0.001F) {
            Ballistics.dirW.set(0.0D, 0.0D, -1D);
        } else {
            Ballistics.dirW.set(Ballistics.v);
            Ballistics.dirW.scale(1.0F / fBombSpeed);
        }
        float fCdotW = (float) Ballistics.dirC.dot(Ballistics.dirW);
        float fRetardForce;
        if (fBombSpeed > 330F) {
            fRetardForce = Ballistics.KF(fBombSpeed) * Ballistics.KD((float) Ballistics.pos.z) * square;
        } else {
            fRetardForce = (0.06F * square * 1.225F * Ballistics.KD((float) Ballistics.pos.z) * fBombSpeed * fBombSpeed) / 2.0F;
        }
        float fRetardAccell = fRetardForce / mass;
        Ballistics.dir.scale(6F * fCdotW, Ballistics.dirC);
        Ballistics.dir.x += -7D * Ballistics.dirW.x;
        Ballistics.dir.y += -7D * Ballistics.dirW.y;
        Ballistics.dir.z += -7D * Ballistics.dirW.z;
        Ballistics.dir.scale(fRetardAccell * fTickLenFs);
        if (!World.cur().diffCur.Realistic_Gunnery) {
            Ballistics.dir.set(0.0D, 0.0D, 0.0D);
        }
        Ballistics.v.add(Ballistics.dir);
        Ballistics.v.z -= fTickLenFs * Atmosphere.g();
        bomb.setSpeed(Ballistics.v);
        Ballistics.pos.x += Ballistics.v.x * fTickLenFs;
        Ballistics.pos.y += Ballistics.v.y * fTickLenFs;
        Ballistics.pos.z += Ballistics.v.z * fTickLenFs;
        if ((mass > 35F) && World.cur().diffCur.Wind_N_Turbulence && World.cur().diffCur.Realistic_Gunnery) {
            Vector3d vector3d = new Vector3d();
            World.wind().getVectorWeapon(Ballistics.pos, vector3d);
            Ballistics.pos.x += vector3d.x * fTickLenFs;
            Ballistics.pos.y += vector3d.y * fTickLenFs;
        }
        if (bomb.curTm > 0.35F) {
            // TODO: Fixed by SAS~Storebror: 1.0F - f6^2 might become <= 0!
            float fStabForce = Math.abs(fCdotW) >= 0.996F ? 0.08F : (float) Math.sqrt(1.0F - (fCdotW * fCdotW));
//            float f9 = (float)Math.sqrt(1.0F - f6 * f6);
//            if(f6 <= -0.996F)
//                f9 = 0.08F;
            float fStabMoment = fRetardForce * 0.07F * fStabForce;
            Ballistics.force.set(Ballistics.dirW);
            Ballistics.force.scale(-fStabMoment);
            Ballistics.dirW.set(Ballistics.dirC);
            Ballistics.dirW.scale(-distFromCMtoStab);
            Ballistics.dirW.cross(Ballistics.dirW, Ballistics.force);
            Ballistics.dirW.scale(1.0F / inertia);
            Ballistics.dirW.scale(fTickLenFs);
            bomb.rotAxis.add(Ballistics.dirW);
            bomb.rotAxis.scale(0.96D);
        }
        Ballistics.axAn.set(bomb.rotAxis);
        Ballistics.axAn.angle *= fTickLenFs;
        Ballistics.axAn.rotateRightHanded(Ballistics.dirC);
        Ballistics.or.setAT0(Ballistics.dirC);
        // TODO: 4.14.1 Backport +++
        if (bomb.rotatingFins) {
            float f10 = (float) Math.toRadians(-Ballistics.or.getTangage());
            float f12 = (float) Math.sqrt((Ballistics.v.x * Ballistics.v.x) + (Ballistics.v.y * Ballistics.v.y));
            float f13 = (float) ((Math.cos(f10) * f12) + (Math.sin(f10) * -Ballistics.v.z));
            if (bomb.rotatingSpeed < 30F) {
                bomb.rotatingSpeed += f13 * 0.002F;
            }
            float f14 = bomb.pos.getAbsOrient().getRoll() - bomb.rotatingSpeed;
            Ballistics.or.setRoll(f14);
        }
        if (bomb instanceof FlareBomb) {
            Ballistics.or.setAT0(Ballistics.v);
            Ballistics.or.setRoll(0.0F);
        }
        // TODO: 4.14.1 Backport ---
        bomb.pos.setAbs(Ballistics.pos, Ballistics.or);
    }

    public static void updateRocketBomb(Actor actor, float f, float f1, float f2, boolean flag) {
        float f3 = Time.tickLenFs();
        float f4 = (float) actor.getSpeed(Ballistics.v);
        actor.pos.getAbs(Ballistics.pos, Ballistics.or);
        Ballistics.dir.set(1.0D, 0.0D, 0.0D);
        Ballistics.or.transform(Ballistics.dir);
        float f5;
        if (f4 > 330F) {
            f5 = Ballistics.KF(f4) * Ballistics.KD((float) Ballistics.pos.z) * f1;
        } else {
            f5 = (0.2F * f1 * 1.225F * Ballistics.KD((float) Ballistics.pos.z) * f4 * f4) / 2.0F;
        }
        float f6 = (f2 - f5) / f;
        Ballistics.dir.scale(f6 * f3);
        Ballistics.v.add(Ballistics.dir);
        if (flag) {
            Ballistics.v.z -= f3 * Atmosphere.g();
        }
        actor.setSpeed(Ballistics.v);
        Ballistics.pos.x += Ballistics.v.x * f3;
        Ballistics.pos.y += Ballistics.v.y * f3;
        Ballistics.pos.z += Ballistics.v.z * f3;
        if (f2 < 1000000F) {
            Ballistics.or.setAT0(Ballistics.v);
        }
        actor.pos.setAbs(Ballistics.pos, Ballistics.or);
    }

    public static void update(Actor actor, float f, float f1) {
        Ballistics.update(actor, f, f1, 0.0F, true);
    }

    public static void update(Actor actor, float f, float f1, float f2, boolean flag) {
        float f6 = Time.tickLenFs();
        float f3 = (float) actor.getSpeed(Ballistics.v);
        actor.pos.getAbs(Ballistics.pos, Ballistics.or);
        Ballistics.dir.set(1.0D, 0.0D, 0.0D);
        Ballistics.or.transform(Ballistics.dir);
        float f4;
        if (f3 > 330F) {
            f4 = Ballistics.KF(f3) * Ballistics.KD((float) Ballistics.pos.z) * f1;
        } else {
            f4 = (0.2F * f1 * 1.225F * Ballistics.KD((float) Ballistics.pos.z) * f3 * f3) / 2.0F;
        }
        float f5 = (f2 - f4) / f;
        Ballistics.dir.scale(f5 * f6);
        Ballistics.v.add(Ballistics.dir);
        if (flag) {
            Ballistics.v.z -= f6 * Atmosphere.g();
        }
        actor.setSpeed(Ballistics.v);
        Ballistics.pos.x += Ballistics.v.x * f6;
        Ballistics.pos.y += Ballistics.v.y * f6;
        Ballistics.pos.z += Ballistics.v.z * f6;
        if (World.cur().diffCur.Wind_N_Turbulence && World.cur().diffCur.Realistic_Gunnery) {
            Vector3d vector3d = new Vector3d();
            World.wind().getVectorWeapon(Ballistics.pos, vector3d);
            Ballistics.pos.x += vector3d.x * f6;
            Ballistics.pos.y += vector3d.y * f6;
        }
        if (f2 < 1.0F) {
            Ballistics.or.setAT0(Ballistics.v);
        }
        actor.pos.setAbs(Ballistics.pos, Ballistics.or);
    }

    public static void update(Point3d point3d, Orient orient, Vector3d vector3d, float f, float f1, float f2, boolean flag, float f3) {
        float f4 = (float) vector3d.length();
        Ballistics.dir.set(1.0D, 0.0D, 0.0D);
        orient.transform(Ballistics.dir);
        float f5;
        if (f4 > 330F) {
            f5 = Ballistics.KF(f4) * Ballistics.KD((float) point3d.z) * f1;
        } else {
            f5 = (0.2F * f1 * 1.225F * Ballistics.KD((float) point3d.z) * f4 * f4) / 2.0F;
        }
        float f6 = (f2 - f5) / f;
        Ballistics.dir.scale(f6 * f3);
        vector3d.add(Ballistics.dir);
        if (flag) {
            vector3d.z -= f3 * Atmosphere.g();
        }
        point3d.x += vector3d.x * f3;
        point3d.y += vector3d.y * f3;
        point3d.z += vector3d.z * f3;
        if (f2 < 1.0F) {
            orient.setAT0(vector3d);
        }
    }

    private static Point3d     pos   = new Point3d();
    private static Orient      or    = new Orient();
    private static Vector3d    v     = new Vector3d();
    private static Vector3d    dir   = new Vector3d();
    private static Vector3d    dirC  = new Vector3d();
    private static Vector3d    dirW  = new Vector3d();
    private static Vector3d    force = new Vector3d();
    private static AxisAngle4d axAn  = new AxisAngle4d();

}
