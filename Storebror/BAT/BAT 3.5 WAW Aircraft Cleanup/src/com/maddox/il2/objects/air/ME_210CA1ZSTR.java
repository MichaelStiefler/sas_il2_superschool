package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.rts.Property;

public class ME_210CA1ZSTR extends ME_210 implements TypeBNZFighter, TypeStormovik, TypeStormovikArmored {

    public ME_210CA1ZSTR() {
        this.phase = 0;
        this.disp = 0.0F;
        this.oldbullets = 0;
        this.g1 = null;
    }

    protected void moveBayDoor(float f) {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.CT.Weapons[1] != null) {
            this.g1 = this.FM.CT.Weapons[1][0];
        }
    }

    public void update(float paramFloat) {
        if ((this.g1 != null) && (this.oldbullets != this.g1.countBullets())) {
            switch (this.phase) {
                default:
                    break;

                case 0:
                    if (this.g1.isShots()) {
                        this.oldbullets = this.g1.countBullets();
                        this.phase = 1;
                        this.disp = 0.0F;
                    }
                    break;

                case 1:
                    this.disp += 31.2F * paramFloat;
                    this.resetYPRmodifier();
                    Aircraft.xyz[0] = this.disp / 2.0F;
                    this.hierMesh().chunkSetLocate("BK37_BARREL", Aircraft.xyz, Aircraft.ypr);
                    if (this.disp >= 0.5F) {
                        this.phase = 2;
                    }
                    break;

                case 2:
                    this.disp -= 4.3F * paramFloat;
                    this.resetYPRmodifier();
                    Aircraft.xyz[0] = this.disp / 2.0F;
                    this.hierMesh().chunkSetLocate("BK37_BARREL", Aircraft.xyz, Aircraft.ypr);
                    if (this.disp <= 0.0F) {
                        this.phase = 3;
                    }
                    break;

                case 3:
                    this.phase = 0;
                    break;
            }
        }
        super.update(paramFloat);
    }

    private int           phase;
    private float         disp;
    private int           oldbullets;
    private BulletEmitter g1;

    static {
        Class class1 = ME_210CA1ZSTR.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me-210");
        Property.set(class1, "meshName", "3DO/Plane/Me-210Ca-1Zerstorer/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "meshName_hu", "3DO/Plane/Me-210Ca-1Zerstorer(hu)/hier.him");
        Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-210Ca-1.fmd:ME210410_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_410B.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 10, 10, 1, 9, 9, 2, 2, 2, 2, 9, 9, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_MGUN03", "_MGUN04", "_CANNON03", "_ExternalDev23", "_ExternalDev24", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalDev25", "_ExternalDev26", "_ExternalRock05", "_ExternalRock06" });
    }
}
