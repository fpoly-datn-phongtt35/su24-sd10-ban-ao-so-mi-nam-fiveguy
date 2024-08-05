package com.example.demo.restController.tinh;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Employee;
import com.example.demo.entity.ThongKe;
import com.example.demo.entity.ThongKeKhachHang;
import com.example.demo.repository.tinh.BillRepositoryTinh;
import com.example.demo.security.service.SCEmployeeService;
import com.example.demo.service.tinh.BillServiceTinh;
import com.example.demo.untility.tinh.PaginationResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin/bill-tinh")
public class BillRestControllerTinh {
    @Autowired
    BillServiceTinh billServiceTinh;

    @Autowired
    SCEmployeeService scEmployeeService;

    @Autowired
    BillRepositoryTinh billRepositoryTinh;

    //Tổng doanh thu =========================================================================
    @GetMapping("/tong-doanh-thu-ngay/{sl}")
    public ResponseEntity<BigDecimal> tongTienDay(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        BigDecimal customers = billRepositoryTinh.tongSoTienDay(sl);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/tong-doanh-thu-tuan/{sl}")
    public ResponseEntity<BigDecimal> tongTienWeek(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        BigDecimal customers = billRepositoryTinh.tongSoTienWeek(sl);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/tong-doanh-thu-thang/{sl}")
    public ResponseEntity<BigDecimal> tongTienMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        BigDecimal customers = billRepositoryTinh.tongSoTienMonth(sl);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/tong-doanh-thu-nam/{sl}")
    public ResponseEntity<BigDecimal> tongTienYear(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        BigDecimal customers = billRepositoryTinh.tongSoTienYear(sl);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/tong-doanh-thu-tuy-chinh")
    public ResponseEntity<BigDecimal> tongTienOption(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        BigDecimal totalAmount = billRepositoryTinh.tongSoTienOption(startDate, endDate);
        return ResponseEntity.ok(totalAmount);
    }

    // End Tổng doanh thu ================================================================

    //Tỏng dơne hàng Thanh Cong====================================================================
    @GetMapping("/tong-hoa-don-ngay/{sl}")
    public int sumBillDay(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillThanhCongDay(sl).size();
    }
    @GetMapping("/tong-hoa-don-tuan/{sl}")
    public int sumBillTuan(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillThanhCongWeek(sl).size();
    }
    @GetMapping("/tong-hoa-don-thang/{sl}")
    public int sumBillMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillThanhCongMonth(sl).size();
    }
    @GetMapping("/tong-hoa-don-nam/{sl}")
    public int sumBillYear(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillThanhCongYear(sl).size();
    }
    @GetMapping("/tong-hoa-don-tuy-chinh")
    public int sumBillOption(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return billRepositoryTinh.tongBillThanhCongOption(startDate, endDate).size();

    }
    //End Tổng dơne Hàng Thanh COng================================================================

    //Tỏng dơne hàng Huy ====================================================================
    @GetMapping("/tong-hoa-don-huy-ngay/{sl}")
    public int sumBillHuyDay(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillHuyDay(sl).size();
    }
    @GetMapping("/tong-hoa-don-huy-tuan/{sl}")
    public int sumBillHuyWeed(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillHuyWeek(sl).size();
    }
    @GetMapping("/tong-hoa-don-huy-thang/{sl}")
    public int sumBillHuyMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillHuyMonth(sl).size();
    }
    @GetMapping("/tong-hoa-don-huy-nam/{sl}")
    public int sumBillHuyYear(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date sl) {
        return billRepositoryTinh.tongBillHuyYear(sl).size();
    }
    @GetMapping("/tong-hoa-don-huy-tuy-chinh")
    public int getCancelledBillsBetweenDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return billRepositoryTinh.tongBillHuyOption(startDate, endDate).size();
    }
    //End Tổng dơne Hàng Huy================================================================

    //San phẩm bán chạy ==================================================================
    @GetMapping("/top-ban-chay-ngay")
    public ResponseEntity<?> topBanChayNgay(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKe> sanPhamPage = billServiceTinh.getSanPhamBanChayNgay(date, pageable);
        return ResponseEntity.ok().body(sanPhamPage);
    }

    @GetMapping("/top-ban-chay-tuan")
    public ResponseEntity<?> topBanChayTuan(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKe> sanPhamPage = billServiceTinh.getSanPhamBanChayTuan(date, pageable);
        return ResponseEntity.ok().body(sanPhamPage);
    }

    @GetMapping("/top-ban-chay-thang")
    public ResponseEntity<?> topBanChayThang(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKe> sanPhamPage = billServiceTinh.getSanPhamBanChayThang(date, pageable);
        return ResponseEntity.ok().body(sanPhamPage);
    }

    @GetMapping("/top-ban-chay-nam")
    public ResponseEntity<?> topBanChayNam(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKe> sanPhamPage = billServiceTinh.getSanPhamBanChayNam(date, pageable);
        return ResponseEntity.ok().body(sanPhamPage);
    }

