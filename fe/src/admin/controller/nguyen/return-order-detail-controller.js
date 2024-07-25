app.controller('nguyen-return-order-detail-ctrl', function ($scope, $http, $routeParams, $location) {

    // Hàm hiển thị thông báo thành công
    $scope.showSuccess = function (message) {
        toastr["success"](message);
    };
    // Hàm hiển thị thông báo lỗi
    $scope.showError = function (message) {
        toastr["error"](message);
    };
    $scope.showWarning = function (message) {
        toastr["warning"](message);
    };

    $scope.idBill = $routeParams.idBill;

    $scope.apiReturnOrder = "http://localhost:8080/api/admin/returnOrder";
    $scope.apiBill = "http://localhost:8080/api/admin/bill";
    $scope.apiBillDetail = "http://localhost:8080/api/admin/billDetail";

    $scope.returnOrders = [];
    $scope.bill = {};
    $scope.billDetails = [];

    $scope.findById = function (id) {
        if (id == undefined || id == null || id == "") return;
        
        $http.get($scope.apiBill + "/" + id).then(function (response) {
            $scope.bill = response.data;
            console.log(response.data);
        }).catch(function () {
            console.log("err");
        });

        $http.get($scope.apiReturnOrder + "/" + id).then(function (response) {
            $scope.returnOrders = response.data;
            console.log($scope.returnOrders);
        });

        $http.get($scope.apiBillDetail + "/getAllByBillId/" + id).then(function (response) {
            $scope.billDetails = response.data;

            $scope.billDetails.forEach(function (bd) {
                bd.inputQuantity = 1; // Set default value to 0
            });
            console.log(response.data);
        });
    };
    $scope.findById($scope.idBill);

    $scope.validateQuantity = function (bdIn) {
        if (bdIn.inputQuantity >=
            bdIn.quantity) {
            bdIn.inputQuantity = bdIn.quantity;
        }
    };

    $scope.addReturnOrder = function (billDetail) {
        if (billDetail.isChecked) {
            const returnOrder = {
                bill: $scope.bill,
                billDetail: billDetail,
                type: 1,
                quantity: billDetail.inputQuantity,
                returnStatus: 2
            };
            $scope.returnOrders.push(returnOrder);
            
            // $scope.returnOrders.forEach(function(ro){
            //     if(returnOrder.billDetail.id == ro.billDetail.id){
            //         ro.quantity = ro.quantity + returnOrder.quantity
            //     }else{
            //         $scope.returnOrders.push(returnOrder);
            //     }
            // })
        } else {
            const index = $scope.returnOrders.findIndex(ro => ro.billDetail.id === billDetail.id);
            if (index !== -1) {
                $scope.returnOrders.splice(index, 1);
            }
        }
    };

    $scope.changeQuantity = function (billDetail, updateQuantity) {
        $scope.returnOrders.forEach(function (ro) {
            if (ro.billDetail.id == billDetail.id && ro.quantity !== updateQuantity) {
                ro.quantity = updateQuantity
                $scope.showSuccess("Chỉnh sửa số lượng thành công")
            }
        })
    }

    $scope.confirmReturn = function () {
        $http.post($scope.apiReturnOrder + "/addReturnOrder", $scope.returnOrders).then(function (response) {
            console.log(response);
            
            $location.path('/admin/bill/' + $scope.bill.id);
        }).catch(function (error) {
            console.error("Error:", error);
        });
    };
});