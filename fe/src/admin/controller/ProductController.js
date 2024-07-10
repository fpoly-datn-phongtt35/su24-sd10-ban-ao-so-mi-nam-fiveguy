app.controller("ProductController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.product = {collar: {}, wrist: {}, material: {}, category: {}, supplier: {}, images: [], productDetails: []};
    $scope.productUpdate = {};
    $scope.products = [];
    $scope.collars = [];
    $scope.wrists = [];
    $scope.materials = [];
    $scope.categories = [];
    $scope.suppliers = [];
    $scope.colors = [];
    $scope.sizes = [];
    $scope.sizeSelected = [];
    $scope.colorSelected = [];
    $scope.color = {};


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
        $scope.product.collar.id = + e.params.data.id;   
        $scope.$apply(); 
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
        $scope.product.wrist.id = + e.params.data.id;
        $scope.$apply();    
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
        $scope.product.material.id = + e.params.data.id;   
        $scope.$apply(); 
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
        $scope.product.category.id = + e.params.data.id;  
        $scope.$apply();  
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
        $scope.product.supplier.id = + e.params.data.id;  
        $scope.$apply(); 
    });
    // End Supplier

    // Begin Size
    $scope.getAllSizes = () => {
        $http.get(`${config.host}/size/all`).then(response => {
            $scope.sizes = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.updateQuantity = function(item) {
       if (item.quantity <= 0) item.quantity = 1;
    };

    $scope.deleteSize = function(item, entity) {
        
        $scope.colorSelected.forEach(color => {
            const index = color.productDetails.indexOf(item);
            if (index > -1) {
                if (color.productDetails.length > 1) {
                    color.productDetails.splice(index, 1);
                } else {
                    toastr["error"]("Phải có ít nhất là 1 size trong danh sách " + entity.name);
                }
            } 
        });
    };

    function isImage(file) {
        return file.name.match(/\.(jpg|jpeg|png|gif|bmp)$/);
    }


    function check_duplicate(name, lst) {
        var status = true;
        if (lst.length > 0) {
            for (e = 0; e < lst.length; e++) {
                if (lst[e].name == name) {
                    status = false;
                    break;
                }
            }
        }
        return status;
    }
    
    $scope.setColor = (color) => {
        $scope.color = color;
    }

    $scope.uploadFile = (event) => {
        let images = event.target.files;
    
        for (let i = 0; i < images.length; i++) {
            let image = images[i];
    
            if (!isImage(image)) {
                toastr.error(image.name + " không đúng định dạng hình ảnh");
                continue;
            }
    
            if (check_duplicate(image.name, $scope.color.images)) {
                if (image.size > 10048576) {
                    toastr.warning(image.name + " có kích thước lớn hơn 10MB");
                    continue;
                }
    
                let reader = new FileReader();
                reader.onload = (function (img) {
                    return function (e) {
                        $scope.$apply(function () {
                            $scope.color.images.push({
                                "name": img.name,
                                "path": e.target.result,
                                "status": 1,
                            });
                        });
                    };
                })(image);
    
                reader.readAsDataURL(image);
            } else {
                toastr.error(image.name + " đã có trong danh sách");
            }
        }
    
        angular.element(event.target).val(null);
    };
    
    $scope.deleteImg = (color, item) => {
        color.images.splice(color.images.indexOf(item), 1);
        
    }

    $scope.productDetails = () => {
        $scope.colorSelected.forEach(color => color.productDetails = []);
        if ($scope.sizeSelected.length > 0 && $scope.colorSelected.length > 0) {
            for (i = 0; i < $scope.colorSelected.length; i++) {
                $scope.sizeSelected.forEach(size => {
                    $scope.colorSelected[i].productDetails.push({
                        size: size,
                        status: 1,
                        quantity: 1
                    })
                })
            }
        } else {
            $scope.colorSelected.forEach(color => color.productDetails = []);
        }
    }
    

    $scope.getAllSizes();

    $('#id-label-size').select2({
        theme: 'bootstrap-5',
        dropdownParent: $("#box-size")
    }).on("change", function (e) { 
        let objIdSize = $('#id-label-size').val();
        $scope.sizeSelected = objIdSize.map(id => $scope.sizes.find(size => size.id.toString() === id));
        $scope.$apply(() => {
            $scope.productDetails();
        });
        
    });
    // End Size

    // Begin Color
    $scope.getAllColors = () => {
        $http.get(`${config.host}/color/all`).then(response => {
            $scope.colors = response.data;
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.getAllColors();

    function formatState (state) {
        if (!state.id) {
            return state.text;
        }
        var $state = $(
            '<span class="d-flex align-items-center"><input type="color" class="form-control form-control-color me-2" value="'+ state.element.value.toLowerCase() +'" />' + state.text + '</span>'
        );
        return $state;
    };

    $('#id-label-color').select2({
        theme: 'bootstrap-5',
        dropdownParent: $("#box-color"),
        templateResult: formatState
    }).on("change", function (e) { 
        let objColorCode = $('#id-label-color').val();
        $scope.colorSelected = objColorCode.map(code => {
            let color = $scope.colors.find(color => color.colorCode.toString() === code);
            return {...color, images: [], productDetails: []}; 
        });        
        $scope.$apply(() => {
            $scope.productDetails();
        });
    });
    // End Color

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

    $scope.checkListImage = () => {
        for (i = 0; i < $scope.colorSelected.length; i++) {
            if ($scope.colorSelected[i].images.length == 0) {
                toastr["warning"]("Vui lòng thêm hình ảnh cho màu " + $scope.colorSelected[i].name);
                return false;
            }
        }
        return true;
    }

    $scope.validated = () => {
        if (!$scope.product.collar.id) {
            toastr["warning"]("Vui lòng chọn cổ áo");
            return false;
        } else if (!$scope.product.wrist.id) {
            toastr["warning"]("Vui lòng chọn cổ tay");
            return false;
        } else if (!$scope.product.material.id) {
            toastr["warning"]("Vui lòng chọn chất liệu");
            return false;
        } else if (!$scope.product.category.id) {
            toastr["warning"]("Vui lòng chọn nhóm sản phẩm");
            return false;
        } else if (!$scope.product.supplier.id) {
            toastr["warning"]("Vui lòng chọn nhà cung cấp");
            return false;
        } else if ($scope.sizeSelected.length == 0) {
            toastr["warning"]("Vui lòng chọn size");
            return false;
        } else if ($scope.colorSelected.length == 0) {
            toastr["warning"]("Vui lòng chọn màu sắc");
            return false;
        } 
        return true;
    }

    $scope.createProduct = () => {
        if ($scope.productForm.$valid &&  $scope.validated() && $scope.checkListImage()) {
            if (!$scope.product.code) {
                $scope.product.code = 'SP' + Number(String(Date.now()).slice(-6));
            }
            $scope.product.images = [];
            $scope.product.productDetails = [];
            $scope.colorSelected.forEach(colorSelect => {
                $scope.colors.forEach(color => {
                    if (colorSelect.id === color.id) {
                        colorSelect.images.forEach(image => {
                            $scope.product.images.push({
                                ...image,
                                color: color
                            });
                        });
                        colorSelect.productDetails.forEach(detail => {
                            $scope.product.productDetails.push({
                                ...detail,
                                color: color
                            });
                        });
                    }
                });
            });
           console.log($scope.product)
           
           $http.post(`${config.host}/product`, $scope.product).then((response) => {
                // $scope.getAllProducts();
                // $scope.resetForm();
                // $('#addProductModel').modal('hide');
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

