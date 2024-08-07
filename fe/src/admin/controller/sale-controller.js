app.controller('SaleController', ['$scope', '$http', '$routeParams', '$timeout', function($scope, $http, $routeParams,$timeout) {

  // notify
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
  $scope.showSuccessNotification = function(message) {
  toastr["success"](message);
  };
  
  // Hàm hiển thị thông báo lỗi
  $scope.showErrorNotification = function(message) {
    toastr["error"](message);
  };
  
  
  $scope.showWarningNotification = function(message) {
    toastr["warning"](message);
  };
    
    var baseUrl = 'http://localhost:8080/api/admin/sales';


    $scope.countCurrentSales = function() {
        $http.get(baseUrl + '/current/count').then(function(response) {
            $scope.currentSalesCount = response.data;
        });
    };

    $scope.countUpcomingSales = function() {
        $http.get(baseUrl + '/upcoming/count').then(function(response) {
            $scope.upcomingSalesCount = response.data;
        });
    };

    $scope.countExpiredSales = function() {
        $http.get(baseUrl + '/expired/count').then(function(response) {
            $scope.expiredSalesCount = response.data;
        });
    };


    var filterStartPicker = flatpickr("#filterStartTime", {
        enableTime: true,
        time_24hr: true,
        dateFormat: "d/m/Y H:i:S",
        onChange: function(selectedDates) {
            $timeout(function() {
                if (selectedDates.length > 0) {
                    var formattedStartDate = $scope.formatDate(selectedDates[0]);
                    $scope.filterStartDate = formattedStartDate;
                    $scope.startDateSale = selectedDates[0];
                } else {
                    $scope.filterStartDate = null;
                    $scope.startDateSale = null;
                }
            });
        }
    });
    
    var filterEndPicker = flatpickr("#filterEndTime", {
        enableTime: true,
        time_24hr: true,
        dateFormat: "d/m/Y H:i:S",
        onChange: function(selectedDates) {
            $timeout(function() {
                if (selectedDates.length > 0) {
                    var formattedEndDate = $scope.formatDate(selectedDates[0]);
                    $scope.filterEndDate = formattedEndDate;
                    $scope.endDateSale = selectedDates[0];
                } else {
                    $scope.filterEndDate = null;
                    $scope.endDateSale = null;
                }
            });
        }
    });

    $scope.refreshData = function() {
        $scope.startDateSale = null;
        $scope.endDateSale = null;
        $scope.status = null;
        $scope.discountType = null;
        $scope.searchTerm = null;
        $scope.currentPage = 0;
        $scope.filterStartDate = null;
        $scope.filterEndDate = null;
    
        $scope.getSalesByConditions(0);
    };
    
    $scope.startDateSale = null;
    $scope.endDateSale = null;
    $scope.status = null;
    $scope.discountType = null; 
    $scope.currentPage = 0;
    $scope.pageSize = 10;
    
    $scope.getSalesByConditions = function(page) {
        if (page === undefined) {
            page = $scope.currentPage;
        }
    
        var config = {
            params: {
                startDate: $scope.startDateSale,
                endDate: $scope.endDateSale,
                status: $scope.status,
                discountType: $scope.discountType, 
                searchTerm: $scope.searchTerm,
                page: page,
                size: $scope.pageSize
            }
        };
    
        $http.get(baseUrl + '/fill', config)
            .then(function(response) {
                // Check if the response has data
                if (response.data.content.length === 0 && page > 0) {
                    $scope.getSalesByConditions(0);
                } else {
                    $scope.sales = response.data.content;
                    $scope.totalPages = response.data.totalPages;
                    $scope.currentPage = page;
                    $scope.desiredPage = page + 1;
                }
            }, function(error) {
                console.error('Error fetching sales:', error);
            });
    };
    
    $scope.setCurrentPageSale = function(page) {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getSalesByConditions(page);
        }
    };

    $scope.desiredPage = 1;
    $scope.goToPage = function() {
        var page = $scope.desiredPage - 1; // Chuyển từ 1-based index sang 0-based index
        if (page >= 0 && page < $scope.totalPages) {
            $scope.getSalesByConditions(page);
        } else {
            // Nếu trang không hợp lệ, đặt lại desiredPage về trang hiện tại
            $scope.desiredPage = $scope.currentPage + 1;
        }
    };
    
    $scope.getSalesByConditions(0); 
    


    // DÙng để xử lí các thuộc tính của sale

    $scope.getStatusText = function(status) {
        switch (status) {
            case 1:
                return "Đang hoạt động";
            case 2:
                return "Sắp bắt đầu";
            case 3:
                return "Hết hạn";
            case 4:
                return "Dừng hoạt động";
            default:
                return "Không xác định"; 
        }
    };

    $scope.getStatusClass = function(status) {
        switch(status) {
            case 1:
                return 'ongoing';
            case 2:
                return 'upcoming';
            case 3:
                return 'expired';
            case 4:
                return 'stopped';
            default:
                return '';
        }
    };

    $scope.getDiscountValueAndType = function(value, type) {
        var typeText = '';
        if (type == 1) {
            typeText = 'đ';
        } else if (type == 2) {
            typeText = '%';
        } else {
            typeText = 'Không xác định';
        }
        return value + ' ' + typeText  ;
    };
    
      function parseDate(dateString) {
        if (!dateString) return null;
        return new Date(dateString);
    }
    // $scope.saleDetail.startDate = instance.formatDate(selectedDates[0], "Y-m-d H:i:S");


    

    
    
    



    $scope.getSale = function(saleId) {
        $http.get(baseUrl + '/' + saleId)
            .then(function(response) {
                var saleData = response.data;
                // Chuyển đổi startDate và endDate sang định dạng phù hợp
                // saleData.startDate = formatDate(saleData.startDate);
                // saleData.endDate = formatDate(saleData.endDate);

                $scope.displayStartDate = $scope.formatDate(saleData.startDate);
                $scope.displayEndDate = $scope.formatDate(saleData.endDate);

                $scope.saleDetail = saleData;
    
                // Lưu trạng thái nếu có vào localStorage
                if (saleData.status) {
                    localStorage.setItem('saleStatus', saleData.status);
                }
    
                // Cập nhật trạng thái
                $scope.updateStatus();
    
            }, function(error) {
                console.error('Error fetching sale details:', error);
            });
    };
    

    if ($routeParams.idSale) {
        $scope.getSale($routeParams.idSale);
    } else {
        $scope.saleDetail = {
            startDate: null,
            endDate: null
        };
    }

    $scope.updateStatus = function() {
        var sale = $scope.saleDetail;
        var today = new Date();
        var startDate = new Date(sale.startDate);
        var endDate = new Date(sale.endDate);

        $scope.statusOptions = [];

        if (sale) {
            // Luôn thêm trạng thái hiện tại lên đầu
            if (sale.status === 1) {
                $scope.statusOptions.push({ value: 1, label: 'Đang hoạt động' });
            } else if (sale.status === 2) {
                $scope.statusOptions.push({ value: 2, label: 'Sắp bắt đầu' });
            } else if (sale.status === 4) {
                $scope.statusOptions.push({ value: 4, label: 'Dừng hoạt động' });
            } else if (sale.status === 3) {
                $scope.statusOptions.push({ value: 3, label: 'Hết hạn' });
            }

            // Thêm các trạng thái khác dựa trên điều kiện
            if (sale.status === 1 || sale.status === 2) {
                $scope.statusOptions.push({ value: 4, label: 'Dừng hoạt động' });
            }

            if (sale.status === 4) {
                if (today >= startDate && today <= endDate) {
                    $scope.statusOptions.push({ value: 1, label: 'Đang hoạt động' });
                } else if (today < startDate) {
                    $scope.statusOptions.push({ value: 2, label: 'Sắp bắt đầu' });
                }
            }
        }
    };
    
    // Gọi hàm updateStatus() khi controller khởi tạo
    

    $scope.fetchAllSales = function() {
        $http.get(baseUrl)
            .then(function(response) {
                $scope.allSales = response.data;
            }, function(error) {
                console.error('Error fetching all sales:', error);
            });
    };
    
    $scope.fetchAllSales();
    


    $scope.checkSaleCodeUpdate = function(code) {
        var updatedSaleId = $routeParams.idSale.toString(); 
        return $scope.allSales.some(function(sale) {
            return sale.code === code && sale.id.toString() !== updatedSaleId; 
        });
    };


    $scope.updateDiscountTypeAdd = function () {
        if ($scope.saleDetail.discountType === '1' || $scope.saleDetail.discountType === 1) {
            $scope.saleDetail.maximumDiscountAmount = null;
        }
        $scope.checkDiscountValueAdd();
    };

    $scope.updateDiscountTypeUpdate = function () {
        if ($scope.saleDetail.discountType === '1' || $scope.saleDetail.discountType === 1) {
            $scope.saleDetail.maximumDiscountAmount = null;
        }
        $scope.checkDiscountValueUpdate();
    };

    
    $scope.checkDiscountValueUpdate = function () {
        if ($scope.saleDetail.discountType === '2' || $scope.saleDetail.discountType === 2) { 
            var isValid = $scope.saleDetail.value <= 100;
            $scope.saleFormUpdate.value.$setValidity('max', isValid); // Đánh dấu validity của input value

            return isValid;
        }

        $scope.saleFormUpdate.value.$setValidity('max', true); 
        return true;
    };
    

    $scope.checkDiscountValueAdd = function () {
        if ($scope.saleDetail.discountType === '2' || $scope.saleDetail.discountType === 2) { 
            var isValid = $scope.saleDetail.value <= 100;
            $scope.saleForm.value.$setValidity('max', isValid); 
            return isValid;
        }

        $scope.saleForm.value.$setValidity('max', true); 
        return true;
    };
    




    $scope.startDateError = '';
    $scope.endDateError = '';

    // Declare variables for flatpickr instances
    var startPicker, endPicker;

    // Function to update minimum end date based on start date
    function updateEndDateMinDate() {
        if ($scope.saleDetail.startDate) {
            var startDateObj = new Date($scope.saleDetail.startDate);
            var minEndDate = new Date(startDateObj.getTime() + 86400000); // startDate + 1 day (86400000 milliseconds)
            endPicker.set('minDate', minEndDate);
        } else {
            endPicker.set('minDate', new Date());
        }
    }

    // Function to validate start and end dates
    function validateDates() {
        $scope.startDateError = '';
        $scope.endDateError = '';
        $scope.saleCodeError = '';
    
        var startDateObj = $scope.saleDetail.startDate ? new Date($scope.saleDetail.startDate) : null;
        var endDateObj = $scope.saleDetail.endDate ? new Date($scope.saleDetail.endDate) : null;
        var saleCode = $scope.saleDetail.code;
    
        var minStartDate = new Date(new Date().getTime() + 60000); // Current time + 1 minute
    
        if (startDateObj && startDateObj < minStartDate) {
            $scope.startDateError = 'Ngày bắt đầu phải cách hiện tại ít nhất 1 phút';
        }
    
        if (endDateObj && startDateObj && endDateObj <= new Date(startDateObj.getTime() + 86400000)) {
            $scope.endDateError = 'Ngày kết thúc phải sau ngày bắt đầu ít nhất 1 ngày.';
        }
    
    
        updateEndDateMinDate();
    }
    
    
    // $scope.validateSaleCode = function() {
    //     var saleCode = $scope.saleDetail.code;
    
    //     if (saleCode && saleCode.length >= 6 && !$scope.checkSaleCodeUpdate(saleCode)) {
    //         // Valid sale code and not already taken
    //         $scope.saleCodeError = '';
    //     } else if (saleCode && saleCode.length < 6) {
    //         // Sale code too short
    //         $scope.saleCodeError = 'Mã giảm giá phải có ít nhất 6 ký tự.';
    //     } else if (saleCode && $scope.checkSaleCodeUpdate(saleCode)) {
    //         // Sale code already exists
    //         $scope.saleCodeError = 'Mã giảm giá đã tồn tại. Vui lòng chọn mã khác.';
    //     } else {
    //         $scope.saleCodeError = ''; // No error case
    //     }
    // };
    
    $scope.formatDate = function(dateString) {
        if (!dateString) return ''; // Xử lý trường hợp dateString không hợp lệ
    
        var date = new Date(dateString);
    
        if (isNaN(date.getTime())) return ''; // Xử lý trường hợp date không hợp lệ
    
        var day = ('0' + date.getDate()).slice(-2);
        var month = ('0' + (date.getMonth() + 1)).slice(-2); // Tháng bắt đầu từ 0
        var year = date.getFullYear();
        var hour = ('0' + date.getHours()).slice(-2);
        var minute = ('0' + date.getMinutes()).slice(-2);
        var second = ('0' + date.getSeconds()).slice(-2);
    
        // Định dạng chuỗi ngày tháng năm giờ phút giây
        var formattedDate = day + '/' + month + '/' + year + ' ' + hour + ':' + minute + ':' + second;
    
        return formattedDate;
    };
    
    // Initialize flatpickr instances inside $timeout to ensure DOM readiness
    $timeout(function() {
        var now = new Date();
    
        startPicker = flatpickr("#startTime", {
            enableTime: true,
            time_24hr: true,
            dateFormat: "d/m/Y H:i:S", // Định dạng mới dd/mm/yyyy HH:MM
            onChange: function(selectedDates, dateStr, instance) {
                $timeout(function() {
                    if (selectedDates.length > 0) {
                        var formattedStartDate = $scope.formatDate(selectedDates[0]);
                        $scope.displayStartDate = formattedStartDate; // Hiển thị định dạng ngày
                        $scope.saleDetail.startDate = selectedDates[0]; // Gán giá trị gốc
                        validateDates();
                    } else {
                        $scope.displayStartDate = null;
                        $scope.saleDetail.startDate = null;
                    }
                });
            }
        });
    
        endPicker = flatpickr("#endTime", {
            enableTime: true,
            time_24hr: true,
            dateFormat: "d/m/Y H:i:S", // Định dạng mới dd/mm/yyyy HH:MM
            onChange: function(selectedDates, dateStr, instance) {
                $timeout(function() {
                    if (selectedDates.length > 0) {
                        var formattedEndDate = $scope.formatDate(selectedDates[0]);
                        $scope.displayEndDate = formattedEndDate; // Hiển thị định dạng ngày
                        $scope.saleDetail.endDate = selectedDates[0]; // Gán giá trị gốc
                        validateDates();
                    } else {
                        $scope.displayEndDate = null;
                        $scope.saleDetail.endDate = null;
                    }
                });
            }
        });
    }, 0);
    

    // Function to clear form and reset errors
    $scope.clear = function() {
        $timeout(function() {
            $scope.saleDetail.startDate = null;
            $scope.saleDetail.endDate = null;
            if (startPicker) startPicker.clear();
            if (endPicker) endPicker.clear();
            $scope.startDateError = '';
            $scope.endDateError = '';
            updateEndDateMinDate();
        });
    };

    // Function to save sale data
    $scope.saveSale = function() {
        if ($scope.saleForm.$valid && $scope.isImageUploaded) {
    
            // Kiểm tra value chỉ khi discountType là 2
            if ($scope.saleDetail.discountType === 2 && $scope.saleDetail.value > 100) {
                $scope.saleForm.value.$setValidity('max', false); // Đánh dấu input value là không hợp lệ
                return;
            }
    
            var saleData = $scope.saleDetail;
            saleData.code = 'SALE' + Number(String(Date.now()).slice(-6));
            saleData.startDate = parseDate(saleData.startDate);
            saleData.endDate = parseDate(saleData.endDate);
            $http.post(baseUrl, saleData)
                .then(function(response) {
                    $('#saleModal').modal('hide');
                    $scope.showSuccessNotification("Thêm đợt giảm giá thành công");
                    $scope.fetchAllSales();
                })
                .catch(function(error) {
                    $scope.showErrorNotification("Thêm đợt giảm giá thất bại");
                    console.error('Error saving sale:', error);
                });
        } else {
            $scope.saleForm.$setSubmitted();
            $scope.showErrorNotification("Vui lòng điền đầy đủ thông tin.");
        }
    };
    
    
    

 // Hàm cập nhật sale
 $scope.updateSale = function() {
    if ($scope.saleFormUpdate.$valid ) {
        if ($scope.saleDetail.value >= 100 && $scope.saleDetail.discountType === 2) {
            $scope.saleFormUpdate.value.$setValidity('max', false); // Đánh dấu input value là không hợp lệ
            return;
        }

        var saleData = $scope.saleDetail;
        // Gửi yêu cầu cập nhật sale
        $http.post(baseUrl, saleData)
            .then(function(response) {
                $scope.showSuccessNotification("Cập nhật đợt giảm giá thành công");
                $scope.fetchAllSales();
            }, function(error) {
                $scope.showErrorNotification("Cập nhật đợt giảm giá thất bại");
                console.error('Error updating sale:', error);
            });
    } else {
        $scope.saleFormUpdate.$setSubmitted();
        $scope.showErrorNotification("Vui lòng điền đầy đủ thông tin.");
    }
};

    




    $scope.getAllProductSale = function() {
        var saleId = $routeParams.idSale;
        // Trả về promise
        return $http.get(baseUrl + '/product-sales/getList/' + saleId).then(function(response) {
            $scope.allProductSales = response.data;
            return response.data;
        }).catch(function(error) {
            console.error('Error fetching product sales:', error);
        });
    };


if (!localStorage.getItem('selectedProductSales')) {
    localStorage.setItem('selectedProductSales', JSON.stringify([]));
}

$scope.clearSelectedProductSales = function() {
    localStorage.removeItem('selectedProductSales');
    localStorage.removeItem('saleStatus');
};

$scope.toggleProductSaleSelection = function(productSale) {
    let selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];

    if (productSale.selected) {
        selectedProductSales.push(productSale);
    } else {
        selectedProductSales = selectedProductSales.filter(function(selectedProductSale) {
            return selectedProductSale.id !== productSale.id;
        });
    }

    localStorage.setItem('selectedProductSales', JSON.stringify(selectedProductSales));
};


$scope.refreshDataProductSale = function() {
    $scope.selectedCategory2 = null;
    $scope.selectedCollar2 = null;
    $scope.selectedWrist2 = null;
    $scope.selectedColor2 = null;
    $scope.selectedSize2 = null;
    $scope.selectedMaterial2 = null;
    $scope.currentPage2 = 0;
    $scope.searchTerm2 = null;

    $scope.filterProductSale(0);
};







$scope.productSales = [];
$scope.currentPage2 = 0;
$scope.pageSize2 = 10;
$scope.totalPages2 = 0;
$scope.searchTerm2 = null;

$scope.selectedProduct2 = null;
$scope.selectedCategory2 = null;
$scope.selectedCollar2 = null;
$scope.selectedWrist2 = null;
$scope.selectedColor2 = null;
$scope.selectedSize2 = null;
$scope.selectedMaterial2 = null;
$scope.selectedStatus2 = null;

$scope.filterProductSale = function(page) {
    if (page === undefined) {
        page = $scope.currentPage2;
    }
    var saleId = $routeParams.idSale ? parseInt($routeParams.idSale, 10) : null;

    if (isNaN(saleId)) {
        console.error('Invalid saleId:', saleId);
        return Promise.reject('Invalid saleId');
    }

    var config = {
        params: {
            saleId: saleId,
            productId: $scope.selectedProduct2,
            categoryId: $scope.selectedCategory2,
            collarId: $scope.selectedCollar2,
            wristId: $scope.selectedWrist2,
            colorId: $scope.selectedColor2,
            sizeId: $scope.selectedSize2,
            materialId: $scope.selectedMaterial2,
            status: $scope.selectedStatus2,
            page: page,
            size: $scope.pageSize2,
            searchTerm: $scope.searchTerm2
        }
    };

    return $http.get(baseUrl + '/product-sales/fillProductSale', config)
        .then(function(response) {
            if (response.data.content.length === 0 && page > 0) {
                return $scope.refreshDataProductSale(); // Recursive call to get the first page if the current page is empty
            } else {
                $scope.productSales = response.data.content;
                $scope.totalPages2 = response.data.totalPages;
                $scope.currentPage2 = page;
                $scope.desiredPageProductSale = page + 1;

                var selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];

                $scope.productSales.forEach(function(productSale) {
                    productSale.selected = selectedProductSales.some(function(selectedProductSale) {
                        return selectedProductSale.id === productSale.id;
                    });
                });

                return {
                    productSales: $scope.productSales,
                    totalPages: $scope.totalPages2
                }; // Return the productSales and totalPages to the caller
            }
        }).catch(function(error) {
            console.error('Error fetching product sales:', error);
            throw error; // Throw error to be caught by the caller
        });
};


