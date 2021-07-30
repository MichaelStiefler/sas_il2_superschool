package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.BombGunTorpMk13;
import com.maddox.il2.objects.weapons.BombGunTorpMk13late;
import com.maddox.rts.Property;

public class A_20G extends A_20 implements TypeStormovik {

    public A_20G() {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else i = s.charAt(5) - 49;
            if (i == 2) i = 1;
            this.hitFlesh(i, shot, byte0);
            return;
        } else {
            super.hitBone(s, shot, point3d);
            return;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 > 89F) {
                    f1 = 89F;
                    flag = false;
                }
                float f2 = Math.abs(f);
                if (f1 < cvt(f2, 140F, 180F, -1F, 25F)) f1 = cvt(f2, 140F, 180F, -1F, 25F);
                break;

            case 1:
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 15F) {
                    f1 = 15F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                break;

            case 2:
                this.FM.turret[1].setHealth(f);
                break;
        }
    }

    public void moveCockpitDoor(float f) {
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, cvt(f, 0.01F, 0.99F, 0.0F, -120F), 0.0F);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 0, 1);
            if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 1, 1);
            if (this.FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 2, 1);
            if (this.FM.AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F) this.FM.AS.hitTank(this, 3, 1);
        }
        for (int i = 1; i < 4; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.CT.Weapons[3] != null && (this.FM.CT.Weapons[3][0] instanceof BombGunTorpMk13 || this.FM.CT.Weapons[3][0] instanceof BombGunTorpMk13late)) {
            this.hierMesh().chunkVisible("Bay1_D0", false);
            this.hierMesh().chunkVisible("Bay2_D0", false);
        }
    }

    static {
        Class class1 = A_20G.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-20");
        Property.set(class1, "meshName", "3DO/Plane/A-20G(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_gb", "3DO/Plane/A-20G(AU)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/A-20G(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar03());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1965.5F);
        Property.set(class1, "FlightModel", "FlightModels/A-20G.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_20G.class, CockpitA_20G_TGunner.class, CockpitA_20G_BGunner.class });
        Property.set(class1, "LOSElevation", 0.92575F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 10, 10, 11, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05" });
    }
}
