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
        // { label: 'Chờ xác nhận', status: 1, count: 0 },
        // { label: 'Đã xác nhận', status: 2, count: 0 },
        // { label: 'Chờ giao hàng', status: 3, count: 0 },
        // { label: 'Đang giao hàng', status: 4, count: 0 },
        // { label: 'Hoàn thành', status: 5, count: 0 },
        // { label: 'Đã hủy', status: 6, count: 0 }
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

    $scope.status = {
        1: { title: "Chờ xác nhận", icon: "schedule", status: 1, color: "#007bff" },
        2: { title: "Chờ vận chuyển", icon: "local_shipping", status: 2, color: "#ffc107" },
        3: { title: "Đang giao", icon: "directions_car", status: 3, color: "#17a2b8" },
        4: { title: "Thành công", icon: "check_circle", status: 4, color: "#4dd100" },
        5: { title: "Đã hủy", icon: "person_cancel", status: 5, color: "#dc3545" },
        6: { title: "Đã hủy", icon: "block", status: 6, color: "#dc3545" },
        7: { title: "Thất bại", icon: "cancel", status: 7, color: "#828282" },
        8: { title: "Thất bại", icon: "cancel", status: 8, color: "#828282" },
        81: { title: "Thất bại", icon: "cancel", status: 81, color: "#828282" },
        9: { title: "Chờ giao lại", icon: "autorenew", status: 9, color: "#ffc107" },
        10: { title: "Đang giao lại", icon: "directions_car", status: 10, color: "#17a2b8" },
        11: { title: "Hoàn hàng", icon: "warehouse", status: 11, color: "#ed79ff" }
    };

    $scope.status = {
        20: { title: "Tạo đơn hàng", icon: "post_add", status: 20 },
        1: { title: "Chờ xác nhận", icon: "hourglass_empty", status: 1 },
        2: { title: "Chờ giao hàng", icon: "inventory", status: 2 },
        3: { title: "Đang giao hàng", icon: "local_shipping", status: 3 },
        4: { title: "Đã giao hàng", icon: "check_circle", status: 4 },

        5: { title: "Khách hủy", icon: "cancel", status: 5 },
        6: { title: "Đã hủy", icon: "not_interested", status: 6 },

        7: { title: "Thất bại", icon: "error", status: 7 },
        8: { title: "Thất bại", icon: "error", status: 8 }, //Lại - thiếu
        81: { title: "Thất bại", icon: "error", status: 81 }, //Mất hàng

        9: { title: "Chờ giao lại", icon: "replay", status: 9 },
        10: { title: "Đang giao lại", icon: "local_shipping", status: 10 },

        11: { title: "Đang hoàn hàng", icon: "assignment_return", status: 11 },
        12: { title: "Đã hoàn hàng", icon: "assignment_turned_in", status: 12 },
        13: { title: "Hoàn hàng thất bại", icon: "assignment_return", status: 13 },

        20: { title: "Thành công", icon: "task_alt", status: 20 },
        21: { title: "Hoàn thành", icon: "task_alt", status: 21 },

        30: { title: "Trả hàng", icon: "store", status: 30 },  //tại quầy
        31: { title: "Trả hàng", icon: "local_shipping", status: 31 },  //tận nơi
        32: { title: "Đã trả hàng", icon: "inventory_2", status: 32 },
        33: { title: "Trả hàng thất bại", icon: "assignment_late", status: 33 },
    };
});