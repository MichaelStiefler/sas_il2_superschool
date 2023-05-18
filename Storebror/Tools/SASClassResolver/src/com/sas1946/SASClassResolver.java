package com.sas1946;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.org.apache.bcel.internal.classfile.ClassParser;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;

public final class SASClassResolver {

    private static class ResolvedElement {
        private String hashedName;
        private String resolvedName;

        public ResolvedElement(String hashedName, String resolvedName) {
            this.hashedName = hashedName;
            this.resolvedName = resolvedName;
        }

        public String getHashedName() {
            return hashedName;
        }

        public String getResolvedName() {
            return resolvedName;
        }

        @Override
        public boolean equals(Object compareElement) {
            if (compareElement instanceof ResolvedElement) {
                return this.resolvedName.equalsIgnoreCase(((ResolvedElement) compareElement).resolvedName);
            }
            return false;
        }
    }

    private static ArrayList<ResolvedElement> resolvedElements;
    private static String  myJarFileName       = "";
    private static boolean startedFromLaunch4j = false;

    private static boolean isHexadecimal(String s) {
        try {
            Integer.parseInt(s, 16);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    private static boolean isDecimal(String s) {
        try {
            Integer.parseInt(s, 10);
            return true;
        } catch (NumberFormatException nfe) {
        }
        return false;
    }

    private static String getJarFileName() {
        try {
            return new File(SASClassResolver.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getName();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void listFiles(List<String> list, File file) {
        File afile[] = file.listFiles();
        for (int i = 0; i < afile.length; i++)
            if (afile[i].isFile())
                list.add(afile[i].getPath());
    }

    private static boolean resolveClass(File file) {
        String fileName = "";
        boolean supposedToBeClass = false;
        String wrongJavaVersion = "";
        String duplicateFiles = "";
        int duplicateNum = 0;
        boolean separatorPrinted = false;
        try {
            fileName = file.getName();
            if (fileName.indexOf(".") == 0) {
                if (fileName.length() == 16 && isHexadecimal(fileName) || isDecimal(fileName)) {
                    if (file.getName().trim().compareToIgnoreCase(myJarFileName.trim()) != 0)
                        supposedToBeClass = true;
                }
            }
            JavaClass javaclass = (new ClassParser(file.getPath())).parse();
            String s = javaclass.getClassName().replace('.', File.separatorChar) + ".class";
            if (javaclass.getMajor() < 45 || javaclass.getMajor() > 47) {
                separatorPrinted = true;
                println("════════════════════════════════════════════════════════════════════════════════");
                println("File " + file.getPath() + " resolves to Java Class " + javaclass.getClassName() + " but has wrong Major Verion " + javaclass.getMajor());
                wrongJavaVersion = "wrong_java_version_files" + File.separatorChar;
            }
            ResolvedElement newResolvedElement = new ResolvedElement(fileName, javaclass.getClassName());
            if (resolvedElements.contains(newResolvedElement)) {
                if (!separatorPrinted) {
                    separatorPrinted = true;
                    println("════════════════════════════════════════════════════════════════════════════════");
                }
                println("File " + file.getPath());
                println(" resolves to Java Class " + javaclass.getClassName());
                println(" but already exists, possibly with different upper and lower case:");
                println("");
                println(file.getName() + " = " + javaclass.getClassName());
                for (int resolvedElementsIndex = 0; resolvedElementsIndex < resolvedElements.size(); resolvedElementsIndex++) {
                    if (resolvedElements.get(resolvedElementsIndex) != null) {
                        if (resolvedElements.get(resolvedElementsIndex).equals(newResolvedElement)) {
                            println(resolvedElements.get(resolvedElementsIndex).getHashedName() + " = " + resolvedElements.get(resolvedElementsIndex).getResolvedName());
                        }
                    }
                }
                println("");
            }
            resolvedElements.add(newResolvedElement);
            File newFile = null;
            while (true) {
                newFile = new File(wrongJavaVersion + duplicateFiles + s);
                if (!newFile.exists()) break;
                if (!newFile.isFile()) break;
                duplicateNum++;
                duplicateFiles = "duplicate_files" + ((duplicateNum > 1)?""+duplicateNum:"") + File.separatorChar;
            }
            
            if (duplicateNum > 0) {
                if (!separatorPrinted) {
                    separatorPrinted = true;
                    println("════════════════════════════════════════════════════════════════════════════════");
                }
                println("Resolved file '" + wrongJavaVersion + s + "'");
                println(" already exists, possibly with different upper and lower case.");
                println("Copying to '" + wrongJavaVersion + duplicateFiles + s + "'");
                println(" instead. Please check these files later!");
                println("");
            }
            
            newFile.getParentFile().mkdirs();
            boolean retVal = file.renameTo(newFile);
            if (!retVal) {
                println("Couldn't rename file " + file.getPath() + " to " + newFile.getPath());
            }
            return retVal;
        } catch (Exception e) {
            if (supposedToBeClass) {
                println("Attempting to resolve class for " + file.getPath() + " caused the following Exception:");
                printException(e);
                return false;
            }
        }
        if (supposedToBeClass)
            println("Cannot resolve class: " + file.getName());
        return false;
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(String s) {
        System.out.print(s);
    }

    private static void printException(Exception exception) {
        println("Exception: " + exception.getClass().getName());
        println("Message: " + exception.getMessage());
        println("");
        exception.printStackTrace();
    }

    private static void showHeader() {
        println("           ┌───────────────────────────────────────────────────────┐");
        println("           │                                                       │");
        println("           │                SAS Class Resolver v1.0                │");
        println("           │                                                       │");
        println("           ├───────────────────────────────────────────────────────┤");
        println("           │                                                       │");
        println("           │                    Featured by the                    │");
        println("           │                Special Aircraft Service               │");
        println("           │                 http://www.sas1946.com                │");
        println("           │          Compatible with all IL-2 game versions       │");
        println("           │                                                       │");
        println("           │                   Released under the                  │");
        println("           │    \"Do whatever the fuck you want with it\" license    │");
        println("           │      see http://storebror.it.cx/sas/dwtfywwi.txt      │");
        println("           │                                                       │");
        println("           └───────────────────────────────────────────────────────┘");
        println("");
    }

    private static void showHelp() {
        println("┌──────────────────────────────────────────────────────────────────────────┐");
        println("│                                                                          │");
        println("│ Usage:                                                                   │");
        println("│ java -jar SASClassResolver.jar [<path of hashed files> or <hashed file>] │");
        println("│                                                                          │");
        println("├──────────────────────────────────────────────────────────────────────────┤");
        println("│                                                                          │");
        println("│ If path/file is omitted, current path will be used.                      │");
        println("│                                                                          │");
        println("└──────────────────────────────────────────────────────────────────────────┘");
        println("");
    }

    public static void main(String args[]) {
        showHeader();
        if (args.length > 0 && args[0].compareToIgnoreCase("-launch4j") == 0) {
            args = Arrays.copyOfRange(args, 1, args.length);
            startedFromLaunch4j = true;
        }
        if (args.length < 1)
            args = new String[] { "." };
        if (args.length == 1) {
            myJarFileName = getJarFileName();
            resolvedElements = new ArrayList<ResolvedElement>();
            try {
                File file = new File(args[0]);
                if (file.isDirectory()) {
                    ArrayList<String> arraylist = new ArrayList<String>();
                    listFiles(arraylist, file);
                    for (int i = 0; i < arraylist.size(); i++)
                        resolveClass(new File(arraylist.get(i).toString()));
                } else
                    resolveClass(file);
            } catch (Exception exception) {
                printException(exception);
            }
        } else {
            showHelp();
        }

        if (startedFromLaunch4j) {
            println("");
            println("           ┌───────────────────────────────────────────────────────┐");
            println("           │                                                       │");
            println("           │      Class Resolving finished, hit enter to exit.     │");
            println("           │                                                       │");
            println("           └───────────────────────────────────────────────────────┘");
            println("");
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
