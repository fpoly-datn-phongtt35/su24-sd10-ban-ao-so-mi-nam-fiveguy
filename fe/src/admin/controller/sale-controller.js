app.controller('SaleController', ['$scope', '$http', '$routeParams', '$location', function($scope, $http, $routeParams, $location) {

  // notify

  toastr.options = {
    "closeButton": false,
    "debug": false,
    "newestOnTop": true,
    "progressBar": false,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
  }
  
  // Hàm hiển thị thông báo thành công
  $scope.showSuccessNotification = function(message) {
  toastr["success"](message);
  };
  
  // Hàm hiển thị thông báo lỗi
  $scope.showErrorNotification = function(message) {
    toastr["error"](message);
  };
  
  
  $scope.showWarningNotification = function(message) {
    toastr["warning"](message);
  };
    
    var baseUrl = 'http://localhost:8080/api/admin/sales';


    $scope.countCurrentSales = function() {
        $http.get(baseUrl + '/current/count').then(function(response) {
            $scope.currentSalesCount = response.data;
        });
    };

    $scope.countUpcomingSales = function() {
        $http.get(baseUrl + '/upcoming/count').then(function(response) {
            $scope.upcomingSalesCount = response.data;
        });
    };

    $scope.countExpiredSales = function() {
        $http.get(baseUrl + '/expired/count').then(function(response) {
            $scope.expiredSalesCount = response.data;
        });
    };


    $scope.refreshData = function() {
        $scope.startDate = null;
        $scope.endDate = null;
        $scope.status = null;
        $scope.discountType = null;
        $scope.searchTerm = null;
        $scope.currentPage = 0;
    
        $scope.getSalesByConditions(0);
    };
    
    $scope.startDate = null;
    $scope.endDate = null;
    $scope.status = null;
    $scope.discountType = null; 
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    
    $scope.getSalesByConditions = function(page) {
        if (page === undefined) {
            page = $scope.currentPage;
        }
    
        var config = {
            params: {
                startDate: $scope.startDate,
                endDate: $scope.endDate,
                status: $scope.status,
                discountType: $scope.discountType, 
                searchTerm: $scope.searchTerm,
                page: page,
                size: $scope.pageSize
            }
        };
        console.log(config);
    
        $http.get(baseUrl + '/fill', config)
            .then(function(response) {
                // Check if the response has data
                if (response.data.content.length === 0 && page > 0) {
                    $scope.getSalesByConditions(0);
                } else {
                    $scope.sales = response.data.content;
                    $scope.totalPages = response.data.totalPages;
                    $scope.currentPage = page;
                }
            }, function(error) {
                console.error('Error fetching sales:', error);
            });
    };
    
    $scope.setCurrentPageSale = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getSalesByConditions(page);
        }
    };
    
    $scope.getSalesByConditions(0); 
    


    // DÙng để xử lí các thuộc tính của sale

    $scope.getStatusText = function(status) {
        switch (status) {
            case 1:
                return "Đang hoạt động";
            case 2:
                return "Sắp bắt đầu";
            case 3:
                return "Hết hạn";
            case 4:
                return "Dừng hoạt động";
            default:
                return "Không xác định"; 
        }
    };
    $scope.getDiscountValueAndType = function(value, type) {
        var typeText = '';
        if (type == 1) {
            typeText = 'đ';
        } else if (type == 2) {
            typeText = '%';
        } else {
            typeText = 'Không xác định';
        }
        return value + ' ' + typeText  ;
    };
    
      function parseDate(dateString) {
        if (!dateString) return null;
        return new Date(dateString);
    }

    $scope.getSale = function(saleId) {
        $http.get(baseUrl + '/' + saleId)
            .then(function(response) {
                var saleData = response.data;
                saleData.startDate = parseDate(saleData.startDate);
                saleData.endDate = parseDate(saleData.endDate);
                $scope.saleDetail = saleData;
                console.log($scope.saleDetail)
                console.log($scope.saleDetail.discountType)
            }, function(error) {
                console.error('Error fetching sale details:', error);
            });
    };

    if ($routeParams.idSale) {
        $scope.getSale($routeParams.idSale);
    } else {
        $scope.saleDetail = {
            startDate: null,
            endDate: null
        };
    }

    

    $scope.fetchAllSales = function() {
        $http.get(baseUrl)
            .then(function(response) {
                $scope.allSales = response.data;
            }, function(error) {
                console.error('Error fetching all sales:', error);
            });
    };
    
    $scope.fetchAllSales();
    
    $scope.checkSaleCode = function(code) {
        return $scope.allSales.some(sale => sale.code === code);
    };

    $scope.checkSaleCodeUpdate = function(code) {
        var updatedSaleId = $routeParams.idSale.toString(); 
        return $scope.allSales.some(function(sale) {
            return sale.code === code && sale.id.toString() !== updatedSaleId; 
        });
    };
    
    
    
    
    
    $scope.saveSale = function() {
        if ($scope.saleForm.$valid) {
            var saleData = $scope.saleDetail;
    
            if ($scope.checkSaleCode(saleData.code)) {
                $scope.showErrorNotification("Mã giảm giá đã tồn tại. Vui lòng chọn mã khác.");
                return;
            }
    
            $http.post(baseUrl, saleData)
                .then(function(response) {
                    console.log('Sale saved successfully', response.data);
                    $('#saleModal').modal('hide');
                    $scope.showSuccessNotification("Thêm đợt giảm giá thành công");
                    $scope.fetchAllSales();
                }, function(error) {
                    $scope.showErrorNotification("Thêm đợt giảm giá thất bại");
                    console.error('Error saving sale:', error);
                });
        } else {
            $scope.saleForm.$setSubmitted();
            $scope.showErrorNotification("Vui lòng điền đầy đủ thông tin.");
        }
    };
    

 // Hàm cập nhật sale
 $scope.updateSale = function() {
    if ($scope.saleFormUpdate.$valid) {
        var saleData = $scope.saleDetail;

        // Kiểm tra mã giảm giá trùng lặp
        if ($scope.checkSaleCodeUpdate(saleData.code)) {
            $scope.showErrorNotification("Mã giảm giá đã tồn tại. Vui lòng chọn mã khác.");
            return;
        }

        // Gửi yêu cầu cập nhật sale
        $http.post(baseUrl, saleData)
            .then(function(response) {
                $scope.showSuccessNotification("Cập nhật đợt giảm giá thành công");
                $scope.fetchAllSales();
            }, function(error) {
                $scope.showErrorNotification("Cập nhật đợt giảm giá thất bại");
                console.error('Error updating sale:', error);
            });
    } else {
        $scope.saleFormUpdate.$setSubmitted();
        $scope.showErrorNotification("Vui lòng điền đầy đủ thông tin.");
    }
};

    




    $scope.getAllProductSale = function() {
        var saleId = $routeParams.idSale;
        // Trả về promise
        return $http.get(baseUrl + '/product-sales/getList/' + saleId).then(function(response) {
            $scope.allProductSales = response.data;
            return response.data;
        }).catch(function(error) {
            console.error('Error fetching product sales:', error);
        });
    };


if (!localStorage.getItem('selectedProductSales')) {
    localStorage.setItem('selectedProductSales', JSON.stringify([]));
}

$scope.clearSelectedProductSales = function() {
    localStorage.removeItem('selectedProductSales');
};

$scope.toggleProductSaleSelection = function(productSale) {
    let selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];

    if (productSale.selected) {
        selectedProductSales.push(productSale);
    } else {
        selectedProductSales = selectedProductSales.filter(function(selectedProductSale) {
            return selectedProductSale.id !== productSale.id;
        });
    }

    localStorage.setItem('selectedProductSales', JSON.stringify(selectedProductSales));
};