$scope.setCurrentPageProductSales2 = function(page) {
    if (page >= 0 && page < $scope.totalPages2) {
        $scope.filterProductSale(page);
    }
};

$scope.desiredPageProductSale = 1;
$scope.goToPageProductSale = function() {
    var page = $scope.desiredPageProductSale - 1; // Chuyển từ 1-based index sang 0-based index
    if (page >= 0 && page < $scope.desiredPageProductSale) {
        $scope.filterProductSale(page);
    } else {
        // Nếu trang không hợp lệ, đặt lại desiredPage về trang hiện tại
        $scope.desiredPageProductSale = $scope.currentPage + 1;
    }
};



// Ensure selected product sales are saved to localStorage when toggled
$scope.toggleProductSelection = function(productSale) {
    let selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];

    if (productSale.selected) {
        selectedProductSales.push(productSale);
    } else {
        selectedProductSales = selectedProductSales.filter(function(selectedProductSale) {
            return selectedProductSale.id !== productSale.id;
        });
    }

    localStorage.setItem('selectedProductSales', JSON.stringify(selectedProductSales));
};

// Initialize the filtering process
// $scope.filterProductSale(0);
    
// Xóa sản phẩm ra khỏi đợt giảm giá \
   

$scope.deleteSelected = function() {
    $scope.getAllProductSale().then(function(allProductSales) {
        if (!allProductSales) return; 
        var selectedProductSales = JSON.parse(localStorage.getItem('selectedProductSales')) || [];
        var selectedIds = selectedProductSales.map(function(selectedProductSale) {
            return selectedProductSale.id;
        });
        $http.post(baseUrl + '/product-sales/deleteList', selectedIds).then(function(response) {
            $scope.productSales = $scope.productSales.filter(function(productSale) {
                return !productSale.selected;
            });
            $scope.allProductSales = $scope.allProductSales.filter(function(productSale) {
                return !selectedIds.includes(productSale.id);
            });
            $scope.refreshDataProductSale();
            $scope.refreshDataProduct();
            $scope.showSuccessNotification("Xóa sản phẩm thành công");
        }).catch(function(error) {
            $scope.showErrorNotification("Xóa sản phẩm thất bại");
            console.error('Error deleting selected product sales:', error);
        });
    });
};


