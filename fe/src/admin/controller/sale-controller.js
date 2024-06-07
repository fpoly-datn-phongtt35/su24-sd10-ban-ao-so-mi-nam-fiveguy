app.controller('SaleController', ['$scope', '$http', '$routeParams', '$location', function($scope, $http, $routeParams, $location) {


    
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

    // $scope.getAllSales = function() {
    //     $http.get(baseUrl).then(function(response) {
    //         $scope.sales = response.data;
    //     }, function(error) {
    //         console.error('Error fetching sales data:', error);
    //     });
    // };

    // $scope.searchSale = function() {
    //     $http.get(baseUrl + '/search', { params: { searchTerm: $scope.searchTerm } })
    //         .then(function(response) {
    //             $scope.sales = response.data;
    //         })
    //         .catch(function(error) {
    //             console.error('Error fetching search results:', error);
    //         });
    // };

    $scope.refreshData = function() {
        $scope.startDate = null;
        $scope.endDate = null;
        $scope.status = null;
        $scope.searchTerm = null;
        $scope.currentPage = 0;

        $scope.getSalesByConditions(0);
    };
    

    $scope.startDate = null;
    $scope.endDate = null;
    $scope.status = null;
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
                // If no data is found and we are not on the first page, reset to first page
                $scope.getSalesByConditions(0);
            } else {
                // Otherwise, update the sales data and pagination
                $scope.sales = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = page;
                console.log($scope.sales);
            }
        }, function(error) {
            console.error('Error fetching sales:', error);
        });
};

$scope.setCurrentPageRateProduct = function(page) {
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

    if ($routeParams.id) {
        $scope.getSale($routeParams.id);
    } else {
        $scope.saleDetail = {
            startDate: null,
            endDate: null
        };
    }



    $scope.saveSale = function() {
        var saleData = $scope.saleDetail;
        $http.post(baseUrl, saleData)
            .then(function(response) {
                console.log('Sale saved successfully', response.data);
                $('#saleModal').modal('hide');
            }, function(error) {
                console.error('Error saving sale:', error);
            });
    };



    // $scope.productSales = [];

    // $scope.filterProductSale = function() {
    //     var saleId = $routeParams.id;
    //     $http.get(baseUrl + '/product-sales/getList/' + saleId ).then(function(response) {
    //         $scope.productSales = response.data;
    //     }, function(error) {
    //         console.error('Error fetching product sales:', error);
    //     });
    // };

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
        var saleId = $routeParams.id ? parseInt($routeParams.id, 10) : null;
    
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
    
    $scope.filterProductSale(0);
      
    
    
    // $scope.products = [];

    // $scope.loadProductsWithoutSaleOrExpiredPromotion = function() {
    //     $http.get(baseUrl + '/products/without-sale-or-expired-promotion')
    //     .then(function(response) {
    //         $scope.products = response.data;
    //     })
    //     .catch(function(error) {
    //         console.error('Error fetching products without sale or expired promotion:', error);
    //     });
    // };

// Xóa sản phẩm ra khỏi đợt giảm giá \
   
   $scope.confirmRemove = function(productSale) {
    if (confirm('Are you sure you want to delete this product sale?')) {
        $scope.deleteProductSale(productSale.id);
    }
};


    $scope.deleteSelected = function() {
        var selectedIds = $scope.productSales.filter(function(productSale) {
            return productSale.selected;
        }).map(function(productSale) {
            return productSale.id;
        });
            $http.post(baseUrl + '/product-sales/deleteList', selectedIds).then(function(response) {
                $scope.productSales = $scope.productSales.filter(function(productSale) {
                    return !selectedIds.includes(productSale.id);
                });
            }).catch(function(error) {
                console.error('Error deleting selected product sales:', error);
            });
    };

    $scope.deleteAll = function() {
            $http.delete(baseUrl + '/product-sales/deleteAll').then(function(response) {
                $scope.productSales = [];
            }).catch(function(error) {
                console.error('Error deleting all product sales:', error);
            });
    };

// Thêm sản phẩm  đợt giảm giá \

    $scope.addSelectedProductSales = function() {
        var selectedProducts = $scope.products.filter(function(product) {
            return product.selected;
        }).map(function(product) {
            var sale =  $scope.saleDetail;
            var discount = 0;

            if (sale.discountType === 1) {
                discount = sale.value;
            } else if (sale.discountType === 2) {
                discount = product.price * (sale.value / 100);
            }
            if (sale.maximumDiscountAmount && discount > sale.maximumDiscountAmount) {
                discount = sale.maximumDiscountAmount;
            }
            var promotionalPrice = product.price - discount;
            return {
                product: product,
                sale: sale,
                promotionalPrice: promotionalPrice,
                discountPrice: discount
            };
        });
        if (selectedProducts.length > 0) {
            $http.post(baseUrl + '/product-sales/addList', selectedProducts).then(function(response) {
                response.data.forEach(function(newProductSale) {
                    $scope.productSales.push(newProductSale);
                });
            }).catch(function(error) {
                console.error('Error adding selected product sales:', error);
            });
        }
    };
    
    // Thêm tất cả sản phẩm vào đợt giảm giá
$scope.addAllProductSales = function() {
    var allProducts = $scope.products.map(function(product) {
        var sale = $scope.saleDetail;
        var discount = 0;
console.log(sale)
        if (sale.discountType === 1) {
            discount = sale.value;
        } else if (sale.discountType === 2) {
            discount = product.price * (sale.value / 100);
        }
        if (sale.maximumDiscountAmount && discount > sale.maximumDiscountAmount) {
            discount = sale.maximumDiscountAmount;
        }
        var promotionalPrice = product.price - discount;
        return {
            product: product,
            sale: sale,
            promotionalPrice: promotionalPrice,
            discountPrice: discount
        };
    });

    if (allProducts.length > 0) {
        $http.post(baseUrl + '/product-sales/addList', allProducts).then(function(response) {
            response.data.forEach(function(newProductSale) {
                $scope.productSales.push(newProductSale);
            });
        }).catch(function(error) {
            console.error('Error adding all product sales:', error);
        });
    }
};

    


    
    $scope.countCurrentSales();
    $scope.countUpcomingSales();
    $scope.countExpiredSales();
    // $scope.getAllSales();








// Fill Product

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
                // Check if the response has data
                if (response.data.content.length === 0 && page > 0) {
                    // If no data is found and we are not on the first page, reset to first page
                    $scope.filterProducts(0);
                } else {
                    // Otherwise, update the products data and pagination
                    $scope.products = response.data.content;
                    $scope.totalPages = response.data.totalPages;
                    $scope.currentPage = page;
                    console.log($scope.products);
                }
            }, function(error) {
                console.error('Error fetching products:', error);
            });
    };


    $scope.setCurrentPageRateProduct = function(page) {
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


}]);
