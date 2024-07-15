app.controller("nguyen-voucher-ctrl", function ($scope, $http, $timeout) {

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
    const apiCustomerType = "http://localhost:8080/api/admin/customerTypeN"
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

    //#region CUSTOMER logic
    // Load selected customers from local storage
    localStorage.setItem('selectedCustomers', []);
    $scope.selectedCustomers = JSON.parse(localStorage.getItem('selectedCustomers') || '{}');

    let allCustomers = [];

    // Fetch all customers from the API and store them in allCustomers
    $http.get('http://localhost:8080/api/admin/customerN/all').then(function (res) {
        allCustomers = res.data || []; // Ensure allCustomers is an empty array if res.data is null or undefined
        console.log(allCustomers);

        // Apply saved selections after fetching all customers
        $scope.applySavedSelections();

        // Load and select customers from the voucher API
        // $scope.loadAndSelectCustomersFromAPI();
    }).catch(function (error) {
        console.error('Error fetching all customers:', error);
    });

    $scope.listCustomer = [];

    // Load selected customers from local storage
    $scope.selectedCustomers = JSON.parse(localStorage.getItem('selectedCustomers') || '{}');

    // $scope.selectAll = false;

    // $scope.toggleAll = function () {
    //     $scope.selectAll = !$scope.selectAll;
    //     angular.forEach(allCustomers, function (customer) {
    //         customer.selected = $scope.selectAll;
    //         $scope.updateSelectedCustomers(customer);
    //     });
    //     $scope.saveSelections(); // Save selections
    // };

    $scope.submitSelected = function () {
        var selectedCustomers = Object.keys($scope.selectedCustomers).map(function (id) {
            return $scope.selectedCustomers[id];
        });
        console.log('Selected Customers:', selectedCustomers);
    };

    $scope.updateSelectedCustomers = function (customer) {
        if (customer.selected) {
            $scope.selectedCustomers[customer.id] = customer;
        } else {
            delete $scope.selectedCustomers[customer.id];
        }
        $scope.saveSelections();
    };

    $scope.saveSelections = function () {
        localStorage.setItem('selectedCustomers', JSON.stringify($scope.selectedCustomers));
    };

    $scope.applySavedSelections = function () {
        angular.forEach($scope.customers, function (customer) {
            customer.selected = !!$scope.selectedCustomers[customer.id];
        });
    };

    $scope.$watch('customers', function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.applySavedSelections();
        }
    });

    // Function to fetch customers from the voucher API and mark them as selected
    $scope.loadAndSelectCustomersFromAPI = function (id) {
        $http.get(apiCustomerVoucher + "/voucher/" + id + "/customers")
            .then(function (res) {
                var apiCustomers = (res.data || []).map(function (object) {
                    return object.id;
                });

                angular.forEach(allCustomers, function (customer) {
                    if (apiCustomers.includes(customer.id)) {
                        customer.selected = true;
                        $scope.updateSelectedCustomers(customer);
                    }
                });

                $scope.saveSelections(); // Save selections
            })
            .catch(function (error) {
                console.error('Error fetching customers from voucher API:', error);
            });
    };

    // Make sure to apply saved selections whenever the customers data is updated
    $scope.$watch(function () {
        return allCustomers;
    }, function (newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.applySavedSelections();
        }
    }, true);

    // Ensure saved selections are applied on initial load
    $scope.applySavedSelections();

    //#endregion

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
        console.log($scope.validateAddMaxReVa);
    }

    $scope.validateAddValue = function () {
        if ($scope.formInputVoucher.value > 100 && $scope.formInputVoucher.discountType == 1) {
            $scope.formInputVoucher.value = 100
        }
        if ($scope.formInputVoucher.value < 1 && $scope.formInputVoucher.discountType == 1) {
            $scope.formInputVoucher.value = 1
        }
        // if( value < 1000  && $scope.formInputVoucher.discountType == 1){
        //     $scope.formInputVoucher.value == 1000
        // }
    };

    //chuyen doi gia tri cua MaximumValueReduce tranh loi luc validation *ADD*
    $scope.discountTypeChangeAdd = function () {
        if ($scope.formInputVoucher.discountType == 1) {
            $scope.formInputVoucher.maximumReductionValue = null
        }
        if ($scope.formInputVoucher.discountType == 2) {
            $scope.formInputVoucher.maximumReductionValue = 0
        }
        $scope.validateAddValue()
    }

    //#endregion

    $scope.listCustomerAdd = []

    //ADD VOUCHER ***************
    $scope.addVoucher = function () {

        $scope.checkValidateAllValue()

        if ($scope.duplicateNameError || $scope.duplicateCodeError || validateValueError) {
            return;
        }

        $scope.listCustomerAdd = Object.keys($scope.selectedCustomers).map(function (id) {
            return $scope.selectedCustomers[id];
        });

        let entitiesCustomerType = $scope.customerTypes.filter(ct => ct.selected)
        console.log($scope.formInputVoucher.applyfor);

        if ($scope.formInputVoucher.applyfor == 0) {
            $scope.listCustomerAdd = [];
            entitiesCustomerType = [];
        }
        if ($scope.formInputVoucher.applyfor == 1) {
            $scope.listCustomerAdd = []
        }
        if ($scope.formInputVoucher.applyfor == 2) {
            entitiesCustomerType = []
        }

        console.log(entitiesCustomerType);

        let data = {
            voucher: $scope.formInputVoucher, customerTypeList: entitiesCustomerType,
            customerList: $scope.listCustomerAdd
        }
        console.log(data)

        $http.post(apiVoucher + "/saveVoucher", data).then(function (res) {
            $('#addVoucherModal').modal('hide');
            $scope.showSuccess("Thêm thành công")

            $scope.getVouchers(0);
        })

        $scope.resetFormAdd()
    }

    //DISABLE FORM UPDATE
    $scope.setDisableUpdateForm = function (bool) {
        document.getElementById("updateCode").disabled = true;
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
        document.getElementById("flexRadioUpdate0").disabled = bool;
        document.getElementById("flexRadioUpdate1").disabled = bool;
        document.getElementById("flexRadioUpdate2").disabled = bool;

        //check status TAM NGUNG
        if (bool) {
            document.getElementById("checkPause").style.display = "none";
        } else {
            document.getElementById("checkPause").style.display = "block";
        }
    }


    // $scope.loadAndSelectCustomersFromAPI = function () {
    //     let allCustomers = []
    //     $http.get('http://localhost:8080/api/admin/customerN/all').then(function (res) {
    //         allCustomers = res.data
    //         console.log(allCustomers);
    //     })
    //     $http.get(apiCustomerVoucher + "/voucher/" + $scope.currentVoucher.id + "/customers")
    //         .then(function (res) {
    //             var apiCustomers = res.data.map(function (object) {
    //                 return object.id;
    //             });

    //             angular.forEach(allCustomers, function (customer) {
    //                 if (apiCustomers.includes(customer.id)) {
    //                     customer.selected = true;
    //                     $scope.updateSelectedCustomers(customer);
    //                 }
    //             });

    //             $scope.saveSelections();
    //         })
    //         .catch(function (error) {
    //             console.error('Error fetching customers from API:', error);
    //         });
    // };

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

        localStorage.setItem('selectedCustomers', []);
        $scope.loadAndSelectCustomersFromAPI($scope.currentVoucher.id);
        $scope.selectedCustomers = JSON.parse(localStorage.getItem('selectedCustomers') || '{}');
        $scope.applySavedSelections();
        $scope.getCustomers(0)

    };

    // Call getAllCustomerType on load
    $scope.getAllCustomerType();

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

    $scope.listCustomerUpdate = []

    //UPDATE VOUCHER ************
    $scope.updateVoucher = function (id) {

        if ($scope.duplicateUpdateNameError || $scope.duplicateUpdateCodeError) {
            return;
        }

        // let entitiesCustomerType = []

        $scope.listCustomerUpdate = Object.keys($scope.selectedCustomers).map(function (id) {
            return $scope.selectedCustomers[id];
        });
        if (checkPauseStatus.checked) {
            $scope.formUpdateVoucher.status = 4
        } else {
            $scope.formUpdateVoucher.status = 0
        }

        let entitiesCustomerType = $scope.customerTypes.filter(ct => ct.selected)
        console.log($scope.formInputVoucher.applyfor);

        console.log(entitiesCustomerType);

        if ($scope.formUpdateVoucher.applyfor == 0) {
            $scope.listCustomerUpdate = [];
            entitiesCustomerType = [];
        }
        if ($scope.formUpdateVoucher.applyfor == 1) {
            $scope.listCustomerUpdate = []
        }
        if ($scope.formUpdateVoucher.applyfor == 2) {
            entitiesCustomerType = []
        }

        let data = angular.copy($scope.formUpdateVoucher)
        let dataUpdate = {
            voucher: angular.copy($scope.formUpdateVoucher),
            customerTypeList: entitiesCustomerType,
            customerList: $scope.listCustomerUpdate
        }
        console.log(data);
        $http.put(apiVoucher + "/updateVoucher/" + id, dataUpdate).then(function (res) {
            $('#updateVoucherModal').modal('hide');
            $scope.showSuccess("Cập nhật thành công")

            $scope.getVouchers(0);
        })
    }

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

    //reset form add
    $scope.resetFormAdd = function () {
        $scope.formInputVoucher = {}
        $scope.formInputVoucher.name = null
        $scope.formInputVoucher.discountType = 1
        $scope.formInputVoucher.applyfor = 0

        localStorage.setItem('selectedCustomers', []);
        $scope.selectedCustomers = JSON.parse(localStorage.getItem('selectedCustomers') || '{}');
        $scope.applySavedSelections();

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

        localStorage.setItem('selectedCustomers', []);
        $scope.selectedCustomers = JSON.parse(localStorage.getItem('selectedCustomers') || '{}');
        $scope.applySavedSelections();

        $scope.duplicateUpdateNameError = false;
        $scope.duplicateUpdateCodeError = false;

        $scope.formUpdateVoucher.$setPristine();
        $scope.formUpdateVoucher.$setUntouched();

        $scope.getCustomers(0);

        localStorage.setItem('selectedCustomers', []);
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

    // pagination and filter voucher *************
    //#region 
    // $scope.filterVoucher = function () {
    //     $http.get(apiVoucher + "/all").then(function (res) {
    //         $scope.vouchers = res.data;
    //     })
    // }

    // $scope.filtervouchers = [];
    // $scope.totalPages = 0;
    // $scope.currentPage = 0;
    // $scope.desiredPage = 1;
    // $scope.filters = {
    //     name: null,
    //     code: null,
    //     discountType: null,
    //     startDate: null,
    //     endDate: null,
    //     status: null
    // };

    // $scope.getVouchers = function (pageNumber) {
    //     // let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
    //     // $http.get('http://localhost:8080/api/admin/voucher/page', { params: params }).then(function(response) {
    //     //     $scope.filtervouchers = response.data.content;
    //     //     $scope.totalPages = response.data.totalPages;
    //     //     $scope.currentPage = pageNumber;
    //     // });

    //     if ($scope.filters.name != null) {
    //         $scope.filters.name = toLowerCaseNonAccentVietnamese($scope.filters.name.toLowerCase())
    //     }

    //     let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
    //     $http.get('http://localhost:8080/api/admin/voucher/page', { params: params }).then(function (response) {
    //         $scope.filtervouchers = response.data.content;
    //         $scope.totalPages = response.data.totalPages;
    //         $scope.currentPage = pageNumber;
    //         $scope.desiredPage = pageNumber + 1;
    //     });
    // };

    // $scope.applyFilters = function () {
    //     $scope.getVouchers(0);
    // };

    // $scope.goToPage = function () {
    //     let pageNumber = $scope.desiredPage - 1;
    //     if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
    //         $scope.getVouchers(pageNumber);
    //     } else {
    //         // Reset desiredPage to currentPage if the input is invalid
    //         $scope.desiredPage = $scope.currentPage + 1;
    //     }
    // };
    // // Initial load
    // $scope.getVouchers(0);

    // $scope.search = function (code1) {
    //     $scope.filters.code = code1

    //     let pageNumber = 0

    //     let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
    //     $http.get('http://localhost:8080/api/admin/voucher/page', { params: params }).then(function (response) {
    //         $scope.filtervouchers = response.data.content;
    //         $scope.totalPages = response.data.totalPages;
    //         $scope.currentPage = pageNumber;
    //         $scope.desiredPage = pageNumber + 1;
    //     });
    // };

    // $scope.clearSearch = function () {
    //     $scope.filters = {
    //         name: null,
    //         code: null,
    //         discountType: null,
    //         startDate: null,
    //         endDate: null,
    //         status: null
    //     };
    //     $scope.search();
    //     $scope.getVouchers(0);
    // };

    // $scope.resetFilters = function () {
    //     $scope.filters = {
    //         name: null,
    //         code: null,
    //         discountType: null,
    //         startDate: null,
    //         endDate: null,
    //         status: null
    //     };
    //     $scope.getVouchers(0);
    // }

    var now = new Date();
    var year = now.getFullYear();
    var month = String(now.getMonth() + 1).padStart(2, '0');
    var day = String(now.getDate()).padStart(2, '0');
    var hours = String(now.getHours()).padStart(2, '0');
    var minutes = String(now.getMinutes()).padStart(2, '0');

    $scope.currentDateTime = year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
    //#endregion


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

    // $scope.searchByKeyword = function() {
    //     let keyword = $scope.searchKeyword || null;
    //     $scope.filters.code = keyword;
    //     $scope.filters.name = keyword;
    //     $scope.filters.applyfor = null;
    //     $scope.filters.discountType = null;
    //     $scope.filters.startDate = null;
    //     $scope.filters.endDate = null;
    //     $scope.filters.status = null;

    //     $scope.getVouchers(0, 1);
    // };

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

    // PAGINATION, FILTER CUSTOMER
    //#region
    $scope.customers = [];
    $scope.totalPagesCustomer = 0;
    $scope.currentPageCustomer = 0;
    $scope.desiredPageCustomer = 1;
    $scope.filtersCustomer = {
        fullName: null,
        phoneNumber: null,
        email: null,
        customerTypeId: null
    };

    $scope.getCustomers = function (pageNumber) {
        var params = {
            pageNumber: pageNumber,
            fullName: $scope.filtersCustomer.fullName,
            phoneNumber: $scope.filtersCustomer.phoneNumber,
            email: $scope.filtersCustomer.email,
            customerTypeId: $scope.filtersCustomer.customerTypeId
        };

        $http.get('http://localhost:8080/api/admin/customerN/page', { params: params })
            .then(function (response) {
                $scope.customers = response.data.content;
                $scope.totalPagesCustomer = response.data.totalPages;
                $scope.currentPageCustomer = response.data.number;
                $scope.desiredPageCustomer = response.data.number + 1;
                $scope.applySavedSelections(); // Apply saved selections
            })
            .catch(function (error) {
                console.error('Error fetching customers:', error);
            });
    };

    $scope.applyFiltersCustomer = function () {
        $scope.getCustomers(0);
    };

    $scope.goToPageCustomer = function () {
        var pageNumber = $scope.desiredPageCustomer - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPagesCustomer) {
            $scope.getCustomers(pageNumber);
        } else {
            $scope.desiredPageCustomer = $scope.currentPageCustomer + 1;
        }
    };

    // Initial load
    $scope.getCustomers(0);

    $scope.resetFiltersCustomer = function () {
        $scope.filtersCustomer = {
            fullName: null,
            phoneNumber: null,
            email: null,
            customerTypeId: null
        };
        $scope.getCustomers(0);
    };
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