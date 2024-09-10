package com.example.demo.service.tinh.serviceImpl;

import com.example.demo.entity.*;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.repository.tinh.ImageRepositoryTinh;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillServiceImplTinh implements BillServiceTinh {

    @Autowired
    ImageRepositoryTinh imageRepositoryTinh;

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
    public Page<ThongKe> getSanPhamBanChayNgay( Pageable pageable) {
        Date date = new Date();
        return getSanPhamBanChay(date,"day",pageable);
    }

    @Override
    public Page<ThongKe> getSanPhamBanChayTuan( Pageable pageable) {
        Date date = new Date();
        return getSanPhamBanChay(date,"week",pageable);
    }

    @Override
    public Page<ThongKe> getSanPhamBanChayThang( Pageable pageable) {
        Date date = new Date();
        return getSanPhamBanChay(date,"month",pageable);
    }

    @Override
    public Page<ThongKe> getSanPhamBanChayNam(Pageable pageable) {
        Date date = new Date();
        return getSanPhamBanChay(date,"year",pageable);
    }

    @Override
    public Page<ThongKe> findSanPhamBanChayTuyChon(Date startDate, Date endDate, Pageable pageable) {
        // Retrieve raw results from the repository
        List<Object[]> results = billRepositoryTinh.findSanPhamBanChayTuyChon(startDate, endDate);

        // Map the results to ThongKe objects
        List<ThongKe> thongKeList = results.stream().map(row -> {
            Long productId = (Long) row[0]; // Product ID
            String productName = (String) row[1]; // Name of the product
            int quantitySold = ((Number) row[2]).intValue(); // Quantity sold
            BigDecimal price = (BigDecimal) row[3]; // Price

            // Retrieve image paths for the product
            List<String> imagePaths = imageRepositoryTinh.findImagePathsByProductId(productId);
            String imagePath = imagePaths.isEmpty() ? null : imagePaths.get(0); // Set to null if list is empty

            return new ThongKe(productId, productName, imagePath, quantitySold, price);
        }).toList();

        // Implement pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), thongKeList.size());
        List<ThongKe> pagedList = thongKeList.subList(start, end);

        // Return a Page object
        return new PageImpl<>(pagedList, pageable, thongKeList.size());
    }



    public Page<ThongKe> getSanPhamBanChay(Date date, String timePeriod, Pageable pageable) {
        // Retrieve raw results from the repository
        List<Object[]> results = billRepositoryTinh.findSanPhamBanChay(date, timePeriod);

        // Map the results to ThongKe objects
        List<ThongKe> thongKeList = results.stream().map(row -> {
            Long productId = (Long) row[0]; // Product ID
            String productName = (String) row[1]; // Name of the product
            int quantitySold = ((Number) row[2]).intValue(); // Quantity sold
            BigDecimal price = (BigDecimal) row[3]; // Price

            // Retrieve image paths for the product
            List<String> imagePaths = imageRepositoryTinh.findImagePathsByProductId(productId);
            String imagePath = imagePaths.isEmpty() ? null : imagePaths.get(0); // Set to null if list is empty

            return new ThongKe(productId, productName, imagePath, quantitySold, price);
        }).toList();

        // Implement pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), thongKeList.size());
        List<ThongKe> pagedList = thongKeList.subList(start, end);

        // Return a Page object
        return new PageImpl<>(pagedList, pageable, thongKeList.size());
    }



//    @Override
//    public Page<ThongKe> getSanPhamBanChayNgay(Date date, Pageable pageable) {
//        String sql = "SELECT " +
//                "    sp.Id AS sanpham_id, " +
//                "    sp.Name AS ten_sanpham, " +
//                "    sp.Price AS price, " +
//                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
//                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
//                "    MAX(ha.Path) AS anh_mac_dinh " +
//                "FROM " +
//                "    ProductDetails pd " +
//                "JOIN " +
//                "    BillDetails hdct ON pd.Id = hdct.IdProductDetail " +
//                "JOIN " +
//                "    Bills b ON hdct.IdBill = b.Id " +
//                "JOIN " +
//                "    Products sp ON pd.IdProduct = sp.Id " +
//                "LEFT JOIN " +
//                "    Images ha ON sp.Id = ha.IdProduct " +
//                "JOIN " +
//                "    PaymentStatus ps ON b.Id = ps.BillId " +
//                "WHERE " +
//                "    CAST(ps.PaymentDate AS DATE) = CAST(? AS DATE) " +
//                "    AND ps.CustomerPaymentStatus = 2 " +
//                "    AND b.Status = 21 " +
//                "GROUP BY " +
//                "    sp.Id, sp.Name, sp.Price " +
//                "ORDER BY " +
//                "    so_luong_ban DESC";
//
//        List<ThongKe> results = jdbctemplate.query(
//                sql,
//                new Object[]{date},
//                (rs, rowNum) -> new ThongKe(
//                        rs.getLong("sanpham_id"),
//                        rs.getString("ten_sanpham"),
//                        rs.getBigDecimal("price"),
//                        rs.getInt("so_luong_ban"),
//                        rs.getBigDecimal("doanh_thu"),
//                        rs.getString("anh_mac_dinh")
////                        rs.getString("id_")
//                )
//        );
//
//        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
//        int start = (int) pageable.getOffset();
//        int end = Math.min(start + pageable.getPageSize(), results.size());
//        return new PageImpl<>(results.subList(start, end), pageable, results.size());
//    }
//
//    @Override
//    public Page<ThongKe> getSanPhamBanChayTuan(Date date, Pageable pageable) {
//        String sql = "SELECT " +
//                "    sp.Id AS sanpham_id, " +
//                "    sp.Name AS ten_sanpham, " +
//                "    sp.Price AS price, " +
//                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
//                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
//                "    MAX(ha.Path) AS anh_mac_dinh " +
//                "FROM " +
//                "    ProductDetails spct " +
//                "JOIN " +
//                "    BillDetails hdct ON spct.Id = hdct.IdProductDetail " +
//                "JOIN " +
//                "    Bills hd ON hd.Id = hdct.IdBill " +
//                "JOIN " +
//                "    Products sp ON spct.IdProduct = sp.Id " +
//                "LEFT JOIN " +
//                "    Images ha ON sp.Id = ha.IdProduct " +
//                "JOIN " +
//                "    PaymentStatus ps ON hd.Id = ps.BillId " +
//                "WHERE " +
//                "    DATEPART(WEEK, ps.PaymentDate) = DATEPART(WEEK, ?) " +
//                "    AND DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
//                "    AND ps.CustomerPaymentStatus = 2 " +
//                "    AND hd.Status = 21 " +
//                "GROUP BY " +
//                "    sp.Id, sp.Name, sp.Price " +
//                "ORDER BY " +
//                "    so_luong_ban DESC";
//
//        List<ThongKe> results = jdbctemplate.query(
//                sql,
//                new Object[]{date, date},
//                (rs, rowNum) -> new ThongKe(
//                        rs.getLong("sanpham_id"),
//                        rs.getString("ten_sanpham"),
//                        rs.getBigDecimal("price"),
//                        rs.getInt("so_luong_ban"),
//                        rs.getBigDecimal("doanh_thu"),
//                        rs.getString("anh_mac_dinh")
//                )
//        );
//
//        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), results.size());
//        return new PageImpl<>(results.subList(start, end), pageable, results.size());
//    }
//
//
//    @Override
//    public Page<ThongKe> getSanPhamBanChayThang(Date date, Pageable pageable) {
//        String sql = "SELECT " +
//                "    sp.Id AS sanpham_id, " +
//                "    sp.Name AS ten_sanpham, " +
//                "    sp.Price AS price, " +
//                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
//                "    COALESCE(SUM(hdct.quantity * hdct.Price), 0) AS doanh_thu, " +
//                "    MAX(ha.Path) AS anh_mac_dinh " +
//                "FROM " +
//                "    ProductDetails spct " +
//                "JOIN " +
//                "    BillDetails hdct ON spct.Id = hdct.IdProductDetail " +
//                "JOIN " +
//                "    Bills hd ON hd.Id = hdct.IdBill " +
//                "JOIN " +
//                "    Products sp ON spct.IdProduct = sp.Id " +
//                "LEFT JOIN " +
//                "    Images ha ON sp.Id = ha.IdProduct " +
//                "JOIN " +
//                "    PaymentStatus ps ON hd.Id = ps.BillId " +
//                "WHERE " +
//                "    DATEPART(MONTH, ps.PaymentDate) = DATEPART(MONTH, ?) " +
//                "    AND DATEPART(YEAR, ps.PaymentDate) = DATEPART(YEAR, ?) " +
//                "    AND ps.CustomerPaymentStatus = 2 " +
//                "    AND hd.Status = 21 " +
//                "GROUP BY " +
//                "    sp.Id, sp.Name, sp.Price " +
//                "ORDER BY " +
//                "    so_luong_ban DESC";
//
//        List<ThongKe> results = jdbctemplate.query(
//                sql,
//                new Object[]{date, date},
//                (rs, rowNum) -> new ThongKe(
//                        rs.getLong("sanpham_id"),
//                        rs.getString("ten_sanpham"),
//                        rs.getBigDecimal("price"),
//                        rs.getInt("so_luong_ban"),
//                        rs.getBigDecimal("doanh_thu"),
//                        rs.getString("anh_mac_dinh")
//                )
//        );
//
//        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), results.size());
//        return new PageImpl<>(results.subList(start, end), pageable, results.size());
//    }
//
//    @Override
//    public Page<ThongKe> getSanPhamBanChayNam(Date date, Pageable pageable) {
//        String sql = "SELECT " +
//                "    sp.id AS sanpham_id, " +
//                "    sp.Name AS ten_sanpham, " +
//                "    sp.Price AS price, " +
//                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban,  " +
//                "    COALESCE(SUM(hdct.quantity * sp.Price), 0) AS doanh_thu, " +
//                "    MAX(ha.Path) AS anh_mac_dinh " +
//                "FROM " +
//                "    ProductDetails spct " +
//                "JOIN " +
//                "    BillDetails hdct ON spct.id = hdct.IdProductDetail " +
//                "JOIN " +
//                "    Bills hd ON hd.id = hdct.IdBill " +
//                "JOIN " +
//                "    Products sp ON spct.IdProduct = sp.id " +
//                "LEFT JOIN " +
//                "    Images ha ON sp.id = ha.IdProduct " +
//                "JOIN " +
//                "    PaymentStatus ps ON hd.id = ps.BillId " +
//                "WHERE " +
//                "    DATEPART(YEAR, ps.paymentDate) = DATEPART(YEAR, ?) " +
//                "    AND ps.customerPaymentStatus = 2 " +
//                "    AND hd.status = 21 " +
//                "GROUP BY " +
//                "    sp.id, sp.Name, sp.Price " +
//                "ORDER BY " +
//                "    so_luong_ban DESC";
//
//        List<ThongKe> results = jdbctemplate.query(
//                sql,
//                new Object[]{date},
//                (rs, rowNum) -> new ThongKe(
//                        rs.getLong("sanpham_id"),
//                        rs.getString("ten_sanpham"),
//                        rs.getBigDecimal("price"),
//                        rs.getInt("so_luong_ban"),
//                        rs.getBigDecimal("doanh_thu"),
//                        rs.getString("anh_mac_dinh")
//                )
//        );
//
//        // Implement pagination manually since we can't use Pageable directly with jdbctemplate
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), results.size());
//        return new PageImpl<>(results.subList(start, end), pageable, results.size());
//    }


