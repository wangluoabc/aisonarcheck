package com.ai.total.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProjectMeasure {

	private String department;
	private String project_name;
	private String project_version;
	private Long id;
	private BigDecimal value;
	private Integer metric_id;
	private Integer snapshot_id;
	private String rule_id;
	private String rules_category_id;
	private String text_value;
	private String tendency;
	private Timestamp measure_date;
	private String project_id;
	private String alert_status;
	private String alert_text;
	private String url;
	private String description;
	private String rule_priority;
	private String characteristic_id;
	private BigDecimal variation_value_1;
	private BigDecimal variation_value_2;
	private BigDecimal variation_value_3;
	private BigDecimal variation_value_4;
	private BigDecimal variation_value_5;
	private String person_id;
	private byte[] measure_data;
	private String plugin_rule_key;
	private String plugin_name;
	private String month;
	
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getRules_category_id() {
		return rules_category_id;
	}

	public void setRules_category_id(String rules_category_id) {
		this.rules_category_id = rules_category_id;
	}

	public String getTendency() {
		return tendency;
	}

	public void setTendency(String tendency) {
		this.tendency = tendency;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getMetric_id() {
		return metric_id;
	}

	public void setMetric_id(Integer metric_id) {
		this.metric_id = metric_id;
	}

	public Integer getSnapshot_id() {
		return snapshot_id;
	}

	public void setSnapshot_id(Integer snapshot_id) {
		this.snapshot_id = snapshot_id;
	}

	public String getText_value() {
		return text_value;
	}

	public void setText_value(String text_value) {
		this.text_value = text_value;
	}

	public Timestamp getMeasure_date() {
		return measure_date;
	}

	public void setMeasure_date(Timestamp measure_date) {
		this.measure_date = measure_date;
	}

	public String getAlert_status() {
		return alert_status;
	}

	public void setAlert_status(String alert_status) {
		this.alert_status = alert_status;
	}

	public String getAlert_text() {
		return alert_text;
	}

	public void setAlert_text(String alert_text) {
		this.alert_text = alert_text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getVariation_value_1() {
		return variation_value_1;
	}

	public void setVariation_value_1(BigDecimal variation_value_1) {
		this.variation_value_1 = variation_value_1;
	}

	public BigDecimal getVariation_value_2() {
		return variation_value_2;
	}

	public String getRule_id() {
		return rule_id;
	}

	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}

	public String getRule_priority() {
		return rule_priority;
	}

	public void setRule_priority(String rule_priority) {
		this.rule_priority = rule_priority;
	}

	public String getCharacteristic_id() {
		return characteristic_id;
	}

	public void setCharacteristic_id(String characteristic_id) {
		this.characteristic_id = characteristic_id;
	}

	public void setVariation_value_2(BigDecimal variation_value_2) {
		this.variation_value_2 = variation_value_2;
	}

	public BigDecimal getVariation_value_3() {
		return variation_value_3;
	}

	public void setVariation_value_3(BigDecimal variation_value_3) {
		this.variation_value_3 = variation_value_3;
	}

	public BigDecimal getVariation_value_4() {
		return variation_value_4;
	}

	public void setVariation_value_4(BigDecimal variation_value_4) {
		this.variation_value_4 = variation_value_4;
	}

	public BigDecimal getVariation_value_5() {
		return variation_value_5;
	}

	public void setVariation_value_5(BigDecimal variation_value_5) {
		this.variation_value_5 = variation_value_5;
	}

 

	public String getPerson_id() {
		return person_id;
	}

	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}

	public byte[] getMeasure_data() {
		return measure_data;
	}

	public void setMeasure_data(byte[] measure_data) {
		this.measure_data = measure_data;
	}

	public String getPlugin_rule_key() {
		return plugin_rule_key;
	}

	public void setPlugin_rule_key(String plugin_rule_key) {
		this.plugin_rule_key = plugin_rule_key;
	}

	public String getPlugin_name() {
		return plugin_name;
	}

	public void setPlugin_name(String plugin_name) {
		this.plugin_name = plugin_name;
	}

}
