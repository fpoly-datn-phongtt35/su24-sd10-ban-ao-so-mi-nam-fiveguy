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

    //set gia tri cua discountType trong add form
    $scope.formInputVoucher.discountType = 1

    //check trung ten va ma add
    $scope.duplicateNameError = false;
    $scope.duplicateCodeError = false;

    $scope.checkDuplicateName = function () {
        $scope.duplicateNameError = $scope.vouchers.some(voucher => voucher.name === $scope.formInputVoucher.name);
    };

    $scope.checkDuplicateCode = function () {
        $scope.duplicateCodeError = $scope.vouchers.some(voucher => voucher.code === $scope.formInputVoucher.code);
    };

    //add voucher
    $scope.addVoucher = function () {

        if ($scope.duplicateNameError || $scope.duplicateCodeError) {
            return;
        }

        let data = $scope.formInputVoucher
        console.log(data)

        $http.post(apiVoucher + "/save", data).then(function (res) {
            $('#addVoucherModal').modal('hide');

            $scope.getVouchers(0);
        })

        $scope.resetFormAdd()
    }

    //chuyen doi gia tri cua MaximumValueReduce tranh loi luc validation *ADD*
    $scope.discountTypeChangeAdd = function () {
        if ($scope.formInputVoucher.discountType == 1) {
            $scope.formInputVoucher.maximumReductionValue = null
        }
        if ($scope.formInputVoucher.discountType == 2) {
            $scope.formInputVoucher.maximumReductionValue = 0
        }
    }

    // // Hàm cập nhật max của startDate khi endDate thay đổi
    // $scope.updateStartDateMax = function () {
    //     if ($scope.formInputVoucher.endDate) {
    //         $scope.formInputVoucher.endDate = $scope.formInputVoucher.endDate;
    //     } else {
    //         $scope.formInputVoucher.endDate = null;
    //     }
    // };

    // Hàm cập nhật min của endDate khi startDate thay đổi
    // $scope.updateEndDateMin = function () {
    //     if ($scope.formInputVoucher.startDate) {
    //         $scope.formInputVoucher.startDate = $scope.formInputVoucher.startDate;
    //     } else {
    //         $scope.formInputVoucher.startDate = null;
    //     }
    // };

    //tao bien current voucher de so sanh gia tri startdate enddate (updatevoucher)
    $scope.currentVoucher = null

    //lay gia tri voucher qua id
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

    //chuyen doi gia tri cua MaximumValueReduce tranh loi luc validation *UPDATE*
    $scope.discountTypeChangeUpdate = function () {
        if ($scope.formUpdateVoucher.discountType == 1) {
            $scope.formUpdateVoucher.maximumReductionValue = null
        }
        if ($scope.formUpdateVoucher.discountType == 2) {
            $scope.formUpdateVoucher.maximumReductionValue = 0
        }
    }

    //check trung ten va ma update
    $scope.duplicateUpdateNameError = false;
    $scope.duplicateUpdateCodeError = false;

    $scope.checkDuplicateNameOnUpdate = function () {
        const currentId = $scope.formUpdateVoucher.id;
        $scope.duplicateUpdateNameError = $scope.vouchers.some(voucher => voucher.name === $scope.formUpdateVoucher.name && voucher.id !== currentId);
    };

    $scope.checkDuplicateCodeOnUpdate = function () {
        const currentId = $scope.formUpdateVoucher.id;
        $scope.duplicateUpdateCodeError = $scope.vouchers.some(voucher => voucher.code === $scope.formUpdateVoucher.code && voucher.id !== currentId);
    };

    //cap nhat voucher
    $scope.updateVoucher = function (id) {
        // if ($scope.formUpdateVoucher.startDate == null) {
        //     $scope.formUpdateVoucher.startDate = $scope.currentVoucher.startDate
        // }

        // if ($scope.formUpdateVoucher.endDate == null) {
        //     $scope.formUpdateVoucher.endDate = $scope.currentVoucher.endDate
        // }

        if ($scope.duplicateUpdateNameError || $scope.duplicateUpdateCodeError) {
            return;
        }

        let data = angular.copy($scope.formUpdateVoucher)
        console.log(data);
        $http.put(apiVoucher + "/update/" + id, data).then(function (res) {
            // $scope.getAllVoucher()

            $scope.getVouchers(0);
        })
    }

    //reset form add
    $scope.resetFormAdd = function () {
        $scope.formInputVoucher = {}
        $scope.formInputVoucher.discountType = 1

        $scope.duplicateNameError = false;
        $scope.duplicateCodeError = false;

        $scope.formAddVoucher.$setPristine();
        $scope.formAddVoucher.$setUntouched();
    }

    $scope.resetFormUpdate = function () {
        $scope.formUpdateVoucher = {}

        $scope.duplicateUpdateNameError = false;
        $scope.duplicateUpdateCodeError = false;

        $scope.formUpdateVoucher.$setPristine();
        $scope.formUpdateVoucher.$setUntouched();
    }

    $scope.filterVoucher = function () {
        $http.get(apiVoucher + "/all").then(function (res) {
            $scope.vouchers = res.data;
        })
    }

    $scope.filtervouchers = [];
    $scope.totalPages = 0;
    $scope.currentPage = 0;
    $scope.desiredPage = 1;
    $scope.filters = {
        name: null,
        code: null,
        discountType: null,
        startDate: null,
        endDate: null,
        status: null
    };

    $scope.getVouchers = function (pageNumber) {
        // let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
        // $http.get('http://localhost:8080/api/admin/voucher/page', { params: params }).then(function(response) {
        //     $scope.filtervouchers = response.data.content;
        //     $scope.totalPages = response.data.totalPages;
        //     $scope.currentPage = pageNumber;
        // });
        let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
        $http.get('http://localhost:8080/api/admin/voucher/page', { params: params }).then(function (response) {
            $scope.filtervouchers = response.data.content;
            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = pageNumber;
            $scope.desiredPage = pageNumber + 1;
        });
    };

    $scope.applyFilters = function () {
        $scope.getVouchers(0);
    };

    $scope.goToPage = function () {
        let pageNumber = $scope.desiredPage - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.getVouchers(pageNumber);
        } else {
            // Reset desiredPage to currentPage if the input is invalid
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };
    // Initial load
    $scope.getVouchers(0);

    $scope.resetFilters = function () {
        $scope.filters = {
            name: null,
            code: null,
            discountType: null,
            startDate: null,
            endDate: null,
            status: null
        };
        $scope.getVouchers(0);
    }

});