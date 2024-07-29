var app = angular.module("appUser", ["ngRoute"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider
  .when("/home", {
    templateUrl: "pages/home.html",
    controller: 'productController'
    
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
      controller: 'productController'
    })
    .when("/home/product", {
      templateUrl: "pages/onlineShop/product.html",
      controller: 'productController'
    })
    .when("/home/product/product-detail/:idProduct", {
      templateUrl : "pages/onlineShop/product-detail.html",
      controller : "productController"
    })
    .when("/home/paymentSuccess", {
      templateUrl: "pages/onlineShop/paymentSuccess.html",
      controller: 'productController'
    })
    .when("/home/paymentFailed", {
      templateUrl: "pages/onlineShop/paymentFailed.html",
      controller: 'productController'
    })
    .when("/home/order", {
      templateUrl : "pages/accountManage/order.html",
      controller : "orderController"
    })
    .when("/home/orderTrack", {
      templateUrl : "pages/accountManage/orderTrack.html",
      controller : "orderController"
    })
    .when("/home/orderDetail/:idBill", {
      templateUrl : "pages/accountManage/orderDetail.html",
      controller : "orderController"
    })

    .when("/unauthorized", {
      templateUrl: "pages/Notification/unauthorized.html"
    })
    .otherwise({
      templateUrl: "pages/Notification/notFound.html"

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

  authService.getUserRole = function () {
    var token = localStorage.getItem('token');
    if (token) {
      var decodedToken = jwt_decode(token);
      return decodedToken.role[0].authority;
    }
    return null;
  };

  authService.authorize = function (allowedRoles) {
    var deferred = $q.defer();
    var userRole = authService.getUserRole();

    if (allowedRoles.indexOf(userRole) !== -1) {
      deferred.resolve();
    } else {
      deferred.reject();
      window.location.href = "http://127.0.0.1:5555/src/admin/index.html#/unauthorized";
    }

    return deferred.promise;
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
