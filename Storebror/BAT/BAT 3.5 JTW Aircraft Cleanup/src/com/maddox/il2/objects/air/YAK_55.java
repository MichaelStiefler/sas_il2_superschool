package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class YAK_55 extends YAK {

    public YAK_55() {
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = -Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.48F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.75F;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.0F), 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC2a_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("GearC4_D0", 0.0F, 30F * f, 0.0F);
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("Sovok1_D0", 0.0F, -14F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Sovok2_D0", 0.0F, 28F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        for (int i = 1; i < 29; i++) {
            this.hierMesh().chunkSetAngles("St" + i + "_D0", 0.0F, -90F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        }

        super.update(f);
    }

    static {
        Class class1 = YAK_55.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-55/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1977F);
        Property.set(class1, "yearExpired", 2011F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-55.fmd:Yak55_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_55.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01" });
    }
}
