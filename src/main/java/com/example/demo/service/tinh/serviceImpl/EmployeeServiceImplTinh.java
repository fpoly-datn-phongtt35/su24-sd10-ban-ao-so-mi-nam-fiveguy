package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Account;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.EmployeeRepositoryTinh;
import com.example.demo.repository.tinh.EmployeeSpecificationTinh;
import com.example.demo.service.tinh.EmployeeServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeServiceImplTinh implements EmployeeServiceTinh {

    @Autowired
    EmployeeRepositoryTinh employeeRepository;

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
    public Employee getByAccount(String account){
        return (Employee) employeeRepository.getByAccount(account);
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
    public Page<Employee> findEmployee(String fullName, String code, String avatar, Date birthDate, Boolean gender, String address,String account, String email, String phoneNumber, Long id, Integer status, Pageable pageable) {

        Specification<Employee> spec = Specification.where(EmployeeSpecificationTinh.hasCode(code))
                .and(EmployeeSpecificationTinh.hasfullName(fullName))
                .and(EmployeeSpecificationTinh.hasAvatar(avatar))
                .and(EmployeeSpecificationTinh.hasBrithDate(birthDate))
                .and(EmployeeSpecificationTinh.hasGenDer(gender))
                .and(EmployeeSpecificationTinh.hasAddRess(address))
                .and(EmployeeSpecificationTinh.hasAccountByAccount(account))
                .and(EmployeeSpecificationTinh.hasAccountByEmail(email))
                .and(EmployeeSpecificationTinh.hasAccountByPhoneNumber(phoneNumber))
                .and(EmployeeSpecificationTinh.hasAccountByRole(id))
                .and(EmployeeSpecificationTinh.hasStatus(status));

        return employeeRepository.findAll(spec, pageable);
    }


}