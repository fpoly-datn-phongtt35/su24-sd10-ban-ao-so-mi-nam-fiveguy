app.controller("SuppilerController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.supplier = {};
    $scope.supplierUpdate = {};
    $scope.suppliers = [];
    $scope.provinces = [];
    $scope.districts = [];
    $scope.wards = [];

    
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

    $scope.searchSuppilers = () => {
        $scope.page = 0;
        $scope.getAllSuppilers();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllSuppilers();
        }
    }

    // $scope.getAllSuppilers();

    $scope.resetForm = () => {
        $scope.supplier = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.supplierUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editSuppiler = (key) => {
        $http.get(`${config.host}/supplier/${key}`).then((response) => {
            $scope.supplierUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.supplier = element;
    }

    $scope.handleStatus = (element) => {
        $scope.supplier = element;
    }

    $scope.updateStatus = () => {
        $http.put(`${config.host}/supplier/status/${$scope.supplier.id}`).then(response => {
            $scope.getAllSuppilers();
            $scope.supplier = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createSuppiler = () => {
        if ($scope.supplierForm.$valid) {
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

    $scope.updateSuppiler = () => {
        if ($scope.supplierFormUpdate.$valid) {
            $http.put(`${config.host}/supplier/${$scope.supplierUpdate.id}`, $scope.supplierUpdate).then(response => {
                $scope.getAllSuppilers();
                $scope.resetFormUpdate();
                $('#editSuppilerModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/supplier/${$scope.supplier.id}`).then(response => {
            $scope.getAllSuppilers();
            $scope.supplier = {};
            $('#deleteSuppilerModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $('select').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#addSupplierModel")
    } );

    // ---------------------------------------
    $http.get('https://vn-public-apis.fpo.vn/provinces/getAll?limit=-1')
    .then(function(response) {
        $scope.provinces = response.data.data.data;
    })
    .catch(function(error) {
        console.error('Lỗi khi gọi API:', error);
    });

// Fetch districts based on selected province
$scope.getProvinces = function(provinceCode) {
    $scope.districts = [];
    $scope.wards = [];
    if (provinceCode) {
        $http.get(`https://vn-public-apis.fpo.vn/districts/getByProvince?provinceCode=${provinceCode}&limit=-1`)
            .then(function(response) {
                $scope.districts = response.data.data.data;
            })
            .catch(function(error) {
                console.error('Lỗi khi gọi API:', error);
            });
    }
};

// Fetch wards based on selected district
$scope.getDistricts = function(districtCode) {
    $scope.wards = [];
    if (districtCode) {
        $http.get(`https://vn-public-apis.fpo.vn/wards/getByDistrict?districtCode=${districtCode}&limit=-1`)
            .then(function(response) {
                $scope.wards = response.data.data.data;
            })
            .catch(function(error) {
                console.error('Lỗi khi gọi API:', error);
            });
    }
};
})