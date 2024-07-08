app.controller('nguyen-bill-ctrl', function ($scope, $http) {

    const apiBill = "http://localhost:8080/api/admin/bill";

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

    $scope.tabs = [
        { label: 'Tất cả', status: null, count: 0 },
        { label: 'Chờ xác nhận', status: 1, count: 0 },
        { label: 'Đã xác nhận', status: 2, count: 0 },
        { label: 'Chờ giao hàng', status: 3, count: 0 },
        { label: 'Đang giao hàng', status: 4, count: 0 },
        { label: 'Hoàn thành', status: 5, count: 0 },
        { label: 'Đã hủy', status: 6, count: 0 }
    ];

    $scope.bills = [];
    $scope.currentTab = null;
    $scope.currentPage = 0;
    $scope.pageSize = 5;
    $scope.totalPages = 0;
    $scope.desiredPage = 1;
    $scope.filters = {};

    $scope.loadBills = function (status, page) {
        let params = {
            status: status,
            code: $scope.filters.code,
            customerName: $scope.filters.customerName,
            phoneNumber: $scope.filters.phoneNumber,
            typeBill: $scope.filters.typeBill,
            startDate: $scope.filters.startDate,
            endDate: $scope.filters.endDate,
            page: page,
            size: $scope.pageSize
        };
        $http.get(apiBill + "/page", { params: params }).then(function (response) {
            $scope.bills = response.data.content;
            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = page;
            $scope.pages = Array.from(Array($scope.totalPages).keys());
            $scope.desiredPage = page + 1;
        });
    };

    $scope.setTab = function (status) {
        $scope.currentTab = status;
        $scope.loadBills(status, 0);
    };

    $scope.loadPage = function (page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.loadBills($scope.currentTab, page);
        }
    };

    $scope.countBillsByStatus = function () {
        $scope.tabs.forEach(function (tab) {
            let params = {
                status: tab.status,
                code: $scope.filters.code,
                customerName: $scope.filters.customerName,
                phoneNumber: $scope.filters.phoneNumber,
                typeBill: $scope.filters.typeBill,
                startDate: $scope.filters.startDate,
                endDate: $scope.filters.endDate,
                page: 0,
                size: 1
            };
            $http.get(apiBill + "/page", { params: params }).then(function (response) {
                tab.count = response.data.totalElements;
            });
        });
    };

    $scope.applyFilters = function () {
        $scope.countBillsByStatus();
        $scope.setTab($scope.currentTab);
    };

    $scope.resetFilters = function () {
        $scope.filters = {};
        $scope.applyFilters();
    };

    $scope.goToPage = function () {
        let pageNumber = $scope.desiredPage - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.loadBills($scope.currentTab, pageNumber);
        } else {
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };

    //change bill status
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

        // $http.put(apiBill + "/billStatusUpdate/" + $scope.idBill, data).then(function (response) {
        //     $scope.showSuccess("Chuyển trạng thái thành công");

        //     $scope.applyFilters();
        // }).catch(function (error) {
        //     $scope.showError("Chuyển trạng thái thất bại");
        // });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
    };

    // Initialize
    $scope.countBillsByStatus();
    $scope.setTab(null);
});