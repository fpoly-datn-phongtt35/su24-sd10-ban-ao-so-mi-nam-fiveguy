app.controller("tinh-detail-employee-controller", function ($scope, $http) {
  $scope.getDetailEmployee = {};
  $scope.formUpdateEm = {};
  $scope.formUpdateAccount = {};
  $scope.updatePassword = {};
  formInputAccountPassword = {};
  const emailAccount = null;
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

  const apiEmployee = "http://localhost:8080/api/admin/employee";
  const apiAccount = "http://localhost:8080/api/admin/account";

  imgShow("image-update", "image-preview-update");
  // imgShow('image', 'image-preview');

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
          };
          reader.readAsDataURL(imageInput.files[0]);
        }
      });
    } else {
      console.error("Element not found");
    }
  }

  $scope.showPasswordChange = false;

  $scope.togglePasswordChange = function () {
    $scope.showPasswordChange = !$scope.showPasswordChange;
  };

  $scope.checkEmail = function () {
    var email = $scope.formInputAccount.email;

    // Kiểm tra nếu trường input trống, coi như hợp lệ
    if (!email) {
      $scope.emailError = "";
      return true;
    }

    // Kiểm tra nếu email không bắt đầu bằng số
    var EMAIL_REGEXP = /^[^\d].*/;
    if (!EMAIL_REGEXP.test(email)) {
      $scope.emailError = "Email không được bắt đầu bằng số";
      return false;
    }

    // Email hợp lệ, tiếp tục kiểm tra trùng email
    $http
      .get(apiAccount + "/check-email", { params: { email: email } })
      .then(function (response) {
        if (response.data) {
          // Email bị trùng
          $scope.emailError = "Email đã tồn tại";
          return false;
        } else {
          // Email hợp lệ và không bị trùng
          $scope.emailError = "";
          return true;
        }
      })
      .catch(function (error) {
        // Xử lý lỗi khi gọi API
        $scope.emailError = "Có lỗi xảy ra khi kiểm tra email";
        return false;
      });
  };

  //Hàm check trùng tên tài khoản
  $scope.checkAccount = function () {
    var account = $scope.formInputAccount.account;
    $http
      .get(apiAccount + "/check-account", { params: { account: account } })
      .then(function (response) {
        if (response.data) {
          // Email bị trùng
          $scope.accountError = "Tài khoản đã tồn tại";
          return false;
        } else {
          // Email hợp lệ và không bị trùng
          $scope.accountError = "";
          return true;
        }
      })
      .catch(function (error) {
        // Xử lý lỗi khi gọi API
        $scope.accountError = "Có lỗi xảy ra khi kiểm tra Tài khoản";
        return false;
      });
  };

  //Hàm check trùng số điện thoại
  $scope.checkPhoneNumber = function () {
    var phoneNumber = $scope.formInputAccount.phoneNumber;
    $http
      .get(apiAccount + "/check-phone-number", {
        params: { phoneNumber: phoneNumber },
      })
      .then(function (response) {
        if (response.data) {
          // Email bị trùng
          $scope.phoneNumberError = "Số điện thoại đã tồn tại";
          return false;
        } else {
          // Email hợp lệ và không bị trùng
          $scope.phoneNumberError = "";
          return true;
        }
      })
      .catch(function (error) {
        // Xử lý lỗi khi gọi API
        $scope.phoneNumberError = "Có lỗi xảy ra khi kiểm tra số điện thoại";
        return false;
      });
  };

  // Hàm tìm account theo email
  $scope.getByEmailAccount = async function (email) {
    try {
      const res = await $http.get(`${apiAccount}/${email}`);
      return res.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

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

  // // //Sửa Nhân Viên
  $scope.uploadBtnUpdate = async function () {
    console.log($scope.formUpdateEm.avatar);
    
    const fileInputupdate = document.getElementById("image-update");
    const file = fileInputupdate.files[0];

    if (!file) {
      $scope.showError = true;
      // Submit form when no image is selected
      const addAccountData = await $scope.suaAccount();
      if (addAccountData) {
        const dataObject = {
          code: $scope.formUpdate.code,
          account: {
            id: addAccountData.id,
          },
          avatar: $scope.formUpdateEm.avatar, // Add the image data here
          fullName: $scope.getDetailEmployee.fullName,
          gender: $scope.getDetailEmployee.gender,
          birthDate: $scope.getDetailEmployee.birthDate,
          address: $scope.getDetailEmployee.address,
          createdAt: $scope.getDetailEmployee.createdAt,
          updatedAt: $scope.getDetailEmployee.updatedAt,
          createdBy: $scope.getDetailEmployee.createdBy,
          updatedBy: $scope.getDetailEmployee.updatedBy,
          status: $scope.getDetailEmployee.status,
        };
        const updateEmployeesData = await $scope.updateEmployee(dataObject);
        $scope.showSuccessNotification("Sửa thông tin thành công");
        $scope.getEmployee(0);
        $scope.resetFormUpdate();
        $("#modalUpdate").modal("hide");
      }
      $scope.$apply();
      return;
    } else {
      $scope.showError = false;

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = async function () {
        const data = reader.result.split(",")[1];
        const postData = {
          name: file.name,
          type: file.type,
          data: data,
        };

        await $scope.postFile(postData); // Ensure this completes and updates $scope.uploadedImageData

        // Check if image data is available
        if ($scope.uploadedImageData) {
          await $scope.submitFormUpdate(); // Proceed with form submission
        } else {
          $scope.showErrorNotification("Lỗi khi tải ảnh lên.");
        }
      };
    }
  };

  // Hàm xử lý sự kiện thay đổi tệp ảnh
$scope.handleFileChange = function () {
  const fileInputupdate = document.getElementById("image-update");
  const file = fileInputupdate.files[0];

  if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
          // Đọc dữ liệu ảnh dưới dạng URL
          const imageDataUrl = e.target.result;
          // Cập nhật $scope.formUpdateEm.avatar với dữ liệu URL ảnh
          $scope.$apply(function () {
              $scope.formUpdateEm.avatar = imageDataUrl;
          });
      };
      reader.readAsDataURL(file);
  }
};

