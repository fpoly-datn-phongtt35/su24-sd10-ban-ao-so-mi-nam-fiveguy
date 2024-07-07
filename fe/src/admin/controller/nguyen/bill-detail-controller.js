app.controller('nguyen-bill-detail-ctrl', function ($scope, $http, $rootScope, $routeParams) {

    const apiBill = "http://localhost:8080/api/admin/bill";
    const apiBillDetail = "http://localhost:8080/api/admin/billDetail";
    const apiBillHistory = "http://localhost:8080/api/admin/billHistory"

    const apiProduct = "http://localhost:8080/api/admin/product"
    const apiProductDetail = "http://localhost:8080/api/admin/productDetail"
    const apiProductProperty = "http://localhost:8080/api/admin/productProperty"

    // Hàm hiển thị thông báo thành công
    $scope.showSuccess = function (message) {
        toastr["success"](message);
    };
    // Hàm hiển thị thông báo lỗi
    $scope.showError = function (message) {
        toastr["error"](message);
    };
    $scope.showWarning = function (message) {
        toastr["warning"](message);
    };

    //idBill from the route
    $scope.idBill = $routeParams.idBill;
    console.log("Bill ID:", $scope.idBill);

    //data
    $scope.billResponse = {}

    //thông tin giao hàng
    $scope.updateBill = {}

    //history
    $scope.billHistoryUpdate = {}
    $scope.listBillHistory = []

    $scope.status = null

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

    //LẤY THÔNG TIN BILL ***************
    $scope.getBillById = function (id) {
        $http.get(apiBill + "/" + id).then(function (res) {
            console.log(res.data)
            $scope.billResponse = res.data
            $scope.status = res.data.status

            //updateBill sử dụng để cập nhật thông tin giao hàng - sử dụng angularcopy để tránh nó binding
            $scope.updateBill = angular.copy($scope.billResponse)
        })
    }
    $scope.getBillById($scope.idBill)

    //XỬ LÝ STATUS BILL, HISTORY BILL, THÔNG TIN GIAO HÀNG, LOGIC CỦA HIỂN THỊ TRẠNG THÁI ĐƠN HÀNG, paymentStatus
    // #region bill status & bill history
    $scope.showModalStatus = function (status) {
        $('#changeStatusModal').modal('show');
        $scope.currentStatus = status;
    };

    $scope.confirmChangeStatus = function () {
        $('#changeStatusModal').modal('hide');

        let bill = angular.copy($scope.billResponse)
        bill.status = $scope.currentStatus

        let billHistory = {
            billId: $scope.idBill,
            status: $scope.currentStatus,
            description: $scope.billHistoryUpdate.description,
        };

        let data = { bill: bill, billHistory: billHistory };

        $http.put(apiBill + "/billStatusUpdate/" + $scope.idBill, data).then(function (response) {
            $scope.showSuccess("Chuyển trạng thái thành công");
            // Cập nhật lại danh sách lịch sử
            $scope.getBillHistoryByBillId();
        }).catch(function (error) {
            $scope.showError("Chuyển trạng thái thất bại");
        });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
    };

    $scope.getBillHistoryByBillId = function () {
        $http.get(apiBillHistory + "/bill/" + $scope.idBill).then(function (res) {
            $scope.listBillHistory = res.data;
            $scope.updateStatusSteps();
        });
    };

    $scope.updateStatusSteps = function () {
        $scope.steps = [
            { status: 1, title: "Chờ xác nhận", icon: "schedule", time: null },
            { status: 2, title: "Đã xác nhận", icon: "check_circle", time: null },
            { status: 3, title: "Chờ vận chuyển", icon: "local_shipping", time: null },
            { status: 4, title: "Đang giao", icon: "directions_car", time: null },
            { status: 5, title: "Thành công", icon: "home", time: null },
            { status: 6, title: "Đã hủy", icon: "cancel", time: null }
        ];

        $scope.listBillHistory.forEach(function (history) {
            let step = $scope.steps.find(s => s.status === history.status);
            if (step) {
                step.time = history.createdAt;
            }
        });

        // Cập nhật lại trạng thái hiện tại
        $scope.status = Math.max.apply(Math, $scope.listBillHistory.map(function (o) { return o.status; }));
    };

    // Update thông tin giao hàng
    $scope.updateShipmentDetail = function () {
        let data = angular.copy($scope.updateBill)
        $http.put(apiBill + "/shipUpdate/" + $scope.idBill, data).then(function (res) {
            $scope.getBillById($scope.idBill)
            $('#editShipmentDetail').modal('hide');
            $scope.showSuccess("Cập nhật thông tin giao hàng thành công")

            $scope.getBillHistoryByBillId()
        })
    }

    $scope.listPaymentStatus = []

    //Hiển thị paymentStatus
    $scope.getAllPaymentStatus = function (billId) {
        $http.get(apiBill + "/" + billId + "/paymentStatus").then(function (res) {
            $scope.listPaymentStatus = res.data
            console.log(res.data);
        })
    }
    $scope.getAllPaymentStatus($scope.idBill)

    // #endregion

    //Lấy thông tin product property để filter và RANGE PRICE
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


    //FILTER, PAGINATION PRODUCT DETAIL
    // #region FILTER, PAGINATION PRODUCT DETAIL
    // $scope.productDetails = [];
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
                    pd.inputQuantity = 1; // Set default value to 0
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
    // #endregion

    //XỬ LÝ BILL DETAIL
    // #region hiển thị, thêm, xử lý số lượng, xóa sản phẩm ở billdetail

    //Tổng số lượng và tổng tiền
    $scope.billDetailSummary = {}

    $scope.getAllBillDetailByBillId = function (billId) {
        $http.get(apiBillDetail + "/getAllByBillId/" + billId).then(function (res) {
            $scope.billDetails = res.data
            console.log(res.data);
        })

        $http.get(apiBillDetail + "/" + billId + "/summary").then(function (res) {
            $scope.billDetailSummary = res.data
            // console.log(res.data);
        })
    }
    $scope.getAllBillDetailByBillId($scope.idBill)

    $scope.formBillDetail = {}

    //add product to bill
    $scope.addBillDetail = function (pdId, quantityInput, priceInput, pPriceInput, pd) {

        if (quantityInput >= pd.quantity) {
            $scope.showWarning("Thêm thất bại, sửa lại số lượng")
            return
        }

        let params = {
            productDetailId: pdId,
            quantity: quantityInput,
            price: priceInput,
            promotionalPrice: pPriceInput
        };
        $http({
            method: 'POST',
            url: apiBill + "/" + $scope.idBill + "/details",
            params: params
        }).then(function (res) {
            $scope.showSuccess("Thêm thành công");
            $scope.getAllBillDetailByBillId($scope.idBill);
            $scope.getProductDetails(0);
            $scope.productDetails.forEach(function (pd) {
                pd.inputQuantity = 1; // Set default value to 0
            });
        }, function (error) {
            console.error('Thêm sản phẩm thất bại:', error);
        });
    };

    $scope.validateQuantity = function (pdIn) {
        // console.log(pdIn.inputQuantity);
        $scope.productDetails.forEach(function (pd) {
            if (pd == pdIn && pdIn.inputQuantity >= pd.quantity) {
                pd.inputQuantity = pd.quantity - 1;
            }
        });
        // console.log(pdIn.inputQuantity);
    };

    //remove product in billdetail
    $scope.removeBillDetail = function (billDetailId) {
        $http.delete(apiBill + "/details/" + billDetailId).then(function (res) {
            $scope.showSuccess("Xóa thành công")
            $scope.getAllBillDetailByBillId($scope.idBill)
        }, function (error) {
            console.error('Xoá sản phẩm thất bại:', error);
        })
    }

    //Validate max quantity in dillDetail
    $scope.validateQuantityBd = function (bd, quantityNew) {

        //total quantity
        if (quantityNew >= bd.productDetail.quantity + bd.quantity) {
            bd.quantityNew = bd.productDetail.quantity + bd.quantity - 1;
        }
    };

    //update quantity in billDetail
    $scope.updateBillDetailQuantity = function (newQuantity, billDetail) {

        // $scope.billDetails.forEach(function (bd) {
        //     if (bd == billDetail && newQuantity > bd.productDetail.quantity) {
        //         bd.quantity = bd.productDetail.quantity - 1;
        //     }
        // });

        let params = {
            newQuantity: newQuantity
        };
        $http({
            method: 'PUT',
            url: apiBill + "/details/" + billDetail.id + "/quantity",
            params: params
        }).then(function (res) {
            $scope.showSuccess("Sửa số lượng thành công");
            $scope.getAllBillDetailByBillId($scope.idBill);
        }, function (error) {
            console.error('Sửa số lượng thất bại:', error);
        });
    };


    // #endregion


    $scope.steptest = [
        { status: 1, title: "Chờ xác nhận", icon: "schedule", time: null },
        { status: 2, title: "Đã xác nhận", icon: "check_circle", time: null },
        { status: 3, title: "Chờ vận chuyển", icon: "local_shipping", time: null },
        { status: 4, title: "Đang giao", icon: "directions_car", time: null },
        { status: 5, title: "Thành công", icon: "home", time: null },
        { status: 6, title: "Đã hủy", icon: "cancel", time: null },
        { status: 7, title: "Giao thất bại", icon: "home", time: null },
        { status: 8, title: "Đang giao lại", icon: "home", time: null }
    ];

    // #region IN HÓA ĐƠN

    $scope.exportBill = function () {
        let data = angular.copy($scope.billResponse)
        $scope.printBill(data)
    }

    $scope.printBill = resp => {
        const invoiceHTML = generateInvoiceHTML(resp);
        const invoiceWindow = window.open('', '_blank');
        invoiceWindow.document.write(invoiceHTML);
        invoiceWindow.document.close();
    }

    const formatCurrency = price => {
        return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(price);
    }

    const formatDate = dateString => {
        const options = { year: 'numeric', month: 'numeric', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' };
        return new Date(dateString).toLocaleString('vi-VN', options);
    };

    function generateInvoiceHTML(resp) {
        const listBillDT = Object.values($scope.billDetails).map(billDT => `
            <tr>
                <td class="desc">${billDT.productDetail.product.name} ${billDT.productDetail.color.name} ${billDT.productDetail.size.name}</td>
                <td class="unit">${billDT.price} - ${billDT.promotionalPrice}</td>
                <td class="qty">${billDT.quantity}</td>
                <td class="total">${formatCurrency(billDT.promotionalPrice * billDT.quantity)}</td>
            </tr>
        `).join('');
        const htmlContent = `<!DOCTYPE html>
    <html>
    <head>
        <style>
            .clearfix:after {
      content: "";
      display: table;
      clear: both;
    }
    
    a {
      color: #5D6975;
      text-decoration: underline;
    }
    
    body {
      position: relative;
      width: 21cm;  
      height: 20cm; 
      margin: 0 auto; 
      color: #001028;
      background: #FFFFFF; 
      font-family: Arial, sans-serif; 
      font-size: 12px; 
      font-family: Arial;
    }
    
    header {
      padding: 10px 0;
      margin-bottom: 30px;
    }
    
    #logo {
      text-align: center;
      margin-bottom: 10px;
    }
    
    #logo img {
      width: 90px;
    }
    
    h1 {
      border-top: 1px solid  #5D6975;
      border-bottom: 1px solid  #5D6975;
      color: #5D6975;
      font-size: 2.4em;
      line-height: 1.4em;
      font-weight: normal;
      text-align: center;
      margin: 0 0 20px 0;
      background: #F5F5F5;
    }
    
    #project {
      float: left;
    }
    
    #project span {
      color: #5D6975;
      text-align: right;
      width: 52px;
      margin-right: 10px;
      display: inline-block;
      font-size: 0.8em;
    }
    
    #company {
      float: right;
      text-align: right;
    }
    
    #project div,
    #company div {
      white-space: nowrap;        
    }
    
    table {
      width: 100%;
      border-collapse: collapse;
      border-spacing: 0;
      margin-bottom: 20px;
    }
    
    table tr:nth-child(2n-1) td {
      background: #F5F5F5;
    }
    
    table th,
    table td {
      text-align: center;
    }
    
    table th {
      padding: 5px 20px;
      color: #5D6975;
      border-bottom: 1px solid #C1CED9;
      white-space: nowrap;        
      font-weight: normal;
    }
    
    table .service,
    table .desc {
      text-align: left;
    }
    
    table td {
      padding: 20px;
      text-align: right;
    }
    
    table td.service,
    table td.desc {
      vertical-align: top;
    }
    
    table td.unit,
    table td.qty,
    table td.total {
      font-size: 1.2em;
    }
    
    table td.grand {
      border-top: 1px solid #5D6975;;
    }
    
    .foter {
      color: #5D6975;
      width: 100%;
      height: 30px;
      border-top: 1px solid #C1CED9;
      padding: 8px 0;
      text-align: center;
    }
    
    .font-b {
        font-weight: bold;
    }
        </style>    
    </head>
    <body>
         <header class="clearfix">
          <div id="logo">
            <img src="https://res.cloudinary.com/dvtz5mjdb/image/upload/v1701333412/image/h1vzhjzyuuwhrhak1bcr.png">
          </div>
          <h1>HÓA ĐƠN</h1>
          <div id="company" class="clearfix">
            <div>#${resp.code}</div>
            <div>Ngày tạo: ${formatDate(resp.createdAt)}</div>
            <div>Ngày thanh toán: ${formatDate(resp.paymentDate)}</div>
          </div>
          <div id="project">
            <div><span>Khách hàng:</span> ${resp.customerEntity ? resp.customerEntity.fullName : 'Khách lẻ'}</div>
            <div><span>SĐT:</span> ${resp ? resp.phoneNumber : ''}</div>
            <div><span>Địa chỉ:</span> ${resp ? resp.address : ''}</div>
          </div>
        </header>
        <main>
          <table>
            <thead>
              <tr>
                <th class="desc">Sản phẩm</th>
                <th>Đơn giá</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
              </tr>
            </thead>
            <tbody>
                ${listBillDT}
              <tr>
                <td colspan="3" class="font-b">Tổng tiền hàng: </td>
                <td class="total font-b">${formatCurrency(resp.totalAmount)}</td>
              </tr>
              ${resp.voucher !== null ? `
                <tr>
                <td colspan="3" class="font-b">Giảm giá</td>
                <td class="total font-b">${resp.voucher.value}${resp.voucher.valueType == 2 ? '%' : '₫'}</td>
                </tr>
              ` : ''}
              <tr>
                <td colspan="3" class="grand font-b">Tổng thanh toán</td>
                <td class="total grand font-b">${resp.totalAmountAfterDiscount == 0 ? formatCurrency(resp.totalAmount + resp.shippingFee) : formatCurrency(resp.totalAmountAfterDiscount + resp.shippingFee)}</td>
              </tr>
            </tbody>
          </table>
           <div class="foter">
                Cảm ơn và hẹn gặp lại!
            </div>
        </main>
        <script>
             window.onload = function() {
                  window.print();
             };
        </script>
    </body>
    </html>`;

        return htmlContent;
    }
    // #endregion
});