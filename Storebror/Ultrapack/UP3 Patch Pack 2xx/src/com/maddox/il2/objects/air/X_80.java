package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class X_80 extends Scheme1 implements TypeFighter, TypeBNZFighter, TypeFighterAceMaker {
// private float oldctl;
// private float curctl;
    public int   k14Mode;
    public int   k14WingspanType;
    public float k14Distance;

    public X_80() {
// this.oldctl = -1.0f;
// this.curctl = -1.0f;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200.0f;
    }

    public void rareAction(final float f, final boolean bool) {
        super.rareAction(f, bool);
        if (this.FM.getAltitude() < 3000.0f) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        ++this.k14Mode;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    // -------------------------------------------------------------------------------------------------------
    // TODO: skylla: gyro-gunsight distance HUD log (for details please see P_51D25NA.class):

    public void typeFighterAceMakerAdjDistancePlus() {
        this.adjustK14AceMakerDistance(+10.0f);
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.adjustK14AceMakerDistance(-10.0f);
    }

    private void adjustK14AceMakerDistance(float f) {
        this.k14Distance += f;
        if (this.k14Distance > 730.0f) {
            this.k14Distance = 730.0f;
        } else if (this.k14Distance < 180.0f) {
            this.k14Distance = 180.0f;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Distance: " + (int) (this.k14Distance) + "m");
    }
    /*
     * public void typeFighterAceMakerAdjDistancePlus() {
     * this.k14Distance += 10.0f;
     * if (this.k14Distance > 800.0f) {
     * this.k14Distance = 800.0f;
     * }
     * HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
     * }
     * 
     * public void typeFighterAceMakerAdjDistanceMinus() {
     * this.k14Distance -= 10.0f;
     * if (this.k14Distance < 200.0f) {
     * this.k14Distance = 200.0f;
     * }
     * HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
     * }
     */
    // -------------------------------------------------------------------------------------------------------

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        --this.k14WingspanType;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        ++this.k14WingspanType;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(final NetMsgGuaranted netMsgGuaranted) throws IOException {
        netMsgGuaranted.writeByte(this.k14Mode);
        netMsgGuaranted.writeByte(this.k14WingspanType);
        netMsgGuaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(final NetMsgInput netMsgInput) throws IOException {
        this.k14Mode = netMsgInput.readByte();
        this.k14WingspanType = netMsgInput.readByte();
        this.k14Distance = netMsgInput.readFloat();
    }

    public void doMurderPilot(final int n) {
        switch (n) {
            case 0: {
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
            }
        }
    }

    public static void moveGear(final HierMesh hierMesh, final float n) {
        final float max = Math.max(-n * 1500.0f, -90.0f);
        hierMesh.chunkSetAngles("GearC2_D0", 0.0f, -90.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearC7_D0", 0.0f, 0.0f, 0.0f);
        hierMesh.chunkSetAngles("GearC4_D0", 0.0f, max, 0.0f);
        hierMesh.chunkSetAngles("GearC5_D0", 0.0f, max, 0.0f);
        hierMesh.chunkSetAngles("GearC7_D0", 0.0f, 0.0f, 0.0f);
        hierMesh.chunkSetAngles("GearL2_D0", 0.0f, -80.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR2_D0", 0.0f, -80.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearL4_D0", 0.0f, -100.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearR4_D0", 0.0f, -100.0f * n, 0.0f);
        hierMesh.chunkSetAngles("GearL6_D0", 0.0f, max, 0.0f);
        hierMesh.chunkSetAngles("GearR6_D0", 0.0f, max, 0.0f);
    }

    protected void moveGear(final float n) {
        moveGear(this.hierMesh(), n);
    }

    public void moveWheelSink() {
        final float cvt = Aircraft.cvt(this.FM.Gears.gWheelSinking[2], 0.0f, 0.19075f, 0.0f, 1.0f);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075f * cvt;
        this.hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearC8_D0", 0.0f, -35.0f * cvt, 0.0f);
        this.hierMesh().chunkSetAngles("GearC9_D0", 0.0f, -70.0f * cvt, 0.0f);
    }

    protected void moveRudder(final float n) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0f, -30.0f * n, 0.0f);
        if (this.FM.CT.GearControl > 0.5f) {
            this.hierMesh().chunkSetAngles("GearC7_D0", 0.0f, -60.0f * n, 0.0f);
        }
    }

    protected void moveFlap(final float n) {
        final float n2 = -45.0f * n;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0f, n2, 0.0f);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0f, n2, 0.0f);
    }

    protected void moveFan(final float n) {
    }

    protected void hitBone(final String s, final Shot shot, final Point3d point3d) {
        if (!s.startsWith("xx")) {
            if (s.startsWith("xcockpit")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x1);
                this.getEnergyPastArmor(0.05f, shot);
            }
            if (s.startsWith("xcf")) {
                this.hitChunk("CF", shot);
            } else if (s.startsWith("xnose")) {
                this.hitChunk("Nose", shot);
            } else if (s.startsWith("xtail")) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else if (s.startsWith("xkeel")) {
                if (this.chunkDamageVisible("Keel1") < 2) {
                    this.hitChunk("Keel1", shot);
                }
            } else if (s.startsWith("xrudder")) {
                this.hitChunk("Rudder1", shot);
            } else if (s.startsWith("xstab")) {
                if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                    this.hitChunk("StabL", shot);
                }
                if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
                    this.hitChunk("StabR", shot);
                }
            } else if (s.startsWith("xvator")) {
                if (s.startsWith("xvatorl")) {
                    this.hitChunk("VatorL", shot);
                }
                if (s.startsWith("xvatorr")) {
                    this.hitChunk("VatorR", shot);
                }
            } else if (s.startsWith("xwing")) {
                if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                    this.hitChunk("WingLIn", shot);
                }
                if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                    this.hitChunk("WingRIn", shot);
                }
                if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                    this.hitChunk("WingLMid", shot);
                }
                if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                    this.hitChunk("WingRMid", shot);
                }
                if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                    this.hitChunk("WingLOut", shot);
                }
                if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                    this.hitChunk("WingROut", shot);
                }
            } else if (s.startsWith("xarone")) {
                if (s.startsWith("xaronel")) {
                    this.hitChunk("AroneL", shot);
                }
                if (s.startsWith("xaroner")) {
                    this.hitChunk("AroneR", shot);
                }
            } else if (s.startsWith("xgear")) {
                if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05f)) {
                    this.debuggunnery("Hydro System: Disabled..");
                    this.FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if (s.endsWith("2") && (World.Rnd().nextFloat() < 0.1f) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2f, 3.435f), shot) > 0.0f)) {
                    this.debuggunnery("Undercarriage: Stuck..");
                    this.FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
                int i_0_ = 0;
                char i;
                if (s.endsWith("a")) {
                    i_0_ = 1;
                    i = (char) (s.charAt(6) - '1');
                } else if (s.endsWith("b")) {
                    i_0_ = 2;
                    i = (char) (s.charAt(6) - '1');
                } else {
                    i = (char) (s.charAt(5) - '1');
                }
                this.hitFlesh(i, shot, i_0_);
            }
            return;
        }
        if (s.startsWith("xxarmor")) {
            this.debuggunnery("Armor: Hit..");
            if (s.endsWith("p1")) {
                this.getEnergyPastArmor(13.350000381469727 / (Math.abs(Aircraft.v1.x) + 9.999999747378752E-5), shot);
                if (shot.power <= 0.0f) {
                    this.doRicochetBack(shot);
                }
            } else if (s.endsWith("p2")) {
                this.getEnergyPastArmor(8.77f, shot);
            } else if (s.endsWith("g1")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(40.0f, 60.0f) / (Math.abs(Aircraft.v1.x) + 9.999999747378752E-5), shot);
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x2);
                if (shot.power <= 0.0f) {
                    this.doRicochetBack(shot);
                }
            }
            return;
        }
        if (s.startsWith("xxcontrols")) {
            this.debuggunnery("Controls: Hit..");
            switch (s.charAt(10) - '0') {
                case '\u0001':
                case '\u0002': {
                    if ((World.Rnd().nextFloat() < 0.5f) && (this.getEnergyPastArmor(1.1f, shot) > 0.0f)) {
                        this.debuggunnery("Controls: Ailerones Controls: Out..");
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        break;
                    }
                    break;
                }
                case '\u0003':
                case '\u0004': {
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.5f, 2.93f), shot) > 0.0f) && (World.Rnd().nextFloat() < 0.25f)) {
                        this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if ((this.getEnergyPastArmor(World.Rnd().nextFloat(0.5f, 2.93f), shot) > 0.0f) && (World.Rnd().nextFloat() < 0.25f)) {
                        this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        break;
                    }
                    break;
                }
            }
            return;
        }
        if (s.startsWith("xxeng1")) {
            this.debuggunnery("Engine Module: Hit..");
            if (s.endsWith("bloc")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0f, 60.0f) / (Math.abs(Aircraft.v1.x) + 9.999999747378752E-5), shot);
            }
            if (s.endsWith("cams") && (this.getEnergyPastArmor(0.45f, shot) > 0.0f) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 20.0f))) {
                this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800.0f)));
                this.debuggunnery("Engine Module: Engine Cams Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                if (World.Rnd().nextFloat() < (shot.power / 24000.0f)) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 2);
                    this.debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                }
                if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.75f)) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                }
            }
            if (s.endsWith("eqpt") && (World.Rnd().nextFloat() < (shot.power / 24000.0f))) {
                this.FM.AS.hitEngine(shot.initiator, 0, 3);
                this.debuggunnery("Engine Module: Hit - Engine Fires..");
            }
            if (s.endsWith("exht")) {
                return;
            }
        } else {
            if (s.startsWith("xxmgun0")) {
                final char paramInt2 = (char) (s.charAt(7) - '1');
                if (this.getEnergyPastArmor(1.5f, shot) > 0.0f) {
                    this.debuggunnery("Armament: Machine Gun (" + (int) paramInt2 + ") Disabled..");
                    this.FM.AS.setJamBullets(0, paramInt2);
                    this.getEnergyPastArmor(World.Rnd().nextFloat(0.5f, 23.325f), shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                final char paramInt2 = (char) (s.charAt(6) - '1');
                if ((this.getEnergyPastArmor(0.1f, shot) > 0.0f) && (World.Rnd().nextFloat() < 0.25f)) {
                    if (this.FM.AS.astateTankStates[paramInt2] == 0) {
                        this.debuggunnery("Fuel Tank (" + (int) paramInt2 + "): Pierced..");
                        this.FM.AS.hitTank(shot.initiator, paramInt2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, paramInt2, 1);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.075f)) {
                        this.FM.AS.hitTank(shot.initiator, paramInt2, 2);
                        this.debuggunnery("Fuel Tank (" + (int) paramInt2 + "): Hit..");
                    }
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(16.5f * World.Rnd().nextFloat(1.0f, 1.5f), shot) > 0.0f)) {
                    this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(16.5f * World.Rnd().nextFloat(1.0f, 1.5f), shot) > 0.0f)) {
                    this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(16.5f * World.Rnd().nextFloat(1.0f, 1.5f), shot) > 0.0f)) {
                    this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(16.5f * World.Rnd().nextFloat(1.0f, 1.5f), shot) > 0.0f)) {
                    this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxhyd")) {
                this.FM.AS.setInternalDamage(shot.initiator, 3);
                return;
            }
            if (s.startsWith("xxpnm")) {
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
        }
    }

    protected boolean cutFM(final int n, final int n2, final Actor engineDies) {
        switch (n) {
            case 19: {
                this.FM.EI.engines[0].setEngineDies(engineDies);
                return super.cutFM(n, n2, engineDies);
            }
            default: {
                return super.cutFM(n, n2, engineDies);
            }
        }
    }

    public void update(final float f) {
        if (Config.isUSE_RENDER() && this.FM.AS.isMaster()) {
            if ((this.FM.EI.engines[0].getPowerOutput() > 0.5f) && (this.FM.EI.engines[0].getStage() == 6)) {
                if (this.FM.EI.engines[0].getPowerOutput() > 0.75f) {
                    this.FM.AS.setSootState(this, 0, 5);
                } else {
                    this.FM.AS.setSootState(this, 0, 4);
                }
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
        super.update(f);
    }

    protected void moveAirBrake(final float n) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0f, -78.5f * n, 0.0f);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0f, -78.5f * n, 0.0f);
    }

    static {
        Property.set(X_80.class, "originCountry", PaintScheme.countryUSA);
    }
}
