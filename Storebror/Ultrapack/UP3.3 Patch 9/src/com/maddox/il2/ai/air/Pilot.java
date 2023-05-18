/*
 * 4.13.1 Class
 * Backport by SAS~Storebror for TD AI code implementation
 */

package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ME_163B1A;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.TypeBNZFighter;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeGlider;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.air.TypeJazzPlayer;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunPara;
import com.maddox.il2.objects.weapons.ParaTorpedoGun;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.CommonTools;
import com.maddox.sas1946.il2.util.TrueRandom;

public class Pilot extends Maneuver {

    public native int[] doFighterDefense(float f, float f1, float f2, float f3, float f4, float f5, int i, int j, int k, boolean flag, boolean flag1, boolean flag2, boolean flag3, boolean flag4, boolean flag5, boolean flag6, boolean flag7);

    public native int doStormovikDefense(float f, float f1, float f2, float f3, int i, int j, int k, boolean flag, int l, int i1);

    public native int doTransportDefense(float f, float f1, float f2, int i, int j, int k, int l, int i1);

    public void targetAll() {
        this.airTargetType = 9;
    }

    public void targetFighters() {
        this.airTargetType = 7;
    }

    public void targetBombers() {
        this.airTargetType = 8;
    }

    public void attackGround(int i) {
        this.groundTargetType = i;
    }

    public Pilot(String s) {
        super(s);
        this.airTargetType = 9;
        this.groundTargetType = 0;
        this.dumbOffTime = 0L;
        this.oldTask = 3;
        this.oldTaskObject = null;
        this.oldGTarget = null;
        this.continueManeuver = false;
        this.diffVLength = 0.0D;
        this.energed = false;
        this.dist = 0.0F;
        this.dE = 0.0F;
        this.bFromPlayer = false;
    }

    private boolean killed(Actor actor) {
        if (actor == null) return true;
        if (actor instanceof BridgeSegment) actor = actor.getOwner();
        if (Actor.isValid(actor)) return !actor.isAlive();
        else return true;
    }

    public boolean killed(FlightModel flightmodel) {
        if (flightmodel == null) return true;
        if (flightmodel.AS.isAllPilotsDead()) return true;
        if (Actor.isValid(flightmodel.actor)) return this.killed(flightmodel.actor);
        else return flightmodel.isTakenMortalDamage();
    }

    private boolean detectable(Actor actor) {
        if (!(actor instanceof Aircraft)) return false;
        if (this.Skill >= 2) return true;
        else {
            vecDanger.set(((Aircraft) actor).FM.Loc);
            vecDanger.sub(this.Loc);
            onMe.scale(-1D, vecDanger);
            this.Or.transformInv(vecDanger);
            return vecDanger.x >= 0.0D;
        }
    }

    public boolean isDumb() {
        return Time.current() < this.dumbOffTime;
    }

    public void setDumbTime(long l) {
        this.dumbOffTime = Time.current() + l;
    }

    public void addDumbTime(long l) {
        if (this.isDumb()) this.dumbOffTime += l;
        else this.setDumbTime(l);
    }

    public void super_update(float f) {
        super.update(f);
    }

    public void update(float f) {
        if (this.actor.net != null && this.actor.net.isMirror()) {
            ((NetAircraft.Mirror) this.actor.net).fmUpdate(f);
            return;
        }
        this.moveCarrier();
        if (this.TaxiMode) {
            World.cur().airdrome.update(this, f);
            return;
        }
        if (this.isTick(8, 0) || this.get_maneuver() == 0) {
            this.setPriorities();
            this.setTaskAndManeuver();
        }
        super.update(f);
    }

    protected void setPriorities() {
        if (this.killed(this.danger)) this.danger = null;
        if (this.killed(this.target)) this.target = null;
        if (this.killed(this.airClient)) this.airClient = null;
        if (this.killed(this.target_ground)) this.target_ground = null;
        this.setBusy(false);
        if (this.AS.isAllPilotsDead()) {
            this.setBusy(true);
            this.set_maneuver(44);
            if (this.crew > 1) ((Aircraft) this.actor).hitDaSilk();
            this.set_task(0);
            return;
        }
        if (this.get_maneuver() == 46) {
            this.setBusy(true);
            this.dontSwitch = true;
            return;
        }
        float f = 0.0F;
        int i = this.EI.getNum();
        if (i != 0) {
            for (int j = 0; j < i; j++)
                f += this.EI.engines[j].getReadyness() / i;

            if (f < 0.7F + World.Rnd().nextFloat() * 0.15F) this.setReadyToReturn(true);
            if (f < 0.3F + World.Rnd().nextFloat() * 0.2F) this.setReadyToDie(true);
        }
        if (this.M.fuel < 0.3F * this.M.maxFuel) {
            int k = this.AP.way.Cur();
            this.AP.way.last();
            float f1 = this.AP.getWayPointDistance();
            this.AP.way.setCur(k);
            if (this.M.maxFuel < 0.001F) this.M.maxFuel = 0.001F;
            float f2 = 1000F * this.Range * this.M.fuel / this.M.maxFuel;
            if (f2 < 2.0F * f1 && !(this.actor instanceof TypeGlider)) this.setReadyToReturn(true);
        }
        if (this.M.fuel < 0.01F && !(this.actor instanceof TypeGlider)) this.setReadyToDie(true);
        if (this.isTakenMortalDamage() || !this.isCapableOfBMP()) {
            this.setBusy(true);
            ((Aircraft) this.actor).hitDaSilk();
            this.set_task(0);
            if (this.Group != null) this.Group.delAircraft((Aircraft) this.actor);
        }
        if (this.isReadyToDie()) {
            this.AP.way.setCur(1);
            this.bombsOut = true;
            this.CT.dropFuelTanks();
            this.set_task(0);
            if (this.get_maneuver() != 49 && this.get_maneuver() != 12 && this.get_maneuver() != 54) {
                this.clear_stack();
                this.set_maneuver(49);
            }
            this.setBusy(true);
            return;
        }
        if (this.get_maneuver() == 44 || this.get_maneuver() == 25 && this.AP.way.Cur() > 6 || this.get_maneuver() == 49 || this.get_maneuver() == 26 || this.get_maneuver() == 64 || this.get_maneuver() == 102 || this.get_maneuver() == 2
                || this.get_maneuver() == 84 || this.get_maneuver() == 57 || this.get_maneuver() == 60 || this.get_maneuver() == 61) {
            this.setBusy(true);
            this.dontSwitch = true;
            return;
        }
//        System.out.println("" + this.getDangerAggressiveness() + " " + (1.0F - 0.12F * this.Skill));
        if (this.getDangerAggressiveness() > (1.0F - 0.12F * this.Skill) * World.Rnd().nextFloat(0.95F, 1.01F) && this.danger != null && (this.danger.actor instanceof TypeFighter || this.danger.actor instanceof TypeStormovik) && this.danger.isOk()) {
            if (this.courage <= 1 && World.Rnd().nextInt(0, 30000) < 2 - this.courage) {
                this.setBusy(true);
                ((Aircraft) this.actor).hitDaSilk();
                this.set_task(0);
                if (this.Group != null) this.Group.delAircraft((Aircraft) this.actor);
            }
            if (this.get_task() != 4) {
                this.set_task(4);
                this.clear_stack();
                this.set_maneuver(0);
                if (this.actor instanceof TypeStormovik && this.Group != null) {
                    int l = this.Group.numInGroup((Aircraft) this.actor);
                    if (((Aircraft) this.danger.actor).aircNumber() < this.Group.nOfAirc && !this.hasBombs() && this.Group.nOfAirc >= l + 2) {
                        Maneuver maneuver = (Maneuver) this.Group.airc[l + 1].FM;
                        Voice.speakCheckYour6((Aircraft) this.actor, (Aircraft) this.danger.actor);
                        Voice.speakHelpFromAir((Aircraft) maneuver.actor, 1);
                        maneuver.airClient = this;
                        this.set_task(6);
                        this.clear_stack();
                        maneuver.target = this.danger;
                        this.set_maneuver(27);
                        this.setBusy(true);
                        return;
                    }
                }
            }
            Voice.speakClearTail((Aircraft) this.actor);
            this.setBusy(true);
            return;
        }
        if (this.isReadyToReturn() && !this.AP.way.isLanding()) {
            if (this.Group != null && this.Group.grTask != 1) {
                AirGroup airgroup = new AirGroup(this.Group);
                airgroup.rejoinGroup = null;
                this.Group.delAircraft((Aircraft) this.actor);
                airgroup.addAircraft((Aircraft) this.actor);
                airgroup.w.last();
                airgroup.w.prev();
                airgroup.w.curr().setTimeout(3);
                airgroup.timeOutForTaskSwitch = 10000;
                this.AP.way.last();
                this.AP.way.prev();
                this.AP.way.curr().getP(p1f);
                p1f.z = -10F + (float) this.Loc.z;
            }
            this.bombsOut = true;
            this.CT.dropFuelTanks();
            return;
        }
        if (this.get_task() == 6) {
            this.CT.GearControl = 0.0F;
            this.CT.arrestorControl = 0.0F;
            if (this.target != null && this.airClient != null && this.target == ((Maneuver) this.airClient).danger) {
                if (this.actor instanceof TypeStormovik) {
                    if (((Maneuver) this.airClient).getDangerAggressiveness() > 0.0F) {
                        this.setBusy(true);
                        return;
                    }
                    this.airClient = null;
                }
                this.setBusy(true);
                return;
            }
            if ((((Aircraft) this.actor).aircIndex() & 1) == 0 && this.Group != null) {
                int i1 = this.Group.numInGroup((Aircraft) this.actor);
                if (this.Group.nOfAirc >= i1 + 2 && (this.Group.airc[i1 + 1].aircIndex() & 1) != 0) {
                    Maneuver maneuver1 = (Maneuver) this.Group.airc[i1 + 1].FM;
                    if (maneuver1.airClient == this && maneuver1.getDangerAggressiveness() > 0.5F && maneuver1.danger != null && maneuver1.danger.isOk()) {
                        Voice.speakCheckYour6((Aircraft) maneuver1.actor, (Aircraft) maneuver1.danger.actor);
                        Voice.speakHelpFromAir((Aircraft) this.actor, 1);
                        this.set_task(6);
                        this.clear_stack();
                        this.target = maneuver1.danger;
                        this.set_maneuver(27);
                        this.setBusy(true);
                        return;
                    }
                }
            }
            if (this.target != null && ((Maneuver) this.target).getDangerAggressiveness() > 0.5F && ((Maneuver) this.target).danger == this && this.target.isOk()) {
                this.setBusy(true);
                return;
            }
        }
        if (this.isDumb()) {
            this.setBusy(true);
            return;
        }
        if (this.get_task() == 7 && this.target_ground != null && this.get_maneuver() != 0) {
            this.setBusy(true);
            return;
        }
        if (this.get_task() == 3 && this.AP.way.isLanding()) this.setBusy(true);
    }

    private void setTaskAndManeuver() {
        if (this.dontSwitch) {
            this.dontSwitch = false;
            return;
        }
        if (!this.isBusy()) {
            if ((this.wasBusy || this.get_maneuver() == 0) && this.Group != null) {
                this.clear_stack();
                this.Group.setTaskAndManeuver(this.Group.numInGroup((Aircraft) this.actor));
            }
        } else if (this.get_maneuver() == 0) {
            this.clear_stack();
            this.setManeuverByTask();
        }
    }

    public void setManeuverByTask() {
        this.clear_stack();

        switch (this.get_task()) {
            default:
                break;

            case 2:
                if (this.Leader != null && Actor.isValid(this.Leader.actor) && !this.Leader.isReadyToReturn() && !this.Leader.isReadyToDie() && this.Leader.getSpeed() > 35F) this.set_maneuver(24);
                else this.set_task(3);
                break;

            case 3:
                this.set_maneuver(21);
                if (this.AP.way.isLanding()) break;
                this.wingman(true);
                if (this.Leader != null) break;
                int i;
                float f;
                if (this.AP.way.Cur() < this.AP.way.size() - 1) {
                    this.AP.way.next();
                    i = this.AP.way.curr().Action;
                    f = this.AP.getWayPointDistance();
                    this.AP.way.prev();
                } else {
                    i = this.AP.way.curr().Action;
                    f = this.AP.getWayPointDistance();
                }
                Pilot pilot = (Pilot) ((Aircraft) this.actor).FM;
                while (pilot.Wingman != null) {
                    pilot = (Pilot) pilot.Wingman;
                    pilot.wingman(true);
                    pilot.AP.way.setCur(this.AP.way.Cur());
                    if (!pilot.AP.way.isLanding() && pilot.get_task() == 3) pilot.set_task(2);
                }
                if (((Aircraft) this.actor).aircIndex() == 0 && this.Speak5minutes == 0 && i == 3 && f < 30000D) {
                    Voice.speak5minutes((Aircraft) this.actor);
                    this.Speak5minutes = 1;
                }
                break;

            case 4:
                if (this.get_maneuver() == 0) this.set_maneuver(21);
                if (this.danger == null) {
                    this.set_task(3);
                    break;
                }
                if (this.actor instanceof TypeFighter) {
                    this.bombsOut = true;
                    this.CT.dropFuelTanks();
                }
                this.dist = (float) this.Loc.distance(this.danger.Loc);
                vecDanger.sub(this.danger.Loc, this.Loc);
                onMe.scale(-1D, vecDanger);
                Maneuver.tmpOr.setYPR(this.Or.getYaw(), 0.0F, 0.0F);
                Maneuver.tmpOr.transformInv(vecDanger);
                diffV.sub(this.danger.Vwld, this.Vwld);
                Maneuver.tmpOr.transformInv(diffV);
                this.diffVLength = diffV.length();
                Maneuver.tmpOr.setYPR(this.danger.Or.getYaw(), 0.0F, 0.0F);
                this.danger.Or.transformInv(onMe);
                vecDanger.normalize();
                onMe.normalize();
                this.dE = (this.Energy - this.danger.Energy) * 0.1019F;
                this.energed = this.danger.Energy > this.Energy;
                this.faster = this.danger.getSpeed() > this.getSpeed();
                this.higher = this.danger.Loc.z > this.Loc.z;
                this.near = this.dist < 300F;
                this.onBack = vecDanger.x < 0.0D && this.dist < 2000F;
                this.visible = vecDanger.dot(MAIN_LOOK) > 0.0D;
                this.looks = onMe.x > 0.0D;
                vecDanger.normalize();
                if (this.onBack && this.near && this.danger instanceof TypeFighter && (this.actor instanceof TypeTransport || this.Wingman == null || this.killed(this.Wingman) || ((Pilot) this.Wingman).target != this.danger)) if (this.isLeader()) {
                    if ((this.actor instanceof TypeFighter || this.actor instanceof TypeStormovik && this.Skill > 1 && this.AP.way.curr().Action == 0) && this.Wingman != null && !this.killed(this.Wingman) && !((Pilot) this.Wingman).requestCoverFor(this))
                        if (this.Wingman.Wingman != null && !this.killed(this.Wingman.Wingman)) ((Pilot) this.Wingman.Wingman).requestCoverFor(this);
                    else if (this.Skill >= 2) {
                        Aircraft aircraft = War.getNearestFriendlyFighter((Aircraft) this.actor, 8000F);
                        if (aircraft != null && aircraft.FM instanceof Pilot) ((Pilot) aircraft.FM).requestCoverFor(this);
                    }
                } else if (this.Skill >= 2) {
                    Aircraft aircraft1 = War.getNearestFriendlyFighter((Aircraft) this.actor, 8000F);
                    if (aircraft1 instanceof TypeFighter && aircraft1.FM instanceof Pilot) ((Pilot) aircraft1.FM).requestCoverFor(this);
                }
//                this.target = this.danger; // TODO: AI TEST, didn't work.
                if (this.actor instanceof TypeFighter) {
                    this.fighterDefence();
                    break;
                }
                if (this.actor instanceof TypeStormovik) this.stormovikDefence();
                else this.transportDefence();
                break;

            case 5:
                if (this.airClient != null && !this.killed(this.airClient)) {
                    this.followOffset.set(100D, 0.0D, 20D);
                    this.set_maneuver(65);
                    break;
                }
                this.airClient = null;
                if (this.target != null && !this.killed(this.target)) {
                    this.set_task(6);
                    this.set_maneuver(0);
                } else {
                    this.set_task(3);
                    this.set_maneuver(21);
                }
                break;

            case 6:
                this.WeWereInAttack = true;
                if (this.actor instanceof TypeFighter && !this.bKeepOrdnance) {
                    this.bombsOut = true;
                    this.CT.dropFuelTanks();
                }
                if (this.target == null || !this.hasCourseWeaponBullets()) {
                    if (this.AP.way.curr().Action == 3) {
                        this.set_task(7);
                        this.set_maneuver(0);
                        break;
                    }
                    this.set_task(3);
                    if (this.Leader == null) this.set_maneuver(21);
                    else this.set_maneuver(24);
                    break;
                }
                int j = ((Aircraft) this.actor).aircIndex();
                if (this.target.actor instanceof TypeBomber) {
                    this.attackBombers();
                    break;
                }
                if (this.target.actor instanceof TypeStormovik) {
                    this.attackStormoviks();
                    break;
                }
                if (j == 0 || j == 2) this.set_maneuver(27);
                if (j != 1 && j != 3) break;
                if (this.Leader != null && !this.killed(this.Leader)) {
                    this.airClient = this.Leader;
                    this.set_task(5);
                    this.set_maneuver(0);
                } else this.set_maneuver(27);
                break;

            case 7:
                if (!this.WeWereInGAttack) this.WeWereInGAttack = true;
                if (!Actor.isAlive(this.target_ground)) {
                    this.set_task(2);
                    this.set_maneuver(0);
                    break;
                }
                boolean flag = true;
                if (this.CT.Weapons[0] != null && this.CT.Weapons[0][0] != null && this.CT.Weapons[0][0].bulletMassa() > 0.05F && this.CT.Weapons[0][0].countBullets() > 0) flag = false;
                if (flag && this.CT.getWeaponMass() < 15F || this.CT.getWeaponMass() < 1.0F) {
                    Voice.speakEndOfAmmo((Aircraft) this.actor);
                    this.set_task(2);
                    this.set_maneuver(0);
                    if (this.AP.way.curr().Action != 3) this.AP.way.next();
                    this.target_ground = null;
                }
                if (this.target_ground instanceof Prey && (((Prey) this.target_ground).HitbyMask() & 1) == 0) {
                    float f1 = 0.0F;
                    for (int k = 0; k < 4; k++)
                        if (this.CT.Weapons[k] != null && this.CT.Weapons[k][0] != null && this.CT.Weapons[k][0].countBullets() != 0 && this.CT.Weapons[k][0].bulletMassa() > f1) f1 = this.CT.Weapons[k][0].bulletMassa();

                    if (f1 < 0.08F || this.target_ground instanceof TgtShip && f1 < 0.55F) {
                        this.set_task(2);
                        this.set_maneuver(0);
                        if (this.AP.way.curr().Action != 3) this.AP.way.next();
                        this.target_ground = null;
                    }
                }
                if (this.CT.Weapons[3] != null && this.CT.Weapons[3][0] != null && this.CT.Weapons[3][0].countBullets() != 0) {
                    if (this.CT.Weapons[3][0] instanceof ParaTorpedoGun) {
                        this.set_maneuver(43);
                        break;
                    }
                    if (this.CT.Weapons[3][0] instanceof TorpedoGun) {
                        if (this.target_ground instanceof TgtShip) {
                            if (this instanceof TypeHasToKG && this.Skill >= 2) this.set_maneuver(73);
                            else this.set_maneuver(51);
                        } else this.set_maneuver(43);
                        break;
                    }
                    if (this.CT.Weapons[3][0] instanceof BombGunPara) {
                        this.AP.way.curr().setTarget(null);
                        this.target_ground = null;
                        this.set_maneuver(21);
                        break;
                    }
                    if (this.CT.Weapons[3][0].bulletMassa() < 5F) {
                        this.set_maneuver(52);
                        break;
                    }
                    if (this.actor instanceof TypeDiveBomber && this.Alt > 1200F) this.set_maneuver(50);
                    else this.set_maneuver(43);
                    break;
                }
                if (this.target_ground instanceof BridgeSegment) {
                    this.set_task(2);
                    this.set_maneuver(0);
                    if (this.AP.way.curr().Action != 3) this.AP.way.next();
                    this.target_ground = null;
                }
                if (this.actor instanceof TypeFighter || this.actor instanceof TypeStormovik) {
                    this.set_maneuver(43);
                    break;
                }
                this.set_task(2);
                this.set_maneuver(0);
                if (this.AP.way.curr().Action != 3) this.AP.way.next();
                this.target_ground = null;
                break;

            case 0:
                if (this.isReadyToDie()) this.set_maneuver(49);
                break;

            case 1:
                this.set_maneuver(45);
                break;
        }
    }

