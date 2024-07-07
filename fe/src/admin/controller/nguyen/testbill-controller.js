app.controller('test-bill-detail-ctrl', function ($scope, $http, $timeout, $rootScope, $routeParams) {

    const apiBill = "http://localhost:8080/api/admin/bill";
    const apiBillDetail = "http://localhost:8080/api/admin/billDetail";
    const apiBillHistory = "http://localhost:8080/api/admin/billHistory"

    //idBill from the route
    $scope.idBill = $routeParams.idBill;
    console.log("Bill ID:", $scope.idBill);

    //data
    $scope.billResponse = {}

    //history
    $scope.billHistoryUpdate = {}
    $scope.listBillHistory = []

    $scope.status = null

    //LẤY THÔNG TIN BILL ***************
    $scope.getBillById = function (id) {
        $http.get(apiBill + "/" + id).then(function (res) {
            console.log(res.data)
            $scope.billResponse = res.data
            $scope.status = res.data.status

            // updateBill sử dụng để cập nhật thông tin giao hàng - sử dụng angular.copy để tránh nó binding
            $scope.updateBill = angular.copy($scope.billResponse)
        })
    }
    $scope.getBillById($scope.idBill)

    // XỬ LÝ STATUS BILL, HISTORY BILL, THÔNG TIN GIAO HÀNG, LOGIC CỦA HIỂN THỊ TRẠNG THÁI ĐƠN HÀNG, paymentStatus
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
            // Cập nhật lại danh sách lịch sử
            $scope.getBillById($scope.idBill);
            $scope.getBillHistoryByBillId();
        }).catch(function (error) {
        });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
    };

    $scope.getBillHistoryByBillId = function () {
        $http.get(apiBillHistory + "/bill/" + $scope.idBill).then(function (res) {
            $scope.listBillHistory = res.data;
            $scope.updateStatusSteps();
            $timeout(function () {
                $scope.scrollToEnd()
                $scope.checkScrollbarVisibility();
            },0);
        });
    };

    $scope.updateStatusSteps = function () {
        const possibleSteps = {
            1: { title: "Chờ xác nhận", icon: "schedule", status: 1 },
            2: { title: "Chờ vận chuyển", icon: "local_shipping", status: 2 },
            3: { title: "Đang giao", icon: "directions_car", status: 3 },
            4: { title: "Thành công", icon: "home", status: 4 },
            5: { title: "Khách hủy", icon: "cancel", status: 5 },
            6: { title: "Đã hủy", icon: "cancel", status: 6 },
            7: { title: "Thất bại", icon: "cancel", status: 7 },
            8: { title: "Thất bại", icon: "cancel", status: 8 },
            9: { title: "Chờ giao lại", icon: "local_shipping", status: 9 },
            10: { title: "Đang giao lại", icon: "directions_car", status: 10 },
            11: { title: "Hoàn hàng", icon: "warehouse", status: 11 }
        };

        $scope.deliveryFlow = {
            1: [2, 5, 6], // Chờ xác nhận -> Chờ vận chuyển hoặc khách hủy hoặc đã hủy
            2: [3, 5, 6], // Chờ vận chuyển -> Đang giao hoặc khách hủy hoặc đã hủy
            3: [4, 7], // Đang giao -> Thành công hoặc Thất bại
            4: [], // Thành công (kết thúc)
            5: [], // Khách hủy (kết thúc)
            6: [], // Đã hủy (kết thúc)
            7: [9, 11], // Thất bại -> Chờ giao lại hoặc Hoàn hàng
            8: [11], // Thất bại -> Chờ giao lại hoặc Hoàn hàng
            9: [10], // Chờ giao lại -> Đang giao lại
            10: [4, 8], // Đang giao lại -> Thành công hoặc Thất bại (lai)
            11: [6], // Hoàn hàng -> Đã hủy
        };

        $scope.steps = [];

        $scope.listBillHistory.forEach(function (history) {
            let step = angular.copy(possibleSteps[history.status]);
            if (step) {
                step.time = history.createdAt;
                $scope.steps.push(step);
            }
        });

        console.log($scope.steps);

        // $scope.status = Math.max.apply(Math, $scope.listBillHistory.map(function (o) { return o.status; }));
        if ($scope.listBillHistory.length > 0) {
            $scope.status = $scope.listBillHistory[$scope.listBillHistory.length - 1].status;
        } else {
            $scope.status = null; // Hoặc một giá trị mặc định nếu không có lịch sử trạng thái
        }
    };

    $scope.scrollToEnd = function () {
        var container = document.querySelector('.progress-container');
        container.scrollLeft = container.scrollWidth;
    };

    $scope.scrollLeft = function () {
        var container = document.querySelector('.progress-container');
        container.scrollBy({
            top: 0,
            left: -200,
            behavior: 'smooth'
        });
    };

    $scope.scrollRight = function () {
        var container = document.querySelector('.progress-container');
        container.scrollBy({
            top: 0,
            left: 200,
            behavior: 'smooth'
        });
    };

    // Check if scrollbar is visible
    $scope.checkScrollbarVisibility = function () {
        var container = document.querySelector('.progress-container');
        $scope.scrollbarVisible = container.scrollWidth > container.clientWidth;
    };
});