app.controller('nguyen-sell-ctrl', function ($scope, $http, $routeParams, $location, $interval) {

    const apiBillSell = "http://localhost:8080/api/admin/sell"

    const apiBill = "http://localhost:8080/api/admin/bill";
    const apiBillDetail = "http://localhost:8080/api/admin/billDetail";
    const apiBillHistory = "http://localhost:8080/api/admin/billHistory"

    const apiReturnOrder = "http://localhost:8080/api/admin/returnOrder";

    const apiProduct = "http://localhost:8080/api/admin/product"
    const apiProductDetail = "http://localhost:8080/api/admin/productDetail"
    const apiProductProperty = "http://localhost:8080/api/admin/productProperty"


    const apiVoucher = "http://localhost:8080/api/admin/voucher"


    $scope.bills = [];
    $scope.currentBillIndex = -1;


    //product
    $scope.productDetail = {}
    $scope.productDetails = []

    //billDetail
    $scope.billDetails = []

    //product property for filter
    $scope.categories = []
    $scope.materials = []
    $scope.collars = []
    $scope.wrists = []
    $scope.colors = []
    $scope.sizes = []

    $scope.priceRange = {
        maxPrice: null,
        minPrice: null
    }

    // Class Bill dựa trên entity Java
    function Bill() {
        this.id = null;
        this.code = '';
        this.reciverName = '';
        this.deliveryDate = new Date();
        this.shippingFee = 0;
        this.transId = '';
        this.addressId = '';
        this.address = '';
        this.phoneNumber = '';
        this.totalAmount = 0;
        this.totalAmountAfterDiscount = 0;
        this.paidAmount = 0;
        this.paidShippingFee = 0;
        this.createdAt = new Date();
        this.customer = null;
        this.employee = null;
        this.paymentMethod = null;
        this.voucher = null;
        this.typeBill = 0;
        this.reason = 0;
        this.note = '';
        this.status = 20;
        this.items = [];  // Thêm mảng items để lưu các mặt hàng trong hóa đơn
    }

    $scope.createNewBill = function () {
        var newBill = new Bill();

        // Gửi yêu cầu tạo hóa đơn mới đến backend
        $http.post(apiBillSell + "/createBill", newBill).then(function (response) {
            $scope.bills.push(response.data);
            $scope.currentBillIndex = $scope.bills.length - 1;
        }, function (error) {
            console.error('Error creating bill:', error);
        });
    };

    $scope.removeBill = function (index) {
        var billId = $scope.bills[index].id;
        $http.delete(apiBill + '/' + billId).then(function (response) {
            if (index === $scope.currentBillIndex) {
                if (index > 0) {
                    $scope.currentBillIndex = index - 1; // Chuyển sang tab bên trái
                } else if (index < $scope.bills.length - 1) {
                    $scope.currentBillIndex = index; // Chuyển sang tab bên phải
                } else {
                    $scope.currentBillIndex = -1; // Không còn hóa đơn nào
                }
            } else if (index < $scope.currentBillIndex) {
                $scope.currentBillIndex = $scope.bills.length - 1;
            }

            $scope.bills.splice(index, 1);
            if ($scope.bills.length === 0) {
                $scope.createNewBill();
            }
        }, function (error) {
            console.error('Error deleting bill:', error);
        });
    };

    $scope.removeItem = function (index) {
        $scope.bills[$scope.currentBillIndex].items.splice(index, 1);
        $scope.calculateTotal();
        $scope.updateBill($scope.bills[$scope.currentBillIndex]);
    };

    $scope.calculateTotal = function () {
        var total = 0;
        $scope.bills[$scope.currentBillIndex].items.forEach(function (item) {
            total += item.quantity * item.price;
        });
        $scope.bills[$scope.currentBillIndex].totalAmount = total;
    };

    $scope.updateBill = function (bill) {
        $http.put(apiBill + '/' + bill.id, bill).then(function (response) {
            console.log('Bill updated:', response.data);
        }, function (error) {
            console.error('Error updating bill:', error);
        });
    };

    function init() {
        // Load bills from the server
        $http.get(apiBill).then(function (response) {
            $scope.bills = response.data;
            if ($scope.bills.length > 0) {
                $scope.currentBillIndex = 0;
            } else {
                $scope.createNewBill();
            }
        }, function (error) {
            console.error('Error loading bills:', error);
        });
    }

    // Kiểm tra và xóa hóa đơn vào 0h hàng ngày
    $interval(function () {
        var now = new Date();
        if (now.getHours() === 0 && now.getMinutes() === 0) {
            $scope.bills = [];
            $scope.createNewBill();
        }
    }, 60000);  // Kiểm tra mỗi phút

    // Gọi hàm init() để khởi tạo
    init();




    // #region product property filter & price range
    $scope.getAllProductProperty = function () {
        $http.get(apiProductProperty + "/all").then(function (res) {
            console.log(res.data)

            $scope.categories = res.data.categories
            $scope.materials = res.data.materials
            $scope.collars = res.data.collars
            $scope.wrists = res.data.wrists
            $scope.colors = res.data.colors
            $scope.sizes = res.data.sizes
        });

        $http.get(apiProduct + "/maxPrice").then(function (res) {
            $scope.priceRange.maxPrice = res.data;
            return $http.get(apiProduct + "/minPrice");
        }).then(function (res) {
            $scope.priceRange.minPrice = res.data;
            // console.log($scope.priceRange.minPrice);

            // Call the slider initialization function
            $scope.initSlider(); // Initialize the slider only after both prices are fetched
        });
    }


    //Range lọc giá
    $scope.initSlider = function () {
        var slider = document.getElementById('slider');
        noUiSlider.create(slider, {
            start: [$scope.priceRange.minPrice, $scope.priceRange.maxPrice],
            tooltips: [
                wNumb({
                    decimals: 0,
                    thousand: ','
                }),
                wNumb({
                    decimals: 0,
                    thousand: ','
                }),
            ],
            step: 10000,
            connect: true,
            range: {
                'min': $scope.priceRange.minPrice,
                'max': $scope.priceRange.maxPrice
            },
            pips: {
                mode: 'steps',
                density: 5,
                format: wNumb({
                    decimals: 0,
                    thousand: ','
                })
            }
        });

        // Example of updating AngularJS model on slider change
        // slider.noUiSlider.on('update', function (values, handle) {
        //     $scope.filters.minPrice = parseInt(values[0]);
        //     $scope.filters.maxPrice = parseInt(values[1]);
        //     $scope.$applyAsync(); // Apply changes to AngularJS model
        // });
    };

    //Gọi ở đây để hiẻn thị được pricerange
    $scope.getAllProductProperty();
    // #endregion

    //#region productDetail


    $scope.currentPage = 0;
    $scope.pageSize = 5;
    $scope.totalPages = 0;
    $scope.filters = {
        productName: null,
        categoryId: null,
        materialId: null,
        wristId: null,
        collarId: null,
        colorId: null,
        sizeId: null,
        minPrice: null,
        maxPrice: null
    };
    $scope.desiredPage = 1;

    $scope.getProductDetails = function (pageNumber) {
        let params = {
            productName: $scope.filters.productName,
            categoryId: $scope.filters.categoryId,
            materialId: $scope.filters.materialId,
            wristId: $scope.filters.wristId,
            collarId: $scope.filters.collarId,
            sizeId: $scope.filters.sizeId,
            colorId: $scope.filters.colorId,
            minPrice: $scope.filters.minPrice,
            maxPrice: $scope.filters.maxPrice,
            page: pageNumber,
            size: $scope.pageSize
        };
        $http.get(apiProductDetail + "/page", { params: params })
            .then(function (response) {
                $scope.productDetails = response.data.content;
                $scope.totalElements = response.data.totalElements;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = pageNumber;
                $scope.pages = Array.from(Array($scope.totalPages).keys());

                $scope.desiredPage = pageNumber + 1;

                // Initialize inputQuantity for each productDetail
                $scope.productDetails.forEach(function (pd) {
                    pd.inputQuantity = 1; // Set default value to 1
                });

            }, function (error) {
                console.error('Error fetching products:', error);
            });
    };

    // Initial load
    $scope.getProductDetails(0);

    $scope.applyFilters = function () {

        var slider = document.getElementById('slider');
        slider.noUiSlider.on('update', function (values, handle) {
            $scope.filters.minPrice = parseInt(values[0]);
            $scope.filters.maxPrice = parseInt(values[1]);
            $scope.$applyAsync(); // Apply changes to AngularJS model
        });

        $scope.getProductDetails(0);
    };

    $scope.goToPage = function () {
        let pageNumber = $scope.desiredPage - 1;
        if (pageNumber >= 0 && pageNumber < $scope.totalPages) {
            $scope.loadBills($scope.currentTab, pageNumber);
        } else {
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };

    $scope.loadPage = function (page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getProductDetails(page);
        }
    };

    $scope.resetFilters = function () {
        $scope.filters = {
            productName: null,
            categoryId: null,
            materialId: null,
            wristId: null,
            collarId: null,
            colorId: null,
            sizeId: null,
            minPrice: null,
            maxPrice: null
        };
        var slider = document.getElementById('slider');
        slider.noUiSlider.reset();
        $scope.getProductDetails(0);
    };

    //#endregion
});