package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class CockpitTaubeObX extends CockpitPilot {
    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Head1_D0", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        } else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        }
    }

    public CockpitTaubeObX(String hierHim, String id) {
        super(hierHim, id);
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] {});
        this.setNightMats(false);
    }

    protected void reflectPlaneToModel() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLOut_D0", hiermesh.isChunkVisible("WingLOut_D0"));
        this.mesh.chunkVisible("WingLOut_D1", hiermesh.isChunkVisible("WingLOut_D1"));
        this.mesh.chunkVisible("WingLOut_D2", hiermesh.isChunkVisible("WingLOut_D2"));
        this.mesh.chunkVisible("WingLOut_D3", hiermesh.isChunkVisible("WingLOut_D3"));
        this.mesh.chunkVisible("WingLOut_CAP", hiermesh.isChunkVisible("WingLOut_CAP"));
        this.mesh.chunkVisible("WingROut_D0", hiermesh.isChunkVisible("WingROut_D0"));
        this.mesh.chunkVisible("WingROut_D1", hiermesh.isChunkVisible("WingROut_D1"));
        this.mesh.chunkVisible("WingROut_D2", hiermesh.isChunkVisible("WingROut_D2"));
        this.mesh.chunkVisible("WingROut_D3", hiermesh.isChunkVisible("WingROut_D3"));
        this.mesh.chunkVisible("WingROut_CAP", hiermesh.isChunkVisible("WingROut_CAP"));
        this.mesh.chunkVisible("CF_D0", hiermesh.isChunkVisible("CF_D0"));
        this.mesh.chunkVisible("CF_D1", hiermesh.isChunkVisible("CF_D1"));
        this.mesh.chunkVisible("CF_D2", hiermesh.isChunkVisible("CF_D2"));
        this.mesh.chunkVisible("CF_D3", hiermesh.isChunkVisible("CF_D3"));
    }

    static {
        Property.set(CockpitTaubeObX.class, "normZN", 0.2F);
    }

    private Point3d  tmpP;
    private Vector3d tmpV;
}
