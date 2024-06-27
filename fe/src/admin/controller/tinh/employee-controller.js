app.controller("tinh-employee-controller", function ($scope, $http) {
  $scope.originalEmployee = [];
  $scope.employee = [];
  $scope.formUpdate = {};
  $scope.formInput = {};
  $scope.formInputAccount = {};
  $scope.formUpdateAccount = {};
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

  imgShow("image", "image-preview");
  imgShow("image-update", "image-preview-update");
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
        };
        reader.readAsDataURL(imageInput.files[0]);
      }
    });
  }

  let allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
  $scope.showErrorImg = function (message) {
    $scope.alertErrorImg = message;
    $scope.showError = true;
  };

  //Hàm check email ko được bắt đầu bằng số
  $scope.checkEmail = function () {
    var email = $scope.formInputAccount.email;
    if (!email) {
      // Trường hợp trường input trống, coi như hợp lệ
      $scope.emailError = "";
      return true;
    }

    // Kiểm tra nếu email không bắt đầu bằng số
    var EMAIL_REGEXP = /^[^\d].*/;
    if (EMAIL_REGEXP.test(email)) {
      // Hợp lệ
      $scope.emailError = "";
      return true;
    }

    // Không hợp lệ
    $scope.emailError = "Email không được bắt đầu bằng số";
    return false;
  };

  // $scope.submitForm = function () {
  //   if ($scope.formCreateEmployee.$valid && $scope.checkEmail()) {
  //     // Gửi form đi
  //   } else {
  //     // Hiển thị lỗi
  //     $scope.formCreateEmployee.$submitted = true;
  //   }
  // };

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
    const fileInput = document.getElementById("image");
    const file = fileInput.files[0];
    if (!file) {
      $scope.showError = true;
      $scope.$apply();
      return;
    }

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
          $scope.submitForm();
        }
      });
    };
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
  $scope.submitForm = async function () {
    if ($scope.formCreateEmployee.$valid && $scope.checkEmail()) {
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
          avatar: $scope.uploadedImageData, // Add the image data here
        };
        const addEmployeesData = await $scope.addEmployee(dataObject);
        console.log("addEmployeesData = ", addEmployeesData);
        $scope.showSuccessNotification("Thêm thông tin thành công");
        $scope.getEmployee(0);
        $scope.resetFormUpdate();
        $("#modalUpdate").modal("hide");
      }
    } else {
      // Hiển thị lỗi
      $scope.showErrorNotification("Không thành công");
      $scope.formCreateEmployee.$submitted = true;
    }
  };
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
    imagePreview.src = "/fe/src/admin/common/img/no-img.png";
    fileInput.value = "";
    fileInput.type = "file";
    $scope.formCreateEmployee.$setPristine();
    $scope.formCreateEmployee.$setUntouched();
  };

  //Sửa Nhân Viên
  $scope.uploadBtnUpdate = async function () {
    const fileInputupdate = document.getElementById("image-update");
    const file = fileInputupdate.files[0];

    if (!file) {
      $scope.showError = true;
      //submit form khi ảnh ko được thay đổi
      if ($scope.formUpdateEmployee.$valid) {
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
            status: $scope.formUpdate.status,
          };
          console.log(dataObject);
          const updateEmployeesData = await $scope.updateEmployee(dataObject);
          $scope.showSuccessNotification("Sửa thông tin thành công");
          $scope.getEmployee(0);
          $scope.resetFormUpdate();
          console.log("updateEmployeesData = ", updateEmployeesData);
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
    // let email = $scope.edit(employee.account.email);

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
    const email = $scope.formUpdateAccount.email;
    if (email) {
      const dataAccount = {
        account: $scope.formUpdateAccount.account,
        email: email,
        phoneNumber: $scope.formUpdateAccount.phoneNumber,
        role: $scope.formUpdateAccount.role,
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
  $scope.updateEmployee = async (objectData) => {
    let item = angular.copy($scope.formUpdate);
    console.log(item);
    try {
      const result = await $http.put(`${apiEmployee}/${item.id}`, objectData);
      console.log("Sửa account thành công", result.data);
      return result.data;
    } catch (error) {
      console.error("Lỗi sửae tài khoản account", error);
      throw error;
    }
  };

  // Form submit update
  $scope.submitFormUpdate = async function () {
    if ($scope.formUpdateEmployee.$valid) {
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
        const updateEmployeesData = await $scope.updateEmployee(dataObject);
        $scope.getEmployee(0);
        $scope.resetFormUpdate();
        $scope.showSuccessNotification("Sửa thông tin thành công");
        console.log("updateEmployeesData = ", updateEmployeesData);
        $("#modalUpdate").modal("hide"); // Đóng modal bằng JavaScript thuần
      }
      // $uibModalInstance.close(); // Đóng modal
    }
  };
  // END Sửa nhân viên

  //Hàm gọi id chi tiết nhân viên
  $scope.edit = function (employee) {
    $scope.emailAccount = employee.account.email;
    // console.log($scope.emailAccount);
    const birthDateNew = $scope.formatDate(employee.birthDate);
    if ($scope.formUpdate.updatedAt) {
      $scope.formUpdate = angular.copy(employee);
    } else {
      $scope.formUpdate = angular.copy(employee);
      $scope.formUpdate.birthDate = new Date(birthDateNew);
      $scope.formUpdate.updatedAt = new Date();
      $scope.formUpdateAccount = angular.copy(employee.account);
    }
    $scope.formUpdate.avatar = angular.copy(employee.avatar);
  };

  // hàm làm mới form update
  $scope.resetFormUpdate = function () {
    $scope.formUpdate = {};
    $scope.formUpdateAccount = {};
    let fileInput = document.getElementById("image-update");
    let imagePreviewUpdate = document.getElementById("image-preview-update");
    imagePreviewUpdate.src = "/fe/src/admin/common/img/no-img.png";
    fileInput.value = "";
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

  //Phân trang
  $scope.filterEmployee = function () {
    $http.get(apiEmployee + "/status1").then(function (res) {
      $scope.employee = res.data;
    });
  };

  $scope.filteremployee = [];
  $scope.totalPages = 0;
  $scope.currentPage = 0;
  $scope.desiredPage = 1;
  $scope.filters = {
    name: null,
    code: null,
    discountType: null,
    startDate: null,
    endDate: null,
    status: null,
  };

  $scope.getEmployee = function (pageNumber) {
    let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
    $http
      .get("http://localhost:8080/api/admin/employee/page", {
        params: params,
      })
      .then(function (response) {
        $scope.filteremployee = response.data.content;
        $scope.totalPages = response.data.totalPages;
        $scope.currentPage = pageNumber;
        $scope.desiredPage = pageNumber + 1;
      });
  };

  $scope.applyFilters = function () {
    $scope.getEmployee(0);
  };

  $scope.goToPage = function () {
    let pageNumber = $scope.desiredPage - 1;
    if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
      $scope.getEmployee(pageNumber);
    } else {
      // Reset desiredPage to currentPage if the input is invalid
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
});
