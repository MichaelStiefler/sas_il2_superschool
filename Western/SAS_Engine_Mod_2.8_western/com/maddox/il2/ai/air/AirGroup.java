/*Modified AirGroup class for the SAS Engine Mod*/

/*By western on 26th/Apr./2018, add 8x Engines / Scheme8 decisions */
/*By western on 23rd/Jun./2018, add 10x Engines / Scheme10 decisions */

package com.maddox.il2.ai.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.Time;
import java.io.PrintStream;
import java.util.*;

// Referenced classes of package com.maddox.il2.ai.air:
//            Maneuver, AirGroupList, Pilot

public class AirGroup
{

    public native int[] groupLove(float f, float f1, float f2, float f3, float f4, float f5, float f6, 
            int i, int j, int k, int l);

    public void setUnderAAA(boolean flag, Aim aim)
    {
        if(aaaNum < 30F)
        {
            aaaShooters.add(aim);
            aaaNum = aaaShooters.size();
        }
    }

    public float getAaaNum()
    {
        return aaaNum;
    }

    boolean setCAP(int i, int j, int k, int l, float f, float f1, boolean flag)
    {
        Maneuver maneuver = (Maneuver)airc[k].FM;
        maneuver.AP.way.curr().getP(point3dWP);
        capAltitude = (int)maneuver.AP.way.curr().z();
        int i1 = maneuver.AP.way.Cur();
        int j1 = i + 3;
        float f2 = maneuver.AP.way.curr().baseSize;
        float f3 = (maneuver.AP.way.curr().orient * 3.141593F) / 180F;
        float f4 = maneuver.AP.way.curr().altDifference;
        if(i == 4)
        {
            if(!flag)
            {
                l = World.Rnd().nextInt(0, 4) + 3;
                f = World.Rnd().nextFloat(0.0F, 360F);
                f1 = (int)World.Rnd().nextFloat(f2 / 2.0F, f2);
            }
            j1 = l;
            f3 = f;
            f2 = (int)f1;
        }
        capFirstWP = i1 + 1;
        capLastWP = capFirstWP + j1;
        capCycles = maneuver.AP.way.curr().cycles;
        for(int k1 = j1; k1 >= 0; k1--)
        {
            w.curr().waypointType = 0;
            WayPoint waypoint = new WayPoint();
            switch(i)
            {
            case 1: // '\001'
                point3dNewWP.set(rotCoord(f3, xr[k1], yr[k1], true), rotCoord(f3, xr[k1], yr[k1], false), 0.0D);
                break;

            case 2: // '\002'
                point3dNewWP.set(rotCoord(f3, xp[k1], yp[k1], true), rotCoord(f3, xp[k1], yp[k1], false), 0.0D);
                break;

            case 3: // '\003'
                point3dNewWP.set(rotCoord(f3, xh[k1], yh[k1], true), rotCoord(f3, xh[k1], yh[k1], false), 0.0D);
                break;

            case 0: // '\0'
            default:
                point3dNewWP.set(rotCoord(f3, xtriangle[k1], ytriangle[k1], true), rotCoord(f3, xtriangle[k1], ytriangle[k1], false), 0.0D);
                break;
            }
            waypoint.set(maneuver.AP.way.curr().getV());
            waypoint.setRadioSilence(maneuver.AP.way.curr().isRadioSilence());
            point3dNewWP.scale(f2);
            point3dNewWP.add(point3dWP);
            pfcf.set(point3dNewWP);
            pfcf.add(0.0F, 0.0F, (f4 / (float)j1) * (float)k1);
            if(pfcf.z < 100F)
                pfcf.z = 100F;
            waypoint.set(pfcf);
            waypoint.waypointType = j;
            waypoint.targetTrigger = maneuver.AP.way.curr().targetTrigger;
            for(int i2 = 0; i2 < nOfAirc; i2++)
                airc[i2].netSendNewWayPoint(waypoint, i1);

            w.insert(i1 + 1, waypoint);
        }

        w.setCur(i1);
        rejoinToGroup(this);
        for(int l1 = 0; l1 < nOfAirc; l1++)
            airc[l1].FM.AP.way.curr().waypointType = 0;

        return true;
    }

    private static float rotCoord(float f, float f1, float f2, boolean flag)
    {
        float f3 = flag ? f1 * (float)Math.cos(f) - f2 * (float)Math.sin(f) : f1 * (float)Math.sin(f) + f2 * (float)Math.cos(f);
        return f3;
    }

    public String grTaskName()
    {
        return GTList[grTask];
    }

    public AirGroup()
    {
        Pos = new Vector3d();
        bSee = false;
        bInitAttack = true;
        outNum = 0;
        underAAA = -1;
        bracketSideGr = 1;
        aaaShooters = new HashSet();
        capFirstWP = 0;
        capLastWP = 0;
        capCycles = 0;
        capAltitude = 500;
        capTimerSet = false;
        convPoint = new HashMap();
        initVars();
    }

    public AirGroup(Squadron squadron, Way way)
    {
        Pos = new Vector3d();
        bSee = false;
        bInitAttack = true;
        outNum = 0;
        underAAA = -1;
        bracketSideGr = 1;
        aaaShooters = new HashSet();
        capFirstWP = 0;
        capLastWP = 0;
        capCycles = 0;
        capAltitude = 500;
        capTimerSet = false;
        convPoint = new HashMap();
        initVars();
        sq = squadron;
        w = way;
    }

    public AirGroup(AirGroup airgroup)
    {
        Pos = new Vector3d();
        bSee = false;
        bInitAttack = true;
        outNum = 0;
        underAAA = -1;
        bracketSideGr = 1;
        aaaShooters = new HashSet();
        capFirstWP = 0;
        capLastWP = 0;
        capCycles = 0;
        capAltitude = 500;
        capTimerSet = false;
        convPoint = new HashMap();
        initVars();
        if(airgroup == null)
            return;
        sq = airgroup.sq;
        if(airgroup.w != null)
        {
            w = new Way(airgroup.w);
            w.setCur(airgroup.w.Cur());
        } else
        {
            w = new Way();
            WayPoint waypoint = new WayPoint((float)airgroup.Pos.x, (float)airgroup.Pos.y, (float)airgroup.Pos.z);
            w.add(waypoint);
        }
        Pos.set(airgroup.Pos);
        int i = AirGroupList.length(airgroup.enemies[0]);
        for(int j = 0; j < i; j++)
            AirGroupList.addAirGroup(enemies, 0, AirGroupList.getGroup(airgroup.enemies[0], j));

        i = AirGroupList.length(airgroup.friends[0]);
        for(int k = 0; k < i; k++)
            AirGroupList.addAirGroup(friends, 0, AirGroupList.getGroup(airgroup.friends[0], k));

        rejoinGroup = airgroup;
        gTargetPreference = airgroup.gTargetPreference;
        aTargetPreference = airgroup.aTargetPreference;
        enemyFighters = airgroup.enemyFighters;
        oldEnemyNum = airgroup.oldEnemyNum;
        if(AirGroupList.groupInList(War.Groups[0], airgroup))
            AirGroupList.addAirGroup(War.Groups, 0, this);
        else
            AirGroupList.addAirGroup(War.Groups, 1, this);
    }

    void initVars()
    {
        nOfAirc = 0;
        airc = new Aircraft[16];
        sq = null;
        w = null;
        Pos = new Vector3d(0.0D, 0.0D, 0.0D);
        enemies = new AirGroupList[1];
        friends = new AirGroupList[1];
        clientGroup = null;
        targetGroup = null;
        leaderGroup = null;
        rejoinGroup = null;
        grAttached = 0;
        gTargetPreference = 0;
        aTargetPreference = 9;
        enemyFighters = false;
        gTargWasFound = false;
        gTargDestroyed = false;
        gTargMode = 0;
        gTargActor = null;
        gTargPoint = new Point3d();
        gTargRadius = 0.0F;
        aTargWasFound = false;
        aTargDestroyed = false;
        weWereInGAttack = false;
        weWereInAttack = false;
        formationType = -1;
        fInterpolation = false;
        oldFType = -1;
        oldFScale = 0.0F;
        oldFInterp = false;
        oldEnemyNum = 0;
        timeOutForTaskSwitch = 0;
        grTask = 1;
        bracketSideGr = World.Rnd().nextBoolean() ? -1 : 1;
    }

