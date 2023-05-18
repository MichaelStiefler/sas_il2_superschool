package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_86F, TypeX4Carrier, PaintSchemeFMPar06, Aircraft, 
//            NetAircraft

public class Hunter_F1 extends Hunter
{

    public Hunter_F1()
    {
    }

    public void engineSurge(float f) {
        if (((FlightModelMain) (super.FM)).AS.isMaster()) {
          if (curthrl == -1F) {
            curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
          } else {
            curthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
            if (curthrl < 1.05F) {
              if ((curthrl - oldthrl) / f > 10.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.40F) {
                if (FM.actor == World.getPlayerAircraft()) {
                  HUD.log("Compressor Stall!");
                }
                super.playSound("weapon.MGunMk108s", true);
                engineSurgeDamage += 1.0000000000000001E-002D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
                ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
                if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
                  FM.AS.hitEngine(this, 0, 100);
                }
                if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
                  FM.EI.engines[0].setEngineDies(this);
                }
              }
              if ((curthrl - oldthrl) / f < -10.0F && (curthrl - oldthrl) / f > -100.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6) {
                super.playSound("weapon.MGunMk108s", true);
                engineSurgeDamage += 1.0000000000000001E-003D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
                ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
                if (World.Rnd().nextFloat() < 0.40F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
                  if (FM.actor == World.getPlayerAircraft()) {
                    HUD.log("Engine Flameout!");
                  }
                  FM.EI.engines[0].setEngineStops(this);
                } else {
                  if (FM.actor == World.getPlayerAircraft()) {
                    HUD.log("Compressor Stall!");
                  }
                }
              }
            }
            oldthrl = curthrl;
          }
        }
      }
      
    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
        }
        catch(Exception exception) { }
        return a_lweaponslot;
    }

    private float oldthrl;
    private float curthrl;
    private float engineSurgeDamage;
    
    static 
    {
        Class localClass = com.maddox.il2.objects.air.Hunter_F1.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "Hunter F1");
        Property.set(localClass, "meshName", "3DO/Plane/Hunter_F1(Multi1)/hier.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(localClass, "yearService", 1949.9F);
        Property.set(localClass, "yearExpired", 1960.3F);
        Property.set(localClass, "FlightModel", "FlightModels/HunterF1.fmd");
        Property.set(localClass, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitHunter.class
        });
        Property.set(localClass, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(localClass, new int[]{
                0, 0, 0, 0, 0, 0, 9, 9, 9, 9,
                9, 3, 3, 9, 3, 3, 9, 2, 2, 9,
                2, 2, 9, 9, 9, 9, 9, 3, 9, 3,
                9, 3, 9, 3
            });
    Aircraft.weaponHooksRegister(localClass, new String[]{
                "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
                "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08",
                "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04",
                "_ExternalDev15", "_ExternalBomb05", "_ExternalDev16", "_ExternalBomb06"
            });
    // 6  = Outer Tank Pylon Left  = _ExternalDev01
    // 7  = Outer Tank Pylon Right = _ExternalDev02
    // 8  = Outer Tank Left        = _ExternalDev03
    // 9  = Outer Tank Right       = _ExternalDev04
    // 10 = Outer Bomb Pylon Left  = _ExternalDev05
    // 11 = Outer Bomb Left        = _ExternalBomb01
    // 12 = Outer Bomb Left Dummy  = _ExternalBomb01
    // 13 = Outer Bomb Pylon Right = _ExternalDev06
    // 14 = Outer Bomb Right       = _ExternalBomb02
    // 15 = Outer Bomb Right Dummy = _ExternalBomb02
    // 16 = AIM9 Pylon Left        = _ExternalDev07
    // 17 = AIM9 Left              = _ExternalRock01
    // 18 = AIM9 Left Dummy        = _ExternalRock01
    // 19 = AIM9 Pylon Right       = _ExternalDev08
    // 20 = AIM9 Right             = _ExternalRock02
    // 21 = AIM9 Right Dummy       = _ExternalRock02

    // 22 = Inner Tank Pylon Left  = _ExternalDev09
    // 23 = Inner Tank Pylon Right = _ExternalDev10
    // 24 = Inner Tank Left        = _ExternalDev11
    // 25 = Inner Tank Right       = _ExternalDev12
    // 26 = Middle Bomb Pylon Left  = _ExternalDev13
    // 27 = Middle Bomb Left        = _ExternalBomb03
    // 28 = Middle Bomb Pylon Right = _ExternalDev14
    // 29 = Middle Bomb Right       = _ExternalBomb04
    // 30 = Inner Bomb Pylon Left  = _ExternalDev15
    // 31 = Inner Bomb Left        = _ExternalBomb05
    // 32 = Inner Bomb Pylon Right = _ExternalDev16
    // 33 = Inner Bomb Right       = _ExternalBomb06

    
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(localClass, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(localClass, "weaponsMap", hashmapint);
            byte byte0 = 40;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(byte0);
            for(int i = 6; i < byte0; i++)
                a_lweaponslot[i] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int j = 0; j < byte0; j++)
                a_lweaponslot[j] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}