package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.Random;

import com.maddox.il2.objects.weapons.MGunMGFFki;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class BF_109D1 extends BF_109B1 {

    public BF_109D1() {
        this.burst_fire = new int[2][2];
    }

    public void update(float f) {
        if (this.getGunByHookName("_CANNON03") instanceof MGunMGFFki) {
            this.hierMesh().chunkVisible("NoseCannon1_D0", true);
            if ((this.FM.EI.engines[0].tOilOut > 70F) || (this.FM.EI.engines[0].getControlThrottle() > 0.8F)) {
                Random random = new Random();
                int i = 1;
                if (this.FM.CT.WeaponControl[i]) {
                    for (int j = 0; j < 1; j++) {
                        int l = this.FM.CT.Weapons[i][j].countBullets();
                        if (l < this.burst_fire[j][1]) {
                            this.burst_fire[j][0]++;
                            this.burst_fire[j][1] = l;
                            int i1 = Math.abs(random.nextInt()) % 100;
                            float f1 = this.burst_fire[j][0] * 1.0F;
                            if (i1 < f1) {
                                this.FM.AS.setJamBullets(i, j);
                            }
                        }
                    }

                } else {
                    for (int k = 0; k < 1; k++) {
                        this.burst_fire[k][0] = 0;
                        this.burst_fire[k][1] = this.FM.CT.Weapons[i][k].countBullets();
                    }

                }
            }
        }
        super.update(f);
    }

    private int burst_fire[][];

    static {
        Class class1 = BF_109D1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109");
        Property.set(class1, "meshName", "3DO/Plane/BF_109D1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(class1, "meshName_de", "3DO/Plane/BF_109D1/hier.him");
        Property.set(class1, "PaintScheme_de", new PaintSchemeFCSPar01());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1941F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109D-1SAS.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109Bx.class });
        Property.set(class1, "LOSElevation", 0.74985F);
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON01", "_CANNON02" });
    }
}