$scope.refreshDataProductSale = function() {
    $scope.selectedCategory2 = null;
    $scope.selectedCollar2 = null;
    $scope.selectedWrist2 = null;
    $scope.selectedColor2 = null;
    $scope.selectedSize2 = null;
    $scope.selectedMaterial2 = null;
    $scope.currentPage2 = 0;
    $scope.searchTerm2 = null;

    $scope.filterProductSale(0);
};

$scope.productSales = [];
$scope.currentPage2 = 0;
// $scope.pageSize2 = 10;
$scope.totalPages2 = 0;
$scope.searchTerm2 = null;

$scope.selectedProduct2 = null;
$scope.selectedCategory2 = null;
$scope.selectedCollar2 = null;
$scope.selectedWrist2 = null;
$scope.selectedColor2 = null;
$scope.selectedSize2 = null;
$scope.selectedMaterial2 = null;
$scope.selectedStatus2 = null;

$scope.filterProductSale = function(page) {
    if (page === undefined) {
        page = $scope.currentPage2;
    }
    var saleId = $routeParams.idSale ? parseInt($routeParams.idSale, 10) : null;

    if (isNaN(saleId)) {
        console.error('Invalid saleId:', saleId);
        return;
    }

    var config = {
        params: {
            saleId: saleId,
            productId: $scope.selectedProduct2,
            categoryId: $scope.selectedCategory2,
            collarId: $scope.selectedCollar2,
            wristId: $scope.selectedWrist2,
            colorId: $scope.selectedColor2,
            sizeId: $scope.selectedSize2,
            materialId: $scope.selectedMaterial2,
            status: $scope.selectedStatus2,
            page: page,
            size: $scope.pageSize2,
            searchTerm: $scope.searchTerm2
        }
    };
    console.log(config);

    $http.get(baseUrl + '/product-sales/fillProductSale', config)
        .then(function(response) {
            if (response.data.content.length === 0 && page > 0) {
                $scope.filterProductSale(0);
            } else {
                $scope.productSales = response.data.content;
                $scope.totalPages2 = response.data.totalPages;
                $scope.currentPage2 = page;
                console.log($scope.productSales);

                var selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];

                $scope.productSales.forEach(function(productSale) {
                    productSale.selected = selectedProductSales.some(function(selectedProductSale) {
                        return selectedProductSale.id === productSale.id;
                    });
                });
            }
        }, function(error) {
            console.error('Error fetching product sales:', error);
        });
};

