app.controller("tinh-bill-controller", function ($scope, $http) {
    $scope.product = [];
    $scope.bill = [];
    $scope.selectedProductDetail = $scope.selectedProductDetails = JSON.parse(localStorage.getItem('selectedProductDetails')) || [];



    //Product =================================================================
    const apiProduct = "http://localhost:8080/api/admin/product-tinh"


    $scope.detailProductBill = function (productDetail) {
        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách đã chọn hay chưa
        let existingProductDetail = $scope.selectedProductDetails.find(item => item.product.id === productDetail.product.id);

        if (existingProductDetail) {
            // Nếu tồn tại, cộng dồn số lượng
            existingProductDetail.quantity += productDetail.inputQuantity;
        } else {
            // Nếu không tồn tại, thêm sản phẩm mới vào danh sách đã chọn
            const selectedProduct = {
                product: productDetail.product,
                quantity: productDetail.inputQuantity,
                remainingQuantity: productDetail.quantity - productDetail.inputQuantity
            };
            $scope.selectedProductDetails.push(selectedProduct);
        }

        // Giảm số lượng tồn kho tương ứng với số lượng đã chọn
        productDetail.quantity -= productDetail.inputQuantity;
        productDetail.remainingQuantity = productDetail.quantity;

        // Lưu lại danh sách sản phẩm đã chọn vào localStorage
        localStorage.setItem('selectedProductDetails', JSON.stringify($scope.selectedProductDetails));
    };


    // Hàm xóa sản phẩm chi tiết khỏi selectedProductDetails
    $scope.removeProductDetail = function (index) {
        // Khôi phục lại số lượng sản phẩm trong danh sách gốc
        let removedProduct = $scope.selectedProductDetails[index];
        let originalProduct = $scope.filterProductDetall.find(item => item.product.id === removedProduct.product.id);
        if (originalProduct) {
            originalProduct.quantity += removedProduct.quantity;
            originalProduct.remainingQuantity = originalProduct.quantity;
        }

        // Xóa sản phẩm khỏi danh sách đã chọn
        $scope.selectedProductDetails.splice(index, 1);
        localStorage.setItem('selectedProductDetails', JSON.stringify($scope.selectedProductDetails));
    };

    // Hàm kiểm tra và điều chỉnh số lượng sản phẩm nhập vào
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



    //Phân trang lọc
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
        code: null,
        price: null,

    };

    $scope.getProductDetall = function (pageNumber) {
        let params = angular.extend({ pageNumber: pageNumber }, $scope.filters);
        $http
            .get("http://localhost:8080/api/admin/product-tinh/page", {
                params: params,
            })
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
            // Reset desiredPage to currentPage if the input is invalid
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };
    // Initial load
    $scope.getProductDetall(0);

    $scope.resetFilters = function () {
        $scope.filters = {
            name: null,
            code: null,
            price: null,
        };
        $scope.getProductDetall(0);
    };
    //End Product ===================================================================================================

    //Bill============================================================
    const apiBill = "http://localhost:8080/api/admin/bill-tinh"

    $scope.getAllBill = function () {
        $http.get(apiBill).then(function (response) {
            $scope.bill = response.data;
            // console.log(response.data);
        }
        )
    }
    $scope.getAllBill()

    $scope.createBill = function () {
        let databill = {
            createAt: new Date,
            status: 1,
        }
        $http.post(apiBill + "/save", databill).then(function (response) {
            $scope.bills.unshift(response.data);
            // $scope.getAllBill();
            // console.log(response.data);
        });
    }
    // EndBill============================================================
})