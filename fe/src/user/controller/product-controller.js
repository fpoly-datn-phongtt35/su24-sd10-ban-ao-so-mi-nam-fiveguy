app.controller("productController", function ($scope, $http, $window,$routeParams,$rootScope,$location,$filter) {

    $rootScope.countProduct = 0;

    var token = localStorage.getItem("token")

    
    var baseUrlInfoProduct = 'http://localhost:8080/api/infoProduct';
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
  
  
  $scope.selectedVoucher = null;

  $scope.showWarningNotification = function(message) {
    toastr["warning"](message);
  };
    $scope.sections = {
        colors: false,
        sizes: false,
        materials: false,
        wrists: false,
        collars: false,
        brands: false
    };

    // Function to toggle section visibility
    $scope.toggleSection = function(section) {
        $scope.sections[section] = !$scope.sections[section];
    };

    $scope.getAllWrists = function() {
        $http.get(baseUrlInfoProduct + '/wrists').then(function(response) {
            $scope.wrists = response.data;
        }, function(error) {
            console.error('Error fetching wrists:', error);
        });
    };

    $scope.getAllSizes = function() {
        $http.get(baseUrlInfoProduct + '/sizes').then(function(response) {
            $scope.sizes = response.data;
        }, function(error) {
            console.error('Error fetching sizes:', error);
        });
    };

    $scope.getAllMaterials = function() {
        $http.get(baseUrlInfoProduct + '/materials').then(function(response) {
            $scope.materials = response.data;
        }, function(error) {
            console.error('Error fetching materials:', error);
        });
    };

    $scope.getAllColors = function() {
        $http.get(baseUrlInfoProduct + '/colors').then(function(response) {
            $scope.colors = response.data;
        }, function(error) {
            console.error('Error fetching colors:', error);
        });
    };

    $scope.getAllCollars = function() {
        $http.get(baseUrlInfoProduct + '/collars').then(function(response) {
            $scope.collars = response.data;
        }, function(error) {
            console.error('Error fetching collars:', error);
        });
    };

    $scope.getAllBrands = function() {
        $http.get(baseUrlInfoProduct + '/brands').then(function(response) {
            $scope.brands = response.data;
        }, function(error) {
            console.error('Error fetching brands:', error);
        });
    };

    $scope.getAllWrists();
    $scope.getAllSizes();
    $scope.getAllMaterials();
    $scope.getAllColors();
    $scope.getAllCollars();
    $scope.getAllBrands();


      // formatCurrency
$scope.formatCurrency = function(value) {
    if (!value) return '';
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
};


$scope.selectedColors = [];
$scope.selectedSizes = [];
$scope.selectedMaterials = [];
$scope.selectedCollars = [];
$scope.selectedWrists = [];
$scope.searchTerm;

$scope.getSearch = function() {

  var searchTerm = localStorage.getItem('searchTerm');
  if (searchTerm !== null) {
  $scope.searchTerm = searchTerm;
      localStorage.removeItem('searchTerm');
  }

};


$scope.filterProducts = function(page) {
  $scope.getSearch();

  if (page === undefined) {
      page = $scope.currentPage;
  }

  var config = {
      params: {
          searchTerm: $scope.searchTerm, // Set searchTerm vào params
          colorIds: $scope.selectedColors.length > 0 ? $scope.selectedColors : null,
          sizeIds: $scope.selectedSizes.length > 0 ? $scope.selectedSizes : null,
          materialIds: $scope.selectedMaterials.length > 0 ? $scope.selectedMaterials : null,
          collarIds: $scope.selectedCollars.length > 0 ? $scope.selectedCollars : null,
          wristIds: $scope.selectedWrists.length > 0 ? $scope.selectedWrists : null,
          minPrice: $scope.minPrice,
          maxPrice: $scope.maxPrice,
          categoryIds: $location.search().categoryId ? [$location.search().categoryId] : null,
          page: page,
          size: $scope.pageSize,
          sortDir: $scope.selectedSortType == 1 ? 'asc' : ($scope.selectedSortType == 2 ? 'desc' : 'desc'),
          sort: $scope.selectedSortType == 0 ? 'createdAt' : 'price'
      }
  };

  console.log(config);

  return $http.get('http://localhost:8080/api/home/products/filter', config)
      .then(function(response) {
          // console.log(response);

          $scope.products = response.data.content.map(product => {
              return product;
          });
          console.log($scope.products);

          $scope.totalPages = response.data.totalPages;
          $scope.currentPage = page;
          $scope.totalProducts = response.data.totalElements;
          return response.data;
      }).catch(function(error) {
          console.error('Error fetching products:', error);
          throw error;
      });
};

$scope.updateSelectedColors = function(color) {
    if (color.isSelected) {
        $scope.selectedColors.push(color.id);
    } else {
        $scope.selectedColors = $scope.selectedColors.filter(id => id !== color.id);
    }
    $scope.filterProducts(0);

};

$scope.updateSelectedSizes = function(size) {
    if (size.isSelected) {
        $scope.selectedSizes.push(size.id);
    } else {
        $scope.selectedSizes = $scope.selectedSizes.filter(id => id !== size.id);
    }
    $scope.filterProducts(0);

};

$scope.updateSelectedMaterials = function(material) {
    if (material.isSelected) {
        if (!$scope.selectedMaterials.includes(material.id)) {
            $scope.selectedMaterials.push(material.id);
        }
    } else {
        $scope.selectedMaterials = $scope.selectedMaterials.filter(id => id !== material.id);
    }
    $scope.filterProducts(0);
};


$scope.updateSelectedCollars = function(collar) {
    if (collar.isSelected) {
        if (!$scope.selectedCollars.includes(collar.id)) {
            $scope.selectedCollars.push(collar.id);
        }
    } else {
        $scope.selectedCollars = $scope.selectedCollars.filter(id => id !== collar.id);
    }
    $scope.filterProducts(0);
};

$scope.updateSelectedWrists = function(wrist) {
    if (wrist.isSelected) {
        if (!$scope.selectedWrists.includes(wrist.id)) {
            $scope.selectedWrists.push(wrist.id);
        }
    } else {
        $scope.selectedWrists = $scope.selectedWrists.filter(id => id !== wrist.id);
    }
    $scope.filterProducts(0);
};


$scope.setCurrentPage = function(page) {
    if (page >= 0 && page < $scope.totalPages) {
        $scope.filterProducts(page);
    }
};

$scope.getPageRange = function() {
  var range = [];
  for (var i = 0; i < $scope.totalPages; i++) {
      range.push(i);
  }
  return range;
};

// $scope.loadPage = function() {
//     $scope.filterProducts(0);
// };


      // Function to refresh data (reset filters and reload products)
      $scope.refreshDataProduct = function() {
          $scope.selectedCategory = null;
          $scope.selectedCollar = null;
          $scope.selectedWrist = null;
          $scope.selectedColor = null;
          $scope.selectedSize = null;
          $scope.selectedMaterial = null;
          $scope.currentPage = 0;
          $scope.searchTerm = null;
          $scope.minPrice = null;
          $scope.maxPrice = null;
  
          $scope.filterProducts(0);
      };
  
      // Initial load of products




    //   ProductDetail -----------------------------------

      var productId = $routeParams.idProduct;

      $scope.productDetailInfo = {};
      $scope.selectedColor = null;
      $scope.selectedSize = null;
  
      $scope.colorD = [];
      $scope.sizesD = [];
      $scope.imagesD = [];
  
      $scope.loadProductDetails = function() {
          $http.get('http://localhost:8080/api/home/product/viewProduct/' + productId)
              .then(function(response) {
                  $scope.productDetailInfo  = response.data;
                  $scope.colorD = response.data.colors;
  
                  if ($scope.colorD.length > 0) {
                      $scope.selectColor($scope.productDetailInfo.colors[0][0]);
                  }
  
                  $scope.sizesD = response.data.sizes;
                  $scope.imagesD = response.data.images;
                  $scope.mainImage = $scope.imagesD.length > 0 ? $scope.imagesD[0] : '';

              }, function(error) {
                  console.error('Error fetching product details:', error);
              });
      };
  
      // Example usage
    //   $scope.loadProductDetails(productId);
    $scope.fetchProductDetails = function() {
        if ($scope.selectedColor && $scope.selectedSize) {
            $scope.getProductDetail(productId, $scope.selectedSize, $scope.selectedColor);
        }
    };
  
      $scope.selectColor = function(colorId) {
          $scope.selectedColor = colorId;
          $scope.getImagesByProductIdAndColorId(productId,colorId);
          $scope.fetchProductDetails();
      };
  
      $scope.selectSize = function(sizeId) {
          $scope.selectedSize = sizeId;
          $scope.fetchProductDetails();
      };

        $scope.setMainImage = function(image) {
            $scope.mainImage = image;
        };

        $scope.getImagesByProductIdAndColorId = function(productId, colorId) {
            $http.get('http://localhost:8080/api/home/' +'product/' + productId + '/color/' + colorId)
                .then(function(response) {
                    $scope.imagesD = response.data; // Cập nhật danh sách ảnh
                    // Cập nhật ảnh chính nếu danh sách ảnh không rỗng
                    $scope.mainImage = $scope.imagesD.length > 0 ? $scope.imagesD[0] : '';
                }, function(error) {
                    console.error('Error fetching images:', error);
                });
        };



        $scope.productDetail = {}; // Initialize productDetail object

        $scope.getProductDetail = function(productId, sizeId, colorId) {
            $http.get('http://localhost:8080/api/home/product/productDetail/' + productId + '/' + sizeId + '/' + colorId)
                .then(function(response) {
                    $scope.productDetail = response.data; // Assuming response.data 
                }, function(error) {
                    $scope.productDetail.quantity = 0;
                });
        };

        $scope.quantitySelected = 1; // Số lượng mặc định
        
        
        $scope.checkQuantityChange2 = function(quantity) {
            if (isNaN(quantity) || quantity < 1 || !Number.isInteger(quantity)) {
                $scope.quantitySelected = 1; // Set lại giá trị là 1 nếu không hợp lệ
            }
        };

        $scope.checkQuantityChange = function(item) {
            if (isNaN(item.quantity) || item.quantity < 1 || !Number.isInteger(item.quantity)) {
              item.quantity = 1; // Set lại giá trị là 1 nếu không hợp lệ
            }
          };



        $scope.cart = {
            items: [],
            add(productDetailId, quantity) {
                    // Thực hiện hành động khi đã đăng nhập
                    $http.post('http://localhost:8080/api/home/cart/add', { productDetailId: productDetailId, quantity: quantity
                        // ,promotionalPrice: promotionalPrice

                    })
                        .then(function (response) {
                            // loadCart();
                            if (response.data.employeeLoggedIn === true) {
                                $scope.showErrorNotification("Nhân viên không thể mua hàng!");
                            } else if (response.data === 1) {
                                $scope.showSuccessNotification("Thêm vào giỏ thành công!");
                                loadCart();
                            } else if (response.data === 2) {
                                $scope.showErrorNotification("Sản phẩm không có đủ số lượng trong kho!");
                            } else {
                                $scope.showErrorNotification("Thêm vào giỏ thất bại!");
                            }
                        })
                        .catch(function (error) {
                            $scope.showWarningNotification("Có lỗi xảy ra!");
                            console.error(error);
                        });
            },
            update(cartDetailId, quantity) {
                    $http.post('http://localhost:8080/api/home/cart/update', { cartDetailId: cartDetailId, quantity: quantity, username: $scope.username })
                        .then(function (response) {
                            if (response.data === 1) {
                                $scope.showSuccessNotification("Cập nhật giỏ thành công!");
                            } else if (response.data === 2) {
                                $scope.showErrorNotification("Sản phẩm không có đủ số lượng trong kho!");
                            } else {
                                $scope.showErrorNotification("Cập nhật giỏ thất bại!");
                            }
                            if (response.data) {
                                loadCart();
                            }
                        })
                        .catch(function (error) {
                            $scope.showWarningNotification("Có lỗi xảy ra!");
                            console.error(error);
                        });
            },
            remove(id) {
                    $http.post('http://localhost:8080/api/home/cart/remove', { cartDetailId: id })
                        .then(function (response) {
                            loadCart();
                            // count();
                            $scope.showSuccessNotification("Xóa sản phẩm thành công!");
                        })
                        .catch(function (error) {
                            $scope.showWarningNotification("Có lỗi xảy ra!");
                            console.error(error);
                        });
            },
            clear() {
                    // Thực hiện hành động khi đã đăng nhập
                    $http.post('http://localhost:8080/api/home/cart/clear')
                        .then(function (response) {
                            loadCart();
                            $scope.showSuccessNotification("Xóa tất cả sản phẩm khỏi giỏ hàng thành công!");
                        })
                        .catch(function (error) {
                            $scope.showWarningNotification("Có lỗi xảy ra!");
                            console.error(error);
                        });
            },
        
            // saveDataAndClearLocalStorage() {
            //     var localStorageData = JSON.parse(localStorage.getItem("cart"));
            //     if (localStorageData && Array.isArray(localStorageData) && localStorageData.length > 0 && localStorageData != []) {
            //         $http.post('http://localhost:8080/api/ol/cart/saveAll', { localStorageData, username: $scope.username })
            //             .then(function (response) {
            //                 if (response.data) {
            //                     console.log("Sản phẩm đã được lưu vào.");
            //                     localStorage.removeItem("cart");
            //                     localStorageData = [];
            //                     $scope.cartItems = [];
            //                     $scope.cart.items = [];
            //                     loadCart();
            //                 } else {
            //                     console.log("Không thể lưu sản phẩm lên server.");
            //                 }
            //             })
            //             .catch(function (error) {
            //                 console.error("Lỗi khi thực hiện API:", error);
            //             });
            //     }
            // }
        };



        // Giỏ hàng người dùng
function loadCart() {
  
      $http.get('http://localhost:8080/api/home/cart/cartDetail')
        .then(function (response) {
          if (response.data) {
            $scope.cartItems = response.data;
                count();
                // $scope.applyVoucher();

              //   console.log($scope.selectedVoucher)
              // if ($scope.selectedVoucher == null) {
                $scope.selectBestVoucher();
              // }

          }
        })
        .catch(function (error) {
          console.error("Error loading cart:", error);
        });
        

        
  }
  loadCart();
        
        
  function countTotalPrice(items) {
    return items.reduce((total, item) => {
        const price = item.promotionalPrice && item.promotionalPrice !== 0 ? item.promotionalPrice : item.price;
        return total + (price * item.quantity);
    }, 0);
}

$scope.valueVoucher = 0;

  function count() {
 if (token != null) {
    if  ($scope.cartItems == null){
        $scope.cartItems = [];
      }
        $rootScope.countProduct = $scope.cartItems.reduce((total, item) => total + item.quantity, 0);
        $scope.totalAmount = countTotalPrice($scope.cartItems);
        $scope.totalAmountAfterDiscount = $scope.totalAmount - $scope.valueVoucher;
        // $scope.getBestVoucher($scope.totalAmount)

        if (typeof $scope.dataDistrict !== 'undefined' && typeof $scope.dataDistrict.DistrictID !== 'undefined' && 
          typeof $scope.dataWard !== 'undefined' && typeof $scope.dataWard.WardCode !== 'undefined') {
          $scope.calculateShippingFee($scope.dataDistrict.DistrictID, $scope.dataWard.WardCode);
      } else {
          $scope.calculateShippingFee($scope.dataDistrict, $scope.dataWard);
      }

    }
    // $scope.selectBestVoucher();

        

    }





//   get address api
$scope.province1 = function () {
    // Định nghĩa headers với token
    var config = {
        headers: {
            'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
        }
    };
  
    // Gọi API
    $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', config)
        .then(function (response) {
            $scope.cities = response.data.data;
        })
        .catch(function (error) {
            // Xử lý lỗi nếu có
            console.error('Error calling API:', error);
        });
  };
  
  $scope.province1();
  
  
  $scope.province = function () {
    // Định nghĩa headers với token
    var config = {
        headers: {
            'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
        }
    };
    // Gọi API
    return $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', config)
        .then(function (response) {
            return response.data.data;
        })
        .catch(function (error) {
            // Xử lý lỗi nếu có
            console.error('Error calling API:', error);
        });
  };
  
  
  
  // Call the province function to load data when the controller initializes
  
  $scope.getNameProvince = function (cityId) {
    $scope.province().then(function () {
      // Kiểm tra xem mảng districts có tồn tại không
      if ($scope.cities) {
        // Tìm quận/huyện được chọn trong mảng districts
        var selectedCity = $scope.cities.find(function (city) {
          return city.ProvinceID == cityId;
        });
        $scope.dataCity = selectedCity;
        console.log($scope.dataCity)
        // $scope.billAddressCity = cityId;
      }
    });
  };
  
  
  // $scope.getNameProvince(248);
  
  
  $scope.getDistrictsByProvince = function (provinceId) {
  
    var config = {
      headers: {
        'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
      }
    };
  
    // Gọi API với tham số province_id
    return $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/district', {
      params: {
        province_id: provinceId
      },
      headers: config.headers
    })
    .then(function (response) {
      // Xử lý kết quả trả về từ API ở đây
      $scope.districts = response.data.data;
    })
    .catch(function (error) {
      console.error('Error calling API:', error);
    });
  };
  
  
  
  $scope.getNameDistrict = function (provinceId, districtId) {
  
    $scope.getDistrictsByProvince(provinceId).then(function () {
      if ($scope.districts) {
        var selectedDistrict = $scope.districts.find(function (district) {
          return district.ProvinceID == provinceId;
        });
        // $scope.billAddressDistrict = districtId;
        $scope.dataDistrict = selectedDistrict;
        console.log($scope.dataDistrict)
        
      }
    });
  };
  
  $scope.getWardsByDistrict = function (districtId) {
    var config = {
        headers: {
            'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
        }
    };
  
    // Return the promise from the $http.get call
    return $http.get('https://online-gateway.ghn.vn/shiip/public-api/master-data/ward', {
        params: {
            district_id: districtId
        },
        headers: config.headers
    })
    .then(function (response) {
        $scope.wards = response.data.data;
    })
    .catch(function (error) {
        console.error('Error calling API:', error);
    });
  };
  
  
  $scope.getNameWard = function (districtId, wardId) {
    $scope.getWardsByDistrict(districtId).then(function () {
      if ($scope.wards) {
        // Tìm quận/huyện được chọn trong mảng districts
        var selectedWard = $scope.wards.find(function (ward) {
          return ward.WardCode == wardId;
        });
  
        if (selectedWard) {
          // $scope.billAddressWard = selectedWard.WardCode;
        $scope.dataWard = selectedWard;
        console.log($scope.dataWard)
  
        } else {
          console.log("Không tìm thấy phường/xã với WardCode: " + wardId);
        }
      }
    });
  };
  
  $scope.shippingFee = 0;
  
  $scope.calculateShippingFee = function (toDistrictId, toWardCode) {
    if (toDistrictId && toWardCode && token != null && token && $rootScope.countProduct > 0) {
    let blows = (toWardCode || "").toString().replace(/\D/g, "");
    let numericDistrictId = Number(toDistrictId);
  
    // Định nghĩa headers với token
    var config = {
        headers: {
            'token': '499b0760-b3cf-11ee-a2c1-ca2feb4b63fa'
        }
    };
  
    // Body data for the POST request
    var requestData = {
      "service_id": 53321,
      "insurance_value": $scope.totalAmountAfterDiscount,
      "coupon": null,
      "from_district_id": 3440,
      "to_district_id": numericDistrictId,
      "to_ward_code": blows,  // Convert to string
      "height": 15,
      "length": 15,
      "weight": 700 * $rootScope.countProduct,
      "width": 15
  };
  
  
    // Gọi API với phương thức POST và thân yêu cầu (body)
    $http.post('https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', requestData, config)
        .then(function (response) {
            $scope.shippingFee = response.data.data.total;
        })
        .catch(function (error) {
            // Xử lý lỗi nếu có
            console.error('Error calling API:', error);
  
           
            $scope.shippingFee = 100000;
            
        });
      }
  
  
  };


    //PayMentMethod
    $scope.loadPaymentMethod = function() {
        $http.get('http://localhost:8080/api/home/paymentMethods')
          .then(function(response) {
            if (response.data) {
              $scope.listPaymentMethods = response.data;
            }
          })
          .catch(function(error) {
            alert("Có lỗi xảy ra khi gọi API!");
            console.error(error);
          });
      };

      $scope.selectedPayment = null; // Khởi tạo giá trị ban đầu
      $scope.selectedPaymentCode = null; // Khởi tạo giá trị ban đầu
      
      $scope.selectPaymentMethod = function(paymentMethod) {
          $scope.selectedPayment = paymentMethod; 
          $scope.selectedPaymentCode = paymentMethod.code
          console.log($scope.selectedPaymentCode)
      };


    //   $scope.hasProductsInBill = function() {
    //     return $scope.bill && $scope.bill.billDetail && $scope.bill.billDetail.length > 0;
    //   };
      



