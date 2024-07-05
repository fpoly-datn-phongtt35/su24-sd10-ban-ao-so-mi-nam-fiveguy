app.controller("tinh-bill-controller", function ($scope, $http) {
    $scope.product = [];
    $scope.bill = [];

    //Product =================================================================
    const apiProduct = "http://localhost:8080/api/admin/product-tinh"

    $scope.getAllProduct = function () {
        $http.get(apiProduct).then(function (response) {
            $scope.product = response.data;
        }
        )
    }
    $scope.getAllProduct()
    //End Product =============================================================
    const apiBill = "http://localhost:8080/api/admin/bill-tinh"

    $scope.getAllBill = function () {
        $http.get(apiBill).then(function (response) {
            $scope.bill = response.data;
            console.log(response.data);
        }
        )
    }
    $scope.getAllBill()
})