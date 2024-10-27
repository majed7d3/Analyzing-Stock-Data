import java.util.Date;
public class StockHistoryImp implements StockHistory {
    private TimeSeriesImp<StockData> TSList;    //a Time Series List thet the data of it is Stock Data
    private String code;                        //the code of the company

    //constructor
    public StockHistoryImp(){
        TSList = new TimeSeriesImp<StockData>();
        this.code = null;
    }

    //constructor with the set of the company code
    public StockHistoryImp(String code){
        TSList = new TimeSeriesImp<StockData>();
        this.code = code;
    }

    //return the size of TSList
    @Override
    public int size() {
       return TSList.size();
    }

    //return true if the TSList is empty, false otherwise
    @Override
    public boolean empty() {
        return TSList.empty();
    }

    //clear the TSList
    @Override
    public void clear() {
        TSList = new TimeSeriesImp<StockData>();
    }

    //return the code of the company
    @Override
    public String getCompanyCode() {
        return code;
    }

    //set the code of the company
    @Override
    public void SetCompanyCode(String companyCode) {
        code = companyCode;
    }

    //return the stock history of the company
    @Override
    public TimeSeries<StockData> getTimeSeries() {
        TimeSeries<StockData> timeSerie = new TimeSeriesImp<StockData>(TSList);
        return timeSerie;
    }

    //return the Stock Data of a pacific date
    //return null if it did not find the Stock Data with that date
    @Override
    public StockData getStockData(Date date) {
        return TSList.getDataPoint(date);
    }

    //add a Stock Data if it is not in the TSList already
    //return true if it is added, false otherwise
    @Override
    public boolean addStockData(Date date, StockData stockData) {
        if(date != null && stockData != null){
            DataPoint<StockData> newDataPoint = new DataPoint<StockData>(date, stockData);
            return TSList.addDataPoint(newDataPoint);
        }
        return false;
    }

    //remove a Stock Data if it is in TSList
    //return true if it is removed, false otherwise
    @Override
    public boolean removeStockData(Date date) {
        return TSList.removeDataPoint(date);
    }
    
}
