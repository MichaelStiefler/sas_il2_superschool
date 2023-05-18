// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 5/07/2011 3:39:50 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Pilot.java

package com.maddox.il2.ai.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunPara;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.ai.air:
//            Maneuver, AirGroup, Airdrome, NearestTargets

public class Pilot extends Maneuver
{

    public Pilot(String s)
    {
        super(s);
        airTargetType = 9;
        groundTargetType = 0;
        dumbOffTime = 0L;
        oldTask = 3;
        oldTaskObject = null;
        oldGTarget = null;
        continueManeuver = false;
        Energed = false;
        dist = 0.0F;
        dE = 0.0F;
    }

    public void targetAll()
    {
        airTargetType = 9;
    }

    public void targetFighters()
    {
        airTargetType = 7;
    }

    public void targetBombers()
    {
        airTargetType = 8;
    }

    public void attackGround(int i)
    {
        groundTargetType = i;
    }

    private boolean killed(Actor actor)
    {
        if(actor == null)
            return true;
        if(actor instanceof BridgeSegment)
            actor = actor.getOwner();
        if(Actor.isValid(actor))
            return !actor.isAlive();
        else
            return true;
    }

    public boolean killed(FlightModel flightmodel)
    {
        if(flightmodel == null)
            return true;
        if(flightmodel.AS.astatePilotStates[0] == 100)
            return true;
        if(Actor.isValid(flightmodel.actor))
            return killed(flightmodel.actor);
        else
            return flightmodel.isTakenMortalDamage();
    }

    private boolean detectable(Actor actor)
    {
        if(!(actor instanceof Aircraft))
            return false;
        if(Skill >= 1)
        {
            return true;
        } else
        {
            VDanger.set(((Aircraft)actor).FM.Loc);
            VDanger.sub(Loc);
            OnMe.scale(-1D, VDanger);
            Or.transformInv(VDanger);
            return VDanger.x >= 0.0D;
        }
    }

    public boolean isDumb()
    {
        return Time.current() < dumbOffTime;
    }

    public void setDumbTime(long l)
    {
        dumbOffTime = Time.current() + l;
    }

    public void addDumbTime(long l)
    {
        if(isDumb())
            dumbOffTime += l;
        else
            setDumbTime(l);
    }

    public void super_update(float f)
    {
        super.update(f);
    }

    public void update(float f)
    {
        if(actor.net != null && actor.net.isMirror())
        {
            ((com.maddox.il2.objects.air.NetAircraft.Mirror)actor.net).fmUpdate(f);
        } else
        {
            moveCarrier();
            if(TaxiMode)
            {
                World.cur().airdrome.update(this, f);
            } else
            {
                if(isTick(8, 0) || get_maneuver() == 0)
                {
                    setPriorities();
                    setTaskAndManeuver();
                }
                super.update(f);
            }
        }
    }

    protected void setPriorities()
    {
        if(killed(danger))
            danger = null;
        if(killed(target))
            target = null;
        if(killed(airClient))
            airClient = null;
        if(killed(target_ground))
            target_ground = null;
        setBusy(false);
        if(AS.isPilotDead(0))
        {
            setBusy(true);
            set_maneuver(44);
            if(crew > 1)
                ((Aircraft)actor).hitDaSilk();
            set_task(0);
        } else
        if(get_maneuver() == 46)
        {
            setBusy(true);
            dontSwitch = true;
        } else
        {
            float f = 0.0F;
            int i = EI.getNum();
            if(i != 0)
            {
                for(int j = 0; j < i; j++)
                    f += EI.engines[j].getReadyness() / (float)i;

                if(f < 0.7F)
                    setReadyToReturn(true);
                if(f < 0.3F)
                    setReadyToDie(true);
            }
            if(M.fuel < 0.3F * M.maxFuel)
            {
                int k = AP.way.Cur();
                AP.way.last();
                float f1 = AP.getWayPointDistance();
                AP.way.setCur(k);
                if(M.maxFuel < 0.001F)
                    M.maxFuel = 0.001F;
                float f2 = (1000F * Range * M.fuel) / M.maxFuel;
                if(f2 < 2.0F * f1 && !(actor instanceof TypeGlider))
                    setReadyToReturn(true);
            }
            if(M.fuel < 0.01F && !(actor instanceof TypeGlider))
                setReadyToDie(true);
            if(isTakenMortalDamage() || !isCapableOfBMP())
            {
                setBusy(true);
                ((Aircraft)actor).hitDaSilk();
                set_task(0);
                if(Group != null)
                    Group.delAircraft((Aircraft)actor);
            }
            if(isReadyToDie())
            {
                AP.way.setCur(1);
                continueManeuver = true;
                CT.dropFuelTanks();
                set_task(0);
                if(get_maneuver() != 49 && get_maneuver() != 12 && get_maneuver() != 54)
                {
                    clear_stack();
                    set_maneuver(49);
                }
                setBusy(true);
            } else
            if(get_maneuver() == 44 || get_maneuver() == 25 || get_maneuver() == 49 || get_maneuver() == 26 || get_maneuver() == 64 || get_maneuver() == 2 || get_maneuver() == 57 || get_maneuver() == 60 || get_maneuver() == 61)
            {
                setBusy(true);
                dontSwitch = true;
            } else
            if(getDangerAggressiveness() > 0.88F && danger != null && ((danger.actor instanceof TypeFighter) || (danger.actor instanceof TypeStormovik)) && ((Maneuver)danger).isOk())
            {
                if(get_task() != 4)
                {
                    set_task(4);
                    clear_stack();
                    set_maneuver(0);
                    if((actor instanceof TypeStormovik) && Group != null)
                    {
                        int l = Group.numInGroup((Aircraft)actor);
                        if(Group.nOfAirc >= l + 2)
                        {
                            Maneuver maneuver = (Maneuver)Group.airc[l + 1].FM;
                            Voice.speakCheckYour6((Aircraft)actor, (Aircraft)danger.actor);
                            Voice.speakHelpFromAir((Aircraft)maneuver.actor, 1);
                            maneuver.airClient = this;
                            set_task(6);
                            clear_stack();
                            maneuver.target = danger;
                            set_maneuver(27);
                            setBusy(true);
                            return;
                        }
                    }
                }
                Voice.speakClearTail((Aircraft)actor);
                setBusy(true);
            } else
            if(isReadyToReturn() && !AP.way.isLanding())
            {
                if(Group != null && Group.grTask != 1)
                {
                    AirGroup airgroup = new AirGroup(Group);
                    airgroup.rejoinGroup = null;
                    Group.delAircraft((Aircraft)actor);
                    airgroup.addAircraft((Aircraft)actor);
                    airgroup.w.last();
                    airgroup.w.prev();
                    airgroup.w.curr().setTimeout(3);
                    airgroup.timeOutForTaskSwitch = 10000;
                    AP.way.last();
                    AP.way.prev();
                    AP.way.curr().getP(p1f);
                    p1f.z = -10F + (float)Loc.z;
                }
                continueManeuver = true;
                CT.dropFuelTanks();
            } else
            {
                if(get_task() == 6)
                {
                    if(target != null && airClient != null && target == ((Maneuver)airClient).danger)
                    {
                        if(actor instanceof TypeStormovik)
                        {
                            if(((Maneuver)airClient).getDangerAggressiveness() > 0.0F)
                            {
                                setBusy(true);
                                return;
                            }
                            airClient = null;
                        }
                        setBusy(true);
                        return;
                    }
                    if((((Aircraft)actor).aircIndex() & 1) == 0 && Group != null)
                    {
                        int i1 = Group.numInGroup((Aircraft)actor);
                        if(Group.nOfAirc >= i1 + 2 && (Group.airc[i1 + 1].aircIndex() & 1) != 0)
                        {
                            Maneuver maneuver1 = (Maneuver)Group.airc[i1 + 1].FM;
                            if(maneuver1.airClient == this && maneuver1.getDangerAggressiveness() > 0.5F && maneuver1.danger != null && ((Maneuver)maneuver1.danger).isOk())
                            {
                                Voice.speakCheckYour6((Aircraft)maneuver1.actor, (Aircraft)maneuver1.danger.actor);
                                Voice.speakHelpFromAir((Aircraft)actor, 1);
                                set_task(6);
                                clear_stack();
                                target = maneuver1.danger;
                                set_maneuver(27);
                                setBusy(true);
                                return;
                            }
                        }
                    }
                    if(target != null && ((Maneuver)target).getDangerAggressiveness() > 0.5F && ((Maneuver)target).danger == this && ((Maneuver)target).isOk())
                    {
                        setBusy(true);
                        return;
                    }
                }
                if(isDumb())
                    setBusy(true);
                else
                if(get_task() == 7 && target_ground != null && get_maneuver() != 0)
                    setBusy(true);
                else
                if(get_task() == 3 && AP.way.isLanding())
                    setBusy(true);
            }
        }
    }

