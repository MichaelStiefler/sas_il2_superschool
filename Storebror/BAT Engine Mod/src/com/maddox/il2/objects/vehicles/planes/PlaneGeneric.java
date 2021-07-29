// By SAS~Storebror 09th/Nov./2017 , to avoid excessive logfile output
// By western0221 12th/Apr./2018 , show Chock 3d when enabled in conf.ini
package com.maddox.il2.objects.vehicles.planes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Line2f;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point2f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.gui.GUIAirArming;
import com.maddox.il2.net.NetFilesTrack;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.A_20;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.B_17;
import com.maddox.il2.objects.air.B_24;
import com.maddox.il2.objects.air.B_25;
import com.maddox.il2.objects.air.B_29X;
import com.maddox.il2.objects.air.F6F;
import com.maddox.il2.objects.air.JU_88;
import com.maddox.il2.objects.air.JU_88NEW;
import com.maddox.il2.objects.air.MOSQUITO;
import com.maddox.il2.objects.air.P_38;
import com.maddox.il2.objects.air.P_40;
import com.maddox.il2.objects.air.P_40SUKAISVOLOCH;
import com.maddox.il2.objects.air.P_47;
import com.maddox.il2.objects.air.P_51;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.il2.objects.air.Wellington;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.rts.Finger;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;
import com.maddox.util.TableFunction2;

