app.controller("tinh-employee-controller", function ($scope, $http, $timeout) {
  $scope.originalEmployee = [];
  $scope.employee = [];
  $scope.formUpdate = {};
  $scope.formInput = {};
  $scope.formInputAccount = {};
  $scope.formUpdateAccount = {};
  // $scope.formUpdateEmployee = {};
  formDetailAuditLog = {};
  $scope.formDelete = {};
  $scope.showAlert = false;
  $scope.currentDate = new Date();
  $scope.showAlert = false;
  $scope.showError = false;
  const emailAccount = null;
  $scope.formtimkiem = "1";
  $scope.load = function () {
    $scope.loading = true;
  };
  $scope.unload = function () {
    $scope.loading = false;
  };

  $scope.uploadedImageData = null;

  const elInput = document.getElementById("file");
  const img = document.getElementById("img");
  // const uploadBtn = document.getElementById("upload");

  const apiEmployee = "http://localhost:8080/api/admin/employee";
  const apiAccount = "http://localhost:8080/api/admin/account";
  const apiAuditLog = "http://localhost:8080/api/admin/audit-log";

  imgShow("image-update", "image-preview-update");

  // notify
  toastr.options = {
    closeButton: false,
    debug: false,
    newestOnTop: true,
    progressBar: false,
    positionClass: "toast-top-right",
    preventDuplicates: false,
    showDuration: "300",
    hideDuration: "1000",
    timeOut: "5000",
    extendedTimeOut: "1000",
    showEasing: "swing",
    hideEasing: "linear",
    showMethod: "fadeIn",
    hideMethod: "fadeOut",
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

  function imgShow(textInput, textPreview) {
    const imageInput = document.getElementById(textInput);
    const imagePreview = document.getElementById(textPreview);
    imageInput.addEventListener("change", function () {
      if (imageInput.files && imageInput.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
          imagePreview.src = e.target.result;
          // Không cần gọi $apply ở đây, chỉ cập nhật src của img
        };
        reader.readAsDataURL(imageInput.files[0]);
      }
    });
  }

  // Gọi imgShow để gắn sự kiện
  imgShow("image", "image-preview");

  let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
  $scope.showErrorImg = function (message) {
    $scope.alertErrorImg = message;
    $scope.showError = true;
  };

  //Hàm check email ko được bắt đầu bằng số, check trùng
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
    const phoneNumber = $scope.formInputAccount.phoneNumber;
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


  //search theo mã
  $scope.search = function () {
    // Kiểm tra từ khóa tìm kiếm
    if ($scope.searchKeyword.trim() !== "") {
      $scope.employee = $scope.originalEmployee.filter(function (item) {
        if (item && item.code) {
          return item.code
            .toLowerCase()
            .includes($scope.searchKeyword.toLowerCase());
        }
        return false;
      });
    } else {
      // Nếu từ khóa tìm kiếm trống, hiển thị lại dữ liệu ban đầu từ originalEmployee
      $scope.employee = angular.copy($scope.originalEmployee);
    }
    // Sau khi lọc, cập nhật dữ liệu hiển thị cho trang đầu tiên
    $scope.changePageSize();
  };

  $scope.getAll = function () {
    $http.get(apiEmployee).then(function (resp) {
      $scope.employee = resp.data;
      //   $scope.employee = angular.copy($scope.originalEmployee);
    });
  };
  $scope.getAll();

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
  // End upload ảnh

  //Thêm mới Nhân Viên
  // api thêm mới account
  $scope.addAccount = async (objectAccount) => {
    console.log(objectAccount);
    try {
      const result = await $http.post(
        `${apiAccount}/saveAccountEmployee`,
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


  $scope.addEmployee = async (objectData) => {
    try {
      const result = await $http.post(`${apiEmployee}/save`, objectData);
      console.log("Thêm mới nhân viên thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi thêm mới tài khoản nhân viên", error);
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
    if ($scope.formCreateEmployee.$valid) {
      try {
        $scope.showLoading(); // Hiển thị spinner khi bắt đầu xử lý

        // Đợi cho hàm uploadBtnAdd hoàn tất
        await $scope.uploadBtnAdd();

        // Tiếp tục thực hiện submitForm sau khi upload hoàn tất
        const addAccountData = await $scope.themAccount();
        if (addAccountData) {
          const dataObject = {
            account: {
              id: addAccountData.id,
            },
            fullName: $scope.formInput.fullName,
            gender: $scope.formInput.gender,
            birthDate: $scope.formInput.birthDate,
            address: $scope.formInput.address,
            avatar: $scope.uploadedImageData, // Thêm dữ liệu hình ảnh ở đây (hoặc null nếu không có ảnh)
          };
          const addEmployeesData = await $scope.addEmployee(dataObject);
          console.log("addEmployeesData = ", addEmployeesData);
          $scope.showSuccessNotification("Thêm thông tin thành công");

          // Sử dụng $timeout để cập nhật scope
          $scope.getEmployee(0);
          $scope.resetFormInput();
          $("#modalAdd").modal("hide");
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
      $scope.formCreateEmployee.$submitted = true;
    }
  };


  // $scope.uploadBtnAdd().then(() => {
  //   $scope.submitForm();
  // }).catch(error => {
  //   console.error("Upload error:", error);
  // });


  // END thêm Nhân Viên

  //Add employee Bằng file excel
  // $scope.uploadExcelFile = function (event) {
  //   const file = event.target.files[0];
  //   const reader = new FileReader();

  //   reader.onload = async function (e) {
  //     const data = new Uint8Array(e.target.result);
  //     const workbook = XLSX.read(data, { type: "array" });
  //     const sheetName = workbook.SheetNames[0];
  //     const worksheet = workbook.Sheets[sheetName];
  //     const jsonData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

  //     // Assuming the first row contains headers
  //     const headers = jsonData[0];
  //     const rows = jsonData.slice(1);

  //     for (const row of rows) {
  //       const accountData = {
  //         account: row[headers.indexOf("account")],
  //         email: row[headers.indexOf("email")],
  //         phoneNumber: row[headers.indexOf("phoneNumber")],
  //       };

  //       const employeeData = {
  //         fullName: row[headers.indexOf("fullName")],
  //         gender: row[headers.indexOf("gender")],
  //         birthDate: row[headers.indexOf("birthDate")],
  //         address: row[headers.indexOf("address")],
  //       };

  //       try {
  //         const existingAccount = await $scope.getByEmailAccount(
  //           accountData.email
  //         );
  //         if (existingAccount) {
  //           console.warn(
  //             `Email ${accountData.email} already exists. Skipping this entry.`
  //           );
  //           $scope.showErrorNotification("Email đã tồn tại");
  //           continue; // Skip this entry and move to the next one
  //         }

  //         const addAccountResponse = await $scope.addAccount(accountData);
  //         if (addAccountResponse) {
  //           const getByEmail = await $scope.getByEmailAccount(
  //             addAccountResponse.email
  //           );
  //           if (getByEmail) {
  //             const addEmployeeData = {
  //               account: {
  //                 id: getByEmail.id,
  //               },
  //               ...employeeData,
  //             };
  //             await $scope.addEmployee(addEmployeeData);
  //             $scope.showSuccessNotification("Thêm thông tin thành công");
  //             $scope.getEmployee(1);
  //             $("#modalAddExcel").modal("hide");
  //           }
  //         }
  //       } catch (error) {
  //         console.error("Error adding employee from Excel row:", row, error);
  //       }
  //     }
  //     $scope.getEmployee(0); // Refresh the employee list
  //     $scope.$apply();
  //   };

  //   reader.readAsArrayBuffer(file);
  // };


  //hàm làm mới form input (modal thêm)
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
    $scope.formCreateEmployee.$setPristine();
    $scope.formCreateEmployee.$setUntouched();
  };

  $scope.checkEmailUpdate = function () {
    var email = $scope.formUpdateAccount.email;

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
  $scope.checkAccountUpdate = function () {
    const account = $scope.formUpdateAccount.account;

    console.log("Checking account:", account); // Debugging log

    $http
      .get(apiAccount + "/check-account", { params: { account: account } })
      .then(function (response) {
        console.log("API Response:", response.data)

        if (response.data) {
          // account bị trùng
          $scope.accountError = "Tài khoản đã tồn tại";
          return false;
        } else {
          // account hợp lệ và không bị trùng
          $scope.accountError = "";
          return true;
        }
      })

  };

  //Hàm check trùng số điện thoại
  $scope.checkPhoneNumberUpdate = function () {
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




  //Sửa Nhân Viên

  $scope.updateAccount = async (objectAccount) => {
    try {

      const result = await $http.put(
        `${apiAccount}/email/${$scope.emailAccount}`,
        objectAccount
      );
      console.log("Sửa tài khoản thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi sửa tài khoản:", error);
      if (error.status === 400) { // Điều chỉnh điều kiện theo mã lỗi của API
        $scope.accountError = "Tài khoản đã tồn tại";
      }
      throw error;
    }
  };


  const pass = null;
  const status = null;
  //Hàm xử lý sửa account. Check điều kiện kiểm tra và gọi api thêm mới account
  $scope.suaAccount = async function () {
    const email = $scope.formUpdateAccount.email;

    if (email) {
      const dataAccount = {
        account: $scope.formUpdateAccount.account,
        email: email,
        phoneNumber: $scope.formUpdateAccount.phoneNumber,
        role: $scope.formUpdateAccount.role,
        password: $scope.pass,
        status: $scope.statu
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

  // sửa nhân viên
  $scope.updateEmployee = async (objectData) => {
    let item = angular.copy($scope.formUpdate);
    try {
      const result = await $http.put(`${apiEmployee}/${item.id}`, objectData);
      console.log("Sửa account thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi sửae tài khoản account", error);
      throw error;
    }
  };

  $scope.uploadAvatar = async function () {
    const fileInput = document.getElementById("image-update");
    const file = fileInput.files[0];

    if (file) {
      $scope.loading = true; // Bật trạng thái loading khi bắt đầu upload ảnh

      const reader = new FileReader();
      reader.readAsDataURL(file);

      return new Promise((resolve, reject) => {
        reader.onload = async function () {
          const data = reader.result.split(",")[1];
          const postData = {
            name: file.name,
            type: file.type,
            data: data,
          };

          try {
            await $scope.postFile(postData);
            if ($scope.uploadedImageData) {
              resolve($scope.uploadedImageData);
            } else {
              $scope.showErrorNotification("Lỗi khi tải ảnh lên.");
              reject(new Error("Failed to upload image"));
            }
          } catch (postFileError) {
            console.error("Error posting file:", postFileError);
            $scope.showErrorNotification("Có lỗi xảy ra khi tải ảnh lên.");
            reject(postFileError);
          }
        };

        reader.onerror = function (error) {
          console.error("FileReader error:", error);
          $scope.showErrorNotification("Lỗi khi đọc file.");
          reject(error);
        };
      });
    } else {
      return Promise.resolve($scope.formUpdate.avatar); // Nếu không có ảnh mới, sử dụng avatar cũ
    }
  };

  $scope.updateEmployeeInfo = async function (avatar) {
    try {
      const addAccountData = await $scope.suaAccount();
      if (addAccountData) {
        const dataObject = {
          code: $scope.formUpdate.code,
          account: {
            id: addAccountData.id,
          },
          avatar: avatar || $scope.formUpdate.avatar, // Sử dụng avatar mới nếu có, hoặc giữ avatar cũ
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

        await $scope.updateEmployee(dataObject);
        $scope.showSuccessNotification("Sửa thông tin thành công");
        $scope.getEmployee(0);
        $scope.resetFormUpdate();
        $("#modalUpdate").modal("hide");
      }
    } catch (error) {
      $scope.showErrorNotification("Có lỗi xảy ra khi cập nhật thông tin.");
      console.error("Error updating employee:", error);
    }
  };

  $scope.submitFormUpdate = async function () {
    $scope.formUpdateEmployee.$submitted = true;

    if ($scope.formUpdateEmployee.$valid && !$scope.accountError) {
      try {
        // Upload avatar nếu có và lấy URL của ảnh mới
        const avatar = await $scope.uploadAvatar();

        // Cập nhật thông tin nhân viên
        await $scope.updateEmployeeInfo(avatar);

      } catch (error) {
        console.error("Error during form submission:", error);
      } finally {
        $scope.$applyAsync(() => {
          $scope.loading = false; // Tắt trạng thái loading khi hoàn tất
        });
      }
    } else {
      $scope.showErrorNotification("Thông tin không hợp lệ.");
      console.log($scope.formUpdateEmployee.$error);
      $scope.$applyAsync(() => {
        $scope.loading = false; // Tắt trạng thái loading nếu có lỗi
      });
    }
  };



  // END Sửa nhân viên

  //Hàm gọi id chi tiết nhân viên
  $scope.edit = function (employee) {
    $scope.emailAccount = employee.account.email;
    const birthDateNew = angular.copy(employee.birthDate);
    const createdAt1 = angular.copy(employee.createdAt);
    const updatedAt1 = angular.copy(employee.updatedAt);
    if ($scope.formUpdate.updatedAt) {
      $scope.formUpdate = angular.copy(employee);
    } else {
      $scope.formUpdate = angular.copy(employee);
      $scope.formUpdate.updatedAt = new Date();
    }
    $scope.formUpdateAccount = angular.copy(employee.account);
    $scope.formUpdate.birthDate = new Date(birthDateNew); // Chuyển đổi birthDate sang đối tượng Date
    $scope.formUpdate.avatar = angular.copy(employee.avatar);
    $scope.formUpdate.createdAt = new Date(createdAt1);
    $scope.formUpdate.updatedAt = new Date(updatedAt1);

    $scope.formUpdateAccount.role.id = angular.copy(employee.account.role.id);
    $scope.pass = angular.copy(employee.account.password);
    $scope.statu = angular.copy(employee.account.status);

  };

  // hàm làm mới form update
  $scope.resetFormUpdate = function () {
    $scope.formUpdate = {};
    $scope.formUpdateAccount = {};
    let fileInput = document.getElementById("image-update");
    let imagePreviewUpdate = document.getElementById("image-preview-update");
    imagePreviewUpdate.src = "/src/admin/common/img/no-img.png";
    fileInput.value = "";
    fileInput.type = "file";
    $scope.formUpdateEmployee.$setPristine();
    $scope.formUpdateEmployee.$setUntouched();
  };

  // xuát file danh sách excel Employee
  $scope.xuatFile = function () {
    $http
      .get(apiEmployee + "/excel")
      .then(function (response) {
        $scope.showSuccessNotification("Xuất thông tin thành công");
      })
      .catch((error) => {
        console.log(error);
      });
  };

  $scope.show = function (employee) {
    $scope.formShow = angular.copy(employee);
    $scope.formShow.valid_form = new Date(employee.valid_form);
    $scope.formShow.valid_until = new Date(employee.valid_until); // Hoặc là giá trị ngày mặc định của bạn
  };

  //Phân trang lọc
  $scope.filterEmployee = function () {
    $http.get(apiEmployee + "/status1").then(function (res) {
      $scope.employee = res.data;
    });
  };

  $scope.filteremployee = [];
  $scope.totalPages = 0;
  $scope.currentPage = 0;
  $scope.desiredPage = 1;
  $scope.size = 5;
  $scope.filters = {
    name: null,
    code: null,
    discountType: null,
    startDate: null,
    endDate: null,
  };

  $scope.getEmployee = function (pageNumber) {
    let params = angular.extend(
      { pageNumber: pageNumber, size: $scope.size },
      $scope.filters
    );
    $http
      .get("http://localhost:8080/api/admin/employee/page", {
        params: params,
      })
      .then(function (response) {
        $scope.filteremployee = response.data.content;
        $scope.totalPages = response.data.totalPages;
        $scope.currentPage = pageNumber; // Đặt biến currentPage tại đây
        $scope.desiredPage = pageNumber + 1; // Nếu bạn đang sử dụng desiredPage để phân trang
      });
  };

  $scope.applyFilters = function () {
    $scope.getEmployee(0);
  };

  $scope.goToPage = function () {
    let pageNumber = $scope.desiredPage - 1;
    if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
      $scope.getEmployee(pageNumber); // Lấy dữ liệu cho trang mới
    } else {
      // Reset desiredPage về currentPage nếu đầu vào không hợp lệ
      $scope.desiredPage = $scope.currentPage + 1;
    }
  };

  // Initial load
  $scope.getEmployee(0);

  $scope.resetFilters = function () {
    $scope.filters = {
      fullName: null,
      code: null,
      phoneNumber: null,
      birthDate: null,
      gender: null,
      status: null,
    };
    $scope.getEmployee(0);
  };

  //delete or update status Employee
  $scope.delete = function (item) {
    $http
      .put(apiEmployee + "/status/" + `${item.id}`, item)
      .then(function (resp) {
        $scope.getEmployee();
      })
      .catch(function (error) {
        console.log("Error", error);
      });
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

  // Lịch sử Nhân viên
  // xuát file danh sách excel Employee
  $scope.xuatFileAuditLog = function () {
    $http
      .get(apiAuditLog + "/exce-lich-su")
      .then(function (response) {
        $scope.showSuccessNotification("Xuất thông tin thành công");
      })
      .catch((error) => {
        console.log(error);
      });
  };

  //phân trang + lọc auddit Log
  // get all da ta lịch sửa nv
  // $scope.getAuditlog = function () {
  //   $http.get(apiAuditLog).then(function (resp) {
  //     $scope.auditLog = resp.data;
  //     //   $scope.employee = angular.copy($scope.originalEmployee);
  //   });
  // };
  $scope.getAuditlog = [];
  $scope.totalPages1 = 0;
  $scope.currentPage1 = 0;
  $scope.desiredPage1 = 1;
  $scope.size1 = 5;
  $scope.filterAuditlog = {
    status: null,
    code: null,
    implementer: null,
    time: null,
    actionType: null,
  };
  // $scope.filterAuditlog.time = $scope.filterAuditlog.time ? $scope.filterAuditlog.time.toISOString().split('T')[0] : null;

  $scope.getAllAuditLog = function (pageNumber) {
    let params = angular.extend(
      { pageNumber: pageNumber, size: $scope.size1 },
      $scope.filterAuditlog
    );
    $http
      .get("http://localhost:8080/api/admin/audit-log/auditlog-page", {
        params: params,
      })
      .then(function (response) {
        $scope.getAuditlog = response.data.content;
        $scope.totalPages1 = response.data.totalPages;
        $scope.currentPage1 = pageNumber;
        $scope.desiredPage1 = pageNumber + 1;
      });
  };

  $scope.applyFiltersAuditlog = function () {
    // Chuyển đổi thời gian từ định dạng yyyy-MM-dd sang Date nếu cần
    if ($scope.filterAuditlog.time) {
      let date = new Date($scope.filterAuditlog.time);
      $scope.filterAuditlog.time = date.toISOString().split('T')[0]; // Lấy ngày, bỏ giờ phút giây
    }
    $scope.getAllAuditLog(0);
  };


  $scope.goToPageAuditlog = function () {
    let pageNumber = $scope.desiredPage1 - 1;
    if (pageNumber >= 0 && pageNumber < $scope.totalPages1) {
      $scope.getAllAuditLog(pageNumber);
    } else {
      // Reset desiredPage to currentPage if the input is invalid
      $scope.desiredPage1 = $scope.currentPage1 + 1;
    }
  };
  // Initial load
  $scope.getAllAuditLog(0);

  $scope.resetfilterAuditlog = function () {
    $scope.filterAuditlog = {
      status: null,
      code: null,
      implementer: null,
      time: null,
      actionType: null,
    };
    $scope.getAllAuditLog(0);
  };

  $scope.formatDateTime = function (dateString) {
    // Tạo đối tượng Date từ chuỗi ngày tháng ISO
    var date = new Date(dateString);

    // Lấy ngày, tháng, năm
    var day = String(date.getDate()).padStart(2, "0");
    var month = String(date.getMonth() + 1).padStart(2, "0"); // Tháng tính từ 0-11, cần +1
    var year = date.getFullYear();

    // Lấy giờ, phút, giây
    var hours = String(date.getHours()).padStart(2, "0");
    var minutes = String(date.getMinutes()).padStart(2, "0");
    var seconds = String(date.getSeconds()).padStart(2, "0");

    // Trả về chuỗi định dạng dd/MM/yyyy HH:mm:ss
    return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
  };
  //Hàm gọi id chi tiết nhân viên
  $scope.detailAuditLog = function (employee) {
    $scope.formDetailAuditLog = angular.copy(employee);
    $scope.formDetailAuditLog.time = $scope.formatDateTime(employee.time);
  };
});
