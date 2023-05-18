
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class F2A2 extends F2A {

    public F2A2() {
        this.bChangedExts = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) {
            F2A2.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        this.bChangedExts = true;
        if (this.FM.isPlayers()) {
            F2A2.bChangedPit = true;
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.bChangedExts) {
            this.doFixBellyDoor();
        }
    }

    public void doFixBellyDoor() {
        this.hierMesh().chunkVisible("CF1_D0", this.hierMesh().isChunkVisible("CF_D0"));
        this.hierMesh().chunkVisible("CF1_D1", this.hierMesh().isChunkVisible("CF_D1"));
        this.hierMesh().chunkVisible("CF1_D2", this.hierMesh().isChunkVisible("CF_D2"));
        this.hierMesh().chunkVisible("CF1_D3", this.hierMesh().isChunkVisible("CF_D3"));
        this.hierMesh().chunkVisible("Engine11_D0", this.hierMesh().isChunkVisible("Engine1_D0"));
        this.hierMesh().chunkVisible("Engine11_D1", this.hierMesh().isChunkVisible("Engine1_D1"));
        this.hierMesh().chunkVisible("Engine11_D2", this.hierMesh().isChunkVisible("Engine1_D2"));
        this.bChangedExts = false;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.725F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    static Class _mthclass$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public boolean        bChangedExts;
    public static boolean bChangedPit = false;

    static {
        Class class1 = F2A2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F2A");
        Property.set(class1, "meshName", "3DO/Plane/F2A-2(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/F2A-2(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFCSPar01());
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/F2A-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF2A2.class });
        Property.set(class1, "LOSElevation", 1.032F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