$scope.deleteAll = function() {
    // Fetch all product sales to get their IDs
    $scope.fetchAllProductSales().then(function(allProductSales) {
        if (allProductSales && allProductSales.length > 0) {
            var selectedIds = allProductSales.map(function(productSale) {
                return productSale.id;
            });

            // Call the API to delete the list of selected product sales
            $http.post('http://localhost:8080/api/admin/sales/product-sales/deleteList', selectedIds)
                .then(function(response) {
                    $scope.productSales = [];
                    $scope.refreshDataProductSale();
                    $scope.refreshDataProduct();

                    $scope.showSuccessNotification("Xóa sản phẩm thành công");

                }).catch(function(error) {
                    $scope.showErrorNotification("Xóa sản phẩm thất bại");
                    console.error('Error deleting all product sales:', error);
                });
        } else {
            $scope.showErrorNotification("Không có sản phẩm để xóa");
        }
    }).catch(function(error) {
        $scope.showErrorNotification("Xóa sản phẩm thất bại");
        console.error('Error fetching product sales:', error);
    });
};



$scope.getFirstImagePath = function(productId) {
    $http.get('http://localhost:8080/api/admin/image/firstImagePath', {
        params: {
            productId: productId
        }
    }).then(function(response) {
        return response.data;
    }, function(error) {
        // Handle error if needed
        console.error('Error fetching first image path:', error);
    });
};




    
    $scope.countCurrentSales();
    $scope.countUpcomingSales();
    $scope.countExpiredSales();
    // $scope.getAllSales();


