app.controller("SizeController", function($scope, $http){
    $scope.page = 0;
    $scope.num = 5;

    $scope.filter = {
        name: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.size = {};
    $scope.sizeUpdate = {};
    $scope.sizes = [];
    
    $scope.getAllSizes = () => {
        if ($scope.num <= 0 || !Number.isInteger($scope.num)) {
            $scope.num = 5;
        }
        $http.get(`${config.host}/size`, 
                {params: {page: $scope.page, size: $scope.num, name: $scope.filter.name,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.sizes = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                console.log("Error", error)
            })
    }

    $scope.searchSizes = () => {
        $scope.page = 0;
        $scope.getAllSizes();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllSizes();
        }
    }

    $scope.getAllSizes();

    $scope.resetForm = () => {
        $scope.size = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.sizeUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editSize = (key) => {
        $http.get(`${config.host}/size/${key}`).then((response) => {
            $scope.sizeUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.size = element;
    }

    $scope.handleStatus = (element) => {
        $scope.size = element;
    }

    $scope.updateStatus = () => {
        $http.put(`${config.host}/size/status/${$scope.size.id}`).then(response => {
            $scope.getAllSizes();
            $scope.size = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createSize = () => {
        if ($scope.sizeForm.$valid) {
           $http.post(`${config.host}/size`, $scope.size).then((response) => {
                $scope.getAllSizes();
                $scope.resetForm();
                $('#addSizeModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                console.log(error);
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateSize = () => {
        if ($scope.sizeFormUpdate.$valid) {
            $http.put(`${config.host}/size/${$scope.sizeUpdate.id}`, $scope.sizeUpdate).then(response => {
                $scope.getAllSizes();
                $scope.resetFormUpdate();
                $('#editSizeModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/size/${$scope.size.id}`).then(response => {
            $scope.getAllSizes();
            $scope.size = {};
            $('#deleteSizeModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }


})