package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class DH116xyz extends Scheme1
    implements TypeFighter, TypeBNZFighter, TypeStormovik, TypeGSuit, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeFastJet, TypeSupersonic
{

    public DH116xyz()
    {
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        bToFire = false;
        guidedMissileUtils = new GuidedMissileUtils(this);
        this.transsonicEffects = new TransonicEffects(this, 0.0F, 9000F, 0.8F, 1.0F, 0.01F, 1.0F, 0.2F, 1.0F, 0.45F, 0.58F, 0.0F, 0.9F, 1.0F, 1.25F);
    }

    public void getGFactors(TypeGSuit.GFactors theGFactors)
    {
        theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
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

    public void setCommonThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = curTime;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = curTime;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long curTime = Time.current();
        if(curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = curTime;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.transsonicEffects.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -37F * f, 0.0F);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && this.FM.CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
        if(!this.FM.isPlayers() && (this.FM instanceof Maneuver))
            if((this.FM.AP.way.isLanding() || this.FM.AP.way.isLandingOnShip()) && this.FM.AP.getWayPointDistance() < 2500F)
            {
                if((double)this.FM.getSpeedKMH() < (double)this.FM.VmaxFLAPS * 0.4D)
                    this.FM.CT.AirBrakeControl = 0.0F;
                else
                    this.FM.CT.AirBrakeControl = 1.0F;
            } else
            if(((Maneuver)this.FM).get_maneuver() == 66)
                this.FM.CT.AirBrakeControl = 0.0F;
            else
            if(((Maneuver)this.FM).get_maneuver() == 7)
                this.FM.CT.AirBrakeControl = 1.0F;
            else
                this.FM.CT.AirBrakeControl = 0.0F;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, 90F, 0.0F) : Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, 90F);
        float f2 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, 90F, 0.0F) : Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 90F);
        float f3 = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, -90F, 0.0F) : Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.9F, -15F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, -88F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f2, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.6F, 0.0F, 88F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, 100F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.15F, 0.0F, -0.1F);
        hierMesh().chunkSetLocate("GearLPiston", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.15F, 0.0F, 0.1F);
        hierMesh().chunkSetLocate("GearRPiston", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0F, 0.2F, 0.0F, -0.4F);
        hierMesh().chunkSetLocate("GearCPiston", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("Vator_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 20F * f, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, 20F * f, 0.0F);
        if(this.FM.CT.getGear() > 0.8F)
            hierMesh().chunkSetAngles("FrontForks", 0.0F, 0.0F, -40F * f);
    }

    protected void moveFlap(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = 0.68F * f;
        Aircraft.xyz[2] = 0.1F * f;
        hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = -30F * f;
        float f2 = 30F * f;
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f2, 0.0F);
    }

    protected void moveAirBrake(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.15F);
        hierMesh().chunkSetLocate("AirBrakeL", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("AirBrakeR", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 55F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("1")) {
                    this.getEnergyPastArmor(World.Rnd().nextFloat(20F, 30F), shot);
                    debugprintln(this, "*** Armor Glass: Hit..");
                    if (shot.power <= 0.0F) {
                        debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        if (World.Rnd().nextFloat() < 0.5F) this.doRicochetBack(shot);
                    }
                }
                if (s.endsWith("2")) this.getEnergyPastArmor(12.7F / (1E-005F + (float) Math.abs(v1.x)), shot);
                if (s.endsWith("3")) this.getEnergyPastArmor(12.7F / (1E-005F + (float) Math.abs(v1.x)), shot);
                if (s.endsWith("4")) this.getEnergyPastArmor(8.9F / (1E-005F + (float) Math.abs(v1.x)), shot);
                if (s.endsWith("5")) this.getEnergyPastArmor(8.9F / (1E-005F + (float) Math.abs(v1.z)), shot);
                return;
            }
            if (s.startsWith("xxarcon")) {
                if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Ailerones Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxvatcon")) {
                if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 1);
                    debugprintln(this, "*** Elevators Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxrudcon")) {
                if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.setControlsDamage(shot.initiator, 2);
                    debugprintln(this, "*** Rudder Controls Out..");
                }
                return;
            }
            if (s.startsWith("xxeng1")) {
                if (this.getEnergyPastArmor(4.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 0.75F) {
                    this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 6800F)));
                    debugprintln(this, "*** Engine 0 Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                    if (this.FM.AS.astateEngineStates[0] < 1) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 1);
                        this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                    }
                    if (World.Rnd().nextFloat() < shot.power / 48000F) {
                        this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        debugprintln(this, "*** Engine 0 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                return;
            }
            if (s.startsWith("xxeng2")) {
                if (this.getEnergyPastArmor(4.45F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[1].getCylindersRatio() * 0.75F) {
                    this.FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 6800F)));
                    debugprintln(this, "*** Engine 1 Cylinders Hit, " + this.FM.EI.engines[1].getCylindersOperable() + "/" + this.FM.EI.engines[1].getCylinders() + " Left..");
                    if (this.FM.AS.astateEngineStates[1] < 1) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 1);
                        this.FM.AS.doSetEngineState(shot.initiator, 1, 1);
                    }
                    if (World.Rnd().nextFloat() < shot.power / 48000F) {
                        this.FM.AS.hitEngine(shot.initiator, 1, 3);
                        debugprintln(this, "*** Engine 1 Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                return;
            }
            if (s.startsWith("xxoilradiat1")) {
                if (this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Engine Module 0: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoilradiat2")) {
                if (this.getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F) {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    debugprintln(this, "*** Engine Module 1: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoiltank1")) {
                if (this.getEnergyPastArmor(2.38F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    debugprintln(this, "*** Engine Module 0: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxoiltank2")) {
                if (this.getEnergyPastArmor(2.38F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    debugprintln(this, "*** Engine Module 1: Oil Radiator Hit..");
                }
                return;
            }
            if (s.startsWith("xxmagneto1")) {
                int i = World.Rnd().nextInt(0, 1);
                this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                debugprintln(this, "*** Engine Module 0: Magneto " + i + " Destroyed..");
                return;
            }
            if (s.startsWith("xxmagneto2")) {
                int j = World.Rnd().nextInt(0, 1);
                this.FM.EI.engines[1].setMagnetoKnockOut(shot.initiator, j);
                debugprintln(this, "*** Engine Module 1: Magneto " + j + " Destroyed..");
                return;
            }
            if (s.startsWith("xxturbo1")) {
                if (this.getEnergyPastArmor(1.23F, shot) > 0.0F) {
                    this.FM.EI.engines[0].setKillCompressor(shot.initiator);
                    debugprintln(this, "*** Engine Module 0: Supercharger Destroyed..");
                }
                return;
            }
            if (s.startsWith("xxturbo2")) {
                if (this.getEnergyPastArmor(1.23F, shot) > 0.0F) {
                    this.FM.EI.engines[1].setKillCompressor(shot.initiator);
                    debugprintln(this, "*** Engine Module 1: Supercharger Destroyed..");
                }
                return;
            }
            if (s.startsWith("xxradiat")) {
                int k = 0;
                if (s.endsWith("3") || s.endsWith("4")) k = 1;
                if (World.Rnd().nextFloat() < 0.25F) if (this.FM.AS.astateEngineStates[k] == 0) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, k, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, k, 1);
                } else if (this.FM.AS.astateEngineStates[k] == 1) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, k, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, k, 2);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                byte byte0 = 0;
                int j1 = s.charAt(6) - 48;
                switch (j1) {
                    case 1:
                        byte0 = 1;
                        break;

                    case 2:
                        byte0 = 1;
                        break;

                    case 3:
                        byte0 = 0;
                        break;

                    case 4:
                        byte0 = 2;
                        break;

                    case 5:
                        byte0 = 2;
                        break;

                    case 6:
                        byte0 = 3;
                        break;
                }
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitTank(shot.initiator, byte0, 1);
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F) this.FM.AS.hitTank(shot.initiator, byte0, 2);
                }
                return;
            }
            if (s.startsWith("xxgun")) {
                int l = s.charAt(5) - 49;
                this.FM.AS.setJamBullets(0, l);
                this.getEnergyPastArmor(23.5F, shot);
                return;
            }
            if (s.startsWith("xxcannon")) {
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(48.6F, shot);
                return;
            }
            if (s.startsWith("xxammogun")) {
                int i1 = World.Rnd().nextInt(0, 3);
                this.FM.AS.setJamBullets(0, i1);
                this.getEnergyPastArmor(23.5F, shot);
                return;
            }
            if (s.startsWith("xxammocan")) {
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(23.5F, shot);
                return;
            }
            if (s.startsWith("xxspar")) {
                debugprintln(this, "*** Spar Construction: Hit..");
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxpark1") && this.chunkDamageVisible("Keel1") > 1 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxpark2") && this.chunkDamageVisible("Keel2") > 1 && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Keel2 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel2_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Stab Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                debugprintln(this, "*** Lock Construction: Hit..");
                if ((s.startsWith("xxlockk1") || s.startsWith("xxlockk2")) && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if ((s.startsWith("xxlockk3") || s.startsWith("xxlockk4")) && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (s.startsWith("xxlocksl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** Vator Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
                    debugprintln(this, "*** AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf") || s.startsWith("xcockpit")) this.hitChunk("CF", shot);
        if (!s.startsWith("xblister")) if (s.startsWith("xengine1")) this.hitChunk("Engine1", shot);
        else if (s.startsWith("xengine2")) this.hitChunk("Engine2", shot);
        else if (s.startsWith("xtail1")) this.hitChunk("Tail1", shot);
        else if (s.startsWith("xtail2")) this.hitChunk("Tail2", shot);
        else if (s.startsWith("xkeel1")) this.hitChunk("Keel1", shot);
        else if (s.startsWith("xkeel2")) this.hitChunk("Keel2", shot);
        else if (s.startsWith("xrudder1")) this.hitChunk("Rudder1", shot);
        else if (s.startsWith("xrudder2")) this.hitChunk("Rudder2", shot);
        else if (s.startsWith("xstabl")) this.hitChunk("StabL", shot);
        else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 1) this.hitChunk("VatorL", shot);
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
            if (s.startsWith("xwingrin") && this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
            if (s.startsWith("xwinglmid") && this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
            if (s.startsWith("xwingrmid") && this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
            if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
            if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
            if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte1 = 0;
            int k1;
            if (s.endsWith("a")) {
                byte1 = 1;
                k1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte1 = 2;
                k1 = s.charAt(6) - 49;
            } else k1 = s.charAt(5) - 49;
            this.hitFlesh(k1, shot, byte1);
        }
    }

    public void update(float f)
    {
        if(!this.FM.isPlayers() && this.FM.Gears.onGround())
            if(this.FM.EI.engines[0].getRPM() < 100F)
                this.FM.CT.cockpitDoorControl = 1.0F;
            else
                this.FM.CT.cockpitDoorControl = 0.0F;
        if((this.FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && this.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            this.FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        if(this.FM.getSpeedKMH() > 56F)
            this.FM.CT.cockpitDoorControl = 0.0F;
        if(this.FM.getSpeedKMH() > 340F)
            this.FM.CT.GearControl = 0.0F;
        if(Config.isUSE_RENDER() && this.FM.AS.isMaster())
            if(this.FM.EI.engines[0].getPowerOutput() > 0.7F && this.FM.EI.engines[0].getStage() == 6)
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.99F)
                    this.FM.AS.setSootState(this, 0, 3);
                else
                    this.FM.AS.setSootState(this, 0, 2);
            } else
            {
                this.FM.AS.setSootState(this, 0, 0);
            }
        if(this.FM.EI.engines[0].getPowerOutput() > 0.55F)
            hierMesh().chunkVisible("ExhaustGas1", true);
        else
            hierMesh().chunkVisible("ExhaustGas1", false);
        if(this.FM.EI.engines[0].getPowerOutput() > 0.65F)
            hierMesh().chunkVisible("ExhaustGas1", false);
        if(this.FM.EI.engines[0].getPowerOutput() > 0.65F)
            hierMesh().chunkVisible("ExhaustGas2", true);
        else
            hierMesh().chunkVisible("ExhaustGas2", false);
        if(this.FM.EI.engines[0].getPowerOutput() > 0.75F)
            hierMesh().chunkVisible("ExhaustGas2", false);
        if(this.FM.EI.engines[0].getPowerOutput() > 0.75F)
            hierMesh().chunkVisible("ExhaustGas3", true);
        else
            hierMesh().chunkVisible("ExhaustGas3", false);
        if(this.FM.EI.engines[0].getPowerOutput() > 0.9F)
            hierMesh().chunkVisible("ExhaustGas3", false);
        if(this.FM.EI.engines[0].getPowerOutput() > 0.9F)
            hierMesh().chunkVisible("ExhaustGas4", true);
        else
            hierMesh().chunkVisible("ExhaustGas4", false);
        if(this.FM.EI.engines[0].getPowerOutput() > 0.6F)
            hierMesh().chunkVisible("CB", true);
        else
            hierMesh().chunkVisible("CB", false);
        guidedMissileUtils.update();
        this.soundbarier();
        super.update(f);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            this.FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);

        case 11:
            cut("StabL");
            cut("StabR");
            this.FM.cut(17, j, actor);
            this.FM.cut(18, j, actor);
            break;

        case 13:
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object paramObject)
            {
                Aircraft localAircraft = (Aircraft)paramObject;
                if(!Actor.isValid(localAircraft))
                {
                    return;
                } else
                {
                    Loc localLoc1 = new Loc();
                    Loc localLoc2 = new Loc();
                    Vector3d localVector3d = new Vector3d(0.0D, 0.0D, 10D);
                    HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
                    localAircraft.pos.getAbs(localLoc2);
                    localHookNamed.computePos(localAircraft, localLoc2, localLoc1);
                    localLoc1.transform(localVector3d);
                    localVector3d.x += localAircraft.FM.Vwld.x;
                    localVector3d.y += localAircraft.FM.Vwld.y;
                    localVector3d.z += localAircraft.FM.Vwld.z;
                    new EjectionSeat(1, localLoc1, localVector3d, localAircraft);
                    return;
                }
            }

        }
;
        hierMesh().chunkVisible("Seat", false);
    }

    private void bailout()
    {
        if(overrideBailout)
            if(this.FM.AS.astateBailoutStep >= 0 && this.FM.AS.astateBailoutStep < 2)
            {
                if(this.FM.CT.cockpitDoorControl > 0.5F && this.FM.CT.getCockpitDoor() > 0.5F)
                    this.FM.AS.astateBailoutStep = 11;
                else
                    this.FM.AS.astateBailoutStep = 2;
            } else
            if(this.FM.AS.astateBailoutStep >= 2 && this.FM.AS.astateBailoutStep <= 3)
            {
                switch(this.FM.AS.astateBailoutStep)
                {
                case 2:
                    if(this.FM.CT.cockpitDoorControl < 0.5F)
                        doRemoveBlister1();
                    break;

                case 3:
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp178_177 = this.FM.AS;
                tmp178_177.astateBailoutStep = (byte)(tmp178_177.astateBailoutStep + 1);
                if(this.FM.AS.astateBailoutStep == 4)
                    this.FM.AS.astateBailoutStep = 11;
            } else
            if(this.FM.AS.astateBailoutStep >= 11 && this.FM.AS.astateBailoutStep <= 19)
            {
                int i = this.FM.AS.astateBailoutStep;
                if(this.FM.AS.isMaster())
                    this.FM.AS.netToMirrors(20, this.FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp383_382 = this.FM.AS;
                tmp383_382.astateBailoutStep = (byte)(tmp383_382.astateBailoutStep + 1);
                if(i == 11)
                {
                    this.FM.setTakenMortalDamage(true, null);
                    if((this.FM instanceof Maneuver) && ((Maneuver)this.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(this.FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)this.FM).set_maneuver(44);
                    }
                }
                if(this.FM.AS.astatePilotStates[i - 11] < 99)
                {
                    doRemoveBodyFromPlane(i - 10);
                    if(i == 11)
                    {
                        doEjectCatapult();
                        lTimeNextEject = Time.current() + 1000L;
                        this.FM.setTakenMortalDamage(true, null);
                        this.FM.CT.WeaponControl[0] = false;
                        this.FM.CT.WeaponControl[1] = false;
                        this.FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        this.FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                        if(i > 10 && i <= 19)
                            EventLog.onBailedOut(this, i - 11);
                        return;
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && this.FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            localWreckage.collide(false);
            Vector3d localVector3d = new Vector3d();
            localVector3d.set(this.FM.Vwld);
            localWreckage.setSpeed(localVector3d);
        }
    }

    public float getAirPressure(float theAltitude) {
        return this.transsonicEffects.getAirPressure(theAltitude);
    }

    public float getAirPressureFactor(float theAltitude) {
        return this.transsonicEffects.getAirPressureFactor(theAltitude);
    }

    public float getAirDensity(float theAltitude) {
        return this.transsonicEffects.getAirDensity(theAltitude);
    }

    public float getAirDensityFactor(float theAltitude) {
        return this.transsonicEffects.getAirDensityFactor(theAltitude);
    }

    public float getMachForAlt(float theAltValue) {
        return this.transsonicEffects.getMachForAlt(theAltValue);
    }

    public float calculateMach() {
        return this.FM.getSpeedKMH() / this.getMachForAlt(this.FM.getAltitude());
    }

    public void soundbarier() {
        this.transsonicEffects.soundbarrier();
    }

    private final TransonicEffects transsonicEffects;

    private GuidedMissileUtils guidedMissileUtils;
    public boolean bToFire;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    private long lTimeNextEject;
    private boolean ejectComplete;
    private boolean overrideBailout;

    static 
    {
        Class class1 = DH116xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
