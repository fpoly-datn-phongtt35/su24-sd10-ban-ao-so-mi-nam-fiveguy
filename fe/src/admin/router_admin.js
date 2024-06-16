var app = angular.module("appAdmin", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider
    .when("/admin/dashboard", {
      templateUrl: "pages/dashboard.html",
      controller: 'dashboardController'
    })   
   
    // <!-- Hiếu -->
    .when("/admin/staff", {
      templateUrl: "pages/staff/staff.html",
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
    // <!-- Tịnh -->
    .when("/admin/customer", {
      templateUrl: "pages/customer/customer.html",
    })
    // <!-- Nguyên -->
    .when("/admin/voucher", {
      templateUrl: "pages/voucher/voucher.html",
      controller: "nguyen-voucher-ctrl"
    })
    .when("/admin/vouchercopy", {
      templateUrl: "pages/voucher/voucher copy.html",
      controller: "nguyen-voucher-ctrl"
    })
    .when("/admin/voucher/create", {
      templateUrl: "pages/voucher/voucher-create.html",
      controller: "nguyen-voucher-create-ctrl"
    })
    // <!-- Hải -->
    .when("/admin/sale", {
      templateUrl: "pages/sale/sale.html",
    })



    .otherwise({
      templateUrl: "pages/dashboard.html",
      controller: 'dashboardController'
    })   
});



