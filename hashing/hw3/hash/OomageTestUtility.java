package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] buckets = new int[M];
        int numOomages = oomages.size();
        for (int i = 0; i < M; i += 1) {
            buckets[i] = 0;
        }
        for (Oomage s : oomages) {
            int b = (s.hashCode() & 0x7FFFFFFF) % M;
            buckets[b] += 1;
        }
        for (int b : buckets) {
            if (b < numOomages / 50 || b > numOomages / 2.5) {
                return false;
            }
        }
        return true;
    }
}
