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

    $scope.idBill = null

    $scope.findById = function (id){
        if(id == undefined || id == null || id == "") return;
        
        $http.get($scope.apiReturnOrder + "/" + id).then(function (response) {
            if(response.data == ""){
                $scope.showError("Không tìm thấy")
                return;
            }

            console.log(response.data);
            $scope.idBill = null

            $location.path('/admin/return-order/' + id);

        }).catch(function () {
            console.log("err");
        });
    }
});