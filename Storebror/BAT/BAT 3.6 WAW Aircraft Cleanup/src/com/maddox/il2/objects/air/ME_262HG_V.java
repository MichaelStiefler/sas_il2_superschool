package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.util.HashMapInt;

public class ME_262HG_V extends ME_262HGII implements TypeX4Carrier {

    public ME_262HG_V() {
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.blisterRemoved = new boolean[2];
        this.blisterRemoved[0] = false;
        this.blisterRemoved[1] = false;
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 60F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 60F * f, 0.0F);
    }

    public void blisterRemoved(int i) {
        if ((i < 1) || (i > 2)) {
            return;
        } else {
            this.blisterRemoved[i - 1] = true;
            return;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (((super.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !flag || !(this.FM instanceof Pilot)) {
            return;
        }
        Pilot pilot = (Pilot) this.FM;
        if ((pilot.get_maneuver() == 63) && (((Maneuver) (pilot)).target != null)) {
            Point3d point3d = new Point3d(((FlightModelMain) (((Maneuver) (pilot)).target)).Loc);
            point3d.sub(this.FM.Loc);
            this.FM.Or.transformInv(point3d);
            if ((((point3d.x > 4000D) && (point3d.x < 5500D)) || ((point3d.x > 100D) && (point3d.x < 5000D) && (World.Rnd().nextFloat() < 0.33F))) && (Time.current() > (this.tX4Prev + 10000L))) {
                this.bToFire = true;
                this.tX4Prev = Time.current();
            }
        }
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
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

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, boolean flag) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.11F, 0.89F, 0.0F, 111F), 0.0F);
        hiermesh.chunkSetAngles("GearC21_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f2, 0.11F, 0.89F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.89F, 0.0F, 73F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.11F, 0.89F, 0.0F, 73F), 0.0F);
        if (f2 < 0.5F) {
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.89F, 0.99F, 90F, 0.0F), 0.0F);
        }
        if (f < 0.5F) {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 88F), 0.0F);
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -130F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.89F, 0.99F, 88F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.89F, 0.99F, -130F, -90F), 0.0F);
        }
        if (f1 < 0.5F) {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.11F, 0.0F, 88F), 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.11F, 0.0F, -130F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.89F, 0.99F, 88F, 0.0F), 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.89F, 0.99F, -130F, -90F), 0.0F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        ME_262HG_V.moveGear(hiermesh, f, f1, f2, true);
    }

    protected void moveGear(float f, float f1, float f2) {
        ME_262HG_V.moveGear(this.hierMesh(), f, f1, f2, this.FM.CT.GearControl > 0.5F);
    }

    public static void moveGear(HierMesh hiermesh, float f, boolean flag) {
        ME_262HG_V.moveGear(hiermesh, f, f, f, flag);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        ME_262HG_V.moveGear(hiermesh, f, f, f, true);
    }

    protected void moveGear(float f) {
        ME_262HG_V.moveGear(this.hierMesh(), f, this.FM.CT.GearControl > 0.5F);
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

    protected void moveFan(float f) {
        for (int i = 0; i < this.FM.EI.getNum(); i++) {
            this.propPos[i] = (this.propPos[i] + (57.3F * this.FM.EI.engines[i].getw() * f)) % 360F;
            this.hierMesh().chunkSetAngles(Aircraft.Props[i][0], 0.0F, -this.propPos[i], 0.0F);
        }

    }

    public boolean bToFire;
    private long   tX4Prev;
    private float  deltaAzimuth;
    private float  deltaTangage;
    public boolean blisterRemoved[];

    static {
        Class class1 = ME_262HG_V.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me 262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262HG-V/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1958.2F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262HG-V.fmd:Me262HGV_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262HG_V.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24" });
    }
}
