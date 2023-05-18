package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombZB360 extends Bomb {

  public BombZB360() {
  }

  public void start()
  {
      super.start();
  }

  public void msgCollision(Actor actor, String s, String s1)
  {
      doFireContaineds();
      Point3d point3d = new Point3d();
      pos.getTime(Time.current(), point3d);
      Class class1 = getClass();
      float f = Property.floatValue(class1, "power", 0.0F);
      int i = Property.intValue(class1, "powerType", 0);
      float f1 = Property.floatValue(class1, "radius", 1.0F);
      MsgExplosion.send(actor, s1, point3d, getOwner(), M, f, i, f1);
  }

  private void doFireContaineds()
  {
      Actor actor = null;
      if(Actor.isValid(getOwner()))
          actor = getOwner();
      Orient orient = new Orient();
      Vector3d vector3d = new Vector3d();
      Point3d point3d = new Point3d(pos.getAbsPoint());
      point3d.z += 2D;
      for(int i = 0; i < 15; i++)
      {
          orient.set(World.Rnd().nextFloat(0.0F, 360F), World.Rnd().nextFloat(-90F, 90F), World.Rnd().nextFloat(-180F, 180F));
          getSpeed(vector3d);
          Vector3d vector3d1 = vector3d;
          vector3d1.add(World.Rnd().nextFloat(30F, -30F), World.Rnd().nextFloat(30F, -30F), 0.0D);
          vector3d1.z = 2D;
          if(i < 3)
          {
              vector3d1.x *= 0.55000001192092896D;
              vector3d1.y *= 0.55000001192092896D;
              BombletFire2 bombletfire2 = new BombletFire2();
              ((Bomb) (bombletfire2)).pos.setUpdateEnable(true);
              ((Bomb) (bombletfire2)).pos.setAbs(point3d, orient);
              ((Bomb) (bombletfire2)).pos.reset();
              bombletfire2.start();
              bombletfire2.setOwner(actor, false, false, false);
              bombletfire2.setSpeed(vector3d1);
          } else
          {
              vector3d1.x *= 0.60000002384185791D;
              vector3d1.y *= 0.60000002384185791D;
              BombletFire bombletfire = new BombletFire();
              ((Bomb) (bombletfire)).pos.setUpdateEnable(true);
              ((Bomb) (bombletfire)).pos.setAbs(point3d, orient);
              ((Bomb) (bombletfire)).pos.reset();
              bombletfire.start();
              bombletfire.setOwner(actor, false, false, false);
              bombletfire.setSpeed(vector3d1);
          }
      }

      postDestroy();
  }

  
  static {
    Class class1 = com.maddox.il2.objects.weapons.BombZB360.class;
    Property.set(class1, "mesh", "3DO/Arms/ZB-360/mono.sim");
    Property.set(class1, "radius", 30F);
    Property.set(class1, "power", 30F);
    Property.set(class1, "powerType", 2);
    Property.set(class1, "kalibr", 0.5F);
    Property.set(class1, "massa", 345F);
    Property.set(class1, "sound", "weapon.bomb_mid");
  }
}