    private void setTaskAndManeuver()
    {
        if(dontSwitch)
            dontSwitch = false;
        else
        if(!isBusy())
        {
            if((wasBusy || get_maneuver() == 0) && Group != null)
            {
                clear_stack();
                Group.setTaskAndManeuver(Group.numInGroup((Aircraft)actor));
            }
        } else
        if(get_maneuver() == 0)
        {
            clear_stack();
            setManeuverByTask();
        }
    }

    public void setManeuverByTask()
    {
        clear_stack();
        switch(get_task())
        {
        default:
            break;

        case 2: // '\002'
            if(Leader != null && Actor.isValid(Leader.actor) && !Leader.isReadyToReturn() && !Leader.isReadyToDie() && Leader.getSpeed() > 35F)
                set_maneuver(24);
            else
                set_task(3);
            break;

        case 3: // '\003'
            set_maneuver(21);
            if(AP.way.isLanding())
                break;
            wingman(true);
            if(Leader != null)
                break;
            boolean flag = false;
            int j;
            float f1;
            if(AP.way.Cur() < AP.way.size() - 1)
            {
                AP.way.next();
                j = AP.way.curr().Action;
                boolean flag1 = AP.way.curr().getTarget() != null;
                f1 = AP.getWayPointDistance();
                AP.way.prev();
            } else
            {
                j = AP.way.curr().Action;
                f1 = AP.getWayPointDistance();
            }
            Pilot pilot = (Pilot)((Aircraft)actor).FM;
            float f2 = AP.getWayPointDistance();
            do
            {
                if(pilot.Wingman == null)
                    break;
                pilot = (Pilot)pilot.Wingman;
                pilot.wingman(true);
                pilot.AP.way.setCur(AP.way.Cur());
                if(!pilot.AP.way.isLanding() && pilot.get_task() == 3)
                    pilot.set_task(2);
            } while(true);
            if(((Aircraft)actor).aircIndex() == 0 && Speak5minutes == 0 && j == 3 && (double)f1 < 30000D)
            {
                Voice.speak5minutes((Aircraft)actor);
                Speak5minutes = 1;
            }
            break;

        case 4: // '\004'
            if(get_maneuver() == 0)
                set_maneuver(21);
            if(danger == null)
            {
                set_task(3);
                break;
            }
            if(actor instanceof TypeFighter)
            {
                continueManeuver = true;
                CT.dropFuelTanks();
            }
            dist = (float)Loc.distance(danger.Loc);
            VDanger.sub(danger.Loc, Loc);
            OnMe.scale(-1D, VDanger);
            Maneuver.tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
            Maneuver.tmpOr.transformInv(VDanger);
            diffV.sub(danger.Vwld, Vwld);
            Maneuver.tmpOr.transformInv(diffV);
            diffVLength = diffV.length();
            Maneuver.tmpOr.setYPR(danger.Or.getYaw(), 0.0F, 0.0F);
            danger.Or.transformInv(OnMe);
            VDanger.normalize();
            OnMe.normalize();
            dE = (Energy - danger.Energy) * 0.1019F;
            Energed = danger.Energy > Energy;
            Faster = danger.getSpeed() > getSpeed();
            Higher = danger.Loc.z > Loc.z;
            Near = dist < World.Rnd().nextFloat(0.9F, 3F) * 300F;
            OnBack = VDanger.x < 0.0D && dist < 2000F;
            Visible = VDanger.dot(MainLook) > 0.0D;
            Looks = OnMe.x > 0.0D;
            VDanger.normalize();
            if(OnBack && Near && (danger instanceof TypeFighter) && ((actor instanceof TypeTransport) || Wingman == null || killed(Wingman) || ((Pilot)Wingman).target != danger))
                if(isLeader())
                {
                    if(((actor instanceof TypeFighter) || (actor instanceof TypeStormovik) && Skill > 1 && AP.way.curr().Action == 0) && Wingman != null && !killed(Wingman) && !((Pilot)Wingman).requestCoverFor(this))
                        if(Wingman.Wingman != null && !killed(Wingman.Wingman))
                            ((Pilot)Wingman.Wingman).requestCoverFor(this);
                        else
                        if(Skill >= 1)
                        {
                            Aircraft aircraft = War.getNearestFriendlyFighter((Aircraft)actor, 8000F);
                            if(aircraft != null && (aircraft.FM instanceof Pilot))
                                ((Pilot)aircraft.FM).requestCoverFor(this);
                        }
                } else
                if(Skill >= 1)
                {
                    Aircraft aircraft1 = War.getNearestFriendlyFighter((Aircraft)actor, 8000F);
                    if((aircraft1 instanceof TypeFighter) && (aircraft1.FM instanceof Pilot))
                        ((Pilot)aircraft1.FM).requestCoverFor(this);
                }
            if(actor instanceof TypeFighter)
            {
                fighterDefence();
                break;
            }
            if(actor instanceof TypeStormovik)
                stormovikDefence();
            else
                transportDefence();
            break;

        case 5: // '\005'
            if(airClient != null && !killed(airClient))
            {
                followOffset.set(150D, 0.0D, 20D);
                set_maneuver(65);
                break;
            }
            airClient = null;
            if(target != null && !killed(target))
            {
                set_task(6);
                set_maneuver(0);
            } else
            {
                set_task(3);
                set_maneuver(21);
            }
            break;

        case 6: // '\006'
            WeWereInAttack = true;
            if(actor instanceof TypeFighter)
            {
                continueManeuver = true;
                CT.dropFuelTanks();
            }
            if(target == null || !hasCourseWeaponBullets())
                if(AP.way.curr().Action == 3)
                {
                    set_task(7);
                    set_maneuver(0);
                } else
                {
                    set_task(3);
                    if(Leader != null)
                        set_maneuver(24);
                    else
                        set_maneuver(21);
                }
            int i = ((Aircraft)actor).aircIndex();
            if(target.actor instanceof TypeBomber)
            {
                attackBombers();
                break;
            }
            if(target.actor instanceof TypeStormovik)
            {
                attackStormoviks();
                break;
            }
            if(i == 0 || i == 2)
                set_maneuver(27);
            if(i != 1 && i != 3)
                break;
            if(Leader != null && !killed(Leader))
            {
                airClient = Leader;
                set_task(5);
                set_maneuver(0);
            } else
            {
                set_maneuver(27);
            }
            break;

        case 7: // '\007'
            if(!WeWereInGAttack)
                WeWereInGAttack = true;
            if(!Actor.isAlive(target_ground))
            {
                set_task(2);
                set_maneuver(0);
                break;
            }
            boolean flag2 = true;
            if(CT.Weapons[0] != null && CT.Weapons[0][0] != null && CT.Weapons[0][0].bulletMassa() > 0.05F && CT.Weapons[0][0].countBullets() > 0)
                flag2 = false;
            if(flag2 && CT.getWeaponMass() < 15F || CT.getWeaponMass() < 1.0F)
            {
                Voice.speakEndOfAmmo((Aircraft)actor);
                set_task(2);
                set_maneuver(0);
                if(AP.way.curr().Action != 3)
                    AP.way.next();
                target_ground = null;
            }
            if((target_ground instanceof Prey) && (((Prey)target_ground).HitbyMask() & 1) == 0)
            {
                float f = 0.0F;
                for(int k = 0; k < 4; k++)
                    if(CT.Weapons[k] != null && CT.Weapons[k][0] != null && CT.Weapons[k][0].countBullets() != 0 && CT.Weapons[k][0].bulletMassa() > f)
                        f = CT.Weapons[k][0].bulletMassa();

                if(f < 0.08F || (target_ground instanceof TgtShip) && f < 0.55F)
                {
                    set_task(2);
                    set_maneuver(0);
                    if(AP.way.curr().Action != 3)
                        AP.way.next();
                    target_ground = null;
                }
            }
            if(CT.Weapons[3] != null && CT.Weapons[3][0] != null && CT.Weapons[3][0].countBullets() != 0)
            {
                if(CT.Weapons[3][0] instanceof TorpedoGun)
                {
                    if(target_ground instanceof TgtShip)
                        set_maneuver(51);
                    else
                        set_maneuver(43);
                    break;
                }
                if(CT.Weapons[3][0] instanceof BombGunPara)
                {
                    AP.way.curr().setTarget(null);
                    target_ground = null;
                    set_maneuver(21);
                    break;
                }
                if(CT.Weapons[3][0].bulletMassa() < 10F)
                {
                    set_maneuver(52);
                    break;
                }
                if((actor instanceof TypeDiveBomber) && Alt > 1200F)
                    set_maneuver(50);
                else
                    set_maneuver(43);
                break;
            }
            if(target_ground instanceof BridgeSegment)
            {
                set_task(2);
                set_maneuver(0);
                if(AP.way.curr().Action != 3)
                    AP.way.next();
                target_ground = null;
            }
            if((actor instanceof TypeFighter) || (actor instanceof TypeStormovik))
            {
                set_maneuver(43);
                break;
            }
            set_task(2);
            set_maneuver(0);
            if(AP.way.curr().Action != 3)
                AP.way.next();
            target_ground = null;
            break;

        case 0: // '\0'
            if(isReadyToDie())
                set_maneuver(49);
            break;

        case 1: // '\001'
            set_maneuver(45);
            break;
        }
    }

