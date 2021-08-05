package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Loc;

public class MissileAimer {

    private static final boolean DT(float f, BulletAimer bulletaimer, Actor actor) {
        actor.futurePosition(f, MissileAimer.targPos);
        MissileAimer.targPos.add(MissileAimer.targShotOffs);
        MissileAimer.DTresult = (MissileAimer.idleT + 10.0F) - f;
        return true;
    }

    public static final boolean aim(BulletAimer bulletaimer, Actor actor, Actor actor1, float f, Point3d point3d, Point3d point3d1) {
        if (f < 0.0F) {
            return false;
        }
        MissileAimer.idleT = f;
        if (point3d == null) {
            MissileAimer.targShotOffs.set(0.0D, 0.0D, 0.0D);
        } else {
            actor.pos.getAbsOrient().transform(point3d, MissileAimer.targShotOffs);
        }
        actor1.pos.getAbs(MissileAimer.hunterLoc);
        actor1.futurePosition(MissileAimer.idleT, MissileAimer.hunterLoc.getPoint());
        if (point3d1 == null) {
            MissileAimer.hunterPos.set(MissileAimer.hunterLoc.getPoint());
        } else {
            MissileAimer.hunterLoc.transform(point3d1, MissileAimer.hunterPos);
        }
        float f1 = MissileAimer.idleT;
        if (!MissileAimer.DT(f1, bulletaimer, actor)) {
            return false;
        }
        float f3 = MissileAimer.DTresult;
        if (f3 < 0.001F) {
            return true;
        }
        float f2 = f1 + f3;
        if (!MissileAimer.DT(f2, bulletaimer, actor)) {
            f2 = f1 + (f3 * 0.5F);
            if (!MissileAimer.DT(f2, bulletaimer, actor)) {
                return false;
            }
        }
        float f4 = MissileAimer.DTresult;
        if (f4 > 0.0F) {
            if (MissileAimer.DT((f1 + f2) / 2.0F, bulletaimer, actor) && (MissileAimer.DTresult <= 0.0F)) {
                f2 = (f1 + f2) / 2.0F;
            } else {
                if ((f2 - f1) < 0.0001F) {
                    f2 = f1 + 0.3F;
                } else if (Math.abs(f4 - f3) < 0.0001F) {
                    f2 = f1 + (2.0F * (f2 - f1));
                } else {
                    f2 -= (f4 * (f2 - f1)) / (f4 - f3);
                    if (f2 <= f1) {
                        f2 = f1 + (2.0F * (f2 - f1));
                    }
                }
                boolean flag = MissileAimer.DT(f2, bulletaimer, actor);
                if (!flag || (MissileAimer.DTresult > 0.0F)) {
                    if (!flag) {
                        return false;
                    }
                    f2 += MissileAimer.DTresult * 2.0F;
                    flag = MissileAimer.DT(f2, bulletaimer, actor);
                    if (!flag || (MissileAimer.DTresult > 0.0F)) {
                        return false;
                    }
                }
            }
            f4 = MissileAimer.DTresult;
        }
        float f5 = 0.0F;
        for (int i = 5; i > 0; i--) {
            if ((f4 > -0.001F) || ((f2 - f1) < 0.001F)) {
                return true;
            }
            if ((f3 - f4) < 0.0001F) {
                f5 = (f1 + f2) / 2.0F;
            } else {
                f5 = f2 - ((f4 * (f2 - f1)) / (f4 - f3));
                if ((f5 <= f1) || (f5 >= f2)) {
                    f5 = (f1 + f2) / 2.0F;
                }
            }
            if (!MissileAimer.DT(f5, bulletaimer, actor)) {
                return false;
            }
            if (MissileAimer.DTresult <= 0.0F) {
                f2 = f5;
                f4 = MissileAimer.DTresult;
            } else {
                f1 = f5;
                f3 = MissileAimer.DTresult;
            }
        }

        if (!MissileAimer.DT((f1 + f2) / 2.0F, bulletaimer, actor)) {
            return MissileAimer.DT(f5, bulletaimer, actor);
        } else {
            return true;
        }
    }

    public static final void getPredictedTargetPosition(Point3d point3d) {
        point3d.set(MissileAimer.targPos);
    }

    public static final Point3d getHunterFirePoint() {
        return MissileAimer.hunterPos;
    }

    private static float   idleT;
    private static Point3d hunterPos    = new Point3d();
    private static float   DTresult;
    private static Point3d targPos      = new Point3d();
    private static Point3d targShotOffs = new Point3d();
    private static Loc     hunterLoc    = new Loc();

}
