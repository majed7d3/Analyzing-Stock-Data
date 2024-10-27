import java.util.Date;
public class StockDataSetAnalyzerImp implements StockDataSetAnalyzer {
	private StockHistoryDataSet stockDataset;
	
    //constructor
	public StockDataSetAnalyzerImp(){
		stockDataset = new StockHistoryDataSetImp();
	}

    public StockDataSetAnalyzerImp(StockDataSetAnalyzerImp dataset){
		this.stockDataset = dataset.stockDataset;
	}

    //return the stock dataset
	@Override
	public StockHistoryDataSet getStockHistoryDataSet() {
        StockHistoryDataSet dataset = new StockHistoryDataSetImp((StockHistoryDataSetImp) stockDataset);
		return dataset; 
	}

    //set the stock dataset
	@Override
	public void setStockHistoryDataSet(StockHistoryDataSet stockHistoryDataSet) {
        StockHistoryDataSet newDataset = new StockHistoryDataSetImp((StockHistoryDataSetImp) stockHistoryDataSet);
		stockDataset = newDataset;
	}

    //return a list of company codes sorted according to their stock performance between start and end dates
    //return an empty list if startDate or endDate is null
    //stockDataset need to not be empty to call this method
	@Override
    public DLLComp<CompPair<String, Double>> getSortedByPerformance(Date startDate, Date endDate){
        DLLComp<CompPair<String, Double>> performances = new DLLCompImp<CompPair<String, Double>>();        //a list of the performance of the companys
        if(startDate == null || startDate == null){
            return performances;
        }
        BST<String,StockHistory> companys = (BST<String,StockHistory>) stockDataset.getStockHistoryMap();   //a list of the companys data
        DLLCompImp<String> codes = (DLLCompImp<String>) stockDataset.getAllCompanyCodes();                  //a list of companys codes
        codes.findFirst();
        while(!codes.last()){                                                                               //a loop to add the performance of each company
            companys.find(codes.retrieve());                                                                //to find a company
            TimeSeries<StockData> timeSerie = companys.retrieve().getTimeSeries();                          //the time serie of the company
            Double SData = timeSerie.getDataPoint(startDate).close;                                         //save the start date closing price
            Double EData = timeSerie.getDataPoint(endDate).close;                                           //save the end date closing price
            Double per = (EData-SData)/SData;                                                               //to find the performance of the stock
            CompPair<String, Double> stockPer = new CompPair<String,Double>(codes.retrieve(), per);         //the stock performance of the company
            performances.insert(stockPer);
            if(!codes.last()){
                codes.findNext();
            }
        }
        //to add the last company
        companys.find(codes.retrieve());
        TimeSeries<StockData> timeSerie = companys.retrieve().getTimeSeries();
        Double SData = timeSerie.getDataPoint(startDate).close;
        Double EData = timeSerie.getDataPoint(endDate).close;
        Double per = (EData-SData)/SData;
        CompPair<String, Double> stockPer = new CompPair<String,Double>(codes.retrieve(), per);
        performances.insert(stockPer);
        //sort the list
		performances.sort(false);
        return performances;
    }

    //return a list of company codes sorted according to their volume between start and end dates
    //stockDataset need to not be empty to call this method
	@Override
	public DLLComp<CompPair<String, Long>> getSortedByVolume(Date startDate, Date endDate){
        DLLComp<CompPair<String, Long>> volumes = new DLLCompImp<CompPair<String, Long>>();                                         //a list of the total volume of each company
        BST<String,StockHistory> companys = (BST<String,StockHistory>) stockDataset.getStockHistoryMap();                           //a list of the companys data
        DLLCompImp<String> codes = (DLLCompImp<String>) stockDataset.getAllCompanyCodes();                                          //a list of companys codes
        codes.findFirst();
        while(!codes.last()){                                                                                                       //a loop to add the total volume of each company
            companys.find(codes.retrieve());                                                                                        //to find the company
            DLL<DataPoint<StockData>> dataPoints = companys.retrieve().getTimeSeries().getDataPointsInRange(startDate, endDate);    //a list of all the data points of the company in the range
            dataPoints.findFirst();
            long sum = 0;
            //a loop to add all the volume
            while(!dataPoints.last()){
                sum += dataPoints.retrieve().value.volume;
                if(!dataPoints.last()){
                    dataPoints.findNext();
                }
            }
            //add the sum in the list
            sum += dataPoints.retrieve().value.volume;
            CompPair<String,Long> total = new CompPair<String,Long>(codes.retrieve(), sum);
            volumes.insert(total);
            if(!codes.last()){
                codes.findNext();
            }
        }
        //to add the last company
        companys.find(codes.retrieve());
        DLL<DataPoint<StockData>> dataPoints = companys.retrieve().getTimeSeries().getDataPointsInRange(startDate, endDate);
        dataPoints.findFirst();
        long sum = 0;
        while(!dataPoints.last()){
            sum += dataPoints.retrieve().value.volume;
            if(!dataPoints.last()){
                dataPoints.findNext();
            }
        }
        sum += dataPoints.retrieve().value.volume;
        CompPair<String,Long> total = new CompPair<String,Long>(codes.retrieve(), sum);
        volumes.insert(total);
        //sort the list
        volumes.sort(false);
        return volumes;
    }

