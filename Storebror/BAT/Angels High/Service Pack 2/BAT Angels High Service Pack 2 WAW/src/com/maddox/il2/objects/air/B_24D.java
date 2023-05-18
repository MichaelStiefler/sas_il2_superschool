package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class B_24D extends B_24 implements TypeBomber {

    public B_24D() {
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
            case 19:
                this.killPilot(this, 4);
                break;
        }
        return super.cutFM(i, i_0_, actor);
    }

    public void rareAction(float f, boolean bool) {
        super.rareAction(f, bool);
        for (int i = 1; i < 7; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    protected void nextDMGLevel(String string, int i, Actor actor) {
        super.nextDMGLevel(string, i, actor);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String string, int i, Actor actor) {
        super.nextCUTLevel(string, i, actor);
        if (this.FM.isPlayers()) {
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

            case 0:
                if (f < -85F) {
                    f = -85F;
                    bool = false;
                }
                if (f > 85F) {
                    f = 85F;
                    bool = false;
                }
                if (f_1_ < -32F) {
                    f_1_ = -32F;
                    bool = false;
                }
                if (f_1_ > 46F) {
                    f_1_ = 46F;
                    bool = false;
                }
                break;

            case 1:
                if (f_1_ < -0F) {
                    f_1_ = -0F;
                    bool = false;
                }
                if (f_1_ > 20F) {
                    f_1_ = 20F;
                    bool = false;
                }
                break;

            case 2:
                if (f_1_ < -70F) {
                    f_1_ = -70F;
                    bool = false;
                }
                if (f_1_ > 7F) {
                    f_1_ = 7F;
                    bool = false;
                }
                break;

            case 3:
                if (f < -35F) {
                    f = -35F;
                    bool = false;
                }
                if (f > 64F) {
                    f = 64F;
                    bool = false;
                }
                if (f_1_ < -37F) {
                    f_1_ = -37F;
                    bool = false;
                }
                if (f_1_ > 50F) {
                    f_1_ = 50F;
                    bool = false;
                }
                break;

            case 4:
                if (f < -67F) {
                    f = -67F;
                    bool = false;
                }
                if (f > 34F) {
                    f = 34F;
                    bool = false;
                }
                if (f_1_ < -37F) {
                    f_1_ = -37F;
                    bool = false;
                }
                if (f_1_ > 50F) {
                    f_1_ = 50F;
                    bool = false;
                }
                break;

            case 5:
                if (f < -85F) {
                    f = -85F;
                    bool = false;
                }
                if (f > 85F) {
                    f = 85F;
                    bool = false;
                }
                if (f_1_ < -32F) {
                    f_1_ = -32F;
                    bool = false;
                }
                if (f_1_ > 46F) {
                    f_1_ = 46F;
                    bool = false;
                }
                break;
        }
        fs[0] = -f;
        fs[1] = f_1_;
        return bool;
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
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
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

    static {
        Class var_class = B_24D.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "B-24");
        Property.set(var_class, "meshName", "3DO/Plane/B-24D(Multi1)/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(var_class, "meshName_us", "3DO/Plane/B-24D(USA)/hier.him");
        Property.set(var_class, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(var_class, "noseart", 1);
        Property.set(var_class, "yearService", 1942.5F);
        Property.set(var_class, "yearExpired", 1944.9F);
        Property.set(var_class, "FlightModel", "FlightModels/B-24J.fmd");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitB_24D.class, CockpitB_24D_Bombardier.class, CockpitB_24D_FGunner.class, CockpitB_24D_TGunner.class, CockpitB_24D_BGunner.class, CockpitB_24D_RGunner.class, CockpitB_24D_LGunner.class, CockpitB_24D_AGunner.class });
        Aircraft.weaponTriggersRegister(var_class, new int[] { 10, 11, 11, 12, 12, 13, 14, 15, 15, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08" });
        try {
            ArrayList arraylist = new ArrayList();
            Property.set(var_class, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(var_class, "weaponsMap", hashmapint);
            int i = 17;
            String string = "default";
            Aircraft._WeaponSlot var__WeaponSlots[] = new Aircraft._WeaponSlot[i];
            var__WeaponSlots[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 365);
            var__WeaponSlots[1] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 375);
            var__WeaponSlots[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 375);
            var__WeaponSlots[7] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[8] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[9] = null;
            var__WeaponSlots[10] = null;
            var__WeaponSlots[11] = null;
            var__WeaponSlots[12] = null;
            var__WeaponSlots[13] = null;
            var__WeaponSlots[14] = null;
            var__WeaponSlots[15] = null;
            var__WeaponSlots[16] = null;
            for (int i_2_ = 17; i_2_ < i; i_2_++) {
                var__WeaponSlots[i_2_] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), var__WeaponSlots);
            string = "16x250";
            var__WeaponSlots = new Aircraft._WeaponSlot[i];
            var__WeaponSlots[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 365);
            var__WeaponSlots[1] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 375);
            var__WeaponSlots[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 375);
            var__WeaponSlots[7] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[8] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[9] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            var__WeaponSlots[10] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            var__WeaponSlots[11] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            var__WeaponSlots[12] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            var__WeaponSlots[13] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            var__WeaponSlots[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            var__WeaponSlots[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            var__WeaponSlots[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 2);
            for (int i_3_ = 17; i_3_ < i; i_3_++) {
                var__WeaponSlots[i_3_] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), var__WeaponSlots);
            string = "16x500";
            var__WeaponSlots = new Aircraft._WeaponSlot[i];
            var__WeaponSlots[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 365);
            var__WeaponSlots[1] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 375);
            var__WeaponSlots[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 375);
            var__WeaponSlots[7] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[8] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[9] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            var__WeaponSlots[10] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            var__WeaponSlots[11] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            var__WeaponSlots[12] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            var__WeaponSlots[13] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            var__WeaponSlots[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            var__WeaponSlots[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            var__WeaponSlots[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 2);
            for (int i_4_ = 17; i_4_ < i; i_4_++) {
                var__WeaponSlots[i_4_] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), var__WeaponSlots);
            string = "8x1000";
            var__WeaponSlots = new Aircraft._WeaponSlot[i];
            var__WeaponSlots[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 365);
            var__WeaponSlots[1] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 375);
            var__WeaponSlots[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 375);
            var__WeaponSlots[7] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[8] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[9] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            var__WeaponSlots[10] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            var__WeaponSlots[11] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            var__WeaponSlots[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            var__WeaponSlots[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            var__WeaponSlots[14] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            var__WeaponSlots[15] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            var__WeaponSlots[16] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            for (int i_5_ = 17; i_5_ < i; i_5_++) {
                var__WeaponSlots[i_5_] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), var__WeaponSlots);
            string = "6x1600";
            var__WeaponSlots = new Aircraft._WeaponSlot[i];
            var__WeaponSlots[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 365);
            var__WeaponSlots[1] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 375);
            var__WeaponSlots[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 375);
            var__WeaponSlots[7] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[8] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[9] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            var__WeaponSlots[10] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            var__WeaponSlots[11] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            var__WeaponSlots[12] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            var__WeaponSlots[13] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            var__WeaponSlots[14] = new Aircraft._WeaponSlot(3, "BombGun1600lbs", 1);
            var__WeaponSlots[15] = null;
            var__WeaponSlots[16] = null;
            for (int i_6_ = 17; i_6_ < i; i_6_++) {
                var__WeaponSlots[i_6_] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), var__WeaponSlots);
            string = "4x2000";
            var__WeaponSlots = new Aircraft._WeaponSlot[i];
            var__WeaponSlots[0] = new Aircraft._WeaponSlot(10, "MGunBrowning50t", 365);
            var__WeaponSlots[1] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[2] = new Aircraft._WeaponSlot(11, "MGunBrowning50t", 610);
            var__WeaponSlots[3] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[4] = new Aircraft._WeaponSlot(12, "MGunBrowning50t", 610);
            var__WeaponSlots[5] = new Aircraft._WeaponSlot(13, "MGunBrowning50t", 375);
            var__WeaponSlots[6] = new Aircraft._WeaponSlot(14, "MGunBrowning50t", 375);
            var__WeaponSlots[7] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[8] = new Aircraft._WeaponSlot(15, "MGunBrowning50t", 500);
            var__WeaponSlots[9] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            var__WeaponSlots[10] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            var__WeaponSlots[11] = null;
            var__WeaponSlots[12] = null;
            var__WeaponSlots[13] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            var__WeaponSlots[14] = new Aircraft._WeaponSlot(3, "BombGun2000lbs", 1);
            var__WeaponSlots[15] = null;
            var__WeaponSlots[16] = null;
            for (int i_7_ = 17; i_7_ < i; i_7_++) {
                var__WeaponSlots[i_7_] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), var__WeaponSlots);
            string = "none";
            var__WeaponSlots = new Aircraft._WeaponSlot[i];
            for (int i_8_ = 17; i_8_ < i; i_8_++) {
                var__WeaponSlots[i_8_] = null;
            }

            arraylist.add(string);
            hashmapint.put(Finger.Int(string), var__WeaponSlots);
        } catch (Exception exception) {
        }
    }
}
