app.controller("WristController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;

    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.wrist = {};
    $scope.wristUpdate = {};
    $scope.wrists = [];
    
    $scope.getAllWrists = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $('#loading').css('display', 'flex');
        $http.get(`${config.host}/wrist`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection,
                status: $scope.filter.status
                }})
            .then((response) => {
                $('#loading').css('display', 'none');
                $scope.wrists = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                $('#loading').css('display', 'none');
                console.log("Error", error)
            })
    }

    $scope.searchWrists = () => {
        $scope.page = 0;
        $scope.getAllWrists();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllWrists();
        }
    }

    $scope.getAllWrists();

    $scope.resetForm = () => {
        $scope.wrist = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.wristUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editWrist = (key) => {
        $http.get(`${config.host}/wrist/${key}`).then((response) => {
            $scope.wristUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.wrist = element;
    }

    $scope.handleStatus = (element) => {
        $scope.wrist = element;
    }

    $scope.updateStatus = () => {
        $('#updateStatus').css('display', 'none');
        $('#loadingStatus').css('display', 'inline-block');
        $http.put(`${config.host}/wrist/status/${$scope.wrist.id}`).then(response => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            $('#updateStatusModel').modal('hide');
            $scope.getAllWrists();
            $scope.wrist = {};
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            $('#updateStatus').css('display', 'inline-block');
            $('#loadingStatus').css('display', 'none');
            console.log("Error", error);
        })
    }

    $scope.createWrist = () => {
        if ($scope.wristForm.$valid) {
            $('#addWrist').css('display', 'none');
            $('#loadingAdd').css('display', 'inline-block');
           $http.post(`${config.host}/wrist`, $scope.wrist).then((response) => {
                $('#addWrist').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                $('#addWristModel').modal('hide');
                $scope.getAllWrists();
                $scope.resetForm();
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                $('#addWrist').css('display', 'inline-block');
                $('#loadingAdd').css('display', 'none');
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateWrist = () => {
        if ($scope.wristFormUpdate.$valid) {
            $('#updateWrist').css('display', 'none');
            $('#loadingUpdate').css('display', 'inline-block');
            $http.put(`${config.host}/wrist/${$scope.wristUpdate.id}`, $scope.wristUpdate).then(response => {
                $('#updateWrist').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                $('#editWristModal').modal('hide');
                $scope.getAllWrists();
                $scope.resetFormUpdate();
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                $('#updateWrist').css('display', 'inline-block');
                $('#loadingUpdate').css('display', 'none');
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $('#deleteWrist').css('display', 'none');
        $('#loadingDelete').css('display', 'inline-block');
        $http.delete(`${config.host}/wrist/${$scope.wrist.id}`).then(response => {
            $('#deleteWrist').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            $('#deleteWristModel').modal('hide');
            $scope.getAllWrists();
            $scope.wrist = {};
            toastr["success"]("Ngừng hoạt động " + response.data.name + " thành công");
        }).catch(error => {
            $('#deleteWrist').css('display', 'inline-block');
            $('#loadingDelete').css('display', 'none');
            toastr["error"](error);
        })
    }


})