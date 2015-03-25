package data;

import java.lang.reflect.Array;


public class IndexBuffer<T> {

	public Node read, write, first;
	Object[] array;
	public int capacity;
	
	public IndexBuffer(int capacity){
		array = new Object[capacity];
		first = new Node(0);
		array[0] = first;
		read = first;
		write = first;
		for(int i = 1; i < capacity; i++){
			write.next = new Node(i);
			array[i] = write.next;
			write.next.previous = write;
			write = write.next;
		}
		write.next = read;
		write.next.previous = write;
		write = write.next;
		
	}
	
	public IndexBuffer(IndexBuffer<T> toCopy){
		first = toCopy.first;
		read = first;
		write = first;
		Node cursor = first;
		for(int i = 1; i < toCopy.capacity; i++){
			cursor = cursor.next;
			write.next = new Node(i);
			write.next.setData(cursor.data);
			write.next.previous = write;
			write = write.next;
		}
		write.next = read;
		write.next.previous = write;
		write = write.next;
	}
	
	public void enqueue(T data){
		if(full()){
			read = read.next;
		}
		write.setData(data);
		write = write.next;
	}
	
	public T dequeue(){
		T data = read.data;
		if(!empty()){
			read.setData(null);
			read = read.next;
		}
		return data;
	}
	
	public void clear(){
		while(!empty()){
			dequeue();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Node get(int i){
		return (Node) array[i];
	}
	
	public boolean full(){
		return read.data != null && read == write;
	}
	
	public boolean empty(){
		return read.data == null && read == write;
	}
	
	public IndexBuffer<T> copy(){
		return new IndexBuffer<>(this);
	}
	
	public T[] toArray(Class<T> className){
		@SuppressWarnings("unchecked")
		T[] out = (T[]) Array.newInstance(className, capacity);
		for(int i = 0; i < capacity; i++){
			out[i] = get(i).data;
		}
		return out;
	}
	
	public class Node {
		public T data;
		public Node next, previous;//previous is needed in some applications
		public int index;
		
		public Node(int index){
			this.index = index;
		}
		
		public void setData(T data){
			this.data = data;
		}
	}
}
