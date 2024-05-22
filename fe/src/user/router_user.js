var app = angular.module("appUser", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider

   
    // <!-- Hiếu -->
    .when("/home/customer", {
      templateUrl: "pages/accountManage/customer.html",
      
      controller: 'thongKeController'
    })
    // <!-- Thưởng -->
 
    // <!-- Tịnh -->

    // <!-- Nguyên -->

    // <!-- Hải -->



    .otherwise({
      redirectTo: "/",
    });
});



