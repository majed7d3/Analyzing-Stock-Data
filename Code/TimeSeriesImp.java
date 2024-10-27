import java.util.Date;
public class TimeSeriesImp<T> implements TimeSeries<T> {
    protected DLLImp<DataPoint<T>> DPList;  //a DLL thet store Data Points of the time series

    //constructor
    public TimeSeriesImp(){
        DPList = new DLLImp<DataPoint<T>>();
    }

    //copy constructor
    public TimeSeriesImp(TimeSeriesImp<T> timeSerie){
        DPList = new DLLImp<DataPoint<T>>(timeSerie.DPList);
    }

    //return the size of the Time Series
    @Override
    public int size() {
        return DPList.size();
    }

    //return true if the Time Series is empty, false otherwise
    @Override
    public boolean empty() {
        return DPList.empty();
    }

    //return the data of a Data Point that match the date that is given
    //return null if it did not found it or DPList is empty
    @Override
    public T getDataPoint(Date date) {
        if(!empty() && date != null){
            DPList.findFirst();                                                                     
            CompPair<T,Date> dateComp = new CompPair<T,Date>(null, date);                     //to store the date that is given 
            CompPair<T,Date> DPLComp = new CompPair<T,Date>(null, DPList.retrieve().date);    //to store current data point date
            for(int i = 0; i < DPList.size(); i++){                                                 //a loop to find the data point
                if(dateComp.compareTo(DPLComp) == 0){                                               //to check if the dates match or not
                    return DPList.retrieve().value;
                }
                if(!DPList.last()){                                                                 
                    DPList.findNext();
                    DPLComp.second = DPList.retrieve().date;
                }
            }
        }
        return null;
    }

    //return a DLL with all dates of the DPList sorted increasingly
    //return an empty DLL if DPList is empty
    @Override
    public DLL<Date> getAllDates() {
        DLLCompImp<Date> sortedList = new DLLCompImp<Date>();
        DPList.findFirst();
        for(int i = 0; i < DPList.size(); i++){
            sortedList.insert(DPList.retrieve().date);
            DPList.findNext();
        }
        sortedList.sort(true);
        return sortedList;
    }

    //return the minimum date in DPList
    //return null if DPList is empty
    @Override
    public Date getMinDate() {
        if(empty()){
            return null;
        }
        DPList.findFirst();
        DLLCompImp<Date> minDate = new DLLCompImp<Date>();
        for(int i = 0; i < DPList.size(); i++){
            minDate.insert(DPList.retrieve().date);
            DPList.findNext();
        }
        return ((DLLCompImp<Date>) minDate).getMin();
    }

    //return the maximum date in DPList
    //return null if DPList is empty
    @Override
    public Date getMaxDate() {
        if(empty()){
            return null;
        }
        DPList.findFirst();
        DLLCompImp<Date> maxDate = new DLLCompImp<Date>();
        for(int i = 0; i < DPList.size(); i++){
            maxDate.insert(DPList.retrieve().date);
            DPList.findNext();
        }
        return ((DLLCompImp<Date>) maxDate).getMax();
    }

    //add a Data Point if it is not in the DPList
    //return true if the insert is successful, otherwise false
    @Override
    public boolean addDataPoint(DataPoint<T> dataPoint) {
        if(dataPoint != null && getDataPoint(dataPoint.date) == null){   //to check if the date is not in the DPList and the new data point not null
            DPList.insert(dataPoint);
            return true;
        }
        return false;
    }

    //update the Data Point that match the date of the date that is given 
    //return true if the update is successful, otherwise false
    @Override
    public boolean updateDataPoint(DataPoint<T> dataPoint) {
        if(dataPoint != null && getDataPoint(dataPoint.date) != null){   //check if the date is in the DPList and the dataPoint not null
            DPList.update(dataPoint);
            return true;
        }
        return false;
    }

    //remove a Data Point if it is exists
    //return true if the deletion is successful, otherwise false
    @Override
    public boolean removeDataPoint(Date date) {
        if(date != null && getDataPoint(date) != null){             //to check if the date is in the PDList and not null
            DPList.remove();
            return true;
        }
        return false;
    }

