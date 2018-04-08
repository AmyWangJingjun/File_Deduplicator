package deDuplicator;

import java.io.Serializable;
import java.util.LinkedList;

public class ListNode extends LinkedList{
	ListNode next;
	ListNode prev;
	String value;
	boolean flag;
	public ListNode(String value) {
		this.value = value;
		this.next = null;
		this.prev = null;
		this.flag = false;
	}
	public void breakLink() {
		ListNode prev = this.prev;
		prev.next = null;
		this.prev = null;
		prev.flag = true;
	}
	public void link(ListNode prev) {
		prev.next = this;
		this.prev = prev;
	}
}