    public boolean requestCoverFor(FlightModel flightmodel) {
        if (this.actor instanceof TypeTransport) {
            Voice.speakHelpFromAir((Aircraft) this.actor, 0);
            return false;
        }
        if (this.danger == null || ((Pilot) this.danger).target != this || this.danger.Loc.distance(this.Loc) > 600D + 200D * this.Skill || this.danger.actor instanceof TypeStormovik || this.danger.actor instanceof TypeBomber) {
            if (((Pilot) flightmodel).danger == null || this.killed(((Pilot) flightmodel).danger) || ((Pilot) flightmodel).danger.actor instanceof TypeTransport || ((Pilot) flightmodel).danger.Loc.distance(flightmodel.Loc) > 3000D) {
                Voice.speakHelpFromAir((Aircraft) this.actor, 2);
                return true;
            }
            this.set_task(6);
            this.set_maneuver(27);
            this.target = ((Pilot) flightmodel).danger;
            if (World.Rnd().nextBoolean()) Voice.speakCoverProvided((Aircraft) this.actor, (Aircraft) flightmodel.actor);
            else Voice.speakHelpFromAir((Aircraft) this.actor, 1);
            return true;
        } else {
            Voice.speakHelpFromAir((Aircraft) this.actor, 0);
            return false;
        }
    }

