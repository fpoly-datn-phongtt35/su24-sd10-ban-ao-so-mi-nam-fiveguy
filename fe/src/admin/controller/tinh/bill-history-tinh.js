app.controller("tinh-bill-history-controller", function ($scope, $http) {
    $scope.billHistory = [];

    const apiBillHistory = "http://localhost:8080/api/admin/bill-history-tinh";



    //phân trang + lọc BillHistory 
    $scope.getBillHistory = function () {
        $http.get(apiBillHistory).then(function (resp) {
            $scope.auditLog = resp.data;
            //   $scope.employee = angular.copy($scope.originalEmployee);
        });
    };

    $scope.getBillHistory = [];
    $scope.totalPages = 0;
    $scope.currentPage = 0;
    $scope.desiredPage = 1;
    $scope.size = 5
    $scope.filterBillHistory = {
        status: null,
        createdAt: null,
        createdBy: null,
        type: null,
    };

    $scope.getAllBillHistory = function (pageNumber) {
        let params = angular.extend({ pageNumber: pageNumber, size: $scope.size }, $scope.filterBillHistory);
        $http
            .get("http://localhost:8080/api/admin/bill-history-tinh/page", {
                params: params,
            })
            .then(function (response) {
                $scope.getBillHistory = response.data.content;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = pageNumber;
                $scope.desiredPage = pageNumber + 1;
            });
    };

    $scope.applyFiltersBillHistory = function () {
        $scope.getAllBillHistory(0);

    };

    $scope.goToPageBillHistory = function () {
        let pageNumber = $scope.desiredPage - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.getAllBillHistory(pageNumber);
        } else {
            // Reset desiredPage to currentPage if the input is invalid
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };
    // Initial load
    $scope.getAllBillHistory(0);

    $scope.resetfilterBillHistory = function () {
        $scope.filterBillHistory = {
            status: null,
            createdAt: null,
            createdBy: null,
            type: null,
        };
        $scope.getAllBillHistory(0);
    };
})

