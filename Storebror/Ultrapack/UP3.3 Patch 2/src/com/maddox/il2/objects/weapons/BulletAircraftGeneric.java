package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
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

public class BulletAircraftGeneric extends Bullet
{

    public void move(float f)
    {
        if(gun == null)
        {
            return;
        } else
        {
            Point3d point3d = p1;
            p1 = p0;
            p0 = point3d;
            dspeed.scale((double)(gun.bulletKV[indx()] * f * 1.0F * fv(speed.length())) / speed.length(), speed);
            dspeed.z += gun.bulletAG[indx()] * f;
            speed.add(dspeed);
            p1.scaleAdd(f, speed, p0);
            p1.x += Wind.x * (double)f;
            p1.y += Wind.y * (double)f;
            return;
        }
    }

    public void timeOut()
    {
        if(gun() != null)
            Explosions.generateExplosion(null, p1, gun().prop.bullet[indx()].power, gun().prop.bullet[indx()].powerType, gun().prop.bullet[indx()].powerRadius, 0.0D);
    }

    public void destroy()
    {
        if(Mission.isPlaying() && !NetMissionTrack.isPlaying() && Actor.isValid(owner()) && Actor.isValid(gun()) && (gun() instanceof MGunAircraftGeneric) && owner() == World.getPlayerAircraft() && World.cur().diffCur.Limited_Ammo)
        {
            int i = bulletss - hashCode();
            if(i != 0 && i <= gun().countBullets() && (i != -1 || !World.isPlayerGunner()))
                postRemove(owner());
        }
        super.destroy();
    }

    private void postRemove(Actor actor)
    {
        new MsgAction(false, actor) {

            public void doAction(Object obj)
            {
                if(obj instanceof Aircraft)
                {
                    Aircraft aircraft = (Aircraft)obj;
                    if(Actor.isValid(aircraft) && Mission.isPlaying())
                        aircraft.detachGun(-1);
                }
            }

        }
;
    }

    public BulletAircraftGeneric(Vector3d vector3d, int i, GunGeneric gungeneric, Loc loc, Vector3d vector3d1, long l)
    {
        super(vector3d, i, gungeneric, loc, vector3d1, l);
        Wind = new Vector3d();
        Wind = vector3d;
        if(Mission.isPlaying() && !NetMissionTrack.isPlaying() && Actor.isValid(owner()) && Actor.isValid(gun()) && (gun() instanceof MGunAircraftGeneric) && owner() == World.getPlayerAircraft() && World.cur().diffCur.Limited_Ammo)
        {
            int j = gun().countBullets();
            bulletss = j + hashCode();
            MGunAircraftGeneric mgunaircraftgeneric = (MGunAircraftGeneric)gun();
            if(mgunaircraftgeneric.guardBullet != null && j >= mgunaircraftgeneric.guardBullet.bulletss - mgunaircraftgeneric.guardBullet.hashCode() && (j != -1 || !World.isPlayerGunner()))
                postRemove(owner());
            mgunaircraftgeneric.guardBullet = this;
        }
    }

    static float fv(double d)
    {
        return d <= 1090D ? (float)(fv[(int)d / 100] + fv[(int)d / 100 + 1]) / 2.0F : 333F;
    }
    
    // TODO: Cheater Protection
    public boolean collided(Actor actor, String s, double paramDouble) {
        boolean bRet = false;
        float fRaiseFactor = 1.0F;
        if (this.owner != null && !this.owner.isNetMirror() && this.properties().kalibr < 0.0003 && AircraftState.isCheater(actor)) {
            // gun calibre is smaller than 20mm, raise bullet power
            fRaiseFactor = 4.0F;
            this.properties().massa *= fRaiseFactor;
            this.properties().power *= fRaiseFactor;
            this.properties().cumulativePower *= fRaiseFactor;
            bRet = super.collided(actor, s, paramDouble);
            NetUser netuser = ((Aircraft) actor).netUser();
            if (netuser != null) {
                NetUserStat netuserstat = netuser.stat();
                if (netuserstat != null) {
                    netuserstat.gotHitBullets++;
                    netuserstat.gotHitMassa += this.properties().massa;
                    netuserstat.gotHitPower += this.properties().power;
                    netuserstat.gotHitCumulativePower += this.properties().cumulativePower;
                }
            }
            this.properties().massa /= fRaiseFactor;
            this.properties().power /= fRaiseFactor;
            this.properties().cumulativePower /= fRaiseFactor;
        } else {
            bRet = super.collided(actor, s, paramDouble);
        }
        return bRet;
    }
    // ---

    protected int bulletss;
    Vector3d Wind;
    private static final int fv[] = {
        1, 1, 5, 15, 52, 87, 123, 160, 196, 233, 
        269, 333
    };

}
