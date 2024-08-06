app.controller("orderController", function ($scope, $http, $window,$routeParams,$rootScope,$location,$timeout) {

    const apiBillHistory = "http://localhost:8080/api/home/bill-history"
    
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

    $scope.idBill = $routeParams.idBill;
    
// Lấy list bill

$scope.searchPhoneNumber = null
$scope.searchText =  "";
// $scope.searchText2 =  "";
$scope.currentPage = 0; // Biến để lưu trữ trang hiện tại
$scope.pageSize = 10; // Biến để lưu trữ kích thước trang
$scope.totalPages = 0; // Biến để lưu trữ tổng số trang
$scope.totalPagesB2 = 0; // Biến để lưu trữ tổng số trang
$scope.totalPages = 0; // Biến để lưu trữ tổng số trang


// check order by phoneNumber
$scope.phoneNumberCheckOrder = ''; 
$scope.isPhoneNumberCheckOrder = false;


$scope.loadBillsByPhoneNumber = function(page) {
    if (page === undefined) {
        page = $scope.currentPage || 0;
    }
    $scope.isPhoneNumberCheckOrder = !isValidPhoneNumber($scope.searchPhoneNumber);
    if ( $scope.isPhoneNumberCheckOrder) {
        return;
      }
    var config = {
        params: {
            phoneNumber: $scope.searchPhoneNumber,
            search: $scope.searchText2,
            page: page,
            size: $scope.pageSize
        }
    };


    return $http.get('http://localhost:8080/api/home/order/phone', config)
        .then(function(response) {
            if (response.data.content.length === 0 && page > 0) {
                return $scope.loadBillsByPhoneNumber(0); // Gọi lại để lấy trang đầu tiên nếu trang hiện tại rỗng
            } else {
                $scope.billsByPhoneNumber = response.data.content;
                $scope.totalPagesB2 = response.data.totalPages;
                $scope.currentPage = page;
                $scope.desiredPage2 = page + 1;
            }
        }).catch(function(error) {
            console.error('Error fetching bills:', error);
            throw error; // Ném lỗi để caller bắt
        });
};


// Hàm để thay đổi trang hiện tại
$scope.setCurrentPageBillByPhoneNumber = function(page) {
    if (page >= 0 && page < $scope.totalPagesB2) {
        $scope.loadBillsByPhoneNumber(page);
    }
};

// Hàm để đi đến trang cụ thể
$scope.goToPageByPhoneNumber = function() {

    var page = $scope.desiredPage2 - 1; // Chuyển từ 1-based index sang 0-based index
    if (page >= 0 && page < $scope.totalPagesB2) {
        $scope.loadBillsByPhoneNumber(page);
    } else {
        // Nếu trang không hợp lệ, đặt lại desiredPage về trang hiện tại
        $scope.desiredPage2 = $scope.currentPage + 1;
        console.log("Invalid Page. Resetting desiredPage2 to:", $scope.desiredPage2);
    }
};
$scope.refreshDataBillPhoneNumber = function() {
    $scope.searchText =  "";
    $scope.currentPage = 0; 
    $scope.pageSize = 10; 
    $scope.totalPagesB2 = 0; 
    $scope.loadBillsByPhoneNumber(0);

}

$scope.refreshDataBillCustomer = function() {
    $scope.searchText =  "";
    $scope.currentPage = 0; // Biến để lưu trữ trang hiện tại
    $scope.pageSize = 10; // Biến để lưu trữ kích thước trang
    $scope.totalPages = 0; 
    $scope.loadBillsForCustomer(0);
}

$scope.desiredPage3 = 1;

$scope.loadBillsForCustomer = function(page) {
    var params = {
        search: $scope.searchText || "",
        page: page,
        size: $scope.pageSize
    };
    
    console.log("Request Params:", params);

    $http.get('http://localhost:8080/api/home/order/customer', {
        params: params
    }).then(function(response) {
        if (response.data) {
            $scope.bill = response.data.content;
            $scope.totalPages = response.data.totalPages;

            $scope.currentPage = page;
            $scope.desiredPage3 = page + 1;
        }
    }).catch(function(error) {
        alert("Có lỗi xảy ra khi gọi API!");
        console.error(error);
    });
};


// Gọi hàm này để thay đổi trang
$scope.setCurrentPageBill = function(page) {
    console.log(page)
    console.log($scope.totalPages)
    if (page >= 0 && page < $scope.totalPages) {

        $scope.loadBillsForCustomer(page);
    }
};

$scope.goToPage = function() {

    var page = $scope.desiredPage3 - 1; 
    if (page >= 0 && page < $scope.totalPages) {
        $scope.loadBillsForCustomer(page);
    } else {
      
        $scope.desiredPage3 = $scope.currentPage + 1;
    }


};




        //LẤY THÔNG TIN BILL ***************
        $scope.getBillById = function (id) {
            $http.get('http://localhost:8080/api/home/bill/' + id).then(function (res) {
                console.log(res.data)
                $scope.billResponse = res.data
                $scope.status = res.data.status
            })
        }
        if ($scope.idBill != undefined) {
            $scope.getBillById($scope.idBill)

        }


        $scope.getBillDetailByIdBill = function (id) {
            $http.get('http://localhost:8080/api/home/bill/billDetail/' + id).then(function (res) {
                console.log(res.data)
                $scope.billDetail = res.data
            })
        }

        if ($scope.idBill != undefined) {
        $scope.getBillDetailByIdBill($scope.idBill)
            
        }


 //XỬ LÝ STATUS BILL, HISTORY BILL, THÔNG TIN GIAO HÀNG, LOGIC CỦA HIỂN THỊ TRẠNG THÁI ĐƠN HÀNG, paymentStatus
    // #region bill status & bill history

    $scope.otherReasonText = null

    $scope.showModalStatus = function (status) {
        $('#changeStatusModal').modal('show');
        $scope.currentStatus = status;
    };

    $scope.getBillHistoryByBillId = function () {
        $http.get(apiBillHistory + "/bill/" + $scope.idBill).then(function (res) {
            $scope.listBillHistory = res.data;
            console.log($scope.listBillHistory)
            $scope.updateStatusSteps();
            $timeout(function () {
                $scope.scrollToEnd()
                $scope.checkScrollbarVisibility();
            }, 0);
        }); 
    };
if ($scope.idBill != undefined) {
    $scope.getBillHistoryByBillId()
    
}


    $scope.showModalStatus = function (nextStatus) {
        $scope.resetCheckBoxes();

        $('#changeStatusModal').modal('show');

        $scope.currentStatus = $scope.status;
        $scope.nextStatus = nextStatus;
        $scope.selectedReasons = [];

        if ($scope.transitionReasons[$scope.currentStatus] && $scope.transitionReasons[$scope.currentStatus][nextStatus]) {
            const reasonKeys = $scope.transitionReasons[$scope.currentStatus][nextStatus];
            $scope.reasonSuggestions = reasonKeys.map(key => ({
                value: key,
                text: $scope.reasonsList[key].text,
                checked: false
            }));
        } else {
            $scope.reasonSuggestions = [];
        }
    };
    $scope.description= '';
    $scope.confirmChangeStatus = function () {
        if (!$scope.isReasonSelected() && $scope.reasonSuggestions.length > 0) {
            $scope.showError("Vui lòng chọn một lý do hoặc nhập lý do khác.");
            return;
        }
        let description = $scope.description || "";
        let reasonUpdate = {
            value: 0
        };
        // Add selected reasons to the description
        $scope.reasonSuggestions.forEach(function (reason) {
            if (reason.checked) {
                description += reason.text + ", ";
                reasonUpdate = reason;
            }
        });

        // Add "Other" reason
        if ($scope.otherReasonChecked && $scope.otherReasonText !== null) {
            description += "Khác: " + $scope.otherReasonText;
        }
        // Remove trailing comma and space
        description = description.trim().replace(/,\s*$/, "");

        $scope.description = description.trim();



        let bill = angular.copy($scope.billResponse);
        bill.reason = reasonUpdate.value
        bill.status = $scope.nextStatus;


        let billHistory = {
            billId: $scope.idBill,
            reason: reasonUpdate.value,
            status: $scope.nextStatus,
            description: $scope.description,
        };

        let data = { bill: bill, billHistory: billHistory };

        console.log(data);
        $http.put('http://localhost:8080/api/home/bill/billStatusUpdate/' + $scope.idBill, data).then(function (response) {

            $('#changeStatusModal').modal('hide');

            $scope.getBillById($scope.idBill);
            $scope.getBillHistoryByBillId();
        }).catch(function (error) {
            $('#changeStatusModal').modal('hide');

            if (response.data == null) {
                $scope.showError("Kiểm tra lại số lượng sản phẩm trong đơn hàng");
            }
            console.log("lỗi update status", error)
        });

        // Xóa nội dung ghi chú sau khi xác nhận
        $scope.billHistoryUpdate.description = null;
        $scope.otherReasonText = null;
    };

    $scope.isReasonSelected = function () {
        return $scope.reasonSuggestions.some(reason => reason.checked) || $scope.otherReasonChecked;
    };

    $scope.toggleReason = function (selectedReason) {
        // Uncheck all other reasons
        $scope.reasonSuggestions.forEach(function (reason) {
            if (reason !== selectedReason) {
                reason.checked = false;
            }
        });

        // Toggle the selected reason
        selectedReason.checked = !selectedReason.checked;

        // if (selectedReason.checked) {
        //     $scope.otherReasonChecked = false;
        //     $scope.otherReasonText = ""; // Clear other reason text
        // }
    };

    //Chuyển sang trả hàng
    $scope.goToReturnOrder = function (){
        $location.path('/admin/return-order/' + $scope.idBill);
    }

    $scope.reasonsList = {
        // Hủy bên khách hàng
        76: { text: "Không liên hệ được nhân viên giao hàng", value: 76, status: 0 },

        70: { text: "Thay đổi ý định mua hàng", value: 70, status: 0, shortenText: "" },
        71: { text: "Tìm thấy giá tốt hơn", value: 71, status: 0, shortenText: "" },
        72: { text: "Thông tin sản phẩm không chính xác", value: 72, status: 0, shortenText: "" },
        73: { text: "Phí vận chuyển cao", value: 73, status: 0, shortenText: "" },
        74: { text: "Sản phẩm hết hàng", value: 74, status: 0, shortenText: "" },
        75: { text: "Sai địa chỉ giao hàng", value: 75, status: 0, shortenText: "" },





    };

    $scope.transitionReasons = {
        1: { // Chờ xác nhận
            5: [70,71,72,73,74,75,76]
        },
        2: { // Chờ vận chuyển
            50: [70,71,72,73,74,75,76]
        },
    };

    $scope.updateStatusSteps = function () {
        $scope.possibleSteps = {
            1: { title: "Chờ xác nhận", icon: "schedule", status: 1 },
            2: { title: "Chờ giao hàng", icon: "local_shipping", status: 2 },
            3: { title: "Đang giao hàng", icon: "directions_car", status: 3 },
            4: { title: "Đã giao hàng", icon: "check_circle", status: 4 },

            5: { title: "Khách hủy", icon: "person_cancel", status: 5 },
            6: { title: "Đã hủy", icon: "block", status: 6 },

            7: { title: "Thất bại", icon: "cancel", status: 7 },
            8: { title: "Thất bại", icon: "cancel", status: 8 },    //giao lại
            81: { title: "Thất bại", icon: "cancel", status: 81 }, //mất hàng

            9: { title: "Chờ giao lại", icon: "autorenew", status: 9 },
            10: { title: "Đang giao lại", icon: "directions_car", status: 10 },

            11: { title: "Đang hoàn hàng", icon: "warehouse", status: 11 },
            12: { title: "Đã hoàn hàng", icon: "warehouse", status: 12 },

            20: { title: "Tạo đơn hàng", icon: "warehouse", status: 20 },
            21: { title: "Hoàn thành", icon: "warehouse", status: 21 },

            30: { title: "Trả hàng", icon: "warehouse", status: 30 },   //trả tại quầy
            31: { title: "Trả hàng", icon: "warehouse", status: 31 },   //trả ship  
            32: { title: "Đã trả hàng", icon: "warehouse", status: 32 },
            
             50: { title: "Khách yêu cầu hủy", icon: "warehouse", status: 50 }// khách yêu cầu hủy
        };

        $scope.possibleSteps = {
            20: { title: "Tạo đơn hàng", icon: "post_add", status: 20 },
            1: { title: "Chờ xác nhận", icon: "hourglass_empty", status: 1 },
            2: { title: "Chờ giao hàng", icon: "inventory", status: 2 },
            3: { title: "Đang giao hàng", icon: "local_shipping", status: 3 },
            4: { title: "Đã giao hàng", icon: "check_circle", status: 4 },

            5: { title: "Khách hủy", icon: "cancel", status: 5 },
            6: { title: "Đã hủy", icon: "not_interested", status: 6 },

            7: { title: "Thất bại", icon: "error", status: 7 },
            8: { title: "Thất bại", icon: "error", status: 8 }, //Lại - thiếu
            81: { title: "Thất bại", icon: "error", status: 81 }, //Mất hàng

            9: { title: "Chờ giao lại", icon: "replay", status: 9 },
            10: { title: "Đang giao lại", icon: "local_shipping", status: 10 },

            11: { title: "Đang hoàn hàng", icon: "assignment_return", status: 11 },
            12: { title: "Đã hoàn hàng", icon: "assignment_turned_in", status: 12 },
            13: { title: "Hoàn hàng thất bại", icon: "assignment_return", status: 13 },

            20: { title: "Thành công", icon: "task_alt", status: 20 },
            21: { title: "Hoàn thành", icon: "task_alt", status: 21 },

            30: { title: "Trả hàng", icon: "store", status: 30 },  //tại quầy
            31: { title: "Trả hàng", icon: "local_shipping", status: 31 },  //tận nơi
            32: { title: "Đã trả hàng", icon: "inventory_2", status: 32 },
            33: { title: "Trả hàng thất bại", icon: "assignment_late", status: 33 },

            50: { title: "Khách yêu cầu hủy", icon: "warehouse", status: 50 }// khách yêu cầu hủy

        };


        $scope.deliveryFlow = {
            1: [5], // Chờ xác nhận -> Chờ vận chuyển hoặc khách hủy hoặc đã hủy
            2: [50], // Chờ vận chuyển -> yêu cầu hủy
          
        };
        $scope.steps = [];

        $scope.listBillHistory.forEach(function (history) {
            let step = angular.copy($scope.possibleSteps[history.status]);
                console.log(history.status)
            if (step && history.type == 1) {
                step.time = history.createdAt;
                step.reason = history.reason;
                $scope.steps.push(step);
            }
        });


        // $scope.status = Math.max.apply(Math, $scope.listBillHistory.map(function (o) { return o.status; }));
        if ($scope.listBillHistory.length > 0) {
            $scope.status = $scope.listBillHistory[$scope.listBillHistory.length - 1].status;
        } else {
            $scope.status = null; // Hoặc một giá trị mặc định nếu không có lịch sử trạng thái
        }
    };

    $scope.getStatusTitle = function (status) {
        return $scope.possibleSteps[status] ? $scope.possibleSteps[status].title : 'Unknown';
    };

    //reset check box
    $scope.resetCheckBoxes = function () {
        $scope.selectedReasons = [];
        $scope.otherReasonChecked = false;
        $scope.otherReasonText = null;

        if ($scope.reasonSuggestions) {
            $scope.reasonSuggestions.forEach(function (reason) {
                reason.checked = false;
            });
        }
    };

    // #region Srcoll bill status
    $scope.scrollToEnd = function () {
        var container = document.querySelector('.progress-container');
        container.scrollLeft = container.scrollWidth;
    };

    $scope.scrollLeft = function () {
        var container = document.querySelector('.progress-container');
        container.scrollBy({
            top: 0,
            left: -400,
            behavior: 'smooth'
        });
    };

    $scope.scrollRight = function () {
        var container = document.querySelector('.progress-container');
        container.scrollBy({
            top: 0,
            left: 400,
            behavior: 'smooth'
        });
    };

    // Check if scrollbar is visible
    $scope.checkScrollbarVisibility = function () {
        var container = document.querySelector('.progress-container');
        $scope.scrollbarVisible = container.scrollWidth > container.clientWidth;
    };
    // #endregion


    
    $scope.listPaymentStatus = []

    //Hiển thị paymentStatus
    $scope.getAllPaymentStatus = function (billId) {
        $http.get(apiBill + "/" + billId + "/paymentStatus").then(function (res) {
            $scope.listPaymentStatus = res.data
            console.log(res.data);
        })
    }
    // $scope.getAllPaymentStatus($scope.idBill)

    // #endregion
    // formatCurrency
    $scope.formatCurrency = function(value) {
        if (!value) return '';
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    };















    function isValidPhoneNumber(phoneNumber) {
        // Regular expression cho số điện thoại di động và cố định Việt Nam
        const regexMobile = /(09|03|07|08|05)+([0-9]{8})\b/;
        const regexLandline = /(02|024|028)+([0-9]{7})\b/;
      
        return regexMobile.test(phoneNumber) || regexLandline.test(phoneNumber);
      }




    // check order by phoneNumber
// $scope.phoneNumberCheckOrder = ''; 
// $scope.isPhoneNumberCheckOrder = false;

// $scope.checkOrder = function() {
//   $scope.isPhoneNumberCheckOrder = !isValidPhoneNumber($scope.phoneNumberCheckOrder);
//   // Thêm kiểm tra cho các trường khác nếu cần
//   console.log('Thông tin đơn hàng:');

//   if ( $scope.isPhoneNumberCheckOrder) {
//     return;
//   }
//   console.log('Thông tin đơn hàng:');

//       $http.get('http://localhost:8080/api/ol/checkOrder/' + $scope.phoneNumberCheckOrder)
//           .then(function(response) {
//               // Xử lý dữ liệu khi API trả về thành công
//               $scope.billInfo = response.data;
//           })
//           .catch(function(error) {
//               // Xử lý lỗi khi gọi API không thành công
//               console.error('Lỗi khi gọi API:', error);
//               $scope.billInfo = null;
//           });
// };






// Rating -------------------------
$scope.getNumber = function(num) {

    return new Array((num));
  };
  
  
  $scope.deleteDataRate = function(rate) {
    $http.delete('http://localhost:8080/api/home/deleteRate/' + rate)
        .then(function(response) {
          $scope.listRatesFuc();
          $scope.showSuccessNotification("Xóa đánh giá thành công");
  
        }, function errorCallback(response) {
        $scope.showErrorNotification("Xóa đánh giá thất bại");
        });
  };
  
  $scope.ratings = []; // To store the retrieved ratings
  
  $scope.selectedBillDetail = null;
  
  $scope.openReview = function(detail) {
    $scope.selectedBillDetail = detail;
    $('#reviewModal').modal('show');
  };
  
  $scope.closeReview = function() {
    $scope.selectedBillDetail = null;
    $('#reviewModal').modal('hide');
  };
  
  
  
  
  $scope.addRating = function() {
    if ($scope.rating.stars === 0) {
      $scope.showErrorNotification("Vui lòng chọn sao!"); 
      return;
    }
  
    var ratingData = {
      rate: $scope.rating.stars,
      content: $scope.rating.content,
    //   idCustomer: 2,
      idBillDetail: $scope.selectedBillDetail.id,
      rated: true
    };
  
    $http.post('http://localhost:8080/api/home/addRate', ratingData)
      .then(function(response) {
        if (response.data === true) {
          $scope.showSuccessNotification("Cảm ơn bạn đã đánh giá"); 
          $scope.showBillDetail();
          $scope.closeReview();
        } else {
          $scope.showErrorNotification("Bạn đã đánh giá cho sản phẩm này trước đó"); 
        }
      })
      .catch(function(error) {
        $scope.showErrorNotification("Đánh giá thất bại vui lòng thử lại"); 
      });
  };
  

$scope.rating = {
    stars: 0,
    content: ''
  };
  
  $scope.toggleStars = function(index) {
    $scope.rating.stars = index + 1; // Lấy giá trị từ index và thêm 1
    const stars = document.querySelectorAll('.fa-star');
    for (let i = 0; i <= index; i++) {
        stars[i].classList.add('checked');
    }
    for (let i = index + 1; i < stars.length; i++) {
        stars[i].classList.remove('checked');
    }
  };
});
