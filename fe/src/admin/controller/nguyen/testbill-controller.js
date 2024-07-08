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
   
    // $scope.showModalStatus = function (updateStatus) {
    //     $('#changeStatusModal').modal('show');
    //     $scope.updateStatus = updateStatus;

    //     $scope.nextStatus = updateStatus;
    //     $scope.selectedReasons = [];

    //     if ($scope.transitionReasons[$scope.status] && $scope.transitionReasons[$scope.status][$scope.nextStatus]) {
    //         $scope.reasonSuggestions = $scope.transitionReasons[$scope.status][$scope.nextStatus];
    //         console.log($scope.reasonSuggestions);
    //     } else {
    //         $scope.reasonSuggestions = [];
    //         console.log($scope.reasonSuggestions);
    //     }

    //     $scope.resetCheckBoxes()
    // };

    $scope.showModalStatus = function (nextStatus) {
        $scope.resetCheckBoxes(); // Reset checkboxes when showing the modal
        $('#changeStatusModal').modal('show');
        $scope.currentStatus = $scope.status;
        $scope.nextStatus = nextStatus;
        $scope.selectedReasons = [];
    
        if ($scope.transitionReasons[$scope.currentStatus] && $scope.transitionReasons[$scope.currentStatus][nextStatus]) {
            const reasonKeys = $scope.transitionReasons[$scope.currentStatus][nextStatus];
            $scope.reasonSuggestions = reasonKeys.map(key => ({
                value: key,
                text: $scope.reasonsList[key].text,
                checked: false
            }));
        } else {
            $scope.reasonSuggestions = [];
        }
    };

    $scope.toggleReason = function (reason) {
        const index = $scope.selectedReasons.indexOf(reason.value);
        if (index > -1) {
            $scope.selectedReasons.splice(index, 1);
        } else {
            $scope.selectedReasons.push(reason.value);
        }
    };

    $scope.confirmChangeStatus = function () {

        let description = $scope.billHistoryUpdate.description || "";

        // Add selected reasons
        if ($scope.selectedReasons.length > 0) {
            description += "\n" + $scope.reasonSuggestions
                .filter(reason => $scope.selectedReasons.includes(reason.value))
                .map(reason => reason.text)
                .join("\n");
        }

        // Add "Other" reason
        if ($scope.otherReasonChecked && $scope.otherReasonText) {
            description += "\nKhác: " + $scope.otherReasonText;
        }

        $scope.billHistoryUpdate.description = description.trim();

        $('#changeStatusModal').modal('hide');

        let bill = angular.copy($scope.billResponse)
        bill.status = $scope.updateStatus

        let billHistory = {
            billId: $scope.idBill,
            status: $scope.updateStatus,
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
            }, 0);
        });
    };

    $scope.transitionReasons = {
        1: { // Chờ xác nhận
            5: [
                { text: "Khách yêu cầu hủy", value: 1 },
            ],
            6: [
                { text: "Khách yêu cầu hủy", value: 1 },
                { text: "Khách không phản hồi", value: 2 },
                { text: "Sản phẩm hết hàng", value: 3 },
                { text: "Sản phẩm bị lỗi", value: 4 },
                { text: "Lỗi hệ thống", value: 5 },
            ]
        },
        2: { // Chờ vận chuyển
            5: [
                { text: "Khách yêu cầu hủy", value: 1 },
            ],
            6: [
                { text: "Khách không phản hồi", value: 1 },
                { text: "Không liên hệ được nhân viên giao hàng", value: 2 },
                { text: "Đơn vị giao hàng báo hủy", value: 3 },
                { text: "Sản phẩm bị lỗi", value: 4 },
                { text: "Lỗi hệ thống", value: 5 },
            ]
        },
        3: { // Đang giao
            7: [
                { text: "Khách không nhận hàng", value: 1 },
                { text: "Không liên hệ được khách", value: 2 },
                { text: "Khách yêu cầu giao lại", value: 3 },
                { text: "Mất hàng", value: 4 },
                { text: "Thiếu hàng", value: 5 },
                { text: "Sản phẩm bị lỗi", value: 6 },
            ]
        },
        10: { // Đang giao lại
            8: [
                { text: "Khách không nhận hàng", value: 1 },
                { text: "Không liên hệ được khách", value: 2 },
                { text: "Mất hàng", value: 3 },
                { text: "Thiếu hàng", value: 4 },
                { text: "Sản phẩm bị lỗi", value: 5 },
            ]
        },
        7: { // Thất bại
            9: [
                { text: "Khách yêu cầu giao lại", value: 1 },
                { text: "Sai địa chỉ giao hàng", value: 2 }
            ],
            11: [
                { text: "Khách không nhận hàng", value: 1 },
                { text: "Sản phẩm bị lỗi", value: 2 }
            ]
        },
        // 8: { // Thất bại (Giao lại)
        //     11: [
        //         { text: "Khách không nhận hàng", value: 1 },
        //         { text: "Sản phẩm lỗi", value: 2 }
        //     ]
        // }
    };

    $scope.reasonsList = {
        1: { text: "Khách yêu cầu hủy", value: 1 },
        2: { text: "Khách không phản hồi", value: 2 },
        3: { text: "Sản phẩm hết hàng", value: 3 },
        4: { text: "Sản phẩm bị lỗi", value: 4 },
        5: { text: "Lỗi hệ thống", value: 5 },
        6: { text: "Không liên hệ được nhân viên giao hàng", value: 6 },
        7: { text: "Đơn vị giao hàng báo hủy", value: 7 },
        8: { text: "Khách không nhận hàng", value: 8 },
        9: { text: "Không liên hệ được khách", value: 9 },
        10: { text: "Khách yêu cầu giao lại", value: 10 },
        11: { text: "Mất hàng", value: 11 },
        12: { text: "Thiếu hàng", value: 12 },
        13: { text: "Sai địa chỉ giao hàng", value: 13 }
    };

    $scope.transitionReasons = {
        1: { // Chờ xác nhận
            5: [1],
            6: [1, 2, 3, 4, 5]
        },
        2: { // Chờ vận chuyển
            5: [1],
            6: [2, 6, 7, 4, 5]
        },
        3: { // Đang giao
            7: [8, 9, 10, 11, 12, 4]
        },
        10: { // Đang giao lại
            8: [8, 9, 11, 12, 4]
        },
        7: { // Thất bại
            9: [10, 13],
            11: [8, 4]
        }
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
            8: [11], // Thất bại -> Hoàn hàng
            9: [10], // Chờ giao lại -> Đang giao lại
            10: [4, 8], // Đang giao lại -> Thành công hoặc Thất bại (lai)
            11: [6], // Hoàn hàng -> Đã hủy
        };

        $scope.steps = [];

        $scope.listBillHistory.forEach(function (history) {
            let step = angular.copy(possibleSteps[history.status]);
            if (step && history.type == 1) {
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

    //reset check box
    $scope.resetCheckBoxes = function () {
        $scope.selectedReasons = [];
        $scope.otherReasonChecked = false;
        $scope.otherReasonText = '';
        
        if ($scope.reasonSuggestions) {
            $scope.reasonSuggestions.forEach(function (reason) {
                reason.checked = false;
            });
        }
    };

    // #region Srcoll bill status
    $scope.scrollToEnd = function () {
        var container = document.querySelector('.progress-container');
        container.scrollLeft = container.scrollWidth;
    };

    $scope.scrollLeft = function () {
        var container = document.querySelector('.progress-container');
        container.scrollBy({
            top: 0,
            left: -400,
            behavior: 'smooth'
        });
    };

    $scope.scrollRight = function () {
        var container = document.querySelector('.progress-container');
        container.scrollBy({
            top: 0,
            left: 400,
            behavior: 'smooth'
        });
    };

    // Check if scrollbar is visible
    $scope.checkScrollbarVisibility = function () {
        var container = document.querySelector('.progress-container');
        $scope.scrollbarVisible = container.scrollWidth > container.clientWidth;
    };
    // #endregion

});