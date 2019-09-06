/*Aashna Sheth	
 *BitTree 
 *Implemented algorithm to encode, compress, decompress, and decode text files
 *1st Submission 4-4-19 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class BitTree {
	private Map<String, Integer> hmFrequency; // has char and frequency
	private Map<String, String> hmCode; // has char and coded char
	private PriorityQueue<BitTreeNode> pQueue;
	private String fileName;
	private String extention;
	private int numOfChars;
	private int encodedLength;
	private String toPrintFileName;

	public BitTree(String fName, String fExt) {
		hmFrequency = new HashMap<String, Integer>();
		hmCode = new HashMap<String, String>();
		pQueue = new PriorityQueue<BitTreeNode>();
		toPrintFileName = fName;
		fileName = "./input/" + fName + fExt;
		extention = fExt;
		numOfChars = 0;
		encodedLength = 0;
	}

	public void encode() {
		// create maps of frequency per character
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			int c = br.read();
			while (c != -1) {
				numOfChars++;
				if (hmFrequency.containsKey(Character.toString((char) c))) {
					hmFrequency.put(Character.toString((char) c), hmFrequency.get(Character.toString((char) c)) + 1);
				} else {
					hmFrequency.put(Character.toString((char) c), 1);
				}
				c = br.read();
			}
			hmFrequency.put("EOF", 1);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// creating a priority queue of all the characters and their frequency
		Set<String> s = new HashSet<String>();
		s = hmFrequency.keySet();
		for (String key : s) {
			BitTreeNode n = new BitTreeNode(key, hmFrequency.get(key));
			pQueue.add(n);
		}

		// write freqMap to file
		mapToFileInt(hmFrequency, toPrintFileName + ".bwt.histoMap.txt");

		// creating a binary tree of all the frequencies summs THIS IS GOING WRONG
		while (pQueue.size() > 1) {
			BitTreeNode left = pQueue.remove();
			BitTreeNode right = pQueue.remove();
			BitTreeNode newNode = new BitTreeNode(left, right);
			pQueue.add(newNode);
		}

		// traversing the tree to create a map of nodes
		BitTreeNode root = pQueue.remove();
		traverse("", root);

		// write codeMap to file
		mapToFile(hmCode, toPrintFileName + ".bwt.codesMap.txt");
	}

	private void traverse(String code, BitTreeNode node) {
		if (node.getLeft() == null && node.getRight() == null) {
			hmCode.put(node.getKey(), code);

		} else {
			traverse(code + "0", node.getLeft());
			traverse(code + "1", node.getRight());
		}
	}

	private void mapToFile(Map<String, String> map, String fName) {
		try {
			PrintWriter printWriter = new PrintWriter("./output/" + fName);
			printWriter.print(map.toString());
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void mapToFileInt(Map<String, Integer> map, String fName) {
		try {
			PrintWriter printWriter = new PrintWriter("./output/" + fName);
			printWriter.print(map.toString());
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void compress() {
		String encodedFile = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			int c = br.read();
			while (c != -1) {
				encodedFile += hmCode.get(Character.toString((char) c));
				c = br.read();
			}
			encodedFile += hmCode.get("EOF");
		} catch (IOException e) {
			e.printStackTrace();
		}
		encodedLength = encodedFile.length();

		// write encoded string to file
		try {
			PrintWriter printWriter = new PrintWriter("./output/" + toPrintFileName + ".bwt.bits.txt");
			printWriter.print(encodedFile);
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// encode as bits
		BitOutputStream bos = new BitOutputStream("./output/" + toPrintFileName + ".bwt.bits");
		for (int i = 0; i < encodedFile.length(); i++) {
			int bit = Character.getNumericValue(encodedFile.charAt(i));
			bos.writeBit(bit);
		}
		bos.close();
		bos.finalize();
	}

	private HashMap<String, String> mapFromFile() {
		try {
			HashMap<String, String> m = new HashMap<String, String>();
			BufferedReader br = new BufferedReader(new FileReader("./output/" + toPrintFileName + ".bwt.codesMap.txt"));
			String line = br.readLine(); // only reads one line
			String fLine = "";
			while (line != null) {
				fLine = fLine + line + "\n";
				line = br.readLine();
//				if (line != null) {
//					fLine += "\n";
//				}
			}
			fLine = fLine.substring(1, fLine.length() - 1);
			String[] s = fLine.split(",");
			for (String str : s) {
				if (str.contains("=")) {
					int indexOfEquals = str.indexOf("=");
					String character = str.substring(0, indexOfEquals);
					if (!character.equals(" ") && !character.equals("\n") && !character.equals("\t")
							&& !character.equals("  ")) {
						character = character.trim();
					}
					String code = str.substring(indexOfEquals + 1, str.length()).trim();
					m.put(character, code);
				}
			}
			return m;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// compressed well

	public void decompress() {
		// reconstruct map
		HashMap<String, String> m = mapFromFile();

		// reconstruct tree from map
		BitTreeNode root = new BitTreeNode();
		Set<String> s = new HashSet<String>();
		s = m.keySet();
		for (String key : s) {
			BitTreeNode currNode = root;
			String value = m.get(key);
			for (int i = 0; i < value.length(); i++) {
				if (Character.getNumericValue(value.charAt(i)) == 0) {
					if (currNode.getLeft() == null) {
						BitTreeNode newNode = new BitTreeNode();
						currNode.setLeft(newNode);
					}
					currNode = currNode.getLeft();
				} else if (Character.getNumericValue(value.charAt(i)) == 1) {
					if (currNode.getRight() == null) {
						BitTreeNode newNode = new BitTreeNode();
						currNode.setRight(newNode);
					}
					currNode = currNode.getRight();
				}
				if (i == value.length() - 1) {
					currNode.setKey(key);
				}
			}
		}

		// read the encodedString number by number and add to decodedString
		String decodedFile = "";
		BitInputStream bis = new BitInputStream("./output/" + toPrintFileName + ".bwt.bits");
		int bit = bis.readBit();
		BitTreeNode cNode = root;
		while (bit != -1) {
			if (bit == 0) {
				cNode = cNode.getLeft();
			} else if (bit == 1) {
				cNode = cNode.getRight();
			}
			String key = cNode.getKey();

			if (key != null && key.equals("EOF")) {
				writeToDecodedFile(decodedFile); // writes to file
				bis.close();
				return;
			}
			if (key != null) {
				if (key.equals("")) {
					decodedFile += "\n";
				} else if (key.equals("\t")) {
					decodedFile += "\t";
				} else {
					decodedFile += cNode.getKey();
				}
				cNode = root;
			}
			bit = bis.readBit();
		}
		bis.close();
		System.out.println(decodedFile);
	}

	private void writeToDecodedFile(String decodedFile) {
		try {
			PrintWriter printWriter = new PrintWriter("./output/" + toPrintFileName + ".bwt.decoded.txt");
			printWriter.print(decodedFile);
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void print() { // prints metadata
		System.out.println("Filename: " + toPrintFileName + " File Size: " + inputFileSize() + " Compressed File Size: "
				+ compressedFileSize() + " Compression Ratio: " + compressionRatio());
	}

	public int inputFileSize() {
		return numOfChars;
	}

	public int compressedFileSize() {
		return (int) Math.ceil(encodedLength / 8.0);
	}

	public double compressionRatio() {
		return ((double) compressedFileSize()) / inputFileSize();
	}
}
