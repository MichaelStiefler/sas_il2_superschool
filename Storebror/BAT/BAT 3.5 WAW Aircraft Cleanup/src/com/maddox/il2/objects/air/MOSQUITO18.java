package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class MOSQUITO18 extends MOSQUITO implements TypeFighter, TypeStormovik {

    public MOSQUITO18() {
        this.oldbullets = -123;
        this.recoilTime = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("4x303")) {
            this.hierMesh().chunkVisible("ExtraGuns_D0", true);
        }
        if (this.thisWeaponsName.startsWith("2x303+rockets") || this.thisWeaponsName.startsWith("2xdt")) {
            this.hierMesh().chunkVisible("Rack1_D0", false);
            this.hierMesh().chunkVisible("Rack2_D0", false);
        }
        if (!this.thisWeaponsName.startsWith("none")) {
            this.g1 = this.FM.CT.Weapons[1][0];
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("TODO")) {
            if (this.getEnergyPastArmor(World.Rnd().nextFloat(3F, 15F), shot) > 0.0F) {
                this.debuggunnery("Armament: Mollins Disabled..");
                this.FM.AS.setJamBullets(1, 0);
                this.getEnergyPastArmor(World.Rnd().nextFloat(5F, 23.325F), shot);
            }
            return;
        } else {
            super.hitBone(s, shot, point3d);
            return;
        }
    }

    public void update(float f) {
        if (this.g1 != null) {
            if (this.recoilTime > 0.0F) {
                this.recoilTime += f;
                float f1 = this.recoilTime * (4.8F + (this.recoilTime * (-8.3F + (this.recoilTime * 3.5F))));
                if (this.recoilTime > 1.0F) {
                    f1 = 0.0F;
                    this.recoilTime = 0.0F;
                    this.oldbullets = this.g1.countBullets();
                }
                this.resetYPRmodifier();
                Aircraft.xyz[1] = -f1;
                this.hierMesh().chunkSetLocate("MolinsCannon_D0", Aircraft.xyz, Aircraft.ypr);
                if (World.cur().diffCur.Realistic_Gunnery) {
                    float f2 = (float) this.FM.getBallAccel().y;
                    f2 = ((f2 * f2) - 0.2F) * 0.2F * f;
                    if (f2 > World.Rnd().nextFloat()) {
                        this.FM.AS.setJamBullets(1, 0);
                    }
                }
            } else if (this.g1.isShots() && (this.oldbullets != this.g1.countBullets())) {
                this.recoilTime += f;
            }
        }
        super.update(f);
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "tse_";
    }

    private BulletEmitter g1;
    private int           oldbullets;
    private float         recoilTime;

    static {
        Class class1 = MOSQUITO18.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mosquito");
        Property.set(class1, "meshName", "3DO/Plane/Mosquito_FB_MkVI(Multi1)/hier_TSE.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_gb", "3DO/Plane/Mosquito_FB_MkVI(GB)/hier_TSE.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/Mosquito-MkXVIII.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMosquito18.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
