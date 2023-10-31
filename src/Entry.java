/**
 * Represents a single data point in the Table class.
 * @author eppu.syyrakki@gmail.com
 */
public class Entry {
    public final int ALTITUDE;
    public final int CONSUMPTION;

    public Entry(int altitude, int consumption){
        this.ALTITUDE = altitude;
        this.CONSUMPTION = consumption;
    }

    public static Entry lerp(Entry a, Entry b, float t) {
        return new Entry(lerp(a.ALTITUDE, b.ALTITUDE, t), lerp(a.CONSUMPTION, b.CONSUMPTION, t));
    }

    private static int lerp(int start, int end, float t) {
        return Math.round((float)start * (1.0f - t) + (float)end * t);
    }

    public int distanceTo(int altitude) {
        return Math.abs(altitude - ALTITUDE);
    }   
}