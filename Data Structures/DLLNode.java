public class DLLNode<T> {
    public T data;              //the data of the node
    public DLLNode<T> next;        //the next node
    public DLLNode<T> previous;    //the previous node

    //constructor
    public DLLNode(){
        data = null;
        next = null;
        previous = null;
    }

    //copy constructor of the data
    public DLLNode(T val){
        data = val;
        next = null;
        previous = null;
    }

    //setters and getters
    public void setData(T data) {
        this.data = data;
    }
    
    public void setNext(DLLNode<T> next) {
        this.next = next;
    }

    public void setPrevious(DLLNode<T> previous) {
        this.previous = previous;
    }

    public T getData() {
        return data;
    }

    public DLLNode<T> getNext() {
        return next;
    }

    public DLLNode<T> getPrevious() {
        return previous;
    }
}
