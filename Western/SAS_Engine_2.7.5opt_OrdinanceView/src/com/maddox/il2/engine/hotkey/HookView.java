
package com.maddox.il2.engine.hotkey;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.il2.objects.ActorViewPoint;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.rts.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class HookView extends HookRender
{
    static class ClipFilter
        implements ActorFilter
    {

        public boolean isUse(Actor actor, double d)
        {
            return actor instanceof BigshipGeneric;
        }

        ClipFilter()
        {
        }
    }


    public boolean getFollow()
    {
        return bFollow;
    }

    public void setFollow(boolean flag)
    {
        bFollow = flag;
    }

    protected boolean isUseCommon()
    {
        return useFlags != 0;
    }

    protected void useCommon(int i, boolean flag)
    {
        if(bUse)
            useFlags |= i;
        else
            useFlags &= ~i;
    }

    protected static float minLen()
    {
        if(_visibleR > 0.0F)
            return _visibleR + 1.5F;
        else
            return _minLen;
    }

    public static float defaultLen()
    {
        if(_visibleR > 0.0F)
            return _visibleR * 3F;
        else
            return _defaultLen;
    }

    protected static float maxLen()
    {
        return _maxLen;
    }

    public float len()
    {
        return len;
    }

    public void resetGame()
    {
        lastBaseActor = null;
        _visibleR = -1F;
        _Azimut = Azimut = 0.0F;
        _Tangage = Tangage = 0.0F;
        timeViewSet = -2000L;
    }

    public void saveRecordedStates(PrintWriter printwriter)
        throws Exception
    {
        printwriter.println(len);
        printwriter.println(Azimut);
        printwriter.println(_Azimut);
        printwriter.println(Tangage);
        printwriter.println(_Tangage);
        printwriter.println(o.azimut());
        printwriter.println(o.tangage());
        printwriter.println(koofAzimut);
        printwriter.println(koofTangage);
        printwriter.println(koofLen);
        printwriter.println(_minLen);
        printwriter.println(_defaultLen);
        printwriter.println(_maxLen);
        printwriter.println(koofSpeed);
    }

    public void loadRecordedStates(BufferedReader bufferedreader)
        throws Exception
    {
        len = Float.parseFloat(bufferedreader.readLine());
        Azimut = Float.parseFloat(bufferedreader.readLine());
        _Azimut = Float.parseFloat(bufferedreader.readLine());
        Tangage = Float.parseFloat(bufferedreader.readLine());
        _Tangage = Float.parseFloat(bufferedreader.readLine());
        o.set(Float.parseFloat(bufferedreader.readLine()), Float.parseFloat(bufferedreader.readLine()), 0.0F);
        koofAzimut = Float.parseFloat(bufferedreader.readLine());
        koofTangage = Float.parseFloat(bufferedreader.readLine());
        koofLen = Float.parseFloat(bufferedreader.readLine());
        _minLen = Float.parseFloat(bufferedreader.readLine());
        _defaultLen = Float.parseFloat(bufferedreader.readLine());
        _maxLen = Float.parseFloat(bufferedreader.readLine());
        koofSpeed = Float.parseFloat(bufferedreader.readLine());
    }

    public void reset()
    {
        timeViewSet = -2000L;
        if(AircraftHotKeys.bFirstHotCmd)
        {
            return;
        } else
        {
            set(lastBaseActor, defaultLen(), 0.0F, 0.0F);
            return;
        }
    }

    public void set(Actor actor, float f, float f1)
    {
        set(actor, defaultLen(), f, f1);
    }

    public void set(Actor actor, float f, float f1, float f2)
    {
        lastBaseActor = actor;
        _visibleR = -1F;
        if(Actor.isValid(actor) && (actor instanceof ActorMesh))
            _visibleR = ((ActorMesh)actor).mesh().visibilityR();
        o.set(f1, f2, 0.0F);
        _Azimut = Azimut = 0.0F;
        _Tangage = Tangage = 0.0F;
        prevTime = Time.real();
        HookView _tmp = this;
        len = f;
        if(camera != null)
        {
            Actor actor1 = camera.pos.base();
            if(actor1 != null)
            {
                actor1.pos.getAbs(o);
                o.increment(f1, f2, 0.0F);
                o.set(o.azimut(), o.tangage(), 0.0F);
            }
            if(bUse)
                camera.pos.inValidate(true);
        }
        o.wrap();
    }

    private float bvalue(float f, float f1, long l)
    {
        float f2 = (koofSpeed * (float)l) / 30F;
        if(f == f1)
            return f;
        if(f > f1)
            if(f < f1 + f2)
                return f;
            else
                return f1 + f2;
        if(f > f1 - f2)
            return f;
        else
            return f1 - f2;
    }

    public boolean computeRenderPos(Actor actor, Loc loc, Loc loc1)
    {
        long l = Time.currentReal();
        if(l != prevTime)
        {
            long l1 = l - prevTime;
            prevTime = l;
            if(_Azimut != Azimut || _Tangage != Tangage)
            {
                Azimut = bvalue(_Azimut, Azimut, l1);
                Tangage = bvalue(_Tangage, Tangage, l1);
                loc.get(o);
                float f = o.azimut() + Azimut;
                o.set(f, Tangage, 0.0F);
                o.wrap360();
            }
            if(_Azimut == Azimut && (Azimut > 180F || Azimut < -180F))
            {
                Azimut %= 360F;
                if(Azimut > 180F)
                    Azimut -= 360F;
                else
                if(Azimut < -180F)
                    Azimut += 360F;
                _Azimut = Azimut;
            }
            if(_Tangage == Tangage && (Tangage > 180F || Tangage < -180F))
            {
                Tangage %= 360F;
                if(Tangage > 180F)
                    Tangage -= 360F;
                else
                if(Tangage < -180F)
                    Tangage += 360F;
                _Tangage = Tangage;
            }
        }
        computePos(actor, loc, loc1);
        return true;
    }

    public void computePos(Actor actor, Loc loc, Loc loc1)
    {
        loc.get(pAbs, oAbs);
        if(bUse)
        {
            if(lastBaseActor != actor)
            {
                lastBaseActor = actor;
                _visibleR = -1F;
                if(Actor.isValid(actor) && (actor instanceof ActorMesh))
                    _visibleR = ((ActorMesh)actor).mesh().visibilityR();
            }
            p.set(-len, 0.0D, 0.0D);
            if(bFollow)
                o.set(oAbs.getAzimut(), o.getTangage(), o.getKren());
            o.transform(p);
            pAbs.add(p);
            if(bClipOnLand)
            {
                double d = Engine.land().HQ_Air(pAbs.x, pAbs.y) + 2D;
                if(pAbs.z < d)
                    pAbs.z = d;
                pClipZ1.set(pAbs);
                pClipZ2.set(pAbs);
                pClipZ1.z -= 2D;
                pClipZ2.z += 42D;
                Actor actor1 = Engine.collideEnv().getLine(pClipZ2, pClipZ1, false, clipFilter, pClipRes);
                if(Actor.isValid(actor1) && pAbs.z < pClipRes.z + 2D)
                    pAbs.z = pClipRes.z + 2D;
            }
            loc1.set(pAbs, o);
        } else
        {
            loc1.set(pAbs, oAbs);
        }
    }

    public void setCamera(Actor actor)
    {
        camera = actor;
    }

    public void use(boolean flag)
    {
        bUse = flag;
        if(camera != null)
            camera.pos.inValidate(true);
        useCommon(1, bUse);
    }

    public boolean isUse()
    {
        return bUse;
    }

    public static void useMouse(boolean flag)
    {
        bUseMouse = flag;
    }

    public static boolean isUseMouse()
    {
        return bUseMouse;
    }

    public boolean clipOnLand(boolean flag)
    {
        boolean flag1 = bClipOnLand;
        bClipOnLand = flag;
        return flag1;
    }

    protected void clipLen(Actor actor)
    {
        if(len < minLen())
            if(Actor.isValid(actor) && (actor instanceof ActorViewPoint))
            {
                if(len < -maxLen())
                    len = -maxLen();
            } else
            {
                len = minLen();
            }
        if(len > maxLen())
            len = maxLen();
    }

    protected void mouseMove(int i, int j, int k)
    {
        if(bUseMouse && isUseCommon())
        {
            if(bChangeLen)
            {
                len += (float)j * koofLen;
                clipLen(lastBaseActor);
            } else
            {
                if(Time.real() < timeViewSet + 1000L)
                    return;
                if(bFollow)
                    i = 0;
                if(bUse)
                {
                    o.set(o.azimut() + (float)i * koofAzimut, o.tangage() + (float)j * koofTangage, 0.0F);
                    o.wrap360();
                }
            }
            if(Actor.isValid(camera))
                camera.pos.inValidate(true);
            Azimut = _Azimut;
            Tangage = _Tangage;
        }
    }

    public void viewSet(float f, float f1)
    {
        if(!bUseMouse && !isUseCommon())
            return;
        timeViewSet = Time.real();
        f %= 360F;
        if(f > 180F)
            f -= 360F;
        else
        if(f < -180F)
            f += 360F;
        f1 %= 360F;
        if(f1 > 180F)
            f1 -= 360F;
        else
        if(f1 < -180F)
            f1 += 360F;
        if(bFollow)
        {
            o.set(o.azimut(), f1, 0.0F);
            o.wrap360();
        } else
        {
            Azimut = _Azimut = f;
            Tangage = _Tangage = f1;
            o.set(Azimut, Tangage, 0.0F);
        }
        if(Actor.isValid(camera))
            camera.pos.inValidate(true);
    }

    public void snapSet(float f, float f1)
    {
        if(!bUse || !bUseMouse || !isUseCommon())
            return;
        if(bFollow)
            f = 0.0F;
        _Azimut = 45F * f;
        _Tangage = 44F * f1;
        if(camera != null)
        {
            Actor actor = camera.pos.base();
            if(actor != null)
            {
                Azimut = (o.azimut() - actor.pos.getAbsOrient().azimut()) % 360F;
                if(Azimut > _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut -= 360F);
                else
                if(Azimut < _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut += 360F);
                Tangage = o.tangage() % 360F;
                if(Tangage > _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage -= 360F);
                else
                if(Tangage < _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage += 360F);
                camera.pos.inValidate(true);
            }
        }
    }

    public void panSet(int i, int j)
    {
        if(!bUse || !bUseMouse || !isUseCommon())
            return;
        if(i == 0 && j == 0)
        {
            _Azimut = 0.0F;
            _Tangage = 0.0F;
        }
        _Azimut = (float)i * stepAzimut + _Azimut;
        if(bFollow)
            _Azimut = 0.0F;
        _Tangage = (float)j * stepTangage + _Tangage;
        if(camera != null)
        {
            Actor actor = camera.pos.base();
            if(actor != null)
            {
                Azimut = (o.azimut() - actor.pos.getAbsOrient().azimut()) % 360F;
                if(Azimut > _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut -= 360F);
                else
                if(Azimut < _Azimut)
                    for(; Math.abs(Azimut - _Azimut) > 180F; Azimut += 360F);
                Tangage = o.tangage() % 360F;
                if(Tangage > _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage -= 360F);
                else
                if(Tangage < _Tangage)
                    for(; Math.abs(Tangage - _Tangage) > 180F; Tangage += 360F);
                camera.pos.inValidate(true);
            }
        }
    }

    private void initHotKeys(String s)
    {
        HotKeyCmdEnv.addCmd(s, new HotKeyCmdMouseMove(true, "Move") {

            public void move(int i, int j, int k)
            {
                mouseMove(i, j, k);
            }

            public void created()
            {
                setRecordId(27);
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "Reset") {

            public void begin()
            {
                if(isUseCommon())
                    reset();
            }

            public void created()
            {
                setRecordId(28);
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(true, "Len") {

            public void begin()
            {
                if(isUseCommon())
                    HookView.bChangeLen = true;
            }

            public void end()
            {
                if(isUseCommon())
                    HookView.bChangeLen = false;
            }

            public void created()
            {
                setRecordId(29);
            }

        }
);
//  original Pablo's code , a part of VisualMOD V9
//        GUIMenu.initHotKeys();
        String sAV = "aircraftView";
        HotKeyCmdEnv.setCurrentEnv(sAV);
        HotKeyEnv.fromIni(sAV, Config.cur.ini, "HotKey " + sAV);
        HotKeyCmdEnv.addCmd(new HotKeyCmd(true, "OrdinanceView", "52") {

            public void created()
            {
                setRecordId(246);
            }

            public void begin()
            {
                if(World.cur().diffCur.No_Outside_Views)
                    return;
                Actor actor = myLatestReleasedOrdinance();
                if(Actor.isValid(actor))
                {
                    boolean flag = !Main3D.cur3D().isViewOutside();
                    Main3D.cur3D().setViewFlow10(actor, false);
//  original Pablo's code , a part of VisualMOD V9
//                    if(flag)
//                        switchToAIGunner();
                }
            }

        }
);
    }

    private Actor myLatestReleasedOrdinance()
    {
        if(Selector.isEnableTrackArgs())
            return Selector.setCurRecordArg0(Selector.getTrackArg0());
        Aircraft aircraft = World.cur().getPlayerAircraft();

        if(aircraft != null)
        {
            for(int i = Engine.ordinances().size() - 1; i >= 0; i--)
            {
                Actor actor = (Actor)Engine.ordinances().get(i);

          // debug code, not neccesary for publishing
          //      System.out.println("Ordinance " + actor + " 's owner: " + actor.getOwner() + " myAC: " + aircraft);

                if(actor != null && !(actor instanceof RocketNull) && !(actor instanceof BombNull) && actor.getOwner() == aircraft)
                {
          // debug code, not neccesary for publishing
          //          System.out.println("Ordinance View is newly set on " + actor);

                    return Selector.setCurRecordArg0(actor);
                }
            }
        }

        return Selector.setCurRecordArg0(null);
    }

    public static void loadConfig()
    {
        koofAzimut = Config.cur.ini.get(sectConf, "AzimutSpeed", koofAzimut, 0.01F, 1.0F);
        koofTangage = Config.cur.ini.get(sectConf, "TangageSpeed", koofTangage, 0.01F, 1.0F);
        koofLen = Config.cur.ini.get(sectConf, "LenSpeed", koofLen, 0.1F, 10F);
        _minLen = Config.cur.ini.get(sectConf, "MinLen", _minLen, 1.0F, 20F);
        _defaultLen = Config.cur.ini.get(sectConf, "DefaultLen", _defaultLen, 1.0F, 100F);
        _maxLen = Config.cur.ini.get(sectConf, "MaxLen", _maxLen, 1.0F, 50000F);
        if(_defaultLen < _minLen)
            _defaultLen = _minLen;
        if(_maxLen < _defaultLen)
            _maxLen = _defaultLen;
        koofSpeed = Config.cur.ini.get(sectConf, "Speed", koofSpeed, 0.1F, 100F);
        koofLeanF = Config.cur.ini.get(sectConf, "LeanF", koofLeanF, 0.01F, 1.0F) * 0.5F;
        koofLeanS = Config.cur.ini.get(sectConf, "LeanS", koofLeanS, 0.01F, 1.0F) * 0.5F;
        koofRaise = Config.cur.ini.get(sectConf, "Raise", koofRaise, 0.01F, 1.0F) * 0.5F;
        rubberBandStretch = Config.cur.ini.get(sectConf, "RubberBand", rubberBandStretch, 0.0F, 1.0F) * 0.08F;
    }

    public static void saveConfig()
    {
        Config.cur.ini.setValue(sectConf, "LeanF", Float.toString(koofLeanF * 2.0F));
        Config.cur.ini.setValue(sectConf, "LeanS", Float.toString(koofLeanS * 2.0F));
        Config.cur.ini.setValue(sectConf, "Raise", Float.toString(koofRaise * 2.0F));
        Config.cur.ini.setValue(sectConf, "RubberBand", Float.toString(rubberBandStretch * 12.5F));
        Config.cur.ini.saveFile();
    }

    public HookView(String s)
    {
        camera = null;
        bUse = false;
        bClipOnLand = true;
        o = new Orientation();
        p = new Point3d();
        pAbs = new Point3d();
        oAbs = new Orient();
        bFollow = false;
        timeViewSet = -2000L;
        HotKeyEnv.fromIni(s, Config.cur.ini, s);
        sectConf = s + " Config";
        loadConfig();
        initHotKeys(s);
        current = this;
    }

    protected HookView()
    {
        camera = null;
        bUse = false;
        bClipOnLand = true;
        o = new Orientation();
        p = new Point3d();
        pAbs = new Point3d();
        oAbs = new Orient();
        bFollow = false;
        timeViewSet = -2000L;
    }

    public static HookView cur()
    {
        return current;
    }

    protected static Actor lastBaseActor = null;
    protected Actor camera;
    protected boolean bUse;
    protected boolean bClipOnLand;
    protected static final int EXT = 1;
    protected static boolean bUseMouse = true;
    protected static boolean bChangeLen = false;
    protected static int useFlags = 0;
    private static float _minLen = 2.0F;
    private static float _defaultLen;
    private static float _maxLen = 500F;
    protected static float _visibleR = -1F;
    protected static float len;
    protected static float stepAzimut = 45F;
    protected static float stepTangage = 30F;
    protected static float maxAzimut = 179F;
    protected static float maxTangage = 89F;
    protected static float minTangage = -89F;
    protected static float Azimut = 0.0F;
    protected static float Tangage = 0.0F;
    protected static float _Azimut = 0.0F;
    protected static float _Tangage = 0.0F;
    protected static long prevTime = 0L;
    protected static float koofAzimut = 0.1F;
    protected static float koofTangage = 0.1F;
    protected static float koofLen = 1.0F;
    protected static float koofSpeed = 6F;
    public static float koofLeanF = 0.4F;
    public static float koofLeanS = 0.4F;
    public static float koofRaise = 0.2F;
    public static float rubberBandStretch = 0.5F;
    protected Orient o;
    protected Point3d p;
    protected Point3d pAbs;
    protected Orient oAbs;
    private boolean bFollow;
    private static Point3d pClipZ1 = new Point3d();
    private static Point3d pClipZ2 = new Point3d();
    private static Point3d pClipRes = new Point3d();
    static ClipFilter clipFilter = new ClipFilter();
    private static final float Circ = 360F;
    private long timeViewSet;
    private static String sectConf;
    public static HookView current = null;

    static 
    {
        _defaultLen = 20F;
        len = _defaultLen;
    }
}
