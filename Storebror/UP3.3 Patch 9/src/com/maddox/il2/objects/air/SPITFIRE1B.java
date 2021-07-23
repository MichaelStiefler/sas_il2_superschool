package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;

public class SPITFIRE1B extends SPITFIRE {

    public SPITFIRE1B() {
        this.burst_fire = new int[2][2];
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.55F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float) Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, (float) Math.PI));
        this.hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        this.hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        // TODO: Fixed by SAS~Storebror, fixing a bad attempt to compute absolute value of signed random integer, since Math.abs(Integer.MIN_VALUE) == Integer.MIN_VALUE!
//        Random random = new Random();
        int i = 1;
        if (this.FM.getOverload() > 2.0F || this.FM.getOverload() < 0.0F || this.FM.getAltitude() > 5000F) if (this.FM.CT.WeaponControl[i]) for (int j = 0; j < 2; j++) {
            int l = this.FM.CT.Weapons[i][j].countBullets();
            if (l < this.burst_fire[j][1]) {
                this.burst_fire[j][0]++;
                this.burst_fire[j][1] = l;
                // TODO: Fixed by SAS~Storebror, fixing a bad attempt to compute absolute value of signed random integer, since Math.abs(Integer.MIN_VALUE) == Integer.MIN_VALUE!
//                int i1 = Math.abs(random.nextInt()) % 100;
                int i1 = TrueRandom.nextInt(0, 100);
                float f1 = this.burst_fire[j][0] * 1.0F;
                if (i1 < f1) this.FM.AS.setJamBullets(i, j);
            }
        }
        else for (int k = 0; k < 2; k++) {
            this.burst_fire[k][0] = 0;
            this.burst_fire[k][1] = this.FM.CT.Weapons[i][k].countBullets();
        }
        super.update(f);
    }

    public static boolean bChangedPit = false;
    private int           burst_fire[][];

    static {
        Class class1 = SPITFIRE1B.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Spit");
        Property.set(class1, "meshName", "3DO/Plane/SpitfireMk1b(Multi1)/Spitfire1b.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "meshName_gb", "3DO/Plane/SpitfireMk1b(Multi1)/Spitfire1b.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeSPIT1());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/SpitfireIb.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSpit1.class });
        Property.set(class1, "LOSElevation", 0.5926F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02" });
    }
}
