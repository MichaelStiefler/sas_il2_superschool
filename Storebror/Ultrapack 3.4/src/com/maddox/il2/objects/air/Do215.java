package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public abstract class Do215 extends Do17 {

    public void update(float f) {

        this.hierMesh().chunkSetAngles("Radiator1_D0", 0.0F, -30F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        this.hierMesh().chunkSetAngles("Radiator2_D0", 0.0F, -30F * this.FM.EI.engines[1].getControlRadiator(), 0.0F);

        super.update(f);
    }

    static {
        Class class1 = Do215.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
