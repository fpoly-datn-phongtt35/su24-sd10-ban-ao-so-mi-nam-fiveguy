app.controller('nguyen-bill-detail-ctrl', function ($scope, $http, $rootScope, $routeParams, $timeout, $location) {

    const apiBill = "http://localhost:8080/api/admin/bill";
    const apiBillDetail = "http://localhost:8080/api/admin/billDetail";
    const apiBillHistory = "http://localhost:8080/api/admin/billHistory"

    const apiReturnOrder = "http://localhost:8080/api/admin/returnOrder";

    const apiProduct = "http://localhost:8080/api/admin/product"
    const apiProductDetail = "http://localhost:8080/api/admin/productDetail"
    const apiProductProperty = "http://localhost:8080/api/admin/productProperty"


    const apiVoucher = "http://localhost:8080/api/admin/voucher"

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

    //billDetail
    $scope.billDetails = []

    //product property for filter
    $scope.categories = []
    $scope.materials = []
    $scope.collars = []
    $scope.wrists = []
    $scope.colors = []
    $scope.sizes = []

    $scope.priceRange = {
        maxPrice: null,
        minPrice: null
    }


    //LẤY THÔNG TIN BILL ***************
    $scope.getBillById = function (id) {
        $http.get(apiBill + "/" + id).then(function (res) {
            console.log(res.data)
            $scope.billResponse = res.data
            $scope.status = res.data.status

            //updateBill sử dụng để cập nhật thông tin giao hàng - sử dụng angularcopy để tránh nó binding
            $scope.updateBill = angular.copy($scope.billResponse)
            $scope.getAllBillDetailByBillId($scope.idBill)

        }).catch(function (error) {
            console.error("Error:", error);
            $location.path('/admin/bill/')
        });
    }
    $scope.getBillById($scope.idBill)

    //XỬ LÝ STATUS BILL, HISTORY BILL, THÔNG TIN GIAO HÀNG, LOGIC CỦA HIỂN THỊ TRẠNG THÁI ĐƠN HÀNG, paymentStatus
    // #region bill status & bill history

    $scope.otherReasonText = null

    $scope.showModalStatus = function (status) {
        $('#changeStatusModal').modal('show');
        $scope.currentStatus = status;
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
    $scope.getBillHistoryByBillId()

    $scope.showModalStatus = function (nextStatus) {
        $http.get(apiBill + "/" + $scope.idBill + "/checkQuantity").then(function (response) {

            //thiếu sản phẩm và trạng thái tiếp theo là chờ giao - 2
            if (response.data == 1 && nextStatus == 2) {
                $scope.showWarning("Giảm số lượng sản phẩm trong đơn hàng");
                return;
            } else if (response.data == 2 && nextStatus == 2) {
                $scope.showWarning("Có sản phẩm trong đơn hàng đã hết");
                return;
            }

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
        }).catch(function (error) {

            console.log("lỗi update status check quantity", error)
            return;
        });
    };

    $scope.confirmChangeStatus = function () {
        if (!$scope.isReasonSelected() && $scope.reasonSuggestions.length > 0) {
            $scope.showError("Vui lòng chọn một lý do hoặc nhập lý do khác.");
            return;
        }
        let description = $scope.billHistoryUpdate.description || "";
        let reasonUpdate = {
            value: 0
        };

        // Add selected reasons to the description
        $scope.reasonSuggestions.forEach(function (reason) {
            if (reason.checked) {
                description += reason.text + ", ";
                reasonUpdate = reason;
            }
        });

        // Add "Other" reason
        if ($scope.otherReasonChecked && $scope.otherReasonText !== null) {
            description += "Khác: " + $scope.otherReasonText;
        }
        // Remove trailing comma and space
        description = description.trim().replace(/,\s*$/, "");

        $scope.billHistoryUpdate.description = description.trim();

        let statusUpdate = $scope.nextStatus
        if ([3, 10].includes($scope.currentStatus) && [7, 8].includes($scope.nextStatus)) {
            if ([9, 10].includes(reasonUpdate.value) && $scope.status == 4) {
                statusUpdate = 7
            }
            if ([9].includes(reasonUpdate.value) && $scope.status == 10) {
                statusUpdate = 8
            }
            if ([11].includes(reasonUpdate.value)) {
                statusUpdate = 81
            }
            if ([8, 12, 4].includes(reasonUpdate.value)) {
                statusUpdate = 8
            }
        }

        let bill = angular.copy($scope.billResponse);
        bill.reason = reasonUpdate.value
        bill.status = statusUpdate;

        if (statusUpdate == 2) {
            bill.shippingFee = $scope.shippingFee
        }


        let billHistory = {
            billId: $scope.idBill,
            reason: reasonUpdate.value,
            status: statusUpdate,
            description: $scope.billHistoryUpdate.description,
        };

        let data = { bill: bill, billHistory: billHistory };

        console.log(data);
        $http.put(apiBill + "/billStatusUpdate/" + $scope.idBill, data).then(function (response) {

            $('#changeStatusModal').modal('hide');

            $scope.getBillById($scope.idBill);
            $scope.getBillHistoryByBillId();

            if (response.data == null) {
                $scope.showError("Kiểm tra lại số lượng sản phẩm trong đơn hàng");
            }
        }).catch(function (error) {
            $('#changeStatusModal').modal('hide');

            console.log("lỗi update status", error)
        });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
        $scope.otherReasonText = null;
    };

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

        // if (selectedReason.checked) {
        //     $scope.otherReasonChecked = false;
        //     $scope.otherReasonText = ""; // Clear other reason text
        // }
    };

    //Chuyển sang trả hàng
    $scope.goToReturnOrder = function () {
        $location.path('/admin/return-order/' + $scope.idBill);
    }

    $scope.reasonsList = {
        1: { text: "Khách yêu cầu hủy", value: 1, status: 0, shortenText: "" },
        2: { text: "Khách không phản hồi", value: 2, status: 0, shortenText: "" },
        3: { text: "Sản phẩm hết hàng", value: 3, status: 0, shortenText: "" },
        4: { text: "Sản phẩm bị lỗi", value: 4, status: 6, shortenText: "Lỗi hàng" },
        5: { text: "Lỗi hệ thống", value: 5, status: 0, shortenText: "" },
        6: { text: "Không liên hệ được nhân viên giao hàng", value: 6, status: 0 },
        7: { text: "Đơn vị giao hàng báo hủy", value: 7, status: 0, shortenText: "" },
        8: { text: "Khách không nhận hàng", value: 8, status: 1, shortenText: "Khách không nhận" },
        9: { text: "Không liên hệ được khách", value: 9, status: 2, shortenText: "Không liên hệ" },
        10: { text: "Khách hẹn giao lại", value: 10, status: 3, shortenText: "Hẹn giao lại" },
        11: { text: "Mất hàng", value: 11, status: 4, shortenText: "Mất hàng" },
        12: { text: "Thiếu hàng", value: 12, status: 5, shortenText: "Thiếu hàng" },
        13: { text: "Sai địa chỉ giao hàng", value: 13, status: 0, shortenText: "" },
    };

    $scope.reasonsList = {
        1: { text: "Khách yêu cầu hủy", value: 1, status: 0, shortenText: "KH hủy" },
        2: { text: "Khách không phản hồi", value: 2, status: 0, shortenText: "KH không phản hồi" },
        3: { text: "Sản phẩm hết hàng", value: 3, status: 0, shortenText: "Hết hàng" },
        4: { text: "Sản phẩm bị lỗi", value: 4, status: 6, shortenText: "Lỗi SP" },
        5: { text: "Lỗi hệ thống", value: 5, status: 0, shortenText: "Lỗi HT" },
        6: { text: "Không liên hệ được nhân viên giao hàng", value: 6, status: 0, shortenText: "Không LH NV" },
        7: { text: "Đơn vị giao hàng báo hủy", value: 7, status: 0, shortenText: "ĐVGH hủy" },
        8: { text: "Khách không nhận hàng", value: 8, status: 1, shortenText: "KH không nhận" },
        9: { text: "Không liên hệ được khách", value: 9, status: 2, shortenText: "Không LH KH" },
        10: { text: "Khách hẹn giao lại", value: 10, status: 3, shortenText: "Hẹn giao lại" },
        11: { text: "Mất hàng", value: 11, status: 4, shortenText: "Mất hàng" },
        12: { text: "Thiếu hàng", value: 12, status: 5, shortenText: "Thiếu hàng" },
        13: { text: "Sai địa chỉ giao hàng", value: 13, status: 0, shortenText: "Sai địa chỉ" },

        14: { text: "Khách trả hàng tại quầy", value: 14, status: 30, shortenText: "Trả tại quầy" },
        15: { text: "Khách yêu cầu trả hàng qua ship", value: 15, status: 31, shortenText: "Trả qua ship" },

        20: { text: "Chưa thanh toán", value: 17, status: 32, shortenText: "hưa thanh toán" },
        21: { text: "Đã thanh toán", value: 18, status: 32, shortenText: "Đã thanh toán" },
        22: { text: "Đã hoàn tiền", value: 18, status: 32, shortenText: "Đã hoàn tiền" },
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
        11: { //Đang hoàn hàng
            13: [4, 11, 12]
        },
        31: { //Trả hàng thất bại
            33: [4, 11, 12]
        }
    };

    $scope.updateStatusSteps = function () {
        $scope.possibleSteps = {
            1: { title: "Chờ xác nhận", icon: "schedule", status: 1 },
            2: { title: "Chờ giao hàng", icon: "local_shipping", status: 2 },
            3: { title: "Đang giao hàng", icon: "directions_car", status: 3 },
            4: { title: "Đã giao hàng", icon: "check_circle", status: 4 },

            5: { title: "Khách hủy", icon: "person_cancel", status: 5 },
            6: { title: "Đã hủy", icon: "block", status: 6 },

            7: { title: "Thất bại", icon: "cancel", status: 7 },
            8: { title: "Thất bại", icon: "cancel", status: 8 },    //giao lại
            81: { title: "Thất bại", icon: "cancel", status: 81 }, //mất hàng

            9: { title: "Chờ giao lại", icon: "autorenew", status: 9 },
            10: { title: "Đang giao lại", icon: "directions_car", status: 10 },

            11: { title: "Đang hoàn hàng", icon: "warehouse", status: 11 },
            12: { title: "Đã hoàn hàng", icon: "warehouse", status: 12 },

            20: { title: "Tạo đơn hàng", icon: "warehouse", status: 20 },
            21: { title: "Hoàn thành", icon: "warehouse", status: 21 },

            30: { title: "Trả hàng", icon: "warehouse", status: 30 },   //trả tại quầy
            31: { title: "Trả hàng", icon: "warehouse", status: 31 },   //trả ship  
            32: { title: "Đã trả hàng", icon: "warehouse", status: 32 },
        };

        $scope.possibleSteps = {
            20: { title: "Tạo đơn hàng", icon: "post_add", status: 20 },
            1: { title: "Chờ xác nhận", icon: "hourglass_empty", status: 1 },
            2: { title: "Chờ giao hàng", icon: "inventory", status: 2 },
            3: { title: "Đang giao hàng", icon: "local_shipping", status: 3 },
            4: { title: "Đã giao hàng", icon: "check_circle", status: 4 },

            60: { title: "Chờ nhập hàng", icon: "not_interested", status: 50 },

            50: { title: "Yêu cầu hủy", icon: "cancel", status: 50 },
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

            21: { title: "Hoàn thành", icon: "task_alt", status: 21 },
            22: { title: "Thành công", icon: "task_alt", status: 20 },

            30: { title: "Trả hàng", icon: "store", status: 30 },  //tại quầy
            31: { title: "Trả hàng", icon: "local_shipping", status: 31 },  //tận nơi
            32: { title: "Đã trả hàng", icon: "inventory_2", status: 32 },
            33: { title: "Trả hàng thất bại", icon: "assignment_late", status: 33 },

        };

        // $scope.deliveryFlow = {
        //     1: [2, 5, 6], // Chờ xác nhận -> Chờ vận chuyển hoặc khách hủy hoặc đã hủy
        //     2: [3, 5, 6], // Chờ vận chuyển -> Đang giao hoặc khách hủy hoặc đã hủy
        //     3: [4, 7, 81], // Đang giao -> Thành công hoặc Thất bại hoặc Thất bại (mất)
        //     4: [], // Thành công (kết thúc)
        //     5: [], // Khách hủy (kết thúc)
        //     6: [], // Đã hủy (kết thúc)
        //     7: [9, 11], // Thất bại -> Chờ giao lại hoặc Hoàn hàng hoặc Hủy(Mất hàng)
        //     8: [11], // Thất bại (lại) -> Hoàn hàng
        //     9: [10], // Chờ giao lại -> Đang giao lại
        //     10: [4, 8, 81], // Đang giao lại -> Thành công hoặc Thất bại (lai) hoặc Thất bại (mất)
        //     11: [6], // Hoàn hàng -> Đã hủy
        //     81: [6], //Thất bại mất -> Đã hủy
        // };

        // $scope.deliveryFlow = {
        //     20: [1],              // Tạo đơn hàng -> Chờ xác nhận
        //     1: [2, 5, 6],         // Chờ xác nhận -> Chờ giao hàng hoặc Khách hủy hoặc Đã hủy
        //     2: [3, 5, 6],         // Chờ giao hàng -> Đang giao hàng hoặc Khách hủy hoặc Đã hủy
        //     3: [4, 7, 81],        // Đang giao hàng -> Đã giao hàng hoặc Thất bại hoặc Thất bại (mất hàng)
        //     4: [21, 30, 31],      // Đã giao hàng -> Hoàn thành hoặc Trả hàng (tại quầy) hoặc Trả hàng (ship)

        //     5: [],                // Khách hủy (kết thúc)
        //     6: [],                // Đã hủy (kết thúc)

        //     7: [9, 11],           // Thất bại -> Chờ giao lại hoặc Đang hoàn hàng
        //     8: [11],              // Thất bại (giao lại) -> Đang hoàn hàng
        //     81: [11],             // Thất bại (mất hàng) -> Đang hoàn hàng

        //     9: [10],              // Chờ giao lại -> Đang giao lại
        //     10: [4, 8, 81],       // Đang giao lại -> Đã giao hàng hoặc Thất bại (giao lại) hoặc Thất bại (mất hàng)

        //     11: [12],             // Đang hoàn hàng -> Đã hoàn hàng
        //     12: [6],              // Đã hoàn hàng -> Đã hủy

        //     21: [],               // Hoàn thành (kết thúc)

        //     30: [32],             // Trả hàng (tại quầy) -> Đã trả hàng
        //     31: [32],             // Trả hàng (ship) -> Đã trả hàng
        //     32: []                // Đã trả hàng (kết thúc)
        // };

        $scope.deliveryFlow = {
            20: [1],              // Tạo đơn hàng -> Chờ xác nhận
            1: [2, 5, 6, 60],         // Chờ xác nhận -> Chờ giao hàng hoặc Khách hủy hoặc Đã hủy
            2: [3, 5, 6, 60],         // Chờ giao hàng -> Đang giao hàng hoặc Khách hủy hoặc Đã hủy
            3: [4, 7, 81],        // Đang giao hàng -> Đã giao hàng hoặc Thất bại hoặc Thất bại (mất hàng)
            4: [21],      // Đã giao hàng -> Hoàn thành hoặc Trả hàng (tại quầy) hoặc Trả hàng (ship)

            60: [1, 5, 6],        // Chờ nhập hàng -> Chờ xác nhận, khách hủy, đã hủy

            5: [],                // Khách hủy (kết thúc)
            6: [],                // Đã hủy (kết thúc)

            7: [9, 11],           // Thất bại -> Chờ giao lại hoặc Đang hoàn hàng
            8: [11],              // Thất bại (giao lại) -> Đang hoàn hàng
            81: [6],              // Thất bại (mất hàng) -> Đã hủy

            9: [10],              // Chờ giao lại -> Đang giao lại
            10: [4, 8, 81],       // Đang giao lại -> Đã giao hàng hoặc Thất bại (giao lại) hoặc Thất bại (mất hàng)
            11: [12, 13],         // Đang hoàn hàng -> Đã hoàn hàng hoặc Hoàn hàng thất bại
            12: [6],              // Đã hoàn hàng -> Đã hủy
            13: [11],              // Hoàn hàng thất bại -> Đã hủy

            21: [30, 31],               // Hoàn thành (kết thúc)

            30: [32, 33],         // Trả hàng (tại quầy) -> Đã trả hàng hoặc Trả hàng thất bại
            31: [32, 33],         // Trả hàng (ship) -> Đã trả hàng hoặc Trả hàng thất bại
            32: [],               // Đã trả hàng (kết thúc)
            33: [6],              // Trả hàng thất bại
        };

        $scope.steps = [];

        $scope.listBillHistory.forEach(function (history) {
            let step = angular.copy($scope.possibleSteps[history.status]);
            if (step && history.type == 1) {
                step.time = history.createdAt;
                step.reason = history.reason;
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

    // Update thông tin giao hàng
    $scope.updateShipmentDetail = function () {
        var fullAddress =
            $scope.addressDetail +
            ', ' +
            $scope.dataWard.WardName +
            ', ' +
            $scope.dataDistrict.DistrictName +
            ', ' +
            $scope.dataCity.ProvinceName;

        var idFullAddress =
            $scope.dataWard.WardCode +
            ', ' +
            $scope.dataDistrict.DistrictID +
            ', ' +
            $scope.dataCity.ProvinceID;
        $scope.updateBill.address = fullAddress;
        $scope.updateBill.addressId = idFullAddress;
        // $scope.updateBill.shippingFee = $scope.shippingFee;

        let data = angular.copy($scope.updateBill)
        $http.put(apiBill + "/shipUpdate/" + $scope.idBill, data).then(function (res) {
            $scope.getBillById($scope.idBill)
            $('#editShipmentDetail').modal('hide');
            $scope.showSuccess("Cập nhật thông tin giao hàng thành công")

            $scope.getBillHistoryByBillId()

            //gọi lại paymentStatus
            // $scope.getAllPaymentStatus($scope.idBill)
        })
    }


    $scope.listPaymentStatus = [];

    $scope.getAllPaymentStatus = function (billId) {
        $scope.getBillById($scope.idBill);
        $http.get(apiBill + "/" + billId + "/paymentStatus").then(function (res) {
            $scope.listPaymentStatus = res.data;
        });
    };

    $scope.calculatePaymentDetails = function () {
        let bill = $scope.billResponse;
        let isPaid = bill.paidShippingFee > 0 || bill.paidAmount > 0;

        if (isPaid) {
            // Đơn đã thanh toán, tính chênh lệch
            let shippingFeeDifference = $scope.shippingFee - bill.paidShippingFee;
            let amountDifference = bill.totalAmountAfterDiscount - bill.paidAmount;
            let totalDifference = shippingFeeDifference + amountDifference;

            return {
                shippingFeeAmount: shippingFeeDifference,
                billAmount: amountDifference,
                totalAmount: totalDifference,
                isRefund: totalDifference < 0
            };
        } else {
            // Đơn chưa thanh toán
            return {
                shippingFeeAmount: bill.shippingFee,
                billAmount: bill.totalAmountAfterDiscount,
                totalAmount: bill.totalAmountAfterDiscount + $scope.shippingFee,
                isRefund: false
            };
        }
    };

    //#region modal thanh toán hoàn tiền

    $scope.showConfirmPayment = function () {
        let paymentDetails = $scope.calculatePaymentDetails();
        if ($scope.paidOrRefundObject.paidOrRefund !== 0) {
            $scope.customerPayment = {
                shippingFeeAmount: paymentDetails.shippingFeeAmount,
                billAmount: paymentDetails.billAmount,
                paymentAmount: $scope.paidOrRefundObject.amount,
                paymentMethod: 1,
                note: null
            };
            $('#paymentAmountModal').modal('show');
        } else {
            $scope.showError("Không có số tiền cần thanh toán.");
        }
    };

    $scope.submitPaymentAmount = function () {
        let paymentStatus = {
            paymentAmount: $scope.customerPayment.paymentAmount,
            customerPaymentStatus: 2,
            paymentMethod: $scope.customerPayment.paymentMethod,
            paymentType: 1,
            note: $scope.customerPayment.note
        };

        let data = {
            bill: $scope.billResponse,
            paymentStatus: paymentStatus,
            payOrRefund: 2
        }

        console.log(data);

        $http.post(apiBill + "/" + $scope.idBill + "/savePaymentStatus", data).then(function (res) {
            $scope.showSuccess("Xác nhận thanh toán thành công");
            $scope.getAllPaymentStatus($scope.idBill);
            $('#paymentAmountModal').modal('hide');
        });
    };

    $scope.showConfirmRefund = function () {
        // $http.get(apiBill + "/" + $scope.idBill + "/checkQuantity").then(function (response) {
        //     if (response.data == 1) {
        //         $scope.showError("Kiểm tra lại số lượng sản phẩm trong đơn hàng");
        //         return;
        //     }
        //     $scope.getBillById($scope.idBill);
        //     $scope.getBillHistoryByBillId();
        //     return;
        // }).catch(function (error) {
        //     console.log("lỗi update status check quantity", error)
        //     return;
        // });

        let paymentDetails = $scope.calculatePaymentDetails();
        if ($scope.paidOrRefundObject.paidOrRefund == 2) {
            $scope.customerPayment = {
                shippingFeeAmount: Math.abs(paymentDetails.shippingFeeAmount),
                billAmount: Math.abs(paymentDetails.billAmount),
                paymentAmount: Math.abs($scope.paidOrRefundObject.amount),
                paymentMethod: 1,
                note: null
            };
            $('#refundAmountModal').modal('show');
        } else {
            $scope.showError("Không có số tiền cần hoàn lại.");
        }
    };

    $scope.submitRefundPayment = function () {

        console.log($scope.customerPayment.paymentMethod);
        let paymentStatus = {
            paymentAmount: $scope.customerPayment.paymentAmount,
            customerPaymentStatus: 3,
            paymentMethod: $scope.customerPayment.paymentMethod,
            paymentType: 3,
            note: $scope.customerPayment.note
        };

        let data = {
            bill: $scope.billResponse,
            paymentStatus: paymentStatus,
            payOrRefund: 2
        }

        console.log(data);

        $http.post(apiBill + "/" + $scope.idBill + "/savePaymentStatus", data).then(function (res) {
            $scope.showSuccess("Xác nhận hoàn tiền thành công");
            $scope.getAllPaymentStatus($scope.idBill);
            $('#refundAmountModal').modal('hide');
            if ($scope.status == 1) {
                $scope.confirmChangeStatusRefund()
            }
        });
    };

    $scope.confirmChangeStatusRefund = function () {
        let description = $scope.billHistoryUpdate.description || "";
        $scope.billHistoryUpdate.description = description.trim();

        let bill = angular.copy($scope.billResponse);
        bill.reason = 0
        bill.status = 2;

        let billHistory = {
            billId: $scope.idBill,
            reason: 0,
            status: 2,
            description: $scope.billHistoryUpdate.description,
        };

        let data = { bill: bill, billHistory: billHistory };

        console.log(data);
        $http.put(apiBill + "/billStatusUpdate/" + $scope.idBill, data).then(function (response) {

            $scope.getBillById($scope.idBill);
            $scope.getBillHistoryByBillId();

        }).catch(function (error) {
            console.log("lỗi update status", error)
        });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
    };

    $scope.calculatePaymentPaidOrRefund = function (bill) {
        var totalPaid = bill.paidAmount + bill.paidShippingFee;
        var totalDue = bill.totalAmountAfterDiscount + bill.shippingFee;
        var difference = totalPaid - totalDue;

        var result = {
            amount: Math.abs(difference),
            paidOrRefund: 3  // Mặc định là 3 (chưa thanh toán)
        };

        // Kiểm tra xem đã thanh toán chưa
        if (bill.paidAmount != 0 || bill.paidShippingFee != 0 || totalPaid == totalDue) {
            if (difference > 0) {
                result.paidOrRefund = 2;  // Refund
            } else if (difference < 0) {
                result.paidOrRefund = 1;  // Paid
            } else {
                result.paidOrRefund = 0;  // Đã thanh toán đủ
            }
        }

        // Kiểm tra trường hợp cả hai giá trị đều bằng 0
        if (totalPaid == 0 && totalDue == 0) {
            result.paidOrRefund = 3;
            result.amount = 0;
        }

        return result;
    };


    $scope.showCancelBillRefund = function () {
        let paymentDetails = $scope.calculatePaymentPaidOrRefund($scope.billResponse);
        if (paymentDetails.paidOrRefund != 3) {
            $scope.customerPayment = {
                shippingFeeAmount: Math.abs(paymentDetails.shippingFeeAmount),
                billAmount: Math.abs(paymentDetails.billAmount),
                paymentAmount: $scope.billResponse.totalAmountAfterDiscount + $scope.billResponse.shippingFee,
                paymentMethod: 1,
                note: null
            };
            var totalPaid = $scope.billResponse.paidAmount + $scope.billResponse.paidShippingFee;
            var totalDue = $scope.billResponse.totalAmountAfterDiscount + $scope.billResponse.shippingFee;
            var difference = totalPaid - totalDue;

            if (paymentDetails.paidOrRefund == 0) {
                $scope.customerPayment.paymentAmount = totalPaid
            }
            if (paymentDetails.paidOrRefund == 1) {
                $scope.customerPayment.paymentAmount = totalPaid
            }
            if (paymentDetails.paidOrRefund == 2) {
                $scope.customerPayment.paymentAmount = totalPaid
            }
            $('#refundAmountCancelBillModal').modal('show');
        } else {
            $scope.showError("Không có số tiền cần hoàn lại.");
        }
    };

    $scope.submitCancelBillPayment = function () {

        console.log($scope.customerPayment.paymentMethod);
        let paymentStatus = {
            paymentAmount: $scope.customerPayment.paymentAmount,
            customerPaymentStatus: 3,
            paymentMethod: $scope.customerPayment.paymentMethod,
            paymentType: 3,
            note: $scope.customerPayment.note
        };

        let data = {
            bill: $scope.billResponse,
            paymentStatus: paymentStatus,
            payOrRefund: 3
        }

        console.log(data);

        $http.post(apiBill + "/" + $scope.idBill + "/savePaymentStatus", data).then(function (res) {
            $scope.showSuccess("Xác nhận hoàn tiền thành công");
            $('#refundAmountCancelBillModal').modal('hide');

            $scope.getBillById($scope.idBill);
            $scope.getBillHistoryByBillId();
        });
    };

    //#endregion


    //theo dõi billResponse
    $scope.$watch('billResponse', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            $scope.listPaymentStatusToShow = []
            $scope.getAllPaymentStatus($scope.idBill)

            //Tính toán hoàn trả hay thanh toán
            $scope.paidOrRefundObject = $scope.calculatePaymentPaidOrRefund($scope.billResponse)

            // $http.get(apiBill + "/" + $scope.idBill + "/checkQuantity").then(function (response) {
            //     if (response.data == 1) {
            //         $scope.errorQuantity = true;
            //     } else if (response.data == 2) {
            //         $scope.errorQuantity = true;
            //     } else {
            //         $scope.errorQuantity = false;
            //     }
            // })
            
            // console.log($scope.errorQuantity + "   afgafhgalkds");
        }
    }, true);

    $scope.$watch('billDetails', function (newValue, oldValue) {
        if (newValue != oldValue) {
            $scope.errorQuantity = false;
            $http.get(apiBill + "/" + $scope.idBill + "/checkQuantity").then(function (response) {
                if (response.data == 1) {
                    $scope.errorQuantity = true;
                } else if (response.data == 2) {
                    $scope.errorQuantity = true;
                } else {
                    $scope.errorQuantity = false;
                }
            })
        }
    })

    // #endregion

    //Lấy thông tin product property để filter và RANGE PRICE
    // #region product property filter & price range
    $scope.getAllProductProperty = function () {
        $http.get(apiProductProperty + "/all").then(function (res) {
            console.log(res.data)

            $scope.categories = res.data.categories
            $scope.materials = res.data.materials
            $scope.collars = res.data.collars
            $scope.wrists = res.data.wrists
            $scope.colors = res.data.colors
            $scope.sizes = res.data.sizes
        });

        $http.get(apiProduct + "/maxPrice").then(function (res) {
            $scope.priceRange.maxPrice = res.data;
            return $http.get(apiProduct + "/minPrice");
        }).then(function (res) {
            $scope.priceRange.minPrice = res.data;
            // console.log($scope.priceRange.minPrice);

            // Call the slider initialization function
            $scope.initSlider(); // Initialize the slider only after both prices are fetched
        });
    }


    //Range lọc giá
    $scope.initSlider = function () {
        var slider = document.getElementById('slider');
        noUiSlider.create(slider, {
            start: [$scope.priceRange.minPrice, $scope.priceRange.maxPrice],
            tooltips: [
                wNumb({
                    decimals: 0,
                    thousand: ','
                }),
                wNumb({
                    decimals: 0,
                    thousand: ','
                }),
            ],
            step: 10000,
            connect: true,
            range: {
                'min': $scope.priceRange.minPrice,
                'max': $scope.priceRange.maxPrice
            },
            pips: {
                mode: 'steps',
                density: 5,
                format: wNumb({
                    decimals: 0,
                    thousand: ','
                })
            }
        });

        // Example of updating AngularJS model on slider change
        // slider.noUiSlider.on('update', function (values, handle) {
        //     $scope.filters.minPrice = parseInt(values[0]);
        //     $scope.filters.maxPrice = parseInt(values[1]);
        //     $scope.$applyAsync(); // Apply changes to AngularJS model
        // });
    };

    //Gọi ở đây để hiẻn thị được pricerange
    $scope.getAllProductProperty();
    // #endregion


    //FILTER, PAGINATION PRODUCT DETAIL
    // #region FILTER, PAGINATION PRODUCT DETAIL
    // $scope.productDetails = [];
    $scope.currentPage = 0;
    $scope.pageSize = 5;
    $scope.totalPages = 0;
    $scope.filters = {
        productName: null,
        categoryId: null,
        materialId: null,
        wristId: null,
        collarId: null,
        colorId: null,
        sizeId: null,
        minPrice: null,
        maxPrice: null
    };
    $scope.desiredPage = 1;

    $scope.getProductDetails = function (pageNumber) {
        let params = {
            productName: $scope.filters.productName,
            categoryId: $scope.filters.categoryId,
            materialId: $scope.filters.materialId,
            wristId: $scope.filters.wristId,
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

                $scope.desiredPage = pageNumber + 1;

                // Initialize inputQuantity for each productDetail
                $scope.productDetails.forEach(function (pd) {
                    pd.inputQuantity = 1; // Set default value to 1
                });

            }, function (error) {
                console.error('Error fetching products:', error);
            });
    };

    // Initial load
    $scope.getProductDetails(0);

    $scope.applyFilters = function () {

        var slider = document.getElementById('slider');
        slider.noUiSlider.on('update', function (values, handle) {
            $scope.filters.minPrice = parseInt(values[0]);
            $scope.filters.maxPrice = parseInt(values[1]);
            $scope.$applyAsync(); // Apply changes to AngularJS model
        });

        $scope.getProductDetails(0);
    };

    $scope.goToPage = function () {
        let pageNumber = $scope.desiredPage - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.loadBills($scope.currentTab, pageNumber);
        } else {
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };

    $scope.loadPage = function (page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getProductDetails(page);
        }
    };

    $scope.resetFilters = function () {
        $scope.filters = {
            productName: null,
            categoryId: null,
            materialId: null,
            wristId: null,
            collarId: null,
            colorId: null,
            sizeId: null,
            minPrice: null,
            maxPrice: null
        };
        var slider = document.getElementById('slider');
        slider.noUiSlider.reset();
        $scope.getProductDetails(0);
    };


    // #endregion

    //XỬ LÝ BILL DETAIL
    // #region hiển thị, thêm, xử lý số lượng, xóa sản phẩm ở billdetail

    //Tổng số lượng và tổng tiền
    $scope.billDetailSummary = {}

    $scope.getAllBillDetailByBillId = function (billId) {
        $http.get(apiBillDetail + "/getAllByBillId/" + billId).then(function (res) {
            $scope.billDetails = res.data
            console.log(res.data);
        })

        $http.get(apiBillDetail + "/" + billId + "/summary").then(function (res) {
            $scope.billDetailSummary = res.data
            // console.log(res.data);

            $scope.showAddress($scope.billResponse)
        })
    }

    $scope.formBillDetail = {}

    //add product to bill
    $scope.addBillDetail = function (pdId, quantityInput, priceInput, pPriceInput, pd) {

        if (quantityInput >= pd.quantity) {
            $scope.showWarning("Thêm thất bại, không được vượt quá số lượng tồn")
            return;
        }

        let params = {
            productDetailId: pdId,
            quantity: quantityInput,
            price: priceInput,
            promotionalPrice: pPriceInput
        };
        $http({
            method: 'POST',
            url: apiBill + "/" + $scope.idBill + "/details",
            params: params
        }).then(function (res) {
            console.log("rssss" + res.data);
            if (res.data == null || res.data == undefined || res.data == "") {
                $scope.showWarning("Thêm thất bại, không được vượt quá số lượng tồn")
                return;
            }

            $scope.showSuccess("Thêm thành công");
            // $scope.getAllBillDetailByBillId($scope.idBill);
            $scope.getProductDetails(0);
            $scope.productDetails.forEach(function (pd) {
                pd.inputQuantity = 1; // Set default value to 0
            });

            //Lấy lại bill để hiển thị lại totalAmount
            $scope.getBillById($scope.idBill)

            //gọi lại paymentStatus
            // $scope.getAllPaymentStatus($scope.idBill)
        }, function (error) {
            console.error('Thêm sản phẩm thất bại:', error);
        });
    };

    $scope.validateQuantity = function (pdIn) {
        // console.log(pdIn.inputQuantity);
        $scope.productDetails.forEach(function (pd) {
            // if (pd == pdIn && pdIn.inputQuantity >= pd.quantity) {
            //     pd.inputQuantity = pd.quantity - 1;
            // }
            if (pd == pdIn && pdIn.inputQuantity >= pd.quantity) {
                pd.inputQuantity = pd.quantity;
            }
        });
        // console.log(pdIn.inputQuantity);
    };

    //remove product in billdetail
    $scope.removeBillDetail = function (billDetailId) {
        $http.delete(apiBill + "/details/" + billDetailId).then(function (res) {
            $scope.showSuccess("Xóa thành công")
            // $scope.getAllBillDetailByBillId($scope.idBill)

            //Lấy lại bill để hiển thị lại totalAmount
            $scope.getBillById($scope.idBill)

            //gọi lại paymentStatus
            // $scope.getAllPaymentStatus($scope.idBill)
        }, function (error) {
            console.error('Xoá sản phẩm thất bại:', error);
        })
    }

    //Validate max quantity in dillDetail
    $scope.validateQuantityBd = function (bd, quantityNew) {

        // if (quantityNew >= bd.productDetail.quantity) {
        //     bd.quantityNew = bd.productDetail.quantity - 1;
        // }
        if (quantityNew >= bd.productDetail.quantity) {
            bd.quantityNew = bd.productDetail.quantity;
        }
    };

    //update quantity in billDetail
    $scope.updateBillDetailQuantity = function (newQuantity, billDetail) {

        if (newQuantity == null || newQuantity == undefined || newQuantity == "" || newQuantity == billDetail.quantity) return;

        // $scope.billDetails.forEach(function (bd) {
        //     if (bd == billDetail && newQuantity > bd.productDetail.quantity) {
        //         bd.quantity = bd.productDetail.quantity - 1;
        //     }
        // });

        let params = {
            newQuantity: newQuantity
        };
        $http({
            method: 'PUT',
            url: apiBill + "/details/" + billDetail.id + "/quantity",
            params: params
        }).then(function (res) {
            $scope.showSuccess("Sửa số lượng thành công");
            // $scope.getAllBillDetailByBillId($scope.idBill);

            //Lấy lại bill để hiển thị lại totalAmount
            $scope.getBillById($scope.idBill)

            //gọi lại paymentStatus
            // $scope.getAllPaymentStatus($scope.idBill)
        }, function (error) {
            console.error('Sửa số lượng thất bại:', error);
        });
    };


    //#region list voucher có thể sử dụng
    $scope.getAllVoucherCanUse = function () {
        $http.get(apiVoucher + "/findAllVoucherCanUse/" + $scope.idBill).then(function (res) {
            $scope.vouchers = res.data
        })
    }
    $scope.getAllVoucherCanUse()

    $scope.setVoucherToBill = function (voucher) {
        $http.put(apiBill + "/" + $scope.idBill + "/setVoucherToBill", voucher).then(function (res) {
            $scope.getBillById($scope.idBill)
            $scope.getAllVoucherCanUse()

            //gọi lại paymentStatus
            // $scope.getAllPaymentStatus($scope.idBill)
        })
    }

    //#endregion

    //Hiển thị sản phẩm trả
    $http.get(apiReturnOrder + "/" + $scope.idBill).then(function (response) {
        $scope.returnOrders = response.data;
        console.log($scope.returnOrders);
    });

    // #endregion


    // #region IN HÓA ĐƠN

    $scope.exportBill = function () {
        let data = angular.copy($scope.billResponse)
        $scope.printBill(data)
    }

    $scope.printBill = resp => {
        const invoiceHTML = generateInvoiceHTML(resp);
        const invoiceWindow = window.open('', '_blank');
        invoiceWindow.document.write(invoiceHTML);
        invoiceWindow.document.close();
    }

    const formatCurrency = price => {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
    }

    const formatDate = dateString => {
        const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' };
        return new Date(dateString).toLocaleString('vi-VN', options);
    };

    function generateInvoiceHTML(resp) {
        const listBillDT = Object.values($scope.billDetails).map(billDT => `
            <tr>
                <td class="desc">${billDT.productDetail.product.name} ${billDT.productDetail.color.name} ${billDT.productDetail.size.name}</td>
                <td class="unit">${billDT.price} - ${billDT.promotionalPrice}</td>
                <td class="qty">${billDT.quantity}</td>
                <td class="total">${formatCurrency(billDT.promotionalPrice * billDT.quantity)}</td>
            </tr>
        `).join('');
        const htmlContent = `<!DOCTYPE html>
    <html>
    <head>
        <style>
            .clearfix:after {
      content: "";
      display: table;
      clear: both;
    }
    
    a {
      color: #5D6975;
      text-decoration: underline;
    }
    
    body {
      position: relative;
      width: 21cm;  
      height: 20cm; 
      margin: 0 auto; 
      color: #001028;
      background: #FFFFFF; 
      font-family: Arial, sans-serif; 
      font-size: 12px; 
      font-family: Arial;
    }
    
    header {
      padding: 10px 0;
      margin-bottom: 30px;
    }
    
    #logo {
      text-align: center;
      margin-bottom: 10px;
    }
    
    #logo img {
      width: 90px;
    }
    
    h1 {
      border-top: 1px solid  #5D6975;
      border-bottom: 1px solid  #5D6975;
      color: #5D6975;
      font-size: 2.4em;
      line-height: 1.4em;
      font-weight: normal;
      text-align: center;
      margin: 0 0 20px 0;
      background: #F5F5F5;
    }
    
    #project {
      float: left;
    }
    
    #project span {
      color: #5D6975;
      text-align: right;
      width: 52px;
      margin-right: 10px;
      display: inline-block;
      font-size: 0.8em;
    }
    
    #company {
      float: right;
      text-align: right;
    }
    
    #project div,
    #company div {
      white-space: nowrap;        
    }
    
    table {
      width: 100%;
      border-collapse: collapse;
      border-spacing: 0;
      margin-bottom: 20px;
    }
    
    table tr:nth-child(2n-1) td {
      background: #F5F5F5;
    }
    
    table th,
    table td {
      text-align: center;
    }
    
    table th {
      padding: 5px 20px;
      color: #5D6975;
      border-bottom: 1px solid #C1CED9;
      white-space: nowrap;        
      font-weight: normal;
    }
    
    table .service,
    table .desc {
      text-align: left;
    }
    
    table td {
      padding: 20px;
      text-align: right;
    }
    
    table td.service,
    table td.desc {
      vertical-align: top;
    }
    
    table td.unit,
    table td.qty,
    table td.total {
      font-size: 1.2em;
    }
    
    table td.grand {
      border-top: 1px solid #5D6975;;
    }
    
    .foter {
      color: #5D6975;
      width: 100%;
      height: 30px;
      border-top: 1px solid #C1CED9;
      padding: 8px 0;
      text-align: center;
    }
    
    .font-b {
        font-weight: bold;
    }
        </style>    
    </head>
    <body>
         <header class="clearfix">
          <div id="logo">
            <img src="https://res.cloudinary.com/dvtz5mjdb/image/upload/v1701333412/image/h1vzhjzyuuwhrhak1bcr.png">
          </div>
          <h1>HÓA ĐƠN</h1>
          <div id="company" class="clearfix">
            <div>#${resp.code}</div>
            <div>Ngày tạo: ${formatDate(resp.createdAt)}</div>
            <div>Ngày thanh toán: ${formatDate(resp.paymentDate)}</div>
          </div>
          <div id="project">
            <div><span>Khách hàng:</span> ${resp.customerEntity ? resp.customerEntity.fullName : 'Khách lẻ'}</div>
            <div><span>SĐT:</span> ${resp ? resp.phoneNumber : ''}</div>
            <div><span>Địa chỉ:</span> ${resp ? resp.address : ''}</div>
          </div>
        </header>
        <main>
          <table>
            <thead>
              <tr>
                <th class="desc">Sản phẩm</th>
                <th>Đơn giá</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
              </tr>
            </thead>
            <tbody>
                ${listBillDT}
              <tr>
                <td colspan="3" class="font-b">Tổng tiền hàng: </td>
                <td class="total font-b">${formatCurrency(resp.totalAmount)}</td>
              </tr>
              ${resp.voucher !== null ? `
                <tr>
                <td colspan="3" class="font-b">Giảm giá</td>
                <td class="total font-b">${resp.voucher.value}${resp.voucher.valueType == 2 ? '%' : '₫'}</td>
                </tr>
              ` : ''}
              <tr>
                <td colspan="3" class="grand font-b">Tổng thanh toán</td>
                <td class="total grand font-b">${resp.totalAmountAfterDiscount == 0 ? formatCurrency(resp.totalAmount + resp.shippingFee) : formatCurrency(resp.totalAmountAfterDiscount + resp.shippingFee)}</td>
              </tr>
            </tbody>
          </table>
           <div class="foter">
                Cảm ơn và hẹn gặp lại!
            </div>
        </main>
        <script>
             window.onload = function() {
                  window.print();
             };
        </script>
    </body>
    </html>`;

        return htmlContent;
    }
    // #endregion


    //#region update thông tin người nhận

    //   get address api
    $scope.province1 = function () {
        // Định nghĩa headers với token
        var config = {
            headers: {
                'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
            }
        };
        // Gọi API
        $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', config)
            .then(function (response) {
                $scope.cities = response.data.data;
            })
            .catch(function (error) {
                // Xử lý lỗi nếu có
                console.error('Error calling API:', error);
            });
    };

    $scope.province1();

    $scope.province = function () {
        // Định nghĩa headers với token
        var config = {
            headers: {
                'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
            }
        };
        // Gọi API
        return $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', config)
            .then(function (response) {
                return response.data.data;
            })
            .catch(function (error) {
                // Xử lý lỗi nếu có
                console.error('Error calling API:', error);
            });
    };

    // Call the province function to load data when the controller initializes
    $scope.getNameProvince = function (cityId) {
        $scope.province().then(function () {
            // Kiểm tra xem mảng districts có tồn tại không
            if ($scope.cities) {
                // Tìm quận/huyện được chọn trong mảng districts
                var selectedCity = $scope.cities.find(function (city) {
                    return city.ProvinceID == cityId;
                });
                $scope.dataCity = selectedCity;
                // console.log($scope.dataCity)
                // $scope.billAddressCity = cityId;
            }
        });
    };

    // $scope.getNameProvince(248);
    $scope.getDistrictsByProvince = function (provinceId) {
        var config = {
            headers: {
                'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
            }
        };
        // Gọi API với tham số province_id
        return $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/district', {
            params: {
                province_id: provinceId
            },
            headers: config.headers
        })
            .then(function (response) {
                // Xử lý kết quả trả về từ API ở đây
                $scope.districts = response.data.data;
            })
            .catch(function (error) {
                console.error('Error calling API:', error);
            });
    };


    $scope.getNameDistrict = function (provinceId, districtId) {
        $scope.getDistrictsByProvince(provinceId).then(function () {
            if ($scope.districts) {
                var selectedDistrict = $scope.districts.find(function (district) {
                    return district.ProvinceID == provinceId;
                });
                // $scope.billAddressDistrict = districtId;
                $scope.dataDistrict = selectedDistrict;
                // console.log($scope.dataDistrict)
            }
        });
    };

    $scope.getWardsByDistrict = function (districtId) {
        var config = {
            headers: {
                'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
            }
        };

        // Return the promise from the $http.get call
        return $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/ward', {
            params: {
                district_id: districtId
            },
            headers: config.headers
        })
            .then(function (response) {
                $scope.wards = response.data.data;
            })
            .catch(function (error) {
                console.error('Error calling API:', error);
            });
    };


    $scope.getNameWard = function (districtId, wardId) {
        $scope.getWardsByDistrict(districtId).then(function () {
            if ($scope.wards) {
                // Tìm quận/huyện được chọn trong mảng districts
                var selectedWard = $scope.wards.find(function (ward) {
                    return ward.WardCode == wardId;
                });

                if (selectedWard) {
                    // $scope.billAddressWard = selectedWard.WardCode;
                    $scope.dataWard = selectedWard;
                    // console.log($scope.dataWard)

                } else {
                    console.log("Không tìm thấy phường/xã với WardCode: " + wardId);
                }
            }
        });
    };

    $scope.shippingFee = 0;
    $scope.calculateShippingFee = function (toDistrictId, toWardCode) {
        if (toDistrictId && toWardCode && $scope.billDetailSummary.totalPromotionalPrice > 0 && $scope.billDetailSummary.totalQuantity > 0) {

            let blows = (toWardCode || "").toString().replace(/\D/g, "");
            let numericDistrictId = Number(toDistrictId);

            // Định nghĩa headers với token
            var config = {
                headers: {
                    'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
                }
            };

            // Body data for the POST request
            var requestData = {
                "service_id": 53321,
                "insurance_value": $scope.billResponse.totalAmountAfterDiscount,
                "coupon": null,
                "from_district_id": 1482,
                "to_district_id": numericDistrictId,
                "to_ward_code": blows,  // Convert to string
                "height": 15,
                "length": 15,
                "weight": 200 * $scope.billDetailSummary.totalQuantity,
                "width": 15
            };

            // Gọi API với phương thức POST và thân yêu cầu (body)
            $http.post('https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', requestData, config)
                .then(function (response) {
                    $scope.shippingFee = response.data.data.total;
                    // console.log($scope.shippingFee)
                    $scope.updateShippingFeeToBill($scope.shippingFee)
                })
                .catch(function (error) {
                    // Xử lý lỗi nếu có
                    console.error('Error calling API:', error);


                    $scope.shippingFee = 50000;
                    $scope.updateShippingFeeToBill($scope.shippingFee)

                });
        }
    };


    //sửa phí giao hàng trong bill khi thay đổi billdetail
    $scope.updateShippingFeeToBill = function () {

        let data = $scope.shippingFee

        if (data == $scope.billResponse.shippingFee) return;
        $http.put(apiBill + "/shippingFeeUpdate/" + $scope.idBill, data).then(function (res) {
            console.log("sửa phí ship thành công");
            // console.log(res.data);
            $http.get(apiBill + "/" + $scope.idBill).then(function (res) {
                // console.log(res.data)
                $scope.billResponse = res.data
            })
        }, function (error) {
            console.error('update shippingfee error', error);
        });
    }

    $scope.showAddress = function (bill) {

        //nếu status khác 1 thì ko cập nhật
        if (bill.status !== 1) return;


        // load địa chỉ
        $scope.addressDetail = '';
        $scope.dataCity = '';
        $scope.dataDistrict = '';
        $scope.dataWard = '';
        // console.log(bill)
        var addressComponentsId = $scope.billResponse.addressId.split(',');
        if (addressComponentsId.length >= 1) {
            $scope.dataCity = addressComponentsId[2].trim();
            $scope.dataDistrict = addressComponentsId[1].trim();
            $scope.dataWard = addressComponentsId[0].trim();
        }
        var addressComponents = $scope.billResponse.address.split(',');
        if (addressComponents.length >= 1) {
            $scope.addressDetail = addressComponents[0].trim();
        }
        $scope.getNameProvince($scope.dataCity);
        $scope.getNameDistrict($scope.dataCity, $scope.dataDistrict);
        $scope.getNameWard($scope.dataDistrict, $scope.dataWard);

        $scope.calculateShippingFee($scope.dataDistrict, $scope.dataWard)
    };

    // $scope.$watch('billDetails', function (newValue, oldValue) {
    //     if (newValue !== oldValue) {
    //         $scope.billDetails.forEach(function(bd) {
    //             if(bd.quantityNew <= 5){
    //                 console.log(bd.quantity);
    //                 bd.quantity = 0
    //                 bd.quantityNew = 0
    //                 $scope.updateBillDetailQuantity1(0, bd);
    //             }
    //             // if(bd.quantityNew > bd.productDetail.quantity){
    //             //     bd.quantity = bd.productDetail.quantity
    //             // }
    //         })
    //     }
    // }, true);

    // $scope.$watch('billResponse', function (newValue, oldValue) {
    //     if ($scope.status == 1 && newValue !== oldValue) {
    //         $scope.showAddress($scope.billResponse)
    //     }
    // }, true);

    // // Theo dõi billDetails
    // $scope.$watch('billDetails', function (newValue, oldValue) {
    //     if ($scope.status == 1 && newValue !== oldValue) {
    //         $scope.showAddress($scope.billResponse)
    //     }
    // }, true);

    //#endregion

});