    public void setAsDanger(Actor actor) {
        if (this.get_maneuver() == 44) return;
        if (this.get_maneuver() == 26) return;
        if (this.isDumb() && !this.isReadyToReturn()) return;
        if (actor.getArmy() == this.actor.getArmy()) {
            this.set_maneuver(8);
            this.setDumbTime(5000L);
            if (actor instanceof Aircraft) Voice.speakCheckFire((Aircraft) this.actor, (Aircraft) actor);
            return;
        }
        if (!Actor.isValid(this.actor)) {
            if (World.cur().isArcade()) {
                Aircraft.debugprintln(actor, "Jeopardizing invalid actor (one being destroyed)..");
                Explosions.generateComicBulb(actor, "Sucker", 5F);
                if (actor instanceof TypeFighter && ((Aircraft) actor).FM instanceof Pilot) ((Pilot) ((Aircraft) actor).FM).set_maneuver(35);
            }
            Voice.speakNiceKill((Aircraft) actor);
            return;
        }
        switch (this.Skill) {
            case 0:
                if (World.Rnd().nextInt(0, 99) < 98) return;
                if (actor instanceof Aircraft) {
                    Vector3d vector3d = new Vector3d();
                    vector3d.sub(this.Loc, ((Aircraft) actor).FM.Loc);
                    ((Aircraft) actor).FM.Or.transformInv(vector3d);
                    if (vector3d.z > 0.0D) return;
                }
                break;

            case 1:
                if (!this.detectable(actor)) return;
                if (World.Rnd().nextInt(0, 99) < 97) return;
                break;

            case 2:
                if (this.getMnTime() < 1.0F) return;
                break;

            case 3:
                if (this.getMnTime() < 1.0F) return;
                break;
        }
        if (this.actor instanceof TypeTransport) {
            if (this.AP.way.curr().Action != 3 && (this.get_maneuver() == 24 || this.get_maneuver() == 21)) {
                this.set_task(4);
                this.set_maneuver(0);
            }
            return;
        }
        if (this.get_task() == 2) {
            this.set_task(3);
            this.set_maneuver(0);
        }
        if (actor instanceof Aircraft) {
            if (actor instanceof TypeFighter) {
                if (this.turret.length > 0 && this.AS.astatePilotStates[this.turret.length] < 90) Voice.speakDanger((Aircraft) this.actor, 4);
                Voice.speakDanger((Aircraft) this.actor, 0);
            }
            Aircraft aircraft = (Aircraft) actor;
            Pilot pilot = this;
            pilot.danger = aircraft.FM;
            if (this.actor instanceof TypeFighter && this.get_task() != 4) {
                this.target = aircraft.FM;
                this.set_task(4);
                this.set_maneuver(0);
                this.clear_stack();
            }
        }
    }

    private void transportDefence() {
        int i = 0;
        for (int j = 0; j < this.turret.length; j++)
            if (this.turret[j].bIsOperable) i++;

        int k = ((Aircraft) this.danger.actor).aircNumber();
        int l = this.doTransportDefense((float) vecDanger.x, this.dist, this.Alt, this.Skill, this.courage, this.flying, i, k);

        // TODO: Cheater Protection +++
        if (l == Maneuver.WAYPOINT || l == Maneuver.FOLLOW) if (AircraftState.isCheaterAdvancedTransportDefence(this.danger.actor)) {
            float onMeThreshold = CommonTools.cvt(this.dist, 300F, 1000F, 0.85F, 0.995F);
            if ((float) onMe.x > onMeThreshold && this.dist < 1500F) switch (TrueRandom.nextInt(5)) {
                case 0:
                    l = Maneuver.EVADE_UP;
                    break;
                case 1:
                    l = Maneuver.EVADE_ATTACK;
                    break;
                case 2:
                    l = Maneuver.PULL_UP_EMERGENCY;
                    break;
                case 3:
                    l = Maneuver.IVAN;
                    break;
                case 4:
                    l = Maneuver.PANIC_MANIC;
                    break;
            }
//                    System.out.println("" + Group.numInGroup((Aircraft)this.actor) + " " + l + " " + onMeThreshold + " " + (float) onMe.x + " " + this.dist);
        }

        this.set_maneuver(l);
    }

    private void stormovikDefence() {
        int i = ((Aircraft) this.danger.actor).aircNumber();
        int j = 0;
        for (int k = 0; k < this.turret.length; k++)
            if (this.turret[k].bIsOperable) j++;

        int l = this.doStormovikDefense((float) vecDanger.x, (float) onMe.x, this.dist, this.Alt, this.Skill, this.courage, this.flying, this.visible, j, i);
        if (l == 27) this.target = this.danger;
        this.set_maneuver(l);
    }

    private void fighterDefence() {
        if (this.bKeepOrdnance) this.bKeepOrdnance = false;
        int ai[] = this.doFighterDefense(this.dE, (float) vecDanger.x, (float) onMe.x, this.dist, (float) this.diffVLength, this.Alt, this.Skill, this.courage, this.flying, this.visible, this.near, this.onBack, this.looks, this.higher, this.faster,
                this.isTurnBetterThanEnemy(), this.isDiveBetterThanEnemy());
        this.set_maneuver(ai[0]);
        for (int i = 1; i < ai.length; i++)
            this.push(ai[i]);

    }

    private boolean isDiveBetterThanEnemy() {
        return this.Alt > 2000F && this.actor instanceof TypeBNZFighter && this.VmaxAllowed > this.danger.VmaxAllowed + 10F;
    }

    private boolean isTurnBetterThanEnemy() {
        return this.Wing.T_turn < this.danger.getWing().T_turn && Math.abs(this.getSpeedKMH() - this.Wing.V_turn) < 100F;
    }

    public void attackBombers() {
        if (this.bFromPlayer) {
            this.bFromPlayer = false;
            return;
        }
        float f = 0.0F;
        if (this.CT.Weapons[1] != null && ((GunGeneric) this.CT.Weapons[1][0]).countBullets() > 0) f = ((GunGeneric) this.CT.Weapons[1][0]).bulletMassa();
        if (this.actor instanceof ME_163B1A) switch (World.Rnd().nextInt(0, 2)) {
            case 0:
                this.setBomberAttackType(7);
                break;

            default:
                this.setBomberAttackType(2);
                break;
        }
        else if (this.actor instanceof TypeJazzPlayer && ((TypeJazzPlayer) this.actor).hasSlantedWeaponBullets()) this.setBomberAttackType(10);
        else switch (this.Skill) {
            default:
                break;

            case 0:
                switch (World.Rnd().nextInt(0, 5)) {
                    case 0:
                        this.setBomberAttackType(7);
                        break;

                    case 1:
                        this.setBomberAttackType(2);
                        break;

                    default:
                        this.setBomberAttackType(5);
                        break;
                }
                break;

            case 1:
                if (f > 0.12F) {
                    switch (World.Rnd().nextInt(0, 3)) {
                        case 0:
                            this.setBomberAttackType(2);
                            break;

                        case 1:
                            this.setBomberAttackType(1);
                            break;

                        default:
                            this.setBomberAttackType(0);
                            break;
                    }
                    break;
                }
                switch (World.Rnd().nextInt(0, 6)) {
                    case 0:
                        this.setBomberAttackType(1);
                        break;

                    case 1:
                        this.setBomberAttackType(7);
                        break;

                    default:
                        this.setBomberAttackType(2);
                        break;
                }
                break;

            case 2:
                if (f > 0.12F) {
                    switch (World.Rnd().nextInt(0, 6)) {
                        case 0:
                            this.setBomberAttackType(2);
                            break;

                        case 1:
                            this.setBomberAttackType(1);
                            break;

                        default:
                            this.setBomberAttackType(0);
                            break;
                    }
                    break;
                }
                if (f > 0.05F) {
                    switch (World.Rnd().nextInt(0, 10)) {
                        case 0:
                        case 1:
                        case 2:
                            this.setBomberAttackType(1);
                            break;

                        case 3:
                            this.setBomberAttackType(7);
                            break;

                        case 4:
                            this.setBomberAttackType(6);
                            break;

                        default:
                            this.setBomberAttackType(2);
                            break;
                    }
                    break;
                }
                switch (World.Rnd().nextInt(0, 6)) {
                    case 0:
                        this.setBomberAttackType(1);
                        break;

                    case 1:
                        this.setBomberAttackType(7);
                        break;

                    case 2:
                        this.setBomberAttackType(3);
                        break;

                    default:
                        this.setBomberAttackType(2);
                        break;
                }
                break;

            case 3:
                if (f > 0.12F) {
                    switch (World.Rnd().nextInt(0, 7)) {
                        case 0:
                            this.setBomberAttackType(2);
                            break;

                        case 1:
                            this.setBomberAttackType(1);
                            break;

                        case 2:
                            this.setBomberAttackType(6);
                            break;

                        default:
                            this.setBomberAttackType(0);
                            break;
                    }
                    break;
                }
                if (f > 0.05F) {
                    switch (World.Rnd().nextInt(0, 10)) {
                        case 0:
                        case 1:
                        case 2:
                            this.setBomberAttackType(1);
                            break;

                        case 3:
                            this.setBomberAttackType(7);
                            break;

                        case 4:
                            this.setBomberAttackType(3);
                            break;

                        case 5:
                            this.setBomberAttackType(6);
                            break;

                        default:
                            this.setBomberAttackType(2);
                            break;
                    }
                    break;
                }
                switch (World.Rnd().nextInt(0, 4)) {
                    case 0:
                        this.setBomberAttackType(1);
                        break;

                    case 1:
                        this.setBomberAttackType(7);
                        break;

                    case 2:
                        this.setBomberAttackType(3);
                        break;

                    default:
                        this.setBomberAttackType(2);
                        break;
                }
                break;
        }
        this.set_maneuver(63);
    }

    public void attackStormoviks() {
        switch (this.Skill) {
            default:
                break;

            case 0:
                switch (World.Rnd().nextInt(0, 5)) {
                    case 0:
                        this.setBomberAttackType(8);
                        break;

                    case 1:
                        this.setBomberAttackType(9);
                        break;

                    default:
                        this.setBomberAttackType(5);
                        break;
                }
                break;

            case 1:
            case 2:
            case 3:
                if (this.target.crew > 1) {
                    switch (World.Rnd().nextInt(0, 6)) {
                        case 0:
                            this.setBomberAttackType(9);
                            break;

                        case 1:
                        case 2:
                            this.setBomberAttackType(0);
                            break;

                        default:
                            this.setBomberAttackType(4);
                            break;
                    }
                    break;
                }
                switch (World.Rnd().nextInt(0, 3)) {
                    case 0:
                        this.setBomberAttackType(9);
                        break;

                    default:
                        this.setBomberAttackType(8);
                        break;
                }
                break;
        }
        this.set_maneuver(63);
    }

