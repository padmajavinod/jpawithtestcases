package com.nisum.jpa.web;


import com.nisum.jpa.entity.EmployeeEntity;
import com.nisum.jpa.exception.RecordNotFoundException;
import com.nisum.jpa.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController
{
    @Autowired
    EmployeeService service;
 
    @GetMapping
    public ResponseEntity<List<EmployeeEntity>> getAllEmployees() {
        List<EmployeeEntity> list = service.getAllEmployees();
 
        return new ResponseEntity<List<EmployeeEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeEntity> getEmployeeById(@PathVariable("id") Long id)
                                                    throws RecordNotFoundException {
        EmployeeEntity entity = service.getEmployeeById(id);
 
        return new ResponseEntity<EmployeeEntity>(entity, new HttpHeaders(), HttpStatus.OK);
    }
 
    @PostMapping
    public ResponseEntity<EmployeeEntity> createOrUpdateEmployee(@RequestBody EmployeeEntity employee)
                                                    throws RecordNotFoundException {
        EmployeeEntity newEmployee = service.createOrUpdateEmployee(employee);
        ResponseEntity<EmployeeEntity> responseEntity=null;
        if(null!=employee.getId()) {
        	responseEntity=new ResponseEntity<EmployeeEntity>(newEmployee, new HttpHeaders(), HttpStatus.OK);
        }else {
			responseEntity=new ResponseEntity<EmployeeEntity>(newEmployee,new HttpHeaders(),HttpStatus.CREATED);
		}
        return responseEntity;
    }
 
    @DeleteMapping("/{id}")
    public HttpStatus deleteEmployeeById(@PathVariable("id") Long id)
                                                    throws RecordNotFoundException {
        service.deleteEmployeeById(id);
        return HttpStatus.ACCEPTED;
    }
 
}