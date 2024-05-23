app.controller("nguyen-voucher-ctrl", function ($scope, $http, $timeout) {

    $scope.voucher = {
        id: null,
        name: null,
        code: null,
        value: null,
        discountType: null,
        maximumReductionValue: null,
        minumumTotalAmount: null,
        quantity: null,
        numberOfUses: null,
        describe: null,
        startDate: null,
        endDate: null,
        createdAt: null,
        createdBy: null,
        updatedAt: null,
        updatedBy: null,
        status: null
    }

    $scope.vouchers = []

    const apiVoucher = "http://localhost:8080/api/admin/voucher"

    $scope.getAllVoucher = function() {
        $http.get(apiVoucher + "/all").then(function (res){
            $scope.vouchers = res.data;
            console.log($scope.vouchers);
        })
    }
    $scope.getAllVoucher()
});