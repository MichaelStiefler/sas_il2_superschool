package com.maddox.il2.objects.weapons;
import com.maddox.rts.Property;

public class PylonUB16 extends Pylon
{
    /*synthetic*/ static Class class$com$maddox$il2$objects$weapons$PylonUB16;
    
    /*synthetic*/ static Class class$(String string) {
	Class var_class;
	try {
	    var_class = Class.forName(string);
	} catch (ClassNotFoundException classnotfoundexception) {
	    throw new NoClassDefFoundError(classnotfoundexception
					       .getMessage());
	}
	return var_class;
    }
    
    static {
	Property.set
	    (((class$com$maddox$il2$objects$weapons$PylonUB16
	       == null)
	      ? (class$com$maddox$il2$objects$weapons$PylonUB16
		 = (class$
		    ("com.maddox.il2.objects.weapons.PylonUB16")))
	      : class$com$maddox$il2$objects$weapons$PylonUB16),
	     "mesh",
	     "3DO/Arms/UB-16/mono.sim");
    }
}
