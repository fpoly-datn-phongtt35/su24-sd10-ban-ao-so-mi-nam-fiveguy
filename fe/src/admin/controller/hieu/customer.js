app.controller("customerCtrl", function ($scope, $http, $timeout) {
  $scope.originalCustomer = [];
  $scope.customer = [];
  $scope.formUpdate = {};
  $scope.formInput = {};
  $scope.formInputAccount = {};
  $scope.showAlert = false;
  $scope.currentDate = new Date();
  $scope.showError = false;
  const emailAccount = null;
  $scope.uploadedImageData = null;

  $scope.load = function () {
    $scope.loading = true;
  };
  $scope.unload = function () {
    $scope.loading = false;
  };
  const apiCustomer = "http://localhost:8080/api/admin/customer";
  const apiAccount = "http://localhost:8080/api/admin/account";

  imgShow("image", "image-preview");
  imgShow("image-update", "image-preview-update");

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

  function imgShow(textInput, textPreview) {
    const imageInput = document.getElementById(textInput);
    const imagePreview = document.getElementById(textPreview);
    imageInput.addEventListener("change", function () {
      if (imageInput.files && imageInput.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
          imagePreview.src = e.target.result;
        };
        reader.readAsDataURL(imageInput.files[0]);
      }
    });
  }
  $scope.getAll = function () {
    $http.get(apiCustomer).then(function (resp) {
      $scope.customer = resp.data;
      //   $scope.customer = angular.copy($scope.originalCustomer);
    });
  };
  $scope.getAll();

  let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
  $scope.showErrorImg = function (message) {
    $scope.alertErrorImg = message;
    $scope.showError = true;
  };
  $scope.showSuccessMessage = function (message) {
    $scope.alertMessage = message;
    $scope.showAlert = true;
    $timeout(function () {
      $scope.closeAlert();
    }, 5000);
  };

  $scope.closeAlert = function () {
    $scope.showAlert = false;
  };

  $scope.timkiemStatus = function () {
    if ($scope.formtimkiem === "1") {
      $http.get(apiCustomer + "/timkiem-status/1").then(function (response) {
        $scope.listVoucher = response.data;
      });
    } else if ($scope.formtimkiem === "2") {
      $http.get(apiCustomer + "/timkiem-status/2").then(function (response) {
        $scope.listVoucher = response.data;
      });
    } else if ($scope.formtimkiem === "0") {
      $http.get(apiCustomer + "/timkiem-status/0").then(function (response) {
        $scope.listVoucher = response.data;
      });
    } else if ($scope.formtimkiem === "1") {
      $http.get(apiCustomer).then(function (response) {
        $scope.listVoucher = response.data;
      });
    }
  };
  $scope.timkiemStatus();

  $scope.search = function () {
    // Kiểm tra từ khóa tìm kiếm
    if ($scope.searchKeyword.trim() !== "") {
      $scope.customer = $scope.originalCustomer.filter(function (item) {
        if (item && item.fullName) {
          return item.fullName
            .toLowerCase()
            .includes($scope.searchKeyword.toLowerCase());
        }
        return false;
      });
    } else {
      // Nếu từ khóa tìm kiếm trống, hiển thị lại dữ liệu ban đầu từ originalCustomer
      $scope.customer = angular.copy($scope.originalCustomer);
    }
    // Sau khi lọc, cập nhật dữ liệu hiển thị cho trang đầu tiên
    $scope.changePageSize();
  };

  $scope.create = function () {
    let fileInput = document.getElementById("image");
    let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
    $scope.showErrorImg = function (message) {
      $scope.alertErrorImg = message;
      $scope.showError = true;
    };
    if (!allowedExtensions.exec(fileInput.value)) {
      $scope.showErrorImg(
        "Please upload file having extensions .jpeg/.jpg/.png/.gif only"
      );
      return;
    } else if (fileInput.files.length > 0) {
      let data = new FormData();
      data.append("file", fileInput.files[0]);
      $scope.load();
      $http
        .post("/rest/upload", data, {
          transformRequest: angular.identity,
          headers: { "Content-Type": undefined },
        })
        .then((resp) => {
          $scope.formInput.avatar = resp.data.name;
          let item = angular.copy($scope.formInput);
          item.createdAt = $scope.currentDate;
          $http
            .post(`/customer`, item)
            .then((resp) => {
              $scope.showSuccessMessage("Create Custoemr successfully!");
              $scope.initialize();
              $scope.resetFormInput();
              $("#modalAdd").modal("hide");
              $scope.showError = false;
              $scope.unload();
            })
            .catch((error) => {
              console.log("Error", error);
              $scope.unload();
              return;
            });
        })
        .catch((error) => {
          console.log("Error", error);
        });
    }
  };
  /// dùng tạm
  // $scope.themkhachhang = function () {
  //     console.log("Đây lèm thêm NV");
  //     let items = angular.copy($scope.formInput);
  //     $http
  //         .post(apiCustomer, items)
  //         .then(function (response) {
  //             console.log(response);
  //             $scope.getAll();
  //             $("#modalAdd").modal("hide");
  //         })
  //         .catch(function () {
  //             console.log("Error", error);
  //         });
  // };
  $scope.suakhachhang = function () {
    // console.log("Đây lèm thêm NV");
    let item = angular.copy($scope.formUpdate);

    $http
      .put(apiCustomer + `/${item.id}`, item)
      .then(function (response) {
        console.log(response);

        $scope.getAll();
        $("#modalUpdate").modal("hide");
      })
      .catch(function () {
        console.log("Error", error);
      });
  };
  $scope.createKA = function () {
    let fileInput = document.getElementById("image");
    if (fileInput.files.length > 0) {
      let data = new FormData();
      data.append("file", fileInput.files[0]);
      $scope.load();
      $http
        .post("http://localhost:8080/rest/upload", data, {
          transformRequest: angular.identity,
          headers: { "Content-Type": undefined },
        })
        .then((resp) => {
          $scope.formInput.avatar = resp.data.name;
          let data = angular.copy($scope.formInput);

          let dataCustomer = {
            fullName: $scope.formInput.fullName,
            avatar: $scope.formInput.avatar,
            birthDate: $scope.formInput.birthDate,
            gender: $scope.formInput.gender,
            status: 1,
          };

          let dataAccount = {
            account: $scope.formInput.account,
            email: $scope.formInput.email,
            phoneNumber: $scope.formInput.phoneNumber,
            status: 1,
            role: {
              fullName: CUSTOMER,
            },
          };

          let dataEA = {
            customerEntity: dataCustomer,
            accountEntity: dataAccount,
          };
          console.log(dataEA);

          $http
            .post(apiCustomer + "/createaKA", dataEA)
            .then((resp) => {
              $scope.showSuccessNotification("Thêm khách hàng thành công");
              $scope.initialize();
              $scope.resetFormInput();
              $("#modalAdd").modal("hide");
              $scope.showError = false;
              $scope.unload();
            })
            .catch((error) => {
              console.log("Error", error);
              $scope.unload();
              return;
            });
        });
    } else {
      $scope.showErrorImg = function (message) {
        $scope.alertErrorImg = message;
        $scope.showError = true;
      };
      $scope.showErrorImg("Please upload file");
    }
  };

  $scope.loadCustomerTypes = function () {
    const apiCustomerType = "http://localhost:8080/api/admin/customerType";
    $http
      .get(apiCustomerType)
      .then(function (response) {
        $scope.customerTypes = response.data;
      })
      .catch(function (error) {
        console.log("Error loading customer types", error);
      });
  };

  $scope.loadCustomerTypes();

  // $scope.update = function () {
  //     let item = angular.copy($scope.formUpdate);
  //     console.log(item)
  //     $http.put(`/customer/${item.id}`, item).then(function (resp) {
  //         $scope.showSuccessMessage("Update Customer successfully");
  //         $scope.resetFormUpdate();
  //         $scope.initialize();
  //         $('#modalUpdate').modal('hide');
  //     }).catch(function (error) {
  //         console.log("Error", error);
  //     });
  // }

  $scope.apiUpdate = function () {
    let item = angular.copy($scope.formUpdate);
    $http
      .put(apiCustomer + `/${item.id}`, item)
      .then((resp) => {
        $scope.showSuccessNotification("Cập nhật thông tin thành công");
        $scope.resetFormUpdate();
        $scope.initialize();
        $("#modalUpdate").modal("hide");
        $scope.showError = false;
        $scope.unload();
      })
      .catch((error) => {
        console.log("Error", error);
        return;
      });
  };
  $scope.update = function () {
    let fileInput = document.getElementById("image-update");
    if ($scope.formUpdate.avatar.length > 0 && !fileInput.files.length > 0) {
      $scope.apiUpdate();
    } else {
      let data = new FormData();
      data.append("file", fileInput.files[0]);
      $scope.load();
      $http
        .post("http://localhost:8080/rest/upload", data, {
          transformRequest: angular.identity,
          headers: { "Content-Type": undefined },
        })
        .then((resp) => {
          $scope.formUpdate.avatar = resp.data.name;
          $scope.apiUpdate();
          $scope.unload();
        })
        .catch((error) => {
          console.log("Error", error);
          $scope.unload();
        });
    }
  };

  $scope.edit = function (customer) {
    $scope.emailAccount = customer.account.email;
    // console.log($scope.emailAccount);
    const birthDateNew = angular.copy(customer.birthDate);
    if ($scope.formUpdate.updatedAt) {
      $scope.formUpdate = angular.copy(customer);
    } else {
      $scope.formUpdate = angular.copy(customer);

      $scope.formUpdate.updatedAt = new Date();
      $scope.formInputAccount = angular.copy(customer.account);
    }
    $scope.formUpdate.birthDate = new Date(birthDateNew);
    console.log($scope.formUpdate.birthDate);

    $scope.formUpdate.avatar = angular.copy(customer.avatar);
  };

  $scope.formatDate = function (inputDate) {
    // Tạo đối tượng Date từ chuỗi ngày gốc
    const date = new Date(inputDate);

    // Lấy ngày, tháng và năm
    const day = ("0" + date.getDate()).slice(-2); // Thêm số 0 nếu cần và lấy 2 chữ số cuối
    const month = ("0" + (date.getMonth() + 1)).slice(-2); // Thêm số 0 nếu cần và lấy 2 chữ số cuối (tháng bắt đầu từ 0)
    const year = date.getFullYear();

    // Định dạng lại thành chuỗi dd/mm/yyyy
    const formattedDate = `${day}/${month}/${year}`;

    return formattedDate;
  };

  $scope.delete = function (item) {
    $http
      .put(apiCustomer + "/status" + `/${item.id}`, item)
      .then(function (resp) {
        $scope.getCustomer(0);
        $scope.showSuccessNotification("Cập nhật thông tin thành công");
      })
      .catch(function (error) {
        console.log("Error", error);
      });
  };

  $scope.resetFormUpdate = function () {
    $scope.formUpdate = {};
    $scope.formInputAccount = {};
    let fileInput = document.getElementById("image-update");
    let imagePreviewUpdate = document.getElementById("image-preview-update");
    imagePreviewUpdate.src = "/src/admin/common/img/no-img.png";
    fileInput.value = "";
    fileInput.type = "file";
    $scope.formUpdateCustomer.$setPristine();
    $scope.formUpdateCustomer.$setUntouched();
  };

  $scope.resetFormInput = function () {
    $scope.formInput = {};
    $scope.formInputAccount = {};
    $scope.formInputAccount.email = null;
    $scope.formInputAccount.phoneNumber = null;
    let fileInput = document.getElementById("image");
    let imagePreview = document.getElementById("image-preview");
    imagePreview.src = "/src/admin/common/img/no-img.png";
    fileInput.value = "";
    fileInput.type = "file";
    $scope.formCreateCustomer.$setPristine();
    $scope.formCreateCustomer.$setUntouched();
  };

  $scope.xuatFile = function () {
    $http.get(apiCustomer + "/excel").then(function (response) {
      $scope.showSuccessNotification("Xuất file thành công");
      // $scope.pageEm = response.data.content;
      // $scope.totalPages = response.data.totalPages
    });
  };

  //Phân trang + lọc + tìm kiếm
  $scope.filterCustomer = function () {
    $http.get(apiCustomer).then(function (resp) {
      $scope.originalCustomer = resp.data;
    });
  };

  $scope.filterCustomer = [];
  $scope.totalPages = 0;
  $scope.currentPage = 0;
  $scope.desiredPage = 1;
  $scope.pageSize = 5;
  $scope.filters = {
    name: null,
    code: null,
    discountType: null,
    startDate: null,
    endDate: null,
    idCustomerType: null,
  };

  $scope.getCustomer = function (pageNumber) {
    let params = angular.extend(
      { pageNumber: pageNumber, size: $scope.pageSize },
      $scope.filters
    );
    $http
      .get("http://localhost:8080/api/admin/customer/page", {
        params: params,
      })
      .then(function (response) {
        $scope.filterCustomer = response.data.content;
        $scope.totalPages = response.data.totalPages;
        $scope.currentPage = pageNumber;
        $scope.desiredPage = pageNumber + 1;
      });
  };

  $scope.applyFilters = function () {
    $scope.getCustomer(0);
  };

  $scope.goToPage = function () {
    let pageNumber = $scope.desiredPage - 1;
    if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
      $scope.getCustomer(pageNumber);
    } else {
      // Reset desiredPage to currentPage if the input is invalid
      $scope.desiredPage = $scope.currentPage + 1;
    }
  };
  // Initial load
  $scope.getCustomer(0);

  $scope.resetFilters = function () {
    $scope.filters = {
      fullName: null,
      code: null,
      phoneNumber: null,
      birthDate: null,
      gender: null,
      status: null,
      idCustomerType: null,
    };
    $scope.getCustomer(0);
  };
  $scope.isEmailValid = false;
  $scope.isAccountValid = false;
  $scope.isPhoneNumberValid = false;

  //Hàm check email ko được bắt đầu bằng số, check trùng
  $scope.checkEmail = function () {
    var email = $scope.formInputAccount.email;

    if (!email) {
      $scope.emailError = "";
      $scope.isEmailValid = true; // Email rỗng coi như hợp lệ
      return true;
    }

    var EMAIL_REGEXP = /^[^\d].*/;
    if (!EMAIL_REGEXP.test(email)) {
      $scope.emailError = "Email không được bắt đầu bằng số";
      $scope.isEmailValid = false;
      return false;
    }

    $http.get(apiAccount + "/check-email", { params: { email: email } })
      .then(function (response) {
        if (response.data) {
          $scope.emailError = "Email đã tồn tại";
          $scope.isEmailValid = false;
        } else {
          $scope.emailError = "";
          $scope.isEmailValid = true;
        }
      })
      .catch(function (error) {
        $scope.emailError = "Có lỗi xảy ra khi kiểm tra email";
        $scope.isEmailValid = false;
      });
  };

  $scope.checkAccount = function () {
    var account = $scope.formInputAccount.account;

    $http.get(apiAccount + "/check-account", { params: { account: account } })
      .then(function (response) {
        if (response.data) {
          $scope.accountError = "Tài khoản đã tồn tại";
          $scope.isAccountValid = false;
        } else {
          $scope.accountError = "";
          $scope.isAccountValid = true;
        }
      })
      .catch(function (error) {
        $scope.accountError = "Có lỗi xảy ra khi kiểm tra tài khoản";
        $scope.isAccountValid = false;
      });
  };

  $scope.checkPhoneNumber = function () {
    const phoneNumber = $scope.formInputAccount.phoneNumber;

    const isValidPhoneNumber = /^(\+84|0)[2|3|5|7|8|9][0-9]{8}$/.test(phoneNumber);

    if (!isValidPhoneNumber) {
      $scope.phoneNumberError = "Số điện thoại không hợp lệ";
      $scope.isPhoneNumberValid = false;
      return;
    }

    $http.get(apiAccount + "/check-phone-number", { params: { phoneNumber: phoneNumber } })
      .then(function (response) {
        if (response.data) {
          $scope.phoneNumberError = "Số điện thoại đã tồn tại";
          $scope.isPhoneNumberValid = false;
        } else {
          $scope.phoneNumberError = "";
          $scope.isPhoneNumberValid = true;
        }
      })
      .catch(function (error) {
        $scope.phoneNumberError = "Có lỗi xảy ra khi kiểm tra số điện thoại";
        $scope.isPhoneNumberValid = false;
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
  ///Hàm thêm customer
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
  //upload ảnh
  $scope.uploadBtnAdd = function () {
    return new Promise((resolve, reject) => {
      const fileInput = document.getElementById("image");
      const file = fileInput.files[0];

      if (!file) {
        // Nếu không có file, resolve promise ngay lập tức
        $scope.uploadedImageData = null; // Đặt dữ liệu ảnh là null
        return resolve(); // Resolve promise ngay lập tức
      }

      $scope.showError = false;

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = function () {
        const data = reader.result.split(",")[1];
        const postData = {
          name: file.name,
          type: file.type,
          data: data,
        };
        $scope.postFile(postData)
          .then(function () {
            $scope.$apply(); // Áp dụng thay đổi vào scope
            resolve(); // Resolve promise khi upload hoàn tất
          })
          .catch(reject);
      };
      reader.onerror = function (error) {
        reject(error);
      };
    });
  };

  $scope.addAccount = async (objectAccount) => {
    console.log(objectAccount);
    try {
      const result = await $http.post(
        `${apiAccount}/saveAccountCustomer`,
        objectAccount
      );
      console.log("Thêm mới account thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi thêm mới tài khoản account", error);
      throw error;
    }
  };

  //Hàm xử lý thêm mới account. Check điều kiện kiểm tra và gọi api thêm mới account
  $scope.themAccount = async function () {
    const email = $scope.formInputAccount.email;
    if (email) {
      const dataAccount = {
        account: $scope.formInputAccount.account,
        // password: password,
        email: email,
        phoneNumber: $scope.formInputAccount.phoneNumber,
      };
      const resAccount = await $scope.getByEmailAccount(email);
      if (resAccount) {
        console.log("Đã có email");
        return false;
      } else {
        const responseData = await $scope.addAccount(dataAccount);
        if (responseData) {
          const getByEmail = await $scope.getByEmailAccount(responseData.email);
          return getByEmail;
        }
      }
    } else {
      console.log("sai định dạng email");
      return false;
    }
  };

  $scope.addCustom = async (objectData) => {
    try {
      const result = await $http.post(`${apiCustomer}/save`, objectData);
      console.log("Thêm mới khách hàng thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi thêm mới tài khoản khách hàng", error);
      throw error;
    }
  };

  // Form submit thêm
  $scope.uploading = false; // Thêm biến trạng thái

  $scope.showLoading = function () {
    $scope.uploading = true; // Hiển thị spinner
  };

  $scope.hideLoading = function () {
    $scope.uploading = false; // Ẩn spinner
  };

  $scope.submitForm = async function () {
    if ($scope.formCreateCustomer.$valid) {
      try {
        // Gọi các hàm kiểm tra trước khi xử lý form
        $scope.checkEmail();
        $scope.checkAccount();
        $scope.checkPhoneNumber();

        // Đợi các kiểm tra hoàn thành
        await $timeout(() => { }, 500); // Đợi một thời gian ngắn để đảm bảo các promise trong các hàm kiểm tra hoàn tất

        // Nếu tất cả các kiểm tra đều hợp lệ
        if ($scope.isEmailValid && $scope.isAccountValid && $scope.isPhoneNumberValid) {
          $scope.showLoading(); // Hiển thị spinner khi bắt đầu xử lý

          // Đợi cho hàm uploadBtnAdd hoàn tất
          await $scope.uploadBtnAdd();

          // Tiếp tục thực hiện submitForm sau khi upload hoàn tất
          const addAccountData = await $scope.themAccount();
          if (addAccountData) {
            const dataObject = {
              account: { id: addAccountData.id },
              fullName: $scope.formInput.fullName,
              gender: $scope.formInput.gender,
              birthDate: $scope.formInput.birthDate,
              address: $scope.formInput.address,
              avatar: $scope.uploadedImageData, // Thêm dữ liệu hình ảnh ở đây (hoặc null nếu không có ảnh)
            };
            const addEmployeesData = await $scope.addCustom(dataObject);
            console.log("addEmployeesData = ", addEmployeesData);
            $scope.showSuccessNotification("Thêm thông tin thành công");

            // Sử dụng $timeout để cập nhật scope
            $scope.getCustomer(0);
            $scope.resetFormInput();
            $("#modalAdd").modal("hide");
          }
        } else {
          $scope.showErrorNotification("Vui lòng kiểm tra lại thông tin đã nhập.");
        }
      } catch (error) {
        console.error("Error:", error);
        $scope.showErrorNotification("Không thành công");
      } finally {
        $scope.hideLoading(); // Ẩn spinner khi hoàn tất xử lý, bất kể thành công hay thất bại
      }
    } else {
      // Hiển thị lỗi
      $scope.showErrorNotification("Không thành công");
      $scope.formCreateCustomer.$submitted = true;
    }
  };

  //Sửa khách hàng
  $scope.uploadBtnUpdate = async function () {
    const fileInputupdate = document.getElementById("image-update");
    const file = fileInputupdate.files[0];

    if (!file) {
      $scope.showError = true;
      //submit form khi ảnh ko được thay đổi
      if ($scope.formUpdateCustomer.$valid) {
        const addAccountData = await $scope.suaAccount();
        console.log(addAccountData);
        if (addAccountData) {
          const dataObject = {
            code: $scope.formUpdate.code,
            avata: $scope.formUpdate.avata,
            account: {
              id: addAccountData.id,
            },

            avatar: $scope.formUpdate.avatar, // Add the image data here
            fullName: $scope.formUpdate.fullName,
            gender: $scope.formUpdate.gender,
            birthDate: $scope.formUpdate.birthDate,
            address: $scope.formUpdate.address,
            createdAt: $scope.formUpdate.createdAt,
            updatedAt: $scope.formUpdate.updatedAt,
            createdBy: $scope.formUpdate.createdBy,
            updatedBy: $scope.formUpdate.updatedBy,
            customerType: $scope.formUpdate.customerType,
            status: $scope.formUpdate.status,
          };
          console.log(dataObject);
          const updateCustomersData = await $scope.updateCustomer(dataObject);
          $scope.showSuccessNotification("Sửa thông tin thành công");
          $scope.getCustomer(0);
          $scope.resetFormUpdate();
          console.log("updateCustomersData = ", updateCustomersData);
          $("#modalUpdate").modal("hide"); // Đóng modal bằng JavaScript thuần
        }
        // $uibModalInstance.close(); // Đóng modal
      }
      $scope.$apply();
      return;
    } else {
      $scope.showError = false;
      $scope.uploading = true; // Đang trong quá trình upload

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = function () {
        const data = reader.result.split(",")[1];
        const postData = {
          name: file.name,
          type: file.type,
          data: data,
        };
        $scope.postFile(postData).then(function () {
          $scope.uploading = false; // Hoàn thành quá trình upload
          $scope.$apply(); // Áp dụng thay đổi vào scope
          if (!$scope.uploading) {
            $scope.submitFormUpdate();
          }
        });
      };
    }
  };

  $scope.updateAccount = async (objectAccount) => {
    // let email = $scope.edit(customer.account.email);

    console.log($scope.emailAccount);
    try {
      const result = await $http.put(
        `${apiAccount}/email/${$scope.emailAccount}`,
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
        role: $scope.formInputAccount.role,
        // password: password
        // status: status
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

  // Thêm mới nhân viên
  $scope.updateCustomer = async (objectData) => {
    let item = angular.copy($scope.formUpdate);
    console.log(item);
    try {
      const result = await $http.put(`${apiCustomer}/${item.id}`, objectData);
      console.log("Sửa account thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi sửae tài khoản account", error);
      throw error;
    }
  };

  $scope.isLoading = false;

  // Form submit update
  $scope.submitFormUpdate = async function () {
    if ($scope.formUpdateCustomer.$valid) {
      $scope.isLoading = true;  // Set loading to true
      try {
        
        const addAccountData = await $scope.suaAccount();
        console.log(addAccountData);
        if (addAccountData) {
          const dataObject = {
            code: $scope.formUpdate.code,
            avata: $scope.formUpdate.avata,
            account: {
              id: addAccountData.id,
            },
            avatar: $scope.uploadedImageData, // Add the image data here
            fullName: $scope.formUpdate.fullName,
            gender: $scope.formUpdate.gender,
            birthDate: $scope.formUpdate.birthDate,
            address: $scope.formUpdate.address,
            createdAt: $scope.formUpdate.createdAt,
            updatedAt: $scope.formUpdate.updatedAt,
            createdBy: $scope.formUpdate.createdBy,
            updatedBy: $scope.formUpdate.updatedBy,
            status: $scope.formUpdate.status,
          };
          console.log(dataObject);
          const updateCustomersData = await $scope.updateCustomer(dataObject);
          $scope.getCustomer(0);
          $scope.resetFormUpdate();
          $scope.showSuccessNotification("Sửa thông tin thành công");
          console.log("updateCustomersData = ", updateCustomersData);
          $("#modalUpdate").modal("hide"); // Close modal
        }
      } catch (error) {
        console.error("Error updating customer:", error);
      } finally {
        $scope.isLoading = false;  // Set loading to false after completion
      }
    }
  };

  // END Sửa khách hàng
});
