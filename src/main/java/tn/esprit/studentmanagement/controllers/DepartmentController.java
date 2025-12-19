package tn.esprit.studentmanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

import java.util.List;

@RestController
@RequestMapping("/Department")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final IDepartmentService departmentService;

    @GetMapping("/getAllDepartment")
    public List<Department> getAll() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/getDepartment/{id}")
    public Department getById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping("/createDepartment")
    public Department create(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @PutMapping("/updateDepartment/{id}")
    public Department update(
            @PathVariable Long id,
            @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public void delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}
