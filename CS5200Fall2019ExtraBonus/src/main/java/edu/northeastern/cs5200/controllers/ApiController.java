package edu.northeastern.cs5200.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.northeastern.cs5200.services.TableService;
import edu.northeastern.cs5200.services.TableService;

public class ApiController {
	@Controller
	public class APIController {
		
		@Autowired
		TableService service;
		
		@PostMapping("/api/{table}")
		public String createTable(@PathVariable("table") String table_name, 
				@RequestBody String body) {
			if (service.checkTableExist(table_name)) {
				JSONObject jo = new JSONObject(body);
				for (String field: jo.keySet()) {
					if (!service.checkFieldExist(table_name, field)) {
						service.updateTable(table_name, field);
					}
				}
			} else {
				service.createTable(table_name, body);
			}
			String res = service.insertData(table_name, body);
			return res == null ? "Null" : res;
		}
		
		@PostMapping("/api/{table1}/{id1}/{table2}/{id2}")
		public String createMappingTable(@PathVariable("table1") String source_name, 
				@PathVariable("id1") int source_id, 
				@PathVariable("table2") String target_name, 
				@PathVariable("id2") int target_id) {
			String map_table = source_name + "_" + target_name;
			if (!service.checkTableExist(map_table)) {
				service.createMappingTale(source_name, source_id, target_name, target_id);			
			}
			return service.insertMappingData(map_table, source_name, source_id, target_name, target_id);
		}
		
		@GetMapping("/api/{table}")
		public String findAllRecords(@PathVariable("table") String table_name) {
			String res = service.searchAllData(table_name);
			return res == null ? "Null" : res;
		}
		
		@GetMapping("/api/{table}/{id}")
		public String findRecordById(@PathVariable("table") String table_name, 
				@PathVariable("id") int id) {
			String res = service.searchDataById(table_name, id);
			return res == null ? "Null" : res;
		}
		
		@GetMapping("/api/{table1}/{id1}/{table2}")
		public String findMappingData(@PathVariable("table1") String source_name, 
				@PathVariable("id1") int source_id, 
				@PathVariable("table2") String target_name) {
			String map_table = source_name + "_" + target_name;
			if (!service.checkTableExist(map_table)) {
				map_table = target_name + "_" + source_name;
			}
			return service.searchMappingTable(source_name, source_id, target_name, map_table);
		}
		@PutMapping("/api/{table}/{id}")
		public String updateRecord(@PathVariable("table") String table_name, 
				@PathVariable("id") int id, @RequestBody String body) {
			String res = service.updateData(table_name, id, body);
			return res == null ? "Null" : res;
		}
		
		@DeleteMapping("/api/{table}/{id}")
		public String removeRecord(@PathVariable("table") String table_name, 
				@PathVariable("id") int id) {
			String res = service.removeData(table_name, id);
			return res == null ? "Null" : res;
		}
		
		@DeleteMapping("/api/{table1}/{id1}/{table2}/{id2}")
		public String removeMappingRecord(@PathVariable("table1") String source_name, 
				@PathVariable("id1") int source_id, 
				@PathVariable("table2") String target_name, 
				@PathVariable("id2") int target_id) {
			return service.removeMappingData(source_name, source_id, target_name, target_id);
		}
		
		@DeleteMapping("/api/{table1}/{id1}/{table2}")
		public String removeAllMappingRecord(@PathVariable("table1") String source_name, 
				@PathVariable("id1") int source_id, 
				@PathVariable("table2") String target_name) {
			return service.removeAllMappingData(source_name, source_id, target_name);
		}
	}

}
