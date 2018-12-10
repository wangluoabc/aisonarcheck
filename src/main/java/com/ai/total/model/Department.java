package com.ai.total.model;

public class Department {
	private String department;
	private String project_name;
	private String project_version;
	private String month;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public String getProject_version() {
		return project_version;
	}

	public void setProject_version(String project_version) {
		this.project_version = project_version;
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("department:").append(this.department).append("|");
		stringBuilder.append("prj_name:").append(this.project_name).append("|");
		stringBuilder.append("prj_version:").append(this.project_version).append("|");
		stringBuilder.append("month:").append(this.month);
		return stringBuilder.toString();
	}
}
