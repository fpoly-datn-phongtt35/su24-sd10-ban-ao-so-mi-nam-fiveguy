package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.entity.ProductDetail;
import com.example.demo.entity.ThongKe;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.BillServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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
                        "    sp.Id AS sanpham_id, " +
                        "    sp.Name AS ten_sanpham, " +
                        "    sp.Price AS price, " +
                        "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                        "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
                        "    COALESCE(MAX(ha.Name), '') AS anh_mac_dinh " +
                        "FROM " +
                        "    ProductDetails pd " +
                        "JOIN " +
                        "    BillDetails hdct ON pd.Id = hdct.IdProductDetail " +
                        "JOIN " +
                        "    Bills b ON hdct.IdBill = b.Id " +
                        "JOIN " +
                        "    Products sp ON pd.IdProduct = sp.Id " +
                        "LEFT JOIN " +
                        "    Images ha ON sp.Id = ha.Id " +
                        "JOIN " +
                        "    PaymentStatus ps ON b.Id = ps.BillId " +
                        "WHERE " +
                        "    ps.PaymentDate = ? " +
                        "    AND ps.CustomerPaymentStatus = 2 " +
                        "    AND b.Status = 4 " +
                        "GROUP BY " +
                        "    sp.Id, sp.Name, sp.Price " +
                        "ORDER BY " +
                        "    so_luong_ban DESC",
                new Object[]{date},
                (rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                )
        );
    }

    @Override
    public List<ThongKe> getSanPhamBanChayTuan(Date date) {
        return jdbctemplate.query(
                "SELECT " +
                        "    sp.Id AS sanpham_id, " +
                        "    sp.Name AS ten_sanpham, " +
                        "    sp.Price AS price, " +
                        "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                        "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
                        "    COALESCE(MAX(ha.Name), '') AS anh_mac_dinh " +
                        "FROM " +
                        "    ProductDetails spct " +
                        "JOIN " +
                        "    BillDetails hdct ON spct.Id = hdct.IdProductDetail " +
                        "JOIN " +
                        "    Bills hd ON hd.Id = hdct.IdBill " +
                        "JOIN " +
                        "    Products sp ON spct.IdProduct = sp.Id " +
                        "LEFT JOIN " +
                        "    Images ha ON sp.Id = ha.Id " +
                        "JOIN " +
                        "    PaymentStatus ps ON hd.Id = ps.BillId " +
                        "WHERE " +
                        "    DATEPART(WEEK, ps.PaymentDate) = DATEPART(WEEK, ?) " +
                        "    AND DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
                        "    AND ps.CustomerPaymentStatus = 2 " +
                        "    AND hd.Status = 4 " +
                        "GROUP BY " +
                        "    sp.Id, sp.Name, sp.Price " +
                        "ORDER BY " +
                        "    so_luong_ban DESC",
                new Object[]{date, date},
                (rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                )
        );
    }

    @Override
    public List<ThongKe> getSanPhamBanChayThang(Date date) {
        return jdbctemplate.query(
                "SELECT " +
                        "    sp.Id AS sanpham_id, " +
                        "    sp.Name AS ten_sanpham, " +
                        "    sp.Price AS price, " +
                        "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                        "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
                        "    COALESCE(MAX(ha.Name), '') AS anh_mac_dinh " +
                        "FROM " +
                        "    ProductDetails spct " +
                        "JOIN " +
                        "    BillDetails hdct ON spct.Id = hdct.IdProductDetail " +
                        "JOIN " +
                        "    Bills hd ON hd.Id = hdct.IdBill " +
                        "JOIN " +
                        "    Products sp ON spct.IdProduct = sp.Id " +
                        "LEFT JOIN " +
                        "    Images ha ON sp.Id = ha.Id " +
                        "JOIN " +
                        "    PaymentStatus ps ON hd.Id = ps.BillId " +
                        "WHERE " +
                        "    DATEPART(MONTH, ps.PaymentDate) = DATEPART(MONTH, ?) " +
                        "    AND DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
                        "    AND ps.CustomerPaymentStatus = 2 " +
                        "    AND hd.Status = 4 " +
                        "GROUP BY " +
                        "    sp.Id, sp.Name, sp.Price " +
                        "ORDER BY " +
                        "    so_luong_ban DESC",
                new Object[]{date, date},
                (rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                )
        );
    }


    @Override
    public List<ThongKe> getSanPhamBanChayNam(Date date) {
        return jdbctemplate.query(
                "SELECT " +
                        "spct.Id AS sanpham_id, " +
                        "sp.Name AS ten_sanpham, " +
                        "sp.Price AS price, " +
                        "COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                        "SUM(hdct.Quantity * sp.Price) AS doanh_thu, " +
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
                        "JOIN " +
                        "PaymentStatus ps ON hd.id = ps.BillId " +
                        "WHERE DATEPART(YEAR, ps.paymentDate) = YEAR(?) " +
                        "AND ps.customerPaymentStatus = 2 " +
                        "AND hd.status = 4 " +
                        "GROUP BY spct.id, sp.Name, ha.Name, sp.Price " +
                        "ORDER BY so_luong_ban DESC",
                new Object[]{date},
                (rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                )
        );
    }

    @Override
    public List<ThongKe> getSanPhamBanChayTrongKhoangThoiGian(Date startDate, Date endDate) {
        return jdbctemplate.query(
                "SELECT " +
                        "    sp.Id AS sanpham_id, " +
                        "    sp.Name AS ten_sanpham, " +
                        "    sp.Price AS price, " +
                        "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                        "    COALESCE(SUM(hdct.quantity * sp.Price), 0) AS doanh_thu, " +
                        "    COALESCE(MAX(ha.Name), '') AS anh_mac_dinh " +
                        "FROM " +
                        "    ProductDetails pd " +
                        "JOIN " +
                        "    BillDetails hdct ON pd.Id = hdct.IdProductDetail " +
                        "JOIN " +
                        "    Bills b ON hdct.IdBill = b.Id " +
                        "JOIN " +
                        "    Products sp ON pd.IdProduct = sp.Id " +
                        "LEFT JOIN " +
                        "    Images ha ON sp.Id = ha.Id " +
                        "JOIN " +
                        "    PaymentStatus ps ON b.Id = ps.BillId " +
                        "WHERE " +
                        "    ps.PaymentDate BETWEEN ? AND ? " +
                        "    AND ps.CustomerPaymentStatus = 2 " +
                        "    AND b.Status = 4 " +
                        "GROUP BY " +
                        "    sp.Id, sp.Name, sp.Price " +
                        "ORDER BY " +
                        "    so_luong_ban DESC",
                new Object[]{startDate, endDate},
                (rs, rowNum) -> new ThongKe(
                        rs.getLong("sanpham_id"),
                        rs.getString("ten_sanpham"),
                        rs.getBigDecimal("price"),
                        rs.getInt("so_luong_ban"),
                        rs.getBigDecimal("doanh_thu"),
                        rs.getString("anh_mac_dinh")
                )
        );
    }
