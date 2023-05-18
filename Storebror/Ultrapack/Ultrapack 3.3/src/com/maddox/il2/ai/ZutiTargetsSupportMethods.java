package com.maddox.il2.ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.gui.ZutiSupportMethods_GUI;
import com.maddox.il2.net.BornPlace;

public class ZutiTargetsSupportMethods {
    public static Set ZUTI_TARGETPOINTS = new HashSet();

    /**
     * When actor from actors.static map file died we should check if it is part of some Destroy ground target.
     *
     * @param actor
     * @param tg
     * @return
     */
    public static boolean checkIfActorPartOfTDestroyGround(Actor actor, TDestroyGround tg) {
        if (actor.pos == null || tg == null) return false;

        Point3d targetPoint = ((Target) tg).pos.getAbsPoint();

        actor.pos.getAbs(targetPoint);
        targetPoint.z = tg.pos.getAbsPoint().z;
        if (tg.pos.getAbsPoint().distance(targetPoint) <= tg.r) {
            if (!Target.isStaticActor(actor)) return false;

            tg.countActors--;
            // System.out.println("TDestroyGround - remaining actors: " + new Integer(tg.countActors).toString());
            if (tg.countActors <= 0) return true;
        }

        return false;
    }

    /**
     * When actor from actors.static map file died we should check if it is part of some Defence ground target.
     *
     * @param actor
     * @param tg
     * @return
     */
    public static boolean checkIfActorPartOfTDefenceGround(com.maddox.il2.engine.Actor actor, TDefenceGround tg) {
        if (actor.pos == null) return false;

        Point3d targetPoint = ((Target) tg).pos.getAbsPoint();

        actor.pos.getAbs(targetPoint);
        targetPoint.z = tg.pos.getAbsPoint().z;
        if (tg.pos.getAbsPoint().distance(targetPoint) <= tg.r) {
            if (!Target.isStaticActor(actor)) return false;

            tg.countActors--;
            // System.out.println("TDestroyGround - remaining actors: " + new Integer(tg.countActors).toString());
            if (tg.countActors <= 0) return true;
        }

        return false;
    }

    /**
     * Converts STATIONARY units on the map that are inside given coordinates to specefied army.
     *
     * @param army
     * @param r
     * @param x
     * @param y
     */
    public static void convertUnitsInRadius(int army, double r, double x, double y) {
        Mission mission = Main.cur().mission;
        if (mission == null) return;

        Actor actor = null;
        Point3d point3d = null;
        ArrayList actors = Main.cur().mission.actors;
        double d = r * r;

        if (actors != null) {
            int size = actors.size();
            for (int i = 0; i < size; i++) {
                actor = (Actor) actors.get(i);
                if (Actor.isValid(actor) && actor.pos != null && ZutiSupportMethods.isStaticActor(actor)) {
                    point3d = actor.pos.getAbsPoint();
                    double distance = (point3d.x - x) * (point3d.x - x) + (point3d.y - y) * (point3d.y - y);
                    if (distance <= d) actor.setArmy(army);
                }
            }
        }
    }

