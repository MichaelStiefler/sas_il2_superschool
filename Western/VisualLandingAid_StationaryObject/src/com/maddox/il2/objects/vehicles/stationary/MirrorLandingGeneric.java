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

public abstract class MirrorLandingGeneric extends VisualLandingAidGeneric
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
                System.out.print("MirrorLanding: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            } else
            {
                return s2;
            }
        }

        private static MirrorLandingProperties LoadMirrorLandingProperties(SectFile sectfile, String s, Class class1)
        {
            MirrorLandingProperties mirrorlandingproperties = new MirrorLandingProperties();
            mirrorlandingproperties.meshName = getS(sectfile, "Mirrorlanding", s + ":Mesh");
            Property.set(class1, "iconName", "icons/unknown.mat");
            Property.set(class1, "meshName", mirrorlandingproperties.meshName);
            return mirrorlandingproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg)
        {
            MirrorLandingGeneric mirrorlandinggeneric = null;
            try
            {
                MirrorLandingGeneric.constr_arg1 = proper;
                MirrorLandingGeneric.constr_arg2 = actorspawnarg;
                mirrorlandinggeneric = (MirrorLandingGeneric)cls.newInstance();
                MirrorLandingGeneric.constr_arg1 = null;
                MirrorLandingGeneric.constr_arg2 = null;
            }
            catch(Exception exception)
            {
                MirrorLandingGeneric.constr_arg1 = null;
                MirrorLandingGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create MirrorLandingGeneric object [class:" + cls.getName() + "]");
                return null;
            }
            return mirrorlandinggeneric;
        }

        public Class cls;
        public MirrorLandingProperties proper;

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
                proper = LoadMirrorLandingProperties(Statics.getTechnicsFile(), s1, class1);
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

    public static class MirrorLandingProperties
    {

        public String meshName;

        public MirrorLandingProperties()
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

    protected MirrorLandingGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private MirrorLandingGeneric(MirrorLandingProperties mirrorlandingproperties, ActorSpawnArg actorspawnarg)
    {
        super(mirrorlandingproperties.meshName);
        prop = null;
        outCommand = new NetMsgFiltered();
        prop = mirrorlandingproperties;
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
        Meatballxyz[0] = Meatballxyz[1] = Meatballxyz[2] = 0.0F;
        Meatballypr[0] = Meatballypr[1] = Meatballypr[2] = 0.0F;
        Align();
        startMove();
        if(!Config.isUSE_RENDER())
            return;
        if(Main.state() != null && Main.state().id() == 18)
            Eff3D.initSetTypeTimer(true);
        DatumLightOn();
        MeatballOn();
        MeatballCenter();
    }

    private void DatumLightOff()
    {
        for(int i=0; i<12; i++)
            Eff3DActor.finish(effdatum[i]);
    }

    private void DatumLightOn()
    {
        effdatum[0] = Eff3DActor.New(this, findHook("_datum01"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[1] = Eff3DActor.New(this, findHook("_datum02"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[2] = Eff3DActor.New(this, findHook("_datum03"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[3] = Eff3DActor.New(this, findHook("_datum04"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[4] = Eff3DActor.New(this, findHook("_datum05"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[5] = Eff3DActor.New(this, findHook("_datum06"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[6] = Eff3DActor.New(this, findHook("_datum07"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[7] = Eff3DActor.New(this, findHook("_datum08"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[8] = Eff3DActor.New(this, findHook("_datum09"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[9] = Eff3DActor.New(this, findHook("_datum10"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[10] = Eff3DActor.New(this, findHook("_datum11"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effdatum[11] = Eff3DActor.New(this, findHook("_datum12"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void CutLightOff()
    {
        for(int i=0; i<4; i++)
            Eff3DActor.finish(effcut[i]);
    }

    private void CutLightOn()
    {
        effcut[0] = Eff3DActor.New(this, findHook("_cut01"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effcut[1] = Eff3DActor.New(this, findHook("_cut02"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effcut[2] = Eff3DActor.New(this, findHook("_cut03"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        effcut[3] = Eff3DActor.New(this, findHook("_cut04"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void WaveoffOff()
    {
        for(int i=0; i<4; i++)
            Eff3DActor.finish(effwaveoff[i]);
    }

    private void WaveoffOn()
    {
        effwaveoff[0] = Eff3DActor.New(this, findHook("_waveoffL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[1] = Eff3DActor.New(this, findHook("_waveoffL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[2] = Eff3DActor.New(this, findHook("_waveoffR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effwaveoff[3] = Eff3DActor.New(this, findHook("_waveoffR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    private void EmergencyWaveoffOff()
    {
        for(int i=0; i<2; i++)
            Eff3DActor.finish(effemgwaveoff[i]);
    }

    private void EmergencyWaveoffOn()
    {
        effemgwaveoff[0] = Eff3DActor.New(this, findHook("_emgwaveoffL"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effemgwaveoff[1] = Eff3DActor.New(this, findHook("_emgwaveoffR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    private void Reserve1Off()
    {
        for(int i=0; i<2; i++)
            Eff3DActor.finish(effreserve1[i]);
    }

    private void Reserve1On()
    {
        effreserve1[0] = Eff3DActor.New(this, findHook("_reserveL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effreserve1[1] = Eff3DActor.New(this, findHook("_reserveR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    private void Reserve2Off()
    {
        for(int i=0; i<2; i++)
            Eff3DActor.finish(effreserve2[i]);
    }

    private void Reserve2On()
    {
        effreserve2[0] = Eff3DActor.New(this, findHook("_reserveL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        effreserve2[1] = Eff3DActor.New(this, findHook("_reserveR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    private void MeatballOff()
    {
        hierMesh().chunkVisible("Meatball", false);
        Eff3DActor.finish(effmeatball[0]);
    }

    private void MeatballOn()
    {
        hierMesh().chunkVisible("Meatball", true);
        effmeatball[0] = Eff3DActor.New(this, findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
    }

    private void MeatballCenter()
    {
        Meatballxyz[0] = Meatballxyz[1] = Meatballxyz[2] = 0.0F;
        hierMesh().chunkSetLocate("Meatball", Meatballxyz, Meatballypr);
        Eff3DActor.finish(effmeatball[0]);
        effmeatball[0] = Eff3DActor.New(this, findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
    }
  
    private void MeatballSet(float horiz, float vert)
    {
        Meatballxyz[0] = 0.0F;
        Meatballxyz[1] = horiz * -0.6F;
        Meatballxyz[2] = vert * -0.6F;
        hierMesh().chunkSetLocate("Meatball", Meatballxyz, Meatballypr);
        if(Time.current() % 100F == 0F)
        {
            Eff3DActor.finish(effmeatball[0]);
            effmeatball[0] = Eff3DActor.New(this, findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
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
        {
            MeatballOn();
            for(int i=0; i<12; i++)
                effdatum[i]._setIntesity(0.4F);
            for(int i=0; i<4; i++)
                effcut[i]._setIntesity(1.0F);
            for(int i=0; i<4; i++)
                effwaveoff[i]._setIntesity(1.0F);
            for(int i=0; i<2; i++)
                effemgwaveoff[i]._setIntesity(1.0F);
            for(int i=0; i<2; i++)
                effreserve1[i]._setIntesity(1.0F);
            for(int i=0; i<2; i++)
                effreserve2[i]._setIntesity(1.0F);
        }
        else
        {
            MeatballOff();
            for(int i=0; i<12; i++)
                effdatum[i]._setIntesity(0.0F);
            for(int i=0; i<4; i++)
                effcut[i]._setIntesity(0.0F);
            for(int i=0; i<4; i++)
                effwaveoff[i]._setIntesity(0.0F);
            for(int i=0; i<2; i++)
                effemgwaveoff[i]._setIntesity(0.0F);
            for(int i=0; i<2; i++)
                effreserve1[i]._setIntesity(0.0F);
            for(int i=0; i<2; i++)
                effreserve2[i]._setIntesity(0.0F);
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
                if(meatballmoving)
                {
                    MeatballCenter();
                    CutLightOff();
                    cutlighton = false;
                }
                meatballmoving = false;
                return true;
            }
            float glidepath = visuallandingcalc.getPapiGlidePath(actorme, aircraft1);
            float vert = Aircraft.cvt(glidepath, -0.8F, 0.8F, -1.0F, 1.0F);
            float beacon = visuallandingcalc.getPapiAzimuthPB(actorme, aircraft1);
            float horiz = Aircraft.cvt(beacon, -92F, 92F, -1.0F, 1.0F);
            MeatballSet(horiz, vert);

            if(glidepath > 0.7F)
            {
                if(!cutlighton)
                {
                    CutLightOn();
                    cutlighton = true;
                }
            }
            else
            {
                if(cutlighton)
                {
                    CutLightOff();
                    cutlighton = false;
                }
            }
            meatballmoving = true;
            return true;
        }
      
        Move()
        {
        }
    }

    
    private MirrorLandingProperties prop;
    private float heightAboveLandSurface;
    private Eff3DActor effmeatball[] = new Eff3DActor[1];
    private Eff3DActor effdatum[] = new Eff3DActor[12];
    private Eff3DActor effcut[] = new Eff3DActor[4];
    private Eff3DActor effwaveoff[] = new Eff3DActor[4];
    private Eff3DActor effemgwaveoff[] = new Eff3DActor[2];
    private Eff3DActor effreserve1[] = new Eff3DActor[2];
    private Eff3DActor effreserve2[] = new Eff3DActor[2];
    private static MirrorLandingProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private NetMsgFiltered outCommand;
    private static final double glideScopeInRads = Math.toRadians(3D);
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private Actor actorme = this;
    private float[] Meatballxyz = new float[3];
    private float[] Meatballypr = new float[3];
    private boolean meatballmoving = false;
    private boolean cutlighton = false;
    private boolean bVisible = true;

  
}
