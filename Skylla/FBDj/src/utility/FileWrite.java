package utility;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class FileWrite implements Serializable {
    private static final long serialVersionUID = 1L;

    // Writes text file
    // if append == true then append to end of file
    public static void writeFile(String directory, String filename, ArrayList<String> data, boolean append) {
        try {
            // Check if directory exists and create new directory if it doesn't
            FileWrite.checkDirectory(directory);

            // Write data to file
//			FileWriter fw = new FileWriter(directory + filename, append);
//
//			for (int i = 0; i < data.size(); i++)
//			{
//				fw.write(data.get(i) + "\n");
//			}
//
//			fw.close();

            try {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + filename, append), "UTF8"));
                for (int i = 0; i < data.size(); i++) {
                    out.write(data.get(i) + "\n");
                }
                out.close();
            } catch (UnsupportedEncodingException e) {} catch (IOException e) {}

        }
//		catch (IOException e)
//		{
//			System.out.println("FileWrite.writeFile: IOException!");
//			e.printStackTrace();
//		}
        catch (Exception e) {
            System.out.println("FileWrite.writeFile: Exception!");
            e.printStackTrace();
        }
    }

    // Implements the Serializable method to write data.
    // Object being passed must also declare Serializable.
    public static void writeFileSerialized(String directory, String filename, Object o) {
        try {
            // Check if directory exists
            // Create new directory if it doesn't already exist
            FileWrite.checkDirectory(directory);

            FileOutputStream fs = new FileOutputStream(directory + filename);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(o);
            os.close();
            fs.close();
        } catch (IOException e) {
            System.out.println("FileWrite.writeFileSerialized: IOException!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("FileWrite.writeFileSerialized: Exception!");
            e.printStackTrace();
        }
    }

    public static void checkDirectory(String directory) {
        // Check if directory exists
        boolean directoryExists = DirectoryRead.directoryExists(directory);

        // Create new directory if it doesn't already exist
        if (!directoryExists) {
            DirectoryWrite.createDirectory(directory);
        }
    }

    // For testing
    public static void main(String[] args) {
        ArrayList<String> testData = new ArrayList<String>();
        testData.add("test");
        FileWrite.writeFileSerialized("testDirectory/", "test", testData);
    }
}
