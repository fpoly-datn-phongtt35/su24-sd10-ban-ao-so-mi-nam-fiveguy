app.controller('nguyen-return-order-detail-ctrl', function ($scope, $http, $routeParams) {

    $scope.idBill = $routeParams.idBill;

    $scope.apiReturnOrder = "http://localhost:8080/api/admin/returnOrder"
    $scope.apiBill = "http://localhost:8080/api/admin/bill"
    $scope.apiBillDetail = "http://localhost:8080/api/admin/billDetail"

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

    $scope.returnOrders = []
    $scope.bill = {}

    $scope.billDetails = []

    $scope.findById = function (id) {
        if (id == undefined || id == null || id == "") return;

        $http.get($scope.apiReturnOrder + "/" + id).then(function (response) {
            $scope.returnOrders = response.data
            console.log($scope.returnOrders);

        })

        $http.get($scope.apiBill + "/" + id).then(function (response) {
            $scope.bill = response.data
            console.log(response.data);

        }).catch(function () {
            console.log("err");
        });

        $http.get($scope.apiBillDetail + "/getAllByBillId/" + id).then(function (response) {
            $scope.billDetails = response.data
            console.log(response.data);

        })

    }
    $scope.findById($scope.idBill)


    $scope.validateQuantity = function (bdIn) {
            if (bdIn.inputQuantity >= bdIn.quantity) {
                bdIn.inputQuantity = bdIn.quantity;
        }
    };
});