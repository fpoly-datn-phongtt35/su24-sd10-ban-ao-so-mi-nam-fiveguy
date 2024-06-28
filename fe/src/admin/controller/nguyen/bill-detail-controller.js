app.controller('nguyen-bill-detail-ctrl', function ($scope, $http, $rootScope, $routeParams) {

    const apiBill = "http://localhost:8080/api/admin/bill";
    const apiBillDetail = "";
    const apiBillHistory = "http://localhost:8080/api/admin/billHistory"

    const apiProductDetail = "http://localhost:8080/api/admin/productDetail"
    const apiProductProperty = "http://localhost:8080/api/admin/productProperty"

    // Hàm hiển thị thông báo thành công
    $scope.showSuccess = function (message) {
        toastr["success"](message);
    };
    // Hàm hiển thị thông báo lỗi
    $scope.showError = function (message) {
        toastr["error"](message);
    };
    $scope.showWarning = function (message) {
        toastr["warning"](message);
    };

    //idBill from the route
    $scope.idBill = $routeParams.idBill;
    console.log("Bill ID:", $scope.idBill);

    //data
    $scope.billResponse = {}

    //thông tin giao hàng
    $scope.updateBill = {}

    //history
    $scope.billHistoryUpdate = {}
    $scope.listBillHistory = []

    $scope.status = null

    //product
    $scope.productDetail = {}
    $scope.productDetails = []

    //product property for filter
    $scope.categories = []
    $scope.materials = []
    $scope.collars = []
    $scope.wrists = []
    $scope.colors = []
    $scope.sizes = []

    //LẤY THÔNG TIN BILL ***************
    $scope.getBillById = function (id) {
        $http.get(apiBill + "/" + id).then(function (res) {
            console.log(res.data)
            $scope.billResponse = res.data
            $scope.status = res.data.status

            //updateBill sử dụng để cập nhật thông tin giao hàng - sử dụng angularcopy để tránh nó binding
            $scope.updateBill = angular.copy($scope.billResponse)
        })
    }
    $scope.getBillById($scope.idBill)

    //XỬ LÝ STATUS BILL, HISTORY BILL, THÔNG TIN GIAO HÀNG, LOGIC CỦA HIỂN THỊ TRẠNG THÁI ĐƠN HÀNG
    // #region MyRegion
    $scope.showModalStatus = function (status) {
        $('#changeStatusModal').modal('show');
        $scope.currentStatus = status;
    };

    $scope.confirmChangeStatus = function () {
        $('#changeStatusModal').modal('hide');

        let bill = angular.copy($scope.billResponse)
        bill.status = $scope.currentStatus

        let billHistory = {
            billId: $scope.idBill,
            status: $scope.currentStatus,
            description: $scope.billHistoryUpdate.description,
        };

        let data = { bill: bill, billHistory: billHistory };

        $http.put(apiBill + "/billStatusUpdate/" + $scope.idBill, data).then(function (response) {
            $scope.showSuccess("Chuyển trạng thái thành công");
            // Cập nhật lại danh sách lịch sử
            $scope.getBillHistoryByBillId();
        }).catch(function (error) {
            $scope.showError("Chuyển trạng thái thất bại");
        });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
    };

    $scope.getBillHistoryByBillId = function () {
        $http.get(apiBillHistory + "/bill/" + $scope.idBill).then(function (res) {
            $scope.listBillHistory = res.data;
            $scope.updateStatusSteps();
        });
    };

    $scope.updateStatusSteps = function () {
        $scope.steps = [
            { status: 1, title: "Chờ xác nhận", icon: "schedule", time: null },
            { status: 2, title: "Đã xác nhận", icon: "check_circle", time: null },
            { status: 3, title: "Chờ vận chuyển", icon: "local_shipping", time: null },
            { status: 4, title: "Đang giao", icon: "directions_car", time: null },
            { status: 5, title: "Thành công", icon: "home", time: null },
            { status: 6, title: "Đã hủy", icon: "cancel", time: null }
        ];

        $scope.listBillHistory.forEach(function (history) {
            let step = $scope.steps.find(s => s.status === history.status);
            if (step) {
                step.time = history.createdAt;
            }
        });

        // Cập nhật lại trạng thái hiện tại
        $scope.status = Math.max.apply(Math, $scope.listBillHistory.map(function (o) { return o.status; }));
    };

    // Update thông tin giao hàng
    $scope.updateShipmentDetail = function () {
        let data = angular.copy($scope.updateBill)
        $http.put(apiBill + "/shipUpdate/" + $scope.idBill, data).then(function (res) {
            $scope.getBillById($scope.idBill)
            $('#editShipmentDetail').modal('hide');
            $scope.showSuccess("Cập nhật thông tin giao hàng thành công")

            $scope.getBillHistoryByBillId()
        })
    }
    // #endregion

    //Lấy thông tin product property để filter
    $scope.getAllProductProperty = function () {
        $http.get(apiProductProperty + "/all").then(function (res) {
            console.log(res.data)
        })
    }
    $scope.getAllProductProperty()

    //FILTER, PAGINATION PRODUCT DETAIL
    // $scope.productDetails = [];
    $scope.currentPage = 0;
    $scope.pageSize = 5;
    $scope.totalPages = 0;
    $scope.filters = {
        productName: '',
        categoryId: null,
        materialId: null,
        wristId: null,
        collarId: null,
        sizeId: null,
        minPrice: null,
        maxPrice: null,
        colorId: null
    };

    $scope.getProductDetails = function (pageNumber) {
        let params = {
            productName: $scope.filters.productName,
            categoryId: $scope.filters.categoryId,
            materialId: $scope.filters.materialId,
            wristId: $scope.filters.typeBill,
            collarId: $scope.filters.collarId,
            sizeId: $scope.filters.sizeId,
            colorId: $scope.filters.colorId,
            minPrice: $scope.filters.minPrice,
            maxPrice: $scope.filters.maxPrice,
            page: pageNumber,
            size: $scope.pageSize
        };
        $http.get(apiProductDetail + "/page", { params: params })
            .then(function (response) {
                $scope.productDetails = response.data.content;
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = pageNumber;
                $scope.pages = Array.from(Array($scope.totalPages).keys());
            }, function (error) {
                console.error('Error fetching products:', error);
            });
    };

    // Initial load
    $scope.getProductDetails(0);

    $scope.applyFilters = function () {
        $scope.getProductDetails(0);
    };

    $scope.loadPage = function (page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getProductDetails(page);
        }
    };

    $scope.resetFilters = function () {
        $scope.filters = {
            productName: '',
            categoryId: null,
            materialId: null,
            wristId: null,
            collarId: null,
            sizeId: null,
            minPrice: null,
            maxPrice: null,
            colorId: null
        };
        $scope.getProductDetails(0);
    };

    //Range lọc giá
    $scope.initSlider = function () {
        var slider = document.getElementById('slider');
        noUiSlider.create(slider, {
            start: [0, 1000],
            connect: true,
            range: {
                'min': 0,
                'max': 1000
            }
        });

        // Example of updating AngularJS model on slider change
        slider.noUiSlider.on('update', function (values, handle) {
            $scope.filters.minPrice = parseInt(values[0]);
            $scope.filters.maxPrice = parseInt(values[1]);
            $scope.$applyAsync(); // Apply changes to AngularJS model
        });
    };

    // Call the slider initialization function
    $scope.initSlider();

});