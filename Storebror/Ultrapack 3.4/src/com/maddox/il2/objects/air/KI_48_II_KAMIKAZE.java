package com.maddox.il2.objects.air;

import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;

public class KI_48_II_KAMIKAZE extends KI_48 implements MsgCollisionRequestListener, MsgCollisionListener {

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "KAMIKAZE_";
    }

    public void doMurderPilot(int i) {
        if (i != 0) return;
        else {
            this.hierMesh().chunkVisible("Pilot1_D0", false);
            this.hierMesh().chunkVisible("Head1_D0", false);
            this.hierMesh().chunkVisible("Pilot1_D1", true);
            this.hierMesh().chunkVisible("HMask1_D0", false);
            return;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor1) {
        switch (i) {
            case 3:
            case 19:
                this.FM.AS.setEngineDies(this, 0);
                return false;
        }
        return super.cutFM(i, j, actor1);
    }

    public void msgEndAction(Object obj, int i) {
        super.msgEndAction(obj, i);
        switch (i) {
            case 2:
                this.actor = Engine.cur.actorLand;
                MsgExplosion.send(this, null, this.FM.Loc, this.actor, 0.0F, 2500F, 0, 890F);
                break;
        }
    }

    protected void doExplosion() {
        super.doExplosion();
        World.cur();
        if (this.FM.Loc.z - 10D < World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y)) if (Engine.land().isWater(this.FM.Loc.x, this.FM.Loc.y)) Explosions.BOMB1000a_Water(this.FM.Loc, 1.0F, 1.0F);
        else // Explosions.BOMB1000a_Land(this.FM.Loc, 1.0F, 1.0F, true, false);
            Explosions.BOMB1000a_Land(this.FM.Loc, 1.0F, 1.0F, true);
    }

    private Actor actor;

    static {
        Class class1 = KI_48_II_KAMIKAZE.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Kamikaze");
        Property.set(class1, "meshName", "3do/plane/Ki-48-II(Multi1)/KAMIKAZE_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_ja", "3do/plane/Ki-48-II(ja)/KAMIKAZE_hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Ki-48-II.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitKI_48.class });
        Property.set(class1, "LOSElevation", 1.4078F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10",
                "_BombSpawn11", "_BombSpawn12" });
    }
}
