// controllers/saleController.js
app.controller('SaleController', ['$scope', '$http', '$location', function($scope, $http, $location) {
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

    $scope.getAllSales();

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

    $scope.getSaleDetails = function(saleId) {

        $http.get(baseUrl + '/' + saleId)
            .then(function(response) {
                // On success, update scope variable with sale details
                $scope.saleDetails = response.data;
                $location.path("/admin/sale/creat").search({ id: saleId });
            }, function(error) {
                // On error, log the error
                console.error('Error fetching sale details:', error);
            });
    };
    $scope.countCurrentSales();
    $scope.countUpcomingSales();
    $scope.countExpiredSales();
}]);
