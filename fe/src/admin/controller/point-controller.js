app.controller('pointAdminController', ['$scope', '$http', '$routeParams', '$timeout', function($scope, $http, $routeParams,$timeout) {

    const apiPointSettings = "http://localhost:8080/api/admin/point-settings";

  // notify
  toastr.options = {
    "closeButton": false,
    "debug": false,
    "newestOnTop": true,
    "progressBar": false,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
  }
  
  // Hàm hiển thị thông báo thành công
  $scope.showSuccessNotification = function(message) {
  toastr["success"](message);
  };
  
  // Hàm hiển thị thông báo lỗi
  $scope.showErrorNotification = function(message) {
    toastr["error"](message);
  };
  
  
  $scope.showWarningNotification = function(message) {
    toastr["warning"](message);
  };


    // Function to get PointSettings
    $scope.getPointSettings = function() {
        $http.get(apiPointSettings)
            .then(function(response) {
                // On success, bind the response data to the scope variable
                $scope.pointConversionRate = response.data.pointsPerAmount;
            })
            .catch(function(error) {
                console.error('Error fetching PointSettings:', error);
            });
    };

    // Function to save updated PointSettings
    $scope.savePointConversionRate = function() {
        var updatedSettings = {
            pointsPerAmount: $scope.pointConversionRate
        };

        $http.put(apiPointSettings, updatedSettings)
            .then(function(response) {
            $scope.showSuccessNotification("Thay đổi thành công tỉ lệ quy đổi điểm thưởng");
            })
            .catch(function(error) {
                $scope.showErrorNotification("Có lỗi xảy ra");
                console.error('Error updating PointSettings:', error);
            });
    };

    // Initialize by fetching PointSettings
    $scope.getPointSettings();





    // customerType --------------------------

    const apiCustomerType = "http://localhost:8080/api/admin/customer-types";

     // Initialize the controller
     $scope.currentPage = 0;
     $scope.pageSize = 10;
     $scope.totalPages = 0;
     $scope.customerTypes = [];
     $scope.desiredPage = 1;
 
     // Fetch customer types from the API
     function loadCustomerTypes() {
         $http.get(apiCustomerType, {
             params: {
                 name: $scope.searchName || '', 
                 page: $scope.currentPage,
                 size: $scope.pageSize
             }
         }).then(function(response) {
             $scope.customerTypes = response.data.content;
             $scope.totalPages = response.data.totalPages;
         }, function(error) {
            console.log(error)
             $scope.showErrorNotification('Failed to load customer types');
         });
     }
 
     loadCustomerTypes();
 
     // Set the current page
     $scope.setCurrentPage = function(page) {
         $scope.currentPage = page;
         loadCustomerTypes();
     };
 
     // Go to a specific page
     $scope.goToPage = function() {
         if ($scope.desiredPage >= 1 && $scope.desiredPage <= $scope.totalPages) {
             $scope.setCurrentPage($scope.desiredPage - 1);
         }
     };
 
// View customer type details in modal for update
// View customer type details in modal for update
$scope.viewCustomerTypeDetails = function(customerType) {
    $scope.selectedCustomerType = angular.copy(customerType);
    $('#editCustomerTypeModal').modal('show');
};

// Open modal for adding a new customer type
$scope.showAddCustomerTypeModal = function() {
    $scope.newCustomerType = {}; // Reset new customer type form
    $('#addCustomerTypeModal').modal('show');
};


// Update an existing customer type
$scope.updateCustomerType = function() {
    $http.put(apiCustomerType, $scope.selectedCustomerType).then(function(response) {
        $scope.showSuccessNotification('Customer type updated successfully');
        $('#editCustomerTypeModal').modal('hide');
        loadCustomerTypes();
    }, function(error) {
        $scope.showErrorNotification('Failed to update customer type');
    });
};


// Add a new customer type
$scope.addCustomerType = function() {
    $scope.newCustomerType.status = 1; // Set default status for new customer type
    $http.post(apiCustomerType, $scope.newCustomerType).then(function(response) {
        $scope.showSuccessNotification('Customer type created successfully');
        $('#addCustomerTypeModal').modal('hide');
        loadCustomerTypes();
    }, function(error) {
        if (error.status === 400 && error.data.message === 'Code already exists') {
            $scope.codeExists = true;
        } else {
            $scope.showErrorNotification('Failed to save Customer Type.');
        }
    });
};


 
     // Handle search input
     $scope.searchCustomerTypes = function() {
         $scope.currentPage = 0;
         loadCustomerTypes();
     };

     $scope.getStatusText = function(status) {
        switch (status) {
            case 1:
                return "Đang hoạt động";
            case 0:
                return "Không hoạt động";
            default:
                return "Không xác định"; 
        }
    };

    $scope.getStatusClass = function(status) {
        switch(status) {
            case 1:
                return 'ongoing';
            case 0:
                return 'expired';
            default:
                return '';
        }
    };

}]);
