package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.*;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.BillServiceTinh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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
    public Page<ThongKe> getSanPhamBanChayNgay(Date date, Pageable pageable) {
        String sql = "SELECT " +
                "    sp.Id AS sanpham_id, " +
                "    sp.Name AS ten_sanpham, " +
                "    sp.Price AS price, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
                "    ha.Path As anh_mac_dinh " +
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
                "    AND b.Status = 21 " +
                "GROUP BY " +
                "    sp.Id, sp.Name, sp.Price, ha.Path " +
                "ORDER BY " +
                "    so_luong_ban DESC";

        List<ThongKe> results = jdbctemplate.query(
                sql,
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

        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        return new PageImpl<>(results.subList(start, end), pageable, results.size());
    }


    @Override
    public Page<ThongKe> getSanPhamBanChayTuan(Date date, Pageable pageable) {
        String sql = "SELECT " +
                "    sp.Id AS sanpham_id, " +
                "    sp.Name AS ten_sanpham, " +
                "    sp.Price AS price, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
                "    ha.Path AS anh_mac_dinh " +
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
                "    AND hd.Status = 21 " +
                "GROUP BY " +
                "    sp.Id, sp.Name, sp.Price, ha.Path " +
                "ORDER BY " +
                "    so_luong_ban DESC";

        List<ThongKe> results = jdbctemplate.query(
                sql,
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

        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        return new PageImpl<>(results.subList(start, end), pageable, results.size());
    }

    @Override
    public Page<ThongKe> getSanPhamBanChayThang(Date date, Pageable pageable) {
        String sql = "SELECT " +
                "    sp.Id AS sanpham_id, " +
                "    sp.Name AS ten_sanpham, " +
                "    sp.Price AS price, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
                "   ha.Path AS anh_mac_dinh " +
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
                "    AND hd.Status = 21 " +
                "GROUP BY " +
                "    sp.Id, sp.Name, sp.Price,ha.Path " +
                "ORDER BY " +
                "    so_luong_ban DESC";

        List<ThongKe> results = jdbctemplate.query(
                sql,
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

        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        return new PageImpl<>(results.subList(start, end), pageable, results.size());
    }

    @Override
    public Page<ThongKe> getSanPhamBanChayNam(Date date, Pageable pageable) {
        String sql = "SELECT " +
                "spct.Id AS sanpham_id, " +
                "sp.Name AS ten_sanpham, " +
                "sp.Price AS price, " +
                "COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                "SUM(hdct.Quantity * sp.Price) AS doanh_thu, " +
                "ha.Path AS anh_mac_dinh " +
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
                "AND hd.status = 21 " +
                "GROUP BY spct.id, sp.Name, ha.Path, sp.Price " +
                "ORDER BY so_luong_ban DESC";

        List<ThongKe> results = jdbctemplate.query(
                sql,
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

        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        return new PageImpl<>(results.subList(start, end), pageable, results.size());
    }

    @Override
    public Page<ThongKe> getSanPhamBanChayTrongKhoangThoiGian(Date startDate, Date endDate, Pageable pageable) {
        String sql = "SELECT " +
                "    sp.Id AS sanpham_id, " +
                "    sp.Name AS ten_sanpham, " +
                "    sp.Price AS price, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
                "    COALESCE(SUM(hdct.quantity * sp.Price), 0) AS doanh_thu, " +
                "    ha.Path AS anh_mac_dinh " +
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
                "    AND b.Status = 21 " +
                "GROUP BY " +
                "    sp.Id, sp.Name, sp.Price, ha.Path " +
                "ORDER BY " +
                "    so_luong_ban DESC";

        List<ThongKe> results = jdbctemplate.query(
                sql,
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

        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), results.size());
        return new PageImpl<>(results.subList(start, end), pageable, results.size());
    }


    //====================Khách hàng mua hàng nhiều nhất
    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNgay(Date date, Pageable pageable) {
        // Truy xuất toàn bộ dữ liệu
        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
                "SELECT " +
                        "    c.Id AS khachhang_id, " +
                        "    c.FullName AS ten_khachhang, " +
                        "    COALESCE(SUM(hdct.quantity), 0) AS tong_so_luong_mua, " +
                        "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS tong_doanh_thu, " +
                        "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
                        "FROM " +
                        "    Customers c " +
                        "JOIN " +
                        "    Bills b ON c.Id = b.idCustomer " +
                        "JOIN " +
                        "    BillDetails hdct ON b.Id = hdct.IdBill " +
                        "JOIN " +
                        "    PaymentStatus ps ON b.Id = ps.BillId " +
                        "WHERE " +
                        "    CAST(ps.PaymentDate AS DATE) = ? " +
                        "    AND ps.CustomerPaymentStatus = 2 " +
                        "    AND b.Status = 21 " +
                        "GROUP BY " +
                        "    c.Id, c.FullName " +
                        "ORDER BY " +
                        "    tong_so_luong_mua DESC",
                new Object[]{date},
                (rs, rowNum) -> new ThongKeKhachHang(
                        rs.getLong("khachhang_id"),
                        rs.getString("ten_khachhang"),
                        rs.getInt("tong_so_luong_mua"),
                        rs.getBigDecimal("tong_doanh_thu"),
                        rs.getInt("tong_so_bill")
                )
        );

        // Áp dụng phân trang trong Java
        int total = khachHangList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);
        List<ThongKeKhachHang> pagedKhachHangList = khachHangList.subList(start, end);

        return new PageImpl<>(pagedKhachHangList, pageable, total);
    }

    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuan(Date date, Pageable pageable) {
        // Truy vấn để lấy dữ liệu với phân trang
        String query = "SELECT " +
                "    c.Id AS khachhang_id, " +
                "    c.FullName AS ten_khachhang, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS tong_so_luong_mua, " +
                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS tong_doanh_thu, " +
                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
                "FROM " +
                "    Customers c " +
                "JOIN " +
                "    Bills b ON c.Id = b.idCustomer " +
                "JOIN " +
                "    BillDetails hdct ON b.Id = hdct.IdBill " +
                "JOIN " +
                "    PaymentStatus ps ON b.Id = ps.BillId " +
                "WHERE " +
                "    DATEPART(WEEK, ps.PaymentDate) = DATEPART(WEEK, ?) " +
                "    AND DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
                "    AND ps.CustomerPaymentStatus = 2 " +
                "    AND b.Status = 21 " +
                "GROUP BY " +
                "    c.Id, c.FullName " +
                "ORDER BY " +
                "    tong_so_luong_mua DESC " +
                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // Thực hiện truy vấn phân trang
        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
                query,
                new Object[]{date, date, pageable.getOffset(), pageable.getPageSize()},
                (rs, rowNum) -> new ThongKeKhachHang(
                        rs.getLong("khachhang_id"),
                        rs.getString("ten_khachhang"),
                        rs.getInt("tong_so_luong_mua"),
                        rs.getBigDecimal("tong_doanh_thu"),
                        rs.getInt("tong_so_bill")
                )
        );

        // Truy vấn để tính tổng số kết quả
        String countQuery = "SELECT COUNT(*) FROM (" +
                "    SELECT " +
                "        c.Id " +
                "    FROM " +
                "        Customers c " +
                "    JOIN " +
                "        Bills b ON c.Id = b.idCustomer " +
                "    JOIN " +
                "        BillDetails hdct ON b.Id = hdct.IdBill " +
                "    JOIN " +
                "        PaymentStatus ps ON b.Id = ps.BillId " +
                "    WHERE " +
                "        DATEPART(WEEK, ps.PaymentDate) = DATEPART(WEEK, ?) " +
                "        AND DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
                "        AND ps.CustomerPaymentStatus = 2 " +
                "        AND b.Status = 21 " +
                "    GROUP BY " +
                "        c.Id, c.FullName " +
                ") AS countTable";

        // Tính tổng số kết quả
        int total = jdbctemplate.queryForObject(countQuery, new Object[]{date, date}, Integer.class);

        // Trả về trang dữ liệu
        return new PageImpl<>(khachHangList, pageable, total);
    }

    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatThang(Date date, Pageable pageable) {
        String sql = "SELECT " +
                "    c.Id AS khachhang_id, " +
                "    c.FullName AS ten_khachhang, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS tong_so_luong_mua, " +
                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS tong_doanh_thu, " +
                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
                "FROM " +
                "    Customers c " +
                "JOIN " +
                "    Bills b ON c.Id = b.idCustomer " +
                "JOIN " +
                "    BillDetails hdct ON b.Id = hdct.IdBill " +
                "JOIN " +
                "    PaymentStatus ps ON b.Id = ps.BillId " +
                "WHERE " +
                "    DATEPART(MONTH, ps.PaymentDate) = DATEPART(MONTH, ?) " +
                "    AND DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
                "    AND ps.CustomerPaymentStatus = 2 " +
                "    AND b.Status = 21 " +
                "GROUP BY " +
                "    c.Id, c.FullName " +
                "ORDER BY " +
                "    tong_so_luong_mua DESC";

        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
                sql,
                new Object[]{date, date},
                (rs, rowNum) -> new ThongKeKhachHang(
                        rs.getLong("khachhang_id"),
                        rs.getString("ten_khachhang"),
                        rs.getInt("tong_so_luong_mua"),
                        rs.getBigDecimal("tong_doanh_thu"),
                        rs.getInt("tong_so_bill")
                )
        );

        int total = khachHangList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);
        List<ThongKeKhachHang> pagedKhachHangList = khachHangList.subList(start, end);

        return new PageImpl<>(pagedKhachHangList, pageable, total);
    }

    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNam(Date date, Pageable pageable) {
        String sql = "SELECT " +
                "    c.Id AS khachhang_id, " +
                "    c.FullName AS ten_khachhang, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS tong_so_luong_mua, " +
                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS tong_doanh_thu, " +
                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
                "FROM " +
                "    Customers c " +
                "JOIN " +
                "    Bills b ON c.Id = b.idCustomer " +
                "JOIN " +
                "    BillDetails hdct ON b.Id = hdct.IdBill " +
                "JOIN " +
                "    PaymentStatus ps ON b.Id = ps.BillId " +
                "WHERE " +
                "    DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
                "    AND ps.CustomerPaymentStatus = 2 " +
                "    AND b.Status = 21 " +
                "GROUP BY " +
                "    c.Id, c.FullName " +
                "ORDER BY " +
                "    tong_so_luong_mua DESC";

        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
                sql,
                new Object[]{date},
                (rs, rowNum) -> new ThongKeKhachHang(
                        rs.getLong("khachhang_id"),
                        rs.getString("ten_khachhang"),
                        rs.getInt("tong_so_luong_mua"),
                        rs.getBigDecimal("tong_doanh_thu"),
                        rs.getInt("tong_so_bill")
                )
        );

        int total = khachHangList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);
        List<ThongKeKhachHang> pagedKhachHangList = khachHangList.subList(start, end);

        return new PageImpl<>(pagedKhachHangList, pageable, total);
    }

    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuyChinh(Date startDate, Date endDate, Pageable pageable) {
        String sql = "SELECT " +
                "    c.Id AS khachhang_id, " +
                "    c.FullName AS ten_khachhang, " +
                "    COALESCE(SUM(hdct.quantity), 0) AS tong_so_luong_mua, " +
                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS tong_doanh_thu, " +
                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
                "FROM " +
                "    Customers c " +
                "JOIN " +
                "    Bills b ON c.Id = b.idCustomer " +
                "JOIN " +
                "    BillDetails hdct ON b.Id = hdct.IdBill " +
                "JOIN " +
                "    PaymentStatus ps ON b.Id = ps.BillId " +
                "WHERE " +
                "    ps.PaymentDate BETWEEN ? AND ? " +
                "    AND ps.CustomerPaymentStatus = 2 " +
                "    AND b.Status = 21 " +
                "GROUP BY " +
                "    c.Id, c.FullName " +
                "ORDER BY " +
                "    tong_so_luong_mua DESC";

        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
                sql,
                new Object[]{startDate, endDate},
                (rs, rowNum) -> new ThongKeKhachHang(
                        rs.getLong("khachhang_id"),
                        rs.getString("ten_khachhang"),
                        rs.getInt("tong_so_luong_mua"),
                        rs.getBigDecimal("tong_doanh_thu"),
                        rs.getInt("tong_so_bill")
                )
        );

        int total = khachHangList.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);
        List<ThongKeKhachHang> pagedKhachHangList = khachHangList.subList(start, end);

        return new PageImpl<>(pagedKhachHangList, pageable, total);
    }


}
