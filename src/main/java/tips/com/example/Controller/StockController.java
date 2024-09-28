package tips.com.example.Controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import tips.com.example.Entity.StockData;
import tips.com.example.Object.Stock;
import tips.com.example.Repo.Stockrepo;
import tips.com.example.Service.StockService;

@RestController

@RequestMapping("/stock")
public class StockController {

	@Autowired
	StockService service;
	
	
	static HashMap<String, String> url_map=new LinkedHashMap<String, String>();
	
	
	
	@Autowired
	Stockrepo stockrepo;
	
	  @GetMapping("/goto")
	    public String search(@RequestParam String query) {
		  String url = "https://www.google.com/search?q=" + query+" tickertap&btnI=1";
          
	        try {
	            // Create the Google search URL
	            
	            // Fetch the HTML page
	            Document doc = Jsoup.connect(url).get();

	            System.out.println("document:"+doc);
	            
	            // Extract the first <a> (anchor) element
	            Element firstLink = doc.select("a").first(); // Select the first anchor element

	            if (firstLink != null) {
	                String resultUrl = firstLink.attr("href"); // Get the href attribute of the anchor
	                
	                   return resultUrl;
	                // Return or process the URL
	            } else {
	                return url;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
                return url;
	        }
	    }

	  
	  @GetMapping("/list-of-stock")
	  public ResponseEntity<Object> getall() throws IOException, InterruptedException{
	    	List<Stock> listofStocks =  StockService.filterStock();
	    	
	    	return ResponseEntity.ok(listofStocks);

	  }
	
	@PutMapping("/update-stockdata")
	public ResponseEntity<Object> getall(@RequestParam String password) throws IOException, InterruptedException{
		
		
		if(password.equalsIgnoreCase("vinit1985")) {
			 String json=service.updateAndSaveLatestData();
			 
			 StockData stockData=new StockData();
			 stockData.setJsonData(json);

			return ResponseEntity.ok(stockrepo.save(stockData));
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PASSWORD IS WRONG");

	}
	


    @Transactional
	@GetMapping("/date")
	public ResponseEntity<Object> getdate(){
		StockData stockData= stockrepo.findTopByOrderByCreatedAtDesc();
		
		
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

	        // Format the LocalDateTime
	        String formattedDate = stockData.getCreatedAt().format(formatter);

		 return ResponseEntity.ok(formattedDate);
		
	}
	
	
	@GetMapping("/combination")
	public ResponseEntity<Object> getCombination() throws Exception{
		
		
		
		List<StockData> list=stockrepo.findAll();
		
		String json=list.stream()
	               .max(Comparator.comparing(StockData::getCreatedAt)).get().getJsonData().toString();
		HashMap<String,Set<String>>  hmap=new LinkedHashMap<String, Set<String>>();
 
         StringTokenizer st=new StringTokenizer(json,",");
         

         int i=1;
         while(st.hasMoreTokens()) {
        	 
        	 
        	 String data=st.nextToken();

        	 String fullstring=data.replaceAll("\"", "").replaceAll("\\{", "");
        	 
        	 
        	 String value=fullstring.split("=")[1];
             String mykey=fullstring.split("=")[0];
        	 
        	 String array[]=value.trim().split("\\|");
             

             Set<String> set=new HashSet<String>(List.of(array));
             
             hmap.put(mykey.trim(),set);
           
         }
         
         System.out.print("no erorr.........................");
         
         
         hmap.forEach((k,v)->{
        	 
        	 System.out.println(k);
         });
         
         

    
         return ResponseEntity.ok(hmap);
         
         
	}
	

	
	
	@DeleteMapping("/delete")
	public void delete() {
		stockrepo.deleteAll();
	}
	
	
	@GetMapping("/search")
	
	public ResponseEntity<Object>  get(@RequestParam String stock){
		
		return ResponseEntity.ok(service.searchapi(stock));
	}
	

}
