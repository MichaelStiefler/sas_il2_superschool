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

public abstract class PapiGeneric extends VisualLandingAidGeneric
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
                System.out.print("Papi: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            } else
            {
                return s2;
            }
        }

        private static PapiProperties LoadPapiProperties(SectFile sectfile, String s, Class class1)
        {
            PapiProperties papiproperties = new PapiProperties();
            papiproperties.meshName = getS(sectfile, "Papi", s + ":Mesh");
            papiproperties.type = sectfile.get("Papi", s + ":Type", 0);
            if(papiproperties.type == 0)
            {
                System.out.println("Papi: Parameter [" + s + "]:<Type> is not defined");
                throw new RuntimeException("Can't set property");
            }
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", papiproperties.meshName);
            return papiproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            PapiGeneric papigeneric = null;
            try
            {
                PapiGeneric.constr_arg1 = proper;
                PapiGeneric.constr_arg2 = actorspawnarg;
                papigeneric = (PapiGeneric)cls.newInstance();
                PapiGeneric.constr_arg1 = null;
                PapiGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                PapiGeneric.constr_arg1 = null;
                PapiGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create PapiGeneric object [class:" + cls.getName() + "]");
                return null;
            }
            return papigeneric;
        }

        public Class cls;
        public PapiProperties proper;

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
                proper = LoadPapiProperties(Statics.getTechnicsFile(), s1, class1);
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

    public static class PapiProperties
    {

        public String meshName;
        public int type;

        public PapiProperties()
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

    protected PapiGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private PapiGeneric(PapiProperties papiproperties, ActorSpawnArg actorspawnarg)
    {
        super(papiproperties.meshName);
        prop = null;
        outCommand = new NetMsgFiltered();
        prop = papiproperties;
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
                for(int i=0; i<4; i++)
                    for(int j=0; j<3; j++)
                        Eff3DActor.finish(eff[i][j]);
            break;
            case 2:
                for(int i=1; i<3; i++)
                    for(int j=0; j<3; j++)
                        Eff3DActor.finish(eff[i][j]);
            break;
            default:
            break;
        }
    }

    private void ColorDefault(int type)
    {
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_papi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_papi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_papi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][0] = Eff3DActor.New(this, findHook("_papi4a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][1] = Eff3DActor.New(this, findHook("_papi4b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][2] = Eff3DActor.New(this, findHook("_papi4c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 2:
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorTooLow(int type)
    {
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_papi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_papi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_papi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][0] = Eff3DActor.New(this, findHook("_papi4a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][1] = Eff3DActor.New(this, findHook("_papi4b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][2] = Eff3DActor.New(this, findHook("_papi4c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 2:
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorLow(int type)
    {
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_papi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_papi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_papi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][0] = Eff3DActor.New(this, findHook("_papi4a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][1] = Eff3DActor.New(this, findHook("_papi4b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][2] = Eff3DActor.New(this, findHook("_papi4c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 2:
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorHigh(int type)
    {
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_papi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_papi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_papi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[3][0] = Eff3DActor.New(this, findHook("_papi4a"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][1] = Eff3DActor.New(this, findHook("_papi4b"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                eff[3][2] = Eff3DActor.New(this, findHook("_papi4c"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            break;
            case 2:
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
            break;
            default:
            break;
        }
    }
  
    private void ColorTooHigh(int type)
    {
        switch(type)
        {
            case 1:
                eff[0][0] = Eff3DActor.New(this, findHook("_papi1a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][1] = Eff3DActor.New(this, findHook("_papi1b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[0][2] = Eff3DActor.New(this, findHook("_papi1c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[3][0] = Eff3DActor.New(this, findHook("_papi4a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[3][1] = Eff3DActor.New(this, findHook("_papi4b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[3][2] = Eff3DActor.New(this, findHook("_papi4c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
            break;
            case 2:
                eff[1][0] = Eff3DActor.New(this, findHook("_papi2a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][1] = Eff3DActor.New(this, findHook("_papi2b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[1][2] = Eff3DActor.New(this, findHook("_papi2c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][0] = Eff3DActor.New(this, findHook("_papi3a"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][1] = Eff3DActor.New(this, findHook("_papi3b"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
                eff[2][2] = Eff3DActor.New(this, findHook("_papi3c"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhite.eff", -1F);
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
        setArmy(0);
        if(flag)
            for(int i=0; i<4; i++)
                for(int j=0; j<3; j++)
                    if(eff[i][j] != null)  eff[i][j]._setIntesity(1.0F);
                    else ;
        else
            for(int i=0; i<4; i++)
                for(int j=0; j<3; j++)
                    if(eff[i][j] != null)  eff[i][j]._setIntesity(0.0F);
                    else ;
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
            else if(glidepath < -0.16F) newColorStage = 4;
            else if(glidepath < 0.16F) newColorStage = 3;
            else if(glidepath < 0.5F) newColorStage = 2;
            else newColorStage = 1;

            if(prop.type == 2)
            {
                switch(newColorStage)
                {
                    case 1:
                          newColorStage = 2;
                          break;
                    case 5:
                          newColorStage = 4;
                          break;
                    default:
                          break;
                }
            }
          
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

    
    private PapiProperties prop;
    private float heightAboveLandSurface;
    private Eff3DActor eff[][] = new Eff3DActor[4][3];
    private static PapiProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private NetMsgFiltered outCommand;
    private static final double glideScopeInRads = Math.toRadians(3D);
    private int oldColorStage;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private Actor actorme = this;
    private boolean bVisible = true;

}