//    @Override
//    public Page<ThongKe> getSanPhamBanChayTrongKhoangThoiGian(Date startDate, Date endDate, Pageable pageable) {
//        // SQL query to get the total count of records
//        String countSql = "SELECT COUNT(DISTINCT sp.Id) " +
//                "FROM ProductDetails pd " +
//                "JOIN BillDetails hdct ON pd.Id = hdct.IdProductDetail " +
//                "JOIN Bills b ON hdct.IdBill = b.Id " +
//                "JOIN Products sp ON pd.IdProduct = sp.Id " +
//                "LEFT JOIN Images ha ON sp.Id = ha.Id " +
//                "JOIN PaymentStatus ps ON b.Id = ps.BillId " +
//                "WHERE ps.PaymentDate BETWEEN :startDate AND :endDate " +
//                "AND ps.CustomerPaymentStatus = 2 " +
//                "AND b.Status = 4";
//
//        // SQL query to get the paginated data
//        String dataSql = "SELECT " +
//                "    sp.Id AS sanpham_id, " +
//                "    sp.Name AS ten_sanpham, " +
//                "    sp.Price AS price, " +
//                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
//                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
//                "    COALESCE(MAX(ha.Name), '') AS anh_mac_dinh " +
//                "FROM ProductDetails pd " +
//                "JOIN BillDetails hdct ON pd.Id = hdct.IdProductDetail " +
//                "JOIN Bills b ON hdct.IdBill = b.Id " +
//                "JOIN Products sp ON pd.IdProduct = sp.Id " +
//                "LEFT JOIN Images ha ON sp.Id = ha.Id " +
//                "JOIN PaymentStatus ps ON b.Id = ps.BillId " +
//                "WHERE ps.PaymentDate BETWEEN :startDate AND :endDate " +
//                "AND ps.CustomerPaymentStatus = 2 " +
//                "AND b.Status = 4 " +
//                "GROUP BY sp.Id, sp.Name, sp.Price " +
//                "ORDER BY so_luong_ban DESC " +
//                "LIMIT :limit OFFSET :offset";
//
//        // Calculate the total count of records
//        int totalCount = namedParameterJdbcTemplate.queryForObject(
//                countSql,
//                new MapSqlParameterSource()
//                        .addValue("startDate", startDate)
//                        .addValue("endDate", endDate),
//                Integer.class
//        );
//
//        // Fetch the paginated result
//        List<ThongKe> data = namedParameterJdbcTemplate.query(
//                dataSql,
//                new MapSqlParameterSource()
//                        .addValue("startDate", startDate)
//                        .addValue("endDate", endDate)
//                        .addValue("limit", pageable.getPageSize())
//                        .addValue("offset", pageable.getOffset()),
//                new RowMapper<ThongKe>() {
//                    @Override
//                    public ThongKe mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        return new ThongKe(
//                                rs.getLong("sanpham_id"),
//                                rs.getString("ten_sanpham"),
//                                rs.getBigDecimal("price"),
//                                rs.getInt("so_luong_ban"),
//                                rs.getBigDecimal("doanh_thu"),
//                                rs.getString("anh_mac_dinh")
//                        );
//                    }
//                }
//        );
//
//        // Return Page object
//        return new PageImpl<>(data, pageable, totalCount);
//    }
}
