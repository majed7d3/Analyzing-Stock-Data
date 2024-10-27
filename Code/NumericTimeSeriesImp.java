public class NumericTimeSeriesImp extends TimeSeriesImp<Double> implements NumericTimeSeries {

    //return a Numeric Time Series that have the Moving Average base on the based on the period given
    @Override
    public NumericTimeSeries calculateMovingAverage(int period){
        TimeSeriesImp<Double> avg = new NumericTimeSeriesImp();     //a list for the moving average
		if(empty() || period <= 0 || period > DPList.size()) {      //check if the time series is empty, or period is 0 or less, or period is bigger then the size of the time series
			return (NumericTimeSeries) avg;                         //if true it return an empty list
		}
        DLL<DataPoint<Double>> sortedList = new DLLImp<DataPoint<Double>>();    //a list to add the data
        DLL<DataPoint<Double>> dataDelete = new DLLImp<DataPoint<Double>>();    //a list to delete the first data
        sortedList = getAllDataPoints();
        dataDelete = getAllDataPoints();
        sortedList.findFirst();
        dataDelete.findFirst();
        Double sum = 0.0;                                                       //the sum of the data
        for(int j = 0; j < period; j++){                                        //a loop for the sum of the first average
            sum += sortedList.retrieve().value;
            if(j+1 < period){
                sortedList.findNext();
            }
        }
        do{
            DataPoint<Double> data = new DataPoint<Double>(sortedList.retrieve().date, sum/period);
            sum -= dataDelete.retrieve().value;                                 //delete the first data in the period
            dataDelete.findNext();                                              
            if(!sortedList.last()){
                sortedList.findNext();
            }
            sum += sortedList.retrieve().value;                                 //add the last data in the period
            avg.addDataPoint(data);
        }while(!sortedList.last());                                             //a loop to add the moving average
        DataPoint<Double> data = new DataPoint<Double>(sortedList.retrieve().date, sum/period);
        avg.addDataPoint(data);                                                 //add the last average (if it is already in avg it will not be add)
        return (NumericTimeSeries) avg;
    }

    //return the maximum Data Point based on the value of it
	@Override
	public DataPoint<Double> getMax() {
		if(!empty()){
            DLLCompImp<CompPair<DataPoint<Double>,Double>> sortedList = new DLLCompImp<CompPair<DataPoint<Double>,Double>>();
            DPList.findFirst();
            while(!DPList.last()){
                CompPair<DataPoint<Double>,Double> newDP = new CompPair<DataPoint<Double>,Double>(DPList.retrieve(), DPList.retrieve().value);
                sortedList.insert(newDP);
                DPList.findNext();
            }
            CompPair<DataPoint<Double>,Double> newDP = new CompPair<DataPoint<Double>,Double>(DPList.retrieve(), DPList.retrieve().value);
            sortedList.insert(newDP);
            return sortedList.getMax().first;
        }
        return null;
	}

    //return the minimum Data Point based on the value of it
	@Override
	public DataPoint<Double> getMin() {
        if(!empty()){
            DLLCompImp<CompPair<DataPoint<Double>,Double>> sortedList = new DLLCompImp<CompPair<DataPoint<Double>,Double>>();
            DPList.findFirst();
            while(!DPList.last()){
                CompPair<DataPoint<Double>,Double> newDP = new CompPair<DataPoint<Double>,Double>(DPList.retrieve(), DPList.retrieve().value);
                sortedList.insert(newDP);
                DPList.findNext();
            }
            CompPair<DataPoint<Double>,Double> newDP = new CompPair<DataPoint<Double>,Double>(DPList.retrieve(), DPList.retrieve().value);
            sortedList.insert(newDP);
            return sortedList.getMin().first;
        }
        return null;
	}
}
