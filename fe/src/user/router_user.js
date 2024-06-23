var app = angular.module("appUser", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider
  .when("/home", {
    templateUrl: "pages/home.html",
    
  })
   
    // <!-- Hiếu -->
    .when("/home/customer", {
      templateUrl: "pages/accountManage/customer.html",
      
      controller: 'thongKeController'
    })
    // <!-- Thưởng -->
 
    // <!-- Tịnh -->

    // <!-- Nguyên -->

    // <!-- Hải -->
    .when("/home/cart", {
      templateUrl: "pages/cart/cart.html",
      controller: 'cartController'
    })


    .otherwise({
      redirectTo: "/",
    });
});



