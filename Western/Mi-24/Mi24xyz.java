
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import java.util.ArrayList;
import java.util.Random;


public class Mi24xyz extends Mi8_24HeliFamily
    implements TypeStormovik, TypeGuidedMissileCarrier, TypeCountermeasure, TypeSACLOS, TypeRadarWarningReceiver
{

    public Mi24xyz()
    {
        kmguList = new ArrayList();
        kmguStat = 0;
        bHasKMGU = false;
        bRemainKMGUbomblets = false;
        kmguClosingTime = -1L;
        saclosSpotPos = new Point3d();
        bSACLOSenabled = false;
        tLastSaclosUpdate = -1L;
        bHasSturm = false;
        tLastSturmCheck = -1L;
        guidedMissileUtils = new GuidedMissileUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        backfire = false;

        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_RHMIS, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, RWR_SECOTOR_NUM, bRWR_Show_Text_Warning);
    }

    public RadarWarningReceiverUtils getRadarWarningReceiverUtils()
    {
        return rwrUtils;
    }

    public void myRadarSearchYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarSearched(actor, soundpreset);
    }

    public void myRadarLockYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarLocked(actor, soundpreset);
    }

    public void startCockpitSounds()
    {
        if(rwrUtils != null)
            rwrUtils.setSoundEnable(true);
    }

    public void stopCockpitSounds()
    {
        if(rwrUtils != null)
            rwrUtils.stopAllRWRSounds();
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 700L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 1000L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

// ---- TypeSACLOS implements .... begin ----
    public Point3d getSACLOStarget()
    {
        return saclosSpotPos;
    }

    public boolean setSACLOStarget(Point3d p3d)
    {
        saclosSpotPos.set(p3d);

        return true;
    }

    public boolean getSACLOSenabled()
    {
        return bSACLOSenabled;
    }

    public boolean setSACLOSenable(boolean flag)
    {
        return bSACLOSenabled = flag;
    }
// ---- TypeSACLOS implements .... end ----

    private void saclosUpdate()
    {
        if(Time.current() == tLastSaclosUpdate)
            return;

        tLastSaclosUpdate = Time.current();
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d, orient2);

        super.pos.setUpdateEnable(true);
        super.pos.getRender(Actor._tmpLoc);
        saclosHook = new HookNamed(this, "_MGUN01");
        saclosLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        saclosHook.computePos(this, Actor._tmpLoc, saclosLoc1);
        saclosLoc1.get(saclosP1);
        saclosLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        saclosHook.computePos(this, Actor._tmpLoc, saclosLoc1);
        saclosLoc1.get(saclosP2);
        if(Landscape.rayHitHQ(saclosP1, saclosP2, saclosPL))
        {
            saclosPL.z -= 0.95D;
            saclosP2.interpolate(saclosP1, saclosPL, 1.0F);
            setSACLOStarget(saclosP2);
            bSACLOSenabled = true;
        }
        else
            bSACLOSenabled = false;
    }

    private void checkAmmo()
    {
        bHasSturm = false;
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_KMGU2_gn16)
                        kmguList.add(FM.CT.Weapons[i][j]);
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_GUV8700_gn16)
                        ((Pylon_GUV8700_gn16) FM.CT.Weapons[i][j]).matSilver();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_GUV8700AGS_gn16)
                        ((Pylon_GUV8700AGS_gn16) FM.CT.Weapons[i][j]).matSilver();
                    else if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunSturmV_gn16)
                            bHasSturm = true;
                    }
                }
            }

        if(kmguList.size() > 0)
        {
            bHasKMGU = true;
            bRemainKMGUbomblets = true;
        }
    }

    public void onAircraftLoaded()
    {
        checkAmmo();
        super.onAircraftLoaded();
        FM.CT.bHasBombSelect = true;
        FM.CT.bHasAntiColLights = true;
        guidedMissileUtils.onAircraftLoaded();

        rwrUtils.onAircraftLoaded();
        rwrUtils.setLockTone("aircraft.usRWRScan", "aircraft.Sirena2", "aircraft.Sirena2", "aircraft.usRWRThreatNew");
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(!FM.isPlayers())
            FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
        anticollights();

        // Only fuel tank '1' is self sealing.
        if(FM.AS.astateTankStates[1] == 1)
        {
            if(World.Rnd().nextFloat() < 0.2F)
                FM.AS.astateTankStates[1] = 0;
        }

        if(bHasSturm && Time.current() > tLastSturmCheck + 120000L)
        {
            int sturmnum = 0;
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    {
                        if(FM.CT.Weapons[i][j].haveBullets())
                        {
                            if(FM.CT.Weapons[i][j] instanceof RocketGunSturmV_gn16)
                                sturmnum += FM.CT.Weapons[i][j].countBullets();
                        }
                    }
                }

            if(sturmnum == 0)
                bHasSturm = false;
        }
    }

    private void anticollights()
    {
        if(FM.CT.bAntiColLights && isGeneratorAlive && chunkDamageVisible("Tail1") < 3)
        {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            super.pos.getAbs(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
        }
    }

    protected void moveElevator(float f)
    {
        if(FM.Gears.nOfGearsOnGr == 3) return;

        // Bt western,  Historically Mi-24 has 'All flying tail' stabilizers even a helicopter !
        hierMesh().chunkSetAngles("StabL_D0", 0.0F, 0.0F, 15F * f);
        hierMesh().chunkSetAngles("StabR_D0", 0.0F, 0.0F, 15F * f);
    }

    public static void moveGear(HierMesh hiermesh, float fL, float fR, float fC)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, cvt(fC, 0.1F, 0.9F, 0.0F, -90F));
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, cvt(fC, 0.1F, 0.9F, 0.0F, -140F));

        float fl4 = fL <= 0.5F ? Aircraft.cvt(fL, 0.05F, 0.45F, 0.0F, 70F) : Aircraft.cvt(fL, 0.8F, 1.0F, 70F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(fL, 0.0F, 0.4F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, fl4, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", cvt(fL, 0.15F, 0.85F, 0.0F, -90F), 0.0F, 0.0F);

        float fr4 = fR <= 0.5F ? Aircraft.cvt(fR, 0.05F, 0.45F, 0.0F, -70F) : Aircraft.cvt(fR, 0.8F, 1.0F, -70F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(fR, 0.0F, 0.4F, 0.0F, 85F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, fr4, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", cvt(fR, 0.15F, 0.85F, 0.0F, 90F), 0.0F, 0.0F);
    }

    protected void moveGear(float fL, float fR, float fC)
    {
        moveGear(hierMesh(), fL, fR, fC);
    }

    public void moveSteering(float f)
    {
        float fstr = cvt(f, -30F, 30F, -30F, 30F);
        if(FM.CT.GearControl > 0.9F)
            hierMesh().chunkSetAngles("GearC2_D0", fstr, 0.0F, -90F);
        else if(FM.CT.GearControl < 0.1F)
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
        hierMesh().chunkSetAngles("Door1_D0", -90F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Door2_D0", 0.0F, -65F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -150F * cvt(f, 0F, 0.66F, 0F, 0.66F), 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 150F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 150F * cvt(f, 0F, 0.66F, 0F, 0.66F), 0.0F);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -150F * f, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.32F, 0.0F, -10F), 0.0F);
        hierMesh().chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.32F, 0.0F, 10F), 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        boolean flag1 = this instanceof Mi24xyz;
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxmainrotor"))
            {
                if(getEnergyPastArmor(0.11F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F)
                    hitProp(0, 2, shot.initiator);
            }
            if(s.startsWith("xxtailrotor"))
            {
                if(getEnergyPastArmor(0.063F, shot) > 0.0F && World.Rnd().nextFloat() < 0.3F)
                    hitProp(1, 2, shot.initiator);
            }
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.startsWith("xxarmorp"))
                {
                    int i = s.charAt(8) - 48;
                    switch(i)
                    {
                    default:
                        break;

                    case 1: // '\001'
                        getEnergyPastArmor(7.07D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                        shot.powerType = 0;
                        break;

                    case 2: // '\002'
                    case 3: // '\003'
                        getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
                        shot.powerType = 0;
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.866D)
                            doRicochet(shot);
                        break;

                    case 4: // '\004'
                        if(((Tuple3d) (point3d)).x > -1.35D)
                        {
                            getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                            shot.powerType = 0;
                            if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.866D)
                                doRicochet(shot);
                        } else
                        {
                            getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                        }
                        break;

                    case 5: // '\005'
                    case 6: // '\006'
                        getEnergyPastArmor(20.2D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                        if(shot.power > 0.0F)
                            break;
                        if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.866D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                        break;

                    case 7: // '\007'
                        getEnergyPastArmor(20.200000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;
                    }
                }
                if(s.startsWith("xxarmorc1"))
                    getEnergyPastArmor(7.07D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                if(s.startsWith("xxarmort1"))
                    getEnergyPastArmor(6.06D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.10D && getEnergyPastArmor(3.40D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxsparlm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparrm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingRMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingRMid") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparlo"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLOut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLOut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparro"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingROut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingROut") > 2 && (double)World.Rnd().nextFloat() > Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.12D && getEnergyPastArmor(6.96D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y + ((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if(s.startsWith("xxeng"))
            {
                int k = s.charAt(5) - 49;
                debuggunnery("Engine Module " + k + ": Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(3.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            debuggunnery("Engine Module " + k + ": Prop Governor Hit, Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, k, 3);
                        } else
                        {
                            debuggunnery("Engine Module " + k + ": Prop Governor Hit, Oil Pipes Damaged..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, k, 4);
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Engine Module " + k + ": Reductor Hit, Bullet Jams Reductor Gear..");
                        FM.EI.engines[k].setEngineStuck(shot.initiator);
                    }
                } else
                if(s.endsWith("feed"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module " + k + ": Feed Lines Hit, Engine Stalled..");
                            FM.EI.engines[k].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module " + k + ": Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, k);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module " + k + ": Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            FM.EI.engines[k].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.endsWith("fue1"))
                {
                    if(getEnergyPastArmor(0.89F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module " + k + ": Fuel Feed Line Pierced, Engine Fires..");
                        FM.AS.hitEngine(shot.initiator, k, 100);
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            debuggunnery("Engine Module " + k + ": Crank Case Hit, Bullet Jams Ball Bearings..");
                            FM.AS.setEngineStuck(shot.initiator, k);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            debuggunnery("Engine Module " + k + ": Crank Case Hit, Readyness Reduced to " + FM.EI.engines[k].getReadyness() + "..");
                            FM.AS.hitEngine(shot.initiator, k, 2);
                        }
                    }
                    FM.EI.engines[k].setReadyness(shot.initiator, FM.EI.engines[k].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debuggunnery("Engine Module " + k + ": Crank Case Hit, Readyness Reduced to " + FM.EI.engines[k].getReadyness() + "..");
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.endsWith("oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module " + k + ": Oil Radiator Hit, Oil Radiator Pierced..");
                    FM.AS.hitOil(shot.initiator, k);
                }
            }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    } else
                    if(FM.AS.astateTankStates[l] == 1)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced, State Shifted..");
                    }
                }
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("30") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: GSh30-2k Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("01") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Left Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Right Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxammo"))
            {
                if(s.startsWith("xxammo1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Nose Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.startsWith("xxammor1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                if(s.startsWith("xxammol2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.startsWith("xxammor2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.00345F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                debuggunnery("Armament System: Bomb Payload Detonated..");
                FM.AS.hitTank(shot.initiator, 0, 10);
                FM.AS.hitTank(shot.initiator, 1, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxpnm") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Pneumo System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if(s.startsWith("xxhyd") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.startsWith("xxins"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcockpit") || s.startsWith("xblister"))
            if(((Tuple3d) (point3d)).z > 0.473D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            else
            if(((Tuple3d) (point3d)).y > 0.0D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            else
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
        if(s.startsWith("xcf"))
        {
            if(((Tuple3d) (point3d)).x < -1.94D)
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            {
                if(((Tuple3d) (point3d)).x <= 1.342D)
                    if(((Tuple3d) (point3d)).z < -0.591D || ((Tuple3d) (point3d)).z > 0.408D && ((Tuple3d) (point3d)).x > 0.0D)
                    {
                        getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.866D)
                            doRicochet(shot);
                    } else
                    {
                        getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
                        if(shot.power <= 0.0F && Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.866D)
                            doRicochet(shot);
                    }
                if(chunkDamageVisible("CF") < 3)
                    hitChunk("CF", shot);
            }
        } else
        if(s.startsWith("xoil"))
        {
            if(((Tuple3d) (point3d)).z < -0.981D)
            {
                getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            } else
            if(((Tuple3d) (point3d)).x > 0.537D || ((Tuple3d) (point3d)).x < -0.10D)
            {
                getEnergyPastArmor(0.20D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 0.0001D), shot);
                if(shot.power <= 0.0F)
                    doRicochetBack(shot);
            } else
            {
                getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            }
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(((Tuple3d) (point3d)).z > 0.159D)
                getEnergyPastArmor((double)(1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 1.335D && ((Tuple3d) (point3d)).x < 2.386D && ((Tuple3d) (point3d)).z > -0.06D && ((Tuple3d) (point3d)).z < 0.064D)
                getEnergyPastArmor(0.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 2.30D && ((Tuple3d) (point3d)).x < 2.992D && ((Tuple3d) (point3d)).z > -0.235D && ((Tuple3d) (point3d)).z < 0.011D)
                getEnergyPastArmor(4.04D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 2.560D && ((Tuple3d) (point3d)).z < -0.595D)
                getEnergyPastArmor(4.04D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 1.849D && ((Tuple3d) (point3d)).x < 2.251D && ((Tuple3d) (point3d)).z < -0.71D)
                getEnergyPastArmor(4.04D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
            if(((Tuple3d) (point3d)).x > 3.003D)
                getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            else
            if(((Tuple3d) (point3d)).z < -0.60600000619888306D)
                getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 0.0001D), shot);
            else
                getEnergyPastArmor(5.05D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 0.0001D), shot);
            if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.866D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
                doRicochet(shot);
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 1)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwingl") && chunkDamageVisible("WingL") < 1)
                hitChunk("WingL", shot);
            if(s.startsWith("xwingr") && chunkDamageVisible("WingR") < 1)
                hitChunk("WingR", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(getEnergyPastArmor(0.25F, shot) > 0.0F)
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                FM.AS.setJamBullets(10, 0);
//                FM.AS.setJamBullets(10, 1);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else
        if(s.startsWith("xhelm"))
        {
            getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if(shot.power <= 0.0F)
                doRicochetBack(shot);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else
            {
                i1 = s.charAt(5) - 49;
            }
            hitFlesh(i1, shot, byte0);
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bObserverKilled = true;
            break;
        }
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        rwrUtils.update();
        backfire = rwrUtils.getBackfire();
        bRadarWarning = rwrUtils.getRadarLockedWarning();
        bMissileWarning = rwrUtils.getMissileWarning();
        if(backfire)
            backFire();
        if(bHasSturm)
            saclosUpdate();
        super.update(f);
        if(bHasKMGU)
            kmguDoorControl();
    }

    private void kmguDoorControl()
    {
        if(hasBomblets() && kmguStat != 15)
        {
            boolean bDroppedBomblets = (FM.CT.getLastBombReleaseTime() > 0L && Time.current() < FM.CT.getLastBombReleaseTime() + 1000L &&
                             (FM.CT.getLastBombReleaseClass() == BombAO2_5_3.class || FM.CT.getLastBombReleaseClass() == BombPTAB25.class));

            if(bDroppedBomblets)
            {
                kmguStat = 15;
                for(int i = 0; i < kmguList.size(); i++)
                    ((Pylon_KMGU2_gn16)(kmguList.get(i))).setDoors(kmguStat);
                kmguClosingTime = -1L;
            }
        }

        if(kmguStat == 15 && kmguClosingTime == -1L && !hasBomblets())
            kmguClosingTime = Time.current();

        if(kmguStat == 15 && kmguClosingTime != -1L && Time.current() > kmguClosingTime + 12000L)
        {
            kmguStat = 0;
            for(int i = 0; i < kmguList.size(); i++)
                ((Pylon_KMGU2_gn16)(kmguList.get(i))).setDoors(kmguStat);
            kmguClosingTime = -1L;
        }
    }

    private boolean hasBomblets()
    {
        if(!bHasKMGU || !bRemainKMGUbomblets)
            return false;

        if(FM.CT.Weapons[3] != null) {
            for(int i = 0; i < FM.CT.Weapons[3].length; i++)
                if(FM.CT.Weapons[3][i] != null && FM.CT.Weapons[3][i].countBullets() != 0)
                    if(FM.CT.Weapons[3][i] instanceof BombGunAO2_5_3 || FM.CT.Weapons[3][i] instanceof BombGunPTAB25)
                        return true;

        }

        bRemainKMGUbomblets = false;
        return false;
    }

    static Class _mthclass$(String x0)
    {
        try
        {
            return Class.forName(x0);
        }
        catch(ClassNotFoundException x1)
        {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public static boolean bChangedPit = false;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private ArrayList kmguList;
    private int kmguStat;
    private boolean bHasKMGU;
    private boolean bRemainKMGUbomblets;
    private long kmguClosingTime;

    //By western0221, Radar Warning Receiver
    private RadarWarningReceiverUtils rwrUtils;
    public boolean bRadarWarning;
    public boolean bMissileWarning;
    public boolean backfire;

    private static final int RWR_GENERATION = 1;
    private static final int RWR_MAX_DETECT = 16;
    private static final int RWR_KEEP_SECONDS = 6;
    private static final double RWR_RECEIVE_ELEVATION = 45.0D;
    private static final boolean RWR_DETECT_RHMIS = false;
    private static final boolean RWR_DETECT_IRMIS = true;
    private static final boolean RWR_DETECT_ELEVATION = false;
    private static final int RWR_SECOTOR_NUM = 8;
    private boolean bRWR_Show_Text_Warning = true;

    private Point3d saclosSpotPos;
    private boolean bSACLOSenabled;
    private long tLastSaclosUpdate;
    private Hook saclosHook;
    private Loc saclosLoc1 = new Loc();
    private Point3d saclosP1 = new Point3d();
    private Point3d saclosP2 = new Point3d();
    private Point3d saclosPL = new Point3d();
    private boolean bHasSturm;
    private long tLastSturmCheck;

}