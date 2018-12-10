package com.ai.total.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.total.model.Department;
import com.ai.total.model.ProjectMeasure;

public class ProjectMeasureDao {

	public List<ProjectMeasure> queryProjectMeasures(Connection conn, Department qDepartment, String flag) {
		List<ProjectMeasure> projectMeasures = new ArrayList<ProjectMeasure>();

		String department = qDepartment.getDepartment();
		String project_name = qDepartment.getProject_name();
		String project_version = qDepartment.getProject_version();
		String month = qDepartment.getMonth();
		String sql = "select cc.ID,cc.VALUE,cc.METRIC_ID,cc.SNAPSHOT_ID,cc.RULE_ID,cc.RULES_CATEGORY_ID,cc.TEXT_VALUE,cc.TENDENCY,cc.MEASURE_DATE,"
				+ "cc.PROJECT_ID,cc.ALERT_STATUS,cc.ALERT_TEXT,cc.URL,cc.DESCRIPTION,cc.RULE_PRIORITY,cc.CHARACTERISTIC_ID,cc.PERSON_ID,"
				+ "cc.VARIATION_VALUE_1,cc.VARIATION_VALUE_2,cc.VARIATION_VALUE_3,cc.VARIATION_VALUE_4,cc.VARIATION_VALUE_5,cc.MEASURE_DATA,"
				+ " (select plugin_name from rules dd where dd.id = cc.rule_id) plugin_name,(select plugin_rule_key from rules dd where dd.id = cc.rule_id) plugin_rule_key "
				+ " from projects aa, snapshots bb, project_measures cc  where aa.scope IN ('PRJ') and aa.id = bb.project_id   and cc.snapshot_id = bb.id"
				+ " and bb.islast = '1' and aa.kee like ?  ";
		String tempsql1 = " union all select cc.ID,cc.VALUE,cc.METRIC_ID,cc.SNAPSHOT_ID,cc.RULE_ID,cc.RULES_CATEGORY_ID,cc.TEXT_VALUE,cc.TENDENCY,cc.MEASURE_DATE,"
				+ "cc.PROJECT_ID,cc.ALERT_STATUS,cc.ALERT_TEXT,cc.URL,cc.DESCRIPTION,cc.RULE_PRIORITY,cc.CHARACTERISTIC_ID,cc.PERSON_ID,"
				+ "cc.VARIATION_VALUE_1,cc.VARIATION_VALUE_2,cc.VARIATION_VALUE_3,cc.VARIATION_VALUE_4,cc.VARIATION_VALUE_5,cc.MEASURE_DATA,"
				+ " (select plugin_name from rules dd where dd.id = cc.rule_id) plugin_name,(select plugin_rule_key from rules dd where dd.id = cc.rule_id) plugin_rule_key "
				+ " from projects aa, snapshots bb, project_measures cc  where aa.scope IN ('FIL') and aa.id = bb.project_id   and cc.snapshot_id = bb.id"
				+ " and bb.islast = '1' and aa.kee like ? and cc.metric_id in (1,3,94, 95, 96, 97, 98) and cc.rule_id is null  ";
		String tempsql2 = " union all select cc.ID,cc.VALUE,cc.METRIC_ID,cc.SNAPSHOT_ID,cc.RULE_ID,cc.RULES_CATEGORY_ID,cc.TEXT_VALUE,cc.TENDENCY,cc.MEASURE_DATE,"
				+ "cc.PROJECT_ID,cc.ALERT_STATUS,cc.ALERT_TEXT,cc.URL,cc.DESCRIPTION,cc.RULE_PRIORITY,cc.CHARACTERISTIC_ID,cc.PERSON_ID,"
				+ "cc.VARIATION_VALUE_1,cc.VARIATION_VALUE_2,cc.VARIATION_VALUE_3,cc.VARIATION_VALUE_4,cc.VARIATION_VALUE_5,cc.MEASURE_DATA,"
				+ " (select plugin_name from rules dd where dd.id = cc.rule_id) plugin_name,(select plugin_rule_key from rules dd where dd.id = cc.rule_id) plugin_rule_key "
				+ " from projects aa, snapshots bb, project_measures cc  where aa.scope IN ('FIL') and aa.id = bb.project_id   and cc.snapshot_id = bb.id"
				+ " and bb.islast = '1' and aa.kee like ? and cc.metric_id in (94, 95, 96, 97, 98) and cc.rule_id is not null  ";
		if (flag.equals("1")) {
			sql = sql + tempsql1;
		}
		if (flag.equals("2")) {
			sql = sql + tempsql1 + tempsql2;
		}
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, project_name + "%");
			if (flag.equals("1")) {
				prest.setString(2, project_name + "%");
			}
			if (flag.equals("2")) {
				prest.setString(3, project_name + "%");
			}
			ResultSet resultSet = prest.executeQuery();
			while (resultSet.next()) {
				ProjectMeasure projectMeasure = new ProjectMeasure();
				projectMeasure.setDepartment(department);
				projectMeasure.setProject_name(project_name);
				projectMeasure.setProject_version(project_version);
				projectMeasure.setMonth(month);
				convertRS2Obj(resultSet, projectMeasure);
				projectMeasures.add(projectMeasure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return projectMeasures;

	}

	private ProjectMeasure convertRS2Obj(ResultSet resultSet, ProjectMeasure projectMeasure) {
		try {
			projectMeasure.setId(resultSet.getLong("id"));
			projectMeasure.setValue(resultSet.getBigDecimal("value"));
			projectMeasure.setMetric_id(resultSet.getInt("metric_id"));
			projectMeasure.setSnapshot_id(resultSet.getInt("snapshot_id"));
			projectMeasure.setRule_id(resultSet.getString("rule_id"));
			projectMeasure.setRules_category_id(resultSet.getString("rules_category_id"));
			projectMeasure.setText_value(resultSet.getString("text_value"));
			projectMeasure.setTendency(resultSet.getString("tendency"));
			projectMeasure.setMeasure_date(resultSet.getTimestamp("measure_date"));
			projectMeasure.setProject_id(resultSet.getString("project_id"));
			projectMeasure.setAlert_status(resultSet.getString("alert_status"));
			projectMeasure.setAlert_text(resultSet.getString("alert_text"));
			projectMeasure.setUrl(resultSet.getString("url"));
			projectMeasure.setDescription(resultSet.getString("description"));
			projectMeasure.setRule_priority(resultSet.getString("rule_priority"));
			projectMeasure.setCharacteristic_id(resultSet.getString("characteristic_id"));
			projectMeasure.setVariation_value_1(resultSet.getBigDecimal("variation_value_1"));
			projectMeasure.setVariation_value_2(resultSet.getBigDecimal("variation_value_2"));
			projectMeasure.setVariation_value_3(resultSet.getBigDecimal("variation_value_3"));
			projectMeasure.setVariation_value_4(resultSet.getBigDecimal("variation_value_4"));
			projectMeasure.setVariation_value_5(resultSet.getBigDecimal("variation_value_5"));
			projectMeasure.setPerson_id(resultSet.getString("person_id"));
			projectMeasure.setMeasure_data(resultSet.getBytes("measure_data"));
			projectMeasure.setPlugin_rule_key(resultSet.getString("plugin_rule_key"));
			projectMeasure.setPlugin_name(resultSet.getString("plugin_name"));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return projectMeasure;
	}

	public void insertSSMProjectMeasures(Connection conn, List<ProjectMeasure> projectMeasures,Department department) {
		String sql = "insert into ssm_project_measures(department,project_name,project_version,id,value,metric_id,snapshot_id,rule_id,rules_category_id,text_value,tendency,"
				+ "measure_date,project_id,alert_status,alert_text,url,description,rule_priority,characteristic_id,person_id,variation_value_1,variation_value_2,variation_value_3,"
				+ "variation_value_4,variation_value_5,measure_data,plugin_rule_key,plugin_name,month) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement iprest = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int flag = 1;
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			for (ProjectMeasure projectMeasure : projectMeasures) {
				iprest.clearParameters();
				int i = 1;
				iprest.setString(i, department.getDepartment());
				i++;
				iprest.setString(i, department.getProject_name());
				i++;
				iprest.setString(i, department.getProject_version());
				i++;
				iprest.setLong(i, projectMeasure.getId());
				i++;
				iprest.setBigDecimal(i, projectMeasure.getValue());
				i++;
				iprest.setInt(i, projectMeasure.getMetric_id());
				i++;
				iprest.setInt(i, projectMeasure.getSnapshot_id());
				i++;
				iprest.setString(i, projectMeasure.getRule_id());
				i++;
				iprest.setString(i, projectMeasure.getRules_category_id());
				i++;
				iprest.setString(i, projectMeasure.getText_value());
				i++;
				iprest.setString(i, projectMeasure.getTendency());
				i++;
				iprest.setTimestamp(i, projectMeasure.getMeasure_date());
				i++;
				iprest.setString(i, projectMeasure.getProject_id());
				i++;
				iprest.setString(i, projectMeasure.getAlert_status());
				i++;
				iprest.setString(i, projectMeasure.getAlert_text());
				i++;
				iprest.setString(i, projectMeasure.getUrl());
				i++;
				iprest.setString(i, projectMeasure.getDescription());
				i++;
				iprest.setString(i, projectMeasure.getRule_priority());
				i++;
				iprest.setString(i, projectMeasure.getCharacteristic_id());
				i++;
				iprest.setString(i, projectMeasure.getPerson_id());
				i++;
				iprest.setBigDecimal(i, projectMeasure.getVariation_value_1());
				i++;
				iprest.setBigDecimal(i, projectMeasure.getVariation_value_2());
				i++;
				iprest.setBigDecimal(i, projectMeasure.getVariation_value_3());
				i++;
				iprest.setBigDecimal(i, projectMeasure.getVariation_value_4());
				i++;
				iprest.setBigDecimal(i, projectMeasure.getVariation_value_5());
				i++;
				iprest.setBytes(i, projectMeasure.getMeasure_data());
				i++;
				iprest.setString(i, projectMeasure.getPlugin_name());
				i++;
				iprest.setString(i, projectMeasure.getPlugin_rule_key());
				i++;
				iprest.setString(i, department.getMonth());
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

	public List<ProjectMeasure> querySSMProjectMeasures(Connection conn, Department qDepartment) {
		List<ProjectMeasure> projectMeasures = new ArrayList<ProjectMeasure>();

		String sql = "select * from ssm_project_measures where department=? and project_name=? and month=?";
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, qDepartment.getDepartment());
			prest.setString(2, qDepartment.getProject_name());
			prest.setString(3, qDepartment.getMonth());
			ResultSet resultSet = prest.executeQuery();
			while (resultSet.next()) {
				ProjectMeasure projectMeasure = new ProjectMeasure();
				projectMeasure.setDepartment(resultSet.getString("department"));
				projectMeasure.setProject_name(resultSet.getString("project_name"));
				projectMeasure.setProject_version(resultSet.getString("project_version"));
				projectMeasure.setMonth(resultSet.getString("month"));
				convertRS2Obj(resultSet, projectMeasure);
				projectMeasures.add(projectMeasure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return projectMeasures;

	}

	public Map<String, ProjectMeasure> querySingleSSMProjectMeasure(Connection conn, Department qDepartment ) {
		
		Map<String, ProjectMeasure> projectMeasuremap = new HashMap<String, ProjectMeasure>();
		String sql = "SELECT aa.kee kee, cc.* FROM ssm_projects aa,ssm_snapshots bb,ssm_project_measures cc WHERE aa.id=bb.project_id and bb.id=cc.snapshot_id and "
				+ "aa.department =? and aa.project_name =? and metric_id=1";
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, qDepartment.getDepartment());
			prest.setString(2, qDepartment.getProject_name());
			ResultSet resultSet = prest.executeQuery();
			while (resultSet.next()) {
				ProjectMeasure projectMeasure = new ProjectMeasure();
				projectMeasure.setDepartment(resultSet.getString("department"));
				projectMeasure.setProject_name(resultSet.getString("project_name"));
				projectMeasure.setProject_version(resultSet.getString("project_version"));
				projectMeasure.setMonth(resultSet.getString("month"));
				convertRS2Obj(resultSet, projectMeasure);
				projectMeasuremap.put(resultSet.getString("kee"), projectMeasure);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return projectMeasuremap;

	}

	public void deleteSSMProjectMeasuresByMonth(Connection conn, Department department) {
		String sql = "delete from ssm_project_measures where department=? and project_name =? and month=?";
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, department.getDepartment());
			prest.setString(2, department.getProject_name());
			prest.setString(3, department.getMonth());
			prest.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}
}
