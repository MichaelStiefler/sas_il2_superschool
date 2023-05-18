package com.maddox.il2.engine.hotkey;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.rts.*;

public class MouseXYZATK
    implements MsgMouseListener
{

    public void resetGame()
    {
        target = null;
    }

    public void setTarget(Actor actor)
    {
        target = actor;
    }

    public void setCamera(Actor actor)
    {
        camera = actor;
    }

    public boolean use(boolean flag)
    {
        boolean flag1 = bUse;
        bUse = flag;
        if(target != null)
            target.pos.inValidate(true);
        return flag1;
    }

    float DEG2RAD(float f)
    {
        return f * 0.01745329F;
    }

    public void msgMouseButton(int i, boolean flag)
    {
    }

    public void msgMouseAbsMove(int i, int j, int k)
    {
    }

    public void msgMouseMove(int i, int j, int k)
    {
        if(bUse && Actor.isValid(target) && Actor.isValid(camera) && iMode != -1)
        {
            camera.pos.getAbs(cameraO);
            target.pos.getRel(p, o);
            switch(iMode)
            {
            case 0: // '\0'
            case 4: // '\004'
                double d = Math.sin(DEG2RAD(cameraO.azimut()));
                double d1 = Math.cos(DEG2RAD(cameraO.azimut()));
                p.x += -d * (double)i * (double)koof[iKoof] + d1 * (double)j * (double)koof[iKoof];
                p.y += -d1 * (double)i * (double)koof[iKoof] - d * (double)j * (double)koof[iKoof];
                break;

            case 1: // '\001'
            case 5: // '\005'
                p.z += (float)j * koof[iKoof];
                break;

            case 2: // '\002'
                o.set(o.azimut() + (float)i * koofAngle, o.tangage() + (float)j * koofAngle, o.kren());
                break;

            case 3: // '\003'
                o.set(o.azimut(), o.tangage() + (float)j * koofAngle, o.kren() + (float)i * koofAngle);
                break;

            case 6: // '\006'
                o.set(o.azimut() + (float)i * koofAngle, o.tangage(), o.kren());
                break;

            case 7: // '\007'
                o.set(o.azimut(), o.tangage() + (float)i * koofAngle, o.kren());
                break;

            case 8: // '\b'
                o.set(o.azimut(), o.tangage(), o.kren() + (float)i * koofAngle);
                break;
            }
            target.pos.setRel(p, o);
        }
    }

    private void initHotKeys(String s)
    {
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "SpeedSlow") {

            public void begin()
            {
                if(bUse)
                    iKoof = 0;
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "SpeedNormal") {

            public void begin()
            {
                if(bUse)
                    iKoof = 1;
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "SpeedFast") {

            public void begin()
            {
                if(bUse)
                    iKoof = 2;
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "XY") {

            public void begin()
            {
                if(bUse)
                    iMode = 0;
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "Z") {

            public void begin()
            {
                if(bUse)
                    iMode = 1;
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "AT") {

            public void begin()
            {
                if(bUse)
                    iMode = 2;
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "KT") {

            public void begin()
            {
                if(bUse)
                    iMode = 3;
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "XYmove") {

            public void begin()
            {
                saveHookUseMouse(4);
            }

            public void end()
            {
                restoreHookUseMouse();
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "Zmove") {

            public void begin()
            {
                saveHookUseMouse(5);
            }

            public void end()
            {
                restoreHookUseMouse();
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "Amove") {

            public void begin()
            {
                saveHookUseMouse(6);
            }

            public void end()
            {
                restoreHookUseMouse();
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "Tmove") {

            public void begin()
            {
                saveHookUseMouse(7);
            }

            public void end()
            {
                restoreHookUseMouse();
            }

        }
);
        HotKeyCmdEnv.addCmd(s, new HotKeyCmd(bRealTime, "Kmove") {

            public void begin()
            {
                saveHookUseMouse(8);
            }

            public void end()
            {
                restoreHookUseMouse();
            }

        }
);
    }

    private void saveHookUseMouse(int i)
    {
        if(!bUse || iMode != -1)
            return;
        iMode = i;
        if(HookView.current == null)
        {
            return;
        } else
        {
            bSaveHookUseMouse = HookView.current.isUseMouse();
            HookView _tmp = HookView.current;
            HookView.useMouse(false);
            return;
        }
    }

    private void restoreHookUseMouse()
    {
        if(!bUse || iMode == -1)
            return;
        iMode = -1;
        if(HookView.current == null)
        {
            return;
        } else
        {
            HookView _tmp = HookView.current;
            HookView.useMouse(bSaveHookUseMouse);
            return;
        }
    }

    public MouseXYZATK(String s)
    {
        bRealTime = false;
        koofAngle = 0.1F;
        iKoof = 1;
        iMode = -1;
        target = null;
        camera = null;
        bUse = true;
        o = new Orient();
        p = new Point3d();
        cameraO = new Orient();
        String s1 = s + " Config";
        bRealTime = Config.cur.ini.get(s1, "RealTime", bRealTime);
        HotKeyEnv.fromIni(s, Config.cur.ini, s);
        MsgAddListener.post(bRealTime ? 64 : 0, Mouse.adapter(), this, null);
        initHotKeys(s);
    }

    private boolean bRealTime;
    private static final int UNKNOWN = -1;
    private static final int XY = 0;
    private static final int Z = 1;
    private static final int AT = 2;
    private static final int KT = 3;
    private static final int XYmove = 4;
    private static final int Zmove = 5;
    private static final int Amove = 6;
    private static final int Tmove = 7;
    private static final int Kmove = 8;
    private float koofAngle;
//    private float koof[] = {
//        0.02F, 1.0F, 10F
//    };
    private float koof[] = {
        0.02F, 0.2F, 1.0F
    };
    private int iKoof;
    private int iMode;
    private Actor target;
    private Actor camera;
    private boolean bUse;
    private Orient o;
    private Point3d p;
    private Orient cameraO;
    private boolean bSaveHookUseMouse;





}
