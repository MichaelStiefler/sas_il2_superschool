package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.BulletGeneric;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.NetUserStat;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.MsgAction;

public class BulletAircraftGeneric extends Bullet {

    public void move(float f) {
        if (this.gun == null) {
            return;
        } else {
            Point3d point3d = this.p1;
            this.p1 = this.p0;
            this.p0 = point3d;
            BulletGeneric.dspeed.scale((this.gun.bulletKV[this.indx()] * f * 1.0F * BulletAircraftGeneric.fv(this.speed.length())) / this.speed.length(), this.speed);
            BulletGeneric.dspeed.z += this.gun.bulletAG[this.indx()] * f;
            this.speed.add(BulletGeneric.dspeed);
            this.p1.scaleAdd(f, this.speed, this.p0);
            this.p1.x += this.Wind.x * f;
            this.p1.y += this.Wind.y * f;
            return;
        }
    }

    public void timeOut() {
        if (this.gun() != null) {
            Explosions.generateExplosion(null, this.p1, this.gun().prop.bullet[this.indx()].power, this.gun().prop.bullet[this.indx()].powerType, this.gun().prop.bullet[this.indx()].powerRadius, 0.0D);
        }
    }

    public void destroy() {
        if (Mission.isPlaying() && !NetMissionTrack.isPlaying() && Actor.isValid(this.owner()) && Actor.isValid(this.gun()) && (this.gun() instanceof MGunAircraftGeneric) && (this.owner() == World.getPlayerAircraft()) && World.cur().diffCur.Limited_Ammo) {
            int i = this.bulletss - this.hashCode();
            if ((i != 0) && (i <= this.gun().countBullets()) && ((i != -1) || !World.isPlayerGunner())) {
                this.postRemove(this.owner());
            }
        }
        super.destroy();
    }

    private void postRemove(Actor actor) {
        new MsgAction(false, actor) {

            public void doAction(Object obj) {
                if (obj instanceof Aircraft) {
                    Aircraft aircraft = (Aircraft) obj;
                    if (Actor.isValid(aircraft) && Mission.isPlaying()) {
                        aircraft.detachGun(-1);
                    }
                }
            }

        };
    }

    public BulletAircraftGeneric(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l) {
        super(vector3d, i, gungeneric, loc, vector3d1, l);
        this.Wind = new Vector3d();
        this.Wind = vector3d;
        if (Mission.isPlaying() && !NetMissionTrack.isPlaying() && Actor.isValid(this.owner()) && Actor.isValid(this.gun()) && (this.gun() instanceof MGunAircraftGeneric) && (this.owner() == World.getPlayerAircraft()) && World.cur().diffCur.Limited_Ammo) {
            int j = this.gun().countBullets();
            this.bulletss = j + this.hashCode();
            MGunAircraftGeneric mgunaircraftgeneric = (MGunAircraftGeneric) this.gun();
            if ((mgunaircraftgeneric.guardBullet != null) && (j >= (mgunaircraftgeneric.guardBullet.bulletss - mgunaircraftgeneric.guardBullet.hashCode())) && ((j != -1) || !World.isPlayerGunner())) {
                this.postRemove(this.owner());
            }
            mgunaircraftgeneric.guardBullet = this;
        }
    }

    static float fv(double d) {
        return d <= 1090D ? (BulletAircraftGeneric.fv[(int) d / 100] + BulletAircraftGeneric.fv[((int) d / 100) + 1]) / 2.0F : 333F;
    }

    // TODO: Cheater Protection
    public boolean collided(Actor actor, String s, double paramDouble) {
        boolean bRet = false;
        float fRaiseFactor = 1.0F;
        boolean isAntiCheaterBullet = false;
        if ((this.owner != null) && !this.owner.isNetMirror() && AircraftState.isCheater(actor)) {
            fRaiseFactor = AircraftState.getCheaterSmallBulletsPowerFactor(actor);
            if ((this.properties().kalibr < 0.0003) && (fRaiseFactor > 1.0F)) {
                // gun calibre is smaller than 20mm, raise bullet power
                // fRaiseFactor = 4.0F;
                isAntiCheaterBullet = true;
                this.properties().kalibr *= fRaiseFactor;
                this.properties().massa *= fRaiseFactor;
                this.properties().power *= fRaiseFactor;
                this.properties().cumulativePower *= fRaiseFactor;
            }
            bRet = super.collided(actor, s, paramDouble);
            NetUser netuser = ((Aircraft) actor).netUser();
//            System.out.print("NetUser=" + (netuser==null?"null":netuser.uniqueName()));
            if (netuser != null) {
                NetUserStat netuserstat = netuser.curstat();
//                if (netuserstat == null) System.out.print("NetUserStat is null!");
                if (netuserstat != null) {
                    netuserstat.gotHitBullets += this.gun.prop.bulletsCluster;
                    netuserstat.gotHitMassa += this.properties().massa;
                    netuserstat.gotHitPower += this.properties().power;
                    netuserstat.gotHitCumulativePower += this.properties().cumulativePower;
//                    System.out.print("gotHitBullets=" + netuserstat.gotHitBullets + ", gotHitMassa=" + netuserstat.gotHitMassa + ", gotHitPower=" + netuserstat.gotHitPower + ", gotHitCumulativePower=" + netuserstat.gotHitCumulativePower);
                }
            }
//            System.out.println();
            if (isAntiCheaterBullet) {
                this.properties().kalibr /= fRaiseFactor;
                this.properties().massa /= fRaiseFactor;
                this.properties().power /= fRaiseFactor;
                this.properties().cumulativePower /= fRaiseFactor;
            }
        } else {
            bRet = super.collided(actor, s, paramDouble);
        }
        return bRet;
    }
    // ---

    protected int            bulletss;
    Vector3d                 Wind;
    private static final int fv[] = { 1, 1, 5, 15, 52, 87, 123, 160, 196, 233, 269, 333 };

}
