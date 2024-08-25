app.controller("tinh-detail-employee-controller", function ($scope, $http, $timeout) {
  $scope.getDetailEmployee = {};
  $scope.formUpdate = {};
  $scope.formUpdateEm = {};
  $scope.formUpdateAccount = {};
  $scope.updatePassword = {};
  $scope.currentPassword = {};
  formInputAccountPassword = {};
  const emailAccount = null;

  $scope.showPassword = false;

  $scope.togglePasswordVisibility = function () {
    $scope.showPassword = !$scope.showPassword;
  };
  $scope.showConfirmPassword = false;

  $scope.toggleConfirmPasswordVisibility = function() {
      $scope.showConfirmPassword = !$scope.showConfirmPassword;
  };
  
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

  $scope.uploading = false; // Thêm biến trạng thái

  $scope.showLoading = function () {
    $scope.uploading = true; // Hiển thị spinner
  };

  $scope.hideLoading = function () {
    $scope.uploading = false; // Ẩn spinner
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
    console.log("hihi");
    
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
    const phoneNumber = $scope.formUpdateAccount.phoneNumber;
    console.log("Checking phone number:", phoneNumber); // Debugging log

    // Check if phone number is valid according to the pattern
    const isValidPhoneNumber = /^(\+84|0)[2|3|5|7|8|9][0-9]{8}$/.test(phoneNumber);

    if (!isValidPhoneNumber) {
      $scope.phoneNumberError = "Số điện thoại không hợp lệ";
      return; // Exit if the phone number is invalid
    }

    // If phone number is valid, check if it exists in the database
    $http
      .get(apiAccount + "/check-phone-number", {
        params: { phoneNumber: phoneNumber }
      })
      .then(function (response) {
        console.log("API Response:", response.data); // Debugging log
        if (response.data) {
          $scope.phoneNumberError = "Số điện thoại đã tồn tại";
        } else {
          $scope.phoneNumberError = "";
          return true;
        }
      })
      .catch(function (error) {
        console.error('API Error:', error); // Handle errors from API
        $scope.phoneNumberError = "Có lỗi xảy ra khi kiểm tra số điện thoại";
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
    $scope.loading = true; // Bật trạng thái loading khi bắt đầu

    const fileInputupdate = document.getElementById("image-update");
    const file = fileInputupdate.files[0];

    try {
      if (!file) {
        $scope.showError = true;

        // Nếu không có ảnh được chọn, tiếp tục quy trình cập nhật mà không có ảnh
        const addAccountData = await $scope.suaAccount();
        if (addAccountData) {
          const dataObject = {
            code: $scope.formUpdate.code,
            account: {
              id: addAccountData.id,
            },
            avatar: $scope.formUpdateEm.avatar, // Sử dụng ảnh hiện tại
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

          await $scope.postFile(postData); // Đảm bảo quá trình upload ảnh hoàn tất và cập nhật $scope.uploadedImageData

          // Kiểm tra xem dữ liệu ảnh đã có chưa
          if ($scope.uploadedImageData) {
            await $scope.submitFormUpdate(); // Tiếp tục quá trình submit form
          } else {
            $scope.showErrorNotification("Lỗi khi tải ảnh lên.");
          }

          // Tắt trạng thái loading khi hoàn tất
          $scope.loading = false;
          $scope.$apply(); // Áp dụng thay đổi vào scope
        };
      }
    } catch (error) {
      console.error("Error during upload or update:", error);
      $scope.showErrorNotification("Có lỗi xảy ra.");
    } finally {
      $scope.loading = false; // Tắt trạng thái loading khi hoàn tất
      $scope.$apply(); // Đảm bảo cập nhật giao diện
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
  $scope.passwordMatched = true; // Biến để kiểm tra mật khẩu hiện tại
  $scope.suaAccountPassword = function () {
    $scope.formUpdatePassword.$submitted = true; // Đánh dấu form đã được submit

    // Kiểm tra tính hợp lệ của form trước khi tiếp tục
    if ($scope.formUpdatePassword.$valid && $scope.passwordMatched && ($scope.updatePassword.password === $scope.formUpdate.confirmPassword)) {
      const email = $scope.getDetailEmployee.account.email;
      if (email) {
        const dataAccount = {
          account: $scope.getDetailEmployee.account.account,
          email: email,
          phoneNumber: $scope.getDetailEmployee.account.phoneNumber,
          role: $scope.getDetailEmployee.account.role,
          password: $scope.updatePassword.password, // Mật khẩu mới
          status: $scope.getDetailEmployee.account.status,
        };

        try {
          const responseData = $scope.updateAccount(dataAccount);
          if (responseData) {
            const getByEmail = $scope.getByEmailAccount(responseData.email);
            $scope.showSuccessNotification("Cập nhật mật khẩu thành công");
            $scope.resetFormUpdatePassword();
            return getByEmail;
          }
        } catch (error) {
          console.error("Error updating account:", error);
          $scope.showErrorNotification("Có lỗi xảy ra khi cập nhật mật khẩu.");
          return false;
        }
      }
    } else {
      // Hiển thị lỗi nếu mật khẩu hiện tại không đúng hoặc mật khẩu mới không khớp
      if (!$scope.passwordMatched) {
        $scope.showErrorNotification("Mật khẩu hiện tại không đúng.");
      } else if ($scope.updatePassword.password !== $scope.formUpdate.confirmPassword) {
        $scope.showErrorNotification("Mật khẩu mới không khớp với xác nhận mật khẩu.");
      }
    }
  };
  $scope.resetFormUpdatePassword = function () {
    $scope.formUpdatePassword.$setPristine();
    $scope.formUpdatePassword.$setValidity();
    $scope.passwordMatched = true;
    $scope.formUpdate.confirmPassword = {}
    $scope.updatePassword = {};
    $scope.currentPassword = {};
  }

  // kiểm tra mật khẩu xác nhận
  app.directive('passwordMatch', function () {
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
  });


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
    if ($scope.formUpdateEmployee.$valid && !$scope.emailError && !$scope.phoneNumberError) {
      const addAccountData = await $scope.suaAccount();
      console.log(addAccountData);
      if (addAccountData) {
        const dataObject = {
          code: $scope.getDetailEmployee.code,
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



});
