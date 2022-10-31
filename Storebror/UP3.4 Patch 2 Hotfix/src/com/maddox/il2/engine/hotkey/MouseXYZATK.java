package com.maddox.il2.engine.hotkey;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.HotKeyCmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Mouse;
import com.maddox.rts.MsgAddListener;
import com.maddox.rts.MsgMouseListener;

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

    public void msgMouseMove(int x, int y, int z)
    {
        if(bUse && Actor.isValid(target) && Actor.isValid(camera) && iMode != UNKNOWN)
        {
            camera.pos.getAbs(cameraO);
            target.pos.getRel(p, o);
            switch(iMode)
            {
            case XY:
            case XYmove:
                double d = Math.sin(DEG2RAD(cameraO.azimut()));
                double d1 = Math.cos(DEG2RAD(cameraO.azimut()));
                p.x += -d * (double)x * (double)koof[iKoof] + d1 * (double)y * (double)koof[iKoof];
                p.y += -d1 * (double)x * (double)koof[iKoof] - d * (double)y * (double)koof[iKoof];
                break;

            case Z:
            case Zmove:
                p.z += (float)y * koof[iKoof];
                break;

            case AT:
                o.set(o.azimut() + (float)x * koofAngle, o.tangage() + (float)y * koofAngle, o.kren());
                break;

            case KT:
                o.set(o.azimut(), o.tangage() + (float)y * koofAngle, o.kren() + (float)x * koofAngle);
                break;

            case Amove:
                o.set(o.azimut() + (float)x * koofAngle, o.tangage(), o.kren());
                break;

            case Tmove:
                o.set(o.azimut(), o.tangage() + (float)x * koofAngle, o.kren());
                break;

            case Kmove:
                o.set(o.azimut(), o.tangage(), o.kren() + (float)x * koofAngle);
                break;
            }
            target.pos.setRel(p, o);
        }
    }

    private void initHotKeys(String paramString) {
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "SpeedSlow") {
            public void begin() {
                if (MouseXYZATK.this.bUse)
                    MouseXYZATK.this.iKoof = 0;
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "SpeedNormal") {
            public void begin() {
                if (MouseXYZATK.this.bUse)
                    MouseXYZATK.this.iKoof = 1;
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "SpeedFast") {
            public void begin() {
                if (MouseXYZATK.this.bUse)
                    MouseXYZATK.this.iKoof = 2;
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "XY") {
            public void begin() {
                if (MouseXYZATK.this.bUse)
                    MouseXYZATK.this.iMode = 0;
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "Z") {
            public void begin() {
                if (MouseXYZATK.this.bUse)
                    MouseXYZATK.this.iMode = 1;
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "AT") {
            public void begin() {
                if (MouseXYZATK.this.bUse)
                    MouseXYZATK.this.iMode = 2;
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "KT") {
            public void begin() {
                if (MouseXYZATK.this.bUse)
                    MouseXYZATK.this.iMode = 3;
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "XYmove") {
            public void begin() {
                MouseXYZATK.this.saveHookUseMouse(4);
            }

            public void end() {
                MouseXYZATK.this.restoreHookUseMouse();
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "Zmove") {
            public void begin() {
                MouseXYZATK.this.saveHookUseMouse(5);
            }

            public void end() {
                MouseXYZATK.this.restoreHookUseMouse();
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "Amove") {
            public void begin() {
                MouseXYZATK.this.saveHookUseMouse(6);
            }

            public void end() {
                MouseXYZATK.this.restoreHookUseMouse();
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "Tmove") {
            public void begin() {
                MouseXYZATK.this.saveHookUseMouse(7);
            }

            public void end() {
                MouseXYZATK.this.restoreHookUseMouse();
            }
        });
        HotKeyCmdEnv.addCmd(paramString, new HotKeyCmd(this.bRealTime, "Kmove") {
            public void begin() {
                MouseXYZATK.this.saveHookUseMouse(8);
            }

            public void end() {
                MouseXYZATK.this.restoreHookUseMouse();
            }
        });
    }

    private void saveHookUseMouse(int i)
    {
        if(!bUse || iMode != UNKNOWN)
            return;
        iMode = i;
        if(HookView.current == null)
        {
            return;
        } else
        {
            bSaveHookUseMouse = HookView.isUseMouse();
            HookView.useMouse(false);
            return;
        }
    }

    private void restoreHookUseMouse()
    {
        if(!bUse || iMode == UNKNOWN)
            return;
        iMode = UNKNOWN;
        if(HookView.current == null)
        {
            return;
        } else
        {
            HookView.useMouse(bSaveHookUseMouse);
            return;
        }
    }

    public MouseXYZATK(String s)
    {
        bRealTime = false;
        koofAngle = 0.1F;
        iKoof = 1;
        iMode = UNKNOWN;
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
    private float koof[] = {
        0.02F, 1.0F, 10F
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
