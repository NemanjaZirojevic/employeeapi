package com.cpaglobal.employeeservice.services;

import com.cpaglobal.employeeservice.exceptions.ResourceNotFoundException;
import com.cpaglobal.employeeservice.model.Employee;

import java.util.List;



public interface EmployeeService {

    List<Employee> findEmployeesByCompanyName(String companyName) throws ResourceNotFoundException;
    List<Employee> findNearestTwoPersonsForEmployee(String employeeName) throws ResourceNotFoundException;


}
