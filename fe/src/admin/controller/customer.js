
app.controller("customerCtrl", function ($scope, $http, $timeout) {

    console.log("Hello")

    $scope.originalCustomer = [];
    $scope.customer = [];
    $scope.formUpdate = {};
    $scope.formInput = {};
    $scope.showAlert = false;
    $scope.currentDate = new Date();
    $scope.showError = false;
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

    $scope.initialize = function () {
        $http.get(apiCustomer).then(function (resp) {
            $scope.originalCustomer = resp.data;
            $scope.customer = angular.copy($scope.originalCustomer);
        });
    };

    $scope.initialize();

    $scope.loadAccounts = function () {
        $http
            .get("/account/not-in-customer-employee") // Thay đổi đường dẫn API tương ứng
            .then(function (resp) {
                $scope.accounts = resp.data;
            })
            .catch(function (error) {
                console.log("Error loading accounts", error);
            });
    };

    $scope.loadAccounts(); // Gọi hàm để nạp danh sách khách hàng khi controller khởi chạy

    // $scope.create = function () {
    //     let item = angular.copy($scope.formInput);
    //     item.createdAt = $scope.currentDate;
    //     $http.post("/customer", item).then(function (resp) {
    //         $scope.showSuccessMessage("Create customer successfully");
    //         $scope.resetFormInput();
    //         $scope.initialize();
    //         $('#modalAdd').modal('hide');
    //     }).catch(function (error) {
    //         console.log("Error", error);
    //     });
    // }
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
                            id: 3,
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
        $http.get(apiCustomerType).then(function (response) {
            $scope.customerTypes = response.data;
        }).catch(function (error) {
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
        if ($scope.formUpdate.updatedAt) {
            $scope.formUpdate = angular.copy(customer);
        } else {
            $scope.formUpdate = angular.copy(customer);
            $scope.formUpdate.updatedAt = new Date();
        }
    };

    $scope.delete = function (item) {
        $http
            .put(apiCustomer + "/update-status" + `/${item.id}`, item)
            .then(function (resp) {
                $scope.showSuccessMessage("Delete Customer successfully");
                $scope.initialize();
            })
            .catch(function (error) {
                console.log("Error", error);
            });
    };

    $scope.resetFormUpdate = function () {
        $scope.formUpdate = {};
        let fileInput = document.getElementById("image-update");
        let imagePreviewUpdate = document.getElementById("image-preview-update");
        imagePreviewUpdate.src = "/assets/img/no-img.png";
        fileInput.value = "";
        fileInput.type = "file";
        $scope.formUpdateCustomer.$setPristine();
        $scope.formUpdateCustomer.$setUntouched();
    };

    $scope.resetFormInput = function () {
        $scope.formInput = {};
        let fileInput = document.getElementById("image");
        let imagePreview = document.getElementById("image-preview");
        imagePreview.src = "/assets/img/no-img.png";
        fileInput.value = "";
        fileInput.type = "file";
        $scope.formCreateCustomer.$setPristine();
        $scope.formCreateCustomer.$setUntouched();
    };

    $scope.changePageSize = function () {
        $scope.paper.page = 0; // Reset về trang đầu tiên khi thay đổi kích thước trang
    };

    $scope.paper = {
        page: 0,
        size: 5, // Kích thước mặc định ban đầu
        get items() {
            let start = this.page * this.size;
            return $scope.customer.slice(start, start + this.size);
        },
        get count() {
            return Math.ceil((1.0 * $scope.customer.length) / this.size);
        },
        first() {
            this.page = 0;
        },
        prev() {
            if (this.page > 0) {
                this.page--;
            }
        },
        next() {
            if (this.page < this.count - 1) {
                this.page++;
            }
        },
        last() {
            this.page = this.count - 1;
        },
    };

    $scope.xuatFile = function () {
        $http.get(apiCustomer + "/excel").then(function (response) {
            $scope.showSuccessNotification("Xuất file thành công");
            // $scope.pageEm = response.data.content;
            // $scope.totalPages = response.data.totalPages
        });
    };
});