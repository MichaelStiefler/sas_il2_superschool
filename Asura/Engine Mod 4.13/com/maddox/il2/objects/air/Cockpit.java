// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) deadcode 
// Source File Name:   Cockpit.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.vehicles.radios.*;
import com.maddox.opengl.gl;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            Aircraft, CockpitPilot

public abstract class Cockpit extends Actor
{
    public static class HookOnlyOrient extends Hook
    {

        public void computePos(Actor actor, Loc loc, Loc loc1)
        {
            loc1.set(Cockpit.nullP, loc.getOrient());
        }

        public HookOnlyOrient()
        {
        }
    }

    class NullDraw extends ActorDraw
    {

        public void preRender()
        {
            if(!Actor.isValid(pos.base()))
                return;
            if(nullMesh == null)
                return;
            if(nullMeshIL2 == null)
                return;
            pos.getRender(__l);
            Main3D.cur3D().cameraCockpit.pos.getRender(__p);
            __l.set(__p);
            if(bEnableRenderingBall)
                nullMesh.setPos(__l);
            else
                nullMeshIL2.setPos(__l);
            if(aircraft() != null)
                nullMesh.chunkSetAngles("Ball", aircraft().FM.getAOS(), -aircraft().FM.getAOA(), 0.0F);
            if(bEnableRenderingBall)
                iPreRender = nullMesh.preRender();
            else
                iPreRender = nullMeshIL2.preRender();
        }

        public void render()
        {
            if(iPreRender == 0)
                return;
            if(!Actor.isValid(pos.base()))
                return;
            if(nullMesh == null)
                return;
            if(nullMeshIL2 == null)
                return;
            if(bEnableRenderingBall)
                nullMesh.render();
            else
                nullMeshIL2.render();
        }

        private int iPreRender;

        NullDraw()
        {
            iPreRender = 0;
        }
    }

    class Draw extends ActorDraw
    {

        public void preRender()
        {
            if(!Actor.isValid(pos.base()))
                return;
            if(mesh == null)
            {
                return;
            } else
            {
                pos.getRender(__l);
                mesh.setPos(__l);
                updateBeacons();
                reflectWorldToInstruments(Time.tickOffset());
                iPreRender = mesh.preRender();
                return;
            }
        }

        public void render(boolean flag)
        {
            if(iPreRender == 0)
                return;
            if(!Actor.isValid(pos.base()))
                return;
            if(mesh == null)
                return;
            Render.currentCamera().activateWorldMode(0);
            gl.GetDoublev(2982, flag ? _modelMatrix3DMirror : _modelMatrix3D);
            if(flag)
                gl.GetDoublev(2983, _projMatrix3DMirror);
            Render.currentCamera().deactivateWorldMode();
            pos.getRender(__l);
            lightUpdate(__l, false);
            pos.base().pos.getRender(__p, __o);
            LightPoint.setOffset((float)__p.x, (float)__p.y, (float)__p.z);
            pos.base().pos.getRender(__l);
            pos.base().draw.lightUpdate(__l, false);
            Render.currentLightEnv().prepareForRender(__p, mesh.visibilityR());
            if(flag)
            {
                if(!bExistMirror)
                    System.out.println("*** Internal error: mirr exist");
                if(!Main3D.cur3D().isViewMirror())
                    System.out.println("*** Internal error: mirr isview");
                String s = nameOfActiveMirrorSurfaceChunk();
                String s1 = nameOfActiveMirrorBaseChunk();
                if(s != null)
                {
                    mesh.setCurChunk(s);
                    mesh.chunkVisible(false);
                }
                if(s1 != null)
                {
                    mesh.setCurChunk(s1);
                    mesh.chunkVisible(true);
                }
                mesh.render();
                return;
            }
            if(!bExistMirror)
            {
                mesh.render();
                return;
            }
            boolean flag1 = Main3D.cur3D().isViewMirror();
            String s2 = nameOfActiveMirrorSurfaceChunk();
            String s3 = nameOfActiveMirrorBaseChunk();
            if(flag1)
            {
                if(s2 != null)
                {
                    mesh.setCurChunk(s2);
                    mesh.chunkVisible(true);
                    mesh.renderChunkMirror(_modelMatrix3D, _modelMatrix3DMirror, _projMatrix3DMirror);
                    mesh.chunkVisible(false);
                }
                if(s3 != null)
                {
                    mesh.setCurChunk(s3);
                    mesh.chunkVisible(true);
                }
                mesh.render();
            } else
            {
                if(s2 != null)
                {
                    mesh.setCurChunk(s2);
                    mesh.chunkVisible(false);
                }
                if(s3 != null)
                {
                    mesh.setCurChunk(s3);
                    mesh.chunkVisible(false);
                }
                mesh.render();
            }
        }

        protected double _modelMatrix3D[];
        protected double _modelMatrix3DMirror[];
        protected double _projMatrix3DMirror[];
        private int iPreRender;

        Draw()
        {
            _modelMatrix3D = new double[16];
            _modelMatrix3DMirror = new double[16];
            _projMatrix3DMirror = new double[16];
            iPreRender = 0;
        }
    }

    static class HookCamera3DMirror extends HookRender
    {

