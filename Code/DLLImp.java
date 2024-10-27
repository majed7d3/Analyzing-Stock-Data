public class DLLImp<T> implements DLL<T> {
   protected DLLNode<T> head;    //the head of the DLL
   protected DLLNode<T> current; //the current of the DLL
   protected int size;        //the size of the DLL

   //constructor
   public DLLImp(){
      head = null;
      current = null;
      size = 0;
   }

   //copy constructor
   public DLLImp(DLLImp<T> doubleLL){
      this.head = doubleLL.head;
      this.current = doubleLL.current;
      this.size = doubleLL.size;
   }

   //return the size of the DLL
   @Override
   public int size() {
      return size;
   }

   //return true if the DLL is empty, false otherwise
   @Override
   public boolean empty() {
      return head == null;
   }

   //return true if the current is the last node, false otherwise
   //the DLL need to not be empty
   @Override
   public boolean last() {
      return current.next == null;
   }

   //return true if the current is the first node, false otherwise
   //the DLL need to not be empty
   @Override
   public boolean first() {
      return current == head;
   }

   //move the current to the first node
   //the DLL need to not be empty
   @Override
   public void findFirst() {
      current = head;
   }

   //move the current to the next node
   //the DLL need to not be empty and current not the last node
   @Override
   public void findNext() {
      current = current.next;
   }

   //move the current to the previous node
   //the DLL need to not be empty and current not the first node (the head)
   @Override
   public void findPrevious() {
      current = current.previous;
   }

   //return the data of the current node
   //the DLL need to not be empty
   @Override
   public T retrieve() {
      return current.data;
   }

   //update the current node data
   //the DLL need to not be empty
   @Override
   public void update(T val) {
      current.data = val;
   }

   //add a new node to the DLL and move the current to it and increase the size
   @Override
   public void insert(T val) {
      DLLNode<T> newNode = new DLLNode<T>(val);
      if(head == null){
         head = current = newNode;
      }
      else{
         if(current.next != null){
            current.next.previous = newNode;
         }
         newNode.next = current.next;
         current.next = newNode;
         newNode.previous = current;
         current = newNode;
      }
      size++;
   }

   //remove the current node and move the current to the next node 
   //if the next node does not exist then the currnet move to the head
   //and decrease the size
   @Override
   public void remove() {
      if(empty()){
         return;
      }
      if(current == head){
         head = head.next;
         if(head != null){
            head.previous = null;
         }
      }
      else{   
         current.previous.next = current.next;
         if(current.next != null){
            current.next.previous = current.previous;
         }
      }
      current = current.next;
      if(current == null){
         current = head;
      }
      size--;
   }
   
}
