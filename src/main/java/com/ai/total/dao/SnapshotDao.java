package com.ai.total.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ai.total.model.Department;
import com.ai.total.model.Snapshot;

public class SnapshotDao {

	public List<Snapshot> querySnapshots(Connection conn, Department qdDepartment, String flag) {
		List<Snapshot> snapshots = new ArrayList<Snapshot>();
		String department = qdDepartment.getDepartment();
		String project_name = qdDepartment.getProject_name();
		String project_version = qdDepartment.getProject_version();
		String month = qdDepartment.getMonth();

		String sql = "select bb.* from projects aa,snapshots bb   WHERE aa.scope IN ('PRJ') and aa.id=bb.project_id and bb.islast = '1' and aa.kee like ? ";
		if (flag.equals("1") || flag.equals("2")) {
			sql = sql
					+ " union all select bb.* from projects aa,snapshots bb   WHERE aa.scope IN ('FIL') and aa.id=bb.project_id and bb.islast = '1' and aa.kee like ?";
		}
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, project_name + "%");
			if (flag.equals("1") || flag.equals("2")) {
				prest.setString(2, project_name + "%");
			}
			ResultSet resultSet = prest.executeQuery();
			while (resultSet.next()) {
				Snapshot snapshot = new Snapshot();
				snapshot.setDepartment(department);
				snapshot.setProject_name(project_name);
				snapshot.setProject_version(project_version);
				snapshot.setMonth(month);
				convertRS2Obj(resultSet, snapshot);
				snapshots.add(snapshot);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ConnectionBase.clean(conn, null, null);
		}

