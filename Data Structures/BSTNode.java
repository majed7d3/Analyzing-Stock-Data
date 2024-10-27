public class BSTNode<K,T> {
    public K key;                       //the key of the node
    public T data;                      //the data of the node
    public BSTNode<K,T> left,right;     //the childrens of the node

    //constructor with set for the key and data
    public BSTNode(K k, T val){
        key = k;
        data = val;
        left = right = null;
    }

    //copy constructor
    public BSTNode(K k, T val,BSTNode<K,T> l, BSTNode<K,T> r){
        key = k;
        data = val;
        left = l;
        right = r;
    }
}