public abstract class PlaneGeneric extends ActorHMesh
    implements MsgExplosionListener, MsgShotListener, Prey, Obstacle, ActorAlign
{
    public static class SPAWN
        implements ActorSpawn
    {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1)
        {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if(f2 == -9865.345F || f2 < f || f2 > f1)
            {
                if(f2 == -9865.345F)
                    System.out.println("Plane: Parameter [" + s + "]:<" + s1 + "> " + "not found");
                else
                    System.out.println("Plane: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
                throw new RuntimeException("Can't set property");
            } else
            {
                return f2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1)
        {
            String s2 = sectfile.get(s, s1);
            if(s2 == null || s2.length() <= 0)
            {
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                    System.out.print("Plane: Parameter [" + s + "]:<" + s1 + "> ");
                    System.out.println(s2 != null ? "is empty" : "not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Plane \"" + s + "\" is not (correctly) declared in technics.ini file!");
                }
                // TODO: ---
            }
            return s2;
        }

        private static String getS(SectFile sectfile, String s, String s1, String s2)
        {
            String s3 = sectfile.get(s, s1);
            if(s3 == null || s3.length() <= 0)
                return s2;
            else
                return s3;
        }

        private static PlaneProperties LoadPlaneProperties(SectFile sectfile, String s, Class class1)
        {
            PlaneProperties planeproperties = new PlaneProperties();
            planeproperties.fnShotPanzer = TableFunctions.GetFunc2("PlaneShotPanzer");
            planeproperties.fnExplodePanzer = TableFunctions.GetFunc2("PlaneExplodePanzer");
            String s1 = getS(sectfile, s, "Class");

            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
            if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (s1 == null || s1.length() == 0)) return null;
            // TODO: ---

            planeproperties.clazz = null;
            try
            {
                planeproperties.clazz = ObjIO.classForName(s1);
            }
            catch(Exception exception)
            {
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL)
                  System.out.println("*** Plane: class '" + s1 + "' not found");
                else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT)
                  System.out.println("Plane: Class \"" + s1 + "\" does not exist. The plane is declared in technics.ini but it's classfiles are missing.");
                // TODO: ---

                return null;
            }
            Property.set(class1, "airClass", (Object)planeproperties.clazz);
            String s2 = Property.stringValue(planeproperties.clazz, "keyName", null);
            if(s2 == null)
                Property.set(class1, "i18nName", "Plane");
            else
                Property.set(class1, "i18nName", I18N.plane(s2));
            planeproperties.explodeName = getS(sectfile, s, "Explode", "Aircraft");
            planeproperties.PANZER = getF(sectfile, s, "PanzerBodyFront", 0.0001F, 50F);
            planeproperties.HITBY_MASK = planeproperties.PANZER <= 0.015F ? -1 : -2;
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            return planeproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            PlaneGeneric planegeneric = null;
            try
            {
                PlaneGeneric.constr_arg1 = proper;
                PlaneGeneric.constr_arg2 = actorspawnarg;
                planegeneric = (PlaneGeneric)cls.newInstance();
                PlaneGeneric.constr_arg1 = null;
                PlaneGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                PlaneGeneric.constr_arg1 = null;
                PlaneGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create stationary Plane object [class:" + cls.getName() + "]");
                return null;
            }
            return planegeneric;
        }

        public Class cls;
        public PlaneProperties proper;

        public SPAWN(Class class1)
        {
            try
            {
                String s = class1.getName();
                int i = s.lastIndexOf('.');
                int j = s.lastIndexOf('$');
                if(i < j)
                    i = j;
                String s1 = s.substring(i + 1);
                proper = LoadPlaneProperties(Statics.getTechnicsFile(), s1, class1);

                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL && proper == null) return;
                // TODO: ---

            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in spawn: " + class1.getName());
            }
            cls = class1;
            Spawn.add(cls, this);
        }
    }

    class Mirror extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
            {
                switch(netmsginput.readByte())
                {
                case 73: // 'I'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                        post(netmsgguaranted);
                    }
                    short word0 = netmsginput.readShort();
                    if(word0 > 0 && dying != 1)
                        Die(null, (short)1, false);
                    return true;

                case 72: // 'H'
                    setMeshVisible(false);
                    return true;

                case 83: // 'S'
                    setMeshVisible(true);
                    return true;

                case 76: // 'L'
                    double d = netmsginput.readDouble();
                    double d1 = netmsginput.readDouble();
                    float f = netmsginput.readFloat();
                    PlaneGeneric.p.x = d;
                    PlaneGeneric.p.y = d1;
                    PlaneGeneric.o.setYaw(f);
                    pos.setAbs(PlaneGeneric.p, PlaneGeneric.o);
                    Align(false, false);
                    return true;

                case 68: // 'D'
                    if(isMirrored())
                    {
                        NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 1);
                        post(netmsgguaranted1);
                    }
                    if(dying != 1)
                    {
                        com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                        Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                        Die(actor, (short)1, true);
                    }
                    return true;
                }
                return false;
            }
            switch(netmsginput.readByte())
            {
            case 68: // 'D'
                out.unLockAndSet(netmsginput, 1);
                out.setIncludeTime(false);
                postRealTo(Message.currentRealTime(), masterChannel(), out);
                return true;
            }
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
                return true;
            if(netmsginput.readByte() != 68)
                return false;
            if(dying == 1)
            {
                return true;
            } else
            {
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                Die(actor, (short)0, true);
                return true;
            }
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    public static class PlaneProperties
    {

        public Class clazz;
        public float height;
        public float pitch;
        public TableFunction2 fnShotPanzer;
        public TableFunction2 fnExplodePanzer;
        public float PANZER;
        public int HITBY_MASK;
        public String explodeName;

        public PlaneProperties()
        {
            clazz = null;
            height = 0.0F;
            pitch = 0.0F;
            fnShotPanzer = null;
            fnExplodePanzer = null;
            PANZER = 0.001F;
            HITBY_MASK = -2;
            explodeName = null;
        }
    }


    public static final double Rnd(double d, double d1)
    {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1)
    {
        return World.Rnd().nextFloat(f, f1);
    }

    private boolean RndB(float f)
    {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public static void reset()
    {
        if(visiblePartsMap != null)
            visiblePartsMap.clear();
    }

    public void setAllowSpawnUse(boolean flag)
    {
        allowSpawning = flag;
    }

    public boolean getAllowSpawnUse()
    {
        return allowSpawning;
    }

    public void setRestoreWrecked(boolean flag)
    {
        restoreWrecked = flag;
    }

    public boolean getRestoreWrecked()
    {
        return restoreWrecked;
    }

    public boolean isGenericSpawnPoint()
    {
        return isGenericSpawnPoint;
    }

    public void setUseMarkings(boolean flag)
    {
        useMarkings = flag;
    }

    public boolean getUseMarkings()
    {
        return useMarkings;
    }

    public void setSkin(String s)
    {
        if(s != null && s.indexOf("\\") != -1)
        {
            char ac[] = s.toCharArray();
            for(int i = 0; i < ac.length; i++)
                if(ac[i] == '\\')
                    ac[i] = ' ';

            s = new String(ac);
        }
        bmpSkin = s;
    }

    public String getSkin()
    {
        return bmpSkin;
    }

    public boolean isStaticPos()
    {
        return true;
    }

    public int getDying()
    {
        return dying;
    }

    public void msgShot(Shot shot)
    {
        shot.bodyMaterial = 2;
        if(isGenericSpawnPoint)
            return;
        if(dying != 0 || !visible)
            return;
        if(shot.power <= 0.0F)
            return;
        if(isNetMirror() && shot.isMirage())
            return;
        if(shot.powerType == 1)
            if(RndB(0.15F))
            {
                return;
            } else
            {
                Die(shot.initiator, (short)0, true);
                return;
            }
        float f = prop.PANZER * Rnd(0.93F, 1.07F);
        float f1 = prop.fnShotPanzer.Value(shot.power, f);
        if(f1 < 1000F && (f1 <= 1.0F || RndB(1.0F / f1)))
            Die(shot.initiator, (short)0, true);
    }

    public void msgExplosion(Explosion explosion)
    {
        if(dying != 0 || !visible)
            return;
        if(isGenericSpawnPoint)
            return;
        if(isNetMirror() && explosion.isMirage())
            return;
        if(explosion.power <= 0.0F)
            return;
        if(explosion.powerType == 1)
        {
            if(TankGeneric.splintersKill(explosion, prop.fnShotPanzer, Rnd(0.0F, 1.0F), Rnd(0.0F, 1.0F), this, 0.6F, 0.0F, prop.PANZER, prop.PANZER, prop.PANZER, prop.PANZER, prop.PANZER, prop.PANZER))
                Die(explosion.initiator, (short)0, true);
            return;
        }
        if(explosion.powerType == 2 && explosion.chunkName != null)
        {
            Die(explosion.initiator, (short)0, true);
            return;
        }
        float f;
        if(explosion.chunkName != null)
            f = 0.5F * explosion.power;
        else
            f = explosion.receivedTNTpower(this);
        f *= Rnd(0.95F, 1.05F);
        float f1 = prop.PANZER;
        float f2 = prop.fnExplodePanzer.Value(f, f1);
        if(f2 < 1000F && (f2 <= 1.0F || RndB(1.0F / f2)))
            Die(explosion.initiator, (short)0, true);
    }

    private void ShowExplode(float f)
    {
        if(f > 0.0F)
            f = Rnd(f, f * 1.6F);
        Explosions.runByName(prop.explodeName, this, "", "", f);
    }

    private void Die(Actor actor, short word0, boolean flag)
    {
        if(dying != 0)
            return;
        if(word0 <= 0)
        {
            if(isNetMirror())
            {
                send_DeathRequest(actor);
                return;
            }
            word0 = 1;
        }
        dying = 1;
        World.onActorDied(this, actor);
        activateMesh(false);
        Align(false, true);
        if(flag)
            ShowExplode(17F);
        if(flag)
            send_DeathCommand(actor);
    }

    public void destroy()
    {
        if(isDestroyed())
        {
            return;
        } else
        {
            if(bChocks)
            {
                if (ChL != null)
                {
                    ChL.destroy();
                    ChL = null;
                }
                if (ChR != null)
                {
                    ChR.destroy();
                    ChR = null;
                }
                if (ChC != null)
                {
                    ChC.destroy();
                    ChC = null;
                }
                bChocks = false;
            }
            super.destroy();
            return;
        }
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    protected PlaneGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private PlaneGeneric(PlaneProperties planeproperties, ActorSpawnArg actorspawnarg)
    {
        prop = null;
        branch = null;
        dying = 0;
        visible = true;
        hasBeenMoved = false;
        allowSpawning = true;
        restoreWrecked = false;
        isGenericSpawnPoint = false;
        bmpSkin = null;
        useMarkings = true;

        // +++ By western, load conf.ini settings for Chocks
        bShowChocks = (Config.cur.ini.get("Mods", "PALShowChocks", 0) == 1 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 2 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 3 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 11 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 12 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 13);
        bUseChocksParking = (Config.cur.ini.get("Mods", "PALShowChocks", 0) == 11 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 12 || Config.cur.ini.get("Mods", "PALShowChocks", 0) == 13);
        bLoadedWheelRadius = false;
        // ---

        prop = planeproperties;
        actorspawnarg.setStationary(this);
        branch = actorspawnarg.country;
        setUseMarkings(actorspawnarg.bNumberOn);
        setSkin(actorspawnarg.paramFileName);

        // +++
        // By western, load each wheels radius values from FM when exist
        if(!bLoadedWheelRadius)
        {
            String s = Property.stringValue(prop.clazz, "FlightModel", null);
            SectFile sectfile = FlightModelMain.sectFile(s);
            double dtemp = (double) sectfile.get("Gear", "WheelDiameterLR", -1.0F);
            if (dtemp > 0D)
            {
                gWheelRadius[0] = gWheelRadius[1] = dtemp * 0.5D;
                bLoadedWheelRadius = true;
            }
            dtemp = (double) sectfile.get("Gear", "WheelDiameterC", -1.0F);
            if (dtemp > 0D)
            {
                gWheelRadius[2] = dtemp * 0.5D;
                bLoadedWheelRadius = true;
            }
            bSeaPlane = (sectfile.get("Aircraft", "Seaplane", 0) == 1);
        }

        // By western, load each chock values from FM when exist
        if(!bLoadedChockProperties)
        {
            String s = Property.stringValue(prop.clazz, "FlightModel", null);
            SectFile sectfile = FlightModelMain.sectFile(s);
            gChockLOffset[0] = (double) sectfile.get("Gear", "ChockLOffsetX", -100.0F);
            gChockLOffset[1] = (double) sectfile.get("Gear", "ChockLOffsetY", -100.0F);
            gChockCOffset[0] = (double) sectfile.get("Gear", "ChockCOffsetX", -100.0F);
            gChockCOffset[1] = (double) sectfile.get("Gear", "ChockCOffsetY", -100.0F);
            gChockLMeshName = getS(sectfile, "Gear", "ChockLMesh", null);
            gChockCMeshName = getS(sectfile, "Gear", "ChockCMesh", null);
            bLoadedChockProperties = true;
        }

        // By western, set generic wheels radius values when not exist specific FM fields
        if(!bLoadedWheelRadius)
        {
            Class clazz = prop.clazz;
            try {
                Aircraft aircraft = (Aircraft)clazz.newInstance();
                loadWheelRadius(aircraft);
                aircraft.destroy();
            } catch(Exception exception) { }
        }
        // ---

        try
        {
            activateMesh(true);
        }
        catch(RuntimeException runtimeexception)
        {
            super.destroy();
            throw runtimeexception;
        }
        collide(true);
        drawing(true);
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        if(prop.height == 0.0F && prop.pitch == 0.0F)
        {
            pnti[0] = hierMesh().hookFind("_ClipLGear");
            pnti[1] = hierMesh().hookFind("_ClipRGear");
            pnti[2] = hierMesh().hookFind("_ClipCGear");
            String s = Property.stringValue(prop.clazz, "FlightModel", null);
            SectFile sectfile = FlightModelMain.sectFile(s);
            if(pnti[0] >= 0 && pnti[1] >= 0 && pnti[2] >= 0 && sectfile.get("Gear", "FromIni", 0) == 0)
            {
                hierMesh().hookMatrix(pnti[2], M4);
                double d = M4.m03;
                double d1 = M4.m23;
                hierMesh().hookMatrix(pnti[0], M4);
                double d2 = M4.m03;
                double d3 = M4.m23;
                hierMesh().hookMatrix(pnti[1], M4);
                d2 = (d2 + M4.m03) * 0.5D;
                d3 = (d3 + M4.m23) * 0.5D;
                double d4 = d2 - d;
                double d5 = d3 - d1;
                prop.pitch = -Geom.RAD2DEG((float)Math.atan2(d5, d4));
                if(d4 < 0.0D)
                    prop.pitch += 180F;
                Line2f line2f = new Line2f();
                line2f.set(new Point2f((float)d2, (float)d3), new Point2f((float)d, (float)d1));
                prop.height = line2f.distance(new Point2f(0.0F, 0.0F));
            } else
            {
                prop.height = sectfile.get("Gear", "H", -0.5F);
                prop.pitch = sectfile.get("Gear", "Pitch", -0.5F);
            }
        }

        if(actorspawnarg.skill == 0)
            setAllowSpawnUse(false);
        else
        if(actorspawnarg.skill == 2)
            setRestoreWrecked(true);
        Align(true, false);
        if(this instanceof Plane.GenericSpawnPointPlane)
        {
            hideTransparentRed();
            isGenericSpawnPoint = true;
            collide(false);
        }
    }

    public boolean isVisible()
    {
        return visible;
    }

    private void parseVisibleParts()
    {
        HierMesh hiermesh = hierMesh();
        ArrayList arraylist = new ArrayList();
        int ai[] = hiermesh.getSubTrees("CF_D0");
        for(int i = 0; i < ai.length; i++)
        {
            hiermesh.setCurChunk(ai[i]);
            String s = hiermesh.chunkName();
            if(hiermesh.isChunkVisible(s))
                arraylist.add(s);
        }

        String as[] = new String[arraylist.size()];
        for(int j = 0; j < arraylist.size(); j++)
            as[j] = (String)arraylist.get(j);

        visiblePartsMap.put(getClass().toString(), as);
    }

    public void setMeshVisible(boolean flag)
    {
        if((dying != 0 || isDestroyed() || !isAlive()) && flag)
            return;
        visible = flag;
        if(!isGenericSpawnPoint)
        {
            drawing(flag);
            collide(flag);
        }
        String s = getClass().toString();
        if(!visiblePartsMap.containsKey(s))
            parseVisibleParts();
        String as[] = (String[])(String[])visiblePartsMap.get(s);
        HierMesh hiermesh = hierMesh();
        if(hiermesh == null)
            return;
        for(int i = 0; i < as.length; i++)
            if(hiermesh.chunkFindCheck(as[i]) >= 0)
                hiermesh.chunkVisible(as[i], flag);

        if(isNetMaster())
            send_SetVisibleCommand(flag);

        // By western, Chocks
        if (ChL != null)
            ChL.drawing(flag);
        if (ChR != null)
            ChR.drawing(flag);
        if (ChC != null)
            ChC.drawing(flag);
    }

    public void setNewLocation(Loc loc)
    {
        if(dying != 0)
        {
            return;
        } else
        {
            p.x = loc.getX();
            p.y = loc.getY();
            o.setYaw(loc.getOrient().getYaw());
            pos.setAbs(p, o);
            Align(false, false);
            send_SetNewLocCommand(loc);
            return;
        }
    }

    public void activateMesh(boolean flag)
    {
        if(flag)
        {
            String s = Regiment.getCountryFromBranch(branch);
            Regiment regiment = Regiment.findFirst(s, branch, getArmy());
            String s1 = Aircraft.getPropertyMesh(prop.clazz, regiment.country());
            setMesh(s1);
            if(bmpSkin != null && !bmpSkin.equals("null") && bmpSkin.length() > 1)
            {
                String s2 = bmpSkin;
                s2 = GUIAirArming.validateFileName(Property.stringValue(prop.clazz, "keyName", null)) + "/" + s2;
                if(NetMissionTrack.isPlaying())
                {
                    s2 = NetFilesTrack.getLocalFileName(Main.cur().netFileServerSkin, Main.cur().netServerParams.host(), s2);
                    if(s2 != null)
                        s2 = Main.cur().netFileServerSkin.alternativePath() + "/" + s2;
                    else
                        Aircraft.prepareMeshCamouflage(s1, hierMesh(), prop.clazz, regiment);
                } else
                {
                    s2 = Main.cur().netFileServerSkin.primaryPath() + "/" + s2;
                }
                if(s2 != null)
                {
                    String s3 = "PaintSchemes/Cache/" + Finger.file(0L, s2, -1);
                    boolean flag1 = Aircraft.prepareMeshSkin(s1, hierMesh(), s2, s3, regiment);
                    if(!flag1)
                    {
                        Aircraft.prepareMeshCamouflage(s1, hierMesh(), prop.clazz, regiment);
                        useMarkings = true;
                    }
                }
            } else
            {
                Aircraft.prepareMeshCamouflage(s1, hierMesh(), prop.clazz, regiment);
            }
            PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(prop.clazz, regiment.country());
            if(useMarkings)
                paintscheme.prepareNum(prop.clazz, hierMesh(), regiment, World.Rnd().nextInt(3), World.Rnd().nextInt(3), World.Rnd().nextInt(98) + 1);
            else
                paintscheme.prepareNum(prop.clazz, hierMesh(), regiment, 0, 0, 0, false);
        }
        String as[] = Aircraft.partNames();
        HierMesh hiermesh = hierMesh();
        for(int i = 1; i < 10 && hiermesh.chunkFindCheck("Pilot" + i + "_D0") >= 0; i++)
        {
            hiermesh.chunkVisible("Pilot" + i + "_D0", false);
            if(hiermesh.chunkFindCheck("Head" + i + "_D0") >= 0)
                hiermesh.chunkVisible("Head" + i + "_D0", false);
            if(hiermesh.chunkFindCheck("HMask" + i + "_D0") >= 0)
                hiermesh.chunkVisible("HMask" + i + "_D0", false);
            if(hiermesh.chunkFindCheck("Pilot" + i + "a_D0") >= 0)
                hiermesh.chunkVisible("Pilot" + i + "a_D0", false);
            if(hiermesh.chunkFindCheck("Head" + i + "a_D0") >= 0)
                hiermesh.chunkVisible("Head" + i + "a_D0", false);
            if(hiermesh.chunkFindCheck("Pilot" + i + "_FAK") >= 0)
                hiermesh.chunkVisible("Pilot" + i + "_FAK", false);
            if(hiermesh.chunkFindCheck("Pilot" + i + "_FAL") >= 0)
                hiermesh.chunkVisible("Pilot" + i + "_FAL", false);
            if(hiermesh.chunkFindCheck("Head" + i + "_FAK") >= 0)
                hiermesh.chunkVisible("Head" + i + "_FAK", false);
            if(hiermesh.chunkFindCheck("Head" + i + "_FAL") >= 0)
                hiermesh.chunkVisible("Head" + i + "_FAL", false);
        }

        if(!flag)
        {
label0:
            for(int j = 0; j < as.length; j++)
            {
                if(hiermesh.chunkFindCheck(as[j] + "_D0") < 0)
                    continue;
                hiermesh.chunkVisible(as[j] + "_D0", false);
                int k = 3;
                do
                {
                    if(k < 0)
                        continue label0;
                    if(hiermesh.chunkFindCheck(as[j] + "_D" + k) >= 0)
                    {
                        hiermesh.chunkVisible(as[j] + "_D" + k, true);
                        continue label0;
                    }
                    k--;
                } while(true);
            }

        }
        Aircraft.forceGear(prop.clazz, hierMesh(), 1.0F);

        // By western, display Parking Style like close variable wing or drop slats, airbrakes
        Aircraft.forceParkingStyle(prop.clazz, hierMesh());

        if(!flag)
            mesh().makeAllMaterialsDarker(0.32F, 0.45F);

        // +++ By western, display Chock 3d models
        if(bUseChocksParking && !(this instanceof Plane.GenericSpawnPointPlane) && !bSeaPlane)
        {
            if(flag)
            {
                pnti[0] = hierMesh().hookFind("_ClipLGear");
                pnti[1] = hierMesh().hookFind("_ClipRGear");
                pnti[2] = hierMesh().hookFind("_ClipCGear");
                if(pnti[0] >= 0 && pnti[1] >= 0 && pnti[2] >= 0)
                {
                    hierMesh().hookMatrix(pnti[0], M4);
                    Point3d pLgr = new Point3d(M4.m03, M4.m13, M4.m23);
                    hierMesh().hookMatrix(pnti[1], M4);
                    Point3d pRgr = new Point3d(M4.m03, M4.m13, M4.m23);
                    hierMesh().hookMatrix(pnti[2], M4);
                    Point3d pCgr = new Point3d(M4.m03, M4.m13, M4.m23);

                    // Chock of Left wheel
                    Point3d tempP = new Point3d(0D, 0D, 0D);
                    hookl = (HookNamed)this.findHook("_ClipLGear");
                    locL.set(pLgr);
                    locL.set(new Orient(180F, prop.pitch, 0F));
                    if (gChockLOffset[0] != -100.0D) {
                        tempP.set(gChockLOffset[0], 0D, -gChockLOffset[0] * Math.tan(Math.toRadians(prop.pitch)));
                        locL.add(tempP);
                    }
                    else if (prop.pitch > 0.8D && prop.pitch < 310D) {
                        tempP.set(-prop.pitch * 0.006D, 0D, prop.pitch * 0.006D * Math.tan(Math.toRadians(prop.pitch)));
                        locL.add(tempP);
                    }
                    if (gChockLOffset[1] != -100.0D) {
                        tempP.set(0D, gChockLOffset[1], 0D);
                        locL.add(tempP);
                    }
                    locL.add(this.pos.getCurrent());
                    if(!Engine.cur.land.isWater(locL.getX(), locL.getY()))
                    {
                        ChL = getChockMesh();
                        ChL.pos.setBase(this, hookl, false);
                        ChL.pos.setAbs(locL);
                        ChL.pos.changeHookToRel();
                        ChL.pos.resetAsBase();
                    }

                    // Chock of Right wheel
                    hookr = (HookNamed)this.findHook("_ClipRGear");
                    locR.set(pRgr);
                    locR.set(new Orient(0F, -prop.pitch, 0F));
                    if (gChockLOffset[0] != -100.0D) {
                        tempP.set(gChockLOffset[0], 0D, -gChockLOffset[0] * Math.tan(Math.toRadians(prop.pitch)));
                        locR.add(tempP);
                    }
                    else if (prop.pitch > 0.8D && prop.pitch < 310D) {
                        tempP.set(-prop.pitch * 0.006D, 0D, prop.pitch * 0.006D * Math.tan(Math.toRadians(prop.pitch)));
                        locR.add(tempP);
                    }
                    if (gChockLOffset[1] != -100.0D) {
                        tempP.set(0D, -gChockLOffset[1], 0D);
                        locR.add(tempP);
                    }
                    locR.add(this.pos.getCurrent());
                    if(!Engine.cur.land.isWater(locR.getX(), locR.getY()))
                    {
                        ChR = getChockMesh();
                        ChR.pos.setBase(this, hookr, false);
                        ChR.pos.setAbs(locR);
                        ChR.pos.changeHookToRel();
                        ChR.pos.resetAsBase();
                    }

                    // Chock of Center wheel
                    if (gChockCMeshName != null) {
                        hookc = (HookNamed)this.findHook("_ClipCGear");
                        locC.set(pCgr);
                        locC.set(new Orient(0F, -prop.pitch, 0F));
                        if (gChockCOffset[0] != -100.0D) {
                            tempP.set(gChockCOffset[0], 0D, -gChockCOffset[0] * Math.tan(Math.toRadians(prop.pitch)));
                            locC.add(tempP);
                        }
                        else if (prop.pitch > 0.8D && prop.pitch < 310D) {
                            tempP.set(-prop.pitch * 0.006D, 0D, prop.pitch * 0.006D * Math.tan(Math.toRadians(prop.pitch)));
                            locC.add(tempP);
                        }
                        if (gChockCOffset[1] != -100.0D) {
                            tempP.set(0D, -gChockCOffset[1], 0D);
                            locC.add(tempP);
                        }
                        locC.add(this.pos.getCurrent());
                        if(!Engine.cur.land.isWater(locC.getX(), locC.getY()))
                        {
                            ChC = new ActorSimpleMesh(gChockCMeshName);
                            ChC.pos.setBase(this, hookc, false);
                            ChC.pos.setAbs(locC);
                            ChC.pos.changeHookToRel();
                            ChC.pos.resetAsBase();
                        }
                    }

                    bChocks = true;
                }
            }
            else
            {
                if (ChL != null)
                {
                    ChL.destroy();
                    ChL = null;
                }
                if (ChR != null)
                {
                    ChR.destroy();
                    ChR = null;
                }
                if (ChC != null)
                {
                    ChC.destroy();
                    ChC = null;
                }
                bChocks = false;
            }
        }
        // ---
    }

    private void Align(boolean flag, boolean flag1)
    {
        pos.getAbs(p, o);
        p.z = Engine.land().HQ(p.x, p.y) + (double)prop.height;
        if(!flag)
            o.increment(0.0F, -prop.pitch, 0.0F);
        Engine.land().N(p.x, p.y, n);
        o.orient(n);
        o.increment(0.0F, prop.pitch, 0.0F);
        if(flag1)
        {
            long l = (long)((p.x % 2.2999999999999998D) * 221D + (p.y % 3.3999999999999999D) * 211D * 211D);
            RangeRandom rangerandom = new RangeRandom(l);
            p.z -= rangerandom.nextFloat(0.1F, 0.4F);
            float f = rangerandom.nextFloat(-2F, 2.0F);
            float f1 = (rangerandom.nextBoolean() ? 1.0F : -1F) * rangerandom.nextFloat(7F, 18F);
            o.increment(f, 0.0F, f1);
        }
        pos.setAbs(p, o);
    }

    public void align()
    {
        Align(false, false);
    }

    public int HitbyMask()
    {
        return prop.HITBY_MASK;
    }

    public int chooseBulletType(BulletProperties abulletproperties[])
    {
        if(dying != 0)
            return -1;
        if(abulletproperties.length == 1)
            return 0;
        if(abulletproperties.length <= 0)
            return -1;
        if(abulletproperties[0].power <= 0.0F)
            return 0;
        if(abulletproperties[1].power <= 0.0F)
            return 1;
        if(abulletproperties[0].cumulativePower > 0.0F)
            return 0;
        if(abulletproperties[1].cumulativePower > 0.0F)
            return 1;
        if(abulletproperties[0].powerType == 1)
            return 0;
        if(abulletproperties[1].powerType == 1)
            return 1;
        return abulletproperties[0].powerType != 2 ? 0 : 1;
    }

    public int chooseShotpoint(BulletProperties bulletproperties)
    {
        return dying == 0 ? 0 : -1;
    }

    public boolean getShotpointOffset(int i, Point3d point3d)
    {
        if(dying != 0)
            return false;
        if(i != 0)
            return false;
        if(point3d != null)
            point3d.set(0.0D, 0.0D, 0.0D);
        return true;
    }

    public boolean unmovableInFuture()
    {
        return true;
    }

    public void collisionDeath()
    {
        if(isNet())
        {
            return;
        } else
        {
            ShowExplode(-1F);
            destroy();
            return;
        }
    }

    public float futurePosition(float f, Point3d point3d)
    {
        pos.getAbs(point3d);
        return f > 0.0F ? f : 0.0F;
    }

    private void send_SetNewLocCommand(Loc loc)
    {
        if(!isNetMaster())
            return;
        hasBeenMoved = true;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(76);
            netmsgguaranted.writeDouble(loc.getX());
            netmsgguaranted.writeDouble(loc.getY());
            netmsgguaranted.writeFloat(loc.getOrient().getYaw());
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_SetVisibleCommand(boolean flag)
    {
        if(!isNetMaster())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            if(flag)
                netmsgguaranted.writeByte(83);
            else
                netmsgguaranted.writeByte(72);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_DeathCommand(Actor actor)
    {
        if(!isNetMaster())
            return;
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try
        {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            net.post(netmsgguaranted);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_DeathRequest(Actor actor)
    {
        if(!isNetMirror())
            return;
        if(net.masterChannel() instanceof NetChannelInStream)
            return;
        try
        {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(68);
            netmsgfiltered.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            netmsgfiltered.setIncludeTime(false);
            net.postTo(Time.current(), net.masterChannel(), netmsgfiltered);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void createNetObject(NetChannel netchannel, int i)
    {
        if(netchannel == null)
            net = new Master(this);
        else
            net = new Mirror(this, netchannel, i);
    }

    public void netFirstUpdate(NetChannel netchannel)
        throws IOException
    {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        if(dying == 0)
            netmsgguaranted.writeShort(0);
        else
            netmsgguaranted.writeShort(1);
        net.postTo(netchannel, netmsgguaranted);
        if(!visible)
        {
            NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted();
            netmsgguaranted1.writeByte(72);
            net.postTo(netchannel, netmsgguaranted1);
        }
        if(hasBeenMoved)
        {
            NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted();
            netmsgguaranted2.writeByte(76);
            netmsgguaranted2.writeDouble(pos.getAbsPoint().x);
            netmsgguaranted2.writeDouble(pos.getAbsPoint().y);
            netmsgguaranted2.writeFloat(pos.getAbsOrient().getYaw());
            net.postTo(netchannel, netmsgguaranted2);
        }
    }

    public void showTransparentRed()
    {
        hierMesh().chunkVisible("Red", true);
        drawing(true);
    }

    public void hideTransparentRed()
    {
        hierMesh().chunkVisible("Red", false);
        drawing(false);
    }

    // +++ By western, Chock mod part
    private ActorSimpleMesh getChockMesh()
    {
        ActorSimpleMesh asm = null;
        if (gChockLMeshName != null)
        {
            asm = new ActorSimpleMesh(gChockLMeshName);
            return asm;
        }
        float fyS = Property.floatValue(prop.clazz, "yearService", 0.0F);
        String soC = Property.stringValue(prop.clazz, "originCountry", null);
        if (soC != null && soC.equals(PaintScheme.countryUSA) && fyS > 1950.0F)
            asm = new ActorSimpleMesh("3DO/Arms/ChocksUSJet/mono.sim");
        else if (soC != null && soC.equals(PaintScheme.countryRussia) && fyS > 1950.0F)
            asm = new ActorSimpleMesh("3DO/Arms/ChocksRusJet1950/mono.sim");
        else {
            if (gWheelRadius[0] > 0.50D)
                asm = new ActorSimpleMesh("3DO/Arms/Chocks/monoL.sim");
            else
                asm = new ActorSimpleMesh("3DO/Arms/Chocks/mono.sim");
        }
        return asm;
    }

    private void loadWheelRadius(Aircraft aircraft)
    {
        if (aircraft instanceof A_20)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.565D;  // main Tire Diameter 44.5"
            gWheelRadius[2] = 0.325D;                    // aux Tire Diameter 25.61"
            gChockLOffset[0] = -0.20D;
        }
        else if (aircraft instanceof B_17)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.718D;  // main Tire Diameter 56.56"
            gWheelRadius[2] = 0.325D;                    // aux Tire Diameter 25.61"
            gChockLOffset[0] = -0.28D;
        }
        else if (aircraft instanceof B_24)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.718D;  // main Tire Diameter 56.56"
            gWheelRadius[2] = 0.459D;                    // aux Tire Diameter 36.15"
        }
        else if (aircraft instanceof B_25)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.468D;  // main Tire Diameter 36.86"
            gWheelRadius[2] = 0.468D;                    // aux Tire Diameter 36.86"
        }
        else if (aircraft instanceof B_29X)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.718D;  // main Tire Diameter 56.56"
            gWheelRadius[2] = 0.459D;                    // aux Tire Diameter 36.15"
            gChockLOffset[0] = -0.17D;
        }
        else if (aircraft instanceof F6F)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.389D;  // main Tire Diameter 30.69"
            gWheelRadius[2] = 0.156D;                    // aux Tire Diameter 12.35"
            gChockLOffset[0] = -0.05D;
            gChockLOffset[1] = 0.16D;
        }
        else if (aircraft instanceof P_38)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.459D;  // main Tire Diameter 36.15"
            gWheelRadius[2] = 0.349D;                    // aux Tire Diameter 27.5"
            gChockLOffset[0] = 0.04D;
            gChockLOffset[1] = 0.01D;
        }
        else if ((aircraft instanceof P_40) || (aircraft instanceof P_40SUKAISVOLOCH))
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.349D;  // main Tire Diameter 27.5"
            gWheelRadius[2] = 0.156D;                    // aux Tire Diameter 12.29"
            gChockLOffset[0] = -0.02D;
            gChockLOffset[1] = 0.09D;
        }
        else if (aircraft instanceof P_47)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.421D;  // main Tire Diameter 33.20"
            gWheelRadius[2] = 0.183D;                    // aux Tire Diameter 14.48"
            gChockLOffset[0] = -0.20D;
        }
        else if (aircraft instanceof P_51)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.349D;  // main Tire Diameter 27.5"
            gWheelRadius[2] = 0.156D;                    // aux Tire Diameter 12.35"
            gChockLOffset[0] = -0.17D;
        }
        else if ((aircraft instanceof JU_88) || (aircraft instanceof JU_88NEW))
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.570D;  // main Tire Diameter 44.94"
            gWheelRadius[2] = 0.260D;                    // aux Tire Diameter 20.48"
        }
        else if (aircraft instanceof MOSQUITO)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.546D;  // main Tire Diameter 43"
            gWheelRadius[2] = 0.239D;                    // aux Tire Diameter 18.85"
        }
        else if (aircraft instanceof Wellington)
        {
            gWheelRadius[0] = gWheelRadius[1] = 0.648D;  // main Tire Diameter 51"
            gWheelRadius[2] = 0.238D;                    // aux Tire Diameter 18.75"
        }
        bLoadedWheelRadius = true;
        return;
    }
    // ---

    private static String getS(SectFile sectfile, String s, String s1, String s2)
    {
        String s3 = sectfile.get(s, s1);
        if (s3 == null || s3.length() <= 0)
            return s2;
        else
            return new String(s3);
    }

    private PlaneProperties prop;
    public String branch;
    private int dying;
    static final int DYING_NONE = 0;
    static final int DYING_DEAD = 1;
    private boolean visible;
    private boolean hasBeenMoved;
    private boolean allowSpawning;
    private boolean restoreWrecked;
    private boolean isGenericSpawnPoint;
    private String bmpSkin;
    private boolean useMarkings;
    private static PlaneProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
