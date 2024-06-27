app.controller("productController", function ($scope, $http, $window) {
    var baseUrlInfoProduct = 'http://localhost:8080/api/admin/infoProduct';

    $scope.sections = {
        colors: false,
        sizes: false,
        materials: false,
        wrists: false,
        collars: false,
        brands: false
    };

    // Function to toggle section visibility
    $scope.toggleSection = function(section) {
        $scope.sections[section] = !$scope.sections[section];
    };

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


      // formatCurrency
$scope.formatCurrency = function(value) {
    if (!value) return '';
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
};


$scope.selectedColors = [];
$scope.selectedSizes = [];
$scope.selectedMaterials = [];
$scope.selectedCollars = [];
$scope.selectedWrists = [];
$scope.searchTerm;

$scope.filterProducts = function(page) {
    if (page === undefined) {
        page = $scope.currentPage;
    }

    var config = {
        params: {
            searchTerm: $scope.searchTerm,
            colorIds: $scope.selectedColors.length > 0 ? $scope.selectedColors : null,
            sizeIds: $scope.selectedSizes.length > 0 ? $scope.selectedSizes : null,
            materialIds: $scope.selectedMaterials.length > 0 ? $scope.selectedMaterials : null,
            collarIds: $scope.selectedCollars.length > 0 ? $scope.selectedCollars : null,
            wristIds: $scope.selectedWrists.length > 0 ? $scope.selectedWrists : null,
            minPrice: $scope.minPrice,
            maxPrice: $scope.maxPrice,
            page: page,
            size: $scope.pageSize,
            sortDir: $scope.selectedSortType == 1 ? 'asc' : ($scope.selectedSortType == 2 ? 'desc' : 'desc'),
            sort: $scope.selectedSortType == 0 ? 'createdAt' : 'price'
        }
    };
    console.log(config);



    return $http.get('http://localhost:8080/api/home/products/filter', config)
        .then(function(response) {
            console.log(response);

            $scope.products = response.data.content.map(product => {
                return product;
            });

            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = page;

            return response.data;
        }).catch(function(error) {
            console.error('Error fetching products:', error);
            throw error;
        });
};

$scope.updateSelectedColors = function(color) {
    if (color.isSelected) {
        $scope.selectedColors.push(color.id);
    } else {
        $scope.selectedColors = $scope.selectedColors.filter(id => id !== color.id);
    }
    $scope.filterProducts();
};

$scope.updateSelectedSizes = function(size) {
    if (size.isSelected) {
        $scope.selectedSizes.push(size.id);
    } else {
        $scope.selectedSizes = $scope.selectedSizes.filter(id => id !== size.id);
    }
    $scope.filterProducts();
};

$scope.updateSelectedMaterials = function(material) {
    if (material.isSelected) {
        $scope.selectedMaterials.push(material.id);
    } else {
        $scope.selectedMaterials = $scope.selectedMaterials.filter(id => id !== material.id);
    }
    $scope.filterProducts();
};

$scope.updateSelectedCollars = function(collar) {
    if (collar.isSelected) {
        $scope.selectedCollars.push(collar.id);
    } else {
        $scope.selectedCollars = $scope.selectedCollars.filter(id => id !== collar.id);
    }
    $scope.filterProducts();
};

$scope.updateSelectedWrists = function(wrist) {
    if (wrist.isSelected) {
        $scope.selectedWrists.push(wrist.id);
    } else {
        $scope.selectedWrists = $scope.selectedWrists.filter(id => id !== wrist.id);
    }
    $scope.filterProducts();
};

$scope.setCurrentPage = function(page) {
    if (page >= 0 && page < $scope.totalPages) {
        $scope.filterProducts(page);
    }
};

$scope.loadPage = function() {
    $scope.filterProducts();
};


      // Function to refresh data (reset filters and reload products)
      $scope.refreshDataProduct = function() {
          $scope.selectedCategory = null;
          $scope.selectedCollar = null;
          $scope.selectedWrist = null;
          $scope.selectedColor = null;
          $scope.selectedSize = null;
          $scope.selectedMaterial = null;
          $scope.currentPage = 0;
          $scope.searchTerm = null;
          $scope.minPrice = null;
          $scope.maxPrice = null;
  
          $scope.filterProducts(0);
      };
  
      // Initial load of products

});