//    @Override
//    public Page<ThongKe> getSanPhamBanChayTrongKhoangThoiGian(Date startDate, Date endDate, Pageable pageable) {
//        // SQL query to select products and their sales data within a given time range
//        String sql = "SELECT " +
//                "    sp.Id AS sanpham_id, " +
//                "    sp.Name AS ten_sanpham, " +
//                "    sp.Price AS price, " +
//                "    COALESCE(SUM(hdct.quantity), 0) AS so_luong_ban, " +
//                "    COALESCE(SUM(hdct.quantity * sp.Price), 0) AS doanh_thu, " +
//                "    MAX(ha.Path) AS anh_mac_dinh " + // Ensure to get the most relevant image
//                "FROM " +
//                "    ProductDetails pd " +
//                "JOIN " +
//                "    BillDetails hdct ON pd.Id = hdct.IdProductDetail " +
//                "JOIN " +
//                "    Bills b ON hdct.IdBill = b.Id " +
//                "JOIN " +
//                "    Products sp ON pd.IdProduct = sp.Id " +
//                "LEFT JOIN " +
//                "    Images ha ON sp.Id = ha.IdProduct " + // Fixed joining condition for images
//                "JOIN " +
//                "    PaymentStatus ps ON b.Id = ps.BillId " +
//                "WHERE " +
//                "    ps.PaymentDate BETWEEN ? AND ? " +
//                "    AND ps.CustomerPaymentStatus = 2 " +
//                "    AND b.Status = 21 " +
//                "GROUP BY " +
//                "    sp.Id, sp.Name, sp.Price " +
//                "ORDER BY " +
//                "    so_luong_ban DESC";
//
//        List<ThongKe> results = jdbctemplate.query(
//                sql,
//                new Object[]{startDate, endDate},
//                (rs, rowNum) -> new ThongKe(
//                        rs.getLong("sanpham_id"),
//                        rs.getString("ten_sanpham"),
//                        rs.getBigDecimal("price"),
//                        rs.getInt("so_luong_ban"),
//                        rs.getBigDecimal("doanh_thu"),
//                        rs.getString("anh_mac_dinh")
//                )
//        );
//
//        // Implement pagination manually
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), results.size());
//        return new PageImpl<>(results.subList(start, end), pageable, results.size());
//    }




    //====================Khách hàng mua hàng nhiều nhất
