package edu.hust.hespi.factorymanagement.controller;

import edu.hust.hespi.factorymanagement.exception.CustomException;
import edu.hust.hespi.factorymanagement.model.dto.EmployeeDTO;
import edu.hust.hespi.factorymanagement.model.input.PageFilterInput;
import edu.hust.hespi.factorymanagement.model.response.CustomResponse;
import edu.hust.hespi.factorymanagement.model.response.PageResponse;
import edu.hust.hespi.factorymanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/search")
//    @ApiOperation("Search report")
    public PageResponse<List<EmployeeDTO>> search(
            @RequestBody PageFilterInput<EmployeeDTO> input
    ) {
        return employeeService.search(input);
    }

    @ExceptionHandler()
    @PostMapping()
    public CustomResponse<EmployeeDTO> create(
            @RequestBody EmployeeDTO input
    ) throws CustomException {
        return new CustomResponse<EmployeeDTO>().success(employeeService.create(input));
    }
}
