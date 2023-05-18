package utility;

/*
 * Returns times and dates
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {
    // Return the FBDj default Unix time in milliseconds
    public static long getTime() {
        return System.currentTimeMillis(); // Unix time in milliseconds
    }

    // Returns Unix time in a pretty format
    public static String getTime(long unixTime) {
        String date = new java.text.SimpleDateFormat("dd MMM, yyyy HH:mm:ss").format(new Date(unixTime));

        return date;
    }

    // Returns the seconds between present and provided Unix time
    public static int getTimeDuration(long startTime) {
        long currentTime = System.currentTimeMillis();

        int duration = (int) (currentTime - startTime);

        return duration; // in milliseconds
    }

    // Returns a custom-defined time format
    public static String getTimeCustom(String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(cal.getTime());

        return time;
    }

    public static String getTimeCustom(long timeStamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String time = sdf.format(timeStamp);

        return time;
    }

    public static void main(String args[]) {
        // Test getTime()
        long unixTime = Time.getTime(); // return FBDj default time
        System.out.println("Time.getTime(): " + unixTime);

        // Test getTimeFormatted(long unixTime)
        String formattedTime = Time.getTime(unixTime);
        System.out.println("Time.getTimeFormatted(): " + formattedTime);

        // Test duration
        // Sleep for 5 secs first
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long duration = Time.getTimeDuration(unixTime);
        System.out.println("Time.getDuration(): " + duration);

        // Test a custom format
        String customFormat = Time.getTimeCustom("yyyy/MM/dd HH:mm");
        System.out.println("Time.getTimeCustom(): " + customFormat); // return only

        customFormat = Time.getTimeCustom(Time.getTime(), "yyyy/MM/dd HH:mm");
        System.out.println("Time.getTimeCustom()2: " + customFormat); // return only
        // year
    }
}
