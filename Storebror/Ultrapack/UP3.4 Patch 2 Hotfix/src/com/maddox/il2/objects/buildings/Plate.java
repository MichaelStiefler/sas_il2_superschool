package com.maddox.il2.objects.buildings;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.LandPlate;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.rts.Finger;
import com.maddox.rts.Message;
import com.maddox.rts.SectFile;
import com.maddox.rts.SoftClass;
import com.maddox.rts.Spawn;

public final class Plate extends ActorMesh
    implements ActorAlign, LandPlate, SoftClass
{
    public static class SPAWN
        implements ActorSpawn
    {

        private static String getS(SectFile sectfile, String s, String s1)
        {
            String s2 = sectfile.get(s, s1);
            if(s2 == null || s2.length() <= 0)
            {
                System.out.print("House: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            } else
            {
                return s2;
            }
        }

        private static Properties LoadProperties(SectFile sectfile, String s, String s1, String s2)
        {
            Properties properties = new Properties();
            properties.SoftClassInnerName = s2;
            properties.FingerOfFullClassName = Finger.Int(s1);
            properties.MeshName = getS(sectfile, s, "Mesh");
            properties.bGround = s2.indexOf("Ground") >= 0;
            return properties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            Plate plate = null;
            try
            {
                plate = (Plate)cls.newInstance();
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                return null;
            }
            plate.prop = prop;
            try
            {
                plate.setMesh(new MeshShared(prop.MeshName));
            }
            catch(RuntimeException runtimeexception)
            {
                plate.destroy();
                throw runtimeexception;
            }
            actorspawnarg.set(plate);
            plate.align();
            plate.drawing(true);
            return plate;
        }

        public Class cls;
        public Properties prop;

        protected SPAWN(String s)
        {
            cls = getClass().getDeclaringClass();
            String s1 = cls.getName();
            String s2 = s1.substring(s1.lastIndexOf('.') + 1);
            String s3 = s2 + '$' + s;
            String s4 = s1 + '$' + s;
            SectFile sectfile = Statics.getBuildingsFile();
            String s5 = null;
            int i = sectfile.sections();
            int j = 0;
            do
            {
                if(j >= i)
                    break;
                if(sectfile.sectionName(j).endsWith(s3))
                {
                    s5 = sectfile.sectionName(j);
                    break;
                }
                j++;
            } while(true);
            if(s5 == null)
            {
                System.out.println("Plate: Section " + s3 + " not found");
                throw new RuntimeException("Can't register spawner");
            }
            try
            {
                prop = LoadProperties(sectfile, s5, s4, s);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in plate spawn registration: " + s4);
            }
            Spawn.add_SoftClass(s4, this);
        }
    }

    public static class Properties
    {

        public String SoftClassInnerName;
        public int FingerOfFullClassName;
        public String MeshName;
        public boolean bGround;

        public Properties()
        {
            SoftClassInnerName = null;
            FingerOfFullClassName = 0;
            MeshName = null;
            bGround = false;
        }
    }


    public Plate()
    {
        prop = null;
    }

    public static void registerSpawner(String s)
    {
        new SPAWN(s);
    }

    public String fullClassName()
    {
        return getClass().getName() + "$" + prop.SoftClassInnerName;
    }

    public int fingerOfFullClassName()
    {
        return prop.FingerOfFullClassName;
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    public boolean isStaticPos()
    {
        return true;
    }

    public boolean isGround()
    {
        return prop.bGround;
    }

    public void setDiedFlag(boolean flag)
    {
        super.setDiedFlag(flag);
        align();
        drawing(true);
    }

    public void align()
    {
        mesh().setFastShadowVisibility(2);
        pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) + heightAboveLandSurface;
        o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        Engine.land().N(p.x, p.y, n);
        o.orient(n);
        pos.setAbs(p, o);
    }

    public float futurePosition(float f, Point3d point3d)
    {
        pos.getAbs(point3d);
        if(f <= 0.0F)
            return 0.0F;
        else
            return f;
    }

    private static final float heightAboveLandSurface = 0.25F;
    private Properties prop;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();
}
