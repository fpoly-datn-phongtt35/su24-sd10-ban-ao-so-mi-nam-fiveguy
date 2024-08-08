app.controller("accountManage", function ($scope, $http, $window) {
  $scope.logout = function () {
    localStorage.removeItem("token");
    localStorage.removeItem("refreshToken");
    window.location.href = "http://127.0.0.1:5555/src/user/index.html#/home";
  };

  $scope.user = {};
  const idCustomer = null;
  const emailAccount = null;

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
  $scope.showSuccessNotification = function (message) {
    toastr["success"](message);
  };

  // Hàm hiển thị thông báo lỗi
  $scope.showErrorNotification = function (message) {
    toastr["error"](message);
  };

  $scope.showWarningNotification = function (message) {
    toastr["warning"](message);
  };

  $scope.getUser = function () {
    $http.get("http://localhost:8080/api/ol/getUser").then(function (response) {
      $scope.user = response.data;
      $scope.idCustomer = response.data.id;
      $scope.emailAccount = response.data.account.email;
      console.log($scope.emailAccount);
    });
  };

  $scope.getUser();

  $scope.getAddressList = function () {
    $http.get("http://localhost:8080/api/ol/authenticated/address").then(
      function successCallback(response) {
        $scope.addressList = response.data;
      },
      function errorCallback(response) {
        // Xử lý khi gọi API không thành công
        console.error("Error while fetching data");
      }
    );
  };

  $scope.addressData = {}; // Khai báo biến addressData trong $scope
  $scope.addressData = {};
  $scope.dataCity = { ProvinceID: "" };
  $scope.dataDistrict = { DistrictID: "" };
  $scope.dataWard = { WardCode: "" };

  $scope.fillDataToUpdate = function (selectedAddress) {
    $scope.addressData = angular.copy(selectedAddress);
    var addressComponents = $scope.addressData.address.split(",");

    if (addressComponents.length >= 1) {
      $scope.addressData.addressDetail = addressComponents[0].trim();
    }

    var addressComponentsId = $scope.addressData.addressId.split(",");
    if (addressComponentsId.length >= 1) {
      $scope.dataCity.ProvinceID = addressComponentsId[2].trim();
      $scope.dataDistrict.DistrictID = addressComponentsId[1].trim();
      $scope.dataWard.WardCode = addressComponentsId[0].trim();
    }

    $scope.getNameProvince($scope.dataCity.ProvinceID);
    $scope.getNameDistrict(
      $scope.dataCity.ProvinceID,
      $scope.dataDistrict.DistrictID
    );
    $scope.getNameWard(
      $scope.dataDistrict.DistrictID,
      $scope.dataWard.WardCode
    );
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
        console.log($scope.dataCity);
        // $scope.billAddressCity = cityId;
      }
    });
  };

  // $scope.getNameProvince(248);

  $scope.getDistrictsByProvince = function (provinceId) {
    var config = {
      headers: {
        token: "499b0760-b3cf-11ee-a2c1-ca2feb4b63fa",
      },
    };

    // Gọi API với tham số province_id
    return $http
      .get(
        "https://online-gateway.ghn.vn/shiip/public-api/master-data/district",
        {
          params: {
            province_id: provinceId,
          },
          headers: config.headers,
        }
      )
      .then(function (response) {
        // Xử lý kết quả trả về từ API ở đây
        $scope.districts = response.data.data;
      })
      .catch(function (error) {
        console.error("Error calling API:", error);
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
        console.log($scope.dataDistrict);
      }
    });
  };

  $scope.getWardsByDistrict = function (districtId) {
    var config = {
      headers: {
        token: "499b0760-b3cf-11ee-a2c1-ca2feb4b63fa",
      },
    };

    // Return the promise from the $http.get call
    return $http
      .get("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward", {
        params: {
          district_id: districtId,
        },
        headers: config.headers,
      })
      .then(function (response) {
        $scope.wards = response.data.data;
      })
      .catch(function (error) {
        console.error("Error calling API:", error);
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
          console.log($scope.dataWard);
        } else {
          console.log("Không tìm thấy phường/xã với WardCode: " + wardId);
        }
      }
    });
  };

  // address

  $scope.province1 = function () {
    // Định nghĩa headers với token
    var config = {
      headers: {
        token: "499b0760-b3cf-11ee-a2c1-ca2feb4b63fa",
      },
    };

    // Gọi API
    $http
      .get(
        "https://online-gateway.ghn.vn/shiip/public-api/master-data/province",
        config
      )
      .then(function (response) {
        $scope.cities = response.data.data;
      })
      .catch(function (error) {
        // Xử lý lỗi nếu có
        console.error("Error calling API:", error);
      });
  };

  $scope.province1();

  $scope.province = function () {
    // Định nghĩa headers với token
    var config = {
      headers: {
        token: "499b0760-b3cf-11ee-a2c1-ca2feb4b63fa",
      },
    };
    // Gọi API
    return $http
      .get(
        "https://online-gateway.ghn.vn/shiip/public-api/master-data/province",
        config
      )
      .then(function (response) {
        return response.data.data;
      })
      .catch(function (error) {
        // Xử lý lỗi nếu có
        console.error("Error calling API:", error);
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
        console.log($scope.dataCity);
        // $scope.billAddressCity = cityId;
      }
    });
  };

  // $scope.getNameProvince(248);

  $scope.getDistrictsByProvince = function (provinceId) {
    var config = {
      headers: {
        token: "499b0760-b3cf-11ee-a2c1-ca2feb4b63fa",
      },
    };

    // Gọi API với tham số province_id
    return $http
      .get(
        "https://online-gateway.ghn.vn/shiip/public-api/master-data/district",
        {
          params: {
            province_id: provinceId,
          },
          headers: config.headers,
        }
      )
      .then(function (response) {
        // Xử lý kết quả trả về từ API ở đây
        $scope.districts = response.data.data;
      })
      .catch(function (error) {
        console.error("Error calling API:", error);
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
        console.log($scope.dataDistrict);
      }
    });
  };

  $scope.getWardsByDistrict = function (districtId) {
    var config = {
      headers: {
        token: "499b0760-b3cf-11ee-a2c1-ca2feb4b63fa",
      },
    };

    // Return the promise from the $http.get call
    return $http
      .get("https://online-gateway.ghn.vn/shiip/public-api/master-data/ward", {
        params: {
          district_id: districtId,
        },
        headers: config.headers,
      })
      .then(function (response) {
        $scope.wards = response.data.data;
      })
      .catch(function (error) {
        console.error("Error calling API:", error);
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
          console.log($scope.dataWard);
        } else {
          console.log("Không tìm thấy phường/xã với WardCode: " + wardId);
        }
      }
    });
  };

  $scope.deleteDataAddress = function (addressId) {
    $http
      .delete(
        "http://localhost:8080/api/ol/authenticated/deleteAddress/" + addressId
      )
      .then(
        function successCallback(response) {
          $scope.getAddressList();
          $scope.showSuccessNotification("Đã xóa địa chỉ thành công");
        },
        function errorCallback(response) {
          console.error("Không thể xóa địa chỉ");
          $scope.showErrorNotification("Xóa địa chỉ thất bại");
        }
      );
  };

  $scope.deleteDataAddress = function (addressId) {
    $http
      .delete(
        "http://localhost:8080/api/ol/authenticated/deleteAddress/" + addressId
      )
      .then(
        function successCallback(response) {
          $scope.getAddressList();
          $scope.showSuccessNotification("Đã xóa địa chỉ thành công");
        },
        function errorCallback(response) {
          console.error("Không thể xóa địa chỉ");
          $scope.showErrorNotification("Xóa địa chỉ thất bại");
        }
      );
  };

  $scope.updateAddress = function () {
    // Kiểm tra các trường thông tin bắt buộc
    let isNameUserAddressInvalid =
      !$scope.addressData.name || $scope.addressData.name.trim().length === 0;
    let isNameDetailAddressInvalid =
      !$scope.addressData.addressDetail ||
      $scope.addressData.addressDetail.trim().length === 0;

    let isCityAddressInvalid =
      !$scope.dataCity ||
      !$scope.dataCity.ProvinceName ||
      $scope.dataCity.ProvinceName.trim().length === 0;

    let isWardAddressInvalid =
      !$scope.dataWard ||
      !$scope.dataWard.WardName ||
      $scope.dataWard.WardName.trim().length === 0;

    let isDistrictAddressInvalid =
      !$scope.dataDistrict ||
      !$scope.dataDistrict.DistrictName ||
      $scope.dataDistrict.DistrictName.trim().length === 0;

    let isPhoneNumberAddressInvalid = !isValidPhoneNumber(
      $scope.addressData.phoneNumber
    );
    // Thêm kiểm tra cho các trường khác nếu cần
    // Hiển thị thông báo lỗi dưới các trường thông tin
    $scope.isNameUserAddressInvalid = isNameUserAddressInvalid;
    $scope.isNameDetailAddressInvalid = isNameDetailAddressInvalid;
    $scope.isCityAddressInvalid = isCityAddressInvalid;
    $scope.isDistrictAddressInvalid = isDistrictAddressInvalid;
    $scope.isWardAddressInvalid = isWardAddressInvalid;
    $scope.isPhoneNumberAddressInvalid = isPhoneNumberAddressInvalid;
    // Hiển thị thông báo lỗi cho các trường khác nếu cần

    // Tiếp tục chỉ khi không có lỗi
    if (
      isNameUserAddressInvalid ||
      isNameDetailAddressInvalid ||
      isCityAddressInvalid ||
      isWardAddressInvalid ||
      isPhoneNumberAddressInvalid ||
      isDistrictAddressInvalid
    ) {
      return;
    }

    var fullAddress =
      $scope.addressData.addressDetail +
      ", " +
      $scope.dataWard.WardName +
      ", " +
      $scope.dataDistrict.DistrictName +
      ", " +
      $scope.dataCity.ProvinceName;

    var idFullAddress =
      $scope.dataWard.WardCode +
      ", " +
      $scope.dataDistrict.DistrictID +
      ", " +
      $scope.dataCity.ProvinceID;

    // Tạo object để gửi thông tin địa chỉ
    var updatedAddress = {
      id: $scope.addressData.id,
      name: $scope.addressData.name,
      phoneNumber: $scope.addressData.phoneNumber,
      address: fullAddress,
      addressId: idFullAddress,
      status: $scope.addressData.status,
      createdAt: $scope.addressData.createdAt,
      defaultAddress: $scope.addressData.defaultAddress,
    };
    $http
      .post(
        "http://localhost:8080/api/ol/authenticated/updateAddress",
        updatedAddress
      )
      .then(function (response) {
        $scope.getAddressList();
        $scope.showSuccessNotification("Cập nhật địa chỉ thành công!");
      })
      .catch(function (error) {
        $scope.showErrorNotification("Cập nhật địa chỉ thất bại!");
      });
  };

  $scope.addAddress = function () {
    // Kiểm tra các trường thông tin bắt buộc
    let isNameUserAddressInvalid =
      !$scope.addressData.name || $scope.addressData.name.trim().length === 0;
    let isNameDetailAddressInvalid =
      !$scope.addressData.addressDetail ||
      $scope.addressData.addressDetail.trim().length === 0;
    let isCityAddressInvalid =
      !$scope.dataCity ||
      !$scope.dataCity.ProvinceName ||
      $scope.dataCity.ProvinceName.trim().length === 0;

    let isWardAddressInvalid =
      !$scope.dataWard ||
      !$scope.dataWard.WardName ||
      $scope.dataWard.WardName.trim().length === 0;

    let isDistrictAddressInvalid =
      !$scope.dataDistrict ||
      !$scope.dataDistrict.DistrictName ||
      $scope.dataDistrict.DistrictName.trim().length === 0;

    let isPhoneNumberAddressInvalid = !isValidPhoneNumber(
      $scope.addressData.phoneNumber
    );
    // Thêm kiểm tra cho các trường khác nếu cần
    // Hiển thị thông báo lỗi dưới các trường thông tin
    $scope.isNameUserAddressInvalid = isNameUserAddressInvalid;
    $scope.isNameDetailAddressInvalid = isNameDetailAddressInvalid;
    $scope.isCityAddressInvalid = isCityAddressInvalid;
    $scope.isDistrictAddressInvalid = isDistrictAddressInvalid;
    $scope.isWardAddressInvalid = isWardAddressInvalid;
    $scope.isPhoneNumberAddressInvalid = isPhoneNumberAddressInvalid;
    // Hiển thị thông báo lỗi cho các trường khác nếu cần

    // Tiếp tục chỉ khi không có lỗi
    if (
      isNameUserAddressInvalid ||
      isNameDetailAddressInvalid ||
      isCityAddressInvalid ||
      isWardAddressInvalid ||
      isPhoneNumberAddressInvalid ||
      isDistrictAddressInvalid
    ) {
      return;
    }

    var fullAddress =
      $scope.addressData.addressDetail +
      ", " +
      $scope.dataWard.WardName +
      ", " +
      $scope.dataDistrict.DistrictName +
      ", " +
      $scope.dataCity.ProvinceName;

    var idFullAddress =
      $scope.dataWard.WardCode +
      ", " +
      $scope.dataDistrict.DistrictID +
      ", " +
      $scope.dataCity.ProvinceID;

    // Tạo object để gửi thông tin địa chỉ mới
    var newAddress = {
      name: $scope.addressData.name,
      phoneNumber: $scope.addressData.phoneNumber,
      address: fullAddress,
      addressId: idFullAddress,
      defaultAddress: $scope.addressData.defaultAddress,
    };
    $http
      .post("http://localhost:8080/api/ol/authenticated/addAddress", newAddress)
      .then(function (response) {
        $scope.getAddressList();
        // $scope.showSuccessNotification("Thêm địa chỉ mới thành công!");
      })
      .catch(function (error) {
        // $scope.showErrorNotification("Thêm địa chỉ mới thất bại!");
      });
  };

  function isValidPhoneNumber(phoneNumber) {
    // Regular expression cho số điện thoại di động và cố định Việt Nam
    const regexMobile = /(09|03|07|08|05)+([0-9]{8})\b/;
    const regexLandline = /(02|024|028)+([0-9]{7})\b/;

    return regexMobile.test(phoneNumber) || regexLandline.test(phoneNumber);
  }

  // /==============================Code tịnh =============================================================
  imgShow("image-update", "image-preview-update");
  $scope.passwordData = {};

  function imgShow(imageInputId, imagePreviewId) {
    const imageInput = document.getElementById(imageInputId);
    const imagePreview = document.getElementById(imagePreviewId);

    if (imageInput && imagePreview) {
      // Add click event to the image preview to open the file dialog
      imagePreview.addEventListener("click", function () {
        imageInput.click();
      });

      imageInput.addEventListener("change", function () {
        if (imageInput.files && imageInput.files[0]) {
          const reader = new FileReader();
          reader.onload = function (e) {
            imagePreview.src = e.target.result;
            // Cập nhật giá trị vào $scope.user.avatar
            $scope.$apply(function () {
              $scope.user.avatar = e.target.result;
            });
          };
          reader.readAsDataURL(imageInput.files[0]);
        }
      });
    } else {
      console.error("Element not found");
    }
  }

  $scope.submitUpdate = function () {
    const data = {
      code: $scope.user.code,
      avatar: $scope.user.avatar, // Đã cập nhật avatar mới
      fullName: $scope.user.fullName,
      gender: $scope.user.gender,
      account: $scope.user.account,
      customerType: $scope.user.customerType,
      birthDate: $scope.user.birthDate,
      status: $scope.user.status,
      createdAt: $scope.user.createdAt,
      updatedAt: $scope.user.updatedAt,
      createdBy: $scope.user.createdBy,
      updatedBy: $scope.user.updatedBy,
    };

    $http
      .put(
        `http://localhost:8080/api/ol/update-user/${$scope.idCustomer}`,
        data
      )
      .then(function (response) {
        $scope.getUser();
        $scope.showSuccessNotification(
          "Cập nhật thông tin thành công!"
        );
        console.log(response.data);
      });
  };

  //hàm đưa ảnh lên drive
  $scope.postFile = async function (postData) {
    try {
      const response = await fetch(
        "https://script.google.com/macros/s/AKfycbyVkDrfWiVgO0GoGdY1WlGuKt2tJLUZR-2qXGdgaZMfEK694pQgtiL7SHDPwoP8LHFrvA/exec",
        {
          method: "POST",
          body: JSON.stringify(postData),
        }
      );
      const data = await response.json();
      console.log(data);
      $scope.uploadedImageData = data.link; // Store the image link globally
      // const image = document.getElementById("image");
      // image.src = data.link + "&sz=s500";
      $scope.$apply();
    } catch (error) {
      alert("Vui lòng thử lại");
    }
  };
  //Sửa khách hàng
  $scope.uploadBtnUpdate = function () {
    return new Promise(async (resolve, reject) => {
      const fileInputupdate = document.getElementById("image-update");
      const file = fileInputupdate.files[0];

      if (!file) {
        $scope.showError = true;
        const dataObject = {
          code: $scope.user.code,
          avatar: $scope.user.avatar,
          fullName: $scope.user.fullName,
          gender: $scope.user.gender,
          account: $scope.user.account,
          customerType: $scope.user.customerType,
          birthDate: $scope.user.birthDate,
          status: $scope.user.status,
          createdAt: $scope.user.createdAt,
          updatedAt: $scope.user.updatedAt,
          createdBy: $scope.user.createdBy,
          updatedBy: $scope.user.updatedBy,
        };
        console.log(dataObject);
        try {
          const updateCustomersData = await $scope.updateCustomer(dataObject);
          console.log("updateCustomersData = ", updateCustomersData);
          $scope.$apply();
          resolve(updateCustomersData);
        } catch (error) {
          console.error("Error:", error);
          reject(error);
        }
        return;
      } else {
        $scope.showError = false;
        $scope.uploading = true;

        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function () {
          const data = reader.result.split(",")[1];
          const postData = {
            name: file.name,
            type: file.type,
            data: data,
          };
          $scope
            .postFile(postData)
            .then(function () {
              $scope.uploading = false;
              $scope.$apply();
              if (!$scope.uploading) {
                $scope.submitFormUpdate();
                $scope.showSuccessNotification(
                  "Cập nhật thông tin thành công!"
                );
              }
              resolve();
            })
            .catch(function (error) {
              console.error("Error:", error);
              reject(error);
            });
        };
      }
    });
  };

  // Thêm mới nhân viên
  $scope.updateCustomer = async (objectData) => {
    try {
      const result = await $http.put(
        `http://localhost:8080/api/ol/update-user/${$scope.idCustomer}`,
        objectData
      );
      console.log("Sửa Customer thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi sửa Customer", error);
      throw error;
    }
  };

  // Form submit update
  $scope.submitFormUpdate = async function () {
    const dataObject = {
      code: $scope.user.code,
      avatar: $scope.uploadedImageData, // Đã cập nhật avatar mới
      fullName: $scope.user.fullName,
      gender: $scope.user.gender,
      account: $scope.user.account,
      customerType: $scope.user.customerType,
      birthDate: $scope.user.birthDate,
      status: $scope.user.status,
      createdAt: $scope.user.createdAt,
      updatedAt: $scope.user.updatedAt,
      createdBy: $scope.user.createdBy,
      updatedBy: $scope.user.updatedBy,
    };
    console.log(dataObject);
    const updateCustomersData = await $scope.updateCustomer(dataObject);

    // $uibModalInstance.close(); // Đóng modal
  };

  $scope.updateAccount = function () {
    return new Promise((resolve, reject) => {
      const data = {
        account: $scope.user.account.account,
        password: $scope.user.account.password,
        email: $scope.user.account.email,
        phoneNumber: $scope.user.account.phoneNumber,
        status: $scope.user.account.status,
        confirmationCode: $scope.user.account.confirmationCode,
        role: $scope.user.account.role,
      };
      $http
        .put(
          `http://localhost:8080/api/ol/update-user-account/${$scope.user.account.id}`,
          data
        )
        .then(function (response) {
          $scope.showSuccessNotification("Cập nhật thông tin thành công!");
          resolve(response.data);
        })
        .catch(function (error) {
          console.error("Error:", error);
          $scope.showErrorNotification("Có lỗi xảy ra khi cập nhật tài khoản.");
          reject(error);
        });
    });
  };

  $scope.updateAccountAndUpload = async function () {
    try {
      // Gọi hàm uploadBtnUpdate
      await $scope.uploadBtnUpdate();
      // Gọi hàm updateAccount
      await $scope.updateAccount();
      // Thông báo thành công (nếu cần)
      $scope.showSuccessNotification("Cập nhật thông tin thành công!");
    } catch (error) {
      console.error("Error:", error);
      $scope.showErrorNotification(
        "Có lỗi xảy ra khi cập nhật tài khoản và ảnh."
      );
    }
  };

  $scope.updatePassword = function () {
    const data = {
      account: $scope.user.account.account,
      password: $scope.passwordData.newPassword,
      email: $scope.user.account.email,
      phoneNumber: $scope.user.account.phoneNumber,
      status: $scope.user.account.status,
      confirmationCode: $scope.user.account.confirmationCode,
      role: $scope.user.account.role,
    };
    $http
      .put(
        `http://localhost:8080/api/ol/update-user-account/${$scope.user.account.id}`,
        data
      )
      .then(function (response) {
        console.log(response.data);
        $scope.showSuccessNotification("Cập nhật tài khoản thành công!");
        resolve(response.data);
      });
  };
});
