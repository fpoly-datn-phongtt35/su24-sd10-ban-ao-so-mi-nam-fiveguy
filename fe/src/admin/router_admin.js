var app = angular.module("appAdmin", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider
    .when("/admin/dashboard", {
      templateUrl: "pages/dashboard.html",
      controller: "dashboardController",
    })

    // <!-- Tịnh -->
    .when("/admin/employee", {
      templateUrl: "pages/employee/employee.html",
      controller: "tinh-employee-controller",
    })
    // <!-- Thưởng -->
    .when("/admin/product", {
      templateUrl: "pages/product/product.html",
    })
    .when("/admin/collar", {
      templateUrl: "pages/product/collar.html",
    })
    .when("/admin/wrist", {
      templateUrl: "pages/product/wrist.html",
    })
    .when("/admin/material", {
      templateUrl: "pages/product/material.html",
    })
    // <!-- Hiếu -->
    .when("/admin/customer", {
      templateUrl: "pages/customer/customer.html",
      controller: 'customerCtrl'

    })
    // <!-- Nguyên -->
    .when("/admin/voucher", {
      templateUrl: "pages/voucher/voucher.html",
    })
    // <!-- Hải -->
    .when("/admin/sale", {
      templateUrl: "pages/sale/sale.html",
    })

    .otherwise({
      templateUrl: "pages/dashboard.html",
      controller: "dashboardController",
    });
});
