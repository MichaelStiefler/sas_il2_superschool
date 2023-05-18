package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class O_47 extends Scheme1 implements TypeScout {

    public O_47() {
        this.bGunUp = false;
        this.btme = -1L;
        this.fGunPos = 0.0F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.63F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.15F, 0.99F, 0.0F, 0.63F);
        this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", -25F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 25F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -56F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 56F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", -22F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 22F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 90F * f, 0.0F);
    }

    protected void moveGear(float f) {
        O_47.moveGear(this.hierMesh(), f);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 0.0F, 45F * f);
        this.hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 0.0F, 45F * f);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 4; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -40F) {
                    f = -40F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxeng1")) {
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 280000F)) {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 100000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 0.66F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 32200F)));
                        if (World.Rnd().nextFloat() < (shot.power / 1000000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("oil1") && (World.Rnd().nextFloat() < 0.5F) && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.1F, shot) > 0.0F) {
                    this.FM.AS.hitTank(shot.initiator, i, 1);
                    if ((World.Rnd().nextFloat() < 0.02F) || ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.11F))) {
                        this.FM.AS.hitTank(shot.initiator, i, 2);
                    }
                }
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
            if (World.Rnd().nextFloat() < 0.19F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            }
            if (World.Rnd().nextFloat() < 0.19F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            }
        } else if (s.startsWith("xeng")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xrudder")) {
            if (this.chunkDamageVisible("Rudder1") < 2) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 2)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 2)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 2)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xWingLIn") && (this.chunkDamageVisible("WingLIn") < 2)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xWingRIn") && (this.chunkDamageVisible("WingRIn") < 2)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xWingLOut") && (this.chunkDamageVisible("WingLOut") < 2)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xWingROut") && (this.chunkDamageVisible("WingROut") < 2)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel") && (this.chunkDamageVisible("AroneL") < 2)) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner") && (this.chunkDamageVisible("AroneR") < 2)) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int j;
            if (s.endsWith("a")) {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else {
                j = s.charAt(5) - 49;
            }
            this.hitFlesh(j, shot, byte0);
        }
    }

    public void update(float f) {
        if (this.FM.isPlayers() && (this.FM.Sq.squareElevators > 0.0F)) {
            if ((this.FM.getSpeedKMH() > 380F) && (this.FM.getSpeedKMH() < 500F)) {
                this.FM.SensPitch = 0.3F;
                this.FM.producedAM.y -= 400F * (300F - this.FM.getSpeedKMH());
            }
            if (this.FM.getSpeedKMH() >= 501F) {
                this.FM.SensPitch = 0.2F;
                this.FM.producedAM.y -= 200F * (300F - this.FM.getSpeedKMH());
            } else {
                this.FM.SensPitch = 0.45F;
            }
        }
        if (this.FM.AS.isMaster() && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && this.FM.Gears.onGround()) {
            if (this.FM.getSpeedKMH() < 20.0F) {
              this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
              this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        if (!this.bGunUp) {
            if (this.fGunPos > 0.0F) {
                this.fGunPos -= 0.2F * f;
                this.FM.turret[0].bIsOperable = false;
                this.hierMesh().chunkVisible("Turret1A_D0", false);
                this.hierMesh().chunkVisible("Turret1B_D0", false);
                this.hierMesh().chunkVisible("Turdown_D0", true);
                this.hierMesh().chunkVisible("Turupps_D0", false);
                this.hierMesh().chunkVisible("Cover_D0", true);
                this.hierMesh().chunkVisible("Hatch_D0", false);
            }
        } else if (this.fGunPos < 1.0F) {
            this.fGunPos += 0.2F * f;
            if ((this.fGunPos > 0.8F) && (this.fGunPos < 0.9F)) {
                this.FM.turret[0].bIsOperable = true;
                this.hierMesh().chunkVisible("Turret1A_D0", true);
                this.hierMesh().chunkVisible("Turret1B_D0", true);
                this.hierMesh().chunkVisible("Turdown_D0", false);
                this.hierMesh().chunkVisible("Turupps_D0", true);
                this.hierMesh().chunkVisible("Cover_D0", false);
                this.hierMesh().chunkVisible("Hatch_D0", true);
            }
        }
        if (this.fGunPos < 0.6F) {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(this.fGunPos, 0.0F, 0.6F, 0.0F, -0.5F);
            this.hierMesh().chunkSetLocate("Blister3_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (this.FM.turret[0].bIsAIControlled) {
            if ((this.FM.turret[0].target != null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                this.bGunUp = true;
            }
            if (Time.current() > this.btme) {
                this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if ((this.FM.turret[0].target == null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                    this.bGunUp = false;
                }
            }
        }
        super.update(f);
    }

    boolean         bGunUp;
    public long     btme;
    public float    fGunPos;

    static {
        Class class1 = O_47.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
