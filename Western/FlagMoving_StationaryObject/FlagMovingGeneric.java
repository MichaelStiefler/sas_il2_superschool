package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.Wind;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public abstract class FlagMovingGeneric extends ActorHMesh
    implements ActorAlign
{

    public static class SPAWN
        implements ActorSpawn
    {

        private static String getS(SectFile sectfile, String s, String s1)
        {
            String s2 = sectfile.get(s, s1);
            if(s2 == null || s2.length() <= 0)
            {
                System.out.print("FlagMoving: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            } else
            {
                return s2;
            }
        }

        private static FlagMovingProperties LoadFlagMovingProperties(SectFile sectfile, String s, Class class1)
        {
            FlagMovingProperties flagmovingproperties = new FlagMovingProperties();
            flagmovingproperties.meshName = getS(sectfile, "FlagMoving", s + ":Mesh");
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", flagmovingproperties.meshName);
            return flagmovingproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            FlagMovingGeneric flagmovinggeneric = null;
            try
            {
                FlagMovingGeneric.constr_arg1 = proper;
                FlagMovingGeneric.constr_arg2 = actorspawnarg;
                flagmovinggeneric = (FlagMovingGeneric)cls.newInstance();
                FlagMovingGeneric.constr_arg1 = null;
                FlagMovingGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                FlagMovingGeneric.constr_arg1 = null;
                FlagMovingGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create FlagMovingGeneric object [class:" + cls.getName() + "]");
                return null;
            }
            return flagmovinggeneric;
        }

        public Class cls;
        public FlagMovingProperties proper;

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
                proper = LoadFlagMovingProperties(Statics.getTechnicsFile(), s1, class1);
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
            return true;
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    public static class FlagMovingProperties
    {

        public String meshName;

        public FlagMovingProperties()
        {
            meshName = null;
        }
    }


    public Object getSwitchListener(Message message)
    {
        return this;
    }

    public boolean isStaticPos()
    {
        return true;
    }

    public void startMove()
    {
        if(!interpEnd("move"))
        {
            interpPut(new Move(), "move", Time.current(), null);
        }
    }

    protected FlagMovingGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private FlagMovingGeneric(FlagMovingProperties flagmovingproperties, ActorSpawnArg actorspawnarg)
    {
        super(flagmovingproperties.meshName);
        prop = null;
        outCommand = new NetMsgFiltered();
        prop = flagmovingproperties;
        double d = 0.0D;
        if(actorspawnarg.timeLenExist)
        {
            d = actorspawnarg.point.z;
            actorspawnarg.point.z = actorspawnarg.timeLen;
        }
        actorspawnarg.setStationary(this);
        if(actorspawnarg.timeLenExist)
            actorspawnarg.point.z = d;
        collide(false);
        drawing(true);
        createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        heightAboveLandSurface = 0.0F;
        Align();
        Loc loc = new Loc();
        loc = this.pos.getAbs();
        poleYaw = -loc.getOrient().getYaw();
        for(; poleYaw >= 360F; poleYaw -= 360)  ;
        for(; poleYaw < 0F; poleYaw += 360)  ;
        startMove();
        if(!Config.isUSE_RENDER())
            return;
        if(Main.state() != null && Main.state().id() == 18)
            Eff3D.initSetTypeTimer(true);
    }

    private void Align()
    {
        pos.getAbs(p);
        if(p.z < Engine.land().HQ(p.x, p.y) + (double)heightAboveLandSurface)
            p.z = Engine.land().HQ(p.x, p.y) + (double)heightAboveLandSurface;
        o.setYPR(pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        pos.setAbs(p, o);
    }

    public void setVisible(boolean flag)
    {
    }

    public void align()
    {
        Align();
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
    }


    class Move extends Interpolate
    {

        public boolean tick()
        {
            Point3d pointHere = actor_mine.pos.getAbsPoint();
            Point3d pointTemp = new Point3d();
            pointTemp.set(pointHere.x, pointHere.y, pointHere.z + 0.01D);
            Vector3d vectorWind = new Vector3d();
            World.wind().getVector(pointTemp, vectorWind);
            Vector3d vectorWindTemp = new Vector3d(vectorWind.x , vectorWind.y , 0.0D);
            float windVL = (float) vectorWindTemp.length();
            float windV = windVL * Time.tickLenFms() * 0.1F;
            if(windV > 20F) windV = 20F;
            if(windV < 0.01F) windV = 0.01F;
            double angle = 0.0D;

            if(windV > 0.01F)
            {
                Orient oTemp = new Orient();
                vectorWind.normalize();

                oTemp.setAT0(vectorWind);
                angle = oTemp.getAzimut() - 90F;
            }
            float new_deg_rot = -(float) angle + poleYaw;
            for( ; new_deg_rot >= 360F; new_deg_rot -= 360F) ;
            for( ; new_deg_rot < 0F; new_deg_rot += 360F) ;
            float div = (float) Math.floor(25F - windV);
            new_deg_rot += World.Rnd().nextFloat(-0.5F * windV - 0.5F, 0.5F * windV + 0.5F);
            deg_rot = ((div - 1F) * deg_rot + new_deg_rot) / div;
            hierMesh().chunkSetAngles("Flag0", deg_rot, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flag1", deg_rot, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flag2", deg_rot, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flag3", deg_rot, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flag4", deg_rot, 0.0F, 0.0F);
            if(windV < 1.0F)
            {
                hierMesh().chunkVisible("Flag0", true);
                hierMesh().chunkVisible("Flag1", false);
                hierMesh().chunkVisible("Flag2", false);
                hierMesh().chunkVisible("Flag3", false);
                hierMesh().chunkVisible("Flag4", false);
            }
            else if(windV < 3.0F)
            {
                hierMesh().chunkVisible("Flag0", false);
                hierMesh().chunkVisible("Flag1", true);
                hierMesh().chunkVisible("Flag2", false);
                hierMesh().chunkVisible("Flag3", false);
                hierMesh().chunkVisible("Flag4", false);
            }
            else if(windV < 6.0F)
            {
                hierMesh().chunkVisible("Flag0", false);
                hierMesh().chunkVisible("Flag1", false);
                hierMesh().chunkVisible("Flag2", true);
                hierMesh().chunkVisible("Flag3", false);
                hierMesh().chunkVisible("Flag4", false);
            }
            else if(windV < 9.0F)
            {
                hierMesh().chunkVisible("Flag0", false);
                hierMesh().chunkVisible("Flag1", false);
                hierMesh().chunkVisible("Flag2", false);
                hierMesh().chunkVisible("Flag3", true);
                hierMesh().chunkVisible("Flag4", false);
            }
            else
            {
                hierMesh().chunkVisible("Flag0", false);
                hierMesh().chunkVisible("Flag1", false);
                hierMesh().chunkVisible("Flag2", false);
                hierMesh().chunkVisible("Flag3", false);
                hierMesh().chunkVisible("Flag4", true);
            }
            return true;
        }
      
        Move()
        {
        }
    }

    
    private FlagMovingProperties prop;
    private float heightAboveLandSurface;
    private static FlagMovingProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private NetMsgFiltered outCommand;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private Actor actor_mine = this;
    private float deg_rot = 0F;
    private float poleYaw = 0;

  
}