//    private static Vector3d tmpv = new Vector3d();
    private static Map visiblePartsMap = new HashMap();
    private static int pnti[] = new int[3];
    private static Matrix4d M4 = new Matrix4d();

    //By western, tune wheels rotation speed using custom wheel radius bigger or smaller than stock 375mm.
    private double gWheelRadius[] = { 0.375D, 0.375D, 0.375D };
    private boolean bLoadedWheelRadius;

    //By western, set Chock Offset and Special msh
    private boolean bLoadedChockProperties;
    private double gChockLOffset[] = { -100.0D, -100.0D };
    private double gChockCOffset[] = { -100.0D, -100.0D };
    private String gChockLMeshName = null;
    private String gChockCMeshName = null;
//    private String gChockCarrierLMeshName = null;
//    private String gChockCarrierCMeshName = null;

    //By western, Chock functions
    private boolean bChocks = false;
    private boolean bSeaPlane = false;
    static boolean bShowChocks = false;
    static public boolean bUseChocksParking = false;
    private ActorSimpleMesh ChL = null;
    private ActorSimpleMesh ChR = null;
    private ActorSimpleMesh ChC = null;
    private Loc locL = new Loc();
    private Loc locR = new Loc();
    private Loc locC = new Loc();
    private HookNamed hookl = null;
    private HookNamed hookr = null;
    private HookNamed hookc = null;

}
