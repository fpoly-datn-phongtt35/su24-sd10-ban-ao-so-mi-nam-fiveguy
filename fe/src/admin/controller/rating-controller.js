app.controller("ratingAdminController", function ($scope, $http, $window, $routeParams, $rootScope, $location, $timeout) {
 
    $scope.currentPage = 0;
 
 
 
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
      $scope.refreshData = function() {
        $scope.approvalStatus = 0; // Default value
        $scope.search = ''; // Default empty string
        $scope.page = 0; // Default starting page
        $scope.size = 10; // Default page size
        $scope.totalPages = 1; // Initialize to 1 or a suitable default
        $scope.desiredPage = 1; // Default starting page in input
        $scope.getRatings(0);
      }
 
      $scope.approvalStatus = 0; // Default value
      $scope.search = ''; // Default empty string
      $scope.page = 0; // Default starting page
      $scope.size = 10; // Default page size
      $scope.totalPages = 1; // Initialize to 1 or a suitable default
      $scope.desiredPage = 1; // Default starting page in input
      
 

    // Function to fetch rates
    $scope.getRatings = function(page) {
        if (page === undefined) {
            page = $scope.page; // Default to current page if not specified
        }
    
        var params = {
            approvalStatus: $scope.approvalStatus,
            search: $scope.search,
            page: page,
            size: $scope.size
        };
    
        $http.get('http://localhost:8080/api/admin/ratings', { params: params })
            .then(function(response) {
                // Update scope variables
                $scope.ratings = response.data.content; // Assuming response data is a Page object
                $scope.totalPages = response.data.totalPages;
                $scope.page = page;
                $scope.desiredPage = page + 1; // Convert 0-based index to 1-based for input
            })
            .catch(function(error) {
                console.log(error);
                $scope.showErrorNotification("Failed to fetch ratings");
            });
    };
    
    
    
    
    $scope.setCurrentPageRating = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getRatings(page);
        }
    };
    
    $scope.goToPage = function() {
        var page = $scope.desiredPage - 1; // Convert 1-based index to 0-based
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getRatings(page);
        } else {
            // If page is invalid, reset desiredPage to current page
            $scope.desiredPage = $scope.page + 1;
        }
    };
    

    // Helper function to generate an array for pagination
    $scope.getNumber = function(num) {
        return new Array(num);
    };

    // Fetch initial data
    $scope.getRatings();

    $scope.deleteDataRate = function(rate) {
        $http.delete('http://localhost:8080/api/home/deleteRate/' + rate)
            .then(function(response) {
                $scope.getRates();
                $scope.showSuccessNotification("Xóa đánh giá thành công");
            }, function errorCallback(response) {
                $scope.showErrorNotification("Xóa đánh giá thất bại");
            });
    };  

 // Function to update a rating


 $scope.updateRating = function(rate, newStatus) {
    rate.approvalStatus = newStatus; // Update the approval status based on the button clicked

    // Send the updated rating to the server
    $http.post('http://localhost:8080/api/admin/update', rate)
        .then(function(response) {
            $scope.getRatings(); // Refresh the ratings list
            $scope.showSuccessNotification("Cập nhật đánh giá thành công");
            
        })
        .catch(function(error) {
            $scope.showErrorNotification("Cập nhật đánh giá thất bại");
            console.error(error);
        });
    $scope.getRatings();

};








$scope.closeReview = function() {
    $scope.selectedRating = null;
    $('#reviewModal').modal('hide');
};

$scope.updateStatus = function() {
    if ($scope.selectedRating.statusCheckbox) {
        $scope.selectedRating.status = 3; // Checkbox is checked, set status to 3
    } else {
        $scope.selectedRating.status = 1; // Checkbox is unchecked, set status to 1
    }
};

$scope.rating = {
    stars: 0,
    content: ''
  };
  
  $scope.toggleStars = function(index) {
    $scope.rating.rate = index + 1; // Update the local rating stars
    $scope.selectedRating.rate = index + 1; // Update the selected rating stars for saving

    const stars = document.querySelectorAll('#reviewModal .fa-star');
    for (let i = 0; i <= index; i++) {
        stars[i].classList.add('checked');
    }
    for (let i = index + 1; i < stars.length; i++) {
        stars[i].classList.remove('checked');
    }
};



$scope.getStatusStyle = function(status) {
    switch(status) {
        case 1:
            return { color: 'green' }; // Đang hiển thị
        case 3:
            return { color: 'red' }; // Đánh giá này đã ẩn
        default:
            return { color: 'black' }; // Default color if status is unknown
    }
};

$scope.getApprovalStatusStyle = function(approvalStatus) {
    switch(approvalStatus) {
        case 1:
            return { color: 'green' }; // Đã được duyệt
        case 2:
            return { color: 'blue' }; // Chờ duyệt
        case 3:
            return { color: 'red' }; // Bị ẩn do vi phạm
        default:
            return { color: 'black' }; // Default color if approvalStatus is unknown
    }
};

$scope.viewProduct = function(productId) {
    console.log(productId)
    // window.location.href = 'http://127.0.0.1:5555/src/user/index.html#/home/product/product-detail';

};


});
