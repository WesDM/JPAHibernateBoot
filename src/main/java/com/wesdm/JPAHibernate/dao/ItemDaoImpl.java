package com.wesdm.JPAHibernate.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.wesdm.JPAHibernate.model.Bid;
import com.wesdm.JPAHibernate.model.Item;
import com.wesdm.JPAHibernate.model.ItemBidSummary;

@Repository
public class ItemDaoImpl extends GenericDaoImpl<Item, Long> implements ItemDao {

	public ItemDaoImpl() {
		super(Item.class);
	}

	@Override
	public List<Item> findAll(boolean withBids) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
		Root<Item> i = criteria.from(Item.class);
		criteria.select(i).distinct(true) // In-memory "distinct"!
				.orderBy(cb.asc(i.get("auctionEnd")));
		if (withBids)
			i.fetch("bids", JoinType.LEFT);
		return em.createQuery(criteria).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Item> findByName(String name, boolean substring) {
		return em.createNamedQuery(substring ? "getItemsByNameSubstring" : "getItemsByName")
				.setParameter("itemName", substring ? ("%" + name + "%") : name).getResultList();
	}

	@Override
	public List<ItemBidSummary> findItemBidSummaries() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ItemBidSummary> criteria = cb.createQuery(ItemBidSummary.class);
		Root<Item> i = criteria.from(Item.class);
		Join<Item, Bid> b = i.join("bids", JoinType.LEFT);
		criteria.select(
				cb.construct(ItemBidSummary.class, i.get("id"), i.get("name"), i.get("auctionEnd"), cb.max(b.<BigDecimal>get("amount"))));
		criteria.orderBy(cb.asc(i.get("auctionEnd")));
		criteria.groupBy(i.get("id"), i.get("name"), i.get("auctionEnd"));
		return em.createQuery(criteria).getResultList();
	}
}
