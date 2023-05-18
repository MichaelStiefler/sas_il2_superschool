package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class Mk6 extends Scheme1 implements TypeScout, TypeTransport {

    public Mk6() {
        this.bHasBlister = true;
        this.fMaxKMHSpeedForOpenCanopy = 170F;
    }

    public void moveCockpitDoor(float f1) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", -160F * f1, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f1);
            }
            this.setDoorSnd(f1);
        }
    }

    public void update(float f1) {
        super.update(f1);
        if ((this.FM.CT.getCockpitDoor() > 0.20000000000000001D) && this.bHasBlister && (this.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy) && (this.hierMesh().chunkFindCheck("Blister1_D0") != -1)) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.bHasBlister = false;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.setGCenter(-0.3F);
        }
    }

    public boolean bHasBlister;
    private float  fMaxKMHSpeedForOpenCanopy;

    static {
        Class class1 = Mk6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Gomhouria Mk6");
        Property.set(class1, "meshName", "3DO/Plane/Bu_181/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
        Property.set(class1, "yearService", 1939.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/Mk6.fmd:Bu-181_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBu_181.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
