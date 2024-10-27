public class DLLCompImp<T extends Comparable<T>> extends DLLImp<T> implements DLLComp<T>{

    //sort the link list in an increasing order if "increasing" is true
    //if "increasing" is fales then it sort in a decreasing order
    //this need not be called if the data (or any one of them) are null
    @Override
    public void sort(boolean increasing) {
        if (empty()) {
            return;
        }
        DLLNode<T> pre = new DLLNode<T>(null); //to save the place of the head node
        pre.next = head;
        head.previous = pre;
        current = pre;
        sort(head, size, increasing);   // call the recursion method sort
        head = current.next;
        head.previous = null;
        pre = null;
        current = head;                 // make the current at the head
    }

    // return the maximum data in the link list
    // the DLL need to not be empty or the data (or any one of them) are null to
    // call this method
    @Override
    public T getMax() {
        CompPair<DLLNode<T>, T> max = new CompPair<DLLNode<T>, T>(head, head.data);                 // to store the maximum node so far
        if (size != 1) {
            int i = 0;
            CompPair<DLLNode<T>, T> mover = new CompPair<DLLNode<T>, T>(head.next, null);    // to store the current node
            do {
                mover.second = mover.first.data;    // store the data of the current node
                if (mover.compareTo(max) > 0) {     // check if mover data is smaller then max data
                    max.second = mover.second;      // if true the maximum will change to the mover node
                    max.first = mover.first;
                }
                mover.first = mover.first.next;     // if false mover will go to the next node
                i++;
            } while (i < size - 1);                 // the loop will stop after the last node
        }
        return max.second;
    }

    // return the minimum data in the link list
    // the DLL need to not be empty or the data (or any one of them) are null to
    // call this method
    @Override
    public T getMin() {
        CompPair<DLLNode<T>, T> min = new CompPair<DLLNode<T>, T>(head, head.data);                 // to store the minimum node so far
        if (size != 1) {
            int i = 0;
            CompPair<DLLNode<T>, T> mover = new CompPair<DLLNode<T>, T>(head.next, null);   // to store the current node
            do {
                mover.second = mover.first.data;    // store the data of the current node
                if (mover.compareTo(min) < 0) {     // check if mover data is smaller then min data
                    min.second = mover.second;      // if true the minimum will change to the mover node
                    min.first = mover.first;
                }
                mover.first = mover.first.next;     // if false mover will go to the next node
                i++;
            } while (i < size - 1);                 // the loop will stop after the last node
        }
        return min.second;
    }

    //a recursion method to sort the DLL
    private void sort(DLLNode<T> top, int length, boolean increasing){
        if (length <= 1) {                          // to check if the split is one node
            return;
        }
        boolean even = (length % 2 == 0);           // a flag to see if the list is even or not
        int midLength = length / 2;                 // to find the middle of the list
        DLLNode<T> mid = mid(top, midLength);       // to find the middle node in the list
        DLLNode<T> preM = mid.previous;             // to save the mid place
        if (even) {                                 // to ckeck if the list is even or not
            sort(mid, midLength, increasing);       // if it is the recursion sort method will be called with the length of the middle of the list
        }
        else {
            sort(mid, midLength + 1, increasing);   // if not the recursion sort method will be called with the length of the middle of the list +1 to move it one node
        }
        mid = preM.next;                                        //to bring back the place of the middle node
        DLLNode<T> preT = top.previous;                         //to save the top place
        sort(top,midLength,increasing);                         //the recursion sort method for the first half
        top = preT.next;                                        //to bring back the place of the top node
        if(even){                                               //to ckeck if the list is even or not
            merge(top,mid,midLength,midLength,increasing);      //if it is the method merge will be called with the mid and top length the same as midLength
        }
        else{
            merge(top,mid,midLength+1,midLength,increasing);    //if not merge will be called with the mid length as midLength with +1 and top length the same as the midLength
        }
    }

    //to fined the middle node of the list
    private DLLNode<T> mid(DLLNode<T> top,int m){
        for(int i = 0; i < m && top.next != null; i++){
            top = top.next;
        }
        return top;
    }

    //to merge two list and be sorted in increasing or decreasing order with "increasing"
    private void merge(DLLNode<T> top,DLLNode<T> mid,int midLength,int topLength, boolean increasing){
        if(top == null | mid == null){                                          //if top node or/and middle node is null
            return;
        }
        CompPair<DLLNode<T>,T> topComp = new CompPair<DLLNode<T>,T>(top, top.data);   //to save and comper to the top node
        CompPair<DLLNode<T>,T> midComp = new CompPair<DLLNode<T>,T>(mid,mid.data);    //to save and comper to the middle node
        DLLNode<T> middleTop = mid;                                                   //to save the place of the current top od the middle list
        boolean newMiddleTop = false;                                                 //a flag to see if the middle change place
        int currTop = 1;                                                              //to count the nodes of the top list that are sorted
        int currMid = 1;                                                              //to count the nodes of the middle list that are sorted
        //a loop that will stop if current top node is out of the list
        //or if current middle node is out of the list, or the current middle node is null
        while(currTop <= topLength && midComp.first != null && currMid <= midLength){
            //if is to comper between top node and middle node
            //if increasing is true then the comparison will be who is smaller
            //if increasing is false then the comparison will be who is bigger
            if((increasing && topComp.compareTo(midComp) > 0) || (!increasing && topComp.compareTo(midComp) < 0)){
                currMid++;
                DLLNode<T> nextMid = midComp.first.next;            //to save the next of the middle node
                //change the place of the current middle top to be in the back of the top node
                if(middleTop.previous != null){
                    middleTop.previous.next = midComp.first.next;
                }
                if(midComp.first.next != null){
                    midComp.first.next.previous = middleTop.previous;
                }
                midComp.first.next = topComp.first;
                middleTop.previous = topComp.first.previous;
                topComp.first.previous = midComp.first;
                if(middleTop.previous != null){
                    middleTop.previous.next = middleTop;
                }
                midComp.first = nextMid;                             //mak the current middle node the next one
                newMiddleTop = false;                                //sense the we added middle node and all nodes before it so we reset that there is new middle top
                middleTop = nextMid;                                 //reset the current middle top
                if(midComp.first != null){                           //to check if this is the last node or not to add the data
                    midComp.second = midComp.first.data;
                }
            }
            //this will do the opposite of the first if
            //but it will check if the current top node get out of the top list 
            //or the current top node is null
            else if(currTop <= topLength && topComp.first != null){
                currTop++;
                DLLNode<T> nextTop = topComp.first.next;            //to save the next of the top node
                //change the place of the current top node to be in the back of the middle node
                if(topComp.first.previous != null){
                    topComp.first.previous.next = topComp.first.next;
                }
                if(topComp.first.next != null){
                    topComp.first.next.previous = topComp.first.previous;
                }
                topComp.first.next = midComp.first;
                topComp.first.previous = midComp.first.previous;
                midComp.first.previous = topComp.first;
                if(topComp.first.previous != null){
                    topComp.first.previous.next = topComp.first;
                }
                topComp.first = nextTop;                             //mak the current top node the next one
                if(currTop <= topLength && topComp.first != null){   //check if the current node is null or not to add the data of the node
                    topComp.second = topComp.first.data;
                }
                if(middleTop.previous != null && !newMiddleTop){     //check if there is no new middle top and the current middle top not null
                    newMiddleTop = true;
                    middleTop = middleTop.previous;                  //if true then change the current middle top
                }
            }
            //if both ifs did not triggered then this will trigger so the loop will not loop for ever
            else{
                currTop++;
                currMid++;
            }
        }
    }
}
