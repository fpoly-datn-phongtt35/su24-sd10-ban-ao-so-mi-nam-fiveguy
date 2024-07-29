app.controller("SupplierController", function($scope, $http){
    $scope.page = 0;
    $scope.pageBrand = 0;
    $scope.size = 5;
    $scope.sizeBrand = 5;
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.filterBrand = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    }
    $scope.supplier = {};
    $scope.supplierUpdate = {};
    $scope.suppliers = [];
    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];
    $scope.brand = {};
    $scope.brands = [];
    $scope.selected = [];
    $scope.selectedUpdate = [];

    $http.get('https://vapi.vnappmob.com/api/province/')
    .then(function(response) {
        $scope.provinces = response.data.results;
    })
    .catch(function(error) {
        console.error('Lỗi khi gọi API:', error);
    });


    $scope.searchBrands = () => {
        $scope.pageBrand = 0;
        $scope.currentPageInput = 1;
        $scope.getAllBrands();
    }

    $scope.getAllBrands = () => {
        $http.get(`${config.host}/brand`, {
            params: {page: $scope.pageBrand, size: $scope.sizeBrand, keyword: $scope.filterBrand.keyword,
                            sortField: $scope.filterBrand.sortField,
                            sortDirection: $scope.filterBrand.sortDirection,
                            status: $scope.filterBrand.status
                            }
        }).then(response => {
            $scope.brands = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.getAllBrands();

    $scope.changePageBrand = (page) => {
        if (page >= 0 && page < $scope.brands.totalPages) {
            $scope.pageBrand = page;
            $scope.currentPageInput = page + 1;
            $scope.getAllBrands();
            return;
        } else {
            $scope.currentPageInput = 1;
            $scope.pageBrand = 0;
            $scope.getAllBrands();
        }
       
    }

    
    $scope.createBrand = () => {
        $http.post(`${config.host}/brand`, $scope.brand).then(response => {
            $scope.brand = {};
            $scope.errorsBrand = {};
            $scope.getAllBrands();
            toastr["success"]("Thêm mới " + response.data.name + " thành công");
       }).catch(error => {
            if (error.status === 400) $scope.errorsBrand = error.data
            else toastr["error"](error);
       })
    }

    $scope.editBrand = (key) => {
        $http.get(`${config.host}/brand/${key}`).then((response) => {
            $scope.brand = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.updateBrand = () => {
        $http.put(`${config.host}/brand/${$scope.brand.id}`,$scope.brand).then(response => {
            $scope.brand = {};
            $scope.errorsBrand = {};
            $scope.getAllBrands();
            toastr["success"]("Cập nhật " + response.data.name + " thành công");
        }). catch(error => {
            if (error.status === 400) $scope.errorsBrand = error.data;
            else toastr["error"](error);
        })         
    }

    $scope.deleteBrand = (id) => {
        $http.delete(`${config.host}/brand/${id}`).then(response => {
            $scope.getAllBrands();
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.updateStatusBrand = (id) => {
        $http.put(`${config.host}/brand/status/${id}`).then(response => {
            $scope.getAllBrands();
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.exist = (item) => {
        return $scope.selected.some(selectedItem => selectedItem.id === item.id);
    }

    $scope.toggleSelection = (item) => {
        let index = $scope.selected.indexOf(item);
        if (index > -1) $scope.selected.splice(index, 1);
        else $scope.selected.push(item);
    }

    $scope.existUpdate = (item) => {
        return $scope.selectedUpdate.some(selectedItem => selectedItem.brand.id === item.id && selectedItem.status === 1);
    }

    $scope.toggleSelectionUpdate = (item) => {
        let index = $scope.selectedUpdate.findIndex(selectedItem => selectedItem.brand.id === item.id);
        if (index > -1 && $scope.selectedUpdate[index].id) $scope.selectedUpdate[index].status = $scope.selectedUpdate[index].status === 1 ? 0 : 1;
        else if (index > -1) {
            $scope.selectedUpdate.splice(index, 1);
        }
        else  {
            $scope.selectedUpdate.push({
                brand: item,
                supplier: {
                    id: $scope.supplierUpdate.id,
                    brand: $scope.supplierUpdate.brand,
                    address: $scope.supplierUpdate.address,
                    createdAt: $scope.supplierUpdate.createdAt,
                    email: $scope.supplierUpdate.email,
                    name: $scope.supplierUpdate.name,
                    phoneNumber: $scope.supplierUpdate.phoneNumber,
                    updatedAt: $scope.supplierUpdate.updatedAt
                },
                status: 1
            });
        }
    }
    
    $scope.getAllSuppilers = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/supplier`, 
                {params: {page: $scope.page, size: $scope.size, keyword: $scope.filter.keyword,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.suppliers = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchSuppliers = () => {
        $scope.page = 0;
        $scope.getAllSuppilers();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllSuppilers();
        }
    }

    $scope.getAllSuppilers();

    $scope.resetForm = () => {
        $scope.supplier = {};
        $scope.errors = {};
        $scope.selected = [];
    }

    $scope.resetFormUpdate = () => {
        $scope.supplierUpdate = {};
        $scope.errorsUpdate = {};
        $scope.selectedUpdate = [];
    }

    
    $scope.handleDelete = (element) => {
        $scope.supplier = element;
    }

    $scope.handleStatus = (element) => {
        $scope.supplier = element;
    }

    
    $scope.delete = () => {
        $('#deleteSupplier').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/supplier/${$scope.supplier.id}`).then(response => {
            $('#deleteSupplier').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $('#deleteSupplierModel').modal('hide');
            $scope.getAllSuppilers();
            $scope.supplier = {};
            toastr["success"]("Ngừng hoạt động " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteSupplier').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            toastr["error"](error);
        })
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/supplier/status/${$scope.supplier.id}`).then(response => {
            $('#updateStatus').css('display', 'none');
            $('#loadingStatus').css('display', 'inline-block');
            $('#updateStatusModel').modal('hide');
            $scope.getAllSuppilers();
            $scope.supplier = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'none');
            $('#loadingStatus').css('display', 'inline-block');
            console.log("Error", error);
        })
    }

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

    

    $scope.createSupplier = () => {
        if ($scope.supplierForm.$valid && $scope.isAddress($scope.provinceValue, $scope.districtValue, $scope.wardValue)) {
            $scope.supplier.brands = $scope.selected;
            $scope.supplier.address = `${$scope.provinceValue}, ${$scope.districtValue}, ${$scope.wardValue}`;
            $('#addSupplier').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/supplier`, $scope.supplier).then((response) => {
                $('#addSupplier').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addSupplierModel').modal('hide');
                $scope.getAllSuppilers();
                $scope.resetForm();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addSupplier').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else console.log(error);
           })
        }
    }

    $scope.updateSupplier = () => {
        if ($scope.supplierUpdateForm.$valid && $scope.isAddress($scope.provinceName, $scope.districtName, $scope.wardName)) {
            $scope.supplierUpdate.brandSuppilers = $scope.selectedUpdate;
            $scope.supplierUpdate.address = `${$scope.provinceName}, ${$scope.districtName}, ${$scope.wardName}`;
            $('#updateSupplier').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/supplier/${$scope.supplierUpdate.id}`, $scope.supplierUpdate).then((response) => {
                $('#updateSupplier').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editSupplierModal').modal('hide');
                $scope.getAllSuppilers();
                $scope.resetFormUpdate();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
            }).catch(error => {
                $('#updateSupplier').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else console.log(error);
            })
        }
    }

    // Initialize Select2 for provinces
    $('#id-update-province').select2({
        theme: "bootstrap-5",
        placeholder: $('#id-update-province').data('placeholder'),
        dropdownParent: $("#box-update-province")
    }).on("select2:select", function(e) {
        $scope.$apply(function() {
            $scope.districts = [];
            $scope.districtName = "";
            $scope.wards = [];
            $scope.wardName = "";

            let provinceId = e.params.data.id;
            $scope.provinceName = e.params.data.text;
            if (provinceId) {
                $("#id-update-district").prop("disabled", true);
                $http.get(`https://vapi.vnappmob.com/api/province/district/${provinceId}`)
                    .then(function(response) {
                        $scope.districts = response.data.results;
                        $("#id-update-district").prop("disabled", false);
                    })
                    .catch(function(error) {
                        console.error('Lỗi khi gọi API:', error);
                    });
            }
        });
    });

    // Initialize Select2 for districts
    $('#id-update-district').select2({
        theme: "bootstrap-5",
        placeholder: $('#id-update-district').data('placeholder'),
        dropdownParent: $("#box-update-district")
    }).on("select2:select", function(e) {
        $scope.$apply(function() {
            $scope.wards = [];
            $scope.wardName = "";

            let districtId = e.params.data.id;
            $scope.districtName = e.params.data.text;
            if (districtId) {
                $("#id-update-ward").prop("disabled", true);
                $http.get(`https://vapi.vnappmob.com/api/province/ward/${districtId}`)
                    .then(function(response) {
                        $scope.wards = response.data.results;
                        $("#id-update-ward").prop("disabled", false);
                    })
                    .catch(function(error) {
                        console.error('Lỗi khi gọi API:', error);
                    });
            }
        });
    });

    // Initialize Select2 for wards
    $('#id-update-ward').select2({
        theme: "bootstrap-5",
        placeholder: $('#id-update-ward').data('placeholder'),
        dropdownParent: $("#box-update-ward")
    }).on("select2:select", function(e) {
        $scope.$apply(function() {
            $scope.wardName = e.params.data.text;
        });
    });

    // Function to edit supplier and load address parts
    $scope.editSupplier = function(key) {
        $http.get(`${config.host}/supplier/${key}`).then(function(response) {
            $scope.supplierUpdate = response.data;
            
            // Split address and select corresponding values
            let address = response.data.address;
            let [provinceName, districtName, wardName] = address.split(', ').map(part => part.trim());

            // Find and select province
            let province = $scope.provinces.find(p => p.province_name === provinceName);
            if (province) {
                $scope.selectedProvince = province.province_id;
                $scope.provinceName = province.province_name;
                $('#id-update-province').val(province.province_id).trigger('change');

                // Load districts and select corresponding district
                $scope.loadDistricts(province.province_id, '.districtUpdate').then(function() {
                    let district = $scope.districts.find(d => d.district_name === districtName);
                    if (district) {
                        $scope.selectedDistrict = district.district_id;
                        $scope.districtName = district.district_name;
                        $('#id-update-district').val(district.district_id).trigger('change');

                        // Load wards and select corresponding ward
                        $scope.loadWards(district.district_id, '.wardUpdate').then(function() {
                            let ward = $scope.wards.find(w => w.ward_name === wardName);
                            if (ward) {
                                $scope.selectedWard = ward.ward_id;
                                $scope.wardName = ward.ward_name;
                                $('#id-update-ward').val(ward.ward_id).trigger('change');
                            }
                        });
                    }
                });
                $http.get(`${config.host}/brand-supplier/${key}`).then(response => {
                    $scope.selectedUpdate = response.data;
                }).catch(function(error) {
                    toastr["error"](error);
                });
            }
        }).catch(function(error) {
            toastr["error"](error);
        });
    };

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


    // --------------------------------------------------------------------------------------------

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
    });

    // ---------------------------------------

})