        private float computeMirroredCamera(Loc loc, Loc loc1, Point3f point3f, Point3f point3f1, int i, int j, float f, 
                Loc loc2, int ai[])
        {
            loc.getMatrix(cam2w);
            loc1.getMatrix(mir2w);
            cam2mir.set(mir2w);
            cam2mir.invert();
            cam2mir.mul(cam2mir, cam2w);
            cmm2mir.setIdentity();
            cmm2mir.m00 = 0.0D;
            cmm2mir.m10 = 0.0D;
            cmm2mir.m20 = 1.0D;
            cmm2mir.m01 = 1.0D;
            cmm2mir.m11 = 0.0D;
            cmm2mir.m21 = 0.0D;
            cmm2mir.m02 = 0.0D;
            cmm2mir.m12 = 1.0D;
            cmm2mir.m22 = 0.0D;
            cmm2mir.m03 = cam2mir.m03;
            cmm2mir.m13 = cam2mir.m13;
            cmm2mir.m23 = -cam2mir.m23;
            cmm2mir.m03 *= 0.45000000000000001D;
            cmm2mir.m13 *= 0.45000000000000001D;
            cmm2mir.m23 *= 0.45000000000000001D;
            cmm2w.mul(mir2w, cmm2mir);
            mir2cmm.set(cmm2mir);
            mir2cmm.invert();
            float f1 = (float)(-cmm2mir.m23);
            if(f1 <= 0.001F)
                return -1F;
            float f3;
            float f4;
            float f5;
            float f2 = f3 = f4 = f5 = 0.0F;
            for(int k = 0; k < 8; k++)
            {
                switch(k)
                {
                case 0: // '\0'
                    p.set(point3f.x, point3f.y, point3f.z);
                    break;

                case 1: // '\001'
                    p.set(point3f1.x, point3f.y, point3f.z);
                    break;

                case 2: // '\002'
                    p.set(point3f.x, point3f1.y, point3f.z);
                    break;

                case 3: // '\003'
                    p.set(point3f.x, point3f.y, point3f1.z);
                    break;

                case 4: // '\004'
                    p.set(point3f1.x, point3f1.y, point3f1.z);
                    break;

                case 5: // '\005'
                    p.set(point3f.x, point3f1.y, point3f1.z);
                    break;

                case 6: // '\006'
                    p.set(point3f1.x, point3f.y, point3f1.z);
                    break;

                case 7: // '\007'
                    p.set(point3f1.x, point3f1.y, point3f.z);
                    break;
                }
                mir2cmm.transform(p);
                float f7 = -(float)(((double)f1 * p.y) / p.x);
                float f9 = (float)(((double)f1 * p.z) / p.x);
                if(k == 0)
                {
                    f2 = f3 = f7;
                    f4 = f5 = f9;
                    continue;
                }
                if(f7 < f2)
                    f2 = f7;
                if(f7 > f3)
                    f3 = f7;
                if(f9 < f4)
                    f4 = f9;
                if(f9 > f5)
                    f5 = f9;
            }

            float f6 = f3 - f2;
            float f8 = f5 - f4;
            if(f6 <= 0.001F || f8 <= 0.001F)
                return -1F;
            f2 -= 0.001F;
            f3 += 0.001F;
            f4 -= 0.001F;
            f5 += 0.001F;
            f6 = f3 - f2;
            f8 = f5 - f4;
            float f10 = f6 / (float)i;
            float f11 = f10 * f;
            float f12 = f11 * (float)j;
            if(f12 > f8)
            {
                f8 = f12;
                f4 = (f4 + f5) * 0.5F - f8 * 0.5F;
                f5 = f4 + f8;
            } else
            {
                f6 *= f8 / f12;
                f2 = (f2 + f3) * 0.5F - f6 * 0.5F;
                f3 = f2 + f6;
            }
            float f13 = 2.0F * Math.max(Math.abs(f2), Math.abs(f3));
            float f14 = 2.0F * Math.max(Math.abs(f4), Math.abs(f5));
            int l = (int)(0.5F + ((float)i * f13) / f6);
            int i1 = (int)(0.5F + ((float)j * f14) / f8);
            float f15 = 2.0F * Geom.RAD2DEG((float)Math.atan2(f13 * 0.5F, f1));
            int j1 = (int)(-((f2 - -f13 / 2.0F) / f13) * (float)l);
            int k1 = (int)(-((f4 - -f14 / 2.0F) / f14) * (float)i1);
            cmm2w.getEulers(Eul);
            Eul[0] *= -57.295777918682049D;
            Eul[1] *= -57.295777918682049D;
            Eul[2] *= 57.295777918682049D;
            loc2.set(cmm2w.m03, cmm2w.m13, cmm2w.m23, (float)Eul[0], (float)Eul[1], (float)Eul[2]);
            ai[0] = j1;
            ai[1] = k1;
            ai[2] = l;
            ai[3] = i1;
            resultNearClipDepth = f1;
            return f15;
        }

        public boolean computeRenderPos(Actor actor, Loc loc, Loc loc1)
        {
            computePos(actor, loc, loc1, true);
            return true;
        }

        public void computePos(Actor actor, Loc loc, Loc loc1)
        {
            computePos(actor, loc, loc1, false);
        }

        public void computePos(Actor actor, Loc loc, Loc loc1, boolean flag)
        {
            if(!Actor.isValid(Main3D.cur3D().cockpitCur))
                return;
            if(!Main3D.cur3D().cockpitCur.bExistMirror)
                return;
            if(!Main3D.cur3D().isViewMirror())
                return;
            if(!Main3D.cur3D().cockpitCur.isFocused())
                return;
            if(!Actor.isValid(World.getPlayerAircraft()))
                return;
            Loc loc2 = loc;
            Main3D.cur3D().cockpitCur.mesh.setCurChunk(Main3D.cur3D().cockpitCur.nameOfActiveMirrorSurfaceChunk());
            Main3D.cur3D().cockpitCur.mesh.getChunkLocObj(mir2w_loc);
            if(flag)
            {
                if(bInCockpit)
                    Main3D.cur3D().cockpitCur.pos.getRender(aLoc);
                else
                    World.getPlayerAircraft().pos.getRender(aLoc);
            } else
            if(bInCockpit)
                Main3D.cur3D().cockpitCur.pos.getAbs(aLoc);
            else
                World.getPlayerAircraft().pos.getAbs(aLoc);
            mir2w_loc.add(mir2w_loc, aLoc);
            Main3D.cur3D().cockpitCur.mesh.getChunkCurVisBoundBox(mirLowP, mirHigP);
            float f = computeMirroredCamera(loc2, mir2w_loc, mirLowP, mirHigP, Main3D.cur3D().mirrorWidth(), Main3D.cur3D().mirrorHeight(), 1.0F, loc1, bInCockpit ? Main3D.cur3D().cockpitCur.cockpit_renderwin : Main3D.cur3D().cockpitCur.world_renderwin);
            if(!flag)
                return;
            if(bInCockpit)
            {
                Main3D.cur3D().cameraCockpitMirror.set(f);
                Main3D.cur3D().cameraCockpitMirror.ZNear = resultNearClipDepth;
            } else
            {
                Main3D.cur3D().camera3DMirror.set(f);
                Main3D.cur3D().camera3DMirror.ZNear = resultNearClipDepth;
            }
        }

        Matrix4d cam2w;
        Matrix4d mir2w;
        Matrix4d cam2mir;
        Point3d p;
        Vector3d X;
        Vector3d Y;
        Vector3d Z;
        Matrix4d cmm2w;
        Matrix4d cmm2mir;
        Matrix4d mir2cmm;
        double Eul[];
        Point3f mirLowP;
        Point3f mirHigP;
        float resultNearClipDepth;
        Loc mir2w_loc;
        Loc aLoc;
        boolean bInCockpit;

