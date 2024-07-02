// document.addEventListener("DOMContentLoaded", function () {
//     const container = document.getElementById('container');
//     const registerBtn = document.getElementById('register');
//     const loginBtn = document.getElementById('login');

//     const loginPanel = document.querySelector('.toggle-panel.toggle-left');
//     const registerPanel = document.querySelector('.toggle-panel.toggle-right');

//     loginBtn.addEventListener('click', () => {
//         loginPanel.classList.add("active");
//         registerPanel.classList.remove("active");
//     });

//     registerBtn.addEventListener('click', () => {
//         loginPanel.classList.remove("active");
//         registerPanel.classList.add("active");
//     });
// });

const container = document.getElementById('container');
const loginBtn = document.getElementById('login');
const registerBtn = document.getElementById('register');

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});


function registerUser() {
    var username = document.getElementById('username').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;

    var formData = {
        account: username,
        email: email,
        password: password,
        crearedAt: new Date(),
        role: {
            id: 3 // Role mặc định là 3
        },
        status: 1
        // Các trường thông tin khác nếu cần thiết
    };

    // Gửi yêu cầu POST đến API endpoint /api/accounts
    fetch('/user/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Đăng ký không thành công!');
        }
        alert('Đăng ký thành công, vui lòng kiểm tra mail để xác nhận!');
        // Xử lý khi đăng ký thành công
    })
    .catch(error => {
        alert(error.message);
        // Xử lý khi có lỗi xảy ra
    });
}

document.getElementById('otp').style.display = 'none';
// document.getElementById('container').style.display = 'none';

function registerUser() {
    // Show OTP form, hide container
    document.getElementById('otp').style.display = 'block';
    document.getElementById('container').style.display = 'none';
}

function confirmOTP() {
    // Hide OTP form, show container
    document.getElementById('otp').style.display = 'none';
    document.getElementById('container').style.display = 'block';
}

function confirmOTP() {
    const otp = document.getElementById('otpInput').value;
    // Gửi yêu cầu xác nhận OTP đến backend
    fetch('/user/confirm-otp', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ otp: otp }) // Gửi mã OTP lên server
    })
    .then(response => response.json())
    .then(data => {
        // Xử lý phản hồi từ server
        if (data.status === '200') {
            alert('OTP confirmed successfully');
            // Xử lý khi OTP được xác nhận thành công, ví dụ: chuyển hướng người dùng đến trang khác
            window.location.href = '/success-page';
        } else {
            alert('OTP confirmation failed');
            // Xử lý khi OTP không hợp lệ, ví dụ: hiển thị thông báo lỗi cho người dùng
        }
    })
    .catch(error => {
        console.error('Error:', error);
        // Xử lý khi có lỗi xảy ra trong quá trình gửi yêu cầu
    });
}