var baseUrlInfoProduct = 'http://localhost:8080/api/infoProduct';

    $scope.wrists = [];
    $scope.sizes = [];
    $scope.materials = [];
    $scope.colors = [];
    $scope.collars = [];
    $scope.brands = [];

    $scope.getAllWrists = function() {
        $http.get(baseUrlInfoProduct + '/wrists').then(function(response) {
            $scope.wrists = response.data;
        }, function(error) {
            console.error('Error fetching wrists:', error);
        });
    };

    $scope.getAllSizes = function() {
        $http.get(baseUrlInfoProduct + '/sizes').then(function(response) {
            $scope.sizes = response.data;
        }, function(error) {
            console.error('Error fetching sizes:', error);
        });
    };

    $scope.getAllMaterials = function() {
        $http.get(baseUrlInfoProduct + '/materials').then(function(response) {
            $scope.materials = response.data;
        }, function(error) {
            console.error('Error fetching materials:', error);
        });
    };

    $scope.getAllColors = function() {
        $http.get(baseUrlInfoProduct + '/colors').then(function(response) {
            $scope.colors = response.data;
        }, function(error) {
            console.error('Error fetching colors:', error);
        });
    };

    $scope.getAllCollars = function() {
        $http.get(baseUrlInfoProduct + '/collars').then(function(response) {
            $scope.collars = response.data;
        }, function(error) {
            console.error('Error fetching collars:', error);
        });
    };

    $scope.getAllBrands = function() {
        $http.get(baseUrlInfoProduct + '/brands').then(function(response) {
            $scope.brands = response.data;
        }, function(error) {
            console.error('Error fetching brands:', error);
        });
    };




    $scope.getAllWrists();
    $scope.getAllSizes();
    $scope.getAllMaterials();
    $scope.getAllColors();
    $scope.getAllCollars();
    $scope.getAllBrands();



    function calculateDiscount(sale, product) {
        var discount = 0;
    
        // Giảm giá cố định
        if (sale.discountType === 1) {
            discount = sale.value;
        } 
        // Giảm giá phần trăm
        else if (sale.discountType === 2) {
            discount = product.price * (sale.value / 100);
            if (sale.maximumDiscountAmount && discount > sale.maximumDiscountAmount) {
                discount = sale.maximumDiscountAmount;
            }
        }
        return discount;
    }
    

    // Thêm sản phẩm  đợt giảm giá \

    $scope.getAllProduct = function() {
        return $http.get('http://localhost:8080/api/admin/sales/products')
            .then(function(response) {
                return response.data;
            })
            .catch(function(error) {
                console.error('Error fetching products:', error);
            });
    };
    
    $scope.addSelectedProductSales = function() {

        var sale = $scope.saleDetail;
        $scope.getAllProduct().then(function(allProducts) {
            var selectedProducts = JSON.parse(localStorage.getItem('selectedProducts')) || [];
            var selectedProductsFromAll = allProducts.filter(function(product) {
                return selectedProducts.some(function(selectedProduct) {
                    return selectedProduct.id === product.id;
                });
            }).map(function(product) {
                var discount = calculateDiscount(sale, product);
                var promotionalPrice = product.price - discount;
                return {
                    product: product,
                    sale: sale,
                    promotionalPrice: promotionalPrice,
                    discountPrice: discount
                };
            });
    
            if (selectedProductsFromAll.length > 0) {
                $http.post(baseUrl + '/product-sales/addList', selectedProductsFromAll).then(function(response) {
                    response.data.forEach(function(newProductSale) {
                        $scope.productSales.push(newProductSale);
                    });
                    $scope.refreshDataProductSale();
                    $scope.refreshDataProduct();
                    $scope.showSuccessNotification("Thêm sản phẩm thành công")
                }).catch(function(error) {
                    $scope.showErrorNotification("Thêm sản phẩm thất bại")
                    console.error('Error adding selected product sales:', error);
                });
            }
        });
    };

        // Thêm tất cả sản phẩm vào đợt giảm giá

