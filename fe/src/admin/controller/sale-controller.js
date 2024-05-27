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

    $scope.getAllSales = function() {
        $http.get(baseUrl).then(function(response) {
            $scope.sales = response.data;
        }, function(error) {
            console.error('Error fetching sales data:', error);
        });
    };



    // DÙng để xử lí các thuộc tính của sale

    $scope.formatDate = function(date) {
        var options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        return new Date(date).toLocaleDateString('en-GB', options);
    };

    $scope.getStatusBadge = function(startDate, endDate) {
        var currentDate = new Date();
        if (currentDate >= new Date(startDate) && currentDate <= new Date(endDate)) {
            return "Đang diễn ra";
        } else if (currentDate < new Date(startDate)) {
            return "Sắp tới";
        } else {
            return "Đã kết thúc";
        }
    };

    $scope.getStatusClass = function(startDate, endDate) {
        var currentDate = new Date();
        if (currentDate >= new Date(startDate) && currentDate <= new Date(endDate)) {
            return "bg-success";
        } else if (currentDate < new Date(startDate)) {
            return "bg-primary";
        } else {
            return "bg-secondary";
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
    
    
    $scope.getSale = function(saleId) {
        $http.get(baseUrl + '/' + saleId)
            .then(function(response) {
                $scope.saleDetails = response.data;
            }, function(error) {
                console.error('Error fetching sale details:', error);
            });
    };

    if ($routeParams.id) {
        $scope.getSale($routeParams.id);
    }



    $scope.saveSale = function() {
        var saleData = $scope.saleDetails;

        if (saleData.id == null) {
            saleData.createdAt = new Date()
        }
        $http.post(baseUrl, saleData)
            .then(function(response) {
                console.log('Sale saved successfully', response.data);
                $('#saleModal').modal('hide');
            }, function(error) {
                console.error('Error saving sale:', error);
            });
    };



    $scope.productSales = [];

    $scope.loadProductSales = function() {
        var saleId = $routeParams.id;
        $http.get(baseUrl + '/product-sales/getList/' + saleId ).then(function(response) {
            $scope.productSales = response.data;
            console.log(response.data)
        }, function(error) {
            console.error('Error fetching product sales:', error);
        });
    };


    $scope.products = [];

    $scope.loadProductsWithoutSaleOrExpiredPromotion = function() {
        $http.get(baseUrl + '/products/without-sale-or-expired-promotion')
        .then(function(response) {
            $scope.products = response.data;
        })
        .catch(function(error) {
            console.error('Error fetching products without sale or expired promotion:', error);
        });
    };

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

        if (selectedIds.length > 0 && confirm('Are you sure you want to delete the selected product sales?')) {
            $http.post(baseUrl + '/product-sales/deleteList', selectedIds).then(function(response) {
                // Successfully deleted selected product sales
                console.log('Selected product sales deleted successfully');
                // Refresh the list or remove the deleted items from the view
                // For example:
                $scope.productSales = $scope.productSales.filter(function(productSale) {
                    return !selectedIds.includes(productSale.id);
                });
            }).catch(function(error) {
                // Handle the error
                console.error('Error deleting selected product sales:', error);
            });
        }
    };

    // Function to delete all product sales
    $scope.deleteAll = function() {
        if (confirm('Are you sure you want to delete all product sales?')) {
            $http.delete(baseUrl + '/product-sales/deleteAll').then(function(response) {
                // Successfully deleted all product sales
                console.log('All product sales deleted successfully');
                // Clear the product sales list
                $scope.productSales = [];
            }).catch(function(error) {
                // Handle the error
                console.error('Error deleting all product sales:', error);
            });
        }
    };

// Thêm sản phẩm ra khỏi đợt giảm giá \

    $scope.addSelectedProductSales = function() {
        var selectedProducts = $scope.products.filter(function(product) {
            return product.selected;
        }).map(function(product) {
            var sale =  $scope.saleDetails;
            var discount = 0;

            if (sale.discountType === 1) {
                // Flat discount
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
                console.log('Selected product sales added successfully');
                response.data.forEach(function(newProductSale) {
                    $scope.productSales.push(newProductSale);
                });
            }).catch(function(error) {
                console.error('Error adding selected product sales:', error);
            });
        }
    };
    
    
    


    
    $scope.countCurrentSales();
    $scope.countUpcomingSales();
    $scope.countExpiredSales();
    $scope.getAllSales();








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



}]);
