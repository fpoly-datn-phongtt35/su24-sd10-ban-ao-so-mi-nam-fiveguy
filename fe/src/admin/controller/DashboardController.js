app.controller("DashboardController", function ($scope, $http, $filter) {
    const ctx = document.getElementById('myChart');
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: [
                'Red',
                'Blue',
                'Yellow'
            ],
            datasets: [{
                label: 'My First Dataset',
                data: [300, 50, 100],
                backgroundColor: [
                    'rgb(255, 99, 132)',
                    'rgb(54, 162, 235)',
                    'rgb(255, 205, 86)'
                ],
                hoverOffset: 4
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    $scope.showDiv = false;

    $scope.toggleDiv = function () {
        $scope.showDiv = !$scope.showDiv;
    };

    //Validate ngày hiện tại
    var today = new Date();
    today.setDate(today.getDate());
    let todayfomat = $filter("date")(today, "yyyy-MM-dd");
    console.log(todayfomat);

    // ===========================get All tổng doang thu======================================
    $scope.tongDoanhThuNgay = 0;
    $scope.tongDoanhThuTuan = 0;
    $scope.tongDoanhThuThang = 0;
    $scope.tongDoanhThuNam = 0;

    $scope.getTongDoangThuNgay = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-ngay/" + `${todayfomat}`).then(function (response) {
            $scope.tongDoanhThuNgay = response.data;
        })
    }
    $scope.getTongDoangThuNgay();

    $scope.getTongDoangThuTuan = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-tuan/" + `${todayfomat}`).then(function (response) {
            $scope.tongDoanhThuTuan = response.data;
        })
    }
    $scope.getTongDoangThuTuan();

    $scope.getTongDoangThuThang = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-thang/" + `${todayfomat}`).then(function (response) {
            $scope.tongDoanhThuThang = response.data;
        })
    }
    $scope.getTongDoangThuThang();

    $scope.getTongDoangThuNam = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-doanh-thu-nam/" + `${todayfomat}`).then(function (response) {
            $scope.tongDoanhThuNam = response.data;
        })
    }
    $scope.getTongDoangThuNam();

    //=================================End Get All tổng doanh thu=============================

    // =========================get All tổng Sanr phẩm bán được========================================
    $scope.tongSanPhamNgay = 0;
    $scope.tongSanPhamTuan = 0;
    $scope.tongSanPhamThang = 0;
    $scope.tongSanPhamNam = 0;

    $scope.getTongSanPhamNgay = function () {
        $http.get("http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-ngay/" + `${todayfomat}`).then(function (response) {
            $scope.tongSanPhamNgay = response.data;
        })
    }
    $scope.getTongSanPhamNgay();

    $scope.getTongSanPhamTuan = function () {
        $http.get("http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-tuan/" + `${todayfomat}`).then(function (response) {
            $scope.tongSanPhamTuan = response.data;
        })
    }
    $scope.getTongSanPhamTuan();

    $scope.getTongSanPhamThang = function () {
        $http.get("http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-thang/" + `${todayfomat}`).then(function (response) {
            $scope.tongSanPhamThang = response.data;
        })
    }
    $scope.getTongSanPhamThang();

    $scope.getTongSanPhamNam = function () {
        $http.get("http://localhost:8080/api/admin/billdetail-tinh/san-pham-ban-ra-nam/" + `${todayfomat}`).then(function (response) {
            $scope.tongSanPhamNam = response.data;
        })
    }
    $scope.getTongSanPhamNam();
    //=================================End Get All tổng Sản phẩm bán được=============================

    // ========================get All tổng Đơn hàng thành công=========================================
    $scope.tongDonHangNgay = 0;
    $scope.tongDonHangTuan = 0;
    $scope.tongDonHangThang = 0;
    $scope.tongDonHangNam = 0;

    $scope.getTongDonHangNgay = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-ngay/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangNgay = response.data;
        })
    }
    $scope.getTongDonHangNgay();

    $scope.getTongDonHangTuan = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-tuan/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangTuan = response.data;
        })
    }
    $scope.getTongDonHangTuan();

    $scope.getTongDonHangThang = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-thang/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangThang = response.data;
        })
    }
    $scope.getTongDonHangThang();

    $scope.getTongDonHangNam = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-nam/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangNam = response.data;
        })
    }
    $scope.getTongDonHangNam();

    //=================================End Get All Đơn hàng thành công=============================

    // =====================get All tổng Đơn hàng Huy============================================
    $scope.tongDonHangHuyNgay = 0;
    $scope.tongDonHangHuyTuan = 0;
    $scope.tongDonHangHuyThang = 0;
    $scope.tongDonHangHuyNam = 0;

    $scope.getTongDonHangHuyNgay = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-ngay/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangHuyNgay = response.data;
        })
    }
    $scope.getTongDonHangHuyNgay();

    $scope.getTongDonHangHuyTuan = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-tuan/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangHuyTuan = response.data;
        })
    }
    $scope.getTongDonHangHuyTuan();

    $scope.getTongDonHangHuyThang = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-thang/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangHuyThang = response.data;
        })
    }
    $scope.getTongDonHangHuyThang();

    $scope.getTongDonHangHuyNam = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/tong-hoa-don-huy-nam/" + `${todayfomat}`).then(function (response) {
            $scope.tongDonHangHuyNam = response.data;
        })
    }
    $scope.getTongDonHangHuyNam();

    //=================================End Get All Đơn hàng Huy=============================

    //=================================Sản Phẩm bán chạy ========================================
    // $scope.sanPhamBanChayNgay = [];
    // $scope.sanPhamBanChayTuan = [];
    // $scope.sanPhamBanChayThang = [];
    // $scope.sanPhamBanChayNam = [];

    // $scope.getSanPhamBanChayNgay = function () {
    //     $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-ngay/" + `${todayfomat}`).then(function (response) {
    //         $scope.sanPhamBanChayNgay = response.data;
    //     })
    // }
    // $scope.getSanPhamBanChayNgay();

    // $scope.getSanPhamBanChayTuan = function () {
    //     $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-tuan/" + `${todayfomat}`).then(function (response) {
    //         $scope.sanPhamBanChayTuan = response.data;
    //     })
    // }
    // $scope.getSanPhamBanChayTuan();

    // $scope.getSanPhamBanChayThang = function () {
    //     $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-thang/" + `${todayfomat}`).then(function (response) {
    //         $scope.sanPhamBanChayThang = response.data;
    //     })
    // }
    // $scope.getSanPhamBanChayThang();

    // $scope.getSanPhamBanChayNam = function () {
    //     $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-nam/" + `${todayfomat}`).then(function (response) {
    //         $scope.sanPhamBanChayNam = response.data;
    //     })
    // }
    // $scope.getSanPhamBanChayNam();


    $scope.sanPhamBanChay = [];

    $scope.getSanPhamBanChayNgay = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-ngay/" + `${todayfomat}`).then(function (response) {
            $scope.sanPhamBanChay = response.data;
        });
    };

    $scope.getSanPhamBanChayTuan = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-tuan/" + `${todayfomat}`).then(function (response) {
            $scope.sanPhamBanChay = response.data;
        });
    };

    $scope.getSanPhamBanChayThang = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-thang/" + `${todayfomat}`).then(function (response) {
            $scope.sanPhamBanChay = response.data;
        });
    };

    $scope.getSanPhamBanChayNam = function () {
        $http.get("http://localhost:8080/api/admin/bill-tinh/top-ban-chay-nam/" + `${todayfomat}`).then(function (response) {
            $scope.sanPhamBanChay = response.data;
        });
    };

    $scope.toggleDiv = function () {
        $scope.showDiv = !$scope.showDiv;
    };

    // Initialize with daily data
    $scope.getSanPhamBanChayNgay();

    // ================================End Sản Phẩm bán chạy ========================================
})