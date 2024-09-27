package tips.com.example.Service;


import tips.com.example.Object.Stock;

public class ThreadProcess implements Runnable {

    private final int startIndex;
    private final int endIndex;

    public ThreadProcess(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        for (int i = startIndex; i <= endIndex; i++) {
            try {
                Stock stock = StockService.listStocks.get(i);
                
                String result = StockService.FinalStockData(stock);

                if (!result.isEmpty()) {
                    result = "\"" + result.substring(0, result.length() - 1) + "\"";
                    String name = "\"" + stock.getName() + "\"";

                    synchronized (StockService.hmap) {
                        StockService.hmap.put(name,result);
                    }
                }

                // Increment the stock scan counter

            } catch (Exception e) {
                // Log the exception (optional)
                System.err.println("Error processing stock at index " + i + ": " + e.getMessage());
                // Increment the stock scan counter even if there was an error
                
            }
        }
    }
}
