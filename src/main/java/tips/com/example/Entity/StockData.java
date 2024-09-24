package tips.com.example.Entity;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StockData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String jsonData;
    
    

    @CreationTimestamp
    private LocalDateTime createdAt;
}
