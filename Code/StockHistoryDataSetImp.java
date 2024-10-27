public class StockHistoryDataSetImp implements StockHistoryDataSet {
    private BST<String,StockHistory> treeOfCompanys;    //the tree of all Stock History of companys

    //constructor
    public StockHistoryDataSetImp(){
        treeOfCompanys = new BST<String,StockHistory>();
    }

    //copy constructor
    public StockHistoryDataSetImp(StockHistoryDataSetImp companys){
        BST<String,StockHistory> newTree = new BST<String,StockHistory>(companys.treeOfCompanys);
        this.treeOfCompanys = newTree;
    }

    //return the number of Company in the tree
    @Override
    public int size() {
        return treeOfCompanys.size();
    }

    //return true if the tree is empty, false otherwise
    @Override
    public boolean empty() {
        return treeOfCompanys.empty();
    }

    //clear the tree
    @Override
    public void clear() {
        treeOfCompanys.clear();
    }

    //return the tree
    @Override
    public Map<String,StockHistory> getStockHistoryMap() {
        Map<String,StockHistory> copy = new BST<String,StockHistory>(treeOfCompanys);
        return copy;
    }

    //return the codes of all the Companys in the tree in a DLL
    @Override
    public DLLComp<String> getAllCompanyCodes() {
        return treeOfCompanys.getKeys();
    }

    //return a Stock History of a pacific company by the code of it
    @Override
    public StockHistory getStockHistory(String companyCode) {
        if(treeOfCompanys.find(companyCode)){   //check if the company code is in the tree
            return treeOfCompanys.retrieve();
        }
        return null;
    }

    //add a Stock History of a company to the tree if it not there
    @Override
    public boolean addStockHistory(StockHistory StockHistory) {
        if(StockHistory != null){
            return treeOfCompanys.insert(StockHistory.getCompanyCode(), StockHistory);
        }
        return false;
    }

    //remove a Stock History of a company if it in the tree
    @Override
    public boolean removeStockHistory(String companyCode) {
        return treeOfCompanys.remove(companyCode);
    }
}