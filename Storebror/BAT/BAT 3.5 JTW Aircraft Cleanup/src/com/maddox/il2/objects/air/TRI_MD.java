package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TRI_MD extends JB implements TypeBomber {

    public TRI_MD() {
        this.bHasBoosters = true;
        this.boosterFireOutTime = -1L;
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightSetForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
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
            if ((this.FM.EI.engines[2].getPowerOutput() > 0.8F) && (this.FM.EI.engines[2].getStage() == 6)) {
                if (this.FM.EI.engines[2].getPowerOutput() > 0.95F) {
                    this.FM.AS.setSootState(this, 2, 3);
                } else {
                    this.FM.AS.setSootState(this, 2, 2);
                }
            } else {
                this.FM.AS.setSootState(this, 2, 0);
            }
            if ((this.FM.EI.engines[3].getPowerOutput() > 0.8F) && (this.FM.EI.engines[3].getStage() == 6)) {
                if (this.FM.EI.engines[3].getPowerOutput() > 0.95F) {
                    this.FM.AS.setSootState(this, 3, 3);
                } else {
                    this.FM.AS.setSootState(this, 3, 2);
                }
            } else {
                this.FM.AS.setSootState(this, 3, 0);
            }
        }
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteF86/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(1.0F);
            this.chute.pos.setRel(new Point3d(-17D, 0.0D, 0.7D), new Orient(0.0F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                    this.chute.pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                }
                this.chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute.destroy();
        }
        if ((this.FM.getSpeedKMH() > 500F) && this.FM.CT.bHasFlapsControl) {
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
        } else {
            this.FM.CT.bHasFlapsControl = true;
        }
        if (this.FM.CT.getFlap() < this.FM.CT.FlapsControl) {
            this.FM.CT.forceFlaps(this.flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 500F, 0.5F, 0.08F)));
        } else {
            this.FM.CT.forceFlaps(this.flapsMovement(f, this.FM.CT.FlapsControl, this.FM.CT.getFlap(), 999F, Aircraft.cvt(this.FM.getSpeedKMH(), 0.0F, 500F, 0.5F, 0.7F)));
        }
        float f2 = this.FM.getSpeedKMH() - 500F;
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        this.FM.CT.dvGear = 0.2F - (f2 / 500F);
        if (this.FM.CT.dvGear < 0.0F) {
            this.FM.CT.dvGear = 0.0F;
        }
    }

    private float flapsMovement(float f, float f1, float f2, float f3, float f4) {
        float f5 = (float) Math.exp(-f / f3);
        float f6 = f1 + ((f2 - f1) * f5);
        if (f6 < f1) {
            f6 += f4 * f;
            if (f6 > f1) {
                f6 = f1;
            }
        } else if (f6 > f1) {
            f6 -= f4 * f;
            if (f6 < f1) {
                f6 = f1;
            }
        }
        return f6;
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

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 < -1F) {
                    f1 = -1F;
                    flag = false;
                }
                if (f1 > 80F) {
                    f1 = 80F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 < -80F) {
                    f1 = -80F;
                    flag = false;
                }
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                    flag = false;
                }
                break;

            case 2:
                if (f1 < -1F) {
                    f1 = -1F;
                    flag = false;
                }
                if (f1 > 80F) {
                    f1 = 80F;
                    flag = false;
                }
                break;

            case 3:
                if (f1 < -80F) {
                    f1 = -80F;
                    flag = false;
                }
                if (f1 > 1.0F) {
                    f1 = 1.0F;
                    flag = false;
                }
                break;

            case 4:
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 35F) {
                    f = 35F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
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

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float f) {
    }

    private boolean       bHasDeployedDragChute;
    private Chute         chute;
    private long          removeChuteTimer;
    protected boolean     bHasBoosters;
    protected long        boosterFireOutTime;
    public static boolean bChangedPit = false;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurForwardAngle;
    public float          fSightSetForwardAngle;
    public float          fSightCurSideslip;

    static {
        Class class1 = TRI_MD.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "3M");
        Property.set(class1, "meshName", "3DO/Plane/3MD/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/3MD.fmd:3MD");
        Property.set(class1, "cockpitClass", new Class[] { Cockpit3MD.class, Cockpit3MD_Bombardier.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04" });
    }
}
