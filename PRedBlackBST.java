package hw2;
// THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
// A TUTOR OR CODE WRITTEN BY OTHER STUDENTS - your name
//PRedBlackBST: a persistent left-leaning red-black tree, without delete.
//You need to implement persistent insertion (the "put" method).  If you
//have time, you should also try to implmement the StackIterator (see below).
//Our parent class PBST has already defined some useful things:
//* some PBST methods (like size, contains, get) do not need any change.
//* the Node class already has a 'color' field and a setColor method.
//* the isRed(x) method, like in the book (treats null as black)
//* the check() method, which checks for correct red-black tree structure.
//* all fields are declared final, to support persistence.



public class PRedBlackBST<Key extends Comparable<Key>, Value>
 extends PBST<Key, Value>
{
 // Constructors: these just call the parent class constructors.
 public PRedBlackBST() { super(); }
 PRedBlackBST(Node r) { super(r); }

 // Color constants, just for convenience.
 static final boolean RED   = true;
 static final boolean BLACK = false;

 // The setRoot(Node r) method.  Its declared return type is still
 // PBST, but the actual type returned is PRedBlackBST.  It could
 // return this tree, if nothing has actually changed.
 PBST<Key,Value> setRoot(Node r) {
     return r==root ? this : new PRedBlackBST<Key,Value>(r);
 }

 // TODO: public put method (top level).
 // Should call the recursive put method, make sure the root
 // is black, and then call setRoot.
 // public PBST<Key,Value> put(Key key, Value val) { ... }
 
 public PBST<Key,Value> put(Key k, Value v) { 
	 //Node newroot = new Node(k, v, null, null, BLACK);
	 Node newroot = put(root, k, v);
	 newroot = newroot.setColor(BLACK);
	 return setRoot(newroot);
 }

 // TODO: recursive put (or insert) method.  This should be
 // like the recursive method in share/book/RedBlackBST.java,
 // except that it builds new nodes in a persistent way.
 //
 // private Node put(Node h, Key key, Value val) { ... }

 /*private*/ Node put(Node h, Key key, Value val) { 
     if (h == null) {
         return new Node(key, val, null, null, RED);
     }
     int cmp = key.compareTo(h.key);
     if      (cmp < 0) h = h.setLeft(put(h.left,  key, val)); 
     else if (cmp > 0) h = h.setRight(put(h.right, key, val)); //TODO 
     else              h = h.setVal(val); // TODO

     // fix-up any right-leaning links
     if (isRed(h.right) && !isRed(h.left))      h = rotateLeft(h); 
     if (isRed(h.left)  &&  isRed(h.left.left)) h = rotateRight(h); 
     if (isRed(h.left)  &&  isRed(h.right))     h = flipColors(h); // i changed this part to return a new node

     return h;
 }

 // TODO: persistent version of rotateRight(h), two new nodes.
 // Node rotateRight(Node h) { ... }
 private Node rotateRight(Node h) {
     assert (h != null) && isRed(h.left);
     Node x = h.left;
     //h.left = x.right;
     //x.right = h;
     //x.color = h.color;
     //h.color = RED;
     Node newh = new Node(h.key, h.val, x.right, h.right, RED);
     Node newx = new Node(x.key, x.val, x.left, newh, h.color);
     return newx;
 }
 // TODO: persistent version of rotateLeft(h), two new nodes.
 
 private Node rotateLeft(Node h) {
     assert (h != null) && isRed(h.right);
     Node x = h.right;
     //h.right = x.left;
     //x.left = h;
     //x.color = h.color;
     //h.color = RED;
	Node newh = new Node(h.key, h.val, h.left, x.left, RED);
	Node newx = new Node(x.key, x.val, newh, x.right, h.color);
     return newx;
 }

 // TODO: presistent version of flipColors(h), three new nodes.
 private Node flipColors(Node h) {
     assert !isRed(h) && isRed(h.left) && isRed(h.right);
     //h.color = RED;
     //h.left.color = BLACK;
     //h.right.color = BLACK;
     Node newhLeft  = new Node(h.left.key, h.left.val, h.left.left, h.left.right, BLACK);
     Node newhRight = new Node(h.right.key, h.right.val, h.right.left, h.right.right, BLACK);
     Node newh = new Node(h.key, h.val, newhLeft, newhRight, RED);
     return newh;
     
 }

 // TODO: once you have done the above "TODO"s, you should be
 // able to pass the test performed by Driver.testPRedBlackBST().
 // So, go check on that.

 // TODO: if you have finished the above test, and still have time,
 // then there is one more challenge: replace the IntIterator
 // (inherited from PBST) with a faster StackIterator, below.
 public java.util.Iterator<Key> iterator() { return new StackIterator(); }

 class StackIterator implements java.util.Iterator<Key> {
     java.util.Stack<Node> todo = new java.util.Stack<Node>();
     // TODO: constructor, and the hasNext() and next() methods.
     // I'll give you remove, since we are not supporting deletion:
     
     public StackIterator(){
    	 Node pass = root;
    	 while(pass != null){
    		 todo.push(pass);
    		 pass = pass.left;
    	 }
     }
     
     public void remove() { // we cannot remove
         throw new UnsupportedOperationException();
     }
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return !todo.isEmpty();
	}
	@Override
	public Key next() {
		// TODO Auto-generated method stub
		Node temp = todo.pop();
		if(temp.right != null){
			todo.push(temp.right);
		    if(temp.right.left != null)
			   todo.push(temp.right.left);
		}
		return key(temp);
	}
	
 }

 // Just like IntIterator, a StackIterator lets us visits the keys
 // in order.  It uses a stack of O(H) nodes (where H is height)
 // to keep track of the unvisited parts of the tree.  It turns
 // out this approach is faster, only O(1) amortized time per visit.
 // Like IntIterator, we will not implement the remove() method, just
 // the hasNext() and next() methods.
 //
 // IDEA: for each Node x on the stack, we still need to visit x,
 // followed by all the nodes in its x.right subtree.
 // So, just to construct the initial stack, you should push the
 // "left spine" of the initial tree onto the stack.

 /* Note: this starts a C-style multiline comment!

 // This line uses StackIterator instead of IntIterator:
 public java.util.Iterator<Key> iterator() { return new StackIterator(); }

 class StackIterator implements java.util.Iterator<Key> {
     java.util.Stack<Node> todo = new java.util.Stack<Node>();
     // TODO: constructor, and the hasNext() and next() methods.
     // I'll give you remove, since we are not supporting deletion:
     public void remove() { // we cannot remove
         throw new UnsupportedOperationException();
     }
 }

 * end of the multiline comment */
 
 
}
