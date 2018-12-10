package com.ai.total.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ai.total.model.Department;
import com.ai.total.tools.ConnectionBase;

public class ParametersDao {
	public String getValueByMonth(Connection conn, Department qdepartment) {

		PreparedStatement prest = null;
		ResultSet resultSet = null;
		String value = "";
		String sql = "select value from ssm_parameters where  department= ? and project_name=? and month=?";
		try {
			prest = conn.prepareStatement(sql);
			prest.setString(1, qdepartment.getDepartment());
			prest.setString(2, qdepartment.getProject_name());
			prest.setString(3, qdepartment.getMonth());
			resultSet = prest.executeQuery();
			while (resultSet.next()) {
				value = resultSet.getString("value");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public boolean updateValue(Connection conn, Department qdepartment, String value) {

		PreparedStatement prest = null;
		int updateflag = 0;
		String sql = "update ssm_parameters set  value=? where  department= ? and project_name=? and month=? ";
		try {
			prest = conn.prepareStatement(sql);
			prest.setString(1, value);
			prest.setString(2, qdepartment.getDepartment());
			prest.setString(3, qdepartment.getProject_name());
			prest.setString(4, qdepartment.getMonth());

			updateflag = prest.executeUpdate();
			if (updateflag < 1) {
				sql = "insert into ssm_parameters (department,project_name,month,value)values (?,?,?,?) ";
				prest.clearParameters();
				prest = conn.prepareStatement(sql);
				prest.setString(1, qdepartment.getDepartment());
				prest.setString(2, qdepartment.getProject_name());
				prest.setString(3, qdepartment.getMonth());
				prest.setString(4, value);
				updateflag = prest.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updateflag >= 1;
	}

	public List<Department> getDepartProjects(Connection conn, String month) {
		List<Department> departments = new ArrayList<Department>();

		PreparedStatement prest = null;
		ResultSet resultSet = null;

		String sql = "select * from ssm_parameters where month like ? ";
		try {
			prest = conn.prepareStatement(sql);
			prest.setString(1, month + "%");
			resultSet = prest.executeQuery();
			while (resultSet.next()) {
				Department department = new Department();
				department.setDepartment(resultSet.getString("department"));
				department.setProject_name(resultSet.getString("project_name"));
				department.setProject_version(resultSet.getString("project_version"));
				department.setMonth(resultSet.getString("month"));
				department.setValue(resultSet.getString("value"));
				departments.add(department);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return departments;
	}

	public void deleteValue(Connection conn, Department department) {
		PreparedStatement prest = null;
		String sql = "delete from ssm_parameters where project_name= ? and department=?  ";
		try {
			prest = conn.prepareStatement(sql);
			prest.setString(1, department.getProject_name());
			prest.setString(2, department.getDepartment());
			prest.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Department getLastDepartProjects(Connection conn, Department ldepartment) {

		PreparedStatement prest = null;
		ResultSet resultSet = null;
		Department department = null;
		String sql = "select * from ssm_parameters where department=? and project_name=?";
		try {
			prest = conn.prepareStatement(sql);
			prest.setString(1, ldepartment.getDepartment());
			prest.setString(2, ldepartment.getProject_name());
			resultSet = prest.executeQuery();
			while (resultSet.next()) {
				department = new Department();
				department.setDepartment(resultSet.getString("department"));
				department.setProject_name(resultSet.getString("project_name"));
				department.setProject_version(resultSet.getString("project_version"));
				department.setMonth(resultSet.getString("month"));
				department.setValue(resultSet.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return department;
	}

	public static void main(String[] args) throws Exception {
		Department department = new Department();
		department.setDepartment("beijing_cmc");
		department.setProject_name("ess");
		department.setMonth("201601");
		analysisTotal(department, "h", "j");
	}

	public static void analysisTotal(Department department, String ds1, String ds2) throws Exception {
		ParametersDao parametersDao = new ParametersDao();

		// pull
		ConnectionBase.setDataSource(ds1);
		System.out.println("pull end");
		// pull end

		try {
			ConnectionBase.setDataSource(ds2);
			// init
			System.out.println("init end");
			// init end

			// push
			parametersDao.updateValue(ConnectionBase.conn, department, "1");
			System.out.println(1);
			System.out.println(2);
			System.out.println(3);
			System.out.println(4);
			parametersDao.updateValue(ConnectionBase.conn, department, "100");
			ConnectionBase.conn.commit();
			System.out.println("push end");
		} catch (Exception e) {
			ConnectionBase.conn.rollback();
		}

		// push end
	}
}