    /**
     * If born place was overrun and some destroy or defence ground targets are inside it's radius, change these targets designations to fit new status.
     *
     * @param bp
     */
    public static void changeTargetSide(BornPlace bp) {
        World world = World.cur();
        if (world == null || world.targetsGuard == null) return;

        List targets = world.targetsGuard.zutiGetTargets();
        List oldTargets = new ArrayList();

        int size = targets.size();

        Target target = null;
        Point3d position = null;
        TDestroyGround tdestroyg = null;
        TDefenceGround tdefenceg = null;

        for (int i = 0; i < size; i++) {
            target = (Target) targets.get(i);
            if (target.pos == null) continue;

            double d = bp.r * bp.r;
            position = target.pos.getAbsPoint();
            double distance = (position.x - bp.place.x) * (position.x - bp.place.x) + (position.y - bp.place.y) * (position.y - bp.place.y);
            if (distance > d) // Target is not in overrun home base radius, go to next target in the list
                continue;

            // System.out.println("Target type: " + target.toString());
            if (target instanceof TDestroyGround) {
                tdestroyg = (TDestroyGround) target;
                if (!tdestroyg.getDiedFlag()) oldTargets.add(target);
            } else if (target instanceof TDefenceGround) {
                tdefenceg = (TDefenceGround) target;
                if (!tdefenceg.getDiedFlag()) oldTargets.add(target);
            }
        }

        // Remove old targets and add new ones
        size = oldTargets.size();
        for (int i = 0; i < size; i++) {
            target = (Target) oldTargets.get(i);
            targets.remove(target);

            // System.out.println("Target type: " + target.toString());
            if (target instanceof TDestroyGround) {
                tdestroyg = (TDestroyGround) target;
                if (!tdestroyg.getDiedFlag()) {
                    position = tdestroyg.pos.getAbsPoint();
                    tdefenceg = new TDefenceGround(tdestroyg.importance(), tdestroyg.zutiTimeout, (int) position.x, (int) position.y, (int) tdestroyg.r, tdestroyg.destructLevel);
                    tdefenceg.countActors = tdestroyg.countActors;

                    // Convert units in this target radius to appropriate army
                    ZutiTargetsSupportMethods.convertUnitsInRadius(bp.army, tdefenceg.r, position.x, position.y);

                    // Switch target description and icon for briefing/minimap screen
                    ZutiSupportMethods_GUI.changeTargetIconDescription(target);

                    EventLog.type(true, "Target TDestroyGround converted to army " + bp.army + " at " + position.x + " " + position.y);
                }
            } else if (target instanceof TDefenceGround) {
                tdefenceg = (TDefenceGround) target;
                if (!tdefenceg.getDiedFlag()) {
                    position = tdefenceg.pos.getAbsPoint();
                    tdestroyg = new TDestroyGround(tdefenceg.importance(), tdefenceg.zutiTimeout, (int) position.x, (int) position.y, (int) tdefenceg.r, tdefenceg.destructLevel);
                    tdestroyg.countActors = tdefenceg.countActors;

                    // Convert units in this target radius to appropriate army
                    ZutiTargetsSupportMethods.convertUnitsInRadius(bp.army, tdestroyg.r, position.x, position.y);

                    // Switch target description and icon for briefing/minimap screen
                    ZutiSupportMethods_GUI.changeTargetIconDescription(target);

                    EventLog.type(true, "Target TDefenceGround converted to army " + bp.army + " at " + position.x + " " + position.y);
                }
            }
        }
    }