        public HookCamera3DMirror(boolean flag)
        {
            cam2w = new Matrix4d();
            mir2w = new Matrix4d();
            cam2mir = new Matrix4d();
            p = new Point3d();
            X = new Vector3d();
            Y = new Vector3d();
            Z = new Vector3d();
            cmm2w = new Matrix4d();
            cmm2mir = new Matrix4d();
            mir2cmm = new Matrix4d();
            Eul = new double[3];
            mirLowP = new Point3f();
            mirHigP = new Point3f();
            mir2w_loc = new Loc();
            aLoc = new Loc();
            bInCockpit = flag;
        }
    }

    public static class Camera3DMirror extends Camera3D
    {

        public boolean activate(float f, int i, int j, int k, int l, int i1, int j1, 
                int k1, int l1, int i2, int j2)
        {
            if(!Actor.isValid(Main3D.cur3D().cockpitCur))
                return super.activate(f, i, j, k, l, i1, j1, k1, l1, i2, j2);
            pos.getRender(_tmpLoc);
            int ai[] = Main3D.cur3D().cockpitCur.world_renderwin;
            if(this == Main3D.cur3D().cameraCockpitMirror)
                ai = Main3D.cur3D().cockpitCur.cockpit_renderwin;
            return super.activate(f, i, j, Main3D.cur3D().mirrorX0() + ai[0], Main3D.cur3D().mirrorY0() + ai[1], ai[2], ai[3], Main3D.cur3D().mirrorX0(), Main3D.cur3D().mirrorY0(), Main3D.cur3D().mirrorWidth(), Main3D.cur3D().mirrorHeight());
        }

        public Camera3DMirror()
        {
        }
    }


    protected void resetYPRmodifier()
    {
        ypr[0] = ypr[1] = ypr[2] = xyz[0] = xyz[1] = xyz[2] = 0.0F;
    }

    public int astatePilotIndx()
    {
        if(_astatePilotIndx == -1)
            _astatePilotIndx = Property.intValue(getClass(), "astatePilotIndx", 0);
        return _astatePilotIndx;
    }

    public boolean isEnableHotKeysOnOutsideView()
    {
        return false;
    }

    public String[] getHotKeyEnvs()
    {
        return null;
    }

    protected void initSounds()
    {
        if(sfxPreset == null)
            sfxPreset = new SoundPreset("aircraft.cockpit");
        sounds = new SoundFX[18];
    }

    protected void sfxClick(int i)
    {
        sfxStart(i);
    }

    protected void sfxStart(int i)
    {
        if(sounds != null && sounds.length > i)
        {
            SoundFX soundfx = sounds[i];
            if(soundfx == null)
            {
                soundfx = aircraft().newSound(sfxPreset, false, false);
                if(soundfx == null)
                    return;
                soundfx.setParent(aircraft().getRootFX());
                sounds[i] = soundfx;
                soundfx.setUsrFlag(i);
            }
            soundfx.play(sfxPos);
        }
    }

    protected void sfxStop(int i)
    {
        if(sounds != null && sounds.length > i && sounds[i] != null)
            sounds[i].stop();
    }

    protected void sfxSetAcoustics(Acoustics acoustics)
    {
        for(int i = 0; i < sounds.length; i++)
            if(sounds[i] != null)
                sounds[i].setAcoustics(acoustics);

    }

    protected void loadBuzzerFX()
    {
        if(sounds != null)
        {
            SoundFX soundfx = sounds[17];
            if(soundfx == null)
            {
                SoundFX soundfx1 = aircraft().newSound(sfxPreset, false, false);
                if(soundfx1 != null)
                {
                    soundfx1.setParent(aircraft().getRootFX());
                    sounds[17] = soundfx1;
                    soundfx1.setUsrFlag(17);
                    soundfx1.setPosition(sfxPos);
                }
            }
        }
    }

    protected void buzzerFX(boolean flag)
    {
        SoundFX soundfx = sounds[17];
        if(soundfx != null)
            soundfx.setPlay(flag);
    }

    public void onDoorMoved(float f)
    {
        if(acoustics != null && acoustics.globFX != null)
            acoustics.globFX.set(1.0F - f);
    }

    public void doToggleDim()
    {
        Cockpit acockpit[] = Main3D.cur3D().cockpits;
        sfxClick(9);
        for(int i = 0; i < acockpit.length; i++)
        {
            Cockpit cockpit = acockpit[i];
            if(Actor.isValid(cockpit))
                cockpit.toggleDim();
        }

    }

    public boolean isToggleDim()
    {
        return cockpitDimControl;
    }

    public void doToggleLight()
    {
        Cockpit acockpit[] = Main3D.cur3D().cockpits;
        sfxClick(1);
        for(int i = 0; i < acockpit.length; i++)
        {
            Cockpit cockpit = acockpit[i];
            if(Actor.isValid(cockpit))
                cockpit.toggleLight();
        }

    }

    public boolean isToggleLight()
    {
        return cockpitLightControl;
    }

    public void doReflectCockitState()
    {
        Cockpit acockpit[] = Main3D.cur3D().cockpits;
        for(int i = 0; i < acockpit.length; i++)
        {
            Cockpit cockpit = acockpit[i];
            if(Actor.isValid(cockpit))
                cockpit.reflectCockpitState();
        }

    }

    protected void setNightMats(boolean flag)
    {
        if(cockpitNightMats == null)
            return;
        for(int i = 0; i < cockpitNightMats.length; i++)
        {
            int j = mesh.materialFind(cockpitNightMats[i] + "_night");
            if(j < 0)
            {
                if(World.cur().isDebugFM())
                    System.out.println(" * * * * * did not find " + cockpitNightMats[i] + "_night");
                continue;
            }
            Mat mat = mesh.material(j);
            if(mat.isValidLayer(0))
            {
                mat.setLayer(0);
                mat.set((short)0, flag);
                continue;
            }
            if(World.cur().isDebugFM())
                System.out.println(" * * * * * " + cockpitNightMats[i] + "_night layer 0 invalid");
        }

    }

    public void reflectWorldToInstruments(float f)
    {
    }

    public void toggleDim()
    {
        doHidePilot();
    }

    public void toggleLight()
    {
    }

    public void reflectCockpitState()
    {
    }

    public boolean isNullShow()
    {
        return bNullShow;
    }

    public void setNullShow(boolean flag)
    {
        Cockpit acockpit[] = Main3D.cur3D().cockpits;
        for(int i = 0; i < acockpit.length; i++)
            acockpit[i]._setNullShow(flag);

        if(bFocus)
        {
            Aircraft aircraft1 = aircraft();
            if(Actor.isValid(aircraft1))
                aircraft1.drawing(!flag);
        }
    }

    protected void _setNullShow(boolean flag)
    {
        bNullShow = flag;
    }

    public boolean isEnableRendering()
    {
        return bEnableRendering;
    }

