package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class E13A1 extends E13A
{
  private static Point3d tmpp = new Point3d();

  public void update(float paramFloat)
  {
    super.update(paramFloat);
    for (int i = 0; i < 3; i++) for (int j = 0; j < 2; j++)
        if (this.FM.Gears.clpGearEff[i][j] != null) {
          tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
          tmpp.z = 0.01D;
          this.FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
          this.FM.Gears.clpGearEff[i][j].pos.reset();
        }
  }

  static
  {
    Class localClass = E13A1.class;
    new NetAircraft.SPAWN(localClass);

    Property.set(localClass, "FlightModel", "FlightModels/E13A1.fmd");

    Property.set(localClass, "iconFar_shortClassName", "E13A");

    Property.set(localClass, "meshName", "3DO/Plane/E13A(MULTI1)/hier.him");
    Property.set(localClass, "PaintScheme", new PaintSchemeBMPar00());
    Property.set(localClass, "meshName_ja", "3DO/Plane/E13A(JA)/hier.him");
    Property.set(localClass, "PaintScheme_ja", new PaintSchemeFCSPar02());

    Property.set(localClass, "cockpitClass", new Class[] { CockpitE13A1.class, CockpitE13A1_Bombardier.class, CockpitE13A1_Gunner.class });

    Property.set(localClass, "yearService", 1941.5F);
    Property.set(localClass, "yearExpired", 1945.5F);

    weaponTriggersRegister(localClass, new int[] { 10, 3, 3, 3, 3 });
    weaponHooksRegister(localClass, new String[] { "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02" });

    weaponsRegister(localClass, "default", new String[] { "MGunVikkersKt 582", null, null, null, null });

    weaponsRegister(localClass, "2x30", new String[] { "MGunVikkersKt 582", null, null, "BombGun30kgJ 1", "BombGun30kgJ 1" });

    weaponsRegister(localClass, "2x50", new String[] { "MGunVikkersKt 582", null, null, "BombGun50kgJ 1", "BombGun50kgJ 1" });

    weaponsRegister(localClass, "4x30", new String[] { "MGunVikkersKt 582", "BombGun30kgJ 1", "BombGun30kgJ 1", "BombGun30kgJ 1", "BombGun30kgJ 1" });

    weaponsRegister(localClass, "4x50", new String[] { "MGunVikkersKt 582", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1", "BombGun50kgJ 1" });

    weaponsRegister(localClass, "2x100", new String[] { "MGunVikkersKt 582", "BombGun100kgJ 1", "BombGun100kgJ 1", null, null });

    weaponsRegister(localClass, "none", new String[] { null, null, null, null, null });
  }
}