    public boolean requestCoverFor(FlightModel flightmodel)
    {
        if(actor instanceof TypeTransport)
        {
            Voice.speakHelpFromAir((Aircraft)actor, 0);
            return false;
        }
        if(danger == null || ((Pilot)danger).target != this || danger.Loc.distance(Loc) > 600D + 200D * (double)Skill || (danger.actor instanceof TypeStormovik) || (danger.actor instanceof TypeBomber))
        {
            if(((Pilot)flightmodel).danger == null || killed(((Pilot)flightmodel).danger) || (((Pilot)flightmodel).danger.actor instanceof TypeTransport) || ((Pilot)flightmodel).danger.Loc.distance(flightmodel.Loc) > 3000D)
            {
                Voice.speakHelpFromAir((Aircraft)actor, 2);
                return true;
            }
            set_task(6);
            set_maneuver(27);
            target = ((Pilot)flightmodel).danger;
            if(World.Rnd().nextBoolean())
                Voice.speakCoverProvided((Aircraft)actor, (Aircraft)flightmodel.actor);
            else
                Voice.speakHelpFromAir((Aircraft)actor, 1);
            return true;
        } else
        {
            Voice.speakHelpFromAir((Aircraft)actor, 0);
            return false;
        }
    }

    public void setAsDanger(Actor actor)
    {
        if(get_maneuver() != 44 && get_maneuver() != 26 && (!isDumb() || isReadyToReturn()))
            if(actor.getArmy() == this.actor.getArmy())
            {
                set_maneuver(8);
                setDumbTime(5000L);
                if(actor instanceof Aircraft)
                    Voice.speakCheckFire((Aircraft)this.actor, (Aircraft)actor);
            } else
            if(!Actor.isValid(this.actor))
            {
                if(World.cur().isArcade())
                {
                    Aircraft.debugprintln(actor, "Jeopardizing invalid actor (one being destroyed)..");
                    Explosions.generateComicBulb(actor, "Sucker", 5F);
                    if((actor instanceof TypeFighter) && (((Aircraft)actor).FM instanceof Pilot))
                        ((Pilot)((Aircraft)actor).FM).set_maneuver(35);
                }
                Voice.speakNiceKill((Aircraft)actor);
            } else
            {
                switch(Skill)
                {
                case 0: // '\0'
                    if(World.Rnd().nextInt(0, 99) >= 98)
                    {
                        if(actor instanceof Aircraft)
                        {
                            Vector3d vector3d = new Vector3d();
                            vector3d.sub(Loc, ((Aircraft)actor).FM.Loc);
                            ((Aircraft)actor).FM.Or.transformInv(vector3d);
                            if(vector3d.z > 0.0D)
                                return;
                        }
                    } else
                    {
                        return;
                    }
                    break;

                case 1: // '\001'
                    if(!detectable(actor) || World.Rnd().nextInt(0, 99) < 97)
                        return;
                    break;

                case 2: // '\002'
                    if(getMnTime() < 1.0F)
                        return;
                    break;

                case 3: // '\003'
                    if(getMnTime() < 1.0F)
                        return;
                    break;
                }
                if(this.actor instanceof TypeTransport)
                {
                    if(AP.way.curr().Action != 3 && (get_maneuver() == 24 || get_maneuver() == 21))
                    {
                        set_task(4);
                        set_maneuver(0);
                    }
                } else
                {
                    if(get_task() == 2)
                    {
                        set_task(3);
                        set_maneuver(0);
                    }
                    if(actor instanceof Aircraft)
                    {
                        if(actor instanceof TypeFighter)
                        {
                            if(turret.length > 0 && AS.astatePilotStates[turret.length] < 90)
                                Voice.speakDanger((Aircraft)this.actor, 4);
                            Voice.speakDanger((Aircraft)this.actor, 0);
                        }
                        Aircraft aircraft = (Aircraft)actor;
                        Pilot pilot = this;
                        pilot.danger = aircraft.FM;
                        if((this.actor instanceof TypeFighter) && get_task() != 4)
                        {
                            target = aircraft.FM;
                            set_task(4);
                            set_maneuver(0);
                            clear_stack();
                        }
                    }
                }
            }
    }

