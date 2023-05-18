// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   AutopilotAI.java

package com.maddox.il2.ai.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.sounds.Voice;

// Referenced classes of package com.maddox.il2.ai.air:
//            Pilot, AirGroup

public class AutopilotAI extends Autopilotage
{

    public AutopilotAI(FlightModel flightmodel)
    {
        //
    	overrideMissileControl = false;
        theMissileControls = null;
        oldEvdecrease = 0.0F;
        newEv = 0.0F;
        bLogDetail = false;
    	//
    	WPoint = new Point3d();
        courseV = new Vector3d();
        windV = new Vector3d();
        Ve = new Vector3d();
        FM = (Pilot)flightmodel;
    }

    public boolean getWayPoint()
    {
        return bWayPoint;
    }

    public boolean getStabAltitude()
    {
        return bStabAltitude;
    }

    public boolean getStabSpeed()
    {
        return bStabSpeed;
    }

    public boolean getStabDirection()
    {
        return bStabDirection;
    }

    public void setWayPoint(boolean flag)
    {
        bWayPoint = flag;
        if(!flag)
            return;
        bStabSpeed = false;
        bStabAltitude = false;
        bStabDirection = false;
        if(WWPoint != null)
        {
            WWPoint.getP(WPoint);
            StabSpeed = WWPoint.Speed;
            StabAltitude = WPoint.z;
        } else
        {
            StabAltitude = 1000D;
            StabSpeed = 80D;
        }
        StabDirection = O.getAzimut();
    }

    public void setStabAltitude(boolean flag)
    {
        bStabAltitude = flag;
        if(!flag)
            return;
        bWayPoint = false;
        FM.getLoc(P);
        StabAltitude = P.z;
        if(!bStabSpeed)
            StabSpeed = FM.getSpeed();
        Pw = FM.CT.PowerControl;
    }

    public void setStabAltitude(float f)
    {
        bStabAltitude = true;
        bWayPoint = false;
        FM.getLoc(P);
        StabAltitude = f;
        if(!bStabSpeed)
            StabSpeed = FM.getSpeed();
        Pw = FM.CT.PowerControl;
    }

    public void setStabSpeed(boolean flag)
    {
        bStabSpeed = flag;
        if(!flag)
        {
            return;
        } else
        {
            bWayPoint = false;
            StabSpeed = FM.getSpeed();
            return;
        }
    }

    public void setStabSpeed(float f)
    {
        bStabSpeed = true;
        bWayPoint = false;
        StabSpeed = f / 3.6F;
    }

    public void setStabDirection(boolean flag)
    {
        bStabDirection = flag;
        if(!flag)
        {
            return;
        } else
        {
            bWayPoint = false;
            O.set(FM.Or);
            StabDirection = O.getAzimut();
            Ail = FM.CT.AileronControl;
            return;
        }
    }

    public void setStabDirection(float f)
    {
        bStabDirection = true;
        bWayPoint = false;
        StabDirection = (f + 3600F) % 360F;
        Ail = FM.CT.AileronControl;
    }

    public void setStabAll(boolean flag)
    {
        bWayPoint = false;
        setStabDirection(flag);
        setStabAltitude(flag);
        setStabSpeed(flag);
        setStabDirection(flag);
    }

    public float getWayPointDistance()
    {
        if(WPoint == null)
        {
            return 1000000F;
        } else
        {
            way.curr().getP(P);
            P.sub(FM.Loc);
            return (float)Math.sqrt(P.x * P.x + P.y * P.y);
        }
    }

    private void voiceCommand(Point3d point3d, Point3d point3d1)
    {
        Ve.sub(point3d1, point3d);
        float f = 57.32484F * (float)Math.atan2(Ve.x, Ve.y);
        int i = (int)f;
        i = (i + 180) % 360;
        Voice.speakHeading((Aircraft)FM.actor, i);
        Voice.speakAltitude((Aircraft)FM.actor, (int)point3d.z);
    }

