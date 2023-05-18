package com.maddox.il2.objects.air;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.il2.objects.weapons.PylonWB151;
import com.maddox.il2.objects.weapons.PylonWB20;
import com.maddox.il2.objects.weapons.PylonWB81A;
import com.maddox.il2.objects.weapons.PylonWB81B;
import com.maddox.rts.Property;

public class JU_87D7 extends JU_87 implements TypeStormovik {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) break;
                if (aobj[i] instanceof PylonWB81B || aobj[i] instanceof PylonWB81A || aobj[i] instanceof PylonWB20 || aobj[i] instanceof PylonWB151) {
                    this.FM.M.massEmpty += 190F;
                    this.FM.CT.bHasAirBrakeControl = false;
                    this.hierMesh().chunkVisible("Brake01_D0", false);
                    this.hierMesh().chunkVisible("Brake02_D0", false);
                    this.hierMesh().chunkVisible("WingLMid_D0", false);
                    this.hierMesh().chunkVisible("WingRMid_D0", false);
                    this.hierMesh().chunkVisible("WingLMid_D0_D7WOB", true);
                    this.hierMesh().chunkVisible("WingRMid_D0_D7WOB", true);
                    break;
                }
                i++;
            } while (true);
        }
    }

    protected void moveAirBrake(float f) {
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) break;
                if (!(aobj[i] instanceof PylonWB81B) && !(aobj[i] instanceof PylonWB81A) && !(aobj[i] instanceof PylonWB20) && !(aobj[i] instanceof PylonWB151)) {
                    this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
                    this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
                    break;
                }
                i++;
            } while (true);
        }
    }

    public void typeDiveBomberAdjAltitudePlus() {
        if (this.getGunByHookName("_MGUN05") instanceof GunEmpty && this.getGunByHookName("_MGUN17") instanceof GunEmpty && this.getGunByHookName("_CANNON03") instanceof GunEmpty && this.getGunByHookName("_CANNON07") instanceof GunEmpty) {
            this.fDiveRecoveryAlt += 25F;
            if (this.fDiveRecoveryAlt > 6000F) this.fDiveRecoveryAlt = 6000F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
        }
    }

    public void typeDiveBomberAdjAltitudeMinus() {
        if (this.getGunByHookName("_MGUN05") instanceof GunEmpty && this.getGunByHookName("_MGUN17") instanceof GunEmpty && this.getGunByHookName("_CANNON03") instanceof GunEmpty && this.getGunByHookName("_CANNON07") instanceof GunEmpty) {
            this.fDiveRecoveryAlt -= 25F;
            if (this.fDiveRecoveryAlt < 0.0F) this.fDiveRecoveryAlt = 0.0F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fDiveRecoveryAlt) });
        }
    }

    public void update(float f) {
        for (int i = 1; i < 5; i++)
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, 15F - 30F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);

        super.update(f);
    }

    static {
        Class class1 = JU_87D7.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87D-7.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-87D-3/hier_D7.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-87");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87D3.class, CockpitJU_87D3_Gunner.class });
        Property.set(class1, "LOSElevation", 0.8499F);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb05",
                        "_ExternalBomb06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_CANNON03",
                        "_CANNON04", "_CANNON05", "_CANNON06", "_MGUN17", "_MGUN18", "_MGUN19", "_MGUN20", "_MGUN21", "_MGUN22", "_MGUN23", "_MGUN24", "_MGUN25", "_MGUN26", "_MGUN27", "_MGUN28", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON10",
                        "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04" });
    }
}
