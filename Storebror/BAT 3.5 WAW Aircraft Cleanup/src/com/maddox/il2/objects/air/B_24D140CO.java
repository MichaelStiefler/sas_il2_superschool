package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class B_24D140CO extends B_24 {

    public void registerPit(CockpitB24D cockpitb24d) {
        this.pit = cockpitb24d;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.hierMesh().chunkVisible("Turret4A_D0", false);
        this.hierMesh().chunkVisible("Turret4B_D0", false);
        this.hierMesh().chunkVisible("Turret5A_D0", false);
        this.hierMesh().chunkVisible("Turret5B_D0", false);
        this.hierMesh().chunkVisible("WindDL_D0", false);
        this.hierMesh().chunkVisible("WindDR_D0", false);
        super.FM.crew = 9;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[0] = 1;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[1] = 2;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[2] = 9;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[3] = 4;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[4] = 5;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[5] = 5;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[6] = 3;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[7] = 7;
        ((FlightModelMain) (super.FM)).AS.astatePilotFunctions[8] = 6;
    }

    public B_24D140CO() {
        this.suspension = 0.0F;
        this.suspR = 0.0F;
        this.suspL = 0.0F;
        this.suspC = 0.0F;
        this.bBallRemoved = false;
        this.topLeftGunJammed = false;
        this.topRightGunJammed = false;
        this.rearLeftGunJammed = false;
        this.rearRightGunJammed = false;
        this.fBallPos = 1.0F;
        this.iBallPos = 1.0F;
        this.btme = -1L;
        this.fRGunPos = 0.0F;
        this.iRGunPos = 0;
        this.fLGunPos = 0.0F;
        this.iLGunPos = 0;
        this.pit = null;
        this.slideRWindow = false;
        this.canopyF = 0.0F;
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 30F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        if ((f > this.canopyF) && (this.pit != null) && (this.canopyF == 0.0F)) {
            this.slideRWindow = this.pit.isViewRight();
        }
        Aircraft.xyz[1] = f * 0.33F;
        if (this.slideRWindow) {
            this.hierMesh().chunkSetLocate("WindowR_D0", Aircraft.xyz, Aircraft.ypr);
        } else {
            this.hierMesh().chunkSetLocate("WindowL_D0", Aircraft.xyz, Aircraft.ypr);
        }
        this.canopyF = f;
        if (this.canopyF < 0.01D) {
            this.canopyF = 0.0F;
        }
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 27: // '\033'
                this.killPilot(this, 8);
                this.hierMesh().chunkVisible("Turret3A_D0", false);
                this.hierMesh().chunkVisible("Turret3B_D0", false);
                this.hierMesh().chunkVisible("Turret3BAux_D0", false);
                this.doWreck("Turret3B_D0");
                this.fBallPos = 0.0F;
                this.iBallPos = 0.0F;
                this.bBallRemoved = true;
                break;

            case 19: // '\023'
                this.killPilot(this, 4);
                this.killPilot(this, 5);
                this.killPilot(this, 7);
                this.killPilot(this, 8);
                this.cut("StabL");
                this.cut("StabR");
                break;

            case 13: // '\r'
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                this.killPilot(this, 2);
                this.killPilot(this, 6);
                this.hierMesh().chunkVisible("Nose_Cap", true);
                this.hierMesh().chunkVisible("Nose_D0", false);
                this.hierMesh().chunkVisible("Nose_D1", false);
                this.hierMesh().chunkVisible("Nose_D2", false);
                this.hierMesh().chunkVisible("NoseAux_D0", false);
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("GearC2_D0", false);
                this.hierMesh().chunkVisible("GearC2a_D0", false);
                this.hierMesh().chunkVisible("GearC3_D0", false);
                this.hierMesh().chunkVisible("GearC1_D0", false);
                this.hierMesh().chunkVisible("GearC8_D0", false);
                this.hierMesh().chunkVisible("GearC9_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", false);
                this.hierMesh().chunkVisible("Pilot2_D1", false);
                this.hierMesh().chunkVisible("Pilot3_D1", false);
                this.hierMesh().chunkVisible("Pilot7_D1", false);
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Head3_D0", false);
                this.hierMesh().chunkVisible("Pilot7_D0", false);
                this.hierMesh().chunkVisible("Head7_D0", false);
                this.hierMesh().chunkVisible("WindowR_D0", false);
                this.hierMesh().chunkVisible("WindowL_D0", false);
                this.hierMesh().chunkVisible("FGun1A_D0", false);
                this.hierMesh().chunkVisible("FGun1B_D0", false);
                this.hierMesh().chunkVisible("Turret7A_D0", false);
                this.hierMesh().chunkVisible("Turret7B_D0", false);
                this.hierMesh().chunkVisible("Turret1A_D0", false);
                this.hierMesh().chunkVisible("Turret1B_D0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void doWreck(String s) {
        if (this.hierMesh().chunkFindCheck(s) != -1) {
            this.hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(((FlightModelMain) (super.FM)).Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0: // '\0'
                if (f < -5F) {
                    f = -5F;
                    flag = false;
                }
                if (f > 50F) {
                    f = 50F;
                    flag = false;
                }
                if (f1 < -40F) {
                    f1 = -40F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;

            case 1: // '\001'
                float f2 = -6.5F;
                if (f1 < -6.5F) {
                    f1 = -6.5F;
                    flag = false;
                }
                if (f1 > 85F) {
                    f1 = 85F;
                    flag = false;
                }
                if (f1 < 7.5F) {
                    if ((f > -21F) && (f < -14F)) {
                        flag = false;
                    }
                    if ((f < 21F) && (f > 14F)) {
                        flag = false;
                    }
                }
                if ((f < 14F) && (f > -14F)) {
                    f2 = -0.9F;
                }
                if ((f < 23F) && (f > -23F)) {
                    f2 = -5F;
                }
                if ((f > 53F) && (f < 81F)) {
                    f2 = Aircraft.cvt(f, 53F, 81F, -6.5F, 0.4F);
                }
                if ((f > 81F) && (f < 100F)) {
                    f2 = Aircraft.cvt(f, 93F, 100F, 0.3F, -6.5F);
                }
                if ((f < -53F) && (f > -81F)) {
                    f2 = Aircraft.cvt(f, -81F, -53F, 0.4F, -6.5F);
                }
                if ((f < -81F) && (f > -100F)) {
                    f2 = Aircraft.cvt(f, -100F, -93F, -6.5F, 0.3F);
                }
                if (f1 < f2) {
                    f1 = f2;
                    flag = false;
                }
                break;

            case 2: // '\002'
                if (this.fBallPos < 0.98999999999999999D) {
                    float f3 = Aircraft.cvt(this.fBallPos, 0.5F, 1.0F, 180F, 0.0F);
                    float f6 = Aircraft.cvt(this.fBallPos, 0.5F, 1.0F, -90F, -22F);
                    if (f > f3) {
                        f = f3;
                    }
                    if (f < -f3) {
                        f = -f3;
                    }
                    if (f1 < f6) {
                        f1 = f6;
                    }
                    if (f1 > f6) {
                        f1 = f6;
                    }
                    flag = false;
                    break;
                }
                if (f1 < -89F) {
                    f1 = -89F;
                    flag = false;
                }
                if (f1 > 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                break;

            case 3: // '\003'
                if (f < -66F) {
                    f = -66F;
                    flag = false;
                }
                if (f > 38F) {
                    f = 38F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 40F) {
                    f1 = 40F;
                    flag = false;
                }
                if (this.fLGunPos < 0.9F) {
                    flag = false;
                }
                float f4 = -45F;
                float f7 = 40F;
                if (f > 18F) {
                    f4 = Aircraft.cvt(f, 18F, 39F, 4F, 5.5F);
                    f7 = Aircraft.cvt(f, 18F, 39F, 2.5F, -2.5F);
                }
                if (f < -43F) {
                    if (f > -53F) {
                        f4 = Aircraft.cvt(f, -53F, -43F, 27F, 24F);
                        f7 = Aircraft.cvt(f, -53F, -43F, -10F, -6.5F);
                    } else if ((f <= -53F) && (f > -63F)) {
                        f4 = Aircraft.cvt(f, -63F, -57F, 15F, 27F);
                        f7 = Aircraft.cvt(f, -63F, -57F, 0.0F, -10F);
                    } else {
                        f4 = 12.5F;
                        f7 = 2.0F;
                    }
                }
                if ((f1 < f4) && (f1 > f7)) {
                    flag = false;
                }
                break;

            case 4: // '\004'
                if (f < -39F) {
                    f = -39F;
                    flag = false;
                }
                if (f > 66F) {
                    f = 66F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 40F) {
                    f1 = 40F;
                    flag = false;
                }
                if (this.fRGunPos < 0.9F) {
                    flag = false;
                }
                float f5 = -45F;
                float f8 = 40F;
                if (f < -18F) {
                    f5 = Aircraft.cvt(f, -39F, -18F, 5.5F, 4F);
                    f8 = Aircraft.cvt(f, -39F, -18F, -2.5F, 2.5F);
                }
                if (f > 43F) {
                    if (f < 53F) {
                        f5 = Aircraft.cvt(f, 43F, 53F, 24F, 27F);
                        f8 = Aircraft.cvt(f, 43F, 53F, -6.5F, -10F);
                    } else if ((f >= 53F) && (f < 63F)) {
                        f5 = Aircraft.cvt(f, 57F, 63F, 27F, 15F);
                        f8 = Aircraft.cvt(f, 57F, 63F, -10F, 0.0F);
                    } else {
                        f5 = 12.5F;
                        f8 = 2.0F;
                    }
                }
                if ((f1 < f5) && (f1 > f8)) {
                    flag = false;
                }
                break;

            case 5: // '\005'
                if (f < -60F) {
                    f = -60F;
                    flag = false;
                }
                if (f > 60F) {
                    f = 60F;
                    flag = false;
                }
                if (f1 < -29F) {
                    f1 = -29F;
                    flag = false;
                }
                if (f1 > 71F) {
                    f1 = 71F;
                    flag = false;
                }
                break;

            case 6: // '\006'
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 0.0F) {
                    f = 0.0F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void update(float f) {
        super.update(f);
        if (((FlightModelMain) (super.FM)).CT.getGear() > 0.9F) {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            Aircraft.xyz[1] = Aircraft.cvt(this.suspC, 0.0F, 0.25F, 0.0F, 0.25F);
            Aircraft.ypr[1] = Reflection.getFloat(this, "fCSteer");//super.fCSteer;
            this.hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (!((FlightModelMain) (super.FM)).AS.isMaster()) {
            return;
        }
        if (this.iBallPos == 0.0F) {
            if (this.fBallPos > this.iBallPos) {
                this.fBallPos -= 0.15F * f;
                if (this.fBallPos < 0.0F) {
                    this.fBallPos = 0.0F;
                }
            }
            this.resetYPRmodifier();
            Aircraft.xyz[2] = -0.71F * this.fBallPos;
            this.hierMesh().chunkSetLocate("Turret3C_D0", Aircraft.xyz, Aircraft.ypr);
        } else if (this.iBallPos == 1.0F) {
            if (this.fBallPos < this.iBallPos) {
                this.fBallPos += 0.15F * f;
                if (this.fBallPos > 1.0F) {
                    this.fBallPos = 1.0F;
                    this.iBallPos = 0.5F;
                    super.FM.turret[2].bIsOperable = true;
                }
            }
            this.resetYPRmodifier();
            Aircraft.xyz[2] = -0.7F * this.fBallPos;
            this.hierMesh().chunkSetLocate("Turret3C_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (Time.current() > this.btme) {
            this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
            if ((super.FM.turret[1].target == null) && (super.FM.turret[2].target == null) && (super.FM.turret[3].target == null) && (super.FM.turret[4].target == null) && (super.FM.turret[5].target == null)) {
                super.FM.turret[2].bIsOperable = false;
                this.iBallPos = 0.0F;
            }
            if ((super.FM.turret[1].target != null) && (((FlightModelMain) (super.FM)).AS.astatePilotStates[8] < 90) && (this.iBallPos == 0.0F)) {
                this.iBallPos = 1.0F;
            }
        }
        if (this.iLGunPos == 0) {
            if (this.fLGunPos > 0.0F) {
                this.fLGunPos -= 0.2F * f;
                this.hierMesh().chunkVisible("Turret4A_D0", false);
                this.hierMesh().chunkVisible("Turret4B_D0", false);
                this.hierMesh().chunkVisible("WindDL_D0", false);
            }
        } else if ((this.iLGunPos == 1) && (this.fLGunPos < 1.0F)) {
            this.fLGunPos += 0.2F * f;
            if ((this.fLGunPos > 0.8F) && (this.fLGunPos < 0.9F)) {
                this.hierMesh().chunkVisible("Turret4A_D0", true);
                this.hierMesh().chunkVisible("Turret4B_D0", true);
                this.hierMesh().chunkVisible("WindDL_D0", true);
            }
        }
        this.hierMesh().chunkSetAngles("HatchDoorL_D0", 0.0F, Aircraft.cvt(1.0F - this.fLGunPos, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        if (super.FM.turret[3].bIsAIControlled) {
            if ((super.FM.turret[3].target != null) && (((FlightModelMain) (super.FM)).AS.astatePilotStates[4] < 90)) {
                this.iLGunPos = 1;
                super.FM.turret[3].bIsOperable = true;
            }
        } else if (super.FM.turret[3].bIsOperable) {
            this.iLGunPos = 1;
        } else {
            this.iLGunPos = 0;
        }
        if (this.iRGunPos == 0) {
            if (this.fRGunPos > 0.0F) {
                this.fRGunPos -= 0.2F * f;
                this.hierMesh().chunkVisible("Turret5A_D0", false);
                this.hierMesh().chunkVisible("Turret5B_D0", false);
                this.hierMesh().chunkVisible("WindDR_D0", false);
            }
        } else if ((this.iRGunPos == 1) && (this.fRGunPos < 1.0F)) {
            this.fRGunPos += 0.2F * f;
            if ((this.fRGunPos > 0.8F) && (this.fRGunPos < 0.9F)) {
                this.hierMesh().chunkVisible("Turret5A_D0", true);
                this.hierMesh().chunkVisible("Turret5B_D0", true);
                this.hierMesh().chunkVisible("WindDR_D0", true);
            }
        }
        this.hierMesh().chunkSetAngles("HatchDoorR_D0", 0.0F, Aircraft.cvt(1.0F - this.fRGunPos, 0.4F, 0.54F, 0.0F, 135F), 0.0F);
        if (super.FM.turret[4].bIsAIControlled) {
            if ((super.FM.turret[4].target != null) && (((FlightModelMain) (super.FM)).AS.astatePilotStates[5] < 90)) {
                this.iRGunPos = 1;
                super.FM.turret[4].bIsOperable = true;
            }
        } else if (super.FM.turret[4].bIsOperable) {
            this.iRGunPos = 1;
        } else {
            this.iRGunPos = 0;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        int k1 = 0;
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                if (s.endsWith("01")) {
                    this.getEnergyPastArmor(12.699999999999999D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("02")) {
                    this.getEnergyPastArmor(12.699999999999999D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("03")) {
                    this.getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("04")) {
                    this.getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("05")) {
                    this.getEnergyPastArmor(0.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999999747524271E-007D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x80);
                } else if (s.endsWith("06")) {
                    this.getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("07")) {
                    this.getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("08")) {
                    this.getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("09")) {
                    this.getEnergyPastArmor(6.3499999046325684D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("10")) {
                    this.getEnergyPastArmor(22D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
                } else if (s.endsWith("11")) {
                    if (this.getEnergyPastArmor(52D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot) > 0.0F) {
                        ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 8);
                    }
                } else if (s.endsWith("12")) {
                    this.getEnergyPastArmor(52D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("13")) {
                    this.getEnergyPastArmor(6.3499999999999996D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("14")) {
                    this.getEnergyPastArmor(7.9375D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("15")) {
                    this.getEnergyPastArmor(9.5250000000000004D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
                } else if (s.endsWith("16")) {
                    this.getEnergyPastArmor(9.5250000000000004D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
                } else if (s.endsWith("17")) {
                    this.getEnergyPastArmor(15.875D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("18")) {
                    this.getEnergyPastArmor(9.5250000000000004D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("19")) {
                    this.getEnergyPastArmor(9.5250000000000004D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("20")) {
                    this.getEnergyPastArmor(51D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("21")) {
                    this.getEnergyPastArmor(22D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("22")) {
                    this.getEnergyPastArmor(52D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("23")) {
                    this.getEnergyPastArmor(9.5250000000000004D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                } else if (s.endsWith("24")) {
                    this.getEnergyPastArmor(9.5250000000000004D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                }
            }
            if (s.equals("xxfrontwindow")) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
            }
            if (s.equals("xxleftwindow")) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
            }
            if (s.equals("xxrightwindow")) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
            }
            if (s.equals("xxtopwindow")) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
            }
            if (s.equals("xxpanelleft")) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
            }
            if (s.equals("xxpanelright")) {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
            }
            if (s.startsWith("xxammo")) {
                int i = s.charAt(6) - 48;
                if (s.length() == 8) {
                    i = 10 + (s.charAt(7) - 48);
                }
                if ((this.getEnergyPastArmor(6.87F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.05F)) {
                    switch (i) {
                        case 1: // '\001'
                            i = 1;
                            k1 = 0;
                            break;

                        case 2: // '\002'
                            return;

                        case 3: // '\003'
                            i = 11;
                            k1 = 0;
                            this.topLeftGunJammed = true;
                            break;

                        case 4: // '\004'
                            i = 11;
                            k1 = 1;
                            this.topRightGunJammed = true;
                            break;

                        case 5: // '\005'
                            i = 12;
                            k1 = 0;
                            break;

                        case 6: // '\006'
                            i = 12;
                            k1 = 1;
                            break;

                        case 7: // '\007'
                            i = 14;
                            k1 = 0;
                            break;

                        case 8: // '\b'
                            i = 13;
                            k1 = 0;
                            break;

                        case 9: // '\t'
                            i = 10;
                            k1 = 0;
                            this.rearLeftGunJammed = true;
                            break;

                        case 10: // '\n'
                            i = 10;
                            k1 = 1;
                            this.rearRightGunJammed = true;
                            break;

                        case 11: // '\013'
                            i = 15;
                            k1 = 0;
                            break;

                        case 12: // '\f'
                            i = 16;
                            k1 = 0;
                            break;
                    }
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(i, k1);
                    return;
                }
            }
            if (s.startsWith("xxcontrols")) {
                int j = s.charAt(10) - 48;
                if (s.length() == 12) {
                    j = 10 + (s.charAt(11) - 48);
                }
                switch (j) {
                    default:
                        break;

                    case 1: // '\001'
                    case 2: // '\002'
                    case 11: // '\013'
                    case 12: // '\f'
                        if ((this.getEnergyPastArmor(5F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                        }
                        break;

                    case 3: // '\003'
                    case 4: // '\004'
                    case 5: // '\005'
                    case 6: // '\006'
                        if ((World.Rnd().nextFloat() < 0.252F) && (this.getEnergyPastArmor(5.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                        break;

                    case 7: // '\007'
                    case 8: // '\b'
                    case 9: // '\t'
                    case 10: // '\n'
                        if ((World.Rnd().nextFloat() < 0.252F) && (this.getEnergyPastArmor(5.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                        }
                        this.getEnergyPastArmor(2.0F, shot);
                        break;
                }
                return;
            }
            if (s.startsWith("xxeng")) {
                int k = s.charAt(5) - 49;
                if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 140000F)) {
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, k);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Stucks..");
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 85000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else if (World.Rnd().nextFloat() < 0.005F) {
                        ((FlightModelMain) (super.FM)).EI.engines[k].setCyliderKnockOut(shot.initiator, 1);
                    } else {
                        ((FlightModelMain) (super.FM)).EI.engines[k].setReadyness(shot.initiator, ((FlightModelMain) (super.FM)).EI.engines[k].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Readyness Reduced to " + ((FlightModelMain) (super.FM)).EI.engines[k].getReadyness() + "..");
                    }
                    this.getEnergyPastArmor(12F, shot);
                }
                if (s.endsWith("cyls")) {
                    if ((this.getEnergyPastArmor(5.85F, shot) > 0.0F) && (World.Rnd().nextFloat() < (((FlightModelMain) (super.FM)).EI.engines[k].getCylindersRatio() * 0.75F))) {
                        ((FlightModelMain) (super.FM)).EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit, " + ((FlightModelMain) (super.FM)).EI.engines[k].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[k].getCylinders() + " Left..");
                        if (World.Rnd().nextFloat() < (shot.power / 18000F)) {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag1")) {
                    ((FlightModelMain) (super.FM)).EI.engines[k].setMagnetoKnockOut(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Magneto #0 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("mag2")) {
                    ((FlightModelMain) (super.FM)).EI.engines[k].setMagnetoKnockOut(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Magneto #1 Destroyed..");
                    this.getEnergyPastArmor(25F, shot);
                }
                if (s.endsWith("oil1") && (this.getEnergyPastArmor(0.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                    ((FlightModelMain) (super.FM)).AS.setOilState(shot.initiator, k, 1);
                    Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Oil Filter Pierced..");
                }
                return;
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr1") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockr2") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder2_D" + this.chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxoil")) {
                int l = s.charAt(5) - 49;
                if ((this.getEnergyPastArmor(0.21F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.2435F)) {
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, l);
                }
                Aircraft.debugprintln(this, "*** Engine (" + l + ") Module: Oil Tank Pierced..");
                return;
            }
            if (s.startsWith("xxpnm")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F) {
                    this.debuggunnery("Pneumo System: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
                }
                return;
            }
            if (s.startsWith("xxradio")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 25F), shot);
                return;
            }
            if (s.startsWith("xxautopilot")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 4F), shot) > 0.0F) {
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                if (s.startsWith("xxsparli") && (this.chunkDamageVisible("WingLIn") > 2) && (this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparri") && (this.chunkDamageVisible("WingRIn") > 2) && (this.getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlm") && (this.chunkDamageVisible("WingLMid") > 2) && (this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparrm") && (this.chunkDamageVisible("WingRMid") > 2) && (this.getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if (s.startsWith("xxsparlo") && (this.chunkDamageVisible("WingLOut") > 2) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if (s.startsWith("xxsparro") && (this.chunkDamageVisible("WingROut") > 2) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.125F)) {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if (s.startsWith("xxspark1") && (this.chunkDamageVisible("Keel1") > 1) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel1_D" + this.chunkDamageVisible("Keel1"), shot.initiator);
                }
                if (s.startsWith("xxspark2") && (this.chunkDamageVisible("Keel2") > 1) && (this.getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    Aircraft.debugprintln(this, "*** Keel2 Spars Damaged..");
                    this.nextDMGLevels(1, 2, "Keel2_D" + this.chunkDamageVisible("Keel2"), shot.initiator);
                }
                if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabL Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)) {
                    this.debuggunnery("*** StabR Spars Damaged..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i1 = s.charAt(6) - 49;
                if (this.getEnergyPastArmor(0.06F, shot) > 0.0F) {
                    if (((FlightModelMain) (super.FM)).AS.astateTankStates[i1] == 0) {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i1, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, i1, 1);
                    }
                    if (shot.powerType == 3) {
                        if (shot.power < 16100F) {
                            if ((((FlightModelMain) (super.FM)).AS.astateTankStates[i1] < 4) && (World.Rnd().nextFloat() < 0.21F)) {
                                ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i1, 1);
                            }
                        } else {
                            ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
                        }
                    } else if (shot.power > 16100F) {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i1, World.Rnd().nextInt(1, 1 + (int) (shot.power / 16100F)));
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
            return;
        }
        if (s.startsWith("xtail")) {
            if (this.chunkDamageVisible("Tail1") < 3) {
                this.hitChunk("Tail1", shot);
            }
            return;
        }
        if (s.startsWith("xkeel1")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
            }
            return;
        }
        if (s.startsWith("xkeel2")) {
            if (this.chunkDamageVisible("Keel2") < 2) {
                this.hitChunk("Keel2", shot);
            }
            return;
        }
        if (s.startsWith("xrudder1")) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
            return;
        }
        if (s.startsWith("xrudder2")) {
            if (this.chunkDamageVisible("Rudder2") < 1) {
                this.hitChunk("Rudder2", shot);
            }
            return;
        }
        if (s.startsWith("xvatorl")) {
            if (this.chunkDamageVisible("VatorL") < 2) {
                this.hitChunk("VatorL", shot);
            }
            return;
        }
        if (s.startsWith("xvatorr")) {
            if (this.chunkDamageVisible("VatorR") < 2) {
                this.hitChunk("VatorR", shot);
            }
            return;
        }
        if (s.startsWith("xstabl")) {
            if (this.chunkDamageVisible("StabL") < 2) {
                this.hitChunk("StabL", shot);
            }
            return;
        }
        if (s.startsWith("xstabr")) {
            if (this.chunkDamageVisible("StabR") < 2) {
                this.hitChunk("StabR", shot);
            }
            return;
        }
        if (s.startsWith("xwinglin")) {
            if (this.chunkDamageVisible("WingLIn") < 3) {
                this.hitChunk("WingLIn", shot);
            }
            return;
        }
        if (s.startsWith("xwingrin")) {
            if (this.chunkDamageVisible("WingRIn") < 3) {
                this.hitChunk("WingRIn", shot);
            }
            return;
        }
        if (s.startsWith("xwinglmid")) {
            if (this.chunkDamageVisible("WingLMid") < 3) {
                this.hitChunk("WingLMid", shot);
            }
            return;
        }
        if (s.startsWith("xwingrmid")) {
            if (this.chunkDamageVisible("WingRMid") < 3) {
                this.hitChunk("WingRMid", shot);
            }
            return;
        }
        if (s.startsWith("xwinglout")) {
            if (this.chunkDamageVisible("WingLOut") < 3) {
                this.hitChunk("WingLOut", shot);
            }
            return;
        }
        if (s.startsWith("xwingrout")) {
            if (this.chunkDamageVisible("WingROut") < 3) {
                this.hitChunk("WingROut", shot);
            }
            return;
        }
        if (s.startsWith("xaronel")) {
            if (this.chunkDamageVisible("AroneL") < 1) {
                this.hitChunk("AroneL", shot);
            }
            return;
        }
        if (s.startsWith("xaroner")) {
            if (this.chunkDamageVisible("AroneR") < 1) {
                this.hitChunk("AroneR", shot);
            }
            return;
        }
        if (s.startsWith("xengine1")) {
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
            return;
        }
        if (s.startsWith("xengine2")) {
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
            return;
        }
        if (s.startsWith("xengine3")) {
            if (this.chunkDamageVisible("Engine3") < 2) {
                this.hitChunk("Engine3", shot);
            }
            return;
        }
        if (s.startsWith("xengine4")) {
            if (this.chunkDamageVisible("Engine4") < 2) {
                this.hitChunk("Engine4", shot);
            }
            return;
        }
        if (s.startsWith("xgear")) {
            if (World.Rnd().nextFloat() < 0.05F) {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                ((FlightModelMain) (super.FM)).Gears.setHydroOperable(false);
            }
            return;
        }
        if (s.startsWith("xturret")) {
            return;
        }
        if (s.startsWith("xmgun")) {
            int j1 = (10 * (s.charAt(5) - 48)) + (s.charAt(6) - 48);
            if ((this.getEnergyPastArmor(6.45F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.35F)) {
                switch (j1) {
                    case 1: // '\001'
                        j1 = 1;
                        k1 = 0;
                        break;

                    case 2: // '\002'
                        return;

                    case 3: // '\003'
                        j1 = 11;
                        k1 = 0;
                        this.topLeftGunJammed = true;
                        break;

                    case 4: // '\004'
                        j1 = 11;
                        k1 = 1;
                        this.topRightGunJammed = true;
                        break;

                    case 5: // '\005'
                        j1 = 12;
                        k1 = 0;
                        break;

                    case 6: // '\006'
                        j1 = 12;
                        k1 = 1;
                        break;

                    case 7: // '\007'
                        j1 = 14;
                        k1 = 0;
                        break;

                    case 8: // '\b'
                        j1 = 13;
                        k1 = 0;
                        break;

                    case 9: // '\t'
                        j1 = 10;
                        k1 = 0;
                        this.rearLeftGunJammed = true;
                        break;

                    case 10: // '\n'
                        j1 = 10;
                        k1 = 1;
                        this.rearRightGunJammed = true;
                        break;

                    case 11: // '\013'
                        j1 = 15;
                        k1 = 0;
                        break;

                    case 12: // '\f'
                        j1 = 16;
                        k1 = 0;
                        break;
                }
                ((FlightModelMain) (super.FM)).AS.setJamBullets(j1, k1);
            }
            return;
        }
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l1;
            if (s.endsWith("a")) {
                byte0 = 1;
                l1 = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l1 = s.charAt(6) - 49;
            } else {
                l1 = s.charAt(5) - 49;
            }
            this.hitFlesh(l1, shot, byte0);
            return;
        } else {
            return;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -55F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(f, 0.05F, 0.75F, 0.0F, -60F), 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -140F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.12F, 0.99F, 0.0F, -20F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, 180F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -98F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.02F, 0.82F, 0.0F, -20F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveWheelSink() {
        if (((FlightModelMain) (super.FM)).Gears.onGround()) {
            this.suspension += 0.008F;
        } else {
            this.suspension -= 0.008F;
        }
        if (this.suspension < 0.0F) {
            this.suspension = 0.0F;
            if (!super.FM.isPlayers()) {
                ((FlightModelMain) (super.FM)).Gears.bTailwheelLocked = true;
            }
        }
        if (this.suspension > 0.1F) {
            this.suspension = 0.1F;
        }
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        float f = Aircraft.cvt(super.FM.getSpeed(), 0.0F, 35F, 0.0F, 1.0F);
        this.suspL = (((FlightModelMain) (super.FM)).Gears.gWheelSinking[0] * f) + this.suspension;
        Aircraft.xyz[1] = Aircraft.cvt(this.suspL, 0.0F, 0.25F, 0.0F, 0.15F);
        this.hierMesh().chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        this.suspR = (((FlightModelMain) (super.FM)).Gears.gWheelSinking[1] * f) + this.suspension;
        Aircraft.xyz[1] = Aircraft.cvt(this.suspR, 0.0F, 0.25F, 0.0F, 0.15F);
        this.hierMesh().chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        this.suspC = (((FlightModelMain) (super.FM)).Gears.gWheelSinking[2] * f) + this.suspension;
    }

    public float getBombSightPDI() {
        return 0.0F;
    }

    private float         suspension;
    public float          suspR;
    public float          suspL;
    public float          suspC;
    public static boolean bChangedPit = false;
    public boolean        bBallRemoved;
    protected boolean     topLeftGunJammed;
    protected boolean     topRightGunJammed;
    protected boolean     rearLeftGunJammed;
    protected boolean     rearRightGunJammed;
    public float          fRGunPos;
    public int            iRGunPos;
    public float          fLGunPos;
    public int            iLGunPos;
    public float          fBallPos;
    public float          iBallPos;
    public long           btme;
    private CockpitB24D   pit;
    private boolean       slideRWindow;
    public float          canopyF;
    public float          fSightCurForwardAngle;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public boolean        bSightClutch;
    public float          fSightCurSideslip;
    public float          fSightHeadTurn;

    static {
        Class class1 = B_24D140CO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-24");
        Property.set(class1, "meshName", "3DO/Plane/B-24D-140-CO/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/B-24D-140-CO/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/B-24J.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB24D.class, CockpitB24D_Bombardier.class, CockpitB24D_FLGunner.class, CockpitB24D_FRGunner.class, CockpitB24D_TGunner.class, CockpitB24D_RGunner.class, CockpitB24D_LGunner.class, CockpitB24D_BGunner.class, CockpitB24D_AGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 10, 11, 11, 12, 12, 13, 14, 15, 15, 16, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN12", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN10", "_MGUN09", "_MGUN03", "_MGUN04", "_MGUN11", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24" });
    }
}
