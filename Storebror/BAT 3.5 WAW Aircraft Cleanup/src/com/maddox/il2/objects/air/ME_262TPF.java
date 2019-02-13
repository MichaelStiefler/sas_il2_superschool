package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ME_262TPF extends ME_262B implements TypeX4Carrier, TypeRadarLiSN2Carrier {

    public ME_262TPF() {
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.headPos = new float[3];
        this.headOr = new float[3];
        this.curPilot = 1;
        this.blisterRemoved = new boolean[2];
        this.blisterRemoved[0] = false;
        this.blisterRemoved[1] = false;
        this.radarGain = 50;
        this.radarMode = 0;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (((super.FM instanceof RealFlightModel) && ((RealFlightModel) super.FM).isRealMode()) || !flag || !(super.FM instanceof Pilot)) {
            return;
        }
        Pilot pilot = (Pilot) super.FM;
        if ((pilot.get_maneuver() == 63) && (((Maneuver) (pilot)).target != null)) {
            Point3d point3d = new Point3d(((FlightModelMain) (((Maneuver) (pilot)).target)).Loc);
            point3d.sub(((FlightModelMain) (super.FM)).Loc);
            ((FlightModelMain) (super.FM)).Or.transformInv(point3d);
            if ((((((Tuple3d) (point3d)).x > 4000D) && (((Tuple3d) (point3d)).x < 5500D)) || ((((Tuple3d) (point3d)).x > 100D) && (((Tuple3d) (point3d)).x < 5000D) && (World.Rnd().nextFloat() < 0.33F))) && (Time.current() > (this.tX4Prev + 10000L))) {
                this.bToFire = true;
                this.tX4Prev = Time.current();
            }
        }
    }

    public void typeX4CAdjSidePlus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = 1.0F;
            return;
        }
        this.radarMode++;
        if (this.radarMode > 1) {
            this.radarMode = 0;
        }
    }

    public void typeX4CAdjSideMinus() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = -1F;
            return;
        }
        this.radarMode--;
        if (this.radarMode < 0) {
            this.radarMode = 1;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        if (this.curPilot == 1) {
            this.deltaTangage = 1.0F;
            return;
        }
        this.radarGain += 10;
        if (this.radarGain > 100) {
            this.radarGain = 100;
        }
    }

    public void typeX4CAdjAttitudeMinus() {
        if (this.curPilot == 1) {
            this.deltaTangage = -1F;
            return;
        }
        this.radarGain -= 10;
        if (this.radarGain < 0) {
            this.radarGain = 0;
        }
    }

    public void typeX4CResetControls() {
        if (this.curPilot == 1) {
            this.deltaAzimuth = this.deltaTangage = 0.0F;
            return;
        } else {
            this.radarGain = 50;
            return;
        }
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0: // '\0'
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!((FlightModelMain) (super.FM)).AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1: // '\001'
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                if (!((FlightModelMain) (super.FM)).AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore3_D0", true);
                }
                break;
        }
    }

    public void update(float f) {
        if (super.FM.isPlayers() && !Main3D.cur3D().isViewOutside()) {
            this.hierMesh().chunkVisible("Blister1_D0", false);
        } else {
            this.hierMesh().chunkVisible("Blister1_D0", true);
        }
        if (((FlightModelMain) (super.FM)).AS.bIsAboutToBailout) {
            this.hierMesh().chunkVisible("Blister1_D0", false);
        }
        super.update(f);
    }

    public void setCurPilot(int theCurPilot) {
        this.curPilot = theCurPilot;
    }

    public int getCurPilot() {
        return this.curPilot;
    }

    public int getRadarGain() {
        return this.radarGain;
    }

    public int getRadarMode() {
        return this.radarMode;
    }

    public void blisterRemoved(int i) {
        if ((i < 1) || (i > 2)) {
            return;
        } else {
            this.blisterRemoved[i - 1] = true;
            return;
        }
    }

    public void movePilotsHead(float f, float f1) {
        if (Config.isUSE_RENDER() && ((this.headTp < f1) || (this.headTm > f1) || (this.headYp < f) || (this.headYm > f))) {
            this.headTp = f1 + 0.0005F;
            this.headTm = f1 - 0.0005F;
            this.headYp = f + 0.0005F;
            this.headYm = f - 0.0005F;
            f *= 0.7F;
            f1 *= 0.7F;
            tmpOrLH.setYPR(0.0F, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, f, 0.0F);
            tmpOrLH.increment(f1, 0.0F, 0.0F);
            tmpOrLH.increment(0.0F, 0.0F, (-0.2F * f1) + (0.05F * f));
            this.headOr[0] = tmpOrLH.getYaw();
            this.headOr[1] = tmpOrLH.getPitch();
            this.headOr[2] = tmpOrLH.getRoll();
            this.headPos[0] = 0.0005F * Math.abs(f);
            this.headPos[1] = -0.0001F * Math.abs(f);
            this.headPos[2] = 0.0F;
            this.hierMesh().chunkSetLocate("Head" + this.curPilot + "_D0", this.headPos, this.headOr);
        }
    }

    public boolean        bToFire;
    private long          tX4Prev;
    private float         deltaAzimuth;
    private float         deltaTangage;
    private float         headPos[];
    private float         headOr[];
    private static Orient tmpOrLH = new Orient();
    private float         headYp;
    private float         headTp;
    private float         headYm;
    private float         headTm;
    private int           curPilot;
    public boolean        blisterRemoved[];
    private int           radarGain;
    private int           radarMode;

    static {
        Class class1 = ME_262TPF.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me262-TPF");
        Property.set(class1, "meshName", "3DO/Plane/ME_262TPF/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.1F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262B-1a.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262NJ.class, CockpitME_262RearRadar.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev03", "_ExternalDev04", "_CANNON05", "_CANNON06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev01", "_ExternalDev02", "_ExternalRock25", "_ExternalRock25", "_ExternalRock26", "_ExternalRock26", "_ExternalDev07", "_ExternalDev08" });
    }
}
