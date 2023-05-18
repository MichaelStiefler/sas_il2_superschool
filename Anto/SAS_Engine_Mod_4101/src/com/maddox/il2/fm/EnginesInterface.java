/*Modified EnginesInterface class for the SAS Engine Mod*/

package com.maddox.il2.fm;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class EnginesInterface extends FMMath
{

    public EnginesInterface()
    {
        num = 0;
        producedF = new Vector3d();
        producedM = new Vector3d();
        reference = null;
        bCatapultArmed = false;
        dCatapultForce = 0.0D;
        iCatapultNumber = 0;
    }

    public void load(FlightModel flightmodel, SectFile sectfile)
    {
        reference = flightmodel;
        String string = "Engine";
        for(num = 0; sectfile.get(string, "Engine" + num + "Family") != null; num++);
            engines = new Motor[num];
        for(tmpI = 0; tmpI < num; tmpI++)
            engines[tmpI] = new Motor();

        bCurControl = new boolean[num];
        Aircraft.debugprintln(((Interpolate) (reference)).actor, "Loading " + num + " engine(s) from '" + sectfile.toString() + "....");
        for(tmpI = 0; tmpI < num; tmpI++)
        {
            String string_0_ = sectfile.get(string, "Engine" + tmpI + "Family");
            String string_1_ = sectfile.get(string, "Engine" + tmpI + "SubModel");
            Aircraft.debugprintln(((Interpolate) (reference)).actor, "Loading engine model from '" + string_0_ + ".emd', submodel '" + string_1_ + "'....");
            engines[tmpI].load(flightmodel, "FlightModels/" + string_0_ + ".emd", string_1_, tmpI);
        }

        if(sectfile.get(string, "Position0x", -99999F) != -99999F)
        {
            Point3d point3d = new Point3d();
            Vector3f vector3f = new Vector3f();
            for(tmpI = 0; tmpI < num; tmpI++)
            {
                point3d.x = sectfile.get(string, "Position" + tmpI + "x", 0.0F);
                point3d.y = sectfile.get(string, "Position" + tmpI + "y", 0.0F);
                point3d.z = sectfile.get(string, "Position" + tmpI + "z", 0.0F);
                engines[tmpI].setPos(point3d);
                vector3f.x = sectfile.get(string, "Vector" + tmpI + "x", 0.0F);
                vector3f.y = sectfile.get(string, "Vector" + tmpI + "y", 0.0F);
                vector3f.z = sectfile.get(string, "Vector" + tmpI + "z", 0.0F);
                engines[tmpI].setVector(vector3f);
                point3d.x = sectfile.get(string, "PropPosition" + tmpI + "x", 0.0F);
                point3d.y = sectfile.get(string, "PropPosition" + tmpI + "y", 0.0F);
                point3d.z = sectfile.get(string, "PropPosition" + tmpI + "z", 0.0F);
                engines[tmpI].setPropPos(point3d);
            }

        }
        setCurControlAll(true);
    }

    public void setNotMirror(boolean bool)
    {
        for(int i = 0; i < getNum(); i++)
            engines[i].setMaster(bool);

    }

    public void set(Actor actor)
    {
        Point3d point3d = new Point3d(0.0D, 0.0D, 0.0D);
        Loc loc = new Loc();
        if(num != 0 && engines[0].getPropPos().distanceSquared(new Point3f(0.0F, 0.0F, 0.0F)) <= 0.0F)
        {
            Vector3f vector3f = new Vector3f(1.0F, 0.0F, 0.0F);
            float fs[][] = new float[4][3];
            float fs_2_[][] = new float[num][3];
            for(tmpI = 0; tmpI < 4; tmpI++)
            {
                Hook hook = actor.findHook("_Clip0" + tmpI);
                loc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                hook.computePos(actor, actor.pos.getAbs(), loc);
                loc.get(point3d);
                actor.pos.getAbs().transformInv(point3d);
                fs[tmpI][0] = (float)((Tuple3d) (point3d)).x;
                fs[tmpI][1] = (float)((Tuple3d) (point3d)).y;
                fs[tmpI][2] = (float)((Tuple3d) (point3d)).z;
            }

            for(tmpI = 0; tmpI < num; tmpI++)
            {
                Hook hook = actor.findHook("_Engine" + (tmpI + 1) + "Smoke");
                loc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                hook.computePos(actor, actor.pos.getAbs(), loc);
                loc.get(point3d);
                actor.pos.getAbs().transformInv(point3d);
                fs_2_[tmpI][0] = (float)((Tuple3d) (point3d)).x;
                fs_2_[tmpI][1] = (float)((Tuple3d) (point3d)).y;
                fs_2_[tmpI][2] = (float)((Tuple3d) (point3d)).z - 0.7F;
            }

            switch(((FlightModelMain) (reference)).Scheme)
            {
            case 0: // '\0'
                point3d.set(0.0D, 0.0D, 0.0D);
                engines[0].setPos(point3d);
                engines[0].setPropPos(point3d);
                engines[0].setVector(vector3f);
                break;

            case 1: // '\001'
                point3d.x = 0.25F * (fs[0][0] + fs[1][0] + fs[2][0] + fs[3][0]);
                point3d.y = 0.0D;
                point3d.z = 0.25F * (fs[0][2] + fs[1][2] + fs[2][2] + fs[3][2]);
                engines[0].setPropPos(point3d);
                point3d.x = fs_2_[0][0];
                point3d.y = 0.0D;
                point3d.z = fs_2_[0][2];
                engines[0].setPos(point3d);
                engines[0].setVector(vector3f);
                break;

            case 2: // '\002'
            case 3: // '\003'
                point3d.x = 0.25F * (fs[0][0] + fs[1][0] + fs[2][0] + fs[3][0]);
                point3d.y = 0.5F * (fs[0][1] + fs[1][1]);
                point3d.z = 0.25F * (fs[0][2] + fs[1][2] + fs[2][2] + fs[3][2]);
                engines[0].setPropPos(point3d);
                point3d.y = 0.5F * (fs[2][1] + fs[3][1]);
                engines[1].setPropPos(point3d);
                point3d.x = 0.5F * (fs_2_[0][0] + fs_2_[1][0]);
                point3d.y = fs_2_[0][1];
                point3d.z = 0.5F * (fs_2_[0][2] + fs_2_[1][2]);
                engines[0].setPos(point3d);
                point3d.y = fs_2_[1][1];
                engines[1].setPos(point3d);
                engines[0].setVector(vector3f);
                engines[1].setVector(vector3f);
                break;

            case 4: // '\004'
                point3d.x = 0.25F * (fs[0][0] + fs[1][0] + fs[2][0] + fs[3][0]);
                point3d.y = fs[0][1];
                point3d.z = 0.25F * (fs[0][2] + fs[1][2] + fs[2][2] + fs[3][2]);
                engines[0].setPropPos(point3d);
                point3d.y = fs[1][1];
                engines[1].setPropPos(point3d);
                point3d.y = fs[2][1];
                engines[2].setPropPos(point3d);
                point3d.y = fs[3][1];
                engines[3].setPropPos(point3d);
                point3d.x = 0.25F * (fs_2_[0][0] + fs_2_[1][0] + fs_2_[2][0] + fs_2_[3][0]);
                point3d.y = fs_2_[0][1];
                point3d.z = 0.25F * (fs_2_[0][2] + fs_2_[1][2] + fs_2_[2][2] + fs_2_[3][2]);
                engines[0].setPos(point3d);
                point3d.y = fs_2_[1][1];
                engines[1].setPos(point3d);
                point3d.y = fs_2_[2][1];
                engines[2].setPos(point3d);
                point3d.y = fs_2_[3][1];
                engines[3].setPos(point3d);
                engines[0].setVector(vector3f);
                engines[1].setVector(vector3f);
                engines[2].setVector(vector3f);
                engines[3].setVector(vector3f);
                break;

            case 5: // '\005'
                point3d.x = 0.25F * (fs[0][0] + fs[1][0] + fs[2][0] + fs[3][0]);
                point3d.y = fs[0][1];
                point3d.z = 0.25F * (fs[0][2] + fs[1][2] + fs[2][2] + fs[3][2]);
                engines[0].setPropPos(point3d);
                point3d.y = fs[1][1];
                engines[1].setPropPos(point3d);
                point3d.y = 0.0D;
                engines[2].setPropPos(point3d);
                point3d.y = fs[2][1];
                engines[3].setPropPos(point3d);
                point3d.y = fs[3][1];
                engines[4].setPropPos(point3d);
                point3d.x = 0.2F * (fs_2_[0][0] + fs_2_[1][0] + fs_2_[2][0] + fs_2_[3][0] + fs_2_[4][0]);
                point3d.y = fs_2_[0][1];
                point3d.z = 0.2F * (fs_2_[0][2] + fs_2_[1][2] + fs_2_[2][2] + fs_2_[3][2] + fs_2_[4][2]);
                engines[0].setPos(point3d);
                point3d.y = fs_2_[1][1];
                engines[1].setPos(point3d);
                point3d.y = fs_2_[2][1];
                engines[2].setPos(point3d);
                point3d.y = fs_2_[3][1];
                engines[3].setPos(point3d);
                point3d.y = fs_2_[4][1];
                engines[4].setPos(point3d);
                engines[0].setVector(vector3f);
                engines[1].setVector(vector3f);
                engines[2].setVector(vector3f);
                engines[3].setVector(vector3f);
                engines[4].setVector(vector3f);
                break;

            case 6: // '\006'
                point3d.x = 0.3333333F * (fs[0][0] + fs[1][0] + fs[2][0]);
                point3d.y = fs[0][1];
                point3d.z = 0.3333333F * (fs[0][2] + fs[1][2] + fs[2][2]);
                engines[0].setPropPos(point3d);
                point3d.y = fs[1][1];
                engines[1].setPropPos(point3d);
                point3d.y = fs[2][1];
                engines[2].setPropPos(point3d);
                point3d.x = 0.3333333F * (fs_2_[0][0] + fs_2_[1][0] + fs_2_[2][0]);
                point3d.y = fs_2_[0][1];
                point3d.z = 0.3333333F * (fs_2_[0][2] + fs_2_[1][2] + fs_2_[2][2]);
                engines[0].setPos(point3d);
                point3d.y = fs_2_[1][1];
                engines[1].setPos(point3d);
                point3d.y = fs_2_[2][1];
                engines[2].setPos(point3d);
                engines[0].setVector(vector3f);
                engines[1].setVector(vector3f);
                engines[2].setVector(vector3f);
                break;

            case 7: // '\007'
                point3d.x = 0.25F * (fs[0][0] + fs[1][0] + fs[2][0] + fs[3][0]);
                point3d.y = fs[0][1];
                point3d.z = 0.25F * (fs[0][2] + fs[1][2] + fs[2][2] + fs[3][2]);
                engines[0].setPropPos(point3d);
                point3d.y = 0.0D;
                engines[1].setPropPos(point3d);
                point3d.y = fs[1][1];
                engines[2].setPropPos(point3d);
                point3d.y = fs[2][1];
                engines[3].setPropPos(point3d);
                point3d.y = 0.0D;
                engines[4].setPropPos(point3d);
                point3d.y = fs[3][1];
                engines[5].setPropPos(point3d);
                point3d.x = 0.1666667F * (fs_2_[0][0] + fs_2_[1][0] + fs_2_[2][0] + fs_2_[3][0] + fs_2_[4][0] + fs_2_[5][0]);
                point3d.y = fs_2_[0][1];
                point3d.z = 0.1666667F * (fs_2_[0][2] + fs_2_[1][2] + fs_2_[2][2] + fs_2_[3][2] + fs_2_[4][2] + fs_2_[5][2]);
                engines[0].setPos(point3d);
                point3d.y = fs_2_[1][1];
                engines[1].setPos(point3d);
                point3d.y = fs_2_[2][1];
                engines[2].setPos(point3d);
                point3d.y = fs_2_[3][1];
                engines[3].setPos(point3d);
                point3d.y = fs_2_[4][1];
                engines[4].setPos(point3d);
                point3d.y = fs_2_[5][1];
                engines[5].setPos(point3d);
                engines[0].setVector(vector3f);
                engines[1].setVector(vector3f);
                engines[2].setVector(vector3f);
                engines[3].setVector(vector3f);
                engines[4].setVector(vector3f);
                engines[5].setVector(vector3f);
                break;

            default:
                throw new RuntimeException("UNIDENTIFIED ENGINE DISTRIBUTION.");
            }
        }
    }

    public void update(float f)
    {
        if(bCatapultArmed && System.currentTimeMillis() > lCatapultStartTime + 2500L / (long)Time.speed())
        {
            bCatapultArmed = false;
            dCatapultForce = 0.0D;
            iCatapultNumber = 0;
        }
        producedF.set(0.0D, 0.0D, 0.0D);
        producedM.set(0.0D, 0.0D, 0.0D);
        for(int i = 0; i < num; i++)
        {
            engines[i].update(f);
            producedF.x += (double)((Tuple3f) (engines[i].getEngineForce())).x + dCatapultForce / (double)num;
            producedF.y += ((Tuple3f) (engines[i].getEngineForce())).y;
            producedF.z += ((Tuple3f) (engines[i].getEngineForce())).z;
            if( !bCatapultArmed ){
                producedM.x += ((Tuple3f) (engines[i].getEngineTorque())).x;
                producedM.y += ((Tuple3f) (engines[i].getEngineTorque())).y;
                producedM.z += ((Tuple3f) (engines[i].getEngineTorque())).z;
            }
        }

    }

    public void netupdate(float f, boolean bool)
    {
        for(int i = 0; i < num; i++)
            engines[i].netupdate(f, bool);

    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int i)
    {
        num = i;
    }

    public void toggle()
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].toggle();

    }

    public void setCurControl(int i, boolean bool)
    {
        bCurControl[i] = bool;
    }

    public void setCurControlAll(boolean bool)
    {
        for(tmpI = 0; tmpI < num; tmpI++)
            bCurControl[tmpI] = bool;

    }

    public boolean getCurControl(int i)
    {
        if(i >= bCurControl.length)
            return false;
        else
            return bCurControl[i];
    }

    public Motor getFirstSelected()
    {
        for(int i = 0; i < num; i++)
            if(bCurControl[i])
                return engines[i];

        return null;
    }

    public int getNumSelected()
    {
        int i = 0;
        for(int i_3_ = 0; i_3_ < num; i_3_++)
            if(bCurControl[i_3_])
                i++;

        return i;
    }

    public float getPropDirSign()
    {
        float f = 0.0F;
        for(int i = 0; i < getNum(); i++)
            if(engines[i].getPropDir() == 0)
                f++;
            else
                f--;

        return f / (float)getNum();
    }

    public float getRadiatorPos()
    {
        float f = 0.0F;
        for(int i = 0; i < getNum(); i++)
            f += engines[i].getControlRadiator();

        return f / (float)getNum();
    }

    public int[] getSublist(int i, int i_4_)
    {
        int is[] = (int[])null;
        if(i_4_ == 1)
            switch(i)
            {
            case 2: // '\002'
            case 3: // '\003'
                is = new int[1];
                break;

            case 6: // '\006'
                is = new int[1];
                break;

            case 4: // '\004'
                is = (new int[] {
                    0, 1
                });
                break;

            case 5: // '\005'
                is = (new int[] {
                    0, 1
                });
                break;

            case 7: // '\007'
                is = (new int[] {
                    0, 1, 2
                });
                break;
            }
        else
        if(i_4_ == 2)
            switch(i)
            {
            case 2: // '\002'
            case 3: // '\003'
                is = (new int[] {
                    1
                });
                break;

            case 6: // '\006'
                is = (new int[] {
                    2
                });
                break;

            case 4: // '\004'
                is = (new int[] {
                    2, 3
                });
                break;

            case 5: // '\005'
                is = (new int[] {
                    3, 4
                });
                break;

            case 7: // '\007'
                is = (new int[] {
                    3, 4, 5
                });
                break;
            }
        return is;
    }

    public boolean isSelectionHasControlThrottle()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlThrottle();

        return bool;
    }

    public boolean isSelectionHasControlProp()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlProp();

        return bool;
    }

    public boolean isSelectionAllowsAutoProp()
    {
        FlightModel flightmodel = reference;
        World.cur();
        if(flightmodel != World.getPlayerFM())
            return true;
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isAllowsAutoProp();

        return bool;
    }

    public boolean isSelectionHasControlMix()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlMix();

        return bool;
    }

    public boolean isSelectionHasControlMagnetos()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlMagnetos();

        return bool;
    }

    public boolean isSelectionHasControlCompressor()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlCompressor();

        return bool;
    }

    public boolean isSelectionHasControlFeather()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlFeather();

        return bool;
    }

    public boolean isSelectionHasControlExtinguisher()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].getExtinguishers() > 0;

        return bool;
    }

    public boolean isSelectionHasControlAfterburner()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlAfterburner();

        return bool;
    }

    public boolean isSelectionAllowsAutoRadiator()
    {
        FlightModel flightmodel = reference;
        World.cur();
        if(flightmodel != World.getPlayerFM())
            return true;
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isAllowsAutoRadiator();

        return bool;
    }

    public boolean isSelectionHasControlRadiator()
    {
        boolean bool = false;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                bool |= engines[tmpI].isHasControlRadiator();

        return bool;
    }

    public float getPowerOutput()
    {
        float f = 0.0F;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            f += engines[tmpI].getPowerOutput();

        return f / (float)getNum();
    }

    public float getThrustOutput()
    {
        float f = 0.0F;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            f += engines[tmpI].getThrustOutput();

        return f / (float)getNum();
    }

    public float getReadyness()
    {
        float f = 0.0F;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            f += engines[tmpI].getReadyness();

        return f / (float)getNum();
    }

    public float getBoostFactor()
    {
        float f = 0.0F;
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            f += engines[tmpI].getBoostFactor();

        return f / (float)getNum();
    }

    public Vector3d getGyro()
    {
        tmpV3d.set(0.0D, 0.0D, 0.0D);
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            tmpV3d.add(engines[tmpI].getEngineGyro());

        return tmpV3d;
    }

    public void setThrottle(float f)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlThrottle(f);

    }

    public void setAfterburnerControl(boolean bool)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlAfterburner(bool);

    }

    public void setProp(float f)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlProp(f);

    }

    public void setPropDelta(int i)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlPropDelta(i);

    }

    public void setPropAuto(boolean bool)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlPropAuto(bool);

    }

    public void setFeather(int i)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlFeather(i);

    }

    public void setMix(float f)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlMix(f);

    }

    public void setMagnetos(int i)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlMagneto(i);

    }

    public void setCompressorStep(int i)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlCompressor(i);

    }

    public void setRadiator(float f)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setControlRadiator(f);

    }

    public void updateRadiator(float f)
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            engines[tmpI].updateRadiator(f);

    }

    public void setEngineStops()
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setEngineStops(((Interpolate) (reference)).actor);

    }

    public void setEngineRunning()
    {
        for(tmpI = 0; tmpI < getNum(); tmpI++)
            if(bCurControl[tmpI])
                engines[tmpI].setEngineRunning(((Interpolate) (reference)).actor);

    }

    public float forcePropAOA(float f, float f_5_, float f_6_, boolean bool)
    {
        float f_7_ = 0.0F;
        for(int i = 0; i < getNum(); i++)
            f_7_ += engines[i].forcePropAOA(f, f_5_, f_6_, bool);

        Aircraft.debugprintln(((Interpolate) (reference)).actor, "Computed thrust at " + f + " m/s and " + f_5_ + " m is " + f_7_ + " N..");
        return f_7_;
    }

    public void setCatapult(float f, float boost, int num)
    {
        if(f > 10000F)
            f = 10000F;
        dCatapultForce = f * boost;
        lCatapultStartTime = System.currentTimeMillis();
        bCatapultArmed = true;
        iCatapultNumber = num;
    }

    public boolean getCatapult()
    {
        return bCatapultArmed;
    }

    public int getCatapultNumber()
    {
        return iCatapultNumber;
    }

    public void resetCatapultTime()
    {
        lCatapultStartTime = System.currentTimeMillis();
    }

    private boolean bCatapultArmed;
    private double dCatapultForce;
    private long lCatapultStartTime;
    private int iCatapultNumber;
    public Motor engines[];
    public boolean bCurControl[];
    private int num;
    public Vector3d producedF;
    public Vector3d producedM;
    private FlightModel reference;
    private static Vector3d tmpV3d = new Vector3d();
    private static int tmpI;

}
