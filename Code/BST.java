public class BST<K extends Comparable<K>, T> implements Map<K,T>{
    private BSTNode<K,T> root;      //the root of the tree
    private BSTNode<K,T> current;   //the current node
    private int size;               //the size of the tree
    private int nbKeyComp;          //the number of key comparisons needed to find a key

    //constructor
    public BST(){
        root = null;
        current = null;
        size = 0;
        nbKeyComp = 0;
    }

    //copy constructor
    public BST(BST<K,T> tree){
        this.root = tree.root;
        this.current = tree.current;
        this.size = tree.size;
        this.nbKeyComp = tree.nbKeyComp;
    }

    //return the size of the tree
    @Override
    public int size() {
        return size;
    }

    //return true if the tree is empty, false otherwise
    @Override
    public boolean empty() {
        return root == null;
    }

    //clear the tree
    @Override
    public void clear() {
        root = null;
        current = null;
        size = 0;
        nbKeyComp = 0;
    }

    //return the data of the current node
    //the tree need to not be empty
    @Override
    public T retrieve() {
        return current.data;
    }

    //update the data of the current node
    //the tree need to not be empty
    @Override
    public void update(T e) {
        current.data = e;
    }

    //find the key and make the currnet the key node if not found the current will not change
    //return true if it is found, false otherwise
    @Override
    public boolean find(K key) {
        nbKeyComp = 0;
        if(!empty() && key != null){                                                              //check if the tree is empty
            CompPair<BSTNode<K,T>,K> foundComp = new CompPair<BSTNode<K,T>,K>(root, root.key);    //save the current node to compare with the key
            CompPair<BSTNode<K,T>,K> keyComp = new CompPair<BSTNode<K,T>,K>(null, key);     //save the key that we need to find to compare it
            while(foundComp.first != null){                                                       //a loop to find the key
                if(keyComp.compareTo(foundComp) == 0){                                            //check if the current node have the same key
                    nbKeyComp++;
                    current = foundComp.first;
                    return true;
                }
                if(keyComp.compareTo(foundComp) > 0){                                            //check if the key is the right of the current node
                    foundComp.first = foundComp.first.right;                                     //move to the right of the current node
                }
                else{                                                                           //move to the left of the current node
                    foundComp.first = foundComp.first.left;
                }
                if(foundComp.first != null){
                    foundComp.second = foundComp.first.key;
                }
                nbKeyComp += 2;
            }
        }
        return false;
    }

    //return the number of key comparisons needed to find a key
    @Override
    public int nbKeyComp(K key) {
        BSTNode<K, T> saveCurr = current;
        if(key != null && !find(key)){
            return nbKeyComp+1;
        }
        current = saveCurr;
        return nbKeyComp;
    }

    //insert a new node to the tree
    @Override
    public boolean insert(K key, T data) {
        if(key != null && data != null){
            BSTNode<K,T> newNode = new BSTNode<K,T>(key, data); //the new node
            if(empty()){                                        //check if the tree is empty
                root = current = newNode;                       //if true the new node will be the root
                size++;
                return true;
            }
            CompPair<BSTNode<K,T>,K> foundComp = new CompPair<BSTNode<K,T>,K>(root, root.key);    //save the current node to compare with the key
            CompPair<BSTNode<K,T>,K> keyComp = new CompPair<BSTNode<K,T>,K>(null, key);     //save the key that we need to find to compare it
            while(foundComp.first != null){                                                       //a loop will end if the current node is null
                if(keyComp.compareTo(foundComp) == 0){                                            //check if the current node have the same key
                    break;
                }
                if(keyComp.compareTo(foundComp) > 0){                                             //if the key is in the right of the current
                    if(foundComp.first.right == null){                                            //if the right of the current is null the new node will be add there
                        foundComp.first.right = newNode;
                        current = foundComp.first.right;
                        size++;
                        return true;
                    }
                    foundComp.first = foundComp.first.right;                                     //otherwise move to the right
                }
                else{                                                                            //if the key is in the left of the current
                    if(foundComp.first.left == null){                                            //if the left of the current is null the new node will be add there
                        foundComp.first.left = newNode;
                        current = foundComp.first.left;
                        size++;
                        return true;
                    }
                    foundComp.first = foundComp.first.left;                                      //otherwise move to the left
                }
                if(foundComp.first != null){
                    foundComp.second = foundComp.first.key;
                }
            }
        }
        return false;
    }

    //remove a node that matches the key given
    @Override
    public boolean remove(K key) {
        if(key != null && !empty()){
            CompPair<BSTNode<K,T>,K> toComp = new CompPair<BSTNode<K,T>,K>(root, root.key);   //save the current node to compare with the key
            CompPair<BSTNode<K,T>,K> keyComp = new CompPair<BSTNode<K,T>,K>(null, key); //save the key that we need to find to compare it and save the parent of the current node
            while(toComp.first != null){                   //the loop will end if we did not find the key
                if(toComp.compareTo(keyComp) > 0){          //check if the key is in the left of the current
                    keyComp.first = toComp.first;
                    toComp.first = toComp.first.left;
                    if(toComp.first != null){
                        toComp.second = toComp.first.key;
                    }
                }
                else if(toComp.compareTo(keyComp) < 0){     //check if the key is in the right of the current
                    keyComp.first = toComp.first;
                    toComp.first = toComp.first.right;
                    if(toComp.first != null){
                        toComp.second = toComp.first.key;
                    }
                }
                else{                                                                   //if we found the key
                    if((toComp.first.left != null) && (toComp.first.right != null)){    //case 3: the node have two childrens
                        BSTNode<K,T> min = toComp.first.right;                          //node to save the smallest node in the right sub tree
                        keyComp.first = toComp.first;
                        while(min.left != null){                                        //a loop to find the smallest node in the right sub tree of the current node
                            keyComp.first = min;
                            min = min.left;
                        }
                        toComp.first.key = min.key;                                   //deleting the node
                        toComp.first.data = min.data;
                        keyComp.second = min.key;
                        toComp.first = min;
                    }
                    //case 2: one child, case 1: no childrens
                    if(toComp.first.left != null){          //the node have the child on the left
                        toComp.first = toComp.first.left;
                    }
                    else{                                   //the node have the child on the right or have no childrens
                        toComp.first = toComp.first.right;
                    }
                    if(keyComp.first == null){              //if there is no parent for the current node
                        root = toComp.first;
                    }
                    else{                                   //to change the parent childrens
                        CompPair<BSTNode<K,T>,K> tmpComp = new CompPair<BSTNode<K,T>,K>(null, keyComp.first.key);
                        if(tmpComp.compareTo(keyComp) > 0){
                            keyComp.first.left = toComp.first;
                        }
                        else{
                            keyComp.first.right = toComp.first;
                        }
                    }
                    current = root;
                    size--;
                    return true;
                }
            }
        }
        return false;
    }

    //return a DLL that have all the keys of the tree sorted in increasing order 
    @Override
    public DLLComp<K> getKeys() {
        DLLComp<K> keysList = new DLLCompImp<K>();
        keys(root,keysList);
        keysList.findFirst();
        return keysList;

    }

    //add all the keys of the tree to keysList sorted increasingly (with in-order traversal)
    private void keys(BSTNode<K,T> t, DLL<K> keysList){
        if(t != null){
            keys(t.left, keysList);
            keysList.insert(t.key);
            keys(t.right, keysList);
        }
    }
}
