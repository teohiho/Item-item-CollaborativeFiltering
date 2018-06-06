import java.util.*;

class CosineComparator implements Comparator<Similarity> {
	public int compare(Similarity s1, Similarity s2) {
        if (s1.getCosine() == s2.getCosine())
            return 0;
        else if (s1.getCosine() > s2.getCosine())
            return -1;
        else
            return 1;
    }
}