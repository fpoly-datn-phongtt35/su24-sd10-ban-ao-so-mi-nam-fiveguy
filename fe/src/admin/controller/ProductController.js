app.controller("ProductController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.product = {collar: {}, wrist: {}, material: {}, category: {}, supplier: {}};
    $scope.productUpdate = {};
    $scope.products = [];
    $scope.collars = [];
    $scope.wrists = [];
    $scope.materials = [];
    $scope.categories = [];
    $scope.suppliers = [];
    
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

    // Begin Collar
    $('#id-label-collar').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-collar")
    }).on("select2:select", function (e) { 
        $scope.product.collar.id = e.params.data.id;   
    });

    $scope.getAllCollars = () => {
        $http.get(`${config.host}/collar/all`).then(response => {
            $scope.collars = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.getAllCollars();
    // End Collar

    // Begin Wrist
    $('#id-label-wrist').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-wrist")
    }).on("select2:select", function (e) { 
        $scope.product.wrist.id = e.params.data.id;   
    });

    $scope.getAllWrists = () => {
        $http.get(`${config.host}/wrist/all`).then(response => {
            $scope.wrists = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.getAllWrists();

    // End Wrist

    // Begin Material
    $scope.getAllMaterials = () => {
        $http.get(`${config.host}/material/all`).then(response => {
            $scope.materials = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.getAllMaterials();

    $('#id-label-material').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-material")
    }).on("select2:select", function (e) { 
        $scope.product.material.id = e.params.data.id;   
    });
    // End Material

    // Begin category
    $scope.getAllCategories = () => {
        $http.get(`${config.host}/category/all`).then(response => {
            $scope.categories = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.getAllCategories();

    $('#id-label-category').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-category")
    }).on("select2:select", function (e) { 
        $scope.product.category.id = e.params.data.id;   
    });
    // End category

    // Begin Supplier
    $scope.getAllSuppliers = () => {
        $http.get(`${config.host}/supplier/all`).then(response => {
            $scope.suppliers = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.getAllSuppliers();

    $('#id-label-supplier').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-supplier")
    }).on("select2:select", function (e) { 
        $scope.product.supplier.id = e.params.data.id;   
    });
    // End Supplier

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