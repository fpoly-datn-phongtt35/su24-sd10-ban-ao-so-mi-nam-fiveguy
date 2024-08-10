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
    
    const inputElementCustomer = document.getElementById('search-customer');
    const hiddenElementCustomer = document.getElementById('customer-list');
    
    inputElementCustomer.addEventListener('input', function() {
        hiddenElementCustomer.style.display = 'block';
    });
    
    document.addEventListener('click', function(event) {
        if (!inputElementCustomer.contains(event.target) && !hiddenElementCustomer.contains(event.target)) {
            hiddenElementCustomer.style.display = 'none';
        }
    });
    

    $scope.customer = {gender: true, addresses: []};
    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.totalQuantity = 0;
    $scope.timeCurrent = new Date();

    $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', {headers: config.headers})
    .then(function(response) {
        $scope.provinces = response.data.data;
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
        $scope.provinceCode = e.params.data.id;
        $scope.provinceValue = e.params.data.text;
        if ($scope.provinceCode) {
            $scope.loadDistricts($scope.provinceCode, '.district');
        } 
    });

      // Load districts by province ID
      $scope.loadDistricts = function(provinceId, cls) {
        $(cls).prop("disabled", true);
        return  $http.get(`https://online-gateway.ghn.vn/shiip/public-api/master-data/district`,  {
            params: {
              province_id: provinceId
            },
            headers: config.headers
          })
                .then(function(response) {
                    $scope.districts = response.data.data;
                    $(cls).prop("disabled", false);
                 })
                .catch(function(error) {
                    console.error('Lỗi khi gọi API:', error);
                });
    };

    // Load wards by district ID
    $scope.loadWards = function(districtId, cls) {
        $(cls).prop("disabled", true);
        return $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/ward', {
            params: {
                district_id: districtId
            },
            headers: config.headers
        })
        .then(function(response) {
            $scope.wards = response.data.data;
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
        $scope.districtCode = e.params.data.id;
        $scope.districtValue = e.params.data.text;
        if ($scope.districtCode) {
            $scope.loadWards($scope.districtCode, '.ward');
        }
    });


    $('.ward').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-ward"),
    }).on("select2:select", function (e) { 
        $scope.wardValue = e.params.data.text;
        $scope.wardCode = e.params.data.id;
        $scope.$apply();
    });

    $scope.getBills = () => {
        $http.get(`${config.host}/bill-th`).then(resp => {
            $scope.bills = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.getTotalQuantity = () => {
        $scope.totalQuantity =  $scope.selectedBill.billDetail.reduce((total, detail) => {
            return total + detail.quantity;
        }, 0);
    }

    $scope.getBill = (id) => {
        if (!$scope.selectedBill || $scope.selectedBill.id != id) {
            $http.get(`${config.host}/bill-th/${id}`).then(resp => {
                $scope.selectedBill = resp.data;
                $scope.getTotalQuantity();
            }).catch(error => {
                console.log("Error", error);
            });
        }
        
    }

    $scope.getBills();

    $scope.addBill = () => {
        $http.post(`${config.host}/bill-th`).then(resp => {
            $scope.bills.push(resp.data);
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.apiRemoveBill = () => {
        $http.delete(`${config.host}/bill-th/delete-bill/${$scope.selectedBill.id}`).then(resp => {
            $scope.selectedBill = null;
            $('#deleteBill').modal('hide');
            toastr["success"]("Xóa " + resp.data.code + " thành công");
            $scope.getBills();
            $scope.totalQuantity = 0;
        }).catch(error => {
            $('#deleteBill').modal('hide');
            console.log("Error", error);
        });
    }

    $scope.removeBill = (bill) => {
        $scope.selectedBill = bill;
        if (bill.billDetail.length > 0) {
            $('#deleteBill').modal('show');
            return;
        }
        $scope.apiRemoveBill();
    }
    
    $scope.loading = false;

    $scope.addProductCart = (productDetail) => {
        if ($scope.selectedBill == null) {
            toastr["warning"]("Vui lòng chọn hóa đơn");
            return;
        }
        if ($scope.loading) return; 
        $scope.loading = true; 
    
        $http.put(`${config.host}/bill-th/add-cart/${productDetail.id}`, $scope.selectedBill)
            .then(resp => {
                $scope.getBills();
                $scope.selectedBill = resp.data;
                $scope.getTotalQuantity();
                toastr["success"]("Thêm " + productDetail.product.name + " " + productDetail.color.name + " vào giỏ hàng thành công");
            })
            .catch(error => {
                if (error.status === 400 && error.data.alert) {
                    toastr["error"](error.data.alert);
                } else {
                    console.log("Error", error);
                }
            })
            .finally(() => {
                $scope.loading = false; 
            });
    };

    $scope.addQuantity = (productDetail) => {
        $scope.add = true;
        $http.put(`${config.host}/bill-th/add-cart/${productDetail.id}`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
            $scope.getTotalQuantity();
            $scope.add = false;
        }).catch(error => {
            if (error.status === 400) {
                if (error.data.alert) {
                    toastr["error"](error.data.alert);
                }
            } else {
                console.log("Error", error);
            }
            $scope.add = false;
        });
    }

    $scope.removeQuantity = (productDetail) => {
        $scope.remove = true;
        $http.put(`${config.host}/bill-th/remove-cart/${productDetail.id}`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
            $scope.getTotalQuantity();
            $scope.remove = false;
        }).catch(error => {
            if (error.status === 400) {
                if (error.data.alert) {
                    toastr["error"](error.data.alert);
                }
            } else {
                console.log("Error", error);
            }
            $scope.remove = false;

        });
    }

    $scope.changeQuantity = (item) => {
        if (typeof item.quantity !== 'number' || !Number.isInteger(item.quantity) || item.quantity < 1) {
            item.quantity = 1;
        }
        $http.put(`${config.host}/bill-th/update-cart/${item.productDetail.id}?updateQty=${item.quantity}`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
            $scope.getTotalQuantity();
        }).catch(error => {
            item.quantity = item.originalQuantity;

            if (error.status === 400) {
                if (error.data.alert) {
                    toastr["error"](error.data.alert);
                }
            } else {
                console.log("Error", error);
            }

        });
    }

    $scope.deletePDCart = (productDetail) => {
        $http.put(`${config.host}/bill-th/delete-cart/${productDetail.id}`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
            $scope.getTotalQuantity();
        }).catch(error => {
            if (error.status === 400) {
                if (error.data.alert) {
                    toastr["error"](error.data.alert);
                }
            } else {
                console.log("Error", error);
            }
        });
    }

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

    let debounceTimerCustomer;

    $scope.debounceSearchCustomer = () => {
        if (debounceTimerCustomer) {
            clearTimeout(debounceTimerCustomer);
        }
    
        $scope.customers = [];
        const keyword = $scope.keywordCustomer?.trim();
    
        if (!keyword) {
            toggleDisplay('#loading-customer', false);
            toggleDisplay('#null-customer', true);
            return;
        }
    
        toggleDisplay('#loading-customer', true);
        toggleDisplay('#null-customer', false);
    
        debounceTimerCustomer = setTimeout($scope.searchCustomers, 2000);
    };
    
    $scope.searchCustomers = () => {
        $http.get(`${config.host}/customer-th`, { params: { keyword: $scope.keywordCustomer } })
            .then(resp => {
                toggleDisplay('#loading-customer', false);
                $scope.customers = resp.data;
                toggleDisplay('#null-customer', resp.data.length === 0);
            })
            .catch(error => {
                toggleDisplay('#loading-customer', false);
                console.error("Error", error);
            });
    };
    
    function toggleDisplay(selector, show) {
        $(selector).css('display', show ? 'flex' : 'none');
    }

    function isImage(file) {
        return file.name.match(/\.(jpg|jpeg|png|gif|bmp)$/);
    }
    $scope.uploadFile = (event) => {
        let image = event.target.files[0]; 
    
        if (!isImage(image)) {
            toastr.error(image.name + " không đúng định dạng hình ảnh");
            return;
        }
    
        if (image.size > 10048576) {
            toastr.warning(image.name + " có kích thước lớn hơn 10MB");
            return;
        }
    
        let reader = new FileReader();
        reader.onload = function (e) {
            $scope.$apply(function () {
                $scope.customer.avatar = e.target.result;
            });
        };
    
        reader.readAsDataURL(image);
    };

    $scope.showCustomerUpdate = () => {
        $http.get(`${config.host}/customer-th/${$scope.selectedBill.customer.id}`).then(resp => {
            $scope.customerUpdate = resp.data;    
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.validateCustomer = (provice, district, ward) => {
        $scope.errors = [];
        if ($scope.customer.birthDate) {
            var birthDate = new Date($scope.customer.birthDate);
            if (birthDate.getFullYear() < 1900) {
                    $scope.errors.birthDate = "Năm sinh phải lớn hơn hoặc bằng 1900";
                    $scope.customer.birthDate = null;
                    return false;
            } 
        }
        if ($scope.phoneNumber) {
            var phoneNumberPattern = /^\d{10,11}$/;

            if (!$scope.phoneNumber.match(phoneNumberPattern)) {
                $scope.errors.phoneNumber = "Số điện thoại không hợp lệ, phải có 10 hoặc 11 chữ số.";
                return false;
            }
        }
        if (provice == undefined) {
            $scope.errors.provice = "Vui lòng chọn tỉnh thành";
            return false;
        } else if (district == "") {
            $scope.errors.district = "Vui lòng chọn quận/huyện";
            return false;
        }  else if (ward == "") {
            $scope.errors.ward = "Vui lòng chọn phường/xã";
            return false;
        } 
        return true;
    }

    $scope.updateBill = () => {
        $http.put(`${config.host}/bill-th`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.setCustomerBill = (customer) => {
        if ($scope.selectedBill == null) {
            toastr["warning"]("Vui lòng chọn hóa đơn");
            return;
        }
        $scope.selectedBill.customer = customer;
        $scope.updateBill();
        hiddenElementCustomer.style.display = 'none';
    }

    $scope.removeCustomer = () => {
        $scope.selectedBill.customer = null;
        $scope.updateBill();
    }

    $scope.createCustomer = () => {
        if ($scope.customerForm.$valid && $scope.validateCustomer($scope.provinceValue, $scope.districtValue, $scope.wardValue)) {
            $scope.customer.addresses.push({
                name: $scope.addressDetail,
                addressId: `${$scope.wardCode}, ${$scope.districtCode}, ${$scope.provinceCode}`,
                address: `${$scope.wardValue}, ${$scope.districtValue}, ${$scope.provinceValue}`,
                phoneNumber: $scope.phoneNumber,
                defaultAddress: true
            })
            $http.post(`${config.host}/customer-th`, $scope.customer).then(resp => {
                toastr["success"]("Thêm mới khách hàng " + resp.data.fullName + " thành công");
                $('#customerModal').modal('hide');
                if ($scope.selectedBill) {
                    $scope.selectedBill.customer = resp.data;
                    $scope.updateBill();
                }               
            })
        }
    }
  
});

app.directive('customOnChange', function() {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        var onChangeHandler = scope.$eval(attrs.customOnChange);
        element.on('change', onChangeHandler);
        element.on('$destroy', function() {
          element.off();
        });
  
      }
    };
  });