    //return a list of company codes sorted by the maximum single day price increase between start and end dates
    //stockDataset need to not be empty to call this method
	@Override
	public DLLComp<CompPair<Pair<String, Date>, Double>> getSortedByMSDPI(Date startDate, Date endDate){
        DLLComp<CompPair<Pair<String, Date>, Double>> msdpi = new DLLCompImp<CompPair<Pair<String, Date>, Double>>();               //a list of the price increase of each company
        BST<String,StockHistory> companys = (BST<String,StockHistory>) stockDataset.getStockHistoryMap();                           //a list of the companys data
        DLLCompImp<String> codes = (DLLCompImp<String>) stockDataset.getAllCompanyCodes();                                          //a list of companys codes
        codes.findFirst();
        while(!codes.last()){                                                                                                       //a loop to calculate the price increase
            companys.find(codes.retrieve());                                                                                        //to find the company
            DLL<DataPoint<StockData>> dataPoints = companys.retrieve().getTimeSeries().getDataPointsInRange(startDate, endDate);    //a list of all the data points of the company in the range
            DLLCompImp<CompPair<Pair<String,Date>,Double>> increase = new DLLCompImp<CompPair<Pair<String, Date>,Double>>();        //a list of all the price increase of the company
            dataPoints.findFirst();
            while(!dataPoints.last()){                                                                                              //a loop to add all the price increase of the company in the range
                //point is to calculate the price increase and save the date of that day and the save the company code
                CompPair<Pair<String, Date>,Double> point = new CompPair<Pair<String,Date>,Double>(new Pair<String,Date>(codes.retrieve(),dataPoints.retrieve().date),(dataPoints.retrieve().value.close - dataPoints.retrieve().value.open)/dataPoints.retrieve().value.open);
                increase.insert(point);
                if(!dataPoints.last()){
                    dataPoints.findNext();
                }
            }
            //to add the last price increase
            CompPair<Pair<String, Date>,Double> point = new CompPair<Pair<String,Date>,Double>(new Pair<String,Date>(codes.retrieve(),dataPoints.retrieve().date),(dataPoints.retrieve().value.close - dataPoints.retrieve().value.open)/dataPoints.retrieve().value.open);
            increase.insert(point);
            //maxIncrease is to find the maximum price increase
            CompPair<Pair<String, Date>, Double> maxIncrease = new CompPair<Pair<String,Date>,Double>(increase.getMax().first, increase.getMax().second);
            msdpi.insert(maxIncrease);
            if(!codes.last()){
                codes.findNext();
            }
        }
        //to add the last company
        companys.find(codes.retrieve());
        DLL<DataPoint<StockData>> dataPoints = companys.retrieve().getTimeSeries().getDataPointsInRange(startDate, endDate);
        DLLCompImp<CompPair<Pair<String,Date>,Double>> increase = new DLLCompImp<CompPair<Pair<String, Date>,Double>>();
        dataPoints.findFirst();
        while(!dataPoints.last()){
            CompPair<Pair<String, Date>,Double> point = new CompPair<Pair<String,Date>,Double>(new Pair<String,Date>(codes.retrieve(),dataPoints.retrieve().date),(dataPoints.retrieve().value.close - dataPoints.retrieve().value.open)/dataPoints.retrieve().value.open);
            increase.insert(point);
            if(!dataPoints.last()){
                dataPoints.findNext();
            }
        }
        //to add the last price increase
        CompPair<Pair<String, Date>,Double> point = new CompPair<Pair<String,Date>,Double>(new Pair<String,Date>(codes.retrieve(),dataPoints.retrieve().date),(dataPoints.retrieve().value.close - dataPoints.retrieve().value.open)/dataPoints.retrieve().value.open);
        increase.insert(point);
        //maxIncrease is to find the maximum price increase
        CompPair<Pair<String, Date>, Double> maxIncrease = new CompPair<Pair<String,Date>,Double>(increase.getMax().first, increase.getMax().second);
        msdpi.insert(maxIncrease);
        //sort the msdpi list
        msdpi.sort(false);
        return msdpi;
    }
	
}


