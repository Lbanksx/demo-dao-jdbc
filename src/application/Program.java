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
		System.out.println("===== Test 2: Seller findByDepartment =====");
		Department department = new Department(2, null);
		List<Seller> seller2 = sellerDao.findByDepartment(department);
		
		for(Seller s : seller2) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println("===== Test 3: Seller findAll =====");
		List<Seller> all = sellerDao.findAll();
		
		for(Seller s : all) {
			System.out.println(s);
		}
		System.out.println();
		System.out.println("===== Test 4: Seller insert =====");
		System.out.println("Seller inserted!");
		System.out.println();
		System.out.println("===== Test 5: Seller update =====");
		seller = sellerDao.findById(1);
		seller.setName("Hugo Nascimento dos Santos");
		sellerDao.update(seller);
		System.out.println("Seller updated: " + seller);
		System.out.println();
		System.out.println("===== Test 6: Seller delete =====");
		sellerDao.deleteId(9);
		System.out.println("Seller deleted!");
	}
}