$scope.setCurrentPageProductSales2 = function(page) {
    if (page >= 0 && page < $scope.totalPages2) {
        $scope.filterProductSale(page);
    }
};

// Ensure selected product sales are saved to localStorage when toggled
$scope.toggleProductSelection = function(productSale) {
    let selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];

    if (productSale.selected) {
        selectedProductSales.push(productSale);
    } else {
        selectedProductSales = selectedProductSales.filter(function(selectedProductSale) {
            return selectedProductSale.id !== productSale.id;
        });
    }

    localStorage.setItem('selectedProductSales', JSON.stringify(selectedProductSales));
};

// Initialize the filtering process
$scope.filterProductSale(0);
    
// Xóa sản phẩm ra khỏi đợt giảm giá \
   

$scope.deleteSelected = function() {
    $scope.getAllProductSale().then(function(allProductSales) {
        if (!allProductSales) return; 
        var selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];
        var selectedIds = selectedProductSales.map(function(selectedProductSale) {
            return selectedProductSale.id;
        });
        $http.post(baseUrl + '/product-sales/deleteList', selectedIds).then(function(response) {
            $scope.productSales = $scope.productSales.filter(function(productSale) {
                return !productSale.selected;
            });
            $scope.allProductSales = $scope.allProductSales.filter(function(productSale) {
                return !selectedIds.includes(productSale.id);
            });
            $scope.filterProducts(0);
            $scope.filterProductSale(0);
            $scope.showSuccessNotification("Xóa sản phẩm thành công");
        }).catch(function(error) {
            $scope.showErrorNotification("Xóa sản phẩm thất bại");
            console.error('Error deleting selected product sales:', error);
        });
    });
};


    $scope.deleteAll = function() {
            $http.delete(baseUrl + '/product-sales/deleteAll').then(function(response) {
                $scope.productSales = [];
            $scope.showSuccessNotification("Xóa sản phẩm thành công");
            $scope.filterProducts(0);
            $scope.filterProductSale(0);
            }).catch(function(error) {
            $scope.showErrorNotification("Xóa sản phẩm thất bại");

                console.error('Error deleting all product sales:', error);
            });
    };

    
    $scope.countCurrentSales();
    $scope.countUpcomingSales();
    $scope.countExpiredSales();
    // $scope.getAllSales();


