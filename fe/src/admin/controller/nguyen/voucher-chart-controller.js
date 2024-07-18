// Định nghĩa controller để xử lý dữ liệu và vẽ biểu đồ
app.controller('nguyen-voucher-chart-ctrl', function ($scope, $http) {
    // Khởi tạo dữ liệu biểu đồ
    $scope.chartData = {};

    $scope.loadData = function () {
        // Kiểm tra và chuyển đổi các giá trị thành số nguyên nếu có
        var startDate = formatDateToYYYYMMDD($scope.startDate);
        var endDate = formatDateToYYYYMMDD($scope.endDate);

        // Gửi request với các tham số đã kiểm tra và chuyển đổi
        $http.get('http://localhost:8080/api/admin/voucher/statistics', {
            params: {
                startDate: startDate,
                endDate: endDate
            }
        }).then(function (response) {
            var data = response.data;

            // Xử lý dữ liệu từ API thành dữ liệu biểu đồ
            $scope.chartData.labels = ['Vouchers Created Today', 'Total Vouchers', 'Vouchers Used Today', 'Total Vouchers Used'];
            $scope.chartData.data = [
                parseInt(data.vouchersCreatedToday),
                parseInt(data.totalVouchers),
                parseInt(data.vouchersUsedToday),
                parseInt(data.totalVouchersUsed)
            ];

            // Gọi hàm để vẽ biểu đồ
            $scope.drawChart();
        }, function (error) {
            console.error('Error loading data: ', error);
        });
    };

    function formatDateToYYYYMMDD(date) {
        var year = date.getFullYear();
        var month = (date.getMonth() + 1 < 10 ? '0' : '') + (date.getMonth() + 1);
        var day = (date.getDate() < 10 ? '0' : '') + date.getDate();
        return year + '-' + month + '-' + day;
    };


    // Hàm vẽ biểu đồ
    $scope.drawChart = function () {
        var ctx = document.getElementById('myChart').getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: $scope.chartData.labels,
                datasets: [{
                    label: 'Voucher Statistics',
                    data: $scope.chartData.data,
                    backgroundColor: [
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)'
                    ],
                    borderColor: [
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    };

    // Load dữ liệu khi controller được khởi tạo
    $scope.loadData();
});