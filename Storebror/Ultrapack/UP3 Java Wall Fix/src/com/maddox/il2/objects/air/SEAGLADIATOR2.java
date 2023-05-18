package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.rts.Property;

public class SEAGLADIATOR2 extends SEAGLADIATOR
{

    public SEAGLADIATOR2()
    {
        arrestor = 0.0F;
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if((!isNet() || !isNetMirror()) && !s.startsWith("Hook"))
            super.msgCollision(actor, s, s1);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -57F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        arrestor = f;
    }

    public void update(float f)
    {
        super.update(f);
        if(FM.CT.getArrestor() > 0.2F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
                if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                    f2 = 0.0F;
                if(f2 > 0.2F)
                    f2 = 0.2F;
                if(f2 > 0.0F)
                    arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
                else
                    arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
                if(arrestor < 0.0F)
                    arrestor = 0.0F;
                else
                if(arrestor > 1.0F)
                    arrestor = 1.0F;
                moveArrestorHook(arrestor);
            }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            FM.CT.bHasArrestorControl = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    private float arrestor;

    static 
    {
        Class class1 = SEAGLADIATOR2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Gladiator");
        Property.set(class1, "meshName", "3DO/Plane/SeaGladiatorMkII(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSEAGLAD());
        Property.set(class1, "meshName_gb", "3DO/Plane/SeaGladiatorMkII(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeSEAGLAD());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/SeaGladiatorMkII.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSEAGLADIATOR.class
        });
        Property.set(class1, "LOSElevation", 0.8472F);
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04"
        });
    }
}
