package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.objects.ActorAlign;

public abstract class VisualLandingAidGeneric extends ActorHMesh
    implements ActorAlign
{

    public VisualLandingAidGeneric(String meshName)
    {
        super(meshName);
    }

    protected VisualLandingAidGeneric()
    {
        this(constr_arg1, constr_arg2);
    }

    private VisualLandingAidGeneric(VlaProperties vlaproperties, ActorSpawnArg actorspawnarg)
    {
    }

    public void setVisible(boolean flag)
    {
    }

    public void align()
    {
    }

    public static class VlaProperties
    {

        public String meshName;
        public int type;

        public VlaProperties()
        {
            meshName = null;
            type = 0;
        }
    }


//    private VlaProperties prop;
//    private float heightAboveLandSurface;
    private static VlaProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
//    private NetMsgFiltered outCommand;
//    private static Actor actorme;

}
