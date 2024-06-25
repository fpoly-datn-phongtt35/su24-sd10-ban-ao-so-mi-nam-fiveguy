


var app3 = angular.module('resetPass', ['ngRoute']);



app3.controller('resetPassCtrcl', function ($scope, $http, $location, $routeParams) {
    var email = $routeParams.email;
    console.log(email);

    var email = $location.search().email;
    console.log(email);

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


    $scope.isPasswordValid = function(pass) {
        const password = pass;
      
        // Kiểm tra password có tồn tại và có độ dài lớn hơn 10 không
        const isLengthValid = password && password.length > 10;
      
        // Kiểm tra mật khẩu có chứa ký tự hoa và số không
        const containsUpperCase = /[A-Z]/.test(password);
        const containsNumber = /\d/.test(password);
      
        return isLengthValid && containsUpperCase && containsNumber;
      };


    $scope.resetPass = function () {
        // Lấy userName từ localStorage
    
        var email = localStorage.getItem("email")
    

        if (!$scope.isPasswordValid($scope.password2)) {
            $scope.showErrorNotification("Mật khẩu có độ dài tối thiểu 10 kí tự, viết Hoa, chứa kí tự số.")
            return;
        }

        if ($scope.password2 !== $scope.passwordCheck) {
            $scope.showErrorNotification("Mật khẩu xác nhận không khớp.")
            return;
        }
        // Tạo đối tượng data để gửi đi thông qua API
        var data = {
            email: email,
            newPassword: $scope.password2
        };
        // Gọi API để reset mật khẩu
        $http.post('http://localhost:8080/auth/reset-password', data)
            .then(function (response) {
                if (response.data === true) {
                    $scope.showSuccessNotification("Đặt lại mật khẩu thành công!")
                    var email = localStorage.removeItem("email")
                    // Thực hiện các hành động sau khi reset mật khẩu thành công
                    window.location.href = 'http://127.0.0.1:5555/src/login/login.html';

                } else {
                    $scope.showErrorNotification("Đặt lại mật khẩu không thành công!")
                }
            })
            .catch(function (error) {
                $scope.showErrorNotification("Đặt lại mật khẩu không thành công!")

            });
    };
});

var app2 = angular.module('otp', ['ngRoute']);

app2.controller('otpCtrcl', function ($scope, $http, $location, $routeParams) {

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

    $scope.countdownValue = 60; // Khởi tạo giá trị đếm ngược ban đầu

    $scope.resendConfirmation = function () {
        if (!$scope.email ) {
            $scope.showErrorNotification("Email và mã xác minh không được để trống.");
            return;
        }

        localStorage.setItem("email",$scope.email);

        $http.post('http://localhost:8080/auth/reSendOTP/' + $scope.email)
            .then(function (response) {
                $scope.message = response.data.message; // Lưu thông báo từ server để hiển thị cho người dùng
                $scope.showSuccessNotification("Mã xác minh thành công")

            })
            .catch(function (error) {
                $scope.showErrorNotification("Mã xác minh không chính xác vui lòng kiểm tra email và nhập lại")

            });

        $scope.resendDisabled = true; // Vô hiệu hóa nút Gửi lại để tránh gửi liên tục
        $scope.showCountdown = true; // Hiển thị text đếm ngược

        var countdown = $scope.countdownValue;
        var countdownInterval = setInterval(function () {
            countdown--;
            $scope.countdownValue = countdown;
            if (countdown <= 0) {
                clearInterval(countdownInterval);
                $scope.resendDisabled = false; // Kích hoạt lại nút Gửi lại khi đếm ngược kết thúc
                $scope.showCountdown = false; // Ẩn text đếm ngược
            }
            $scope.$apply(); // Cập nhật giao diện
        }, 1000);
        
    };
    $scope.resendOTP = function () {
        $http.post('http://localhost:8080/auth/reSendOTP/' + $scope.email)
            .then(function (response) {
                console.log('Gửi lại Mã thành công:', response.data);
                $scope.showSuccessNotification("Vui lòng kiểm tra mã xác minh trong email");
            })
            .catch(function (error) {
                console.error('Lỗi khi gửi lại Mã:', error);
            });
    };

    $scope.submitOTP = function () {

        if (!$scope.email || !$scope.otp) {
            $scope.showErrorNotification("Email và mã xác minh không được để trống.");
            return;
        }
        
        var email = localStorage.getItem("email")

        var data = {
            email: email,
            enteredOTP: $scope.otp
        };

        $http.post('http://localhost:8080/auth/confirm-otp', data)
            .then(function (response) {
                // Xử lý kết quả từ server sau khi xác nhận OTP thành công
                if (response.data === true) {
                    localStorage.setItem('email', $scope.email);
                    $scope.showSuccessNotification("Mã được xác nhận thành công!");

                    // Thực hiện các hành động khác sau khi xác nhận mã OTP thành công
                    window.location.href = 'http://127.0.0.1:5555/src/login/resetPass.html';

                } else {
                    // Mã OTP không hợp lệ
                    $scope.showErrorNotification("Mã không hợp lệ!");

                    // Thực hiện xử lý khi mã OTP không hợp lệ
                }
            })
            .catch(function (error) {
                $scope.showErrorNotification("Mã không hợp lệ!");
            });
    };

});

