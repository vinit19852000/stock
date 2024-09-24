package tips.com.example.Service;

import tips.com.example.Object.Stock;

public class ThreadProcess implements Runnable{

	int index;
	int end;
	
	public ThreadProcess(int index,int end) {
		this.index=index;
		this.end=end;
	}
	
	@Override
	public void run() {
		
 	   
 	   for(int i=index;i<=end;i++) {
 		   
 		   try {
 			   Stock st=StockService.listStocks.get(i);
 			   String result=StockService.FinalStockData(st);
 			   
 			
 			   if(result.isEmpty()) {
 				   
 			   }else {

 				   result="\""+result.substring(0,result.length()-1)+"\"";
 				   
 				   String name="\""+st.getName()+"\"";
 				   
 				   if(StockService.hmap.containsKey(result)) {
 					  StockService.hmap.put(result, StockService.hmap.get(result)+"|"+name);
 				   }else {
 	 				  StockService.hmap.put(result, name);
 				   }

 			   }
 			   synchronized (StockService.class) {
				
 				   StockService.scanStock.incrementAndGet();
			}
 			   
 		   }catch(Exception e)
 		   {

 			   synchronized (StockService.class) {
 					
 				   StockService.scanStock.incrementAndGet();
			}
 		   }
 	   }
 	   
	}

}
