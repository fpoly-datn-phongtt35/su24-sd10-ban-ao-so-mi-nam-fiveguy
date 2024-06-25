app.controller("nguyen-voucher-create-ctrl", function ($scope, $http, $timeout) {

    const apiVoucher = "http://localhost:8080/api/admin/voucher"

    $scope.vouchers = []
    $scope.getAllVoucher = function () {
        $http.get(apiVoucher + "/all").then(function (res) {
            $scope.vouchers = res.data;
        })
    }
    $scope.getAllVoucher()
});