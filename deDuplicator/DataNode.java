package deDuplicator;

import java.util.LinkedList;

public class DataNode extends LinkedList{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ListNode value;
	DataNode next;
	boolean flag;
	public DataNode() {

	}
	public DataNode(ListNode value) {
		this.value = value;
		this.next = null;
	}
	public void link(DataNode prev) {
		prev.next = this;
	}
}