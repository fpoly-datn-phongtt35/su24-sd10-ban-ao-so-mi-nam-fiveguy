package com.example.demo.service.serviceImpl.tinh;

import com.cloudinary.Cloudinary;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.EmployeeRepository;
import com.example.demo.repository.tinh.EmployeeSpecification;
import com.example.demo.service.tinh.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    // get all Employee
    @Override
    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    //get all Employ status = 1 (dang làm)
    @Override
    public List<Employee> getAllStatusDangLam(){
        return employeeRepository.getAllStatusDangLam();
    }

    @Override
    public Employee getById(Long id){
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Employee> getAllStatus(Integer status){
        return employeeRepository.getAllStatus(status);
    }

    @Override
    public Employee create(Employee employees){
//        List<Employees> hi = new ArrayList<>();
        Employee employees1 = new Employee();
        String randomCode = generateRandomCode(6);
        employees1.setCode(randomCode);
        employees1.setFullName(employees.getFullName());
        employees1.setAvatar(employees.getAvatar());
        employees1.setBirthDate(employees.getBirthDate());
        employees1.setGender(employees.getGender());
        employees1.setAddress(employees.getAddress());
        employees1.setAccount(employees.getAccount());
        employees1.setCreatedAt(new Date());
        employees1.setUpdatedAt(new Date());
        employees1.setCreatedBy("admin");
        employees1.setUpdatedBy("admin");
        employees1.setStatus(1);

        return employeeRepository.save(employees1);

    }

    @Override
    public void delete(Long id){
        employeeRepository.deleteById(id);
    }

    @Override
    public  Employee update(Long id, Employee employees){
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee employees1 = existingEmployee.get();
            employees1.setCode(employees.getCode());
            employees1.setFullName(employees.getFullName());
            employees1.setAvatar(employees.getAvatar());
            employees1.setBirthDate(employees.getBirthDate());
            employees1.setGender(employees.getGender());
            employees1.setAddress(employees.getAddress());
            employees1.setAccount(employees.getAccount());
            employees1.setCreatedAt(employees.getCreatedAt());
            employees1.setUpdatedAt(new Date());
            employees1.setCreatedBy("admin");
            employees1.setUpdatedBy("admin");
            employees1.setStatus(employees.getStatus());

            return employeeRepository.save(employees1); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID " + id);
//            return null;
        }
    }

    private String generateRandomCode(int length) {
        String uppercaseCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomCode = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(uppercaseCharacters.length());
            char randomChar = uppercaseCharacters.charAt(randomIndex);
            randomCode.append(randomChar);
        }

        return randomCode.toString();
    }

    //delete theo status
    @Override
    public  Employee updateRole(Long id, Employee employees){
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee employees1 = existingEmployee.get();
            employees1.setCode(employees.getCode());
            employees1.setFullName(employees.getFullName());
            employees1.setAvatar(employees.getAvatar());
            employees1.setBirthDate(employees.getBirthDate());
            employees1.setGender(employees.getGender());
            employees1.setAddress(employees.getAddress());
            employees1.setAccount(employees.getAccount());
            employees1.setCreatedAt(employees.getCreatedAt());
            employees1.setUpdatedAt(new Date());
            employees1.setCreatedBy("admin");
            employees1.setUpdatedBy("admin");
            employees1.setStatus(2);

            return employeeRepository.save(employees1); // Lưu khách hàng đã cập nhật vào cơ sở dữ liệu
        } else {
            // Trả về null hoặc thông báo lỗi nếu không tìm thấy khách hàng với ID này
            throw new IllegalArgumentException("Không tìm thấy Employee với ID " + id);
//            return null;
        }
    }

    // search ma
    @Override
    public Page<Employee>  searchMa(String ma, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeesList = employeeRepository.searchMa(ma, pageable);
        return employeesList;
    }
    //    @Override
//    public Page<Employee> getFilteredEmployees(String fullName, String code, String avatar, String birthDate, Boolean gender, String address, String account, Integer status, int page, int size, String sortField, String sortDirection) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
//        return employeeRepository.findAll(EmployeeSpecification.filterEmployees(fullName, code, avatar, birthDate, gender, address, account, status), pageable);
//    }
    @Override
    public Page<Employee> findEmployee(String fullName, String code, String avatar,Date birthDate, Boolean gender, String address, String account, Integer status, Pageable pageable) {
        Specification<Employee> spec = Specification.where(EmployeeSpecification.hasCode(code))
                .and(EmployeeSpecification.hasfullName(fullName))
                .and(EmployeeSpecification.hasAvatar(avatar))
                .and(EmployeeSpecification.hasBrithDate(birthDate))
                .and(EmployeeSpecification.hasGenDer(gender))
                .and(EmployeeSpecification.hasAddRess(address))
                .and(EmployeeSpecification.hasAccountr(account))
                .and(EmployeeSpecification.hasStatus(status));

        return employeeRepository.findAll(spec, pageable);
    }

}