// bill -----------------------------

$scope.isPaymentClicked = false;
$scope.checkPhoneNumber= true;
    $scope.address = "";
    // paymentMethod voucher
    $scope.bill = {
      code: 'HD' + Number(String(new Date().getTime()).slice(-6)),
      // createdAt: '',
      // paymentDate: '',
      totalAmount: 0,
      totalAmountAfterDiscount: 0,
      reciverName: "",
      // deliveryDate: '',
      shippingFee: 0,
      address: "",
      phoneNumber: "",
    //   note: "",
      typeBill: 3,
      status: 10,
      reason: 0,
      note: "",
      paymentMethod: "",
      voucher: "",
      createdAt: new Date(),
    //   customerEntity: "",
      get billDetail(){
     if ($scope.cartItems && $scope.cartItems.length > 0) {
      return $scope.cartItems.map(item => {
        return {
            productDetail: item.productDetail,
            price: item.price,
            promotionalPrice: (item.promotionalPrice !== null && item.promotionalPrice !== undefined) ? item.promotionalPrice : 0,
            quantity: item.quantity,
            status: 1
        };
        
      });
  } else {
      return []; 
  }
},
      purchase() {
       

      // Kiểm tra các trường thông tin bắt buộc
let isBillReciverInvalid = !$scope.bill.reciverName || $scope.bill.reciverName.trim().length === 0;
let isBillAddressDetailInvalid = !$scope.billAddressDetail || $scope.billAddressDetail.trim().length === 0;

let isBillAddressCityInvalid = !$scope.dataCity || !$scope.dataCity.ProvinceName || $scope.dataCity.ProvinceName.trim().length === 0;

let isBillAddressWardInvalid = !$scope.dataWard || !$scope.dataWard.WardName || $scope.dataWard.WardName.trim().length === 0;

let isBillAddressDistrictInvalid = !$scope.dataDistrict || !$scope.dataDistrict.DistrictName || $scope.dataDistrict.DistrictName.trim().length === 0;


let isBillPhoneNumberInvalid = !isValidPhoneNumber($scope.bill.phoneNumber);

let isBillPaymentInvalid = $scope.selectedPayment == null;



// Thêm kiểm tra cho các trường khác nếu cần

// Hiển thị thông báo lỗi dưới các trường thông tin
$scope.isBillReciverInvalid = isBillReciverInvalid;
$scope.isBillAddressDetailInvalid = isBillAddressDetailInvalid;
$scope.isBillAddressCityInvalid = isBillAddressCityInvalid;
$scope.isBillAddressDistrictInvalid = isBillAddressDistrictInvalid;
$scope.isBillAddressWardInvalid = isBillAddressWardInvalid;
$scope.isBillPhoneNumberInvalid = isBillPhoneNumberInvalid;
$scope.isBillPaymentInvalid = isBillPaymentInvalid;
// Hiển thị thông báo lỗi cho các trường khác nếu cần

// Tiếp tục chỉ khi không có lỗi
if (isBillReciverInvalid || isBillAddressDetailInvalid || isBillAddressCityInvalid || isBillAddressDistrictInvalid || isBillAddressWardInvalid || isBillPhoneNumberInvalid || isBillPaymentInvalid) {
  $scope.showErrorNotification("Vui lòng nhập đầy đủ thông tin!");
  return;
}

var fullAddress =
$scope.billAddressDetail +
', ' +
$scope.dataWard.WardName +
', ' +
$scope.dataDistrict.DistrictName +
', ' +
$scope.dataCity.ProvinceName;

        // Nếu thông tin đã đầy đủ, tiến hành gửi dữ liệu lên server
        var bill = angular.copy(this);
        bill.totalAmount = $scope.totalAmount + $scope.shippingFee;
        bill.totalAmountAfterDiscount = $scope.totalAmountAfterDiscount + $scope.shippingFee;
        bill.address = fullAddress;
        bill.paymentMethod = $scope.selectedPayment;
        bill.voucher = $scope.voucherData;
        // bill.customerEntity = $scope.userData;
        bill.shippingFee = $scope.shippingFee;
        // Tiến hành gửi dữ liệu lên server
      console.log(bill)


        $http.post("http://localhost:8080/api/home/bill/create", bill)
          .then(resp => {
           // Xử lý phản hồi từ server
              let body = resp.data;
              console.log(body);
              if (body == 333) {
                $location.path('/home/paymentSuccess');
              }
              if (body != null && body.hasOwnProperty("redirect") ) {
                  window.location.href = body.redirect; 
              } else if (typeof body === 'object' && body.hasOwnProperty("resultCode") && body.hasOwnProperty("insufficientQuantityProducts")) {
                // Kiểm tra xem có danh sách sản phẩm không đủ số lượng hay không
                let resultCode = body.resultCode;
                if (resultCode === 2) {
                    // Hiển thị thông báo cho từng sản phẩm không đủ số lượng
                    let insufficientQuantityProducts = body.insufficientQuantityProducts;
                    insufficientQuantityProducts.forEach(product => {
                        $scope.showErrorNotification( product + "không có đủ số lượng trong kho");
                    });
                }
                // Xử lý logic tương ứng với resultCode khác
            } else if (typeof body === 'number') {
                 if (body === 3) {
                    $scope.showErrorNotification("Mã giảm giá không có đủ số lượng trong kho vui lòng chọn mã giảm giá kho")
                      // Xử lý logic tương ứng
                  } else {
                    $scope.showWarningNotification("Có lỗi xảy ra!");

                  }
              } else {
                $scope.showWarningNotification("Có lỗi xảy ra!");

              }
          })
          .catch(error => {
            $scope.showErrorNotification("Đặt hàng thất bại");
            console.log(error);
          });
   
    }
      
  }




    // Chọn address
    $scope.fillDataToBill  = function(address) {
        if (address != '') {
        
          $scope.defaultAddress = address;
          // Gán dữ liệu từ địa chỉ mặc định vào hóa đơn
          $scope.bill.reciverName = $scope.defaultAddress.name;
          $scope.bill.phoneNumber = $scope.defaultAddress.phoneNumber;
        
          var addressComponents = $scope.defaultAddress.address.split(',');
        
          // Xử lý dữ liệu địa chỉ
          if (addressComponents.length >= 1) {
            $scope.billAddressDetail = addressComponents[0].trim();
          }
        
          var addressComponentsId = $scope.defaultAddress.addressId.split(',');
          if (addressComponentsId.length >= 1) {
            $scope.dataCity = addressComponentsId[2].trim();
          $scope.dataDistrict = addressComponentsId[1].trim();
          $scope.dataWard = addressComponentsId[0].trim();
          }
        $scope.getNameProvince($scope.dataCity)
        $scope.getNameDistrict($scope.dataCity,$scope.dataDistrict)
        $scope.getNameWard($scope.dataDistrict,$scope.dataWard)

        // count();
        $scope.calculateShippingFee($scope.dataDistrict,$scope.dataWard)
        }
        
        };

    $scope.openAddress = function() {
        $('#addressModal').modal('show');
        $scope.getAddressList();
      };
      $scope.closeAddress = function() {
        $('#addressModal').modal('hide');
      };


      $scope.getAddressList = function() {
        if (token != null && token) {
            $http.get('http://localhost:8080/api/home/address')
                .then(function successCallback(response) {
                    if (response.data) {
                        $scope.addressList = response.data;
                    } else {
                        // Xử lý khi không có địa chỉ trả về
                        $scope.addressList = [];
                        console.warn('No addresses found');
                    }
                }, function errorCallback(response) {
                    // Xử lý khi gọi API không thành công
                    console.error('Error while fetching data');
                });
        }
    };
    
    // $scope.addressData.defaultAddress = '';
    
    $scope.getDefaultAddress = function() {
        if (token != null && token) {
            $http.get('http://localhost:8080/api/home/addressDefault')
                .then(function(response) {
                    if (response.data) {
                        $scope.fillDataToBill(response.data);
                    } else {
                        // Xử lý khi không có địa chỉ mặc định trả về
                        console.warn('No default address found');
                    }
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
        }
    };
    


      $scope.getDefaultAddress();

      $scope.selectAddressBill  = function(address) {
        $scope.fillDataToBill(address);
      
      };



      function isValidPhoneNumber(phoneNumber) {
        // Regular expression cho số điện thoại di động và cố định Việt Nam
        const regexMobile = /(09|03|07|08|05)+([0-9]{8})\b/;
        const regexLandline = /(02|024|028)+([0-9]{7})\b/;
      
        return regexMobile.test(phoneNumber) || regexLandline.test(phoneNumber);
      }










    //   home ------------------------

    $scope.slideIndex = 0;
    let slideInterval;
    
    $scope.showSlide = function(index) {
        const slides = document.querySelectorAll('.carousel-item');
        if (slides.length > 0) {
            if (index >= slides.length) { $scope.slideIndex = 0; }
            if (index < 0) { $scope.slideIndex = slides.length - 1; }
            for (let i = 0; i < slides.length; i++) {
                slides[i].classList.remove('active');
            }
            slides[$scope.slideIndex].classList.add('active');
        }
    }
    
    $scope.prevSlide = function() {
        $scope.slideIndex--;
        $scope.showSlide($scope.slideIndex);
    }
    
    $scope.nextSlide = function() {
        $scope.slideIndex++;
        $scope.showSlide($scope.slideIndex);
    }
    
    function startSlideShow() {
        slideInterval = setInterval(() => {
            $scope.nextSlide();
        }, 20000); // Chuyển slide sau mỗi 20 giây (20000 milliseconds)
    }
    
    // Bắt đầu chuyển slide tự động khi tải trang
    startSlideShow();
    
  
  
  
  
  
  
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
  
  
      $scope.getAllSalePaths = function() {
        $http.get('http://localhost:8080/api/home/salePaths')
          .then(function(response) {
            if (response.data) {
              $scope.salePaths = response.data;
            }
          })
          .catch(function(error) {
            alert("Có lỗi xảy ra khi gọi API!");
            console.error(error);
          });
    };
  
    $scope.getAllSalePaths();
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
  
      $scope.overlaySearchActive = false;

      $scope.openOverlaySearch = function() {
          $scope.overlaySearchActive = true;
      };
      
      $scope.closeOverlay = function(event) {
          if (event.target.id === 'overlay') {
              $scope.overlaySearchActive = false;
          }
      };
      
      $scope.toggleOverlaySearch = function() {
          $scope.overlaySearchActive = !$scope.overlaySearchActive;
      };
      
  
    // $scope.loadProductSearch = function() {
    //     // Logic to load products based on $scope.searchText
    // };
  
  
    $scope.searchProducts = function(search) {
      if (!search || search.trim() === "") {
          $scope.searchedProducts = null;
          return;
      }
      $scope.searchTerm= search;
 

      $http.get('http://localhost:8080/api/home/product/search', {
          params: { name: search }
      })
      .then(function(response) {
          if (response.data) {
              $scope.searchedProducts = response.data;
          console.log($scope.searchedProducts);

          }
      })
      .catch(function(error) {
          alert("Có lỗi xảy ra khi tìm kiếm sản phẩm!");
          console.error(error);
      });
  };
  
  $scope.viewMore = function() {
    localStorage.setItem('searchTerm', $scope.searchTerm);
    $location.path('/home/product');
};


  // voucher ---------------

  $scope.vouchers = [];
  $scope.displayedVouchers = [];
  $scope.showAllVouchers = false;
  
  $scope.getAllVouchers = function() {
    $http.get('http://localhost:8080/api/home/vouchers')
      .then(function(response) {
        if (response.data) {
          $scope.vouchers = response.data;
          $scope.displayedVouchers = $scope.vouchers.slice(0, 4); // Hiển thị 6 vouchers ban đầu
        }
      })
      .catch(function(error) {
        alert("Có lỗi xảy ra khi gọi API để lấy danh sách vouchers!");
        console.error(error);
      });
  };
  
  $scope.viewMoreVouchers = function() {
    $scope.showAllVouchers = true;
    $scope.displayedVouchers = $scope.vouchers;
  };

  // $scope.getAllVouchers();
  // $scope.getBestVoucher = function(totalAmount) {
  //   $http.get('http://localhost:8080/api/home/bestVoucher', {
  //       params: { totalAmount: totalAmount }
  //     })
  //     .then(function(response) {
  //       if (response.data) {
  //         console.log(response.data)
          
  //         $scope.selectedVoucher = response.data;
  //         response.data.selected = true;
  //         $scope.applyVoucher();
  //       } 
  //     })
  //     .catch(function(error) {
  //       alert("Có lỗi xảy ra khi gọi API để lấy voucher tốt nhất!");
  //       console.error(error);
  //     });

  // };
$scope.selectBestVoucher = function() {
  if (!$scope.customerVouchers || $scope.customerVouchers.length === 0) {
    console.log("Không có vouchers để chọn.");
    return;
  }

  // Sử dụng reduce để tìm best voucher dựa trên totalAmount và minimumTotalAmount
  var bestVoucher = $scope.customerVouchers.reduce(function(minVoucher, currentVoucher) {
    // Kiểm tra nếu voucher còn số lượng và totalAmount đủ lớn để áp dụng voucher này
    if (currentVoucher.quantity > 0 && $scope.totalAmount >= currentVoucher.minimumTotalAmount) {
      // Tính độ chênh lệch giữa totalAmount và minimumTotalAmount của voucher
      var difference = $scope.totalAmount - currentVoucher.minimumTotalAmount;
      console.log(difference);
      // So sánh để chọn voucher có độ chênh lệch nhỏ nhất
      return difference < minVoucher.difference ? { voucher: currentVoucher, difference: difference } : minVoucher;
    }
    return minVoucher;
  }, { voucher: null, difference: Infinity }).voucher; // Khởi tạo giá trị ban đầu cho minVoucher và difference

  // Nếu tìm được voucher phù hợp, chọn và áp dụng
  if (bestVoucher) {
    if ($scope.selectedVoucher) {
      $scope.selectedVoucher.selected = false; // Bỏ chọn voucher trước đó
    }
    $scope.selectedVoucher = bestVoucher;
    bestVoucher.selected = true;
    $scope.applyVoucher();
  } else {
    // Không tìm được voucher phù hợp
    console.log("Không tìm được voucher phù hợp.");
  }
};



  $scope.getVouchersForCustomer = function() {
    $http.get('http://localhost:8080/api/home/customer/vouchers')
      .then(function(response) {
        if (response.data) {
          $scope.customerVouchers = response.data;
          // Thêm khoảng thời gian trễ trước khi thực hiện hành động tiếp theo
          setTimeout(function() {
            // Gọi hàm chọn voucher tốt nhất sau khi delay
            $scope.$apply(function() {
              if ($scope.customerVouchers && $scope.customerVouchers.length > 0) {
                $scope.selectBestVoucher();
              } else {
                console.log("Không có vouchers cho khách hàng.");
              }
            });
          }, 1000); // Thời gian trễ là 1000ms (1 giây)
        }
      })
      .catch(function(error) {
        alert("Có lỗi xảy ra khi gọi API để lấy vouchers cho khách hàng!");
        console.error(error);
      });
  };
  
  

  
  $scope.getVouchersForCustomer();
  
  $scope.openVoucherModal = function() {
    $('#voucherModal').modal('show');
  };
  
  // $scope.originalTotalAmount = $scope.totalAmount; // Store the original total amount
  
  $scope.selectVoucher = function(selectedVoucher) {
    console.log(selectedVoucher);
    if (selectedVoucher.quantity > 0) {
      if ($scope.selectedVoucher === selectedVoucher) {
        $scope.selectedVoucher = null;
        selectedVoucher.selected = false;
        $scope.applyVoucher();
      } else {
        if ($scope.selectedVoucher) {
          $scope.selectedVoucher.selected = false; // Bỏ chọn voucher trước đó
        }
        $scope.selectedVoucher = selectedVoucher;
        selectedVoucher.selected = true;
        $scope.applyVoucher();
      }
    }
  };
  
  


  $scope.applyVoucher = function() {
    if ($scope.selectedVoucher != null) {
      if ($scope.selectedVoucher.quantity > 0) {
        if ($scope.totalAmount >= $scope.selectedVoucher.minimumTotalAmount) {
          var voucherCopy = angular.copy($scope.selectedVoucher);
          delete voucherCopy.selected;
          $scope.voucherData = voucherCopy;
  
          if ($scope.selectedVoucher.discountType === 1) {
            // Percentage discount
            var discountPercentage = $scope.selectedVoucher.value / 100;
            $scope.valueVoucher = $scope.totalAmount * discountPercentage;
            if ($scope.valueVoucher > $scope.selectedVoucher.maximumReductionValue) {
              $scope.valueVoucher = $scope.selectedVoucher.maximumReductionValue;
            }
            $scope.totalAmountAfterDiscount = $scope.totalAmount - $scope.valueVoucher;
  
          } else if ($scope.selectedVoucher.discountType === 2) {
            // Fixed amount discount
            $scope.valueVoucher = $scope.selectedVoucher.value;
            if ($scope.valueVoucher > $scope.selectedVoucher.maximumReductionValue) {
              $scope.valueVoucher = $scope.selectedVoucher.maximumReductionValue;
            }
            $scope.totalAmountAfterDiscount = $scope.totalAmount - $scope.valueVoucher;
          }
  
          $scope.voucherMessage = 'Mã giảm giá đã được áp dụng';
          $scope.showSuccessNotification($scope.voucherMessage);
        } else {
          $scope.voucherData = null;
          $scope.valueVoucher = 0;
          $scope.voucherMessage =
            'Mã giảm giá ' +
            $scope.selectedVoucher.code +
            ' chỉ sử dụng cho đơn hàng có tổng trị giá trên ' +
            $filter('number')($scope.selectedVoucher.minimumTotalAmount) +
            ' đ';
          $scope.showErrorNotification($scope.voucherMessage);
  
          if ($scope.totalAmount < $scope.selectedVoucher.minimumTotalAmount) {
            $scope.selectedVoucher.selected = false;
            $scope.selectedVoucher = null;
            $scope.totalAmountAfterDiscount = $scope.totalAmount; // Initialize totalAmountAfterDiscount
          }
        }
      }
    } else {
      // No voucher selected, reset to original total amount
                  // $scope.selectBestVoucher();

      $scope.voucherData = null;
      $scope.voucherMessage = '';
      $scope.valueVoucher = 0;
      $scope.totalAmountAfterDiscount = $scope.totalAmount;
    }
  };



  

  
// Đoạn mã AngularJS
// Đoạn mã AngularJS



  

});
