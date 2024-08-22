app.controller('nguyen-return-order-ctrl', function ($scope, $http, $location) {

    $scope.apiReturnOrder = "http://localhost:8080/api/admin/returnOrder"

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

    $scope.code = null

    $scope.findById = function (code) {
        if (code == undefined || code == null || code == "") return;
    
        $http.get($scope.apiReturnOrder + "/findByBillCode/" + code).then(function (response) {
            console.log(response.data);
    
            if (response.data == "") {
                $scope.showError("Không tìm thấy đơn hàng");
                return;
            }
    
            if (response.data.status !== 21) {
                $scope.showWarning("Đơn hàng không hợp lệ để trả hàng");
                return;
            }
    
            // Kiểm tra nếu đơn hàng có status = 21
            if (response.data.status == 21) {
                var deliveryDate = response.data.deliveryDate;
    
                // Kiểm tra nếu ngày hiện tại đã quá 7 ngày kể từ ngày giao hàng
                if (!$scope.canShowReturnButton(deliveryDate)) {
                    $scope.showWarning("Đơn hàng đã quá thời gian trả hàng (7 ngày)");
                    return;
                }
            }
    
            // Nếu tất cả các điều kiện hợp lệ, chuyển hướng đến trang trả hàng
            $location.path('/admin/return-order/' + response.data.id);
    
        }).catch(function () {
            console.log("err");
        });
    };

    $scope.canShowReturnButton = function (deliveryDate) {
        var deliveryJsDate = new Date(deliveryDate);
        var currentDate = new Date();
    
        var sevenDaysAfterDelivery = new Date(deliveryJsDate);
        sevenDaysAfterDelivery.setDate(sevenDaysAfterDelivery.getDate() + 7);
    
        return currentDate <= sevenDaysAfterDelivery;
    };
    
});