package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;

public abstract class J1N1F extends J1Nxyz {

    public J1N1F() {
        this.bChangedPit = true;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            this.bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -35F) {
                    f = -35F;
                    flag = false;
                }
                if (f > 35F) {
                    f = 35F;
                    flag = false;
                }
                if (f1 < -20F) {
                    f1 = -20F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 30F * f, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        boolean flag1 = (this.FM.CT.Weapons[10] != null) && this.FM.CT.Weapons[10][0].haveBullets();
        if (!flag1) {
            this.FM.turret[0].bIsOperable = false;
        }
        if (this.gunnerDead) {
            this.FM.turret[0].bIsOperable = false;
        }
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public boolean bChangedPit;
}
