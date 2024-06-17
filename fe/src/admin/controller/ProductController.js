app.controller("ProductController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.product = {};
    $scope.productUpdate = {};
    $scope.products = [];
    
    $scope.getAllProducts = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $http.get(`${config.host}/product`, 
                {params: {page: $scope.page, size: $scope.size, keyword: $scope.filter.keyword,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.products = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                console.log("Error", error)
            })
    }

    $scope.searchProducts = () => {
        $scope.page = 0;
        $scope.getAllProducts();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllProducts();
        }
    }

    // $scope.getAllProducts();

    $scope.resetForm = () => {
        $scope.product = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.productUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editProduct = (key) => {
        $http.get(`${config.host}/product/${key}`).then((response) => {
            $scope.productUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.product = element;
    }

    $scope.handleStatus = (element) => {
        $scope.product = element;
    }

    $scope.updateStatus = () => {
        $http.put(`${config.host}/product/status/${$scope.product.id}`).then(response => {
            $scope.getAllProducts();
            $scope.product = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createProduct = () => {
        if ($scope.productForm.$valid) {
           $http.post(`${config.host}/product`, $scope.product).then((response) => {
                $scope.getAllProducts();
                $scope.resetForm();
                $('#addProductModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateProduct = () => {
        if ($scope.productFormUpdate.$valid) {
            $http.put(`${config.host}/product/${$scope.productUpdate.id}`, $scope.productUpdate).then(response => {
                $scope.getAllProducts();
                $scope.resetFormUpdate();
                $('#editProductModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/product/${$scope.product.id}`).then(response => {
            $scope.getAllProducts();
            $scope.product = {};
            $('#deleteProductModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }


})