//    @Override
//    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNgay(Date date, Pageable pageable) {
//        // Query to fetch customer statistics similar to the conditions in findTotalQuantitySoldByDate
//        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
//                "SELECT " +
//                        "    c.Id AS khachhang_id, " +
//                        "    c.FullName AS ten_khachhang, " +
//                        "    COALESCE(SUM(hdct.quantity - COALESCE(ro.returnQuantity, 0)), 0) AS tong_so_luong_mua, " +
//                        "    COALESCE(SUM(b.totalAmountAfterDiscount) - COALESCE(SUM(p.paymentAmount), 0), 0) AS tong_doanh_thu, " +
//                        "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
//                        "FROM " +
//                        "    Customers c " +
//                        "JOIN " +
//                        "    Bills b ON c.Id = b.idCustomer " +
//                        "JOIN " +
//                        "    BillDetails hdct ON b.Id = hdct.IdBill " +
//                        "LEFT JOIN ( " +
//                        "    SELECT r.IdBillDetail, SUM(r.Quantity) AS returnQuantity " +
//                        "    FROM ReturnOrders r " +
//                        "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
//                        "    WHERE p.PaymentType = 4 " +
//                        "    GROUP BY r.IdBillDetail " +
//                        ") ro ON hdct.id = ro.IdBillDetail " +
//                        "LEFT JOIN PaymentStatus p ON b.Id = p.BillId AND p.PaymentType = 4 " +
//                        "WHERE " +
//                        "    EXISTS ( " +
//                        "        SELECT 1 " +
//                        "        FROM BillHistories bh " +
//                        "        WHERE bh.BillId = b.id " +
//                        "        AND bh.Status = 21 " +
//                        "    ) " +
//                        "    AND CAST(b.createdAt AS DATE) = ? " +
//                        "GROUP BY " +
//                        "    c.Id, c.FullName " +
//                        "ORDER BY " +
//                        "    tong_so_luong_mua DESC",
//                new Object[]{date},
//                (rs, rowNum) -> new ThongKeKhachHang(
//                        rs.getLong("khachhang_id"),
//                        rs.getString("ten_khachhang"),
//                        rs.getInt("tong_so_luong_mua"),
//                        rs.getBigDecimal("tong_doanh_thu"),
//                        rs.getInt("tong_so_bill")
//                )
//        );
//
//        // Apply pagination in Java
//        int total = khachHangList.size();
//        int start = (int) pageable.getOffset();
//        int end = Math.min((start + pageable.getPageSize()), total);
//        List<ThongKeKhachHang> pagedKhachHangList = khachHangList.subList(start, end);
//
//        return new PageImpl<>(pagedKhachHangList, pageable, total);
//    }
//
//
//
//    @Override
//    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuan(Date date, Pageable pageable) {
//        String query = "SELECT " +
//                "    c.Id AS khachhang_id, " +
//                "    c.FullName AS ten_khachhang, " +
//                "    COALESCE(SUM(hdct.quantity - COALESCE(ro.returnQuantity, 0)), 0) AS tong_so_luong_mua, " +
//                "    COALESCE(SUM((hdct.quantity - COALESCE(ro.returnQuantity, 0)) * hdct.PromotionalPrice), 0) AS tong_doanh_thu, " +
//                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
//                "FROM " +
//                "    Customers c " +
//                "JOIN " +
//                "    Bills b ON c.Id = b.idCustomer " +
//                "JOIN " +
//                "    BillDetails hdct ON b.Id = hdct.IdBill " +
//                "LEFT JOIN ( " +
//                "    SELECT r.IdBillDetail, SUM(r.Quantity) AS returnQuantity " +
//                "    FROM ReturnOrders r " +
//                "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
//                "    WHERE p.PaymentType = 4 " +
//                "    GROUP BY r.IdBillDetail " +
//                ") ro ON hdct.id = ro.IdBillDetail " +
//                "WHERE " +
//                "    EXISTS ( " +
//                "        SELECT 1 " +
//                "        FROM BillHistories bh " +
//                "        WHERE bh.BillId = b.id " +
//                "        AND bh.Status = 21 " +
//                "    ) " +
//                "    AND DATEPART(WEEK, b.createdAt) = DATEPART(WEEK, ?) " +
//                "    AND DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, ?) " +
//                "GROUP BY " +
//                "    c.Id, c.FullName " +
//                "ORDER BY " +
//                "    tong_so_luong_mua DESC " +
//                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//
//        // Thực hiện truy vấn và phân trang
//        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
//                query,
//                new Object[]{date, date, pageable.getOffset(), pageable.getPageSize()},
//                (rs, rowNum) -> new ThongKeKhachHang(
//                        rs.getLong("khachhang_id"),
//                        rs.getString("ten_khachhang"),
//                        rs.getInt("tong_so_luong_mua"),
//                        rs.getBigDecimal("tong_doanh_thu"),
//                        rs.getInt("tong_so_bill")
//                )
//        );
//
//        String countQuery = "SELECT COUNT(*) FROM (" + query + ") AS countTable";
//        int total = jdbctemplate.queryForObject(countQuery, new Object[]{date, date}, Integer.class);
//
//        return new PageImpl<>(khachHangList, pageable, total);
//    }
//
//    @Override
//    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatThang(Date date, Pageable pageable) {
//        String query = "SELECT " +
//                "    c.Id AS khachhang_id, " +
//                "    c.FullName AS ten_khachhang, " +
//                "    COALESCE(SUM(hdct.quantity - COALESCE(ro.returnQuantity, 0)), 0) AS tong_so_luong_mua, " +
//                "    COALESCE(SUM((hdct.quantity - COALESCE(ro.returnQuantity, 0)) * hdct.PromotionalPrice), 0) AS tong_doanh_thu, " +
//                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
//                "FROM " +
//                "    Customers c " +
//                "JOIN " +
//                "    Bills b ON c.Id = b.idCustomer " +
//                "JOIN " +
//                "    BillDetails hdct ON b.Id = hdct.IdBill " +
//                "LEFT JOIN ( " +
//                "    SELECT r.IdBillDetail, SUM(r.Quantity) AS returnQuantity " +
//                "    FROM ReturnOrders r " +
//                "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
//                "    WHERE p.PaymentType = 4 " +
//                "    GROUP BY r.IdBillDetail " +
//                ") ro ON hdct.id = ro.IdBillDetail " +
//                "WHERE " +
//                "    EXISTS ( " +
//                "        SELECT 1 " +
//                "        FROM BillHistories bh " +
//                "        WHERE bh.BillId = b.id " +
//                "        AND bh.Status = 21 " +
//                "    ) " +
//                "    AND DATEPART(MONTH, b.createdAt) = DATEPART(MONTH, ?) " +
//                "    AND DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, ?) " +
//                "GROUP BY " +
//                "    c.Id, c.FullName " +
//                "ORDER BY " +
//                "    tong_so_luong_mua DESC " +
//                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//
//        // Thực hiện truy vấn và phân trang
//        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
//                query,
//                new Object[]{date, date, pageable.getOffset(), pageable.getPageSize()},
//                (rs, rowNum) -> new ThongKeKhachHang(
//                        rs.getLong("khachhang_id"),
//                        rs.getString("ten_khachhang"),
//                        rs.getInt("tong_so_luong_mua"),
//                        rs.getBigDecimal("tong_doanh_thu"),
//                        rs.getInt("tong_so_bill")
//                )
//        );
//
//        String countQuery = "SELECT COUNT(*) FROM (" + query + ") AS countTable";
//        int total = jdbctemplate.queryForObject(countQuery, new Object[]{date, date}, Integer.class);
//
//        return new PageImpl<>(khachHangList, pageable, total);
//    }
//
//    @Override
//    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNam(Date date, Pageable pageable) {
//        String query = "SELECT " +
//                "    c.Id AS khachhang_id, " +
//                "    c.FullName AS ten_khachhang, " +
//                "    COALESCE(SUM(hdct.quantity - COALESCE(ro.returnQuantity, 0)), 0) AS tong_so_luong_mua, " +
//                "    COALESCE(SUM((hdct.quantity - COALESCE(ro.returnQuantity, 0)) * hdct.PromotionalPrice), 0) AS tong_doanh_thu, " +
//                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
//                "FROM " +
//                "    Customers c " +
//                "JOIN " +
//                "    Bills b ON c.Id = b.idCustomer " +
//                "JOIN " +
//                "    BillDetails hdct ON b.Id = hdct.IdBill " +
//                "LEFT JOIN ( " +
//                "    SELECT r.IdBillDetail, SUM(r.Quantity) AS returnQuantity " +
//                "    FROM ReturnOrders r " +
//                "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
//                "    WHERE p.PaymentType = 4 " +
//                "    GROUP BY r.IdBillDetail " +
//                ") ro ON hdct.id = ro.IdBillDetail " +
//                "WHERE " +
//                "    EXISTS ( " +
//                "        SELECT 1 " +
//                "        FROM BillHistories bh " +
//                "        WHERE bh.BillId = b.id " +
//                "        AND bh.Status = 21 " +
//                "    ) " +
//                "    AND DATEPART(YEAR, b.createdAt) = DATEPART(YEAR, ?) " +
//                "GROUP BY " +
//                "    c.Id, c.FullName " +
//                "ORDER BY " +
//                "    tong_so_luong_mua DESC " +
//                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//
//        // Thực hiện truy vấn và phân trang
//        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
//                query,
//                new Object[]{date, pageable.getOffset(), pageable.getPageSize()},
//                (rs, rowNum) -> new ThongKeKhachHang(
//                        rs.getLong("khachhang_id"),
//                        rs.getString("ten_khachhang"),
//                        rs.getInt("tong_so_luong_mua"),
//                        rs.getBigDecimal("tong_doanh_thu"),
//                        rs.getInt("tong_so_bill")
//                )
//        );
//
//        String countQuery = "SELECT COUNT(*) FROM (" + query + ") AS countTable";
//        int total = jdbctemplate.queryForObject(countQuery, new Object[]{date}, Integer.class);
//
//        return new PageImpl<>(khachHangList, pageable, total);
//    }