    public void release()
    {
        for(int i = 0; i < nOfAirc; i++)
        {
            if(airc[i] != null)
                ((Maneuver)airc[i].FM).Group = null;
            airc[i] = null;
        }

        nOfAirc = 0;
        sq = null;
        w = null;
        Pos = null;
        if(enemies[0] != null)
            enemies[0].release();
        if(friends[0] != null)
            friends[0].release();
        enemies = null;
        friends = null;
        clientGroup = null;
        targetGroup = null;
        leaderGroup = null;
        rejoinGroup = null;
        gTargPoint = null;
        convPoint.clear();
    }

    public void addAircraft(Aircraft aircraft)
    {
        if(nOfAirc >= 16)
        {
            System.out.print("Group > 16 in squadron " + sq.name());
            return;
        }
        int i;
        if(aircraft.getSquadron() == sq)
            for(i = 0; i < nOfAirc && airc[i].getSquadron() == sq && airc[i].getWing().indexInSquadron() * 4 + airc[i].aircIndex() <= aircraft.getWing().indexInSquadron() * 4 + aircraft.aircIndex(); i++);
        else
            i = nOfAirc;
        for(int j = nOfAirc - 1; j >= i; j--)
            airc[j + 1] = airc[j];

        airc[i] = aircraft;
        if(w != null)
        {
            aircraft.FM.AP.way = new Way(w);
            aircraft.FM.AP.way.setCur(w.Cur());
        }
        nOfAirc++;
        grSA += aircraft.FM.sight;
        if(grSA > 20)
            grSA = 20;
        grCourage += aircraft.FM.courage;
        if(grCourage > 20)
            grCourage = 20;
        grSubSkill += aircraft.FM.subSkill;
        if(grSubSkill > 60)
            grSubSkill = 60;
        grSkill += aircraft.FM.Skill;
        if(grSkill > 12)
            grSkill = 12;
        if(aircraft == World.getPlayerAircraft())
            aircraft.initPlayerTaxingWay();
        if(aircraft.FM instanceof Maneuver)
            ((Maneuver)aircraft.FM).Group = this;
    }

    public void delAircraft(Aircraft aircraft)
    {
        int i = 0;
        do
        {
            if(i >= nOfAirc)
                break;
            if(aircraft == airc[i])
            {
                ((Maneuver)airc[i].FM).Group = null;
                for(int j = i; j < nOfAirc - 1; j++)
                    airc[j] = airc[j + 1];

                nOfAirc--;
                grSA -= aircraft.FM.sight;
                grCourage -= aircraft.FM.courage;
                grSubSkill -= aircraft.FM.subSkill;
                grSkill -= aircraft.FM.Skill;
                break;
            }
            i++;
        } while(true);
        if(grTask == 1 || grTask == 2)
            setTaskAndManeuver(0);
    }

    public void changeAircraft(Aircraft aircraft, Aircraft aircraft1)
    {
        for(int i = 0; i < nOfAirc; i++)
            if(aircraft == airc[i])
            {
                ((Maneuver)aircraft.FM).Group = null;
                ((Maneuver)aircraft1.FM).Group = this;
                ((Maneuver)aircraft1.FM).setBusy(false);
                airc[i] = aircraft1;
                return;
            }

    }

    public void rejoinToGroup(AirGroup airgroup)
    {
        if(airgroup == null)
            return;
        for(int i = nOfAirc - 1; i >= 0; i--)
        {
            Aircraft aircraft = airc[i];
            delAircraft(aircraft);
            airgroup.addAircraft(aircraft);
        }

        rejoinGroup = null;
    }

    public void attachGroup(AirGroup airgroup)
    {
        if(airgroup == null)
            return;
        for(int i = 0; i < nOfAirc; i++)
        {
            if(!(airc[i].FM instanceof Maneuver))
                continue;
            Maneuver maneuver = (Maneuver)airc[i].FM;
            if((maneuver instanceof RealFlightModel) && ((RealFlightModel)maneuver).isRealMode())
                continue;
            if(maneuver.get_maneuver() == 26)
                return;
            if(maneuver.get_maneuver() == 64)
                return;
            if(maneuver.get_maneuver() == 102)
                return;
        }

        w = null;
        w = new Way(airgroup.w);
        w.setCur(airgroup.w.Cur());
        for(int j = 0; j < nOfAirc; j++)
        {
            airc[j].FM.AP.way = null;
            airc[j].FM.AP.way = new Way(airgroup.w);
            airc[j].FM.AP.way.setCur(airgroup.w.Cur());
        }

        Formation.leaderOffset(airc[0].FM, formationType, airc[0].FM.Offset);
        leaderGroup = airgroup;
        leaderGroup.grAttached++;
        grTask = 1;
        setFormationAndScale(airgroup.formationType, 1.0F, true);
    }

    void detachGroup(AirGroup airgroup)
    {
        if(airgroup == null)
            return;
        leaderGroup.grAttached--;
        if(leaderGroup.grAttached < 0)
            leaderGroup.grAttached = 0;
        leaderGroup = null;
        grTask = 1;
        setTaskAndManeuver(0);
    }

    public int numInGroup(Aircraft aircraft)
    {
        for(int i = 0; i < nOfAirc; i++)
            if(aircraft == airc[i])
                return i;

        return -1;
    }

    public void setEnemyFighters()
    {
        int i = AirGroupList.length(enemies[0]);
        outNum = 0;
        enemyFighters = false;
        for(int j = 0; j < i; j++)
        {
            AirGroup airgroup = AirGroupList.getGroup(enemies[0], j);
            if(airgroup.nOfAirc > 0 && (airgroup.airc[0] instanceof TypeFighter))
            {
                enemyFighters = true;
                outNum += airgroup.nOfAirc - nOfAirc;
                return;
            }
        }

    }

    public void setFormationAndScale(byte byte0, float f, boolean flag)
    {
        if(oldFType == byte0 && oldFScale == f && oldFInterp == flag)
            return;
        fInterpolation = flag;
        for(int i = 1; i < nOfAirc; i++)
        {
            if(airc[i] instanceof TypeGlider)
                return;
            ((Maneuver)airc[i].FM).formationScale = f;
            Formation.gather(airc[i].FM, byte0, tmpV);
            if(!flag)
                airc[i].FM.Offset.set(tmpV);
            formationType = ((Maneuver)airc[i].FM).formationType;
        }

        if(grTask == 1 || grTask == 2)
            setTaskAndManeuver(0);
        oldFType = byte0;
        oldFScale = f;
        oldFInterp = flag;
    }

    public void formationUpdate()
    {
        if(fInterpolation)
        {
            boolean flag = true;
            for(int i = 1; i < nOfAirc; i++)
            {
                if(!Actor.isAlive(airc[i]))
                    continue;
                if(airc[i] instanceof TypeGlider)
                    return;
                Formation.gather(airc[i].FM, formationType, tmpV);
                tmpV1.sub(tmpV, airc[i].FM.Offset);
                float f = (float)tmpV1.length();
                if(f == 0.0F)
                    continue;
                flag = false;
                if(f < 0.1F)
                {
                    airc[i].FM.Offset.set(tmpV);
                    continue;
                }
                double d = 0.00040000000000000002D * tmpV1.length();
                if(oldFScale > 0.0F)
                    d *= 10F / oldFScale;
                if(d > 1.5D)
                    d = 1.5D;
                tmpV1.normalize();
                tmpV1.scale(d);
                airc[i].FM.Offset.add(tmpV1);
            }

            if(flag)
                fInterpolation = false;
            if(grTask == 1 || grTask == 2)
                setTaskAndManeuver(0);
        }
    }

    public boolean inCorridor(Point3d point3d)
    {
        if(w == null)
            return true;
        float f = 25000F;
        if(w.curr().Action == 2)
            f = 3000F;
        int i = w.Cur();
        if(i == 0)
            return true;
        w.prev();
        tmpP = w.curr().getP();
        w.setCur(i);
        tmpV.sub(w.curr().getP(), tmpP);
        P1P2vector.set(tmpV);
        float f1 = (float)P1P2vector.length();
        if(f1 > 0.0001F)
            P1P2vector.scale(1.0F / f1);
        else
            P1P2vector.set(1.0D, 0.0D);
        tmpV.sub(point3d, tmpP);
        myPoint.set(tmpV);
        if(P1P2vector.dot(myPoint) < (double)(-f))
            return false;
        norm1.set(-P1P2vector.y, P1P2vector.x);
        float f2 = (float)norm1.dot(myPoint);
        if(f2 > f)
            return false;
        if(f2 < -f)
            return false;
        tmpV.sub(point3d, w.curr().getP());
        myPoint.set(tmpV);
        return P1P2vector.dot(myPoint) <= (double)f;
    }

