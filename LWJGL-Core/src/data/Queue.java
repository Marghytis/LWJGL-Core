package data;

import java.lang.reflect.Array;

public class Queue<T> {
	public Node leftEnd, rightEnd;
	public int length;
	
	public void enqueue(T data){
		length++;
		Node newNode = new Node(data);
		if(length != 0){
			rightEnd.next = newNode;
			rightEnd = newNode;
		} else {
			leftEnd = newNode;
			rightEnd = newNode;
		}
	}
	
	public T dequeue(){
		if(length != 0){
			T data = leftEnd.data;
			leftEnd = leftEnd.next;
			return data;
		} else {
			return null;
		}
	}
	
	public T peak(){
		if(length != 0){
			return leftEnd.data;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray(Class<T> className){
		T[] out = (T[]) Array.newInstance(className, length);
		int i = 0;
		for(Node cursor = leftEnd; cursor != rightEnd; cursor = cursor.next, i++){
			out[i] = cursor.data;
		}
		return out;
	}
	
	public class Node {
		public Node next;
		public T data;
		
		public Node(T data){
			this.data = data;
		}
	}
}