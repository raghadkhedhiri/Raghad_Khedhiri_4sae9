package tn.esprit.studentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Department not found with id " + id));
    }

    @Override
    public Department createDepartment(Department department) {
        department.setIdDepartment(null);
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Long id, Department department) {
        Department existing = getDepartmentById(id);

        existing.setName(department.getName());
        existing.setLocation(department.getLocation());
        existing.setPhone(department.getPhone());
        existing.setHead(department.getHead());

        return departmentRepository.save(existing);
    }

    @Override
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Department not found with id " + id);
        }
        departmentRepository.deleteById(id);
    }
}