    public void setEnableRendering(boolean flag)
    {
        Cockpit acockpit[] = Main3D.cur3D().cockpits;
        for(int i = 0; i < acockpit.length; i++)
            acockpit[i]._setEnableRendering(flag);

    }

    protected void _setEnableRendering(boolean flag)
    {
        bEnableRendering = flag;
    }

    public boolean isEnableRenderingBall()
    {
        return bEnableRenderingBall;
    }

    public void setEnableRenderingBall(boolean flag)
    {
        Cockpit acockpit[] = Main3D.cur3D().cockpits;
        for(int i = 0; i < acockpit.length; i++)
            acockpit[i]._setEnableRenderingBall(flag);

    }

    protected void _setEnableRenderingBall(boolean flag)
    {
        bEnableRenderingBall = flag;
    }

    public boolean isFocused()
    {
        return bFocus;
    }

    public boolean isEnableFocusing()
    {
        if(!Actor.isValid(aircraft()))
            return false;
        return !aircraft().FM.AS.isPilotParatrooper(astatePilotIndx());
    }

    public boolean focusEnter()
    {
        if(!isEnableFocusing())
            return false;
        if(bFocus)
            return true;
        if(!doFocusEnter())
        {
            return false;
        } else
        {
            bFocus = true;
            Main3D.cur3D().enableCockpitHotKeys();
            return true;
        }
    }

    public void focusLeave()
    {
        if(!bFocus)
            return;
        doFocusLeave();
        bFocus = false;
        aircraft().stopMorseSounds();
        if(!isEnableHotKeysOnOutsideView())
            Main3D.cur3D().disableCockpitHotKeys();
    }

    protected boolean doFocusEnter()
    {
        return true;
    }

    protected void doFocusLeave()
    {
    }

    public boolean existPadlock()
    {
        return false;
    }

    public boolean isPadlock()
    {
        return bFocus ? false : false;
    }

    public Actor getPadlockEnemy()
    {
        return null;
    }

    public boolean startPadlock(Actor actor)
    {
        return bFocus ? false : false;
    }

    public void stopPadlock()
    {
    }

    public void endPadlock()
    {
    }

    public void setPadlockForward(boolean flag)
    {
    }

    public boolean isToggleAim()
    {
        return bFocus ? false : false;
    }

    public void doToggleAim(boolean flag)
    {
    }

    public boolean isToggleUp()
    {
        return bFocus ? false : false;
    }

    public void doToggleUp(boolean flag)
    {
    }

    public String nameOfActiveMirrorSurfaceChunk()
    {
        return "Mirror";
    }

    public String nameOfActiveMirrorBaseChunk()
    {
        return "BaseMirror";
    }

    public static Hook getHookCamera3DMirror(boolean flag)
    {
        return new HookCamera3DMirror(flag);
    }

    public void grabMirrorFromScreen(int i, int j, int k, int l)
    {
        mirrorMat.grabFromScreen(i, j, k, l);
    }

    public void preRender(boolean flag)
    {
        if(!flag && bNullShow)
        {
            Aircraft aircraft1 = World.getPlayerAircraft();
            if(Actor.isValid(aircraft1))
            {
                ((Actor) (aircraft1)).pos.getRender(__l);
                ((Actor) (aircraft1)).draw.soundUpdate(aircraft1, __l);
                ((Aircraft)aircraft1).updateLLights();
            }
        }
        if(!bEnableRendering)
            return;
        if(bNullShow)
            drawNullMesh.preRender();
        else
            drawMesh.preRender();
    }

    public void render(boolean flag)
    {
        if(!bEnableRendering)
            return;
        if(bNullShow)
            drawNullMesh.render();
        else
            drawMesh.render(flag);
    }

    public Aircraft aircraft()
    {
        if(fm == null)
            return null;
        else
            return (Aircraft)fm.actor;
    }

    public boolean isExistMirror()
    {
        return bExistMirror;
    }

    public void destroy()
    {
        if(isFocused())
            Main3D.cur3D().setView(fm.actor, true);
        fm = null;
        super.destroy();
    }

    public Cockpit(String s, String s1)
    {
        fm = null;
        cockpitDimControl = false;
        cockpitLightControl = false;
        cockpitNightMats = null;
        printCompassHeading = false;
        _astatePilotIndx = -1;
        sounds = null;
        sfxPos = new Point3d(1.0D, 0.0D, 0.0D);
        prevWaypointF = 0.0F;
        skip = false;
        distanceV = new Vector3d();
        ndBeaconRange = 1.0F;
        ndBeaconDirection = 0.0F;
        TACANBeaconDistance = 0.0F;
        blData = new BlindLandingData();
        acPoint = new Point3d();
        tempPoint1 = new Point3d();
        tempPoint2 = new Point3d();
        morseCharsPlayed = 0;
        clearToPlay = true;
        hidePilot = false;
        bNullShow = false;
        bEnableRendering = true;
        bEnableRenderingBall = true;
        bFocus = false;
        world_renderwin = new int[4];
        cockpit_renderwin = new int[4];
        drawMesh = new Draw();
        drawNullMesh = new NullDraw();
        bExistMirror = false;
        __l = new Loc();
        __p = new Point3d();
        __o = new Orient();
        fm = _newAircraft.FM;
        pos = new ActorPosMove(this);
        if(s != null)
        {
            mesh = new HierMesh(s);
            int i = mesh.materialFind("MIRROR");
            if(i != -1)
            {
                bExistMirror = true;
                mirrorMat = mesh.material(i);
            }
        }
        nullMesh = new HierMesh("3DO/Cockpit/Nill/hier.him");
        nullMeshIL2 = new Mesh("3DO/Cockpit/null/mono.sim");
        pilotMatsIDs = null;
        try
        {
            acoustics = new Acoustics(s1);
            acoustics.setParent(Engine.worldAcoustics());
            initSounds();
        }
        catch(Exception exception)
        {
            System.out.println("Cockpit Acoustics NOT initialized: " + exception.getMessage());
        }
        if(this instanceof CockpitPilot)
            printCompassHeading = false;
    }

    protected void createActorHashCode()
    {
        makeActorRealHashCode();
    }

    protected float cvt(float f, float f1, float f2, float f3, float f4)
    {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
    }

    protected float interp(float f, float f1, float f2)
    {
        return f1 + (f - f1) * f2;
    }

    protected float floatindex(float f, float af[])
    {
        int i = (int)f;
        if(i >= af.length - 1)
            return af[af.length - 1];
        if(i < 0)
            return af[0];
        if(i == 0)
        {
            if(f > 0.0F)
                return af[0] + f * (af[1] - af[0]);
            else
                return af[0];
        } else
        {
            return af[i] + (f % (float)i) * (af[i + 1] - af[i]);
        }
    }

