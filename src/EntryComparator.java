import java.util.Comparator;

public class EntryComparator implements Comparator<Entry> {
    private final int alt;

    public EntryComparator(int alt) {
        this.alt = alt;
    }

    @Override
    public int compare(Entry o1, Entry o2) {
        int o1Dist = o1.distanceTo(alt);
        int o2Dist = o2.distanceTo(alt);

        if (o1Dist == o2Dist) { return 0; }

        return o1Dist < o2Dist ? -1 : 1;
    }
}
