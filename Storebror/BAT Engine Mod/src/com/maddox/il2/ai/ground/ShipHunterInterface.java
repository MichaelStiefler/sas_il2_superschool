package com.maddox.il2.ai.ground;

import com.maddox.il2.engine.Actor;

public interface ShipHunterInterface {

    float getReloadingTime(ShipAim aim);

    float chainFireTime(ShipAim aim);

    float probabKeepSameEnemy(Actor actor);

    float minTimeRelaxAfterFight();

    boolean enterToFireMode(int i, Actor actor, float f, ShipAim aim);

    void gunStartParking(ShipAim aim);

    void gunInMove(boolean flag, ShipAim aim);

    Actor findEnemy(ShipAim aim);

    int targetGun(ShipAim aim, Actor actor, float f, boolean flag);

    int targetMissile(ShipAim aim, Actor actor, float f, boolean flag);

    boolean isMissile(ShipAim aim);

    void singleShot(ShipAim aim);

    void startFire(ShipAim aim);

    void continueFire(ShipAim aim);

    void stopFire(ShipAim aim);
}
