package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	
	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("===== Test 1: Seller findById =====");
		Seller seller = sellerDao.findById(1);
		System.out.println(seller);
		System.out.println();
		System.out.println("===== Test 1: Seller findByDepartment =====");
		Department department = new Department(2, null);
		List<Seller> seller2 = sellerDao.findByDepartment(department);
		
		for(Seller s : seller2) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println("===== Test 1: Seller findAll =====");
		List<Seller> all = sellerDao.findAll();
		
		for(Seller s : all) {
			System.out.println(s);
		}
	}
}
