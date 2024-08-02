app.controller("SellQuicklyController", function($scope, $http){
    const inputElement = document.getElementById('search-product');
    const hiddenElement = document.getElementById('item-list');
    
    inputElement.addEventListener('input', function() {
        hiddenElement.style.display = 'block';
    });
    
    document.addEventListener('click', function(event) {
        if (!inputElement.contains(event.target) && !hiddenElement.contains(event.target)) {
            hiddenElement.style.display = 'none';
        }
    });
    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.customer = {gender: true};
    $scope.timeCurrent = new Date();

    $http.get('https://vapi.vnappmob.com/api/province/')
    .then(function(response) {
        $scope.provinces = response.data.results;
    })
    .catch(function(error) {
        console.error('Lỗi khi gọi API:', error);
    });

    $('.province').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-province")
    }).on("select2:select", function (e) { 
        $scope.districts = [];
        $scope.districtValue = "";
        $scope.wards = [];
        $scope.wardValue = "";
        let provinceCode = e.params.data.id;
        $scope.provinceValue = e.params.data.text;
        if (provinceCode) {
            $scope.loadDistricts(provinceCode, '.district');
        } 
    });

      // Load districts by province ID
      $scope.loadDistricts = function(provinceId, cls) {
        $(cls).prop("disabled", true);
        return  $http.get(`https://vapi.vnappmob.com/api/province/district/${provinceId}`)
                .then(function(response) {
                    $scope.districts = response.data.results;
                    $(cls).prop("disabled", false);
                 })
                .catch(function(error) {
                    console.error('Lỗi khi gọi API:', error);
                });
    };

    // Load wards by district ID
    $scope.loadWards = function(districtId, cls) {
        $(cls).prop("disabled", true);
        return $http.get(`https://vapi.vnappmob.com/api/province/ward/${districtId}`)
        .then(function(response) {
            $scope.wards = response.data.results;
            $(cls).prop("disabled", false);
        })
        .catch(function(error) {
            console.error('Lỗi khi gọi API:', error);
        });
    };

    $('.district').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-district")
    }).on("select2:select", function (e) { 
        $scope.wards = [];
        $scope.wardValue = "";
        let districtCode = e.params.data.id;
        $scope.districtValue = e.params.data.text;
        if (districtCode) {
            $scope.loadWards(districtCode, '.ward');
        }
    });


    $('.ward').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-ward"),
    }).on("select2:select", function (e) { 
        $scope.wardValue = e.params.data.text;
        $scope.$apply();
    });

    $scope.bills = JSON.parse(localStorage.getItem("bills")) || [{
        code: 'HD' + Number(String(new Date().getTime()).slice(-6)),
            createdAt: new Date(),
            itemQty: 0,
            moneyReturn: 0,
            customerPay: 0,
            totalCustomerPay: 0,
            totalAmount: 0,
            reciverName: null,
            deliveryDate: new Date(),
            shippingFee: 0,
            phoneNumber: null,
            note: null,
            status: 1,
            voucher: null,
            typeBill: 1,
            cart: [],
            customer: {},
    }];
     $('#selectCustomer').select2({
    })
    $scope.selectedBill = $scope.bills[0];
    $scope.removeBill = index => {
        if ($scope.bills.length == 1) return;
        $scope.bills.splice(index, 1);
        $scope.selectedBill = null;
        localStorage.setItem("bills", JSON.stringify($scope.bills));
    };
    function makeBill() {
        return {
            code: 'HD' + Number(String(new Date().getTime()).slice(-6)),
            createdAt: new Date(),
            itemQty: 0,
            moneyReturn: 0,
            customerPay: 0,
            totalCustomerPay: 0,
            totalAmount: 0,
            reciverName: null,
            deliveryDate: new Date(),
            shippingFee: 0,
            phoneNumber: null,
            note: null,
            status: 1,
            voucher: null,
            typeBill: 1,
            cart: [],
            customer: {}
        }
    }

    $scope.addBill = () => {
        if ($scope.bills.length > 7) {
            toastr["warning"]("Chỉ được tạo tối đa 8 hóa đơn");
            return;
        }
        $scope.bills.push(makeBill());
        localStorage.setItem("bills", JSON.stringify($scope.bills));
    }

    $scope.setSelected = bill => {
        $scope.selectedBill = bill;
    }

    $scope

    let debounceTimer;

    $scope.debounceSearch = () => {
        if (debounceTimer) {
            clearTimeout(debounceTimer);
        }
        $scope.productDetails = [];
        if (!$scope.keyword || $scope.keyword.trim() === '') {
            $('#responseNull').css('display', 'flex');
            $('#loading').css('display', 'none');
            return;
        }
        $('#loading').css('display', 'flex');
        $('#responseNull').css('display', 'none');
        debounceTimer = setTimeout(() => {
            $scope.searchProducts();
        }, 2000);
    };

    $scope.searchProducts = () => {
            $http.get(`${config.host}/product-detail`, {params: {keyword: $scope.keyword}}).then(resp => {
                $('#loading').css('display', 'none');
                $scope.productDetails = resp.data;
                if (resp.data.length == 0)  
                $('#responseNull').css('display', 'flex');
                else {
                    $('#responseNull').css('display', 'none');
                }
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error);
            });
       
    }

    $scope.addProductCart = (id) => {
        if ($scope.selectedBill == null) {
            toastr["warning"]("Vui lòng chọn hóa đơn")
            return;
        }
        var item = $scope.selectedBill.cart.find(item => item.id == id);
        if (item) {
            item.qty++;
        } else {
            $http.get(`${config.host}/product-detail/one/` + id).then(resp => {
                resp.data.qty = 1;
                $scope.selectedBill.cart.push(resp.data);
            }).catch(error => {
                if (error.status === 400) {
                    if (error.data.alert) {
                        toastr["error"](error.data.alert);
                    }
                } 
            });
        }
    }

    
});