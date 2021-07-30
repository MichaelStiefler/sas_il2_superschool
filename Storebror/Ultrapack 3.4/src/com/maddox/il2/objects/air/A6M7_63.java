package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class A6M7_63 extends A6M {

    public A6M7_63() {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xx")) {
            if (s.equals("xxarmorg")) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(44F, 46F), shot);
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                Aircraft.debugprintln(this, "*** Armor Glass: Hit..");
                if (shot.power <= 0.0F) {
                    Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                    if (World.Rnd().nextFloat() < 0.5F) this.doRicochetBack(shot);
                }
                return;
            }
            if (s.equals("xxarmors")) {
                this.getEnergyPastArmor(8D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-006D), shot);
                return;
            }
            if (s.startsWith("xxammor")) {
                if (World.Rnd().nextFloat() < 0.01F) {
                    this.debuggunnery("Armament: Machine Gun Chain Broken..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                return;
            }
            if (s.startsWith("xxammowmgl")) {
                if (World.Rnd().nextFloat() < 0.01F) {
                    this.debuggunnery("Armament: Machine Gun Chain Broken..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                return;
            }
            if (s.startsWith("xxammowmgr")) {
                if (World.Rnd().nextFloat() < 0.01F) {
                    this.debuggunnery("Armament: Machine Gun Chain Broken..");
                    this.FM.AS.setJamBullets(0, 2);
                }
                return;
            }
            if (s.startsWith("xxtank")) {
                int i = s.charAt(6) - 49;
                if (i < 3) {
                    if (this.getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.45F) {
                        if (this.FM.AS.astateTankStates[i] == 0) {
                            this.debuggunnery("Fuel Tank (" + i + "): Pierced..");
                            this.FM.AS.hitTank(shot.initiator, i, 2);
                            this.FM.AS.doSetTankState(shot.initiator, i, 2);
                        }
                        if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.995F) {
                            this.FM.AS.hitTank(shot.initiator, i, 1);
                            this.debuggunnery("Fuel Tank (" + i + "): Hit..");
                        }
                    }
                    return;
                }
            }
        }
        super.hitBone(s, shot, point3d);
    }

    static {
        Class class1 = A6M7_63.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A6M");
        Property.set(class1, "meshName", "3DO/Plane/A6M7(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_ja", "3DO/Plane/A6M7(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar02());
        Property.set(class1, "yearService", 1943.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/A6M7_Model63.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA6M7_63.class });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 1, 1, 9, 3, 9, 9, 2, 2, 2, 2, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03",
                "_ExternalRock04", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07" });
    }
}