//    @Override
//    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuyChinh(Date startDate, Date endDate, Pageable pageable) {
//        String query = "SELECT " +
//                "    c.Id AS khachhang_id, " +
//                "    c.FullName AS ten_khachhang, " +
//                "    COALESCE(SUM(hdct.quantity - COALESCE(ro.returnQuantity, 0)), 0) AS tong_so_luong_mua, " +
//                "    COALESCE(SUM((hdct.quantity - COALESCE(ro.returnQuantity, 0)) * hdct.PromotionalPrice), 0) AS tong_doanh_thu, " +
//                "    COUNT(DISTINCT b.Id) AS tong_so_bill " +
//                "FROM " +
//                "    Customers c " +
//                "JOIN " +
//                "    Bills b ON c.Id = b.idCustomer " +
//                "JOIN " +
//                "    BillDetails hdct ON b.Id = hdct.IdBill " +
//                "LEFT JOIN ( " +
//                "    SELECT r.IdBillDetail, SUM(r.Quantity) AS returnQuantity " +
//                "    FROM ReturnOrders r " +
//                "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
//                "    WHERE p.PaymentType = 4 " +
//                "    GROUP BY r.IdBillDetail " +
//                ") ro ON hdct.id = ro.IdBillDetail " +
//                "WHERE " +
//                "    EXISTS ( " +
//                "        SELECT 1 " +
//                "        FROM BillHistories bh " +
//                "        WHERE bh.BillId = b.id " +
//                "        AND bh.Status = 21 " +
//                "    ) " +
//                "    AND b.createdAt BETWEEN ? AND ? " +
//                "GROUP BY " +
//                "    c.Id, c.FullName " +
//                "ORDER BY " +
//                "    tong_so_luong_mua DESC " +
//                "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
//
//        // Thực hiện truy vấn và phân trang
//        List<ThongKeKhachHang> khachHangList = jdbctemplate.query(
//                query,
//                new Object[]{startDate, endDate, pageable.getOffset(), pageable.getPageSize()},
//                (rs, rowNum) -> new ThongKeKhachHang(
//                        rs.getLong("khachhang_id"),
//                        rs.getString("ten_khachhang"),
//                        rs.getInt("tong_so_luong_mua"),
//                        rs.getBigDecimal("tong_doanh_thu"),
//                        rs.getInt("tong_so_bill")
//                )
//        );
//
//        String countQuery = "SELECT COUNT(*) FROM (" + query + ") AS countTable";
//        int total = jdbctemplate.queryForObject(countQuery, new Object[]{startDate, endDate}, Integer.class);
//
//        return new PageImpl<>(khachHangList, pageable, total);
//    }


