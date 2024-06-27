app.controller('nguyen-bill-detail-ctrl', function ($scope, $http, $routeParams) {

    // Access the idBill parameter from the route
    $scope.idBill = $routeParams.idBill;
    console.log("Bill ID:", $scope.idBill);

    $scope.getBillById = function (id){
        $http.get()
    }

    $scope.status = $scope.idBill;

    // Modal visibility
    $scope.modalVisible = false;

    // Function to show modal and set current status
    $scope.showModal = function (step) {
        $scope.modalVisible = true;
        $scope.status = step;
    };

    // Function to hide modal
    $scope.hideModal = function () {
        $scope.modalVisible = false;
    };
});