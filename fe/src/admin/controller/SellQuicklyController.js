app.controller("SellQuicklyController", function($scope, $http){
    $scope.bills = JSON.parse(localStorage.getItem("bills")) || [{
        code: 'HD' + Number(String(new Date().getTime()).slice(-6)),
            createdAt: new Date(),
            itemQty: 0,
            moneyReturn: 0,
            customerPay: 0,
            totalCustomerPay: 0,
            totalAmount: 0,
            reciverName: null,
            deliveryDate: new Date(),
            shippingFee: 0,
            phoneNumber: null,
            note: null,
            status: 1,
            voucher: null,
            typeBill: 1,
            cart: [],
            shippingFee: 0
    }];
    function makeBill() {
        return {
            code: 'HD' + Number(String(new Date().getTime()).slice(-6)),
            createdAt: new Date(),
            itemQty: 0,
            moneyReturn: 0,
            customerPay: 0,
            totalCustomerPay: 0,
            totalAmount: 0,
            reciverName: null,
            deliveryDate: new Date(),
            shippingFee: 0,
            phoneNumber: null,
            note: null,
            status: 1,
            voucher: null,
            typeBill: 1,
            cart: [],
            shippingFee: 0
        }
    }

    $scope.addBill = () => {
        if ($scope.bills.length >= 5) {
            toastr["warning"]("Chỉ được tạo tối đa 5 hóa đơn");
            return;
        }
        $scope.bills.push(makeBill());
        localStorage.setItem("bills", JSON.stringify($scope.bills));
    }

    
});