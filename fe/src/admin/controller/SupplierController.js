app.controller("SuppilerController", function($scope, $http){
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
    $scope.selectedAll = false;

    $scope.searchBrands = () => {
        $scope.pageBrand = 0;
        $scope.getAllBrands();
    }

    $scope.getAllBrands = () => {
        $http.get(`${config.host}/brand`, {
            params: {page: $scope.pageBrand, size: $scope.sizeBrand, keyword: $scope.filterBrand.keyword,
                            sortField: $scope.filterBrand.sortField,
                            sortDirection: $scope.filterBrand.sortDirection
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
        return $scope.selected.indexOf(item) > -1;
    }

    $scope.toggleSelection = (item) => {
        let index = $scope.selected.indexOf(item);
        if (index > -1) $scope.selected.splice(index, 1);
        else $scope.selected.push(item);
    }

    $scope.checkAll = () => {
        if (!$scope.selectedAll) {
            angular.forEach($scope.brands.content, (item) => {
                index = $scope.selected.indexOf(item);
                if (index >= 0) return true;
                else $scope.selected.push(item);
            })
        } else $scope.selected = [];
    }
    
    $scope.getAllSuppilers = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $http.get(`${config.host}/supplier`, 
                {params: {page: $scope.page, size: $scope.size, keyword: $scope.filter.keyword,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.suppliers = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                console.log("Error", error)
            })
    }

    // $scope.searchSuppilers = () => {
    //     $scope.page = 0;
    //     $scope.getAllSuppilers();
    // }
    
    // $scope.changePage = (page) => {
    //     if (page >= 0 && page < $scope.totalPages) {
    //         $scope.page = page;
    //         $scope.getAllSuppilers();
    //     }
    // }

    $scope.getAllSuppilers();

    $scope.resetForm = () => {
        $scope.supplier = {};
        $scope.errors = {};
    }

    // $scope.resetFormUpdate = () => {
    //     $scope.supplierUpdate = {};
    //     $scope.errorsUpdate = {};
    // }

    
    // $scope.handleDelete = (element) => {
    //     $scope.supplier = element;
    // }

    // $scope.handleStatus = (element) => {
    //     $scope.supplier = element;
    // }

    // $scope.updateStatus = () => {
    //     $http.put(`${config.host}/supplier/status/${$scope.supplier.id}`).then(response => {
    //         $scope.getAllSuppilers();
    //         $scope.supplier = {};
    //         $('#updateStatusModel').modal('hide');
    //         toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
    //     }).catch(error => {
    //         console.log("Error", error);
    //     })
    // }

    $scope.isAddress = () => {
        if ($scope.provinceValue == undefined) {
            toastr["warning"]("Vui lòng chọn tỉnh thành");
            return false;
        } else if ($scope.districtValue == "") {
            toastr["warning"]("Vui lòng chọn quận/huyện");
            return false;
        }  else if ($scope.wardValue == "") {
            toastr["warning"]("Vui lòng chọn phường/xã");
            return false;
        } 
        return true;
    }

    $scope.createSupplier = () => {
        if ($scope.supplierForm.$valid && $scope.isAddress()) {
            $scope.supplier.brands = $scope.selected;
            $scope.supplier.address = `${$scope.provinceValue}, ${$scope.districtValue}, ${$scope.wardValue}`;
           $http.post(`${config.host}/supplier`, $scope.supplier).then((response) => {
                $scope.getAllSuppilers();
                $scope.resetForm();
                $('#addSuppilerModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    
    $scope.selectedProvince = null;
    $scope.selectedDistrict = null;
    $scope.selectedWard = null;

    // Load provinces
    $http.get('https://vapi.vnappmob.com/api/province/')
        .then(function(response) {
            $scope.provinces = response.data.results;
        })
        .catch(function(error) {
            console.error('Lỗi khi gọi API:', error);
        });

    // Initialize Select2 for provinces
    $('#id-update-province').select2({
        theme: "bootstrap-5",
        placeholder: $('#id-update-province').data('placeholder'),
        dropdownParent: $("#box-update-province")
    }).on("select2:select", function(e) {
        $scope.$apply(function() {
            $scope.districts = [];
            $scope.selectedDistrict = null;
            $scope.wards = [];
            $scope.selectedWard = null;

            let provinceId = e.params.data.id;
            $scope.selectedProvince = provinceId;
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
            $scope.selectedWard = null;

            let districtId = e.params.data.id;
            $scope.selectedDistrict = districtId;
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
            $scope.selectedWard = e.params.data.id;
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
                $('#id-update-province').val(province.province_id).trigger('change');

                // Load districts and select corresponding district
                $scope.loadDistricts(province.province_id).then(function() {
                    let district = $scope.districts.find(d => d.district_name === districtName);
                    if (district) {
                        $scope.selectedDistrict = district.district_id;
                        $('#id-update-district').val(district.district_id).trigger('change');

                        // Load wards and select corresponding ward
                        $scope.loadWards(district.district_id).then(function() {
                            let ward = $scope.wards.find(w => w.ward_name === wardName);
                            if (ward) {
                                $scope.selectedWard = ward.ward_id;
                                $('#id-update-ward').val(ward.ward_id).trigger('change');
                            }
                        });
                    }
                });
            }
        }).catch(function(error) {
            toastr["error"](error);
        });
    };

    // Load districts by province ID
    $scope.loadDistricts = function(provinceId) {
        return $http.get(`https://vapi.vnappmob.com/api/province/district/${provinceId}`)
            .then(function(response) {
                $scope.districts = response.data.results;
            })
            .catch(function(error) {
                console.error('Lỗi khi gọi API:', error);
            });
    };

    // Load wards by district ID
    $scope.loadWards = function(districtId) {
        return $http.get(`https://vapi.vnappmob.com/api/province/ward/${districtId}`)
            .then(function(response) {
                $scope.wards = response.data.results;
            })
            .catch(function(error) {
                console.error('Lỗi khi gọi API:', error);
            });
    };

    // $scope.updateSuppiler = () => {
    //     if ($scope.supplierFormUpdate.$valid) {
    //         $http.put(`${config.host}/supplier/${$scope.supplierUpdate.id}`, $scope.supplierUpdate).then(response => {
    //             $scope.getAllSuppilers();
    //             $scope.resetFormUpdate();
    //             $('#editSuppilerModal').modal('hide');
    //             toastr["success"]("Cập nhật " + response.data.name + " thành công");
    //         }). catch(error => {
    //             if (error.status === 400) $scope.errorsUpdate = error.data;
    //             else toastr["error"](error);
    //         })
    //     }
    // }

    // $scope.delete = () => {
    //     $http.delete(`${config.host}/supplier/${$scope.supplier.id}`).then(response => {
    //         $scope.getAllSuppilers();
    //         $scope.supplier = {};
    //         $('#deleteSuppilerModel').modal('hide');
    //         toastr["success"]("Xóa " + response.data.name + " thành công");
    //     }).catch(error => {
    //         toastr["error"](error);
    //     })
    // }

    // --------------------------------------------------------------------------------------------

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
            $(".district").prop("disabled", true);
            $http.get(`https://vapi.vnappmob.com/api/province/district/${provinceCode}`)
                .then(function(response) {
                    $scope.districts = response.data.results;
                    $(".district").prop("disabled", false);
                })
                .catch(function(error) {
                    console.error('Lỗi khi gọi API:', error);
                });
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
            $(".ward").prop("disabled", true);
            $http.get(`https://vapi.vnappmob.com/api/province/ward/${districtCode}`)
                .then(function(response) {
                    $scope.wards = response.data.results;
                    $(".ward").prop("disabled", false);
                })
                .catch(function(error) {
                    console.error('Lỗi khi gọi API:', error);
                });
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