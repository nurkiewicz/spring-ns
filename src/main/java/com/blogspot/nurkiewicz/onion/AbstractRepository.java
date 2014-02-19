package com.blogspot.nurkiewicz.onion;

import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public class AbstractRepository {
	private String entityName;
	private String table;
	private DataSource dataSource;
	private PlatformTransactionManager transactionManager;

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void init() {
	}

	public void destroy() {
	}
}
