app.controller("BrandController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC',
    };
    $scope.brand = {};
    $scope.brandUpdate = {};
    $scope.brands = [];
    
    $scope.getAllBrands = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/brand-th`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status,
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.brands = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchBrands = () => {
        $scope.page = 0;
        $scope.getAllBrands();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllBrands();
        }
    }

    $scope.getAllBrands();

    $scope.resetForm = () => {
        $scope.brand = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.brandUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editBrand = (key) => {
        $http.get(`${config.host}/brand-th/${key}`).then((response) => {
            $scope.brandUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.brand = element;
    }

    $scope.handleStatus = (element) => {
        $scope.brand = element;
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/brand-th/status/${$scope.brand.id}`).then(response => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            $('#updateStatusModel').modal('hide');
            $scope.getAllBrands();
            $scope.brand = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            console.log("Error", error);
        })
    }

    $scope.createBrand = () => {
        if ($scope.brandForm.$valid) {
            $('#addBrand').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/brand-th`, $scope.brand).then((response) => {
                $('#addBrand').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addBrandModel').modal('hide');
                $scope.getAllBrands();
                $scope.resetForm();
                
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addBrand').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateBrand = () => {
        if ($scope.brandFormUpdate.$valid) {
            $('#updateBrand').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/brand-th/${$scope.brandUpdate.id}`, $scope.brandUpdate).then(response => {
                $('#updateBrand').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editBrandModal').modal('hide');
                $scope.getAllBrands();
                $scope.resetFormUpdate();           
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                $('#updateBrand').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $('#deleteBrand').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/brand-th/${$scope.brand.id}`).then(response => {
            $('#deleteBrand').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $('#deleteBrandModel').modal('hide');
            $scope.getAllBrands();
            $scope.brand = {};
            toastr["success"]("Ngừng hoạt động " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteBrand').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            toastr["error"](error);
        })
    }


})