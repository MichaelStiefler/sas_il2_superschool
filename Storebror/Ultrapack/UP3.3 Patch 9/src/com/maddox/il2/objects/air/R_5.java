package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class R_5 extends R_5Bomber
    implements TypeBomber, TypeStormovik
{

    public R_5()
    {
        strafeWithGuns = false;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(FM.turret.length != 0)
        {
            if(thisWeaponsName.startsWith("Gunpods"))
                strafeWithGuns = true;
            this.moveGunner();
        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if((double)getGunnerAnimation() < 1.0D)
        {
            af[0] = 0.0F;
            af[1] = 0.0F;
            return false;
        }
        float f = -af[0];
        float f1 = af[1];
        if(f < -95F)
        {
            f = -95F;
            flag = false;
        }
        if(f > 95F)
        {
            f = 95F;
            flag = false;
        }
        if(f1 > 75F)
        {
            f1 = 75F;
            flag = false;
        }
        if(f1 < -3.5F && f > -12F && f < 12F)
        {
            f1 = -3.5F;
            flag = false;
        } else
        if(f > -37F && f <= -12F && f1 < Aircraft.cvt(f, -37F, -12F, -30F, -3.5F))
        {
            f1 = Aircraft.cvt(f, -37F, -12F, -30F, -3.5F);
            flag = false;
        } else
        if(f >= 12F && f < 37F && f1 < Aircraft.cvt(f, 12F, 37F, -3.5F, -30F))
        {
            f1 = Aircraft.cvt(f, 12F, 37F, -3.5F, -30F);
            flag = false;
        } else
        if(f > -60F && f <= -37F && f1 < Aircraft.cvt(f, -60F, -37F, -45F, -30F))
        {
            f1 = Aircraft.cvt(f, -60F, -37F, -45F, -30F);
            flag = false;
        } else
        if(f >= 37F && f < 60F && f1 < Aircraft.cvt(f, 37F, 60F, -30F, -45F))
        {
            f1 = Aircraft.cvt(f, 37F, 60F, -30F, -45F);
            flag = false;
        } else
        if(f1 < -45F)
        {
            f1 = -45F;
            flag = false;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void update(float f)
    {
        this.updateRadiator();
        super.update(f);
        if(FM.turret.length != 0)
        {
            gunnerAiming();
            gunnerTarget2();
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("HMask2_FAKE", false);
            hierMesh().chunkVisible("HMask3_D0", false);
        } else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
            hierMesh().chunkVisible("HMask2_FAKE", hierMesh().isChunkVisible("Pilot2_FAKE"));
            hierMesh().chunkVisible("HMask3_D0", hierMesh().isChunkVisible("Pilot3_D0"));
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1:
            FM.turret[0].setHealth(f);
            if(f <= 0.0F)
                gunnerDead = true;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D1", hierMesh().isChunkVisible("Pilot2_D0"));
            hierMesh().chunkVisible("Pilot3_D1", hierMesh().isChunkVisible("Pilot3_D0"));
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            gunnerDead = true;
            break;
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        if(i == 2)
        {
            super.doRemoveBodyFromPlane(3);
            gunnerEjected = true;
        }
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(regiment.country().equals(PaintScheme.countryRussia))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0)
                if(i > 0x1282cb5)
                    return "41_";
                else
                    return "";
        }
        return "";
    }

    static 
    {
        Class class1 = R_5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "R-5");
        Property.set(class1, "meshName", "3do/plane/R-5/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar08());
        Property.set(class1, "yearService", 1931F);
        Property.set(class1, "yearExpired", 1944F);
        Property.set(class1, "FlightModel", "FlightModels/R-5.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitR_5.class, CockpitR_5_OP1.class, CockpitR_5_Bombardier.class, CockpitR_5_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 10, 0, 0, 0, 0, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 9,
            9, 9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", 
            "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", 
            "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalDev01",
            "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05"
        });
    }
}
