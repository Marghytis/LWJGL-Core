package data;

@SuppressWarnings("unchecked")
public class TwoEndSnake<T> {

	private Node leftEnd, rightEnd;
	public void appendLeft(T... dates){
		for(int i = 0; i < dates.length; i++){
			leftEnd.left = new Node(dates[i], null, leftEnd);
			leftEnd = leftEnd.left;
		}
	}
	
	public void appendRight(T... dates){
		for(int i = 0; i < dates.length; i++){
			rightEnd.right = new Node(dates[i], null, rightEnd);
			rightEnd = rightEnd.right;
		}
	}
	
	public class Node {
		T data;
		Node left, right;
		
		public Node(T data){
			this.data = data;
		}

		public Node(T data, Node left, Node right){
			this(data);
			this.left = left;
			this.right = right;
		}
	}
	
}
