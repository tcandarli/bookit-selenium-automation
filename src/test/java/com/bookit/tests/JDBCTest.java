package com.bookit.tests;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.bookit.utilities.DBUtils;

public class JDBCTest {

	String dbUrl = "jdbc:postgresql://localhost:5432/hr";
	String dbUsername = "postgres";
	String dbPassword = "abc";

	@Test
	public void PostGreSQL() throws SQLException {

		Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = statement.executeQuery("Select * from countries");

		// next method -> move pointer to next row

//		while (resultset.next()) {
//			System.out.println(
//					resultset.getString(1) + " - " + resultset.getString("country_name") + " - " + resultset.getInt(3));
//		}

		resultset.next();
		System.out.println(
				resultset.getString(1) + " - " + resultset.getString(2) + " - " + resultset.getInt(3));

		resultset.next();
		System.out.println(
				resultset.getString(1) + " - " + resultset.getString("country_name") + " - " + resultset.getInt(3));

		resultset.next();
		resultset.next();
		resultset.next();

		System.out.println(resultset.getRow()); // Retrieves the current row number

		resultset.first();
		System.out.println(resultset.getRow());

		// find out how many record in the resultset?
		resultset.last();
		int rowCount = resultset.getRow();

		System.out.println("Total number of rows: " + rowCount);

		// how to move first row and loop everything again after you are at last row?
		resultset.beforeFirst();
		while (resultset.next()) {
			System.out.println(
					resultset.getString(1) + " - " + resultset.getString("country_name") + " - " + resultset.getInt(3));

		}

		resultset.close();
		statement.close();
		connection.close();

	}

	@Test(enabled = false)
	public void JDBCMetaData() throws SQLException {

		Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = statement.executeQuery("Select * from countries");

		// database metadata (create object)
		DatabaseMetaData dbMetaData = connection.getMetaData();

		// which username are we using?
		System.out.println("User: " + dbMetaData.getUserName());

		// database product name
		System.out.println("Database product name: " + dbMetaData.getDatabaseProductName());

		// database product version
		System.out.println("Database product version: " + dbMetaData.getDatabaseProductVersion());

		// -------------------------------------

		// resultset metata create object
		ResultSetMetaData rsMetaData = resultset.getMetaData();

		// how many columns we have?
		System.out.println("Columns count: " + rsMetaData.getColumnCount());

		// get column name
		System.out.println("First column name: " + rsMetaData.getColumnName(1));

		// get table name
		System.out.println("Table name: " + rsMetaData.getTableName(1));

		// print all column names using loop

		int columnCount = rsMetaData.getColumnCount();

		for (int i = 1; i <= columnCount; i++) {
			System.out.println(rsMetaData.getColumnName(i));
		}

		resultset.close();
		statement.close();
		connection.close();
	}

	@Test(enabled = false)
	public void DBUtil() throws SQLException {

		Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = statement
				.executeQuery("select first_name, last_name, salary, job_id from employees limit 5");

		// database metadata (create object)
		DatabaseMetaData dbMetaData = connection.getMetaData();

		// resultset metadata create object
		ResultSetMetaData rsMetaData = resultset.getMetaData();

		// our main structure, it will keep whole query result
		List<Map<String, Object>> queryData = new ArrayList<>();

		// we will add the first row data to this map
		Map<String, Object> row1 = new HashMap<>();
		// we will add the second row data to this map
		Map<String, Object> row2 = new HashMap<>();

		// point the first row
		resultset.next();

		// key is column name, value is value of that column
//		row1.put("first_name", "Steven");
//		row1.put("last_name", "King");
//		row1.put("Salary", 24000);
//		row1.put("job_id", "AD_PRES");

		// get value from DB
		row1.put("first_name", resultset.getObject("first_name"));
		row1.put("last_name", resultset.getObject("last_name"));
		row1.put("Salary", resultset.getObject("salary"));
		row1.put("job_id", resultset.getObject("job_id"));

		resultset.next();
		// get value from DB
		row2.put("first_name", resultset.getObject("first_name"));
		row2.put("last_name", resultset.getObject("last_name"));
		row2.put("Salary", resultset.getObject("salary"));
		row2.put("job_id", resultset.getObject("job_id"));

		// verify map is keeping all values
		System.out.println(row1.toString());

		// push row1 and row2map to list as a whole row
		queryData.add(row1);
		queryData.add(row2);

		System.out.println("First row: " + queryData.get(0).toString());
		System.out.println("First row: " + queryData.get(1).toString());

		resultset.close();
		statement.close();
		connection.close();
	}

	@Test(enabled = false)
	public void DBUtilDynamic() throws SQLException {

		Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet resultset = statement.executeQuery("select * from countries");

		// database metadata (create object)
		DatabaseMetaData dbMetaData = connection.getMetaData();

		// resultset metadata create object
		ResultSetMetaData rsMetaData = resultset.getMetaData();

		// -------------DYNAMIC LIST FOR EVERY QUERY ----------------

		// Main List
		List<Map<String, Object>> queryData = new ArrayList<>();

		// number of columns
		int colCount = rsMetaData.getColumnCount();

		while (resultset.next()) {

			// creating map to adding each row inside
			Map<String, Object> row = new HashMap<>();

			for (int i = 1; i <= colCount; i++) {

				row.put(rsMetaData.getColumnName(i), resultset.getObject(i));

			}
			queryData.add(row);
		}

//			System.out.println(queryData.get(0));
//			System.out.println(queryData.get(1));
//			System.out.println(queryData.get(2));
//			System.out.println(queryData.get(3));

		// print each row from the list
		for (Map<String, Object> map : queryData) {
			System.out.println(map.get("country_name"));
		}

		resultset.close();
		statement.close();
		connection.close();
	}

	@Test(enabled = false)
	private void useDBUtils() {

		// create connection with given information
		DBUtils.createConnection();

		String query = "SELECT first_name, last_name, salary, job_id FROM employees";

		List<Map<String, Object>> queryData = DBUtils.getQueryResultMap(query);

		// print first row salary value
		System.out.println(queryData.get(0).get("salary"));

		// close connection
		DBUtils.destroy();

	}

	@Test(enabled = false)
	private void useDBUtils2() {

		// create connection with given information
		DBUtils.createConnection();

		String query = "SELECT first_name, last_name, salary, job_id FROM employees where employee_id = 107";

		Map<String, Object> oneRowResult = DBUtils.getRowMap(query);

		// print first row salary value
		System.out.println(oneRowResult.get("job_id"));

		// close connection
		DBUtils.destroy();

	}

}
