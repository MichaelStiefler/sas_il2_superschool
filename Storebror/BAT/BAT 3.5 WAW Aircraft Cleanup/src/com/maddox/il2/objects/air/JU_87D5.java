package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class JU_87D5 extends JU_87 implements TypeStormovik {

    public JU_87D5() {
        this.flapps = 0.2F;
        this.fDiveVelocity = 500F;
        this.fDiveAngle = 70F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.58F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
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
        this.FM.CT.dvCockpitDoor = 0.65F;
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 5; i++) {
                String s = "Water" + i + "_D0";
                this.hierMesh().chunkSetAngles(s, 0.0F, 15F - (30F * f1), 0.0F);
            }

        }
        this.fDiveAngle = -this.FM.Or.getTangage();
        if (this.fDiveAngle > 89F) {
            this.fDiveAngle = 89F;
        }
        if (this.fDiveAngle < 10F) {
            this.fDiveAngle = 10F;
        }
        super.update(f);
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
        this.fDiveVelocity += 10F;
        if (this.fDiveVelocity > 700F) {
            this.fDiveVelocity = 700F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjVelocityMinus() {
        this.fDiveVelocity -= 10F;
        if (this.fDiveVelocity < 150F) {
            this.fDiveVelocity = 150F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fDiveVelocity) });
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals(PaintScheme.countryRomania)) {
            return PaintScheme.countryRomania + "_";
        } else {
            return "";
        }
    }

    private float flapps;
    public float  fDiveVelocity;
    public float  fDiveAngle;

    static {
        Class class1 = JU_87D5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87D-5.fmd");
        Property.set(class1, "meshName", "3DO/Plane/Ju-87D-5/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87D5.class, CockpitJU_87D3_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 10, 10, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07" });
    }
}
