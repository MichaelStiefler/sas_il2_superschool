package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class B_26B extends B26 implements TypeBomber, TypeStormovik, TypeStormovikArmored {

    public B_26B() {
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
    }

    protected boolean cutFM(int i, int i_0_, Actor actor) {
        switch (i) {
            case 19: // '\023'
                this.killPilot(this, 4);
                this.hierMesh().chunkVisible("Wire2_D0", false);
                break;
        }
        return super.cutFM(i, i_0_, actor);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        ((FlightModelMain) (super.FM)).AS.wantBeaconsNet(true);
    }

    public void rareAction(float f, boolean bool) {
        super.rareAction(f, bool);
        for (int i = 1; i < 7; i++) {
            if (super.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    protected void nextDMGLevel(String string, int i, Actor actor) {
        super.nextDMGLevel(string, i, actor);
        if (super.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String string, int i, Actor actor) {
        super.nextCUTLevel(string, i, actor);
        if (super.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float fs[]) {
        boolean bool = super.turretAngles(i, fs);
        float f = -fs[0];
        float f_1_ = fs[1];
        switch (i) {
            default:
                break;

            case 0: // '\0'
                if (f < -23F) {
                    f = -23F;
                    bool = false;
                }
                if (f > 23F) {
                    f = 23F;
                    bool = false;
                }
                if (f_1_ < -25F) {
                    f_1_ = -25F;
                    bool = false;
                }
                if (f_1_ > 15F) {
                    f_1_ = 15F;
                    bool = false;
                }
                break;

            case 1: // '\001'
                if (f_1_ < 0.0F) {
                    f_1_ = 0.0F;
                    bool = false;
                }
                if (f_1_ > 73F) {
                    f_1_ = 73F;
                    bool = false;
                }
                break;

            case 2: // '\002'
                if (f < -38F) {
                    f = -38F;
                    bool = false;
                }
                if (f > 38F) {
                    f = 38F;
                    bool = false;
                }
                if (f_1_ < -41F) {
                    f_1_ = -41F;
                    bool = false;
                }
                if (f_1_ > 43F) {
                    f_1_ = 43F;
                    bool = false;
                }
                break;

            case 3: // '\003'
                if (f < -85F) {
                    f = -85F;
                    bool = false;
                }
                if (f > 22F) {
                    f = 22F;
                    bool = false;
                }
                if (f_1_ < -40F) {
                    f_1_ = -40F;
                    bool = false;
                }
                if (f_1_ > 32F) {
                    f_1_ = 32F;
                    bool = false;
                }
                break;

            case 4: // '\004'
                if (f < -34F) {
                    f = -34F;
                    bool = false;
                }
                if (f > 30F) {
                    f = 30F;
                    bool = false;
                }
                if (f_1_ < -30F) {
                    f_1_ = -30F;
                    bool = false;
                }
                if (f_1_ > 32F) {
                    f_1_ = 32F;
                    bool = false;
                }
                break;
        }
        fs[0] = -f;
        fs[1] = f_1_;
        return bool;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2: // '\002'
                super.FM.turret[0].setHealth(f);
                break;

            case 3: // '\003'
                super.FM.turret[1].setHealth(f);
                break;

            case 4: // '\004'
                super.FM.turret[2].setHealth(f);
                break;

            case 5: // '\005'
                super.FM.turret[3].setHealth(f);
                super.FM.turret[4].setHealth(f);
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
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
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
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
        if (this.bSightAutomation) {
            this.typeBomberToggleAutomation();
        }
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.05F;
        if (this.fSightCurSideslip > 3F) {
            this.fSightCurSideslip = 3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.05F;
        if (this.fSightCurSideslip < -3F) {
            this.fSightCurSideslip = -3F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
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
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 50F;
        if (this.fSightCurAltitude < 1000F) {
            this.fSightCurAltitude = 1000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = toMeters(this.fSightCurAltitude) * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
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
        if (Math.abs(((FlightModelMain) (super.FM)).Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) {
                this.fSightCurReadyness = 0.0F;
            }
        }
        if (this.fSightCurReadyness < 1.0F) {
            this.fSightCurReadyness += 0.0333333F * f;
        } else if (this.bSightAutomation) {
            this.fSightCurDistance -= toMetersPerSecond(this.fSightCurSpeed) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / toMeters(this.fSightCurAltitude)));
            if (this.fSightCurDistance < (toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(toMeters(this.fSightCurAltitude) * 0.2038736F))) {
                this.bSightBombDump = true;
            }
            if (this.bSightBombDump) {
                if (super.FM.isTick(3, 0)) {
                    if ((((FlightModelMain) (super.FM)).CT.Weapons[3] != null) && (((FlightModelMain) (super.FM)).CT.Weapons[3][((FlightModelMain) (super.FM)).CT.Weapons[3].length - 1] != null) && ((FlightModelMain) (super.FM)).CT.Weapons[3][((FlightModelMain) (super.FM)).CT.Weapons[3].length - 1].haveBullets()) {
                        ((FlightModelMain) (super.FM)).CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else {
                    ((FlightModelMain) (super.FM)).CT.WeaponControl[3] = false;
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

    static {
        Class class1 = B_26B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-26");
        Property.set(class1, "meshName", "3DO/Plane/B-26B(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "meshName_us", "3DO/Plane/B-26B(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar03());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/B-26B.fmd:B26B_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB26B.class, CockpitB26B_Bombardier.class, CockpitB26B_FGunner.class, CockpitB26B_TGunner.class, CockpitB26B_AGunner.class, CockpitB26B_RGunner.class, CockpitB26B_LGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 10, 11, 11, 12, 12, 13, 14, 3, 3, 3, 3, 3, 3, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_ExternalDev05", "_ExternalBomb09" });
    }
}
