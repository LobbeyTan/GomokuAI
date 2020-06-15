package utils;

public class Stack<T extends Comparable<T>> {
	private StackListNode<T> head;
	
	public Stack() {
		head = null;
	}
	
	public int size() {
		StackListNode<T> currentNode = head;
		int cnt = 0;
		
		while(currentNode != null) {
			cnt++;
			currentNode = currentNode.getNextNode();
		}
		
		return cnt;
	}
	
	public void push(T data) {
		head = new StackListNode<>(data, head);
	}
	
	public T pop() {
		if(isEmpty()) return null;
		
		StackListNode<T> temp = head;
		head = head.getNextNode();
		
		return temp.getData();
	}
	
	public T peek() {
		if(isEmpty()) return null;
		
		return head.getData();
	}
	
	public void display() {
		StackListNode<T> currentNode = head;
		
		while(currentNode != null) {
			System.out.print(currentNode.toString());
			currentNode = currentNode.getNextNode();
		}
		System.out.println();
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		StackListNode<T> currentNode = head;
		
		while(currentNode != null) {
			str.append(currentNode.getData());
			currentNode = currentNode.getNextNode();
		}
		
		return str.toString();
	}
	
	public boolean isEmpty() {
		return (head == null);
	}
	
	public void clear() {
		head = null;
	}
	
	public boolean contains(T data) {
		StackListNode<T> currentNode = head;
		
		while(currentNode != null) {
			if(currentNode.getData().compareTo(data) == 0) return true;
			currentNode = currentNode.getNextNode();
		}
		return false;
	}
	
	public int indexOf(T key) {
		StackListNode<T> currentNode = head;
		int index = 0;
		
		while(currentNode != null) {
			if(currentNode.getData().compareTo(key) == 0) return index;
			currentNode = currentNode.getNextNode();
			index ++;
		}
		
		return -1;
	}
	
	public T get(int index) {
		StackListNode<T> currentNode = head;
		if(index < 0 || index >= size()) {
			System.out.println("Invalid index input");
		}else if(head != null) {
			for(int i = 0; i < index; i++) {
				currentNode = currentNode.getNextNode();
			}
			return currentNode.getData();
		}else {
			System.out.println("The list is empty");
		}
		return null;
	}
	
	public void addNode(T data) {
		StackListNode<T> newNode = new StackListNode<>(data, null);
		StackListNode<T> currentNode = head;
		
		if(head == null) {
			head = newNode;
		}else {
			while(currentNode.getNextNode() != null) {
				currentNode = currentNode.getNextNode();
			}
			currentNode.setNextNode(newNode);
		}
	}
	
	public void addNodeByPosition(T data, int index) {
		StackListNode<T> newNode = new StackListNode<>(data, null);
		StackListNode<T> currentNode = head;
		
		if(index < 0 || index > size()) {
			System.out.println("Invalid index input. No node is added");
		}else if(index == 0) {
			head = new StackListNode<>(data, head);
		}else {
			for(int i = 1; i < index; i++) {
				currentNode = currentNode.getNextNode();
			}
			StackListNode<T> temp = currentNode.getNextNode();
			currentNode.setNextNode(newNode);
			newNode.setNextNode(temp);
		}
	}
	
	public void delNode(T data) {
		StackListNode<T> currentNode = head;
		StackListNode<T> previousNode = null;
		
		if(head == null) return;
		if(head.getNextNode() == null) {clear(); return;}
			
		while(currentNode != null) {
			if(currentNode.getData().compareTo(data) == 0) break;
			previousNode = currentNode;
			currentNode = currentNode.getNextNode();
		}
		if(currentNode == null) return;
		if(previousNode == null) {head = head.getNextNode(); return;}
		
		previousNode.setNextNode(currentNode.getNextNode());
		currentNode = null;
	}
	
	public Stack<T> copyOf() {
		Stack<T> stack = new Stack<>();
		for(int i = size()-1; i >= 0; i--) {
			stack.push(this.get(i));
		}
		return stack;
	}
}

