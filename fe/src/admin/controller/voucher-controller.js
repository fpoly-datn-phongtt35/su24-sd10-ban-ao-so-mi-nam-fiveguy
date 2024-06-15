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
    const apiCustomerType = "http://localhost:8080/api/admin/customerType"
    const apiCustomerTypeVoucher = "http://localhost:8080/api/admin/customerTypeVoucher"

    $scope.getAllVoucher = function () {
        $http.get(apiVoucher + "/all").then(function (res) {
            $scope.vouchers = res.data;
        })
    }
    $scope.getAllVoucher()

    $scope.customerTypes = [];
    $scope.selectedCustomerTypes = [];
    // $scope.selectedBears = ['1', '2', '3'];

    $scope.getAllCustomerType = function () {
        $http.get('http://localhost:8080/api/admin/customerType/all').then(function (res) {
            $scope.customerTypes = res.data;
            console.log($scope.customerTypes);

            // Use $timeout to ensure Chosen is re-initialized after data is assigned
            $timeout(function () {
                $(".chosen-select").trigger("chosen:updated");
                $(".chosen-select-update").trigger("chosen:updated");
            }, 0);
        });
    };

    $scope.updateSelection = function () {
        console.log("Selected IDs: ", $scope.selectedCustomerTypes);
    };

    // Call getAllCustomerType on load
    $scope.getAllCustomerType();

    // Initialize Chosen
    $scope.initChosen = function () {
        $('#chosen-select').chosen({
            no_results_text: "Oops, nothing found!", width: '100%'
        }).change(function () {
            $scope.$apply(function () {
                // Update selectedCustomerTypes with the IDs of the selected items
                $scope.selectedCustomerTypes = $('#chosen-select').val();
                $scope.updateSelection();
            });
        });
    };

    // Call initChosen when the document is ready
    angular.element(document).ready(function () {
        $scope.initChosen();
    });

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

        let numberList = $scope.selectedCustomerTypes.map(Number);
        const entitiesCustomerType = numberList.map((id) => {
            return {
                id: id,
                name: null
            };
        });

        console.log(entitiesCustomerType);
        console.log(numberList);

        let data = { voucher: $scope.formInputVoucher, customerTypeList: entitiesCustomerType }
        console.log(data)

        $http.post(apiVoucher + "/saveVoucher", data).then(function (res) {
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

    //tao list lựa chọn customerType trong Update form
    $scope.selectedCustomerTypesUpdate = [];
    $scope.listCustomerTypeVoucher = [];

    $scope.getListSelectedCustomerTypesUpdate = function (voucherId) {
        return $http.get(apiCustomerTypeVoucher + "/allCustomerType/" + voucherId).then(function (res) {
            console.log(res.data);
            $scope.listCustomerTypeVoucher = res.data;
            return $scope.listCustomerTypeVoucher.map(entity => entity.customerType.id.toString());
        });
    };

    $scope.setDisableUpdateForm = function (bool) {
        document.getElementById("updateCode").disabled = bool;
        document.getElementById("updateName").disabled = bool;
        document.getElementById("updateDiscountType").disabled = bool;
        document.getElementById("updateValue").disabled = bool;
        document.getElementById("updateMinBill").disabled = bool;
        document.getElementById("updateMaxReVa").disabled = bool;
        document.getElementById("updateQuantity").disabled = bool;
        document.getElementById("updateNumberOfUses").disabled = bool;
        document.getElementById("updateDescribe").disabled = bool;
        document.getElementById("updateStartDate").disabled = bool;
        document.getElementById("updateEndDate").disabled = bool;

        document.getElementById("chosen-select-update").disabled = bool;
    }

    // Create a variable currentVoucher to compare start date and end date (update voucher)
    $scope.currentVoucher = null;

    // Get the voucher value by id
    $scope.getVoucherById = function (voucher) {
        $scope.formUpdateVoucher = angular.copy(voucher);
        if (voucher.startDate != null) {
            $scope.formUpdateVoucher.startDate = new Date(voucher.startDate);
        } else {
            $scope.formUpdateVoucher.startDate = null;
        }
        if (voucher.endDate != null) {
            $scope.formUpdateVoucher.endDate = new Date(voucher.endDate);
        } else {
            $scope.formUpdateVoucher.endDate = null;
        }

        $scope.currentVoucher = angular.copy(voucher);
        console.log(voucher);

        if ($scope.currentVoucher.status == 2) {
            $scope.setDisableUpdateForm(true)
        } else {
            $scope.setDisableUpdateForm(false)
        }

        $scope.getListSelectedCustomerTypesUpdate($scope.currentVoucher.id).then(function (result) {
            $scope.selectedCustomerTypesUpdate = result;
            console.log($scope.selectedCustomerTypesUpdate);

            $timeout(function () {
                $("#chosen-select-update").trigger("chosen:updated");
            }, 0);
        });
    };


    $scope.updateSelectionEdit = function () {
        console.log("Selected IDs: ", $scope.selectedCustomerTypesUpdate);
    };

    // Call getAllCustomerType on load
    $scope.getAllCustomerType();

    // Initialize Chosen
    $scope.initChosenUpdate = function () {
        $('#chosen-select-update').chosen({
            no_results_text: "Oops, nothing found!", width: '100%'
        }).change(function () {
            $scope.$apply(function () {
                // Update selectedCustomerTypes with the IDs of the selected items
                $scope.selectedCustomerTypesUpdate = $('#chosen-select-update').val();
                $scope.updateSelectionEdit();
            });
        });
    };

    // Call initChosen when the document is ready
    angular.element(document).ready(function () {
        $scope.initChosenUpdate();
    });

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

        let numberList = $scope.selectedCustomerTypesUpdate.map(Number);
        const entitiesCustomerType = numberList.map((id) => {
            return {
                id: id,
                name: null
            };
        });

        console.log(entitiesCustomerType);
        console.log(numberList);

        if ($scope.duplicateUpdateNameError || $scope.duplicateUpdateCodeError) {
            return;
        }

        let data = angular.copy($scope.formUpdateVoucher)
        let dataUpdate = { voucher: angular.copy($scope.formUpdateVoucher), customerTypeList: entitiesCustomerType }
        console.log(data);
        $http.put(apiVoucher + "/updateVoucher/" + id, dataUpdate).then(function (res) {
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

        $scope.selectedCustomerTypes = []
        $timeout(function () {
            $(".chosen-select").trigger("chosen:updated");
        }, 0);
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
    var now = new Date();
    var year = now.getFullYear();
    var month = String(now.getMonth() + 1).padStart(2, '0');
    var day = String(now.getDate()).padStart(2, '0');
    var hours = String(now.getHours()).padStart(2, '0');
    var minutes = String(now.getMinutes()).padStart(2, '0');

    $scope.currentDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;

});