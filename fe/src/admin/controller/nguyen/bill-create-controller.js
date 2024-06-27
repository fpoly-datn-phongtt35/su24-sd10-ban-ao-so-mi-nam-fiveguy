app.controller('nguyen-bill-create-ctrl', function ($scope, $http) {

    $scope.formInputBill = {}

    const apiBill = "http://localhost:8080/api/admin/bill";

    const apiVoucher = "http://localhost:8080/api/admin/voucher";

    const apiCustomer = "http://localhost:8080/api/admin/customer";
    const apiCustomerType = "http://localhost:8080/api/admin/customerType";

    const apiPaymentMethod = "http://localhost:8080/api/admin/paymentMethod";

    const apiProduct = "http://localhost:8080/api/admin/product";
    const apiProductDetail = "http://localhost:8080/api/admin/productDetail";
    const apiCategory = "http://localhost:8080/api/admin/category";
    const apiMaterial = "http://localhost:8080/api/admin/material";
    const apiCollar = "http://localhost:8080/api/admin/collar";
    const apiWrist = "http://localhost:8080/api/admin/wrist";
    const apiSize = "http://localhost:8080/api/admin/size";
    const apiColor = "http://localhost:8080/api/admin/color";

    $scope.bills = []

    $scope.vouchers = []

    $scope.customers = []
    $scope.customerTypes = []

    $scope.paymentMethods = []

    $scope.products = []
    $scope.productDetails = []
    $scope.categories = []
    $scope.materials = []
    $scope.collars = []
    $scope.wrists = []
    $scope.sizes = []
    $scope.colors = []

    $scope.getMutilpleEntityData = function () {
        $http.get(apiBill + "/all").then(function (res) { $scope.bills = res.data || []; });
        $http.get(apiVoucher + "/all").then(function (res) { $scope.vouchers = res.data || []; });
        $http.get(apiCustomer + "/all").then(function (res) { $scope.customers = res.data || []; });
        $http.get(apiCustomerType + "/all").then(function (res) { $scope.customerTypes = res.data || []; });
        $http.get(apiPaymentMethod + "/all").then(function (res) { $scope.paymentMethods = res.data || []; });
        $http.get(apiProduct + "/all").then(function (res) { $scope.products = res.data || []; });
        $http.get(apiProductDetail + "/all").then(function (res) { $scope.productDetails = res.data || []; });
        $http.get(apiCategory + "/all").then(function (res) { $scope.categories = res.data || []; });
        $http.get(apiMaterial + "/all").then(function (res) { $scope.materials = res.data || []; });
        $http.get(apiCollar + "/all").then(function (res) { $scope.collars = res.data || []; });
        $http.get(apiWrist + "/all").then(function (res) { $scope.wrists = res.data || []; });
        $http.get(apiSize + "/all").then(function (res) { $scope.sizes = res.data || []; });
        $http.get(apiColor + "/all").then(function (res) { $scope.colors = res.data || []; });
    }
});