    /**
     *
     * @param playerPos
     * @param sameArmyAsGameArmy
     * @return
     */
    public static Point3d getNearestTarget(Point3d playerPos, boolean sameArmyAsGameArmy) {
        World world = World.cur();
        if (world == null || world.targetsGuard == null) return null;

        List targets = world.targetsGuard.zutiGetTargets();
        Point3d result = null;

        double min = -1.0D;
        int size = targets.size();

        Target target = null;
        Point3d position = null;
        TDestroyBridge tdestroyb = null;
        TDefenceBridge tdefenceb = null;
        TDestroy td = null;
        TEscort te = null;

        for (int i = 0; i < size; i++) {
            target = (Target) targets.get(i);
            if (target.isAlive()) {
                // For SAME army
                if (sameArmyAsGameArmy && target instanceof TDestroyGround) {
                    position = target.pos.getAbsPoint();
                    double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                    // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                    if (tmpDistance < min || min < 0.0D) {
                        min = tmpDistance;
                        result = position;
                    }
                }
                if (sameArmyAsGameArmy && target instanceof TDestroyBridge) {
                    tdestroyb = (TDestroyBridge) target;
                    if (tdestroyb.actor == null) continue;

                    position = tdestroyb.actor.pos.getAbsPoint();
                    double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                    // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                    if (tmpDistance < min || min < 0.0D) {
                        min = tmpDistance;
                        result = position;
                    }
                }
                if (sameArmyAsGameArmy && target instanceof TDestroy) {
                    td = (TDestroy) target;
                    if (td.actor == null) continue;

                    if (td.actor instanceof Wing) {
                        position = getPointFromWing((Wing) td.actor);
                        if (position == null) continue;

                        // System.out.println("Destroy 2: " + point3d.toString());
                        double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                        // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                        if (tmpDistance < min || min < 0.0D) {
                            min = tmpDistance;
                            result = position;
                        }
                    } else {
                        // Not group of planes, td.actor should have position set
                        position = td.actor.pos.getAbsPoint();
                        double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                        // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                        if (tmpDistance < min || min < 0.0D) {
                            min = tmpDistance;
                            result = position;
                        }
                    }
                }
                // For OPPOSING army
                if (!sameArmyAsGameArmy && target instanceof TDefenceGround) {
                    position = target.pos.getAbsPoint();
                    double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                    // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                    if (tmpDistance < min || min < 0.0D) {
                        min = tmpDistance;
                        result = position;
                    }
                }
                if (!sameArmyAsGameArmy && target instanceof TDefenceBridge) {
                    tdefenceb = (TDefenceBridge) target;
                    if (tdefenceb.actor == null) continue;

                    position = td.actor.pos.getAbsPoint();
                    double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                    // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                    if (tmpDistance < min || min < 0.0D) {
                        min = tmpDistance;
                        result = position;
                    }
                }
                if (!sameArmyAsGameArmy && target instanceof TEscort) {
                    te = (TEscort) target;
                    te.checkActor();
                    if (te.actor == null) continue;

                    if (te.actor instanceof Wing) {
                        position = getPointFromWing((Wing) td.actor);
                        if (position == null) continue;

                        // System.out.println("Escort 2: " + point3d.toString());
                        double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                        // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                        if (tmpDistance < min || min < 0.0D) {
                            min = tmpDistance;
                            result = position;
                        }
                    } else {
                        // Not group of planes, td.actor should have position set
                        position = td.actor.pos.getAbsPoint();
                        double tmpDistance = Math.pow(playerPos.x - position.x, 2) + Math.pow(playerPos.y - position.y, 2);
                        // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
                        if (tmpDistance < min || min < 0.0D) {
                            min = tmpDistance;
                            result = position;
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Method returns current position of a wing.
     *
     * @param wing
     * @return
     */
    public static Point3d getPointFromWing(Wing wing) {
        Point3d point3d = null;
        int length = wing.airc.length;
        for (int i = 0; i < length; i++)
            if (wing.airc[i] != null && !wing.airc[i].getDiedFlag()) {
                point3d = wing.airc[i].pos.getAbsPoint();
                break;
            }

        return point3d;
    }

    /**
     * Are given coordinates over Destroy or Defence ground targets?
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean isOverTarget(double x, double y) {
        World world = World.cur();
        if (world == null || world.targetsGuard == null) return false;

        Target target = null;
        TDestroyGround tg1 = null;
        TDefenceGround tg2 = null;
        List targets = world.targetsGuard.zutiGetTargets();
        int size = targets.size();
        for (int i_4_ = 0; i_4_ < size; i_4_++) {
            target = (Target) targets.get(i_4_);
            // System.out.println("Target type: " + target.toString());

            if (target instanceof TDestroyGround) {
                tg1 = (TDestroyGround) target;
                if (!tg1.getDiedFlag() && tg1.zutiIsOverTarged(x, y)) return true;
            } else if (target instanceof TDefenceGround) {
                tg2 = (TDefenceGround) target;
                if (!tg2.getDiedFlag() && tg2.zutiIsOverTarged(x, y)) return true;
            }
        }

        return false;
    }
}