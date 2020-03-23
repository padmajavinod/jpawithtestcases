package com.nisum.jpa.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.jpa.entity.EmployeeEntity;
import com.nisum.jpa.service.EmployeeService;
import com.nisum.jpa.web.EmployeeController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Before
	public void before() {
		
		EmployeeEntity dummyEmployee=new EmployeeEntity(1L, "padmaja", "vinod", "pgundeboina@nisum.com");
		List<EmployeeEntity> dummyEmployees=Arrays.asList(dummyEmployee);
		when(employeeService.getAllEmployees()).thenReturn(dummyEmployees);
		
		when(employeeService.getEmployeeById(Mockito.anyLong())).thenReturn(dummyEmployee);
		
		EmployeeEntity dummyPostEmployee=new EmployeeEntity(2L,"padmaja", "g", "padmajavinodch@gmail.com");
		when(employeeService.createOrUpdateEmployee(Mockito.any(EmployeeEntity.class))).thenReturn(dummyPostEmployee);
		
		//doNothing().when(employeeService).deleteEmployeeById(Mockito.anyLong());
		//when(employeeService.deleteEmployeeById(Mockito.anyLong())).
		//doThrow(RecordNotFoundException.class).when(employeeService).deleteEmployeeById(100L);
	}
	
	@Test
	public void getAllEmployeesTest() throws Exception {
		
		mockMvc.perform(get("/employees")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[*]").isNotEmpty())
		.andExpect(jsonPath("$[*].id").isNotEmpty())
	    .andExpect(jsonPath("$[0].firstName", is("padmaja")));
		
	}
	
	@Test
	public void getEmployeeByIdTest() throws Exception {
		
		mockMvc.perform(get("/employees/{id}",1)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L))
				.andExpect(jsonPath("$.email", is("pgundeboina@nisum.com")));
		
	}
	
	@Test
	public void createOrUpdateEmployee_CreateTest() throws Exception {
	
		mockMvc.perform(post("/employees")
				.content(objectToJsonString(new EmployeeEntity("padmaja", "vinod", "padmajavinodch@gmail.com")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.firstName",is("padmaja")));
	}
	
	public void createOrUpdateEmployee_UpdateTest() throws Exception {
		
		mockMvc.perform(put("/employees")
				.content(objectToJsonString(new EmployeeEntity(2L,"padhu","padhu","padmajavinodch@gmail.com")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.lasttName", is("padhu")))
				.andExpect(jsonPath("$.email", is("padmajavinodch@gmail.com")));
	}
	
	@Test
	public void deleteEmployeeByIdTest() throws Exception {
		
		mockMvc.perform(delete("/employees/{id}",2))
		.andReturn().equals(HttpStatus.ACCEPTED);
		
		/*
		 * Objects.equals(mockMvc.perform(delete("/employees/{id}",2))
		 * .andReturn(),HttpStatus.ACCEPTED);
		 */
	}
	
	/*
	 * @Test(expected = RecordNotFoundException.class) public void
	 * deleteEmployeeByIdExceptionTest() throws Exception {
	 * 
	 * mockMvc.perform(delete("/employees/{id}",100L));
	 * 
	 * }
	 */
	public static String objectToJsonString(final EmployeeEntity employee) throws JsonProcessingException {
		
		String jsonString=new ObjectMapper().writeValueAsString(employee);
		return jsonString;
	}
	
}
