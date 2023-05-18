package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;

public class BombGunTorpMk34_B24 extends BombGunTorpMk34 {
    public void loadBullets(int i) {
        super.loadBullets(i);
        if (this.bullets() == 0) {
            return;
        } else {
            Point3d point3d = this.bomb.pos.getRelPoint();
            point3d.x -= 0.13D;
            point3d.y -= 0.19D;
            this.bomb.pos.setRel(point3d);
            return;
        }
    }
}
