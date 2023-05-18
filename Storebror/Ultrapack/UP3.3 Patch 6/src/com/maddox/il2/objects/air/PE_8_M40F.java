package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class PE_8_M40F extends PE_8xyz
{

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        float f3 = Math.max(-f * 800F, -50F);
        float f4 = Math.max(-f1 * 800F, -50F);
        if(f3 > -2F)
            f3 = 0.0F;
        if(f4 > -2F)
            f4 = 0.0F;
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f3, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f4, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f4, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -80.3F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -80.3F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -33F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -33F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, -34.7F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, -34.7F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, -125F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, -125F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, -11.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, -11.5F * f1, 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, f, f);
    }
    
    public void update(float f)
    {
        float f1 = FM.EI.engines[0].getControlRadiator() * 30F;
        if(Math.abs(flapps[0] - f1) > 0.01F)
        {
            flapps[0] = f1;
            hierMesh().chunkSetAngles("Radiator1_D0", 0.0F, f1, 0.0F);
            hierMesh().chunkSetAngles("Radiator4_D0", 0.0F, -f1, 0.0F);
            hierMesh().chunkSetAngles("Radiator2_D0", -f1 * 1.3F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Radiator3_D0", f1 * 1.3F, 0.0F, 0.0F);
        }
        float f2 = FM.EI.engines[1].getControlRadiator() * 30F;
        if(Math.abs(flapps[1] - f2) > 0.01F)
        {
            flapps[1] = f2;
            hierMesh().chunkSetAngles("Radiator5_D0", 0.0F, f2, 0.0F);
            hierMesh().chunkSetAngles("Radiator8_D0", 0.0F, -f2, 0.0F);
            hierMesh().chunkSetAngles("Radiator6_D0", -f2 * 1.3F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Radiator7_D0", f2 * 1.3F, 0.0F, 0.0F);
        }
        super.update(f);
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(!regiment.country().equals(PaintScheme.countryRussia))
            return "multi_";
        else
            return "";
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        s = s.toLowerCase();
        if(s.startsWith("xxtank"))
        {
            int i = s.charAt(6) - 49;
            if(i >= 4)
                return;
            if(getEnergyPastArmor(1.0F, shot) > 0.0F)
            {
                if(FM.AS.astateTankStates[i] == 0 && World.Rnd().nextFloat() < 0.1F)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if(shot.powerType == 3)
                {
                    if(shot.power < 18000F)
                    {
                        if(FM.AS.astateTankStates[i] < 4 && World.Rnd().nextFloat() < 0.21F)
                            FM.AS.hitTank(shot.initiator, i, 1);
                    } else
                    {
                        FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(1, 1 + (int)(shot.power / 18000F)));
                    }
                } else
                if(shot.power > 18000F)
                    FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(1, 1 + (int)(shot.power / 18000F)));
            }
            return;
        } else
        {
            super.hitBone(s, shot, point3d);
            return;
        }
    }

    private float flapps[] = {
        0.0F, 0.0F
    };

    static 
    {
        Class class1 = PE_8_M40F.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Pe-8");
        Property.set(class1, "meshName_ru", "3DO/Plane/Pe-8_M40F/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeBMPar02());
        Property.set(class1, "meshName", "3DO/Plane/Pe-8_M40F/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Pe-8M40.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitPE_8.class, CockpitPE_8_Bombardier.class, CockpitPE_8_NGunner.class, CockpitPE_8_TGunner.class, CockpitPE_8_WLGunner.class, CockpitPE_8_WRGunner.class, CockpitPE_8_RGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 10, 11, 12, 13, 14, 3, 3, 3, 3, 
            3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 
            3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 
            3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 
            3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 
            3, 9, 3, 3, 3, 3, 9, 3, 3, 3, 
            3, 9, 3, 3, 9, 3, 3, 9, 3, 3, 
            9, 3, 3, 9, 3, 3, 9, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02", 
            "_BombSpawn03", "_ExternalDev01", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev02", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", 
            "_ExternalBomb10", "_ExternalDev03", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalDev04", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", 
            "_ExternalBomb18", "_ExternalDev05", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_ExternalDev06", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", 
            "_BombSpawn11", "_ExternalDev07", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_ExternalDev08", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", 
            "_BombSpawn19", "_ExternalDev09", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_ExternalDev10", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", 
            "_BombSpawn27", "_ExternalDev11", "_ExternalBomb19", "_ExternalBomb20", "_ExternalDev12", "_ExternalBomb21", "_ExternalBomb22", "_ExternalDev13", "_BombSpawn28", "_BombSpawn29", 
            "_ExternalDev14", "_BombSpawn30", "_BombSpawn31", "_ExternalDev15", "_BombSpawn32", "_BombSpawn33", "_ExternalDev16", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36"
        });
    }
}
