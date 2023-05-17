package spring.boot.oath2.scrabdatas.persistent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.boot.oath2.scrabdatas.entity.StockHistEntity;
import spring.boot.oath2.scrabdatas.entity.pk.StockHistEntityPk;

@Repository
public interface StockHistRepo extends JpaRepository<StockHistEntity,StockHistEntityPk>{

}