    public boolean useRealisticNavigationInstruments()
    {
        return World.cur().diffCur.RealisticNavigationInstruments;
    }

    public static void lightningStrike(Point3d point3d)
    {
        lightningPoint = point3d;
        lightningStriked = true;
    }

    private void updateBeacons()
    {
        if(lightningStriked)
        {
            lightningStriked = false;
            V.sub(fm.Loc, lightningPoint);
            float f = (float)V.length();
            atmosphereInterference = cvt(f, 1000F, 9000F, 1.0F, 0.0F);
        } else
        if(atmosphereInterference > 0.01F)
            atmosphereInterference = (float)((double)atmosphereInterference * 0.97999999999999998D);
        if(!bFocus)
            return;
        Aircraft aircraft1 = aircraft();
        if(aircraft1.FM.AS.listenLorenzBlindLanding)
            listenLorenzBlindLanding(aircraft1);
        else
        if(aircraft1.FM.AS.listenNDBeacon)
            listenNDBeacon(aircraft1, false);
        else
        if(aircraft1.FM.AS.listenRadioStation)
            listenNDBeacon(aircraft1, true);
        else
    	if(aircraft1.FM.AS.listenTACAN)
            listenTACANBeacon(aircraft1);
        else
        if(aircraft1.FM.AS.listenYGBeacon)
        {
            ndBeaconRange = 1.0F;
            ndBeaconDirection = 0.0F;
            Actor actor = (Actor)Main.cur().mission.getBeacons(aircraft1.getArmy()).get(aircraft1.FM.AS.getBeacon() - 1);
            if(!actor.isAlive())
                return;
            playYEYGMorseCode(aircraft1, actor, "DWRKANUGMLFS".toCharArray());
        } else
        if(aircraft1.FM.AS.hayrakeCarrier != null && aircraft1.FM.AS.hayrakeCarrier.isAlive())
        {
            ndBeaconRange = 1.0F;
            ndBeaconDirection = 0.0F;
            playYEYGMorseCode(aircraft1, aircraft1.FM.AS.hayrakeCarrier, aircraft1.FM.AS.hayrakeCode.toCharArray());
        } else
        {
            ndBeaconRange = 1.0F;
            ndBeaconDirection = 0.0F;
        }
    }

    private void playYEYGMorseCode(Aircraft aircraft1, Actor actor, char ac[])
    {
        float f = cvt((float)Time.current() % 30000F, 0.0F, 30000F, 0.0F, 360F);
        boolean flag = false;
        if((float)Time.current() % 300000F <= 30000F)
            flag = true;
        actor.pos.getAbs(tempPoint1);
        aircraft1.pos.getAbs(tempPoint2);
        tempPoint2.sub(tempPoint1);
        float f1 = 57.32484F * (float)Math.atan2(tempPoint2.x, tempPoint2.y);
        f1 -= 180F;
        for(f1 = (f1 + 180F) % 360F; f1 < 0.0F; f1 += 360F);
        for(; f1 >= 360F; f1 -= 360F);
        float f2 = Math.abs(f - f1);
        Point3d point3d = new Point3d();
        point3d.x = actor.pos.getAbsPoint().x;
        point3d.y = actor.pos.getAbsPoint().y;
        point3d.z = actor.pos.getAbsPoint().z + 40D;
        float f3 = 15F;
        if(f2 > f3)
        {
            BeaconGeneric.getSignalAttenuation(point3d, aircraft1, false, false, true, true);
            aircraft1.playYEYGCarrier(false, 0.0F);
            clearToPlay = true;
            return;
        }
        float f4 = BeaconGeneric.getSignalAttenuation(point3d, aircraft1, false, false, true, false);
        if(f4 == 1.0F)
            return;
        float f5 = (1.0F - f4) * ((f3 - f2) / f3);
        aircraft1.playYEYGCarrier(true, f5 * 0.5F);
        int i = (int)f;
        if(i % 15 == 0)
            clearToPlay = true;
        if(i % 13 != 0)
            return;
        if(aircraft1.isMorseSequencePlaying() && !clearToPlay)
            return;
        if(flag)
        {
            String s = Beacon.getBeaconID(aircraft1.FM.AS.getBeacon() - 1);
            String s1 = "";
            if(morseCharsPlayed % 2 == 0)
                s1 = "" + s.charAt(0);
            else
                s1 = "" + s.charAt(1);
            aircraft1.morseSequenceStart(s1, f5);
            clearToPlay = false;
            morseCharsPlayed++;
        } else
        {
            morseCharsPlayed = 0;
            float f6 = 0.0F;
            if(ac.length == 12)
                f6 = 0.03333334F * f;
            else
            if(ac.length == 24)
                f6 = 0.06666667F * f;
            if(f6 >= (float)ac.length)
                f6 = 0.0F;
            char c = ac[(int)f6];
            String s2 = "" + c;
            aircraft1.morseSequenceStart(s2, f5);
            clearToPlay = false;
        }
    }

    private void listenLorenzBlindLanding(Aircraft aircraft1)
    {
        blData.reset();
        com.maddox.il2.objects.vehicles.radios.Beacon.LorenzBLBeacon lorenzblbeacon = (com.maddox.il2.objects.vehicles.radios.Beacon.LorenzBLBeacon)getBeacon();
        if(lorenzblbeacon == null || !lorenzblbeacon.isAlive())
        {
        	ndBeaconRange = 1.0F;
            ndBeaconDirection = 0.0F;
        	aircraft1.stopMorseSounds();
            return;
        }
        lorenzblbeacon.rideBeam(aircraft1, blData);
        tempPoint1.x = lorenzblbeacon.pos.getAbsPoint().x;
        tempPoint1.y = lorenzblbeacon.pos.getAbsPoint().y;
        tempPoint1.z = lorenzblbeacon.pos.getAbsPoint().z + 20D;
        aircraft1.pos.getAbs(acPoint);
        acPoint.sub(tempPoint1);
        TACANBeaconDistance = (float)Math.sqrt(acPoint.x * acPoint.x + acPoint.y * acPoint.y + acPoint.z * acPoint.z);
        ndBeaconRange = BeaconGeneric.getSignalAttenuation(tempPoint1, aircraft1, true, false, false, false);
        if(lorenzblbeacon instanceof TypeHasILSBlindLanding)
            return;
        if(Math.random() < 0.02D)
            terrainAndNightError = BeaconGeneric.getTerrainAndNightError(aircraft1);
        float f = blData.blindLandingAzimuthBP;
        float f1 = (float)Math.random() * (0.5F - blData.signalStrength);
        float f2 = cvt(blData.signalStrength * 2.0F, 0.0F, 0.75F, 0.0F, 1.2F) - f1;
        float f3 = 12F;
        float f4 = 0.3F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        if(f < f4 && f > -f4)
        {
            aircraft1.playLorenzDash(false, 0.0F);
            aircraft1.playLorenzDot(false, 0.0F);
            aircraft1.playLorenzSolid(true, f2);
            return;
        }
        if(f > f3)
        {
            f5 = 1.0F;
            f6 = 0.0F;
        } else
        if(f < -f3)
        {
            f6 = 1.0F;
            f5 = 0.0F;
        } else
        {
            f5 = cvt(f, -f3 / 2.0F, f4 * 10F, 0.0F, 1.0F);
            f6 = cvt(f, -f4 * 10F, f3 / 2.0F, 1.0F, 0.0F);
        }
        aircraft1.playLorenzSolid(true, 0.0F);
        aircraft1.playLorenzDash(true, f6 * f2);
        aircraft1.playLorenzDot(true, f5 * f2);
    }