		return snapshots;
	}

	public List<Snapshot> querySSMSnapshots(Connection conn, Department department) {
		List<Snapshot> snapshots = new ArrayList<Snapshot>();
		String sql = "select * from ssm_snapshots where department=? and project_name=? and month=?";
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, department.getDepartment());
			prest.setString(2, department.getProject_name());
			prest.setString(3, department.getMonth());
			ResultSet resultSet = prest.executeQuery();
			while (resultSet.next()) {
				Snapshot snapshot = new Snapshot();
				snapshot.setDepartment(department.getDepartment());
				snapshot.setProject_name(department.getProject_name());
				snapshot.setProject_version(resultSet.getString("project_version"));
				snapshot.setMonth(department.getMonth());
				convertRS2Obj(resultSet, snapshot);
				snapshots.add(snapshot);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return snapshots;
	}

	public void deleteSSMSnapshotsByMonth(Connection conn, Department department) {
		String sql = "delete from ssm_snapshots where department=? and project_name=?  and month=?";
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

	public void updateSSMSnapshotsFlag(Connection conn, Department department) {
		String sql = "update   ssm_snapshots set  islast =false  where department=? and project_name=? ";
		try {
			PreparedStatement prest = conn.prepareStatement(sql);
			prest.setString(1, department.getDepartment());
			prest.setString(2, department.getProject_name());
			prest.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertSSMSnapshots(Connection conn, List<Snapshot> snapshots, Department department) {
		String sql = "insert into ssm_snapshots  (department,   project_name,   project_version,   ID,   CREATED_AT,   BUILD_DATE,   PROJECT_ID,   "
				+ "PARENT_SNAPSHOT_ID,   STATUS,   PURGE_STATUS,   ISLAST,   SCOPE,   QUALIFIER,   ROOT_SNAPSHOT_ID,   VERSION,   PATH,   DEPTH,   "
				+ "ROOT_PROJECT_ID,   PERIOD1_MODE,   PERIOD1_PARAM,   PERIOD1_DATE,   PERIOD2_MODE,   PERIOD2_PARAM,   "
				+ "PERIOD2_DATE,   PERIOD3_MODE,   PERIOD3_PARAM,   PERIOD3_DATE,   PERIOD4_MODE,   PERIOD4_PARAM,   PERIOD4_DATE, "
				+ "  PERIOD5_MODE,   PERIOD5_PARAM,   PERIOD5_DATE,month) values  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement iprest = conn.prepareStatement(sql);
			int flag = 1;
			boolean autocommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			for (Snapshot snapshot : snapshots) {
				int i = 1;
				iprest.clearParameters();
				iprest.setString(i, department.getDepartment());
				i++;
				iprest.setString(i, department.getProject_name());
				i++;
				iprest.setString(i, department.getProject_version());
				i++;
				iprest.setInt(i, snapshot.getId());
				i++;
				iprest.setString(i, String.valueOf(snapshot.getCreated_at()));
				i++;
				iprest.setString(i, String.valueOf(snapshot.getBuild_date()));
				i++;
				iprest.setInt(i, snapshot.getProject_id());
				i++;
				iprest.setString(i, snapshot.getParent_snapshot_id());
				i++;
				iprest.setString(i, snapshot.getStatus());
				i++;
				iprest.setString(i, snapshot.getPurge_status());
				i++;
				iprest.setInt(i, snapshot.getIslast());
				i++;
				iprest.setString(i, snapshot.getScope());
				i++;
				iprest.setString(i, snapshot.getQualifier());
				i++;
				iprest.setString(i, snapshot.getRoot_snapshot_id());
				i++;
				iprest.setString(i, snapshot.getVersion());
				i++;
				iprest.setString(i, snapshot.getPath());
				i++;
				iprest.setInt(i, snapshot.getDepth());
				i++;
				iprest.setString(i, snapshot.getRoot_project_id());
				i++;
				iprest.setString(i, snapshot.getPeriod1_mode());
				i++;
				iprest.setString(i, snapshot.getPeriod1_param());
				i++;
				iprest.setString(i, snapshot.getPeriod1_date());
				i++;
				iprest.setString(i, snapshot.getPeriod2_mode());
				i++;
				iprest.setString(i, snapshot.getPeriod2_param());
				i++;
				iprest.setString(i, snapshot.getPeriod2_date());
				i++;
				iprest.setString(i, snapshot.getPeriod3_mode());
				i++;
				iprest.setString(i, snapshot.getPeriod3_param());
				i++;
				iprest.setString(i, snapshot.getPeriod3_date());
				i++;
				iprest.setString(i, snapshot.getPeriod4_mode());
				i++;
				iprest.setString(i, snapshot.getPeriod4_param());
				i++;
				iprest.setString(i, snapshot.getPeriod4_date());
				i++;
				iprest.setString(i, snapshot.getPeriod5_mode());
				i++;
				iprest.setString(i, snapshot.getPeriod5_param());
				i++;
				iprest.setString(i, snapshot.getPeriod5_date());
				i++;
				iprest.setString(i, department.getMonth());
				i++;
				iprest.addBatch();
				if (flag % 1000 == 0) {
					iprest.executeBatch();
					conn.commit();
				}
			}
			iprest.executeBatch();
			conn.commit();
			conn.setAutoCommit(autocommit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void convertRS2Obj(ResultSet resultSet, Snapshot snapshot) {
		try {
			snapshot.setId(resultSet.getInt("id"));
			snapshot.setProject_id(resultSet.getInt("project_id"));
			snapshot.setParent_snapshot_id(resultSet.getString("parent_snapshot_id"));
			snapshot.setStatus(resultSet.getString("status"));
			snapshot.setIslast(resultSet.getInt("islast"));
			snapshot.setScope(resultSet.getString("scope"));
			snapshot.setQualifier(resultSet.getString("qualifier"));
			snapshot.setRoot_snapshot_id(resultSet.getString("root_snapshot_id"));
			snapshot.setVersion(resultSet.getString("version"));
			snapshot.setPath(resultSet.getString("path"));
			snapshot.setDepth(resultSet.getInt("depth"));
			snapshot.setRoot_project_id(resultSet.getString("root_project_id"));
			snapshot.setPeriod1_mode(resultSet.getString("period1_mode"));
			snapshot.setPeriod1_param(resultSet.getString("period1_param"));
			snapshot.setPeriod2_mode(resultSet.getString("period2_mode"));
			snapshot.setPeriod2_param(resultSet.getString("period2_param"));
			snapshot.setPeriod3_mode(resultSet.getString("period3_mode"));
			snapshot.setPeriod3_param(resultSet.getString("period3_param"));
			snapshot.setPeriod4_mode(resultSet.getString("period4_mode"));
			snapshot.setPeriod4_param(resultSet.getString("period4_param"));
			snapshot.setPeriod5_mode(resultSet.getString("period5_mode"));
			snapshot.setPeriod5_param(resultSet.getString("period5_param"));
			snapshot.setPurge_status(resultSet.getString("purge_status"));
			snapshot.setCreated_at(BigInteger.valueOf(resultSet.getLong("created_at")));
			snapshot.setBuild_date(BigInteger.valueOf(resultSet.getLong("build_date")));
			snapshot.setPeriod1_date(resultSet.getString("period1_date"));
			snapshot.setPeriod2_date(resultSet.getString("period2_date"));
			snapshot.setPeriod3_date(resultSet.getString("period3_date"));
			snapshot.setPeriod4_date(resultSet.getString("period4_date"));
			snapshot.setPeriod5_date(resultSet.getString("period5_date"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// SnapshotDao snapshotDao = new SnapshotDao();
		// ConnectionBase.setDataSource("h");
		// Department department = new Department();
		// department.setDepartment("beijing_cmc");
		// List<Snapshot> snapshots =
		// snapshotDao.querySSMSnapshots(ConnectionBase.conn, department);
		// snapshotDao.deleteSSMSnapshots(ConnectionBase.conn, snapshots);
		// try {
		//
		// snapshots = snapshotDao.querySSMSnapshots(ConnectionBase.conn,
		// department);
		// ConnectionBase.conn.commit();
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
	}
}
