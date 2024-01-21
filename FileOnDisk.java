package project4;

import java.io.*;
import java.util.*;
/**
 * This class represents a file or directory in the program.
 * 
 * @author Ryan Shokrpour
 * @version 11-5-2023
 */

 public class FileOnDisk extends File
 {

    private String pathName;
    private long length;

    //stores the total length of all files in the directory
    private long totalLength = 0;
    //an ArrayList of all the files in this directory that have been checked
    private ArrayList<FileOnDisk> files = new ArrayList<FileOnDisk>();
    //boolean that keeps track of if the files have been sorted
    private boolean sorted = false;
    

   /**
    * Constructs a new FileOnDisk object with a file path parameter
    * @param filePath represents the path locating the file in its directory structure
    * @throws NullPointerException when the file path is null
    * @throws IllegalArgumentException when the canonical path of the file cannot be accessed from the original path
    */
   public FileOnDisk(String filePath) throws NullPointerException, IllegalArgumentException
   {
        super(filePath);

        //attempts to obtain the path of the file
        try
        {
            pathName = super.getCanonicalPath();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("The file path should not lead to an inaccessible file");
        }     
        length = super.length();
   }

   /**
    * Getter that returns the canonical path of the file
    * @return a String representing the canonical path name
    */
   public String getPathName()
   {
       return pathName;
   }

   /**
    * Getter that returns the length of all files combined if the object is a directory
    * or the length of a single file if the object is a file
	* @return the length of a file or an entire directory as a long    
    */
   public long getTotalSize()
   {
       //checks if the object is simply a file to return just its size
       if (this.isFile())
       {
           return length;
       }
        //checks if the length of all files has not been calculated and stored already
        if (totalLength == 0)
        {
            exploreDir(pathName);
            return totalLength; 
        }
        //returns the previously stored length of all files
        else
        {
            return totalLength;
        }
   }

   /**
    * Getter that returns a specified amount of the largest files in the directory structure
    * @param numOfFiles represents the number of files intended to be returned
    * @return a List of the largest files in the directory structure
    */
   public List <FileOnDisk> getLargestFiles(int numOfFiles)
   {
       ArrayList<FileOnDisk> largestFiles = new ArrayList<FileOnDisk>();

       //returns null if the <code>FileOnDisk</code> object is a file
       if (this.isFile())
       {
           return null;
       }
       else if (this.isDirectory())
       {
           //checks if directory has already been explored
           if (files.size() == 0)
           {
               exploreDir(pathName);
           }
           //checks if the files in the directory have already been sorted
           if (!sorted)
           {
               //sorts the files
               mergeSort(files, 0, files.size() - 1);
               sorted = true;
           }

           //adds the amount of files it can to the list to be returned
           for (int i = 0; i < numOfFiles && i < files.size(); i++)
           {
               //ignores any directories that were added to the larger ArrayList of files
               if (files.get(i).isFile())
               {
                   largestFiles.add(files.get(i));
               }
               //index <code>i</code> is still incremented therefore it is necessary to also increment the parameter value for a skipped iteration
               else
               {
                   numOfFiles++;
               }
           }
           return largestFiles;
       }
       return null;
   }

   /**
    * Converts a file into a readable String value containing its size and path name
    * @return a string representing the size and path name of a file
    */
   public String toString()
   {
        //converts the byte size if necessary
        return byteConverter(getTotalSize()) + pathName;
   }

   /**
    * Converts a long representing bytes into different units
    * @param bytes is the bytes number to be converted
    * @return a formatted string representing the new measurement
    */
   public static String byteConverter(long bytes)
   {    
        double size = bytes;
        String unit = "bytes  ";

        //divides the bytes by 1024 multiples times if necessary to obtain a more clean measurement
        if (size >= 1024)
        {
            size /= 1024;
            unit = "KB     ";
        }
        if (size >= 1024)
        {
            size /= 1024;
            unit = "MB     ";
        } 
        if (size >= 1024)
        {
            size /= 1024;
            unit = "GB     ";
        }   

        return String.format("%8.2f " + unit, size);
   }

   /**
    * Merge sort implementation for an unsorted ArrayList of files
    * @param unsortedFiles is the ArrayList of files to be sorted
    * @param start is the recursive starting index for the splitting array
    * @param end is the recursive ending index for the splitting array
    */
   private static void mergeSort(ArrayList<FileOnDisk> unsortedFiles, int start, int end)
   {
       //base case if the current size of the split array is un-sortable
       if (start >= end)
       {
           return;
       }
       //finds a center to split the array at
       int middle = (start + end)/2;
       //recursive case that splits the array to sort the halves as well
       mergeSort(unsortedFiles, start, middle);
       mergeSort(unsortedFiles, middle + 1, end);
       //merges the two sorted halves
       merge(unsortedFiles, start, middle, middle + 1, end);
   }
   /**
    * Merges two sorted ArrayList of files within one larger un-sorted ArrayList
    * @param unsortedFiles is the ArrayList of files to be merged into
    * @param firstStart is the starting index for the first array subset
    * @param firstEnd is the ending index for the first array subset
    * @param secStart is the starting index for the second array subset
    * @param secEnd is the ending index for the second array subset
    */
   private static void merge(ArrayList<FileOnDisk> unsortedFiles, int firstStart, int firstEnd, int secStart, int secEnd)
   {
       //temporary ArrayList to store the actively sorting merging halves
       ArrayList<FileOnDisk> mergerTemp = new ArrayList<FileOnDisk>();

       //initializes indices to traverse through each sorted half
       int firstCount = firstStart;
       int secCount = secStart;

       //initializes a comparator to distinctly compare the files in a specialized way
       FileOnDiskComparatorBySize fileComparer = new FileOnDiskComparatorBySize();
       //traverses through both subsets and adds each preceding value to the temporary ArrayList
       while (firstCount <= firstEnd && secCount <= secEnd)
       {
           //adds the value from the first array subset
           if (fileComparer.compare(unsortedFiles.get(firstCount), unsortedFiles.get(secCount)) <= 0)
           {
               mergerTemp.add(unsortedFiles.get(firstCount));
               firstCount++;
           }
           //adds the value from the second array subset
           else
           {
               mergerTemp.add(unsortedFiles.get(secCount));
               secCount++;
           }
       }

       //adds whatever remaining values in the first subset to the temporary ArrayList in their order
       if (firstCount <= firstEnd)
       {
           for (int i = firstCount; i <= firstEnd; i++)
           {
               mergerTemp.add(unsortedFiles.get(i));
           }
       }
       //adds whatever remaining values in the second subset to the temporary ArrayList in their order
       else if (secCount <= secEnd)
       {
           for (int i = secCount; i <= secEnd; i++)
           {
               mergerTemp.add(unsortedFiles.get(i));
           }
       }

       //replaces the values in the overarching larger unsorted ArrayList with the now-sorted values from the temporary ArrayList
       for (int i = 0; i < mergerTemp.size(); i++)
       {
           unsortedFiles.set(i + firstStart, mergerTemp.get(i));
       }
   }

   /**
    * Recursive function that explores through an entire directory structure
    * and keeps track of all the files it has passed through and adds their lengths to the total size
    * @param dirName is the name of the directory being searched through
    */
   private void exploreDir (String dirName)
   {

       //represents the current file being analyzed
        FileOnDisk file = new FileOnDisk(dirName);

        //checks if the file does not exist or has already been accounted for 
        if (!file.exists() || files.contains(file))
        {
            //do nothing with this file or directory
            return;
        }

        //checks if it is a file (base case)
        if (file.isFile())
        {
            //adds the file size to the total size of all files in the directory structure
            totalLength += file.length;
            //adds the file to the list noting the already traversed through files
            files.add(file);
        }
        //checks if it is a directory (recursive case)
        else if (file.isDirectory())
        {
            //adds the directory to the list noting the already traversed through files
            files.add(file);
            //obtains the list of files in the directory
            File[] listOfFiles = file.listFiles();

            //checks if the list of files within the directory is null
            if (listOfFiles == null)
            {
                return;
            }
            //explores each file in the subdirectory
            for (File temp: listOfFiles)
            {
                //tries to obtain the canonical path of the file
                try
                {
                    exploreDir(temp.getCanonicalPath());
                }
                catch (IOException e)
                {
                    //skip that file or directory if it couldn't obtain the canonical path
                }            
            }
        }
   }
 }
