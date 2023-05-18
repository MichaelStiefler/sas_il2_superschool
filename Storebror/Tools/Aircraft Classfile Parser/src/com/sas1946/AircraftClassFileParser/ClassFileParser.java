package com.sas1946.AircraftClassFileParser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassFileParser {
	public void parseFiles(String directoryName) {
		File directory = new File(directoryName);

		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				if (file.getName().matches("^.*\\.java")) {
					System.out.println(file.getAbsolutePath());
//                    if (file.getName().equals("AR_234B2NJ.java")) {
//                        this.parseFile(file);
//                        break;
//                    }
					
					
					List<String> listBefore = null;
					List<String> listAfter = null;
					do {
			            try {
			            	listBefore = Files.readAllLines(file.toPath(), Charset.defaultCharset());
			            	this.parseFile(file);
			            	listAfter = Files.readAllLines(file.toPath(), Charset.defaultCharset());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} while (!listBefore.equals(listAfter));
				}
			} else if (file.isDirectory()) {
				this.parseFiles(file.getAbsolutePath());
			}
		}
	}

	private void parseFile(File file) {
        try {
            List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            
            boolean weaponTriggersRegister = false;
            boolean weaponHooksRegister = false;
            String packageName = null;
            String methodName = null;
            int curBracketLevel = 0;
            int oldBracketLevel = 0;
           
            for (int listIndex=0; listIndex<list.size(); listIndex++) {
                String line = list.get(listIndex);
                String lineOld;
                
                do {
                    lineOld = new String(line);
	                // Get Package Name
	                if (line.trim().startsWith("package")) {
	                    packageName = getPackageName(line);
	                } else if (packageName != null) {
	                    // Trim full qualified names
	                    if (line.contains(packageName + ".") && !line.contains("\"" + packageName + ".") && !line.trim().startsWith("import")) {
	                        line = line.replace(packageName + ".", "");
	                    }
	                }
	                
	                // Change all non-Array Cockpit Class declarations (single cockpit planes) to Array Style
	                line = line.replaceAll("^(.*Property\\.set\\(.+,\\s\\\"cockpitClass\\\",\\s)Cockpit(.+)\\);", "$1new Class[] { Cockpit$2 });");
	                
	                // Remove surplus comments after switch cases
	                line = line.replaceAll("^(\\s*case\\s\\d+:)\\s\\/\\/\\s\\'.+\\'", "$1");
	                
	                // Replace "CLASS.THIS()" be real class reference
	                line = line.replaceAll("^(\\s+)Class\\s((\\w|\\d)+)\\s=\\sCLASS\\.THIS\\(\\);", "$1Class $2 = " + file.getName().replaceFirst("[.][^.]+$", "") + ".class;");
	                
	                // Replace "CLASS.THIS()" be real class reference No. 2
	                line = line.replaceAll("^(.*Property\\.set\\()CLASS\\.THIS\\(\\)(.*$)", "$1" + file.getName().replaceFirst("[.][^.]+$", "") + ".class" + "$2");

	                // Remove surplus actor typecast
	                line = line.replaceAll("(^.*)\\(\\(Actor\\)\\s\\((\\S+?)\\)\\)(.*$)", "$1$2$3");
	
	                // Remove surplus SndAircraft typecast
	                line = line.replaceAll("(^.*)\\(\\(SndAircraft\\)\\s\\((\\S+?)\\)\\)(.*$)", "$1$2$3");
	
	                // Remove surplus FlightModelMain typecast
	                line = line.replaceAll("(^.*)\\(\\(FlightModelMain\\)\\s\\((\\S+?)\\)\\)(.*$)", "$1$2$3");
	
	                // Remove surplus Tuple3d typecast
	                line = line.replaceAll("(^.*)\\(\\(Tuple3d\\)\\s\\((\\S+?)\\)\\)(.*$)", "$1$2$3");
	
	                // Remove surplus Tuple3f typecast
	                line = line.replaceAll("(^.*)\\(\\(Tuple3f\\)\\s\\((\\S+?)\\)\\)(.*$)", "$1$2$3");
	
	                // Set Math.PI where it applies
	                line = line.replace("3.1415926535897931D", "Math.PI");
	                
	                // Fix gravitational constant misused custom const value 1
	                line = line.replace("0.2038736F", "(2F / 9.81F)");
	                
	                // Add Object Cast to Bullet declarations
	                line = line.replaceAll("^(.*Property\\.set\\(.+,\\s\\\"bulletClass\\\",\\s)(?!\\(Object\\))(.+)\\.class\\);", "$1(Object)$2.class);");

	                // Get rid of 0.10000000000000001D style constants
	                Pattern zeroesPattern = Pattern.compile("(^.*\\D+)(\\d+\\.[0-9]*[1-9]+)0{4,}([^0]\\d*)([DF])(.*$)");
	                while (true) {
	                    Matcher zeroesMatcher = zeroesPattern.matcher(line);
	                    if (!zeroesMatcher.find()) break;
	                    MatchResult zeroesMatcherResult = zeroesMatcher.toMatchResult();
	                    line = zeroesMatcherResult.group(1) + zeroesMatcherResult.group(2) + zeroesMatcherResult.group(4) + zeroesMatcherResult.group(5);
	                    //line = zeroesMatcher.replaceFirst("$1$2$4$5");
	                }
	                
	                // Get rid of 0.59999999999999998D style constants
	                Pattern ninesPattern = Pattern.compile("(^.*\\D+)(\\d+)(\\.)(\\d*[0-8]+)(9{4,})(\\d*)([DF])(.*$)");
	                while (true) {
	                    Matcher ninesMatcher = ninesPattern.matcher(line);
	                    if (!ninesMatcher.find()) break;
	                    MatchResult ninesMatcherResult = ninesMatcher.toMatchResult();
	                    BigDecimal ninesBigDecimal = new BigDecimal(ninesMatcherResult.group(2) + ninesMatcherResult.group(3) + ninesMatcherResult.group(4) + ninesMatcherResult.group(5) + ninesMatcherResult.group(6));
	                    ninesBigDecimal = ninesBigDecimal.setScale(ninesMatcherResult.group(4).length(), RoundingMode.HALF_UP);
	                    line = ninesMatcherResult.group(1) + ninesBigDecimal.toPlainString() + ninesMatcherResult.group(7) + ninesMatcherResult.group(8);
	                }
	                
	                // set appropriate constants instead of gfactors.setGFactors(1.5F, 1.5F, 1.0F, 2.0F, 2.0F, 2.0F);
	                Pattern gFactorsPattern = Pattern.compile("(^.*\\.)setGFactors\\((.*F),\\s(.*F),\\s(.*F),\\s(.*F),\\s(.*F),\\s(.*F)(\\);)");
	                while (true) {
	                    Matcher gFactorsMatcher = gFactorsPattern.matcher(line);
	                    if (!gFactorsMatcher.find()) break;
	                    MatchResult gFactorsMatcherResult = gFactorsMatcher.toMatchResult();
	                    line = gFactorsMatcherResult.group(1) + "setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR" + gFactorsMatcherResult.group(8);
	                    list.add(listIndex + 1, "//    private static final float POS_G_RECOVERY_FACTOR = " + gFactorsMatcherResult.group(7) + ";");
	                    list.add(listIndex + 1, "//    private static final float POS_G_TIME_FACTOR = " + gFactorsMatcherResult.group(6) + ";");
	                    list.add(listIndex + 1, "//    private static final float POS_G_TOLERANCE_FACTOR = " + gFactorsMatcherResult.group(5) + ";");
	                    list.add(listIndex + 1, "//    private static final float NEG_G_RECOVERY_FACTOR = " + gFactorsMatcherResult.group(4) + ";");
	                    list.add(listIndex + 1, "//    private static final float NEG_G_TIME_FACTOR = " + gFactorsMatcherResult.group(3) + ";");
	                    list.add(listIndex + 1, "//    private static final float NEG_G_TOLERANCE_FACTOR = " + gFactorsMatcherResult.group(2) + ";");
	                }
	                
	                // set appropriate constants instead of new RadarWarningReceiverUtils(this, 1, 16, 7, 45D, false, false, bRWR_Show_Text_Warning);
	                Pattern rwrUtilsPattern = Pattern.compile("(^.*)new RadarWarningReceiverUtils\\((.*), (\\d+), (\\d+), (\\d+), (.*D), ((?:true|false)), ((?:true|false)), (.*)(\\);)");
	                while (true) {
	                    Matcher rwrUtilsMatcher = rwrUtilsPattern.matcher(line);
	                    if (!rwrUtilsMatcher.find()) break;
	                    MatchResult rwrUtilsMatcherResult = rwrUtilsMatcher.toMatchResult();
	                    line = rwrUtilsMatcherResult.group(1) + "new RadarWarningReceiverUtils(" + rwrUtilsMatcherResult.group(2) + ", RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, " + rwrUtilsMatcherResult.group(9) + rwrUtilsMatcherResult.group(10);
	                    list.add(listIndex + 1, "//    private static final boolean RWR_DETECT_ELEVATION = " + rwrUtilsMatcherResult.group(8) + ";");
	                    list.add(listIndex + 1, "//    private static final boolean RWR_DETECT_IRMIS = " + rwrUtilsMatcherResult.group(7) + ";");
	                    list.add(listIndex + 1, "//    private static final double RWR_RECEIVE_ELEVATION = " + rwrUtilsMatcherResult.group(6) + ";");
	                    list.add(listIndex + 1, "//    private static final int RWR_KEEP_SECONDS = " + rwrUtilsMatcherResult.group(5) + ";");
	                    list.add(listIndex + 1, "//    private static final int RWR_MAX_DETECT = " + rwrUtilsMatcherResult.group(4) + ";");
	                    list.add(listIndex + 1, "//    private static final int RWR_GENERATION = " + rwrUtilsMatcherResult.group(3) + ";");
	                }
	
	                
	                // If the current line might be a method declaration, store the method name
	                if (curBracketLevel == 1 && !line.trim().isEmpty() && !line.contains(";") && !line.contains("//") && !line.contains("/*")) {
	                    if (line.contains("(") && line.contains(")")) {
	                        String[] parts = line.trim().substring(0, line.trim().indexOf("(")).split(" ");
	                        methodName = parts[parts.length-1];
	                    }
	                }
	                
	                if (methodName != null) {
	                    // Change all super calls to this calls, except for real super method calls
	                    line = line.replaceAll("^(.*)super\\.((?!" + methodName + "\\().*)", "$1this.$2");
	                }
	
	                list.set(listIndex, line);
	                oldBracketLevel = curBracketLevel;
	                curBracketLevel += occurances(line, "{");
	                curBracketLevel -= occurances(line, "}");
	                
	                // If we reached the end of a method, reset method name to null
	                if (oldBracketLevel > curBracketLevel && curBracketLevel == 1)
	                    methodName = null;
	                
	                while (line.matches("^.*static\\sClass\\s+class\\$com\\$maddox.*;")) {
	                    list.remove(listIndex);
	                    line = list.get(listIndex);
	                }
	                    
	                if (line.trim().startsWith("// Decompiled by Jad"))
	                    removeCommentBlock(list, listIndex);
	                else if (line.trim().startsWith("// Referenced classes"))
	                    removeCommentBlock(list, listIndex);
	                else if (line.trim().startsWith("static Class _mthclass")
	                        || line.trim().startsWith("private static final float floatindex(float")
	                        || line.trim().startsWith("private static float floatindex(float f"))
	                    removeMethod(list, listIndex);
	                
	                if (line.contains("weaponTriggersRegister"))
	                    weaponTriggersRegister = true;
	                if (line.contains("weaponHooksRegister"))
	                    weaponHooksRegister = true;
	                if (weaponTriggersRegister && weaponHooksRegister) {
	                    removeWeaponSlots(list, listIndex);
	                    weaponTriggersRegister = weaponHooksRegister = false;
	                }
                } while (!line.equals(lineOld));
            }
            
            
            Files.write(file.toPath(),list,Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void removeCommentBlock(List<String> list, int startIndex) {
		while (startIndex < list.size() && list.get(startIndex).trim().startsWith("//")) {
			list.remove(startIndex);
		}
		while (startIndex < list.size() && list.get(startIndex).trim().isEmpty()) {
			list.remove(startIndex);
		}
	}

	private void removeMethod(List<String> list, int startIndex) {
		String line;
		int bracketCounter = list.get(startIndex).contains("{") ? 1 : 0;
		list.remove(startIndex);
		if (bracketCounter == 0) {
			while (true) {
				line = list.get(startIndex);
				list.remove(startIndex);
				bracketCounter += occurances(line, "{");
				bracketCounter -= occurances(line, "}");
				if (bracketCounter > 0) break;
			}
		}
		while (true) {
			line = list.get(startIndex);
			list.remove(startIndex);
			bracketCounter += occurances(line, "{");
			bracketCounter -= occurances(line, "}");
			if (bracketCounter < 1) break;
		}
		while (startIndex < list.size() && list.get(startIndex).trim().isEmpty()) {
			list.remove(startIndex);
		}
	}

	private int occurances(String haystack, String needle) {
		int count = 0;
		int lastIndex = 0;
		while (lastIndex != -1) {
			lastIndex = haystack.indexOf(needle, lastIndex);
			if (lastIndex != -1) {
				count++;
				lastIndex += needle.length();
			}
		}
		return count;
	}

	private void removeWeaponSlots(List<String> list, int startIndex) {
		while (!list.get(startIndex++).contains(";"))
			;
		int bracketCounter = 1;
		String line;
		while (true) {
			line = list.get(startIndex);
			bracketCounter += occurances(line, "{");
			bracketCounter -= occurances(line, "}");
			if (bracketCounter < 1) break;
			list.remove(startIndex);
		}
		while (startIndex < list.size() && list.get(startIndex).trim().isEmpty()) {
			list.remove(startIndex);
		}
	}

	private String getPackageName(String line) {
		String packageName = line.trim();
		packageName = packageName.replace("package ", "");
		return packageName.substring(0, packageName.length() - 1);
	}

}
