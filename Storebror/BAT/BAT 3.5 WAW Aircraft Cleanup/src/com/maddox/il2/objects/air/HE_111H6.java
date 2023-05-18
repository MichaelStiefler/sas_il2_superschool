package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.il2.objects.weapons.Torpedo;
import com.maddox.rts.Property;

public class HE_111H6 extends HE_111 implements TypeHasToKG {

    public HE_111H6() {
        this.hasToKG = false;
        this.spreadAngle = 0;
    }

    public void update(float f) {
        if (this.FM.turret[5].tMode == 2) {
            this.FM.turret[5].tMode = 4;
        }
        super.update(f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            for (int i = 0; i < aobj.length; i++) {
                if (aobj[i] instanceof Torpedo) {
                    this.hasToKG = true;
                    return;
                }
            }

        }
    }

    public void typeBomberAdjSideslipPlus() {
        if (this.hasToKG) {
            this.fAOB++;
            if (this.fAOB > 180F) {
                this.fAOB = 180F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) this.fAOB) });
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
        } else {
            super.typeBomberAdjSideslipPlus();
        }
    }

    public void typeBomberAdjSideslipMinus() {
        if (this.hasToKG) {
            this.fAOB--;
            if (this.fAOB < -180F) {
                this.fAOB = -180F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] { new Integer((int) this.fAOB) });
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
        } else {
            super.typeBomberAdjSideslipMinus();
        }
    }

    public void typeBomberAdjSpeedPlus() {
        if (this.hasToKG) {
            this.fShipSpeed++;
            if (this.fShipSpeed > 35F) {
                this.fShipSpeed = 35F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
        } else {
            super.typeBomberAdjSpeedPlus();
        }
    }

    public void typeBomberAdjSpeedMinus() {
        if (this.hasToKG) {
            this.fShipSpeed--;
            if (this.fShipSpeed < 0.0F) {
                this.fShipSpeed = 0.0F;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] { new Integer((int) this.fShipSpeed) });
            ToKGUtils.setTorpedoGyroAngle(this.FM, this.fAOB, this.fShipSpeed);
        } else {
            super.typeBomberAdjSpeedMinus();
        }
    }

    public void typeBomberAdjAltitudeReset() {
        if (!this.hasToKG) {
            super.typeBomberAdjAltitudeReset();
        }
    }

    public void typeBomberAdjAltitudePlus() {
        if (!this.hasToKG) {
            super.typeBomberAdjAltitudePlus();
        }
    }

    public void typeBomberAdjAltitudeMinus() {
        if (!this.hasToKG) {
            super.typeBomberAdjAltitudeMinus();
        }
    }

    public void typeBomberAdjSpeedReset() {
        if (!this.hasToKG) {
            super.typeBomberAdjSpeedReset();
        }
    }

    public void typeBomberAdjDistanceReset() {
        if (!this.hasToKG) {
            super.typeBomberAdjDistanceReset();
        }
    }

    public void typeBomberAdjDistancePlus() {
        if (!this.hasToKG) {
            super.typeBomberAdjDistancePlus();
        } else {
            this.spreadAngle++;
            if (this.spreadAngle > 30) {
                this.spreadAngle = 30;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] { new Integer(this.spreadAngle) });
            this.FM.AS.setSpreadAngle(this.spreadAngle);
        }
    }

    public void typeBomberAdjDistanceMinus() {
        if (!this.hasToKG) {
            super.typeBomberAdjDistanceMinus();
        } else {
            this.spreadAngle--;
            if (this.spreadAngle < 0) {
                this.spreadAngle = 0;
            }
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] { new Integer(this.spreadAngle) });
            this.FM.AS.setSpreadAngle(this.spreadAngle);
        }
    }

    public void typeBomberAdjSideslipReset() {
        if (!this.hasToKG) {
            super.typeBomberAdjSideslipReset();
        }
    }

    public boolean isSalvo() {
        return this.thisWeaponsName.indexOf("spread") == -1;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if ((regiment == null) || (regiment.country() == null)) {
            return "";
        }
        if (regiment.country().equals(PaintScheme.countryHungary)) {
            return PaintScheme.countryHungary + "_";
        }
        if (regiment.country().equals(PaintScheme.countryRomania)) {
            return PaintScheme.countryRomania + "_";
        } else {
            return "";
        }
    }

    protected float fAOB;
    protected float fShipSpeed;
    public boolean  hasToKG;
    protected int   spreadAngle;

    static {
        Class class1 = HE_111H6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111H-6/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111H-6.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHE_111H6.class, CockpitHE_111H6_Bombardier.class, CockpitHE_111H6_NGunner.class, CockpitHE_111H2_TGunner.class, CockpitHE_111H2_BGunner.class, CockpitHE_111H2_LGunner.class, CockpitHE_111H2_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 15, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04" });
    }
}