    private void transportDefence()
    {
        switch(Skill)
        {
        default:
            break;

        case 0: // '\0'
            set_maneuver(21);
            break;

        case 1: // '\001'
            if(isLonely(30F, true))
                set_maneuver(38);
            break;

        case 2: // '\002'
        case 3: // '\003'
            if(!isLonely(40F, false))
                break;
            switch(World.Rnd().nextInt(0, 3))
            {
            case 0: // '\0'
                set_maneuver(39);
                break;

            case 1: // '\001'
                set_maneuver(38);
                break;

            case 2: // '\002'
                set_maneuver(15);
                break;

            case 3: // '\003'
                set_maneuver(14);
                break;
            }
            break;
        }
    }

    private void stormovikDefence()
    {
        if(dist > 400F)
            set_maneuver(3);
        else
            switch(Skill)
            {
            default:
                break;

            case 0: // '\0'
                if(World.Rnd().nextBoolean())
                    set_maneuver(14);
                else
                    set_maneuver(56);
                break;

            case 1: // '\001'
                if(Visible)
                {
                    set_maneuver(14);
                    break;
                }
                switch(World.Rnd().nextInt(0, 7))
                {
                case 0: // '\0'
                    set_maneuver(29);
                    break;

                case 1: // '\001'
                    set_maneuver(33);
                    break;

                case 2: // '\002'
                case 3: // '\003'
                case 4: // '\004'
                case 5: // '\005'
                    set_maneuver(14);
                    break;

                case 6: // '\006'
                case 7: // '\007'
                    set_maneuver(39);
                    break;
                }
                break;

            case 2: // '\002'
            case 3: // '\003'
                if(Visible)
                {
                    if(VDanger.x > 0.93999999761581421D)
                    {
                        switch(World.Rnd().nextInt(0, 13))
                        {
                        case 0: // '\0'
                        case 1: // '\001'
                        case 2: // '\002'
                            set_maneuver(38);
                            break;

                        case 3: // '\003'
                        case 4: // '\004'
                            set_maneuver(27);
                            target = danger;
                            break;

                        case 5: // '\005'
                        case 6: // '\006'
                            set_maneuver(39);
                            break;

                        case 7: // '\007'
                        case 8: // '\b'
                        case 9: // '\t'
                            set_maneuver(29);
                            break;

                        case 10: // '\n'
                        case 11: // '\013'
                            set_maneuver(6);
                            break;

                        case 12: // '\f'
                            set_maneuver(56);
                            break;

                        case 13: // '\r'
                            set_maneuver(14);
                            break;
                        }
                        break;
                    }
                    if(Math.abs(VDanger.x) < 0.27500000596046448D)
                    {
                        switch(World.Rnd().nextInt(0, 7))
                        {
                        case 0: // '\0'
                            set_maneuver(16);
                            break;

                        case 1: // '\001'
                            set_maneuver(17);
                            break;

                        case 2: // '\002'
                        case 3: // '\003'
                        case 4: // '\004'
                            set_maneuver(28);
                            break;

                        case 5: // '\005'
                        case 6: // '\006'
                            set_maneuver(6);
                            break;

                        case 7: // '\007'
                            set_maneuver(13);
                            break;
                        }
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                    case 1: // '\001'
                        set_maneuver(14);
                        break;

                    case 2: // '\002'
                        set_maneuver(29);
                        break;

                    case 3: // '\003'
                        set_maneuver(39);
                        break;
                    }
                    break;
                }
                switch(World.Rnd().nextInt(0, 17))
                {
                case 0: // '\0'
                case 1: // '\001'
                case 2: // '\002'
                case 3: // '\003'
                case 4: // '\004'
                    set_maneuver(29);
                    break;

                case 5: // '\005'
                case 6: // '\006'
                    set_maneuver(39);
                    break;

                case 7: // '\007'
                case 8: // '\b'
                    set_maneuver(28);
                    break;

                case 9: // '\t'
                case 10: // '\n'
                    set_maneuver(39);
                    break;

                case 11: // '\013'
                    set_maneuver(38);
                    break;

                case 12: // '\f'
                case 13: // '\r'
                    set_maneuver(32);
                    break;

                case 14: // '\016'
                    set_maneuver(7);
                    break;

                case 15: // '\017'
                case 16: // '\020'
                case 17: // '\021'
                    set_maneuver(15);
                    break;
                }
                break;
            }
    }

