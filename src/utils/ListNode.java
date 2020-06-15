package utils;

public class ListNode<E> {
	private E data;
	private ListNode<E> nextNode;
	
	public ListNode(E data, ListNode<E> nextNode) {
		this.data = data;
		this.nextNode = nextNode;
	}
	
	public E getData() {
		return this.data;
	}
	
	public ListNode<E> getNextNode(){
		return this.nextNode;
	}
	
	public void setData(E data) {
		this.data = data;
	}
	
	public void setNextNode(ListNode<E> nextNode) {
		this.nextNode = nextNode;
	}
	
	public String toString() {
		return this.data.toString() + " --> ";
	}
}