var app = angular.module('security2', ['ngRoute']);

app.controller('LoginCtrl2', function ($scope, $http, $location) {

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


    // app.config(function ($routeProvider) {
    //     $routeProvider
    //         .when('/forgotPass', {
    //             templateUrl: 'login/forgotPassword.html',
    //             controller: 'LoginCtrl' // Controller cho trang này (nếu cần)
    //         })        
    //         .when('/login', {
    //             templateUrl: 'login/index.html',
    //             controller: 'LoginCtrl' // Controller cho trang này (nếu cần)
    //         })
    //         .otherwise({ redirectTo: '/login' }); // Route mặc định
    // });


    $scope.user = {};


    $scope.login = function () {
        if (!$scope.username || !$scope.password) {
            $scope.showErrorNotification("Vui lòng điền đầy đủ thông tin tên người dùng và mật khẩu.");

            return;
        }

        var data = {
            username: $scope.username,
            password: $scope.password
        };
        $http.post('http://localhost:8080/auth/login', data)
            .then(function (response) {
                // Lưu token vào Local Storage
                localStorage.setItem('token', response.data.accessToken);
                console.log(response.data.accessToken);
                localStorage.setItem('refreshToken', response.data.refreshToken);
                $scope.showSuccessNotification("Đăng nhập thành công");
                window.location.href = 'http://127.0.0.1:5555/src/user/index.html#/home';
            })
            .catch(function (error) {
                if (error.status === 404) {

                    $scope.showErrorNotification("Tài khoản không tồn tại.");
                } else if (error.status === 401) {

                    $scope.showErrorNotification("Sai tên người dùng hoặc mật khẩu.");
                } else {
                    $scope.showErrorNotification("Đăng nhập không thành công.");
                }
                console.error('Đăng nhập không thành công:', error);
            });
    };

    $scope.validateEmail = function (email) {
        // Biểu thức chính quy kiểm tra định dạng email
        var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        return emailPattern.test(email);
    };

    $scope.isPasswordValid = function(pass) {
        const password = pass;
      
        // Kiểm tra password có tồn tại và có độ dài lớn hơn 10 không
        const isLengthValid = password && password.length > 10;
      
        // Kiểm tra mật khẩu có chứa ký tự hoa và số không
        const containsUpperCase = /[A-Z]/.test(password);
        const containsNumber = /\d/.test(password);
      
        return isLengthValid && containsUpperCase && containsNumber;
      };


    $scope.checkEmailExists = function () {
        return $http.post('http://localhost:8080/check-email', { email: $scope.user.email })
            .then(function (emailResponse) {
                $scope.emailExists = emailResponse.data;
            })
            .catch(function (error) {

                console.error('Lỗi khi kiểm tra email:', error);
                throw error; // Throw error to propagate it upwards
            });
    };

    $scope.checkAccountExists = function () {
        return $http.post('http://localhost:8080/check-account', { account: $scope.user.account })
            .then(function (accountResponse) {
                $scope.accountExists = accountResponse.data;
            })
            .catch(function (error) {

                console.error('Lỗi khi kiểm tra account:', error);
                throw error; // Throw error to propagate it upwards
            });
    };

    $scope.registerUser = function () {
        $scope.lastNameError = false;
        $scope.firstNameError = false;
        $scope.emailError = false;
        $scope.accountError = false;
        $scope.passwordError = false;
        $scope.confirmPasswordError = false;

        $scope.checkEmailExists().then(function () {
            $scope.checkAccountExists().then(function () {
                if (!$scope.user.lastName) {
                    $scope.lastNameError = true;
                }

                if (!$scope.user.firstName) {
                    $scope.firstNameError = true;
                }

                if (!$scope.validateEmail($scope.user.email) || $scope.emailExists) {
                    $scope.emailError = true;
                }

                if (!$scope.user.account || $scope.accountExists) {
                    $scope.accountError = true;
                }

                if (!$scope.isPasswordValid($scope.user.password)) {
                    $scope.passwordError = true;

                } else {
                    $scope.passwordError = false;


                }
                

                if ($scope.user.password !== $scope.user.confirmPassword) {
                    $scope.confirmPasswordError = true;
                } else {
                    $scope.confirmPasswordError = false;
                }


                if (
                    $scope.lastNameError ||
                    $scope.firstNameError ||
                    $scope.emailError ||
                    $scope.accountError ||
                    $scope.passwordError ||
                    $scope.confirmPasswordError
                ) {
                    return;
                }

                $http.post('http://localhost:8080/auth/register', $scope.user)
                    .then(function (registerResponse) {
                        console.log('Kết quả từ API:', registerResponse.data);
                        $scope.showSuccessNotification("Đăng ký thành công");
                        window.location.href = "http://127.0.0.1:5555/src/login/login.html";
                    })
                    .catch(function (registerError) {
                        $scope.showErrorNotification("Đăng kí thất bại");
                        console.error('Lỗi khi gọi API:', registerError);
                    });
            });
        });
    };



    // $scope.logout = function () {
    //     // Xóa token khỏi localStorage
    //     localStorage.removeItem('token');
    //     localStorage.removeItem('refreshToken');
    //     alert('Đăng xuất thành công');

    //     window.location.href = 'http://127.0.0.1:5555/olView/index.html#!/home';
    // };

});