    //return a DLL with all Data Point of the DPList that is sorted increasingly
    //return an empty DLL if DPList is empty
    @Override
    public DLL<DataPoint<T>> getAllDataPoints() {
        DLL<DataPoint<T>> DPSorted = new DLLImp<DataPoint<T>>();                                                //a list for the sorted data points
        DLLCompImp<CompPair<DataPoint<T>,Date>> sortedList = new DLLCompImp<CompPair<DataPoint<T>,Date>>();     //a list of the sorted data points
        DPList.findFirst();
        if(!DPList.empty()){
            while(!DPList.last()){                                    //the loop will add all data points to sortedList
                CompPair<DataPoint<T>,Date> newDP = new CompPair<DataPoint<T>,Date>(DPList.retrieve(), DPList.retrieve().date);
                sortedList.insert(newDP);
                DPList.findNext();
            }
            CompPair<DataPoint<T>,Date> newDP = new CompPair<DataPoint<T>,Date>(DPList.retrieve(), DPList.retrieve().date);
            sortedList.insert(newDP);
            sortedList.sort(true);
            while(!sortedList.last()){                                //a loop to add all the sorted data points to DPSorted
                DataPoint<T> sortedDP = sortedList.retrieve().first;
                DPSorted.insert(sortedDP);
                sortedList.findNext();
            }
            DataPoint<T> sortedDP = sortedList.retrieve().first;
            DPSorted.insert(sortedDP);
        }
        return DPSorted;
    }

    //return a Time Series with all Data Points of a pacific period
    //return an empty DLL if range is before all the data points 
    //or after them or startDate > endDate 
    //or the range is between two data points or DPList is empty
    @Override
    public DLL<DataPoint<T>> getDataPointsInRange(Date startDate, Date endDate){
        DLLImp<DataPoint<T>> rangeList = new DLLImp<DataPoint<T>>();    //a list that will have the data points in the range
        DLL<DataPoint<T>> sorted = new DLLImp<DataPoint<T>>();          //a list of the data points sorted
        boolean endIsNull = (endDate == null);                          //a flag for if end date is null or not
        boolean startIsNull = (startDate == null);                      //a flag for if start date is null or not
        sorted = getAllDataPoints();
        sorted.findFirst();
        if(!sorted.empty()){
            CompPair<DLL<DataPoint<T>>, Date> currDate = new CompPair<DLL<DataPoint<T>>,Date>(sorted, sorted.retrieve().date);  //to save the current data points
            CompPair<DLL<DataPoint<T>>, Date> staComp = new CompPair<DLL<DataPoint<T>>,Date>(null, startDate);            //to compare the start date
            CompPair<DLL<DataPoint<T>>, Date> endComp = new CompPair<DLL<DataPoint<T>>,Date>(null, endDate);              //to compare the end Date
            while(!startIsNull && currDate.compareTo(staComp) < 0 && !currDate.first.last()){                 //a loop to make the current data points at the start date
                currDate.first.findNext();
                currDate.second = currDate.first.retrieve().date;
            }
            //to check if start date is after the last data point or end date is before the current data point
            if((!startIsNull && currDate.compareTo(staComp) < 0 && currDate.first.last()) || (!endIsNull && (currDate.compareTo(endComp) > 0))){
                return rangeList;
            }
            //a loop to add data points to rangeList 
            //if endIsNull false then it will add until the data points are out of range 
            //if true then it will add until currDate is the last data points
            do{
                rangeList.insert(currDate.first.retrieve());
                if(!currDate.first.last()){
                    currDate.first.findNext();
                    currDate.second = currDate.first.retrieve().date;
                }
            }while((!endIsNull && (currDate.compareTo(endComp) < 0) && !currDate.first.last()) || (endIsNull && !currDate.first.last()));
            //rangeList have to have more then one node to go in this if
            //if end date is null and the current data point is the last it will add them
            //if end date after the last data point it will add the last data point
            //if end date is the current data point but rangeList has to have more then one data point to add the current data point
            if((DPList.size() > 1) && ((currDate.first.last() && (endIsNull || (currDate.compareTo(endComp) < 0))) || (currDate.compareTo(endComp) == 0))){                   
                rangeList.insert(currDate.first.retrieve()); 
            }
        }
        return rangeList;
    }
}