package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class TA_152C extends TA_152_BASE implements TypeX4Carrier {

    public TA_152C() {
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
    }

    protected void moveFlap(float f) {
        float f1 = -50F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -f1, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM instanceof RealFlightModel && ((RealFlightModel) this.FM).isRealMode() || !flag || !(this.FM instanceof Pilot)) return;
        Pilot pilot = (Pilot) this.FM;
        if (pilot.get_maneuver() == 63 && pilot.target != null) {
            Point3d point3d = new Point3d(pilot.target.Loc);
            point3d.sub(this.FM.Loc);
            this.FM.Or.transformInv(point3d);
            if ((point3d.x > 4000D && point3d.x < 5500D || point3d.x > 100D && point3d.x < 5000D && World.Rnd().nextFloat() < 0.33F) && Time.current() > this.tX4Prev + 10000L) {
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

    public boolean bToFire;
    private long   tX4Prev;
    private float  deltaAzimuth;
    private float  deltaTangage;

    static {
        Class class1 = TA_152C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ta.152");
        Property.set(class1, "meshName", "3DO/Plane/Ta-152C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ta-152C (Ultrapack).fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTA_152C.class });
        Property.set(class1, "LOSElevation", 0.755F);
        weaponTriggersRegister(class1, new int[] { 0, 1, 1, 1, 1, 9, 9, 2, 2, 2, 2, 3, 3 });
        weaponHooksRegister(class1,
                new String[] { "_CANNON01", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalBomb02", "_ExternalBomb03" });
    }
}
