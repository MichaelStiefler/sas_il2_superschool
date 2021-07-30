package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Time;

public class MBR_2xyz extends Scheme1 implements TypeTransport, TypeBomber {

    public MBR_2xyz() {
        this.curTurretPosition = 0.0F;
        this.gunnerDead = false;
        this.gunnerEjected = false;
        this.tme0 = 0L;
        this.airstartr = false;
        this.bRSArmed = false;
        this.canopyF = 0.0F;
        this.tiltCanopyOpened = false;
        this.slideCanopyOpened = false;
        this.blisterRemoved = false;
        this.bChangedPit = true;
        this.tSAim = 0.0F;
        this.kSAim = 0.0F;
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 150F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 4; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 11:
                super.cutFM(17, j, actor);
                this.FM.cut(17, j, actor);
                super.cutFM(18, j, actor);
                this.FM.cut(18, j, actor);
                return super.cutFM(i, j, actor);

            case 19:
                this.FM.Gears.bIsSail = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveElevator(float f) {
        float f1 = 20F * f;
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        float f1 = -20F * f;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f1, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -30F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.hierMesh().chunkVisible("Pilot2_D0", false);
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.turret.length != 0) {
            this.gunnerAiming();
            this.gunnerTarget();
        }
    }

    public void gunnerTarget() {
        if (this.gunnerDead || this.gunnerEjected) {
            this.FM.turret[0].bIsOperable = false;
            return;
        }
        if (!this.FM.turret[0].bIsAIControlled) {
            this.FM.turret[0].bIsOperable = true;
            return;
        }
        if (Time.current() > this.tme0) {
            this.tme0 = Time.current() + 2000L;
            if (this.FM.turret.length != 0) {
                Actor actor = War.GetNearestEnemy(this, 16, 7000F);
                Aircraft aircraft = War.getNearestEnemy(this, 6000F);
                if (actor != null && !(actor instanceof BridgeSegment) || aircraft != null) {
                    this.FM.turret[0].bIsOperable = true;
                    this.FM.turret[0].bIsAIControlled = true;
                } else {
                    this.FM.turret[0].tu[0] = this.FM.turret[0].tu[1] = 0.0F;
                    this.hierMesh().setCurChunk(this.FM.turret[0].indexA);
                    this.hierMesh().chunkSetAngles(this.FM.turret[0].tu);
                    this.hierMesh().setCurChunk(this.FM.turret[0].indexB);
                    this.hierMesh().chunkSetAngles(this.FM.turret[0].tu);
                    this.FM.turret[0].bIsAIControlled = true;
                }
            }
        }
    }

    public void gunnerAiming() {
        this.moveGunner();
    }

