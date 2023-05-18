  package com.maddox.il2.objects.weapons;
  
  import com.maddox.JGP.Color3f;
  import com.maddox.JGP.Point3d;
  import com.maddox.JGP.Tuple3d;
  import com.maddox.JGP.Vector3d;
  import com.maddox.il2.ai.RangeRandom;
  import com.maddox.il2.ai.World;
  import com.maddox.il2.engine.Actor;
  import com.maddox.il2.engine.Eff3DActor;
  import com.maddox.il2.engine.Engine;
  import com.maddox.il2.engine.Loc;
  import com.maddox.il2.engine.Orient;
  import com.maddox.rts.Property;
  import java.util.List;
  
  
  public class RocketFlare
    extends Rocket
  {
    public void start(float f, int i)
    {
      float f1 = 30.0F;
      super.start(f1, i);
      
      this.speed.z = -20.0D;
      this.speed.x = 0.0D;
      this.speed.y = 0.0D;
      Vector3d vector3d = new Vector3d(0.0D, 0.0D, 0.0D);
      vector3d.x += World.Rnd().nextFloat_Dome(-2.0F, 2.0F);
      vector3d.y += World.Rnd().nextFloat_Dome(-1.2F, 1.2F);
      
      this.speed.add(vector3d);
      
  
      setSpeed(this.speed);
      Eff3DActor.New(this, null, new Loc(), 1.0F, "3do/Effects/Fireworks/Piropatron.eff", f1);
      Eff3DActor.New(this, null, new Loc(), 0.8F, "3do/effects/rocket/rocketsmokewhitestart.eff", f1);
      
      Engine.countermeasures().add(this);
    }
    
    protected void doExplosion(Actor actor, String s)
    {
      destroy();
      Engine.missiles().remove(this);
    }
    
  
  
  
  
    static Orient Or = new Orient();
    private static Point3d p = new Point3d();
    private static Vector3d v = new Vector3d();
    
    static
    {
      Class class1 = RocketFlare.class;
      Property.set(class1, "mesh", "3do/arms/Piropatron/mono.sim");
      //Property.set(class1, "sprite", "3DO/Effects/GunFire/20mm/GunFlare.eff");//null
      Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
      Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
      Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
      Property.set(class1, "smokeStart", "3do/effects/rocket/rocketsmokewhitestart.eff");
      Property.set(class1, "smokeTile", "3do/effects/rocket/rocketsmokewhitetile.eff");
      Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
      Property.set(class1, "emitLen", 5.0F);
      Property.set(class1, "emitMax", 20.0F);
      //Property.set(class1, "sound", null);
      Property.set(class1, "timeLife", 1.5F);
      Property.set(class1, "timeFire", 0.01F);//0.1F);
      Property.set(class1, "force", 741F);//210.0F);
      //Property.set(class1, "dragCoefficient", 0.3F);
      Property.set(class1, "powerType", 0);
      Property.set(class1, "power", 0.01F);
      Property.set(class1, "radius", 0.01F);
      Property.set(class1, "kalibr", 0.001F);//0.03
      Property.set(class1, "massa", 0.247F);
      Property.set(class1, "massaEnd", 0.247F);
      Property.set(class1, "shotFreq", 3.0F);
    }
    
    public RocketFlare() {}
    
    protected void doExplosionAir() {}
  }
 
/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\EngMod
 * Qualified Name:     com.maddox.il2.objects.weapons.RocketFlare
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */