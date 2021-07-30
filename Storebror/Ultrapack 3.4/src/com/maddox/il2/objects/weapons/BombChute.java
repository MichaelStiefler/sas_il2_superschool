package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.objects.air.Chute;

public class BombChute extends Chute {
    public BombChute(Actor actor, float scale) {
        super(actor);
        String meshName = "3do/arms/BombChute/mono_" + (int) (scale * 100F) + ".sim";
        Mesh mesh = null;
        try {
            mesh = new Mesh(meshName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mesh == null) System.out.println("Bomb Chute Mesh " + meshName + " could not be loaded, using default mesh instead!");
        else this.mesh = mesh;
    }
}
