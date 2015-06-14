package data;

import java.lang.reflect.Array;


public class IndexBuffer<T> {

	public Node read, write;
	Object[] array;
	public int capacity;
	
	public IndexBuffer(int capacity){
		this.capacity = capacity;
		array = new Object[capacity];
		Node newNode = new Node(0);
		array[0] = newNode;
		read = newNode;
		write = newNode;
		for(int i = 1; i < capacity; i++){
			write.next = new Node(i);
			array[i] = write.next;
			write.next.previous = write;
			write = write.next;
		}
		write.next = read;
		write.next.previous = write;
		write = write.next;
		if(capacity == 0) (new Exception("Capacity is 0!!!")).printStackTrace();
	}
	
	@SuppressWarnings("unchecked")
	public IndexBuffer(IndexBuffer<T> toCopy){
		capacity = toCopy.capacity;
		array = new Object[capacity];
		Node first = new Node(0);
		first.setData(((Node)toCopy.array[0]).data);
		if(capacity > 0) array[0] = first;
		read = first;
		write = first;
		Node cursor = (Node)toCopy.array[0];
		for(int i = 1; i < toCopy.capacity; i++){
			cursor = cursor.next;
			write.next = new Node(i);
			write.next.setData(cursor.data);
			array[i] = write.next;
			write.next.previous = write;
			write = write.next;
		}
		write.next = read;
		write.next.previous = write;
		write = (Node)array[toCopy.write.index];
		read = (Node)array[toCopy.read.index];
	}
	
	public void enqueue(T data){
		if(data != null){
			if(full()){
				read = read.next;
			}
			write.setData(data);
			write = write.next;
		}
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
		return i >= capacity ? null : (Node) array[i];
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
	
	public String toString(){
		String out = "INDEX_BUFFER[ ";
		for(Node cursor = read; cursor != write; cursor = cursor.next){
			if(cursor.data != null) out += cursor.data.toString() + " , ";
		}
		return out + "]";
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
