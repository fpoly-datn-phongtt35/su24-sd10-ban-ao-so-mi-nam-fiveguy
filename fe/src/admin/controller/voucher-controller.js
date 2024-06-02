app.controller("nguyen-voucher-ctrl", function ($scope, $http, $timeout) {

    $scope.voucher = {
        // id: null,
        // name: null,
        // code: null,
        // value: null,
        // discountType: null,
        // maximumReductionValue: null,
        // minumumTotalAmount: null,
        // quantity: null,
        // numberOfUses: null,
        // describe: null,
        // startDate: null,
        // endDate: null,
        // createdAt: null,
        // createdBy: null,
        // updatedAt: null,
        // updatedBy: null,
        // status: null
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

    $scope.formInputVoucher.discountType = 1

    $scope.addVoucher = function () {
        // let maxReVa = +document.getElementById("maximumReductionValue").value

        // if ($scope.formInputVoucher.discountType == 2) {
        //     $scope.formInputVoucher.maximumReductionValue = maxReVa
        // }   
        // if ($scope.formInputVoucher.discountType == 1 && $scope.formInputVoucher.maximumReductionValue == null) {
        //     $scope.formInputVoucher.maximumReductionValue = null
        // }

        let data = $scope.formInputVoucher
        console.log(data)

        $http.post(apiVoucher + "/save", data).then(function (res) {
            $scope.getAllVoucher()
        })

        $scope.resetFormAdd()
    }

    $scope.discountTypeChangeAdd = function () {
        if ($scope.formInputVoucher.discountType == 1) {
            $scope.formInputVoucher.maximumReductionValue = null
        }
        if ($scope.formInputVoucher.discountType == 2) {
            $scope.formInputVoucher.maximumReductionValue = 0
        }
    }

    $scope.currentVoucher = null

    $scope.getVoucherById = function (voucher) {
        $scope.formUpdateVoucher = angular.copy(voucher)
        if (voucher.startDate != null) {
            $scope.formUpdateVoucher.startDate = new Date(voucher.startDate)
        } else {
            $scope.formUpdateVoucher.startDate = null
        }
        if (voucher.endDate != null) {
            $scope.formUpdateVoucher.endDate = new Date(voucher.endDate)
        } else {
            $scope.formUpdateVoucher.endDate = null
        }

        $scope.currentVoucher = angular.copy(voucher)
        console.log(voucher);
    }

    $scope.discountTypeChangeUpdate = function () {
        if ($scope.formUpdateVoucher.discountType == 1) {
            $scope.formUpdateVoucher.maximumReductionValue = null
        }
        if ($scope.formUpdateVoucher.discountType == 2) {
            $scope.formUpdateVoucher.maximumReductionValue = 0
        }
    }

    $scope.updateVoucher = function (id) {
        if ($scope.formUpdateVoucher.startDate == null) {
            $scope.formUpdateVoucher.startDate = $scope.currentVoucher.startDate
        }

        if ($scope.formUpdateVoucher.endDate == null) {
            $scope.formUpdateVoucher.endDate = $scope.currentVoucher.endDate
        }

        let data = angular.copy($scope.formUpdateVoucher)
        console.log(data);
        $http.put(apiVoucher + "/update/" + id, data).then(function (res) {
            $scope.getAllVoucher()
        })
    }

    $scope.resetFormAdd = function () {
        $scope.formInputVoucher = {}
        $scope.formInputVoucher.discountType = 1

        $scope.formAddVoucher.$setPristine();
        $scope.formAddVoucher.$setUntouched();
    }

    $scope.resetFormUpdate = function () {
        $scope.formUpdateVoucher = {}
    }
});