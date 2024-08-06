app.controller("DashboardController", function ($scope, $http, $filter) {
    $scope.selectedButton = "";
    $scope.showDiv = false;

    // Hàm hiển thị thông báo thành công
    $scope.showSuccessNotification = function (message) {
        toastr["success"](message);
    };

    // Hàm hiển thị thông báo lỗi
    $scope.showErrorNotification = function (message) {
        toastr["error"](message);
    };

    $scope.showWarningNotification = function (message) {
        toastr["warning"](message);
    };

    //Validate ngày hiện tại
    var today = new Date();
    today.setDate(today.getDate());
    let todayfomat = $filter("date")(today, "yyyy-MM-dd");

    // Ngày hôm qua
    var yesterday = new Date(today);
    yesterday.setDate(yesterday.getDate() - 1);
    let yesterdayFormatted = $filter("date")(yesterday, "yyyy-MM-dd");

    // Tháng trước
    var lastMonth = new Date(today);
    lastMonth.setMonth(lastMonth.getMonth() - 1);
    let lastMonthFormatted = $filter("date")(lastMonth, "yyyy-MM-dd");

    // Năm trước
    var lastYear = new Date(today);
    lastYear.setFullYear(lastYear.getFullYear() - 1);
    let lastYearFormatted = $filter("date")(lastYear, "yyyy-MM-dd");

    //Tỏng hóa đơn then trang thái===================================================================================
    $scope.tongBillStatuses = {};

    $scope.getTongBillStatus = function (url, status) {
        return $http
            .get(`${url}?startDate=${todayfomat}&status=${status}`)
            .then(function (response) {
                $scope.tongBillStatuses[status] = response.data;
                // console.log(`Status ${status}:`, response.data);
            })
            .catch(function (error) {
                console.error(`Có lỗi xảy ra khi gọi API với status=${status}:`, error);
            });
    };

    $scope.getTongBillStatusNgay = function () {
        let promises = [1, 2, 3, 4, 5, 6, 21, 32, 8, 9].map((status) =>
            $scope.getTongBillStatus(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-trang-thai-ngay",
                status
            )
        );
        Promise.all(promises).then(updateChart);
    };

    $scope.getTongBillStatusTuan = function () {
        let promises = [5, 6, 21, 32].map((status) =>
            $scope.getTongBillStatus(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-trang-thai-tuan",
                status
            )
        );
        Promise.all(promises).then(updateChart);
    };

    $scope.getTongBillStatusThang = function () {
        let promises = [1, 2, 3, 4, 5, 6, 21, 32, 8, 9].map((status) =>
            $scope.getTongBillStatus(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-trang-thai-thang",
                status
            )
        );
        Promise.all(promises).then(updateChart);
    };

    $scope.getTongBillStatusNam = function () {
        let promises = [1, 2, 3, 4, 5, 6, 21, 32, 8, 9].map((status) =>
            $scope.getTongBillStatus(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-trang-thai-nam",
                status
            )
        );
        Promise.all(promises).then(updateChart);
    };

    $scope.getTongBillStatusTuyChinh = function () {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(
                new Date(startDate),
                "yyyy-MM-dd"
            );
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Tạo một mảng các promises để gọi API cho từng trạng thái
            const statusPromises = [1, 2, 3, 4, 5, 6, 21, 32, 8, 9].map((status) =>
                $scope.getTongBillStatus(
                    `http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-trang-thai-tuy-chinh?startDate=${formattedStartDate}&endDate=${formattedEndDate}`,
                    status
                )
            );
            Promise.all(statusPromises).then(updateChart);
        }
    };

    // Đảm bảo getTongBillStatus trả về promise từ $http.get

    //===============Sơ đồ Hình tròn=============================================================

    const ctx = document.getElementById("myChart").getContext("2d");
    let myChart = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: [
                "Chờ Xác nhận",
                "Chờ Vận chuyển",
                "Đang giao",
                "Thành công",
                "Đã hủy",
                "Thất bại",
                "Chờ giao lại",
                "Đang giao lại",
                "Đơn trả",
            ],
            datasets: [
                {
                    label: "Số lượng hóa đơn theo trạng thái",
                    data: [0, 0, 0, 0, 0, 0, 0, 0, 0], // Mảng dữ liệu sẽ được cập nhật sau
                    backgroundColor: [
                        "#eca147",
                        "rgb(54, 162, 235)",
                        "#45e144",
                        "#459446",
                        "#dc3545",
                        "#ff7c4e",
                        "rgb(255, 205, 86)",
                        "#0dcaf0",
                        "rgb(255, 99, 132)",
                    ],
                    hoverOffset: 4,
                },
            ],
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: "top",
                },
                tooltip: {
                    callbacks: {
                        label: function (tooltipItem) {
                            return tooltipItem.label + ": " + tooltipItem.raw;
                        },
                    },
                },
            },
        },
    });
    function updateChart() {
        // Chuyển các giá trị từ mảng tongBillStatuses thành mảng dữ liệu cho biểu đồ
        const data = [
            $scope.tongBillStatuses[1] || 0,
            $scope.tongBillStatuses[2] || 0,
            $scope.tongBillStatuses[3] || 0,
            $scope.tongBillStatuses[21] || 0,
            ($scope.tongBillStatuses[5] || 0) + ($scope.tongBillStatuses[6] || 0),
            ($scope.tongBillStatuses[7] || 0) + ($scope.tongBillStatuses[8] || 0),
            $scope.tongBillStatuses[9] || 0,
            $scope.tongBillStatuses[10] || 0,
            $scope.tongBillStatuses[32] || 0,
        ];

        // Cập nhật dữ liệu của biểu đồ
        myChart.data.datasets[0].data = data;
        myChart.update();
    }
    //===============END Sơ đồ Hình tròn=============================================================

    // ===========================get All tổng doang thu======================================
    $scope.tongDoanhThuNgay = 0;
    $scope.tongDoanhThuTuan = 0;
    $scope.tongDoanhThuThang = 0;
    $scope.tongDoanhThuNam = 0;
    $scope.tongDoanhThuTuyChinh = 0;

    $scope.getTongDoangThuNgay = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-ngay/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDoanhThuNgay = response.data;
            });
    };
    $scope.getTongDoangThuNgay();

    $scope.getTongDoangThuTuan = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-tuan/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDoanhThuTuan = response.data;
            });
    };
    $scope.getTongDoangThuTuan();

    $scope.getTongDoangThuThang = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-thang/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDoanhThuThang = response.data;
            });
    };
    $scope.getTongDoangThuThang();

    $scope.getTongDoangThuNam = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-nam/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDoanhThuNam = response.data;
            });
    };
    $scope.getTongDoangThuNam();

    $scope.getTongDoanhThuTuyChinh = function () {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(
                new Date(startDate),
                "yyyy-MM-dd"
            );
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Gọi API với ngày bắt đầu và ngày kết thúc
            $http
                .get(
                    `http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-tuy-chinh?startDate=${formattedStartDate}&endDate=${formattedEndDate}`
                )
                .then(function (response) {
                    $scope.tongDoanhThuTuyChinh = response.data;
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi gọi API:", error);
                });
        }
    };

    // doanh thu của ngày tháng năm trước đó
    $scope.getTongDoangThuNgayTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-ngay/" +
                `${yesterdayFormatted}`
            )
            .then(function (response) {
                $scope.tongDoanhThuNgayTruoc = response.data;
            });
    };
    $scope.getTongDoangThuNgayTruoc();

    $scope.getTongDoangThuThangTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-thang/" +
                `${lastMonthFormatted}`
            )
            .then(function (response) {
                $scope.tongDoanhThuThangTruoc = response.data;
            });
    };
    $scope.getTongDoangThuThangTruoc();

    $scope.getTongDoangThuNamTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-nam/" +
                `${lastYearFormatted}`
            )
            .then(function (response) {
                $scope.tongDoanhThuNamTruoc = response.data;
            });
    };
    $scope.getTongDoangThuNamTruoc();

    //=================================End Get All tổng doanh thu=============================

    // =========================get All tổng Sản phẩm bán được========================================
    $scope.tongSanPhamNgay = 0;
    $scope.tongSanPhamTuan = 0;
    $scope.tongSanPhamThang = 0;
    $scope.tongSanPhamNam = 0;
    $scope.tongSanPhamTuyChinh = 0;

    $scope.getTongSanPhamNgay = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-ngay/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongSanPhamNgay = response.data;
            });
    };
    $scope.getTongSanPhamNgay();

    $scope.getTongSanPhamTuan = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-tuan/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongSanPhamTuan = response.data;
            });
    };
    $scope.getTongSanPhamTuan();

    $scope.getTongSanPhamThang = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-thang/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongSanPhamThang = response.data;
            });
    };
    $scope.getTongSanPhamThang();

    $scope.getTongSanPhamNam = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-nam/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongSanPhamNam = response.data;
            });
    };
    $scope.getTongSanPhamNam();

    $scope.getTongSanPhamTuyChinh = function () {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(
                new Date(startDate),
                "yyyy-MM-dd"
            );
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Gọi API với ngày bắt đầu và ngày kết thúc
            $http
                .get(
                    `http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-tuy-chinh?startDate=${formattedStartDate}&endDate=${formattedEndDate}`
                )
                .then(function (response) {
                    $scope.tongSanPhamTuyChinh = response.data;
                    console.log(response.data);
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi gọi API:", error);
                });
        }
    };

    //Sản phẩm của ngày tháng năm trước đó
    $scope.getTongSanPhamNgayTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-ngay/" +
                `${yesterdayFormatted}`
            )
            .then(function (response) {
                $scope.tongSanPhamNgayTruoc = response.data;
            });
    };
    $scope.getTongSanPhamNgayTruoc();

    $scope.getTongSanPhamThangTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-thang/" +
                `${lastMonthFormatted}`
            )
            .then(function (response) {
                $scope.tongSanPhamThangTruoc = response.data;
            });
    };
    $scope.getTongSanPhamThangTruoc();

    $scope.getTongSanPhamNamTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-nam/" +
                `${lastYearFormatted}`
            )
            .then(function (response) {
                $scope.tongSanPhamNamTruoc = response.data;
            });
    };
    $scope.getTongSanPhamNamTruoc();
    //=================================End Get All tổng Sản phẩm bán được=============================

    // ========================get All tổng Đơn hàng thành công=========================================
    $scope.tongDonHangNgay = 0;
    $scope.tongDonHangTuan = 0;
    $scope.tongDonHangThang = 0;
    $scope.tongDonHangNam = 0;
    $scope.tongDonHangTuyChinh = 0;

    $scope.getTongDonHangNgay = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-ngay/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangNgay = response.data;
            });
    };
    $scope.getTongDonHangNgay();

    $scope.getTongDonHangTuan = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tuan/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangTuan = response.data;
            });
    };
    $scope.getTongDonHangTuan();

    $scope.getTongDonHangThang = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-thang/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangThang = response.data;
            });
    };
    $scope.getTongDonHangThang();

    $scope.getTongDonHangNam = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-nam/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangNam = response.data;
            });
    };
    $scope.getTongDonHangNam();

    $scope.getTongDonHangTuyChinh = function () {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(
                new Date(startDate),
                "yyyy-MM-dd"
            );
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Gọi API với ngày bắt đầu và ngày kết thúc
            $http
                .get(
                    `http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tuy-chinh?startDate=${formattedStartDate}&endDate=${formattedEndDate}`
                )
                .then(function (response) {
                    $scope.tongDonHangTuyChinh = response.data;
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi gọi API:", error);
                });
        }
    };

    //Tỏng hóa đơn ngày tháng năm trước
    $scope.getTongDonHangNgayTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-ngay/" +
                `${yesterdayFormatted}`
            )
            .then(function (response) {
                $scope.tongDonHangNgayTruoc = response.data;
            });
    };
    $scope.getTongDonHangNgayTruoc();

    $scope.getTongDonHangThangTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-thang/" +
                `${lastMonthFormatted}`
            )
            .then(function (response) {
                $scope.tongDonHangThangTruoc = response.data;
            });
    };
    $scope.getTongDonHangThangTruoc();

    $scope.getTongDonHangNamTruoc = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-nam/" +
                `${lastYearFormatted}`
            )
            .then(function (response) {
                $scope.tongDonHangNamTruoc = response.data;
            });
    };
    $scope.getTongDonHangNamTruoc();

    //=================================End Get All Đơn hàng thành công=============================

    // ========================get All tổng Đơn hàng tra hang=========================================
    $scope.tongDonHangTraNgay = 0;
    $scope.tongDonHangTraTuan = 0;
    $scope.tongDonHangTraThang = 0;
    $scope.tongDonHangTraNam = 0;
    $scope.tongDonHangTraTuyChinh = 0;

    $scope.getTongDonHangTraNgay = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tra-hang-ngay/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangTraNgay = response.data;
            });
    };
    $scope.getTongDonHangTraNgay();

    $scope.getTongDonHangTraTuan = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tra-hang-tuan/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangTraTuan = response.data;
            });
    };
    $scope.getTongDonHangTraTuan();

    $scope.getTongDonHangTraThang = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tra-hang-thang/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangTraThang = response.data;
            });
    };
    $scope.getTongDonHangTraThang();

    $scope.getTongDonHangTraNam = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tra-hang-nam/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangTraNam = response.data;
            });
    };
    $scope.getTongDonHangTraNam();

    $scope.getTongDonHangTraTuyChinh = function () {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(
                new Date(startDate),
                "yyyy-MM-dd"
            );
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Gọi API với ngày bắt đầu và ngày kết thúc
            $http
                .get(
                    `http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tra-hang-tuy-chinh?startDate=${formattedStartDate}&endDate=${formattedEndDate}`
                )
                .then(function (response) {
                    $scope.tongDonHangTraTuyChinh = response.data;
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi gọi API:", error);
                });
        }
    };

    //=================================End Get All Đơn hàng tra hang=============================


    // =====================get All tổng Đơn hàng Huy============================================
    $scope.tongDonHangHuyNgay = 0;
    $scope.tongDonHangHuyTuan = 0;
    $scope.tongDonHangHuyThang = 0;
    $scope.tongDonHangHuyNam = 0;
    $scope.tongDonHangHuyTuyChinh = 0;

    $scope.getTongDonHangHuyNgay = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-ngay/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangHuyNgay = response.data;
            });
    };
    $scope.getTongDonHangHuyNgay();

    $scope.getTongDonHangHuyTuan = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-tuan/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangHuyTuan = response.data;
            });
    };
    $scope.getTongDonHangHuyTuan();

    $scope.getTongDonHangHuyThang = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-thang/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangHuyThang = response.data;
            });
    };
    $scope.getTongDonHangHuyThang();

    $scope.getTongDonHangHuyNam = function () {
        $http
            .get(
                "http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-nam/" +
                `${todayfomat}`
            )
            .then(function (response) {
                $scope.tongDonHangHuyNam = response.data;
            });
    };
    $scope.getTongDonHangHuyNam();

    $scope.getTongDonHangHuyTuyChinh = function () {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(
                new Date(startDate),
                "yyyy-MM-dd"
            );
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Gọi API với ngày bắt đầu và ngày kết thúc
            $http
                .get(
                    `http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-tuy-chinh?startDate=${formattedStartDate}&endDate=${formattedEndDate}`
                )
                .then(function (response) {
                    $scope.tongDonHangHuyTuyChinh = response.data;
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi gọi API:", error);
                });
        }
    };
    //=================================End Get All Đơn hàng Huy=============================

    //=================================Sản Phẩm bán chạy ========================================

    $scope.sanPhamBanChay = [];
    $scope.size2 = 5;
    $scope.totalPages = 0;
    $scope.currentPage2 = 0;
    $scope.desiredPage2 = 1;

    $scope.getSanPhamBanChayNgay = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size2
        };

        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-ngay", { params: params })
            .then(function (response) {
                $scope.sanPhamBanChay = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage2 = response.data.number;
                $scope.desiredPage2 = $scope.currentPage2 + 1;
            })
            .catch(function (error) {
                console.error("Error: ", error);
            });
    };

    $scope.getSanPhamBanChayTuan = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size2
        };

        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-tuan", { params: params })
            .then(function (response) {
                $scope.sanPhamBanChay = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage2 = response.data.number;
                $scope.desiredPage2 = $scope.currentPage2 + 1;
            })
            .catch(function (error) {
                console.error("Error: ", error);
            });
    };

    $scope.getSanPhamBanChayThang = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size2
        };

        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-thang", { params: params })
            .then(function (response) {
                $scope.sanPhamBanChay = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage2 = response.data.number;
                $scope.desiredPage2 = $scope.currentPage2 + 1;
            })
            .catch(function (error) {
                console.error("Error: ", error);
            });
    };

    $scope.getSanPhamBanChayNam = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size2
        };

        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-nam", { params: params })
            .then(function (response) {
                $scope.sanPhamBanChay = response.data.content;
                console.log(response.data.content);

                $scope.totalPages = response.data.totalPages;
                $scope.currentPage2 = response.data.number;
                $scope.desiredPage2 = $scope.currentPage2 + 1;
            })
            .catch(function (error) {
                console.error("Error: ", error);
            });
    };

    $scope.getSanPhamBanChayTuyChinh = function (pageNumber) {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(new Date(startDate), "yyyy-MM-dd");
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Gọi API với ngày bắt đầu, ngày kết thúc, trang hiện tại và kích thước trang
            let params = {
                startDate: formattedStartDate,
                endDate: formattedEndDate,
                page: pageNumber || 0,
                size: $scope.size2
            };

            $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-khoang-thoi-gian", { params: params })
                .then(function (response) {
                    $scope.sanPhamBanChay = response.data.content;
                    $scope.totalPages = response.data.totalPages;
                    $scope.currentPage2 = response.data.number;
                    $scope.desiredPage2 = $scope.currentPage2 + 1;
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi gọi API:", error);
                });
        }
    };


    $scope.applyFiltersSanPhamBanChay = function () {
        switch ($scope.selectedButton) {
            case 'ngay':
                $scope.getSanPhamBanChayNgay(0);
                break;
            case 'tuan':
                $scope.getSanPhamBanChayTuan(0);
                break;
            case 'thang':
                $scope.getSanPhamBanChayThang(0);
                break;
            case 'nam':
                $scope.getSanPhamBanChayNam(0);
                break;
            case 'tuyChinh':
                $scope.getSanPhamBanChayTuyChinh(0);
                break;
        }
    };

    $scope.goToPageSanPhamBanChay = function () {
        let pageNumber = $scope.desiredPage2 - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            switch ($scope.selectedButton) {
                case 'ngay':
                    $scope.getSanPhamBanChayNgay(pageNumber);
                    break;
                case 'tuan':
                    $scope.getSanPhamBanChayTuan(pageNumber);
                    break;
                case 'thang':
                    $scope.getSanPhamBanChayThang(pageNumber);
                    break;
                case 'nam':
                    $scope.getSanPhamBanChayNam(pageNumber);
                    break;
                case 'tuyChinh':
                    $scope.getSanPhamBanChayTuyChinh(pageNumber);
                    break;
            }
        } else {
            $scope.desiredPage2 = $scope.currentPage2 + 1;
        }
    };

    $scope.toggleDiv = function () {
        $scope.selectedButton = "tuyChinh";
        $scope.showDiv = !$scope.showDiv;
    };

    // Initialize with daily data
    // $scope.getSanPhamBanChayNgay(0);

    // ================================End Sản Phẩm bán chạy ========================================

    //=================================Khachs hàng mua nhiều nhất================================
    $scope.khachHangMuaNhieuNhat = [];

    $scope.size3 = 5;
    $scope.totalPages = 0;
    $scope.currentPage3 = 0;
    $scope.desiredPage3 = 1;

    $scope.getKhachHangMuaNhieuNhatNgay = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size3
        };

        console.log("Request params: ", params); // Log để kiểm tra tham số

        $http.get("http://localhost:8080/api/admin/bill-tinh/khach-hang-mua-nhieu-nhat-ngay", { params: params })
            .then(function (response) {
                $scope.khachHangMuaNhieuNhat = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage3 = response.data.number;
                $scope.desiredPage3 = $scope.currentPage3 + 1;
            }).catch(function (error) {
                console.error("Error: ", error); // Log lỗi để kiểm tra
            });
    };

    $scope.getKhachHangMuaNhieuNhatTuan = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size3
        };

        console.log("Request params: ", params); // Log để kiểm tra tham số

        $http.get("http://localhost:8080/api/admin/bill-tinh/khach-hang-mua-nhieu-nhat-tuan", { params: params })
            .then(function (response) {
                $scope.khachHangMuaNhieuNhat = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage3 = response.data.number;
                $scope.desiredPage3 = $scope.currentPage3 + 1;
            }).catch(function (error) {
                console.error("Error: ", error); // Log lỗi để kiểm tra
            });
    };

    $scope.getKhachHangMuaNhieuNhatThang = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size3
        };

        console.log("Request params: ", params); // Log để kiểm tra tham số

        $http.get("http://localhost:8080/api/admin/bill-tinh/khach-hang-mua-nhieu-nhat-thang", { params: params })
            .then(function (response) {
                $scope.khachHangMuaNhieuNhat = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage3 = response.data.number;
                $scope.desiredPage3 = $scope.currentPage3 + 1;
            }).catch(function (error) {
                console.error("Error: ", error); // Log lỗi để kiểm tra
            });
    };

    $scope.getKhachHangMuaNhieuNhatNam = function (pageNumber) {
        let params = {
            date: todayfomat,
            page: pageNumber || 0,
            size: $scope.size3
        };

        console.log("Request params: ", params); // Log để kiểm tra tham số

        $http.get("http://localhost:8080/api/admin/bill-tinh/khach-hang-mua-nhieu-nhat-nam", { params: params })
            .then(function (response) {
                $scope.khachHangMuaNhieuNhat = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage3 = response.data.number;
                $scope.desiredPage3 = $scope.currentPage3 + 1;
            }).catch(function (error) {
                console.error("Error: ", error); // Log lỗi để kiểm tra
            });
    };

    $scope.getKhachHangMuaNhieuNhatTuyChinh = function (pageNumber) {
        // Lấy giá trị ngày từ ng-model
        const startDate = $scope.filterStartDate;
        const endDate = $scope.filterEndDate;

        if (startDate && endDate) {
            // Định dạng ngày theo kiểu 'yyyy-MM-dd'
            const formattedStartDate = $filter("date")(new Date(startDate), "yyyy-MM-dd");
            const formattedEndDate = $filter("date")(new Date(endDate), "yyyy-MM-dd");

            // Gọi API với ngày bắt đầu, ngày kết thúc, trang và kích thước trang
            $http
                .get(
                    `http://localhost:8080/api/admin/bill-tinh/khach-hang-mua-nhieu-nhat-tuy-chinh?startDate=${formattedStartDate}&endDate=${formattedEndDate}&page=${pageNumber || 0}&size=${$scope.size}`
                )
                .then(function (response) {
                    $scope.khachHangMuaNhieuNhat = response.data.content;
                    $scope.totalPages = response.data.totalPages;
                    $scope.currentPage3 = response.data.number;
                    $scope.desiredPage3 = $scope.currentPage3 + 1;
                })
                .catch(function (error) {
                    console.error("Có lỗi xảy ra khi gọi API:", error);
                });
        }
    };

    $scope.applyFiltersKhachHangMuaNhieu = function () {
        switch ($scope.selectedButton) {
            case 'ngay':
                $scope.getKhachHangMuaNhieuNhatNgay(0);
                break;
            case 'tuan':
                $scope.getKhachHangMuaNhieuNhatTuan(0);
                break;
            case 'thang':
                $scope.getKhachHangMuaNhieuNhatThang(0);
                break;
            case 'nam':
                $scope.getKhachHangMuaNhieuNhatNam(0);
                break;
            case 'tuyChinh':
                $scope.getKhachHangMuaNhieuNhatTuyChinh(0);
                break;
        }
    };

    $scope.goToPageKhachHangMuaNhieu = function () {
        let pageNumber = $scope.desiredPage3 - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            switch ($scope.selectedButton) {
                case 'ngay':
                    $scope.getKhachHangMuaNhieuNhatNgay(pageNumber);
                    break;
                case 'tuan':
                    $scope.getKhachHangMuaNhieuNhatTuan(pageNumber);
                    break;
                case 'thang':
                    $scope.getKhachHangMuaNhieuNhatThang(pageNumber);
                    break;
                case 'nam':
                    $scope.getKhachHangMuaNhieuNhatNam(pageNumber);
                    break;
                case 'tuyChinh':
                    $scope.getKhachHangMuaNhieuNhatTuyChinh(pageNumber);
                    break;
            }
        } else {
            $scope.desiredPage3 = $scope.currentPage3 + 1;
        }
    };

    //=================================END Khachs hàng mua nhiều nhất================================

    // Hàm kiểm tra ngày và gọi getTongDoanhThuTuyChinh
    $scope.checkDates = function () {
        const startDate = new Date($scope.filterStartDate);
        const endDate = new Date($scope.filterEndDate);

        if (startDate && endDate) {
            if (startDate > endDate) {
                $scope.showErrorNotification("Ngày kết thúc phải lớn hơn ngày bắt đầu");
                $scope.filterEndDate = null; // Xóa ngày kết thúc nếu không hợp lệ
            } else {
                // Nếu ngày hợp lệ, gọi hàm để tìm kiếm
                $scope.getTongDoanhThuTuyChinh();
                $scope.getTongDonHangTuyChinh();
                $scope.getTongDonHangTraTuyChinh();
                $scope.getTongDonHangHuyTuyChinh();
                $scope.getTongSanPhamTuyChinh();
                $scope.getSanPhamBanChayTuyChinh();
                $scope.getTongBillStatusTuyChinh();
                $scope.getKhachHangMuaNhieuNhatTuyChinh();
                // $scope.getTongBillStatus = function (url) {
                //     return $http.get(url); // $http.get tự động trả về promise
                // };
            }
        }
    };

    //Hàm submit bộ lọc
    $scope.hihi = "";

    $scope.submitNgay = function () {
        $scope.selectedButton = "ngay";
        $scope.getSanPhamBanChayNgay();
        $scope.getTongBillStatusNgay();
        $scope.getKhachHangMuaNhieuNhatNgay(0);
        $scope.hihi = "Theo Ngày";
    };

    $scope.submitTuan = function () {
        $scope.selectedButton = "tuan";
        $scope.getSanPhamBanChayTuan();
        $scope.getTongBillStatusTuan();
        $scope.getKhachHangMuaNhieuNhatTuan(0);
        $scope.hihi = "Theo Tuần";
    };

    $scope.submitThang = function () {
        $scope.selectedButton = "thang";
        $scope.getSanPhamBanChayThang();
        $scope.getTongBillStatusThang();
        $scope.getKhachHangMuaNhieuNhatThang();
        $scope.hihi = "Theo Tháng";
    };

    $scope.submitNam = function () {
        $scope.selectedButton = "nam";
        $scope.getSanPhamBanChayNam();
        $scope.getTongBillStatusNam();
        $scope.getKhachHangMuaNhieuNhatNam();
        $scope.hihi = "Theo Năm";
    };
    $scope.submitNam();

    //List sản phẩm
    // $scope.filterProductDetall = function () {
    //     $http.get(apiProduct).then(function (response) {
    //         $scope.product = response.data;
    //     });
    // };
    // $scope.validateQuantity(1);


    //===================================Sản phẩm gần hết=============================================================
    $scope.filterProductDetall = [];
    $scope.totalPages = 0;
    $scope.currentPage1 = 0;
    $scope.desiredPage1 = 1;
    $scope.size1 = 5;
    $scope.filters = {
        name: null,
        price: null,
        totalQuantity: 100, // Thêm bộ lọc totalQuantity
    };

    $scope.getProductDetall = function (pageNumber) {
        let params = angular.extend(
            { pageNumber: pageNumber, size: $scope.size1 },
            $scope.filters
        );
        $http
            .get("http://localhost:8080/api/admin/product-tinh/page-product", {
                params: params,
            })
            .then(function (response) {
                console.log("API response:", response.data); // Ghi lại phản hồi của API
                if (response.data && response.data.content) {
                    $scope.filterProductDetall = response.data.content;

                    $scope.filterProductDetall.forEach(function (product) {
                        product.inputQuantity = 1;
                        product.remainingQuantity = product.quantity || 0;
                        // $scope.initializeEmployee(product);
                    });
                    $scope.totalPages = response.data.totalPages || 0;
                    $scope.currentPage1 = pageNumber;
                    $scope.desiredPage1 = pageNumber + 1;
                } else {
                    console.error("Invalid API response structure");
                }
            })
            .catch(function (error) {
                console.error("Error fetching product details:", error);
            });
    };

    $scope.applyFiltersSanPhamSapHet = function () {
        $scope.getProductDetall(0);
    };

    $scope.goToPageSanPhamSapHet = function () {
        let pageNumber = $scope.desiredPage1 - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.getProductDetall(pageNumber);
        } else {
            $scope.desiredPage1 = $scope.currentPage1 + 1;
        }
    };

    $scope.getProductDetall(0);

    $scope.getTotalQuantity = function (product) {
        return product.totalQuantity;
    };

    $scope.getSizeNames = function (details) {
        if (!details) {
            return ""; // Hoặc bất kỳ giá trị mặc định nào bạn muốn khi details là null hoặc undefined
        }
        return details.map((detail) => detail.size.name).join(", ");
    };

});
