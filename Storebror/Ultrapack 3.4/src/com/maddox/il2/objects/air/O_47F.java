package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.engine.HierMesh;

public abstract class O_47F extends O_47 implements TypeSailPlane {

    public static void moveGear(HierMesh hiermesh1, float f1) {
    }

    protected void moveGear(float f) {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        super.hitBone(s, shot, point3d);
        if (s.startsWith("xstrutin")) {
            if (s.startsWith("xstrutinl") && (this.chunkDamageVisible("StrutinL") < 2)) {
                this.hitChunk("StrutinL", shot);
            }
            if (s.startsWith("xstrutinr") && (this.chunkDamageVisible("StrutinR") < 2)) {
                this.hitChunk("StrutinR", shot);
            }
        } else if (s.startsWith("xstruts")) {
            if (s.startsWith("xstrutsl") && (this.chunkDamageVisible("StrutsL") < 2)) {
                this.hitChunk("StrutsL", shot);
            }
            if (s.startsWith("xstrutsr") && (this.chunkDamageVisible("StrutsR") < 2)) {
                this.hitChunk("StrutsR", shot);
            }
        }
    }

}
