package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.entity.ProductDetail;
import com.example.demo.entity.ThongKe;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.BillServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BillServiceImplTinh implements BillServiceTinh {
    @Autowired
    BillRepositoryTinh billRepositoryTinh;

    @Autowired
    SCEmployeeService scEmployeeService;

    @Autowired
    JdbcTemplate jdbctemplate;

//    @Override
//    public List<Bill> getAll((Pageable pageable){return billRepositoryTinh.getAllBillChoThanhToan));}

    @Override
    public Bill create( Bill bill){
        Bill bill1 = new Bill();
//        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);

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

    @Transactional
    public void updateBillStatus(Long id) {
        billRepositoryTinh.updateBillStatus(id);
    }

    @Override
    public List<ThongKe> getSanPhamBanChayNgay(Date date) {
        return jdbctemplate.query(
                "SELECT " +
                        "spct.Id AS sanpham_id, " +
                        "sp.Name AS ten_sanpham, " +
                        "sp.Price AS price, " +
                        "SUM(hdct.Quantity) AS so_luong_ban, " +
                        "SUM(hdct.Price) AS doanh_thu, " +
                        "ha.Name AS anh_mac_dinh " +
                        "FROM " +
                        "ProductDetails spct " +
                        "JOIN " +
                        "BillDetails hdct ON spct.id = hdct.IdProductDetail " +
                        "JOIN " +
                        "Bills hd ON hd.id = hdct.IdBill " +
                        "JOIN " +
                        "Products sp ON spct.IdProduct = sp.id " +
                        "JOIN " +
                        "Images ha ON spct.id = ha.Id " +
                        "WHERE hd.paymentDate = ? and hd.status = 4" +
                        "GROUP BY spct.id, sp.Name, ha.Name, sp.Price " +
                        "ORDER BY so_luong_ban DESC",
                new Object[]{date},
                ((rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                ))
        );
    }
    @Override
    public List<ThongKe> getSanPhamBanChayTuan(Date date) {
        return jdbctemplate.query(
                "SELECT " +
                        "spct.Id AS sanpham_id, " +
                        "sp.Name AS ten_sanpham, " +
                        "sp.Price AS price, " +
                        "SUM(hdct.Quantity) AS so_luong_ban, " +
                        "SUM(hdct.Price) AS doanh_thu, " +
                        "ha.Name AS anh_mac_dinh " +
                        "FROM " +
                        "ProductDetails spct " +
                        "JOIN " +
                        "BillDetails hdct ON spct.id = hdct.IdProductDetail " +
                        "JOIN " +
                        "Bills hd ON hd.id = hdct.IdBill " +
                        "JOIN " +
                        "Products sp ON spct.IdProduct = sp.id " +
                        "JOIN " +
                        "Images ha ON spct.id = ha.Id " +
                        "WHERE DATEPART(WEEK, hd.PaymentDate) = DATEPART(WEEK, ?) " +
                        "AND DATEPART(YEAR, hd.PaymentDate) = DATEPART(YEAR, ?) " +
                        "AND hd.status = 4 " +
                        "GROUP BY spct.id, sp.Name, ha.Name, sp.Price " +
                        "ORDER BY so_luong_ban DESC",
                new Object[]{date, date},
                ((rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                ))
        );
    }

    @Override
    public List<ThongKe> getSanPhamBanChayThang(Date date) {
        return jdbctemplate.query(
                "SELECT " +
                        "spct.Id AS sanpham_id, " +
                        "sp.Name AS ten_sanpham, " +
                        "sp.Price AS price, " +
                        "SUM(hdct.Quantity) AS so_luong_ban, " +
                        "SUM(hdct.Price) AS doanh_thu, " +
                        "ha.Name AS anh_mac_dinh " +
                        "FROM " +
                        "ProductDetails spct " +
                        "JOIN " +
                        "BillDetails hdct ON spct.id = hdct.IdProductDetail " +
                        "JOIN " +
                        "Bills hd ON hd.id = hdct.IdBill " +
                        "JOIN " +
                        "Products sp ON spct.IdProduct = sp.id " +
                        "JOIN " +
                        "Images ha ON spct.id = ha.Id " +
                        "where DATEPART(MONTH, hd.PaymentDate) =  Month(?) and DATEPART(YEAR, hd.PaymentDate) = YEAR(?) and hd.status = 4" +
                        "GROUP BY spct.id, sp.Name, ha.Name, sp.Price " +
                        "ORDER BY so_luong_ban DESC",
                new Object[]{date, date},
                ((rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                ))
        );
    }

    @Override
    public List<ThongKe> getSanPhamBanChayNam(Date date) {
        return jdbctemplate.query(
                "SELECT " +
                        "spct.Id AS sanpham_id, " +
                        "sp.Name AS ten_sanpham, " +
                        "sp.Price AS price, " +
                        "SUM(hdct.Quantity) AS so_luong_ban, " +
                        "SUM(hdct.Price) AS doanh_thu, " +
                        "ha.Name AS anh_mac_dinh " +
                        "FROM " +
                        "ProductDetails spct " +
                        "JOIN " +
                        "BillDetails hdct ON spct.id = hdct.IdProductDetail " +
                        "JOIN " +
                        "Bills hd ON hd.id = hdct.IdBill " +
                        "JOIN " +
                        "Products sp ON spct.IdProduct = sp.id " +
                        "JOIN " +
                        "Images ha ON spct.id = ha.Id " +
                        "where DATEPART(YEAR, hd.PaymentDate) = YEAR(?) and hd.status = 4" +
                        "GROUP BY spct.id, sp.Name, ha.Name, sp.Price " +
                        "ORDER BY so_luong_ban DESC",
                new Object[]{date},
                ((rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                ))
        );
    }


}
