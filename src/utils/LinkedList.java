package utils;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class LinkedList<E extends Comparable<E>> {
	private ListNode<E> head;
	
	public LinkedList() {
		head = null;
	}
	
	public int length() {
		ListNode<E> currentNode = head;
		int cnt = 0;
		
		while(currentNode != null) {
			cnt++;
			currentNode = currentNode.getNextNode();
		}
		
		return cnt;
	}
	
	public void clear() {
		this.head = null;
	}
	
	public boolean isEmpty() {
		return (head == null);
	}
	
	public void showList() {
		ListNode<E> currentNode = head;
		
		while(currentNode != null) {
			System.out.print(currentNode.toString());
			currentNode = currentNode.getNextNode();
		}
		System.out.println();
	}
	
	public E peek() {
		return head.getData();
	}
	
	public E get(int index) {
		ListNode<E> currentNode = head;
		if(index < 0 || index >= length()) {
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
	
	public ListNode<E> getHead(){
		return this.head;
	}
	
	public int indexOf(E key) {
		ListNode<E> currentNode = head;
		int index = 0;
		
		while(currentNode != null) {
			if(currentNode.getData().compareTo(key) == 0) return index;
			currentNode = currentNode.getNextNode();
			index ++;
		}
		
		return -1;
	}
	
	public boolean contains(E key) {
		ListNode<E> currentNode = head;
		
		while(currentNode != null) {
			if(currentNode.getData().compareTo(key) == 0) return true;
			currentNode = currentNode.getNextNode();
		}
		
		return false;
	}
	
	public void addNode(E data) {
		ListNode<E> newNode = new ListNode<>(data, null);
		ListNode<E> currentNode = head;
		
		if(head == null) {
			head = newNode;
		}else {
			while(currentNode.getNextNode() != null) {
				currentNode = currentNode.getNextNode();
			}
			currentNode.setNextNode(newNode);
		}
	}
	
	public void addFrontNode(E data) {
		head = new ListNode<>(data, head);
	}
	
	public void addNodeByPosition(E data, int index) {
		ListNode<E> newNode = new ListNode<>(data, null);
		ListNode<E> currentNode = head;
		
		if(index < 0 || index > length()) {
			System.out.println("Invalid index input. No node is added");
		}else if(index == 0) {
			addFrontNode(data);
		}else if(index == length()) {
			addNode(data);
		}else {
			for(int i = 1; i < index; i++) {
				currentNode = currentNode.getNextNode();
			}
			ListNode<E> temp = currentNode.getNextNode();
			currentNode.setNextNode(newNode);
			newNode.setNextNode(temp);
		}
	}
	
	public void addAll(E[] data) {
		for(int i = 0; i < data.length; i++) {
			addNode(data[i]);
		}
	}
	
	public void delNode() {
		ListNode<E> currentNode = head;
		ListNode<E> previousNode = null;
		
		if(head != null) {
			if(head.getNextNode() == null) {
				clear();
			}else {
				while(currentNode.getNextNode() != null) {
					previousNode = currentNode;
					currentNode = currentNode.getNextNode();
				}
				previousNode.setNextNode(null);
			}
		}else {
			System.out.println("The list is empty");
		}
	}
	
	public void delFrontNode() {
		if(head != null) {
			head = head.getNextNode();
		}else {
			System.out.println("The list is empty");
		}
	}
	
	public void delNodeByPosition(int index) {
		ListNode<E> currentNode = head;
		
		if(head != null) {
			if(index < 0 || index >= length()) {
//				System.out.println("Invalid index input. No node deleted");
			}else if(index == 0) {
				delFrontNode();
			}else if(index == length() - 1) {
				delNode();
			}else {
				for(int i = 1; i < index; i++) {
					currentNode = currentNode.getNextNode();
				}
				ListNode<E> temp = currentNode.getNextNode();
				currentNode.setNextNode(temp.getNextNode());
				temp = null;
			}
		}else {
//			System.out.println("The list is empty");
		}
	}
	
	public void delNode(E data) {
		int index = this.indexOf(data);
		delNodeByPosition(index);
	}
	
	public String toString() {
		ListNode<E> currentNode = head;
		StringBuilder str = new StringBuilder();
		
		while(currentNode != null) {
			str.append(currentNode.getData() + " --> ");
			currentNode = currentNode.getNextNode();
		}
		
		return str.toString();
	}
	
	public LinkedList<E> copyOfSortedList() {
		LinkedList<E> list = new LinkedList<>();
		ListNode<E> currentNode = head;
		
		while(currentNode != null) {
			list.addSortNode(currentNode.getData());
			currentNode = currentNode.getNextNode();
		}
		return list;
	}
	
	//==============================================================================================================================================================================================//
	
	public void addSortNode(E data) {
		ListNode<E> newNode = new ListNode<>(data, null);
		ListNode<E> currentNode = head;
		int index = 0;
		
		if(head == null) {
			head = newNode;
		}else {
			while(currentNode != null) {
				if(currentNode.getData().compareTo(data) == 0) return;
				if(currentNode.getData().compareTo(data) > 0) break;
				index++;
				currentNode = currentNode.getNextNode();
			}
			addNodeByPosition(data, index);
		}
	}
	
	public LinkedList<E>[] splitList(){
		ListNode<E> currentNode = head;
		LinkedList<E>[] split = new LinkedList[2];
		split[0] = new LinkedList<>();
		split[1] = new LinkedList<>();
		
		int currentIndex = 0;
		int half = (length() % 2 == 0) ? length() / 2 : length() / 2 + 1;
		
		while(currentNode != null) {
			if(currentIndex < half) {
				split[0].addNode(currentNode.getData());
			}else {
				split[1].addNode(currentNode.getData());
			}
			currentIndex ++;
			currentNode = currentNode.getNextNode();
		}
		
		return split;
	}
	
	public LinkedList<E>[] alternateList(){
		ListNode<E> currentNode = head;
		LinkedList<E>[] split = new LinkedList[2];
		split[0] = new LinkedList<>();
		split[1] = new LinkedList<>();
		
		int index = 0;
		
		while(currentNode != null) {
			if(index % 2 == 0) {
				split[0].addNode(currentNode.getData());
			}else {
				split[1].addNode(currentNode.getData());
			}
			index++;
			currentNode = currentNode.getNextNode();
		}
		
		return split;
	}
	
	public LinkedList<E> mergeList(LinkedList<E> list_2){
		LinkedList<E> rtn = new LinkedList<>();
		
		ListNode<E> currentNode_1 = head;
		ListNode<E> currentNode_2 = list_2.head;
		
		while(currentNode_1 != null || currentNode_2 != null) {
			if(currentNode_1 != null) {
				rtn.addNode(currentNode_1.getData());
				currentNode_1 = currentNode_1.getNextNode();
			}
			if(currentNode_2 != null) {
				rtn.addNode(currentNode_2.getData());
				currentNode_2 = currentNode_2.getNextNode();
			}
		}
		
		return rtn;
	}
	
	public void reverse() {
		if(head == null) {
			clear();
		}else {
			E data = head.getData();
			head = head.getNextNode();
			reverse();
			this.addNode(data);
		}
	}
	
	public Iterator<E> listIterator() {
		LinkedListIterator iterator = new LinkedListIterator();
		
		while(iterator.hasNext()) {
			if(iterator.next().toString().contains("A")) iterator.remove();
		}
		
		return new LinkedListIterator();
	}
	
	class LinkedListIterator implements Iterator<E> {
		private ListNode<E> currentNode = head;
		
		public boolean hasNext() {
			return (currentNode != null);
		}

		public E next() {
			E data = currentNode.getData();
			currentNode = currentNode.getNextNode();
			return data;
		}
		
		public void remove() {
			delNodeByPosition(indexOf(currentNode.getData()));
		}
	}
}
//=================================================================================================================================================================================================//


