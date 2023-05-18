// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   OverLoad.java

package com.maddox.il2.objects.effects;

import com.maddox.JGP.Point2f;
import com.maddox.TexImage.TexImage;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.opengl.*;
import com.maddox.sound.AudioDevice;

public class OverLoad extends Render
    implements MsgGLContextListener
{

    public void destroy()
    {
        com.maddox.il2.engine.Camera camera = getCamera();
        if(Actor.isValid(camera))
            camera.destroy();
        super.destroy();
    }

    public void msgGLContext(int i)
    {
        if(i == 8)
            Tex[0] = 0;
    }

    protected void contextResize(int i, int j)
    {
    }

    public void preRender()
    {
        if(Tex[0] == 0)
        {
            gl.Enable(3553);
            gl.GenTextures(1, Tex);
            TexImage teximage = new TexImage();
            try
            {
                teximage.LoadTGA("3do/effects/overload/overload.tga");
            }
            catch(Exception exception)
            {
                return;
            }
            gl.BindTexture(3553, Tex[0]);
            gl.TexParameteri(3553, 10242, 10497);
            gl.TexParameteri(3553, 10243, 10497);
            gl.TexParameteri(3553, 10240, 9729);
            gl.TexParameteri(3553, 10241, 9729);
            gl.TexImage2D(3553, 0, 6409, teximage.sx, teximage.sy, 0, 6409, 5121, teximage.image);
        }
    }

    public OverLoad(int i, float f)
    {
        super(f);
        _indx = 0;
        _indx = i;
        useClearDepth(false);
        useClearColor(false);
        if(_indx == 0)
            setName("renderOverLoad");
        GLContext.getCurrent().msgAddListener(this, null);
        if(i != 0)
            Main3D.cur3D()._getAspectViewPort(i, viewPort);
    }

    private static final float clamp(float f, float f1, float f2)
    {
        return Math.min(f2, Math.max(f1, f));
    }

    public void render()
    {
        if(!World.cur().diffCur.Blackouts_N_Redouts)
            return;
        if(!(World.getPlayerFM() instanceof RealFlightModel))
        {
            if(World.getPlayerFM() != null && Main3D.cur3D().cockpitCur != null)
            {
                float f = 1.0F;
                f = World.getPlayerFM().AS.getPilotHealth(Main3D.cur3D().cockpitCur.astatePilotIndx());
                float f1 = 1.0F - f;
                Render.clearStates();
                gl.ShadeModel(7425);
                gl.Disable(2929);
                gl.Enable(3553);
                gl.Enable(3042);
                gl.AlphaFunc(516, 0.0F);
                renderMinus(f1);
            }
            return;
        }
        RealFlightModel realflightmodel = (RealFlightModel)World.getPlayerFM();
        if(realflightmodel == null)
            return;
        float f2 = 1.0F;
        if(Main3D.cur3D().cockpitCur != null)
            f2 = realflightmodel.AS.getPilotHealth(Main3D.cur3D().cockpitCur.astatePilotIndx());
        float f3 = 1.0F - f2;
        if(realflightmodel.saveDeep < 0.02F && realflightmodel.saveDeep > -0.02F && (double)f3 < 0.02D)
            return;
        Render.clearStates();
        gl.ShadeModel(7425);
        gl.Disable(2929);
        gl.Enable(3553);
        gl.Enable(3042);
        gl.AlphaFunc(516, 0.0F);
        if(realflightmodel.saveDeep >= 0.02F)
        {
            renderPlus(realflightmodel.saveDeep);
            renderSound(realflightmodel.saveDeep * 0.66F);
            if((double)f3 >= 0.02D)
                renderMinus(f3);
        } else
        if(realflightmodel.saveDeep <= -0.02F)
        {
            renderMinus(-realflightmodel.saveDeep + f3);
            renderSound(-realflightmodel.saveDeep * 1.35F);
        } else
        {
            renderMinus(f3);
        }
    }

    private void renderSound(float f)
    {
        if(_indx != 0)
            return;
        f = Math.abs(f);
        if(f > 0.7F)
            f = 1.0F;
        else
        if(f < 0.2F)
            f = 0.0F;
        else
            f = (f - 0.2F) / 0.5F;
        AudioDevice.setControl(2000, (int)(f * 100F));
    }

    public void setShow(boolean flag)
    {
        if(_indx != 0)
            return;
        super.setShow(flag);
        if(!flag)
            renderSound(0.0F);
    }

    public boolean isShow()
    {
        if(_indx == 0)
            return super.isShow();
        else
            return Config.cur.isUse3Renders() && Main3D.cur3D().overLoad.isShow();
    }

    private void renderPlus(float f)
    {
        gl.BindTexture(3553, Tex[0]);
        gl.BlendFunc(774, 770);
        if(f >= 0.97F)
            f = 0.97F;
        f *= 3F;
        float f1;
        float f2;
        float f3;
        float f4;
        if(f <= 1.0F)
        {
            f1 = 0.0F;
            f2 = 1.0F;
            f3 = f;
            f4 = 1.0F - f;
        } else
        if(f <= 2.0F)
        {
            f--;
            f1 = 1.0F * f;
            f2 = 1.0F - f * 0.5F;
            f3 = 1.0F - f;
            f4 = 0.0F;
        } else
        {
            f -= 2.0F;
            if(f > 1.0F)
                f = 1.0F;
            f1 = 1.0F - f;
            f2 = 0.5F - f * 0.5F;
            f3 = 0.0F;
            f4 = 0.0F;
        }
        if(_indx != 0)
        {
            f1 = f3;
            f2 = f4;
        }
        gl.Begin(6);
        gl.Color4f(f1, f1, f1, f2);
        gl.TexCoord2f(0.0F, 0.0F);
        Vertex2f(0.0F, 0.0F);
        gl.Color4f(f3, f3, f3, f4);
        for(int i = 0; i <= 16; i++)
        {
            gl.TexCoord2f(tnts[i].x, tnts[i].y);
            Vertex2f(pnts[i].x, pnts[i].y);
        }

        gl.End();
        gl.BlendFunc(770, 771);
    }

    private void renderMinus(float f)
    {
        f = clamp(f, 0.0F, 1.0F);
        gl.Disable(3553);
        gl.BlendFunc(770, 771);
        gl.Begin(6);
        gl.Color4f(1.0F - f, 0.0F, 0.0F, f);
        Vertex2f(0.0F, 0.0F);
        for(int i = 0; i <= 16; i++)
            Vertex2f(pnts[i].x, pnts[i].y);

        gl.End();
        gl.Enable(3553);
    }

    private void Vertex2f(float f, float f1)
    {
        gl.Vertex2f((f + 1.0F) * 0.5F, (f1 + 1.0F) * 0.5F);
    }

    int _indx;
    private int Tex[] = {
        0
    };
    private static Point2f pnts[];
    private static Point2f tnts[];
    static 
    {
        pnts = new Point2f[17];
        tnts = new Point2f[17];
        for(int i = 0; i <= 16; i++)
        {
            pnts[i] = new Point2f();
            tnts[i] = new Point2f();
            double d = ((double)((float)i * 2.0F) * 3.1415926535897931D) / 16D;
            pnts[i].x = (float)Math.cos(d) * 1.48F;
            pnts[i].y = (float)Math.sin(d) * 1.48F;
            tnts[i].x = pnts[i].x * 3.5F;
            tnts[i].y = pnts[i].y * 3.5F;
        }

    }
}
