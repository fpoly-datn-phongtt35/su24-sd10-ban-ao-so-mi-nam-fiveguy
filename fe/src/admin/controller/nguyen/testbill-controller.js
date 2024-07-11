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

    $scope.otherReasonText = null

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
    $scope.getBillHistoryByBillId()

    $scope.showModalStatus = function (nextStatus) {
        $scope.resetCheckBoxes();

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

    $scope.confirmChangeStatus = function () {
        if (!$scope.isReasonSelected() && $scope.reasonSuggestions.length > 0) {
            alert("Vui lòng chọn một lý do hoặc nhập lý do khác.");
            return;
        }

        let description = $scope.billHistoryUpdate.description || "";

        let reasonUpdate = 0;
        // Add selected reasons to the description
        $scope.reasonSuggestions.forEach(function (reason) {
            if (reason.checked) {
                description += reason.text + ", ";
                reasonUpdate = reason.value;
            }
        });

        // Add "Other" reason
        if ($scope.otherReasonChecked && $scope.otherReasonText !== null) {
            description += "Khác: " + $scope.otherReasonText;
        }
        // Remove trailing comma and space
        description = description.trim().replace(/,\s*$/, "");

        $scope.billHistoryUpdate.description = description.trim();

        // if ([7, 8].includes($scope.nextStatus) && [3, 10].includes($scope.currentStatus)) {
        //     $scope.reasonSuggestions.forEach(function (reason) {
        //         if (reason.checked) {
        //             if (reason.value == 11) {
        //                 reasonUpdate = 1
        //                 return
        //             }
        //             if (reason.value == 12 || reason.value == 4) {
        //                 reasonUpdate = 2
        //                 return
        //             }
        //         }
        //     });
        // }

        let statusUpdate = $scope.nextStatus
        if ([3, 10].includes($scope.currentStatus) && [7, 8].includes($scope.nextStatus) && [11].includes(reasonUpdate)) {
            statusUpdate = 81;
        }
        if ([3, 10].includes($scope.currentStatus) && [7, 8].includes($scope.nextStatus) && [12, 4].includes(reasonUpdate)) {
            statusUpdate = 8;
        }

        let bill = angular.copy($scope.billResponse);
        bill.reason = reasonUpdate
        bill.status = statusUpdate;


        let billHistory = {
            billId: $scope.idBill,
            status: statusUpdate,
            description: $scope.billHistoryUpdate.description,
        };

        let data = { bill: bill, billHistory: billHistory };

        $('#changeStatusModal').modal('hide');
        console.log(data);
        $http.put(apiBill + "/billStatusUpdate/" + $scope.idBill, data).then(function (response) {
            // Cập nhật lại danh sách lịch sử
            $scope.getBillById($scope.idBill);
            $scope.getBillHistoryByBillId();
        }).catch(function (error) {
            console.log("lỗi update status")
        });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
        $scope.otherReasonText = null;
    };

    // $scope.toggleReason = function (reason) {
    //     reason.checked = !reason.checked; // Toggle the checked state of the reason
    // };
    // $scope.toggleReason = function (reason) {
    //     // Uncheck all other reasons
    //     angular.forEach($scope.reasonSuggestions, function (r) {
    //         if (r !== reason) {
    //             r.checked = false;
    //         }
    //     });
    //     // Toggle the checked state of the selected reason
    //     reason.checked = !reason.checked;
    // };
    $scope.isReasonSelected = function () {
        return $scope.reasonSuggestions.some(reason => reason.checked) || $scope.otherReasonChecked;
    };
    $scope.toggleReason = function (selectedReason) {
        // Uncheck all other reasons
        $scope.reasonSuggestions.forEach(function (reason) {
            if (reason !== selectedReason) {
                reason.checked = false;
            }
        });
    
        // Toggle the selected reason
        selectedReason.checked = !selectedReason.checked;
    
        // Uncheck "Other" reason if any predefined reason is selected
        // if (selectedReason.checked) {
        //     $scope.otherReasonChecked = false;
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
        10: { text: "Khách hẹn giao lại", value: 10 },
        11: { text: "Mất hàng", value: 11 },
        12: { text: "Thiếu hàng", value: 12 },
        13: { text: "Sai địa chỉ giao hàng", value: 13 }
    };
    $scope.transitionReasons1 = {
        1: { // Chờ xác nhận
            5: [1],
            6: [1, 2, 3, 4, 5]
        },
        2: { // Chờ vận chuyển
            5: [1],
            6: [2, 6, 7, 4, 5]
        },
        3: { // Đang giao
            101: [8, 9, 10, 11, 12, 4]
        },
        10: { // Đang giao lại
            108: [8, 9, 11, 12, 4]
        },
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
            11: [8, 4],
        },
        81: { //Thất bại (mất)
            6: [11]
        }
    };

    $scope.updateStatusSteps = function () {
        $scope.possibleSteps = {
            1: { title: "Chờ xác nhận", icon: "schedule", status: 1 },
            2: { title: "Chờ vận chuyển", icon: "local_shipping", status: 2 },
            3: { title: "Đang giao", icon: "directions_car", status: 3 },
            4: { title: "Thành công", icon: "home", status: 4 },
            5: { title: "Khách hủy", icon: "cancel", status: 5 },
            6: { title: "Đã hủy", icon: "cancel", status: 6 },
            7: { title: "Thất bại", icon: "cancel", status: 7 },
            8: { title: "Thất bại(Lại, lỗi)", icon: "cancel", status: 8 },
            81: { title: "Thất bại(Mất)", icon: "cancel", status: 81 },
            9: { title: "Chờ giao lại", icon: "local_shipping", status: 9 },
            10: { title: "Đang giao lại", icon: "directions_car", status: 10 },
            11: { title: "Hoàn hàng", icon: "warehouse", status: 11 }
        };

        $scope.possibleSteps1 = {
            1: { title: "Chờ xác nhận", icon: "schedule", status: 1 },
            2: { title: "Chờ vận chuyển", icon: "local_shipping", status: 2 },
            3: { title: "Đang giao", icon: "directions_car", status: 3 },
            4: { title: "Thành công", icon: "home", status: 4 },
            5: { title: "Khách hủy", icon: "cancel", status: 5 },
            6: { title: "Đã hủy", icon: "cancel", status: 6 },
            101: { title: "Thất bại", icon: "cancel", status: 101 },
            102: { title: "Thất bại(Hẹn)", icon: "cancel", status: 102, reason: "Hẹn giao lại" },
            103: { title: "Thất bại(Mất)", icon: "cancel", status: 103, reason: "Mất hàng" },
            104: { title: "Thất bại(Lỗi)", icon: "cancel", status: 104, reason: "Lỗi hàng" },
            105: { title: "Thất bại(Thiếu)", icon: "cancel", status: 105, reason: "Thiếu hàng" },
            106: { title: "Thất bại(Không nhận)", icon: "cancel", status: 106, reason: "Không nhận" },
            107: { title: "Thất bại(Không liên hệ)", icon: "cancel", status: 107, reason: "Không liên hệ" },
            108: { title: "Thất bại(Lại)", icon: "cancel", status: 108 },
            9: { title: "Chờ giao lại", icon: "local_shipping", status: 9 },
            10: { title: "Đang giao lại", icon: "directions_car", status: 10 },
            11: { title: "Hoàn hàng", icon: "warehouse", status: 11 }
        };

        $scope.deliveryFlow = {
            1: [2, 5, 6], // Chờ xác nhận -> Chờ vận chuyển hoặc khách hủy hoặc đã hủy
            2: [3, 5, 6], // Chờ vận chuyển -> Đang giao hoặc khách hủy hoặc đã hủy
            3: [4, 7, 81], // Đang giao -> Thành công hoặc Thất bại hoặc Thất bại (mất)
            4: [], // Thành công (kết thúc)
            5: [], // Khách hủy (kết thúc)
            6: [], // Đã hủy (kết thúc)
            7: [9, 11], // Thất bại -> Chờ giao lại hoặc Hoàn hàng hoặc Hủy(Mất hàng)
            8: [11], // Thất bại (lại) -> Hoàn hàng
            9: [10], // Chờ giao lại -> Đang giao lại
            10: [4, 8, 81], // Đang giao lại -> Thành công hoặc Thất bại (lai) hoặc Thất bại (mất)
            11: [6], // Hoàn hàng -> Đã hủy
            81: [6], //Thất bại mất -> Đã hủy
        };

        $scope.deliveryFlow1 = {
            1: [2, 5, 6], // Chờ xác nhận -> Chờ vận chuyển hoặc khách hủy hoặc đã hủy
            2: [3, 5, 6], // Chờ vận chuyển -> Đang giao hoặc khách hủy hoặc đã hủy
            3: [4, 101, 102, 103, 104, 105, 106, 107], // Đang giao -> Thành công hoặc Thất bại 101 - 107
            4: [], // Thành công (kết thúc)
            5: [], // Khách hủy (kết thúc)
            6: [], // Đã hủy (kết thúc)
            101: [9, 11], // giao lại, hoàn
            102: [9, 11], // hoàn
            103: [6], // hủy
            104: [11], // hoàn
            105: [11], // hoàn
            106: [11], // hoàn
            107: [9, 11], // giao lại, hoàn
            108: [11], // hoàn
            9: [10], // Chờ giao lại -> Đang giao lại
            10: [4, 103, 104, 105, 106, 107, 108], // Đang giao lại -> Thành công hoặc Thất bại (103, 104, 105, 106, 107, 108)
            11: [6], // Hoàn hàng -> Đã hủy
        };

        $scope.steps = [];

        $scope.listBillHistory.forEach(function (history) {
            let step = angular.copy($scope.possibleSteps[history.status]);
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

    $scope.getStatusTitle = function (status) {
        return $scope.possibleSteps[status] ? $scope.possibleSteps[status].title : 'Unknown';
    };

    // Toggle reason function to handle the checking and unchecking logic
    $scope.unChecked = function (selectedReason) {
        if (selectedReason.checked) {
            // If reason 11 is checked, uncheck reasons 4 and 12
            if (selectedReason.value === 11) {
                $scope.reasonSuggestions.forEach(function (reason) {
                    if (reason.value === 4 || reason.value === 12) {
                        reason.checked = false;
                    }
                    if (reason.value === 10) {
                        reason.checked = false;
                    }
                });
            }
            // If reason 4 is checked, uncheck reasons 11 and 12
            if (selectedReason.value === 4 || selectedReason.value === 12) {
                $scope.reasonSuggestions.forEach(function (reason) {
                    if (reason.value === 11) {
                        reason.checked = false;
                    }
                    if (reason.value === 10) {
                        reason.checked = false;
                    }
                });
            }
            if (selectedReason.value === 8 || selectedReason.value === 9) {
                $scope.reasonSuggestions.forEach(function (reason) {
                    if (reason.value === 10) {
                        reason.checked = false;
                    }
                });
            }
            if (selectedReason.value === 10) {
                $scope.reasonSuggestions.forEach(function (reason) {
                    if (reason.value === 8 || reason.value === 9) {
                        reason.checked = false;
                    }
                    if (reason.value === 4 || reason.value === 12) {
                        reason.checked = false;
                    }
                    if (reason.value === 11) {
                        reason.checked = false;
                    }
                });
            }
        }
    };

    //reset check box
    $scope.resetCheckBoxes = function () {
        $scope.selectedReasons = [];
        $scope.otherReasonChecked = false;
        $scope.otherReasonText = null;

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