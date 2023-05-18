package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class MV_22 extends Scheme2 implements TypeTransport {

    public MV_22() {
        this.bWingOff = false;
        this.APmode1 = false;
        this.APmode2 = false;
        this.counter = 0;
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
        this.error = 0;
        this.FL = false;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("Vator_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 130F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -130F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, 90F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, 90F * f);
    }

    protected void moveWing(float f) {
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 1.0F, 260F, 90F, 0.0F));
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 1.0F, 260F, 90F, 0.0F));
    }

    protected void WingOff() {
        if (!this.bWingOff) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, 0.0F, 0.0F);
            this.bWingOff = true;
            return;
        }
    }

    protected void moveGear(float f) {
        MV_22.moveGear(this.hierMesh(), f);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, 40F * f);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, 40F * f);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.startsWith("xxeng")) {
                int i = s.charAt(5) - 49;
                if (s.endsWith("gear")) {
                    if (this.getEnergyPastArmor(1.7F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat(20000F, 140000F) < shot.power) {
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                            this.debuggunnery("*** Engine" + i + " Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat(10000F, 50000F) < shot.power) {
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                            this.debuggunnery("*** Engine" + i + " Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.04F) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                        this.debuggunnery("*** Engine" + i + " Crank Case Hit - Readyness Reduced to " + this.FM.EI.engines[i].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("case")) {
                    if ((this.getEnergyPastArmor(0.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[i].getCylindersRatio() * 0.9878F))) {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        this.debuggunnery("*** Engine" + i + " Cylinders Hit, " + this.FM.EI.engines[i].getCylindersOperable() + "/" + this.FM.EI.engines[i].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            this.debuggunnery("*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.05F, shot) > 0.0F) {
                        this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    }
                    this.getEnergyPastArmor(2.0F, shot);
                }
                if (s.endsWith("oil1") && (this.getEnergyPastArmor(4.21F, shot) > 0.0F)) {
                    this.FM.AS.hitOil(shot.initiator, i);
                    this.getEnergyPastArmor(0.42F, shot);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int j = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (this.FM.AS.astateTankStates[j] == 0) {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100F) {
                            if ((this.FM.AS.astateTankStates[j] < 4) && (World.Rnd().nextFloat() < 0.21F)) {
                                this.FM.AS.hitTank(shot.initiator, j, 1);
                            }
                        } else {
                            this.FM.AS.hitTank(shot.initiator, j, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                        }
                    } else if (shot.power > 16100F) {
                        this.FM.AS.hitTank(shot.initiator, j, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                    }
                }
                return;
            } else {
                return;
            }
        }
        if (s.startsWith("xcf")) {
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xcn")) {
            if (this.chunkDamageVisible("CN") < 3) {
                this.hitChunk("CN", shot);
            }
        } else if (s.startsWith("xtail1")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
        } else if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
        } else if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) {
                this.hitChunk("Keel2", shot);
            }
        } else if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) {
                this.hitChunk("Rudder2", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (this.chunkDamageVisible("Stab") < 3) {
                this.hitChunk("Stab", shot);
            }
        } else if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("Vator") < 1) {
                this.hitChunk("Vator", shot);
            }
        } else if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", shot);
            }
        } else if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", shot);
            }
        } else if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 1) {
                this.hitChunk("AroneL", shot);
            }
        } else if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int k;
            if (s.endsWith("a") || s.endsWith("a2")) {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else if (s.endsWith("b") || s.endsWith("b2")) {
                byte0 = 2;
                k = s.charAt(6) - 49;
            } else {
                k = s.charAt(5) - 49;
            }
            this.hitFlesh(k, shot, byte0);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;
        }
    }

    public void update(float f) {
        super.update(f);
        this.computeVerticalThrust();
        this.computeOrizzontalThrust();
        this.computeOrizzontalThrust2();
        this.computeHovering();
        if (this.FM.getSpeed() > 0.0F) {
            this.moveWing(f);
            this.bWingOff = false;
        } else {
            this.WingOff();
        }
    }

    public void computeVerticalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        World.getPlayerAircraft();
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 260F, 1.0F, 0.0F);
        float f3 = Aircraft.cvt(this.FM.getAltitude(), 0.0F, 5000F, 1.0F, 0.4F);
        float f4 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && (f1 < 5F) && flag) {
            f4 = 1.25F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && (f1 > 5F) && flag) {
            f4 = 1.1F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5) && !flag) {
            f4 = 1.15F * f;
        }
        this.FM.producedAF.z += f4 * (10F * this.FM.M.mass) * (1.0F * f2) * (1.0F * f3);
    }

    public void computeOrizzontalThrust() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 260F, 1.0F, 0.0F);
        float f3 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && (this.FM.getSpeedKMH() > 1.0F) && !flag) {
            f3 = 0.14F * f;
        } else if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.2F) && (f1 > 25F) && flag) {
            f3 = 0.045F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void computeOrizzontalThrust2() {
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.getPowerOutput() > 0.0F) && (this.FM.getSpeedKMH() < 0.0F)) {
            f1 = 0.4F * f;
        }
        this.FM.producedAF.x += f1 * (10F * this.FM.M.mass);
    }

    public void computeHovering() {
        boolean flag = true;
        if (this.FM.isPlayers() && (this.FM instanceof RealFlightModel)) {
            flag = !((RealFlightModel) this.FM).RealMode;
        }
        float f = this.FM.EI.engines[0].getThrustOutput();
        float f1 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) * 3.6F;
        float f2 = Aircraft.cvt(f1, 0.0F, 100F, 1.0F, 0.0F);
        float f3 = 0.0F;
        if ((this.FM.EI.engines[0].getStage() > 5) && !flag && (f1 < 35F) && (this.FM.getSpeedKMH() > 15F) && this.FM.CT.StabilizerControl) {
            f3 = 0.13F * f;
        }
        this.FM.producedAF.x -= f3 * (10F * this.FM.M.mass) * (1.0F * f2);
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            if (!this.APmode1) {
                this.APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else if (this.APmode1) {
                this.APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        }
        if (i == 21) {
            if (!this.APmode2) {
                this.APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else if (this.APmode2) {
                this.APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
        }
        if (i == 27) {
            if (!this.ILS) {
                this.ILS = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS ON");
            } else {
                this.ILS = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS OFF");
            }
        }
        if (i == 28) {
            if (!this.FL) {
                this.FL = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL ON");
            } else {
                this.FL = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL OFF");
            }
        }
    }

    public void rareAction(float f, boolean flag) {
        if ((this.raretimer != Time.current()) && (this == World.getPlayerAircraft())) {
            this.counter++;
            if ((this.counter % 12) == 9) {
                this.InertialNavigation();
            }
        }
        super.rareAction(f, flag);
        this.raretimer = Time.current();
    }

    private boolean InertialNavigation() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if ((aircraft.getSpeed(vector3d) > 20D) && (aircraft.pos.getAbsPoint().z >= 150D) && (aircraft instanceof MV_22)) {
            this.pos.getAbs(point3d);
            if (Mission.cur() != null) {
                this.error++;
                if (this.error > 99) {
                    this.error = 1;
                }
            }
            int i = this.error;
            int j = i;
            Random random = new Random();
            int k = random.nextInt(100);
            if (k > 50) {
                i -= i * 2;
            }
            k = random.nextInt(100);
            if (k > 50) {
                j -= j * 2;
            }
            double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D / 10D;
            double d2 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D / 10D;
            char c = (char) (int) (65D + Math.floor(((d1 / 676D) - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char) (int) (65D + Math.floor(((d1 / 26D) - Math.floor(d1 / 26D)) * 26D));
            String s = "";
            if (d > 260D) {
                s = "" + c + c1;
            } else {
                s = "" + c1;
            }
            int l = (int) Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "INS: " + s + "-" + l);
        }
        return true;
    }

    public void setTimer(int i) {
        Random random = new Random();
        this.Timer1 = (float) (random.nextInt(i) * 0.1D);
        this.Timer2 = (float) (random.nextInt(i) * 0.1D);
    }

    public void resetTimer(float f) {
        this.Timer1 = f;
        this.Timer2 = f;
    }

    protected boolean bWingOff;
    public boolean    APmode1;
    public boolean    APmode2;
    public float      Timer1;
    public float      Timer2;
    private int       freq;
    private int       counter;
    private long      raretimer;
    private int       error;
    public boolean    ILS;
    public boolean    FL;

    static {
        Class class1 = MV_22.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Osprey");
        Property.set(class1, "meshName", "3DO/Plane/MV-22/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 2000F);
        Property.set(class1, "yearExpired", 2021F);
        Property.set(class1, "FlightModel", "FlightModels/MV22.fmd:MV22_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitV22_Osprey.class });
        Property.set(class1, "LOSElevation", 0.5265F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
    }
}
