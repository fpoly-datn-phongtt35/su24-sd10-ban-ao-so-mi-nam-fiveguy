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

    $scope.searchBrands = () => {
        $scope.pageBrand = 0;
        $scope.getAllBrands();
    }

    $scope.getAllBrands = () => {
        if ($scope.pageBrand <= 0 || !Number.isInteger($scope.pageBrand)) {
            $scope.pageBrand = 0;
        }
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
    
    // $scope.getAllSuppilers = () => {
    //     if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
    //         $scope.size = 5;
    //     }
    //     $http.get(`${config.host}/supplier`, 
    //             {params: {page: $scope.page, size: $scope.size, keyword: $scope.filter.keyword,
    //             sortField: $scope.filter.sortField,
    //             sortDirection: $scope.filter.sortDirection
    //             }})
    //         .then((response) => {
    //             $scope.suppliers = response.data;
    //             $scope.totalPages = response.data.totalPages;
    //             $scope.currentPage = response.data.pageable.pageNumber;
    //         }).catch(error => {
    //             console.log("Error", error)
    //         })
    // }

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

    // $scope.getAllSuppilers();

    // $scope.resetForm = () => {
    //     $scope.supplier = {};
    //     $scope.errors = {};
    // }

    // $scope.resetFormUpdate = () => {
    //     $scope.supplierUpdate = {};
    //     $scope.errorsUpdate = {};
    // }

    // $scope.editSuppiler = (key) => {
    //     $http.get(`${config.host}/supplier/${key}`).then((response) => {
    //         $scope.supplierUpdate = response.data;
    //     }).catch(error => {
    //         toastr["error"](error);
    //     })
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
        $scope.errors = {};
        if ($scope.provinceValue == undefined) {
            $scope.errors.province = "Vui lòng chọn tỉnh thành";
            return false;
        } else if ($scope.districtValue == "") {
            $scope.errors.district = "Vui lòng chọn quận/huyện";
            return false;
        } else if ($scope.wardValue == "") {
            $scope.errors.ward = "Vui lòng chọn phường/xã";
            return false;
        }
        return true;
    }

    $scope.createSupplier = () => {
        if ($scope.supplierForm.$valid && $scope.isAddress()) {
            console.log($scope.districtValue);
        //    $http.post(`${config.host}/supplier`, $scope.supplier).then((response) => {
        //         $scope.getAllSuppilers();
        //         $scope.resetForm();
        //         $('#addSuppilerModel').modal('hide');
        //         toastr["success"]("Thêm mới " + response.data.name + " thành công");
        //    }).catch(error => {
        //         if (error.status === 400) $scope.errors = error.data
        //         else toastr["error"](error);
        //    })
        }
    }


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