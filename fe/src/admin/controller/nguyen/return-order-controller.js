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

    $scope.findById = function (code){
        if(code == undefined || code == null || code == "") return;
        
        $http.get($scope.apiReturnOrder + "/findByBillCode/" + code).then(function (response) {

            console.log(response.data);
            if(response.data == ""){
                $scope.showError("Không tìm thấy đơn hàng")
                return;
            }

            if(response.data.status !== 21){
                $scope.showWarning("Đơn hàng không hợp lệ để trả hàng")
                return;
            }

            console.log(response.data);

            $location.path('/admin/return-order/' + response.data.id);

        }).catch(function () {
            console.log("err");
        });
    }
});