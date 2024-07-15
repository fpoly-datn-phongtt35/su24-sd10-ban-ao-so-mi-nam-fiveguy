app.controller("ProductController", function($scope, $http, $timeout){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.product = {collar: {}, wrist: {}, material: {}, category: {}, supplier: {}, images: [], productDetails: [], status: 1};
    $scope.productUpdate = {collar: {}, wrist: {}, material: {}, category: {}, supplier: {}, images: [], productDetails: [], status: 1};
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
    $scope.sizeSelect = [];
    $scope.colorSelect = [];
    $scope.color = {};

    $scope.createBarcode = () => {
        var val1 = Math.floor(100000 + Math.random() * 999999);
        var val2 = Math.floor(10000 + Math.random() * 99999);
        return '7+'+val1+'+'+val2;
    }
    

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
        $scope.$apply(); 
    });

    $('#id-update-collar').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-update-collar")
    }).on("select2:select", function (e) { 
        $scope.productUpdate.collar.id = e.params.data.id;   
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
        $scope.product.wrist.id = e.params.data.id;
        $scope.$apply();    
    });

    $('#id-update-wrist').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-update-wrist")
    }).on("select2:select", function (e) { 
        $scope.productUpdate.wrist.id = e.params.data.id;
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
        $scope.product.material.id = e.params.data.id;   
        $scope.$apply(); 
    });

    $('#id-update-material').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-update-material")
    }).on("select2:select", function (e) { 
        $scope.productUpdate.material.id = e.params.data.id;   
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
        $scope.product.category.id = e.params.data.id;  
        $scope.$apply();  
    });

    $('#id-update-category').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-update-category")
    }).on("select2:select", function (e) { 
        $scope.productUpdate.category.id = e.params.data.id;  
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
        $scope.product.supplier.id = e.params.data.id;  
        $scope.$apply(); 
    });

    $('#id-update-supplier').select2( {
        theme: "bootstrap-5",
        placeholder: $(this).data('placeholder'),
        dropdownParent: $("#box-update-supplier")
    }).on("select2:select", function (e) { 
        $scope.productUpdate.supplier.id = e.params.data.id;  
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

    
    $scope.deleteSizeUpdate = function(item, entity) {
        
        $scope.colorSelect.forEach(color => {
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

    $scope.uploadFileUpdate = (event) => {
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
                            // Kiểm tra nếu ảnh đã tồn tại trong $scope.colorUpdateSelected
                            let imageExists = false;
                            for (let color of $scope.colorUpdateSelected) {
                                let existingImage = color.images.find(item => item.name === img.name);
                                if (existingImage) {
                                    existingImage.status = 1; // Cập nhật trạng thái ảnh thành 1
                                    imageExists = true;
                                    break;
                                }
                            }
        
                            if (!imageExists) {
                                // Nếu ảnh chưa tồn tại, thêm ảnh mới vào $scope.color.images
                                $scope.color.images.push({
                                    "name": img.name,
                                    "path": e.target.result,
                                    "status": 1,
                                });
                            }
                        });
                    };
                })(image);
        
                reader.readAsDataURL(image);
            }  else {
                toastr.error(image.name + " đã có trong danh sách");
            }     
        }
    
        angular.element(event.target).val(null);
    };
    
    
    $scope.deleteImg = (color, item) => {
        color.images.splice(color.images.indexOf(item), 1);
        
    }

    $scope.toggleImgStatus = function(color, item) {
        let existingColor = $scope.colorUpdateSelected.find(c => c.colorCode === color.colorCode);
        if (existingColor) {
            let existingImage = existingColor.images.find(img => img.path === item.path);
            if (existingImage.id) {
                item.status = (item.status === 1) ? 0 : 1;
            } else if (existingImage.id == null) {
                color.images.splice(color.images.indexOf(item), 1);
            }
        } else {
            color.images.splice(color.images.indexOf(item), 1);
        }
    };

    $scope.productDetails = () => {
        $scope.colorSelected.forEach(color => color.productDetails = []);
        if ($scope.sizeSelected.length > 0 && $scope.colorSelected.length > 0) {
            for (i = 0; i < $scope.colorSelected.length; i++) {
                $scope.sizeSelected.forEach(size => {
                    $scope.colorSelected[i].productDetails.push({
                        size: size,
                        status: 1,
                        quantity: 1,
                        barcode:  $scope.createBarcode(),
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

    
    $('#id-update-size').select2({
        theme: 'bootstrap-5',
        dropdownParent: $("#box-update-size")
    }).on("change", function (e) {
        $scope.$apply(() => {
            let objIdSize = $('#id-update-size').val();
            $scope.sizeSelect = objIdSize.map(id => $scope.sizes.find(size => size.id.toString() === id));
    
            $scope.colorSelect.forEach(color => {
                color.productDetails = color.productDetails.filter(detail =>
                    $scope.sizeSelect.some(size => size.id === detail.size.id)
                );
    
                $scope.sizeSelect.forEach(size => {
                    if (!color.productDetails.some(detail => detail.size.id === size.id)) {
                        color.productDetails.push({
                            size: size,
                            status: 1,
                            quantity: 1,
                            barcode: $scope.createBarcode(),
                            isNew: true
                        });
                    }
                });
            });
        });
    }).on("select2:unselecting", function (e) {
        let unselectingSizeId = e.params.args.data.id;
        let preventUnselect = false;
    
        $scope.colorSelect.forEach(color => {
            if (color.productDetails.some(detail => detail.size.id.toString() === unselectingSizeId)) {
                let matchingColorUpdate = $scope.colorUpdateSelected.find(colorUpdate => colorUpdate.colorCode === color.colorCode);
    
                if (matchingColorUpdate) {
                    let matchingSizeInColorUpdate = matchingColorUpdate.productDetails.some(detail => detail.size.id.toString() === unselectingSizeId);
                    if (matchingSizeInColorUpdate) {
                        preventUnselect = true;
                    }
                }
            }
        });
    
        if (preventUnselect) {
            e.preventDefault();
        }
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

    $('#id-update-color').select2({
        theme: 'bootstrap-5',
        dropdownParent: $("#box-update-color"),
        templateResult: formatState
    }).on("change", function (e) { 
        $scope.$apply(() => {
            let objColorCode = $('#id-update-color').val();
        
            $scope.colorSelect = objColorCode.map(code => {
                let color = $scope.colors.find(color => color.colorCode.toString() === code);
                let colorUpdate = $scope.colorUpdateSelected.find(color => color.colorCode.toString() === code);
                
                let productDetails = colorUpdate ? colorUpdate.productDetails : $scope.sizeSelect.map(size => ({
                    size: size,
                    status: 1,
                    quantity: 1,
                    barcode: $scope.createBarcode(),
                    isNew: true
                }));
                
                return {...color, images: colorUpdate ? colorUpdate.images : [], productDetails: productDetails}; 
            });  
        });  
         
    }).on("select2:unselecting", function(e) {
        let unselectedColorName = $scope.colorSelect.find(color => color.colorCode.toString() === e.params.args.data.id).colorCode;
        $scope.colorUpdateSelected.forEach(c => {
            if (c.colorCode === unselectedColorName) {
                e.preventDefault();
            }
        })
    })
   
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

    $scope.getAllProducts();

    $scope.resetFormUpdate = () => {
        $scope.productUpdate = {collar: {}, wrist: {}, material: {}, category: {}, supplier: {}, images: [], productDetails: [], status: 1};
        $scope.errorsUpdate = [];
        $timeout(() => {
            $('#id-update-collar').val(null).trigger('change');
            $('#id-update-wrist').val(null).trigger('change');
            $('#id-update-material').val(null).trigger('change');
            $('#id-update-category').val(null).trigger('change');
            $('#id-update-supplier').val(null).trigger('change');
            $('#id-update-color').val(null).trigger('change');
            $('#id-update-size').val(null).trigger('change');
            $scope.colorSelect = [];
            $scope.sizeSelect = [];
        });
    }

    $scope.editProduct = (key) => {
        $http.get(`${config.host}/product/${key}`).then((response) => {
            $scope.productUpdate = response.data;
            $timeout(() => {
                ['collar', 'wrist', 'material', 'category', 'supplier'].forEach(field => {
                    $(`#id-update-${field}`).val($scope.productUpdate[field].id).trigger('change');
                });
            });
    
            const groupByColor = (items, type) => {
                return items.reduce((acc, item) => {
                    const colorCode = item.color.colorCode;
                    acc[colorCode] = acc[colorCode] || { productDetails: [], images: [] };
                    acc[colorCode][type].push(item);
                    return acc;
                }, {});
            };
    
            const fetchImages = $http.get(`${config.host}/image/${response.data.id}`);
            const fetchProductDetails = $http.get(`${config.host}/product-detail/${response.data.id}`);
    
            Promise.all([fetchImages, fetchProductDetails]).then(([imageResponse, detailResponse]) => {
                const groupedImages = groupByColor(imageResponse.data, 'images');
                const groupedDetails = groupByColor(detailResponse.data.map(({ color, ...detail }) => ({ color, ...detail })), 'productDetails');
    
                const allColorCodes = new Set([...Object.keys(groupedImages), ...Object.keys(groupedDetails)]);
    
                $scope.colorUpdateSelected = Array.from(allColorCodes).map(colorCode => ({
                    colorCode,
                    productDetails: groupedDetails[colorCode] ? groupedDetails[colorCode].productDetails : [],
                    images: groupedImages[colorCode] ? groupedImages[colorCode].images : [],
                }));
    
                $scope.getColorCodes = () => $scope.colorUpdateSelected.map(color => color.colorCode);
                $scope.getSizes = () => {
                    const sizes = new Set();
                    $scope.colorUpdateSelected.forEach(color => {
                        color.productDetails.forEach(c => sizes.add(c.size.id));
                    });
                    return Array.from(sizes);
                };
                $('#id-update-color').val($scope.getColorCodes()).trigger('change');
                $('#id-update-size').val($scope.getSizes()).trigger('change');
            }).catch(error => console.error("Error", error));
        }).catch(error => console.error("Error", error));
    };
    

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
                toastr["warning"]("Vui lòng thêm hình ảnh cho " + $scope.colorSelected[i].name);
                return false;
            }
        }
        return true;
    }

    
    $scope.checkListImageUpdate = () => {
        for (i = 0; i < $scope.colorSelect.length; i++) {
            if ($scope.colorSelect[i].images.length == 0) {
                toastr["warning"]("Vui lòng thêm hình ảnh cho " + $scope.colorSelect[i].name);
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
        } else if ($scope.sizeSelected.length == 0 && $scope.colorSelected.length > 0) {
            toastr["warning"]("Vui lòng chọn size");
            return false;
        } else if ($scope.colorSelected.length == 0 && $scope.sizeSelected.length > 0) {
            toastr["warning"]("Vui lòng chọn màu sắc");
            return false;
        } 
        return true;
    }

    $scope.validatedUpdate = () => {
        if (!$scope.productUpdate.collar.id) {
            toastr["warning"]("Vui lòng chọn cổ áo");
            return false;
        } else if (!$scope.productUpdate.wrist.id) {
            toastr["warning"]("Vui lòng chọn cổ tay");
            return false;
        } else if (!$scope.productUpdate.material.id) {
            toastr["warning"]("Vui lòng chọn chất liệu");
            return false;
        } else if (!$scope.productUpdate.category.id) {
            toastr["warning"]("Vui lòng chọn nhóm sản phẩm");
            return false;
        } else if (!$scope.productUpdate.supplier.id) {
            toastr["warning"]("Vui lòng chọn nhà cung cấp");
            return false;
        } else if ($scope.sizeSelect.length == 0 && $scope.colorSelect.length > 0) {
            toastr["warning"]("Vui lòng chọn size");
            return false;
        } else if ($scope.colorSelect.length == 0 && $scope.sizeSelect.length > 0) {
            toastr["warning"]("Vui lòng chọn màu sắc");
            return false;
        } 
        return true;
    }

    $scope.err = (err) => {
        if (err.code) {
            toastr["error"](err.code);
        } else if (err.name) {
            toastr["error"](err.name);
        } else if (err.price) {
            toastr["error"](err.price);
        } else if (err.supplier) {
            toastr["error"](err.supplier);
        } else if (err.material) {
            toastr["error"](err.material);
        } else if (err.wrist) {
            toastr["error"](err.wrist);
        } else if (err.collar) {
            toastr["error"](err.collar);
        } else if (err.category) {
            toastr["error"](err.category);
        }
    }

    $scope.resetForm = () => {
        $scope.product = {collar: {}, wrist: {}, material: {}, category: {}, supplier: {}, images: [], productDetails: [], status: 1};
        $scope.errors = [];
        $timeout(() => {
            $('#id-label-collar').val(null).trigger('change');
            $('#id-label-wrist').val(null).trigger('change');
            $('#id-label-material').val(null).trigger('change');
            $('#id-label-category').val(null).trigger('change');
            $('#id-label-supplier').val(null).trigger('change');
            $('#id-label-color').val(null).trigger('change');
            $('#id-label-size').val(null).trigger('change');
            $scope.colorSelected = [];
            $scope.sizeSelected = [];
        });
    }

    $scope.createProduct = () => {
        if ($scope.productForm.$valid && $scope.validated()) {
            if (!$scope.product.code) {
                $scope.product.code = 'SP' + Number(String(Date.now()).slice(-6));
            }
            if ($scope.colorSelected.length > 0 && $scope.sizeSelected.length > 0) {
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
                if (!$scope.checkListImage()) return;
               
            }
           $http.post(`${config.host}/product`, $scope.product).then((response) => {

                $scope.getAllProducts();
                $scope.resetForm();
                $('#addProductModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) {
                    $scope.errors = error.data;
                    $scope.err(error.data);
                } 
                else toastr["error"](error);
           })
        }
    }

    $scope.updateProduct = () => {
        if ($scope.productFormUpdate.$valid && $scope.validatedUpdate()) {
            if (!$scope.productUpdate.code) {
                $scope.productUpdate.code = 'SP' + Number(String(Date.now()).slice(-6));
            }
            if ($scope.colorSelect.length > 0 && $scope.sizeSelect.length > 0) {
                $scope.productUpdate.images = [];
                $scope.productUpdate.productDetails = [];
                $scope.colorSelect.forEach(c => {
                    $scope.colors.forEach(color => {
                        if (c.id === color.id) {
                            c.images.forEach(image => {
                                $scope.productUpdate.images.push({
                                    ...image,
                                    color: color
                                });
                            });
                            c.productDetails.forEach(detail => {
                                $scope.productUpdate.productDetails.push({
                                    ...detail,
                                    color: color
                                });
                            });
                        }
                    });
                });
                if (!$scope.checkListImageUpdate()) return;
            }
            $http.put(`${config.host}/product/${$scope.productUpdate.id}`, $scope.productUpdate).then((response) => {
                $scope.getAllProducts();
                $scope.resetFormUpdate();
                $('#editProductModel').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) {
                    $scope.errorsUpdate = error.data;
                    $scope.err(error.data);
                } 
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

  app.filter('vndCurrency', function() {
    return function(input) {
      if (isNaN(input)) {
        return input;
      }
      return parseInt(input).toLocaleString('vi-VN') + '₫';
    };
  });
  

