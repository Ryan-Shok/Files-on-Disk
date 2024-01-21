package project4;

import java.io.*;
import java.util.*;
/**
 * This class provides a simple program that provides information about 
 * directory sizes (or rather combined size of all the files stored 
 * in a directory) along with a list of largest files. 
 * 
 * @author Joanna Klukowska
 * @version 10-31-2023 
 */
 
public class DiskUsage {
	
	/**
     * This program expects two command line arguments.
     * @param args <code>args[0]</code> is the name of the directory 
     *             to explore, 
     *             <code>args[1]</code> is an optional argument that 
     *             can be used to indicate how many files should be 
     *             displayed in the list of largest files (the default 
     *             value is 20)
     */ 
	public static void main(String[] args) throws IOException{
		
        //make sure that there is at least one command line argument
		if (args.length == 0) {
			System.err.println("Missing name of the directory/file.\n");
			System.exit(0);
		}
		// use the directory from args[0]
		String directory = args[0];
		FileOnDisk dir = new FileOnDisk(directory);
		if ( !dir.exists() ) {
			System.out.printf("ERROR: %s does not exist.\n", directory); 
			System.exit(1);
		}
        
        int numOfFiles = 20; 
        // if args[1] contains a valid positive number, use it 
        // as the number of files to display 
        if (args.length == 2) {
            try {
                numOfFiles = Integer.parseInt(args[1]);
                numOfFiles = numOfFiles > 0 ? numOfFiles : 20;  
            }
            catch (NumberFormatException ex ) {
                //ignoring the second argument, using 20 as 
                //the number of files to display 
            }
        }
		
        // show the total size of the directory and its path 
		System.out.println( dir.toString() );
		
		
		// show the list of largest files (from largest to smallest) 
		
		System.out.printf("Largest %d files: \n", numOfFiles );
		
        List<FileOnDisk> list = dir.getLargestFiles(numOfFiles);
		
		for (FileOnDisk f : list ) {
			System.out.println( f );
        }
		
	}
    
}
