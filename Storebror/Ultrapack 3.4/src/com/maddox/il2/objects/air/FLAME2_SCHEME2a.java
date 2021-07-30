package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;

public abstract class FLAME2_SCHEME2a extends Scheme2a {

    public void update(float f) {
        super.update(f);
        this.CombustionFlame();
    }

    private void CombustionFlame() {
        int i = World.Rnd().nextInt(0, 50);
        int j = World.Rnd().nextInt(1, 6);
        switch (i) {
            case 1:
                this.random = "01";
                break;

            case 8:
                this.random = "02";
                break;
        }
        switch (j) {
            case 1:
                this.random3 = "01";
                break;

            case 2:
                this.random3 = "02";
                break;
        }
        if (this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[0].getPowerOutput() > 0.85F) Eff3DActor.New(this, this.findHook("_Engine1EF_" + this.random3), null, 1.0F, "3DO/Effects/Fireworks/HolyGrail2.eff", -1F);
        if (this.FM.EI.engines[0].getStage() > 1 && this.FM.EI.engines[0].getStage() < 3 && this.FM.getSpeedKMH() < 10F) Eff3DActor.New(this, this.findHook("_Engine1EF_" + this.random), null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1.eff", -1F);
        if (this.FM.EI.engines[1].getStage() == 6 && this.FM.EI.engines[1].getPowerOutput() > 0.85F) Eff3DActor.New(this, this.findHook("_Engine2EF_" + this.random3), null, 1.0F, "3DO/Effects/Fireworks/HolyGrail2.eff", -1F);
        if (this.FM.EI.engines[1].getStage() > 1 && this.FM.EI.engines[1].getStage() < 3 && this.FM.getSpeedKMH() < 10F) Eff3DActor.New(this, this.findHook("_Engine2EF_" + this.random), null, 1.0F, "3DO/Effects/Aircraft/HolyGrail1.eff", -1F);
    }

    private String random;
    private String random3;
}
