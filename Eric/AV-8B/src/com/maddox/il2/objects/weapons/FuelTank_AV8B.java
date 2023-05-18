// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 5/17/2012 6:26:12 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FuelTank_AV8B.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.*;
import com.maddox.il2.objects.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.*;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_AV8B extends FuelTank
{

    public FuelTank_AV8B()
    {
    }    

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_AV8B.class;
        Property.set(class1, "mesh", "3do/Arms/TankAV8B/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 184F);
    }
}