package com.nisum.jpa.test;


import com.nisum.jpa.entity.EmployeeEntity;
import com.nisum.jpa.exception.RecordNotFoundException;
import com.nisum.jpa.repository.EmployeeRepository;
import com.nisum.jpa.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {

	@Autowired
	private EmployeeService employeeService;
	
	@MockBean
	private EmployeeRepository employeeRepository;
	
	@Before
	public void setUp() {
		
		//setup for findById()
		EmployeeEntity dummyEmployee=new EmployeeEntity(1L, "padmaja", "vinod", "padmajavinodch@gmail.com");
		when(employeeRepository.findById(1L)).thenReturn(Optional.of(dummyEmployee));
		
		//setup for findAll()
		List<EmployeeEntity> dummyEmployees=Arrays.asList(dummyEmployee);
		when(employeeRepository.findAll()).thenReturn(dummyEmployees);
		
		//setup for deleteById()
		doNothing().when(employeeRepository).deleteById(Mockito.anyLong());
		doThrow(RecordNotFoundException.class).when(employeeRepository).deleteById(100L);
		
		//setup for createOrUpdate(): create operation
		EmployeeEntity dummyCreateEmployee=new EmployeeEntity("padmaja", "vinod", "padmajavinodch@gmail.com");
		when(employeeRepository.save(Mockito.any(EmployeeEntity.class))).thenReturn(dummyEmployee);
		
		//setup for createOrUpdate(): update operation
		EmployeeEntity dummyUpdateEmployee=new EmployeeEntity(1L, "padmaja", "vinnu", "pgundeboina@nisum.com");
		when(employeeRepository.save(Mockito.any(EmployeeEntity.class))).thenReturn(dummyUpdateEmployee);
	}
	
	@Test
	public void createOrUpdate_CreateTest() {
		
		EmployeeEntity returnedEntity=employeeService.createOrUpdateEmployee(new EmployeeEntity("padhu", "vinnu", "padmajavinodch@gmail.com"));
		assertThat(returnedEntity).isNotNull();
		assertThat(returnedEntity.getId()).isEqualTo(1L);
	}
	
	@Test
	public void createOrUpdate_UpdateTest() {
		
		EmployeeEntity returnedEntity=employeeService.createOrUpdateEmployee(new EmployeeEntity(1L,"padmaja", "vinod", "pgundeboina@gmail.com"));
		assertThat(returnedEntity).isNotNull();
		assertThat(returnedEntity.getId()).isEqualTo(1L);
		assertThat(returnedEntity.getFirstName()).isEqualTo("padmaja");
	}
	
	@Test
	public void getEmployeeByIdTest() throws RecordNotFoundException {
		
		EmployeeEntity returnedEmployee=employeeService.getEmployeeById(1L);
		assertThat(returnedEmployee.getFirstName()).isEqualTo("padhu");
	}
	
	@Test(expected = RecordNotFoundException.class)
	public void getEmployeeByIdExceptionTest() throws RecordNotFoundException {
		
		employeeService.getEmployeeById(100L);
	}
	
	@Test
	public void getAllEmployeesTest() {
		
		List<EmployeeEntity> allEmployeesReturned=employeeService.getAllEmployees();
		assertThat(allEmployeesReturned).isNotEmpty();
		assertThat(allEmployeesReturned.size()).isEqualTo(1);
		assertThat(allEmployeesReturned.get(0)).isEqualTo(new EmployeeEntity(1L, "padhu", "vnnu", "padmajavinodch@gmail.com"));
	}
	
	@Test
	public void getAllEmployeesEmptyTest() {
		
		List<EmployeeEntity> dummyEmptyEmployees=new ArrayList<EmployeeEntity>();
		when(employeeRepository.findAll()).thenReturn(dummyEmptyEmployees);
		
		List<EmployeeEntity> allEmployeesReturned=employeeService.getAllEmployees();
		assertThat(allEmployeesReturned).isEmpty();
		assertThat(allEmployeesReturned.size()).isEqualTo(0);
	} 
	
	@Test
	public void deleteEmployeeByIdTest() throws RecordNotFoundException {
		
		employeeService.deleteEmployeeById(1L);
		Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(1L);
	}
	
	@Test(expected = RecordNotFoundException.class)
	public void deleteEmployeeByIdExceptionTest() throws RecordNotFoundException {
		
		employeeService.deleteEmployeeById(100L);
		Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(100L);;
	}
}