    public boolean isOnBlindLandingMarker()
    {
        Aircraft aircraft1 = aircraft();
        if(blData.isOnInnerMarker)
        {
            aircraft1.playLorenzInnerMarker(true, 1.0F);
            return true;
        }
        if(blData.isOnOuterMarker)
        {
            aircraft1.playLorenzOuterMarker(true, 1.0F);
            return true;
        } else
        {
            aircraft1.playLorenzInnerMarker(false, 0.0F);
            aircraft1.playLorenzOuterMarker(false, 0.0F);
            return false;
        }
    }

    public float getBeaconRange()
    {
        Aircraft aircraft1 = aircraft();
        if(aircraft1.FM.AS.listenLorenzBlindLanding)
            return 7.0422F * ((1.0F - blData.signalStrength) + World.rnd().nextFloat(-atmosphereInterference * 2.0F, atmosphereInterference * 2.0F));
        if(useRealisticNavigationInstruments())
            return ndBeaconRange + (float)Math.random() * 0.2F * ndBeaconRange + World.rnd().nextFloat(-atmosphereInterference * 2.0F, atmosphereInterference * 2.0F);
        else
            return 1.0F;
    }

    public float getGlidePath()
    {
        Aircraft aircraft1 = aircraft();
        if(aircraft1.FM.AS.listenLorenzBlindLanding)
        {
            int i = fm.AS.getBeacon();
            ArrayList arraylist = Main.cur().mission.getBeacons(fm.actor.getArmy());
            Actor actor = (Actor)arraylist.get(i - 1);
            double d = actor.pos.getAbsPoint().z + 10D;
            if(actor instanceof TypeHasILSBlindLanding)
                d -= 150D;
            float f = (1.0F - blData.signalStrength) * 100F;
            double d1 = Math.abs(aircraft1.pos.getAbsPoint().x - actor.pos.getAbsPoint().x);
            double d2 = Math.abs(aircraft1.pos.getAbsPoint().y - actor.pos.getAbsPoint().y);
            float f1 = 1700F + World.rnd().nextFloat(-f, f);
            if(actor instanceof TypeHasILSBlindLanding)
                f1 -= 3500F;
            double d3 = Math.sqrt(d1 * d1 + d2 * d2) - (double)f1;
            double d4 = aircraft1.pos.getAbsPoint().z - d;
            if(d3 < d4)
                return -1F;
            double d5 = Math.asin(d4 / d3);
            double d6 = glideScopeInRads - d5;
            float f2 = (float)Math.toDegrees(d6) * blData.signalStrength + World.rnd().nextFloat(-atmosphereInterference * 2.0F, atmosphereInterference * 2.0F);
            if(f2 > 1.0F)
                f2 = 1.0F;
            else
            if(f2 < -1F)
                f2 = -1F;
            return f2;
        } else
        {
            return 0.0F;
        }
    }

    public float getBeaconDirection()
    {
        Aircraft aircraft1 = aircraft();
        if(bFocus && aircraft1.FM.AS.listenLorenzBlindLanding)
            return blData.blindLandingAzimuthPB + World.rnd().nextFloat(-atmosphereInterference * 90F, atmosphereInterference * 90F);
        if(useRealisticNavigationInstruments() && Main.cur().mission.hasBeacons(fm.actor.getArmy()))
            return ndBeaconDirection + World.rnd().nextFloat(-atmosphereInterference * 90F, atmosphereInterference * 90F);
        else
            return 0.0F;
    }

    public float getBeaconDistance()
    {
        Aircraft aircraft1 = aircraft();
        if(bFocus && aircraft1.FM.AS.listenLorenzBlindLanding)
            return TACANBeaconDistance;
        if(useRealisticNavigationInstruments() && Main.cur().mission.hasBeacons(fm.actor.getArmy()) && aircraft1.FM.AS.listenTACAN)
            return TACANBeaconDistance;
        else
            return 0.0F;
    }
    
    private Actor getBeacon()
    {
        int i = fm.AS.getBeacon();
        if(i == 0)
            return null;
        ArrayList arraylist = Main.cur().mission.getBeacons(fm.actor.getArmy());
        Actor actor = (Actor)arraylist.get(i - 1);
        if((actor instanceof TypeHasYGBeacon) || (actor instanceof TypeHasHayRake))
            return null;
        ArrayList arraylist1 = Main.cur().mission.getMeacons(fm.actor.getArmy());
        if(arraylist1.size() >= i && !(actor instanceof com.maddox.il2.objects.vehicles.radios.Beacon.LorenzBLBeacon) && !(actor instanceof TypeHasTACAN))
        {
            Actor actor1 = (Actor)arraylist1.get(i - 1);
            if(actor1.isAlive())
            {
                distanceV.sub(actor1.pos.getAbsPoint(), fm.Loc);
                double d = distanceV.length();
                distanceV.sub(actor.pos.getAbsPoint(), fm.Loc);
                double d1 = distanceV.length();
                if(d < d1 || !actor.isAlive())
                    actor = actor1;
            }
        }
        return actor;
    }

