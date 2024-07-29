
app.controller("ProductController", function($scope, $http, $timeout){
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC',
        min: 500,
        max: 10000000
    };

    $scope.priceGap = 1000;

    $scope.updateRange = function(type) {
        if (type === 'min' && ($scope.filter.max - $scope.filter.min) < $scope.priceGap) {
            $scope.filter.min = $scope.filter.max - $scope.priceGap;
        } else if (type === 'max' && ($scope.filter.max - $scope.filter.min) < $scope.priceGap) {
            $scope.filter.max = $scope.filter.min + $scope.priceGap;
        }
        $scope.updateProgressStyle();
    };

    $scope.updateInput = function(type) {
        if (type === 'min' && ($scope.filter.max - $scope.filter.min) < $scope.priceGap) {
            $scope.filter.min = $scope.filter.max - $scope.priceGap;
        } else if (type === 'max' && ($scope.filter.max - $scope.filter.min) < $scope.priceGap) {
            $scope.filter.max = $scope.filter.min + $scope.priceGap;
        }
        $scope.updateProgressStyle();
    };

    $scope.updateProgressStyle = function() {
        var minPercentage = (($scope.filter.min - 500) / (10000000 - 500)) * 100;
        var maxPercentage = 100 - (($scope.filter.max - 500) / (10000000 - 500)) * 100;
        $scope.progressStyle = {
            left: minPercentage + '%',
            right: maxPercentage + '%'
        };
    };

    // Initialize progress style
    $scope.updateProgressStyle();

    $scope.page = 0;
    $scope.size = 5;
    
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
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/product`, 
                {params: {page: $scope.page, size: $scope.size, keyword: $scope.filter.keyword,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                minPrice: $scope.filter.min,
                maxPrice: $scope.filter.max,
                status: $scope.filter.status
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.products = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
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
       if (item.quantity < 0) item.quantity = 1;
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
        const sizeExistsInColor = $scope.colorSelected.some(c => c.productDetails.some(detail => detail.size.id === item.size.id));
        if (!sizeExistsInColor) {
            const updatedSelect = $('#id-label-size').val().filter(id => id !== item.size.id.toString());
            $timeout(() => {
                $('#id-label-size').val(updatedSelect).trigger('change');
            });
           
        }
    };

    $scope.hasMissingSizes = function(color) {
        let missingSizes = $scope.sizeSelect.filter(sizeSelectItem => {
            return !color.productDetails.some(productDetail => productDetail.size.id === sizeSelectItem.id);
        });
        return missingSizes.length > 0;
    };
    
    $scope.addMissingSizes = function(color) {
        $scope.sizeSelect.forEach(sizeSelectItem => {
            if (!color.productDetails.some(productDetail => productDetail.size.id === sizeSelectItem.id)) {
                let size = $scope.sizes.find(s => s.id == sizeSelectItem.id);
                color.productDetails.push({ size: size, status: 1, quantity: 0, barcode: $scope.createBarcode(), isNew: true});
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
        const sizeExistsInColor = $scope.colorSelect.some(c => c.productDetails.some(detail => detail.size.id === item.size.id));
        if (!sizeExistsInColor) {
            const updatedSelect = $('#id-update-size').val().filter(id => id !== item.size.id.toString());
            $timeout(() => {
                $('#id-update-size').val(updatedSelect).trigger('change');
            });
            $scope.sizeSelect = $scope.sizeSelect.filter(size =>
                size.id !== item.size.id
            );
        }
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
                            if ($scope.color.images.length >= 10) {
                                toastr.warning("Chỉ được thêm tối đa 10 ảnh");
                                return;
                            }
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
                if (image.size > 10048576) {
                    toastr.warning(image.name + " có kích thước lớn hơn 10MB");
                    continue;
                }
                let reader = new FileReader();
                reader.onload = (function (img) {
                    return function (e) {
                        $scope.$apply(function () {                      
                                let existingImage = $scope.color.images.find(item => item.name == img.name && item.status == 0);
                                if (existingImage) {
                                    existingImage.status = 1; // Cập nhật trạng thái ảnh thành 1           
                                }
                                else {
                                    if ($scope.color.images.length >= 10) {
                                        toastr.warning("Chỉ được thêm tối đa 10 ảnh");
                                        return;
                                    }
                                    if (check_duplicate(image.name, $scope.color.images)) {
                                        $scope.color.images.push({
                                            "name": img.name,
                                            "path": e.target.result,
                                            "status": 1,
                                            "isNew": true
                                        });
                                    } else {
                                        toastr.error(image.name + " đã có trong danh sách");
                                    }   
                                }
                        });
                    };
                })(image);
        
                reader.readAsDataURL(image);
        }
        angular.element(event.target).val(null);
    };
    
    $scope.reloadImage = function(color, item) {
        let input = document.createElement('input');
        input.type = 'file';
        input.accept = 'image/*';
    
        // Trigger the file selection dialog
        input.click();
    
        input.onchange = function(event) {
            let file = event.target.files[0];
            
            if (file && isImage(file)) {
                let reader = new FileReader();
                reader.onload = function(e) {
                    $scope.$apply(function() {
                        // Update the current image's name and path
                        item.name = file.name;
                        item.path = e.target.result;
                        item.status = 1;
                    });
                };
                reader.readAsDataURL(file);
            } else {
                toastr.error("Định dạng hình ảnh không hợp lệ hoặc không có tệp nào được chọn");
            }
        };
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

    $scope.getAllSizes();

    $('#id-label-size').select2({
        theme: 'bootstrap-5',
        dropdownParent: $("#box-size")
    }).on("change", function (e) {
        $scope.$apply(() => {
            let objIdSize = $('#id-label-size').val();
            $scope.sizeSelected = objIdSize.map(id => $scope.sizes.find(size => size.id.toString() === id));
                $scope.colorSelected.forEach(color => {
                    color.productDetails = color.productDetails.filter(detail =>
                        $scope.sizeSelected.some(size => size.id === detail.size.id)
                    );
                    
                    $scope.sizeSelected.forEach(size => {
                        if (!color.productDetails.some(detail => detail.size.id === size.id)) {
                            color.productDetails.push({
                                size: size,
                                status: 1,
                                quantity: 0,
                                barcode: $scope.createBarcode(),
                            });
                        }
                    });
                });
        });
    });
    
    
    $('#id-update-size').select2({
        theme: 'bootstrap-5',
        dropdownParent: $("#box-update-size")
    }).on("select2:select", function (e) {
        $scope.$apply(() => {
            let size = $scope.sizes.find(s => s.id == e.params.data.id);
            if (size) { 
                $scope.colorSelect = $scope.colorSelect.map(color => {
                    color.productDetails.push({
                        size: size,
                        quantity: 0,
                        status: 1,
                        barcode: $scope.createBarcode(),
                        isNew: true
                    });
                return color; 
                });
                $scope.sizeSelect.push(size);
            }
        });
    }).on("select2:unselecting", function (e) {
        const sizeExistsInColor = $scope.colorSelect.some(color =>
            color.productDetails.some(detail =>
                detail.isNew === true
            )
        );
        if (!sizeExistsInColor) {
            e.preventDefault();
        } else {
            $scope.$apply(() => {
                $scope.colorSelect = $scope.colorSelect.map(color => {
                    color.productDetails = color.productDetails.filter(detail =>
                        detail.size.id.toString() !== e.params.args.data.id
                    );
                    return color;
                });
                $scope.sizeSelect = $scope.sizeSelect.filter(size =>
                    size.id.toString() !== e.params.args.data.id
                );
            })
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
        $scope.$apply(() => {
            let newColorSelected = objColorCode.map(code => {
                let existingColor = $scope.colorSelected.find(color => color.colorCode.toString() === code);
                if (existingColor) {
                    return existingColor;
                } else {
                    let color = $scope.colors.find(color => color.colorCode.toString() === code);
                    let productDetails = $scope.sizeSelected.map(size => ({
                        size: size,
                        status: 1,
                        quantity: 0,
                        barcode: $scope.createBarcode()
                    }));
                    return {
                        ...color,
                        images: [],
                        productDetails: productDetails ? productDetails : []
                    };
                }
            });
    
            $scope.colorSelected = newColorSelected;
        });
    });
    

    $('#id-update-color').select2({
        theme: 'bootstrap-5',
        dropdownParent: $("#box-update-color"),
        templateResult: formatState
    }).on("select2:select", function (e) { 
        $scope.$apply(() => {
            let color = $scope.colors.find(color => color.colorCode.toString() === e.params.data.id);
            let productDetails = $scope.sizeSelect.length > 0 ? 
                $scope.sizeSelect.map(size => ({
                    size: size,
                    status: 1,
                    quantity: 0,
                    barcode: $scope.createBarcode(),
                    isNew: true
                })) : [];
            
            if (color) {
                $scope.colorSelect.push({
                    ...color,
                    images: [],
                    productDetails: productDetails
                });
            }     
        });  
         
    }).on("select2:unselecting", function(e) {
        let unselectedColorName = $scope.colorSelect.find(color => color.colorCode.toString() === e.params.args.data.id).colorCode;
        $scope.colorUpdateSelected.forEach(c => {
            if (c.colorCode === unselectedColorName) {
                e.preventDefault();
            } 
        })
        let isUniqueColorCode = ! $scope.colorUpdateSelected.some(c => c.colorCode ===  e.params.args.data.id);
        if (isUniqueColorCode) {
           $scope.$apply(() => {
            $scope.colorSelect = $scope.colorSelect.filter(c => c.colorCode != e.params.args.data.id)
           })
        }
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
                $scope.$apply(() => {
                    $scope.colorSelect = $scope.getColorCodes().map(code => {
                        let color = $scope.colors.find(color => color.colorCode.toString() === code);
                        let colorUpdate = $scope.colorUpdateSelected.find(color => color.colorCode.toString() === code);
                        return {...color, images: colorUpdate ? colorUpdate.images : [],  productDetails: colorUpdate ? colorUpdate.productDetails : []}; 
                    });  
                })
                $('#id-update-size').val($scope.getSizes()).trigger('change');
                $scope.$apply(() => {
                    let objIdSize = $('#id-update-size').val();
                    $scope.sizeSelect = objIdSize.map(id => $scope.sizes.find(size => size.id.toString() === id));
                });  
                $('#id-update-color').val($scope.getColorCodes()).trigger('change');
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
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/product/status/${$scope.product.id}`).then(response => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            $('#updateStatusModel').modal('hide');
            $scope.getAllProducts();
            $scope.product = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'none');
            $('#loadingStatus').css('display', 'inline-block');
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
        else if ($scope.product.importPrice >= $scope.product.price) {
            toastr["warning"]("Giá nhập phải nhỏ hơn giá bán");
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
        } else if ($scope.productUpdate.importPrice >= $scope.productUpdate.price) {
            toastr["warning"]("Giá nhập phải nhỏ hơn giá bán");
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
            $('#id-label-color').val([]).trigger('change');
            $('#id-label-size').val([]).trigger('change');
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
            $('#addProduct').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/product`, $scope.product).then((response) => {
                $('#addProduct').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addProductModel').modal('hide');
                $scope.getAllProducts();
                $scope.resetForm();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addProduct').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
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
            $('#updateProduct').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/product/${$scope.productUpdate.id}`, $scope.productUpdate).then((response) => {
                $('#updateProduct').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editProductModel').modal('hide');
                $scope.getAllProducts();
                $scope.resetFormUpdate();
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
           }).catch(error => {
            $('#updateProduct').css('display', 'inline-block');
            $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) {
                    $scope.errorsUpdate = error.data;
                    $scope.err(error.data);
                } 
                else toastr["error"](error);
           })
        }
    }

    $scope.delete = () => {
        $('#deleteProduct').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/product/${$scope.product.id}`).then(response => {
            $('#deleteProduct').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $('#deleteProductModel').modal('hide');
            $scope.getAllProducts();
            $scope.product = {};
            toastr["success"]("Ngừng kinh doanh " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteProduct').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
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
      return parseInt(input).toLocaleString('vi-VN') + ' VND';
    };
  });
  