var baseUrlInfoProduct = 'http://localhost:8080/api/admin/infoProduct';

    $scope.wrists = [];
    $scope.sizes = [];
    $scope.materials = [];
    $scope.colors = [];
    $scope.collars = [];
    $scope.brands = [];

    $scope.getAllWrists = function() {
        $http.get(baseUrlInfoProduct + '/wrists').then(function(response) {
            $scope.wrists = response.data;
        }, function(error) {
            console.error('Error fetching wrists:', error);
        });
    };

    $scope.getAllSizes = function() {
        $http.get(baseUrlInfoProduct + '/sizes').then(function(response) {
            $scope.sizes = response.data;
        }, function(error) {
            console.error('Error fetching sizes:', error);
        });
    };

    $scope.getAllMaterials = function() {
        $http.get(baseUrlInfoProduct + '/materials').then(function(response) {
            $scope.materials = response.data;
        }, function(error) {
            console.error('Error fetching materials:', error);
        });
    };

    $scope.getAllColors = function() {
        $http.get(baseUrlInfoProduct + '/colors').then(function(response) {
            $scope.colors = response.data;
        }, function(error) {
            console.error('Error fetching colors:', error);
        });
    };

    $scope.getAllCollars = function() {
        $http.get(baseUrlInfoProduct + '/collars').then(function(response) {
            $scope.collars = response.data;
        }, function(error) {
            console.error('Error fetching collars:', error);
        });
    };

    $scope.getAllBrands = function() {
        $http.get(baseUrlInfoProduct + '/brands').then(function(response) {
            $scope.brands = response.data;
        }, function(error) {
            console.error('Error fetching brands:', error);
        });
    };




    $scope.getAllWrists();
    $scope.getAllSizes();
    $scope.getAllMaterials();
    $scope.getAllColors();
    $scope.getAllCollars();
    $scope.getAllBrands();

    
// Function to calculate discount
function calculateDiscount(sale, product) {
    console.log(discount)
    
        var discount = 0;
        if (sale.discountType === 1) {
            discount = sale.value;
        } else if (sale.discountType === 2) {
            discount = product.price * (sale.value / 100);
        }
        if (sale.maximumDiscountAmount && discount > sale.maximumDiscountAmount) {
            discount = sale.maximumDiscountAmount;
        }
        return discount;
    }

    // Thêm sản phẩm  đợt giảm giá \

    $scope.getAllProduct = function() {
        return $http.get('http://localhost:8080/api/admin/sales/products')
            .then(function(response) {
                return response.data;
            })
            .catch(function(error) {
                console.error('Error fetching products:', error);
            });
    };
    
    $scope.addSelectedProductSales = function() {

        var sale = $scope.saleDetail;
        $scope.getAllProduct().then(function(allProducts) {
            var selectedProducts = JSON.parse(localStorage.getItem('selectedProducts')) || [];
            var selectedProductsFromAll = allProducts.filter(function(product) {
                return selectedProducts.some(function(selectedProduct) {
                    return selectedProduct.id === product.id;
                });
            }).map(function(product) {
                var discount = calculateDiscount(sale, product);
                var promotionalPrice = product.price - discount;
                console.log(discount)
                return {
                    product: product,
                    sale: sale,
                    promotionalPrice: promotionalPrice,
                    discountPrice: discount
                };
            });
    
            if (selectedProductsFromAll.length > 0) {
                $http.post(baseUrl + '/product-sales/addList', selectedProductsFromAll).then(function(response) {
                    response.data.forEach(function(newProductSale) {
                        $scope.productSales.push(newProductSale);
                    });
                    $scope.filterProducts(0);
                    $scope.filterProductSale(0);
                    $scope.showSuccessNotification("Thêm sản phẩm thành công")
                }).catch(function(error) {
                    $scope.showErrorNotification("Thêm sản phẩm thất bại")
                    console.error('Error adding selected product sales:', error);
                });
            }
        });
    };

        // Thêm tất cả sản phẩm vào đợt giảm giá
