package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class MIG_3POKRYSHKIN extends MIG_3
    implements TypeAcePlane
{

    public MIG_3POKRYSHKIN()
    {
        kangle = 0.0F;
    }

    public void update(float f)
    {
        if(FM.getSpeed() > 5F)
        {
            hierMesh().chunkSetAngles("SlatL_D0", cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 0.9F), 0.0F, 0.0F);
            hierMesh().chunkSetAngles("SlatR_D0", cvt(FM.getAOA(), 6.8F, 11F, 0.0F, 0.9F), 0.0F, 0.0F);
        }
        hierMesh().chunkSetAngles("WaterFlap_D0", 0.0F, 30F * kangle, 0.0F);
        hierMesh().chunkSetAngles("OilRad1_D0", 0.0F, -20F * kangle, 0.0F);
        hierMesh().chunkSetAngles("OilRad2_D0", 0.0F, -20F * kangle, 0.0F);
        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        super.update(f);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.Skill = 3;
    }

    private float kangle;

    static 
    {
        Class class1 = MIG_3POKRYSHKIN.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG");
        Property.set(class1, "meshName", "3do/plane/MIG-3(ofPokryshkin)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeSpecial());
        Property.set(class1, "FlightModel", "FlightModels/MiG-3(ofPokryshkin).fmd");
        weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 1, 3, 3, 3, 3, 9, 
            9, 9, 9, 2, 2, 2, 2, 2, 2
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", 
            "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06"
        });
    }
}
