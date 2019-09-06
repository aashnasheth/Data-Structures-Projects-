import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitTreeTest {
	static ByteArrayOutputStream outContent; 
	BitTree bitTree = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
    	System.setOut(System.out);
    }
	
	private void initialize()
	{
	}
	
	static private FileReader openFile(String folder, String fileName, String extension) {
        File f = null;
        FileReader fr = null;
        try {
        	String path = folder + fileName + extension;
        	f = new File(path);
        	fr = new FileReader(f);	
        }
        catch (Exception e) {
        	e.printStackTrace();
        }		
        return fr;
	}
	
	static private boolean isTerminationChar(int character, boolean newlineIsTermination) {
		if ((character == -1) || 
			(newlineIsTermination && (character == '\n' || character == '\r'))) {
			return true;
		}
		return false;
	}
	
	static private void compareFiles(String fileName, String extension, String solutionFolder, String solutionExtension, boolean allowEndingNewline) {
		if (solutionExtension == null) {
			solutionExtension = extension;
		}
		FileReader solutionFR = openFile(solutionFolder, fileName, solutionExtension);
		assertTrue("Solution file is missing", solutionFR != null);
		FileReader testFR = openFile("./output/", fileName, extension);
		assertTrue("Test file " + fileName + extension + " not found", testFR != null);
		
		boolean finished = false;
		int position = 0;
		while (!finished) {
			int solutionInt = 0;
			int testInt = 0;
			try {
				solutionInt = solutionFR.read();
				testInt = testFR.read();
			}
			catch (Exception e) {
				assertTrue("Failed to read file " + fileName + extension, false);
			}
			assertTrue("Character at postion " + Integer.toString(position) + "in " + fileName + extension + " is not correct", 
					solutionInt == testInt || 
					(solutionInt == -1 && isTerminationChar(testInt, allowEndingNewline)) ||
					(testInt == -1 && isTerminationChar(solutionInt, allowEndingNewline)));
			position++;
			if (solutionInt == -1 || testInt == -1) {
				finished = true;
			}
		}
	}
	
	private HashMap<String, String> parseHashMap(String folder, String fileName, String extension) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		FileReader hashMapFR = openFile(folder, fileName, extension);
		BufferedReader hashMapBR = new BufferedReader(hashMapFR);
		String hashMapString = hashMapBR.toString();
		String frString = hashMapFR.toString();

		return hashMap;
	}
	
	private static void CheckHistoMap(String fileName) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
		
		compareFiles(fileName, ".bwt.histoMap.txt", "./AnswerFiles/", null, true);
		//HashMap<String, String> map = parseHashMap("./AnswerFiles/", )
	}

	private static void CheckCodesMap(String fileName) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
		
		compareFiles(fileName, ".bwt.codesMap.txt", "./AnswerFiles/", null, true);
	}	
	
	private static void checkBitsString(String fileName) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
			bitTree.compress();
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
		
		compareFiles(fileName, ".bwt.bits.txt", "./AnswerFiles/", null, true);
	
	}	
	
	private static void checkBits(String fileName) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
			bitTree.compress();
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
		
		compareFiles(fileName, ".bwt.bits", "./AnswerFiles/", null, true);
	
	}
	
	private static void checkDecompress(String fileName) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
			bitTree.compress();
			bitTree.decompress();
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
		
		compareFiles(fileName, ".bwt.decoded.txt", "./input/", ".txt", false);
	
	}
	
	private static void checkDecompressFromFile(String fileName) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
			bitTree.compress();
			BitTree bitTree2 = new BitTree(fileName, ".txt");
			bitTree2.decompress();
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
		
		compareFiles(fileName, ".bwt.decoded.txt", "./input/", ".txt", false);
	
	}	
	
	private static void checkInputFileSize(String fileName, double expected) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
			bitTree.compress();
			double size = bitTree.inputFileSize();
			assertTrue("Expected size = " + Double.toString(expected) + " Actual size = " + Double.toString(size), size == expected);
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
	}
	
	private static void checkCompressedFileSize(String fileName, double expected) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
			bitTree.compress();
			double size = bitTree.compressedFileSize();
			assertTrue("Expected size = " + Double.toString(expected) + " Actual size = " + Double.toString(size), size == expected);
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
	}
	
	private static void checkCompressionRatio(String fileName, double expected) {
		try {
			BitTree bitTree = new BitTree(fileName, ".txt");
			bitTree.encode();
			bitTree.compress();
			double size = bitTree.compressionRatio();
			assertTrue("Expected size = " + Double.toString(expected) + " Actual size = " + Double.toString(size), size == expected);
		}
		catch (Exception e) {
			assertTrue("Unexpected exception thrown" + e.getStackTrace(), false);
		}
	}
	
	
	@Test
	public void histoMapTestexample() 
	{
		CheckHistoMap("example");
	}
	
	@Test
	public void histoMapTestexample2() 
	{
		CheckHistoMap("example2");
	}
	
	@Test
	public void histoMapTestexample3() 
	{
		CheckHistoMap("example3");
	}
//--------
	@Test
	public void codesMapTestexample() 
	{
		CheckCodesMap("example");
	}
	
	@Test
	public void codesMapTestexample2() 
	{
		CheckCodesMap("example2");
	}
	
	@Test
	public void codesMapTestexample3() 
	{
		CheckCodesMap("example3");
	}
//-------------------------	
	
	@Test
	public void bitsStringTestexample() 
	{
		checkBitsString("example");
	}
	
	@Test
	public void bitsStringTestexample2() 
	{
		checkBitsString("example2");
	}
	
	@Test
	public void bitsStringTestexample3() 
	{
		checkBitsString("example3");
	}	

	//-------------------------	
	
		@Test
		public void bitsTestexample() 
		{
			checkBits("example");
		}
		
		@Test
		public void bitsTestexample2() 
		{
			checkBits("example2");
		}
		
		@Test
		public void bitsTestexample3() 
		{
			checkBits("example3");
		}
//-------------------------	
		
		@Test
		public void decompressTestexample() 
		{
			checkDecompress("example");
		}
		
		@Test
		public void decompressTestexample2() 
		{
			checkDecompress("example2");
		}
		
		@Test
		public void decompressTestexample3() 
		{
			checkDecompress("example3");
		}
//-------------------------	
		
			
			@Test
			public void decompressFromFileTestexample() 
			{
				checkDecompressFromFile("example");
			}
			
			@Test
			public void decompressFromFileTestexample2() 
			{
				checkDecompressFromFile("example2");
			}
			
			@Test
			public void decompressFromFileTestexample3() 
			{
				checkDecompressFromFile("example3");
			}
				
//------------------			

			@Test
			public void fileSizeDocExample() {
				checkInputFileSize("docExample", 10.0);
			}
		
			@Test
			public void compressedFileSizeDocExample() {
				checkCompressedFileSize("docExample", 4);
			}
		
			@Test
			public void compressionRatioDocExample() {
				checkCompressionRatio("docExample", 0.4);
			}
		
}