// Thêm sự kiện lắng nghe sự thay đổi tệp ảnh
document.getElementById("image-update").addEventListener("change", $scope.handleFileChange);


  $scope.updateAccount = async (objectAccount) => {
    // let email = $scope.edit(employee.account.email);

    console.log($scope.emailAccount);
    try {
      const result = await $http.put(
        `${apiAccount}/email-detail-employee/${$scope.emailAccount}`,
        objectAccount
      );
      console.log("Sửa account thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi Sửa tài khoản account", error);
      throw error;
    }
  };

  //Hàm xử lý sửa account. Check điều kiện kiểm tra và gọi api thêm mới account
  $scope.suaAccount = async function () {
    const email = $scope.formInputAccount.email;
    if (email) {
      const dataAccount = {
        account: $scope.formInputAccount.account,
        email: email,
        phoneNumber: $scope.formInputAccount.phoneNumber,
        role: $scope.getDetailEmployee.account.role,
        password: $scope.getDetailEmployee.account.password,
        status: $scope.getDetailEmployee.account.status,
      };

      try {
        const responseData = await $scope.updateAccount(dataAccount);
        if (responseData) {
          const getByEmail = await $scope.getByEmailAccount(responseData.email);
          return getByEmail;
        }
      } catch (error) {
        console.error("Error updating account:", error);
        return false;
      }
    } else {
      console.log("sai định dạng email");
      return false;
    }
  };

  //đổi password
  $scope.suaAccountPassword = function () {
    const email = $scope.getDetailEmployee.account.email;
    if (email) {
      const dataAccount = {
        account: $scope.getDetailEmployee.account.account,
        email: email,
        phoneNumber: $scope.getDetailEmployee.account.phoneNumber,
        role: $scope.getDetailEmployee.account.role,
        password: $scope.updatePassword.password,
        status: $scope.getDetailEmployee.account.status,
      };

      try {
        const responseData = $scope.updateAccount(dataAccount);
        if (responseData) {
          const getByEmail = $scope.getByEmailAccount(responseData.email);
          return getByEmail;
        }
      } catch (error) {
        console.error("Error updating account:", error);
        return false;
      }
    }
  };

  // // Thêm mới nhân viên
  $scope.updateEmployee = async (objectData) => {
    let item = angular.copy($scope.formUpdateEm);
    try {
      const result = await $http.put(`${apiEmployee}/${item.id}`, objectData);
      console.log("Sửa account thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi sửae tài khoản account", error);
      throw error;
    }
  };

  // // Form submit update
  
