package com.maddox.il2.objects.air;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.il2.objects.weapons.Torpedo;
import com.maddox.rts.Property;

public class HE_111H6 extends HE_111
    implements TypeHasToKG
{

    public HE_111H6()
    {
        hasToKG = false;
        spreadAngle = 0;
    }

    public void update(float f)
    {
        if(FM.turret[5].tMode == 2)
            FM.turret[5].tMode = 4;
        super.update(f);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        Object aobj[] = pos.getBaseAttached();
        if(aobj != null)
        {
            for(int i = 0; i < aobj.length; i++)
                if(aobj[i] instanceof Torpedo)
                {
                    hasToKG = true;
                    return;
                }

        }
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(hasToKG)
        {
            fAOB++;
            if(fAOB > 180F)
                fAOB = 180F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] {
                new Integer((int)fAOB)
            });
            ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
        } else
        {
            super.typeBomberAdjSideslipPlus();
        }
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(hasToKG)
        {
            fAOB--;
            if(fAOB < -180F)
                fAOB = -180F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] {
                new Integer((int)fAOB)
            });
            ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
        } else
        {
            super.typeBomberAdjSideslipMinus();
        }
    }

    public void typeBomberAdjSpeedPlus()
    {
        if(hasToKG)
        {
            fShipSpeed++;
            if(fShipSpeed > 35F)
                fShipSpeed = 35F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] {
                new Integer((int)fShipSpeed)
            });
            ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
        } else
        {
            super.typeBomberAdjSpeedPlus();
        }
    }

    public void typeBomberAdjSpeedMinus()
    {
        if(hasToKG)
        {
            fShipSpeed--;
            if(fShipSpeed < 0.0F)
                fShipSpeed = 0.0F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] {
                new Integer((int)fShipSpeed)
            });
            ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
        } else
        {
            super.typeBomberAdjSpeedMinus();
        }
    }

    public void typeBomberAdjAltitudeReset()
    {
        if(!hasToKG)
            super.typeBomberAdjAltitudeReset();
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(!hasToKG)
            super.typeBomberAdjAltitudePlus();
    }

    public void typeBomberAdjAltitudeMinus()
    {
        if(!hasToKG)
            super.typeBomberAdjAltitudeMinus();
    }

    public void typeBomberAdjSpeedReset()
    {
        if(!hasToKG)
            super.typeBomberAdjSpeedReset();
    }

    public void typeBomberAdjDistanceReset()
    {
        if(!hasToKG)
            super.typeBomberAdjDistanceReset();
    }

    public void typeBomberAdjDistancePlus()
    {
        if(!hasToKG)
        {
            super.typeBomberAdjDistancePlus();
        } else
        {
            spreadAngle++;
            if(spreadAngle > 30)
                spreadAngle = 30;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] {
                new Integer(spreadAngle)
            });
            FM.AS.setSpreadAngle(spreadAngle);
        }
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(!hasToKG)
        {
            super.typeBomberAdjDistanceMinus();
        } else
        {
            spreadAngle--;
            if(spreadAngle < 0)
                spreadAngle = 0;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] {
                new Integer(spreadAngle)
            });
            FM.AS.setSpreadAngle(spreadAngle);
        }
    }

    public void typeBomberAdjSideslipReset()
    {
        if(!hasToKG)
            super.typeBomberAdjSideslipReset();
    }

    public boolean isSalvo()
    {
        return thisWeaponsName.indexOf("spread") == -1;
    }

    protected float fAOB;
    protected float fShipSpeed;
    public boolean hasToKG;
    protected int spreadAngle;

    static 
    {
        Class class1 = HE_111H6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "He-111");
        Property.set(class1, "meshName", "3do/plane/He-111H-6/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/He-111H-6.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitHE_111H6.class, CockpitHE_111H6_Bombardier.class, CockpitHE_111H6_NGunner.class, CockpitHE_111H2_TGunner.class, CockpitHE_111H2_BGunner.class, CockpitHE_111H2_LGunner.class, CockpitHE_111H2_RGunner.class
        });
        weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 13, 14, 15, 3, 3, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04"
        });
    }
}
