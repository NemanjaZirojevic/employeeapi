package com.cpaglobal.employeeservice.web.controllers;


import com.cpaglobal.employeeservice.exceptions.ResourceNotFoundException;
import com.cpaglobal.employeeservice.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/employees")
@RestController
public class EmployeeController {

   private EmployeeService employeeService;

   @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = {"/company/{companyName}"},
                produces = { "application/json" })
    public ResponseEntity getUserByCompanyName(@PathVariable("companyName") String companyName) throws ResourceNotFoundException {
        return new ResponseEntity<>(employeeService.findEmployeesByCompanyName(companyName), HttpStatus.OK);
    }

    @GetMapping(path = {"/near/employee/{employeeName}"},
                produces = { "application/json" })
    public ResponseEntity getNearestEmployeesForEmployee(@PathVariable("employeeName") String employeeName) throws ResourceNotFoundException {
        return new ResponseEntity<>(employeeService.findNearestTwoPersonsForEmployee(employeeName), HttpStatus.OK);
    }



}
