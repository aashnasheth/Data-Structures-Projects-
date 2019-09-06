
public class BitsMain {
	public static void main(String args[]) {
		BitTree bt = new BitTree("jumpingfrog", ".txt");
		bt.encode();
		bt.compress();
		bt.decompress();
		bt.print();
	}
}