    @GetMapping("/top-ban-chay-khoang-thoi-gian")
    public ResponseEntity<?> topBanChayKhoangThoiGian(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKe> sanPhamPage = billServiceTinh.getSanPhamBanChayTrongKhoangThoiGian(startDate, endDate, pageable);
        return ResponseEntity.ok().body(sanPhamPage);
    }
//    public ResponseEntity<Page<ThongKe>> getSanPhamBanChayTrongKhoangThoiGian(
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
//            @RequestParam int page,
//            @RequestParam int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<ThongKe> result = billServiceTinh.getSanPhamBanChayTrongKhoangThoiGian(startDate, endDate, pageable);
//        return ResponseEntity.ok(result);
//    }
    //End Sản phẩm bán chạy ==============================================================

    //Tông bill theo status
    @GetMapping("/tong-hoa-don-trang-thai-ngay")
    public int tongHoaDonStatusNgay(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam Integer status) {
        return billRepositoryTinh.tongStatusBillDay(startDate, status).size();
    }

    @GetMapping("/tong-hoa-don-trang-thai-tuan")
    public int tongHoaDonStatusTuan(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam Integer status) {
        return billRepositoryTinh.tongStatusBillWeek(startDate, status).size();
    }

    @GetMapping("/tong-hoa-don-trang-thai-thang")
    public int tongHoaDonStatusThang(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam Integer status) {
        return billRepositoryTinh.tongStatusBillMonth(startDate, status).size();
    }

    @GetMapping("/tong-hoa-don-trang-thai-nam")
    public int tongHoaDonStatusNam(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam Integer status) {
        return billRepositoryTinh.tongStatusBillYear(startDate, status).size();
    }
    @GetMapping("/tong-hoa-don-trang-thai-tuy-chinh")
    public int tongHoaDonStatusTuyChinh(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam Integer status) {
        return billRepositoryTinh.tongStatusBillOption(startDate,endDate, status).size();
    }

    //Khách hàng mua nhiều nhất-----------------------------------------------
    @GetMapping("/khach-hang-mua-nhieu-nhat-ngay")
    public ResponseEntity<?> khachHangMuaNhietNhatNgay(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<ThongKeKhachHang> khachHangPage = billServiceTinh.getKhachHangMuaNhieuNhatNgay(date, PageRequest.of(page, size));
        return ResponseEntity.ok().body(khachHangPage);
    }

    @GetMapping("/khach-hang-mua-nhieu-nhat-tuan")
    public ResponseEntity<?> khachHangMuaNhieuNhatTuan(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<ThongKeKhachHang> khachHangPage = (Page<ThongKeKhachHang>) billServiceTinh.getKhachHangMuaNhieuNhatTuan(date, PageRequest.of(page, size));
        return ResponseEntity.ok().body(khachHangPage);
    }


    @GetMapping("/khach-hang-mua-nhieu-nhat-thang")
    public ResponseEntity<?> khachHangMuaNhieuNhatThang(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKeKhachHang> khachHangPage = billServiceTinh.getKhachHangMuaNhieuNhatThang(date, pageable);
        return ResponseEntity.ok().body(khachHangPage);
    }


    @GetMapping("/khach-hang-mua-nhieu-nhat-nam")
    public ResponseEntity<?> khachHangMuaNhieuNhatNam(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKeKhachHang> khachHangPage = billServiceTinh.getKhachHangMuaNhieuNhatNam(date, pageable);
        return ResponseEntity.ok().body(khachHangPage);
    }


    @GetMapping("/khach-hang-mua-nhieu-nhat-tuy-chinh")
    public ResponseEntity<?> khachHangMuaNhieuNhatTuyChinh(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThongKeKhachHang> khachHangPage = billServiceTinh.getKhachHangMuaNhieuNhatTuyChinh(startDate, endDate, pageable);
        return ResponseEntity.ok().body(khachHangPage);
    }



    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Bill bill) {
        try {
            Bill createdBill = billServiceTinh.create(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/save", produces = "application/json")
    public Bill createBill(@RequestHeader("Authorization") String token, @RequestBody Bill bill) {
        Bill bill1 = new Bill();
        Optional<Employee> employee = scEmployeeService.getEmployeeByToken(token);

        String randomCode = generateRandomCode(6);
        bill1.setCode(randomCode);
        bill1.setReciverName(bill.getReciverName());
        bill1.setDeliveryDate(bill.getDeliveryDate());
        bill1.setShippingFee(bill.getShippingFee());
//        bill1.setTransId(bill.getTransId());
        bill1.setAddress(bill.getAddress());
        bill1.setPhoneNumber(bill.getPhoneNumber());
        bill1.setTotalAmount(bill.getTotalAmount());
        bill1.setTotalAmountAfterDiscount(bill.getTotalAmountAfterDiscount());
        bill1.setPaymentMethod(bill.getPaymentMethod());
        bill1.setVoucher(bill.getVoucher());
        bill1.setNote(bill.getNote());
        bill1.setReason(bill.getReason());
        bill1.setEmployee(employee.get());
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

    @PutMapping("/update-bill-status-thanh-cong/{id}")
    public ResponseEntity<String> updateBillStatus(@PathVariable Long id) {
        try {
            billServiceTinh.updateBillStatus(id);
            return ResponseEntity.ok("Bill status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating Bill status");
        }
    }

    @GetMapping("/page")
    public PaginationResponse<Bill> getBillPage(
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = true, defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, size, sort);
        Page<Bill> page = billRepositoryTinh.getAllBillChoThanhToan(pageable);
        return new PaginationResponse<>(page);
    }

}
