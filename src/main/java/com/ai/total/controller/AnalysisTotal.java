package com.ai.total.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ai.total.dao.ParametersDao;
import com.ai.total.dao.ProjectDao;
import com.ai.total.dao.ProjectMeasureDao;
import com.ai.total.dao.SnapshotDao;
import com.ai.total.model.Department;
import com.ai.total.model.Project;
import com.ai.total.model.ProjectMeasure;
import com.ai.total.model.Snapshot;
import com.ai.total.tools.ConnectionBase;
import com.ai.total.tools.FileUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AnalysisTotal {
	public static void analysisTotal(Department department, String ds1, String ds2, String flag) throws Exception {
		ProjectDao projectDao = new ProjectDao();
		SnapshotDao snapshotDao = new SnapshotDao();
		ProjectMeasureDao projectMeasureDao = new ProjectMeasureDao();
		ParametersDao parametersDao = new ParametersDao();

		ConnectionBase.setDataSource(ds1);
		boolean watflag = false;
		int watsize = 0;
		List<Project> projects = null;
		List<Snapshot> snapshots = null;
		List<ProjectMeasure> projectMeasures = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		BigInteger builddate = null;
		String builddatestr = null;
		while (!watflag && watsize < 3) {

			snapshots = snapshotDao.querySnapshots(ConnectionBase.conn, department, flag);

			if (snapshots != null && snapshots.size() > 0) {
				builddate = snapshots.get(0).getBuild_date();
				builddatestr = simpleDateFormat.format(builddate);
				System.out.println(
						"BEF:compare department date:" + department.getMonth() + " with database date:" + builddatestr);
				if (Long.valueOf(builddatestr) > Long.valueOf(department.getMonth())) {
					System.out.println("Error date: there is new Data for this project!");
					System.exit(99);
				} else if (Long.valueOf(builddatestr) < Long.valueOf(department.getMonth())) {
					System.out.println("Error date: no this time Data for this project!");
					watflag = false;
				} else {
					watflag = true;
				}
			} else {
				watflag = false;
			}

			if (watflag) {
				projects = projectDao.queryProjects(ConnectionBase.conn, department, flag);
				projectMeasures = projectMeasureDao.queryProjectMeasures(ConnectionBase.conn, department, flag);
				if (projects.isEmpty() || snapshots.isEmpty() || projectMeasures.isEmpty()) {
					watflag = false;
				}
			}

			watsize++;
			if (!watflag) {
				System.out.println("waiting...");
				Thread.sleep(600 * 1000l);
				// Thread.sleep(600);
			}
		}

		if (watflag) {
			System.out.println(department.toString() + ":data pull end");
			try {
				ConnectionBase.setDataSource(ds2);
				ConnectionBase.conn.setAutoCommit(false);
				// init
				int i = 0;// value �汾��
				String value = parametersDao.getValueByMonth(ConnectionBase.conn, department);
				if (!value.equals("")) {
					System.out.println(department.toString() + ":exists data. delete ...");
					projectDao.deleteSSMProjects(ConnectionBase.conn, department);
					snapshotDao.deleteSSMSnapshotsByMonth(ConnectionBase.conn, department);
					projectMeasureDao.deleteSSMProjectMeasuresByMonth(ConnectionBase.conn, department);
					i++;
				}

				System.out.println(department.toString() + ":init end");
				// init end
				projectDao.deleteSSMProjects(ConnectionBase.conn, department);

				snapshotDao.updateSSMSnapshotsFlag(ConnectionBase.conn, department);

				// push
				parametersDao.updateValue(ConnectionBase.conn, department, "1");
				projectDao.insertSSMProjects(ConnectionBase.conn, projects);
				snapshotDao.insertSSMSnapshots(ConnectionBase.conn, snapshots, department);
				projectMeasureDao.insertSSMProjectMeasures(ConnectionBase.conn, projectMeasures, department);
				String newValue = "100";
				if (!value.equals("") && Integer.valueOf(value) >= 100) {
					newValue = String.valueOf(Integer.valueOf(value) + i);
				} else {
					newValue = String.valueOf(Integer.valueOf("100") + i);
				}
				parametersDao.updateValue(ConnectionBase.conn, department, newValue);
				ConnectionBase.conn.commit();
				System.out.println("projects size:" + projects.size() + ":snapshots size:" + snapshots.size()
						+ ":projectMeasures size:" + projectMeasures.size());
				System.out.println(department.toString() + ":push end");

				if (flag.equals("1") || flag.equals("2")) {
					analysisFileLines(ConnectionBase.conn, department);
					ConnectionBase.conn.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(department.toString() + " no data !!!!!");
			System.exit(99);
		}

	}

	public static void analysisTotalDbfile(String dbfile, String month) throws Exception {
		ParametersDao parametersDao = new ParametersDao();
		ProjectDao projectDao = new ProjectDao();
		SnapshotDao snapshotDao = new SnapshotDao();
		ProjectMeasureDao projectMeasureDao = new ProjectMeasureDao();

		ConnectionBase.setDataSource2(dbfile);
		List<Department> newFileDepartments = parametersDao.getDepartProjects(ConnectionBase.conn, month);// �ϴ��ļ��д��ڵ�
		if (newFileDepartments.size() < 1) {
			System.out.println("no  data " + month);
		}
		for (Department newFileDepartment : newFileDepartments) {
			System.out.println();
			System.out.println(newFileDepartment.toString());
			String new_value = newFileDepartment.getValue();
			if (Integer.valueOf(new_value) < 100) {
				System.out.println(" error data!value :" + new_value);
				continue;
			}
			ConnectionBase.setDataSource2(dbfile);

			List<Project> projects = projectDao.querySSMProjects(ConnectionBase.conn, newFileDepartment);
			List<Snapshot> snapshots = snapshotDao.querySSMSnapshots(ConnectionBase.conn, newFileDepartment);
			List<ProjectMeasure> projectMeasures = projectMeasureDao.querySSMProjectMeasures(ConnectionBase.conn,
					newFileDepartment);
			if (projects.isEmpty() || snapshots.isEmpty() || projectMeasures.isEmpty()) {
				System.out.println("no data !!!");
				continue;
			}
			System.out.println("projects size:" + projects.size() + ":snapshots size:" + snapshots.size()
					+ ":projectMeasures size:" + projectMeasures.size());
			System.out.println("pull end");
			String oldMonth = newFileDepartment.getMonth().trim();
			if (oldMonth.length() == 6) {
				String newMonth = "";
				if (snapshots != null && snapshots.size() > 0) {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
					newMonth = simpleDateFormat.format(snapshots.get(0).getBuild_date());
					newFileDepartment.setMonth(newMonth);
				}
				System.out.println("change date format " + oldMonth + " to " + newMonth);

			}

			boolean doflag = false;

			ConnectionBase.setDataSource("h");
			String curr_value = parametersDao.getValueByMonth(ConnectionBase.conn, newFileDepartment);// ��ǰ�ܲ�����汾��
			System.out.println("compare  date curr_value " + curr_value + "|new_value:" + new_value);
			if (curr_value.equals("")) {
				System.out.println("new time and new Data,do it!");
				doflag = true;
			} else if (Integer.valueOf(curr_value) < Integer.valueOf(new_value)) {
				System.out.println("old time and new Data version,do it!");
				doflag = true;
			} else {
				System.out.println("old time and old Data ,can not input!");
				doflag = false;
			}

			if (doflag) {
				ConnectionBase.setDataSource("h");
				// init
				ConnectionBase.conn.setAutoCommit(false);
				parametersDao.updateValue(ConnectionBase.conn, newFileDepartment, "1");
				projectDao.deleteSSMProjects(ConnectionBase.conn, newFileDepartment);
				if (!curr_value.equals("")) {
					snapshotDao.deleteSSMSnapshotsByMonth(ConnectionBase.conn, newFileDepartment);
					projectMeasureDao.deleteSSMProjectMeasuresByMonth(ConnectionBase.conn, newFileDepartment);
				}
				snapshotDao.updateSSMSnapshotsFlag(ConnectionBase.conn, newFileDepartment);

				System.out.println("init end");

				// push
				projectDao.insertSSMProjects(ConnectionBase.conn, projects);
				snapshotDao.insertSSMSnapshots(ConnectionBase.conn, snapshots, newFileDepartment);
				projectMeasureDao.insertSSMProjectMeasures(ConnectionBase.conn, projectMeasures, newFileDepartment);

				parametersDao.updateValue(ConnectionBase.conn, newFileDepartment, new_value);
				ConnectionBase.conn.commit();
				ConnectionBase.conn.setAutoCommit(true);
				System.out.println("push end");
				// push end
			}
		}
	}

	public static void usage(String info) {
		System.out.println(info);
		System.out.println("java [option] -jar BatchSonarSort-1.0.jar");
		System.out.println("    -Dtype             total flag ,must");
		System.out.println("    -Ddepartment  set statistics department,must!");
		System.out.println("    -Dproject         project_name must!");
		System.out.println("    -Dversion         project version");
		System.out.println("    -Dft                 ds1,ds2  ds1 to ds2  ds1=j  ds2=h");
		System.out.println(
				"eg: java -Dtype=qz -Ddepartment=beijing_cmc -Dproject=crm -Dversion=1.0 -jar AnalysisTotal-1.0.jar");
		System.exit(99);
	}

	public static Department getDepartment() {
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyyMMdd");

		String departmentstr = System.getProperty("department");
		String project_name = System.getProperty("project");
		String version = System.getProperty("version");
		String month = System.getProperty("month");
		if (month == null || month.equals("") || month.length() <= 6) {
			month = simpleDateFormat2.format(new Date());
		}

		if (departmentstr == null || departmentstr.equals("")) {
			usage("department is null");
		}

		if (project_name == null || project_name.equals("")) {
			usage("project_name is null");
		}
		Department department = new Department();
		department.setDepartment(departmentstr);
		department.setProject_name(project_name);
		department.setProject_version(version);
		department.setMonth(month);
		System.out.println(department.toString());
		return department;
	}

	public static void main(String[] args) throws Exception {

		// // test ��ȫ�����ȡ���ϴ��ļ��Ŀ�
		// {
		// System.setProperty("type", "qz");
		// System.setProperty("department", "PRD_zhejiang_billing_upc");
		// System.setProperty("project",
		// "PRD_zhejiang_billing_upc_page_template");
		// System.setProperty("version", "1.0");
		// System.setProperty("ft", "h,j");
		// // System.setProperty("month", "20170619");
		// System.setProperty("flag", "1");
		// }

		// test �뵽���Ŀ�
		// {
		// System.setProperty("type", "zq");
		// System.setProperty("dbfile", "sonarup");
		// System.setProperty("ft", "h,j");
		// System.setProperty("month", "20170616");
		// }

		String type = System.getProperty("type");
		if (type == null || type.equals("")) {
			usage("type can not be null");
		}
		String flag = System.getProperty("flag");
		if (flag == null || flag.equals("")) {
			flag = "0";
		}

		if (type.equalsIgnoreCase("qz")) {
			Department department = getDepartment();
			String dbRoute = System.getProperty("ft");
			if (dbRoute == null || dbRoute.equals("")) {
				dbRoute = "h,j";
			}
			String ftS[] = dbRoute.split(",");
			if (ftS.length != 2) {
				usage("ft fomat is wrong");
			}
			String ds1 = ftS[0];
			String ds2 = ftS[1];
			analysisTotal(department, ds1, ds2, flag);// done code
			System.out.println("done success!");

		} else if (type.equalsIgnoreCase("zq")) {
			String dbfile = System.getProperty("dbfile");
			String month = System.getProperty("month");

			if (dbfile == null || dbfile.equals("")) {
				usage("dbfile is null");
			}

			analysisTotalDbfile(dbfile, month);
			System.out.println("done success!");
		} else {
			usage("no deal with type" + type);
		}
	}

	public static void analysisFileLines(Connection conn, Department department) {

		ProjectMeasureDao projectMeasureDao = new ProjectMeasureDao();
		String JsonContext = FileUtil.ReadFile("./lines-report.json");// lines
																		// report
		JSONObject jsonObject = JSONObject.fromObject(JsonContext);
		JSONArray jsonArray = jsonObject.getJSONArray("lines report");
		System.out.println("--------json.size--------" + jsonArray.size());
		Map<String, ProjectMeasure> projectMeasuremap = projectMeasureDao.querySingleSSMProjectMeasure(conn,
				department);
		System.out.println("---map.size--" + projectMeasuremap.size());
		List<ProjectMeasure> projectMeasurelist = new ArrayList<ProjectMeasure>();
		for (int i = 0; i < jsonArray.size(); i++) {
			int lines = Integer.parseInt((String) jsonArray.getJSONObject(i).get("lines"));
			String fileName = (String) jsonArray.getJSONObject(i).get("file");
			String key = fileName.substring(0, fileName.indexOf("/"));
			String longname = fileName.substring(fileName.indexOf("/") + 1);
			String kee = key + ":" + longname;
			ProjectMeasure projectMeasure = projectMeasuremap.get(kee);
			if (projectMeasure != null) {
				projectMeasure.setMetric_id(2);
				projectMeasure.setValue(new BigDecimal(lines));
				projectMeasurelist.add(projectMeasure);
				projectMeasuremap.remove(kee);
			}

		}
		System.out.println("---map.size--" + projectMeasuremap.size());

		System.out.println("---list.size--" + projectMeasurelist.size());

		projectMeasureDao.insertSSMProjectMeasures(conn, projectMeasurelist, department);
		System.out.println(department.getMonth() + ":projectMeasurelist:" + projectMeasurelist.size());

	}
}