    public void wingmanAttacks(int i) {
        label0: switch (this.Skill) {
            case 0:
                switch (i) {
                    case 0:
                    case 1:
                    case 3:
                    case 4:
                        switch (this.flying) {
                            case 0:
                                this.followOffset.set(World.Rnd().nextFloat(150F, 200F), 100 - World.Rnd().nextInt(0, 1) * 200 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 50F));
                                this.set_maneuver(65);
                                break;

                            case 1:
                                this.followOffset.set(World.Rnd().nextFloat(100F, 150F), 200 - World.Rnd().nextInt(0, 1) * 400 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 20F));
                                this.set_maneuver(65);
                                break;

                            case 2:
                                this.followOffset.set(World.Rnd().nextFloat(50F, 100F), 300 - World.Rnd().nextInt(0, 1) * 600 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-20F, 10F));
                                this.set_maneuver(65);
                                break;
                        }
                        break;

                    case 2:
                        switch (this.flying) {
                            case 0:
                                this.followOffset.set(World.Rnd().nextFloat(100F, 200F), 100 - World.Rnd().nextInt(0, 1) * 300 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 150F));
                                this.set_maneuver(65);
                                break;

                            case 1:
                                this.followOffset.set(World.Rnd().nextFloat(100F, 250F), 200 - World.Rnd().nextInt(0, 1) * 500 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 120F));
                                this.set_maneuver(65);
                                break;

                            case 2:
                                this.followOffset.set(World.Rnd().nextFloat(50F, 200F), 300 - World.Rnd().nextInt(0, 1) * 600 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-200F, 100F));
                                this.set_maneuver(65);
                                break;
                        }
                        break;
                }
                break;

            case 1:
                switch (i) {
                    default:
                        break;

                    case 0:
                        if (this.courage == 3) {
                            switch (World.Rnd().nextInt(0, 2)) {
                                case 0:
                                    this.set_maneuver(27);
                                    break;

                                case 1:
                                    this.set_maneuver(Maneuver.GATTACK_TORPEDO_TOKG);
                                    break;

                                case 2:
                                    this.spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 100F), 0.0D);
                                    this.set_maneuver(74);
                                    break;
                            }
                            break label0;
                        }
                        switch (this.flying) {
                            case 1:
                                this.spreadV3d.set(World.Rnd().nextFloat(50F, 100F), 300 - World.Rnd().nextInt(0, 1) * 600 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-20F, 10F));
                                this.set_maneuver(76);
                                break;

                            case 2:
                                this.spreadV3d.set(World.Rnd().nextFloat(-50F, 50F), 300 - World.Rnd().nextInt(0, 1) * 600 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
                                this.set_maneuver(76);
                                break;

                            case 3:
                                this.spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
                                this.set_maneuver(76);
                                break;
                        }
                        break label0;

                    case 1:
                        switch (this.flying) {
                            case 1:
                                this.spreadV3d.set(World.Rnd().nextFloat(50F, 100F), 300 - World.Rnd().nextInt(0, 1) * 600 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-20F, 10F));
                                this.set_maneuver(76);
                                break;

                            case 2:
                                this.spreadV3d.set(World.Rnd().nextFloat(-50F, 50F), 300 - World.Rnd().nextInt(0, 1) * 600 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
                                this.set_maneuver(76);
                                break;

                            case 3:
                                this.spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-10F, 10F));
                                this.set_maneuver(76);
                                break;
                        }
                        break label0;

                    case 2:
                        switch (this.flying) {
                            case 1:
                                this.followOffset.set(World.Rnd().nextFloat(100F, 200F), 100 - World.Rnd().nextInt(0, 1) * 300 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 150F));
                                this.set_maneuver(65);
                                break;

                            case 2:
                                this.followOffset.set(World.Rnd().nextFloat(100F, 250F), 200 - World.Rnd().nextInt(0, 1) * 500 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-100F, 120F));
                                this.set_maneuver(65);
                                break;

                            case 3:
                                this.followOffset.set(World.Rnd().nextFloat(50F, 200F), 300 - World.Rnd().nextInt(0, 1) * 600 + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(-200F, 100F));
                                this.set_maneuver(65);
                                break;
                        }
                        break;

                    case 3:
                        this.spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), -1400 + ((Maneuver) this.airClient).bracketSide * 2800 + World.Rnd().nextFloat(-200F, 200F), World.Rnd().nextFloat(0.0F, 250F));
                        this.set_maneuver(74);
                        break;

                    case 4:
                        this.set_maneuver(65);
                        break;
                }
                break;

            case 2:
                switch (i) {
                    default:
                        break;

                    case 0:
                        switch (this.flying) {
                            case 2:
                                this.setWingmanAttackType(4);
                                this.set_maneuver(81);
                                break;

                            case 3:
                                this.setWingmanAttackType(6);
                                this.set_maneuver(81);
                                break;

                            case 4:
                                this.setWingmanAttackType(7);
                                this.set_maneuver(81);
                                break;
                        }
                        break label0;

                    case 1:
                        switch (this.flying) {
                            case 2:
                                this.spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 100F), 0.0D);
                                this.set_maneuver(74);
                                break;

                            case 3:
                                this.spreadV3d.set(World.Rnd().nextFloat(-100F, -300F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 100F), 0.0D);
                                this.set_maneuver(74);
                                break;

                            case 4:
                                this.spreadV3d.set(World.Rnd().nextFloat(-10F, -100F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 100F), -20D);
                                this.set_maneuver(74);
                                break;
                        }
                        break;

                    case 2:
                        this.spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), 100 - World.Rnd().nextInt(0, 1) * 250 + World.Rnd().nextFloat(-50F, 50F), 0.0D);
                        this.set_maneuver(65);
                        break;

                    case 3:
                        this.spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), -1400 + ((Maneuver) this.airClient).bracketSide * 2800 + World.Rnd().nextFloat(-150F, 150F), World.Rnd().nextFloat(0.0F, 550F));
                        this.set_maneuver(74);
                        break;

                    case 4:
                        this.set_maneuver(65);
                        break;
                }
                break;

            case 3:
                switch (i) {
                    default:
                        break;

                    case 0:
                        switch (this.flying) {
                            default:
                                break;

                            case 3:
                                this.setWingmanAttackType(4);
                                this.set_maneuver(81);
                                break label0;

                            case 4:
                                this.setWingmanAttackType(6);
                                this.set_maneuver(81);
                                break label0;

                            case 5:
                                if (World.Rnd().nextBoolean()) this.setWingmanAttackType(8);
                                else this.setWingmanAttackType(9);
                                this.set_maneuver(81);
                                break;
                        }
                        break label0;

                    case 1:
                        switch (this.flying) {
                            case 3:
                                this.spreadV3d.set(World.Rnd().nextFloat(-20F, 20F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 100F), 0.0D);
                                this.set_maneuver(74);
                                break;

                            case 4:
                                this.spreadV3d.set(World.Rnd().nextFloat(-100F, -300F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 100F), 0.0D);
                                this.set_maneuver(74);
                                break;

                            case 5:
                                this.spreadV3d.set(World.Rnd().nextFloat(-10F, -100F), 350 - World.Rnd().nextInt(0, 1) * 700 + World.Rnd().nextFloat(-50F, 100F), -20D);
                                this.set_maneuver(74);
                                break;
                        }
                        break;

                    case 2:
                        this.spreadV3d.set(World.Rnd().nextFloat(-10F, 20F), 100 - World.Rnd().nextInt(0, 1) * 200 + World.Rnd().nextFloat(-50F, 50F), 0.0D);
                        this.set_maneuver(65);
                        break;

                    case 3:
                        this.spreadV3d.set(World.Rnd().nextFloat(-10F, 50F), -1400 + ((Maneuver) this.airClient).bracketSide * 2800 + World.Rnd().nextFloat(-200F, 200F), World.Rnd().nextFloat(0.0F, 550F));
                        this.set_maneuver(74);
                        break;

                    case 4:
                        this.spreadV3d.set(World.Rnd().nextFloat(-10F, 20F), 100 - World.Rnd().nextInt(0, 1) * 200 + World.Rnd().nextFloat(-50F, 50F), 0.0D);
                        this.set_maneuver(65);
                        break;
                }
                break;

            default:
                this.set_maneuver(65);
                break;
        }
    }

    private void assignManeuverToWingmen(int i) {
        for (Pilot pilot = this; pilot.Wingman != null;) {
            pilot = (Pilot) pilot.Wingman;
            pilot.set_maneuver(i);
        }

    }

    private void assignTaskToWingmen(int i) {
        for (Pilot pilot = this; pilot.Wingman != null;) {
            pilot = (Pilot) pilot.Wingman;
            pilot.set_task(i);
        }

    }

    public boolean isLeader() {
        if (this.actor instanceof TypeFighter) return ((Aircraft) this.actor).aircIndex() % 2 == 0;
        else return this.Leader == null;
    }

    public boolean isLonely(float f, boolean flag) {
        if (flag) {
            if (this.Leader == null && this.Wingman == null) return true;
            double d = 0.0D;
            if (this.Leader != null) d = this.Leader.Loc.distance(this.Loc);
            if (this.Wingman != null) d = Math.min(this.Wingman.Loc.distance(this.Loc), d);
            return d > f;
        }
        Actor actor = NearestTargets.getEnemy(9, -1, this.Loc, f, 0);
        if (Actor.isValid(actor)) return actor.pos.getAbsPoint().distance(this.Loc) > f;
        else return true;
    }

    public static final int       GTARGET_ALL      = 0;
    public static final int       GTARGET_TANKS    = 1;
    public static final int       GTARGET_FLAK     = 2;
    public static final int       GTARGET_VEHICLES = 3;
    public static final int       GTARGET_TRAIN    = 4;
    public static final int       GTARGET_BRIDGE   = 5;
    public static final int       GTARGET_SHIPS    = 6;
    public static final int       ATARGET_FIGHTERS = 7;
    public static final int       ATARGET_BOMBERS  = 8;
    public static final int       ATARGET_ALL      = 9;
    public static final int       TARGET_FIGHTERS  = 7;
    public static final int       TARGET_BOMBERS   = 8;
    public static final int       TARGET_ALL       = 9;
    private int                   airTargetType;
    private int                   groundTargetType;
    private long                  dumbOffTime;
    private int                   oldTask;
    private FlightModel           oldTaskObject;
    private Actor                 oldGTarget;
    private boolean               continueManeuver;
    private static final Vector3d MAIN_LOOK        = new Vector3d(0.34202014D, 0.0D, 0.9396926D);
    private static Vector3d       vecDanger        = new Vector3d();
    private static Vector3d       onMe             = new Vector3d();
    private static Vector3d       diffV            = new Vector3d();
    private double                diffVLength;
    private static Vector3f       tmpV             = new Vector3f();
    private static Point3d        p1               = new Point3d();
    private static Point3d        p2               = new Point3d();
    private static Point3f        p1f              = new Point3f();
    private static Point3f        p2f              = new Point3f();
    private boolean               visible;
    private boolean               near;
    private boolean               onBack;
    private boolean               looks;
    private boolean               higher;
    private boolean               faster;
    private boolean               energed;
    private float                 dist;
    private float                 dE;
    public boolean                bFromPlayer;
    private Actor                 act;
    private Actor                 actg;

}