//    @Override
//    public List<ThongKe> getBySanPhamBanChayNgay(Date date, Long productId) {
//        String query = "SELECT " +
//                "    sp.Id AS sanpham_id, " +
//                "    sp.Name AS ten_sanpham, " +
//                "    sp.Price AS price, " +
//                "    COALESCE(SUM(hdct.quantity - COALESCE(ro.returnQuantity, 0)), 0) AS so_luong_ban, " +
//                "    COALESCE(SUM((hdct.quantity - COALESCE(ro.returnQuantity, 0)) * hdct.PromotionalPrice), 0) AS doanh_thu, " +
//                "    COALESCE(MAX(ha.Name), '') AS anh_mac_dinh " +
//                "FROM " +
//                "    Products sp " +
//                "JOIN " +
//                "    ProductDetails pd ON sp.Id = pd.IdProduct " +
//                "JOIN " +
//                "    BillDetails hdct ON pd.Id = hdct.IdProductDetail " +
//                "JOIN " +
//                "    Bills b ON hdct.IdBill = b.Id " +
//                "LEFT JOIN " +
//                "    Images ha ON sp.Id = ha.IdProduct " +
//                "LEFT JOIN ( " +
//                "    SELECT r.IdBillDetail, SUM(r.Quantity) AS returnQuantity " +
//                "    FROM ReturnOrders r " +
//                "    JOIN PaymentStatus p ON r.IdBill = p.BillId " +
//                "    WHERE p.PaymentType = 4 " +
//                "    GROUP BY r.IdBillDetail " +
//                ") ro ON hdct.Id = ro.IdBillDetail " +
//                "WHERE " +
//                "    EXISTS ( " +
//                "        SELECT 1 " +
//                "        FROM BillHistories bh " +
//                "        WHERE bh.BillId = b.Id " +
//                "        AND bh.Status = 21 " +
//                "    ) " +
//                "    AND CAST(b.createdAt AS DATE) = CAST(? AS DATE) " + // Điều kiện lọc theo ngày
//                "    AND sp.Id = ? " + // Điều kiện lọc theo productId
//                "GROUP BY " +
//                "    sp.Id, sp.Name, sp.Price " +
//                "ORDER BY " +
//                "    so_luong_ban DESC";
//
//        return jdbctemplate.query(
//                query,
//                new Object[]{date, productId},
//                (rs, rowNum) -> new ThongKe(
//                        rs.getLong("sanpham_id"),
//                        rs.getString("ten_sanpham"),
//                        rs.getBigDecimal("price"),
//                        rs.getInt("so_luong_ban"),
//                        rs.getBigDecimal("doanh_thu"),
//                        rs.getString("anh_mac_dinh")
//                )
//        );
//    }


    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNgay(Pageable pageable) {
        Date today = new Date();
        return getKhachHangMuaNhieuNhat(today, "day", pageable);
    }

    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuan(Pageable pageable) {
        Date today = new Date();
        return getKhachHangMuaNhieuNhat(today, "week", pageable);
    }

    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatThang(Pageable pageable) {
        Date today = new Date();
        return getKhachHangMuaNhieuNhat(today, "month", pageable);
    }

    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatNam(Pageable pageable) {
        Date today = new Date();
        return getKhachHangMuaNhieuNhat(today, "year", pageable);
    }

    private Page<ThongKeKhachHang> getKhachHangMuaNhieuNhat(Date date, String period, Pageable pageable) {
        // Validate period
        if (!Arrays.asList("day", "week", "month", "year").contains(period)) {
            throw new IllegalArgumentException("Invalid period. Must be one of 'day', 'week', 'month', 'year'.");
        }

        // Execute the query to get all results without pagination
        List<Map<String, Object>> result = billRepositoryTinh.findKhachHangMuaNhieuNhat(date, period);

        // Map the results to ThongKeKhachHang objects
        List<ThongKeKhachHang> khachHangList = result.stream()
                .map(row -> new ThongKeKhachHang(
                        ((Number) row.get("customer_id")).longValue(),
                        (String) row.get("customer_name"),
                        (String) row.get("customer_avatar"),  // Mapping avatar
                        ((Number) row.get("adjusted_quantity")).intValue(),
                        (BigDecimal) row.get("adjusted_revenue")
                ))
                .collect(Collectors.toList());

        // Manually paginate the list
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), khachHangList.size());
        List<ThongKeKhachHang> paginatedList = khachHangList.subList(start, end);

        // Return the paginated result as a Page
        return new PageImpl<>(paginatedList, pageable, khachHangList.size());
    }


    @Override
    public Page<ThongKeKhachHang> getKhachHangMuaNhieuNhatTuyChinh(Date startDate, Date endDate, Pageable pageable) {
        // Execute the query to get all results without pagination
        List<Map<String, Object>> result = billRepositoryTinh.findKhachHangMuaNhieuNhatTuyChon(startDate, endDate);

        // Map the results to ThongKeKhachHang objects
        List<ThongKeKhachHang> khachHangList = result.stream()
                .map(row -> new ThongKeKhachHang(
                        ((Number) row.get("customer_id")).longValue(),
                        (String) row.get("customer_name"),
                        (String) row.get("customer_avatar"),  // Mapping avatar
                        ((Number) row.get("adjusted_quantity")).intValue(),
                        (BigDecimal) row.get("adjusted_revenue")
                ))
                .collect(Collectors.toList());

        // Manually paginate the list
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), khachHangList.size());
        List<ThongKeKhachHang> paginatedList = khachHangList.subList(start, end);

        // Return the paginated result as a Page
        return new PageImpl<>(paginatedList, pageable, khachHangList.size());
    }

    @Override
    public List<ThongKe> getBySanPhamBanChayNgay(Date date, Long productId) {
        return null;
    }

}
