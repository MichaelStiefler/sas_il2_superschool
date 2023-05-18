package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class B_24J extends B_24 implements TypeBomber {

    public B_24J() {
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
        this.bpos = 1.0F;
        this.bcurpos = 1.0F;
        this.btme = -1L;
    }

    public void update(float f) {
        super.update(f);
        if (!this.FM.AS.isMaster()) {
            return;
        }
        if (this.bpos == 0.0F) {
            if (this.bcurpos > this.bpos) {
                this.bcurpos -= 0.2F * f;
                if (this.bcurpos < 0.0F) {
                    this.bcurpos = 0.0F;
                }
            }
            this.resetYPRmodifier();
            Aircraft.xyz[1] = -0.71F + (0.71F * this.bcurpos);
            this.hierMesh().chunkSetLocate("Turret3A_D0", Aircraft.xyz, Aircraft.ypr);
        } else if (this.bpos == 1.0F) {
            if (this.bcurpos < this.bpos) {
                this.bcurpos += 0.2F * f;
                if (this.bcurpos > 1.0F) {
                    this.bcurpos = 1.0F;
                    this.bpos = 0.5F;
                    this.FM.turret[2].bIsOperable = true;
                }
            }
            this.resetYPRmodifier();
            Aircraft.xyz[1] = -0.7F + (0.7F * this.bcurpos);
            this.hierMesh().chunkSetLocate("Turret3A_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (Time.current() > this.btme) {
            this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
            if ((this.FM.turret[1].target == null) && (this.FM.turret[2].target == null) && (this.FM.turret[3].target == null) && (this.FM.turret[4].target == null)) {
                this.FM.turret[2].bIsOperable = false;
                this.bpos = 0.0F;
            }
            if ((this.FM.turret[1].target != null) && (this.FM.AS.astatePilotStates[4] < 90)) {
                this.bpos = 1.0F;
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.killPilot(this, 4);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 7; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            B_24J.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            B_24J.bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -85F) {
                    f = -85F;
                    flag = false;
                }
                if (f > 85F) {
                    f = 85F;
                    flag = false;
                }
                if (f1 < -32F) {
                    f1 = -32F;
                    flag = false;
                }
                if (f1 > 46F) {
                    f1 = 46F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 < -0F) {
                    f1 = -0F;
                    flag = false;
                }
                if (f1 > 20F) {
                    f1 = 20F;
                    flag = false;
                }
                break;

            case 2:
                if (f1 < -70F) {
                    f1 = -70F;
                    flag = false;
                }
                if (f1 > 7F) {
                    f1 = 7F;
                    flag = false;
                }
                break;

            case 3:
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 64F) {
                    f = 64F;
                    flag = false;
                }
                if (f1 < -37F) {
                    f1 = -37F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;

            case 4:
                if (f < -67F) {
                    f = -67F;
                    flag = false;
                }
                if (f > 34F) {
                    f = 34F;
                    flag = false;
                }
                if (f1 < -37F) {
                    f1 = -37F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;

            case 5:
                if (f < -85F) {
                    f = -85F;
                    flag = false;
                }
                if (f > 85F) {
                    f = 85F;
                    flag = false;
                }
                if (f1 < -32F) {
                    f1 = -32F;
                    flag = false;
                }
                if (f1 > 46F) {
                    f1 = 46F;
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

            case 3:
                this.FM.turret[1].bIsOperable = false;
                break;

            case 4:
                this.FM.turret[2].bIsOperable = false;
                break;

            case 5:
                this.FM.turret[3].bIsOperable = false;
                this.FM.turret[4].bIsOperable = false;
                break;
        }
    }

    private static final float toMeters(float f) {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f) {
        return 0.4470401F * f;
    }

    public boolean typeBomberToggleAutomation() {
        this.bSightAutomation = !this.bSightAutomation;
        this.bSightBombDump = false;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
        return this.bSightAutomation;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle++;
        if (this.fSightCurForwardAngle > 85F) {
            this.fSightCurForwardAngle = 85F;
        }
        this.fSightCurDistance = B_24J.toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle--;
        if (this.fSightCurForwardAngle < 0.0F) {
            this.fSightCurForwardAngle = 0.0F;
        }
        this.fSightCurDistance = B_24J.toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.1F;
        if (this.fSightCurSideslip > 3F) {
            this.fSightCurSideslip = 3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.1F;
        if (this.fSightCurSideslip < -3F) {
            this.fSightCurSideslip = -3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 10F)) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 3000F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 50F;
        if (this.fSightCurAltitude > 50000F) {
            this.fSightCurAltitude = 50000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = B_24J.toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 50F;
        if (this.fSightCurAltitude < 1000F) {
            this.fSightCurAltitude = 1000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = B_24J.toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 200F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 450F) {
            this.fSightCurSpeed = 450F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10F;
        if (this.fSightCurSpeed < 100F) {
            this.fSightCurSpeed = 100F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) {
                this.fSightCurReadyness = 0.0F;
            }
        }
        if (this.fSightCurReadyness < 1.0F) {
            this.fSightCurReadyness += 0.0333333F * f;
        } else if (this.bSightAutomation) {
            this.fSightCurDistance -= B_24J.toMetersPerSecond(this.fSightCurSpeed) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / B_24J.toMeters(this.fSightCurAltitude)));
            if (this.fSightCurDistance < (B_24J.toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(B_24J.toMeters(this.fSightCurAltitude) * (2F / 9.81F)))) {
                this.bSightBombDump = true;
            }
            if (this.bSightBombDump) {
                if (this.FM.isTick(3, 0)) {
                    if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
                        this.FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else {
                    this.FM.CT.WeaponControl[3] = false;
                }
            }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(this.fSightCurDistance);
        netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.bSightAutomation = (i & 1) != 0;
        this.bSightBombDump = (i & 2) != 0;
        this.fSightCurDistance = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
        this.fSightCurSideslip = -3F + (netmsginput.readUnsignedByte() / 33.33333F);
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public static boolean bChangedPit = false;
    private boolean       bSightAutomation;
    private boolean       bSightBombDump;
    private float         fSightCurDistance;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;
    private float         bpos;
    private float         bcurpos;
    private long          btme;

    static {
        Class class1 = B_24J.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-24");
        Property.set(class1, "meshName", "3DO/Plane/B-24J(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/B-24J(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 2800.9F);
        Property.set(class1, "FlightModel", "FlightModels/B-24J_Mod.fmd:B24JMOD");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB_24J.class, CockpitB_24J_Bombardier.class, CockpitB_24J_FGunner.class, CockpitB_24J_TGunner.class, CockpitB_24J_AGunner.class, CockpitB_24J_BGunner.class, CockpitB_24J_RGunner.class, CockpitB_24J_LGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 13, 14, 15, 15, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn02", "_BombSpawn03", "_BombSpawn03", "_BombSpawn04", "_BombSpawn04", "_BombSpawn05", "_BombSpawn05", "_BombSpawn06", "_BombSpawn06", "_BombSpawn07", "_BombSpawn07", "_BombSpawn08", "_BombSpawn08", "_BombSpawn09", "_BombSpawn09", "_BombSpawn10", "_BombSpawn10", "_BombSpawn11", "_BombSpawn11", "_BombSpawn12", "_BombSpawn12", "_BombSpawn13", "_BombSpawn13", "_BombSpawn14", "_BombSpawn14", "_BombSpawn15", "_BombSpawn15", "_BombSpawn16", "_BombSpawn16", "_BombSpawn17", "_BombSpawn17", "_BombSpawn18", "_BombSpawn18", "_BombSpawn19", "_BombSpawn19", "_BombSpawn20", "_BombSpawn20", "_BombSpawn21", "_BombSpawn21", "_BombSpawn22", "_BombSpawn22", "_BombSpawn23", "_BombSpawn23", "_BombSpawn24", "_BombSpawn24" });
    }
}
