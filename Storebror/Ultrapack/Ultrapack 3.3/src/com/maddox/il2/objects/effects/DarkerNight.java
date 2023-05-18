package com.maddox.il2.objects.effects;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Sun;
import com.maddox.il2.game.Main3D;
import com.maddox.opengl.GLContext;
import com.maddox.opengl.MsgGLContextListener;
import com.maddox.opengl.gl;

public class DarkerNight extends Render implements MsgGLContextListener {

    public void msgGLContext(int i) {
    }

    protected void contextResize(int i, int j) {
    }

    public void preRender() {
        // TODO: +++ DarkerNight Customization Mod by SAS~Storebror +++
        float alphaMin = Sun.cvt(Config.cur.iDarkness, Config.MIN_NIGHT_SETTINGS, Config.MAX_NIGHT_SETTINGS, 0.35F, 1.0F);
        // TODO: --- DarkerNight Customization Mod by SAS~Storebror ---
        if (World.Sun().ToSun.z < 0.0F) {
            this.alpha = World.Sun().sunMultiplier;
            // TODO: +++ DarkerNight Customization Mod by SAS~Storebror +++
//            if (this.alpha < 0.35F)
//                this.alpha = 0.35F;
            if (this.alpha < alphaMin) this.alpha = alphaMin;
            // TODO: --- DarkerNight Customization Mod by SAS~Storebror ---
        } else this.alpha = 1.0F;
    }

    public DarkerNight(int i, float f) {
        super(f);
        this._indx = 0;
        this.alpha = 1.0F;
        this._indx = i;
        this.useClearDepth(false);
        this.useClearColor(false);
        if (this._indx == 0) this.setName("DarkerNight");
        GLContext.getCurrent().msgAddListener(this, null);
        if (i != 0) Main3D.cur3D()._getAspectViewPort(i, this.viewPort);
    }

    public void render() {
        if (this.alpha < 1.0D) {
            Render.clearStates();
            gl.ShadeModel(7425);
            gl.Disable(2929);
            gl.Enable(3553);
            gl.Enable(3042);
            gl.AlphaFunc(516, 0.0F);
            gl.BlendFunc(774, 770);
            gl.Begin(6);
            gl.Color4f(0.0F, 0.0F, 0.0F, this.alpha);
            gl.Vertex2f(0.0F, 3F);
            gl.Vertex2f(-3F, -3F);
            gl.Vertex2f(3F, -3F);
            gl.End();
            gl.BlendFunc(770, 771);
        }
    }

    public void setShow(boolean flag) {
        if (this._indx == 0) super.setShow(flag);
    }

    public boolean isShow() {
        if (this._indx == 0) return super.isShow();
        else return Config.cur.isUse3Renders() && Main3D.cur3D().overLoad.isShow();
    }

    int           _indx;
    private float alpha;
}
