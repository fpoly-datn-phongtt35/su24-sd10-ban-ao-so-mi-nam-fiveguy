$scope.updateStatusSteps = function () {
    const possibleSteps = {
        1: { title: "Chờ xác nhận", icon: "schedule", status: 1 },
        2: { title: "Chờ vận chuyển", icon: "local_shipping", status: 2 },
        3: { title: "Đang giao", icon: "directions_car", status: 3 },
        4: { title: "Thành công", icon: "home", status: 4 },
        5: { title: "Khách hủy", icon: "cancel", status: 5 },
        6: { title: "Đã hủy", icon: "cancel", status: 6 },
        7: { title: "Thất bại", icon: "cancel", status: 7 },
        8: { title: "Thất bại", icon: "cancel", status: 8 },
        9: { title: "Chờ giao lại", icon: "local_shipping", status: 9 },
        10: { title: "Đang giao lại", icon: "directions_car", status: 10 },
        11: { title: "Hoàn hàng", icon: "warehouse", status: 11 }
    };

    $scope.deliveryFlow = {
        1: [2, 5, 6], // Chờ xác nhận -> Chờ vận chuyển hoặc khách hủy hoặc đã hủy
        2: [3, 5, 6], // Chờ vận chuyển -> Đang giao hoặc khách hủy hoặc đã hủy
        3: [4, 7], // Đang giao -> Thành công hoặc Thất bại
        4: [], // Thành công (kết thúc)
        5: [], // Khách hủy (kết thúc)
        6: [], // Đã hủy (kết thúc)
        7: [8, 10], // Thất bại -> Chờ giao lại hoặc Đang chuyển hàng về kho
        8: [11], // Thất bại -> Chờ giao lại hoặc Đang chuyển hàng về kho
        9: [10], // Chờ giao lại -> Đang giao lại
        10: [4, 8], // Đang giao lại -> Thành công hoặc Thất bại (lai)
        11: [6], // Hoàn hàng -> Đã hủy
    };

    $scope.steps = [];

    $scope.listBillHistory.forEach(function (history) {
        let step = $scope.steps.find(s => s.status === history.status);
        if (step) {
            step.time = history.createdAt;
        }
    });

    $scope.status = Math.max.apply(Math, $scope.listBillHistory.map(function (o) { return o.status; }));
    // if ($scope.listBillHistory.length > 0) {
    //     $scope.status = $scope.listBillHistory[$scope.listBillHistory.length - 1].status;
    // } else {
    //     $scope.status = 1; // Hoặc một giá trị mặc định nếu không có lịch sử trạng thái
    // }
};