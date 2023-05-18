package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class MeteorF4 extends Scheme2 implements TypeFighter, TypeBNZFighter {

    public MeteorF4() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.48F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.05F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout && this.hierMesh().isChunkVisible("Blister1_D0")) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;
        }
    }

    protected void moveGear(float f) {
        MeteorF4.moveGear(this.hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 110F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        float f1 = Math.max(-f * 1000F, -90F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        if (this.FM.CT.getGear() > 0.75F) {
            this.hierMesh().chunkSetAngles("GearC22_D0", 0.0F, -40F * f, 0.0F);
        }
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake03_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake04_D0", 0.0F, 90F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 2) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xxtank1")) {
            int i = s.charAt(6) - 49;
            if ((this.getEnergyPastArmor(0.4F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.99F)) {
                if (this.FM.AS.astateTankStates[i] == 0) {
                    Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                    this.FM.AS.hitTank(shot.initiator, i, 2);
                }
                if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.FM.AS.hitTank(shot.initiator, i, 2);
                    Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                }
            }
        } else if (s.startsWith("xxmgun")) {
            if (s.endsWith("01")) {
                Aircraft.debugprintln(this, "*** Cannon #1: Disabled..");
                this.FM.AS.setJamBullets(0, 0);
            }
            if (s.endsWith("02")) {
                Aircraft.debugprintln(this, "*** Cannon #2: Disabled..");
                this.FM.AS.setJamBullets(0, 1);
            }
            if (s.endsWith("03")) {
                Aircraft.debugprintln(this, "*** Cannon #3: Disabled..");
                this.FM.AS.setJamBullets(1, 0);
            }
            if (s.endsWith("04")) {
                Aircraft.debugprintln(this, "*** Cannon #4: Disabled..");
                this.FM.AS.setJamBullets(1, 1);
            }
            this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
        } else if (s.startsWith("xxcontrols")) {
            int j = s.charAt(10) - 48;
            switch (j) {
                case 1:
                    if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                        }
                        if (World.Rnd().nextFloat() < 0.25F) {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                        }
                    }
                    // fall through

                case 2:
                case 3:
                    if (this.getEnergyPastArmor(1.5F, shot) > 0.0F) {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                    }
                    break;
            }
        } else if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel")) {
            this.hitChunk("Keel1", shot);
        } else if (s.startsWith("xstabl")) {
            this.hitChunk("StabL", shot);
        } else if (s.startsWith("xstabr")) {
            this.hitChunk("StabR", shot);
        } else if (s.startsWith("xwing")) {
            if (s.endsWith("lin") && (this.chunkDamageVisible("WingLIn") < 2)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.endsWith("rin") && (this.chunkDamageVisible("WingRIn") < 2)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.endsWith("lmid") && (this.chunkDamageVisible("WingLMid") < 2)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.endsWith("rmid") && (this.chunkDamageVisible("WingRMid") < 2)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.endsWith("lout") && (this.chunkDamageVisible("WingLOut") < 2)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.endsWith("rout") && (this.chunkDamageVisible("WingROut") < 2)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xengine")) {
            int k = s.charAt(7) - 49;
            if ((point3d.x > 0.0D) && (point3d.x < 0.69699999999999995D)) {
                this.FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
            }
            if (World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass) {
                this.FM.AS.hitEngine(shot.initiator, k, 5);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else {
                l = s.charAt(5) - 49;
            }
            if (l == 2) {
                l = 1;
            }
            Aircraft.debugprintln(this, "*** hitFlesh..");
            this.hitFlesh(l, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 11:
                this.cutFM(17, j, actor);
                this.FM.cut(17, j, actor);
                this.cutFM(18, j, actor);
                this.FM.cut(18, j, actor);
                return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        if (this.FM.AS.isMaster()) {
            for (int i = 0; i < 2; i++) {
                if (this.curctl[i] == -1F) {
                    this.curctl[i] = this.oldctl[i] = this.FM.EI.engines[i].getControlThrottle();
                } else {
                    this.curctl[i] = this.FM.EI.engines[i].getControlThrottle();
                    if ((((this.curctl[i] - this.oldctl[i]) / f) > 3F) && (this.FM.EI.engines[i].getRPM() < 2400F) && (this.FM.EI.engines[i].getStage() == 6) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.FM.AS.hitEngine(this, i, 100);
                    }
                    if ((((this.curctl[i] - this.oldctl[i]) / f) < -3F) && (this.FM.EI.engines[i].getRPM() < 2400F) && (this.FM.EI.engines[i].getStage() == 6)) {
                        if ((World.Rnd().nextFloat() < 0.25F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                            this.FM.EI.engines[i].setEngineStops(this);
                        }
                        if ((World.Rnd().nextFloat() < 0.75F) && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
                            this.FM.EI.engines[i].setKillCompressor(this);
                        }
                    }
                    this.oldctl[i] = this.curctl[i];
                }
            }

            if (Config.isUSE_RENDER()) {
                if ((this.FM.EI.engines[0].getPowerOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() == 6)) {
                    if (this.FM.EI.engines[0].getPowerOutput() > 0.95F) {
                        this.FM.AS.setSootState(this, 0, 3);
                    } else {
                        this.FM.AS.setSootState(this, 0, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, 0, 0);
                }
                if ((this.FM.EI.engines[1].getPowerOutput() > 0.8F) && (this.FM.EI.engines[1].getStage() == 6)) {
                    if (this.FM.EI.engines[1].getPowerOutput() > 0.95F) {
                        this.FM.AS.setSootState(this, 1, 3);
                    } else {
                        this.FM.AS.setSootState(this, 1, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, 1, 0);
                }
            }
        }
        super.update(f);
    }

    private float oldctl[] = { -1F, -1F };
    private float curctl[] = { -1F, -1F };

    static {
        Class class1 = MeteorF4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Meteor");
        Property.set(class1, "meshName", "3DO/Plane/MeteorF4/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/MeteorF4.fmd:Meteors");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMeteor.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17" });
    }
}
