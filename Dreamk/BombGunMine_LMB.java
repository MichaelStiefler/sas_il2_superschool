     package com.maddox.il2.objects.weapons;
     
     import com.maddox.rts.Property;
     
     public class BombGunMine_LMB  extends BombGun
     {
    	   public BombGunMine_LMB()
    	    {
    	    }
    	   public void setBombDelay(float f)
    	    {
    	        super.bombDelay = 0.0F;
    	        if(super.bomb != null)
    	            super.bomb.delayExplosion = super.bombDelay;
    	    }

    	    
              
       static
       {
       Class localClass = BombGunMine_LMB.class;
       Property.set(localClass, "bulletClass",(Object)com.maddox.il2.objects.weapons.BombMine_LMB.class);
       Property.set(localClass, "bullets", 1);
       Property.set(localClass, "shotFreq", 1.0F);
       Property.set(localClass, "external", 1);
       Property.set(localClass, "sound", "weapon.bombgun_torpedo");
       }
     }


 
 