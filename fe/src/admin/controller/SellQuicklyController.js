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
    
    $scope.customer = {gender: true};
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
                $scope.customer.path = e.target.result;
            });
        };
    
        reader.readAsDataURL(image);
    };

    $scope.isAddress = (provice, district, ward) => {
        if (provice == undefined) {
            toastr["warning"]("Vui lòng chọn tỉnh thành");
            return false;
        } else if (district == "") {
            toastr["warning"]("Vui lòng chọn quận/huyện");
            return false;
        }  else if (ward == "") {
            toastr["warning"]("Vui lòng chọn phường/xã");
            return false;
        } 
        return true;
    }

    $scope.createCustomer = () => {
        if ($scope.customerForm.$valid && $scope.isAddress($scope.provinceValue, $scope.districtValue, $scope.wardValue)) {
            console.log(123)
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