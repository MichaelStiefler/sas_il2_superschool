package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class HE_111H12 extends HE_111 implements TypeX4Carrier, TypeGuidedBombCarrier {

    public HE_111H12() {
        this.slider = false;
        this.sliderX = 0.0F;
        this.sliderZ = 0.0F;
        this.pilot2kill = false;
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.isGuidingBomb = false;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 2) {
                this.hitChunk("Nose", shot);
            }
            if (shot.power > 200000F) {
                this.FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(3, 192));
                this.FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(3, 192));
            }
            if (World.Rnd().nextFloat() < 0.1F) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            if (point3d.x > 4.505000114440918D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            } else if (point3d.y > 0.0D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                if (World.Rnd().nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                }
            } else {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if (World.Rnd().nextFloat() < 0.1F) {
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                }
            }
        } else if (s.equals("xxarmorg1")) {
            this.getEnergyPastArmor(5F, shot);
        } else if (s.equals("xxarmoro1")) {
            this.getEnergyPastArmor(8F, shot);
        } else if (s.equals("xxarmoro2")) {
            this.getEnergyPastArmor(8F, shot);
        } else if (s.equals("xoil1") && (shot.power > 0.0F)) {
            this.FM.AS.hitOil(shot.initiator, 0);
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.equals("xoil2") && (shot.power > 0.0F)) {
            this.FM.AS.hitOil(shot.initiator, 1);
            if (this.chunkDamageVisible("Engine2") < 2) {
                this.hitChunk("Engine2", shot);
            }
        } else if (s.startsWith("xxoil")) {
            int i = 0;
            if (s.endsWith("2")) {
                i = 1;
            }
            if ((this.getEnergyPastArmor(0.21F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.49F)) {
                this.FM.AS.hitOil(shot.initiator, i);
                this.getEnergyPastArmor(0.42F, shot);
            }
        } else {
            super.hitBone(s, shot, point3d);
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        switch (i) {
            case 1:
                this.hierMesh().chunkVisible("Pilot1_FAK", false);
                this.hierMesh().chunkVisible("Head1_FAK", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot2_FAK", false);
                break;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.crew = 3;
        this.FM.AS.astatePilotFunctions[0] = 1;
        this.FM.AS.astatePilotFunctions[1] = 9;
        this.FM.AS.astatePilotFunctions[2] = 4;
        this.FM.CT.bHasBayDoorControl = false;
    }

    public void rareAction(float f, boolean flag) {
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3) {
                if (World.Rnd().nextFloat() < 0.05F) {
                    this.FM.AS.hitTank(this, 0, 1);
                }
                if (World.Rnd().nextFloat() < 0.05F) {
                    this.FM.AS.hitTank(this, 1, 1);
                }
            }
            if (this.FM.AS.astateEngineStates[1] > 3) {
                if (World.Rnd().nextFloat() < 0.05F) {
                    this.FM.AS.hitTank(this, 2, 1);
                }
                if (World.Rnd().nextFloat() < 0.05F) {
                    this.FM.AS.hitTank(this, 3, 1);
                }
            }
            if ((this.FM.AS.astateTankStates[0] > 5) && (World.Rnd().nextFloat() < 0.02F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[1] > 5) && (World.Rnd().nextFloat() < 0.02F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[1] > 5) && (World.Rnd().nextFloat() < 0.125F)) {
                this.FM.AS.hitTank(this, 2, 1);
            }
            if ((this.FM.AS.astateTankStates[2] > 5) && (World.Rnd().nextFloat() < 0.125F)) {
                this.FM.AS.hitTank(this, 1, 1);
            }
            if ((this.FM.AS.astateTankStates[2] > 5) && (World.Rnd().nextFloat() < 0.02F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            }
            if ((this.FM.AS.astateTankStates[3] > 5) && (World.Rnd().nextFloat() < 0.02F)) {
                this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
            }
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_FAK"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_FAK"));
            this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
        }
        this.mydebug("========================== isGuidingBomb = " + this.isGuidingBomb);
    }

    public void update(float f) {
        if (this.slider) {
            if (this.sliderX < 0.9F) {
                this.sliderX = this.sliderX + 0.01F;
            }
            if (this.sliderZ < 0.05F) {
                this.sliderZ = this.sliderZ + 0.005F;
            }
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = this.sliderX;
            Aircraft.xyz[2] = this.sliderZ;
            this.hierMesh().chunkSetLocate("Window_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (this.FM.AS.astatePlayerIndex == 1) {
            if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[1]) {
                this.SturmanBusy(1);
            } else if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[2]) {
                this.SturmanBusy(2);
            }
        } else if (Time.current() > this.tme3) {
            this.tme3 = Time.current() + 100L;
            if ((this.FM.turret.length != 0) && !this.pilot2kill) {
                this.FM.turret[0].bIsOperable = true;
            }
        }
        super.update(f);
    }

    private void SturmanBusy(int i) {
        switch (i) {
            default:
                break;

            case 1:
                if (!this.pilot2kill) {
                    this.FM.turret[0].bIsOperable = false;
                }
                break;

            case 2:
                if (!this.pilot2kill) {
                    this.FM.turret[0].bIsOperable = true;
                }
                break;
        }
    }

    public void hitDaSilk() {
        if ((this.FM.AS.astatePilotStates[0] < 95) && !this.slider) {
            this.slider = true;
        }
        super.hitDaSilk();
    }

    public boolean turretAngles(int i, float af[]) {
        for (int j = 0; j < 2; j++) {
            af[j] = (af[j] + 3600F) % 360F;
            if (af[j] > 180F) {
                af[j] -= 360F;
            }
        }

        af[2] = 0.0F;
        boolean flag = true;
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -25F) {
                    f = -25F;
                    flag = false;
                }
                if (f > 15F) {
                    f = 15F;
                    flag = false;
                }
                if (f1 < -40F) {
                    f1 = -40F;
                    flag = false;
                }
                if (f1 > 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                break;

            case 1:
                if (f < -40F) {
                    f = -40F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -5F) {
                    f1 = -5F;
                    flag = false;
                }
                if (f1 > 60F) {
                    f1 = 60F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
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
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                if (this.bPitUnfocused && !this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot1_FAK", false);
                    this.hierMesh().chunkVisible("Pilot1_FAL", true);
                    this.hierMesh().chunkVisible("Head1_FAK", false);
                }
                if (this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot1_FAK", false);
                    this.hierMesh().chunkVisible("Pilot1_FAL", false);
                    this.hierMesh().chunkVisible("Head1_FAK", false);
                }
                if (this.hierMesh().isChunkVisible("Nose_D0") || this.hierMesh().isChunkVisible("Nose_D1") || this.hierMesh().isChunkVisible("Nose_D2")) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1:
                this.pilot2kill = true;
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                if (this.bPitUnfocused && !this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot2_FAK", false);
                    this.hierMesh().chunkVisible("Pilot2_FAL", true);
                    this.hierMesh().chunkVisible("HMask2_D0", false);
                }
                if (this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot2_FAK", false);
                    this.hierMesh().chunkVisible("Pilot2_FAL", false);
                    this.hierMesh().chunkVisible("HMask2_D0", false);
                }
                if (this.hierMesh().isChunkVisible("Nose_D0") || this.hierMesh().isChunkVisible("Nose_D1") || this.hierMesh().isChunkVisible("Nose_D2")) {
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Gore3_D0", true);
                break;
        }
    }

    public boolean typeGuidedBombCisMasterAlive() {
        return this.isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag) {
        this.isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding() {
        return this.isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag) {
        this.isGuidingBomb = flag;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.002F;
        this.mydebug("Chimata typeX4CAdjSidePlus, deltaAzimuth = " + this.deltaAzimuth);
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.002F;
        this.mydebug("Chimata typeX4CAdjSideMinus, deltaAzimuth = " + this.deltaAzimuth);
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.002F;
        this.mydebug("Chimata typeX4CAdjAttitudePlus, deltaTangage = " + this.deltaTangage);
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.002F;
        this.mydebug("Chimata typeX4CAdjAttitudeMinus, deltaTangage = " + this.deltaTangage);
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle++;
        if (this.fSightCurForwardAngle > 85F) {
            this.fSightCurForwardAngle = 85F;
        }
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        if (!this.isGuidingBomb) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        }
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle--;
        if (this.fSightCurForwardAngle < 0.0F) {
            this.fSightCurForwardAngle = 0.0F;
        }
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        if (!this.isGuidingBomb) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        }
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjSideslipPlus() {
        if (!this.isGuidingBomb) {
            this.fSightCurSideslip += 0.1F;
            if (this.fSightCurSideslip > 3F) {
                this.fSightCurSideslip = 3F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
        }
    }

    public void typeBomberAdjSideslipMinus() {
        if (!this.isGuidingBomb) {
            this.fSightCurSideslip -= 0.1F;
            if (this.fSightCurSideslip < -3F) {
                this.fSightCurSideslip = -3F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
        }
    }

    public void typeBomberAdjAltitudePlus() {
        if (!this.isGuidingBomb) {
            this.fSightCurAltitude += 10F;
            if (this.fSightCurAltitude > 10000F) {
                this.fSightCurAltitude = 10000F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
            this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        }
    }

    public void typeBomberAdjAltitudeMinus() {
        if (!this.isGuidingBomb) {
            this.fSightCurAltitude -= 10F;
            if (this.fSightCurAltitude < 850F) {
                this.fSightCurAltitude = 850F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
            this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        }
    }

    protected void mydebug(String s) {
    }

    public boolean  bToFire;
    private float   deltaAzimuth;
    private float   deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;
    private boolean slider;
    private float   sliderX;
    private float   sliderZ;
    private long    tme3;
    private boolean pilot2kill;

    static {
        Class class1 = HE_111H12.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111H-12/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942.12F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111H-12.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHE_111H12.class, CockpitHE_111H12_Bombardier.class, CockpitHE_111H12_NGunner.class, CockpitHE_111H12_TGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
