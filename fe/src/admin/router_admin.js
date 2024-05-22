var app = angular.module("appAdmin", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider
    .when("/admin/thongKe", {
      templateUrl: "pages/thongke.html",
      controller: 'thongKeController'
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
    })
    // <!-- Hải -->
    .when("/admin/sale", {
      templateUrl: "pages/sale/sale.html",
    })
    .when("/admin/sale/creat", {
      templateUrl: "pages/sale/creat-sale.html",
    })


    .otherwise({
      redirectTo: "/",
    });
});



