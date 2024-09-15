app.controller('nguyen-return-order-detail-ctrl', function ($scope, $http, $routeParams, $location) {

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

    $scope.idBill = $routeParams.idBill;

    $scope.apiReturnOrder = "http://localhost:8080/api/admin/returnOrder";
    $scope.apiBill = "http://localhost:8080/api/admin/bill";
    $scope.apiBillDetail = "http://localhost:8080/api/admin/billDetail";

    $scope.returnOrders = [];
    $scope.bill = {};
    $scope.billDetails = [];

    $scope.returnOrdersSummary = {};

    $scope.findById = function (id) {
        if (id == undefined || id == null || id == "") return;

        $http.get($scope.apiBill + "/" + id).then(function (response) {
            $scope.bill = response.data;
            console.log(response.data);
        }).catch(function () {
            console.log("err");
        });

        $http.get($scope.apiReturnOrder + "/" + id).then(function (response) {
            $scope.returnOrders = response.data;
            console.log($scope.returnOrders);
        });

        $http.get($scope.apiBillDetail + "/getAllByBillId/" + id).then(function (response) {
            $scope.billDetails = response.data;

            $scope.billDetails.forEach(function (bd) {
                bd.inputQuantity = 1; // Set default value to 0
            });
            console.log(response.data);
        });
    };
    $scope.findById($scope.idBill);
    $scope.validateQuantity = function (bdIn) {
        if (bdIn.inputQuantity >=
            bdIn.quantity) {
            bdIn.inputQuantity = bdIn.quantity;
        }
    };

    $scope.isProductInReturnList = function (billDetail) {
        return $scope.returnOrders.some(ro => ro.billDetail.id === billDetail.id);
    };
    $scope.addReturnOrder = function (billDetail, quantity = 1, skipPromotionCheck = false) {
        if (!skipPromotionCheck && billDetail.promotionalPrice != billDetail.price) {
            $scope.showWarning("Không thể trả hàng cho sản phẩm có giá khuyến mãi");
            return false;
        }

        if ($scope.isProductInReturnList(billDetail)) {
            $scope.showWarning("Sản phẩm đã được thêm vào danh sách trả hàng");
            return false;
        }

        let refundPrice = (billDetail.promotionalPrice * (1 - $scope.billDetailSummary.tiLeGiam))

        const returnOrder = {
            bill: $scope.bill,
            billDetail: billDetail,
            type: 1,
            quantity: quantity,
            defectiveQuantity: 0,
            refundPrice: refundPrice,
            returnReason: 'Khách muốn trả hàng', // default reason
            returnStatus: 2,
            otherReason: ''
        };

        $scope.updateDefectiveQuantity(returnOrder);
        $scope.returnOrders.push(returnOrder);

        if (!skipPromotionCheck) {
            $scope.showSuccess("Đã thêm sản phẩm vào danh sách trả hàng");
        }
        return true;
    };

    // $scope.updateReturnReason = function(ro) {
    //     if (ro.returnReason === 'Lý do khác') {
    //         ro.note = ro.otherReason || ''; // Use custom reason if provided
    //     } else {f
    //         ro.note = ro.returnReason; // Use selected reason directly
    //     }
    // };

    $scope.addAllReturnableProducts = function () {
        let addedCount = 0;
        $scope.billDetails.forEach(function (bd) {
            if (bd.promotionalPrice === bd.price && !$scope.isProductInReturnList(bd)) {
                const success = $scope.addReturnOrder(bd, bd.quantity, true);
                if (success) {
                    addedCount++;
                }
            }
        });
        if (addedCount > 0) {
            $scope.showSuccess(`Đã thêm ${addedCount} sản phẩm vào danh sách trả hàng`);
        } else {
            $scope.showWarning("Không có sản phẩm nào có thể thêm vào danh sách trả hàng");
        }
    };

    $scope.addSingleProductReturn = function (billDetail) {
        $scope.addReturnOrder(billDetail, 1);
    };

    $scope.validateQuantity = function (ro) {
        if (ro.quantity > ro.billDetail.quantity) {
            ro.quantity = ro.billDetail.quantity;
        }

        if (ro.quantity < 1) {
            ro.quantity = 1;
        }

        $scope.updateDefectiveQuantity(ro);
    };

    $scope.removeReturnOrder = function (returnOrder) {
        const index = $scope.returnOrders.indexOf(returnOrder);
        if (index !== -1) {
            $scope.returnOrders.splice(index, 1);
            $scope.showSuccess("Đã xóa sản phẩm khỏi danh sách trả hàng");
        }
    };

    $scope.updateDefectiveQuantity = function (ro) {
        ro.defectiveQuantity = ro.returnReason == 'Lỗi do sản xuất' ? ro.quantity : 0;
    };

    $scope.updateReturnReason = function (ro) {
        $scope.updateDefectiveQuantity(ro);
        ro.otherReason = ""
    };

    $scope.confirmReturn = function () {

        // Loop through each return order
        // $scope.returnOrders.forEach(function (returnOrder) {
        //     // Check if the reason is "Lý do khác"
        //     if (returnOrder.returnReason == "Lý do khác") {
        //         // Prepend "Lý do khác: " to otherReason
        //         returnOrder.returnReason = "Lý do khác: " + returnOrder.otherReason;
        //     }
        // });

        var submitData = angular.copy($scope.returnOrders);

        // Process each return order
        submitData.forEach(function(ro) {
            if (ro.otherReason != null && ro.otherReason.trim() !== "") {
                // If otherReason is not null or empty, append it to returnReason
                ro.returnReason = ro.returnReason + " : " + ro.otherReason.trim();
            }
            // Remove the separate otherReason field as it's now part of returnReason
            delete ro.otherReason;
        });
        // Send the updated returnOrders to the server
        $http.post($scope.apiReturnOrder + "/addReturnOrder", submitData).then(function (response) {
            console.log(response);
            $('#confirmReturnModal').modal('hide');
            $location.path('/admin/bill/' + $scope.bill.id);
        }).catch(function (error) {
            console.error("Error:", error);
        });
    };
    
    $scope.returnOrdersSummary.tongTienTra = 0;


    $scope.calculateSummary = function () {
        $http.put($scope.apiReturnOrder + "/" + $scope.idBill + "/calculateSummary", $scope.returnOrders).then(function (response) {
            console.log(response.data);
            $scope.returnOrdersSummary = response.data
        }).catch(function (error) {
            console.error("Error:", error);
        });
    }

    $scope.$watch('returnOrders', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            $scope.calculateSummary()
            // console.log($scope.returnOrders);
        }
    }, true);

    $scope.calculateSummaryBillDetail = function () {
        $http.put($scope.apiReturnOrder + "/" + $scope.idBill + "/calculateSummaryBillDetail", $scope.billDetails).then(function (response) {
            // console.log(response.data);
            $scope.billDetailSummary = response.data
        }).catch(function (error) {
            console.error("Error:", error);
        });
    }

    $scope.$watch('billDetails', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            $scope.calculateSummaryBillDetail()
        }
    }, true);



    $scope.formatInput = function (input) {
        // Lấy giá trị hiện tại của input, chỉ giữ lại số nguyên
        let value = input.value.replace(/\D/g, '');

        // Format số
        if (value) {
            let number = parseInt(value, 10);
            input.value = formatCurrency(number);
        }
    }

    $scope.formatCurrency = function (number) {
        // Làm tròn số đến 2 chữ số thập phân
        let roundedNumber = Number(number).toFixed(0);

        // Tách phần nguyên và phần thập phân
        let parts = roundedNumber.split('.');
        let integerPart = parts[0];
        let decimalPart = parts.length > 1 ? parts[1] : '';

        // Thêm dấu chấm để phân tách hàng nghìn
        integerPart = integerPart.replace(/\B(?=(\d{3})+(?!\d))/g, '.');

        // Ghép lại phần nguyên và phần thập phân (nếu có)
        let formattedNumber = integerPart + (decimalPart ? ',' + decimalPart : '');

        // Thêm ký hiệu tiền tệ
        return formattedNumber + ' đ';
    }



});


app.filter('formatCurrency', function () {
    return function (input) {
        if (!input) return '';

        // Chuyển đổi input thành số và làm tròn đến số nguyên
        var number = Math.round(parseFloat(input));

        // Format số thành chuỗi có dấu chấm phân cách hàng nghìn
        return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    };
});