$scope.submitFormUpdate = async function () {
    // Đánh dấu form đã được submit để ng-show hoạt động
    $scope.formUpdateEmployee.$submitted = true;

    // Kiểm tra tính hợp lệ của form
    if ($scope.formUpdateEmployee.$valid) {
      const addAccountData = await $scope.suaAccount();
      console.log(addAccountData);
      if (addAccountData) {
        const dataObject = {
          code: $scope.getDetailEmployee.code,
          // avatar: $scope.getDetailEmployee.avatar,
          account: {
            id: addAccountData.id,
          },
          avatar: $scope.formUpdateEm.avatar,
          fullName: $scope.formUpdateEm.fullName,
          gender: $scope.formUpdateEm.gender,
          birthDate: $scope.formUpdateEm.birthDate,
          address: $scope.formUpdateEm.address,
          createdAt: $scope.getDetailEmployee.createdAt,
          updatedAt: $scope.getDetailEmployee.updatedAt,
          createdBy: $scope.getDetailEmployee.createdBy,
          updatedBy: $scope.getDetailEmployee.updatedBy,
          status: $scope.getDetailEmployee.status,
        };
        console.log(dataObject);
        const updateEmployeesData = await $scope.updateEmployee(dataObject);
        $scope.showSuccessNotification("Sửa thông tin thành công");
        console.log("updateEmployeesData = ", updateEmployeesData);
        $("#modalUpdate").modal("hide"); // Đóng modal bằng JavaScript thuần
      }
    } else {
      // Hiển thị lỗi
      $scope.showErrorNotification("Không thành công");
      console.log($scope.formUpdateEmployee.$error);
    }
  };


  $scope.detailEmployee = function () {
    $http.get(apiEmployee + "/detail-employee").then(function (response) {
      $scope.getDetailEmployee = response.data;
      $scope.formUpdateEm = angular.copy(response.data); // Sử dụng angular.copy để tránh tham chiếu trực tiếp
      $scope.formInputAccount = angular.copy(response.data.account);
      $scope.emailAccount = response.data.account.email;

      // Định dạng ngày tháng
      $scope.formUpdateEm.birthDate = new Date(response.data.birthDate);

      // Đặt mật khẩu từ cơ sở dữ liệu
      $scope.dbPassword = response.data.account.password;
    });
  };

  $scope.detailEmployee();

  $scope.passwordMatched = true;
  $scope.currentPassword = {};

  $scope.checkPassword = function () {
    // So sánh mật khẩu người dùng nhập vào với mật khẩu từ cơ sở dữ liệu
    $scope.passwordMatched = $scope.formInputAccount.password === $scope.dbPassword;
  };


  app.directive('passwordMatch', [function () {
    return {
      require: 'ngModel',
      scope: {
        otherModelValue: '=passwordMatch'
      },
      link: function (scope, element, attributes, ngModel) {
        ngModel.$validators.match = function (modelValue) {
          return modelValue === scope.otherModelValue;
        };

        scope.$watch('otherModelValue', function () {
          ngModel.$validate();
        });
      }
    };
  }]);

});
