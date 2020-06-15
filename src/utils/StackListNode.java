package utils;
public class StackListNode<T> {
	private T data;
	private StackListNode<T> nextNode;
	
	public StackListNode() {
		this.data = null;
		this.nextNode = null;
	}
	
	public StackListNode(T data, StackListNode<T> nextNode) {
		this.data = data;
		this.nextNode = nextNode;
	}
	
	public T getData() {
		return this.data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public StackListNode<T> getNextNode() {
		return this.nextNode;
	}
	
	public void setNextNode(StackListNode<T> nextNode) {
		this.nextNode = nextNode;
	}
	
	public String toString() {
		return " <-- " + this.data;
	}
}