    public void setGroupTask(int i)
    {
        grTask = i;
        bracketSideGr = World.Rnd().nextBoolean() ? -1 : 1;
        if(grTask == 1 || grTask == 2 || grTask == 8)
            setTaskAndManeuver(0);
        else
        if(grTask == 3 && bInitAttack)
        {
            setTaskAndManeuver(0);
            bInitAttack = false;
        } else
        {
            for(int j = 0; j < nOfAirc; j++)
                if(!((Maneuver)airc[j].FM).isBusy())
                    setTaskAndManeuver(j);

        }
    }

    public void dropBombs()
    {
        for(int i = 0; i < nOfAirc; i++)
            if(!((Maneuver)airc[i].FM).isBusy())
                ((Maneuver)airc[i].FM).bombsOut = true;

        if(friends[0] != null)
        {
            int j = AirGroupList.length(friends[0]);
            for(int k = 0; k < j; k++)
            {
                AirGroup airgroup = AirGroupList.getGroup(friends[0], k);
                if(airgroup != null && airgroup.leaderGroup == this)
                    airgroup.dropBombs();
            }

        }
    }

    Aircraft firstOkAirc(int i)
    {
        for(int j = 0; j < nOfAirc; j++)
        {
            if(i >= 0 && i < nOfAirc && (j == i || j == i + 1 && airc[j].aircIndex() == airc[i].aircIndex() + 1))
                continue;
            Maneuver maneuver = (Maneuver)airc[j].FM;
            if((maneuver.get_task() == 7 || maneuver.get_task() == 6 || maneuver.get_task() == 4) && maneuver.isOk())
                return airc[j];
        }

        return null;
    }

    public boolean waitGroup(int i)
    {
        Aircraft aircraft = firstOkAirc(i);
        Maneuver maneuver = (Maneuver)airc[i].FM;
        if(aircraft != null)
        {
            maneuver.airClient = aircraft.FM;
            maneuver.set_task(1);
            maneuver.clear_stack();
            maneuver.set_maneuver(59);
            return true;
        } else
        {
            maneuver.set_task(3);
            maneuver.clear_stack();
            maneuver.set_maneuver(21);
            return false;
        }
    }

    public void setGTargMode(int i)
    {
        gTargetPreference = i;
    }

    public void setGTargMode(Actor actor)
    {
        if(actor != null && Actor.isAlive(actor))
        {
            if((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric) || (actor instanceof Chief) || (actor instanceof Bridge))
            {
                gTargMode = 1;
                gTargActor = actor;
            } else
            {
                gTargMode = 2;
                gTargActor = actor;
                gTargPoint.set(actor.pos.getAbsPoint());
                gTargRadius = 200F;
                if(actor instanceof BigshipGeneric)
                {
                    gTargRadius = 20F;
                    setGTargMode(6);
                }
            }
        } else
        {
            gTargMode = 0;
        }
    }

    public void setGTargMode(Point3d point3d, float f)
    {
        gTargMode = 2;
        gTargPoint.set(point3d);
        gTargRadius = f;
    }

    public Actor setGAttackObject(int i)
    {
        if(i > nOfAirc - 1)
            return null;
        if(i < 0)
            return null;
        Actor actor = null;
        if(gTargMode == 1)
            actor = War.GetRandomFromChief(airc[i], gTargActor);
        else
        if(gTargMode == 2)
            actor = War.GetNearestEnemy(airc[i], gTargetPreference, gTargPoint, gTargRadius);
        if(actor != null)
        {
            gTargWasFound = true;
            gTargDestroyed = false;
        }
        if(actor == null && gTargWasFound)
        {
            gTargDestroyed = true;
            gTargWasFound = false;
        }
        return actor;
    }

    public void setATargMode(int i)
    {
        aTargetPreference = i;
    }

    public AirGroup chooseTargetGroup()
    {
        if(enemies == null)
            return null;
        int i = AirGroupList.length(enemies[0]);
        AirGroup airgroup = null;
        float f = 1E+012F;
        for(int j = 0; j < i; j++)
        {
            AirGroup airgroup1 = AirGroupList.getGroup(enemies[0], j);
            boolean flag = false;
            if(airgroup1 != null && airgroup1.nOfAirc > 0)
            {
                if(aTargetPreference == 9)
                    flag = true;
                else
                if(aTargetPreference == 7 && (airgroup1.airc[0] instanceof TypeFighter))
                    flag = true;
                else
                if(aTargetPreference == 8 && !(airgroup1.airc[0] instanceof TypeFighter))
                    flag = true;
                if(flag)
                {
                    int k = 0;
                    do
                    {
                        if(k >= airgroup1.nOfAirc)
                            break;
                        if(Actor.isAlive(airgroup1.airc[k]) && airgroup1.airc[k].FM.isCapableOfBMP() && !airgroup1.airc[k].FM.isTakenMortalDamage())
                        {
                            flag = true;
                            break;
                        }
                        k++;
                    } while(true);
                }
                if(!flag)
                    continue;
                tmpV.sub(Pos, airgroup1.Pos);
                if(tmpV.lengthSquared() < (double)f)
                {
                    airgroup = airgroup1;
                    f = (float)tmpV.lengthSquared();
                }
            } else
            {
                AirGroupList.delAirGroup(enemies, 0, airgroup1);
            }
        }

        return airgroup;
    }

    boolean somebodyAttacks()
    {
        boolean flag = false;
        int i = 0;
        do
        {
            if(i >= nOfAirc)
                break;
            Maneuver maneuver = (Maneuver)airc[i].FM;
            if((maneuver instanceof RealFlightModel) && ((RealFlightModel)maneuver).isRealMode() && airc[i].aircIndex() == 0)
            {
                flag = true;
                break;
            }
            if(!isWingman(i) && maneuver.isOk() && maneuver.hasCourseWeaponBullets())
            {
                flag = true;
                break;
            }
            i++;
        } while(true);
        return flag;
    }

    boolean somebodyGAttacks()
    {
        boolean flag = false;
        int i = 0;
        do
        {
            if(i >= nOfAirc)
                break;
            Maneuver maneuver = (Maneuver)airc[i].FM;
            if((maneuver instanceof RealFlightModel) && ((RealFlightModel)maneuver).isRealMode() && airc[i].aircIndex() == 0)
            {
                flag = true;
                break;
            }
            if(maneuver.isOk() && maneuver.get_task() != 1)
            {
                flag = true;
                break;
            }
            i++;
        } while(true);
        return flag;
    }

    void switchWayPoint()
    {
        Maneuver maneuver = (Maneuver)airc[0].FM;
        tmpV.sub(w.curr().getP(), maneuver.Loc);
        float f = (float)tmpV.lengthSquared();
        int i = w.Cur();
        w.next();
        tmpV.sub(w.curr().getP(), maneuver.Loc);
        float f1 = (float)tmpV.lengthSquared();
        w.setCur(i);
        if(f > f1)
        {
            String s = airc[0].FM.AP.way.curr().getTargetName();
            airc[0].FM.AP.way.next();
            w.next();
            if(airc[0].FM.AP.way.curr().Action == 0 && airc[0].FM.AP.way.curr().getTarget() == null)
                airc[0].FM.AP.way.curr().setTarget(s);
            if(w.curr().getTarget() == null)
                w.curr().setTarget(s);
        }
    }

    public boolean isWingman(int i)
    {
        if(i < 0)
            return false;
        Maneuver maneuver = (Maneuver)airc[i].FM;
        if((airc[i].aircIndex() & 1) != 0 && !maneuver.aggressiveWingman)
        {
            if(i > 0)
                maneuver.Leader = airc[i - 1].FM;
            else
                return false;
            if(maneuver.Leader != null && airc[i - 1].aircIndex() == airc[i].aircIndex() - 1 && enemyFighters && Actor.isAlive(airc[i - 1]) && maneuver.Leader.isOk())
                return true;
        }
        return false;
    }

    private static Aircraft chooseTarget(AirGroup airgroup)
    {
        Aircraft aircraft = null;
        if(airgroup != null && airgroup.nOfAirc > 0)
            aircraft = airgroup.airc[World.Rnd().nextInt(0, airgroup.nOfAirc - 1)];
        if(aircraft != null && (!Actor.isAlive(aircraft) || !aircraft.FM.isOk()) && airgroup != null)
        {
            for(int i = 0; i < airgroup.nOfAirc; i++)
                if(Actor.isAlive(airgroup.airc[i]) && aircraft.FM.isOk())
                    aircraft = airgroup.airc[i];

        }
        return aircraft;
    }

