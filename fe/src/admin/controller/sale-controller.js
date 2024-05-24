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
    
    
    $scope.getSaleDetails = function(saleId) {
        $http.get(baseUrl + '/' + saleId)
            .then(function(response) {
                $scope.saleDetails = response.data;
            }, function(error) {
                console.error('Error fetching sale details:', error);
            });
    };

    if ($routeParams.id) {
        $scope.getSaleDetails($routeParams.id);
    }



    $scope.saveSaleDetails = function() {
        var saleData = $scope.saleDetails;

        if (saleData.id == null) {
            saleData.createdAt = new Date()
        }
        $http.post(baseUrl, saleData)
            .then(function(response) {
                console.log('Sale saved successfully', response.data);
                // Optionally, redirect to another page or reset the form
            }, function(error) {
                console.error('Error saving sale:', error);
            });
    };








    
    $scope.countCurrentSales();
    $scope.countUpcomingSales();
    $scope.countExpiredSales();
    $scope.getAllSales();

}]);