$scope.addAllProductSales = function() {
    var apiUrl = baseUrl + '/product-sales/addAll/' + $routeParams.idSale;
    $http.post(apiUrl)
        .then(function(response) {
            $scope.productSales = [];
            response.data.forEach(function(newProductSale) {
                $scope.productSales.push(newProductSale);
            });
            $scope.filterProducts(0);
            $scope.filterProductSale(0);
            $scope.showSuccessNotification("Thêm sản phẩm thành công")
        })
        .catch(function(error) {
            $scope.showErrorNotification("Thêm sản phẩm thất bại")
            console.error('Error adding all product sales:', error);
        });
};
    
    if (!localStorage.getItem('selectedProducts')) {
        localStorage.setItem('selectedProducts', JSON.stringify([]));
    }
    
    $scope.clearSelectedProducts = function() {
        localStorage.removeItem('selectedProducts');
    };
    
    $scope.toggleProductSelection = function(product) {
        let selectedProducts = JSON.parse(localStorage.getItem('selectedProducts')) || [];
    
        if (product.selected) {
            selectedProducts.push(product);
        } else {
            selectedProducts = selectedProducts.filter(function(selectedProduct) {
                return selectedProduct.id !== product.id;
            });
        }
    
        localStorage.setItem('selectedProducts', JSON.stringify(selectedProducts));
    };
    

    $scope.refreshDataProduct = function() {
        $scope.selectedCategory = null;
        $scope.selectedCollar = null;
        $scope.selectedWrist = null;
        $scope.selectedColor = null;
        $scope.selectedSize = null;
        $scope.selectedMaterial = null;
        $scope.currentPage = 0;
        $scope.searchTerm = null;


        $scope.filterProducts(0);
    };

    $scope.selectedCategory = null;
    $scope.selectedCollar = null;
    $scope.selectedWrist = null;
    $scope.selectedColor = null;
    $scope.selectedSize = null;
    $scope.selectedMaterial = null;

    $scope.filterProducts = function(page) {
        if (page === undefined) {
            page = $scope.currentPage;
        }
    
        var config = {
            params: {
                categoryId: $scope.selectedCategory,
                collarId: $scope.selectedCollar,
                wristId: $scope.selectedWrist,
                colorId: $scope.selectedColor,
                sizeId: $scope.selectedSize,
                materialId: $scope.selectedMaterial,
                searchTerm: $scope.searchTerm,
                page: page,
                size: $scope.pageSize
            }
        };
    
        $http.get('http://localhost:8080/api/admin/sales/products/filter', config)
            .then(function(response) {
                if (response.data.content.length === 0 && page > 0) {
                    $scope.filterProducts(0);
                } else {
                    $scope.products = response.data.content;
                    $scope.totalPages = response.data.totalPages;
                    $scope.currentPage = page;
                    let selectedProducts = JSON.parse(localStorage.getItem('selectedProducts')) || [];
    
                    $scope.products.forEach(function(product) {
                        var selectedProduct = selectedProducts.find(function(selectedProduct) {
                            return selectedProduct.id === product.id;
                        });
                        product.selected = !!selectedProduct;
                    });
    
                    console.log($scope.products);
                }
            }, function(error) {
                console.error('Error fetching products:', error);
            });
    };
    


    $scope.setCurrentPageRateProduct = function(page) {
        console.log(page)
        console.log($scope.totalPages)
        if (page >= 0 && page < $scope.totalPages) {
            $scope.filterProducts(page);
        }
    };


    $scope.filterProducts(0);
    

    // $scope.searchProducts = function() {
    //     $http.get('http://localhost:8080/api/admin/sales/products/search', {
    //         params: {
    //             searchTerm: $scope.searchTerm
    //         }
    //     }).then(function(response) {
    //         // Xử lý dữ liệu trả về từ API
    //         $scope.products = response.data;
    //     }, function(error) {
    //         // Xử lý lỗi nếu có
    //         console.error('Error searching products:', error);
    //     });
    // };


    // summary sale

    $scope.getSaleSummary = function() {
        var saleId = $routeParams.idSale;

        $http.get(baseUrl + '/summary/' + saleId)
            .then(function(response) {
                console.log(response.data)
                $scope.summary = response.data;
                $scope.summary.profitMargin = ($scope.summary.totalProfit / $scope.summary.totalRevenue) * 100;
            })
            .catch(function(error) {
                console.error('Error fetching sale summary:', error);
            });
    };

}]);