    public FlightModel setAAttackObject(int i)
    {
        if(i > nOfAirc - 1)
            return null;
        if(i < 0)
            return null;
        AirGroup airgroup = targetGroup;
        if(airgroup == null || airgroup.nOfAirc == 0)
            airgroup = chooseTargetGroup();
        Aircraft aircraft = chooseTarget(airgroup);
        if(aircraft != null)
        {
            aTargWasFound = true;
            aTargDestroyed = false;
        }
        if(aircraft == null && aTargWasFound)
        {
            aTargDestroyed = true;
            aTargWasFound = false;
        }
        return aircraft == null ? null : aircraft.FM;
    }

    public void setTaskAndManeuver(int n)
    {
        if(n > nOfAirc - 1)
            return;
        if(n < 0)
            return;
        Maneuver maneuver = (Maneuver)airc[n].FM;
Label_3414:
        switch (grTask) {
        case 1:
            FlightModel leader = null;
            tmpV.set(0.0D, 0.0D, 0.0D);
            for (int i = 0; i < nOfAirc; ++i) {
                Maneuver leader2 = (Maneuver)airc[i].FM;
                if (airc[i] instanceof TypeGlider) {
                    leader2.accurate_set_FOLLOW();
                }
                else {
                    tmpV.add(leader2.Offset);
                    if (!leader2.isBusy() || (leader2 instanceof RealFlightModel && ((RealFlightModel)leader2).isRealMode() && leader2.isOk())) {
                        leader2.Leader = null;
                        if (leaderGroup == null || leaderGroup.nOfAirc == 0)
                            leader2.accurate_set_task_maneuver(3, 21);
                        else if (((Maneuver)leaderGroup.airc[0].FM).isBusy())
                            leader2.accurate_set_task_maneuver(3, 21);
                        else {
                            leader2.accurate_set_FOLLOW();
                            leader2.followOffset.set(tmpV);
                            leader2.Leader = leaderGroup.airc[0].FM;
                        }
                        tmpV.set(0.0D, 0.0D, 0.0D);
                        for (int j = i + 1; j < nOfAirc; ++j) {
                            Maneuver maneuver2 = (Maneuver)airc[j].FM;
                            tmpV.add(maneuver2.Offset);
                            if (!maneuver2.isBusy()) {
                                maneuver2.accurate_set_FOLLOW();
                                if (airc[j] instanceof TypeGlider)
                                    continue;
                                if (leader == null) {
                                    maneuver2.followOffset.set(tmpV);
                                    maneuver2.Leader = leader2;
                                }
                                else {
                                    maneuver2.followOffset.set(maneuver2.Offset);
                                    maneuver2.Leader = leader;
                                }
                            }
                            if (maneuver2 instanceof RealFlightModel) {
                                if ((airc[j].aircIndex() & 0x1) == 0x0)
                                    leader = maneuver2;
                            }
                            else {
                                leader = null;
                            }
                        }
                        break;
                    }
                }
            }
            break;
        case 4:
            if (maneuver.isBusy())
                break;

            if (maneuver.target_ground == null || !Actor.isAlive(maneuver.target_ground) || maneuver.Loc.distance(maneuver.target_ground.pos.getAbsPoint()) > 4000.0D)
                maneuver.target_ground = setGAttackObject(n);
            if (maneuver.target_ground == null) {
                if (!waitGroup(n) && n == 0) {
                    if (maneuver.AP.way.curr().Action == 3)
                        maneuver.AP.way.next();
                    setGroupTask(1);
                    break;
                }
                break;
            }
            else {
                if (airc[n] instanceof TypeDockable) {
                    if (airc[n] instanceof I_16TYPE24DRONE)
                        ((I_16TYPE24DRONE)airc[n]).typeDockableAttemptDetach();
                    if (airc[n] instanceof MXY_7)
                        ((MXY_7)airc[n]).typeDockableAttemptDetach();
                    if (airc[n] instanceof G4M2E)
                        ((G4M2E)airc[n]).typeDockableAttemptDetach();
                    if (airc[n] instanceof TB_3_4M_34R_SPB)
                        ((TB_3_4M_34R_SPB)airc[n]).typeDockableAttemptDetach();
                }
                if ((maneuver.AP.way.Cur() == maneuver.AP.way.size() - 1 && maneuver.AP.way.curr().Action == 3) || airc[n] instanceof MXY_7) {
                    maneuver.kamikaze = true;
                    maneuver.set_task(7);
                    maneuver.clear_stack();
                    maneuver.set_maneuver(46);
                    break;
                }
                boolean b = true;
                if (maneuver.hasRockets())
                    b = false;
                if (maneuver.CT.Weapons[0] != null && maneuver.CT.Weapons[0][0] != null && maneuver.CT.Weapons[0][0].bulletMassa() > 0.05f && maneuver.CT.Weapons[0][0].countBullets() > 0)
                    b = false;
                if ((b && maneuver.CT.getWeaponMass() < 7.0F) || maneuver.CT.getWeaponMass() < 1.0F || maneuver.bSkipGroundAttack) {
                    Voice.speakEndOfAmmo(airc[n]);
                    if (!waitGroup(n) && n == 0) {
                        if (maneuver.AP.way.curr().Action == 3)
                            maneuver.AP.way.next();
                        setGroupTask(1);
                        break;
                    }
                    break;
                }
                else {
                    if (maneuver.target_ground instanceof Prey && (((Prey)maneuver.target_ground).HitbyMask() & 0x1) == 0x0) {
                        float bulletMassa = 0.0F;
                        for (int k = 0; k < 4; ++k) {
                            if (maneuver.CT.Weapons[k] != null) {
                                for (int l = 0; l < maneuver.CT.Weapons[k].length; ++l) {
                                    if (maneuver.CT.Weapons[k][l] != null && maneuver.CT.Weapons[k][l].countBullets() != 0 && maneuver.CT.Weapons[k][l].bulletMassa() > bulletMassa)
                                        bulletMassa = maneuver.CT.Weapons[k][l].bulletMassa();
                                }
                            }
                        }
                        if (bulletMassa < 0.08F || (maneuver.target_ground instanceof TgtShip && bulletMassa < 0.55F)) {
                            maneuver.AP.way.next();
                            maneuver.set_task(1);
                            maneuver.clear_stack();
                            maneuver.set_maneuver(21);
                            maneuver.target_ground = null;
                            break;
                        }
                    }
                    if (maneuver.CT.Weapons[3] != null) {
                        for (int n2 = 0; n2 < maneuver.CT.Weapons[3].length; ++n2) {
                            if (maneuver.CT.Weapons[3][n2] != null && !(maneuver.CT.Weapons[3][n2] instanceof BombGunNull) && maneuver.CT.Weapons[3][n2].countBullets() != 0) {
                                if (maneuver.CT.Weapons[3][n2] instanceof ParaTorpedoGun) {
                                    maneuver.set_task(7);
                                    maneuver.clear_stack();
                                    maneuver.set_maneuver(43);
                                    return;
                                }
                                if (maneuver.CT.Weapons[3][n2] instanceof TorpedoGun) {
                                    if (!(maneuver.target_ground instanceof TgtShip)) {
                                        maneuver.set_task(7);
                                        maneuver.clear_stack();
                                        maneuver.set_maneuver(43);
                                        return;
                                    }
                                    maneuver.set_task(7);
                                    maneuver.clear_stack();
                                    if (!(maneuver.target_ground instanceof BigshipGeneric)) {
                                        maneuver.set_maneuver(51);
                                        return;
                                    }
                                    BigshipGeneric bigshipGeneric = (BigshipGeneric)maneuver.target_ground;
                                    if (airc[n] instanceof TypeHasToKG && !bigshipGeneric.zutiIsStatic() && airc[n].FM.Skill >= 2 && bigshipGeneric.collisionR() > 45.0F) {
                                        maneuver.set_maneuver(73);
                                        return;
                                    }
                                    maneuver.set_maneuver(51);
                                    return;
                                }
                                else {
                                    if (airc[n] instanceof TypeGuidedBombCarrier && maneuver.CT.Weapons[3][n2] instanceof RocketGunHS_293) {
                                        maneuver.set_task(7);
                                        maneuver.clear_stack();
                                        maneuver.set_maneuver(71);
                                        return;
                                    }
                                    if (airc[n] instanceof TypeGuidedBombCarrier && maneuver.CT.Weapons[3][n2] instanceof RocketGunFritzX) {
                                        maneuver.set_task(7);
                                        maneuver.clear_stack();
                                        maneuver.set_maneuver(72);
                                        return;
                                    }
                                    if (maneuver.CT.Weapons[3][n2] instanceof BombGunPara) {
                                        w.curr().setTarget(null);
                                        maneuver.target_ground = null;
                                        grTask = 1;
                                        setTaskAndManeuver(n);
                                        return;
                                    }
                                    if (maneuver.CT.Weapons[3][n2].bulletMassa() < 10.0f && !(maneuver.CT.Weapons[3][n2] instanceof BombGunParafrag8)) {
                                        maneuver.set_task(7);
                                        maneuver.clear_stack();
                                        maneuver.set_maneuver(52);
                                        return;
                                    }
                                    if (airc[n] instanceof TypeDiveBomber && maneuver.Alt > 1200.0F) {
                                        maneuver.set_task(7);
                                        maneuver.clear_stack();
                                        maneuver.set_maneuver(50);
                                        return;
                                    }
                                    if (n2 == maneuver.CT.Weapons[3].length - 1) {
                                        maneuver.set_task(7);
                                        maneuver.clear_stack();
                                        maneuver.set_maneuver(43);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    if (maneuver.target_ground instanceof BridgeSegment && !maneuver.hasRockets()) {
                        maneuver.set_task(1);
                        maneuver.clear_stack();
                        maneuver.set_maneuver(59);
                        maneuver.target_ground = null;
                        break;
                    }
                    if (airc[n] instanceof F4U && maneuver.CT.Weapons[2] != null && maneuver.CT.Weapons[2][0].bulletMassa() > 100.0F && maneuver.CT.Weapons[2][0].countBullets() > 0) {
                        maneuver.set_task(7);
                        maneuver.clear_stack();
                        maneuver.set_maneuver(47);
                        break;
                    }
                    if (airc[n] instanceof R_5xyz) {
                        if (((R_5xyz)airc[n]).strafeWithGuns) {
                            maneuver.set_task(7);
                            maneuver.clear_stack();
                            maneuver.set_maneuver(43);
                            break;
                        }
                        w.curr().setTarget(null);
                        maneuver.target_ground = null;
                        grTask = 1;
                        setTaskAndManeuver(n);
                        grTask = 4;
                        break;
                    }
                    else {
                        if (airc[n] instanceof TBD) {
                            w.curr().setTarget(null);
                            maneuver.target_ground = null;
                            grTask = 1;
                            setTaskAndManeuver(n);
                            grTask = 4;
                            break;
                        }
                        if (airc[n] instanceof TypeFighter || airc[n] instanceof TypeStormovik) {
                            maneuver.set_task(7);
                            maneuver.clear_stack();
                            maneuver.set_maneuver(43);
                            break;
                        }
                        w.curr().setTarget(null);
                        maneuver.target_ground = null;
                        grTask = 1;
                        setTaskAndManeuver(n);
                        grTask = 4;
                        break;
                    }
                }
            }
//            break;  // never come here
        case 3:
            if (maneuver.isBusy())
                break;

            if ((!(maneuver instanceof RealFlightModel) || !((RealFlightModel)maneuver).isRealMode()) && !maneuver.bKeepOrdnance) {
                maneuver.bombsOut = true;
                maneuver.CT.dropFuelTanks();
            }
            if (isWingman(n) && !bInitAttack) {
                maneuver.airClient = maneuver.Leader;
                maneuver.set_task(5);
                maneuver.clear_stack();
                switch (maneuver.Skill) {
                case 0:
                    ((Pilot)maneuver).wingmanAttacks(1);
                    break Label_3414;
                case 1:
                    ((Pilot)maneuver).wingmanAttacks(1);
                    break Label_3414;
                case 2:
                    ((Pilot)maneuver).wingmanAttacks(1);
                    break Label_3414;
                case 3:
                    ((Pilot)maneuver).wingmanAttacks(1);
                    break Label_3414;
                default:
                    maneuver.followOffset.set(200.0D, 0.0D, 20.0D);
                    ((Pilot)maneuver).wingmanAttacks(1);
                    break Label_3414;
                }
            }
            else {
                maneuver.airClient = null;
                int n3 = 1;
                if (inCorridor(maneuver.Loc))
                    n3 = 0;
                if (n3 != 0) {
                    int cur = w.Cur();
                    w.next();
                    if (inCorridor(maneuver.Loc))
                        n3 = 0;
                    w.setCur(cur);
                    if (n3 != 0) {
                        int cur2 = w.Cur();
                        w.prev();
                        if (inCorridor(maneuver.Loc))
                            n3 = 0;
                        w.setCur(cur2);
                    }
                }
                if (n3 != 0) {
                    maneuver.set_task(3);
                    maneuver.clear_stack();
                    maneuver.set_maneuver(21);
                    break;
                }
                if (maneuver.target == null || !maneuver.target.isOk() || maneuver.Loc.distance(maneuver.target.Loc) > 4000.0D)
                    maneuver.target = setAAttackObject(n);
                if (maneuver.target == null || !maneuver.hasCourseWeaponBullets()) {
                    if (!waitGroup(n) && n == 0) {
                        setGroupTask(1);
                        break;
                    }
                    break;
                }
                else {
                    if (w.curr().Action == 2 && maneuver.Loc.distance(maneuver.target.Loc) > 2500.0D) {
                        maneuver.set_task(3);
                        maneuver.clear_stack();
                        maneuver.set_maneuver(21);
                        break;
                    }
                    maneuver.set_task(6);
                    if (maneuver.target.actor instanceof TypeFighter) {
                        if (n == 0 && bInitAttack) {
                            maneuver.clear_stack();
                            Vector3d vector3d = new Vector3d();
                            Vector3d vector3d2 = new Vector3d();
                            vector3d.sub(maneuver.target.Loc, maneuver.Loc);
                            maneuver.Or.transformInv(vector3d);
                            vector3d.z = 0.0;
                            vector3d.normalize();
                            vector3d2.set(vector3d);
                            maneuver.target.Or.transformInv(vector3d2);
                            vector3d2.z = 0.0;
                            vector3d2.normalize();
                            float n4 = (float)maneuver.Vwld.length();
                            float n5 = (float)maneuver.target.Vwld.length();
                            float n6 = (float)maneuver.Loc.distance(maneuver.target.Loc);
                            if (n5 > 30.0F && maneuver.target.Loc.z - Engine.land().HQ_Air(maneuver.target.Loc.x, maneuver.target.Loc.y) > 50.0D) {
                                int[] groupLove = groupLove((float)vector3d.x, (float)vector3d2.y, n4, n5, (float)maneuver.Loc.z, (float)maneuver.target.Loc.z, n6, outNum, grSubSkill, Mission.curCloudsType(), nOfAirc);
                                maneuver.set_maneuver(groupLove[0]);
                                for (int n7 = 1; n7 < groupLove.length; ++n7) {
                                    maneuver.push(groupLove[n7]);
                                }
                                break;
                            }
                            maneuver.set_maneuver(27);
                            coverFree();
                            break;
                        }
                        else {
                            if (bInitAttack)
                                break;

                            if (maneuver.VmaxH * 0.94F > maneuver.target.VmaxH && (maneuver.Loc.distance(maneuver.target.Loc) > 1000.0D || maneuver.Vwld.length() > maneuver.target.Vwld.length() + 10.0D)) {
                                maneuver.clear_stack();
                                maneuver.set_maneuver(62);
                                break;
                            }
                            maneuver.clear_stack();
                            maneuver.set_maneuver(27);
                            break;
                        }
                    }
                    else {
                        if (maneuver.target.actor instanceof TypeStormovik) {
                            ((Pilot)maneuver).attackStormoviks();
                            break;
                        }
                        ((Pilot)maneuver).attackBombers();
                        break;
                    }
                }
            }
//            break;  // never come here
        case 2:
            FlightModel leader3 = null;
            tmpV.set(0.0D, 0.0D, 0.0D);
            for (int n8 = 0; n8 < nOfAirc; ++n8) {
                Maneuver leader4 = (Maneuver)airc[n8].FM;
                tmpV.add(leader4.Offset);
                if (!leader4.isBusy() || n8 == nOfAirc - 1 || (leader4 instanceof RealFlightModel && ((RealFlightModel)leader4).isRealMode())) {
                    leader4.Leader = null;
                    if (clientGroup != null && clientGroup.nOfAirc > 0 && clientGroup.airc[0] != null) {
                        leader4.airClient = clientGroup.airc[0].FM;
                        leader4.accurate_set_task_maneuver(5, 59);
                    }
                    else {
                        leader4.accurate_set_task_maneuver(3, 21);
                    }
                    tmpV.set(0.0D, 0.0D, 0.0D);
                    for (int n9 = n8 + 1; n9 < nOfAirc; ++n9) {
                        Maneuver maneuver3 = (Maneuver)airc[n9].FM;
                        tmpV.add(maneuver3.Offset);
                        if (!maneuver3.isBusy()) {
                            maneuver3.accurate_set_FOLLOW();
                            if (leader3 == null) {
                                maneuver3.followOffset.set(tmpV);
                                maneuver3.Leader = leader4;
                            }
                            else {
                                maneuver3.followOffset.set(maneuver3.Offset);
                                maneuver3.Leader = leader3;
                            }
                        }
                        if (maneuver3 instanceof RealFlightModel) {
                            if ((airc[n9].aircIndex() & 0x1) == 0x0) {
                                leader3 = maneuver3;
                            }
                        }
                        else {
                            leader3 = null;
                        }
                    }
                    break;
                }
            }
            break;
        case 5:
            if (maneuver.isBusy())
                return;

            break;
        case 7:
            if (maneuver.isBusy())
                return;

            Maneuver maneuver4 = (Maneuver)airc[0].FM;
            if (n > 0 && (maneuver4.getSpeed() < 30.0F || maneuver4.Loc.z - Engine.land().HQ_Air(maneuver4.Loc.x, maneuver4.Loc.y) < 51.5D)) {
                maneuver.airClient = airc[n - 1].FM;
                maneuver.push();
                maneuver.push(82);
                maneuver.pop();
                break;
            }
            break;
        case 6:
            if (maneuver.isBusy())
                return;

            break;
        case 8:
            if (maneuver.isBusy())
                return;

            maneuver.set_maneuver(106);
            maneuver.actionTimerStop = maneuver.AP.way.curr().delayTimer * 60000L;
            break;
        default:
            if (maneuver.isBusy())
                return;

            maneuver.set_maneuver(21);
            break;
        }
    }

    public void coverClose()
    {
        for(int i = 1; i < nOfAirc; i++)
        {
            Maneuver maneuver = (Maneuver)airc[i].FM;
            maneuver.Leader = maneuver.airClient = airc[i - 1].FM;
            maneuver.set_maneuver(65);
        }

    }

    public void coverStd()
    {
        Maneuver maneuver = (Maneuver)airc[0].FM;
        for(int i = 1; i < nOfAirc; i++)
        {
            Maneuver maneuver1 = (Maneuver)airc[i].FM;
            if((airc[i].aircIndex() & 1) == 0)
            {
                maneuver1.airClient = maneuver;
                ((Pilot)maneuver1).wingmanAttacks(3);
            } else
            {
                maneuver1.Leader = maneuver1.airClient = airc[i - 1].FM;
                ((Pilot)maneuver1).wingmanAttacks(1);
            }
        }

    }

    void coverFree()
    {
        for(int i = 1; i < nOfAirc; i++)
        {
            Maneuver maneuver = (Maneuver)airc[i].FM;
            ((Pilot)maneuver).wingmanAttacks(0);
        }

    }

    public void attackBox()
    {
        for(int i = 1; i < nOfAirc; i++)
        {
            Maneuver maneuver = (Maneuver)airc[i].FM;
            maneuver.target = setAAttackObject(i);
            maneuver.set_maneuver(93);
        }

    }

    public void attackLine()
    {
        for(int i = 1; i < nOfAirc; i++)
        {
            Maneuver maneuver = (Maneuver)airc[i].FM;
            maneuver.target = setAAttackObject(i);
            maneuver.set_maneuver(92);
        }

    }

    public void attackFluid(int i)
    {
        Maneuver maneuver = (Maneuver)airc[0].FM;
        for(int j = 1; j < nOfAirc; j++)
        {
            Maneuver maneuver1 = (Maneuver)airc[j].FM;
            if((airc[j].aircIndex() & 1) != 0)
            {
                maneuver1.Leader = maneuver1.airClient = airc[j - 1].FM;
                ((Pilot)maneuver1).wingmanAttacks(1);
            } else
            {
                maneuver1.airClient = maneuver;
                maneuver1.push(27);
                maneuver1.set_maneuver(i);
            }
        }

    }

    public void update()
    {
        if(nOfAirc == 0 || airc[0] == null)
            return;
        for(int i = 1; i < nOfAirc; i++)
            if(!Actor.isAlive(airc[i]))
            {
                delAircraft(airc[i]);
                i--;
            }

        Maneuver maneuver = (Maneuver)airc[0].FM;
        if(Time.current() > timeNextAAACheck)
        {
            aaaShooters.clear();
            aaaNum /= 1.5F;
            if(aaaNum < 0.5F)
                aaaNum = 0.0F;
            timeNextAAACheck += 10000L;
        }
        if(maneuver.AP.way.curr().waypointType < 406 && maneuver.AP.way.curr().waypointType >= 401)
        {
            int j = maneuver.AP.way.curr().waypointType - 401;
            setCAP(j, 406, 0, 0, 0.0F, 0.0F, false);
            if(maneuver.AP.way.curr().delayTimer > 0)
                capTimerSet = true;
        }
        if(maneuver.AP.way.curr().waypointType == 406 && capTimerSet)
        {
            for(int k = capFirstWP; k < capLastWP + 1; k++)
                w.look_at_point(k).delayTimer = w.look_at_point(capFirstWP - 1).delayTimer + (int)(Time.current() / 60000L);

            capTimerSet = false;
        }
        if(maneuver.AP.way.curr().waypointType == 406 && maneuver.AP.way.curr().delayTimer > 0 && maneuver.AP.way.curr().delayTimer <= (int)(Time.current() / 60000L))
        {
            w.setCur(capLastWP + 1);
            rejoinToGroup(this);
            maneuver.AP.way.setCur(capLastWP + 1);
            capCycles = 0;
            capFirstWP = 0;
            capLastWP = 0;
            capTimerSet = false;
        }
        if(maneuver.AP.way.Cur() == capLastWP && capCycles > 0)
        {
            w.setCur(capFirstWP);
            for(int l = capFirstWP; l < capLastWP + 1; l++)
            {
                Point3d point3d = w.look_at_point(l).getP();
                float f = w.look_at_point(capFirstWP - 1).altDifference;
                point3d.add(0.0D, 0.0D, f);
                if(point3d.z < 100D)
                    point3d.z = 100D;
                w.look_at_point(l).set(point3d);
            }

            rejoinToGroup(this);
            maneuver.AP.way.setCur(capFirstWP);
            capCycles--;
            if(capCycles == 0)
            {
                capFirstWP = 0;
                capLastWP = 0;
                capTimerSet = false;
            }
        }
        if(maneuver.AP.way.curr().waypointType == 407 && grTask != 8)
        {
            setGroupTask(8);
            airc[0].bSpotter = true;
        }
        if(leaderGroup != null)
            if(leaderGroup.nOfAirc == 0)
                detachGroup(leaderGroup);
            else
            if(leaderGroup.airc[0] == null)
                detachGroup(leaderGroup);
            else
            if(leaderGroup.airc[0].FM.AP.way.isLanding())
            {
                detachGroup(leaderGroup);
            } else
            {
                maneuver.AP.way.setCur(leaderGroup.w.Cur());
                if(maneuver.get_maneuver() == 21 && !((Maneuver)leaderGroup.airc[0].FM).isBusy())
                    setTaskAndManeuver(0);
            }
        if(grTask == 7 && (maneuver.isCrashedOnGround() || !maneuver.isOk() || maneuver.Loc.z - Engine.land().HQ_Air(maneuver.Loc.x, maneuver.Loc.y) > 400D))
            setGroupTask(1);
        if(w == null)
            w = new Way(maneuver.AP.way);
        if(!maneuver.AP.way.isLanding() && maneuver.isOk())
            w.setCur(maneuver.AP.way.Cur());
        if(!maneuver.AP.way.isLanding())
        {
            for(int i1 = 1; i1 < nOfAirc; i1++)
                if(!((Maneuver)airc[i1].FM).AP.way.isLanding() && !((Maneuver)airc[i1].FM).isBusy())
                    ((Maneuver)airc[i1].FM).AP.way.setCur(w.Cur());

        }
        if(maneuver.AP.way.curr().isRadioSilence() || Main.cur().mission.zutiMisc_DisableAIRadioChatter)
        {
            for(int j1 = 0; j1 < nOfAirc; j1++)
                ((Maneuver)airc[j1].FM).silence = true;

        } else
        {
            for(int k1 = 0; k1 < nOfAirc; k1++)
                ((Maneuver)airc[k1].FM).silence = false;

        }
        Pos.set(maneuver.Loc);
        if(formationType == -1)
            setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, true);
        if(timeOutForTaskSwitch == 0)
label0:
            switch(w.curr().Action)
            {
            case 3: // '\003'
                boolean flag = w.curr().getTarget() != null || (airc[0] instanceof TypeFighter) || (airc[0] instanceof TypeStormovik) && (!(airc[0] instanceof TypeBomber) || airc[0].FM.getAltitude() < 2500F) || (airc[0] instanceof D3A) || (airc[0] instanceof MXY_7) || (airc[0] instanceof JU_87);
                if(grTask == 4)
                {
                    weWereInGAttack = true;
                    boolean flag1 = somebodyGAttacks();
                    boolean flag6 = false;
                    for(int k3 = 0; k3 < nOfAirc; k3++)
                    {
                        Maneuver maneuver3 = (Maneuver)airc[k3].FM;
                        if(maneuver3.gattackCounter >= 7)
                            flag6 = true;
                    }

                    if(!flag1 || flag6 || gTargDestroyed)
                    {
                        airc[0].FM.AP.way.next();
                        w.next();
                        setGroupTask(1);
                        for(int l3 = 1; l3 < nOfAirc; l3++)
                        {
                            Maneuver maneuver4 = (Maneuver)airc[l3].FM;
                            maneuver4.push(57);
                            maneuver4.pop();
                        }

                        setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, true);
                        if(flag6)
                        {
                            for(int i4 = 0; i4 < nOfAirc; i4++)
                                ((Maneuver)airc[i4].FM).gattackCounter = 0;

                        }
                    }
                } else
                if(grTask == 3)
                {
                    switchWayPoint();
                    weWereInGAttack = true;
                    if(AirGroupList.length(enemies[0]) != oldEnemyNum)
                        setGroupTask(3);
                    boolean flag2 = somebodyAttacks();
                    if(!flag2 || aTargDestroyed)
                    {
                        setGroupTask(1);
                        if(!flag2)
                            timeOutForTaskSwitch = 90;
                        for(int l2 = 1; l2 < nOfAirc; l2++)
                        {
                            Maneuver maneuver1 = (Maneuver)airc[l2].FM;
                            if(!maneuver1.isBusy())
                            {
                                maneuver1.push(57);
                                maneuver1.pop();
                            }
                        }

                    }
                }
                if(grTask != 1 || w.curr().Action != 3)
                    break;
                gTargWasFound = false;
                gTargDestroyed = false;
                gTargMode = 0;
                if(!flag)
                    break;
                setFormationAndScale((byte)9, 8F, true);
                if(maneuver.AP.getWayPointDistance() >= 5000F)
                    break;
                boolean flag3 = false;
                if(w.curr().getTarget() != null)
                {
                    setGTargMode(w.curr().getTarget());
                    if(gTargMode == 0)
                    {
                        flag3 = true;
                    } else
                    {
                        maneuver.target_ground = setGAttackObject(0);
                        if(maneuver.target_ground != null && maneuver.target_ground.distance(airc[0]) < 12000D)
                        {
                            setGroupTask(4);
                            Voice.speakBeginGattack(airc[0]);
                        } else
                        if(maneuver.AP.getWayPointDistance() < 1500F)
                            flag3 = true;
                    }
                } else
                {
                    flag3 = true;
                }
                if(!flag3)
                    break;
                Engine.land();
                tmpP3d.set(w.curr().x(), w.curr().y(), Landscape.HQ(w.curr().x(), w.curr().y()));
                setGTargMode(tmpP3d, 800F);
                maneuver.target_ground = setGAttackObject(0);
                if(maneuver.target_ground != null)
                {
                    setGroupTask(4);
                    Voice.speakBeginGattack(airc[0]);
                }
                break;

            case 0: // '\0'
            case 2: // '\002'
                if(grTask == 2)
                {
                    if(enemyFighters)
                    {
                        int l1 = AirGroupList.length(enemies[0]);
                        int i3 = 0;
                        do
                        {
                            if(i3 >= l1)
                                break;
                            AirGroup airgroup = AirGroupList.getGroup(enemies[0], i3);
                            if(airgroup.nOfAirc > 0 && (airgroup.airc[0] instanceof TypeFighter))
                            {
                                bInitAttack = false;
                                targetGroup = airgroup;
                                grTask = 3;
                                setTaskAndManeuver(0);
                                break;
                            }
                            i3++;
                        } while(true);
                    }
                    if(w.Cur() >= w.size() - 1)
                    {
                        setGroupTask(1);
                        setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, true);
                    }
                    if(clientGroup == null || clientGroup.nOfAirc == 0 || clientGroup.w.Cur() >= clientGroup.w.size() - 1 || clientGroup.airc[0].FM.AP.way.isLanding())
                    {
                        maneuver.AP.way.next();
                        w.setCur(maneuver.AP.way.Cur());
                        for(int i2 = 1; i2 < nOfAirc; i2++)
                            ((Maneuver)airc[i2].FM).AP.way.setCur(w.Cur());

                        setGroupTask(1);
                        setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, true);
                    }
                    switchWayPoint();
                    break;
                }
                if(grTask == 3)
                {
                    switchWayPoint();
                    weWereInGAttack = true;
                    if(AirGroupList.length(enemies[0]) != oldEnemyNum)
                        setGroupTask(3);
                    boolean flag4 = somebodyAttacks();
                    if(flag4 && !aTargDestroyed)
                        break;
                    setGroupTask(1);
                    setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, false);
                    if(!flag4)
                        timeOutForTaskSwitch = 90;
                    int j3 = 1;
                    do
                    {
                        if(j3 >= nOfAirc)
                            break label0;
                        Maneuver maneuver2 = (Maneuver)airc[j3].FM;
                        if(!maneuver2.isBusy())
                        {
                            maneuver2.push(57);
                            maneuver2.pop();
                        }
                        j3++;
                    } while(true);
                }
                if(grTask == 4)
                {
                    weWereInGAttack = true;
                    boolean flag5 = somebodyGAttacks();
                    boolean flag7 = false;
                    for(int j4 = 0; j4 < nOfAirc; j4++)
                    {
                        Maneuver maneuver5 = (Maneuver)airc[j4].FM;
                        if(maneuver5.gattackCounter >= 7)
                            flag7 = true;
                    }

                    if(flag5 && !flag7 && !gTargDestroyed)
                        break;
                    setGroupTask(1);
                    setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, true);
                    for(int k4 = 1; k4 < nOfAirc; k4++)
                    {
                        Maneuver maneuver6 = (Maneuver)airc[k4].FM;
                        maneuver6.push(57);
                        maneuver6.pop();
                    }

                    if(!flag7)
                        break;
                    for(int l4 = 0; l4 < nOfAirc; l4++)
                        ((Maneuver)airc[l4].FM).gattackCounter = 0;

                    break;
                }
                if(grTask == 8)
                {
                    if(maneuver.target_ground == null)
                    {
                        Engine.land();
                        tmpP3d.set(w.curr().x(), w.curr().y(), Landscape.HQ(w.curr().x(), w.curr().y()));
                        maneuver.target_ground = NearestEnemies.getAFoundEnemy(tmpP3d, 800D, maneuver.actor.getArmy());
                    }
                    break;
                }
                if(grTask != 1)
                    break;
                if(weWereInGAttack || gTargMode != 0)
                {
                    weWereInGAttack = false;
                    gTargMode = 0;
                    setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, false);
                    ((Maneuver)airc[0].FM).WeWereInGAttack = true;
                }
                if(weWereInAttack)
                {
                    weWereInAttack = false;
                    setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, false);
                    ((Maneuver)airc[0].FM).WeWereInAttack = true;
                }
                if(w.Cur() > 0 && grAttached == 0 && oldFType == 0)
                {
                    w.curr().getP(tmpP);
                    tmpV.sub(tmpP, Pos);
                    setFormationAndScale((byte)maneuver.AP.way.curr().formation, 1.0F, true);
                }
                int j2 = w.Cur();
                w.next();
                if(w.curr().Action == 2 || w.curr().Action == 3 && (w.curr().getTarget() != null && !(airc[0] instanceof Scheme4) && !(airc[0] instanceof Scheme8) && !(airc[0] instanceof Scheme10) || (airc[0] instanceof TypeStormovik) || (airc[0] instanceof JU_87)))
                {
                    w.curr().getP(tmpP);
                    tmpV.sub(tmpP, Pos);
                    float f1 = (float)tmpV.length();
                    if(f1 < 20000F)
                        if(w.curr().waypointType == 104)
                            setFormationAndScale((byte)10, 8F, true);
                        else
                        if(airc[0].FM.getAltitude() > 1200F)
                            setFormationAndScale((byte)9, 8F, true);
                }
                w.setCur(j2);
                if(w.curr().getTarget() != null)
                {
                    Actor actor = w.curr().getTargetActorRandom();
                    if(actor instanceof Aircraft)
                    {
                        tmpV.sub(((Aircraft)actor).FM.Loc, Pos);
                        if(tmpV.lengthSquared() < 144000000D)
                            if(actor.getArmy() == airc[0].getArmy())
                            {
                                if((airc[0] instanceof TypeFighter) && !maneuver.hasBombs() && !maneuver.hasRockets())
                                {
                                    if(w.Cur() < w.size() - 2)
                                    {
                                        clientGroup = ((Maneuver)((Aircraft)actor).FM).Group;
                                        setGroupTask(2);
                                        setFormationAndScale((byte)maneuver.AP.way.curr().formation, 2.5F, true);
                                    }
                                } else
                                {
                                    attachGroup(((Maneuver)((Aircraft)actor).FM).Group);
                                }
                            } else
                            if((airc[0] instanceof TypeFighter) || (airc[0] instanceof TypeStormovik))
                            {
                                targetGroup = ((Maneuver)((Aircraft)actor).FM).Group;
                                setGroupTask(3);
                            }
                    }
                } else
                if(AirGroupList.length(enemies[0]) > 0)
                {
                    boolean flag8 = false;
                    if(airc[0] instanceof TypeStormovik)
                    {
                        int i5 = AirGroupList.length(enemies[0]);
                        int k5 = 0;
                        do
                        {
                            if(k5 >= i5)
                                break;
                            AirGroup airgroup1 = AirGroupList.getGroup(enemies[0], k5);
                            if(airgroup1 != null && airgroup1.nOfAirc != 0 && !(airgroup1.airc[0] instanceof TypeFighter))
                            {
                                flag8 = true;
                                targetGroup = airgroup1;
                                break;
                            }
                            k5++;
                        } while(true);
                        if(flag8)
                        {
                            if(maneuver.hasBombs())
                                flag8 = false;
                            int k2 = w.Cur();
                            for(; flag8 && w.Cur() < w.size() - 1; w.next())
                                if(w.curr().Action == 3)
                                    flag8 = false;

                            w.setCur(k2);
                        }
                    }
                    if(!flag8 && (airc[0] instanceof TypeFighter))
                    {
                        for(int j5 = 0; j5 < nOfAirc; j5++)
                            if(((Maneuver)airc[j5].FM).canAttack())
                                flag8 = true;

                        if(flag8 && maneuver.CT.getWeaponMass() > 220F)
                            flag8 = false;
                    }
                    if(flag8)
                        setGroupTask(3);
                }
                if(rejoinGroup != null)
                    rejoinToGroup(rejoinGroup);
                break;

            case 1: // '\001'
            default:
                grTask = 1;
                break;
            }
        oldEnemyNum = AirGroupList.length(enemies[0]);
        if(timeOutForTaskSwitch > 0)
            timeOutForTaskSwitch--;
    }

    public void setEnemyConvPoint(FlightModel flightmodel, Actor actor)
    {
        Point3d point3d = new Point3d();
        point3d.set(flightmodel.Loc);
        Object aobj[] = new Object[3];
        aobj[0] = point3d;
        aobj[1] = actor;
        aobj[2] = new Long(Time.current());
        convPoint.put(flightmodel, ((Object) (aobj)));
    }

    public Point3d getEnemyConvPoint(FlightModel flightmodel, Actor actor)
    {
        cleanConvPointMap();
        Object obj = convPoint.get(flightmodel);
        if(obj != null)
        {
            Object aobj[] = (Object[])(Object[])obj;
            if(aobj[1] == actor)
                return null;
            else
                return (Point3d)aobj[0];
        } else
        {
            return null;
        }
    }

    private void cleanConvPointMap()
    {
        Set set = convPoint.keySet();
        Iterator iterator = set.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Object obj = iterator.next();
            Object obj1 = convPoint.get(obj);
            Object aobj[] = (Object[])(Object[])obj1;
            long l = ((Long)aobj[2]).longValue();
            if(Time.current() - l > 60000L)
                iterator.remove();
        } while(true);
    }

    public int nOfAirc;
    public Aircraft airc[];
    private Squadron sq;
    public Way w;
    public Vector3d Pos;
    public AirGroupList enemies[];
    private AirGroupList friends[];
    public AirGroup clientGroup;
    public AirGroup targetGroup;
    public AirGroup leaderGroup;
    public AirGroup rejoinGroup;
    private int grAttached;
    public int gTargetPreference;
    public int aTargetPreference;
    private boolean enemyFighters;
    private boolean gTargWasFound;
    private boolean gTargDestroyed;
    private int gTargMode;
    private Actor gTargActor;
    private Point3d gTargPoint;
    private float gTargRadius;
    private boolean aTargWasFound;
    private boolean aTargDestroyed;
    private boolean weWereInGAttack;
    private boolean weWereInAttack;
    public byte formationType;
    private byte oldFType;
    private float oldFScale;
    private boolean oldFInterp;
    private boolean fInterpolation;
    private int oldEnemyNum;
    public int timeOutForTaskSwitch;
    public int grTask;
    private static final Point3d point3dWP = new Point3d();
    private static final Point3d point3dNewWP = new Point3d();
    private static final Point3f pfcf = new Point3f();
    private int grSA;
    private int grSubSkill;
    private int grCourage;
    private int grSkill;
    public boolean bSee;
    public boolean bInitAttack;
    public int outNum;
    public static final byte W_FREE_HUNT = 0;
    public static final byte W_COVER = 1;
    public static final byte W_CLOUDS = 2;
    public static final byte W_SECTION_BRACKET = 3;
    public static final byte W_EVADE = 4;
    public static final int FLY_WAYPOINT = 1;
    public static final int DEFENDING = 2;
    public static final int ATTACK_AIR = 3;
    public static final int ATTACK_GROUND = 4;
    private static final int TAKEOFF = 5;
    private static final int LANDING = 6;
    public static final int TAKEOFF_AND_HANG_ON = 7;
    private static final int ARTILLERY_SPOTTING = 8;
    private static final int GT_MODE_NONE = 0;
    private static final int GT_MODE_CHIEF = 1;
    private static final int GT_MODE_AROUND_POINT = 2;
    private static final byte TRIANGLE = 0;
    private static final byte SQUARE = 1;
    private static final byte PENTAGON = 2;
    private static final byte HEXAGON = 3;
    private static final byte RANDOM = 4;
    private int underAAA;
    private long timeNextAAACheck;
    public int bracketSideGr;
    private final HashSet aaaShooters;
    public float aaaNum;
    private static final String GTList[] = {
        "NO_TASK.", "FLY_WAYPOINT", "DEFENDING", "ATTACK_AIR", "ATTACK_GROUND", "TAKEOFF", "LANDING", "TAKEOFF_AND_HANG_ON", "ARTILLERY_SPOTTING"
    };
    private static final float xtriangle[] = {
        577.35F, -288.675F, -288.675F, 577.35F
    };
    private static final float ytriangle[] = {
        0.0F, 500F, -500F, 0.0F
    };
    private static final float xr[] = {
        707.11F, 0.0F, -707.11F, 0.0F, 707.11F
    };
    private static final float yr[] = {
        0.0F, 707.11F, 0.0F, -707.11F, 0.0F
    };
    private static final float xh[] = {
        1000F, 500F, -500F, -1000F, -500F, 500F, 1000F
    };
    private static final float yh[] = {
        -0F, 866.025F, 866.025F, 0.0F, -866.025F, -866.025F, 0.0F
    };
    private static final float xp[] = {
        850.651F, 262.866F, -688.191F, -688.191F, 262.866F, 850.651F
    };
    private static final float yp[] = {
        0.0F, 809.017F, 500F, -500F, -809.017F, 0.0F
    };
    private int capFirstWP;
    private int capLastWP;
    private int capCycles;
    private int capAltitude;
    private boolean capTimerSet;
    private static final Vector3d tmpV = new Vector3d();
    private static final Vector3d tmpV1 = new Vector3d();
    private static Point3d tmpP = new Point3d();
    private static final Point3d tmpP3d = new Point3d();
    private static final Vector2d P1P2vector = new Vector2d();
    private static final Vector2d norm1 = new Vector2d();
    private static final Vector2d myPoint = new Vector2d();
    private final HashMap convPoint;

}
