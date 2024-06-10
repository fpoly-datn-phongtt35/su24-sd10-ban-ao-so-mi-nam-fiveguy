app.controller("tinh-employee-controller", function ($scope, $http) {
  $scope.originalEmployee = [];
  $scope.employee = [];
  $scope.formUpdate = {};
  $scope.formInput = {};
  $scope.formDelete = {};
  $scope.showAlert = false;
  $scope.currentDate = new Date();
  $scope.showAlert = false;
  $scope.showError = false;

  $scope.formtimkiem = "1";
  $scope.load = function () {
    $scope.loading = true;
  };
  $scope.unload = function () {
    $scope.loading = false;
  };
  const apiEmployee = "http://localhost:8080/api/admin/employee";
  const apiAccount = "http://localhost:8080/account";

  imgShow("image", "image-preview");
  // imgShow("image-update", "image-preview-update");
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

  $scope.getStatus = function () {
    if ($scope.formtimkiem === "1") {
      $http.get(apiEmployee + "/search-status/1").then(function (resp) {
        $scope.originalEmployee = resp.data;
        $scope.employee = angular.copy($scope.originalEmployee);
      });
    } else if ($scope.formtimkiem === "2") {
      $http.get(apiEmployee + "/search-status/2").then(function (resp) {
        $scope.originalEmployee = resp.data;
        $scope.employee = angular.copy($scope.originalEmployee);
      });
    }
  };
  $scope.getStatus();

  //   $scope.loadAccounts = function () {
  //     $http
  //       .get(apiAccount + "/not-in-customer-employee")
  //       .then(function (resp) {
  //         $scope.accounts = resp.data;
  //       })
  //       .catch(function (error) {
  //         console.log("Error loading accounts", error);
  //       });
  //   };

  //   $scope.loadAccounts();

  //Thêm nhân viên demo
  $scope.themNhanVien = function () {
    // console.log("Đây lèm thêm NV");
    let items = angular.copy($scope.formInput);
    $http
      .post(apiEmployee, items)
      .then(function (response) {
        console.log(response);
        $scope.getEmployee();
        $("#modalAdd").modal("hide");
      })
      .catch(function () {
        console.log("Error", error);
      });
  };
  // create employee

  $scope.resetFormInput = function () {
    $scope.formInput = {};
    let fileInput = document.getElementById("image");
    let imagePreview = document.getElementById("image-preview");
    imagePreview.src = "/fe/src/admin/common/img/no-img.png";
    fileInput.value = "";
    fileInput.type = "file";
    $scope.formCreateEmployee.$setPristine();
    $scope.formCreateEmployee.$setUntouched();
  };

  //Sửa nhân viên demo
  $scope.suaNhanVien = function () {
    let item = angular.copy($scope.formUpdate);
    $http
      .put(apiEmployee + `/${item.id}`, item)
      .then(function (response) {
        console.log(response);
        $scope.getEmployee();
        $("#modalUpdate").modal("hide");
      })
      .catch(function () {
        console.log("Error", error);
      });
  };

  $scope.edit = function (employee) {
    console.log(employee);
    const birthDateNew = $scope.formatDate(employee.birthDate);

    console.log(birthDateNew);
    if ($scope.formUpdate.updatedAt) {
      $scope.formUpdate = angular.copy(employee);
    } else {
      $scope.formUpdate = angular.copy(employee);
      $scope.formUpdate.birthDate = new Date(birthDateNew);
      $scope.formUpdate.birthDate = new Date(birthDateNew);
      $scope.formUpdate.updatedAt = new Date();

      console.log($scope.formUpdate);
    }
  };

  $scope.resetFormUpdate = function () {
    $scope.formUpdate = {};
    let fileInput = document.getElementById("image-update");
    let imagePreviewUpdate = document.getElementById("image-preview-update");
    imagePreviewUpdate.src = "/assets/img/no-img.png";
    fileInput.value = "";
    fileInput.type = "file";
    $scope.formUpdateEmployee.$setPristine();
    $scope.formUpdateEmployee.$setUntouched();
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
      .get("http://localhost:8080/api/admin/employee/page", { params: params })
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
});
