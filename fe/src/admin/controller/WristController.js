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
        $http.get(`${config.host}/wrist`, 
                {params: {page: $scope.page, size: $scope.size, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.wrists = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
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
        $http.put(`${config.host}/wrist/status/${$scope.wrist.id}`).then(response => {
            $scope.getAllWrists();
            $scope.wrist = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createWrist = () => {
        if ($scope.wristForm.$valid) {
           $http.post(`${config.host}/wrist`, $scope.wrist).then((response) => {
                $scope.getAllWrists();
                $scope.resetForm();
                $('#addWristModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateWrist = () => {
        if ($scope.wristFormUpdate.$valid) {
            $http.put(`${config.host}/wrist/${$scope.wristUpdate.id}`, $scope.wristUpdate).then(response => {
                $scope.getAllWrists();
                $scope.resetFormUpdate();
                $('#editWristModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/wrist/${$scope.wrist.id}`).then(response => {
            $scope.getAllWrists();
            $scope.wrist = {};
            $('#deleteWristModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }


})