    public void moveGunner() {
        if (this.gunnerDead || this.gunnerEjected) {
            this.FM.turret[0].bIsOperable = false;
            return;
        }
        this.FM.turret[0].bIsOperable = true;
        this.hierMesh().chunkVisible("Pilot2_D0", true);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    public void moveCockpitDoor(float f) {
        if (this.airstartr && f > 0.99F) this.airstartr = false;
        else this.airstartr = true;
        if (f > this.canopyF) {
            if ((this.FM.Gears.onGround() && this.FM.getSpeed() < 5F || this.tiltCanopyOpened) && (this.FM.isPlayers() || this.isNetPlayer())) {
                this.tiltCanopyOpened = true;
                this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -f * 179F, 0.0F);
            } else {
                this.slideCanopyOpened = true;
                this.resetYPRmodifier();
                Aircraft.xyz[0] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.2F);
                this.hierMesh().chunkSetLocate("BlisterW_D0", Aircraft.xyz, Aircraft.ypr);
            }
        } else if (this.FM.Gears.onGround() && this.FM.getSpeed() < 5F && !this.slideCanopyOpened || this.tiltCanopyOpened) {
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -f * 179F, 0.0F);
            if (this.FM.getSpeed() > 50F && f < 0.6F && !this.blisterRemoved) this.doRemoveBlisters();
            if (f == 0.0F) this.tiltCanopyOpened = false;
        } else {
            this.resetYPRmodifier();
            Aircraft.xyz[0] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.2F);
            this.hierMesh().chunkSetLocate("BlisterW_D0", Aircraft.xyz, Aircraft.ypr);
            if (f == 0.0F) this.slideCanopyOpened = false;
        }
        this.canopyF = f;
        if (this.canopyF < 0.01D) this.canopyF = 0.0F;
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void blisterRemoved(int i) {
        if (i == 1) this.doRemoveBlisters();
    }

    private final void doRemoveBlisters() {
        this.blisterRemoved = true;
        this.FM.CT.bHasCockpitDoorControl = false;
        this.bChangedPit = true;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                if (f <= 0.0F) this.gunnerDead = true;
                break;

            case 2:
                this.FM.turret[1].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                if (World.cur().isHighGore()) this.hierMesh().chunkVisible("Gore1_D0", true);
                this.gunnerDead = true;
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                if (World.cur().isHighGore()) this.hierMesh().chunkVisible("Gore2_D0", true);
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        if (i == 1) this.gunnerEjected = true;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    private boolean StopFireNoseTurret(float f, float f1) {
        boolean flag = false;
        if ((f <= -111.55F || f >= 111.55F) && f1 > -11.63F && f1 < 3F) if (f1 > 2.5F) {
            if (f <= -113.44F || f >= 113.44F) {
                float f3 = Aircraft.cvt(Math.abs(f), 113.44F, 180F, 3F, 2.5F);
                if (f1 < f3) flag = true;
                else flag = false;
            } else if (f <= -112F && f >= -113.44F || f >= 112F && f <= 113.44F) {
                float f4 = Aircraft.cvt(Math.abs(f), 112F, 113.44F, 2.5F, 3F);
                if (f1 < f4) flag = true;
                else flag = false;
            }
        } else if (Math.abs(f) > 111.55F) {
            float f5 = Aircraft.cvt(Math.abs(f), 111.55F, 180F, 1.76F, -11.63F);
            if (f1 > f5) flag = true;
            else flag = false;
        } else if (Math.abs(f) > 111.55F && Math.abs(f) < 112F) {
            float f6 = Aircraft.cvt(Math.abs(f), 111.55F, 112F, 1.76F, 2.5F);
            if (f1 > f6) flag = true;
            else flag = false;
        }
        return !flag;
    }

    private boolean StopFireRearTurret(float f, float f1) {
        boolean flag = false;
        if (f >= -38.5F && f <= 38.5F && f1 > 8.5F && f1 < 16.5F && !flag) if (f1 > 13.6F) {
            float f3 = Aircraft.cvt(Math.abs(f), 0.0F, 38.5F, 16.5F, 13.6F);
            if (f1 < f3) flag = true;
            else flag = false;
        } else if (Math.abs(f) < 32.31F) {
            float f4 = Aircraft.cvt(Math.abs(f), 0.0F, 32.31F, 9.5F, 8.5F);
            if (f1 > f4) flag = true;
            else flag = false;
        } else {
            float f5 = Aircraft.cvt(Math.abs(f), 32.31F, 38.5F, 8.5F, 13.6F);
            if (f1 > f5) flag = true;
            else flag = false;
        }
        return !flag;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 > 89.5F) {
                    f1 = 89.5F;
                    flag = false;
                }
                if (f1 < -40F) {
                    f1 = -40F;
                    flag = false;
                }
                if (f >= -90F && f <= 90F) {
                    if (f1 < -40F) {
                        f1 = -40F;
                        flag = false;
                    }
                } else if (f >= -156.6F && f <= 156.6F) {
                    float f4 = Aircraft.cvt(Math.abs(f), 90F, 156.6F, -40F, -11.89F);
                    if (f1 < f4) {
                        f1 = f4;
                        flag = false;
                    }
                } else if (f >= -158.6F && f <= 158.6F) {
                    float f5 = Aircraft.cvt(Math.abs(f), 156.6F, 158.6F, -11.89F, -3.09F);
                    if (f1 < f5) {
                        f1 = f5;
                        flag = false;
                    }
                } else if (f > -162F && f < 162F) {
                    float f6 = Aircraft.cvt(Math.abs(f), 158.6F, 162F, -3.09F, 18.69F);
                    if (f1 < f6) {
                        f1 = f6;
                        flag = false;
                    }
                } else if (f1 < 40F) {
                    if (f1 < 18.69F) f1 = 18.69F;
                    if (f >= 0.0F) f = 162F;
                    else f = -162F;
                    flag = false;
                } else {
                    float f7 = Aircraft.cvt(Math.abs(f), 162F, 180F, 40F, 50F);
                    if (f1 < f7) {
                        f1 = f7;
                        flag = false;
                    }
                }
                if (flag) flag = this.StopFireNoseTurret(f, f1);
                break;

            case 1:
                if (f1 > 89.5F) {
                    f1 = 89.5F;
                    flag = false;
                }
                if (f >= -1.43F && f <= 1.43F) {
                    if (f1 < 30.8F) {
                        flag = false;
                        if (f1 < -2F) f1 = -2F;
                    } else flag = true;
                } else if (f >= -2.5F && f <= 2.5F) {
                    float f9 = Aircraft.cvt(Math.abs(f), 1.43F, 2.5F, 30.8F, -2F);
                    if (f1 < f9) {
                        f1 = f9;
                        flag = false;
                    }
                    if (f1 < -2F) {
                        f1 = -2F;
                        flag = false;
                    }
                } else if (f >= -2.9F && f <= 2.9F) {
                    float f10 = Aircraft.cvt(Math.abs(f), 2.5F, 2.9F, -2F, -3.4F);
                    if (f1 < f10) {
                        f1 = f10;
                        flag = false;
                    }
                } else if (f >= -15.2F && f <= 15.2F) {
                    float f11 = Aircraft.cvt(Math.abs(f), 2.9F, 15.2F, -3.4F, -13.09F);
                    if (f1 < f11) {
                        f1 = f11;
                        flag = false;
                    }
                } else if (f >= -65.38F && f <= 65.38F) {
                    float f12 = Aircraft.cvt(Math.abs(f), 15.2F, 65.38F, -13.09F, -40F);
                    if (f1 < f12) {
                        f1 = f12;
                        flag = false;
                    }
                } else if (f >= -100F && f <= 100F) {
                    if (f1 < -40F) {
                        f1 = -40F;
                        flag = false;
                    }
                } else if (f >= -105F && f <= 105F) {
                    float f13 = Aircraft.cvt(Math.abs(f), 100F, 105F, -40F, 2.0F);
                    if (f1 < f13) {
                        f1 = f13;
                        flag = false;
                    }
                } else if (f >= -110.4F && f <= 110.4F) {
                    float f14 = Aircraft.cvt(Math.abs(f), 105.7F, 110.4F, 2.0F, 3F);
                    if (f1 < f14) {
                        f1 = f14;
                        flag = false;
                    }
                } else if (f >= -146.2F && f <= 146.2F) {
                    if (f1 < 3F) {
                        f1 = 3F;
                        flag = false;
                    }
                } else {
                    if (f1 < 3F) {
                        f1 = 3F;
                        flag = false;
                    }
                    if (f < -146.2F) {
                        f = -146.2F;
                        flag = false;
                    } else if (f > 146.2F) {
                        f = 146.2F;
                        flag = false;
                    }
                }
                if (flag) flag = this.StopFireRearTurret(f, f1);
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.equals("xxftonrglass")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if (s.equals("xxcanopyleft")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            if (s.equals("xxcanopyright")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            if (s.equals("xxpanel")) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if (s.startsWith("xxcontrols")) {
                int i = s.charAt(10) - 48;
                switch (i) {
                    default:
                        break;

                    case 1:
                        if (World.Rnd().nextFloat() < 0.05F || shot.mass > 0.092F && World.Rnd().nextFloat() < 0.1F) {
                            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                        }
                        break;

                    case 2:
                        if (this.getEnergyPastArmor(1.0F, shot) <= 0.0F) break;
                        if (World.Rnd().nextFloat() < 0.12F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                        }
                        if (World.Rnd().nextFloat() < 0.12F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                        }
                        break;
                }
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxspart") && World.Rnd().nextFloat() < 0.36F && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(6.8F, shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    this.msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if (s.startsWith("xxsparli") && this.chunkDamageVisible("WingLIn") > 2 && this.getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && this.chunkDamageVisible("WingRIn") > 2 && this.getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && this.chunkDamageVisible("WingLMid") > 2 && this.getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && this.chunkDamageVisible("WingRMid") > 2 && this.getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLOut") > 2 && this.getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 2 && this.getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparsl") && this.chunkDamageVisible("StabL") > 1 && this.getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && this.chunkDamageVisible("StabR") > 1 && this.getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if (s.startsWith("xxspare1") && this.getEnergyPastArmor(28F, shot) > 0.0F) {
                    Aircraft.debugprintln(this, "*** Engine1 Suspension Broken in Half..");
                    this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
            }
            if (s.startsWith("xxeng")) {
                int j = s.charAt(5) - 49;
                if (s.endsWith("prop") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F) {
                    this.FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Failed..");
                }
                if (s.endsWith("gear") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F) {
                    this.FM.EI.engines[j].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Damaged..");
                }
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < shot.power / 200000F) {
                            this.FM.AS.setEngineStuck(shot.initiator, j);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 50000F) {
                            this.FM.AS.hitEngine(shot.initiator, j, 2);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Damaged..");
                        }
                        if (World.Rnd().nextFloat() < shot.power / 28000F) {
                            this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Cylinder Feed Out, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                        }
                        if (World.Rnd().nextFloat() < 0.08F) {
                            this.FM.EI.engines[j].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        this.FM.EI.engines[j].setReadyness(shot.initiator, this.FM.EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[j].getReadyness() + "..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[j].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stalled..");
                    }
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.AS.hitEngine(shot.initiator, j, 10);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    this.getEnergyPastArmor(6F, shot);
                }
                if ((s.endsWith("cyl1") || s.endsWith("cyl2")) && this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[j].getCylindersRatio() * 1.72F) {
                    this.FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit, " + this.FM.EI.engines[j].getCylindersOperable() + "/" + this.FM.EI.engines[j].getCylinders() + " Left..");
                    if (World.Rnd().nextFloat() < 0.01F) {
                        this.FM.EI.engines[j].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinder Case Broken - Engine Stuck..");
                    }
                    if (World.Rnd().nextFloat() < shot.power / 24000F) {
                        this.FM.AS.hitEngine(shot.initiator, j, 3);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit - Engine Fires..");
                    }
                    this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
                    this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Supercharger Out..");
                }
                if (s.endsWith("eqpt") && this.getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) {
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Magneto Out..");
                    }
                    if (World.Rnd().nextFloat() < 0.11F) {
                        this.FM.EI.engines[j].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Compressor Feed Out..");
                    }
                }
                return;
            }
            if (s.endsWith("oil1")) {
                if (World.Rnd().nextFloat() < 0.5F && this.getEnergyPastArmor(0.25F, shot) > 0.0F) this.debuggunnery("Engine Module: Oil Radiator Hit..");
                this.FM.AS.hitOil(shot.initiator, 0);
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if (k >= 2 && this.FM.M.maxFuel <= 502.5F) return;
                if (this.getEnergyPastArmor(0.4F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F) {
                    if (this.FM.AS.astateTankStates[k] == 0) this.FM.AS.hitTank(shot.initiator, k, 2);
                    if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitTank(shot.initiator, k, 2);
                }
                return;
            } else return;
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) this.hitChunk("CF", shot);
            if (shot.power > 33000F && point3d.x > 1.0D) {
                this.FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(30, 192));
                this.FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(30, 192));
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xrudder")) this.hitChunk("Rudder1", shot);
        else if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) this.hitChunk("StabR", shot);
        } else if (s.startsWith("xvatorl")) this.hitChunk("VatorL", shot);
        else if (s.startsWith("xvatorr")) this.hitChunk("VatorR", shot);
        else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) this.hitChunk("WingLIn", shot);
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) this.hitChunk("WingRIn", shot);
        } else if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) this.hitChunk("WingLMid", shot);
        } else if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) this.hitChunk("WingRMid", shot);
        } else if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) this.hitChunk("WingLOut", shot);
        } else if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) this.hitChunk("WingROut", shot);
        } else if (s.startsWith("xaronel")) this.hitChunk("AroneL", shot);
        else if (s.startsWith("xaroner")) this.hitChunk("AroneR", shot);
        else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
            this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 168000F));
            Aircraft.debugprintln(this, "*** Engine1 Hit - Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
        } else if (s.startsWith("xturret")) {
            if (s.startsWith("xturret1")) {
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
            }
            if (s.startsWith("xturret2")) this.FM.AS.setJamBullets(11, 0);
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else l = s.charAt(5) - 49;
            this.hitFlesh(l, shot, byte0);
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 75.0F) this.fSightCurForwardAngle = 75.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15.0F) this.fSightCurForwardAngle = -15.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 1.0F;
        if (this.fSightCurSideslip > 45.0F) this.fSightCurSideslip = 45.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 1.0F;
        if (this.fSightCurSideslip < -45.0F) this.fSightCurSideslip = -45.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300.0F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10.0F;
        if (this.fSightCurAltitude > 6000.0F) this.fSightCurAltitude = 6000.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10.0F;
        if (this.fSightCurAltitude < 300.0F) this.fSightCurAltitude = 300.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50.0F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5.0F;
        if (this.fSightCurSpeed > 650.0F) this.fSightCurSpeed = 650.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5.0F;
        if (this.fSightCurSpeed < 50.0F) this.fSightCurSpeed = 50.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netMsgGuaranted) throws IOException {
        netMsgGuaranted.writeFloat(this.fSightCurAltitude);
        netMsgGuaranted.writeFloat(this.fSightCurSpeed);
        netMsgGuaranted.writeFloat(this.fSightCurForwardAngle);
        netMsgGuaranted.writeFloat(this.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netMsgInput) throws IOException {
        this.fSightCurAltitude = netMsgInput.readFloat();
        this.fSightCurSpeed = netMsgInput.readFloat();
        this.fSightCurForwardAngle = netMsgInput.readFloat();
        this.fSightCurSideslip = netMsgInput.readFloat();
    }

    protected float curTurretPosition;
    public boolean  gunnerDead;
    public boolean  gunnerEjected;
//    private float gunnerAnimation;
    private long    tme0;
    public boolean  airstartr;
    public boolean  bRSArmed;
    public float    canopyF;
    public boolean  tiltCanopyOpened;
    private boolean slideCanopyOpened;
    public boolean  blisterRemoved;
    public boolean  bChangedPit;
    public float    tSAim;
    public float    kSAim;
    public float    fSightCurAltitude;
    public float    fSightCurSpeed;
    public float    fSightCurForwardAngle;
    public float    fSightSetForwardAngle;
    public float    fSightCurSideslip;
}
