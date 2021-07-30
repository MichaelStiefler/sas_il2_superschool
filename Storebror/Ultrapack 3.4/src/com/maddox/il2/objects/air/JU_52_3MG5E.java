package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class JU_52_3MG5E extends JU_52 implements TypeTransport, TypeSailPlane {

    public JU_52_3MG5E() {
        this.tmpp = new Point3d();
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 2; j++)
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    this.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    this.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(this.tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }

    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -50F) {
            af[0] = -50F;
            flag = false;
        } else if (af[0] > 50F) {
            af[0] = 50F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (f < 20F) {
            if (af[1] < -1F) {
                af[1] = -1F;
                flag = false;
            }
        } else if (af[1] < -5F) {
            af[1] = -5F;
            flag = false;
        }
        if (af[1] > 45F) {
            af[1] = 45F;
            flag = false;
        }
        return flag;
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 1, 1);
        if (this.FM.AS.astateEngineStates[0] > 2 && this.FM.AS.astateEngineStates[1] > 2) this.FM.setCapableOfBMP(false, shot.initiator);
        super.msgShot(shot);
    }

    private Point3d tmpp;

    static {
        Class class1 = JU_52_3MG5E.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-52_3mg5e.fmd");
        Property.set(class1, "meshName", "3do/plane/Ju-52_3mg5e/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-52");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1938.5F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU525E.class, CockpitJU525E_GunnerOpen.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_BombSpawn01" });
    }
}
