package tips.com.example.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tips.com.example.Entity.StockData;


public interface Stockrepo extends JpaRepository<StockData, Long>{

    StockData findTopByOrderByCreatedAtDesc();
}
