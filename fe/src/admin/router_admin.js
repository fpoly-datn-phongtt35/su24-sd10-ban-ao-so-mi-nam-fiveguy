var app = angular.module("appAdmin", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider
    .when("/", {
      templateUrl: "pages/thongke.html",
    })


    // <!-- Hiếu -->

    // <!-- Thưởng -->
    
    // <!-- Tịnh -->
    
    // <!-- Nguyên -->
    
    // <!-- Hải -->


    .otherwise({
      redirectTo: "/",
    });
});



