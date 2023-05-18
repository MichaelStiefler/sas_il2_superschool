package utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class FileRead implements Serializable {
    private static final long serialVersionUID = 1L;

    // Returns ArrayList containing the contents of the specified file (include
    // full path to file)
    public static ArrayList<String> getFile(String filename) {
        ArrayList<String> file = new ArrayList<String>();

        try {
            // Open file
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
//			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));

            // Read file into ArrayList
            String line;
            while ((line = reader.readLine()) != null) {
                file.add(line);
            }

            // Close file
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("ReadFile.getFile FileNotFoundException!");
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("ReadFile.getFile IOException!");
            System.out.println(e.toString());
        } catch (Exception e) {
            System.out.println("ReadFile.getFile Exception!");
            System.out.println(e.toString());
        }

        return file;
    }

    public static Object getFileSerialized(String filename) {
        Object o = null;

        try {
            // Open the serialized object stream
            File file = new File(filename);
            FileInputStream fIn = new FileInputStream(file);
            ObjectInputStream oIn = new ObjectInputStream(fIn);

            // Read in the object
            o = oIn.readObject();

            // Close the streams
            oIn.close();
            fIn.close();
        } catch (FileNotFoundException e) {
            System.out.println("ReadFile.getFileSerialized FileNotFoundException!");
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println("ReadFile.getFileSerialized IOException!");
            System.out.println(e.toString());
        } catch (Exception e) {
            System.out.println("ReadFile.getFileSerialized Exception!");
            System.out.println(e.toString());
        }

        return o;
    }
}