    private void fighterDefence()
    {
        switch(Skill)
        {
        default:
            break;

        case 0: // '\0'
            if(VDanger.x > 0.90000000000000002D)
            {
                set_maneuver(15);
                break;
            }
            switch(World.Rnd().nextInt(0, 3))
            {
            case 0: // '\0'
            case 1: // '\001'
                set_maneuver(15);
                break;

            default:
                set_maneuver(15);
                break;
            }
            break;

        case 1: // '\001'
            if((double)dE > 500D)
            {
                if(VDanger.x > 0.90000000000000002D)
                {
                    set_maneuver(27);
                    break;
                }
                switch(World.Rnd().nextInt(0, 3))
                {
                case 0: // '\0'
                case 1: // '\001'
                    set_maneuver(32);
                    break;

                default:
                    set_maneuver(16);
                    break;
                }
                break;
            }
            if((double)dE > 200D)
            {
                if(VDanger.x > 0.90000000000000002D)
                {
                    set_maneuver(27);
                    break;
                }
                switch(World.Rnd().nextInt(0, 6))
                {
                case 1: // '\001'
                case 2: // '\002'
                case 3: // '\003'
                    set_maneuver(39);
                    break;

                case 4: // '\004'
                    set_maneuver(35);
                    break;

                default:
                    set_maneuver(39);
                    break;
                }
                break;
            }
            if((double)dE > -25D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(19);
                                break;

                            case 1: // '\001'
                                set_maneuver(33);
                                break;

                            case 2: // '\002'
                                set_maneuver(28);
                                break;

                            case 3: // '\003'
                                set_maneuver(36);
                                break;

                            case 4: // '\004'
                                set_maneuver(32);
                                break;

                            case 5: // '\005'
                                set_maneuver(29);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 4))
                            {
                            case 0: // '\0'
                                set_maneuver(32);
                                break;

                            case 1: // '\001'
                                set_maneuver(18);
                                break;

                            case 2: // '\002'
                                set_maneuver(28);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(28);
                        break;

                    case 1: // '\001'
                        set_maneuver(29);
                        break;

                    case 2: // '\002'
                        set_maneuver(33);
                        break;

                    case 3: // '\003'
                        set_maneuver(32);
                        break;

                    default:
                        set_maneuver(29);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(33);
                        break;

                    case 1: // '\001'
                        set_maneuver(36);
                        break;

                    default:
                        set_maneuver(29);
                        break;
                    }
                else
                    set_maneuver(19);
                break;
            }
            if((double)dE > -200D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(19);
                                break;

                            case 1: // '\001'
                                set_maneuver(32);
                                break;

                            case 2: // '\002'
                                set_maneuver(16);
                                break;

                            case 3: // '\003'
                                set_maneuver(34);
                                break;

                            case 4: // '\004'
                                set_maneuver(35);
                                break;

                            case 5: // '\005'
                                set_maneuver(31);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 4))
                            {
                            case 0: // '\0'
                                set_maneuver(32);
                                break;

                            case 1: // '\001'
                                set_maneuver(28);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(27);
                        break;

                    case 1: // '\001'
                        set_maneuver(28);
                        break;

                    case 2: // '\002'
                        set_maneuver(29);
                        break;

                    case 3: // '\003'
                        set_maneuver(31);
                        break;

                    default:
                        set_maneuver(42);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(28);
                        break;

                    case 1: // '\001'
                        set_maneuver(34);
                        break;

                    default:
                        set_maneuver(29);
                        break;
                    }
                else
                    set_maneuver(19);
                break;
            }
            if((double)dE > -400D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(3);
                                break;

                            case 1: // '\001'
                                set_maneuver(32);
                                break;

                            case 2: // '\002'
                                set_maneuver(33);
                                break;

                            case 3: // '\003'
                                set_maneuver(30);
                                break;

                            case 4: // '\004'
                                set_maneuver(39);
                                break;

                            case 5: // '\005'
                                set_maneuver(31);
                                break;

                            default:
                                set_maneuver(15);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 5))
                            {
                            case 0: // '\0'
                                set_maneuver(34);
                                break;

                            case 1: // '\001'
                                set_maneuver(3);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            case 3: // '\003'
                                set_maneuver(34);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(32);
                        break;

                    case 1: // '\001'
                        set_maneuver(29);
                        break;

                    case 2: // '\002'
                        set_maneuver(3);
                        break;

                    case 3: // '\003'
                        set_maneuver(39);
                        break;

                    default:
                        set_maneuver(28);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(19);
                        break;

                    case 1: // '\001'
                        set_maneuver(3);
                        break;

                    default:
                        set_maneuver(33);
                        break;
                    }
                else
                    set_maneuver(28);
                break;
            }
            switch(World.Rnd().nextInt(0, 5))
            {
            case 0: // '\0'
                set_maneuver(30);
                break;

            case 1: // '\001'
                set_maneuver(3);
                break;

            case 2: // '\002'
                set_maneuver(18);
                break;

            case 3: // '\003'
                set_maneuver(33);
                break;

            default:
                set_maneuver(34);
                break;
            }
            break;

        case 2: // '\002'
            if((double)dE > 500D)
            {
                if(VDanger.x > 0.90000000000000002D)
                {
                    set_maneuver(27);
                    break;
                }
                if(OnMe.x > 0.90000000000000002D && (double)dist < 200D)
                {
                    set_maneuver(28);
                    break;
                }
                switch(World.Rnd().nextInt(0, 3))
                {
                case 0: // '\0'
                    set_maneuver(67);
                    break;

                case 1: // '\001'
                    set_maneuver(40);
                    break;

                default:
                    set_maneuver(16);
                    break;
                }
                break;
            }
            if((double)dE > 200D)
            {
                if(VDanger.x > 0.90000000000000002D)
                {
                    set_maneuver(27);
                    break;
                }
                if(OnMe.x > 0.90000000000000002D && (double)dist > 200D)
                {
                    set_maneuver(33);
                    break;
                }
                switch(World.Rnd().nextInt(0, 3))
                {
                case 0: // '\0'
                    set_maneuver(67);
                    break;

                case 1: // '\001'
                    set_maneuver(40);
                    break;

                default:
                    set_maneuver(39);
                    break;
                }
                break;
            }
            if((double)dE > -25D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(19);
                                break;

                            case 1: // '\001'
                                set_maneuver(19);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            case 3: // '\003'
                                set_maneuver(29);
                                break;

                            case 4: // '\004'
                                set_maneuver(30);
                                break;

                            case 5: // '\005'
                                set_maneuver(15);
                                break;

                            default:
                                set_maneuver(14);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 4))
                            {
                            case 0: // '\0'
                                set_maneuver(29);
                                break;

                            case 1: // '\001'
                                set_maneuver(8);
                                break;

                            case 2: // '\002'
                                set_maneuver(28);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                        else
                            set_maneuver(5);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(30);
                        break;

                    case 1: // '\001'
                        set_maneuver(19);
                        break;

                    case 2: // '\002'
                        set_maneuver(28);
                        break;

                    case 3: // '\003'
                        set_maneuver(33);
                        break;

                    default:
                        set_maneuver(39);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(28);
                        break;

                    case 1: // '\001'
                        set_maneuver(29);
                        break;

                    default:
                        set_maneuver(67);
                        break;
                    }
                else
                    set_maneuver(67);
                break;
            }
            if((double)dE > -200D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(34);
                                break;

                            case 1: // '\001'
                                set_maneuver(19);
                                break;

                            case 2: // '\002'
                                set_maneuver(33);
                                break;

                            case 3: // '\003'
                                set_maneuver(34);
                                break;

                            case 4: // '\004'
                                set_maneuver(7);
                                break;

                            case 5: // '\005'
                                set_maneuver(32);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 4))
                            {
                            case 0: // '\0'
                                set_maneuver(29);
                                break;

                            case 1: // '\001'
                                set_maneuver(37);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(32);
                        break;

                    case 1: // '\001'
                        set_maneuver(19);
                        break;

                    case 2: // '\002'
                        set_maneuver(33);
                        break;

                    case 3: // '\003'
                        set_maneuver(34);
                        break;

                    default:
                        set_maneuver(33);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(19);
                        break;

                    case 1: // '\001'
                        set_maneuver(34);
                        break;

                    default:
                        set_maneuver(67);
                        break;
                    }
                else
                    set_maneuver(67);
                break;
            }
            if((double)dE > -400D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(34);
                                break;

                            case 1: // '\001'
                                set_maneuver(3);
                                break;

                            case 2: // '\002'
                                set_maneuver(19);
                                break;

                            case 3: // '\003'
                                set_maneuver(31);
                                break;

                            case 4: // '\004'
                                set_maneuver(30);
                                break;

                            case 5: // '\005'
                                set_maneuver(36);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(28);
                                break;

                            case 1: // '\001'
                                set_maneuver(33);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            case 3: // '\003'
                                set_maneuver(28);
                                break;

                            case 4: // '\004'
                                set_maneuver(33);
                                break;

                            case 5: // '\005'
                                set_maneuver(32);
                                break;

                            default:
                                set_maneuver(40);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(28);
                        break;

                    case 1: // '\001'
                        set_maneuver(17);
                        break;

                    case 2: // '\002'
                        set_maneuver(32);
                        break;

                    case 3: // '\003'
                        set_maneuver(36);
                        break;

                    default:
                        set_maneuver(15);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 6))
                    {
                    case 0: // '\0'
                        set_maneuver(19);
                        break;

                    case 1: // '\001'
                        set_maneuver(31);
                        break;

                    case 2: // '\002'
                        set_maneuver(31);
                        break;

                    case 3: // '\003'
                        set_maneuver(31);
                        break;

                    case 4: // '\004'
                        set_maneuver(3);
                        break;

                    default:
                        set_maneuver(67);
                        break;
                    }
                else
                    set_maneuver(67);
                break;
            }
            switch(World.Rnd().nextInt(0, 5))
            {
            case 0: // '\0'
                set_maneuver(27);
                break;

            case 1: // '\001'
                set_maneuver(29);
                break;

            case 2: // '\002'
                set_maneuver(19);
                break;

            case 3: // '\003'
                set_maneuver(33);
                break;

            default:
                set_maneuver(34);
                break;
            }
            break;

        case 3: // '\003'
            if((double)dE > 500D)
            {
                if(VDanger.x > 0.90000000000000002D)
                {
                    set_maneuver(27);
                    break;
                }
                if(OnMe.x > 0.90000000000000002D && (double)dist < 200D)
                {
                    set_maneuver(19);
                    break;
                }
                switch(World.Rnd().nextInt(0, 3))
                {
                case 0: // '\0'
                    set_maneuver(67);
                    break;

                case 1: // '\001'
                    set_maneuver(28);
                    break;

                default:
                    set_maneuver(16);
                    break;
                }
                break;
            }
            if((double)dE > 200D)
            {
                if(VDanger.x > 0.90000000000000002D)
                {
                    set_maneuver(27);
                    break;
                }
                if(OnMe.x > 0.90000000000000002D && (double)dist > 200D)
                {
                    set_maneuver(33);
                    break;
                }
                switch(World.Rnd().nextInt(0, 3))
                {
                case 0: // '\0'
                    set_maneuver(67);
                    break;

                case 1: // '\001'
                    set_maneuver(35);
                    break;

                default:
                    set_maneuver(16);
                    break;
                }
                break;
            }
            if((double)dE > -25D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(33);
                                break;

                            case 1: // '\001'
                                set_maneuver(18);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            case 3: // '\003'
                                set_maneuver(37);
                                break;

                            case 4: // '\004'
                                set_maneuver(36);
                                break;

                            case 5: // '\005'
                                set_maneuver(28);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 4))
                            {
                            case 0: // '\0'
                                set_maneuver(29);
                                break;

                            case 1: // '\001'
                                set_maneuver(36);
                                break;

                            case 2: // '\002'
                                set_maneuver(28);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(30);
                        break;

                    case 1: // '\001'
                        set_maneuver(19);
                        break;

                    case 2: // '\002'
                        set_maneuver(28);
                        break;

                    case 3: // '\003'
                        set_maneuver(33);
                        break;

                    default:
                        set_maneuver(39);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(28);
                        break;

                    case 1: // '\001'
                        set_maneuver(29);
                        break;

                    default:
                        set_maneuver(67);
                        break;
                    }
                else
                    set_maneuver(67);
                break;
            }
            if((double)dE > -200D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(32);
                                break;

                            case 1: // '\001'
                                set_maneuver(18);
                                break;

                            case 2: // '\002'
                                set_maneuver(33);
                                break;

                            case 3: // '\003'
                                set_maneuver(34);
                                break;

                            case 4: // '\004'
                                set_maneuver(7);
                                break;

                            case 5: // '\005'
                                set_maneuver(32);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 4))
                            {
                            case 0: // '\0'
                                set_maneuver(29);
                                break;

                            case 1: // '\001'
                                set_maneuver(32);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(34);
                        break;

                    case 1: // '\001'
                        set_maneuver(19);
                        break;

                    case 2: // '\002'
                        set_maneuver(33);
                        break;

                    case 3: // '\003'
                        set_maneuver(34);
                        break;

                    default:
                        set_maneuver(19);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(3);
                        break;

                    case 1: // '\001'
                        set_maneuver(34);
                        break;

                    default:
                        set_maneuver(67);
                        break;
                    }
                else
                    set_maneuver(67);
                break;
            }
            if((double)dE > -400D)
            {
                if(VDanger.x > 0.80000000000000004D)
                {
                    set_maneuver(27);
                    break;
                }
                if(diffVLength < 50D && OnMe.x > 0.80000000000000004D)
                {
                    if((double)dist < 200D)
                    {
                        if((double)Alt > 500D)
                        {
                            switch(World.Rnd().nextInt(0, 7))
                            {
                            case 0: // '\0'
                                set_maneuver(34);
                                break;

                            case 1: // '\001'
                                set_maneuver(34);
                                break;

                            case 2: // '\002'
                                set_maneuver(19);
                                break;

                            case 3: // '\003'
                                set_maneuver(31);
                                break;

                            case 4: // '\004'
                                set_maneuver(30);
                                break;

                            case 5: // '\005'
                                set_maneuver(36);
                                break;

                            default:
                                set_maneuver(29);
                                break;
                            }
                            break;
                        }
                        if(Alt > 150F)
                            switch(World.Rnd().nextInt(0, 4))
                            {
                            case 0: // '\0'
                                set_maneuver(28);
                                break;

                            case 1: // '\001'
                                set_maneuver(34);
                                break;

                            case 2: // '\002'
                                set_maneuver(32);
                                break;

                            default:
                                set_maneuver(34);
                                break;
                            }
                        else
                            set_maneuver(29);
                        break;
                    }
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        set_maneuver(32);
                        break;

                    case 1: // '\001'
                        set_maneuver(34);
                        break;

                    case 2: // '\002'
                        set_maneuver(32);
                        break;

                    case 3: // '\003'
                        set_maneuver(36);
                        break;

                    default:
                        set_maneuver(40);
                        break;
                    }
                    break;
                }
                if(OnMe.x > 0.90000000000000002D)
                    switch(World.Rnd().nextInt(0, 3))
                    {
                    case 0: // '\0'
                        set_maneuver(34);
                        break;

                    case 1: // '\001'
                        set_maneuver(31);
                        break;

                    default:
                        set_maneuver(67);
                        break;
                    }
                else
                    set_maneuver(67);
                break;
            }
            switch(World.Rnd().nextInt(0, 5))
            {
            case 0: // '\0'
                set_maneuver(27);
                break;

            case 1: // '\001'
                set_maneuver(32);
                break;

            case 2: // '\002'
                set_maneuver(19);
                break;

            case 3: // '\003'
                set_maneuver(33);
                break;

            default:
                set_maneuver(34);
                break;
            }
            break;
        }
    }

    public void attackBombers()
    {
        float f = 0.0F;
        if(CT.Weapons[1] != null && ((GunGeneric)CT.Weapons[1][0]).countBullets() > 0)
            f = ((GunGeneric)CT.Weapons[1][0]).bulletMassa();
        if(actor instanceof ME_163B1A)
            switch(World.Rnd().nextInt(0, 2))
            {
            case 0: // '\0'
                setBomberAttackType(7);
                break;

            default:
                setBomberAttackType(2);
                break;
            }
        else     	
        //TODO:DBW Version
        if(((actor instanceof TypeJazzPlayer) || (actor instanceof KI_46_OTSUHEI))&& ((GunGeneric)CT.Weapons[0][0]).countBullets() > 0)
        	switch(World.Rnd().nextInt(0, 3))
        	{
        	case 0: // '\0'
        		setBomberAttackType(7);
        		break;

        	case 1: // '\0'
        		setBomberAttackType(2);
        		break;

        	default:
        		setBomberAttackType(10);
        		break;
        	}
        else
        if(((actor instanceof TypeSupersonic)))
        	setBomberAttackType(7);
        //DBW End
        else
        if(((actor instanceof TypeBNZFighter) || (actor instanceof F4U) || (actor instanceof F6F) || (actor instanceof F4F) || (actor instanceof SPITFIRE5C2) || (actor instanceof SPITFIRE5C4)) && !(actor instanceof ME_262))
            switch(World.Rnd().nextInt(0, 7))
            {
            case 0: // '\0'
                setBomberAttackType(3);
                break;

            case 1: // '\001'
                setBomberAttackType(4);
                break;

            case 2: // '\002'
                setBomberAttackType(3);
                break;

            case 3: // '\003'
                setBomberAttackType(2);
                break;

            case 4: // '\004'
                setBomberAttackType(3);
                break;

            case 5: // '\005'
                setBomberAttackType(0);
                break;

            case 6: // '\006'
                setBomberAttackType(6);
                break;

            default:
                setBomberAttackType(1);
                break;
            }
        else
            switch(Skill)
            {
            default:
                break;

            case 0: // '\0'
                switch(World.Rnd().nextInt(0, 5))
                {
                case 0: // '\0'
                    setBomberAttackType(8);
                    break;

                case 1: // '\001'
                    setBomberAttackType(0);
                    break;

                default:
                    setBomberAttackType(5);
                    break;
                }
                break;

            case 1: // '\001'
                if(f > 0.09F)
                {
                    switch(World.Rnd().nextInt(0, 8))
                    {
                    case 0: // '\0'
                        setBomberAttackType(0);
                        break;

                    case 1: // '\001'
                        setBomberAttackType(6);
                        break;

                    case 2: // '\002'
                        setBomberAttackType(8);
                        break;

                    case 3: // '\003'
                        setBomberAttackType(9);
                        break;

                    case 4: // '\004'
                        setBomberAttackType(4);
                        break;

                    case 5: // '\005'
                        setBomberAttackType(0);
                        break;

                    case 6: // '\006'
                        setBomberAttackType(7);
                        break;

                    case 7: // '\007'
                        setBomberAttackType(8);
                        break;

                    default:
                        setBomberAttackType(8);
                        break;
                    }
                    break;
                }
                switch(World.Rnd().nextInt(0, 6))
                {
                case 0: // '\0'
                    setBomberAttackType(1);
                    break;

                case 1: // '\001'
                    setBomberAttackType(7);
                    break;

                default:
                    setBomberAttackType(2);
                    break;
                }
                break;

            case 2: // '\002'
                if(f > 0.09F)
                {
                    switch(World.Rnd().nextInt(0, 6))
                    {
                    case 0: // '\0'
                        setBomberAttackType(0);
                        break;

                    case 1: // '\001'
                        setBomberAttackType(1);
                        break;

                    case 2: // '\002'
                        setBomberAttackType(8);
                        break;

                    case 3: // '\003'
                        setBomberAttackType(8);
                        break;

                    default:
                        setBomberAttackType(0);
                        break;
                    }
                    break;
                }
                if(f > 0.05F)
                {
                    switch(World.Rnd().nextInt(0, 10))
                    {
                    case 0: // '\0'
                    case 1: // '\001'
                    case 2: // '\002'
                        setBomberAttackType(1);
                        break;

                    case 3: // '\003'
                        setBomberAttackType(7);
                        break;

                    case 4: // '\004'
                        setBomberAttackType(6);
                        break;

                    default:
                        setBomberAttackType(2);
                        break;
                    }
                    break;
                }
                switch(World.Rnd().nextInt(0, 6))
                {
                case 0: // '\0'
                    setBomberAttackType(1);
                    break;

                case 1: // '\001'
                    setBomberAttackType(7);
                    break;

                case 2: // '\002'
                    setBomberAttackType(8);
                    break;

                default:
                    setBomberAttackType(2);
                    break;
                }
                break;

            case 3: // '\003'
                if(f > 0.09F)
                {
                    switch(World.Rnd().nextInt(0, 4))
                    {
                    case 0: // '\0'
                        setBomberAttackType(7);
                        break;

                    case 1: // '\001'
                        setBomberAttackType(1);
                        break;

                    case 2: // '\002'
                        setBomberAttackType(2);
                        break;

                    default:
                        setBomberAttackType(10);
                        break;
                    }
                    break;
                }
                if(f > 0.05F)
                {
                    switch(World.Rnd().nextInt(0, 5))
                    {
                    case 0: // '\0'
                        setBomberAttackType(1);
                        break;

                    case 1: // '\001'
                        setBomberAttackType(2);
                        break;

                    case 2: // '\002'
                        setBomberAttackType(4);
                        break;

                    case 3: // '\003'
                        setBomberAttackType(7);
                        break;

                    default:
                        setBomberAttackType(8);
                        break;
                    }
                    break;
                }
                switch(World.Rnd().nextInt(0, 4))
                {
                case 0: // '\0'
                    setBomberAttackType(1);
                    break;

                case 1: // '\001'
                    setBomberAttackType(2);
                    break;

                case 2: // '\002'
                    setBomberAttackType(6);
                    break;

                default:
                    setBomberAttackType(7);
                    break;
                }
                break;
            }
        set_maneuver(63);
    }

    public void attackStormoviks()
    {
        switch(Skill)
        {
        default:
            break;

        case 0: // '\0'
            switch(World.Rnd().nextInt(0, 5))
            {
            case 0: // '\0'
                setBomberAttackType(8);
                break;

            case 1: // '\001'
                setBomberAttackType(9);
                break;

            default:
                setBomberAttackType(5);
                break;
            }
            break;

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
            if(target.crew > 1)
            {
                switch(World.Rnd().nextInt(0, 6))
                {
                case 0: // '\0'
                    setBomberAttackType(9);
                    break;

                case 1: // '\001'
                case 2: // '\002'
                    setBomberAttackType(0);
                    break;

                default:
                    setBomberAttackType(4);
                    break;
                }
                break;
            }
            switch(World.Rnd().nextInt(0, 3))
            {
            case 0: // '\0'
                setBomberAttackType(9);
                break;

            default:
                setBomberAttackType(8);
                break;
            }
            break;
        }
        set_maneuver(63);
    }

    private void assignManeuverToWingmen(int i)
    {
        for(Pilot pilot = this; pilot.Wingman != null;)
        {
            pilot = (Pilot)pilot.Wingman;
            pilot.set_maneuver(i);
        }

    }

    private void assignTaskToWingmen(int i)
    {
        for(Pilot pilot = this; pilot.Wingman != null;)
        {
            pilot = (Pilot)pilot.Wingman;
            pilot.set_task(i);
        }

    }

    public boolean isLeader()
    {
        if(actor instanceof TypeFighter)
            return ((Aircraft)actor).aircIndex() % 2 == 0;
        else
            return Leader == null;
    }

    public boolean isLonely(float f, boolean flag)
    {
        if(flag)
        {
            if(Leader == null && Wingman == null)
                return true;
            double d = 0.0D;
            if(Leader != null)
                d = Leader.Loc.distance(Loc);
            if(Wingman != null)
                d = Math.min(Wingman.Loc.distance(Loc), d);
            return d > (double)f;
        }
        Actor actor = NearestTargets.getEnemy(9, -1, Loc, f, 0);
        if(Actor.isValid(actor))
            return actor.pos.getAbsPoint().distance(Loc) > (double)f;
        else
            return true;
    }

    public static final int GTARGET_ALL = 0;
    public static final int GTARGET_TANKS = 1;
    public static final int GTARGET_FLAK = 2;
    public static final int GTARGET_VEHICLES = 3;
    public static final int GTARGET_TRAIN = 4;
    public static final int GTARGET_BRIDGE = 5;
    public static final int GTARGET_SHIPS = 6;
    public static final int ATARGET_FIGHTERS = 7;
    public static final int ATARGET_BOMBERS = 8;
    public static final int ATARGET_ALL = 9;
    public static final int TARGET_FIGHTERS = 7;
    public static final int TARGET_BOMBERS = 8;
    public static final int TARGET_ALL = 9;
    private static final Vector3d MainLook = new Vector3d(0.34202013999999997D, 0.0D, 0.93969259999999999D);
    private static Vector3d VDanger = new Vector3d();
    private static Vector3d OnMe = new Vector3d();
    private static Vector3d diffV = new Vector3d();
    private static double diffVLength = 0.0D;
    private static Vector3f tmpV = new Vector3f();
    private static Point3d p1 = new Point3d();
    private static Point3d p2 = new Point3d();
    private static Point3f p1f = new Point3f();
    private static Point3f p2f = new Point3f();
    private int airTargetType;
    private int groundTargetType;
    private long dumbOffTime;
    private int oldTask;
    private FlightModel oldTaskObject;
    private Actor oldGTarget;
    private boolean continueManeuver;
    private boolean Visible;
    private boolean Near;
    private boolean OnBack;
    private boolean Looks;
    private boolean Higher;
    private boolean Faster;
    private boolean Energed;
    private float dist;
    private float dE;
    private Actor act;
    private Actor actg;

}