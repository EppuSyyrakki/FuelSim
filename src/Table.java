import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents storage and calculation functionality for calculating airplane fuel consumption when 
 * changing altitudes, according to a CSV file.
 */
public class Table { 
    private final ArrayList<Entry> climb;
    private final ArrayList<Entry> descend;

    /**
     * Reads values and stores them when creating this object. File format must be CSV with 3 columns: 
     * altitude, climb fuel consumption, descend fuel consumption.
     * @param fileName The file containing the fuel consumption table.
     */
    public Table(String fileName) {  
        Scanner scanner;
        var climbTemp = new ArrayList<Entry>();
        var descendTemp = new ArrayList<Entry>();
        int rows = 0;

        try {
            scanner = new Scanner(new File("./" + fileName));
        } catch (Exception e) {
            System.out.println("CSV file " + fileName + " not found.");
            climb = null;
            descend = null;
            return;
        }        

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(",");

            try {
                int alt = Integer.parseInt(values[0]);
                int asc = Integer.parseInt(values[1]);
                int desc = Integer.parseInt(values[2]);
                climbTemp.add(new Entry(alt * 1000, asc));
                descendTemp.add(new Entry(alt * 1000, desc));
                rows++;
            } 
            catch (NumberFormatException e) {
                // Header line in CSV or non-integer entry.
                continue;
            }
        }

        assert(rows > 1);   // need at least 2 rows for extra/interpolation to work
        climb = new ArrayList<Entry>(climbTemp.reversed()); // reverse for readability (start from 0 altitude)
        descend = new ArrayList<Entry>(descendTemp.reversed());
        scanner.close();
    }
    
    /**
     * Public API of the fuel consumption calculator.
     * @param current The starting altitude (feet)
     * @param target The target altitude (feet)
     * @param useSimple True uses simple loop function for calculation. False uses comparator.
     * @return Fuel usage for change in altitude according to CSV file provided in constructor.
     */
    public int getConsumption(int current, int target, boolean useSimple) {
        if (current == target) { return 0; }    // early exit

        boolean climbing = current < target;
        ArrayList<Entry> table = climbing ? climb : descend;
        Entry start = useSimple ? getEntry(current, table) : getEntryComparator(current, table);
        Entry end = useSimple ? getEntry(target, table) : getEntryComparator(target, table);               
        int result = end.CONSUMPTION - start.CONSUMPTION;
        return climbing ? result : result * -1; // Descend table gives negative results
    }

    /**
     * Naive implementation to find closest altitude. Loops through table to find closest altitude and 
     * interpolates between it and the one above it, or extrapolate from bottom/top 2 if given altitude 
     * is below or above the values in the table.
     * @param altitude Target altitude to find the closest matches to.
     * @param table Table to use (climb or ascend).
     * @return The interpolated altitude and consumption values as an Entry object.
     */
    private static Entry getEntry(int altitude, ArrayList<Entry> table) {
        Entry a = null, b = null;

        if (altitude < table.get(0).ALTITUDE) {  // below table values
            a = table.get(1);   // lerping downwards from lowest 2
            b = table.get(0);
        }
        else if (altitude > table.get(table.size() - 1).ALTITUDE) {  // above table values
            a = table.get(table.size() - 2);    // lerping upwards from highest 2
            b = table.get(table.size() - 1);
        }
        else {
            for (int i = 1; i < table.size(); i++) {    // within table values
                if (altitude <= table.get(i).ALTITUDE) {
                    a = table.get(i - 1);   // lerping between 2 table values
                    b = table.get(i);
                    break;
                }
            }
        }

        if (a == null || b == null) {
            System.err.println("Failed to get table entry for altitude " + altitude);
            return null;
        }

        return Entry.lerp(a, b, normalize(altitude, a.ALTITUDE, b.ALTITUDE));
    }

    /**
     * Finds 2 closest altitudes by sorting a list with a comparator and interpolates (or extrapolates) a value.
     * @param altitude Target altitude to find the closest matches to.
     * @param table Table to use (climb or ascend).
     * @return The interpolated altitude and consumption values as an Entry object.
     */
    private static Entry getEntryComparator(int altitude, ArrayList<Entry> table) {
        ArrayList<Entry> tmp = new ArrayList<>(table);
        tmp.sort(new EntryComparator(altitude));
        Entry a = tmp.get(0);
        Entry b = tmp.get(1);              
        return Entry.lerp(a, b, normalize(altitude, a.ALTITUDE, b.ALTITUDE));
    }

    /**
     * Normalizes a value to between 0 and 1. Unclamped, so can return values larger than 1 for extrapolation.
     */
    private static float normalize(int value, int min, int max) {
        return ((float)value - (float)min) / ((float)max - (float)min);
    }
}