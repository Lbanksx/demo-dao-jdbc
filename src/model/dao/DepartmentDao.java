package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	
	public void insert(Department dep);
	public void update(Department dep);
	public void deleteId(Integer id);
	public Department findById(Integer id);
	public List<Department> findAll();
}
