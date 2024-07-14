app.controller("tinh-bill-controller", function ($scope, $http) {
    $scope.product = [];
    $scope.bill = [];
    $scope.totalPrice = 0;
    $scope.idBill = null;
    $scope.selectedProductDetails = JSON.parse(localStorage.getItem('selectedProductDetails')) || [2];
    $scope.productSelected = $scope.selectedProductDetails.length > 0;

    //Product =================================================================

    $scope.detailProductBill = function (productDetail) {
        if (!$scope.idBill) {
            alert('Vui lòng chọn hóa đơn trước khi thêm sản phẩm.');
            return;
        }

        let billId = $scope.idBill.id;

        if (!$scope.selectedProductDetails[billId]) {
            $scope.selectedProductDetails[billId] = [];
        }

        // Tìm kiếm xem sản phẩm đã tồn tại trong danh sách chưa
        let existingProductDetail = $scope.selectedProductDetails[billId].find(item => item.id === productDetail.id);
        if (existingProductDetail) {
            // Nếu đã tồn tại, cập nhật quantity
            existingProductDetail.quantity += productDetail.inputQuantity;
        } else {
            // Nếu chưa tồn tại, thêm mới sản phẩm vào danh sách
            const selectedProduct = {
                id: productDetail.id,  // Đảm bảo lưu id từ cơ sở dữ liệu
                product: productDetail.product,
                color: productDetail.color,
                size: productDetail.size,
                quantity: productDetail.inputQuantity,
                remainingQuantity: productDetail.quantity - productDetail.inputQuantity,
                price: productDetail.product.price,
                image: productDetail.product.image
            };
            $scope.selectedProductDetails[billId].push(selectedProduct);
        }

        productDetail.quantity -= productDetail.inputQuantity;
        productDetail.remainingQuantity = productDetail.quantity;

        localStorage.setItem('selectedProductDetails', JSON.stringify($scope.selectedProductDetails));
        $scope.calculateTotalPrice();
        $scope.productSelected = true;
        console.log($scope.selectedProductDetails[$scope.idBill.id]);
    };



    $scope.hasSelectedProducts = false;

    $scope.calculateTotalPrice = function () {
        if ($scope.selectedProductDetails && $scope.selectedProductDetails[$scope.idBill.id]) {
            $scope.totalPrice = $scope.selectedProductDetails[$scope.idBill.id].reduce((sum, productDetail) => {
                return sum + (productDetail.price * productDetail.quantity);
            }, 0);
            $scope.hasSelectedProducts = true;
        } else {
            $scope.totalPrice = 0;
            $scope.hasSelectedProducts = false;
        }
    };

    $scope.removeProductDetail = function (index) {
        // Xóa phần tử được chọn khỏi mảng selectedProductDetails[idBill.id]
        $scope.selectedProductDetails[$scope.idBill.id].splice(index, 1);

        // Cập nhật lại dữ liệu trong localStorage
        localStorage.setItem('selectedProductDetails', JSON.stringify($scope.selectedProductDetails));

        // Tính toán lại totalPrice
        $scope.calculateTotalPrice();

        // Kiểm tra xem còn sản phẩm được chọn hay không để cập nhật biến productSelected
        $scope.productSelected = $scope.selectedProductDetails.length > 0;
    };

    $scope.validateQuantity = function (pdIn) {
        $scope.filterProductDetall.forEach(function (pd) {
            if (pd === pdIn) {
                if (pdIn.inputQuantity > pd.quantity) {
                    alert('Hết hàng');
                    pdIn.inputQuantity = pd.quantity;
                }
                pdIn.remainingQuantity = pd.quantity - pdIn.inputQuantity;
            }
        });
    };

    $scope.filterProductDetall = function () {
        $http.get(apiProduct).then(function (response) {
            $scope.product = response.data;
        });
    };

    $scope.filterProductDetall = [];
    $scope.totalPages = 0;
    $scope.currentPage = 0;
    $scope.desiredPage = 1;
    $scope.filters = {
        name: null,
        price: null,
    };

    $scope.getProductDetall = function (pageNumber) {
        let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
        $http.get("http://localhost:8080/api/admin/product-tinh/page", { params: params })
            .then(function (response) {
                $scope.filterProductDetall = response.data.content;
                $scope.filterProductDetall.forEach(function (productDetail) {
                    productDetail.inputQuantity = 1;
                    productDetail.remainingQuantity = productDetail.quantity;
                });
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = pageNumber;
                $scope.desiredPage = pageNumber + 1;
            });
    };

    $scope.applyFilters = function () {
        $scope.getProductDetall(0);
    };

    $scope.goToPage = function () {
        let pageNumber = $scope.desiredPage - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.getProductDetall(pageNumber);
        } else {
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };

    $scope.getProductDetall(0);

    //============================End ProductDetail================================================
    //============================Bill============================================================

    const apiBill = "http://localhost:8080/api/admin/bill-tinh";

    $scope.getAllBill = function () {
        $http.get(apiBill).then(function (response) {
            $scope.bill = response.data;
        });
    };
    $scope.getAllBill();

    $scope.createBill = function () {
        let databill = {
            createAt: new Date,
            status: 1,
        };
        $http.post(apiBill + "/save", databill).then(function (response) {
            // $scope.bills.unshift(response.data);
            $scope.getAllBill();
        });
    };
    // $scope.createBill();
    $scope.editBill = function (employee) {
        $scope.selectedBill = angular.copy(employee);
        $scope.selectedBill.createdAt = $scope.formatDateTime(employee.createdAt);
        $scope.idBill = angular.copy(employee);
        console.log('Edited bill id:', $scope.idBill);
    };

    $scope.formatDateTime = function (dateString) {
        var date = new Date(dateString);
        var day = String(date.getDate()).padStart(2, '0');
        var month = String(date.getMonth() + 1).padStart(2, '0');
        var year = date.getFullYear();
        var hours = String(date.getHours()).padStart(2, '0');
        var minutes = String(date.getMinutes()).padStart(2, '0');
        var seconds = String(date.getSeconds()).padStart(2, '0');
        return `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
    };
    //====================End Bill============================================
    //====================BillDetail============================================

    const apiBillDetail = "http://localhost:8080/api/admin/billdetail-tinh";

    $scope.createBillDetail = function () {
        if (!$scope.idBill) {
            alert('Vui lòng chọn hóa đơn trước khi tạo chi tiết hóa đơn.');
            return;
        }

        if (!$scope.selectedProductDetails[$scope.idBill.id] || $scope.selectedProductDetails[$scope.idBill.id].length === 0) {
            alert('Không có sản phẩm nào được chọn.');
            return;
        }

        $scope.selectedProductDetails[$scope.idBill.id].forEach(productDetail => {
            console.log("ID productDetail: " + productDetail.id); // Sử dụng id của productDetail từ cơ sở dữ liệu
            console.log("ID bill: " + $scope.idBill.id);

            let payload = {
                bill: { id: $scope.idBill.id },
                productDetail: { id: productDetail.id }, // Sử dụng id của productDetail
                quantity: productDetail.quantity,
                price: productDetail.price * productDetail.quantity,
                promotionalPrice: productDetail.promotionalPrice || 0, // Giá khuyến mãi nếu có
                status: 1 // Trạng thái
            };

            $http.post(apiBillDetail + "/save", payload).then(function (response) {
                console.log('Bill detail saved successfully', response.data);
            }).catch(function (error) {
                console.error('Error saving bill detail', error);
            });
        });
    };

    //====================End BillDetail============================================

});
