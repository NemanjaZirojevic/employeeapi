package com.cpaglobal.employeeservice.web.controllers;

import com.cpaglobal.employeeservice.exceptions.ResourceNotFoundException;
import com.cpaglobal.employeeservice.model.Address;
import com.cpaglobal.employeeservice.model.Company;
import com.cpaglobal.employeeservice.model.Employee;
import com.cpaglobal.employeeservice.model.Geolocation;
import com.cpaglobal.employeeservice.services.EmployeeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("controllers")
@DisplayName("Employee controller - ")
@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    Employee validEmployee, validNearEmployeeOne, validNearEmployeeTwo;

    Address validAddress, validAddressOne, validAddressTwo;

    Company validCompany, validCompanyOne, validCompanyTwo;

    Geolocation validGeolocation, secondValidGeolocation, firstValidGeoLocation;


    List<Employee> validEmployeeList;
    List<Employee> validNearEmployeeList;

    private static final String COMPANY_NAME_NOT_EXIST = "wrong_company_name";
    private static final String EMPLOYEE_NAME_NOT_EXIST = "employee_name_not_exist";


    @BeforeEach
    void setUp() {
        validGeolocation = Geolocation.builder()
                .lat(-37.3159)
                .log(81.1496)
                .build();

        secondValidGeolocation = Geolocation.builder()
                .lat(-38.2386)
                .log(57.2232)
                .build();

        firstValidGeoLocation = Geolocation.builder()
                .lat(-37.3159)
                .log(81.1496)
                .build();

        validAddress = Address.builder()
                .city("Gwenborough")
                .street("Kulas Light")
                .suite("Apt. 556")
                .zipcode("92998-3874")
                .geoLocation(validGeolocation)
                .build();

        validAddressOne = Address.builder()
                .city("Roscoeview")
                .street("Skiles Walks")
                .suite("Suite 351")
                .zipcode("33263")
                .geoLocation(firstValidGeoLocation)
                .build();

        validAddressTwo = Address.builder()
                .city("Roscoeview")
                .street("Kattie Turnpike")
                .suite("Suite 198")
                .zipcode("31428-2261")
                .geoLocation(secondValidGeolocation)
                .build();

        validCompany = Company.builder()
                .name("Romaguera-Crona")
                .catchPhrase("Multi-layered client-server neural-net")
                .bs("harness real-time e-markets")
                .build();

        validCompanyOne = Company.builder()
                .name("Keebler LLC")
                .catchPhrase("User-centric fault-tolerant solution")
                .bs("revolutionize end-to-end systems")
                .build();

        validCompanyTwo = Company.builder()
                .name("Hoeger LLC")
                .catchPhrase("Centralized empowering task-force")
                .bs("target end-to-end models")
                .build();

        validEmployee = Employee.builder().
                id(1)
                .name("Leanne Graham")
                .username("Bret")
                .email("Sincere@april.biz")
                .phone("1-770-736-8031 x56442")
                .website("hildegard.org")
                .address(validAddress)
                .company(validCompany)
                .build();

        validNearEmployeeOne = Employee.builder().
                id(5)
                .name("Chelsey Dietrich")
                .username("Kamren")
                .email("Lucio_Hettinger@annie.ca")
                .phone("(254)954-1289")
                .website("demarco.info")
                .address(validAddressOne)
                .company(validCompanyOne)
                .build();

        validNearEmployeeTwo = Employee.builder().
                id(10)
                .name("Clementina DuBuque")
                .username("Moriah.Stanton")
                .email("Rey.Padberg@karina.bi")
                .phone("024-648-3804")
                .website("ambrose.net")
                .address(validAddressTwo)
                .company(validCompanyTwo)
                .build();

        validEmployeeList = Arrays.asList(validEmployee);
        validNearEmployeeList = Arrays.asList(validNearEmployeeOne, validNearEmployeeTwo);
    }

    @Test
    @DisplayName("Test getNearestEmployees method throws exception")
    void getUserByCompanyNameThrowResourceNotFoundException() throws Exception {
        //given
        given(employeeService.findEmployeesByCompanyName(COMPANY_NAME_NOT_EXIST))
                .willThrow(new ResourceNotFoundException("No employees found for company with name " + COMPANY_NAME_NOT_EXIST + "!"));

        //when
        mockMvc.perform(get("/api/v1/employees/company/" + COMPANY_NAME_NOT_EXIST))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


    }

    @Test
    @DisplayName("Test getNearestEmployees method without exception")
    void getNearestEmployeesForEmployeeWithoutException() throws Exception {
        given(employeeService.findEmployeesByCompanyName(validEmployee.getCompany().getName()))
                .willReturn(validEmployeeList);
        mockMvc.perform(get("/api/v1/employees/company/" + validEmployee.getCompany().getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(validEmployee.getId())))
                .andExpect(jsonPath("$[0].address.street", is(validEmployee.getAddress().getStreet())))
                .andReturn();

    }

    @Test
    @DisplayName("Test getNearestEmployeesForEmployee throws exception")
    void getNearestEmployeesForEmployeeTestThrowsException() throws Exception {
        given(employeeService.findNearestTwoPersonsForEmployee(EMPLOYEE_NAME_NOT_EXIST))
                .willThrow(new ResourceNotFoundException("User with name " + EMPLOYEE_NAME_NOT_EXIST + " not found!"));

        mockMvc.perform(get("/api/v1/employees/near/employee/" + EMPLOYEE_NAME_NOT_EXIST))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    @DisplayName("Test getNearestEmployeesForEmployee returns nearest employees")
    void getNearestEmployeesForEmployeeTest() throws Exception {
        given(employeeService.findNearestTwoPersonsForEmployee(validEmployee.getName()))
                .willReturn(validNearEmployeeList);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/employees/near/employee/" + validEmployee.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id",is(validNearEmployeeOne.getId())))
                .andExpect(jsonPath("$[1].id",is(validNearEmployeeTwo.getId())))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @AfterEach
    void tearDown() {
        reset(employeeService);
    }
}