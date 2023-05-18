  package com.maddox.il2.objects.weapons;
  
  import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

import java.util.List;
  
  
  public class GunFlare
    extends Gun
  {
	  public void start(float f, int i)
	    {Engine.countermeasures().add(this);}
	  protected void doExplosion(Actor actor, String s)
	    {
	      destroy();
	      Engine.missiles().remove(this);
	    }
    
   
    	
    	   public GunProperties createProperties() {
    		     GunProperties gunproperties = super.createProperties();
    		     gunproperties.bCannon = false;
    		     gunproperties.bUseHookAsRel = true;
    		     gunproperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
    		     gunproperties.fire = "3DO/Effects/GunFire/30mm/GunFire.eff";
    		     gunproperties.sprite = "3DO/Effects/GunFire/30mm/GunFlare.eff";
    		     gunproperties.smoke = "effects/smokes/MachineGun.eff";
    		     gunproperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
    		     gunproperties.sound = "weapon.mgun_30_500";
    		     gunproperties.customSound = "weapon.cannon_mk103";
    		     gunproperties.emitColor = new com.maddox.JGP.Color3f(1.0F, 1.0F, 0.0F);
    		     gunproperties.emitI = 2.5F;
    		     gunproperties.emitR = 1.5F;
    		     gunproperties.emitTime = 0.03F;
    		     gunproperties.aimMinDist = 10.0F;
    		     gunproperties.aimMaxDist = 1000.0F;
    		     gunproperties.weaponType = -1;
    		     gunproperties.maxDeltaAngle = 0.43F;
    		     gunproperties.shotFreq = 3.0F;
    		     gunproperties.traceFreq = 1;
    		     gunproperties.bullets = 50;
    		     gunproperties.bulletsCluster = 1;
    		     gunproperties.bullet = new BulletProperties[] { new BulletProperties()};
    		     gunproperties.bullet[0].massa = 0.247F;
    		     gunproperties.bullet[0].kalibr = 6.67E-4F;
    		     gunproperties.bullet[0].speed = 30.0F;
    		     gunproperties.bullet[0].power = 0;
    		     gunproperties.bullet[0].powerType = 0;
    		     gunproperties.bullet[0].powerRadius = 0F;
    		     gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmYellow/mono.sim";
    		     gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
    		     gunproperties.bullet[0].traceColor = -771686401;
    		     gunproperties.bullet[0].timeLife = 3.3F;
    		     return gunproperties;
    		   }
    		 
      
    
    
    public GunFlare() {}
    
    protected void doExplosionAir() {}
  }
 
/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\EngMod
 * Qualified Name:     com.maddox.il2.objects.weapons.RocketFlare
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */