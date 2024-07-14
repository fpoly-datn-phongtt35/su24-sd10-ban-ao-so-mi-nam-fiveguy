app.controller("nguyen-voucher2-ctrl", function ($scope, $http, $timeout) {

    $scope.voucher = {
        // id: null,
        // name: null,
        // code: null,
        // value: null,
        // discountType: null,
        // maximumReductionValue: null,
        // minimumTotalAmount: null,
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
    const apiCustomerVoucher = "http://localhost:8080/api/admin/customerVoucher"

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

    $scope.getAllVoucher = function () {
        $http.get(apiVoucher + "/all").then(function (res) {
            $scope.vouchers = res.data;
        })
    }
    $scope.getAllVoucher()

    $scope.customerTypes = [];
    $scope.selectedCustomerTypes = [];

    // lay tat ca customerType
    $scope.getAllCustomerType = function () {
        $http.get(apiCustomerType + '/all').then(function (res) {
            $scope.customerTypes = res.data;
            console.log($scope.customerTypes);
        });
    };
    $scope.getAllCustomerType();

    $scope.formInputVoucher.applyfor = 0

    //set gia tri cua discountType trong add form
    $scope.formInputVoucher.discountType = 1

    //#region validate form add

    //check trung ten va ma add
    $scope.duplicateNameError = false;
    $scope.duplicateCodeError = false;
    $scope.codeLengthError = false;

    $scope.checkDuplicateName = function () {
        $scope.duplicateNameError = $scope.vouchers.some(voucher => voucher.name === $scope.formInputVoucher.name);
    };

    $scope.checkDuplicateCode = function () {
        const codeWithoutSpaces = $scope.formInputVoucher.code.replace(/\s+/g, '');

        $scope.duplicateCodeError = $scope.vouchers.some(voucher =>
            voucher.code.toUpperCase() === $scope.formInputVoucher.code.toUpperCase()
        );
        $scope.codeLengthError = codeWithoutSpaces.length > 15;
    };

    $scope.validateValueError = false;
    $scope.validateAddMaxReVa = null;

    $scope.checkValidateAllValue = function () {
        let value = $scope.formInputVoucher.value
        let max = $scope.formInputVoucher.maximumReductionValue
        let min = $scope.formInputVoucher.minimumTotalAmount
        if ($scope.formInputVoucher.maximumReductionValue == undefined) {
            max = 0;
        }
        if ($scope.formInputVoucher.minimumTotalAmount == undefined) {
            min = 0;
        }

        console.log("-");
        console.log(value);
        console.log(max);
        console.log(min);
        console.log(min * value / 100);

        if (min * value / 100 > max) {
            $scope.validateValueError = true
            $scope.validateAddMaxReVa = min * value / 100;
        } else {
            $scope.validateValueError = false
            $scope.validateAddMaxReVa = 0;
        }
    }

    $scope.validateAddValue = function (discountType) {
        if ($scope.formInputVoucher.value > 100 && discountType == 1) {
            $scope.formInputVoucher.value = 100
        }
        if ($scope.formInputVoucher.value < 1 && discountType == 1) {
            $scope.formInputVoucher.value = 1
        }
    };

    //chuyen doi gia tri cua MaximumValueReduce tranh loi luc validation *ADD*
    $scope.discountTypeChangeAdd = function (discountType) {
        $scope.formInputVoucher.maximumReductionValue = 0
        $scope.validateAddValue(discountType)
    }

    //#endregion

    //ADD VOUCHER ***************
    $scope.addVoucher = function () {

        if ($scope.formInputVoucher.discountType == 1) {
            $scope.checkValidateAllValue()
        }

        if ($scope.duplicateNameError || $scope.duplicateCodeError || $scope.validateValueError) {
            return;
        }

        $scope.entitiesCustomerType = $scope.customerTypes.filter(ct => ct.selected)
        console.log($scope.formInputVoucher.applyfor);

        if ($scope.formInputVoucher.applyfor == 0) {
            entitiesCustomerType = [];
        }

        console.log($scope.entitiesCustomerType.length);
        if ($scope.entitiesCustomerType.length == 0) {
            return;
        }

        let data = {
            voucher: $scope.formInputVoucher, customerTypeList: $scope.entitiesCustomerType
        }
        console.log(data)

        $http.post(apiVoucher + "/saveVoucher", data).then(function (res) {
            $('#addVoucherModal').modal('hide');
            $scope.showSuccess("Thêm thành công")
            $scope.getVouchers(0);
            $scope.resetFormAdd()
        })
    }

    //###########################################################################################################################
    //###########################################################################################################################
    //###########################################################################################################################
    //###########################################################################################################################
    //###########################################################################################################################

    //DISABLE FORM UPDATE
    $scope.setDisableUpdateForm = function (bool) {
        document.getElementById("updateCode").disabled = true;
        document.getElementById("updateName").disabled = bool;
        // document.getElementById("updateDiscountType").disabled = bool;
        document.getElementById("updateValue").disabled = bool;
        document.getElementById("updateMinBill").disabled = bool;
        document.getElementById("updateMaxReVa").disabled = bool;
        document.getElementById("updateQuantity").disabled = bool;
        document.getElementById("updateNumberOfUses").disabled = bool;
        document.getElementById("updateDescribe").disabled = bool;
        document.getElementById("updateStartDate").disabled = bool;
        document.getElementById("updateEndDate").disabled = bool;
        document.getElementById("flexRadioUpdate0").disabled = bool;
        document.getElementById("flexRadioUpdate1").disabled = bool;
        document.getElementById("radioTypeUpdate1").disabled = bool;
        document.getElementById("radioTypeUpdate2").disabled = bool;

        //check status TAM NGUNG
        if (bool) {
            document.getElementById("checkPause").style.display = "none";
        } else {
            document.getElementById("checkPause").style.display = "block";
        }
    }

    //tao list lựa chọn customerType trong Update form
    $scope.listCustomerTypeVoucher = [];

    $scope.getListSelectedCustomerTypesUpdate = function (voucherId) {
        return $http.get(apiCustomerTypeVoucher + "/allCustomerType/" + voucherId).then(function (res) {
            console.log(res.data);
            $scope.listCustomerTypeVoucher = res.data;
            return $scope.listCustomerTypeVoucher.map(entity => entity.customerType.id);
        });
    };

    // Create a variable currentVoucher to compare start date and end date (update voucher)
    $scope.currentVoucher = null;

    $scope.valuesList = []


    let checkPauseStatus = document.getElementById("checkPauseStatus");

    // Get the voucher value by id
    $scope.getVoucherById = function (voucher) {
        $scope.resetFormUpdate()
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

        checkPauseStatus.checked = $scope.currentVoucher.status == 4

        $scope.getListSelectedCustomerTypesUpdate($scope.currentVoucher.id).then(function (result) {
            $scope.selectedCustomerTypesUpdate = result;
            console.log($scope.selectedCustomerTypesUpdate);
            $scope.checkCheckboxes($scope.selectedCustomerTypesUpdate)
        });
        if ($scope.currentVoucher.status == 2) {
            $scope.setDisableUpdateForm(true)
        } else {
            $scope.setDisableUpdateForm(false)
        }
        if ($scope.currentVoucher.status == 2) {
            document.getElementById("updateButton").style.display = "none"
        } else {
            document.getElementById("updateButton").style.display = "block"
        }
        // document.getElementById("flexRadioUpdate0").disabled = true;
        // document.getElementById("flexRadioUpdate1").disabled = true;
        document.getElementById("updateStartDate").disabled = true;

    };

    // Call getAllCustomerType on load
    $scope.getAllCustomerType();

    //#region validate form update

    $scope.validateValueErrorUpdate = false;
    $scope.validateAddMaxReVaUpdate = null;

    $scope.checkValidateAllValueUpdate = function () {
        let value = $scope.formUpdateVoucher.value
        let max = $scope.formUpdateVoucher.maximumReductionValue
        let min = $scope.formUpdateVoucher.minimumTotalAmount

        if ($scope.formUpdateVoucher.maximumReductionValue == undefined) {
            max = 0;
        }
        if ($scope.formUpdateVoucher.minimumTotalAmount == undefined) {
            min = 0;
        }
        console.log("-");
        console.log(value);
        console.log(max);
        console.log(min);
        console.log(min * value / 100);
        if (min * value / 100 > max) {
            $scope.validateValueErrorUpdate = true
            $scope.validateAddMaxReVaUpdate = min * value / 100;
        } else {
            $scope.validateValueErrorUpdate = false
            $scope.validateAddMaxReVaUpdate = 0;
        }
        console.log($scope.validateAddMaxReVaUpdate);
    }


    $scope.validateUpdateValue = function (discountType) {
        if ($scope.formUpdateVoucher.value > 100 && discountType == 1) {
            $scope.formUpdateVoucher.value = 100
        }
        if ($scope.formUpdateVoucher.value < 1 && discountType == 1) {
            $scope.formUpdateVoucher.value = 1
        }
        // if( value < 1000  && $scope.formInputVoucher.discountType == 1){
        //     $scope.formInputVoucher.value == 1000
        // }
    };

    //chuyen doi gia tri cua MaximumValueReduce tranh loi luc validation *ADD*
    $scope.discountTypeChangeUpdate = function (discountType) {
        console.log($scope.formInputVoucher.discountType);
        $scope.formUpdateVoucher.maximumReductionValue = 0
        $scope.validateUpdateValue(discountType)
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
    //#endregion


    //UPDATE VOUCHER ************
    $scope.updateVoucher = function (id) {
        if ($scope.formUpdateVoucher.discountType == 1) {
            $scope.checkValidateAllValueUpdate()
        }

        if ($scope.duplicateUpdateNameError || $scope.duplicateUpdateCodeError || $scope.validateValueErrorUpdate) {
            return;
        }

        // let entitiesCustomerType = []

        if (checkPauseStatus.checked) {
            $scope.formUpdateVoucher.status = 4
        } else {
            $scope.formUpdateVoucher.status = 0
        }

        $scope.entitiesCustomerTypeUpdate = $scope.customerTypes.filter(ct => ct.selected)
        console.log($scope.formInputVoucher.applyfor);

        console.log($scope.entitiesCustomerTypeUpdate);
        if($scope.entitiesCustomerTypeUpdate.length == 0 && $scope.formUpdateVoucher.applyfor == 2){
            return
        }

        if ($scope.formUpdateVoucher.applyfor == 0) {
            $scope.entitiesCustomerTypeUpdate = [];
        }

        let data = angular.copy($scope.formUpdateVoucher)
        let dataUpdate = {
            voucher: angular.copy($scope.formUpdateVoucher),
            customerTypeList: $scope.entitiesCustomerTypeUpdate
        }
        console.log(data);
        $http.put(apiVoucher + "/updateVoucher/" + id, dataUpdate).then(function (res) {
            $('#updateVoucherModal').modal('hide');
            $scope.showSuccess("Cập nhật thành công")

            $scope.getVouchers(0);
        })
    }

    //#region thong kê voucher
    $scope.staticVoucher = {}
    $scope.statVoucher = {}
    $scope.voucherInfo = []

    $scope.getStaticVoucher = function (voucher) {
        let v = angular.copy(voucher)
        console.log(voucher);
        $http.get(apiVoucher + "/" + v.id + "/statistics").then(function (res) {
            $scope.staticVoucher = res.data
        });
        $http.get(apiVoucher + "/" + v.id + "/stats").then(function (res) {
            $scope.statVoucher = res.data.content
            console.log(res.data);
        });
        $scope.voucherInfo = v
    };
    //#endregion

    //reset form add
    $scope.resetFormAdd = function () {
        $scope.formInputVoucher = {}
        $scope.formInputVoucher.name = null
        $scope.formInputVoucher.discountType = 1
        $scope.formInputVoucher.applyfor = 0

        $scope.validateValueError = false;
        $scope.validateAddMaxReVa = null;

        $scope.duplicateNameError = false;
        $scope.duplicateCodeError = false;
        $scope.codeLengthError = false;

        $scope.formAddVoucher.$setPristine();
        $scope.formAddVoucher.$setUntouched();

        $scope.selectedCustomerTypes = []

        $scope.resetCheckboxes()
    }

    $scope.resetFormUpdate = function () {
        $scope.formUpdateVoucher = {}

        $scope.validateValueErrorUpdate = false;
        $scope.validateAddMaxReVaUpdate = null;

        $scope.duplicateUpdateNameError = false;
        $scope.duplicateUpdateCodeError = false;

        $scope.formEditVoucher.$setPristine();
        $scope.formEditVoucher.$setUntouched();

    }

    //ham chuyen tieng viet co dau sang khong dau
    function toLowerCaseNonAccentVietnamese(str) {
        str = str.toLowerCase();
        str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
        str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
        str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
        str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
        str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
        str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
        str = str.replace(/đ/g, "d");
        // Some system encode vietnamese combining accent as individual utf-8 characters
        str = str.replace(/\u0300|\u0301|\u0303|\u0309|\u0323/g, ""); // Huyền sắc hỏi ngã nặng 
        str = str.replace(/\u02C6|\u0306|\u031B/g, ""); // Â, Ê, Ă, Ơ, Ư
        return str;
    }

    var now = new Date();
    var year = now.getFullYear();
    var month = String(now.getMonth() + 1).padStart(2, '0');
    var day = String(now.getDate()).padStart(2, '0');
    var hours = String(now.getHours()).padStart(2, '0');
    var minutes = String(now.getMinutes()).padStart(2, '0');

    $scope.currentDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;

    // PAGINATION, FILTER VOUCHER
    //#region
    $scope.filters = {
        code: null,
        name: null,
        applyfor: null,
        discountType: null,
        startDate: null,
        endDate: null,
        status: null
    };

    $scope.filtervouchers = [];
    $scope.totalPages = 0;
    $scope.currentPage = 0;
    $scope.desiredPage = 1;

    $scope.getVouchers = function (pageNumber) {
        if ($scope.filters.name) {
            $scope.filters.name = toLowerCaseNonAccentVietnamese($scope.filters.name.toLowerCase());
        }

        let keyword = $scope.searchKeyword || null;
        $scope.filters.code = keyword;
        $scope.filters.name = keyword;

        params = angular.extend({ pageNumber: pageNumber }, $scope.filters);


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
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };

    $scope.clearKeyword = function () {
        $scope.searchKeyword = null;
        // $scope.getVouchers(0);
    };

    $scope.resetFilters = function () {
        $scope.searchKeyword = null;
        $scope.filters = {
            name: null,
            code: null,
            applyfor: null,
            discountType: null,
            startDate: null,
            endDate: null,
            status: null
        };
        $scope.getVouchers(0);
    };

    // Initial load
    $scope.getVouchers(0);
    //#endregion

    //#region CHECK BOX CUSTOMER TYPE
    $scope.toggleSelectAll = function () {
        var selectAllStatus = !$scope.allChecked;
        $scope.customerTypes.forEach(function (type) {
            type.selected = selectAllStatus;
        });
        $scope.allChecked = selectAllStatus;
    };

    $scope.updateSelectAll = function () {
        $scope.allChecked = $scope.customerTypes.every(function (type) {
            return type.selected;
        });
    };

    $scope.logCheckedCheckboxes = function () {
        const checkedCheckboxes = $scope.customerTypes
            .filter(ct => ct.selected)
        // .map(ct => ct.id);
        console.log(checkedCheckboxes);
    };

    $scope.resetCheckboxes = function () {
        $scope.customerTypes.forEach(function (type) {
            type.selected = false;
        });
        $scope.allChecked = false;
    };

    $scope.checkCheckboxes = function (ids) {
        $scope.customerTypes.forEach(function (type) {
            type.selected = ids.includes(type.id);
        });
        $scope.updateSelectAll();
    };
    //#endregion

});

app.filter('ceil', ['$filter', function ($filter) {
    return function (input) {
        if (isNaN(input)) return input;
        return $filter('number')(parseInt(Math.ceil(input), 0), 0);
    };
}]);
app.filter('toInt', function () {
    return function (input) {
        if (isNaN(input)) return input;
        return parseInt(input, 10);
    };
});