    public void update(float f)
    {
        if(overrideMissileControl)
            theMissileControls.WeaponControl[2] = true;
        FM.getLoc(PlLoc);
        boolean flag = false;
        SA = (float)Math.max(StabAltitude, Engine.land().HQ_Air(PlLoc.x, PlLoc.y) + 5D);
        if(((Maneuver) (FM)).Group != null && ((Maneuver) (FM)).Group.getAaaNum() > 3F && ((Aircraft)FM.actor).aircIndex() == 0 && FM.isTick(165, 0))
            avoidance = (float)(1 - World.Rnd().nextInt(0, 1) * 2) * World.Rnd().nextFloat(15F, 30F);
        if(bWayPoint)
        {
            boolean flag1 = false;
            if(((Aircraft)FM.actor instanceof TypeFastJet) && FM.getSpeed() > 155F)
                flag1 = way.isReachedFastJet(PlLoc);
            else
                flag1 = way.isReached(PlLoc);
            if(WWPoint != way.auto(PlLoc) || flag1)
            {
                WWPoint = !((Aircraft)FM.actor instanceof TypeFastJet) || FM.getSpeed() <= 155F ? way.auto(PlLoc) : way.autoFastJet(PlLoc);
                WWPoint.getP(WPoint);
                if(((Aircraft)FM.actor).aircIndex() == 0 && !way.isLanding())
                {
                    voiceCommand(WPoint, PlLoc);
                    FM.formationType = (byte)way.curr().formation;
                    if(((Maneuver) (FM)).Group != null)
                    {
                        ((Maneuver) (FM)).Group.setFormationAndScale(FM.formationType, 1.0F, true);
                        ((Maneuver) (FM)).Group.formationType = FM.formationType;
                    }
                }
                StabSpeed = WWPoint.Speed - 2.0F * (float)((Aircraft)FM.actor).aircIndex();
                StabAltitude = WPoint.z;
                if(WWPoint.Action == 3)
                {
                    Actor actor = WWPoint.getTarget();
                    if(actor != null)
                        FM.target_ground = null;
                    else
                    if(((Aircraft)FM.actor instanceof TypeBomber) && (FM.CT.Weapons[2] != null && FM.CT.Weapons[2][0] != null && FM.CT.Weapons[2][FM.CT.Weapons[2].length - 1].haveBullets() || FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets()))
                    {
                        FM.CT.BayDoorControl = 1.0F;
                        for(Pilot pilot1 = FM; pilot1.Wingman != null;)
                        {
                            pilot1 = (Pilot)pilot1.Wingman;
                            pilot1.CT.BayDoorControl = 1.0F;
                        }

                    }
                } else
                {
                    if(((Aircraft)FM.actor instanceof TypeBomber) || FM.CT.bHasBayDoorControl)
                    {
                        FM.CT.BayDoorControl = 0.0F;
                        for(Pilot pilot = FM; pilot.Wingman != null;)
                        {
                            pilot = (Pilot)pilot.Wingman;
                            pilot.CT.BayDoorControl = 0.0F;
                        }

                    }
                    Actor actor1 = WWPoint.getTarget();
                    if(actor1 instanceof Aircraft)
                        if(actor1.getArmy() == FM.actor.getArmy())
                            FM.airClient = ((Aircraft)actor1).FM;
                        else
                            FM.target = ((Aircraft)actor1).FM;
                }
                if(way.isLanding())
                {
                    FM.getLoc(P);
                    if(way.Cur() > 3 && P.z > WPoint.z + (((Aircraft)FM.actor instanceof TypeFastJet) ? 600D : 500D))
                    {
                        if(!FM.CT.bHasFlapsControlSwitch)
                            FM.CT.FlapsControl = 0.0F;
                        way.setCur(1);
                    }
                    if(way.Cur() > 3 && FM.getSpeed() > way.curr().getV() * 1.8F && ((Aircraft)FM.actor instanceof TypeFastJet))
                    {
                        if(!FM.CT.bHasFlapsControlSwitch)
                            FM.CT.FlapsControl = 0.0F;
                        way.setCur(1);
                    }
                    if(way.Cur() == 4 && ((Aircraft)FM.actor instanceof TypeFastJet) && !FM.CT.bHasFlapsControlSwitch)
                        FM.CT.FlapsControl = 0.4F;
                    if(way.Cur() == 5 && (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter))
                        Voice.speakLanding((Aircraft)FM.actor);
                    if(way.Cur() == 6 || way.Cur() == 7)
                    {
                        int i = 0;
                        if(Actor.isAlive(way.landingAirport))
                            i = way.landingAirport.landingFeedback(WPoint, (Aircraft)FM.actor);
                        if(i == 0 && (!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter))
                            Voice.speakLandingPermited((Aircraft)FM.actor);
                        if(i == 1)
                        {
                            if(!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
                                Voice.speakLandingDenied((Aircraft)FM.actor);
                            way.first();
                            FM.push(2);
                            FM.push(2);
                            FM.push(2);
                            FM.push(2);
                            FM.pop();
                            if(!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
                                Voice.speakGoAround((Aircraft)FM.actor);
                            if(!FM.CT.bHasFlapsControlSwitch)
                                FM.CT.FlapsControl = 0.4F;
                            FM.CT.GearControl = 0.0F;
                            return;
                        }
                        if(i == 2)
                        {
                            if(!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
                                Voice.speakWaveOff((Aircraft)FM.actor);
                            if(FM.isReadyToReturn())
                            {
                                if(!Mission.isDogfight() || !Main.cur().mission.zutiMisc_DisableAIRadioChatter)
                                    Voice.speakGoingIn((Aircraft)FM.actor);
                                FM.AS.setCockpitDoor(FM.actor, 1);
                                FM.CT.GearControl = 1.0F;
                                return;
                            }
                            way.first();
                            FM.push(2);
                            FM.push(2);
                            FM.push(2);
                            FM.push(2);
                            FM.pop();
                            if(!FM.CT.bHasFlapsControlSwitch)
                                FM.CT.FlapsControl = 0.4F;
                            FM.CT.GearControl = 0.0F;
                            Aircraft.debugprintln(FM.actor, "Going around!.");
                            return;
                        }
                        FM.CT.GearControl = 1.0F;
                    }
                }
            }
            if(way.isLanding() && way.Cur() < 6 && way.getCurDist() < 800D)
                way.next();
            if((way.Cur() == way.size() - 1 && getWayPointDistance() < 2000F && way.curr().getTarget() == null && FM.M.fuel < 0.2F * FM.M.maxFuel || way.curr().Action == 2) && !way.isLanding())
            {
                Airport airport = Airport.makeLandWay(FM);
                if(airport != null)
                {
                    WWPoint = null;
                    way.first();
                    update(f);
                    return;
                }
                FM.set_task(3);
                FM.set_maneuver(49);
                FM.setBusy(true);
            }
            boolean flag2 = false;
            if(World.cur().diffCur.Wind_N_Turbulence)
            {
                World.cur();
                if(!World.wind().noWind && FM.Skill > 0)
                {
                    World.cur();
                    World.wind().getVectorAI(WPoint, windV);
                    windV.scale(-1D);
                    if(FM.Skill == 1)
                        windV.scale(0.75D);
                    courseV.set(WPoint.x - PlLoc.x, WPoint.y - PlLoc.y, 0.0D);
                    courseV.normalize();
                    courseV.scale(FM.getSpeed());
                    courseV.add(windV);
                    StabDirection = -FMMath.RAD2DEG((float)Math.atan2(courseV.y, courseV.x));
                    flag2 = true;
                }
            }
            if(!flag2)
                StabDirection = -FMMath.RAD2DEG((float)Math.atan2(WPoint.y - PlLoc.y, WPoint.x - PlLoc.x));
        }
        if(bStabSpeed || bWayPoint)
        {
            Pw = 0.3F - 0.04F * (FM.getSpeed() - (float)StabSpeed);
            if(Pw > 1.0F)
                Pw = 1.0F;
            else
            if(Pw < 0.0F)
                Pw = 0.0F;
        }
        if(bStabAltitude || bWayPoint)
        {
            Ev = FM.CT.ElevatorControl;
            double d = SA - FM.getAltitude();
            double d2 = 0.0D;
            double d3 = 0.0D;
            if(d > -50D)
            {
                float f5 = 5F + 0.00025F * FM.getAltitude();
                f5 = (float)((double)f5 + 0.02D * (250D - (FM.Vmax <= 300F ? FM.Vmax : 300D)));
                if(f5 > 14F)
                    f5 = 14F;
                d2 = Math.min(FM.getAOA() - f5, FM.Or.getTangage() - 1.0F) * 1.0F * f + 0.5F * FM.getForwAccel();
            }
            if(d < 50D)
            {
                float f6 = -15F + FM.M.mass * 0.00033F;
                if(f6 < -4F)
                    f6 = -4F;
                d3 = (FM.Or.getTangage() - f6) * 0.8F * f;
            }
            if(d <= -50D || d >= 50D || !((Aircraft)FM.actor instanceof TypeFastJet))
                flag = true;
            double d4 = 0.01D * (d + 50D);
            if(d4 > 1.0D)
                d4 = 1.0D;
            if(d4 < 0.0D)
                d4 = 0.0D;
            if(flag)
            {
                Ev -= d4 * d2 + (1.0D - d4) * d3;
                Ev += 1.0D * FM.getW().y + 0.5D * FM.getAW().y;
                if(FM.getSpeed() < 1.3F * FM.VminFLAPS)
                    Ev -= 0.004F * f;
            }
            if(d > -50D && FM.getSpeed() > 30F && ((Aircraft)FM.actor instanceof TypeFastJet))
            {
                if(FM.getVertSpeed() < -0F)
                {
                    if((getWayPointDistance() / FM.getSpeed()) * FM.getVertSpeed() < (float)d - Math.min(SA * 0.05F, 50F))
                    {
                        newEv = ((float)d / (getWayPointDistance() / FM.getSpeed()) - FM.getVertSpeed()) * 0.05F;
                        if(newEv > 0.6F)
                            newEv = 0.6F;
                        if(newEv < 0.0F)
                            newEv = 0.0F;
                    } else
                    {
                        if(newEv < 0.0F)
                            newEv += 0.002F;
                        if(newEv > 0.0F)
                            newEv = 0.0F;
                    }
                } else
                if(FM.getVertSpeed() > 4F)
                    newEv *= 0.98F;
                else
                if(FM.getVertSpeed() > 2.0F)
                    newEv *= 0.996F;
                Ev = newEv;
            }
            float f7 = (9F * FM.getSpeed()) / FM.VminFLAPS;
            if(FM.VminFLAPS < 28F)
                f7 = 10F;
            if(f7 > 25F)
                f7 = 25F;
            float f12 = (f7 - FM.Or.getTangage()) * 0.1F;
            float f13 = -15F + FM.M.mass * 0.00033F;
            if(f13 < -4F)
                f13 = -4F;
            float f14 = (f13 - FM.Or.getTangage()) * 0.2F;
            if(FM.getVertSpeed() < 0.0F && FM.Alt / -FM.getVertSpeed() < 40F && FM.getAOA() < 15F && (!way.isLanding() || way.Cur() <= 2))
                f14 = 0.5F;
            if(Ev > f12)
                Ev = f12;
            if(Ev < f14)
                Ev = f14;
            FM.CT.ElevatorControl = 0.8F * FM.CT.ElevatorControl + 0.2F * Ev;
        }
        float f1 = 0.0F;
        if(bStabDirection || bWayPoint)
        {
            double d1 = SA - FM.getAltitude();
            f1 = FM.Or.getAzimut();
            float f2 = FM.Or.getKren();
            if(((Maneuver) (FM)).Group.getAaaNum() > 3F && ((Aircraft)FM.actor).aircIndex() == 0)
                f1 = (float)((double)f1 - (StabDirection + (double)avoidance));
            else
                f1 = (float)((double)f1 - StabDirection);
            f1 = (f1 + 3600F) % 360F;
            f2 = (f2 + 3600F) % 360F;
            if(f1 > 180F)
                f1 -= 360F;
            if(f2 > 180F)
                f2 -= 360F;
            float f3 = ((FM.getSpeed() - FM.VminFLAPS) * 3.6F + FM.getVertSpeed() * 40F) * 0.25F;
            if(way.isLanding())
                f3 = 65F;
            if(f3 < 15F)
                f3 = 15F;
            else
            if(f3 > 65F)
                f3 = 65F;
            if(f1 < -f3)
                f1 = -f3;
            else
            if(f1 > f3)
                f1 = f3;
            Ail = -0.01F * (f1 + f2 + 3F * (float)FM.getW().x + 0.5F * (float)FM.getAW().x);
            if(Ail > 1.0F)
                Ail = 1.0F;
            else
            if(Ail < -1F)
                Ail = -1F;
            WPoint.get(Ve);
            Ve.sub(FM.Loc);
            FM.Or.transformInv(Ve);
            float f4 = 24F + (FM.getSpeed() / FM.Vmin - 1.2F) + ((float)FM.Loc.z - (float)Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) - 100F) / 10F + (float)FM.Vwld.z * 1.6F;
            boolean flag3 = false;
            if(f4 > 60F)
            {
                f4 = 60F;
                flag3 = true;
            }
            boolean flag4 = false;
            if((double)Math.abs(FM.Or.getRoll() - 360F) > (double)f4 * 1.3D && !flag3)
            {
                FM.CT.AileronControl = FM.Or.getRoll() - 360F >= 0.0F ? 0.5F : -0.5F;
                flag4 = true;
            } else
            if(Math.abs(FM.Or.getRoll() - 360F) > f4 && !flag3)
            {
                if(FM.CT.AileronControl * Ail > 0.0F)
                    FM.CT.AileronControl = 0.0F;
                else
                    FM.CT.AileronControl = Ail;
                flag4 = true;
            } else
            if((double)Math.abs(FM.Or.getRoll() - 360F) > (double)f4 * 0.90000000000000002D && !flag3)
            {
                float f8;
                if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
                    f8 = -0.01F * FM.Or.getKren();
                else
                    f8 = Ail;
                if(FM.CT.AileronControl > 0.0F && f8 > 0.0F)
                {
                    if(f8 > FM.CT.AileronControl * 0.33F)
                    {
                        FM.CT.AileronControl = f8 * 0.33F;
                        flag4 = true;
                    } else
                    {
                        FM.CT.AileronControl = f8;
                    }
                } else
                if(FM.CT.AileronControl > 0.0F && f8 < 0.0F)
                    FM.CT.AileronControl = f8;
                else
                if(FM.CT.AileronControl < 0.0F && f8 < 0.0F)
                {
                    if(f8 < FM.CT.AileronControl * 0.33F)
                    {
                        FM.CT.AileronControl = f8 * 0.33F;
                        flag4 = true;
                    } else
                    {
                        FM.CT.AileronControl = f8;
                    }
                } else
                {
                    FM.CT.AileronControl = f8;
                }
                if(flag4 = false)
                    flag4 = true;
            } else
            if((double)Math.abs(FM.Or.getRoll() - 360F) > (double)f4 * 0.80000000000000004D && !flag3)
            {
                float f9;
                if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
                    f9 = -0.01F * FM.Or.getKren();
                else
                    f9 = Ail;
                if(FM.CT.AileronControl > 0.0F && f9 > 0.0F)
                {
                    if(f9 > FM.CT.AileronControl * 0.5F)
                    {
                        FM.CT.AileronControl = f9 * 0.5F;
                        flag4 = true;
                    } else
                    {
                        FM.CT.AileronControl = f9;
                    }
                } else
                if(FM.CT.AileronControl > 0.0F && f9 < 0.0F)
                    FM.CT.AileronControl = f9;
                else
                if(FM.CT.AileronControl < 0.0F && f9 < 0.0F)
                {
                    if(f9 < FM.CT.AileronControl * 0.5F)
                    {
                        FM.CT.AileronControl = f9 * 0.5F;
                        flag4 = true;
                    } else
                    {
                        FM.CT.AileronControl = f9;
                    }
                } else
                {
                    FM.CT.AileronControl = f9;
                }
                if(flag4 = false)
                    flag4 = true;
            } else
            if((double)Math.abs(FM.Or.getRoll() - 360F) > (double)f4 * 0.69999999999999996D && !flag3)
            {
                float f10;
                if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
                    f10 = -0.01F * FM.Or.getKren();
                else
                    f10 = Ail;
                if(FM.CT.AileronControl > 0.0F && f10 > 0.0F)
                {
                    if(f10 > FM.CT.AileronControl * 0.75F)
                    {
                        FM.CT.AileronControl = f10 * 0.75F;
                        flag4 = true;
                    } else
                    {
                        FM.CT.AileronControl = f10;
                    }
                } else
                if(FM.CT.AileronControl > 0.0F && f10 < 0.0F)
                    FM.CT.AileronControl = f10;
                if(FM.CT.AileronControl < 0.0F && f10 < 0.0F)
                {
                    if(f10 < FM.CT.AileronControl * 0.75F)
                    {
                        FM.CT.AileronControl = f10 * 0.75F;
                        flag4 = true;
                    } else
                    {
                        FM.CT.AileronControl = f10;
                    }
                } else
                {
                    FM.CT.AileronControl = f10;
                }
                if(flag4 = false)
                    flag4 = true;
            } else
            if(Math.abs(Ve.y) < 25D && Math.abs(Ve.x) < 150D)
                FM.CT.AileronControl = -0.01F * FM.Or.getKren();
            else
                FM.CT.AileronControl = Ail;
            float f11 = FM.CT.ElevatorControl + (Math.abs(f2) * 0.004F * f + (flag4 ? 0.08F : 0.0F));
            if(f11 > 1.0F)
                f11 = 1.0F;
            if(f11 < -1F)
                f11 = -1F;
            FM.CT.ElevatorControl = f11;
            FM.CT.RudderControl -= FM.getAOS() * 0.04F * f;
        }
        if(bWayPoint && way.isLanding())
        {
            if(World.Rnd().nextFloat() < 0.01F)
                FM.doDumpBombsPassively();
            if(way.Cur() > 5)
                FM.set_maneuver(25);
            FM.CT.RudderControl -= f1 * 0.04F * f;
            landUpdate(f);
        }
    }

    private void landUpdate(float f)
    {
        if(((FM.getAltitude() - 10F) + FM.getVertSpeed() * 5F) - SA > 0.0F)
        {
            if(FM.Vwld.z > -10D)
                FM.Vwld.z -= 1.0F * f;
        } else
        if(FM.Vwld.z < 10D)
            FM.Vwld.z += 1.0F * f;
        if(FM.getAOA() > 11F && FM.CT.ElevatorControl > -0.3F)
            FM.CT.ElevatorControl -= 0.3F * f;
    }

    public void setOverrideMissileControl(Controls controls, boolean flag)
    {
        theMissileControls = controls;
        overrideMissileControl = flag;
    }
    
    public boolean bWayPoint;
    public boolean bStabAltitude;
    public boolean bStabSpeed;
    public boolean bStabDirection;
    protected double StabAltitude;
    protected double StabSpeed;
    protected double StabDirection;
    protected Pilot FM;
    protected WayPoint WWPoint;
    protected Point3d WPoint;
    private static Point3d P = new Point3d();
    private static Point3d PlLoc = new Point3d();
    private static Orientation O = new Orientation();
    protected Vector3d courseV;
    protected Vector3d windV;
    private float Ail;
    private float Pw;
    private float Ru;
    private float Ev;
    private float SA;
    private float avoidance;
    private Vector3d Ve;
    //
    private boolean overrideMissileControl;
    private Controls theMissileControls;
    private float oldEvdecrease;
    private float newEv;
    private boolean bLogDetail;
}
