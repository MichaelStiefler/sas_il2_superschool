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


    private VlaProperties prop;
    private float heightAboveLandSurface;
    private static VlaProperties constr_arg1 = null;
    private static ActorSpawnArg constr_arg2 = null;
    private NetMsgFiltered outCommand;
    private static Actor actorme;

}
