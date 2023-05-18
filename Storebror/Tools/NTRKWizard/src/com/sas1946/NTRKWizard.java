package com.sas1946;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.rts.Finger;
import com.maddox.rts.InOutStreams;
import com.maddox.rts.KryptoInputFilter;
import com.maddox.rts.KryptoOutputFilter;

public class NTRKWizard
{
	public static void main(String args[])
    {
        new NTRKWizard(args);
    }

    private void println(String s)
    {
        System.out.println(s);
    }

    private void print(String s)
    {
        System.out.print(s);
    }

    private void printException(Exception exception)
    {
        println("Exception: " + exception.getClass().getName());
        println("Message: " + exception.getMessage());
        println("");
        exception.printStackTrace();
    }

    private int indexOf(List list, String s)
    {
        for(int i = 0; i < list.size(); i++)
            if(list.get(i).toString().equalsIgnoreCase(s))
                return i;

        return -1;
    }

    private void listFiles(List list, File file, String s)
    {
        File afile[] = file.listFiles();
        for(int i = 0; i < afile.length; i++)
            if(afile[i].isFile()) {
                list.add(afile[i].getPath().substring(s.length()));
            }
            else
                listFiles(list, afile[i], s);

    }

    private void load()
    {
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new FileReader("filelist.txt"));
            fileList.clear();
            do
            {
                String s;
                if((s = bufferedreader.readLine()) == null)
                    break;
                s = s.trim();
                if(indexOf(fileList, s) == -1)
                    fileList.add(s);
            } while(true);
            bufferedreader.close();
        }
        catch(Exception exception) { }
    }

    private String resolveName(String s)
    {
        for(int i = 0; i < fileList.size(); i++) {
        	for (int j=0; j < encryptStrings.length; j++) {
	            if((Integer.toString(Finger.Int(fileList.get(i).toString().toLowerCase() + encryptStrings[j]))).equals(s))
	                return fileList.get(i).toString();
        	}
        }
        return s;
    }

    private boolean add(InOutStreams inoutstreams, String s, String s1)
    {
        try
        {
            FileInputStream fileinputstream = new FileInputStream(s1);
            byte abyte0[] = new byte[fileinputstream.available()];
            fileinputstream.read(abyte0, 0, abyte0.length);
            fileinputstream.close();
            if(!s.toLowerCase().startsWith("flightmodels/")) {
            	OutputStream out = inoutstreams.createStream(s);
            	out.write(abyte0, 0, abyte0.length);
            	out.close();
            } else {
            	for (int j=0; j < encryptStrings.length; j++) {
            		int streamFinger = Finger.Int(s.toLowerCase() + encryptStrings[j]);
	            	KryptoOutputFilter out = new KryptoOutputFilter(inoutstreams.createStream(Integer.toString(streamFinger)), getSwTbl(Finger.Int(s.toLowerCase() + "ogh9"), abyte0.length));
	            	out.write(abyte0, 0, abyte0.length);
	            	out.close();
            	}
            }
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    private boolean extract(InOutStreams inoutstreams, String s, String s1)
    {
        try
        {
            File file = new File(s1);
            file.getParentFile().mkdirs();
            InputStream inputstream = inoutstreams.openStream(s);
            FileOutputStream fileoutputstream = new FileOutputStream(s1);
            String s2 = resolveName(s);
            byte abyte0[] = new byte[4096];
            Object obj;
            if(s2.equals(s))
                obj = inputstream;
            else
                obj = new KryptoInputFilter(inputstream, getSwTbl(Finger.Int(s2.toLowerCase() + "ogh9"), inputstream.available()));
            do
            {
                int i = ((InputStream) (obj)).available();
                if(i == 0)
                    break;
                if(i > abyte0.length)
                    i = abyte0.length;
                ((InputStream) (obj)).read(abyte0, 0, i);
                fileoutputstream.write(abyte0, 0, i);
            } while(true);
            ((InputStream) (obj)).close();
            fileoutputstream.close();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    private static int[] getSwTbl(int i, int j)
    {
        if(i < 0)
            i = -i;
        if(j < 0)
            j = -j;
        int k = (j + i / 5) % 16 + 14;
        int l = (j + i / 19) % Finger.kTable.length;
        if(k < 0)
            k = -k % 16;
        if(k < 10)
            k = 10;
        if(l < 0)
            l = -l % Finger.kTable.length;
        int ai[] = new int[k];
        for(int i1 = 0; i1 < k; i1++)
            ai[i1] = Finger.kTable[(l + i1) % Finger.kTable.length];

        return ai;
    }

    private void getEncryptions(String encryptions) {
    	StringTokenizer encryptionTokens = new StringTokenizer(encryptions, ",");
    	encryptStrings = new String[encryptionTokens.countTokens()];
    	int tokenIndex = 0;
		print("Encryption Types: ");
    	while(encryptionTokens.hasMoreTokens()) {
    		String encryptionType = encryptionTokens.nextToken().trim();
    		if (encryptionType.equalsIgnoreCase("1")) {
    			encryptStrings[tokenIndex++] = ENC_IL2_STOCK;
    			print("Stock IL-2");
    		} else if (encryptionType.equalsIgnoreCase("2")) {
    			encryptStrings[tokenIndex++] = ENC_IL2_STOCK_410;
    			print("Stock IL-2 4.10");
    		} else if (encryptionType.equalsIgnoreCase("3")) {
    			encryptStrings[tokenIndex++] = ENC_HSFX_EXPERT;
    			print("HSFX Expert");
    		} else {
    			encryptStrings[tokenIndex++] = encryptionType;
    			print("Custom (");
    			print(encryptionType);
    			print(")");
    		}
        	if (encryptionTokens.hasMoreTokens())
        		print(", ");
    	}
    }
    
    private void listFiles(String as[]) {
        try
        {
        	if (as.length == 3)
        		this.getEncryptions(as[2]);
        	else
        		println("Using all known Encryption Types.");
            load();
            ArrayList filesInArchive = new ArrayList();
            InOutStreams inoutstreams = new InOutStreams();
            inoutstreams.open(new File(as[1]), false);
            inoutstreams.getEntryNames(filesInArchive);
            inoutstreams.close();
            for(int i = 0; i < filesInArchive.size(); i++)
                println(resolveName(filesInArchive.get(i).toString()));
        }
        catch(Exception exception)
        {
            printException(exception);
        }
    }
    
    private void extractFiles(String as[]) {
        try
        {
        	if (as.length == 4)
        		this.getEncryptions(as[3]);
        	else
        		println("Using all known Encryption Types.");
//	        System.loadLibrary("rts");
	        load();
            ArrayList filesInArchive = new ArrayList();
            ArrayList filesExtracted = new ArrayList();
            InOutStreams inoutstreams2 = new InOutStreams();
            inoutstreams2.open(new File(as[1]), false);
            inoutstreams2.getEntryNames(filesInArchive);
            this.showPercentage();
            int listSize = filesInArchive.size();
            float filesPerStep = (float)listSize / 41F;
            int lastStep = 0;
            for(int j = 0; j < filesInArchive.size(); j++)
            {
                String s2 = as[2] + File.separator + resolveName(filesInArchive.get(j).toString()).replace('/', File.separatorChar);
                if (!filesExtracted.contains(s2)) {
	                filesExtracted.add(s2);
	                if(!extract(inoutstreams2, filesInArchive.get(j).toString(), s2))
	                    println("Cannot extract file: " + s2);
	            }
                int curStep = (int)(((float)j / filesPerStep) + 0.5F);
                for (int steps=lastStep; steps<curStep; steps++)
                	print("þ");
                lastStep = curStep;
            }

            inoutstreams2.close();
        }
        catch(Exception exception)
        {
            printException(exception);
        }
    }
    
    private void addFiles(String as[]) {
        try
        {
        	if (as.length == 4)
        		this.getEncryptions(as[3]);
        	else
        		println("Using all known Encryption Types.");
//	        System.loadLibrary("rts");
	        load();
            File file = new File(as[2]);
            InOutStreams inoutstreams1 = new InOutStreams();
            inoutstreams1.open(new File(as[1]), true);
            if(file.isFile())
            {
                if(!add(inoutstreams1, as[2].replace(File.separatorChar, '/'), as[2]))
                    println("Cannot add file: " + as[2]);
            } else
            {
                ArrayList filesInArchive = new ArrayList();
                this.showPercentage();
                listFiles(filesInArchive, file, file.getPath() + File.separator);
                int listSize = filesInArchive.size();
                float filesPerStep = (float)listSize / 41F;
                int lastStep = 0;
                for(int k = 0; k < filesInArchive.size(); k++)
                {
                    String s3 = as[2] + File.separator + filesInArchive.get(k).toString();
                    if(!add(inoutstreams1, filesInArchive.get(k).toString().replace(File.separatorChar, '/'), s3))
                        println("Cannot add file: " + s3);
                    int curStep = (int)(((float)k / filesPerStep) + 0.5F);
                    for (int steps=lastStep; steps<curStep; steps++)
                    	print("þ");
                    lastStep = curStep;
                }

            }
            inoutstreams1.close();
        }
        catch(Exception exception)
        {
            printException(exception);
        }
    }

    private void showPercentage() {
        println("");
        println("0%       25%       50%       75%     100%");
        println("╠═══════════════════════════════════════╣");
    }

    private void showHeader() {
        println("                   ┌───────────────────────────────────────────────────────┐");
        println("                   │                                                       │");
        println("                   │                    NTRK Wizard v0.5                   │");
        println("                   │                                                       │");
        println("                   ├───────────────────────────────────────────────────────┤");
        println("                   │                                                       │");
        println("                   │                    Featured by the                    │");
        println("                   │                Special Aircraft Service               │");
        println("                   │                 http://www.sas1946.com                │");
        println("                   │ Based on NTRK Wizard 0.2 by Tim (timqwerty@yandex.ru) │");
        println("                   │      Compatible with all IL-2 game versions from      │");
        println("                   │      4.07m through 4.13m, including HSFX Expert       │");
        println("                   │                                                       │");
        println("                   │                   Released under the                  │");
        println("                   │    \"Do whatever the fuck you want with it\" license    │");
        println("                   │      see http://storebror.it.cx/sas/dwtfywwi.txt      │");
        println("                   │                                                       │");
        println("                   └───────────────────────────────────────────────────────┘");
        println("");
    }

    private void showHelp() {
        println("┌──────────────────────────────────────────────────────────────────────────────────────────────┐");
        println("│                                                                                              │");
        println("│ Usage:                                                                                       │");
        println("│ java -jar NTRKWizard.jar <command> <archive/file> [<path to extract>] [<encryption type(s)>] │");
        println("│                                                                                              │");
        println("├──────────────────────────────────────────────────────────────────────────────────────────────┤");
        println("│                                                                                              │");
        println("│ Commands are:                                                                                │");
        println("│   a    Add file to archive, <path to extract> is required                                    │");
        println("│   e,x  Extract files, <path to extract> is required                                          │");
        println("│   l    List archive, don't specify <path to extract>                                         │");
        println("│   <encryption type(s)> is/are (an) optional parameter(s) for Add and Extract commands.       │");
        println("│                                                                                              │");
        println("│ When <encryption type(s)> is/are specified, only the specified encryption(s) will be used.   │");
        println("│ Multiple <encryption types> are comma separated.                                             │");
        println("│ Valid <encryption type(s)> are e.g.:                                                         │");
        println("│   1 = Use IL-2 Stock encryption, valid for all game versions except 4.10m/4.10.1m            │");
        println("│   2 = Use IL-2 Stock encryption, valid for 4.10m/4.10.1m                                     │");
        println("│   3 = Use HSFX Expert Mode encryption                                                        │");
        println("│ <any other string> = Use custom encryption                                                   │");
        println("│ When no <encryption type(s)> are specified, all known encryption Types (1,2 and 3) are used. │");
        println("│                                                                                              │");
        println("├──────────────────────────────────────────────────────────────────────────────────────────────┤");
        println("│                                                                                              │");
        println("│ Examples:                                                                                    │");
        println("│                                                                                              │");
        println("│ The following line extracts the \"buttons\" file to the subfolder \"fmdata\",                    │");
        println("│ using all known encryption Types:                                                            │");
        println("│   java -jar NTRKWizard.jar x buttons fmdata                                                  │");
        println("│                                                                                              │");
        println("│ The following line adds all files from the \"fmdata\" to the \"buttons\" file,                   │");
        println("│ using all known encryption Types (which means all files will be added three times,           │");
        println("│ once for each encryption type, meaning that such a buttons file will work with all known     │");
        println("│ game versions:                                                                               │");
        println("│   java -jar NTRKWizard.jar a buttons fmdata                                                  │");
        println("│                                                                                              │");
        println("│ The following line adds all files from the \"fmdata\" to the \"buttons\" file,                   │");
        println("│ using IL-2 Stock encryption, valid for all game versions except 4.10m/4.10.1m, only:         │");
        println("│   java -jar NTRKWizard.jar a buttons fmdata 1                                                │");
        println("│                                                                                              │");
        println("│ The following line adds all files from the \"fmdata\" to the \"buttons\" file,                   │");
        println("│ using IL-2 Stock encryption, valid for all game versions including 4.10m/4.10.1m:            │");
        println("│   java -jar NTRKWizard.jar a buttons fmdata 1,2                                              │");
        println("│                                                                                              │");
        println("│ The following line adds all files from the \"fmdata\" to the \"buttons\" file,                   │");
        println("│ using a custom encryption:                                                                   │");
        println("│   java -jar NTRKWizard.jar a buttons fmdata d2w1                                             │");
        println("│                                                                                              │");
        println("└──────────────────────────────────────────────────────────────────────────────────────────────┘");
        println("");
    }
    
    private NTRKWizard(String as[])
    {
    	this.showHeader();
        fileList = new ArrayList();
		int command = COMMAND_UNKNOWN;
		
		switch (as.length) {
		case 2:
			if (as[0].toLowerCase().equals("l"))
				command = COMMAND_LIST;
			break;
		case 3:
			if (as[0].toLowerCase().equals("l"))
				command = COMMAND_LIST;
			else if (as[0].toLowerCase().equals("e") || as[0].toLowerCase().equals("x"))
				command = COMMAND_EXTRACT;
			else if (as[0].toLowerCase().equals("a"))
				command = COMMAND_ADD;
			break;
		case 4:
			if (as[0].toLowerCase().equals("e") || as[0].toLowerCase().equals("x"))
				command = COMMAND_EXTRACT;
			else if (as[0].toLowerCase().equals("a"))
				command = COMMAND_ADD;
			break;
		default:
			break;
		}
		
		switch (command) {
		case COMMAND_LIST:
        	this.listFiles(as);
			break;
		case COMMAND_EXTRACT:
        	this.extractFiles(as);
			break;
		case COMMAND_ADD:
        	this.addFiles(as);
			break;
		default:
        	this.showHelp();
			break;
		}
    }
    
    static {
        System.loadLibrary("rts");
    }

    private ArrayList fileList;
    private static final int COMMAND_UNKNOWN = 0;
    private static final int COMMAND_LIST = 1;
    private static final int COMMAND_EXTRACT = 2;
    private static final int COMMAND_ADD = 3;
    
    private static final String ENC_IL2_STOCK = "d2w0";
    private static final String ENC_IL2_STOCK_410 = "d2wO";
    private static final String ENC_HSFX_EXPERT = "d2w5";
    private static String[] encryptStrings = {ENC_IL2_STOCK, ENC_IL2_STOCK_410, ENC_HSFX_EXPERT};
}
