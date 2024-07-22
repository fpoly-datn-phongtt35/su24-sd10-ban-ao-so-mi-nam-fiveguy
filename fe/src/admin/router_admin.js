var app = angular.module("appAdmin", ["ngRoute", "angular.filter"]);
app.config(function ($routeProvider, $locationProvider) {
  $locationProvider.hashPrefix("");

  $routeProvider
    .when("/admin/dashboard", {
      templateUrl: "pages/dashboard.html",
      controller: 'dashboardController'
    })

   // <!-- Hiếu -->
   .when("/admin/customer", {
    templateUrl: "pages/customer/customer.html",
    controller: 'customerCtrl'

  })

    // <!-- Tịnh -->
    .when("/admin/employee", {
      templateUrl: "pages/employee/employee.html",
      controller: "tinh-employee-controller",
    })

    .when("/admin/bil-tinh/create", {
      templateUrl: "pages/bill-tinh/bill-tinh-create.html",
      controller: "tinh-bill-controller",
    })

    .when("/admin/bil-history-tinh", {
      templateUrl: "pages/bill-tinh/bill-history-tinh.html",
      controller: "tinh-bill-history-controller",
    })

    .when("/admin/dashboard", {
      templateUrl: "pages/dashboard.html",
      controller: "DashboardController",
    })
    // end Tinh
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
    // <!-- Nguyên -->
    .when("/admin/v1", {
      templateUrl: "pages/voucher/voucher.html",
      controller: "nguyen-voucher-ctrl"
    })
    .when("/admin/vtest", {
      templateUrl: "pages/voucher/voucher copy.html",
      controller: "nguyen-voucher-test"
    })
    .when("/admin/voucher", {
      templateUrl: "pages/voucher/voucher2.html",
      controller: "nguyen-voucher2-ctrl"
    })
    .when("/admin/voucher/chart", {
      templateUrl: "pages/voucher/voucher-chart.html",
      controller: "nguyen-voucher-chart-ctrl"
    })
    .when("/admin/bill", {
      templateUrl: "pages/bill-nguyen/bill.html",
      controller: "nguyen-bill-ctrl"
    })
    .when("/admin/bill/create", {
      templateUrl: "pages/bill-nguyen/bill-create.html",
      controller: "nguyen-bill-create-ctrl"
    })
    .when("/admin/bill/:idBill", {
      templateUrl: "pages/bill-nguyen/bill-detail.html",
      controller: "nguyen-bill-detail-ctrl"
    })
    .when("/admin/billtest/:idBill", {
      templateUrl: "pages/bill-nguyen/testbill.html",
      controller: "test-bill-detail-ctrl"
    })
    
    // <!-- Hải -->
    .when("/admin/sale", {
      templateUrl: "pages/sale/sale.html",
      controller: 'SaleController'
    })
    .when("/admin/sale/update/:idSale", {
      templateUrl: "pages/sale/saleDetail.html",
      controller: "SaleController"
    })


    .otherwise({
      templateUrl: "pages/dashboard.html",
      controller: 'dashboardController'
    })
});













//Request thêm token vào các yêu cầu HTTP
// Tạo một interceptor trong AngularJS
app.factory('TokenInterceptor', function ($q, $injector) {
  var isRefreshing = false;
  var requestsToRetry = [];

  function retryRequests(newToken) {
    var $http = $injector.get('$http');
    requestsToRetry.forEach(function (pendingRequest) {
      pendingRequest.config.headers['Authorization'] = 'Bearer ' + newToken;
      $http(pendingRequest.config).then(pendingRequest.deferred.resolve, pendingRequest.deferred.reject);
    });
    requestsToRetry = [];
  }

  return {
    request: function (config) {
      var token = localStorage.getItem('token');
      if (token) {
        config.headers['Authorization'] = 'Bearer ' + token;
      }
      return config;
    },
    responseError: function (response) {
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
          .then(function (data) {
            localStorage.setItem('token', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            isRefreshing = false;
            retryRequests(data.accessToken);
          })
          .catch(function () {
            isRefreshing = false;
            window.location.href = "http://127.0.0.1:5555/src/login/login.html";
            requestsToRetry.forEach(function (pendingRequest) {
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

app.config(function ($httpProvider) {
  $httpProvider.interceptors.push('TokenInterceptor');
  $httpProvider.useApplyAsync(true);
});

app.factory('AuthService', function ($http) {
  var authService = {};

  authService.isAuthenticated = function () {
    var token = localStorage.getItem('token');
    return !!token;
  };

  authService.refreshToken = function (refreshToken) {
    return $http.post('http://localhost:8080/RFToken/' + refreshToken)
      .then(function (response) {
        return response.data;
      })
      .catch(function (error) {
        throw error;
      });
  };

  authService.removeToken = function () {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
  };

  return authService;
});
