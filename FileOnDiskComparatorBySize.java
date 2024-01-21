package project4;

import java.util.*;
/**
 * This class implements a comparison between FileOnDisk objects
 *
 * @author Ryan Shokrpour
 * @version 11-5-2023
 */

public class FileOnDiskComparatorBySize implements Comparator<FileOnDisk>
{

    /**
     * Compares two FileOnDisk objects by sizes (bytes; larger comes first) and, if the sizes are equal by path names (using lexicographic ordering).
     * @param o1 represents the first file being compared
     * @param o2 represents the second file being compared
     * @return a negative int, zero, or a positive int as the first argument comes before, is equal to, or comes after the second
     * @throws NullPointerException when either file is null
     */
    public int compare(FileOnDisk o1, FileOnDisk o2) throws NullPointerException
    {
        //checks if either argument is null
        if (o1 == null || o2 == null)
        {
            throw new NullPointerException("Can't compare null files");
        }
        //checks if the first argument is smaller than the second one; if so it should come after it
        else if (o1.length() < o2.length())
        {
            return 1;
        }
        //checks if the first argument is greater than the second one; if so it should come before it
        else if (o1.length() > o2.length())
        {
            return -1;
        }
        //if the sizes were equal, compares their placements based on the <code>compareTo()</code> method of the String class for their paths
        else
        {
            return o1.getPathName().toLowerCase().compareTo(o2.getPathName().toLowerCase());
        }
    }
}
