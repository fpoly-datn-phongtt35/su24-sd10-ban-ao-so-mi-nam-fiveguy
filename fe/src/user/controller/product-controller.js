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


    
      // Initialize scope variables
      $scope.selectedCategory = null;
      $scope.selectedCollar = null;
      $scope.selectedWrist = null;
      $scope.selectedColor = null;
      $scope.selectedSize = null;
      $scope.selectedMaterial = null;
      $scope.currentPage = 0;
      $scope.pageSize = 10; // Default page size
      $scope.searchTerm = null;
      $scope.minPrice = null;
      $scope.maxPrice = null;
      $scope.totalPages = 0;
      $scope.products = [];
  
      // Function to filter products
  // formatCurrency
$scope.formatCurrency = function(value) {
    if (!value) return '';
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
};

// filterProducts
$scope.filterProducts = function(page) {
    if (page === undefined) {
        page = $scope.currentPage;
    }

    var config = {
        params: {
            name: $scope.searchTerm,
            colorNames: $scope.selectedColor ? [$scope.selectedColor] : null,
            sizeNames: $scope.selectedSize ? [$scope.selectedSize] : null,
            materialNames: $scope.selectedMaterial ? [$scope.selectedMaterial] : null,
            collarNames: $scope.selectedCollar ? [$scope.selectedCollar] : null,
            wristNames: $scope.selectedWrist ? [$scope.selectedWrist] : null,
            minPrice: $scope.minPrice,
            maxPrice: $scope.maxPrice,
            page: page,
            size: $scope.pageSize,
            sortDir: 'asc', // Example sort direction, adjust as needed
            sort: 'price'   // Example sort field, adjust as needed
        }
    };

    return $http.get('http://localhost:8080/api/home/product/filter', config)
        .then(function(response) {
            console.log(response);

            $scope.products = response.data.content.map(product => {
                return product;
            });

            $scope.totalPages = response.data.totalPages;
            $scope.currentPage = page;

            return response.data; // Return data to caller if needed
        }).catch(function(error) {
            console.error('Error fetching products:', error);
            throw error;
        });
};

$scope.setCurrentPage = function(page) {
    if (page >= 0 && page < $scope.totalPages) {
        $scope.filterProducts(page);
    }
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
