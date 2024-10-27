import java.io.*;
import java.text.*;
import java.util.Date;

public class StockDataLoaderImp implements StockDataLoader {

    //return a stock history of a company from a csv file
    //return null if file does not exists or it is not a file or we can not read it or it is not a csv file
    //or the data in the file is not format as (Date,Open,High,Low,Close,Volume)
    //or the date in the file is not format as (yyyy-MM-dd)
    //or the file do not have any data, or there is an empty line, or the data in the line is empty
    @Override
    public StockHistory loadStockDataFile(String fileName){
        StockHistory companyStock = new StockHistoryImp();                                          //the list of the data from the file
        File file = new File(fileName);
        String dataFormat = "Date,Open,High,Low,Close,Volume";                                      //the format of the data in the file
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");                   //the format of the dates in the file
        CompPair<Date,Date> oldDateComp = new CompPair<Date,Date>(null, null);         //to save the last data point date
        CompPair<Date,Date> newDateComp = new CompPair<Date,Date>(null, null);         //to save the current data point date
        String fileFormat = "";                                                                     //the format of the file
        int extension = fileName.lastIndexOf('.');                                               //to find the extension of the file
        if (extension > 0) {
            fileFormat = fileName.substring(extension+1);                                           //to save the extension of the file
        }
        //to check if the file exists, and it is a file, and we can read it                          
        try{
            if(file.exists() && file.isFile() && file.canRead() && (fileFormat.equalsIgnoreCase("csv"))){               
                try{                                                                            //to catch if we did not found the file
                    FileReader reader = new FileReader(file);                                   
                    BufferedReader buffer = new BufferedReader(reader);                         
                    companyStock.SetCompanyCode(file.getName().split(".csv")[0]);         //to set the code of the company by the file name
                    try{                                                                        //to catch any IOException (pacifically if we surpassed the final line) 
                        String line = buffer.readLine();                                        //the line reader
                        if(dataFormat.equalsIgnoreCase(line)){                                  //check if the data Format match
                            for(int i = 0; (line = buffer.readLine()) != null; i++) {           //a loop will stop if there is no more lines
                                String[] dataSplitter = line.split(",");                  //to Splitter the data from the file
                                if(dataSplitter.length <= 1){                                   //to ckeck if the file have one line (meaning there is no data)
                                    return null;
                                }
                                StockData stockData = new StockData(Double.parseDouble(dataSplitter[1]), Double.parseDouble(dataSplitter[4]), Double.parseDouble(dataSplitter[2]), Double.parseDouble(dataSplitter[3]), Long.parseLong(dataSplitter[5]));
                                try{                                                            //to catch ParseException from the Date
                                    Date date = dateFormat.parse(dataSplitter[0]);
                                    newDateComp.second = date;
                                    //if the last date is bigger then the current date it will return null
                                    //or the last date is the same as the current date it will return null
                                    if(i > 0 && ((oldDateComp.compareTo(newDateComp) == 0) || (oldDateComp.compareTo(newDateComp) > 0))){
                                        return null;
                                    }
                                    oldDateComp.second = date;
                                    companyStock.addStockData(date, stockData);                  //add the stock Data to the company stock history
    
                                }catch(ParseException e){
                                    return null;
                                }
                            }
                        }
                        else{
                            return null;
                        }
                    }catch(IOException  e){
                        
                    }
                    finally{
                        try{
                            buffer.close();
                            reader.close();
                        }catch(IOException e){
                            
                        }
                    }
                }catch(FileNotFoundException e){
                    return null;
                }
                if(companyStock.size() != 0){
                    return companyStock;
                }
            }
            return null;
        }catch(Exception e){
            return null;
        }
    }

    //return a Dataset of companys stock history from a directory with csv file
    //return null if the directory does not exists, or directoryPath is not a directory
    //or there is a file that is not a csv file in the directory
    //or if loadStockDataFile return null this method will also return null
    @Override
    public StockHistoryDataSet loadStockDataDir(String directoryPath) {
        StockHistoryDataSet dataset = new StockHistoryDataSetImp();                     //the Stock History Dataset of the directory
        File directory = new File(directoryPath);                                       //the directory
        if(directory.exists() && directory.isDirectory()){                              //check if the directory exists and if the directoryPath is a directory
            String[] files = directory.list();                                          //a list of the files in the directory
            for(int i = 0; i < files.length; i++){                                      //a loop to add all the files to the Dataset
                StockHistory company = loadStockDataFile(directoryPath+"/"+files[i]);
                if(company != null){
                    dataset.addStockHistory(company);
                }
                else{
                    return null;
                }
            }
            return dataset;
        }
        return null;
    }
    
}