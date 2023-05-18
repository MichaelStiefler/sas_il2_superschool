
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.*;
import java.util.ArrayList;
import java.util.List;


public class CockpitF18FLIR extends CockpitGunner
{

    public boolean isEnableFocusing()
    {
        if(!((F_18)aircraft()).bHasLaser)
            return false;
        return !aircraft().FM.AS.isPilotParatrooper(0);
    }

    protected boolean doFocusEnter()
    {
        if(super.doFocusEnter())
        {
            ((F_18)aircraft()).FLIR = true;
            ((TypeLaserDesignator)aircraft()).setLaserOn(true);
            CmdEnv.top().exec("fov 33.3");
            return true;
        } else
        {
            return false;
        }
    }

    protected void doFocusLeave()
    {
        super.doFocusLeave();
        ((F_18)aircraft()).FLIR = false;
        ((F_18)aircraft()).laserTimer = Time.current() + 180000L;  // Laser Auto OFF after 180 sec.
    }

    public void moveGun(Orient orient)
    {
        super.moveGun(orient);
        orient1.set(orient);
        laserTrack(orient);
        instrument(orient);
    }

    private void instrument(Orient orient)
    {
        if(!((F_18)aircraft()).Nvision)
        {
            super.mesh.chunkVisible("Screen", false);
            super.mesh.chunkVisible("Dark", true);
        } else
        {
            super.mesh.chunkVisible("Screen", true);
            super.mesh.chunkVisible("Dark", false);
        }
        float yaw = cvt(orient.getYaw(), -120F, 120F, -0.25F, 0.25F);
        resetYPRmodifier();
        Cockpit.xyz[0] = yaw;
        super.mesh.chunkSetLocate("Z_Z_FLIR_HDG", Cockpit.xyz, Cockpit.ypr);
        resetYPRmodifier();
        float pitch = cvt(orient.getPitch(), 270F, 360F, -0.5F, 0.0F);
        Cockpit.xyz[1] = pitch;
        super.mesh.chunkSetLocate("Z_Z_FLIR_VERT", Cockpit.xyz, Cockpit.ypr);
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        ((F_18)aircraft()).pos.getAbs(point3d, orient2);
        float roll = orient2.getRoll();
        float antiroll = 360F - roll;
        if(antiroll > 180F)
            antiroll -= 360F;
        float fn = orient2.getPitch();
        float r = orient.getPitch();
        if(fn > 90F)
            r = orient.getPitch() - 360F;
        if(fn < 90F)
            r = orient.getPitch();
        if(fn > 90F)
            pitch = (fn - 360F) + r;
        if(fn < 90F)
            pitch = fn - r;
        super.mesh.chunkSetAngles("Z_Z_FLIR_BRG", 0.0F, antiroll, 0.0F);
        resetYPRmodifier();
        pitch = cvt(pitch, 0.0F, -180F, 0.0F, 0.15F);
        Cockpit.xyz[1] = pitch;
        super.mesh.chunkSetLocate("Z_Z_FLIR_BRG2", Cockpit.xyz, Cockpit.ypr);
    }

