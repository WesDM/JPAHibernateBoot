package com.wesdm.JPAHibernate.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;

@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_GENERATOR_POOLED")
	@org.hibernate.annotations.GenericGenerator(name = "ID_GENERATOR_POOLED", strategy = "enhanced-sequence",
			parameters = { @org.hibernate.annotations.Parameter(name = "sequence_name", value = "item_seq"),
					@org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
					@org.hibernate.annotations.Parameter(name = "increment_size", value = "5"),
					@org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo") })
	// @SequenceGenerator(name = "sequence_generator", sequenceName = "item_seq", allocationSize = 5) can't use pooled or pooled-lo optimizers with
	// @SequenceGenerator
	protected Long id;

	protected String name;

	@ElementCollection
	@CollectionTable(name = "IMAGE", joinColumns = @JoinColumn(name = "item_id")) // overrides default name for table to IMAGE
	@OrderColumn(name = "index_id") //used so Hibernate can sort insertion order after fetching. what about using timestamp to sort?
	@OrderBy("filename, width DESC")  //executed at sql level, ordered in DB
	@AttributeOverride(name = "filename", column = @Column(name = "FNAME", nullable = false))
	protected Set<Image> images = new HashSet<Image>();

	@OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
	protected Set<Bid> bids = new HashSet<>();

	@OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
	protected Set<CategorizedItem> categorizedItems = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SELLER_ID", nullable = false, updatable = false, insertable = false)
	//seller id col will be set when item is created. it will never be null therefore JoinColumn is sufficient
	//@JoinTable(name = "ITEM_SELLER", joinColumns = @JoinColumn(name = "ITEM_ID", unique = true),
		//		inverseJoinColumns = @JoinColumn(name = "SELLER_ID", nullable = false))
	protected User seller;
	

	@ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "BUYER_ID", nullable = true, updatable = false, insertable = false)
	@JoinTable(name = "ITEM_BUYER", joinColumns = @JoinColumn(name = "ITEM_ID", unique = true),
			inverseJoinColumns = @JoinColumn(name = "BUYER_ID", nullable = false))
	protected User buyer;
	// JoinTable hides the table from app view..no Java class. Good choice to avoid null columns


	public Item() {
	}

	public Item(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<CategorizedItem> getCategorizedItems() {
		return categorizedItems;
	}

	public void addBid(Bid bid) {
		// Be defensive
		if (bid == null)
			throw new NullPointerException("Can't add null Bid");
		if (bid.getItem() != null)
			throw new IllegalStateException("Bid is already assigned to an Item");

		getBids().add(bid);
		bid.setItem(this);
	}

	public Bid placeBid(Bid currentHighestBid, BigDecimal bidAmount) {
		if (currentHighestBid == null || bidAmount.compareTo(currentHighestBid.getAmount()) > 0) {
			return new Bid(bidAmount, this);
		}
		return null;
	}

	// ...

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

	public void setSeller(User seller) {
		this.seller = seller;
	}

	public User getSeller() {
		return seller;
	}
}
