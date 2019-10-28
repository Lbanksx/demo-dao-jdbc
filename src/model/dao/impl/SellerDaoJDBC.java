package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller seller) {
		PreparedStatement ps = null;
		
		String sql = "insert into seller(Name, Email, birthDate, BaseSalary, DepartmentId) values(?, ?, ?, ?, ?)";
		
		try {
			
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
					
			if(rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro, nenhuma linha alterada!");
			}
		}
		catch(SQLException e) {
			throw new DbException("Erro: " + e.getMessage());
		}
		finally {
			DB.closeStatment(ps);
		}
	}

	@Override
	public void update(Seller seller) {
		
		PreparedStatement ps = null;
		
		String sql = "update seller set Name = ?, Email = ?, birthDate = ?, BaseSalary = ?, DepartmentId = ? where Id = ?";
		
		try {
			
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			ps.setInt(6,  seller.getId());
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException("Erro: " + e.getMessage());
		}
		finally {
			DB.closeStatment(ps);
		}
	}

	@Override
	public void deleteId(Integer id) {
		PreparedStatement ps = null;
		
		String sql = "delete from seller where Id = ?";
		
		try {
			
			ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException("Erro ao deletar: " + e.getMessage());
		}
	}

	@Override
	public Seller findById(Integer id) {
		
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String url = "select seller.*, department.Name as DepName from seller inner join department on seller.DepartmentId = department.Id where seller.Id = ?";
		
		try {
			
			ps = conn.prepareStatement(url);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller seller = instantiateSeller(rs, dep);
				return seller;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatment(ps);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("birthDate"));
		seller.setDepartment(dep);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {

		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {

		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String sql = "select seller.*, department.Name as DepName from seller inner join department on seller.DepartmentId = department.Id order by Name";
		
		try {
			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatment(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		
		String url = "select seller.*, department.Name as DepName from seller inner join department on seller.DepartmentId = department.Id where DepartmentId = ? order by Name";
		
		try {
			
			ps = conn.prepareStatement(url);
			ps.setInt(1, department.getId());
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatment(ps);
			DB.closeResultSet(rs);
		}
	}
}
