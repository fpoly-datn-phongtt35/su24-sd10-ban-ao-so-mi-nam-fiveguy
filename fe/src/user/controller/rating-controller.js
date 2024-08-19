app.controller("ratingController", function ($scope, $http, $window, $routeParams, $rootScope, $location, $timeout) {
 
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
    $scope.search = ''; // Default empty string
    $scope.currentPage = 0; // Default starting page
    $scope.size = 10; // Default page size
    $scope.totalPages = 1; // Initialize to 1 or a suitable default
    $scope.getRates(0);
  }

  $scope.search = ''; // Default empty string
    $scope.currentPage = 0; // Default starting page
    $scope.size = 10; // Default page size
    $scope.totalPages = 1; // Initialize to 1 or a suitable default

// Function to fetch rates
// Function to fetch rates
$scope.getRates = function(page) {
    // Use the current page if no page is provided
    if (page === undefined) {
        page = $scope.currentPage;
    }
    
    var params = {
        page: page,
        search: $scope.search // Include the search parameter
    };
    
    $http.get('http://localhost:8080/api/home/rates', { params: params })
        .then(function(response) {
            if (response.data) {
                if (typeof response.data === 'string') {
                    $scope.listRates = []; // Clear the listRates if no data
                    console.error(response.data); // Log the error message
                } else {
                    $scope.listRates = response.data.content; // Assuming response data is a Page object
                    $scope.totalPages = response.data.totalPages; // Set total pages if available
                    $scope.currentPage = page; // Update current page
                    $scope.desiredPage = page + 1; // Convert 0-based index to 1-based for input
                }
            }
        })
        .catch(function(error) {
            console.error("Có lỗi xảy ra khi gọi API để lấy danh sách rates!", error);
        });
};

// Set current page and fetch data
$scope.setCurrentPageRate = function(page) {
    if (page >= 0 && page < $scope.totalPages) {
        $scope.getRates(page); // Fetch rates for the selected page
    }
};

// Handle go to page
$scope.goToPage = function() {
    var page = $scope.desiredPage - 1; // Convert 1-based index to 0-based
    if (page >= 0 && page < $scope.totalPages) {
        $scope.setCurrentPageRate(page);
    } else {
        // If page is invalid, reset desiredPage to current page
        $scope.desiredPage = $scope.currentPage + 1;
    }
};




    // Helper function to generate an array for pagination
    $scope.getNumber = function(num) {
        return new Array(num);
    };

    // Fetch initial data
    $scope.getRates(0);

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
 $scope.openReview = function(rate) {
    $scope.selectedRating = angular.copy(rate); // Create a copy of the selected rating
    $scope.originalRating = angular.copy(rate); // Store a copy of the original rating

    // Initialize the checkbox based on the status
    $scope.selectedRating.statusCheckbox = $scope.selectedRating.status === 3;

    $('#reviewModal').modal('show');
};

$scope.updateStatus = function() {
    // Update the status based on the checkbox value
    $scope.selectedRating.status = $scope.selectedRating.statusCheckbox ? 3 : 1;
};

$scope.updateRating = function() {
    // Function to check if data has changed
    function hasDataChanged(original, updated) {
        const keysToCompare = ['rate', 'content'];
        for (let key of keysToCompare) {
            if (original[key] !== updated[key]) {
                return true;
            }
        }
        return false;
    }

    // Check if data has changed before making the API call
    const dataChanged = hasDataChanged($scope.originalRating, $scope.selectedRating);

    if (dataChanged) {
        // If content or rate has changed, set approvalStatus to 2
        $scope.selectedRating.approvalStatus = 2;
    }

    // Always call the API if data or status has changed
    if (dataChanged || $scope.originalRating.status !== $scope.selectedRating.status) {
        $http.post('http://localhost:8080/api/home/update', $scope.selectedRating)
            .then(function(response) {
                console.log($scope.selectedRating);
                $scope.getRates();
                $scope.closeReview();
                $scope.showSuccessNotification("Cập nhật đánh giá thành công");
            })
            .catch(function(error) {
                $scope.showErrorNotification("Cập nhật đánh giá thất bại");
                console.error(error);
            });
    } else {
        $scope.showWarningNotification("Không có thay đổi nào được phát hiện");
    }
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



});
