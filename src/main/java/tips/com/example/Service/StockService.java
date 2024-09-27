package tips.com.example.Service;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.util.Json;
import tips.com.example.Entity.StockData;
import tips.com.example.Object.Stock;
import tips.com.example.Repo.Stockrepo;


@Service
public class StockService {
	
	static int myindex=1;
	@Autowired
	Stockrepo stockrepo;
	

	
	static HashMap<String, String> hmap=new LinkedHashMap<String, String>();
    static List<Stock> listStocks=new ArrayList<Stock>();
	static AtomicInteger scanStock = new AtomicInteger(0); 
 
	static int aa=1;
	static int sum=0;
	public static String getStockData(Stock stock) {
		
    	String id=stock.getId();
	       String urlString = "https://analyze.api.tickertape.in/stocks/scorecard/"+id;
	       
	       StringBuilder response = new StringBuilder();
	       
           
	        try {
	            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
	            
	            // Set request method
	            connection.setRequestMethod("GET");
	            
	            // Set headers
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("accept-language", "en-US,en;q=0.9");
	            connection.setRequestProperty("access-control-request-headers", "accept-version");
	            connection.setRequestProperty("access-control-request-method", "GET");
	            connection.setRequestProperty("origin", "https://www.tickertape.in");
	            connection.setRequestProperty("priority", "u=1, i");
	            connection.setRequestProperty("sec-fetch-dest", "empty");
	            connection.setRequestProperty("sec-fetch-mode", "cors");
	            connection.setRequestProperty("sec-fetch-site", "same-site");
	            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36");

	            // Connect and get response code
	            connection.connect();
	            int responseCode = connection.getResponseCode();
	            
	            if (responseCode == HttpURLConnection.HTTP_OK) { // 200
	                // Read the response
	                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String inputLine;
	                
	                while ((inputLine = in.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                in.close();
	                
	            } else {
	                //System.out.println("GET request failed: " + responseCode+" :"+stock.getName()+" :"+stock.getId());
		        	response.append("No Result");
	            }

	        } catch (Exception e) {
	        	response.append("No Result");
	        }
	        
	        return response.toString();
	        
	        
	}


	public static void main(String[] args) throws IOException, InterruptedException {
		
			   
	
	}
	public static List<Stock> filterStock() throws IOException, InterruptedException{
    	String result=listOfStock();
        
    	
    	    
        
        System.out.println(result);
        Pattern pattern = Pattern.compile("\"sid\":\"[^\"]*\"");
     
         
        HashMap<Integer, String> hm=new LinkedHashMap<Integer, String>();
        Matcher mather=pattern.matcher(result);
        int i=1;
        while(mather.find()) {
        	
        	String id=mather.group();
        	
        	hm.put(i, id);
        	i++;
        }
        
        
        Pattern pattern2 = Pattern.compile("\"name\":\"[^\"]*\"");
        
        
        
        Matcher mather2=pattern2.matcher(result);
        int j=1;
        while(mather2.find()) {
        	
        	String name=mather2.group();
        	
        	
        	hm.put(j, hm.get(j)+","+name);
        	j++;
        }
         return   hm.values().stream().collect(Collectors.toList()).stream().map(l->{
            	String filter=	l.replaceAll("\"name\":","").replaceAll("\"sid\":", "").replaceAll("\"", "");
            	
             String a[]=	filter.split(",");
             
             Stock stock=new Stock();
             stock.setId(a[0]);
             stock.setName(a[1]);
             
             return stock;
            }).collect(Collectors.toList());
	}
	
	public static String gettickerURL(String response,String key) {
		
		
		
		String url="";
		
		
		if(response.isBlank()||response.isEmpty()) {
			return "";
		}
		
	    
        Pattern pattern = Pattern.compile("\"name\":\"[^\"]*\"");
     
         
        HashMap<Integer, String> hm=new LinkedHashMap<Integer, String>();
        Matcher mather=pattern.matcher(response);
        int i=1;
        while(mather.find()) {
        	
        	String id=mather.group();
        	
        	hm.put(i, id);
        	i++;
        }
        
        
        Pattern pattern2 = Pattern.compile("\"slug\":\"[^\"]*\"");
        
        
        
        Matcher mather2=pattern2.matcher(response);
        int j=1;
        while(mather2.find()) {
        	
        	String name=mather2.group();
        	
        	
        	hm.put(j, hm.get(j)+","+name);
        	j++;
        }
    
        
        return "https://www.tickertape.in"+hm.values().stream().collect(Collectors.toList()).stream().map(l->{
        	String filter=	l.replaceAll("\"name\":","").replaceAll("\"slug\":", "").replaceAll("\"", "");
        	
         String a[]=	filter.split(",");
         
         Stock stock=new Stock();
         stock.setId(a[0]);
         stock.setName(a[1]);
         
         return stock;
        }).collect(Collectors.toList()).get(0).getName();
        

		
	}
	
	
	public   String searchapi(String stockname) {
        StringBuilder response = new StringBuilder();

		 try {
	            // Create URL
	            String urlString = "https://api.tickertape.in/search?text="+URLEncoder.encode(stockname, StandardCharsets.UTF_8);;
	            
	            URL url = new URL(urlString);
	            
	            System.out.print(urlString);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

	            // Set request method
	            connection.setRequestMethod("GET");

	            // Set headers
	            connection.setRequestProperty("accept", "application/json, text/plain, */*");
	            connection.setRequestProperty("accept-language", "en-US,en;q=0.9");
	            connection.setRequestProperty("accept-version", "8.14.0");

	         
	            connection.setRequestProperty("priority", "u=1, i");
	            connection.setRequestProperty("sec-ch-ua", "\"Chromium\";v=\"128\", \"Not;A=Brand\";v=\"24\", \"Google Chrome\";v=\"128\"");
	            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36");

	            // Connect and get response code
	            int responseCode = connection.getResponseCode();

	            if (responseCode == HttpURLConnection.HTTP_OK) { // Check for success
	                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	                String inputLine;

	                while ((inputLine = in.readLine()) != null) {
	                    response.append(inputLine);
	                }
	                in.close();

	                // Print the result
	                System.out.println(response.toString());
	            } else {
	            	
	                System.out.println("GET request failed: " + responseCode);
	            }

	        } catch (Exception e) {

	        }

		 return 	 gettickerURL(response.toString(), stockname);
	}
	
    public static String  listOfStock() throws IOException, InterruptedException {
        
        // Create the request body as a JSON string
        String requestBody = """
            {
                "match":{},
                "sortBy":"mrktCapf",
                "sortOrder":-1,
                "project":["subindustry","mrktCapf","lastPrice","apef"],
                "offset":0,
                "count":6000,
                "sids":[]
            }
        """;

        // Create the HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.tickertape.in/screener/query"))
            .header("accept", "application/json, text/plain, */*")
            .header("accept-language", "en-US,en;q=0.9")
            .header("accept-version", "8.14.0")
            .header("content-type", "application/json")
            .header("cookie", "x-lp-tk=your-cookie-here")
            .header("origin", "https://www.tickertape.in")
            .header("sec-fetch-site", "same-site")
            .header("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Mobile Safari/537.36")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        // Create the HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Send the request and handle the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Return the response body
        return response.body();
    }
    
    public static String FinalStockData(Stock stock) {
    	

        String result=getStockData(stock);
        
        
        
        Pattern pattern = Pattern.compile("\"name\":\"([^\"]*)\",\"tag\":\"([^\"]*)\"");
        Matcher mathcher=pattern.matcher(result);
        StringBuilder stringBuilder=new StringBuilder();
        
        if(result.equalsIgnoreCase("No Result")) {
        	
        	return stringBuilder.toString();
        }
       
        while(mathcher.find()) {
      	  
      	  stringBuilder.append( mathcher.group().replaceAll("\"name\":", "").replaceAll("\"tag\":", "").replaceAll("\"", "").split(",")[1]+":");
        }
        
        
        return stringBuilder.toString();
     }
    
    
  public String updateAndSaveLatestData() throws IOException, InterruptedException {

		StockService service=new  StockService();
		HashMap<String,String> a= service.getScannedStock();
		
		
		System.out.println("main output--------------------------");
		System.out.println("total  leangth:"+a.size());
		
		
		HashMap<String,String> b=new LinkedHashMap<String, String>();
		int i=1;
		for(Map.Entry<String,String> map:a.entrySet()) {
			
			 
			if(map.getValue().split(":").length==6) {
				
				b.put(map.getKey(),map.getValue());
				i++;
			}

		}
		
	   HashMap<String,String> sorted=  b.entrySet().stream().sorted( (aa,bb)->{
	    	
	    	    String my[]=  aa.getValue().split(":");
	    	    int result=0;
	    	    
	    	    result=result+(my[0].equalsIgnoreCase("Low")?1:my[0].equalsIgnoreCase("High")?3:2);
	    	    result=result+(my[1].equalsIgnoreCase("High")?1:my[1].equalsIgnoreCase("Low")?3:2);
	    	    result=result+(my[2].equalsIgnoreCase("Low")?1:my[2].equalsIgnoreCase("High")?3:2);
	    	    result=result+(my[3].equalsIgnoreCase("Low")?1:my[3].equalsIgnoreCase("High")?3:2);
	    	    result=result+(my[4].equalsIgnoreCase("Good")?5:my[4].equalsIgnoreCase("Avg")?2:1);
	    	    result=result+(my[5].equalsIgnoreCase("Low")?3:my[5].equalsIgnoreCase("High")?1:2);
	    	    
	    	      
	    	    String my2[]=  bb.getValue().split(":");
	    	    int result2=0;
	    	    
	    	    result2=result2+(my2[0].equalsIgnoreCase("Low")?1:my2[0].equalsIgnoreCase("High")?3:2);
	    	    result2=result2+(my2[1].equalsIgnoreCase("High")?1:my2[1].equalsIgnoreCase("Low")?3:2);
	    	    result2=result2+(my2[2].equalsIgnoreCase("Low")?1:my2[2].equalsIgnoreCase("High")?3:2);
	    	    result2=result2+(my2[3].equalsIgnoreCase("Low")?1:my2[3].equalsIgnoreCase("High")?3:2);
	    	    result2=result2+(my2[4].equalsIgnoreCase("Good")?5:my2[4].equalsIgnoreCase("Avg")?2:1);
	    	    result2=result2+(my2[5].equalsIgnoreCase("Low")?3:my2[5].equalsIgnoreCase("High")?1:2);
	    	    
	    	   return  result<result2?1:result==result2?0:-1;
	    	      
	    }).collect(Collectors.toMap(
	            Map.Entry::getKey,    // Key mapper
	            Map.Entry::getValue,  // Value mapper
	            (oldValue, newValue) -> oldValue,  // Merge function (for duplicates)
	            LinkedHashMap::new    // Collect into LinkedHashMap
	        ));
	   
	   

	   
	   
	   HashMap<String,String> finalmap=new LinkedHashMap<String, String>();
	   
	   sorted.forEach((k,v)->{
		   
		   if(finalmap.containsKey(v)) {
			   
			   finalmap.put(v,finalmap.get(v)+"|"+k);
		   }else {
			   finalmap.put(v,k);
		   }
		   
	   });
	   
	   finalmap.forEach((k,v)->{
		   
		   System.out.println(myindex+":"+k+":"+v);
		   myindex++;
	   });

	   return finalmap.toString();
  }
    
    public  HashMap<String, String>  getScannedStock() throws IOException, InterruptedException {
    	
    	   

    	List<Stock> listofStocks = StockService.filterStock();

    	listStocks=listofStocks;
    	System.out.println("live stock size: " + listofStocks.size());

    	int index = 0;
    	int end = listofStocks.size() - 1;

    	int core = Runtime.getRuntime().availableProcessors();
    	int check = end;
    	int least = 0;

    	// Calculate the least divisible by core
    	int checkTemp = end;
    	while (checkTemp > 0) {
    	    if (checkTemp % core == 0) {
    	        least = checkTemp;
    	        break;
    	    }
    	    checkTemp--;
    	}

    	int newIndex = 0;
    	int newLast = least / core;
    	int remain = end - check;

    	// Create an ExecutorService
    	ExecutorService executorService = Executors.newFixedThreadPool(core + 1);

    	// Submit tasks to the executor
    	for (int i = 1; i <= core; i++) {
    	    executorService.execute(new ThreadProcess(newIndex, newLast));
    	    newIndex++;
    	    newLast += (least / core);
    	}

    	// Submit the remaining tasks
    	executorService.execute(new ThreadProcess(newIndex, end));

    	 executorService.shutdown();
         // Wait for all tasks to complete
         if (!executorService.awaitTermination(5, TimeUnit.MINUTES)) {
             System.err.println("Tasks did not finish in the allotted time.");
         }

         // Now all tasks are complete
         System.out.println("All tasks completed. Now we can run the main thread.");
         
         return StockService.hmap;
    
    }
    
    
    public  HashMap<String, String> convertJsonToMap(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, HashMap.class);
    }
    	   
    
    public String save() {
    	String json=StockService.hmap.toString();
        
        StockData stockData=new StockData();
        stockData.setJsonData(json);
        stockData.setId(1l);
        
        stockrepo.save(stockData);
        
        return json;
    }
    	   


}
