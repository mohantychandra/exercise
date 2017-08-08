import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
public class Example {
    public static void main(String[] args) {
            String csvFile = "c:\\csv\\test.csv";
            String line = "";
            String cvsSplitBy = ",";
            HashMap<String, String> row = new HashMap();
            HashMap<String, Integer> amount = new HashMap<String,Integer>();
        try{
            /**
             * Assuming The source of input file in csv format and location is hardcoded as "c:\\csv\\test.csv"
             */
            //Read the input file and extract the name value pairs
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            int j=1;  
            int buy=0;
            int sale=0;
            while ((line = br.readLine()) != null) {
                    // use comma as separator
                    String[] token = line.split(cvsSplitBy);
                    for (int i = 0; i < token.length; i++) {
                    //Store the name value pairs in a HashMap
                    row.put(token[i], token[i+1]);
                    i++;
                }
                //Call the method to get the data in hashmap
                amount = getUsdAmount(row);
                //Iterate the HashMap and store sum of everyday outgoing and incoming amount in USD
                Iterator iterator = amount.entrySet().iterator();
                       while (iterator.hasNext()) {
                           Map.Entry me2 = (Map.Entry) iterator.next();
                           if(me2.getKey().equals("BUY")){
                               buy=buy+Integer.parseInt(me2.getValue().toString());
                           }
                           if(me2.getKey().equals("SALE")){
                               sale=sale+Integer.parseInt(me2.getValue().toString());
                           }
                           iterator.remove(); // avoids a ConcurrentModificationException 
                       } 
                j++;
            }
            //Display the report in the console
            if(buy>sale){
                System.out.println("-----------------------Daily Report----------------");
                System.out.println("BUY Amount in USD Settled Outgoing Everyday: "+buy);
                System.out.println("SALE Amount in USD Settled Incoming Everyday: "+sale);
                System.out.println("----------------------------------------------------");
            }else{
                System.out.println("---------------------Daily Report-----------------");
                System.out.println("SALE Amount in USD Settled Outgoing Everyday: "+sale);
                System.out.println("BUY Amount in USD Settled Incoming Everyday: "+buy);
                System.out.println("-------------------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
     
        }
    /**
     * This function will iterate through the data extracted from the csv file and store in the HashMap object usdAmountOfTrade
     * @param row
     * @return usdAmountOfTrade
     */
    public static HashMap getUsdAmount(HashMap<String,String> row){
        //System.out.println("DAY"+DAY);
        HashMap<String,Integer> usdAmountOfTrade = new HashMap<String,Integer>();
        String currency ="";
        String buy="";
        String sale ="";
        Boolean DAY = false;
        Boolean dayForUSD = false;
        int agreedFx=0;
        int units = 0;
        int pricePerUnit=0;
        int UsdBuyAmountOfTrade = 0;
        int UsdSaleAmountOfTrade = 0;
        //Calculate todays date falls within the SUNDAY to THURSEDAY for currency not USD
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_WEEK);
        ArrayList<Integer> daylist = new ArrayList<Integer>();
        daylist.add(Calendar.SUNDAY);
        daylist.add(Calendar.MONDAY);
        daylist.add(Calendar.TUESDAY);
        daylist.add(Calendar.WEDNESDAY);
        daylist.add(Calendar.THURSDAY);
        if (daylist.contains(day)){
                DAY = true;
        }else{
                DAY = false;
        }
        //Calculate todays date falls within the SUNDAY to THURSEDAY for currency as USD
        ArrayList<Integer> daylst = new ArrayList<Integer>();
        daylst.add(Calendar.MONDAY);
        daylst.add(Calendar.TUESDAY);
        daylst.add(Calendar.WEDNESDAY);
        daylst.add(Calendar.THURSDAY);
        daylst.add(Calendar.FRIDAY);
        //System.out.println("day"+day);
        if (daylst.contains(day)){
                dayForUSD = true;
        }else{
                dayForUSD = false;
        }
            
        Iterator it = row.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(pair.getKey().equals("Currency")){
                currency = pair.getValue().toString();
            }
            if(pair.getKey().equals("Trans")){
                buy = pair.getValue().toString();
            }
            if(pair.getKey().equals("Trans")){
                sale = pair.getValue().toString();
            }
            if(pair.getKey().equals("AgreedFx")){
                agreedFx = Integer.parseInt(pair.getValue().toString());
                //System.out.println("agreedFx"+agreedFx);
            }   
            if(pair.getKey().equals("Units")){
                units = Integer.parseInt(pair.getValue().toString());
                //System.out.println("units"+units);
            }   
            if(pair.getKey().equals("Price per unit")){
                pricePerUnit = Integer.parseInt(pair.getValue().toString());
                //System.out.println("pricePerUnit"+pricePerUnit);
            }    
        it.remove(); // avoids a ConcurrentModificationException 
        }
        if((!currency.equalsIgnoreCase("USD"))&&(DAY)){
        if(buy.equalsIgnoreCase("B")){
         UsdBuyAmountOfTrade = (pricePerUnit*units*agreedFx);
         usdAmountOfTrade.put("BUY", UsdBuyAmountOfTrade);
         }
        if(sale.equalsIgnoreCase("S")){
        UsdSaleAmountOfTrade = (pricePerUnit*units*agreedFx);
        usdAmountOfTrade.put("SALE", UsdSaleAmountOfTrade);
        }
        }else if(dayForUSD){
            if(buy.equalsIgnoreCase("B")){
             UsdBuyAmountOfTrade = (pricePerUnit*units);
             usdAmountOfTrade.put("BUY", UsdBuyAmountOfTrade);
             }
            if(sale.equalsIgnoreCase("S")){
            UsdSaleAmountOfTrade = (pricePerUnit*units);
            usdAmountOfTrade.put("SALE", UsdSaleAmountOfTrade);
            }
        }
        return usdAmountOfTrade;
        }
    }
    