    private void listenNDBeacon(Aircraft aircraft1, boolean flag)
    {
        Actor actor = getBeacon();
        if(actor == null || !actor.isAlive())
        {
            ndBeaconRange = 1.0F;
            ndBeaconDirection = 0.0F;
            if(flag)
                CmdMusic.setCurrentVolume(0.001F);
            else
                aircraft1.playBeaconCarrier(false, 0.0F);
            return;
        }
        tempPoint1.x = actor.pos.getAbsPoint().x;
        tempPoint1.y = actor.pos.getAbsPoint().y;
        tempPoint1.z = actor.pos.getAbsPoint().z + 20D;
        aircraft1.pos.getAbs(acPoint);
        acPoint.sub(tempPoint1);
        float f = aircraft1.pos.getAbsOrient().getYaw() + 180F;
        float f1 = 57.32484F * (float)Math.atan2(acPoint.y, acPoint.x) - f;
        for(f1 = (f1 + 180F) % 360F; f1 < 0.0F; f1 += 360F);
        for(; f1 >= 360F; f1 -= 360F);
        if(f1 > 270F)
            f1 -= 360F;
        if(f1 > 90F)
            f1 = -(f1 - 180F);
        ndBeaconRange = BeaconGeneric.getSignalAttenuation(tempPoint1, aircraft1, !flag, flag, false, false);
        if(Math.random() < 0.02D)
            terrainAndNightError = BeaconGeneric.getTerrainAndNightError(aircraft1);
        f1 += terrainAndNightError;
        float f2 = floatindex(cvt(1.0F - ndBeaconRange, 0.0F, 1.0F, 0.0F, 9F), volumeLogScale);
        float f3 = AudioDevice.vMusic.get();
        float f4 = (f3 + 1.0F) / 15F;
        if(!flag)
        {
            float f5 = (float)Math.random() * ndBeaconRange;
            float f6 = (float)Time.current() % 60000F;
            if(f6 <= 500F && !aircraft1.isMorseSequencePlaying())
            {
                String s = Beacon.getBeaconID(aircraft1.FM.AS.getBeacon() - 1);
                f4 = f2 * f4 * 0.75F;
                aircraft1.morseSequenceStart(s, f4);
            } else
            {
                f4 = f2 * f4 * 0.75F - f5;
                aircraft1.playBeaconCarrier(true, f4);
            }
        } else
        {
            CmdMusic.setCurrentVolume(f2);
            aircraft1.playRadioStatic(true, (-0.5F + (1.0F - f2 * f4)) * 2.0F);
        }
        ndBeaconDirection = f2 * f1 + ((float)Math.random() - 0.5F) * ndBeaconRange;
    }

    private void listenTACANBeacon(Aircraft aircraft1)
    {
        Actor actor = getBeacon();
        if(actor == null || !actor.isAlive())
        {
            ndBeaconRange = 1.0F;
            ndBeaconDirection = 0.0F;
        }
        tempPoint1.x = actor.pos.getAbsPoint().x;
        tempPoint1.y = actor.pos.getAbsPoint().y;
        tempPoint1.z = actor.pos.getAbsPoint().z + 20D;
        aircraft1.pos.getAbs(acPoint);
        acPoint.sub(tempPoint1);
        float f = aircraft1.pos.getAbsOrient().getYaw() + 180F;
        float f1 = 57.32484F * (float)Math.atan2(acPoint.y, acPoint.x) - f;
        for(f1 = (f1 + 180F) % 360F; f1 < 0.0F; f1 += 360F);
        for(; f1 >= 360F; f1 -= 360F);
        if(f1 > 270F)
            f1 -= 360F;
        if(f1 > 90F)
            f1 = -(f1 - 180F);
        ndBeaconRange = BeaconGeneric.getSignalAttenuation(tempPoint1, aircraft1, true, false, false, false);
        if(Math.random() < 0.02D)
            terrainAndNightError = BeaconGeneric.getTerrainAndNightError(aircraft1);
        f1 += terrainAndNightError;
        float f2 = floatindex(cvt(1.0F - ndBeaconRange, 0.0F, 1.0F, 0.0F, 9F), volumeLogScale);
        float f3 = AudioDevice.vMusic.get();
        float f4 = (f3 + 1.0F) / 15F;
        float f5 = (float)Math.random() * ndBeaconRange;
        float f6 = (float)Time.current() % 60000F;
        ndBeaconDirection = f2 * f1 + ((float)Math.random() - 0.5F) * ndBeaconRange;
        TACANBeaconDistance = (float)Math.sqrt(acPoint.x * acPoint.x + acPoint.y * acPoint.y + acPoint.z * acPoint.z);
    }
    
    private float getRadioCompassWaypoint(boolean flag, boolean flag1, boolean flag2)
    {
        Actor actor = getBeacon();
        if(actor == null || !actor.isAlive())
            if(flag2)
            {
                prevWaypointF = aircraft().FM.Or.azimut();
                return prevWaypointF;
            } else
            {
                prevWaypointF = 0.0F;
                return prevWaypointF;
            }
        Aircraft aircraft1 = aircraft();
        tempPoint1.x = actor.pos.getAbsPoint().x;
        tempPoint1.y = actor.pos.getAbsPoint().y;
        tempPoint1.z = actor.pos.getAbsPoint().z + 20D;
        V.sub(tempPoint1, fm.Loc);
        float f;
        if(flag)
        {
            if(flag1)
                for(f = (float)(57.295779513082323D * Math.atan2(-V.y, V.x)); f <= -180F; f += 180F);
            else
                for(f = (float)(57.295779513082323D * Math.atan2(V.y, V.x)); f <= -180F; f += 180F);
        } else
        {
            for(f = (float)(57.295779513082323D * Math.atan2(V.x, V.y)); f <= -180F; f += 180F);
        }
        for(; f > 180F; f -= 180F);
        f += terrainAndNightError;
        if((double)ndBeaconRange > 0.98999999999999999D)
            f = aircraft().FM.Or.azimut();
        else
            f += World.rnd().nextFloat(-ndBeaconRange - atmosphereInterference * 5F, ndBeaconRange + atmosphereInterference * 5F) * 30F;
        return f;
    }

    private float getWaypoint(boolean flag, boolean flag1, float f, boolean flag2)
    {
        if(useRealisticNavigationInstruments())
            if(flag2)
            {
                if(Main.cur().mission.hasBeacons(fm.actor.getArmy()))
                {
                    skip = !skip;
                    if(skip)
                    {
                        return prevWaypointF;
                    } else
                    {
                        float f1 = getRadioCompassWaypoint(flag, flag1, flag2);
                        prevWaypointF = f1;
                        return f1;
                    }
                } else
                {
                    return aircraft().FM.Or.azimut();
                }
            } else
            {
                return aircraft().headingBug;
            }
        WayPoint waypoint = fm.AP.way.curr();
        if(waypoint == null)
            return 0.0F;
        waypoint.getP(P1);
        V.sub(P1, fm.Loc);
        float f2;
        if(flag)
        {
            if(flag1)
                for(f2 = (float)(57.295779513082323D * Math.atan2(-V.y, V.x)); f2 <= -180F; f2 += 180F);
            else
                for(f2 = (float)(57.295779513082323D * Math.atan2(V.y, V.x)); f2 <= -180F; f2 += 180F);
        } else
        {
            for(f2 = (float)(57.295779513082323D * Math.atan2(V.x, V.y)); f2 <= -180F; f2 += 180F);
        }
        for(; f2 > 180F; f2 -= 180F);
        return f2 + World.rnd().nextFloat(-f, f);
    }

