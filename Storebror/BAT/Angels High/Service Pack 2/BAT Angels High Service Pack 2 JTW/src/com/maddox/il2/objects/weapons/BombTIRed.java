package com.maddox.il2.objects.weapons;

public class BombTIRed extends BombTiBase2 {
    protected Bomb spawnNewBomb() {
        return new BombTiRd();
    }

    static {
        BombTiBase2.initStatic(BombTIRed.class);
    }
}
