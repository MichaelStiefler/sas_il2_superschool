
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3f;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            Skyhawk, TypeTankerDrogue, TypeDockable, Aircraft,
//            PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure,
//            NetAircraft, Cockpit, TypeGSuit

public class SkyhawkA4E extends SkyhawkFuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure
{

    public SkyhawkA4E()
    {
        bChangedPit = false;
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        guidedMissileUtils = new GuidedMissileUtils(this);
        bHasLAUcaps = false;
        lastUpdateTime = -1L;
        lastRareActionTime = -1L;
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16)
                        ((Pylon_USTER_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16)
                        ((Pylon_USMERfw_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_USMERmd_gn16)
                        ((Pylon_USMERmd_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16)
                        ((Pylon_LAU10_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                        ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                        ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                        ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                        ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16)
                        ((FuelTankGun_TankSkyhawkNF_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16)
                        ((FuelTankGun_TankSkyhawk400gal_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16)
                        ((Pylon_Mk4HIPEGpod_gn16)FM.CT.Weapons[i][j]).matHighvis();

                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                        bHasLAUcaps = true;

                    if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                        counterFlareList.add(FM.CT.Weapons[i][j]);
                    else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                        counterChaffList.add(FM.CT.Weapons[i][j]);
                }
            }
    }

    private void checkDeleteLAUcaps()
    {
        if(FM.CT.saveWeaponControl[2])
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                            ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                            ((Pylon_LAU130_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                            ((Pylon_LAU131_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                }
            bHasLAUcaps = false;
        }
    }

    private void checkMk7NukeTailfin()
    {
        hasMk7Nuke = 0;

        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j] instanceof BombGunMk7nuke8kt_gn16)
                    {
                        if(FM.CT.Weapons[i][j].countBullets() > 0)
                            hasMk7Nuke |= 1;
                    }
                    else if(FM.CT.Weapons[i][j] instanceof BombGunMk7nuke22kt_gn16)
                    {
                        if(FM.CT.Weapons[i][j].countBullets() > 0)
                            hasMk7Nuke |= 2;
                    }
            }

        if(hasMk7Nuke == 0)
            return;

        if(FM.CT.getGear() > 0.03F || FM.CT.getFlap() > 0.03F)
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof BombGunMk7nuke8kt_gn16 && FM.CT.Weapons[i][j].countBullets() > 0)
                            ((BombGunMk7nuke8kt_gn16)FM.CT.Weapons[i][j]).extendTailfin(false);
                        else if(FM.CT.Weapons[i][j] instanceof BombGunMk7nuke22kt_gn16 && FM.CT.Weapons[i][j].countBullets() > 0)
                            ((BombGunMk7nuke22kt_gn16)FM.CT.Weapons[i][j]).extendTailfin(false);
                }
        }
        else
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof BombGunMk7nuke8kt_gn16 && FM.CT.Weapons[i][j].countBullets() > 0)
                            ((BombGunMk7nuke8kt_gn16)FM.CT.Weapons[i][j]).extendTailfin(true);
                        else if(FM.CT.Weapons[i][j] instanceof BombGunMk7nuke22kt_gn16 && FM.CT.Weapons[i][j].countBullets() > 0)
                            ((BombGunMk7nuke22kt_gn16)FM.CT.Weapons[i][j]).extendTailfin(true);
                }
        }
    }

    private void checkMk12NukeTailfin()
    {
        hasMk12Nuke = 0;

        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j] instanceof BombGunMk12nuke12kt_gn16 && FM.CT.Weapons[i][j].countBullets() > 0)
                        hasMk12Nuke |= 1;
            }

        if(hasMk12Nuke == 0)
            return;

        if(FM.CT.getGear() > 0.03F || FM.CT.getFlap() > 0.03F)
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof BombGunMk12nuke12kt_gn16 && FM.CT.Weapons[i][j].countBullets() > 0)
                            ((BombGunMk12nuke12kt_gn16)FM.CT.Weapons[i][j]).extendTailfin(false);
                }
        }
        else
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof BombGunMk12nuke12kt_gn16 && FM.CT.Weapons[i][j].countBullets() > 0)
                            ((BombGunMk12nuke12kt_gn16)FM.CT.Weapons[i][j]).extendTailfin(true);
                }
        }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 700L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 1300L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        super.bNoSpoiler = true;
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void update(float f)
    {
        if(lastUpdateTime != Time.current())
        {
            if(bHasLAUcaps)
                checkDeleteLAUcaps();
        }
        super.update(f);
        if(lastUpdateTime != Time.current())
        {
            guidedMissileUtils.update();
            if(super.backfire)
                backFire();
            lastUpdateTime = Time.current();
        }
    }

    public void missionStarting()
    {
        super.missionStarting();

        checkChangeWeaponColors();
        checkMk7NukeTailfin();
        checkMk12NukeTailfin();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);

        if(lastRareActionTime != Time.current())
        {
            if(hasMk7Nuke > 0)
                checkMk7NukeTailfin();
            if(hasMk12Nuke > 0)
                checkMk12NukeTailfin();
            lastRareActionTime = Time.current();
        }
    }

    public boolean bChangedPit;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private boolean bHasLAUcaps;
    private byte hasMk7Nuke = 0;
    private byte hasMk12Nuke = 0;
    private long lastUpdateTime;
    private long lastRareActionTime;

    static
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4E(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1962F);
        Property.set(class1, "yearExpired", 1982F);
        Property.set(class1, "FlightModel", "FlightModels/a4e.fmd:SKYHAWKS");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSkyhawkA4F.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 7
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",        "_CANNON02",        "_MGUN01",          "_MGUN02",          "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalDev01",
            "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb01",  "_ExternalBomb06",
            "_ExternalBomb07",  "_ExternalBomb08",  "_ExternalBomb09",  "_ExternalBomb10",  "_ExternalBomb11",  "_ExternalBomb12",  "_Bomb13",          "_ExternalBomb14",  "_Bomb15",          "_ExternalBomb16",
            "_Bomb17",          "_ExternalBomb18",  "_ExternalRock01",  "_ExternalRock01",  "_ExternalRock02",  "_ExternalRock02",  "_ExternalRock03",  "_ExternalRock03",  "_ExternalRock04",  "_ExternalRock04",
            "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev10",   "_ExternalDev11",
            "_ExternalDev12",   "_ExternalDev10",   "_ExternalDev11",   "_ExternalDev12",   "_Rock05",          "_Rock06",          "_Rock07",          "_Rock08",          "_Rock09",          "_Rock10",
            "_Rock11",          "_Rock12",          "_Rock13",          "_Rock14",          "_Rock15",          "_Rock16",          "_Rock17",          "_Rock18",          "_Rock19",          "_Rock20",
            "_Rock21",          "_Rock22",          "_Rock23",          "_Rock24",          "_Rock25",          "_Rock26",          "_Rock27",          "_Rock28",          "_ExternalRock43",  "_ExternalRock43",
            "_ExternalRock29",  "_ExternalRock29",  "_ExternalRock30",  "_ExternalRock30",  "_ExternalRock31",  "_ExternalRock31",  "_ExternalRock32",  "_ExternalRock32",  "_ExternalRock33",  "_ExternalRock33",
            "_ExternalRock34",  "_ExternalRock34",  "_ExternalRock35",  "_ExternalRock35",  "_ExternalRock36",  "_ExternalRock36",  "_ExternalRock37",  "_ExternalRock37",  "_ExternalRock38",  "_ExternalRock38",
            "_ExternalRock39",  "_ExternalRock39",  "_ExternalRock40",  "_ExternalRock40",  "_Rock41",          "_Rock42",          "_Bomb19",          "_Bomb20",          "_ExternalBomb21",  "_ExternalBomb22",
            "_Bomb23",          "_ExternalBomb24",  "_ExternalBomb25",  "_Bomb26",          "_ExternalBomb27",  "_ExternalBomb28",  "_Flare01"
        });
    }
}