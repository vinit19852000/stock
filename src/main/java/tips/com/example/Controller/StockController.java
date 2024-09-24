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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	
	@PostMapping("save-api-url")
	public ResponseEntity<Object> saveurl() throws Exception{
		
		  HashMap<String, Set<String>> hm=  (HashMap<String, Set<String>>) getCombination().getBody();
		  
		  hm.forEach((k,v)->{
			  
			  
			  v.forEach(e->{
				  
				String url=  service.searchapi(e);
				  
				  url_map.put(e, url);
			  });
		  }
				  
				  
				  
		);
		  
		  return ResponseEntity.ok(url_map);
		  
		           
	}
	
	@GetMapping("/get-all-stock")
	public ResponseEntity<Object> getall() throws IOException, InterruptedException{
		
		
		 String json=service.getScannedStock();
		 
		 StockData stockData=new StockData();
		 stockData.setJsonData(json);

		return ResponseEntity.ok(stockrepo.save(stockData));
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
		HashMap<String,Set<String>>  hmap=new HashMap<String, Set<String>>() ;
 
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
	

	
	@GetMapping("/map")
	public ResponseEntity<Object> getletest(@RequestParam String key) throws Exception{
		
		
		System.out.print("key:"+key);
		
		List<StockData> list=stockrepo.findAll();
		
		String json=list.stream()
	               .max(Comparator.comparing(StockData::getCreatedAt)).get().getJsonData().toString();
		HashMap<String,Set<String>>  hmap=new HashMap<String, Set<String>>() ;
 
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
         
         

    	 System.out.println("\n");
    	 System.out.println(key);
         
         if(!hmap.containsKey(key)) {
        	 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Result Found");
        	 
         }
         
         
         return ResponseEntity.ok(hmap.get(key));
         
         
	}
	
	
	@DeleteMapping("/delete")
	public void delete() {
		stockrepo.deleteAll();
	}
	
	
	@GetMapping("/search")
	
	public ResponseEntity<Object>  get(@RequestParam String stock){
		
		return ResponseEntity.ok(service.searchapi(stock));
	}
	
	@PostMapping("/sample")
	public ResponseEntity<Object> Post() throws IOException, InterruptedException{
		
		
		StockData stockData=new StockData();
		stockData.setJsonData("{1:2,3:4784,3:4}");
		
		
		return ResponseEntity.ok(stockrepo.save(stockData));
		

		
	}
	
	@GetMapping("/get")
	public Object get() {
		
		
		return  stockrepo.findAll();
	}
	
}
