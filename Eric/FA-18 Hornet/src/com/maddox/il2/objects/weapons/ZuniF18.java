// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 7/22/2013 4:53:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Pylon_Zuni.java

package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.F_18C;
import com.maddox.il2.objects.air.F_18S;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Pylon

public class ZuniF18 extends Bomb
{

    public ZuniF18()
    {
    	rate = 4;
    	
    }
    
    public void start()
    {
        super.start();
        drawing(true);
    }
    
    public void interpolateTick()
    {    	
    	ammocount();	
    }
    
    public Aircraft aircraft()
       {
        return ((Aircraft)this.fm.actor);
       }
    
    private void ammocount()
    {   	
    	if(((F_18C)aircraft()).FM.CT.Weapons[1] != null)
    	g1 = ((F_18C)aircraft()).FM.CT.Weapons[1][0];
    	rate = g1.countBullets();
    	if(rate == 3)
    		setMesh("3DO/Arms/Mk82LGBD/mono3.sim");
    	if(rate == 2)
    		setMesh("3DO/Arms/Mk82LGBD/mono2.sim");
    	if(rate == 1)
    		setMesh("3DO/Arms/Mk82LGBD/mono1.sim");
    	if(rate == 0)
    		setMesh("3DO/Arms/Mk82LGBD/mono0.sim");
    }
    
    private BulletEmitter g1;
    private float rate;
    public FlightModel fm = null;

    static 
    {
    	Class class1 = com.maddox.il2.objects.weapons.ZuniF18.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_ZuniF18/mono.sim");
        Property.set(class1, "radius", 1F);
        Property.set(class1, "power", 1F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1F);
        Property.set(class1, "massa", 15F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}