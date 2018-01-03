package com.wesdm.JPAHibernate.dao;

import java.util.List;

import com.wesdm.JPAHibernate.model.Item;
import com.wesdm.JPAHibernate.model.ItemBidSummary;

public interface ItemDao extends GenericDao<Item, Long> {

    List<Item> findAll(boolean withBids);

    List<Item> findByName(String name, boolean fuzzy);

    List<ItemBidSummary> findItemBidSummaries();

}

//public interface ItemDao extends JpaRepository<Item, Long>{
//	
////	@Query("select i from Item i")
////	List<Item> findAllItems();
//}
