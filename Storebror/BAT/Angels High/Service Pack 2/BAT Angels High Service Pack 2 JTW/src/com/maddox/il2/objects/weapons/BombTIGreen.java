package com.maddox.il2.objects.weapons;

public class BombTIGreen extends BombTiBase2 {
    protected Bomb spawnNewBomb() {
        return new BombTiGr();
    }

    static {
        BombTiBase2.initStatic(BombTIGreen.class);
    }
}
