package edu.northeastern.cs5200.services;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.northeastern.cs5200.MyConnection;
@Service
public class TableService {

	
		
		private PreparedStatement ps = null;
		private Statement s = null;
		private ResultSet results = null;
		
		
		public void createTable(String name, String body) {
			JSONObject jo = new JSONObject(body);
			StringBuilder sb = new StringBuilder();
			String base = "CREATE TABLE " + name 
					+ "  (`id` int not null auto_increment,   ";
			sb.append(base);
			for (String field: jo.keySet()) {
				String field_create = field + " varchar(200),   ";
				sb.append(field_create);
			}
			String end = "primary key(`id`))";
			sb.append(end);
			System.out.println("Create table: " + sb.toString());
			try {
				s = MyConnection.getConnection().createStatement();
				s.executeUpdate(sb.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public String createMappingTale(String source_name, int source_id, String target_name, int target_id) {
			if (searchDataById(source_name, source_id) == null || 
					searchDataById(target_name, target_id) == null) {
				return null;
			}
			String map_table = source_name + "_" + target_name;
			String create = "CREATE TABLE " + map_table 
					+ " (id int not null auto_increment, "
					+ source_name + " int, "
					+ target_name + " int, "
					+ "primary key(id), "
					+ "foreign key(" + source_name + ") "
					+ "references " + source_name + "(id), "
					+ "foreign key(" + target_name + ") "
					+ "references " + target_name + "(id))";
			System.out.println("Create mapping table: " + create);
			try {
				s = MyConnection.getConnection().createStatement();
				s.executeUpdate(create);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return map_table;
		}
		
		public void updateTable(String name, String field_name) {
			String sql = "ALTER TABLE " + name + " ADD COLUMN " + field_name
					+ "  VARCHAR(200)";
			System.out.println("Alter table: " + sql);
			try {
				s = MyConnection.getConnection().createStatement();
				s.executeUpdate(sql);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public boolean checkTableExist(String name) {
			try {
				DatabaseMetaData meta = MyConnection.getConnection().getMetaData();
				results = meta.getTables(null, "hw6", name, null);
				if (results.next()) {
					return true;
				} else {
					return false;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		public boolean checkFieldExist(String table_name, String field_name) {
			try {
				DatabaseMetaData meta = MyConnection.getConnection().getMetaData();
				results = meta.getColumns(null, "hw6", table_name, field_name);
				if (results.next()) {
					return true;
				} else {
					return false;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		
		public String updateData(String table_name, int id, String body) {
			if (searchDataById(table_name, id) == null) {
				return null;
			}
			JSONObject jo = new JSONObject(body);
			String field_name = jo.keys().next();
			if (!checkFieldExist(table_name, field_name)) {
				updateTable(table_name, field_name);
			}
			String update = "UPDATE " + table_name + " SET " + field_name
					+ "=" + "'" + jo.getString(field_name) + "'" + " WHERE id="
					+ String.valueOf(id);
			System.out.println("Update data: " + update);
			try {
				s = MyConnection.getConnection().createStatement();
				s.executeUpdate(update);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return searchDataById(table_name, id);
		}
		
		public String removeData(String table_name, int id) {
			if (searchDataById(table_name, id) == null) {
				return null;
			}
			String remove = "DELETE FROM " + table_name + " WHERE id=" + String.valueOf(id);
			System.out.println("Remove data: " + remove);
			int result = 0;
			try {
				s = MyConnection.getConnection().createStatement();
				result = s.executeUpdate(remove);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (result > 0) {
				return "Remove succeed with rows affected: " + String.valueOf(result);
			} else {
				return "Ops, something wrong with the remove action";
			}
		} 
		
		public String removeMappingData(String source_name, int source_id, 
				String target_name, int target_id) {
			String map_table = source_name + "_" + target_name;
			String remove = "DELETE FROM " + map_table + " WHERE "
					+ source_name + "=" + String.valueOf(source_id) + " AND "
					+ target_name + "=" + String.valueOf(target_id);
			System.out.println("Remove one mapping data: " + remove);
			int result = 0;
			try {
				s = MyConnection.getConnection().createStatement();
				result = s.executeUpdate(remove);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (result > 0) {
				return "Remove succeed with rows affected: " + String.valueOf(result);
			} else {
				return "Ops, something wrong with the remove action";
			}
		}
		
		public String removeAllMappingData(String source_name, int source_id, 
				String target_name) {
			String map_table = source_name + "_" + target_name;
			String remove = "DELETE FROM " + map_table + " WHERE "
					+ source_name + "=" + String.valueOf(source_id);
			System.out.println("Remove all mapping data: " + remove);
			int result = 0;
			try {
				s = MyConnection.getConnection().createStatement();
				result = s.executeUpdate(remove);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (result > 0) {
				return "Remove succeed with rows affected: " + String.valueOf(result);
			} else {
				return "Ops, something wrong with the remove action";
			}
		}
		
		
		
		
		public String insertData(String name, String body) {
			String base = "INSERT INTO " + name + "(";
			String value = "VALUE(";
			JSONObject jo = new JSONObject(body);
			int size = jo.keySet().size();
			int count = 0;
			for (String fieldName: jo.keySet()) {
				if (count != size - 1) {
					base += fieldName + ",";
					value += "'" + jo.getString(fieldName) + "'" + ",";
				} else {
					base += fieldName + ") ";
					value += "'" + jo.getString(fieldName) + "'" + ")";
				}
				count++;
			}
			String sql = base + value;
			System.out.println("Insert data: " + sql);
			try {
				s = MyConnection.getConnection().createStatement();
				s.executeUpdate(sql);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return searchDataByAllFields(name, body);
		}
		
		public String insertMappingData(String table_name, String source_name, int source_id,
				String target_name, int target_id) {
			String insert = "INSERT INTO " + table_name + "(" 
					+ source_name + ", " + target_name + ") " 
					+ "VALUE(" + String.valueOf(source_id) + ", "
					+ String.valueOf(target_id) + ")";
			System.out.println("Insert data: " + insert);
			try {
				s = MyConnection.getConnection().createStatement();
				s.executeUpdate(insert);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String body = new JSONObject()
								.put(source_name, String.valueOf(source_id))
								.put(target_name, String.valueOf(target_id)).toString();
			return searchDataByAllFields(table_name, body);
		}
		
		public String searchMappingTable(String source_name, int source_id, 
				String target_name, String map_table) {
			String query = "SELECT " + target_name + " FROM " + map_table
					+ " WHERE " + source_name + "=" + String.valueOf(source_id);
			System.out.println("Search mapping table: " + query);
			ArrayList<String> res = new ArrayList<>();
			try {
				s = MyConnection.getConnection().createStatement();
				results = s.executeQuery(query);
				if (!results.isBeforeFirst()) {
					return null;
				}
				JSONArray ja = resultSetToJSONArray(results);
				ja.forEach(item -> {
					if (item instanceof JSONObject) {
						JSONObject jo = (JSONObject) item;
						int id = jo.getInt(target_name);
						res.add(searchDataById(target_name, id));
					}
				});
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (res.isEmpty()) {
				return null;
			} else {
				return res.toString();			
			}
		}
		
		public String searchDataById(String table_name, int id) {
			if (!checkTableExist(table_name)) {
				return null;
			}
			JSONObject json = null;
			String query = "SELECT * FROM " + table_name + " WHERE id=" 
					+ String.valueOf(id);
			System.out.println("Search by Id: " + query);
			try {
				s = MyConnection.getConnection().createStatement();
				results = s.executeQuery(query);
				if (!results.isBeforeFirst()) {
					return null;
				}
				json = resultSetToJSONArray(results).getJSONObject(0);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (json == null) {
				return null;
			} else {			
				return json.toString();
			}
		}
		
		public String searchAllData(String table_name) {
			if (!checkTableExist(table_name)) {
				return null;
			}
			String query = "SELECT * FROM " + table_name;
			System.out.println("Search all: " + query);
			JSONArray json = null;
			try {
				s = MyConnection.getConnection().createStatement();
				results = s.executeQuery(query);
				if (!results.isBeforeFirst()) {
					return null;
				}
				json = resultSetToJSONArray(results);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (json == null) {
				return null;
			} else {			
				return json.toString();
			}
		}
		
		public String searchDataByAllFields(String table_name, String body) {
			if (!checkTableExist(table_name)) {
				return null;
			}
			JSONObject json = new JSONObject(body);
			String query = "SELECT * FROM " + table_name + " WHERE ";
			int size = json.keySet().size();
			int count = 0;
			for (String field: json.keySet()) {
				if (count < size - 1) {
					query += field + "=" + "'" + json.getString(field) + "'" + " AND "; 
				} else {
					query += field + "=" + "'" + json.getString(field) + "'";
				}
				count++;
			}
			System.out.println("Search by all fields: " + query);
			JSONObject res = null;
			try {
				s = MyConnection.getConnection().createStatement();
				results = s.executeQuery(query);
				if (!results.isBeforeFirst()) {
					return null;
				}
				res = resultSetToJSONArray(results).getJSONObject(0);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (res == null) {
				return null;
			} else {			
				return res.toString();
			}
			
			
		}
		
		public String searchDataByFieldName(String table_name, String field_name, String field_value) {
			if (!checkTableExist(table_name)) {
				return null;
			}
			String query = "SELECT * FROM " + table_name + " WHERE " 
					+ field_name + "=" + "'" + field_value + "'";
			System.out.println("Search by field name: " + query);
			JSONArray json = null;
			try {
				s = MyConnection.getConnection().createStatement();
				results = s.executeQuery(query);
				if (!results.isBeforeFirst()) {
					return null;
				}
				json = resultSetToJSONArray(results);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (json == null) {
				return null;
			} else {			
				return json.toString();
			}
		}
		
		 private JSONArray resultSetToJSONArray( ResultSet rs ) throws SQLException, JSONException {
			 JSONArray json = new JSONArray();
			 ResultSetMetaData rsmd = rs.getMetaData();

				    while(rs.next()) {
				      int numColumns = rsmd.getColumnCount();
				      JSONObject obj = new JSONObject();

				      for (int i=1; i<numColumns+1; i++) {
				        String column_name = rsmd.getColumnName(i);

				        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
				         obj.put(column_name, rs.getArray(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
				         obj.put(column_name, rs.getInt(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
				         obj.put(column_name, rs.getBoolean(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
				         obj.put(column_name, rs.getBlob(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
				         obj.put(column_name, rs.getDouble(column_name)); 
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
				         obj.put(column_name, rs.getFloat(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
				         obj.put(column_name, rs.getInt(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
				         obj.put(column_name, rs.getNString(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
				         obj.put(column_name, rs.getString(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
				         obj.put(column_name, rs.getInt(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
				         obj.put(column_name, rs.getInt(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
				         obj.put(column_name, rs.getDate(column_name));
				        }
				        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
				        obj.put(column_name, rs.getTimestamp(column_name));   
				        }
				        else{
				         obj.put(column_name, rs.getObject(column_name));
				        }
				      }

				      json.put(obj);
				    }

				    return json;
				  }
	

}
