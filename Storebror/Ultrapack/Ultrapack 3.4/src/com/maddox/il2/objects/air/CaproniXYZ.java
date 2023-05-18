package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class CaproniXYZ extends Scheme6 implements TypeBomber, TypeTransport {

    public CaproniXYZ() {
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.llpos = 0.0F;
        this.bPitUnfocused = true;
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Point3d point3d = new Point3d(0.0D, 0.0D, 0.0D);
        com.maddox.JGP.Point3f point3f = this.FM.EI.engines[0].getPropPos();
        point3d.x = point3f.x;
        point3d.y = 4.1D;
        point3d.z = point3f.z;
        this.FM.EI.engines[0].setPropPos(point3d);
        point3f = this.FM.EI.engines[0].getEnginePos();
        point3d.x = point3f.x;
        point3d.y = 4D;
        point3d.z = point3f.z;
        this.FM.EI.engines[0].setPos(point3d);
        point3f = this.FM.EI.engines[1].getPropPos();
        point3d.x = point3f.x;
        point3d.y = 0.0D;
        point3d.z = point3f.z;
        this.FM.EI.engines[1].setPropPos(point3d);
        point3f = this.FM.EI.engines[1].getEnginePos();
        point3d.x = point3f.x;
        point3d.y = 0.0D;
        point3d.z = point3f.z;
        this.FM.EI.engines[1].setPos(point3d);
        point3f = this.FM.EI.engines[2].getPropPos();
        point3d.x = point3f.x;
        point3d.y = -4.1D;
        point3d.z = point3f.z;
        this.FM.EI.engines[2].setPropPos(point3d);
        point3f = this.FM.EI.engines[2].getEnginePos();
        point3d.x = point3f.x;
        point3d.y = -4D;
        point3d.z = point3f.z;
        this.FM.EI.engines[2].setPos(point3d);
    }

    public void doKillPilot(int i) {
        switch (i) {
            default:
                break;

            case 2:
                if (this.FM.turret.length > 0) {
                    this.FM.turret[0].bIsOperable = false;
                }
                break;

            case 3:
                if (this.FM.turret.length > 0) {
                    this.FM.turret[1].bIsOperable = false;
                }
                break;

            case 4:
                if (this.FM.turret.length > 0) {
                    this.FM.turret[2].bIsOperable = false;
                }
                break;

            case 5:
                if (this.FM.turret.length > 0) {
                    this.FM.turret[3].bIsOperable = false;
                }
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                break;

            case 4:
                this.hierMesh().chunkVisible("Pilot5_D0", false);
                this.hierMesh().chunkVisible("Pilot5_D1", true);
                break;
        }
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Cart_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("CartRot_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444D)) % 360F;
        this.hierMesh().chunkSetAngles("Cart_D0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("Engine1") && (World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Engine2") && (World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)) {
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("Engine3") && (World.Rnd().nextFloat(0.0F, 0.5F) < shot.mass)) {
            this.FM.AS.hitEngine(shot.initiator, 2, 1);
        }
        if ((this.FM.AS.astateEngineStates[0] > 2) && (this.FM.AS.astateEngineStates[1] > 2)) {
            this.FM.setCapableOfBMP(false, shot.initiator);
        }
        if (shot.chunkName.startsWith("Turret")) {
            this.FM.turret[0].bIsOperable = false;
        }
        if (shot.chunkName.startsWith("Tail1") && (Aircraft.Pd.z > 0.5D) && (Aircraft.Pd.x > -6D) && (Aircraft.Pd.x < -4.95D) && (World.Rnd().nextFloat() < 0.5F)) {
            this.FM.AS.hitPilot(shot.initiator, 2, (int) (shot.mass * 1000F * 0.5F));
        }
        if (shot.chunkName.startsWith("CF") && (Aircraft.v1.x < -0.2D) && (Aircraft.Pd.x > 2.6D) && (Aircraft.Pd.z > 0.735D) && (World.Rnd().nextFloat() < 0.178F)) {
            this.FM.AS.hitPilot(shot.initiator, Aircraft.Pd.y > 0.0D ? 0 : 1, (int) (shot.mass * 900F));
        }
        if (shot.chunkName.startsWith("WingLIn") && (Math.abs(Aircraft.Pd.y) < 2.1D)) {
            this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(0, (int) (shot.mass * 30F)));
        }
        if (shot.chunkName.startsWith("WingRIn") && (Math.abs(Aircraft.Pd.y) < 2.1D)) {
            this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int) (shot.mass * 30F)));
        }
        super.msgShot(shot);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        switch (i) {
            default:
                break;

            case 0:
                if (af[0] < -50F) {
                    af[0] = -50F;
                    flag = false;
                } else if (af[0] > 50F) {
                    af[0] = 50F;
                    flag = false;
                }
                float f = Math.abs(af[0]);
                if (f < 20F) {
                    if (af[1] < -1F) {
                        af[1] = -1F;
                        flag = false;
                    }
                } else if (af[1] < -5F) {
                    af[1] = -5F;
                    flag = false;
                }
                if (af[1] > 45F) {
                    af[1] = 45F;
                    flag = false;
                }
                break;

            case 1:
                if (af[0] < -120F) {
                    af[0] = -120F;
                    flag = false;
                } else if (af[0] > 120F) {
                    af[0] = 120F;
                    flag = false;
                }
                if (af[1] < -85F) {
                    af[1] = -85F;
                    flag = false;
                }
                if (af[1] > 5F) {
                    af[1] = 5F;
                    flag = false;
                }
                break;

            case 2:
                if (af[0] < -60F) {
                    af[0] = -60F;
                    flag = false;
                } else if (af[0] > 45F) {
                    af[0] = 45F;
                    flag = false;
                }
                if (af[1] < -35F) {
                    af[1] = -35F;
                    flag = false;
                }
                if (af[1] > 35F) {
                    af[1] = 35F;
                    flag = false;
                }
                break;

            case 3:
                if (af[0] < -45F) {
                    af[0] = -45F;
                    flag = false;
                } else if (af[0] > 60F) {
                    af[0] = 60F;
                    flag = false;
                }
                if (af[1] < -35F) {
                    af[1] = -35F;
                    flag = false;
                }
                if (af[1] > 35F) {
                    af[1] = 35F;
                    flag = false;
                }
                break;
        }
        return flag;
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
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
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
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
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
        this.fSightCurAltitude = 850F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 10000F) {
            this.fSightCurAltitude = 10000F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 850F) {
            this.fSightCurAltitude = 850F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
        this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 150F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 10F;
        if (this.fSightCurSpeed > 600F) {
            this.fSightCurSpeed = 600F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 10F;
        if (this.fSightCurSpeed < 150F) {
            this.fSightCurSpeed = 150F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
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
            this.fSightCurDistance -= (this.fSightCurSpeed / 3.6F) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude));
            if (this.fSightCurDistance < ((this.fSightCurSpeed / 3.6F) * Math.sqrt(this.fSightCurAltitude * (2F / 9.81F)))) {
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

    public void update(float f) {
        if (this.FM.AS.bLandingLightOn) {
            if (this.llpos < 1.0F) {
                this.llpos += 0.5F * f;
                this.hierMesh().chunkSetAngles("LLight_D0", 0.0F, -90F * this.llpos, 0.0F);
            }
        } else if (this.llpos > 0.0F) {
            this.llpos -= 0.5F * f;
            this.hierMesh().chunkSetAngles("LLight_D0", 0.0F, -90F * this.llpos, 0.0F);
        }
        super.update(f);
    }

    private boolean bDynamoOperational;
    private float   dynamoOrient;
    private boolean bDynamoRotary;
    private int     pk;
    private float   llpos;
    public boolean  bPitUnfocused;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float   fSightCurDistance;
    public float    fSightCurForwardAngle;
    public float    fSightCurSideslip;
    public float    fSightCurAltitude;
    public float    fSightCurSpeed;
    public float    fSightCurReadyness;

    static {
        Class class1 = CaproniXYZ.class;
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
    }
}
