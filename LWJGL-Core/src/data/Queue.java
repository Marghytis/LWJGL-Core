package data;

public class Queue<T> {
	public Node leftEnd, rightEnd;
	
	public void enqueue(T data){
		Node newNode = new Node(data);
		if(leftEnd != null){
			rightEnd.next = newNode;
			rightEnd = newNode;
		} else {
			leftEnd = newNode;
			rightEnd = newNode;
		}
	}
	
	public T dequeue(){
		if(leftEnd != null){
			T data = leftEnd.data;
			leftEnd = leftEnd.next;
			return data;
		} else {
			return null;
		}
	}
	
	public T peak(){
		if(leftEnd != null){
			return leftEnd.data;
		} else {
			return null;
		}
	}
	
	public class Node {
		public Node next;
		public T data;
		
		public Node(T data){
			this.data = data;
		}
	}
}