$scope.fetchAllProducts = function() {
    var allProducts = [];
    var fetchPage = function(page) {
        return $scope.filterProducts(page).then(function(result) {
            allProducts = allProducts.concat(result.products);
            if (page < result.totalPages - 1) {
                return fetchPage(page + 1); // Recursive call to fetch the next page
            } else {
                return allProducts; // Return all collected products
            }
        }).catch(function(error) {
            console.error('Error in fetchPage:', error);
            throw error; // Propagate error
        });
    };
    return fetchPage(0); // Start fetching from the first page
};


$scope.fetchAllProductSales = function() {
    var allProductSales = [];
    var fetchPage = function(page) {
        return $scope.filterProductSale(page).then(function(result) {
            allProductSales = allProductSales.concat(result.productSales);
            if (page < result.totalPages - 1) {
                return fetchPage(page + 1); // Recursive call to fetch the next page
            } else {
                return allProductSales; // Return all collected product sales
            }
        }).catch(function(error) {
            console.error('Error in fetchPage:', error);
            throw error; // Propagate error
        });
    };
    return fetchPage(0); // Start fetching from the first page
};




$scope.addAllProductSales = function() {



    $scope.fetchAllProducts().then(function(allProducts) {
        if (allProducts && allProducts.length > 0) {
        var sale = $scope.saleDetail;
            var selectedProductsFromAll = allProducts.map(function(product) {
                var discount = calculateDiscount(sale, product); // Assuming calculateDiscount is defined
                var promotionalPrice = product.price - discount;
                return {
                    product: product,
                    sale: sale,
                    promotionalPrice: promotionalPrice,
                    discountPrice: discount
                };
            });
            // Call the API to add product sales
            $http.post('http://localhost:8080/api/admin/sales/product-sales/addList', selectedProductsFromAll)
                .then(function(response) {
                    $scope.refreshDataProductSale();
                    $scope.refreshDataProduct();

                    $scope.showSuccessNotification("Thêm sản phẩm thành công")
                }).catch(function(error) {
                    $scope.showErrorNotification("Thêm sản phẩm thất bại");
                    console.error('Error adding product sales:', error);
                });
        } else {
            $scope.showErrorNotification("Không có sản phẩm để thêm");
        }
    }).catch(function(error) {
        $scope.showErrorNotification("Thêm sản phẩm thất bại");
        console.error('Error fetching products:', error);
    });
};




    
    if (!localStorage.getItem('selectedProducts')) {
        localStorage.setItem('selectedProducts', JSON.stringify([]));
    }
    
    $scope.clearSelectedProducts = function() {
        localStorage.removeItem('selectedProducts');
    };
    
    $scope.toggleProductSelection = function(product) {
        let selectedProducts = JSON.parse(localStorage.getItem('selectedProducts')) || [];
    
        if (product.selected) {
            selectedProducts.push(product);
        } else {
            selectedProducts = selectedProducts.filter(function(selectedProduct) {
                return selectedProduct.id !== product.id;
            });
        }
    
        localStorage.setItem('selectedProducts', JSON.stringify(selectedProducts));
    };
    

    $scope.refreshDataProduct = function() {
        $scope.selectedCategory = null;
        $scope.selectedCollar = null;
        $scope.selectedWrist = null;
        $scope.selectedColor = null;
        $scope.selectedSize = null;
        $scope.selectedMaterial = null;
        $scope.currentPage = 0;
        $scope.searchTerm = null;


        $scope.filterProducts(0);
    };

    $scope.selectedCategory = null;
    $scope.selectedCollar = null;
    $scope.selectedWrist = null;
    $scope.selectedColor = null;
    $scope.selectedSize = null;
    $scope.selectedMaterial = null;

    $scope.filterProducts = function(page) {
        if (page === undefined) {
            page = $scope.currentPage;
        }
    
        var config = {
            params: {
                categoryId: $scope.selectedCategory,
                collarId: $scope.selectedCollar,
                wristId: $scope.selectedWrist,
                colorId: $scope.selectedColor,
                sizeId: $scope.selectedSize,
                materialId: $scope.selectedMaterial,
                searchTerm: $scope.searchTerm,
                page: page,
                size: $scope.pageSize
            }
        };
    
        return $http.get('http://localhost:8080/api/admin/sales/products/filter', config)
            .then(function(response) {
                if (response.data.content.length === 0 && page > 0) {
                    return $scope.filterProducts(0); // Recursive call to get the first page if the current page is empty
                } else {
                    $scope.products = response.data.content;
                    $scope.totalPagesProduct = response.data.totalPages;
                    $scope.currentPage = page;
                    $scope.desiredPageProduct = page + 1;
                    let selectedProducts = JSON.parse(localStorage.getItem('selectedProducts')) || [];
    
                    $scope.products.forEach(function(product) {
                        var selectedProduct = selectedProducts.find(function(selectedProduct) {
                            return selectedProduct.id === product.id;
                        });
                        product.selected = !!selectedProduct;
                    });
    
                    return {
                        products: $scope.products,
                        totalPages: $scope.totalPagesProduct
                    }; // Return the products and totalPages to the caller
                }
            }).catch(function(error) {
                console.error('Error fetching products:', error);
                throw error; // Throw error to be caught by the caller
            });
    };
    
    


    $scope.setCurrentPageRateProduct = function(page) {
        if (page >= 0 && page < $scope.totalPagesProduct) {
            $scope.filterProducts(page);
        }
    };


    $scope.desiredPageProduct = 1;
    $scope.goToPageProduct = function() {
        var page = $scope.desiredPageProduct - 1; // Chuyển từ 1-based index sang 0-based index
        if (page >= 0 && page < $scope.totalPagesProduct) {
            $scope.filterProducts(page);
        } else {
            // Nếu trang không hợp lệ, đặt lại desiredPage về trang hiện tại
            $scope.desiredPageProduct = $scope.currentPage + 1;
        }
    };
    



    // $scope.filterProducts(0);
    

    // $scope.searchProducts = function() {
    //     $http.get('http://localhost:8080/api/admin/sales/products/search', {
    //         params: {
    //             searchTerm: $scope.searchTerm
    //         }
    //     }).then(function(response) {
    //         // Xử lý dữ liệu trả về từ API
    //         $scope.products = response.data;
    //     }, function(error) {
    //         // Xử lý lỗi nếu có
    //         console.error('Error searching products:', error);
    //     });
    // };



    
    // formatCurrency
    $scope.formatCurrency = function(value) {
        if (!value) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    };


    $scope.getSaleStatusFromLocal = function() {
        return localStorage.getItem('saleStatus');
    };


    // summary sale

    $scope.getSaleSummary = function(saleId) {

        $http.get(baseUrl + '/summary/' + saleId)
            .then(function(response) {
                $scope.summary = response.data;
                // $scope.summary.profitMargin = ($scope.summary.totalProfit / $scope.summary.totalRevenue) * 100;
            })
            .catch(function(error) {
                console.error('Error fetching sale summary:', error);
            });
    };

    $scope.staticCustomers = [];

    $scope.getCustomerDetailsBySaleId = function(saleId) {
        $http.get(baseUrl + '/staticCustomer/' + saleId )
            .then(function(response) {
                $scope.staticCustomers = response.data;
                // Các xử lý dữ liệu khác nếu cần thiết
            })
            .catch(function(error) {
                console.error('Error fetching customer details:', error);
            });
    };

    $scope.getCustomerDetails = function(saleId, customerId) {
        $http.get(baseUrl + '/customerDetails', {
            params: { saleId: saleId, customerId: customerId }
        })
        .then(function(response) {
            $scope.selectedCustomerProducts = response.data; // Assign data to variable
            console.log($scope.selectedCustomerProducts);
        })
        .catch(function(error) {
            console.error('Error fetching customer details:', error);
        });
    };
    
    $scope.showCustomerDetails = function(customer, index) {
        if ($scope.selectedCustomerIndex === index) {
            $scope.selectedCustomerProducts = []; // Close details if already open
            $scope.selectedCustomerIndex = -1;
        } else {
            $scope.selectedCustomerIndex = index; // Save the index of the selected customer
            // Call API to get customer product details
            $scope.getCustomerDetails(customer.saleId, customer.customerId);
        }
    };
    
    


    $scope.showSaleDetails = function(sale) {
        $scope.saleInfo = sale;
        $scope.getSaleSummary(sale.id);
        $scope.getCustomerDetailsBySaleId(sale.id);
        $('#staticSale').modal('show');
    };

    // $scope.getTotalNumberOfPurchases = function() {
    //     if (!Array.isArray($scope.staticCustomers)) {
    //         return 0;
    //     }
    //     return $scope.staticCustomers.reduce(function(total, customer) {
    //         return total + customer.numberOfPurchases;
    //     }, 0);
    // };
    
    // $scope.getTotalAmountBeforeDiscount = function() {
    //     if (!Array.isArray($scope.staticCustomers)) {
    //         return 0;
    //     }
    //     return $scope.staticCustomers.reduce(function(total, customer) {
    //         return total + customer.totalAmountBeforeDiscount;
    //     }, 0);
    // };
    
    // $scope.getTotalAmountAfterDiscount = function() {
    //     if (!Array.isArray($scope.staticCustomers)) {
    //         return 0;
    //     }
    //     return $scope.staticCustomers.reduce(function(total, customer) {
    //         return total + customer.totalAmountAfterDiscount;
    //     }, 0);
    // };
    
    // $scope.getTotalDiscountAmount = function() {
    //     if (!Array.isArray($scope.staticCustomers)) {
    //         return 0;
    //     }
    //     return $scope.staticCustomers.reduce(function(total, customer) {
    //         return total + customer.totalDiscountAmount;
    //     }, 0);
    // };
    
    

    $scope.imagePreview = null;
    $scope.showError = false;
    $scope.alertErrorImg = "Lỗi chưa xác định";
    $scope.fileInput = null;
    $scope.uploadedImagePath = ""; // Variable to store the uploaded image path
    $scope.isImageUploaded = false; // Variable to check if image is uploaded
    
    $scope.handleImageChange = function () {
        let fileInputc = document.getElementById("image-update");
        let file2 = fileInputc.files[0];
        console.log(file2);
    
        if (file2) {
            $scope.imagePreview = URL.createObjectURL(file2);
            $scope.isImageUploaded = false; // Reset the flag
            // You can upload the image immediately by calling the uploadImage function
            $scope.uploadImage(file2);
        }
    };
    
    $scope.uploadImage = function(file) {
        var data = new FormData();
        data.append("file", file);
    
        $http.post("http://localhost:8080/api/rest/upload", data, {
            transformRequest: angular.identity,
            headers: { "Content-Type": undefined }
        })
        .then(function(resp) {
            $scope.uploadedImagePath = resp.data.name;
            $scope.saleDetail.path = $scope.uploadedImagePath;
            $scope.isImageUploaded = true; // Set the flag to true after successful upload
        })
        .catch(function(error) {
            console.log("Error uploading image", error);
            $scope.saleForm.saleImage.$setValidity('serverError', false); // Set validity for custom error display
        });
    };
    
    
    



}]);







// console.log("Check")
