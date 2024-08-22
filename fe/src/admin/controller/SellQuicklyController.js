app.controller("SellQuicklyController", function($scope, $http, $filter, $timeout){
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
    
    $scope.excessMoney = 0;
    $scope.customer = {gender: true, addresses: []};
    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.totalQuantity = 0;
    $scope.timeCurrent = new Date();
    $scope.errors = [];
    $scope.defaultAddressUpdate = false;

    $scope.runCheckPaid = false;

    // Hàm kiểm tra thanh toán sử dụng Promise
    $scope.checkPaid = async function(price, content) {
        while (!$scope.runCheckPaid) { // Vòng lặp sẽ chạy liên tục cho đến khi isSuccess = true
            try {
                const response = await fetch("https://script.google.com/macros/s/AKfycbzdKN0wLHKHI2LLF85S30OHZIfAIgqxJ0v448_YI3807eXmakDh2KIb3Ev81l4nPfB7/exec");
                const data = await response.json();
                
                if (data && data.data && data.data.length > 0) {
                    const lastPaid = data.data[data.data.length - 1];
                    const lastPrice = lastPaid["Giá trị"];
                    const lastContent = lastPaid["Mô tả"];
                    
                    if (lastPrice >= price && lastContent.includes(content)) {
                        $scope.apiPayment();
                        $scope.qr = null;
                        $scope.runCheckPaid = true;
                        $('#qrModal').modal('hide'); // Đánh dấu là đã thành công để dừng kiểm tra
                    } 
                } else {
                    console.log("Dữ liệu thanh toán không hợp lệ");
                }
            } catch (error) {
                console.error("Lỗi khi kiểm tra thanh toán:", error);
            }

            // Chờ 1 giây trước khi kiểm tra lại
            await new Promise(resolve => setTimeout(resolve, 1000));
        }
    };

    $scope.changeRunCheckPaid = () => {
        $scope.runCheckPaid = true;
    }

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
        placeholder: $(this).data('placeholder')
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
        placeholder: $(this).data('placeholder')
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

    $scope.getPaymentMethods = () => {
        $http.get(`${config.host}/payment-method-th`).then(resp => {
            $scope.paymentMethods = resp.data;
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.getPaymentMethods();

    $scope.getTotalQuantity = () => {
        $scope.totalQuantity =  $scope.selectedBill.billDetail.reduce((total, detail) => {
            return total + detail.quantity;
        }, 0);
    }

   

    $scope.getBill = (id) => {
        if (!$scope.selectedBill || $scope.selectedBill.id != id) {
            $http.get(`${config.host}/bill-th/${id}`).then(resp => {
                $scope.selectedBill = resp.data;
                // $scope.clearInputPrice();

                if ($scope.selectedBill.typeBill == 1) {
                    $scope.isChecked = false

                } else {
                    $scope.isChecked = true
                }
                $scope.getTotalQuantity();
            $scope.showAddress($scope.selectedBill);
            
            $scope.showShippingFee($scope.selectedBill);
            $scope.clearInputPrice();
            $scope.keyword = '';
            $scope.keywordCustomer = '';
            }).catch(error => {
                console.log("Error", error);
            }).finally(() => {
                $scope.getVouchersForCustomer();
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

    $scope.loadingRemove = false;

    $scope.apiRemoveBill = () => {
        $scope.loadingRemove = true;
        $http.delete(`${config.host}/bill-th/delete-bill/${$scope.selectedBill.id}`).then(resp => {
            $('#deleteBill').modal('hide');
            toastr["success"]("Xóa " + resp.data.code + " thành công");
            $scope.getBills();
            $scope.totalQuantity = 0;
        }).catch(error => {
            $('#deleteBill').modal('hide');
            console.log("Error", error);
        }).finally(() => {
            $scope.selectedBill = null;
            $scope.selectedVoucher = null;
            $scope.loadingRemove = false; 
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

    function onScanSuccess(decodedText) {
        $http.get(`${config.host}/product-detail/barcode`, {params: {barcode: decodedText}}).then(resp => {
            if (!resp.data) {
                toastr["warning"]("Không tìm thấy sản phẩm, vui lòng kiểm tra lại!");
                return;
            } else {
                $scope.addProductCart(resp.data);
                $('#barcode').modal('hide');
                html5QrcodeScanner.clear();
            }
        }) .catch(error => {
            console.log("Error", error);
        })
       
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
                $scope.getVouchersForCustomer();
                $scope.loading = false; 
            });
    };

    $scope.addQuantity = (productDetail) => {
        $scope.add = true;
        $http.put(`${config.host}/bill-th/add-cart/${productDetail.id}`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
            $scope.getTotalQuantity();
            $scope.showShippingFee($scope.selectedBill);
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
        }).finally(() => {
            $scope.selectBestVoucher();  

        });
    }

    $scope.removeQuantity = (productDetail) => {
        $scope.remove = true;
        $http.put(`${config.host}/bill-th/remove-cart/${productDetail.id}`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
            $scope.getTotalQuantity();
            $scope.showShippingFee($scope.selectedBill);
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

        }).finally(() => {
            $scope.selectBestVoucher(); 
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
            $scope.showShippingFee($scope.selectedBill);
        }).catch(error => {
            item.quantity = item.originalQuantity;

            if (error.status === 400) {
                if (error.data.alert) {
                    toastr["error"](error.data.alert);
                }
            } else {
                console.log("Error", error);
            }
        }).finally(() => {
            $scope.selectBestVoucher();
        });
    }

    $scope.deletePDCart = (productDetail) => {
        $http.put(`${config.host}/bill-th/delete-cart/${productDetail.id}`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
            $scope.getTotalQuantity();
            $scope.showShippingFee($scope.selectedBill);
        }).catch(error => {
            if (error.status === 400) {
                if (error.data.alert) {
                    toastr["error"](error.data.alert);
                }
            } else {
                console.log("Error", error);
            }
        }).finally(() => {
            $scope.selectBestVoucher();  
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
        }, 1000);
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
    
        debounceTimerCustomer = setTimeout($scope.searchCustomers, 1000);
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

    $scope.uploadUpdateFile = (event) => {
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
                $scope.customerUpdate.avatar = e.target.result;
            });
        };
    
        reader.readAsDataURL(image);
    };


    $scope.showCustomerUpdate = () => {
        $http.get(`${config.host}/customer-th/${$scope.selectedBill.customer.id}`).then(resp => {
            $scope.customerUpdate = resp.data;    
            $scope.customerUpdate.birthDate = new Date(resp.data.birthDate)
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.validateAddress = (provice, district, ward) => {
        $scope.errors = [];
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
        if ($scope.phoneNumber) {
            var phoneNumberPattern = /^\d{10,11}$/;

            if (!$scope.phoneNumber.match(phoneNumberPattern)) {
                $scope.errors.phoneNumber = "Số điện thoại không hợp lệ, phải có 10 hoặc 11 chữ số.";
                return false;
            }
        }   
        return true;
    }

    $scope.validateCustomer = (provice, district, ward) => {
        $scope.validateAddress(provice, district, ward);
        if ($scope.customer.birthDate) {
            var birthDate = new Date($scope.customer.birthDate);
            if (birthDate.getFullYear() < 1900) {
                    $scope.errors.birthDate = "Năm sinh phải lớn hơn hoặc bằng 1900";
                    $scope.customer.birthDate = null;
                    return false;
            } 
        } 
    }

    $scope.updateBill = () => {
        $http.put(`${config.host}/bill-th`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = resp.data;
        }).catch(error => {
            console.log("Error", error);
        }).finally(() => {
            //   $scope.getVouchersForCustomer();
        });
    }

    $scope.changeInputPrice = function(value) {
        $scope.selectedBill.paidAmount = value;
        $scope.updateBill();
        
    }

    $scope.clearInputPrice = function() {
        $scope.inputP = null;
        $scope.excessMoney = null;
    };

    $scope.setCustomerBill = (customer) => {
        if ($scope.selectedBill == null) {
            toastr["warning"]("Vui lòng chọn hóa đơn");
            return;
        }
        $scope.selectedBill.customer = customer;
        $scope.updateBill();
        if ($scope.selectedBill.typeBill == 2) {
            $scope.showAddress($scope.selectedBill);
        }
        hiddenElementCustomer.style.display = 'none';
    }

    $scope.removeCustomer = () => {
        $scope.selectedBill.customer = null;
        $scope.updateBill();
    }

    $scope.createCustomer = () => {
        $('#addCustomer').css('display', 'none');
        $('#loadingAdd').css('display', 'inline-block');
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
                $scope.resetCustomer();        
                $('#addCustomer').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');   
            }).catch(error => {
                $('#addCustomer').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none'); 
                console.log("Error", error);
            });
        }
    }

    $scope.updateCustomer = () => {
        $('#updateCustomer').css('display', 'none');
        $('#loadingUpdate').css('display', 'inline-block');
        if ($scope.customerUpdateForm.$valid) {
            $http.put(`${config.host}/customer-th`, $scope.customerUpdate).then(resp => {
                toastr["success"]("Cập nhật khách hàng " + resp.data.fullName + " thành công");
                $('#customerUpdateModal').modal('hide');
                $scope.selectedBill.customer = resp.data;
                $scope.updateBill();
                $scope.resetCustomer();     
                $('#updateCustomer').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');   
            }).catch(error => {
                $('#addCustomer').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none'); 
                console.log("Error", error);
            });
        }
    }

    $scope.resetAddress = () => {
        $scope.idAddress = null;
        $scope.provinceCode = undefined;
        $scope.addressDetail = '';
        $scope.provinceValue = '';
        $scope.districtCode = '';
        $scope.districtValue = '';
        $scope.wardCode = '';
        $scope.wardValue = '';
        $scope.phoneNumber = '';
        $scope.defaultAddress = false;
        $scope.defaultAddressUpdate = false;
        $('.province').val(null).trigger('change');
        $('.district').val(null).trigger('change');
        $('.ward').val(null).trigger('change');

        $scope.shippingFee = 0; 
        $scope.reciverName= null;
    }

    $scope.resetCustomer = () => {
        $scope.customer = {gender: true, addresses: []};
        $scope.resetAddress();
        $scope.errors = [];
    }

    $scope.showUpdateAddress = (address) => {
        $scope.phoneNumber = address.phoneNumber;
        $scope.addressDetail = address.name;
        let [wardCode, districtCode, provinceCode] = address.addressId.split(', ').map(part => part.trim());
        let [wardValue, districtValue, provinceValue] = address.address.split(', ').map(part => part.trim());
        $('.province').val(provinceCode).trigger('change');
        $scope.provinceCode = provinceCode;
        $scope.provinceValue = provinceValue;
        $scope.loadDistricts(provinceCode, '.district').then(function() {
            $scope.districtCode = districtCode;
        });
        $scope.districtValue = districtValue;
        $scope.loadWards(districtCode, '.ward').then(() => {
            $scope.wardCode = wardCode;
        })
        $scope.wardValue = wardValue;
        $scope.defaultAddress = address.defaultAddress;
        $scope.defaultAddressUpdate = address.defaultAddress;
        $scope.idAddress = address.id;
    }

    $scope.addAddress = () => {
        if ($scope.addressAddForm.$valid && $scope.validateAddress($scope.provinceValue, $scope.districtValue, $scope.wardValue)) {
            if ($scope.defaultAddress) {
                $scope.customerUpdate.addresses.forEach(address => {
                    if (address.defaultAddress) {
                        address.defaultAddress = false;
                    }
                });
            }
            $scope.customerUpdate.addresses.push({
                name: $scope.addressDetail,
                addressId: `${$scope.wardCode}, ${$scope.districtCode}, ${$scope.provinceCode}`,
                address: `${$scope.wardValue}, ${$scope.districtValue}, ${$scope.provinceValue}`,
                phoneNumber: $scope.phoneNumber,
                defaultAddress: $scope.defaultAddress,
                status: 1,
                customer: {id: $scope.customerUpdate.id}
            })
            $http.post(`${config.host}/address-th`,  $scope.customerUpdate.addresses).then(resp => {
                $scope.customerUpdate.addresses = resp.data;
            
                $scope.resetAddress();
                toastr["success"]("Thêm địa chỉ thành công");
                $('#addressModal').modal('hide');
                $('#customerUpdateModal').modal('show');
            }).catch(error => {
                console.log("Error", error);
            });
        }
    }
    

    $scope.updateAddress = () => {
        let address = {
            id: $scope.idAddress,
            name: $scope.addressDetail,
            addressId: `${$scope.wardCode}, ${$scope.districtCode}, ${$scope.provinceCode}`,
            address: `${$scope.wardValue}, ${$scope.districtValue}, ${$scope.provinceValue}`,
            phoneNumber: $scope.phoneNumber
        }
        
        if ($scope.defaultAddressUpdate !== $scope.defaultAddress) {
            address.defaultAddress = $scope.defaultAddressUpdate;
        } else {
            address.defaultAddress = $scope.defaultAddress;
        }
        $http.put(`${config.host}/address-th`,  address).then(resp => {
            $scope.resetAddress();
            $scope.showCustomerUpdate();  
            toastr["success"]("Cập nhật địa chỉ thành công");
            $('#addressUpdateModal').modal('hide');
            $('#customerUpdateModal').modal('show');
        }).catch(error => {
            console.log("Error", error);
        }).finally(() => {
            if ($scope.selectedBill.typeBill == 2) {
                $scope.showAddress($scope.selectedBill);
            } 
        });
    }

    $scope.deleteAddress = () => {
        $http.delete(`${config.host}/address-th/${$scope.idAddress}`).then(resp => {
            $scope.resetAddress();
            $scope.showCustomerUpdate();
            toastr["success"]("Xóa địa chỉ thành công");
            $('#addressDeleteModal').modal('hide');
            $('#customerUpdateModal').modal('show');
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.apiPayment = () => {
        $http.put(`${config.host}/bill-th/payment`, $scope.selectedBill).then(resp => {
            $scope.getBills();
            $scope.selectedBill = null;
            $scope.totalQuantity = 0;
            toastr["success"]("Thanh toán " + resp.data.code + " thành công");
        }).catch(error => {
            console.log("Error", error);
        });
    }

    $scope.paymentBill = () => {


        $scope.selectedBill.totalAmountAfterDiscount = $scope.selectedBill.totalAmount - $scope.valueVoucher + $scope.shippingFee;


        
        if ($scope.selectedBill.billDetail.length == 0) {
            toastr["warning"]("Vui lòng thêm sản phẩm vào giỏ hàng");
            return;
        }


        if ( $scope.inputP) {
            toastr["error"]("Số tiền khách thanh toán không đủ");
            return;
        }


        if ($scope.selectedBill.paymentMethod.name == '13') {

            if ($scope.selectedBill.paidAmount == null) {
                toastr["error"]("Vui lòng nhập số tiền khách thanh toán");
                return;
            }
            if ($scope.selectedBill.paidAmount < $scope.selectedBill.totalAmount - $scope.valueVoucher + $scope.shippingFee ) {
                toastr["error"]("Số tiền khách thanh toán không đủ");
                return;
            }
            

        }


        if($scope.selectedBill.typeBill == 2){

            if (!$scope.selectedBill.address || !$scope.selectedBill.addressId || !$scope.selectedBill.reciverName || !$scope.selectedBill.phoneNumber  ) {
                toastr["error"]("Vui lòng xác nhận địa chỉ giao hàng");
                return;
            }
        }
     
        if ($scope.selectedBill.paymentMethod.code == "14") {
            let paidContent = $scope.selectedBill.code;
            let paidPrice = $scope.selectedBill.totalAmountAfterDiscount;
            $scope.qr = `https://img.vietqr.io/image/${MY_BANK.BANK_ID}-${MY_BANK.ACCOUNT_NO}-compact2.png?amount=${paidPrice}&addInfo=${paidContent}`;
            $scope.runCheckPaid = false;
            $scope.checkPaid(paidPrice, paidContent);
            $('#qrModal').modal('show');
        } else {
            $scope.apiPayment();
        }
    }

    // Voucher
    $scope.openVoucherModal = function() {
        $('#voucherModal').modal('show');
        $scope.getVouchersForCustomer();
      };


      $scope.removeVoucherCurrent = function(){
        $http.put('http://localhost:8080/api/admin/bill-th/' + $scope.selectedBill.id + '/remove-voucher')
        .then(function(response) {
            // Handle success
            $scope.selectedBill = response.data; // Update the bill with the response data
        })
        .catch(function(error) {
            // Handle error
            console.error("Error updating voucher:", error);
        });
      }

      $scope.selectBestVoucher = function() {
        if (!$scope.customerVouchers || $scope.customerVouchers.length === 0) {
          return;
        }   
        
        // Bước 1: Lọc danh sách voucher còn số lượng và đủ điều kiện áp dụng
        var validVouchers = $scope.customerVouchers.filter(function(voucher) {
          return voucher.quantity > 0 && $scope.selectedBill.totalAmount >= voucher.minimumTotalAmount && voucher.show == 1;
        });
      
      
        if (validVouchers.length === 0) {
          if ($scope.selectedVoucher) {
            $scope.removeVoucherCurrent();
            $scope.selectedVoucher.selected = false; // Bỏ chọn voucher trước đó
          }
          $scope.voucherData = null;
          $scope.voucherMessage = '';
          $scope.valueVoucher = 0;
          $scope.selectedBill.totalAmountAfterDiscount = $scope.selectedBill.totalAmount;
          return;
        }
      
        // Bước 2: Tính toán giá trị giảm giá cho mỗi voucher
        validVouchers.forEach(function(voucher) {
          var valueVoucher = 0;
          if (voucher.discountType === 1) {
            // Percentage discount
            var discountPercentage = voucher.value / 100;
            valueVoucher = $scope.selectedBill.totalAmount * discountPercentage;
            if (voucher.maximumReductionValue && valueVoucher > voucher.maximumReductionValue) {
              valueVoucher = voucher.maximumReductionValue;
            }
          } else if (voucher.discountType === 2) {
            // Fixed amount discount
            valueVoucher = voucher.value;
            if (voucher.maximumReductionValue && valueVoucher > voucher.maximumReductionValue) {
              valueVoucher = voucher.maximumReductionValue;
            }
          }
          voucher.valueVoucher = valueVoucher;
        });
      
        // Bước 3: Tìm voucher có giá trị giảm giá cao nhất hoặc giá trị giảm tối ưu nhất
        var bestVoucher = validVouchers.reduce(function(prev, current) {
          if (prev.valueVoucher > current.valueVoucher) {
            return prev;
          } else if (prev.valueVoucher === current.valueVoucher) {
            // Compare by maximum reduction value or other criteria if discount values are equal
            return (prev.maximumReductionValue > current.maximumReductionValue) ? prev : current;
          } else {
            return current;
          }
        });
  

        // if ($scope.selectedBill.voucher.id === bestVoucher.id) {
        // console.log("aaaaa")

        //     return;
        //   }
      
        // Áp dụng voucher tốt nhất
        // if ($scope.selectedVoucher) {
        //   $scope.selectedVoucher.selected = false; // Bỏ chọn voucher trước đó
        // }
        $scope.selectedVoucher = bestVoucher;
        $scope.valueVoucher = bestVoucher.valueVoucher;
        $scope.selectedBill.totalAmountAfterDiscount = $scope.selectedBill.totalAmount - $scope.valueVoucher;
        $scope.applyVoucher();
      };

      $scope.searchVoucher = function() {
        $scope.getVouchersForCustomer();
      };


      $scope.selectVoucher = function(selectedVoucher) {
        if (selectedVoucher.quantity > 0) {
          if ($scope.selectedVoucher === selectedVoucher) {
            $scope.selectedVoucher = null;
            selectedVoucher.selected = false;

            $scope.removeVoucherCurrent();
       

            $scope.applyVoucher();
          } else {
            if ($scope.selectedVoucher) {
              $scope.selectedVoucher.selected = false; // Bỏ chọn voucher trước đó
            }
            $scope.selectedVoucher = selectedVoucher;
            // selectedVoucher.selected = true;
            $scope.applyVoucher();
          }
        }
      };
      
      
    
    
      $scope.applyVoucher = function() {

        if ($scope.selectedVoucher != null) {
          if ($scope.selectedVoucher.quantity > 0) {
            if ($scope.selectedBill.totalAmount >= $scope.selectedVoucher.minimumTotalAmount) {
              var voucherCopy = angular.copy($scope.selectedVoucher);
              delete voucherCopy.selected;
              // $scope.voucherData = voucherCopy;
      
              if ($scope.selectedVoucher.discountType === 1) {
    
    
                var discountPercentage = $scope.selectedVoucher.value / 100;
    
                $scope.valueVoucher = $scope.selectedBill.totalAmount * discountPercentage;
    
                if ($scope.valueVoucher >= $scope.selectedVoucher.maximumReductionValue) {
                  $scope.valueVoucher = $scope.selectedVoucher.maximumReductionValue;
                }
    
                $scope.selectedBill.totalAmountAfterDiscount = $scope.selectedBill.totalAmount - $scope.valueVoucher;
      
              } else if ($scope.selectedVoucher.discountType === 2) {
                // Fixed amount discount
                $scope.valueVoucher = $scope.selectedVoucher.value;
                if ($scope.valueVoucher >= $scope.selectedVoucher.maximumReductionValue) {
                  $scope.valueVoucher = $scope.selectedVoucher.maximumReductionValue;
                }
                $scope.selectedBill.totalAmountAfterDiscount = $scope.selectedBill.totalAmount - $scope.valueVoucher;
              }

                $http.put('http://localhost:8080/api/admin/bill-th/' + $scope.selectedBill.id + '/update-voucher/' + $scope.selectedVoucher.id)
                .then(function(response) {
                    // Handle success
                    $scope.selectedBill = response.data; // Update the bill with the response data
                })
                .catch(function(error) {
                    // Handle error
                    console.error("Error updating voucher:", error);
                });


                $scope.voucherMessage = 'Mã giảm giá đã được áp dụng';
                $scope.selectedVoucher.selected = true;

                if ($scope.selectedBill.voucher != null &&$scope.selectedVoucher.id == $scope.selectedBill.voucher.id) {
                    return;
                }

                $scope.selectedBill.voucher = $scope.selectedVoucher;
              toastr["success"]($scope.voucherMessage)
            } else {

        
              // $scope.voucherData = null;
              $scope.valueVoucher = 0;
              $scope.voucherMessage =
                'Mã giảm giá ' +
                $scope.selectedVoucher.code +
                ' chỉ sử dụng cho đơn hàng có tổng trị giá trên ' +
                $filter('number')($scope.selectedVoucher.minimumTotalAmount) +
                ' đ';
                toastr["error"]($scope.voucherMessage);
              if ($scope.selectedBill.totalAmount < $scope.selectedVoucher.minimumTotalAmount) {
                $scope.selectedVoucher.selected = false;
                $scope.selectedVoucher = null;
                $scope.selectedBill.totalAmountAfterDiscount = $scope.selectedBill.totalAmount; // Initialize totalAmountAfterDiscount
                $scope.selectedBill.voucher = null;

              }
            }
          }
        } else {
          
          $scope.voucherMessage = '';
          $scope.valueVoucher = 0;
          $scope.selectedBill.totalAmountAfterDiscount = $scope.selectedBill.totalAmount;
        }
      };

      $scope.getVouchersForCustomer = function() {
        var idCustomer = $scope.selectedBill.customer != null ? $scope.selectedBill.customer.id : null;
        var configVoucher = {
          params: {
            id: idCustomer,
            search: $scope.voucherSearch
          }
      };
        $http.get(`${config.host}/voucher-th/customer/vouchers`,configVoucher)
          .then(function(response) {
            if (response.data) {
              $scope.customerVouchers = response.data;
              // Thêm khoảng thời gian trễ trước khi thực hiện hành động tiếp theo
              if ($scope.customerVouchers && $scope.customerVouchers.length > 0) {
                if ($scope.selectedBill.voucher == null) {
                $scope.selectBestVoucher();
                }else{
                    
                    for (let i = 0; i < $scope.customerVouchers.length; i++) {
                        if ($scope.customerVouchers[i].id === $scope.selectedBill.voucher.id) {
                              $scope.customerVouchers[i].selected = true;

                              $scope.selectedVoucher = $scope.customerVouchers[i];
                            break; // Exit the loop once the voucher is found
                        }
                    }
                    $scope.applyVoucher();
                }

              } 
            }
          })
          .catch(function(error) {
            // alert("Có lỗi xảy ra khi gọi API để lấy vouchers cho khách hàng!");
            console.log(error);
          });
      };

      $scope.formatCurrency = function(value) {
        if (!value) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    };
   
    // Barcode
    var html5QrcodeScanner = null;

    $scope.createBarcode = () => {
        if ($scope.selectedBill == null) {
            toastr["warning"]("Vui lòng chọn hóa đơn");
            return;
        }
        $('#barcode').modal('show');
        html5QrcodeScanner = new Html5QrcodeScanner(
            "qr-reader", { fps: 10, qrbox: 250 });
        html5QrcodeScanner.render(onScanSuccess);
    }

    $scope.clearBarCode = () => {
        html5QrcodeScanner.clear();
    }




// shippefee

  
$scope.calculateShippingFee = function (toDistrictId, toWardCode) {
    $scope.shippingFee = 0;

  if (toDistrictId && toWardCode &&  $scope.totalQuantity > 0) {
    
      let blows = (toWardCode || "").toString().replace(/\D/g, "");
      let numericDistrictId = Number(toDistrictId);

      // Định nghĩa headers với token
      var config = {
          headers: {
              'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
          }
      };

      // Body data for the POST request
      var requestData = {
          "service_id": 53321,
          "insurance_value": 1,
          "coupon": null,
          "from_district_id": 1482,
          "to_district_id": numericDistrictId,
          "to_ward_code": blows,
          "height": 15,
          "length": 15,
          "weight": 200 * $scope.totalQuantity, // Trọng lượng (700g mỗi sản phẩm)
          "width": 15
      };

      // Gọi API với phương thức POST và thân yêu cầu (body)
      $http.post('https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', requestData, config)
          .then(function (response) {
            $scope.shippingFee = response.data.data.total ;
            $scope.updateShippingFeeToBill($scope.shippingFee);
            console.log($scope.shippingFee)

          })
          .catch(function (error) {
              // Xử lý lỗi nếu có
              console.error('Error calling API:', error);
              $scope.shippingFee = 50000;
  $scope.updateShippingFeeToBill($scope.shippingFee);

          });
  }


};




// update bill  


$scope.updateTypeBill = function() {
    if ($scope.isChecked) {
        $scope.selectedBill.typeBill = 2;
        $scope.showAddress($scope.selectedBill)
        // Set to 2 when the checkbox is checked
    } else {
        $scope.selectedBill.typeBill = 1;  // Set to 1 when the checkbox is unchecked
    }
    $scope.showShippingFee($scope.selectedBill);
// Call the API to update typeBill using $http.post
        $http.post('http://localhost:8080/api/admin/bill-th/typeBill', $scope.selectedBill)
        .then(function(response) {
   
            $scope.selectedBill = response.data;
            console.log('TypeBill updated successfully', response.data);
        })
        .catch(function(error) {
            // Handle error
            console.error('Error updating typeBill', error);
        });



};


$scope.addAddressCustomer = () => {


    // $scope.resetAddress();

    if ($scope.addressAddForm.$valid && $scope.validateAddress($scope.provinceValue, $scope.districtValue, $scope.wardValue)) {
        $scope.calculateShippingFee($scope.districtCode,$scope.wardCode);
     
        var fullAddress =
        $scope.addressDetail +
        ', ' +
        $scope.wardValue +
        ', ' +
        $scope.districtValue +
        ', ' +
        $scope.provinceValue;

        var idFullAddress =
        $scope.wardCode +
        ', ' +
        $scope.districtCode +
        ', ' +
        $scope.provinceCode;
        
        const billData = {
            address: fullAddress,
            addressId: idFullAddress,
            reciverName: $scope.reciverName,
            // shippingFee: $scope.shippingFee,
            phoneNumber: $scope.phoneNumber,
        };

        console.log(billData);
        
        $http.put('http://localhost:8080/api/admin/bill-th/address/' + $scope.selectedBill.id, billData)
        .then(function(response) {
            // Handle success
            toastr["success"]("Xác nhận địa chỉ giao hàng " + " thành công");
            $scope.selectedBill = response.data;
        })
        .catch(function(error) {
            // Handle error
            console.error('Error updating bill', error);
            toastr["error"]("Xác nhận địa chỉ giao hàng " + " thất bại");
        });


        
    }
}

$scope.getDefaultAddress = function(customerId) {
    return $http.get('http://localhost:8080/api/admin/address-th/default/' + customerId)
    .then(function(response) {
        // Xử lý kết quả thành công
        $scope.defaultAddressCustomer = response.data;
        return $scope.defaultAddressCustomer;
    })
    .catch(function(error) {
        // Xử lý lỗi
        console.error("Error fetching default address:", error);
        throw error; // Đảm bảo lỗi được ném ra ngoài để Promise bị từ chối
    });
};



$scope.loadAddressForBill = function(bill) {
    return new Promise((resolve, reject) => {
        // Kiểm tra xem địa chỉ trong bill có tồn tại không
        if (!bill.address || bill.address.trim() === '') {
            // Nếu không tồn tại, kiểm tra xem bill có customer chưa
            if (bill.customer && bill.customer.id) {
                // Gọi API để lấy địa chỉ mặc định của khách hàng
                $scope.getDefaultAddress(bill.customer.id).then(function() {
                    // Sau khi lấy được địa chỉ mặc định, kiểm tra xem có địa chỉ không
                    if ($scope.defaultAddressCustomer && $scope.defaultAddressCustomer.address) {
                        // Cập nhật bill với địa chỉ mặc định
                        bill.address = $scope.defaultAddressCustomer.address;
                        bill.phoneNumber = $scope.defaultAddressCustomer.phoneNumber;
                        bill.reciverName = $scope.defaultAddressCustomer.name;
                        bill.addressId = $scope.defaultAddressCustomer.addressId;
                    }
                    resolve(bill);
                }).catch(function(error) {
                    console.error("Error fetching default address:", error);
                    reject(error);
                });
            } else {
                // Nếu bill không có customer
                resolve(bill);
            }
        } else {
            // Nếu địa chỉ trong bill tồn tại, trả về bill
            resolve(bill);
        }
    });
};




$scope.showAddress = function(bill) {

    $scope.resetAddress();
    // Đảm bảo rằng dữ liệu đã được load
    $scope.loadAddressForBill(bill).then(function(loadedBill) {
        if (loadedBill.address && loadedBill.phoneNumber && loadedBill.reciverName && typeof loadedBill.address === 'string') {
            // Split the address by commas
            var addressParts = loadedBill.address.split(',');
        
            // Trim the first part of the address and assign it to addressDetail
            $scope.addressDetail = addressParts[0].trim();
            $scope.phoneNumber = loadedBill.phoneNumber;
            $scope.reciverName = loadedBill.reciverName;
        }
    
        if (loadedBill.addressId && loadedBill.address && typeof loadedBill.addressId === 'string') {
            let [wardCode, districtCode, provinceCode] = loadedBill.addressId.split(', ').map(part => part.trim());
         
            let [wardValue, districtValue, provinceValue] = loadedBill.address.split(', ').map(part => part.trim());
    
            $('.province').val(provinceCode).trigger('change');
            $scope.provinceCode = provinceCode;
            $scope.provinceValue = provinceValue;
            $scope.loadDistricts(provinceCode, '.district').then(function() {
                $scope.districtCode = districtCode;
            });
            $scope.districtValue = districtValue;
            $scope.loadWards(districtCode, '.ward').then(() => {
                $scope.wardCode = wardCode;
            })
            $scope.wardValue = wardValue;
        }
    }).catch(function(error) {
        console.error("Error loading address data:", error);
    });
};


$scope.showShippingFee = function (bill) {
    if ($scope.selectedBill.typeBill == 1) {
        $scope.shippingFee = 0;
        return
    }

    $scope.inputPrice2 = 1;
    if (bill.addressId && bill.address && typeof bill.addressId === 'string') {
        let [wardCode, districtCode, provinceCode] = bill.addressId.split(', ').map(part => part.trim());



    $scope.calculateShippingFee(districtCode,wardCode);
    }


    };



    $scope.updateShippingFeeToBill = function (shippingFee) {
        console.log($scope.selectedBill)

        let data = shippingFee;
        console.log($scope.shippingFee)
        console.log($scope.selectedBill.shippingFee)

        if (data == $scope.selectedBill.shippingFee) return;
        $http.put("http://localhost:8080/api/admin/bill-th/shippingFeeUpdate/" + $scope.selectedBill.id, data).then(function (res) {
            console.log("sửa phí ship thành công");
            // console.log(res.data);
        //  $scope.getBill();
        }, function (error) {
            console.error('update shippingfee error', error);
        });
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

