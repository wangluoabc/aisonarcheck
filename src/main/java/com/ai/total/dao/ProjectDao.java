package com.ai.total.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ai.total.model.Department;
import com.ai.total.model.Project;

public class ProjectDao {

	public List<Project> queryProjects(Connection conn, Department qdepartment, String flag) {
		List<Project> projects = new ArrayList<Project>();
		PreparedStatement prest = null;
		ResultSet resultSet = null;
		String department = qdepartment.getDepartment();
		String project_name = qdepartment.getProject_name();
		String project_version = qdepartment.getProject_version();
		String sql = "select * from projects where scope IN ('PRJ') and kee like ?";
		if (flag.equals("1") || flag.equals("2")) {
			sql = sql + " union all select * from projects where scope IN ('FIL') and kee like ?";
		}
		try {
			prest = conn.prepareStatement(sql);
			prest.setString(1, project_name + "%");
			if (flag.equals("1") || flag.equals("2")) {
				prest.setString(2, project_name + "%");
			}
			resultSet = prest.executeQuery();
			while (resultSet.next()) {
				Project project = new Project();
				project.setDepartment(department);
				project.setProject_name(project_name);
				project.setProject_version(project_version);
				convertRS2Obj(resultSet, project);
				projects.add(project);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projects;
	}

	public List<Project> querySSMProjects(Connection conn, Department department) {
		List<Project> projects = new ArrayList<Project>();
		String sql = "select * from ssm_projects where department=? and project_name=? ";
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, department.getDepartment());
			prest.setString(2, department.getProject_name());
			ResultSet resultSet = prest.executeQuery();
			while (resultSet.next()) {
				Project project = new Project();
				project.setDepartment(resultSet.getString("department"));
				project.setProject_name(resultSet.getString("project_name"));
				project.setProject_version(resultSet.getString("project_version"));
				convertRS2Obj(resultSet, project);
				projects.add(project);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projects;
	}

	public void insertSSMProjects(Connection conn, List<Project> projects) {
		String sql = "insert into ssm_projects(department,project_name,project_version,id,name,description,enabled,scope,qualifier,"
				+ "kee,root_id,language,copy_resource_id,long_name,person_id,created_at,path,deprecated_kee,uuid,project_uuid,module_uuid,"
				+ "module_uuid_path,authorization_updated_at) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement iprest = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int flag = 1;
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			for (Project project : projects) {
				iprest.clearParameters();
				iprest.setString(1, project.getDepartment());
				iprest.setString(2, project.getProject_name());
				iprest.setString(3, project.getProject_version());
				iprest.setInt(4, project.getId());
				iprest.setString(5, project.getName());
				iprest.setString(6, project.getDescription());
				iprest.setInt(7, project.getEnabled());
				iprest.setString(8, project.getScope());
				iprest.setString(9, project.getQualifier());
				iprest.setString(10, project.getKee());
				iprest.setString(11, project.getRoot_id());
				iprest.setString(12, project.getLanguage());
				iprest.setString(13, project.getCopy_resource_id());
				iprest.setString(14, project.getLong_name());
				iprest.setString(15, project.getPerson_id());
				iprest.setTimestamp(16, project.getCreated_at());
				iprest.setString(17, project.getPath());
				iprest.setString(18, project.getDeprecated_kee());
				iprest.setString(19, project.getUuid());
				iprest.setString(20, project.getProject_uuid());
				iprest.setString(21, project.getModule_uuid());
				iprest.setString(22, project.getModule_uuid_path());
				iprest.setString(23, project.getAuthorization_updated_at());
				iprest.addBatch();
				if (flag % 1000 == 0) {
					iprest.executeBatch();
					conn.commit();
				}
			}
			iprest.executeBatch();
			conn.commit();
			conn.setAutoCommit(autocommit);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteSSMProjects(Connection conn, Department department) {
		String sql = "delete from ssm_projects where department=?  and project_name=? ";
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, department.getDepartment());
			prest.setString(2, department.getProject_name());
			prest.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Project convertRS2Obj(ResultSet resultSet, Project project) {
		try {
			project.setId(resultSet.getInt("id"));
			project.setName(resultSet.getString("name"));
			project.setDescription(resultSet.getString("description"));
			project.setEnabled(resultSet.getInt("enabled"));
			project.setScope(resultSet.getString("scope"));
			project.setQualifier(resultSet.getString("qualifier"));
			project.setKee(resultSet.getString("kee"));
			project.setRoot_id(resultSet.getString("root_id"));
			project.setLanguage(resultSet.getString("language"));
			project.setCopy_resource_id(resultSet.getString("copy_resource_id"));
			project.setLong_name(resultSet.getString("long_name"));
			project.setPerson_id(resultSet.getString("person_id"));
			project.setCreated_at(resultSet.getTimestamp("created_at"));
			project.setPath(resultSet.getString("path"));
			project.setDeprecated_kee(resultSet.getString("deprecated_kee"));
			project.setUuid(resultSet.getString("uuid"));
			project.setProject_uuid(resultSet.getString("project_uuid"));
			project.setModule_uuid(resultSet.getString("module_uuid"));
			project.setModule_uuid_path(resultSet.getString("module_uuid_path"));
			project.setAuthorization_updated_at(resultSet.getString("authorization_updated_at"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return project;
	}
}
