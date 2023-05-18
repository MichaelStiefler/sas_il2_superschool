package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class HE_111P2_Late extends HE_111 {

    public HE_111P2_Late() {
        this.slider = false;
        this.sliderX = 0.0F;
        this.sliderZ = 0.0F;
        this.pilot2kill = false;
        this.pilot5kill = false;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xnose")) {
            if (this.chunkDamageVisible("Nose") < 2) this.hitChunk("Nose", shot);
            if (shot.power > 200000F) {
                this.FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(3, 192));
                this.FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(3, 192));
            }
            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if (point3d.x > 4.505D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            else if (point3d.y > 0.0D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            } else {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
            }
        } else super.hitBone(s, shot, point3d);
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

            case 4:
                this.hierMesh().chunkVisible("Pilot4_FAK", false);
                break;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.crew = 5;
        this.FM.AS.astatePilotFunctions[0] = 1;
        this.FM.AS.astatePilotFunctions[1] = 9;
        this.FM.AS.astatePilotFunctions[2] = 4;
        this.FM.AS.astatePilotFunctions[3] = 6;
        this.FM.AS.astatePilotFunctions[4] = 5;
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.5F;
        this.gun3 = this.getGunByHookName("_MGUN04");
        this.gun4 = this.getGunByHookName("_MGUN05");
    }

    public void rareAction(float f, boolean flag) {
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3) {
                if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 0, 1);
                if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 1, 1);
            }
            if (this.FM.AS.astateEngineStates[1] > 3) {
                if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 2, 1);
                if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 3, 1);
            }
            if (this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
            if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.125F) this.FM.AS.hitTank(this, 2, 1);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.125F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
            if (this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
        }
        Actor actor = War.GetNearestEnemy(this, 16, 6000F);
        Aircraft aircraft = War.getNearestEnemy(this, 5000F);
        if ((actor != null && !(actor instanceof BridgeSegment) || aircraft != null) && this.FM.CT.getCockpitDoor() < 0.01F) this.FM.AS.setCockpitDoor(this, 1);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
            this.hierMesh().chunkVisible("HMask3_D0", false);
            this.hierMesh().chunkVisible("HMask4_D0", false);
            this.hierMesh().chunkVisible("HMask5_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_FAK"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_FAK"));
            this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
            this.hierMesh().chunkVisible("HMask4_D0", this.hierMesh().isChunkVisible("Pilot4_FAK"));
            this.hierMesh().chunkVisible("HMask5_D0", this.hierMesh().isChunkVisible("Pilot5_D0"));
        }
    }

    public void update(float f) {
        if (this.slider) {
            if (this.sliderX < 0.9F) this.sliderX = this.sliderX + 0.01F;
            if (this.sliderZ < 0.05F) this.sliderZ = this.sliderZ + 0.005F;
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = this.sliderX;
            Aircraft.xyz[2] = this.sliderZ;
            this.hierMesh().chunkSetLocate("Window_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (this.FM.AS.astatePlayerIndex == 1) {
            if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[1]) this.SturmanBusy(1);
            else if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[2]) this.SturmanBusy(2);
        } else if (Time.current() > this.tme3) {
            this.tme3 = Time.current() + 100L;
            if (this.FM.turret.length != 0 && !this.pilot2kill) this.FM.turret[0].bIsOperable = true;
        }
        if (this.FM.AS.astatePlayerIndex == 4) {
            if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[6]) this.SturmanBusy(5);
            else if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[5]) this.SturmanBusy(6);
        } else if (Time.current() > this.tme) {
            this.tme = Time.current() + 1500L;
            if (this.FM.turret.length != 0) {
                Actor actor = null;
                actor = this.FM.turret[3].target;
                if (actor == null) actor = this.FM.turret[4].target;
                if (actor != null) if (Actor.isValid(actor)) {
                    this.pos.getAbs(Aircraft.tmpLoc2);
                    actor.pos.getAbs(Aircraft.tmpLoc3);
                    Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                    if (Aircraft.tmpLoc3.getPoint().y < 0.0D) this.SturmanBusy(5);
                    else this.SturmanBusy(6);
                } else if (!this.pilot5kill) {
                    this.FM.turret[3].bIsOperable = true;
                    this.FM.turret[4].bIsOperable = true;
                }
            }
        }
        super.update(f);
    }

    private void SturmanBusy(int i) {
        switch (i) {
            case 3:
            case 4:
            default:
                break;

            case 2:
                if (!this.pilot2kill) this.FM.turret[0].bIsOperable = true;
                break;

            case 1:
                if (!this.pilot2kill) this.FM.turret[0].bIsOperable = false;
                break;

            case 5:
                if (this.pilot5kill) break;
                this.FM.turret[3].bIsOperable = false;
                this.FM.turret[4].bIsOperable = true;
                this.hierMesh().chunkSetAngles("Pilot5_D0", 0.0F, 0.0F, 0.0F);
                if (this.FM.turret.length != 0 && World.cur().diffCur.Limited_Ammo) {
                    this.gunmax = Math.max(this.gun3.countBullets(), this.gun4.countBullets());
                    this.gunmin = 0;
                    this.gun4.loadBullets(this.gunmax);
                    this.gun3.loadBullets(this.gunmin);
                }
                break;

            case 6:
                if (this.pilot5kill) break;
                this.FM.turret[3].bIsOperable = true;
                this.FM.turret[4].bIsOperable = false;
                this.hierMesh().chunkSetAngles("Pilot5_D0", -180F, 0.0F, 0.0F);
                if (this.FM.turret.length != 0 && World.cur().diffCur.Limited_Ammo) {
                    this.gunmax = Math.max(this.gun3.countBullets(), this.gun4.countBullets());
                    this.gunmin = 0;
                    this.gun3.loadBullets(this.gunmax);
                    this.gun4.loadBullets(this.gunmin);
                }
                break;
        }
    }

    public void moveCockpitDoor(float f) {
        Aircraft.xyz[0] = 0.0F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[1] = f * -0.65F;
        this.hierMesh().chunkSetLocate("Kolpak_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void hitDaSilk() {
        if (this.FM.AS.astatePilotStates[0] < 95 && !this.slider) this.slider = true;
        if (this.FM.AS.astatePilotStates[2] < 95 && this.FM.CT.getCockpitDoor() < 0.01F) this.FM.AS.setCockpitDoor(this, 1);
        super.hitDaSilk();
    }

    public boolean turretAngles(int i, float af[]) {
        for (int j = 0; j < 2; j++) {
            af[j] = (af[j] + 3600F) % 360F;
            if (af[j] > 180F) af[j] -= 360F;
        }

        af[2] = 0.0F;
        boolean flag = true;
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 35F) {
                    f = 35F;
                    flag = false;
                }
                if (f1 < -27F) {
                    f1 = -27F;
                    flag = false;
                }
                if (f1 > 13F) {
                    f1 = 13F;
                    flag = false;
                }
                break;

            case 1:
                float f2 = this.FM.CT.getCockpitDoor();
                if (f2 >= 0.99F) {
                    if (f < -90F) {
                        f = -90F;
                        flag = false;
                    }
                    if (f > 90F) {
                        f = 90F;
                        flag = false;
                    }
                }
                if (f2 < 0.99F) {
                    if (f < -42F) {
                        f = -42F;
                        flag = false;
                    }
                    if (f > 42F) {
                        f = 42F;
                        flag = false;
                    }
                }
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 63F) {
                    f1 = 63F;
                    flag = false;
                }
                break;

            case 2:
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 40F) {
                    f = 40F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 46F) {
                    f1 = 46F;
                    flag = false;
                }
                break;

            case 3:
                if (f < -55F) {
                    f = -55F;
                    flag = false;
                }
                if (f > 23F) {
                    f = 23F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                if (f1 > 55F + 0.6F * f) {
                    f1 = 55F + 0.6F * f;
                    flag = false;
                }
                break;

            case 4:
                if (f < -23F) {
                    f = -23F;
                    flag = false;
                }
                if (f > 55F) {
                    f = 55F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                if (f1 > 55F - 0.6F * f) {
                    f1 = 55F - 0.6F * f;
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

            case 3:
                this.FM.turret[2].setHealth(f);
                break;

            case 4:
                this.FM.turret[3].setHealth(f);
                this.FM.turret[4].setHealth(f);
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
                if (this.hierMesh().isChunkVisible("Nose_D0") || this.hierMesh().isChunkVisible("Nose_D1") || this.hierMesh().isChunkVisible("Nose_D2")) this.hierMesh().chunkVisible("Gore1_D0", true);
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
                if (this.hierMesh().isChunkVisible("Nose_D0") || this.hierMesh().isChunkVisible("Nose_D1") || this.hierMesh().isChunkVisible("Nose_D2")) this.hierMesh().chunkVisible("Gore2_D0", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("Gore3_D0", true);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                if (this.bPitUnfocused && !this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot4_FAK", false);
                    this.hierMesh().chunkVisible("Pilot4_FAL", true);
                    this.hierMesh().chunkVisible("HMask4_D0", false);
                }
                if (this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Pilot4_FAK", false);
                    this.hierMesh().chunkVisible("Pilot4_FAL", false);
                    this.hierMesh().chunkVisible("HMask4_D0", false);
                }
                break;

            case 4:
                this.pilot5kill = true;
                this.hierMesh().chunkVisible("Pilot5_D0", false);
                this.hierMesh().chunkVisible("Pilot5_D1", true);
                this.hierMesh().chunkVisible("HMask5_D0", false);
                break;
        }
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay5_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay7_D0", 0.0F, 74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay9_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay10_D0", 0.0F, 94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay11_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay12_D0", 0.0F, 94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay13_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay14_D0", 0.0F, 94F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay15_D0", 0.0F, -74F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay16_D0", 0.0F, 94F * f, 0.0F);
    }

    private boolean slider;
    private float   sliderX;
    private float   sliderZ;
    private long    tme;
    private long    tme3;
    private boolean pilot2kill;
    private boolean pilot5kill;
    private int     gunmax;
    private int     gunmin;
    private Gun     gun3;
    private Gun     gun4;

    static {
        Class class1 = HE_111P2_Late.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111P-2-Late/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1940.1F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111P-2-Late.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHE_111H1.class, CockpitHE_111H1_Bombardier.class, CockpitHE_111H1_NGunner.class, CockpitHE_111H1_TGunner.class, CockpitHE_111P2_Late_BGunner.class, CockpitHE_111P2_Late_LGunner.class,
                CockpitHE_111P2_Late_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08" });
    }
}
