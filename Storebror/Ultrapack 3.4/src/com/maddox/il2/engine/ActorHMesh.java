package com.maddox.il2.engine;

import com.maddox.rts.Time;

public abstract class ActorHMesh extends ActorMesh {

    public HierMesh hierMesh() {
        return this.hmesh;
    }

    public Mesh mesh() {
        return this.hmesh != null ? this.hmesh : super.mesh();
    }

    public float collisionR() {
        // TODO: +++ Mission Change Nullpointer Exception Hotfix by SAS~Storebror +++
// return this.mesh().collisionR();
//        if (this.mesh() == null) System.out.println("Mission Change Nullpointer Exception Hotfix: " + this.name() + " would have caused issues on removal.");
        return this.mesh() == null ? this.lastCollisionR : this.mesh().collisionR();
        // TODO: --- Mission Change Nullpointer Exception Hotfix by SAS~Storebror ---
    }

    public int[] hideSubTrees(String s) {
        return this.hmesh.hideSubTrees(s);
    }

    public void destroyChildFiltered(Class class1) {
        Object aobj[] = this.getOwnerAttached();
        for (int i = 0; i < aobj.length; i++)
            if (aobj[i] != null && class1.isInstance(aobj[i])) ((Actor) aobj[i]).destroy();

    }

    public void getChunkLoc(Loc loc) {
        this.hmesh.getChunkLocObj(loc);
    }

    public void getChunkLocAbs(Loc loc) {
        this.hmesh.getChunkLocObj(loc);
        loc.add(this.pos.getAbs());
    }

    public void getChunkLocTimeAbs(Loc loc) {
        this.hmesh.getChunkLocObj(loc);
        this.pos.getTime(Time.current(), _L);
        loc.add(_L);
    }

    public float getChunkMass() {
        return this.hmesh.getChunkMass();
    }

    public void destroy() {
        if (this.isDestroyed()) return;
        // TODO: +++ Mission Change Nullpointer Exception Hotfix by SAS~Storebror +++
        if (this.mesh() != null) this.lastCollisionR = this.mesh().collisionR();
        // TODO: --- Mission Change Nullpointer Exception Hotfix by SAS~Storebror ---
        super.destroy();
        if (this.hmesh != null) {
            this.hmesh.destroy();
            this.hmesh = null;
        }
    }

    protected ActorHMesh() {
        this.hmesh = null;
    }

    protected ActorHMesh(Loc loc) {
        super(loc);
        this.hmesh = null;
    }

    protected ActorHMesh(ActorPos actorpos) {
        super(actorpos);
        this.hmesh = null;
    }

    public void setMesh(String s) {
        boolean flag = this.mesh() != null && this.pos != null;
        if (s.endsWith(".sim")) {
            this.hmesh = null;
            super.setMesh(s);
        } else {
            this.mesh = null;
            this.hmesh = new HierMesh(s);
        }
        if (flag) this.pos.actorChanged();
    }

    public void setMesh(Mesh mesh1) {
        boolean flag = this.mesh() != null && this.pos != null;
        this.mesh = mesh1;
        this.hmesh = null;
        if (flag) this.pos.actorChanged();
    }

    protected void setMesh(HierMesh hiermesh) {
        boolean flag = this.mesh() != null && this.pos != null;
        this.hmesh = hiermesh;
        this.mesh = null;
        if (flag) this.pos.actorChanged();
    }

    public ActorHMesh(String s) {
        this.hmesh = null;
        try {
            this.setMesh(s);
        } catch (RuntimeException runtimeexception) {
            super.destroy();
            throw runtimeexception;
        }
    }

    public ActorHMesh(String s, Loc loc) {
        super(loc);
        this.hmesh = null;
        try {
            this.setMesh(s);
        } catch (RuntimeException runtimeexception) {
            super.destroy();
            throw runtimeexception;
        }
    }

    public ActorHMesh(String s, ActorPos actorpos) {
        super(actorpos);
        this.hmesh = null;
        try {
            this.setMesh(s);
        } catch (RuntimeException runtimeexception) {
            super.destroy();
            throw runtimeexception;
        }
    }

    public ActorHMesh(HierMesh hiermesh, Loc loc) {
        super(loc);
        this.hmesh = null;
        try {
            this.setMesh(hiermesh);
        } catch (RuntimeException runtimeexception) {
            super.destroy();
            throw runtimeexception;
        }
    }

    private HierMesh   hmesh;
    // TODO: +++ Mission Change Nullpointer Exception Hotfix by SAS~Storebror +++
    private float      lastCollisionR = 0.0F;
    // TODO: --- Mission Change Nullpointer Exception Hotfix by SAS~Storebror ---
    private static Loc _L             = new Loc();

}
