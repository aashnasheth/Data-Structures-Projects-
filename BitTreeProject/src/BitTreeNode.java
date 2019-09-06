
public class BitTreeNode implements Comparable<BitTreeNode> {
	private int value;
	// private int f;
	private String key;
	private BitTreeNode left;
	private BitTreeNode right;

	public BitTreeNode() {
		left = null;
		right = null;
	}

	public BitTreeNode(String chr, int freq) {
		key = chr;
		value = freq;
	}

	public BitTreeNode(BitTreeNode l, BitTreeNode r) {
		left = l;
		right = r;
		value = l.value + r.value;
	}

	// decide how to compare 2 nodes and return proper integer values so you can
	// properly build your priority queue
	public int compareTo(BitTreeNode other) {
		if (this.value < other.value) {
			return -1;
		} else if (this.value > other.value) {
			return 1;
		} else if (this.value == other.value) {
			if (key != null && other.key != null) {
				if (key.equals("EOF")) {
					return -1;
				} else {
					return key.compareTo(other.key);
				}
			}
		}
		return 0;
	}

	public String toString() {
		return ("'" + key + "'" + "=" + value);
	}

	public BitTreeNode getRight() {
		return right;
	}

	public void setRight(BitTreeNode n) {
		right = n;
	}

	public void setKey(String s) {
		key = s;
	}

	public BitTreeNode getLeft() {
		return left;
	}

	public void setLeft(BitTreeNode n) {
		left = n;
	}

	public String getKey() {
		return key;
	}
}
