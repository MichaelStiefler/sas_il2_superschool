package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class CockpitSB103A extends CockpitSB103
{

    public CockpitSB103A()
    {
        super("3DO/Cockpit/SB_103A/hier.him");
    }

    public void reflectCockpitState()
    {
        if((fm.AS.astateCockpitState & 1) != 0)
        {
            mesh.chunkVisible("front_cannopy_DM2", true);
            mesh.chunkVisible("front_cannopy_DM1", true);
            mesh.chunkVisible("Main_DM1", true);
        }
        if((fm.AS.astateCockpitState & 0x40) != 0)
        {
            mesh.chunkVisible("Panel_DM", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Speed", false);
                mesh.chunkVisible("Prib_Speed", true);
                mesh.chunkVisible("Z_ND_airspeed", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Pioner", false);
                mesh.chunkVisible("Prib_Pioner_dmg", true);
                mesh.chunkVisible("Z_ND_Turn", false);
                mesh.chunkVisible("Z_ND_ball", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Variom", false);
                mesh.chunkVisible("Prib_Variom_dmg", true);
                mesh.chunkVisible("Z_ND_variometr2", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Alt", false);
                mesh.chunkVisible("Prib_Alt_dmg", true);
                mesh.chunkVisible("Z_ND_alt_km", false);
                mesh.chunkVisible("Z_ND_alt_m", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_RPK", false);
                mesh.chunkVisible("Prib_RPK_dmg", true);
                mesh.chunkVisible("Z_ND_RPK", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("prib_5", false);
                mesh.chunkVisible("prib_5_DM", true);
            }
        }
        if((fm.AS.astateCockpitState & 4) != 0)
        {
            mesh.chunkVisible("Z_cannopy_DM2", true);
            mesh.chunkVisible("Z_cannopy_DM5", true);
            mesh.chunkVisible("Panel_DM", true);
            mesh.chunkVisible("Main_DM2", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_AVR", false);
                mesh.chunkVisible("Prib_AVR_dmg", true);
                mesh.chunkVisible("Z_ND_clock_hour", false);
                mesh.chunkVisible("Z_ND_clock_min", false);
                mesh.chunkVisible("Z_ND_clock_sec", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Al_L", false);
                mesh.chunkVisible("Prib_Al_L_dmg", true);
                mesh.chunkVisible("Z_ND_Alpha_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Al_R", false);
                mesh.chunkVisible("Prib_Al_R_dmg", true);
                mesh.chunkVisible("Z_ND_Alpha_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Fuel_1", false);
                mesh.chunkVisible("Prib_Fuel_1_dmg", true);
                mesh.chunkVisible("Z_ND_fuel_1", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Fuel_2", false);
                mesh.chunkVisible("Prib_Fuel_2_dmg", true);
                mesh.chunkVisible("Z_ND_fuel_2", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Fuel_3", false);
                mesh.chunkVisible("Prib_Fuel_3_dmg", true);
                mesh.chunkVisible("Z_ND_fuel_3", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Fuel_4", false);
                mesh.chunkVisible("Prib_Fuel_4_dmg", true);
                mesh.chunkVisible("Z_ND_fuel_4", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_AG", false);
                mesh.chunkVisible("Prib_AG_dmg", true);
                mesh.chunkVisible("Z_ND_ag3", false);
            }
        }
        if((fm.AS.astateCockpitState & 0x10) != 0)
        {
            mesh.chunkVisible("Z_cannopy_DM1", true);
            mesh.chunkVisible("Z_cannopy_DM4", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_RPM_R", false);
                mesh.chunkVisible("Prib_RPM_R_dmg", true);
                mesh.chunkVisible("Z_ND_rpm_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Boos_L", false);
                mesh.chunkVisible("Prib_Boos_L_dmg", true);
                mesh.chunkVisible("Z_ND_boost_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Boos_L", false);
                mesh.chunkVisible("Prib_Boos_L_dmg", true);
                mesh.chunkVisible("Z_ND_boost_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_FueP_R", false);
                mesh.chunkVisible("Prib_FueP_R_dmg", true);
                mesh.chunkVisible("Z_ND_fuelpress_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_OilP_L", false);
                mesh.chunkVisible("Prib_OilP_L_dmg", true);
                mesh.chunkVisible("Z_ND_oilpress_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_TWat_R", false);
                mesh.chunkVisible("Prib_TWat_R_dmg", true);
                mesh.chunkVisible("Z_ND_water_tem_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_TOil_L", false);
                mesh.chunkVisible("Prib_TOil_L_dmg", true);
                mesh.chunkVisible("Z_ND_oil_tem_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Volt", false);
                mesh.chunkVisible("Prib_Volt_dmg", true);
                mesh.chunkVisible("Z_ND_Voltmeter", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("prib_3", false);
                mesh.chunkVisible("prib_3_DM", true);
            }
        }
        if((fm.AS.astateCockpitState & 0x20) != 0)
        {
            mesh.chunkVisible("Z_cannopy_DM3", true);
            mesh.chunkVisible("Panel_DM", true);
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_RPM_L", false);
                mesh.chunkVisible("Prib_RPM_L_dmg", true);
                mesh.chunkVisible("Z_ND_rpm_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Boos_R", false);
                mesh.chunkVisible("Prib_Boos_R_dmg", true);
                mesh.chunkVisible("Z_ND_boost_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Boos_R", false);
                mesh.chunkVisible("Prib_Boos_R_dmg", true);
                mesh.chunkVisible("Z_ND_boost_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_FueP_L", false);
                mesh.chunkVisible("Prib_FueP_L_dmg", true);
                mesh.chunkVisible("Z_ND_fuelpress_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_OilP_R", false);
                mesh.chunkVisible("Prib_OilP_R_dmg", true);
                mesh.chunkVisible("Z_ND_oilpress_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_TWat_L", false);
                mesh.chunkVisible("Prib_TWat_L_dmg", true);
                mesh.chunkVisible("Z_ND_water_tem_L", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_TOil_R", false);
                mesh.chunkVisible("Prib_TOil_R_dmg", true);
                mesh.chunkVisible("Z_ND_oil_tem_R", false);
            }
            if(World.Rnd().nextFloat() < 0.65F)
            {
                mesh.chunkVisible("Prib_Manom", false);
                mesh.chunkVisible("Prib_Manom_dmg", true);
                mesh.chunkVisible("Z_ND_Air_pres", false);
            }
        }
    }

    static 
    {
        Property.set(CockpitSB103A.class, "normZN", 0.9F);
    }
}