    public void laserTrack(Orient orient)
    {
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        ((F_18)aircraft()).pos.getAbs(point3d, orient2);
        float roll = orient2.getRoll();
        float fn = orient2.getPitch();
        float pitch = 0.0F;
        if(fn > 90F)
            pitch = fn - 360F;
        if(fn < 90F)
            pitch = fn;
        super.mesh.chunkSetAngles("baseflir", 0.0F, -pitch, roll);
        if(((F_18)aircraft()).hold)
        {
            super.mesh.chunkVisible("Z_Z_FLIR_Lock", true);
            LaserP3.x = LaserP2.x;
            LaserP3.y = LaserP2.y;
            LaserP3.z = LaserP2.z;
            Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP3.x, LaserP3.y, LaserP3.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            autotrack.clear();
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if(((actor instanceof CarGeneric) || (actor instanceof TankGeneric) || (actor instanceof ShipGeneric)) && !(actor instanceof StationaryGeneric) && actor != World.getPlayerAircraft() && actor.getArmy() != World.getPlayerArmy())
                {
                    Point3d pointOrtho = new Point3d();
                    pointOrtho.set(actor.pos.getAbsPoint());
                    if(((Tuple3d) (pointOrtho)).x > ((TypeLaserDesignator)aircraft()).getLaserSpot().x - 8D)
                    {
                        if(((Tuple3d) (pointOrtho)).x < ((TypeLaserDesignator)aircraft()).getLaserSpot().x + 8D)
                        {
                            if(((Tuple3d) (pointOrtho)).y < ((TypeLaserDesignator)aircraft()).getLaserSpot().y + 8D)
                            {
                                if(((Tuple3d) (pointOrtho)).y > ((TypeLaserDesignator)aircraft()).getLaserSpot().y - 8D)
                                    autotrack.add(pointOrtho);
                            }
                        }
                    }
                }
            }

            if(autotrack.size() > 0)
            {
                super.mesh.chunkVisible("Z_Z_FLIR_Tracking", true);
                Point3d victimPos = new Point3d();
                victimPos.set((Point3d)autotrack.get(0));
                ((TypeLaserDesignator)aircraft()).setLaserSpot(victimPos);
            } else
            {
                super.mesh.chunkVisible("Z_Z_FLIR_Tracking", false);
            }
            Point3d laser = new Point3d();
            laser.set(((TypeLaserDesignator)aircraft()).getLaserSpot());
            laser.sub(point3d);
            double d1 = ((Tuple3d) (laser)).y;
            double d2 = ((Tuple3d) (laser)).x;
            double d3 = ((Tuple3d) (laser)).z;
            double radius = Math.abs(Math.sqrt(d1 * d1 + d2 * d2));
            float f = (orient.getYaw() - orient2.getYaw()) + 90F;
            if(f > 360F)
                f -= 360F;
            else
            if(f < 0.0F)
                f += 360F;
            float f1 = f;
            float t = 270.8F - (float)Math.toDegrees(Math.atan(radius / d3));
            float te = 0.0F;
            float x = orient2.getYaw();
            if(x <= 0.0F)
                te = 180F + x;
            if(x > 0.0F)
                te = x + 180F;
            float y = 0.0F;
            if(f1 > 90F && f1 <= 180F)
                y = -(float)Math.toDegrees(Math.atan(d1 / d2)) + orient2.getYaw();
            else
            if(f1 > 180F && f1 <= 270F)
                y = -(float)Math.toDegrees(Math.atan(d1 / d2)) + te;
            else
            if(f1 > 270F && f1 <= 360F)
                y = -(float)Math.toDegrees(Math.atan(d1 / d2)) + te;
            else
            if(f1 > 0.0F && f1 <= 90F)
                y = -(float)Math.toDegrees(Math.atan(d1 / d2)) + orient2.getYaw();
            super.mesh.chunkSetAngles("Turret1A", y, 180F, 180F);
            super.mesh.chunkSetAngles("Turret1B", 180F, -t, 180F);
        }
        else
        {
            LaserHook[1] = new HookNamed(super.mesh, "_Laser1");
            ((F_18)aircraft()).pos.getRender(Actor._tmpLoc);
            LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            LaserHook[1].computePos(this, Actor._tmpLoc, LaserLoc1);
            LaserLoc1.get(LaserP1);
            LaserLoc1.set(40000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            LaserHook[1].computePos(this, Actor._tmpLoc, LaserLoc1);
            LaserLoc1.get(LaserP2);
            Engine.land();
            if(Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL))
            {
                LaserPL.z -= 0.94999999999999996D;
                LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
                ((TypeLaserDesignator)aircraft()).setLaserSpot(LaserP2);
                Eff3DActor eff3dactor1 = Eff3DActor.New(null, null, new Loc(((Tuple3d) (LaserP2)).x, ((Tuple3d) (LaserP2)).y, ((Tuple3d) (LaserP2)).z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            }
            super.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
            super.mesh.chunkSetAngles("Turret1B", 180F, -orient.getPitch(), 180F);
            super.mesh.chunkVisible("Z_Z_FLIR_Lock", false);
            super.mesh.chunkVisible("Z_Z_FLIR_Tracking", false);
        }
    }

    public void clipAnglesGun(Orient orient)
    {
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = Math.abs(f);
        for(; f < -180F; f += 360F);
        for(; f > 180F; f -= 360F);
        for(; prevA0 < -180F; prevA0 += 360F);
        for(; prevA0 > 180F; prevA0 -= 360F);
        if(!isRealMode())
        {
            prevA0 = f;
        } else
        {
            if(bNeedSetUp)
            {
                prevTime = Time.current() - 1L;
                bNeedSetUp = false;
            }
            if(f < -120F && prevA0 > 120F)
                f += 360F;
            else
            if(f > 120F && prevA0 < -120F)
                prevA0 += 360F;
            float f3 = f - prevA0;
            float f4 = 0.001F * (float)(Time.current() - prevTime);
            float f5 = Math.abs(f3 / f4);
            if(f5 > 120F)
                if(f > prevA0)
                    f = prevA0 + 120F * f4;
                else
                if(f < prevA0)
                    f = prevA0 - 120F * f4;
            prevTime = Time.current();
            if(f1 > 0.0F)
                f1 = 0.0F;
            if(f1 < -95F)
                f1 = -95F;
            orient.setYPR(f, f1, 0.0F);
            orient.wrap();
            prevA0 = f;
        }
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 4) != 0)
            super.mesh.chunkVisible("Z_Holes1_D1", true);
        if((fm.AS.astateCockpitState & 0x10) != 0)
            super.mesh.chunkVisible("Z_Holes2_D1", true);
    }

    public CockpitF18FLIR()
    {
        super("3DO/Cockpit/US_Common_FLIR/AV8FLIR.him", "he111_gunner");
        autotrack = new ArrayList();
        LaserHook = new Hook[4];
        dY = 0.0D;
        dX = 0.0D;
        point3d1 = new Point3d();
        orient1 = new Orient();
        spot1 = new Point3d();
        bNeedSetUp = true;
        prevTime = -1L;
        prevA0 = 0.0F;
    }

    private ArrayList autotrack;
    private double dY;
    private double dX;
    private boolean bNeedSetUp;
    private long prevTime;
    private float prevA0;
    public Point3d point3d1;
    public Orient orient1;
    public Point3d spot1;
    public Point3d spot2;
    private Hook LaserHook[];
    private Loc LaserLoc1 = new Loc();
    private Point3d LaserP1 = new Point3d();
    private Point3d LaserP2 = new Point3d();
    private Point3d LaserPL = new Point3d();
    private Point3d LaserP3 = new Point3d();


    static
    {
        Property.set(com.maddox.il2.objects.air.CockpitF18FLIR.class, "weaponControlNum", 10);
        Property.set(com.maddox.il2.objects.air.CockpitF18FLIR.class, "astatePilotIndx", 0);
    }
}