app.controller("cartController", function ($scope, $http, $window) {




    console.log("Hello")
  // address

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
  
  
  
  $scope.calculateShippingFee = function (toDistrictId, toWardCode) {
    // console.log(toDistrictId)
    // console.log(toWardCode)
    if (toDistrictId && toWardCode && $rootScope.countProduct > 0) {
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
  

});
