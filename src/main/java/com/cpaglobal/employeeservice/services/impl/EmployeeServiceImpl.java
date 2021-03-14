package com.cpaglobal.employeeservice.services.impl;

import com.cpaglobal.employeeservice.exceptions.ResourceNotFoundException;
import com.cpaglobal.employeeservice.model.Company;
import com.cpaglobal.employeeservice.model.Employee;
import com.cpaglobal.employeeservice.services.EmployeeService;
import com.cpaglobal.employeeservice.model.Address;
import com.cpaglobal.employeeservice.model.Geolocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Value("${API_URL}")
    private String USER_API;

    private final RestTemplate restTemplate;

    @Autowired
    public EmployeeServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Employee> findEmployeesByCompanyName(String companyName) throws ResourceNotFoundException {
        Employee[] userArray = callExternalApi();
        List<Employee> employees =
                 Arrays.stream(userArray)
                .filter(user -> user.getCompany().getName().equalsIgnoreCase(companyName))
                .collect(Collectors.toList());
        if (employees.size() == 0){
            throw new ResourceNotFoundException("No employees found for company with name " + companyName + "!");
        }
        return employees;
    }

    @Override
    public List<Employee> findNearestTwoPersonsForEmployee(String employeeName) throws ResourceNotFoundException {
        Employee[] userArray = callExternalApi();
        Employee employee = Arrays.stream(userArray)
                .filter(employeeDto -> employeeDto.getName().equalsIgnoreCase(employeeName))
                .findAny()
                .orElse(null);

        if (employee == null) {
            throw new ResourceNotFoundException("User with name " + employeeName + " not found!");
        }
        Geolocation userLocation = employee.getAddress().getGeoLocation();
        List<Geolocation> otherLocations =
                 Arrays.stream(userArray)
                        .map(Employee::getAddress)
                        .map(Address::getGeoLocation)
                        .collect(Collectors.toList());


        List<Geolocation> nearestLocations = userLocation.getNearestTwoPoints(otherLocations);
        return Arrays.stream(userArray)
                        .filter(employeeDto -> nearestLocations.stream()
                        .anyMatch(location ->
                                 location.equals(employeeDto.getAddress().getGeoLocation())))
                        .collect(Collectors.toList());
    }

    private Employee[]  callExternalApi(){
        ResponseEntity<Employee[]> responseEntity =   restTemplate.getForEntity(USER_API, Employee[].class);
        return responseEntity.getBody();
    }

}
