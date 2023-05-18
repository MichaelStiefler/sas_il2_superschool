/*    */ package com.maddox.il2.objects.weapons;
/*    */ 
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ParatrooperSoldierAirborn;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.rts.Property;
 
 public class BombParaSoldierAirborn extends Bomb
 {
   public BombParaSoldierAirborn() {}
   
public void start() {
    Loc var1 = new Loc();
    new Orient();
    Class var3 = this.getClass();
    this.init(Property.floatValue(var3, "kalibr", 0.082F), Property.floatValue(var3, "massa", 6.8F));
    this.setOwner(this.pos.base(), false, false, false);
    this.pos.setBase((Actor)null, (Hook)null, true);
    this.pos.setAbs(this.pos.getCurrent());
    this.pos.getAbs(var1);
    new ParatrooperSoldierAirborn(this.getOwner(), this.getOwner().getArmy(), 255, var1, ((Aircraft)this.getOwner()).FM.Vwld);
    this.destroy();
 }

   static
   {
     Class localClass = BombParaSoldierAirborn.class;
     Property.set(localClass, "mesh", "3DO/Arms/Null/mono.sim");
     Property.set(localClass, "radius", 0.0F);
     Property.set(localClass, "power", 0.0F);
     Property.set(localClass, "powerType", 0);
     Property.set(localClass, "kalibr", 0.5F);
     Property.set(localClass, "massa", 100.0F);
   }
 }
