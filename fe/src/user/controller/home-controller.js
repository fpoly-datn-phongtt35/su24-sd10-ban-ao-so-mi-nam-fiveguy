app.controller("homeController", function ($scope, $http, $window,$routeParams,$rootScope,$location) {


//Js Home Slide 
let slideIndex = 0;
let slideInterval;

$scope.showSlide = function(index) {
  const slides = document.querySelectorAll('.carousel-item');
  if (slides.length > 0) {
    if (index >= slides.length) { slideIndex = 0; }
    if (index < 0) { slideIndex = slides.length - 1; }
    for (let i = 0; i < slides.length; i++) {
      slides[i].classList.remove('active');
    }
    slides[slideIndex].classList.add('active');
  }
}
    $scope.prevSlide = function() {
        slideIndex--;
        $scope.showSlide(slideIndex);
      }
      
      $scope.nextSlide = function() {
        slideIndex++;
        $scope.showSlide(slideIndex);
      }
      
      function startSlideShow() {
        slideInterval = setInterval(() => {
          $scope.nextSlide();
        }, 20000); // Chuyển slide sau mỗi 10 giây (10000 milliseconds)
      }









    $scope.loadActiveCategories = function() {
        $http.get('http://localhost:8080/api/home/categories')
          .then(function(response) {
            if (response.data) {
              $scope.listActiveCategories = response.data;
            }
          })
          .catch(function(error) {
            alert("Có lỗi xảy ra khi gọi API!");
            console.error(error);
          });
    };


    $scope.loadActiveCategories();


    // Function to load products ordered by total quantity sold
$scope.loadProductsByTotalQuantitySold = function() {
    $http.get('http://localhost:8080/api/home/product/totalQuantitySold')
      .then(function(response) {
        if (response.data) {
          $scope.productsByTotalQuantitySold = response.data.slice(0, 12); // Lấy chỉ 12 đối tượng đầu tiên

          console.log($scope.productsByTotalQuantitySold)
        }
      })
      .catch(function(error) {
        alert("Có lỗi xảy ra khi gọi API lấy sản phẩm theo tổng số lượng bán!");
        console.error(error);
      });
};

// Function to load products ordered by created date
$scope.loadProductsByCreatedAt = function() {
    $http.get('http://localhost:8080/api/home/product/createdAt')
      .then(function(response) {
        if (response.data) {
          $scope.productsByCreatedAt = response.data.slice(0, 12); // Lấy chỉ 12 đối tượng đầu tiên
        }
      })
      .catch(function(error) {
        alert("Có lỗi xảy ra khi gọi API lấy sản phẩm theo ngày tạo!");
        console.error(error);
      });
};


      // formatCurrency
      $scope.formatCurrency = function(value) {
        if (!value) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    };

    $scope.openOverlaySearch = function() {
      document.getElementById('overlay').style.display = 'block';
  };

  $scope.toggleOverlaySearch = function() {
      document.getElementById('overlay').style.display = 'none';
  };

  $scope.closeOverlay = function(event) {
      if (event.target.id === 'overlay') {
          document.getElementById('overlay').style.display = 'none';
      }
  };

  // $scope.loadProductSearch = function() {
  //     // Logic to load products based on $scope.searchText
  // };


  $scope.searchProducts = function(name) {
    $http.get('http://localhost:8080/api/home/product/search', {
      params: { name: name }
    })
    .then(function(response) {
      if (response.data) {
        $scope.searchedProducts = response.data;
      }
    })
    .catch(function(error) {
      alert("Có lỗi xảy ra khi tìm kiếm sản phẩm!");
      console.error(error);
    });
  };





});
