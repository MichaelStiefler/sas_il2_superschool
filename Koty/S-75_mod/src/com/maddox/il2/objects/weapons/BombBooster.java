			package com.maddox.il2.objects.weapons;
			import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
			
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
			
			public class BombBooster extends BombFAB100
			{
			  public BombBooster(Actor owner,Vector3d speed,Orient orient,Point3d pos) 
			  {
				  //init(1f, 1f);
				  this.setOwner(owner);
				  this.pos.setAbs(pos, orient);
				  this.setSpeed(speed);
			  }
			  
			  static {
			    Class localClass = BombBooster.class;
			    Property.set(localClass, "mesh", "3do/SAM/Dvina/V-755/Booster_.sim");
			    
			    Property.set(localClass, "power", 5.0F);
			    Property.set(localClass, "powerType", 0);
			    Property.set(localClass, "kalibr", 0.52F);
			    Property.set(localClass, "massa", 900.0F);
			    Property.set(localClass, "sound", "weapon.bomb_mid");
			  }
			}
			




