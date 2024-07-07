package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.service.tinh.BillServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class BillServiceImplTinh implements BillServiceTinh {
    @Autowired
    BillRepositoryTinh billRepositoryTinh;

    @Override
    public List<Bill> getAll(){return billRepositoryTinh.findAll();}

    @Override
    public Bill create(Bill bill){
        Bill bill1 = new Bill();
        Employee employee = new Employee();

        String randomCode = generateRandomCode(6);
        bill1.setCode(randomCode);
        bill1.setEmployee(bill.getEmployee());
        bill1.setCreatedAt(new Date());
        bill1.setCustomer(bill.getCustomer());
        bill1.setTypeBill(1);
        bill1.setStatus(1);

        return billRepositoryTinh.save(bill1);

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
}
