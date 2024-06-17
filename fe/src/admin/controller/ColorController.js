app.controller("ColorController", function($scope, $http){
    $scope.page = 0;
    $scope.size = 5;
    
    $scope.filter = {
        keyword: '',
        sortField: 'createdAt',
        sortDirection: 'ASC'
    };
    $scope.color = {};
    $scope.colorUpdate = {};
    $scope.colors = [];
    
    $scope.getAllColors = () => {
        if ($scope.size <= 0 || !Number.isInteger($scope.size)) {
            $scope.size = 5;
        }
        $http.get(`${config.host}/color`, 
                {params: {page: $scope.page, size: $scope.size, keyword: $scope.filter.keyword,
                sortField: $scope.filter.sortField,
                sortDirection: $scope.filter.sortDirection
                }})
            .then((response) => {
                $scope.colors = response.data;
                $scope.totalPages = response.data.totalPages;
                $scope.currentPage = response.data.pageable.pageNumber;
            }).catch(error => {
                console.log("Error", error)
            })
    }

    $scope.searchColors = () => {
        $scope.page = 0;
        $scope.getAllColors();
    }
    
    $scope.changePage = (page) => {
        if (page >= 0 && page < $scope.totalPages) {
            $scope.page = page;
            $scope.getAllColors();
        }
    }

    $scope.getAllColors();

    $scope.resetForm = () => {
        $scope.color = {};
        $scope.errors = {};
    }

    $scope.resetFormUpdate = () => {
        $scope.colorUpdate = {};
        $scope.errorsUpdate = {};
    }

    $scope.editColor = (key) => {
        $http.get(`${config.host}/color/${key}`).then((response) => {
            $scope.colorUpdate = response.data;
        }).catch(error => {
            toastr["error"](error);
        })
    }

    $scope.handleDelete = (element) => {
        $scope.color = element;
    }

    $scope.handleStatus = (element) => {
        $scope.color = element;
    }

    $scope.updateStatus = () => {
        $http.put(`${config.host}/color/status/${$scope.color.id}`).then(response => {
            $scope.getAllColors();
            $scope.color = {};
            $('#updateStatusModel').modal('hide');
            toastr["success"]("Cập nhật trạng thái " + response.data.name + " thành công");
        }).catch(error => {
            console.log("Error", error);
        })
    }

    $scope.createColor = () => {
        if ($scope.colorForm.$valid) {
           $http.post(`${config.host}/color`, $scope.color).then((response) => {
                $scope.getAllColors();
                $scope.resetForm();
                $('#addColorModel').modal('hide');
                toastr["success"]("Thêm mới " + response.data.name + " thành công");
           }).catch(error => {
                if (error.status === 400) $scope.errors = error.data
                else toastr["error"](error);
           })
        }
    }

    $scope.updateColor = () => {
        if ($scope.colorFormUpdate.$valid) {
            $http.put(`${config.host}/color/${$scope.colorUpdate.id}`, $scope.colorUpdate).then(response => {
                $scope.getAllColors();
                $scope.resetFormUpdate();
                $('#editColorModal').modal('hide');
                toastr["success"]("Cập nhật " + response.data.name + " thành công");
            }). catch(error => {
                if (error.status === 400) $scope.errorsUpdate = error.data;
                else toastr["error"](error);
            })
        }
    }

    $scope.delete = () => {
        $http.delete(`${config.host}/color/${$scope.color.id}`).then(response => {
            $scope.getAllColors();
            $scope.color = {};
            $('#deleteColorModel').modal('hide');
            toastr["success"]("Xóa " + response.data.name + " thành công");
        }).catch(error => {
            toastr["error"](error);
        })
    }


})