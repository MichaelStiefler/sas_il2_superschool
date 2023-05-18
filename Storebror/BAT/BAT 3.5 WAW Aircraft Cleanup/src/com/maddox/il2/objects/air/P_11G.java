package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.rts.Property;

public class P_11G extends P_11 {

    public P_11G() {
        this.bHasBlister = true;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            for (int i = 0; i < aobj.length; i++) {
                if (aobj[i] instanceof Bomb) {
                    this.hierMesh().chunkVisible("RackL_D0", true);
                    this.hierMesh().chunkVisible("RackR_D0", true);
                }
            }

        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        if (f < 0.1F) {
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.08F, 0.0F, 2.0F);
            Aircraft.xyz[0] = 0.0F + (0.0F * (float) Math.cos(((Aircraft.ypr[1] * 3.141593F) / 180F) + 0.0F));
            Aircraft.xyz[2] = 0.0F + (0.0F * (float) Math.sin(((Aircraft.ypr[1] * 3.141593F) / 180F) + 0.0F));
        } else {
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.15F, 0.99F, 2.0F, 120F);
            Aircraft.xyz[0] = 0.0F + (0.0F * (float) Math.cos(((Aircraft.ypr[1] * 3.141593F) / 180F) + 0.0F));
            Aircraft.xyz[2] = 0.0F + (0.0F * (float) Math.sin(((Aircraft.ypr[1] * 3.141593F) / 180F) + 0.0F));
        }
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        super.update(f);
        if ((this.FM.CT.getCockpitDoor() > 0.20000000000000001D) && this.bHasBlister && (this.FM.getSpeedKMH() > 150F) && (this.hierMesh().chunkFindCheck("Blister1_D0") != -1)) {
            try {
                if (this == World.getPlayerAircraft()) {
                    ((CockpitP_11G) Main3D.cur3D().cockpitCur).removeCanopy();
                }
            } catch (Exception exception) {
            }
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.bHasBlister = false;
            this.FM.CT.bHasCockpitDoorControl = false;
        }
    }

    public static boolean bChangedPit = false;
    public boolean        bHasBlister;
    static {
        Class class1 = P_11G.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "P.11");
        Property.set(class1, "meshName", "3DO/Plane/P-11g(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_pl", "3DO/Plane/P-11g/hier.him");
        Property.set(class1, "PaintScheme_pl", new PaintSchemeFCSPar01());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1939.5F);
        Property.set(class1, "FlightModel", "FlightModels/P-11g.fmd:P11G");
        Property.set(class1, "cockpitClass", new Class[] { CockpitP_11G.class });
        Property.set(class1, "LOSElevation", 0.7956F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
