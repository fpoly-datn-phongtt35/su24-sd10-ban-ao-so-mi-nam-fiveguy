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

    $scope.formInputVoucher = {}

    $scope.formUpdateVoucher = {}

    const apiVoucher = "http://localhost:8080/api/admin/voucher"

    $scope.getAllVoucher = function () {
        $http.get(apiVoucher + "/all").then(function (res) {
            $scope.vouchers = res.data;
        })
    }
    $scope.getAllVoucher()

    $scope.addVoucher = function () {
        let data = $scope.formInputVoucher
        $http.post(apiVoucher + "/save", data).then(function (res) {
            $scope.getAllVoucher()
        })
    }

    $scope.getVoucherById = function (voucher) {
        $scope.formUpdateVoucher = angular.copy(voucher)
    }

    $scope.updateVoucher = function (id) {
        let data = angular.copy($scope.formUpdateVoucher)
        console.log(data);
        $http.put(apiVoucher + "/update/" + id, data).then(function (res) {
            $scope.getAllVoucher()
        })
    }
});