    protected float waypointAzimuth()
    {
        return getWaypoint(false, false, 0.0F, false);
    }

    protected float waypointAzimuth(float f)
    {
        return getWaypoint(false, false, f, false);
    }

    protected float waypointAzimuthInvert(float f)
    {
        return getWaypoint(true, false, f, false);
    }

    protected float waypointAzimuthInvertMinus(float f)
    {
        return getWaypoint(true, true, f, false);
    }

    protected float radioCompassAzimuthInvertMinus()
    {
        return getWaypoint(true, true, 0.0F, true);
    }

    public float getReticleBrightness()
    {
        return 1.0F;
    }

    public void setReticleBrightness(float f)
    {
    }

    public float[] getBombSightFovs()
    {
        return defaultBSightFoVs;
    }

    public float[] get6DoFLimits()
    {
        return limits6DoF;
    }

    private void findPilotMats()
    {
        if(pilotMatsIDs != null)
            return;
        ArrayList arraylist = new ArrayList();
        HierMesh hiermesh = aircraft().hierMesh();
        int i = hiermesh.materials();
        for(int j = 0; j < i; j++)
        {
            Mat mat = hiermesh.material(j);
            String s = mat.Name();
            int k = s.lastIndexOf("/");
            s = s.substring(k + 1, s.length() - 4);
            Integer integer = new Integer(j);
            if(s.startsWith("Pilot") && !arraylist.contains(integer))
                arraylist.add(integer);
        }

        pilotMatsIDs = (Integer[])(Integer[])arraylist.toArray(new Integer[0]);
        arraylist.clear();
    }

    protected void doShowPilot()
    {
        setPilotMatVisDist(0.0F);
    }

    public void doHidePilot()
    {
        setPilotMatVisDist(15F);
    }

    private void setPilotMatVisDist(float f)
    {
        if(bExistMirror || hidePilot)
        {
            findPilotMats();
            HierMesh hiermesh = aircraft().hierMesh();
            for(int i = 0; i < pilotMatsIDs.length; i++)
            {
                int j = pilotMatsIDs[i].intValue();
                try
                {
                    if(j != -1)
                    {
                        Mat mat = hiermesh.material(j);
                        mat.setLayer(0);
                        mat.set((byte)0, f);
                    }
                }
                catch(Exception exception) { }
            }

        }
    }

    public FlightModel fm;
    public boolean cockpitDimControl;
    public boolean cockpitLightControl;
    protected String cockpitNightMats[];
    protected static float ypr[] = {
        0.0F, 0.0F, 0.0F
    };
    protected static float xyz[] = {
        0.0F, 0.0F, 0.0F
    };
    protected boolean printCompassHeading;
    protected int _astatePilotIndx;
    public static final int SNDCLK_NULL = 0;
    public static final int SNDCLK_BUTTONDEPRESSED1 = 1;
    public static final int SNDCLK_BUTTONDEPRESSED2 = 2;
    public static final int SNDCLK_BUTTONDEPRESSED3 = 3;
    public static final int SNDCLK_TUMBLERFLIPPED1 = 4;
    public static final int SNDCLK_TUMBLERFLIPPED2 = 5;
    public static final int SNDCLK_TUMBLERFLIPPED3 = 6;
    public static final int SNDINF_RUSTYWHEELTURNING1 = 7;
    public static final int SNDINF_OILEDMETALWHEELWITHCHAIN1 = 8;
    public static final int SNDCLK_SMALLLEVERSLIDES1 = 9;
    public static final int SNDCLK_SMALLLEVERSLIDES2 = 10;
    public static final int SNDINF_LEVERSLIDES1 = 11;
    public static final int SNDINF_LEVERSLIDES2 = 12;
    public static final int SNDCLK_RUSTYLEVERSLIDES1 = 13;
    public static final int SNDCLK_SMALLVALVE1 = 14;
    public static final int SNDINF_SMALLVALVEWITHGASLEAK1 = 15;
    public static final int SNDINF_WORMGEARTURNS1 = 16;
    public static final int SNDINF_BUZZER_109 = 17;
    public static final int SND_COUNT = 18;
    protected static SoundPreset sfxPreset = null;
    protected SoundFX sounds[];
    protected Point3d sfxPos;
    private float prevWaypointF;
    private boolean skip;
    private Vector3d distanceV;
    private float ndBeaconRange;
    private float ndBeaconDirection;
    private float TACANBeaconDistance;
    private BlindLandingData blData;
    private Point3d acPoint;
    private Point3d tempPoint1;
    private Point3d tempPoint2;
    private static final String ygCode = "DWRKANUGMLFS";
    private static final int beaconAntennaHeight = 20;
    private static final int YEYGAntennaHeight = 40;
    private static float atmosphereInterference = 0.0F;
    private static Point3d lightningPoint = new Point3d();
    private static boolean lightningStriked = false;
    public static final float radioCompassUpdateF = 0.02F;
    private static float terrainAndNightError = 0.0F;
    private static final double glideScopeInRads = Math.toRadians(3D);
    private int morseCharsPlayed;
    private boolean clearToPlay;
    public boolean hidePilot;
    private static final float volumeLogScale[] = {
        0.0F, 0.301F, 0.477F, 0.602F, 0.698F, 0.778F, 0.845F, 0.903F, 0.954F, 1.0F
    };
    private boolean bNullShow;
    private boolean bEnableRendering;
    private boolean bEnableRenderingBall;
    private boolean bFocus;
    private int world_renderwin[];
    private int cockpit_renderwin[];
    public HierMesh mesh;
    private HierMesh nullMesh;
    private Mesh nullMeshIL2;
    private Draw drawMesh;
    private NullDraw drawNullMesh;
    protected boolean bExistMirror;
    private Mat mirrorMat;
    private Loc __l;
    private Point3d __p;
    private Orient __o;
    public static Aircraft _newAircraft = null;
    private static Point3d nullP = new Point3d();
    protected static Point3d P1 = new Point3d();
    protected static Vector3d V = new Vector3d();
    private static final float defaultBSightFoVs[] = {
        15F, 26F
    };
    public float limits6DoF[] = {
        0.7F, 0.055F, -0.07F, 0.11F, 0.12F, -0.11F, 0.03F, -0.03F
    };
    private static Integer pilotMatsIDs[] = null;











}
