package util;

public class Loop<T> {
	
	public int size;
	public LoopNode leftEnd, rightEnd;

	public Loop(int size){
		this.size = size;
	}
	
	public T appendRight(T data){
		T deleted = leftEnd.data;
		leftEnd.data = data;
		
		rightEnd = leftEnd;
		leftEnd = leftEnd.right;
		
		return deleted;
	}
	
	public T appendLeft(T data){
		T deleted = rightEnd.data;
		rightEnd.data = data;
		
		leftEnd = rightEnd;
		rightEnd = rightEnd.left;
		
		return deleted;
	}

	class LoopNode {
		public LoopNode left, right;
		public T data;
		
		public LoopNode(LoopNode left, LoopNode right){
			this.left = left;
			this.right = right;
		}
	}
}
