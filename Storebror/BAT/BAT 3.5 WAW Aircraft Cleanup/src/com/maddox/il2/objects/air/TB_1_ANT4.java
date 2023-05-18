package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class TB_1_ANT4 extends TB_3 {

    public TB_1_ANT4() {
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        float f2 = Math.abs(f);
        switch (i) {
            default:
                break;

            case 0:
                if (f1 < -47F) {
                    f1 = -47F;
                    flag = false;
                }
                if (f1 > 47F) {
                    f1 = 47F;
                    flag = false;
                }
                if (f2 < 147F) {
                    if (f1 < ((0.5964912F * f2) - 117.6842F)) {
                        f1 = (0.5964912F * f2) - 117.6842F;
                        flag = false;
                    }
                } else if (f2 < 157F) {
                    if (f1 < ((0.3F * f2) - 74.1F)) {
                        f1 = (0.3F * f2) - 74.1F;
                        flag = false;
                    }
                } else if (f1 < ((0.2173913F * f2) - 61.13044F)) {
                    f1 = (0.2173913F * f2) - 61.13044F;
                    flag = false;
                }
                if (f2 >= 110F) {
                    if (f2 < 115F) {
                        if ((f1 < -5F) && (f1 > -20F)) {
                            flag = false;
                        }
                    } else if (f2 < 160F) {
                        if (f1 < -5F) {
                            flag = false;
                        }
                    } else if (f1 < 15F) {
                        flag = false;
                    }
                }
                break;

            case 1:
                if (f1 < -47F) {
                    f1 = -47F;
                    flag = false;
                }
                if (f1 > 47F) {
                    f1 = 47F;
                    flag = false;
                }
                if (f < -38F) {
                    if (f1 < -32F) {
                        f1 = -32F;
                        flag = false;
                    }
                } else if (f < -16F) {
                    if (f1 < ((0.5909091F * f) - 9.545455F)) {
                        f1 = (0.5909091F * f) - 9.545455F;
                        flag = false;
                    }
                } else if (f < 35F) {
                    if (f1 < -19F) {
                        f1 = -19F;
                        flag = false;
                    }
                } else if (f < 44F) {
                    if (f1 < ((-3.111111F * f) + 89.88889F)) {
                        f1 = (-3.111111F * f) + 89.88889F;
                        flag = false;
                    }
                } else if (f < 139F) {
                    if (f1 < -47F) {
                        f1 = -47F;
                        flag = false;
                    }
                } else if (f < 150F) {
                    if (f1 < ((1.363636F * f) - 236.5455F)) {
                        f1 = (1.363636F * f) - 236.5455F;
                        flag = false;
                    }
                } else if (f1 < -32F) {
                    f1 = -32F;
                    flag = false;
                }
                if (f < -175.7F) {
                    if (f1 < 80.8F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -167F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -124.8F) {
                    if (f1 < -22.8F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -82F) {
                    if (f1 < -16F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 24F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 32F) {
                    if (f1 < -8.3F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 80F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 174F) {
                    if (f1 < ((0.5F * f) - 87F)) {
                        flag = false;
                    }
                    break;
                }
                if (f < 178.7F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f1 < 80.8F) {
                    flag = false;
                }
                break;

            case 2:
                if (f1 < -47F) {
                    f1 = -47F;
                    flag = false;
                }
                if (f1 > 47F) {
                    f1 = 47F;
                    flag = false;
                }
                if (f < -68F) {
                    if (f1 < -32F) {
                        f1 = -32F;
                        flag = false;
                    }
                } else if (f < -22F) {
                    if (f1 < ((0.5347826F * f) + 4.365217F)) {
                        f1 = (0.5347826F * f) + 4.365217F;
                        flag = false;
                    }
                } else if (f < 27F) {
                    if (f1 < ((-0.3387755F * f) - 14.85306F)) {
                        f1 = (-0.3387755F * f) - 14.85306F;
                        flag = false;
                    }
                } else if (f < 40F) {
                    if (f1 < ((-1.769231F * f) + 23.76923F)) {
                        f1 = (-1.769231F * f) + 23.76923F;
                        flag = false;
                    }
                } else if (f < 137F) {
                    if (f1 < -47F) {
                        f1 = -47F;
                        flag = false;
                    }
                } else if (f < 152F) {
                    if (f1 < ((1.0F * f) - 184F)) {
                        f1 = (1.0F * f) - 184F;
                        flag = false;
                    }
                } else if (f1 < -32F) {
                    f1 = -32F;
                    flag = false;
                }
                if (f < -172F) {
                    if (f1 < 2.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -123F) {
                    if (f1 < 30F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -102F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -36F) {
                    if (f1 < -9F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -5.1F) {
                    if (f1 < 0.0F) {
                        flag = false;
                    }
                    break;
                }
                if (f < -1.2F) {
                    if (f1 < 24.5F) {
                        flag = false;
                    }
                    break;
                }
                if (f < 62F) {
                    if (f1 < ((-0.7436709F * f) - 0.892496F)) {
                        f1 = (-0.7436709F * f) - 0.892496F;
                        flag = false;
                    }
                    break;
                }
                if (f < 103F) {
                    if (f1 < -47F) {
                        flag = false;
                    }
                    break;
                }
                if (f1 < 0.0F) {
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            for (int i = 0; i < 4; i++) {
                if ((this.FM.AS.astateEngineStates[i] > 3) && (this.FM.EI.engines[i].getReadyness() < 0.1F)) {
                    this.FM.AS.repairEngine(i);
                }
            }

            for (int j = 0; j < 4; j++) {
                if ((this.FM.AS.astateTankStates[j] > 3) && (this.FM.AS.astatePilotStates[4] < 50F) && (this.FM.AS.astatePilotStates[7] < 50F) && (World.Rnd().nextFloat() < 0.1F)) {
                    this.FM.AS.repairTank(j);
                }
            }

        }
    }

    public void update(float f) {
        super.update(f);
        if (!(this instanceof TypeSeaPlane) && !(this instanceof TB_1_ANT4Ski)) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -this.FM.Gears.gWheelAngles[0], 0.0F);
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -this.FM.Gears.gWheelAngles[1], 0.0F);
        }
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if ((explosion.chunkName != null) && (explosion.chunkName.startsWith("Wing") || explosion.chunkName.startsWith("Tail")) && explosion.chunkName.endsWith("D3") && (explosion.power < 0.014F)) {
            return;
        } else {
            super.msgExplosion(explosion);
            return;
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 50F;
        if (this.fSightCurAltitude > 5000F) {
            this.fSightCurAltitude = 5000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 50F;
        if (this.fSightCurAltitude < 300F) {
            this.fSightCurAltitude = 300F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 350F) {
            this.fSightCurSpeed = 350F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) {
            this.fSightCurSpeed = 50F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f1) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;

    static {
        Class class1 = TB_1_ANT4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "meshName", "3DO/Plane/TB_1_ANT4/hier.him");
        Property.set(class1, "iconFar_shortClassName", "ANT4");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/TB_1_ANT_4.fmd:ANT4_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitANT_4T.class, CockpitANT_4T_Bombardier.class });
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_BombSpawn01", "_BombSpawn02" });
    }
}
