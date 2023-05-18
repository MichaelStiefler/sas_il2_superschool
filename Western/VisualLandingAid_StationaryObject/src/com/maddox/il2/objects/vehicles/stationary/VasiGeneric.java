package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.JGP.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public abstract class VasiGeneric extends VisualLandingAidGeneric
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
                System.out.print("Vasi: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            } else
            {
                return s2;
            }
        }

        private static VasiProperties LoadVasiProperties(SectFile sectfile, String s, Class class1)
        {
            VasiProperties vasiproperties = new VasiProperties();
            vasiproperties.meshName = sectfile.get("Vasi", s + ":Mesh");
            if(vasiproperties.meshName == null || vasiproperties.meshName.length() <= 0)
            {
                System.out.println("Vasi: Parameter [" + s + "]:<Mesh> is not defined");
                throw new RuntimeException("Can't set property");
            }
            vasiproperties.type = sectfile.get("Vasi", s + ":Type", 0);
            if(vasiproperties.type == 0)
            {
                System.out.println("Vasi: Parameter [" + s + "]:<Type> is not defined");
                throw new RuntimeException("Can't set property");
            }
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", vasiproperties.meshName);
            return vasiproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            VasiGeneric vasigeneric = null;
            try
            {
                VasiGeneric.constr_arg1 = proper;
                VasiGeneric.constr_arg2 = actorspawnarg;
                vasigeneric = (VasiGeneric)cls.newInstance();
                VasiGeneric.constr_arg1 = null;
                VasiGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                VasiGeneric.constr_arg1 = null;
                VasiGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create VasiGeneric object [class:" + cls.getName() + "]");
                return null;
            }
            return vasigeneric;
        }

        public Class cls;
        public VasiProperties proper;

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
                proper = LoadVasiProperties(Statics.getTechnicsFile(), s1, class1);
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

    public static class VasiProperties
    {

        public String meshName;
        public int type;

        public VasiProperties()
        {
            meshName = null;
            type = 0;
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

    protected VasiGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private VasiGeneric(VasiProperties vasiproperties, ActorSpawnArg actorspawnarg)
    {
        super(vasiproperties.meshName);
        prop = null;
        outCommand = new NetMsgFiltered();
        prop = vasiproperties;
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
        startMove();
        if(!Config.isUSE_RENDER())
            return;
        if(Main.state() != null && Main.state().id() == 18)
            Eff3D.initSetTypeTimer(true);
        ColorDefault(prop.type);
        oldColorStage = 3;
    }

    private void ColorCancel(int type)
    {
        switch(type)
        {
            case 1:
                for(int i=0; i<1; i++)
                    for(int j=0; j<3; j++)
                        Eff3DActor.finish(eff[i][j]);
            break;
            case 2:
                for(int i=0; i<2; i++)
                    for(int j=0; j<6; j++)
                        Eff3DActor.finish(eff[i][j]);
            break;
            case 3:
                for(int i=0; i<3; i++)
                    for(int j=0; j<6; j++)
                        Eff3DActor.finish(eff[i][j]);
            break;
            default:
            break;
        }
    }

    private void ColorDefault(int type)
    {
        if(!bVisible) return;
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
            break;
            case 2:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 3:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_vasi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_vasi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_vasi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][3] = Eff3DActor.New(this, findHook("_vasi3d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][4] = Eff3DActor.New(this, findHook("_vasi3e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][5] = Eff3DActor.New(this, findHook("_vasi3f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorTooLow(int type)
    {
        if(!bVisible) return;
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 2:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 3:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_vasi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_vasi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_vasi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][3] = Eff3DActor.New(this, findHook("_vasi3d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][4] = Eff3DActor.New(this, findHook("_vasi3e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][5] = Eff3DActor.New(this, findHook("_vasi3f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorLow(int type)
    {
        if(!bVisible) return;
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareYellow.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareYellow.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareYellow.eff", -1F);
            break;
            case 2:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 3:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_vasi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_vasi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_vasi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][3] = Eff3DActor.New(this, findHook("_vasi3d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][4] = Eff3DActor.New(this, findHook("_vasi3e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][5] = Eff3DActor.New(this, findHook("_vasi3f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorHigh(int type)
    {
        if(!bVisible) return;
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
            break;
            case 2:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
            break;
            case 3:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_vasi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_vasi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_vasi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][3] = Eff3DActor.New(this, findHook("_vasi3d"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][4] = Eff3DActor.New(this, findHook("_vasi3e"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][5] = Eff3DActor.New(this, findHook("_vasi3f"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorTooHigh(int type)
    {
        if(!bVisible) return;
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
            break;
            case 2:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
            break;
            case 3:
                eff[0][0] = Eff3DActor.New(this, findHook("_vasi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_vasi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_vasi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][3] = Eff3DActor.New(this, findHook("_vasi1d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][4] = Eff3DActor.New(this, findHook("_vasi1e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][5] = Eff3DActor.New(this, findHook("_vasi1f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_vasi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_vasi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_vasi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][3] = Eff3DActor.New(this, findHook("_vasi2d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][4] = Eff3DActor.New(this, findHook("_vasi2e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][5] = Eff3DActor.New(this, findHook("_vasi2f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_vasi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_vasi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_vasi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][3] = Eff3DActor.New(this, findHook("_vasi3d"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][4] = Eff3DActor.New(this, findHook("_vasi3e"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][5] = Eff3DActor.New(this, findHook("_vasi3f"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
            break;
            default:
            break;
        }
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
		System.out.println("Vasi - 486 : setVisible( " + flag + ")");
        setArmy(0);
        bVisible = flag;
        if(flag)
        {
            ColorCancel(prop.type);
            switch(oldColorStage)
            {
                case 1:
                    ColorTooLow(prop.type);
                    break;
                case 2:
                    ColorLow(prop.type);
                    break;
                case 3:
                    ColorDefault(prop.type);
                    break;
                case 4:
                    ColorHigh(prop.type);
                    break;
                case 5:
                    ColorTooHigh(prop.type);
                    break;
                default:
                    ColorDefault(prop.type);
                    break;
            }
        }
        else
        {
            ColorCancel(prop.type);
        }
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
            VisualLandingCalc visuallandingcalc = new VisualLandingCalc();
            Aircraft aircraft1 = visuallandingcalc.getNearestAircraftFront(actorme);
            if(aircraft1 == null)
            {
                if(oldColorStage != 3)
                {
                    ColorCancel(prop.type);
                    ColorDefault(prop.type);
                    oldColorStage = 3;
                }
                return true;
            }
            int newColorStage;
            float glidepath = visuallandingcalc.getPapiGlidePath(actorme, aircraft1);
            if(glidepath < -0.5F) newColorStage = 5;
            else if(glidepath < -0.20F) newColorStage = 4;
            else if(glidepath < 0.20F) newColorStage = 3;
            else if(glidepath < ((prop.type == 1)? 0.3F : 0.5F)) newColorStage = 2;
            else newColorStage = 1;

            if(newColorStage != oldColorStage)
            {
                ColorCancel(prop.type);
                switch(newColorStage)
                {
                    case 1:
                        ColorTooLow(prop.type);
                        break;
                    case 2:
                        ColorLow(prop.type);
                        break;
                    case 3:
                        ColorDefault(prop.type);
                        break;
                    case 4:
                        ColorHigh(prop.type);
                        break;
                    case 5:
                        ColorTooHigh(prop.type);
                        break;
                    default:
                        ColorDefault(prop.type);
                        break;
                }
                oldColorStage = newColorStage;
            }
            return true;
        }
      
        Move()
        {
        }
    }

    
    private VasiProperties prop;
    private float heightAboveLandSurface;
    private Eff3DActor eff[][] = new Eff3DActor[3][6];
    private static VasiProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private NetMsgFiltered outCommand;
    private static final double glideScopeInRads = Math.toRadians(3D);
    private int oldColorStage;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private Actor actorme = this;
    private boolean bVisible = true;

}
