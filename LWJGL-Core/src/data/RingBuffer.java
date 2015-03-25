package data;

public class RingBuffer<T> {

	public Node start, end;
	public int realSize;
	
	public RingBuffer(int capacity){
		this.realSize = capacity-1;
		start = new Node();
		end = start;
		for(int i = 0; i < capacity; i++){
			end.next = new Node();
			end = end.next;
		}
		end.next = start;
		end = start;
	}
	
	public void enqueue(T data){
		end.setData(data);
		end = end.next;
		if(end.equals(start)){
			start.data = null;
			start = start.next;
		}
	}
	
	public T dequeue(){
		T data = start.data;
		if(!start.equals(end)){
			start.data = null;
			start = start.next;
		}
		return data;
	}
	
	
	public class Node {
		public T data;
		public Node next;
		
		public Node(){}
		
		public Node(T data){
			this.data = data;
		}
		
		public void setData(T data){
			this.data = data;
		}
	}
}
