var app = angular.module("appAdmin", ["ngRoute", "angular.filter"]);
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
    .when("/admin/category", {
      templateUrl: "pages/product/category.html",
    })
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
    .when("/admin/size", {
      templateUrl: "pages/product/size.html",
    })
    .when("/admin/color", {
      templateUrl: "pages/product/color.html",
    })
    .when("/admin/supplier", {
      templateUrl: "pages/product/supplier.html",
    })
    .when("/admin/sell-quickly", {
      templateUrl: "pages/sell-quickly.html",
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



    .otherwise({
      templateUrl: "pages/dashboard.html",
      controller: 'dashboardController'
    })   
});













//Request thêm token vào các yêu cầu HTTP
// Tạo một interceptor trong AngularJS
app.factory('TokenInterceptor', function($q, $injector) {
  var isRefreshing = false;
  var requestsToRetry = [];

  function retryRequests(newToken) {
    var $http = $injector.get('$http');
    requestsToRetry.forEach(function(pendingRequest) {
      pendingRequest.config.headers['Authorization'] = 'Bearer ' + newToken;
      $http(pendingRequest.config).then(pendingRequest.deferred.resolve, pendingRequest.deferred.reject);
    });
    requestsToRetry = [];
  }

  return {
    request: function(config) {
      var token = localStorage.getItem('token');
      if (token) {
        config.headers['Authorization'] = 'Bearer ' + token;
      }
      return config;
    },
    responseError: function(response) {
      var $http = $injector.get('$http');
      var authService = $injector.get('AuthService');
      var status = response.status;

      if (status === 406) {
        authService.removeToken();
        window.location.href = "http://127.0.0.1:5555/src/login/login.html";
        return $q.reject(response);
      }

      if (status === 401) {
        var deferred = $q.defer();
        var refreshToken = localStorage.getItem('refreshToken');
        authService.removeToken();

        if (!refreshToken || isRefreshing) {
          window.location.href = "http://127.0.0.1:5555/src/login/login.html";
          return deferred.promise;
        }

        isRefreshing = true;
        requestsToRetry.push({ config: response.config, deferred: deferred });

        authService.refreshToken(refreshToken)
          .then(function(data) {
            localStorage.setItem('token', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            isRefreshing = false;
            retryRequests(data.accessToken);
          })
          .catch(function() {
            isRefreshing = false;
            window.location.href = "http://127.0.0.1:5555/src/login/login.html";
            requestsToRetry.forEach(function(pendingRequest) {
              pendingRequest.deferred.reject(response);
            });
            requestsToRetry = [];
          });

        return deferred.promise;
      }

      return $q.reject(response);
    }
  };
});

app.config(function($httpProvider) {
  $httpProvider.interceptors.push('TokenInterceptor');
  $httpProvider.useApplyAsync(true);
});

app.factory('AuthService', function($http) {
  var authService = {};

  authService.isAuthenticated = function() {
    var token = localStorage.getItem('token');
    return !!token;
  };

  authService.refreshToken = function(refreshToken) {
    return $http.post('http://localhost:8080/RFToken/' + refreshToken)
      .then(function(response) {
        return response.data;
      })
      .catch(function(error) {
        throw error;
      });
  };

  authService.removeToken = function() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
  };

  return authService;
});
