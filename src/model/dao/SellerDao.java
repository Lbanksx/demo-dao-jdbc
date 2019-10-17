package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	
	public void insert(Seller dep);
	public void update(Seller dep);
	public void deleteId(Integer id);
	public Seller findById(Integer id);
	public List<Seller> findAll();
}
